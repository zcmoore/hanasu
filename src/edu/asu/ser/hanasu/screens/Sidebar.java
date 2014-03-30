package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JPanel;

import edu.asu.ser.hanasu.screens.ScreenManager.ScreenType;
import java.awt.Component;

@SuppressWarnings("serial")
public class Sidebar extends JPanel
{
	
	/**
	 * Create the panel.
	 */
	public Sidebar()
	{
		this.setPreferredSize(new Dimension(75, 300));
		setLayout(new GridLayout(7, 1, 0, 0));
		
		SidebarButton sdbrbtnM = new SidebarButton("Main Screen", "M");
		sdbrbtnM.addActionListener(new TransitionListener(ScreenType.MAIN));
		
		Component verticalGlue = Box.createVerticalGlue();
		add(verticalGlue);
		add(sdbrbtnM);
		
		SidebarButton sdbrbtnR = new SidebarButton("Recent Channel", "R1");
		add(sdbrbtnR);
		
		SidebarButton sdbrbtnR_1 = new SidebarButton("Recent Channel", "R2");
		add(sdbrbtnR_1);
		
		SidebarButton sdbrbtnR_2 = new SidebarButton("Recent Channel", "R3");
		add(sdbrbtnR_2);
		
		SidebarButton sdbrbtnC = new SidebarButton("Channel Screen", "C");
		sdbrbtnM.addActionListener(new TransitionListener(ScreenType.CHANNEL));
		add(sdbrbtnC);
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		add(verticalGlue_1);
	}
	
}
