package edu.asu.ser.hanasu.screens;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import edu.asu.ser.hanasu.screens.components.Sidebar;

@SuppressWarnings("serial")
public abstract class Screen extends ImagePanel implements Managable
{
	private JLayeredPane layeredPane;
	protected JPanel accessiblePane;
	protected ScreenManager screenManagerReference;
	
	public Screen(Sidebar sidebar, Image backgroundImage, ScreenManager screenManager)
	{
		super(backgroundImage);
		screenManagerReference = screenManager;
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
	
	public void onEnter()
	{
		
	}
	
	public void onExit()
	{
		
	}
}
