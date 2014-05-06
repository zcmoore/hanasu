package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class MessagesReceivedBox extends JTextArea
{
	BufferedImage transparentBoundsImage;
	double xScaleFactor, yScaleFactor;
	int parentWidth, parentHeight;
	
	public MessagesReceivedBox()
	{
		super();
		
		try
		{
			String buttonPath = "src/Images/ChatScreenReceivedMessagesBox.png";
			Image readImage = ImageIO.read(new File(buttonPath));
			transparentBoundsImage = (BufferedImage) readImage;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		this.setEditable(false);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setLineWrap(true);
		this.setWrapStyleWord(true);
		this.setOpaque(false);
	}
	
	public Dimension getImageDimensions()
	{
		return new Dimension(transparentBoundsImage.getWidth(),transparentBoundsImage.getHeight());
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
	}
	
	
	
	
}
