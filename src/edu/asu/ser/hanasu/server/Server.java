package edu.asu.ser.hanasu.server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import edu.asu.ser.hanasu.server.Command.Commands;

public class Server
{
	// TODO replace with hash function
	private static int uniqueId;
	
	private HashMap<Integer, ClientThread> allClientIDMap;
	private HashMap<String, ClientThread> channelsMap;
	
	private NewServerGUI serverGUI;
	private int portNumber;
	private boolean keepGoing;
	private SimpleDateFormat simpleDateFormat;
	
	public Server(int portNumber, NewServerGUI serverGUI)
	{
		this.serverGUI = serverGUI;
		this.portNumber = portNumber;
		allClientIDMap = new HashMap<Integer, ClientThread>();
		channelsMap = new HashMap<String, ClientThread>();
		simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		
	}
	
	public void start()
	{
		keepGoing = true;
		try
		{
			ServerSocket serverSocket = new ServerSocket(portNumber);
			
			while (keepGoing)
			{
				displayMessageOnGUI("Server waiting for Channels on port "
						+ portNumber + ".");
				
				Socket socket = serverSocket.accept();
				
				if (!keepGoing)
					break;
				
				ClientThread clientThread = new ClientThread(socket);
				clientThread.start();
				allClientIDMap.put(clientThread.uniqueClientID, clientThread);
			}
			try
			{
				serverSocket.close();
				for (ClientThread clientThreadVar : allClientIDMap.values())
				{
					try
					{
						clientThreadVar.objectInputStream.close();
						clientThreadVar.objectOutputStream.close();
						clientThreadVar.clientSocket.close();
					}
					catch (IOException ioException)
					{
						ioException.printStackTrace();
					}
					catch (Exception exception)
					{
						exception.printStackTrace();
					}
				}
			}
			catch (Exception e)
			{
				displayMessageOnGUI("Exception closing the server and channels: "
						+ e);
			}
		}
		catch (ClassCastException exception)
		{
			exception.printStackTrace();
		}
		catch (IOException e)
		{
			String messageTemp = "IOException on new ServerSocket: " + e + "\n";
			displayMessageOnGUI(messageTemp);
		}
		catch (Exception exception)
		{
			displayMessageOnGUI(exception.toString());
		}
	}
	
	protected void stop()
	{
		keepGoing = false;
		try
		{
			new Socket("localhost", portNumber);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void displayMessageOnGUI(String string)
	{
		serverGUI.writeToEventTextArea(string + "\n");
	}
	
	synchronized void remove(int id)
	{
		for (ClientThread clientThreadVar : allClientIDMap.values())
		{
			if (clientThreadVar.uniqueClientID == id)
			{
				allClientIDMap.remove(clientThreadVar);
				return;
			}
		}
	}
	
	class ClientThread extends Thread
	{
		Socket clientSocket;
		ObjectInputStream objectInputStream;
		ObjectOutputStream objectOutputStream;
		int uniqueClientID;
		String username;
		Object chatMessage;
		String date;
		
		public ClientThread(Socket socketParam) throws Exception
		{
			uniqueClientID = ++uniqueId;
			this.clientSocket = socketParam;
			
			System.out
					.println("Thread trying to create Object Input/Output Streams");
			try
			{
				objectOutputStream = new ObjectOutputStream(
						clientSocket.getOutputStream());
				objectInputStream = new ObjectInputStream(
						clientSocket.getInputStream());
				Object incomingMessage = objectInputStream.readObject();
				username = "" + uniqueClientID;
				if (incomingMessage instanceof Command)
				{
					switch (((Command) incomingMessage).getCommand())
					{
						case CHANNEL:
							String channelNameString = ((Command) incomingMessage)
									.getReturnedString().toLowerCase();
							if (!(channelsMap.containsKey(channelNameString)))
								channelsMap.put(channelNameString, this);
							break;
						case CHANNEL_REQUEST:
							if (channelsMap
									.containsKey(((Command) incomingMessage)
											.getReturnedString().toLowerCase()))
							{
								ClientThread channelThread = channelsMap
										.get(((Command) incomingMessage)
												.getReturnedString()
												.toLowerCase());
								byte[] message = null;
								EncryptedMessage channelRequest = new EncryptedMessage(
										message, uniqueClientID);
								channelThread.objectOutputStream
										.writeObject(channelRequest);
							}
							break;
						default:
							break;
					}
				}
				else
				{
					throw new Exception("Dumb Shit");
				}
				displayMessageOnGUI(username + " just connected.");
			}
			catch (IOException ioException)
			{
				displayMessageOnGUI("IOException creating new Input/output Streams: "
						+ ioException);
				return;
			}
			date = new Date().toString() + "\n";
		}
		
		public void run()
		{
			// to loop until LOGOUT message
			boolean keepGoing = true;
			
			while (keepGoing)
			{
				try
				{
					chatMessage = objectInputStream.readObject();
				}
				catch (IOException ioException)
				{
					displayMessageOnGUI(username
							+ " Exception reading Streams: " + ioException);
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
					System.err.println("You done goofed. "
							+ exception.toString());
					break;
				}
				
				if (chatMessage instanceof Command)
				{
					Command incomingCommand = (Command) chatMessage;
					switch (incomingCommand.getCommand())
					{
						case LOGOUT:
							displayMessageOnGUI(username
									+ "Disconnected with a Logout Message");
							keepGoing = false;
							Command commandToSend = new Command(Commands.LOGOUT);
							commandToSend.setReturnedString("Logged Out");
							try
							{
								objectOutputStream.writeObject(commandToSend);
							}
							catch (IOException ioException)
							{
								ioException.printStackTrace();
							}
							break;
						case REMOVAL:
							if (channelsMap.containsKey(incomingCommand
									.getReturnedString()))
							{
								Command removalCommand = new Command(
										Commands.REMOVAL);
								removalCommand.setReturnedString(Integer
										.toString(uniqueClientID));
								
								ClientThread clientThread = channelsMap
										.get(incomingCommand
												.getReturnedString());
								try
								{
									clientThread.objectOutputStream.writeObject(removalCommand);
								}
								catch (IOException ioException)
								{
									ioException.printStackTrace();
								}
							}
							
							break;
						default:
							break;
					}
				}
				else if (chatMessage instanceof EncryptedMessage)
				{
					if (((EncryptedMessage) chatMessage).getIDOrString() instanceof String)
					{
						
						try
						{
							// loop up channel in map
							String channelName = ((String) ((EncryptedMessage) chatMessage)
									.getIDOrString()).toLowerCase();
							
							if (channelsMap.containsKey(channelName))
							{
								ClientThread clientThread = channelsMap
										.get(channelName);
								clientThread.objectOutputStream
										.writeObject(new EncryptedMessage(
												((EncryptedMessage) chatMessage)
														.getMessage(),
												this.uniqueClientID));
								if (serverGUI.getDebuggingStatus())
								{
									serverGUI.writeToDebugTextArea(new String(
											((EncryptedMessage) chatMessage)
													.getMessage()));
								}
							}
							else
							{
								return;
							}
						}
						catch (IOException ioException)
						{
							ioException.printStackTrace();
							break;
						}
						catch (Exception exception)
						{
							exception.printStackTrace();
							System.err.println("You done goofed "
									+ exception.toString());
							break;
						}
					}
					else if (((EncryptedMessage) chatMessage).getIDOrString() instanceof Integer)
					{
						try
						{
							// look up client in maps
							if (allClientIDMap
									.containsKey(((EncryptedMessage) chatMessage)
											.getIDOrString()))
							{
								ClientThread clientThread = allClientIDMap
										.get(((EncryptedMessage) chatMessage)
												.getIDOrString());
								clientThread.objectOutputStream
										.writeObject(chatMessage);
							}
							
						}
						catch (IOException ioException)
						{
							ioException.printStackTrace();
							break;
						}
						catch (Exception exception)
						{
							System.err.println("You done goofed "
									+ exception.toString());
							break;
						}
					}
				}
			}
			remove(uniqueClientID);
			close();
		}
		
		private void close()
		{
			try
			{
				if (objectOutputStream != null)
					objectOutputStream.close();
			}
			catch (Exception exception)
			{
				displayMessageOnGUI(exception.toString());
			}
			try
			{
				if (objectInputStream != null)
					objectInputStream.close();
			}
			catch (Exception exception)
			{
				displayMessageOnGUI(exception.toString());
			};
			try
			{
				if (clientSocket != null)
					clientSocket.close();
			}
			catch (Exception exception)
			{
				displayMessageOnGUI(exception.toString());
			}
		}
	}
}
