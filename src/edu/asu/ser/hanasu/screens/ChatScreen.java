package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ChatScreen extends Screen
{
	private JTextArea chatRoom;
	private JTextField messageEnter;
	
	
	public ChatScreen(Sidebar sidebar, Image backgroundImage, ScreenManager screenManager)
	{
		super(sidebar, backgroundImage, screenManager);
		setBackground(Color.RED);
		
		accessiblePane.setLayout(new GridLayout(1,0));
	}
	
	@Override
	public void prepareToExit()
	{
	}
	
	@Override
	public void prepareToEnter()
	{
	}
	
	@Override
	public void reset()
	{
	}
	
	@Override
	public void disable()
	{
	}
	
	@Override
	public void enable()
	{
	}
	
}
