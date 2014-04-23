package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import edu.asu.ser.hanasu.screens.SidebarButton.SidebarButtonType;

public class ScreenManager
{
	private final HashMap<SidebarButtonType, ActionListener> sidebarListeners;
	private final HashMap<ScreenType, Screen> screens;
	private ClientContainer client;
	
	private MainScreen mainScreen;
	private ChannelScreen channelScreen;
	private ChatScreen chatScreen;
	
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
					returnScreenBackground(ScreenType.MAIN));
			screens.put(ScreenType.MAIN, mainScreen);
			
			Sidebar channelBar = new Sidebar(sidebarListeners);
			channelScreen = new ChannelScreen(channelBar,
					returnScreenBackground(ScreenType.CHANNEL));
			screens.put(ScreenType.CHANNEL, channelScreen);
			
			Sidebar chatBar = new Sidebar(sidebarListeners);
			chatScreen = new ChatScreen(chatBar,
					returnScreenBackground(ScreenType.CHAT));
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
				String channelPath = "src/Images/MainScreenBackground.png";
				return ImageIO.read(new File(channelPath));
			case CHAT:
				String chatPath = "src/Images/MainScreenBackground.png";
				return ImageIO.read(new File(chatPath));
		}
		return null;
	}
	
}
