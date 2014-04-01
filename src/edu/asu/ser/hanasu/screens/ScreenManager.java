package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import edu.asu.ser.hanasu.screens.SidebarButton.SidebarButtonType;

public class ScreenManager
{
	private static Image barImage;
	
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
	
	private ScreenManager()
	{
		screens = new HashMap<>();
		sidebarListeners = generateSidebarListeners();
		
		Sidebar mainBar = new Sidebar(sidebarListeners);
		mainScreen = new MainScreen(mainBar);
		screens.put(ScreenType.MAIN, mainScreen);
		
		Sidebar channelBar = new Sidebar(sidebarListeners);
		channelScreen = new ChannelScreen(channelBar);
		screens.put(ScreenType.CHANNEL, channelScreen);
		
		Sidebar chatBar = new Sidebar(sidebarListeners);
		chatScreen = new ChatScreen(chatBar);
		screens.put(ScreenType.CHAT, chatScreen);
		
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
		
		// exitScreen.prepareToExit();
		// destinationScreen.prepareToEnter();
		
		client.transition(destinationScreen);
		
		destinationScreen.onEnter();
		exitScreen.onExit();
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
	
}
