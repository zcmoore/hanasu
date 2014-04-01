package edu.asu.ser.hanasu.screens;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.JLayeredPane;

import java.util.HashMap;

@SuppressWarnings("serial")
public abstract class Screen extends JPanel
{
	protected JPanel innerPane;
	
	public Screen(Sidebar sidebar)
	{
		setLayout(new GridLayout(1, 0, 0, 0));
		
		JLayeredPane layeredPane = new JLayeredPane();
		add(layeredPane);
		
		sidebar.setBounds(0, 0, 100, 300);
		sidebar.setVisible(true);
		layeredPane.add(sidebar, new Integer(300));
		
		innerPane = new JPanel();
		innerPane.setVisible(true);
		innerPane.setBounds(0, 0, 450, 300);
		layeredPane.add(innerPane, new Integer(200));
		
		JLabel label = new JLabel("test");
	}
	
	/**
	 * Protocol to activate before exiting a screen. This should close all local
	 * streams and processes that belong only to this screen.
	 */
	public abstract void prepareToExit();
	
	/**
	 * Protocol to activate before entering a screen. Open necessary streams if
	 * applicable.
	 */
	public abstract void prepareToEnter();
	
	/**
	 * Restores this screen to its default state.
	 */
	public abstract void reset();
	
	public void onEnter()
	{
		
	}
	
	public void onExit()
	{
		
	}
	
	/**
	 * Disables this screen and all components within its panel.
	 */
	@Override
	public void disable()
	{
		for (Component component : this.getComponents())
		{
			component.setEnabled(false);
		}
		
		this.setEnabled(false);
	}
	
	/**
	 * Enables this screen and all components within its panel.
	 */
	@Override
	public void enable()
	{
		for (Component component : this.getComponents())
		{
			component.setEnabled(true);
		}
		
		this.setEnabled(false);
	}
}
