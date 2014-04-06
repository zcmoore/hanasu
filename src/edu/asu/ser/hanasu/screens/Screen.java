package edu.asu.ser.hanasu.screens;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class Screen extends ImagePanel
{
	private JLayeredPane layeredPane;
	protected JPanel accessiblePane;
	
	public Screen(Sidebar sidebar, Image backgroundImage)
	{
		super(backgroundImage);
		setLayout(new GridLayout(1, 0, 0, 0));
		
		layeredPane = new JLayeredPane();
		add(layeredPane);
		
		accessiblePane = new JPanel();
		accessiblePane.setVisible(true);
		accessiblePane.setBounds(0, 0, 450, 300);
		layeredPane.add(accessiblePane, new Integer(200));
		
		JPanel topComponentsContainer = new JPanel();
		topComponentsContainer.setBounds(0, 0, 450, 300);
		layeredPane.add(topComponentsContainer, new Integer(300));
		
		topComponentsContainer.setLayout(new BorderLayout(0, 0));
		topComponentsContainer.setOpaque(false);
		topComponentsContainer.add(sidebar, BorderLayout.EAST);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		
		for (Component pane : layeredPane.getComponents())
		{
			pane.setBounds(0, 0, width, height);
		}
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
