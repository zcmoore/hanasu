package edu.asu.ser.hanasu.encryption;

import java.net.URL;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class SBox
{
	/**
	 * Contents of this box, used for substitution. The size of boxValues will
	 * always be 256, as it contains a substitution for every byte value (total
	 * combinations: 256)
	 */
	protected final byte[] boxValues;
	
	/**
	 * Base constructor for all SBox implementations. Ensures the size of
	 * boxValues is appropriate (256). The size of boxValues cannot be altered.
	 */
	protected SBox()
	{
		// Box must have a substitution value for each value of a byte
		boxValues = new byte[16 * 16];
	}
	
	public static SBox loadSBox(URL filePath)
	{
		throw new NotImplementedException();
	}
	
	public static SBox loadSBox(String filePath)
	{
		throw new NotImplementedException();
	}
	
	public static SBox generateSBox(RijndaelField galoisField)
	{
		throw new NotImplementedException();
	}
	
	public static SBox generateSBox(int galoisBase, int galoisExponent)
	{
		throw new NotImplementedException();
	}
	
	public int size()
	{
		return boxValues.length;
	}
	
	public byte substitute(byte inputByte)
	{
		System.out.println("byte: " + Integer.toBinaryString(((int) inputByte)));
		System.out.println(Integer.toBinaryString(((int) inputByte) & 0xFF));
		System.out.println();
		return boxValues[(((int) inputByte) & 0xFF)];
	}
	
	public byte substitute(char inputByte)
	{
		return substitute((byte) inputByte);
	}
}
