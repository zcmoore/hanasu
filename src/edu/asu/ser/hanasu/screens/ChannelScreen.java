package edu.asu.ser.hanasu.screens;

import java.awt.Color;

import javax.swing.JToggleButton;
import javax.swing.JTree;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
