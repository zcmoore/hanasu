package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Timer;

public class ScreenManager
{
	private static Timer transitionTimer = new TransitionTimer();
	private static ScreenManager activeManager;
	private static ClientContainer clientContainer;
	private static Image barImage;
	
	private MainScreen mainScreen;
	private ChannelScreen channelScreen;
	//TODO: add chat screen
	
	@SuppressWarnings("serial")
	private static class TransitionTimer extends Timer
	{
		public TransitionTimer()
		{
			super(5, new TransitionTimerListener());
		}
		
	}
	
	private static class TransitionTimerListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JViewport viewport = clientContainer.getScrollPane().getViewport();
			Point position = viewport.getViewPosition();
			position.x += 5;
			
			if (position.x >= 450)
			{
				JPanel panel = clientContainer.getInnerPane();
				panel.remove(0);
				
				position.x = 0;
				viewport.setViewPosition(position);
				transitionTimer.stop();
			}
			else
			{
				viewport.setViewPosition(position);
			}
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				ScreenManager.createManager();
			}
		});
	}
	
	private ScreenManager()
	{
		if (clientContainer == null)
		{
			clientContainer = new ClientContainer();
			clientContainer.setVisible(true);
		}
	}
	
	public static ScreenManager createManager()
	{
		ScreenManager newManager = new ScreenManager();
		
		if (activeManager == null)
		{
			activeManager = newManager;
		}
		
		return newManager;
	}
	
	public static void transition(Screen destination)
	{
		JPanel panel = clientContainer.getInnerPane();
		destination.setPreferredSize(new Dimension(450, 300));
		panel.add(destination);
		
		transitionTimer.start();
	}
	
	public static ClientContainer getClientContainer()
	{
		return clientContainer;
	}
	
	public static void setClientContainer(ClientContainer clientContainer)
	{
		ScreenManager.clientContainer = clientContainer;
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
