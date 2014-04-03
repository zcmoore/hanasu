package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.JTree;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@SuppressWarnings("serial")
public class ChannelScreen extends Screen
{
	
	/**
	 * Create the panel.
	 */
	public ChannelScreen(Sidebar sidebar)
	{
		super(sidebar);
		// Temporary/Test contents
		// TODO: replace with actual MainScreen contents
		setBackground(Color.RED);
		accessiblePane.setLayout(new GridLayout(3, 2, 0, 0));
		
		JTree tree = new JTree();
		tree.setForeground(Color.RED);
		tree.setBackground(Color.RED);
		accessiblePane.add(tree);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton(
				"New toggle button");
		accessiblePane.add(tglbtnNewToggleButton);
		
		JLabel lblNewLabel = new JLabel("New label");
		accessiblePane.add(lblNewLabel);
		
	}
	
	@Override
	public void prepareToExit()
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	@Override
	public void prepareToEnter()
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	@Override
	public void disable()
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	@Override
	public void enable()
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
}
