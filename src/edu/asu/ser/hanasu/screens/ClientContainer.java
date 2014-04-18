package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import edu.asu.ser.hanasu.Singleton;

@SuppressWarnings("serial")
public class ClientContainer extends JFrame implements Singleton
{
	private static final Integer TOP_LAYER = new Integer(700);
	private static final Integer DIMMING_LAYER = new Integer(500);
	private static final Integer SCREEN_LAYER = new Integer(300);
	
	private final AlphaPane dimmingPane = createDimmingLayer();
	
	private JLayeredPane layeredPane;
	private JPanel currentPanel;
	
	private class Transition extends Timer
	{
		private JPanel target;
		
		public Transition(JPanel target)
		{
			super(5, null);
			this.target = target;
			addActionListener(new TransitionTimerListener());
			
		}
		
		@Override
		public void start()
		{
			dimmingPane.setAlpha(0);
			ClientContainer.this.addDimmingPane();
			target.setLocation(getInnerDimension().width, 0);
			ClientContainer.this.addTopPane(target);
			super.start();
		}
		
		private class TransitionTimerListener implements ActionListener
		{
			// TODO: Clean & modularize
			// TODO: Replace with "ClassicalMotion" objects
			// TODO: Calculate values based on width and time
			private double positionAcceleration = -0.27;
			private double positionVelocity = 25;
			
			private float dimmingAlpha = 0;
			private double dimmingVelocity = 0.01;
			private double dimmingAcceleration = 0;
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Point position = target.getLocation();
				position.x -= positionVelocity;
				
				if (position.x <= 0)
				{
					layeredPane.remove(ClientContainer.this.currentPanel);
					layeredPane.remove(ClientContainer.this.dimmingPane);
					
					ClientContainer.this.currentPanel = target;
					
					position.x = 0;
					target.setLocation(position);
					
					((Screen) getCurrentPanel()).onEnter();
					
					layeredPane.setLayer(target, ClientContainer.SCREEN_LAYER);
					Transition.this.stop();
				}
				else
				{
					target.setLocation(position);
					dimmingAlpha += dimmingVelocity;
					dimmingVelocity += dimmingAcceleration;
					
					dimmingPane.setAlpha(dimmingAlpha);
					positionVelocity += positionAcceleration;
					
					if (positionVelocity < 1)
						positionVelocity = 1;
					if (dimmingVelocity <= 0)
						dimmingVelocity = 0.01;
					if (dimmingAlpha > 1)
						dimmingAlpha = 1;
				}
			}
		}
	}
	
	private class ValidationTimer extends Timer
	{
		public ValidationTimer(int time)
		{
			super(time, null);
			addActionListener(new ValidationTimerListener());
		}
		
		private class ValidationTimerListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ClientContainer.this.validate();
			}
		}
	}
	
	private class SizeAdapter extends ComponentAdapter
	{
		@Override
		public void componentResized(ComponentEvent event)
		{
			ClientContainer.this.validateSize();
		}
	}
	
	/**
	 * Create the frame.
	 */
	public ClientContainer(Screen initialScreen)
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		initialScreen.setBounds(0, 0, 1280, 720);
		
		layeredPane = new JLayeredPane();
		setContentPane(layeredPane);
		
		currentPanel = initialScreen;
		layeredPane.add(initialScreen, SCREEN_LAYER);
		
		this.addComponentListener(new SizeAdapter());
		this.setVisible(true);
		
		new ValidationTimer(100).start();
	}
	
	private void addDimmingPane()
	{
		layeredPane.remove(dimmingPane);
		layeredPane.add(dimmingPane, DIMMING_LAYER);
	}
	
	private void addTopPane(JPanel panel)
	{
		layeredPane.add(panel, ClientContainer.TOP_LAYER);
	}
	
	public void transition(JPanel destination)
	{
		if (getCurrentPanel() != destination)
		{
			destination.setSize(getInnerDimension());
			Transition transition = new Transition(destination);
			destination.setPreferredSize(getInnerDimension());
			
			transition.start();
		}
	}
	
	public Dimension getInnerDimension()
	{
		return getContentPane().getSize();
	}
	
	public JPanel getCurrentPanel()
	{
		return currentPanel;
	}
	
	private void validateSize()
	{
		AspectRatio ratio = AspectRatio.x16_9;
		Dimension currentSize = getSize();
		
		if (!ratio.isMultiple(currentSize))
		{
			currentSize = ratio.formatDimension(currentSize);
			setSize(currentSize);
		}
		
		try
		{
			if (!currentSize.equals(getCurrentPanel().getSize()))
			{
				Dimension innerDimension = new Dimension(getContentPane()
						.getSize());
				getCurrentPanel().setBounds(0, 0, innerDimension.width,
						innerDimension.height);
			}
		}
		catch (NullPointerException nullPointerException)
		{
			nullPointerException.printStackTrace();
		}
	}
	
	private static AlphaPane createDimmingLayer()
	{
		AlphaPane dimmingLayer = new AlphaPane(Color.BLACK);
		dimmingLayer.setBounds(0, 0, 1280, 720);
		dimmingLayer.setAlpha(0.75f);
		
		return dimmingLayer;
	}
}
