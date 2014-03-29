package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ClientContainer extends JFrame
{
	private JScrollPane scrollPane;
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
		setContentPane(scrollPane);
		
		MainScreen mainScreen = new MainScreen();
		mainScreen.setPreferredSize(new Dimension(450, 300));
		innerPane.add(mainScreen);
		
		this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	e.getComponent().getHeight();
            }
        });
	}
	
	public JScrollPane getScrollPane()
	{
		return scrollPane;
	}

	public JPanel getInnerPane()
	{
		return innerPane;
	}
	
	
	
}
