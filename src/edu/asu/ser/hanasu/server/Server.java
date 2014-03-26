package edu.asu.ser.hanasu.server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server
{
	private static int uniqueId;
	private ArrayList<ClientThread> clientList;
	private NewServerGUI serverGUI;
	private int portNumber;
	private boolean keepGoing;
	
	public Server(int port, NewServerGUI sg)
	{
		this.serverGUI = sg;
		this.portNumber = port;
		clientList = new ArrayList<ClientThread>();
	}
	
	public void start()
	{
		keepGoing = true;
		try
		{
			ServerSocket serverSocket = new ServerSocket(portNumber);
			
			while (keepGoing)
			{
				displayMessageOnGUI("Server waiting for Clients on port "
						+ portNumber + ".");
				
				Socket socket = serverSocket.accept();
				
				if (!keepGoing)
					break;
				
				ClientThread clientThread = new ClientThread(socket);
				clientList.add(clientThread);
				clientThread.start();
			}
			try
			{
				serverSocket.close();
				for (ClientThread clientThreadVar : clientList)
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
				}
			}
			catch (Exception e)
			{
				displayMessageOnGUI("Exception closing the server and clients: "
						+ e);
			}
		}
		catch (IOException e)
		{
			String messageTemp = "IOException on new ServerSocket: " + e + "\n";
			displayMessageOnGUI(messageTemp);
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
	
	private synchronized void outputMessageToClient(EncryptedMessage message)
	{
		String encryptedmessageWUserName = new String(message.getMessage());
		// Implies encryption is turned off
		if (serverGUI.getDebuggingStatus())
		{
			// Username is attached in run() method
			serverGUI.writeToDebugTextArea(encryptedmessageWUserName);
			
		}
		
		// decrementing loop in case of client disconnect
		for (int index = clientList.size(); --index >= 0;)
		{
			ClientThread clientThreadVar = clientList.get(index);
			
			if(clientThreadVar.getClientSocket().getInetAddress().getHostAddress() == message.getSendTo().getHostAddress())
				if (!clientThreadVar.writeMessage(encryptedmessageWUserName))
				{
					clientList.remove(index);
					displayMessageOnGUI("Disconnected Client "
							+ clientThreadVar.username + " removed from list.");
				}
			
		}
	}
	
	synchronized void remove(int id)
	{
		for (ClientThread clientThreadVar : clientList)
		{
			if (clientThreadVar.clientUniqueID == id)
			{
				clientList.remove(clientThreadVar);
				return;
			}
		}
	}
	
	class ClientThread extends Thread
	{
		Socket clientSocket;
		ObjectInputStream objectInputStream;
		ObjectOutputStream objectOutputStream;
		int clientUniqueID;
		String username;
		EncryptedMessage chatMessage;
		String date;
		
		public ClientThread(Socket socketParam)
		{
			clientUniqueID = ++uniqueId;
			this.clientSocket = socketParam;
			
			System.out
					.println("Thread trying to create Object Input/Output Streams");
			try
			{
				objectOutputStream = new ObjectOutputStream(
						clientSocket.getOutputStream());
				objectInputStream = new ObjectInputStream(
						clientSocket.getInputStream());
				username = (String) objectInputStream.readObject();
				displayMessageOnGUI(username + " just connected.");
			}
			catch (IOException e)
			{
				displayMessageOnGUI("IOException creating new Input/output Streams: "
						+ e);
				return;
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			date = new Date().toString() + "\n";
		}
		
		public void run()
		{
			// to loop until LOGOUT
			boolean keepGoing = true;
			while (keepGoing)
			{
				try
				{
					chatMessage = (EncryptedMessage) objectInputStream
							.readObject();
					
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
				
				if (chatMessage != null)
				{
					String message = new String(username + ": " + new String(chatMessage.getMessage()) + ".\n");
					
					switch (chatMessage.getType())
					{
					
						case EncryptedMessage.MESSAGE:
							outputMessageToClient(
									new EncryptedMessage(
											EncryptedMessage.MESSAGE,
											message.getBytes(),
											chatMessage.getSendTo()));
							break;
						case EncryptedMessage.LOGOUT:
							displayMessageOnGUI(username
									+ " disconnected with a LOGOUT message.");
							keepGoing = false;
							break;
						case EncryptedMessage.CLIENTSCONNECTED:
							writeMessage("List of the users connected at "
									+ new SimpleDateFormat().format(new Date())
									+ "\n");
							
							for (int index = 0; index < clientList.size(); ++index)
							{
								ClientThread ct = clientList.get(index);
								writeMessage((index + 1) + ") " + ct.username
										+ " since " + ct.date);
							}
							break;
					}
				}
			}
			remove(clientUniqueID);
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
		
		// What actually sends messages to clients
		private boolean writeMessage(String message)
		{
			if (!clientSocket.isConnected())
			{
				close();
				return false;
			}
			
			try
			{
				objectOutputStream.writeObject(new EncryptedMessage(
						EncryptedMessage.MESSAGE, message.getBytes(),
						InetAddress.getByName("")));
				
			}
			catch (IOException e)
			{
				displayMessageOnGUI("Error sending message to " + username);
				displayMessageOnGUI(e.toString());
			}
			
			return true;
		}
		
		private Socket getClientSocket()
		{
			return clientSocket;
		}
	}
}
