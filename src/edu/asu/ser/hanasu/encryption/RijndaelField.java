package edu.asu.ser.hanasu.encryption;

/**
 * Galois Field specific to the AES algorithm.
 * 
 * Note: the Rijndael Field is a Finite Field equal to GF(2**8)
 * 
 * @author Moore, Zachary
 * 
 */
public class RijndaelField
{
	/**
	 * Constant value that is added to the product of two bytes, iff the product
	 * of two bytes multiplied in this field overflows.
	 */
	public static final byte MUL_CONSTANT = 0x1B;
	
	/**
	 * Adds to bytes as specified by the field GF(2**8). Note: in this field,
	 * addition is equivalent to the XOR of the two bytes).
	 * 
	 * @param byte1
	 * @param byte2
	 * @return
	 */
	public static byte add(byte byte1, byte byte2)
	{
		return (byte) (byte1 ^ byte2);
	}
	
	/**
	 * @see #multiply(byte, byte)
	 */
	public static byte multiply(int multiplier, int multiplicand)
	{
		return multiply((byte) multiplier, (byte) multiplicand);
	}

	/**
	 * @see #multiply(byte, byte)
	 */
	public static byte multiply(int multiplier, byte multiplicand)
	{
		return multiply((byte) multiplier, multiplicand);
	}

	/**
	 * @see #multiply(byte, byte)
	 */
	public static byte multiply(byte multiplier, int multiplicand)
	{
		return multiply(multiplier, (byte) multiplicand);
	}
	
	/**
	 * Multiply two bytes in the Rijndael Field.
	 * 
	 * @param multiplier Byte1 to be multiplied
	 * @param multiplicand Byte2 to be multiplied
	 * @return Product of the multiplier and multiplicand
	 */
	public static byte multiply(byte multiplier, byte multiplicand)
	{
		byte result;
		
		// if the LSB of the multiplicand is 1, add byte1 to the result
		result = ((multiplicand & 1) == 1) ? multiplier : 0;
		
		for (int position = 1; position < 8; position++)
		{
			boolean relevantBit = ((multiplicand >> position) & 1) == 1;
			
			if (relevantBit)
			{
				byte intermediateByte = multiplier;
				
				for (int iteration = 0; iteration < position; iteration++)
				{
					boolean msb = (((intermediateByte >> 7) & 1) == 1);
					
					// Shift left 1 and mask the most significant bit
					intermediateByte = (byte) ((intermediateByte << 1) & 0xFF);
					
					// In the case of an overflow, add MUL_CONSTANT
					if (msb)
					{
						intermediateByte ^= MUL_CONSTANT;
						
					}
				}
				
				result ^= intermediateByte;
			}
			else
			{
				continue;
			}
		}
		
		return result;
	}
}
