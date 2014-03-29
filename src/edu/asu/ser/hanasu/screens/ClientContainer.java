package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.GridLayout;
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
	private static Timer transitionTimer = new TransitionTimer();
	private static ClientContainer activeClient;
	private JScrollPane scrollPane;
	private JPanel innerPane;
	
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
			JViewport viewport = activeClient.scrollPane.getViewport();
			Point position = viewport.getViewPosition();
			position.x += 5;
			
			if (position.x >= 450)
			{
				JPanel panel = activeClient.innerPane;
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
	
	public static ClientContainer create()
	{
		if (activeClient == null)
			activeClient = new ClientContainer();
		else
			throw new IllegalStateException("ClientContiner is Singleton");
		
		return activeClient;
	}
	
	/**
	 * Create the frame.
	 */
	private ClientContainer()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450 + 16, 300 + 38);
		innerPane = new JPanel();
		innerPane.setSize(900, 300);
		innerPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		scrollPane = new JScrollPane(innerPane);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		setContentPane(scrollPane);
		
		MainScreen mainScreen = new MainScreen();
		mainScreen.setPreferredSize(new Dimension(450, 300));
		innerPane.add(mainScreen);
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e)
			{
				e.getComponent().getHeight();
			}
		});
		
		this.setVisible(true);
	}
	
	public static void transition(JPanel destination)
	{
		JPanel panel = activeClient.innerPane;
		destination.setPreferredSize(new Dimension(450, 300));
		panel.add(destination);
		
		transitionTimer.start();
	}
	
	public static ClientContainer getActiveClient()
	{
		return activeClient;
	}
}
