package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
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
	private final Timer transitionTimer;
	private JLayeredPane layeredPane;
	
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
	
	private class TransitionTimer extends Timer
	{
		public TransitionTimer()
		{
			super(5, null);
			addActionListener(new TransitionTimerListener());
		}
		
		private class TransitionTimerListener implements ActionListener
		{
			private double delta;
			
			TransitionTimerListener()
			{
				
			}
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// @formatter:off
				/*
				JViewport viewport = scrollPane.getViewport();
				Point position = viewport.getViewPosition();
				position.x += delta;
				
				if (position.x >= innerDimension.width)
				{
					innerPane.remove(0);
					
					position.x = 0;
					viewport.setViewPosition(position);
					((Screen) getCurrentPanel()).onEnter();
					TransitionTimer.this.stop();
				}
				else
				{
					viewport.setViewPosition(position);
				}
				*/
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
		layeredPane.add(initialScreen);
		setContentPane(layeredPane);
		
		this.addComponentListener(new SizeAdapter());
		this.setVisible(true);
		
		transitionTimer = new TransitionTimer();
		new ValidationTimer(100).start();
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
}
