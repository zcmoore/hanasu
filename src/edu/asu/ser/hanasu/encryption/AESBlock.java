package edu.asu.ser.hanasu.encryption;

public class AESBlock
{
	private byte[][] data;
	
	static enum SupportedBlockSize
	{
		BIT_128,
		BIT_192,
		BIT_256;
	}
}
