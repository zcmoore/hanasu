package edu.asu.ser.hanasu.screens;

import java.awt.EventQueue;
import java.awt.Image;
import java.util.ArrayList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ScreenManager
{
	private static Image barImage;
	private static ArrayList<Screen> screens;
	private static Screen defaultScreen;
	private static Screen currentScreen;
	private static String nickName;
	private static ClientContainer clientContainer;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					clientContainer = new ClientContainer();
					clientContainer.setVisible(true);
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void transition(Screen destination)
	{
		//TODO: implement screen transition code
		throw new NotImplementedException();
	}

	public static Image getBarImage()
	{
		return barImage;
	}

	public static void setBarImage(Image barImage)
	{
		ScreenManager.barImage = barImage;
	}

	public static ArrayList<Screen> getScreens()
	{
		return screens;
	}

	public static void setScreens(ArrayList<Screen> screens)
	{
		ScreenManager.screens = screens;
	}

	public static Screen getDefaultScreen()
	{
		return defaultScreen;
	}

	public static void setDefaultScreen(Screen defaultScreen)
	{
		ScreenManager.defaultScreen = defaultScreen;
	}

	public static Screen getCurrentScreen()
	{
		return currentScreen;
	}

	public static void setCurrentScreen(Screen currentScreen)
	{
		ScreenManager.currentScreen = currentScreen;
	}

	public static String getNickName()
	{
		return nickName;
	}

	public static void setNickName(String nickName)
	{
		ScreenManager.nickName = nickName;
	}

	public static ClientContainer getClientContainer()
	{
		return clientContainer;
	}

	public static void setClientContainer(ClientContainer clientContainer)
	{
		ScreenManager.clientContainer = clientContainer;
	}
	
}
