package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import edu.asu.ser.hanasu.screens.ScreenManager.UserObject;
import edu.asu.ser.hanasu.server.Command;
import edu.asu.ser.hanasu.server.EncryptedMessage;
import edu.asu.ser.hanasu.server.Command.Commands;

//TODO implement time on bottom right hand corner
//TODO implement currently connected to
@SuppressWarnings("serial")
public class ChatScreen extends Screen
{
	ChatScreenLayeredPane chatScreenLayeredPane;
	private ScreenManager screenManager;
	
	public ChatScreen(Sidebar sidebar, Image backgroundImage,
			ScreenManager screenManager)
	{
		super(sidebar, backgroundImage, screenManager);
		setBackground(Color.RED);
		
		accessiblePane.setLayout(new GridLayout(1, 0));
		
		chatScreenLayeredPane = new ChatScreenLayeredPane(
				accessiblePane.getWidth(), accessiblePane.getHeight(),
				screenManager);
		chatScreenLayeredPane.setPreferredSize(new Dimension(accessiblePane
				.getWidth(), accessiblePane.getHeight()));
		chatScreenLayeredPane.setBounds(0, 0, accessiblePane.getWidth(),
				accessiblePane.getHeight());
		chatScreenLayeredPane.setOpaque(false);
		
		accessiblePane.add(chatScreenLayeredPane);
		accessiblePane.setOpaque(false);
		
		this.addComponentListener(new SizeAdapter());
		
		this.screenManager = screenManager;
		
	}
	
	@Override
	public void prepareToExit()
	{
		Command removalCommand = new Command(Commands.REMOVAL);
		removalCommand.setReturnedString(screenManager.getUserObject()
				.getHostName());
		screenManager.getClientForServer()
				.sendMessageToServer(removalCommand);
		screenManager.getClientForServer()
				.sendMessageToServer(new Command(Commands.LOGOUT));
		
		chatScreenLayeredPane.getChatMessageEnterBox().setText("");
		chatScreenLayeredPane.getChatMessagesReceivedBox().setText("");
		screenManager.getUserObject().setConnected(false);
	}
	
	@Override
	public void prepareToEnter()
	{
		// All components are already set to blank and do not need to be loaded
		// from the userobject
	}
	
	@Override
	public void reset()
	{
		chatScreenLayeredPane.getChatMessageEnterBox().setText("");
		chatScreenLayeredPane.getChatMessagesReceivedBox().setText("");
	}
	
	@Override
	public void disable()
	{
	}
	
	@Override
	public void enable()
	{
	}
	
	public void append(String message)
	{
		chatScreenLayeredPane.getChatMessagesReceivedBox().append(message);
	}
	
	public void connectionFailed()
	{
		
	}
	
	public UserObject getUserObject()
	{
		return screenManager.getUserObject();
	}
	
	private class SizeAdapter extends ComponentAdapter
	{
		@Override
		public void componentResized(ComponentEvent event)
		{
			System.out.println("resizing");
			JPanel newPanel = ((JPanel) event.getComponent());
			
			chatScreenLayeredPane.getEnterButton().setBounds(0, 0,
					newPanel.getWidth(), newPanel.getHeight());
			chatScreenLayeredPane.getEnterButton().setScales(
					newPanel.getWidth(), newPanel.getHeight());
			
			chatScreenLayeredPane.getChatMessageEnterBox().setBounds(0, 0,
					newPanel.getWidth(), newPanel.getHeight());
			chatScreenLayeredPane.getChatMessageEnterBox().setScales(
					newPanel.getWidth(), newPanel.getHeight());
			
			Dimension oldImagesDimensions = (chatScreenLayeredPane
					.getChatMessageEnterBox().getImageDimensions());
			
			Dimension chosenImageDimensions = (chatScreenLayeredPane
					.getChatMessagesReceivedBox().getImageDimensions());
			
			double newXScale = newPanel.getWidth()
					/ oldImagesDimensions.getWidth();
			double newYScale = newPanel.getHeight()
					/ oldImagesDimensions.getHeight();
			
			chatScreenLayeredPane.getChatMessagesReceivedBox().setBounds(
					(int) (490 * newXScale), (int) (40 * newYScale),
					(int) (chosenImageDimensions.getWidth() * newXScale),
					(int) (chosenImageDimensions.getHeight() * newYScale));
			
		}
	}
	
}
