package edu.asu.ser.hanasu.screens;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;

import javax.swing.JLayeredPane;

import edu.asu.ser.hanasu.screens.components.ChatMessageField;
import edu.asu.ser.hanasu.screens.components.EnterButton;
import edu.asu.ser.hanasu.screens.components.MessagesReceivedBox;
import edu.asu.ser.hanasu.server.EncryptedMessage;

@SuppressWarnings("serial")
public class ChatScreenLayeredPane extends JLayeredPane
{
	private MessagesReceivedBox chatMessagesReceivedBox;
	private ChatMessageField chatMessageEnterBox;
	private EnterButton enterButton;
	private ScreenManager screenManager;
	
	public ChatScreenLayeredPane(int width, int height,
			ScreenManager screenManager)
	{
		chatMessagesReceivedBox = new MessagesReceivedBox();
		chatMessageEnterBox = new ChatMessageField();
		enterButton = new EnterButton();
		
		chatMessagesReceivedBox.setBounds(0, 0, (int) chatMessagesReceivedBox
				.getImageDimensions().getWidth(), (int) chatMessagesReceivedBox
				.getImageDimensions().getHeight());
		chatMessagesReceivedBox.setOpaque(false);
		
		chatMessageEnterBox.setBounds(0, 0, width, height);
		chatMessageEnterBox.setLocation(0, 0);
		
		enterButton.setBounds(0, 0, width, height);
		enterButton.setLocation(0, 0);
		
		this.add(chatMessagesReceivedBox, JLayeredPane.DEFAULT_LAYER);
		this.add(chatMessageEnterBox, JLayeredPane.DEFAULT_LAYER);
		this.add(enterButton, JLayeredPane.DEFAULT_LAYER);
		
		enterButton.addMouseListener(new ChatScreenMouseListener());
		chatMessageEnterBox.addMouseListener(new ChatScreenMouseListener());
		chatMessageEnterBox.addKeyListener(new ChatScreenKeyListener());
		
		this.setOpaque(false);
		
		this.screenManager = screenManager;
		
	}
	
	public MessagesReceivedBox getChatMessagesReceivedBox()
	{
		return chatMessagesReceivedBox;
	}
	
	public ChatMessageField getChatMessageEnterBox()
	{
		return chatMessageEnterBox;
	}
	
	public EnterButton getEnterButton()
	{
		return enterButton;
	}
	
	public void setChatMessagesReceivedBox(
			MessagesReceivedBox chatMessagesReceivedBox)
	{
		this.chatMessagesReceivedBox = chatMessagesReceivedBox;
	}
	
	public void setChatMessageEnterBox(ChatMessageField chatMessageEnterBox)
	{
		this.chatMessageEnterBox = chatMessageEnterBox;
	}
	
	public void setEnterButton(EnterButton enterButton)
	{
		this.enterButton = enterButton;
	}
	
	private class ChatScreenMouseListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
		}
		
		@Override
		public void mouseEntered(MouseEvent e)
		{
			
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			Point point = e.getPoint();
			if (enterButton.checkAlphaValue(point.x, point.y))
			{
				System.out.println("Button clicked");
				byte[] unencryptedMessage;
				try
				{
					unencryptedMessage = chatMessageEnterBox.getText()
							.getBytes("UTF-8");
					String channelName = screenManager.getUserObject()
							.getHostName();
					screenManager.getClientForServer().sendMessageToServer(
							new EncryptedMessage(unencryptedMessage,
									channelName));
					chatMessageEnterBox.setText("");
				}
				catch (UnsupportedEncodingException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
		}
		
	}
	
	private class ChatScreenKeyListener implements KeyListener
	{
		
		@Override
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				// call client to encrypt and send
				// reset text in chatfield
				byte[] unencryptedMessage;
				try
				{
					unencryptedMessage = chatMessageEnterBox.getText().trim()
							.getBytes("UTF-8");
					String channelName = screenManager.getUserObject()
							.getHostName();
					screenManager.getClientForServer().sendMessageToServer(
							new EncryptedMessage(unencryptedMessage,
									channelName));
					chatMessageEnterBox.setText("");
				}
				catch (UnsupportedEncodingException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e)
		{
		}
		
		@Override
		public void keyTyped(KeyEvent e)
		{
			
		}
		
	}
	
}
