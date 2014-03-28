package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@SuppressWarnings("serial")
public class ClientContainer extends JFrame
{
	private static JScrollPane scrollPane;
	private JPanel innerPane;
	
	/**
	 * Create the frame.
	 */
	public ClientContainer()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450 + 16, 300 + 38);
		innerPane = new JPanel();
		innerPane.setSize(900, 300);
		innerPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		scrollPane = new JScrollPane(innerPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setSize(450, 300);
		scrollPane.getViewport().setSize(450, 300);
		setContentPane(scrollPane);
		
		MainScreen mainScreen = new MainScreen();
		mainScreen.setPreferredSize(new Dimension(450, 300));
		innerPane.add(mainScreen);
		
		ChannelScreen channelScreen = new ChannelScreen();
		channelScreen.setPreferredSize(new Dimension(450, 300));
		innerPane.add(channelScreen);
		
		this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	e.getComponent().getHeight();
            }
        });
	}
	
	public static JScrollPane getScrollPane()
	{
		return scrollPane;
	}
	
}
