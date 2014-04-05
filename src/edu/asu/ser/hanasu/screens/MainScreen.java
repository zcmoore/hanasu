package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@SuppressWarnings("serial")
public class MainScreen extends Screen
{
	private JTextField nickNameField;
	private ScreenManager screenManager;
	private String nickname;
	
	public MainScreen(Sidebar sidebar, Image backgroundImage)
	{
		super(sidebar, backgroundImage);
		setTextFieldProperties();
		this.accessiblePane.add(nickNameField);
		accessiblePane.setBackground(Color.RED);
	}

	private void setTextFieldProperties()
	{
		nickNameField = new JTextField("nickname");
		nickNameField.setBorder(BorderFactory.createEmptyBorder());
		nickNameField.setForeground(Color.WHITE);
		nickNameField.setOpaque(false);
		nickNameField.addActionListener(new TextFieldActionAndFocus());
	}

	@Override
	public void prepareToExit()
	{
		//write to file
		
		throw new NotImplementedException();
	}

	@Override
	public void prepareToEnter()
	{
		//call screen manager object
		//if can load file
		reset();
		//else populate nickname
		throw new NotImplementedException();
	}

	@Override
	public void reset()
	{
		nickNameField.setText("nickname");
		throw new NotImplementedException();
	}

	@Override
	public void disable()
	{
		throw new NotImplementedException();
	}

	@Override
	public void enable()
	{
		throw new NotImplementedException();
	}
	
	private void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
	
	private String getNickname()
	{
		return this.nickname;
	}
	
	public class TextFieldActionAndFocus implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			setNickname(nickNameField.getText().trim());
			
			System.out.println("Enter: " + nickNameField.getText());
		}

		
	}
	
	
	
}
