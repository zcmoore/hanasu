package edu.asu.ser.hanasu.screens;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneUI;

@SuppressWarnings("serial")
public abstract class Screen extends JPanel
{
	protected JPanel innerPane;
	protected JSplitPane innerSplitPane;
	
	public Screen(Sidebar sidebar)
	{
		super.setLayout(new GridLayout(1, 0, 0, 0));
		innerPane = new JPanel();
		
		innerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				innerPane, sidebar);
		innerSplitPane.setDividerLocation(400);
		hideDivider(innerSplitPane);
		
		JSplitPane verticalSplitPane = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT, new JButton(), innerSplitPane);
		hideDivider(verticalSplitPane);
		
		super.add(verticalSplitPane);
	}
	
	private static void hideDivider(JSplitPane splitPane)
	{
		((BasicSplitPaneUI) splitPane.getUI()).getDivider().setVisible(false);
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
		innerSplitPane.setDividerLocation(400);
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
