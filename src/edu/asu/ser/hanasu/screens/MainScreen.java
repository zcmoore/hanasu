package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;

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
				System.out.println("\nMainScreen Bounds: " + getBounds());
				System.out.println("ScrollPane Bounds: " + ClientContainer.getScrollPane().getBounds());
				System.out.println("Client Bounds: " + ScreenManager.getClientContainer().getBounds() + "\n");
			}
		});
	}
	
}
