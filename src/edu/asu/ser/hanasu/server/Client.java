package edu.asu.ser.hanasu.server;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import edu.asu.ser.hanasu.server.Command.Commands;

public class Client
{
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private SimpleDateFormat simpleDateFormat;
	private Socket clientSocket;
	
	private ClientGUI clientGUI;
	
	private String serverAddress, username;
	private int portNumber;
	
	public Client(String server, int port, String username, ClientGUI cg)
	{
		this.serverAddress = server;
		this.portNumber = port;
		this.username = username;
		this.clientGUI = cg;
		simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
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
			sendChannelRequest(clientGUI.getChannelName());
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
	
	boolean sendMessageToServer(Object messageToSend)
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
				byte[] messageWUserName = (username + ": "
						+ new String(unencryptedMessage.getMessage()) + "\n")
						.getBytes("UTF-8");
				// TODO Encryption call HERE on messageWUserName
				byte[] encryptedMessage = messageWUserName;
				
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
						// TODO decrypt call
						byte[] unencryptedMessage = incomingMessage
								.getMessage();
						clientGUI.append(simpleDateFormat.format(new Date())
								+ " " + new String(unencryptedMessage));
					}
				}
				catch (IOException ioException)
				{
					displayNonMessage("Server has close the connection: "
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
