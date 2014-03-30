package edu.asu.ser.hanasu.screens;

import java.awt.Component;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class Screen extends JPanel
{
	/**
	 * Protocol to activate before exiting a screen. This should close all local
	 * streams and processes that belong only to this screen.
	 */
	public abstract void prepareToExit();
	
	/**
	 * Protocol to activate before exiting a screen.
	 */
	public abstract void prepareToEnter();
	
	/**
	 * Restores this screen to its default state.
	 */
	public abstract void reset();
	
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
