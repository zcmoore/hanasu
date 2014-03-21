package edu.asu.ser.hanasu.encryption;

import java.net.URL;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class SBox
{
	/** Contents of this box, used for substitution */
	protected byte[] boxValues;
	
	public static SBox loadSBox(URL filePath)
	{
		throw new NotImplementedException();
	}
	
	public static SBox loadSBox(String filePath)
	{
		throw new NotImplementedException();
	}
	
	public static SBox generateSBox(GaloisField galoisField)
	{
		throw new NotImplementedException();
	}
	
	public static SBox generateSBox(int galoisBase, int galoisExponent)
	{
		throw new NotImplementedException();
	}
}
