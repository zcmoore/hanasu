package edu.asu.ser.hanasu.encryption;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class AESCryptographer
{
	private static SBox sBox;
	private final AESBlock[] key;
	
	public AESCryptographer(byte[] key)
	{
		// TODO Implement
		// Call #AESCryptographer(AESBlock key)
		throw new NotImplementedException();
	}
	
	public AESCryptographer(byte[][] key)
	{
		// TODO Implement
		// Call #AESCryptographer(AESBlock key)
		throw new NotImplementedException();
	}
	
	public AESCryptographer(AESBlock key)
	{
		// TODO Implement
		// Expand and set key
		throw new NotImplementedException();
	}
	
	public byte[] encrypt(String message)
	{
		throw new NotImplementedException();
	}
	
	public byte[] encrypt(byte[] data)
	{
		throw new NotImplementedException();
	}
	
	public String decrypt(byte[] data)
	{
		throw new NotImplementedException();
	}
	
	public String decrypt(AESBlock data)
	{
		throw new NotImplementedException();
	}
	
	private AESBlock[] expandKey(AESBlock key)
	{
		throw new NotImplementedException();
	}
	
}
