package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@SuppressWarnings("serial")
public class MainScreen extends Screen
{
	/**
	 * Create the panel.
	 */
	public MainScreen()
	{
		// Temporary/Test contents
		// TODO: replace with actual MainScreen contents
		setBackground(Color.GRAY);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		chckbxNewCheckBox.setBackground(Color.GRAY);
		add(chckbxNewCheckBox);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBackground(Color.GRAY);
		add(btnNewButton);
		
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				ScreenManager manager = ScreenManager.getActiveManager();
				ClientContainer.transition(manager.getMainScreen());
			}
		});
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
