package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.Component;
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
	private Component currentPane;
	
	private class SizeAdapter extends ComponentAdapter
	{
		@Override
		public void componentResized(ComponentEvent event)
		{
			ClientContainer.this.validateSize();
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
	
	@Override
	public void validate()
	{
		super.validate();
		validateSize();
	}
	
	private class Transition extends Timer
	{
		public Transition(Component target)
		{
			super(5, null);
			addActionListener(new TransitionTimerListener(target));
			dimmingPane.setOpacity(0);
			layeredPane.add(dimmingPane, ClientContainer.DIMMING_LAYER);
			layeredPane.add(target, ClientContainer.TOP_LAYER);
		}
		
		private class TransitionTimerListener implements ActionListener
		{
			private Component target;
			private double delta;
			
			TransitionTimerListener(Component target)
			{
				this.target = target;
			}
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// @formatter:off
				Point position = target.getLocation();
				position.x -= delta;
				
				if (position.x <= 0)
				{
					layeredPane.remove(ClientContainer.this.currentPane);
					layeredPane.remove(ClientContainer.this.dimmingPane);
					
					ClientContainer.this.currentPane = target;
					
					position.x = 0;
					target.setLocation(position);
					
					((Screen) getCurrentPanel()).onEnter();
					
					layeredPane.setLayer(target, ClientContainer.SCREEN_LAYER);
					Transition.this.stop();
				}
				else
				{
					target.setLocation(position);
				}
				// @formatter:on
			}
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
		
		currentPane = initialScreen;
		layeredPane.add(initialScreen, SCREEN_LAYER);
		
		this.addComponentListener(new SizeAdapter());
		this.setVisible(true);
		
		new ValidationTimer(100).start();
	}
	
	public void validateSize()
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
	
	public JPanel getCurrentPanel()
	{
		JPanel currentPanel = (JPanel) layeredPane.getComponent(0);
		return currentPanel;
	}
	
	public void transition(JPanel destination)
	{
		// @formatter:off
		/*
		if (getCurrentPanel() != destination)
		{
			System.out.println("Transition Start");
			destination.setPreferredSize(innerDimension);
			innerPane.add(destination);
			
			transitionTimer.start();
		}
		*/
		// @formatter:on
	}
	
	private static AlphaPane createDimmingLayer()
	{
		AlphaPane dimmingLayer = new AlphaPane(Color.BLACK);
		dimmingLayer.setBounds(0, 0, 1280, 720);
		dimmingLayer.setOpacity(0.75f);
		
		return dimmingLayer;
	}
}
