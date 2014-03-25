package edu.asu.ser.hanasu.server;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.*;
import javax.swing.*;

public class NewServerGUI extends JFrame implements ActionListener,
		WindowListener
{
	
	private static final long serialVersionUID = -2318140618087709876L;
	private JPanel backPanelTop, backPanelCenter;
	private JTextField portField;
	private JButton startButton;
	private JTextArea debugTextArea, eventTextArea;
	private Server server;
	private boolean isDebuggingOn = false;
	
	public NewServerGUI(int portNumber, boolean isDebuggingOn)
	{
		super("Chat Server GUI");
		server = null;
		// because components get messy
		initComponents(portNumber, isDebuggingOn);
	}
	
	private void initComponents(int portNumber, boolean isDebuggingOn)
	{
		
		this.isDebuggingOn = isDebuggingOn;
		backPanelTop = new JPanel();
		backPanelCenter = new JPanel(new GridLayout(1, 2));
		portField = new JTextField(" " + portNumber);
		startButton = new JButton("Start");
		debugTextArea = new JTextArea(20, 40);
		eventTextArea = new JTextArea(20, 40);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		panelPositioning();
		
		startButton.addActionListener(this);
		
		writeToEventTextArea("Feel Free to resize the window for you own convenience.\n ");
		writeToDebugTextArea("Welcome to server!\n This is the Debugging Room.\n");
		writeToEventTextArea("All important events are written here.\n");
		addWindowListener(this);
		
		setResizable(true);
		setLocation(new Point(500, 320));
		pack();
		setVisible(true);
		
	}
	
	private void panelPositioning()
	{
		
		backPanelTop.add(new JLabel("Port Number:"));
		backPanelTop.add(portField);
		backPanelTop.add(startButton);
		add(backPanelTop, BorderLayout.NORTH);
		
		debugTextArea.setEditable(false);
		eventTextArea.setEditable(false);
		
		backPanelCenter.add(new JScrollPane(eventTextArea));
		if (isDebuggingOn)
			backPanelCenter.add(new JScrollPane(debugTextArea));
		
		add(backPanelCenter);
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
		// Server still running, need to exit
		// Only button here is Start...
		if (server != null)
		{
			server.stop();
			server = null;
			portField.setEditable(true);
			return;
		}
		
		int port;
		
		try
		{
			// Trim because its an int regarding a port number
			port = Integer.parseInt(portField.getText().trim());
		}
		catch (Exception exception)
		{
			writeToEventTextArea(exception.toString());
			return;
		}
		
		// Server has stopped and port created
		server = new Server(port, this);
		new ServerRunning().start();
		// We have created the server
		// No need to change port, exit to change port
		portField.setEditable(false);
	}
	
	public static void main(String[] args)
	{
		try
		{
			for (UIManager.LookAndFeelInfo info : UIManager
					.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				new NewServerGUI(443, true);
			}
			
		});
	}
	
	public void writeToDebugTextArea(String string)
	{
		debugTextArea.append(string);
		debugTextArea.setCaretPosition(debugTextArea.getText().length());
	}
	
	public void writeToEventTextArea(String string)
	{
		eventTextArea.append(string);
		eventTextArea.setCaretPosition(eventTextArea.getText().length());
		
	}
	
	public boolean getDebuggingStatus()
	{
		return isDebuggingOn;
	}
	
	class ServerRunning extends Thread
	{
		public void run()
		{
			server.start();
			portField.setEditable(true);
			
			writeToEventTextArea("Server crashed\n");
			server = null;
		}
	}
	
	public void windowClosed(WindowEvent arg0)
	{
		// Window is closing
		// need to close everything if still running
		if (server != null)
		{
			try
			{
				server.stop();
			}
			catch (Exception onClose)
			{
				// Not writing to GUI because were closing...
				onClose.printStackTrace();
			}
		}
		dispose();
		// 0 for successful close
		System.exit(0);
	}
	
	@Override
	public void windowActivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void windowClosing(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void windowDeactivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void windowDeiconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void windowIconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void windowOpened(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
}
