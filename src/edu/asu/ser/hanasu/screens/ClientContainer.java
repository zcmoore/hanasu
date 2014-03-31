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
	private Timer transitionTimer = new TransitionTimer();
	private JScrollPane scrollPane;
	private JPanel innerPane;
	
	private class TransitionTimer extends Timer
	{
		public TransitionTimer()
		{
			super(5, null);
			addActionListener(new TransitionTimerListener());
		}
		
		private class TransitionTimerListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JViewport viewport = scrollPane.getViewport();
				Point position = viewport.getViewPosition();
				position.x += 5;
				
				if (position.x >= 450)
				{
					innerPane.remove(0);
					
					position.x = 0;
					viewport.setViewPosition(position);
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
		int boundWidth = 450 + insets.left + insets.right;
		int boundHeight = 300 + insets.top + insets.bottom;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, boundWidth, boundHeight);
		innerPane = new JPanel();
		innerPane.setSize(900, 300);
		innerPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		scrollPane = new JScrollPane(innerPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		setContentPane(scrollPane);
		
		initialScreen.setPreferredSize(new Dimension(450, 300));
		innerPane.add(initialScreen);
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e)
			{
				e.getComponent().getHeight();
			}
		});
		
		this.setVisible(true);
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
			destination.setPreferredSize(new Dimension(450, 300));
			innerPane.add(destination);
			
			transitionTimer.start();
		}
	}
}
