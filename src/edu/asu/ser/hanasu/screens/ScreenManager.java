package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.Image;

public class ScreenManager
{
	private static ScreenManager activeManager;
	private static Image barImage;
	
	private MainScreen mainScreen;
	private ChannelScreen channelScreen;
	private ChatScreen chatScreen;
	
	private ScreenManager()
	{
		mainScreen = new MainScreen();
		channelScreen = new ChannelScreen();
		chatScreen = new ChatScreen();
		
		mainScreen.setPreferredSize(new Dimension(450, 300));
		channelScreen.setPreferredSize(new Dimension(450, 300));
		chatScreen.setPreferredSize(new Dimension(450, 300));
	}
	
	public static ScreenManager createManager()
	{
		ScreenManager newManager = new ScreenManager();
		
		if (activeManager == null)
			activeManager = newManager;
		
		return newManager;
	}
	
	public static Image getBarImage()
	{
		return barImage;
	}
	
	public static void setBarImage(Image barImage)
	{
		ScreenManager.barImage = barImage;
	}
	
	public static ScreenManager getActiveManager()
	{
		return activeManager;
	}
	
	public void activate()
	{
		ScreenManager.activeManager = this;
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
