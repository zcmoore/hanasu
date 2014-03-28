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
	private static ScreenManager activeManager;
	private static ClientContainer clientContainer;
	private static Image barImage;
	private static Timer transitionTimer;
	
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
			activeManager = new ScreenManager();
		}
		
		return newManager;
	}
	
	public static void transition(Screen destination)
	{
		JPanel panel = clientContainer.getInnerPane();
		destination.setPreferredSize(new Dimension(450, 300));
		panel.add(destination);
		
		// TODO: modularize
		if (transitionTimer != null)
			transitionTimer.stop();
		
		transitionTimer = new Timer(5, null);
		transitionTimer.addActionListener(new ActionListener() {
			
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
				System.out.println("tick");
			}
		});
		
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
	
	public static void setActiveManager(ScreenManager activeManager)
	{
		ScreenManager.activeManager = activeManager;
	}
	
}
