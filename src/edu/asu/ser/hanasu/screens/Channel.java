package edu.asu.ser.hanasu.screens;

public abstract class Channel
{
	protected String name;
	protected String password;
	
	public Channel()
	{
		name = null;
		password = null;
	}
	
	public boolean connect()
	{
		return true;
	}
	
	public void disconnect()
	{
		
	}
	
}
