package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Timer;

import edu.asu.ser.hanasu.Singleton;

@SuppressWarnings("serial")
public class ClientContainer extends JFrame implements Singleton
{
	private final Timer transitionTimer;
	private final JScrollPane scrollPane;
	private final JPanel innerPane;
	
	protected Dimension outterDimension;
	protected Dimension innerDimension;
	
	private class SizeAdapter extends ComponentAdapter
	{
		@Override
		public void componentResized(ComponentEvent event)
		{
			JFrame newFrame = ((JFrame) event.getComponent());
			
			outterDimension = AspectRatio.x16_9.formatDimension(newFrame.getSize());
			newFrame.setSize(outterDimension);
			innerDimension = (Dimension) newFrame.getContentPane().getSize().clone();
			
			//getCurrentPanel().setSize(innerDimension);
			
			ResizeTimer timer = new ResizeTimer(100);
			timer.start();
		}
	}
	
	private class ResizeTimer extends Timer
	{
		public ResizeTimer(int time)
		{
			super(time, null);
			addActionListener(new ResizeTimerListener());
		}
		
		private class ResizeTimerListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setSize(outterDimension);
				ResizeTimer.this.stop();
			}
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
				double stepsPerSecond = 1000 / 5;
				double desiredTime = 0.5;
				double totalSteps = stepsPerSecond * desiredTime;
				double totalDelta = innerDimension.width;
				
				delta = totalDelta / totalSteps;
			}
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
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
			}
		}
	}
	
	/**
	 * Create the frame.
	 */
	public ClientContainer(Screen initialScreen)
	{
		Insets insets = getInsets();
		int boundWidth = 1280 + insets.left + insets.right;
		int boundHeight = 720 + insets.top + insets.bottom;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, boundWidth, boundHeight);
		innerPane = new JPanel();
		innerPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		scrollPane = new JScrollPane(innerPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		setContentPane(scrollPane);
		
		innerPane.add(initialScreen);
		
		this.addComponentListener(new SizeAdapter());
		this.setVisible(true);
		
		outterDimension = new Dimension(getWidth(), getHeight());
		innerDimension = new Dimension(getContentPane().getSize());
		
		transitionTimer = new TransitionTimer();
	}
	
	public JPanel getCurrentPanel()
	{
		JPanel currentPanel = (JPanel) innerPane.getComponent(0);
		return currentPanel;
	}
	
	public void transition(JPanel destination)
	{
		if (getCurrentPanel() != destination)
		{
			System.out.println("Transition Start");
			destination.setPreferredSize(innerDimension);
			innerPane.add(destination);
			
			transitionTimer.start();
		}
	}
}
