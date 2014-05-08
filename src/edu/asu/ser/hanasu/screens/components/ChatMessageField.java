package edu.asu.ser.hanasu.screens.components;

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

@SuppressWarnings("serial")
public class ChatMessageField extends JTextArea
{
	BufferedImage textFieldImage;
	double xScaleFactor, yScaleFactor;
	int parentWidth, parentHeight;
	
	public ChatMessageField()
	{
		super();
		
		try
		{
			String buttonPath = "src/Images/ChatScreenChatTextEnterEdited.png";
			Image readImage = ImageIO.read(new File(buttonPath));
			textFieldImage = (BufferedImage) readImage;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		this.setLineWrap(true);
		this.setWrapStyleWord(true);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setOpaque(false);
		
	}
	
	public boolean checkAlphaValue(int posX, int posY)
	{
		if (posX < parentWidth
				&& posY < parentHeight)
			if (posX > 0 && posY > 0)
			{
				
				return (textFieldImage.getRGB((int) (posX / xScaleFactor),
						(int) (posY / yScaleFactor)) & 0xFF000000) != 0;
			}
		
		return false;
	}
		
	
	public void setScales(int newWidth, int newHeight)
	{
		xScaleFactor = (newWidth / (double) textFieldImage
				.getWidth());
		yScaleFactor = (newHeight / (double) textFieldImage
				.getHeight());
		
		parentWidth = newWidth;
		parentHeight = newHeight;
	}
	
	public Dimension getImageDimensions()
	{
		return new Dimension(textFieldImage.getWidth(),textFieldImage.getHeight());
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D graphics2d = (Graphics2D) g;
		
		//super.paintComponent(g);
		
		graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		
		graphics2d.drawImage(textFieldImage, 0, 0, parentWidth, parentHeight,
				null);
		
		graphics2d.drawString(this.getText(), (int)(0 + (495 * xScaleFactor)), (int)(parentHeight - (180 * yScaleFactor)));
		
		repaint();
			
	}
}
