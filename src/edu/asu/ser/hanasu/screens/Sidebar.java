package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;

import edu.asu.ser.hanasu.screens.SidebarButton.SidebarButtonType;

@SuppressWarnings("serial")
public class Sidebar extends JPanel
{
	/**
	 * Create the panel.
	 */
	public Sidebar(HashMap<SidebarButtonType, ActionListener> listeners)
	{
		this.setPreferredSize(new Dimension(75, 300));
		this.setOpaque(false);
		setLayout(new GridLayout(7, 1, 0, 0));
		
		SidebarButton sdbrbtnM = new SidebarButton("Main Screen", "M");
		sdbrbtnM.addActionListener(listeners.get(SidebarButtonType.MAIN_SCREEN_BUTTON));
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		add(panel);
		add(sdbrbtnM);
		
		SidebarButton sdbrbtnR = new SidebarButton("Recent Channel", "R1");
		add(sdbrbtnR);
		
		SidebarButton sdbrbtnR_1 = new SidebarButton("Recent Channel", "R2");
		add(sdbrbtnR_1);
		
		SidebarButton sdbrbtnR_2 = new SidebarButton("Recent Channel", "R3");
		add(sdbrbtnR_2);
		
		SidebarButton sdbrbtnC = new SidebarButton("Channel Screen", "C");
		sdbrbtnC.addActionListener(listeners.get(SidebarButtonType.CHANNEL_SCREEN_BUTTON));
		add(sdbrbtnC);
	}
	
}
