package edu.asu.ser.hanasu.server;
/*
 * Test purposes only
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

import javax.swing.*;

import edu.asu.ser.hanasu.server.Command.Commands;

@SuppressWarnings("serial")
public class ChannelGUI extends JFrame implements ActionListener
{
	private JLabel label;
	// to hold the Username and later on the messages
	private JTextField tf;
	private JTextField tfServer, tfPort;
	private JButton login, logout;
	private JTextArea chatRoom;
	private Channel channel;
	private int defaultPortNumber;
	private String hostName;
	boolean connected;
	
	public ChannelGUI(String host, int port)
	{
		
		super("Chat Client");
		this.defaultPortNumber = port;
		this.hostName = host;
		
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
		tf = new JTextField("Channel X");
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
		
		JPanel southPanel = new JPanel();
		southPanel.add(login);
		southPanel.add(logout);
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
		chatRoom.setCaretPosition(chatRoom.getText().length() - 1);
	}
	
	// called by the GUI is the connection failed
	// we reset our buttons, label, textfield
	void connectionFailed()
	{
		login.setEnabled(true);
		logout.setEnabled(false);
		label.setText("Enter your username below");
		tf.setText("Channel Name");
		// reset port number and host name as a construction time
		tfPort.setText("" + defaultPortNumber);
		tfServer.setText(hostName);
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
			if (o == logout)
			{
				channel.sendMessageToServer(new Command(Commands.LOGOUT));
				return;
			}
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		
		if (o == login)
		{
			// ok it is a connection request
			String channelName = tf.getText().trim();
			// empty username ignore it
			if (channelName.length() == 0)
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
			channel = new Channel(server, port, this, channelName);
			// test if we can start the Client
			if (!channel.start())
				return;
			tf.setText("");
			
			connected = true;
			// disable login button
			login.setEnabled(false);
			
			logout.setEnabled(true);
			// disable the Server and Port JTextField
			tfServer.setEditable(false);
			tfPort.setEditable(false);

			tf.addActionListener(this);
		}
		
	}
	
	public static void main(String[] args)
	{
		new ChannelGUI("localhost", 443);
	}
	
	
}
