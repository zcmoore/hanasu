package edu.asu.ser.hanasu.server;

import java.io.*;

public class EncryptedMessage implements Serializable
{
	//Generated serial id
	private static final long serialVersionUID = 9171419644778126526L;
	static final int CLIENTSCONNECTED = 0, MESSAGE = 1, LOGOUT = 2;
	private int type;
	private byte[] message;
	
	// constructor
	EncryptedMessage(int type, byte[] message)
	{
		this.type = type;
		this.message = message;
	}
	
	// getters
	public int getType()
	{
		return type;
	}
	
	public byte[] getMessage()
	{
		return message;
	}
}
