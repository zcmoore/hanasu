package edu.asu.ser.hanasu.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.asu.ser.hanasu.server.Command.Commands;

public class Channel
{
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private Socket clientSocket;
	private ChannelGUI channelGUI;
	private ArrayList<Integer> clientIDs;
	
	private String serverAddress;
	private String channelName;
	private int portNumber;
	
	public Channel(String serverAddress, int portNumber, ChannelGUI channelGUI,
			String channelName)
	{
		this.serverAddress = serverAddress;
		this.portNumber = portNumber;
		this.channelGUI = channelGUI;
		this.channelName = channelName;
		this.clientIDs = new ArrayList<Integer>();
	}
	
	private void displayNonMessage(String msg)
	{
		channelGUI.append(msg + "\n");
	}
	
	public boolean start()
	{
		try
		{
			clientSocket = new Socket(serverAddress, portNumber);
		}
		catch (Exception exception)
		{
			System.out.println("Got an exception with socket");
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
			sendChannelRegister(channelName);
		}
		catch (IOException ioException)
		{
			channelGUI.append("IOException");
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
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
		
		if (channelGUI != null)
			channelGUI.connectionFailed();
		
	}
	
	private void sendChannelRegister(String channelName) throws IOException
	{
		Command addToMap = new Command(Commands.CHANNEL);
		addToMap.setReturnedString(channelName);
		objectOutputStream.writeObject(addToMap);
	}
	
	boolean sendMessageToServer(Object messageToSend)
	{
		try
		{
			if (messageToSend instanceof Command)
			{
				objectOutputStream.writeObject(messageToSend);
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
	
	class ListenFromServer extends Thread
	{
		
		public void run()
		{
			while (true)
			{
				try
				{
					Object message = objectInputStream.readObject();
					
					if (message instanceof EncryptedMessage)
					{
						EncryptedMessage incomingMessage = (EncryptedMessage) message;
						if (incomingMessage.getIDOrString() instanceof Integer)
						{
							if (!(incomingMessage.getMessage() == null))
							{
								for (Integer clientID : clientIDs)
								{
									objectOutputStream
											.writeObject(new EncryptedMessage(
													incomingMessage
															.getMessage(),
													clientID));
								}
							}
							else if (!(clientIDs.contains(incomingMessage
									.getIDOrString())))
							{
								clientIDs.add((Integer) incomingMessage
										.getIDOrString());
							}
						}
						else if (incomingMessage.getIDOrString() instanceof String)
						{
							throw new Exception("You done goofed.");
						}
					}
					else if (message instanceof Command)
					{
						switch (((Command) message).getCommand())
						{
							case REMOVAL:
								int intToRemove = Integer
										.parseInt(((Command) message)
												.getReturnedString());
								System.out.println("Remove " + intToRemove);
								if(clientIDs.contains(intToRemove))
								{
									clientIDs.remove(intToRemove);
									System.out.println("Removed " + intToRemove);
								}
								
								break;
							default:
								Command incomingCommand = (Command) message;
								channelGUI.append(incomingCommand
										.getReturnedString());
								break;
						}
					}
				}
				catch (IOException ioException)
				{
					displayNonMessage("Server has close the connection: "
							+ ioException);
					disconnect();
					channelGUI.connectionFailed();
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
			disconnect();
		}
	}
}
