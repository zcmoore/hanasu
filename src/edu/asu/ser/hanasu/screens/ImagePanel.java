package edu.asu.ser.hanasu.screens;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel
{
	private Image backgroundImage;
	private Image toBeScaledImage;
	
	public ImagePanel(Image backgroundImage)
	{
		this.backgroundImage = backgroundImage;
		this.toBeScaledImage = backgroundImage;
	}
	
	{
		addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent event)
			{
				int width = getWidth();
				int height = getHeight();
				if(width > 0 && height > 0)
					backgroundImage = toBeScaledImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			}
		});
	}
	
	protected void paintComponent(Graphics g)
	{
		Graphics2D tempGraphics = (Graphics2D) g.create();
		
		tempGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	
		tempGraphics.drawImage(backgroundImage, 0, 0, null);
		
		tempGraphics.dispose();
		
	}
}
