package edu.asu.ser.hanasu.encryption;

/**
 * Provides support operations for blocks (i.e. 2D arrays) of data. Each row of
 * a uniform block will be referred to as a word.
 * 
 * @author Moore, Zachary
 * 
 */
public class Blocks
{
	/**
	 * Performs a LEFTWARD circular shift on an array of bytes. All operations
	 * are performed on the given array. The given array will be altered.
	 * 
	 * [1, 2, 3, 4] shifted by 1 will result in the array [2, 3, 4, 1]
	 * 
	 * @param word
	 *            The byte[] to be shifted.
	 * @param amount
	 *            The amount by which to shift the array.
	 */
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
	
	/**
	 * Performs a RIGHTWARD circular shift on an array of bytes. All operations
	 * are performed on the given array. The given array will be altered.
	 * 
	 * [1, 2, 3, 4] shifted by 1 will result in the array [4, 1, 2, 3]
	 * 
	 * @param word
	 *            The byte[] to be shifted.
	 * @param amount
	 *            The amount by which to shift the array.
	 */
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
	
	/**
	 * Performs an XOR operation across all bytes of two byte arrays. If the
	 * length of the arrays are different, then the most significant entries of
	 * the longer array will remain the same, and the least significant entries
	 * will be XORed with the smaller array.
	 * 
	 * @param word1
	 *            Array of bytes to be XORed
	 * @param word2
	 *            Array of bytes to be XORed
	 * @return Result of the batch XOR operation
	 */
	public static byte[] xor(byte[] word1, byte[] word2)
	{
		// TODO: account for IndexOutOfBounds
		int length = Math.max(word1.length, word2.length);
		byte[] result = new byte[length];
		
		for (int index = 0; index < length; index++)
		{
			result[index] = (byte) (word1[index] ^ word2[index]);
			// TODO: add exception handling
		}
		
		return result;
	}
	
	/**
	 * Casts an array of integers as bytes, and returns the resultant byte[].
	 * 
	 * Note: unlike {@link java.nio.IntBuffer#put(int[])} followed by
	 * {@link java.nio.ByteBuffer#array()}, this method will treat each int as a
	 * single byte, rather than a collection of 4 bytes. 
	 * 
	 * This method can be interpreted as a support method for "unsigned" bytes.
	 * 
	 * @param intValues
	 * @return
	 */
	public static byte[] castToBytes(int[] intValues)
	{
		byte[] byteValues = new byte[intValues.length];
		
		for (int index = 0; index < intValues.length; index++)
		{
			byteValues[index] = (byte) ((intValues[index]) & 0xFF);
		}
		
		return byteValues;
	}
}
