package edu.asu.ser.hanasu.encryption;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Object used to encrypt and decrypt data, using AES encryption.
 * 
 * @author Moore, Zachary
 *
 */
public class AESCryptographer
{
	/** Used for substituting bytes during the diffusion process in each round */
	public static SBox sBox = obtainSBox();
	
	/**
	 * Size of the data blocks used by this Cryptographer. This will determine
	 * the number of rows in the data blocks (not the key blocks).
	 */
	private final AESBlockType blockType;
	
	/**
	 * Size of the key blocks used by this Cryptographer. This will determine
	 * the number of rows in the data blocks (not the data blocks).
	 */
	private final AESBlockType keyType;
	
	/**
	 * Keys to encrypt and decrypt data. This is the expanded key; the base key
	 * is included in this array, at index 0
	 */
	private AESBlock[] key;
	
	public AESCryptographer(byte[] key, AESBlockType blockSize)
			throws InvalidBlockSizeException
	{
		this(blockSize, AESBlockType.getType(key.length));
		
		AESBlock baseKey = new AESBlock(key, keyType);
		this.key = expandKey(baseKey);
	}
	
	private AESCryptographer(AESBlockType blockSize, AESBlockType keySize)
	{
		this.blockType = blockSize;
		this.keyType = keySize;
	}
	
	public static SBox obtainSBox()
	{
		return new RijndaelSBox();
	}
	
	public byte[] encrypt(String message)
	{
		// TODO: implement
		throw new NotImplementedException();
	}
	
	public byte[] encrypt(byte[] data)
	{
		// TODO: implement
		throw new NotImplementedException();
	}
	
	public String decrypt(byte[] data)
	{
		// TODO: implement
		throw new NotImplementedException();
	}
	
	public String decrypt(AESBlock data)
	{
		// TODO: implement
		throw new NotImplementedException();
	}
	
	private AESBlock[] expandKey(AESBlock key)
	{
		// TODO: implement
		throw new NotImplementedException();
	}
	
}
