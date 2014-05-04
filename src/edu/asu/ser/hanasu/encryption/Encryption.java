/**
 * 
 */
package edu.asu.ser.hanasu.encryption;

import java.util.Scanner;

/**
 * Console interface through which the user can interact with the encryption
 * module.
 * 
 * @author Moore, Zachary
 * 
 */
public class Encryption
{
	private static AESCryptographer cryptographer;
	private static Scanner scan = new Scanner(System.in);
	private static AESBlockType type = AESBlockType.BIT_128;
	
	/**
	 * @param args
	 *            Unused console arguments
	 */
	public static void main(String[] args)
	{
		String input = "";
		println("Welcome to AES encryption console.");
		println("This interface will allow you to encrypt and decrypt messages using AES encryption. Type \"help\" for a list of available commands");
		
		while (!input.equalsIgnoreCase("quit"))
		{
			print("> ");
			input = scan.nextLine();
			if (input.equals("help"))
			{
				println("Available commands:");
				println("\tsetup - setup cryptographer for encryption");
				println("\tencrypt - encrypt a message");
				println("\tdecrypt - decrypt a message");
				println("\tquit - exit this program");
			}
			else if (input.equals("encrypt"))
			{
				encrypt();
			}
			else if (input.equals("decrypt"))
			{
				decrypt();
			}
			else if (input.equals("setup"))
			{
				setup();
			}
			else if (!input.equals("quit"))
			{
				println("Invalid command. Enter \"help\" for a list of commands.");
			}
		}
		
	}
	
	public static void setup()
	{
		if (cryptographer != null)
		{
			println("Setup will overwrite the current cryptographer. Continue?");
			print(">> ");
			String input = scan.nextLine();
			if (!(input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")))
				return;
		}
		
		println("AES encryption will be executed using a 128-bit key and 128-bit blocks.");
		cryptographer = manufactureCryptographer();
		println("Setup Complete!\nKey:");
		printKey();
		println("");
	}
	
	public static void printKey()
	{
		if (cryptographer != null)
		{
			AESBlock key = cryptographer.getKey();
			printBlock(key);
		}
	}
	
	public static void printBlock(AESBlock block)
	{
		for (int wordIndex = 0; wordIndex < type.wordCount(); wordIndex++)
		{
			println(arrayString(block.getWord(wordIndex)));
		}
	}
	
	public static AESCryptographer manufactureCryptographer()
	{
		AESCryptographer cryptographer;
		print("Enter a key: ");
		String plainKey = scan.nextLine();
		
		try
		{
			byte[] key = plainKey.getBytes("UTF-8");
			if (key.length < type.size())
			{
				println("Key will be padded. Continue?");
				print(">> ");
				String input = scan.nextLine();
				if ((input.equalsIgnoreCase("yes") || input
						.equalsIgnoreCase("y")))
					cryptographer = new AESCryptographer(type, padKey(key));
				else
					return manufactureCryptographer();
			}
			else
			{
				cryptographer = new AESCryptographer(type, key);
			}
		}
		catch (Exception e)
		{
			println("Error parsing key: " + e.getMessage());
			println("Key must be 16 characters or less.");
			return manufactureCryptographer();
		}
		
		return cryptographer;
	}
	
	public static String arrayString(byte[] array)
	{
		StringBuilder string = new StringBuilder();
		string.append("[\t");
		for (byte entry : array)
			string.append(entry + "\t");
		string.append("]");
		return string.toString();
	}
	
	public static byte[] padKey(byte[] key)
	{
		byte[] padded = new byte[type.size()];
		System.arraycopy(key, 0, padded, 0, key.length);
		return padded;
	}
	
	public static void decrypt()
	{
		if (cryptographer == null)
		{
			println("Cryptographer must first be setup. Type \"setup\"");
			return;
		}
		else
		{
			println("Cryptographer will decrypt a sequence of bytes.");
			println("Bytes from a matrix should be read from top to bottom; left to right.");
			println("Separate entries with a space. You may list multiple blocks in one sequence.");
			print("Enter a sequence: ");
			String sequenceText = scan.nextLine();
			String[] entries = sequenceText.split(" ");
			byte[] encryptedData = new byte[entries.length];
			try
			{
				for (int index = 0; index < entries.length; index++)
				{
					encryptedData[index] = Byte.parseByte(entries[index]);
				}
			}
			catch (Exception e)
			{
				println("Parsing error: " + e.getMessage());
				println("Byte values must be within the range -128 to 127. Letters are not allowed.");
				println("Returning to command line");
			}
			
			String decrypted = cryptographer.decrypt(encryptedData);
			println("Decryption Successful.");
			println("Decrypted Message: " + decrypted);
		}
	}
	
	public static void encrypt()
	{
		if (cryptographer == null)
		{
			println("Cryptographer must first be setup. Type \"setup\"");
			return;
		}
		else
		{
			println("Enter a message: ");
			String message = scan.nextLine();
			byte[] encryptedData = cryptographer.encrypt(message);
			AESBlock[] blocks = AESBlock.parseBlocks(encryptedData, type);
			println("Encryption Successful. Data was divided into "
					+ blocks.length + " blocks.");
			println("Encrypted Data:");
			for (int i = 0; i < blocks.length; i++)
			{
				println("Block " + i + ":");
				printBlock(blocks[i]);
			}
		}
	}
	
	public static void println(String string)
	{
		System.out.println(string);
	}
	
	public static void print(String string)
	{
		System.out.print(string);
	}
	
}
