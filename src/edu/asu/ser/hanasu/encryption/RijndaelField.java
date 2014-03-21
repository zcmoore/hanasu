package edu.asu.ser.hanasu.encryption;

public class RijndaelField
{
	public static final byte MUL_CONSTNT = 0x1B;
	
	public static byte add(byte byte1, byte byte2)
	{
		return (byte) (byte1 ^ byte2);
	}
	
	public static byte multiply(byte byte1, byte byte2)
	{
		byte result;
		
		// if the LSB of byte2 is 1, add byte1 to the result
		result = ((byte2 & 1) == 1) ? byte1 : 0;
		
		for (int position = 1; position < 8; position++)
		{
			boolean relevantBit = (byte2 >> position) == 1;
			
			if (relevantBit)
			{
				byte intermediateByte = byte1;
				
				for (int iteration = 0; iteration < position; iteration++)
				{
					boolean msb = (((intermediateByte >> 7) & 1) == 1);
					
					// Shift left 1 and mask the most significant bit
					intermediateByte = (byte) ((intermediateByte << 1) & 0xFF);
					
					if (msb)
					{
						intermediateByte ^= MUL_CONSTNT;
						
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
