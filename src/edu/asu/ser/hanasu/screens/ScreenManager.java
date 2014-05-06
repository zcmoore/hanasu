package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import edu.asu.ser.hanasu.SaveFile;
import edu.asu.ser.hanasu.screens.SidebarButton.SidebarButtonType;
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
			if (destination.equals(ScreenType.CHATFAV1))
			{
				if (getChannelScreen().getKanaStrokes().get(0)
						.getAssociatedChannel().isSet())
				{
					if(getChannelScreen()
							.getKanaStrokes()
							.get(0)
							.getAssociatedChannel()
							.connect(
									getChannelScreen().getScreenManager(),
									getChannelScreen().getKanaStrokes().get(0)
											.getServerIP()))
					{
						transition(ScreenType.CHAT);
					}
					else
					{
						JOptionPane.showMessageDialog(getMainScreen().getParent(), "Connection Unsuccessful. Check Credentials, Or Server?");
					}
					
				}
				else
				{
					transition(ScreenType.CHANNEL);
				}
			}
			else if (destination.equals(ScreenType.CHATFAV2))
			{
				if (getChannelScreen().getKanaStrokes().get(1)
						.getAssociatedChannel().isSet())
				{
					if(getChannelScreen()
							.getKanaStrokes()
							.get(1)
							.getAssociatedChannel()
							.connect(
									getChannelScreen().getScreenManager(),
									getChannelScreen().getKanaStrokes().get(1)
											.getServerIP()))
					{
						transition(ScreenType.CHAT);
					}
					else
					{
						JOptionPane.showMessageDialog(getMainScreen().getParent(), "Connection Unsuccessful. Check Credentials, Or Server?");
					}
				}
				else
				{
					transition(ScreenType.CHANNEL);
				}
			}
			else if (destination.equals(ScreenType.CHATFAV3))
			{
				if (getChannelScreen().getKanaStrokes().get(2)
						.getAssociatedChannel().isSet())
				{
					if (getChannelScreen()
							.getKanaStrokes()
							.get(2)
							.getAssociatedChannel()
							.connect(
									getChannelScreen().getScreenManager(),
									getChannelScreen().getKanaStrokes().get(2)
											.getServerIP()))
					{
						transition(ScreenType.CHAT);
					}
					else
					{
						JOptionPane.showMessageDialog(getMainScreen().getParent(), "Connection Unsuccessful. Check Credentials, Or Server?");
					}
				}
				else
				{
					transition(ScreenType.CHANNEL);
				}
			}
			else
			{
				transition(destination);
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
			// TODO: re-enable
			
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
		
		createCloseListener();
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
		switch (screenType)
		{
			case MAIN:
				String mainPath = "src/Images/MainScreenBackground.png";
				return ImageIO.read(new File(mainPath));
			case CHANNEL:
				String channelPath = "src/Images/NewChannelScreen.png";
				return ImageIO.read(new File(channelPath));
			case CHAT:
				String chatPath = "src/Images/ChatScreenBackground.png";
				return ImageIO.read(new File(chatPath));
		}
		return null;
	}
	
	private void createUserObject()
	{
		if (new File("src/UserObject.ser").exists())
		{
			try (ObjectInputStream objectInputStream = new ObjectInputStream(
					new FileInputStream("src/UserObject.ser")))
			{
				SaveFile userSaveFile = (SaveFile) objectInputStream
						.readObject();
				
				userObject = populateUserObject(new UserObject(), userSaveFile);
				
			}
			catch (IOException exception)
			{
				System.out.println("IOException: ");
				userObject = new UserObject();
				exception.printStackTrace();
			}
			catch (ClassNotFoundException exception)
			{
				System.out.println("Class not found exception");
				userObject = new UserObject();
				// exception.printStackTrace();
			}
		}
		else
		{
			userObject = new UserObject();
		}
	}
	
	public void saveUserObject() throws IOException
	{
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				new FileOutputStream("src/UserObject.ser")))
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
			String theKey, String username)
	{
		System.out.println("call to create client!");
		
		try
		{
			byte[] key;
			key = theKey.getBytes("UTF-8");
			setClientForServer(new Client(serverName, port, username,
					getChatScreen(), key));
			if (clientForServer.start())
				return true;
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
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
	
	public UserObject getUserObject()
	{
		return userObject;
	}
	
	public boolean isTransitioning()
	{
		return client.isTransitioning();
	}
	
	public Screen getCurrentScreen()
	{
		return (Screen) client.getCurrentPanel();
	}
	
	public UserObject populateUserObject(UserObject userObjectParam,
			SaveFile loadedSaveFile)
	{
		userObjectParam.setNickName(loadedSaveFile.getUserName());
		
		ArrayList<KanaStroke> tempKana = getChannelScreen().getKanaStrokes();
		
		for (int index = 0; index < tempKana.size(); index++)
		{
			tempKana.get(index).setServerIP(
					loadedSaveFile.getServerIPs().get(index));
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
		ArrayList<KanaStroke> tempKana = userObjectParam.getStrokesArray();
		
		for (int index = 0; index < tempKana.size(); index++)
		{
			tempServerIPs.add(index, tempKana.get(index).getServerIP().trim());
			
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
		return loadedSaveFile;
	}
	
	public class UserObject
	{
		private String userNickName;
		private String channelName;
		private String channelPassword;
		private ArrayList<KanaStroke> strokesArray;
		private boolean isConnected;
		
		// TODO add the 3 favorite channels
		
		public UserObject()
		{
			userNickName = "nickname";
			channelName = "Channel X";
			channelPassword = "ABCDEFGHIJKLMNOP";
			strokesArray = getChannelScreen().getKanaStrokes();
			isConnected = false;
			System.out.println("New userobject created");
		}
		
		public String getNickName()
		{
			return userNickName;
		}
		
		public String getHostName()
		{
			return channelName;
		}
		
		public String getChannelPassword()
		{
			return channelPassword;
		}
		
		public ArrayList<KanaStroke> getStrokesArray()
		{
			return strokesArray;
		}
		
		public boolean isConnected()
		{
			return isConnected;
		}
		
		public void setNickName(String nickName)
		{
			this.userNickName = nickName;
		}
		
		public void setHostName(String hostName)
		{
			this.channelName = hostName;
		}
		
		public void setChannelPassword(String channelPassword)
		{
			this.channelPassword = channelPassword;
		}
		
		public void setStrokesArray(ArrayList<KanaStroke> strokesArray)
		{
			this.strokesArray = strokesArray;
		}
		
		public void setConnected(boolean isConnected)
		{
			this.isConnected = isConnected;
		}
	}
	
	private void createCloseListener()
	{
		client.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				try
				{
					saveUserObject();
				}
				catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}
}
