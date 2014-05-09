package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import edu.asu.ser.hanasu.SaveFile;
import edu.asu.ser.hanasu.SilentWindowListener;
import edu.asu.ser.hanasu.UserObject;
import edu.asu.ser.hanasu.screens.components.KanaStroke;
import edu.asu.ser.hanasu.screens.components.Sidebar;
import edu.asu.ser.hanasu.screens.components.SidebarButton.SidebarButtonType;
import edu.asu.ser.hanasu.server.Client;

public class ScreenManager
{
	private final HashMap<SidebarButtonType, ActionListener> sidebarListeners;
	private final HashMap<ScreenType, Screen> screens;
	private ClientContainer client;
	private Client clientForServer;
	
	private MainScreen mainScreen;
	private ChannelScreen channelScreen;
	private ChatScreen chatScreen;
	
	private UserObject userObject;
	
	public static enum ScreenType
	{
		MAIN,
		CHANNEL,
		CHAT,
		
		// TODO: remove these; "CHAT" should cover these as well
		CHATFAV1,
		CHATFAV2,
		CHATFAV3;
	}
	
	private class TransitionListener implements ActionListener
	{
		private ScreenType destination;
		
		public TransitionListener(ScreenType destination)
		{
			this.destination = destination;
		}
		
		@Override
		public void actionPerformed(ActionEvent event)
		{
			int strokeNumber;
			
			switch (destination)
			{
				case CHATFAV1:
					strokeNumber = 0;
					break;
				case CHATFAV2:
					strokeNumber = 1;
					break;
				case CHATFAV3:
					strokeNumber = 2;
					break;
				default:
					transition(ScreenType.CHANNEL);
					return;
			}
			
			KanaStroke stroke = getChannelScreen().getKanaStrokes().get(
					strokeNumber);
			
			if (stroke.getAssociatedChannel().isSet())
			{
				if (stroke.getAssociatedChannel().connect(
						getChannelScreen().getScreenManager(),
						stroke.getServerIP(), stroke.getServerPort()))
				{
					transition(ScreenType.CHAT);
				}
				else
				{
					JOptionPane
							.showMessageDialog(getMainScreen().getParent(),
									"Connection Unsuccessful. Check Credentials, Or Server?");
				}
			}
			else
			{
				transition(ScreenType.CHANNEL);
			}
		}
	}
	
	private static abstract class TransitionAction implements ActionListener
	{
		protected Screen exitScreen;
		protected Screen enterScreen;
		
		private TransitionAction(Screen exitScreen, Screen enterScreen)
		{
			this.exitScreen = exitScreen;
			this.enterScreen = enterScreen;
		}
	}
	
	private static class OnStart extends TransitionAction
	{
		private OnStart(Screen exitScreen, Screen enterScreen)
		{
			super(exitScreen, enterScreen);
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			exitScreen.prepareToExit();
			enterScreen.prepareToEnter();
		}
	}
	
	private static class OnFinish extends TransitionAction
	{
		private OnFinish(Screen exitScreen, Screen enterScreen)
		{
			super(exitScreen, enterScreen);
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			exitScreen.onExit();
			enterScreen.onEnter();
		}
	}
	
	private ScreenManager()
	{
		screens = new HashMap<>();
		sidebarListeners = generateSidebarListeners();
		
		try
		{
			Sidebar channelBar = new Sidebar(sidebarListeners);
			channelScreen = new ChannelScreen(channelBar,
					returnScreenBackground(ScreenType.CHANNEL), this);
			
			createUserObject();
			
			Sidebar mainBar = new Sidebar(sidebarListeners);
			mainScreen = new MainScreen(mainBar,
					returnScreenBackground(ScreenType.MAIN), this);
			
			Sidebar chatBar = new Sidebar(sidebarListeners);
			chatScreen = new ChatScreen(chatBar,
					returnScreenBackground(ScreenType.CHAT), this);
			
			screens.put(ScreenType.MAIN, mainScreen);
			screens.put(ScreenType.CHANNEL, channelScreen);
			screens.put(ScreenType.CHAT, chatScreen);
			
			// TODO: remove these, after the CHATFAV group has been removed
			screens.put(ScreenType.CHATFAV1, chatScreen);
			screens.put(ScreenType.CHATFAV2, chatScreen);
			screens.put(ScreenType.CHATFAV3, chatScreen);
			
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		
		mainScreen.setPreferredSize(new Dimension(450, 300));
		channelScreen.setPreferredSize(new Dimension(450, 300));
		chatScreen.setPreferredSize(new Dimension(450, 300));
		
		client = new ClientContainer(mainScreen);
		client.addWindowListener(new ExitListener());
	}
	
	public static ScreenManager createManager()
	{
		ScreenManager newManager = new ScreenManager();
		return newManager;
	}
	
	private HashMap<SidebarButtonType, ActionListener> generateSidebarListeners()
	{
		HashMap<SidebarButtonType, ActionListener> map = new HashMap<>();
		
		ActionListener mainListener = new TransitionListener(ScreenType.MAIN);
		map.put(SidebarButtonType.MAIN_SCREEN_BUTTON, mainListener);
		
		ActionListener channelListener = new TransitionListener(
				ScreenType.CHANNEL);
		map.put(SidebarButtonType.CHANNEL_SCREEN_BUTTON, channelListener);
		
		ActionListener chatListener1 = new TransitionListener(
				ScreenType.CHATFAV1);
		map.put(SidebarButtonType.CHANNEL_BUTTON_1, chatListener1);
		
		ActionListener chatListener2 = new TransitionListener(
				ScreenType.CHATFAV2);
		map.put(SidebarButtonType.CHANNEL_BUTTON_2, chatListener2);
		
		ActionListener chatListener3 = new TransitionListener(
				ScreenType.CHATFAV3);
		map.put(SidebarButtonType.CHANNEL_BUTTON_3, chatListener3);
		
		return map;
	}
	
	public void transition(ScreenType destination)
	{
		Screen exitScreen = (Screen) client.getCurrentPanel();
		Screen destinationScreen = screens.get(destination);
		
		client.transition(destinationScreen, new OnStart(exitScreen,
				destinationScreen), new OnFinish(exitScreen, destinationScreen));
	}
	
	public MainScreen getMainScreen()
	{
		return mainScreen;
	}
	
	public void setMainScreen(MainScreen mainScreen)
	{
		this.mainScreen = mainScreen;
	}
	
	public ChannelScreen getChannelScreen()
	{
		return channelScreen;
	}
	
	public void setChannelScreen(ChannelScreen channelScreen)
	{
		this.channelScreen = channelScreen;
	}
	
	public ChatScreen getChatScreen()
	{
		return chatScreen;
	}
	
	public void setChatScreen(ChatScreen chatScreen)
	{
		this.chatScreen = chatScreen;
	}
	
	private Image returnScreenBackground(ScreenType screenType)
			throws IOException
	{
		String chatPath = "src/Images/ChatScreenBackground.png";
		String channelPath = "src/Images/NewChannelScreen.png";
		String mainPath = "src/Images/MainScreenBackground.png";
		
		switch (screenType)
		{
			case MAIN:
				return ImageIO.read(new File(mainPath));
			case CHANNEL:
				return ImageIO.read(new File(channelPath));
			case CHAT:
				return ImageIO.read(new File(chatPath));
			case CHATFAV1:
				return ImageIO.read(new File(chatPath));
			case CHATFAV2:
				return ImageIO.read(new File(chatPath));
			case CHATFAV3:
				return ImageIO.read(new File(chatPath));
			default:
				throw new NotImplementedException();
		}
	}
	
	public ScreenType getChatScreenType()
	{
		return ScreenType.CHAT;
	}
	
	public Client getClientForServer()
	{
		return clientForServer;
	}
	
	public void setClientForServer(Client clientForServer)
	{
		this.clientForServer = clientForServer;
	}
	
	public boolean isTransitioning()
	{
		return client.isTransitioning();
	}
	
	public Screen getCurrentScreen()
	{
		return (Screen) client.getCurrentPanel();
	}
	
	public UserObject getUserObject()
	{
		return userObject;
	}
	
	private class ExitListener extends SilentWindowListener
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			saveUserObject();
		}
	}
	
	// ========================================================================
	// TODO: Refactor: Move the following to appropriate locations
	// ========================================================================
	
	public UserObject populateUserObject(UserObject userObjectParam,
			SaveFile loadedSaveFile)
	{
		userObjectParam.setNickName(loadedSaveFile.getUserName());
		
		ArrayList<KanaStroke> tempKana = getChannelScreen().getKanaStrokes();
		
		for (int index = 0; index < tempKana.size(); index++)
		{
			tempKana.get(index).setServerIP(
					loadedSaveFile.getServerIPs().get(index));
			tempKana.get(index).setServerPort(
					loadedSaveFile.getServerPorts().get(index));
			tempKana.get(index).setAssociatedChannelSave(
					loadedSaveFile.getChannelNames().get(index).trim(),
					loadedSaveFile.getChannelPasswords().get(index).trim());
		}
		
		userObjectParam.setStrokesArray(tempKana);
		
		return userObjectParam;
	}
	
	public SaveFile populateSaveFile(UserObject userObjectParam,
			SaveFile loadedSaveFile)
	{
		loadedSaveFile.setUserName(userObjectParam.getNickName());
		
		ArrayList<String> tempChannelNames = new ArrayList<>(8);
		ArrayList<String> tempChannelPasswords = new ArrayList<>(8);
		ArrayList<String> tempServerIPs = new ArrayList<>(8);
		ArrayList<Integer> tempServerPorts = new ArrayList<>(8);
		ArrayList<KanaStroke> tempKana = userObjectParam.getStrokesArray();
		
		for (int index = 0; index < tempKana.size(); index++)
		{
			tempServerIPs.add(index, tempKana.get(index).getServerIP().trim());
			tempServerPorts.add(index, tempKana.get(index).getServerPort());
			
			if (tempKana.get(index).getAssociatedChannel().getName().equals(""))
			{
				tempChannelNames.add(index, "");
			}
			else
			{
				tempChannelNames.add(index, tempKana.get(index)
						.getAssociatedChannel().getName().trim());
				
			}
			
			if (tempKana.get(index).getAssociatedChannel().getPassword()
					.equals(""))
			{
				tempChannelPasswords.add(index, "");
			}
			else
			{
				tempChannelPasswords.add(index, tempKana.get(index)
						.getAssociatedChannel().getPassword().trim());
			}
			
		}
		
		loadedSaveFile.setServerIPs(tempServerIPs);
		loadedSaveFile.setChannelNames(tempChannelNames);
		loadedSaveFile.setChannelPasswords(tempChannelPasswords);
		loadedSaveFile.setServerPorts(tempServerPorts);
		return loadedSaveFile;
	}
	
	private void createUserObject()
	{
		if (new File("UserObject.ser").exists())
		{
			try (ObjectInputStream objectInputStream = new ObjectInputStream(
					new FileInputStream("UserObject.ser")))
			{
				SaveFile userSaveFile = (SaveFile) objectInputStream
						.readObject();
				
				// TODO: replace with more elegant solution
				userObject = populateUserObject(new UserObject(
						getChannelScreen().getKanaStrokes()), userSaveFile);
				
			}
			catch (IOException exception)
			{
				// TODO: replace with more elegant solution
				userObject = new UserObject(getChannelScreen().getKanaStrokes());
				exception.printStackTrace();
			}
			catch (ClassNotFoundException exception)
			{
				// TODO: replace with more elegant solution
				userObject = new UserObject(getChannelScreen().getKanaStrokes());
				// exception.printStackTrace();
			}
		}
		else
		{
			userObject = new UserObject(getChannelScreen().getKanaStrokes());
		}
	}
	
	public void saveUserObject()
	{
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				new FileOutputStream("UserObject.ser")))
		{
			SaveFile saveFile = populateSaveFile(userObject, new SaveFile());
			
			objectOutputStream.writeObject(saveFile);
			
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	public boolean createAndConnectServer(String serverName, int port,
			String key, String username)
	{
		System.out.println("call to create client!");
		
		try
		{
			byte[] byteKey;
			byteKey = key.getBytes("UTF-8");
			setClientForServer(new Client(serverName, port, username,
					getChatScreen(), byteKey));
			if (clientForServer.start())
				return true;
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO: handle exception
			e.printStackTrace();
			throw new Error();
		}
		
		return false;
	}
}
