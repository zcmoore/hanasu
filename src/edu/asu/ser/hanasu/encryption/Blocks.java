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
			byte[] circularEntries = new byte[amount];
			System.arraycopy(word, 0, circularEntries, 0, amount);
			
			// Shift left by amount
			System.arraycopy(word, amount, word, 0, word.length - 1);
			
			// Wrap the first entries
			System.arraycopy(circularEntries, 0, word, amount + 1, amount);
		}
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
