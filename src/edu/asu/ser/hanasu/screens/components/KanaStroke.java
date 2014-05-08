package edu.asu.ser.hanasu.screens.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import edu.asu.ser.hanasu.screens.ScreenManager;
import edu.asu.ser.hanasu.server.VirtualChannel;

@SuppressWarnings("serial")
public class KanaStroke extends JButton 
{
	protected boolean highlighted;
	protected VirtualChannel associatedChannel;
	private BufferedImage imageWTransparency;
	private ScreenManager screenManagerReference;
	double xScaleFactor, yScaleFactor;
	int parentWidth, parentHeight;
	private String serverIP = "localhost";
	private int serverPort;
	
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
		this.serverPort = 443;
	}
	
	public void setAssociatedChannel(String name, String password)
	{
		associatedChannel.setChannel(name, password);
	}
	
	public void setAssociatedChannelSave(String name, String password)
	{
		associatedChannel.setSaveChannels(name, password);
	}
	
	public void suStrokeConnect()
	{
		String name = "", password = "";
		
		name = (String) JOptionPane.showInputDialog("Channel Name: ");
		
		if(name != null)
		{
		
			if((name.equals(null)) || (name.equals("")) || (name.equals(" ")))
			{}
			else
			{
				password = (String) JOptionPane.showInputDialog("Channel Password: ");
				serverIP = (String) JOptionPane.showInputDialog("Please Input Server IP: ");
				serverPort = Integer.parseInt((String) JOptionPane.showInputDialog("Please Input Port Number: "));
			}
				
			
			setAssociatedChannel(name, password);
			
			if(associatedChannel.connect(screenManagerReference, serverIP, serverPort))
			{
				screenManagerReference.transition(screenManagerReference.getChatScreenType());
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Connection Unsuccessful");
			}
		}
		
	}
	
	public void onLeftMouseClick()
	{
		if (associatedChannel.isSet())
		{
			if(associatedChannel.connect(screenManagerReference, serverIP, serverPort))
			{
				screenManagerReference.transition(screenManagerReference.getChatScreenType());
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Connection Unsuccessful");
			}
		}
		else
		{
			onRightMouseClick();
		}
		
	}
	
	public void onRightMouseClick()
	{
		String name = "", password = "";
		
		name = (String) JOptionPane.showInputDialog("Channel Name: ");
		if(name != null)
		{
		
			if((name.equals(null)) || (name.equals("")) || (name.equals(" ")))
				password = "";
			else
			{
				password = (String) JOptionPane.showInputDialog("Password, Length 128 Bits (16 char): ");
				if(password.length() != 16)
				{
					password = keepAskingForPassword();
				}
				serverIP = (String) JOptionPane.showInputDialog("Please Input Server IP: ");
				serverPort = Integer.parseInt((String) JOptionPane.showInputDialog("Please Input Port Number: "));
			}
			
			setAssociatedChannel(name, password);
		}
	}
	

	private String keepAskingForPassword()
	{
		String password = (String) JOptionPane.showInputDialog("Password, Length 128 Bits (16 char): ");
		if(password.length() == 16)
			return password;
		else
			return keepAskingForPassword();
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

	public String getServerIP()
	{
		return serverIP;
	}
	
	public int getServerPort()
	{
		return serverPort;
	}
	
	public void setServerIP(String serverIP)
	{
		this.serverIP = serverIP;
	}

	public void setServerPort(int serverPort)
	{
		this.serverPort = serverPort;
	}

	public VirtualChannel getAssociatedChannel()
	{
		return associatedChannel;
	}
	
	
}
