package edu.asu.ser.hanasu.encryption;

public class Blocks
{
	public static void shiftWordLeft(byte[] word, int amount)
	{
		// Throws NullPointerException if word is null
		// If word.length is 0 or 1, shifting is complete
		if (word.length <= 1)
		{
			return;
		}
		else
		{
			// Determine the first [amount] entries
			byte[] firstEntries = new byte[amount];
			System.arraycopy(word, 0, firstEntries, 0, amount);
			
			// Shift left by amount
			System.arraycopy(word, amount, word, 0, word.length - amount);
			
			// Wrap the first entries
			System.arraycopy(firstEntries, 0, word, word.length - amount,
					amount);
		}
	}
	
	public static void shiftWordRight(byte[] word, int amount)
	{
		// Throws NullPointerException if word is null
		// If word.length is 0 or 1, shifting is complete
		if (word.length <= 1)
		{
			return;
		}
		else
		{
			// Determine the last [amount] entries
			byte[] lastEntries = new byte[amount];
			System.arraycopy(word, word.length - amount, lastEntries, 0, amount);
			
			// Shift right by amount
			System.arraycopy(word, 0, word, amount, word.length - amount);
			
			// Wrap the last entries
			System.arraycopy(lastEntries, 0, word, 0, amount);
		}
	}
	
	public static byte[] convertStringToByteArray(String message)
	{
		char[] characters = message.toCharArray();
		byte[] bytes = new byte[characters.length];
		
		for (int index = 0; index < bytes.length; index++)
		{
			bytes[index] = (byte) characters[index];
		}
		
		return bytes;
	}
	
	public static String convertByteArrayToString(byte[] bytes)
	{
		StringBuilder message = new StringBuilder();
		
		for (int index = 0; index < bytes.length; index++)
		{
			message.append((char) bytes[index]);
		}
		
		return message.toString();
	}
	
	public static byte[] xor(byte[] word1, byte[] word2)
	{
		int length = Math.max(word1.length, word2.length);
		byte[] result = new byte[length];
		
		for (int index = 0; index < length; index++)
		{
			result[index] = (byte) (word1[index] ^ word2[index]);
			// TODO: add exception handling
		}
		
		return result;
	}
}
