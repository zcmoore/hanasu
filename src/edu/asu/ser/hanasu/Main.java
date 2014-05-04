package edu.asu.ser.hanasu;

import java.awt.EventQueue;

import edu.asu.ser.hanasu.screens.ScreenManager;

public class Main
{
	private static ScreenManager manager;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Setup());
	}
	
	private static class Setup implements Runnable
	{
		public void run()
		{
			manager = ScreenManager.createManager();
		}
	}
	
	public static ScreenManager getManager()
	{
		return manager;
	}
}
