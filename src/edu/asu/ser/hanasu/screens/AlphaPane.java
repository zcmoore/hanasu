package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

// TODO: add component transparency support
// TODO: add image support
/**
 * Panel to serve as a semi-transparent overlay. As of version 0.1, only solid colours are
 * supported. The opacity of components that are added to a panel of this type are not
 * controlled or guaranteed by this class.
 * 
 * @author Moore, Zachary
 * @version 0.1
 */
@SuppressWarnings("serial")
public class AlphaPane extends JPanel
{
	/**
	 * Constructor. Sets the background colour (i.e. overlay colour) of this panel to the
	 * specified colour. The default alpha value is 1.
	 * 
	 * @param colour
	 */
	public AlphaPane(Color colour)
	{
		super.setBackground(colour);
		super.setOpaque(false);
		setAlpha(1);
	}
	
	/**
	 * Constructor. Sets the background colour and alpha value of this panel.
	 * 
	 * @param colour
	 *            Desired background (i.e. overlay) colour.
	 * @param alpha
	 *            Desired alpha value.
	 */
	public AlphaPane(Color colour, float alpha)
	{
		this(colour);
		setAlpha(alpha);
	}
	
	/**
	 * Override to set the alpha value to either 1 or 0. This panel's isOpaque value will
	 * always remain false, so to not show the actual panel. As such,
	 * javax.swing.JComponent#setOpaque(boolean) is not invoked.
	 * 
	 * @see javax.swing.JComponent#setOpaque(boolean)
	 */
	@Override
	public void setOpaque(boolean isOpaque)
	{
		if (isOpaque)
			setAlpha(1);
		else
			setAlpha(0);
	}
	
	/*
	 * (non-Javadoc) Paints this component using the protocol specified by JPanel, and
	 * then paints a transparent overlay that corresponds to this panels background
	 * colour.
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.setColor(super.getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	/**
	 * Sets the alpha value for this panel.
	 * 
	 * Note: as of version 0.1, the specified alpha will only affect the background colour
	 * of this panel. Any components within this container will not be affected.
	 * 
	 * @param alpha
	 */
	public void setAlpha(float alpha)
	{
		try
		{
			Color colour = super.getBackground();
			float red = (float) colour.getRed() / 255f;
			float green = (float) colour.getGreen() / 255f;
			float blue = (float) colour.getBlue() / 255f;
			
			colour = new Color(red, green, blue, alpha);
			super.setBackground(colour);
		}
		catch (NullPointerException | IllegalArgumentException e)
		{
			String message = "AlphaPane caught expected exception in setAlpha(): "
					+ e.getClass();
			System.err.println(message);
		}
	}
	
	/**
	 * Equivalent to {@link #setAlpha(float)}
	 * 
	 * @see #setAlpha(float)
	 */
	public void setAlpha(double alpha)
	{
		setAlpha((float) alpha);
	}
}
