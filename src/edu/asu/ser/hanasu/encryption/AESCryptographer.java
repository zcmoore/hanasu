package edu.asu.ser.hanasu.encryption;

import java.util.ArrayList;
import java.util.Arrays;

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
	private final AESBlock[] key;
	
	public AESCryptographer(byte[] key, AESBlockType blockSize)
			throws InvalidBlockSizeException
	{
		this.blockType = blockSize;
		this.keyType = AESBlockType.getType(key.length);
		
		AESBlock baseKey = new AESBlock(key, keyType);
		this.key = expandKey(baseKey);
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
		try
		{
			ArrayList<AESBlock> dataBlocks = new ArrayList<>();
			int index = 0;
			
			// Divide data into blocks for processing
			while (index < data.length)
			{
				int length = blockType.numberOfBytes;
				int remainingData = data.length - index;
				length = (remainingData < length) ? remainingData : length;
				byte[] dataSection = new byte[length];
				System.arraycopy(data, index, dataSection, 0, length);
				
				AESBlock dataBlock = new AESBlock(dataSection, blockType);
				dataBlocks.add(dataBlock);
				index += length;
			}
			
			for (int round = 1; round < key.length; round++)
			{
				if (round != key.length - 1)
				{
					for (AESBlock block : dataBlocks)
					{
						block.substituteBytes();
						block.shiftRows();
						block.mixColumns();
						block.addRoundKey(key[round]);
					}
				}
				else
				{
					for (AESBlock block : dataBlocks)
					{
						block.substituteBytes();
						block.shiftRows();
						block.addRoundKey(key[round]);
					}
				}	
			}
		}
		catch (InvalidBlockSizeException e)
		{
			e.printStackTrace();
		}
		
		// TODO: convert dataBlocks to byte[]
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
	
	private AESBlock[] expandKey(AESBlock key) throws InvalidBlockSizeException
	{
		int numberOfRounds = Math.max(keyType.wordCount(),
				blockType.wordCount()) + 6;
		AESBlock[] expandedKey = new AESBlock[numberOfRounds + 1];
		expandedKey[0] = key;
		
		for (int roundIndex = 1; roundIndex <= numberOfRounds; roundIndex++)
		{
			AESBlock previousBlock = expandedKey[roundIndex - 1];
			expandedKey[roundIndex] = new AESBlock(keyType);
			
			byte[][] currentBlock = expandedKey[roundIndex].getData();
			
			// Handle the first word separately
			byte relevantRoundConstant = getRelevantRoundConstant(roundIndex);
			byte[] grounInput = previousBlock.getWord(keyType.wordLength() - 1);
			byte[] groundWord = funtionG(grounInput, relevantRoundConstant);
			currentBlock[0] = Blocks.xor(previousBlock.getWord(0), groundWord);
			
			for (int word = 1; word < keyType.wordCount(); word++)
			{
				currentBlock[word] = Blocks.xor(currentBlock[word - 1], previousBlock.getWord(word));
			}
		}
		
		return expandedKey;
	}
	
	private byte[] funtionG(byte[] word, byte relevantRoundConstant)
	{
		// Work on a copy - do not alter the provided array
		byte[] result = Arrays.copyOf(word, blockType.wordLength());
		
		// Shift left 1
		Blocks.shiftWordLeft(result, 1);
		
		// Substitute bytes
		for (int index = 0; index < result.length; index++)
		{
			result[index] = sBox.substitute(result[index]);
		}
		
		// Add RoundConstant
		result[0] ^= relevantRoundConstant;
		
		return result;
	}
	
	private byte getRelevantRoundConstant(int roundIndex)
	{
		byte result = 1;
		
		// RoundKey[i] = 2 * RoundKey[i - 1]
		for (int index = 1; index < roundIndex; index++)
		{
			result = RijndaelField.multiply(2, result);
		}
		
		return result;
	}
	
}
