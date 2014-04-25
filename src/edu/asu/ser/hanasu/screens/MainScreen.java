package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@SuppressWarnings("serial")
public class MainScreen extends Screen
{
	private JTextField nickNameField;
	private JLabel textFieldVericationIcon;
	private ScreenManager screenManager;
	private String nickName;
	
	public MainScreen(Sidebar sidebar, Image backgroundImage, ScreenManager screenManager)
	{
		super(sidebar, backgroundImage, screenManager);

		accessiblePane.setBackground(Color.BLUE);
		accessiblePane.setOpaque(false);
		accessiblePane.setLayout(new FlowLayout(2, 10, 5));
		
		setTextFieldProperties();
		accessiblePane
				.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		accessiblePane.add(textFieldVericationIcon);
		accessiblePane.add(nickNameField);
		
		this.screenManager = screenManager;
	}
	
	private void setTextFieldProperties()
	{
		nickNameField = new JTextField("nickname");
		nickNameField.setBorder(BorderFactory.createEmptyBorder());
		nickNameField.setForeground(Color.WHITE);
		nickNameField.setOpaque(false);
		nickNameField.addActionListener(new TextFieldActionAndFocus());
		textFieldVericationIcon = new JLabel();
	}
	
	@Override
	public void prepareToExit()
	{
		screenManager.getUserObject().setNickName(nickName);
		textFieldVericationIcon.setIcon(null);
	}
	
	@Override
	public void prepareToEnter()
	{
		nickName = screenManager.getUserObject().getNickName();
		nickNameField.setText(screenManager.getUserObject().getNickName());
	}
	
	@Override
	public void reset()
	{
		nickNameField.setText(screenManager.getUserObject().getNickName());
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
	
	public class TextFieldActionAndFocus implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			nickName = nickNameField.getText().trim();
			screenManager.getUserObject().setNickName(nickName);
			textFieldVericationIcon.setIcon(new ImageIcon(
					"src/Images/GreenCheck.png"));
			
			System.out.println("Enter Key: " + nickNameField.getText());
		}
	}
	
}
