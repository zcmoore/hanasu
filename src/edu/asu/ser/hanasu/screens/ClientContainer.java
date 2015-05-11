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
public class ClientContainer extends JFrame implements Singleton, Layered
{
	private static final Integer DIMMING_LAYER = SEMI_TOP_LAYER;
	private static final Integer SCREEN_LAYER = SEMI_BOTTOM_LAYER;
	
	private final AlphaPane dimmingPane = createDimmingLayer();
	
	private JLayeredPane layeredPane;
	private JPanel currentPanel;
	private Transition currentTransition;
	
	private class Transition extends Timer
	{
		private JPanel target;
		private ActionListener onStart;
		private ActionListener onFinish;
		
		public Transition(JPanel target)
		{
			super(5, null);
			this.target = target;
			addActionListener(new TransitionTimerListener());
		}
		
		public Transition(JPanel target, ActionListener onStart,
				ActionListener onFinish)
		{
			this(target);
			this.onStart = onStart;
			this.onFinish = onFinish;
		}
		
		@Override
		public void start()
		{
			if (currentTransition != null)
				throw new IllegalStateException();
			else
				currentTransition = this;
			
			if (onStart != null)
				onStart.actionPerformed(null);
			
			dimmingPane.setAlpha(0);
			ClientContainer.this.addDimmingPane();
			target.setLocation(getInnerDimension().width, 0);
			ClientContainer.this.addTopPane(target);
			super.start();
		}
		
		public void stop()
		{
			currentTransition = null;
			super.stop();
		}
		
		private class TransitionTimerListener implements ActionListener
		{
			// TODO: Clean & modularize
			// TODO: Replace with "NewtonianBehaviour" objects
			// TODO: Calculate values based on width and time
			private NewtonianBehaviour position;
			private NewtonianBehaviour luminosity;
			
			public TransitionTimerListener()
			{
				position = new NewtonianBehaviour(1280, -50, 1);
				position.setMaxVelocity(-1);
				position.setMinValue(0);
				
				luminosity = new NewtonianBehaviour(0, 0.02, 0);
				luminosity.setMaxValue(1.0);
				luminosity.setMinVelocity(0.01);
				System.out.println(position);
			}
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Point targetLocation = target.getLocation();
				targetLocation.x = (int) position.advance();
				target.setLocation(targetLocation);
				
				luminosity.advance();
				dimmingPane.setAlpha(luminosity.getValue());
				
				if (position.isAtLimit())
					finish();
			}
			
			public void finish()
			{
				layeredPane.remove(ClientContainer.this.currentPanel);
				layeredPane.remove(ClientContainer.this.dimmingPane);
				
				ClientContainer.this.currentPanel = target;
				
				layeredPane.setLayer(target, ClientContainer.SCREEN_LAYER);
				Transition.this.stop();
				
				if (onFinish != null)
					onFinish.actionPerformed(null);
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
		transition(destination, null, null);
	}
	
	public void transition(JPanel destination, ActionListener onStart,
			ActionListener onFinish)
	{
		if (getCurrentPanel() != destination)
		{
			destination.setSize(getInnerDimension());
			Transition transition = new Transition(destination, onStart,
					onFinish);
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

	public boolean isTransitioning()
	{
		return this.currentTransition != null;
	}
}
