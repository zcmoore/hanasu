package edu.asu.ser.hanasu.screens;

public abstract class Channel
{
	protected String name;
	protected String password;
	
	public Channel()
	{
		name = "";
		password = "";
	}
	
	public boolean connect()
	{
		return true;
	}
	
	public void disconnect()
	{
		
	}

	public String getName()
	{
		return name;
	}

	public String getPassword()
	{
		return password;
	}
	
	
	
}
