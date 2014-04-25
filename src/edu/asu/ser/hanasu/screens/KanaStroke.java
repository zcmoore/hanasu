package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class KanaStroke extends JButton
{
	protected boolean highlighted;
	protected VirtualChannel associatedChannel;
	private BufferedImage imageWTransparency;
	private ScreenManager screenManagerReference;
	double xScaleFactor, yScaleFactor;
	int parentWidth, parentHeight;
	
	public KanaStroke(String strokeHighlightedPath, ScreenManager screenManager)
	{
		super();
		
		highlighted = false;
		
		try
		{
			Image readImage = ImageIO.read(new File(strokeHighlightedPath));
			imageWTransparency = (BufferedImage) readImage;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		associatedChannel = new VirtualChannel();
		
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setFocusPainted(false);
		this.screenManagerReference = screenManager;
		
	}
	
	public void setAssociatedChannel(String name, String password)
	{
		associatedChannel.setChannel(name, password);
	}
	
	public void onLeftMouseClick()
	{
		if (associatedChannel.isSet())
		{
			associatedChannel.connect(screenManagerReference);
		}
		else
		{
			onRightMouseClick();
		}
		
		System.out.println("Left button");
	}
	
	public void onRightMouseClick()
	{
		String name, password;
		name = (String) JOptionPane.showInputDialog("Channel Name: ");
		password = (String) JOptionPane.showInputDialog("Channel Password: ");
		
		setAssociatedChannel(name, password);
		
		System.out.println("Right button");
	}
	
	public void highlight()
	{
		// starts drawing highlighted image
		this.highlighted = true;
		//repaint();
	}
	
	public void dehighlight()
	{
		// stop drawing highlighted stroke
		this.highlighted = false;
		//repaint();
	}
	
	public boolean isHighlighted()
	{
		return this.highlighted;
	}
	
	public boolean checkAlphaValue(int posX, int posY)
	{
		if (posX < parentWidth
				&& posY < parentHeight)
			if (posX > 0 && posY > 0)
				return (imageWTransparency.getRGB((int) (posX / xScaleFactor),
						(int) (posY / yScaleFactor)) & 0xFF000000) == 0xFF000000;
		return false;
		
	}
	
	public void setScales(int newWidth, int newHeight)
	{
		xScaleFactor = (newWidth / (double) imageWTransparency
				.getWidth());
		yScaleFactor = (newHeight / (double) imageWTransparency
				.getHeight());
		
		parentWidth = newWidth;
		parentHeight = newHeight;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		
		if (isHighlighted())
		{
			Graphics2D graphics2d = (Graphics2D) g;

			graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			graphics2d.drawImage(imageWTransparency, 0, 0, parentWidth, parentHeight,
					null);
			
		}
		repaint();
	}
	
}
