package edu.asu.ser.hanasu.screens;

import java.awt.Image;

public class ScreenManager
{
	private static ScreenManager activeManager;
	private static Image barImage;
	
	private MainScreen mainScreen;
	private ChannelScreen channelScreen;
	
	// TODO: add chat screen
	
	private ScreenManager()
	{
	}
	
	public static ScreenManager createManager()
	{
		ScreenManager newManager = new ScreenManager();
		
		if (activeManager == null)
			activeManager = newManager;
		
		return newManager;
	}
	
	public static void transition(Screen destination)
	{
		ClientContainer.transition(destination);
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
	
}
