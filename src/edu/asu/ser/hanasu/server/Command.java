package edu.asu.ser.hanasu.server;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Command implements Serializable
{
	public static enum Commands {CLIENTS_CONNECTED, LOGOUT, CHANNEL, CHANNEL_REQUEST, REMOVAL};
	private Commands command;
	private String returnedString;
	
	public Command(Commands command)
	{
		this.command = command;
	}
	
	public Commands getCommand()
	{
		return command;
	}

	public String getReturnedString()
	{
		return returnedString;
	}

	public void setReturnedString(String returnedString)
	{
		this.returnedString = returnedString;
	}
	

}
