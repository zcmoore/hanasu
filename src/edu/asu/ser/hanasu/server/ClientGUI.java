package edu.asu.ser.hanasu.server;
/*
 * Test purposes only
 */
import javax.swing.*;

import edu.asu.ser.hanasu.server.Command.Commands;

import java.awt.*;
import java.awt.event.*;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SuppressWarnings("serial")
public class ClientGUI extends JFrame implements ActionListener
{
	
	private JLabel label;
	// to hold the Username and later on the messages
	private JTextField tf;
	private JTextField tfServer, tfPort;
	private JButton login, logout, whoIsIn;
	private JTextArea chatRoom;
	private boolean connected;
	private Client client;
	private int defaultPort;
	private String defaultHost;
	private String channelName;
	
	public ClientGUI(String host, int port, String channelName)
	{
		
		super("Chat Client");
		this.defaultPort = port;
		this.defaultHost = host;
		this.channelName = channelName;
		
		// The NorthPanel with:
		JPanel northPanel = new JPanel(new GridLayout(3, 1));
		// the server name and the port number
		JPanel serverAndPort = new JPanel(new GridLayout(1, 5, 1, 3));
		// the two JTextField with default value for server address and port
		// number
		tfServer = new JTextField(host);
		tfPort = new JTextField("" + port);
		tfPort.setHorizontalAlignment(SwingConstants.RIGHT);
		
		serverAndPort.add(new JLabel("Server Address:  "));
		serverAndPort.add(tfServer);
		serverAndPort.add(new JLabel("Port Number:  "));
		serverAndPort.add(tfPort);
		serverAndPort.add(new JLabel(""));
		// adds the Server an port field to the GUI
		northPanel.add(serverAndPort);
		
		// the Label and the TextField
		label = new JLabel("Enter your username below", SwingConstants.CENTER);
		northPanel.add(label);
		tf = new JTextField("Anonymous");
		tf.setBackground(Color.WHITE);
		northPanel.add(tf);
		add(northPanel, BorderLayout.NORTH);
		
		// The CenterPanel which is the chat room
		chatRoom = new JTextArea("Welcome to the Chat room\n", 80, 80);
		JPanel centerPanel = new JPanel(new GridLayout(1, 1));
		centerPanel.add(new JScrollPane(chatRoom));
		chatRoom.setEditable(false);
		add(centerPanel, BorderLayout.CENTER);
		
		// the 3 buttons
		login = new JButton("Login");
		login.addActionListener(this);
		logout = new JButton("Logout");
		logout.addActionListener(this);
		logout.setEnabled(false); // you have to login before being able to
									// logout
		whoIsIn = new JButton("Who is in");
		whoIsIn.addActionListener(this);
		whoIsIn.setEnabled(false); // you have to login before being able to Who
									// is in
		
		JPanel southPanel = new JPanel();
		southPanel.add(login);
		southPanel.add(logout);
		southPanel.add(whoIsIn);
		add(southPanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		tf.requestFocus();
		
	}
	
	// called by the Client to append text in the TextArea
	void append(String str)
	{
		chatRoom.append(str);
		chatRoom.setCaretPosition(chatRoom.getText().length());
	}
	
	// called by the GUI is the connection failed
	// we reset our buttons, label, textfield
	void connectionFailed()
	{
		login.setEnabled(true);
		logout.setEnabled(false);
		whoIsIn.setEnabled(false);
		label.setText("Enter your username below");
		tf.setText("Anonymous");
		// reset port number and host name as a construction time
		tfPort.setText("" + defaultPort);
		tfServer.setText(defaultHost);
		// let the user change them
		tfServer.setEditable(false);
		tfPort.setEditable(false);
		// don't react to a <CR> after the username
		tf.removeActionListener(this);
		connected = false;
	}
	
	/*
	 * Button or JTextField clicked
	 */
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		
		try
		{
			byte[] unencryptedMessage;
			unencryptedMessage = tf.getText().getBytes("UTF-8");
			
			try
			{
				if (o == logout)
				{
					Command removalCommand = new Command(Commands.REMOVAL);
					removalCommand.setReturnedString(channelName);
					client.sendMessageToServer(removalCommand);
					client.sendMessageToServer(new Command(Commands.LOGOUT));
					
					return;
				}
				
				// if it the who is in button
				if (o == whoIsIn)
				{
					client.sendMessageToServer(new Command(Commands.CLIENTS_CONNECTED));
					return;
				}
				
				// it is coming from the JTextField
				if (connected)
				{
					client.sendMessageToServer(new EncryptedMessage(unencryptedMessage, channelName));
					tf.setText("");
					return;
				}
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
		}
		catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		
		if (o == login)
		{
			// ok it is a connection request
			String username = tf.getText().trim();
			// empty username ignore it
			if (username.length() == 0)
				return;
			// empty serverAddress ignore it
			String server = tfServer.getText().trim();
			if (server.length() == 0)
				return;
			// empty or invalid port numer, ignore it
			String portNumber = tfPort.getText().trim();
			if (portNumber.length() == 0)
				return;
			int port = 0;
			try
			{
				port = Integer.parseInt(portNumber);
			}
			catch (Exception exception)
			{
				return;
			}
			
			// try creating a new Client with GUI
			client = new Client(server, port, username, this);
			// test if we can start the Client
			if (!client.start())
				return;
			tf.setText("");
			label.setText("Enter your message below");
			connected = true;
			
			// disable login button
			login.setEnabled(false);
			// enable the 2 buttons
			logout.setEnabled(true);
			whoIsIn.setEnabled(true);
			// disable the Server and Port JTextField
			tfServer.setEditable(false);
			tfPort.setEditable(false);
			// Action listener for when the user enter a message
			tf.addActionListener(this);
		}
		
	}
	
	public String getChannelName()
	{
		return channelName;
	}
	
	public static void main(String[] args)
	{
		new ClientGUI("localhost", 443, "Channel x");
	}
	
}