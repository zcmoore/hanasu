package edu.asu.ser.hanasu.server;

import edu.asu.ser.hanasu.screens.ScreenManager;


public class VirtualChannel
{
    private String name;
    private String password;
	private boolean local;
	
	public VirtualChannel()
	{
		local = false;
	}
	
	public boolean connect(ScreenManager screenManager, String serverIPReal, int serverPort)
	{
		if(password.length() != 16)
			return false;
		if(name.equals(""))
			return false;
		
		if(!(serverIPReal.equals("localhost")))
		{
			local = true;
			screenManager.getUserObject().setHostName(this.name);
			if(screenManager.createAndConnectServer(serverIPReal, serverPort, this.password, screenManager.getUserObject().getNickName()))
			{
				screenManager.getUserObject().setConnected(true);
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			String severIP = "localhost";
			screenManager.getUserObject().setHostName(this.name);
			if(screenManager.createAndConnectServer(severIP, serverPort, this.password, screenManager.getUserObject().getNickName()))
			{
				screenManager.getUserObject().setConnected(true);
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public boolean isLocal()
	{
		return local;
	}
	
	public boolean isSet()
	{
		if((!(this.name.equals(""))) && (!(this.password.equals(""))))
			return true;
		else
			return false;
	}
	
	public void setChannel(String name, String password)
	{
		//For default debug purposes
		if(name.equalsIgnoreCase("") || name.equalsIgnoreCase(" "))
		{
			name = "Channel X"; 
			password = "ABCDEFGHIJKLMNOP";
		}
		
		this.name = name;
		this.password = password;
	}
	
	public void setSaveChannels(String name, String password)
	{
		this.name = name;
		this.password = password;
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
