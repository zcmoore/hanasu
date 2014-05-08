package edu.asu.ser.hanasu.screens.components;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class SidebarButton extends JButton
{
	Image buttonImage;
	
	public static enum SidebarButtonType
	{
		MAIN_SCREEN_BUTTON,
		CHANNEL_BUTTON_1,
		CHANNEL_BUTTON_2,
		CHANNEL_BUTTON_3,
		CHANNEL_SCREEN_BUTTON;
	}
	
	public SidebarButton(String name, String abbreviation)
	{
		String buttonPath = "src/Images/Circle.png";
		try
		{
			buttonImage = ImageIO.read(new File(buttonPath));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		this.setOpaque(false);
		this.setBorder(BorderFactory.createEmptyBorder());
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics tempGraphics = g.create();
		g.drawImage(buttonImage, this.getWidth() / 3, this.getHeight() / 3, null);
	}
}
