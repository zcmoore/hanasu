package edu.asu.ser.hanasu.server;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EncryptedMessage implements Serializable
{
	
	private byte[] encryptedMessage;
	
	//Object to be a string for channel name
	//or int for ID
	private Object stringOrID;
	
	
	public EncryptedMessage(byte[] message, Object id)
	{
		this.encryptedMessage = message;
		this.stringOrID = id;
	}
	
	public byte[] getMessage()
	{
		return encryptedMessage;
	}
	
	public Object getIDOrString()
	{
		return stringOrID;
	}
	
}
