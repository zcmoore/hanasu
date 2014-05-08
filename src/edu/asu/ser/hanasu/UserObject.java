package edu.asu.ser.hanasu;

import java.util.ArrayList;

import edu.asu.ser.hanasu.screens.components.KanaStroke;

public class UserObject
{
	private String userNickName;
	private String channelName;
	private String channelPassword;
	// TODO: remove strokes tracking
	private ArrayList<KanaStroke> strokesArray;
	private boolean isConnected;
	
	// TODO add the 3 favorite channels
	
	public UserObject(ArrayList<KanaStroke> strokesArray)
	{
		this.userNickName = "nickname";
		this.channelName = "Channel X";
		this.channelPassword = "ABCDEFGHIJKLMNOP";
		
		// TODO: replace with more elegant solution
		this.strokesArray = strokesArray;
		this.isConnected = false;
		System.out.println("New userobject created");
	}
	
	public String getNickName()
	{
		return userNickName;
	}
	
	public String getHostName()
	{
		return channelName;
	}
	
	public String getChannelPassword()
	{
		return channelPassword;
	}
	
	public ArrayList<KanaStroke> getStrokesArray()
	{
		return strokesArray;
	}
	
	public boolean isConnected()
	{
		return isConnected;
	}
	
	public void setNickName(String nickName)
	{
		this.userNickName = nickName;
	}
	
	public void setHostName(String hostName)
	{
		this.channelName = hostName;
	}
	
	public void setChannelPassword(String channelPassword)
	{
		this.channelPassword = channelPassword;
	}
	
	public void setStrokesArray(ArrayList<KanaStroke> strokesArray)
	{
		this.strokesArray = strokesArray;
	}
	
	public void setConnected(boolean isConnected)
	{
		this.isConnected = isConnected;
	}
}
