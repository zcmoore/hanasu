package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import edu.asu.ser.hanasu.screens.SidebarButton.SidebarButtonType;
import edu.asu.ser.hanasu.server.Client;

public class ScreenManager
{
	private final HashMap<SidebarButtonType, ActionListener> sidebarListeners;
	private final HashMap<ScreenType, Screen> screens;
	private ClientContainer client;
	
	private MainScreen mainScreen;
	private ChannelScreen channelScreen;
	private ChatScreen chatScreen;
	
	private UserObject userObject;
	
	private static enum ScreenType
	{
		MAIN,
		CHANNEL,
		CHAT;
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
			transition(destination);
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
			
			//@formatter:off
			/*exitScreen.prepareToExit();
			enterScreen.prepareToEnter();*/
			//@formatter:on
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
			Sidebar mainBar = new Sidebar(sidebarListeners);
			mainScreen = new MainScreen(mainBar,
					returnScreenBackground(ScreenType.MAIN), this);
			
			
			Sidebar chatBar = new Sidebar(sidebarListeners);
			chatScreen = new ChatScreen(chatBar,
					returnScreenBackground(ScreenType.CHAT), this);
			
			Sidebar channelBar = new Sidebar(sidebarListeners);
			channelScreen = new ChannelScreen(channelBar,
					returnScreenBackground(ScreenType.CHANNEL), this);
			
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
		
		createUserObject();
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
				String chatPath = "src/Images/MainScreenBackground.png";
				return ImageIO.read(new File(chatPath));
		}
		return null;
	}
	
	private void createUserObject()
	{
		if (new File("src/UserObject.ser").exists())
		{
			//TODO implement UserObject not set
			try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("src/UserObject.ser")))
			{
				//TODO implement fix for file but no object
				UserObject userObjectRead = (UserObject) objectInputStream.readObject();
				userObject = userObjectRead;
			} 
			catch (IOException exception)
			{
				System.out.println("IOException: ");
				userObject = new UserObject();
				//exception.printStackTrace();
			}
			catch (ClassNotFoundException exception)
			{
				System.out.println("Class not found exception");
				userObject = new UserObject();
				//exception.printStackTrace();
			}
		}
		else
		{
			userObject = new UserObject();
		}
	}
	
	public boolean createAndConnectServer(String serverName, int port, String passsword, String username)
	{
		System.out.println("call to create client!");
		
		//getUserObject().setClientForServer(new Client(serverName, port, username, null));
		//getUserObject().clientForServer.start();
		return false;
	}
	
	public UserObject getUserObject()
	{
		return userObject;
	}
	
	public class UserObject implements Serializable
	{
		private Client clientForServer;
		private String nickName;
		private String hostName;
		private String channelPassword;
		private ArrayList<KanaStroke> strokesArray;
		
		public UserObject()
		{
			nickName = "nickname";
			hostName = "Channel X";
			channelPassword = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			strokesArray = null;
			System.out.println("New userobject created");
		}
		
		public String getNickName()
		{
			return nickName;
		}
		
		public String getHostName()
		{
			return hostName;
		}

		public String getChannelPassword()
		{
			return channelPassword;
		}
		
		public Client getClientForServer()
		{
			return clientForServer;
		}

		public ArrayList<KanaStroke> getStrokesArray()
		{
			return strokesArray;
		}

		public void setNickName(String nickName)
		{
			this.nickName = nickName;
		}

		public void setHostName(String hostName)
		{
			this.hostName = hostName;
		}

		public void setChannelPassword(String channelPassword)
		{
			this.channelPassword = channelPassword;
		}

		public void setClientForServer(Client clientForServer)
		{
			this.clientForServer = clientForServer;
		}

		public void setStrokesArray(ArrayList<KanaStroke> strokesArray)
		{
			this.strokesArray = strokesArray;
		}
	}
}
