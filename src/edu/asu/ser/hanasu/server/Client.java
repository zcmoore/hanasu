package edu.asu.ser.hanasu.server;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import edu.asu.ser.hanasu.encryption.AESBlockType;
import edu.asu.ser.hanasu.encryption.AESCryptographer;
import edu.asu.ser.hanasu.encryption.InvalidBlockSizeException;
import edu.asu.ser.hanasu.screens.ChatScreen;
import edu.asu.ser.hanasu.server.Command.Commands;

public class Client
{
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private SimpleDateFormat simpleDateFormat;
	private Socket clientSocket;
	
	private ChatScreen clientGUI;
	
	private String serverAddress, username;
	private int portNumber;
	private AESCryptographer cryptographer;
	
	public Client(String server, int port, String username, ChatScreen cg,
			byte[] key)
	{
		this.serverAddress = server;
		this.portNumber = port;
		this.username = username;
		this.clientGUI = cg;
		simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		try
		{
			cryptographer = new AESCryptographer(AESBlockType.BIT_128, key);
		}
		catch (InvalidBlockSizeException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean start()
	{
		try
		{
			clientSocket = new Socket(serverAddress, portNumber);
		}
		catch (Exception exception)
		{
			displayNonMessage("Error connectiong to server:" + exception);
			return false;
		}
		String msg = "Connection accepted " + clientSocket.getInetAddress()
				+ ":" + clientSocket.getPort();
		displayNonMessage(msg);
		
		try
		{
			objectInputStream = new ObjectInputStream(
					clientSocket.getInputStream());
			objectOutputStream = new ObjectOutputStream(
					clientSocket.getOutputStream());
		}
		catch (IOException ioException)
		{
			System.err.println("Stuff wrong here.");
			displayNonMessage("IOException creating new Input/output Streams: "
					+ ioException);
			return false;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			return false;
		}
		
		new ListenFromServer().start();
		
		try
		{
			sendChannelRequest(clientGUI.getUserObject().getHostName());
		}
		catch (IOException ioException)
		{
			displayNonMessage("IOException " + ioException);
			return false;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private void sendChannelRequest(String channelName) throws IOException
	{
		Command channelRequestCommand = new Command(Commands.CHANNEL_REQUEST);
		channelRequestCommand.setReturnedString(channelName);
		objectOutputStream.writeObject(channelRequestCommand);
	}
	
	private void displayNonMessage(String msg)
	{
		clientGUI.append(msg + "\n");
	}
	
	public boolean sendMessageToServer(Object messageToSend)
	{
		try
		{
			if (messageToSend instanceof Command)
			{
				objectOutputStream.writeObject(messageToSend);
			}
			else if (messageToSend instanceof EncryptedMessage)
			{
				// Puts username on message
				EncryptedMessage unencryptedMessage = (EncryptedMessage) messageToSend;
				
				byte[] messageWUserName = (username + ": " + new String(
						unencryptedMessage.getMessage()) + "\n").getBytes("UTF-8");
				
				byte[] encryptedMessage = cryptographer
						.encrypt(messageWUserName);
				
				if (unencryptedMessage.getIDOrString() instanceof String)
				{
					EncryptedMessage encryptedMessageToSend = new EncryptedMessage(
							encryptedMessage,
							unencryptedMessage.getIDOrString());
					objectOutputStream.writeObject(encryptedMessageToSend);
				}
				if (unencryptedMessage.getIDOrString() instanceof Integer)
				{
					throw new Exception("Clients dont send ID's.");
				}
			}
		}
		catch (IOException ioException)
		{
			displayNonMessage("Exception writing to server: " + ioException);
			return false;
		}
		catch (Exception exception)
		{
			displayNonMessage(exception.toString());
			return false;
		}
		
		return true;
	}
	
	private void disconnect()
	{
		try
		{
			if (objectInputStream != null)
				objectInputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			if (objectOutputStream != null)
				objectOutputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			if (clientSocket != null)
				clientSocket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (clientGUI != null)
			clientGUI.connectionFailed();
		
	}
	
	class ListenFromServer extends Thread
	{
		
		public void run()
		{
			while (true)
			{
				try
				{
					Object message = objectInputStream.readObject();
					if (message instanceof Command)
					{
						Command incomingCommand = (Command) message;
						clientGUI.append(simpleDateFormat.format(new Date())
								+ " " + incomingCommand.getReturnedString());
					}
					else if (message instanceof EncryptedMessage)
					{
						EncryptedMessage incomingMessage = (EncryptedMessage) message;
						
						byte[] unencryptedMessage = cryptographer.decrypt(
								incomingMessage.getMessage()).getBytes();
						
						clientGUI.append(simpleDateFormat.format(new Date())
								+ " " + new String(unencryptedMessage));
					}
				}
				catch (IOException ioException)
				{
					displayNonMessage("Server has closed the connection: "
							+ ioException);
					clientGUI.connectionFailed();
					break;
				}
				catch (ClassNotFoundException classNotFoundException)
				{
					classNotFoundException.printStackTrace();
					break;
				}
				catch (Exception exception)
				{
					exception.printStackTrace();
					break;
				}
			}
		}
	}
}
