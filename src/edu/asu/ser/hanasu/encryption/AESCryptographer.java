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
	private static SBox sBox = obtainSBox();
	
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
	
	/**
	 * Represents a single block of data. Note: the size of the block is
	 * determined by {@link AESCryptographer#blockType}. AESBlocks should not
	 * exist outside the context of an AESCryptographer object. When the data
	 * exits outside of this context, it should be in the form of a byte[], not
	 * an AESBlock.
	 * 
	 * Private to prevent unintentional access from outside AESCryptographer.
	 * 
	 * Also note: AESBlock is used for data and the AES key.
	 * 
	 * @author Moore, Zachary
	 * 
	 */
	private class AESBlock
	{
		/** Data stored in this block. */
		private byte[][] data;
		
		/**
		 * Creates an AESBlock containing the provided data. Note, the size of
		 * this block (i.e. the number of bytes it can hold) is determined by
		 * the AESCryptographer in which this object exists. If the length of
		 * the provided data is larger than the block size, an
		 * InvalidBlockSizeException will be thrown. If the length of the
		 * provided data is smaller than the block size, it will be padded,
		 * i.e., its length will be extended (byte-0 will be added) such that
		 * its new length is equal to the block size.
		 * 
		 * Note: the actual data array will not be extended, to improve
		 * performance. Rather, the entries are not added to the 2D array when
		 * the data is being mixed.
		 * 
		 * @param data
		 *            The data to store in this block, as a byte[]
		 * @throws InvalidBlockSizeException
		 */
		public AESBlock(byte[] data) throws InvalidBlockSizeException
		{
			// Ensure data is not too large to fit in a single block
			if (data.length > blockType.numberOfBytes)
			{
				throw new InvalidBlockSizeException("Block is too large");
			}
			
			// Instantiate data[][]
			int numRows = blockType.numberOfRows();
			int numColumns = blockType.numberOfColumns();
			this.data = new byte[numRows][numColumns];
			
			// Move data from byte[] into byte[][], top to bottom, L to R
			for (int column = 0, index = 0; column < numColumns; column++)
			{
				for (int row = 0; row < numRows; row++, index++)
				{
					if (index < data.length)
						this.data[row][column] = data[index];
					else
						break;
				}
			}
		}
		
		public byte[][] getData()
		{
			return data;
		}
		
		/**
		 * Returns the specified word (i.e. row) of data contained in this
		 * block. Note that the number of words in a block is determined by its
		 * size - a 128-bit block has 4 words, a 192-bit block has 6 words, and
		 * a 256-bit block has 8 words.
		 * 
		 * @param index
		 *            Index of the desired word
		 * @return Value of the specified word, as an array of bytes (length 4)
		 */
		public byte[] getWord(int index)
		{
			return data[index];
		}
		
		public void substituteBytes()
		{
			// Replace all values in data[][] with their SBox substitution
			for (int row = 0; row < blockType.numberOfRows(); row++)
			{
				for (int column = 0; column < blockType.numberOfColumns(); column++)
				{
					byte rawData = data[row][column];
					data[row][column] = sBox.substitute(rawData);
				}
			}
		}
		
		public void shiftRows()
		{
			// Shift all words in this block by an amount equal to their index
			for (int wordIndex = 0; wordIndex < blockType.numberOfRows(); wordIndex++)
			{
				byte[] word = getWord(wordIndex);
				shiftWord(word, wordIndex);
			}
		}
		
		private void shiftWord(byte[] word, int amount)
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
				byte[] circularEntries = new byte[amount];
				System.arraycopy(word, 0, circularEntries, 0, amount);
				
				// Shift left by amount
				System.arraycopy(word, amount, word, 0, word.length - 1);
				
				// Wrap the first entries
				System.arraycopy(circularEntries, 0, word, amount + 1, amount);
			}
		}
		
		public void mixColumns()
		{
			// TODO: implement
			throw new NotImplementedException();
		}
		
		public void addRoundKey()
		{
			// TODO: implement
			throw new NotImplementedException();
		}
	}
	
	private AESCryptographer(AESBlockType blockSize, AESBlockType keySize)
	{
		this.blockType = blockSize;
		this.keyType = keySize;
	}
	
	public AESCryptographer(byte[] key, AESBlockType blockSize)
			throws InvalidBlockSizeException
	{
		this(blockSize, AESBlockType.getType(key.length));
		
		AESBlock baseKey = new AESBlock(key);
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
