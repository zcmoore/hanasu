package edu.asu.ser.hanasu.screens;

import javax.swing.JToggleButton;

import java.awt.Color;

import javax.swing.JTree;

@SuppressWarnings("serial")
public class ChannelScreen extends Screen
{
	
	/**
	 * Create the panel.
	 */
	public ChannelScreen()
	{
		// Temporary/Test contents
		// TODO: replace with actual MainScreen contents
		setBackground(Color.RED);
		
		JTree tree = new JTree();
		tree.setForeground(Color.RED);
		tree.setBackground(Color.RED);
		add(tree);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton(
				"New toggle button");
		add(tglbtnNewToggleButton);
		
	}
	
}
