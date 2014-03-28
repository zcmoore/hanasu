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
	 * Adds a number of bytes together as specified by the field GF(2**8). See
	 * {@link #add(byte, byte)}
	 * 
	 * @param bytes
	 *            An array of bytes to sum together
	 * @return The sum of the given bytes
	 * @see #add(byte, byte)
	 */
	public static byte sum(byte... addends)
	{
		byte sum = 0;
		for (byte addend : addends)
		{
			sum = add(sum, addend);
		}
		
		return sum;
	}
	
	/**
	 * Batch operator. Multiplies all bytes in one array by each corresponding
	 * byte in a second array. If arrays are of different sizes, an exception
	 * will be thrown
	 * 
	 * @param bytes
	 *            An array of bytes to multiply together
	 * @return The products of the given arrays
	 * @see #multiply(byte, byte)
	 */
	public static byte[] products(byte[] multipliers, byte[] multiplicands)
	{
		byte[] products;
		
		if (multipliers.length == multiplicands.length)
		{
			products = new byte[multipliers.length];
		}
		else
		{
			throw new IllegalArgumentException("arrays must be the same length");
		}
		
		for (int index = 0; index < products.length; index++)
		{
			byte multiplier = multipliers[index];
			byte multiplicand = multiplicands[index];
			products[index] = multiply(multiplier, multiplicand);
		}
		
		return products;
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
	 * @param multiplier
	 *            Byte1 to be multiplied
	 * @param multiplicand
	 *            Byte2 to be multiplied
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
