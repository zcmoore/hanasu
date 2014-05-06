package edu.asu.ser.hanasu;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SaveFile implements Serializable
{
	String userName;
	ArrayList<String> channelNames;
	ArrayList<String> channelPasswords;
	ArrayList<String> serverIPs;
	ArrayList<Integer> serverPorts;
	
	public SaveFile()
	{
		channelNames = new ArrayList<>();
		channelPasswords = new ArrayList<>();
		serverIPs = new ArrayList<>();
		serverPorts = new ArrayList<>();
	}
	
	public String getUserName()
	{
		return userName;
	}
	public ArrayList<String> getChannelNames()
	{
		return channelNames;
	}
	public ArrayList<String> getChannelPasswords()
	{
		return channelPasswords;
	}
	public ArrayList<String> getServerIPs()
	{
		return serverIPs;
	}
	
	public ArrayList<Integer> getServerPorts()
	{
		return serverPorts;
	}

	public void setServerPorts(ArrayList<Integer> serverPorts)
	{
		this.serverPorts = serverPorts;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public void setChannelNames(ArrayList<String> channelNames)
	{
		this.channelNames = channelNames;
	}
	public void setChannelPasswords(ArrayList<String> channelPasswords)
	{
		this.channelPasswords = channelPasswords;
	}
	public void setServerIPs(ArrayList<String> serverIPs)
	{
		this.serverIPs = serverIPs;
	}
	
	
	
}
