package edu.asu.ser.hanasu.server;

import java.io.*;
import java.net.InetAddress;

public class EncryptedMessage implements Serializable
{
	// Generated serial id
	private static final long serialVersionUID = 9171419644778126526L;
	
	static final int CLIENTSCONNECTED = 0, MESSAGE = 1, LOGOUT = 2;
	private int type;
	
	// IP to send to via server send method
	private InetAddress sendTo;
	
	// Encrypted Message
	private byte[] message;
	
	// constructor
	EncryptedMessage(int type, byte[] message, InetAddress sendTo)
	{
		this.type = type;
		this.message = message;
		this.sendTo = sendTo;
		
	}
	
	public InetAddress getSendTo()
	{
		return sendTo;
	}
	
	public int getType()
	{
		return type;
	}
	
	public byte[] getMessage()
	{
		return message;
	}
}
