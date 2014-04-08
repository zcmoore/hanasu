package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AlphaPane extends JPanel
{
	private Color colour;
	
	public AlphaPane(Color colour)
	{
		this.colour = colour;
		setOpaque(false);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.setColor(colour);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	public void setOpacity(float opacity)
	{
		 int red = colour.getRed();
		 int green = colour.getGreen();
		 int blue = colour.getBlue();
		
		colour = new Color(red, green, blue, opacity);
	}
}
