package edu.asu.ser.hanasu.server;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client
{
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private Socket clientSocket;
	
	private ClientGUI clientGUI;
	
	private String serverAddress, username;
	private int portNumber;
	
	public Client(String server, int port, String username)
	{
		this(server, port, username, null);
	}
	
	public Client(String server, int port, String username, ClientGUI cg)
	{
		this.serverAddress = server;
		this.portNumber = port;
		this.username = username;
		this.clientGUI = cg;
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
			displayNonMessage("IOException creating new Input/output Streams: "
					+ ioException);
			return false;
		}
		
		new ListenFromServer().start();
		try
		{
			objectOutputStream.writeObject(username);
		}
		catch (IOException ioException)
		{
			displayNonMessage("Exception doing login : " + ioException);
			disconnect();
			return false;
		}
		return true;
	}
	
	private void displayNonMessage(String msg)
	{
		if (clientGUI == null)
			System.out.println(msg);
		else
			clientGUI.append(msg + "\n");
	}
	
	boolean sendMessageToServer(EncryptedMessage msg)
	{
		try
		{
			objectOutputStream.writeObject(msg);
		}
		catch (IOException ioException)
		{
			displayNonMessage("Exception writing to server: " + ioException);
			return false;
		}
		catch(Exception exception)
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
	
	public static void main(String[] args) throws UnknownHostException
	{
		int portNumber = 443;
		String serverAddress = "localhost";
		String userName = "Anonymous";
		
		switch (args.length)
		{
			case 3:
				serverAddress = args[2];
			case 2:
				try
				{
					portNumber = Integer.parseInt(args[1]);
				}
				catch (Exception e)
				{
					System.out.println("Invalid port number.");
					System.out
							.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
					return;
				}
			case 1:
				userName = args[0];
			case 0:
				break;
			default:
				System.out
						.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
				return;
		}
		Client client = new Client(serverAddress, portNumber, userName);
		
		if (!client.start())
			return;
		
		Scanner scan = new Scanner(System.in);
		
		while (true)
		{
			System.out.print("> ");
			String message = scan.nextLine();
			byte[] unencryptedMessage = null;
			
			//TODO fix sendTo address
			if (message.equalsIgnoreCase("LOGOUT"))
			{
				client.sendMessageToServer(new EncryptedMessage(
						EncryptedMessage.LOGOUT, unencryptedMessage, InetAddress.getByName("localhost")));
				break;
			}
			else if (message.equalsIgnoreCase("WHOISIN"))
			{
				client.sendMessageToServer(new EncryptedMessage(
						EncryptedMessage.CLIENTSCONNECTED, unencryptedMessage, InetAddress.getByName("localhost")));
			}
			else
			{
				// TODO encrypt call
				byte[] encryptedMessage = unencryptedMessage;
				client.sendMessageToServer(new EncryptedMessage(
						EncryptedMessage.MESSAGE, encryptedMessage, InetAddress.getByName("localhost")));
			}
		}
		client.disconnect();
		scan.close();
	}
	
	class ListenFromServer extends Thread
	{
		
		public void run()
		{
			while (true)
			{
				try
				{
					EncryptedMessage encryptedMessage = (EncryptedMessage) objectInputStream.readObject();
					// TODO decrypt call and attach date/time received
					clientGUI.append(new String(encryptedMessage.getMessage()));
				}
				catch (IOException ioException)
				{
					displayNonMessage("Server has close the connection: "
							+ ioException);
					if (clientGUI != null)
						clientGUI.connectionFailed();
					break;
				}
				catch (ClassNotFoundException classNotFoundException)
				{
					classNotFoundException.printStackTrace();
				}
			}
		}
	}
}
