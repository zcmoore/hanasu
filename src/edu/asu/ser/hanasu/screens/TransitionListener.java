package edu.asu.ser.hanasu.screens;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.asu.ser.hanasu.screens.ScreenManager.ScreenType;

public class TransitionListener implements ActionListener
{
	private ScreenType transitionTarget;
	
	public TransitionListener(ScreenType transitionTarget)
	{
		this.transitionTarget = transitionTarget;
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		ScreenManager.transition(transitionTarget);
	}
	
}
