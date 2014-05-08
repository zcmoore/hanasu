package edu.asu.ser.hanasu.screens.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class EnterButton extends JButton
{
	BufferedImage buttonImage;
	double xScaleFactor, yScaleFactor;
	int parentWidth, parentHeight;
	
	public EnterButton()
	{
		super();

		try
		{
			String buttonPath = "src/Images/ChatScreenEnterButton.png";
			Image readImage = ImageIO.read(new File(buttonPath));
			buttonImage = (BufferedImage) readImage;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setFocusPainted(false);
		
	}
	
	public boolean checkAlphaValue(int posX, int posY)
	{
		if (posX < parentWidth
				&& posY < parentHeight)
			if (posX > 0 && posY > 0)
			{
				
				return (buttonImage.getRGB((int) (posX / xScaleFactor),
						(int) (posY / yScaleFactor)) & 0xFF000000) != 0;
			}
		return false;
		
	}
	
	public void setScales(int newWidth, int newHeight)
	{
		xScaleFactor = (newWidth / (double) buttonImage
				.getWidth());
		yScaleFactor = (newHeight / (double) buttonImage
				.getHeight());
		
		parentWidth = newWidth;
		parentHeight = newHeight;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D graphics2d = (Graphics2D) g;

		graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		
		graphics2d.drawImage(buttonImage, 0, 0, parentWidth, parentHeight,
				null);
		
		repaint();
			
	}
	
	
	
}
