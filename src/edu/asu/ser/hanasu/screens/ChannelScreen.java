package edu.asu.ser.hanasu.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@SuppressWarnings("serial")
public class ChannelScreen extends Screen
{
	private KanaGroup kanaGroup;
	private ScreenManager screenManager;
	
	public ChannelScreen(Sidebar sidebar, Image backgroundImage, ScreenManager screenManager)
	{
		super(sidebar, backgroundImage, screenManager);
		
		setBackground(Color.RED);
		
		accessiblePane.setLayout(new GridLayout(1,0));
		
		kanaGroup = new KanaGroup(accessiblePane.getWidth(), accessiblePane.getHeight(), screenManager);
		
		kanaGroup.setPreferredSize(new Dimension(accessiblePane.getWidth(), accessiblePane.getHeight()));
		kanaGroup.setBounds(0, 0, accessiblePane.getWidth(), accessiblePane.getHeight());
		kanaGroup.setOpaque(false);
		
		accessiblePane.add(kanaGroup);
		
		accessiblePane.setOpaque(false);
		
		this.screenManager = screenManager;
		
		this.addComponentListener(new SizeAdapter());
	}
	
	public ScreenManager getScreenManager()
	{
		return screenManager;
	}
	
	@Override
	public void prepareToExit()
	{
		screenManager.getUserObject().setStrokesArray(kanaGroup.getStrokeArray());
	}
	
	@Override
	public void prepareToEnter()
	{
		if(screenManager.getUserObject().getStrokesArray() != null)
		{
			System.out.println("object strokes not null");
			kanaGroup.setStrokeArray(screenManager.getUserObject().getStrokesArray());
		}
	}
	
	@Override
	public void reset()
	{
		if(screenManager.getUserObject().getStrokesArray() != null)
		{
			kanaGroup.setStrokeArray(screenManager.getUserObject().getStrokesArray());
		}
		else
		{
			kanaGroup.setStrokeArray(new KanaGroup(accessiblePane.getWidth(), accessiblePane.getHeight(), screenManager).getStrokeArray());
		}
	}
	
	@Override
	public void disable()
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	@Override
	public void enable()
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	public ArrayList<KanaStroke> getKanaStrokes()
	{
		return kanaGroup.getStrokeArray();
	}
	
	private class SizeAdapter extends ComponentAdapter
	{
		@Override
		public void componentResized(ComponentEvent event)
		{
			System.out.println("resizing");
			JPanel newPanel = ((JPanel) event.getComponent());
			
			for(KanaStroke stroke : kanaGroup.getStrokeArray())
			{
				stroke.setBounds(0, 0, newPanel.getWidth(), newPanel.getHeight());
				stroke.setScales(newPanel.getWidth(), newPanel.getHeight());
			}
		}
	}
}
