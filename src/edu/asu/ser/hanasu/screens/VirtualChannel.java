package edu.asu.ser.hanasu.screens;

public class VirtualChannel extends Channel
{
	private boolean local;
	
	public VirtualChannel()
	{
		local = false;
	}
	
	public boolean connect(ScreenManager screenManager)
	{
		String severIP = "localhost";
		int serverPort = 443;

		if(screenManager.createAndConnectServer(this.name, serverPort, this.password, screenManager.getUserObject().getNickName()))
			return true;
		else
			return true;
	}
	
	public void disconnect()
	{
		//ScreenManager.getUserObject().getClientForServer().disconnect();
	}
	
	public boolean isLocal()
	{
		return local;
	}
	
	public boolean isSet()
	{
		if((this.name != null) && (this.password != null))
			return true;
		else
			return false;
	}
	
	public void setChannel(String name, String password)
	{
		//For default debug purposes
		if(name.equalsIgnoreCase("") || name.equalsIgnoreCase(" "))
			name = "Channel X"; 
		if(password.equalsIgnoreCase("") || password.equalsIgnoreCase(" "))
			password = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		this.name = name;
		this.password = password;
	}
	
}
