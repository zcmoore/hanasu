package edu.asu.ser.hanasu.encryption;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Represents a single block of data within the context of AES encryption.
 * AESBlock objects are used to represent a structured byte[], that can
 * represent any data, such as objects or Strings.
 * 
 * AESBlock provides operators to manipulate its data, including methods to
 * confuse and diffuse data contained in this block.
 * 
 * AESBlock is intended for use with an AESCryptogropher object, but can be used
 * outside of this context. AESBlock uses the SBox defined by
 * {@link AESCryptographer#sBox}
 * 
 * @author Moore, Zachary
 * 
 */
public class AESBlock
{
	/** Data stored in this block. */
	private byte[][] data;
	
	/** Specifications of this block. */
	private final AESBlockType blockType;
	
	/**
	 * Creates an AESBlock containing the provided data. If the length of the
	 * provided data is larger than the block size, an InvalidBlockSizeException
	 * will be thrown. If the length of the provided data is smaller than the
	 * block size, it will be padded, i.e., its length will be extended (byte-0
	 * will be added) such that its new length is equal to the block size.
	 * 
	 * Note: the actual data array will not be extended, to improve performance.
	 * Rather, the entries are not added to the 2D array when the data is being
	 * mixed.
	 * 
	 * @param data
	 *            The data to store in this block, as a byte[]
	 * @throws InvalidBlockSizeException
	 *             Indicates that the data cannot fit into a block of the given
	 *             type/size
	 */
	public AESBlock(byte[] data, AESBlockType blockType)
			throws InvalidBlockSizeException
	{
		this.blockType = blockType;
		
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
	
	/**
	 * Creates an empty AESBlock of the specified type.
	 * 
	 * @param blockType
	 *            The type of block to be created.
	 * @throws InvalidBlockSizeException
	 *             Indicates that the data cannot fit into a block of the given
	 *             type/size
	 */
	public AESBlock(AESBlockType blockType) throws InvalidBlockSizeException
	{
		this(new byte[0], blockType);
	}
	
	/**
	 * Creates an array of AESBlocks using the given data. The number of blocks
	 * produced is determined by the size of the given data array. This
	 * algorithm will produce the minimum number of blocks that can hold all
	 * uncompressed data specified by the given array.
	 * 
	 * @param data
	 *            The byte[] to encapsulate into a number of AESBlocks
	 * @param blockType
	 *            The type/size of block that should be produced
	 * @return An array of AESBlocks containing all of the data in the given
	 *         data[]
	 */
	public static AESBlock[] parseBlocks(byte[] data, AESBlockType blockType)
	{
		int blockLength = blockType.numberOfBytes;
		int numberOfBlocks = data.length / blockLength;
		int remainingBytes = data.length % blockLength;
		AESBlock[] dataBlocks;
		
		try
		{
			if (remainingBytes > 0)
			{
				dataBlocks = new AESBlock[numberOfBlocks + 1];
				
				// Handle the last block
				byte[] dataSection = new byte[remainingBytes];
				int startIndex = data.length - remainingBytes;
				System.arraycopy(data, startIndex, dataSection, 0,
						remainingBytes);
				
				AESBlock dataBlock;
				
				dataBlock = new AESBlock(dataSection, blockType);
				dataBlocks[numberOfBlocks] = dataBlock;
			}
			else
			{
				dataBlocks = new AESBlock[numberOfBlocks];
			}
			
			for (int index = 0; index < numberOfBlocks; index++)
			{
				byte[] dataSection = new byte[blockLength];
				int startIndex = index * blockLength;
				System.arraycopy(data, startIndex, dataSection, 0, blockLength);
				
				AESBlock dataBlock = new AESBlock(dataSection, blockType);
				dataBlocks[index] = dataBlock;
			}
		}
		catch (InvalidBlockSizeException e)
		{
			e.printStackTrace();
			dataBlocks = null;
		}
		
		return dataBlocks;
	}
	
	/**
	 * 
	 * 
	 * @return The data held in this block, backed by the block itself.
	 */
	public byte[][] getData()
	{
		return data;
	}
	
	/**
	 * Returns the specified word (i.e. row) of data contained in this block.
	 * Note that the number of words in a block is determined by its size - a
	 * 128-bit block has 4 words, a 192-bit block has 6 words, and a 256-bit
	 * block has 8 words.
	 * 
	 * @param index
	 *            Index of the desired word
	 * @return Value of the specified word, as an array of bytes (length 4)
	 */
	public byte[] getWord(int index)
	{
		return data[index];
	}
	
	/**
	 * Replaces all bytes currently held in this AESBlock with their
	 * corresponding values specified by {@link AESCryptographer#sBox}
	 * 
	 * @see SBox#substitute(byte)
	 */
	public void substituteBytes()
	{
		// Replace all values in data[][] with their SBox substitution
		for (int row = 0; row < blockType.numberOfRows(); row++)
		{
			for (int column = 0; column < blockType.numberOfColumns(); column++)
			{
				byte rawData = data[row][column];
				data[row][column] = AESCryptographer.sBox.substitute(rawData);
			}
		}
	}
	
	/**
	 * Replaces all bytes currently held in this AESBlock with their
	 * corresponding inverse values specified by {@link AESCryptographer#sBox}
	 * 
	 * @see SBox#invert(byte)
	 */
	public void invertBytes()
	{
		// Replace all values in data[][] with their SBox inverse
		for (int row = 0; row < blockType.numberOfRows(); row++)
		{
			for (int column = 0; column < blockType.numberOfColumns(); column++)
			{
				byte rawData = data[row][column];
				data[row][column] = AESCryptographer.sBox.invert(rawData);
			}
		}
	}
	
	/**
	 * Diffuse the data in this block by shifting all rows to the left by an
	 * amount equal to the row index of each row. That is, row 0 will be shifted
	 * by an amount of 0, row one will be shifted left by a value of 1, etc.
	 * 
	 * @see Blocks#shiftWordLeft(byte[], int)
	 */
	public void shiftRows()
	{
		// Shift all words in this block by an amount equal to their index
		for (int wordIndex = 0; wordIndex < blockType.numberOfRows(); wordIndex++)
		{
			byte[] word = getWord(wordIndex);
			Blocks.shiftWordLeft(word, wordIndex);
		}
	}
	
	/**
	 * Undo the confusion caused by {@link #shiftRows()} by shifting all rows to
	 * the right by an amount equal to the row index of each row. That is, row 0
	 * will be shifted by an amount of 0, row one will be shifted right by a
	 * value of 1, etc.
	 * 
	 * @see Blocks#shiftWordRight(byte[], int)
	 */
	public void inverseShiftRows()
	{
		// Shift all words in this block by an amount equal to their index
		for (int wordIndex = 0; wordIndex < blockType.numberOfRows(); wordIndex++)
		{
			byte[] word = getWord(wordIndex);
			Blocks.shiftWordRight(word, wordIndex);
		}
	}
	
	/**
	 * Confuses the data by column using a matrix multiplication, where the
	 * multiplier is a matrix such that its first row is {2, 3, 1, 1} and each
	 * subsequent row is equal to the previous row shifted to the right by 1.
	 */
	public void mixColumns()
	{
		if (this.blockType != AESBlockType.BIT_128)
		{
			// TODO: add support for 192 and 256-bit AES
			throw new NotImplementedException();
		}
		
		// TODO: replace with loop to perform pseudo matrix multiplication
		int numColumns = blockType.numberOfColumns();
		int numRows = blockType.numberOfRows();
		for (int column = 0; column < numColumns; column++)
		{
			byte[] mixedColumn = new byte[numRows];
			for (int row = 0; row < numRows; row++)
			{
				int nextRow = (row + 1) % numRows;
				int row2 = (row + 2) % numRows;
				int row3 = (row + 3) % numRows;
				
				byte mixedValue;
				byte intermediateValue;
				
				// @formatter:off
				mixedValue = RijndaelField.multiply(data[row][column], 0x02);
				
				intermediateValue = RijndaelField.multiply(data[nextRow][column], 0x03);
				mixedValue = RijndaelField.add(mixedValue, intermediateValue);
				
				mixedValue = RijndaelField.add(mixedValue, data[row2][column]);
				mixedValue = RijndaelField.add(mixedValue, data[row3][column]);
				// @formatter:on
				
				mixedColumn[row] = mixedValue;
			}
			
			// Copy mixedColumn into data column
			for (int row = 0; row < numRows; row++)
			{
				data[row][column] = mixedColumn[row];
			}
		}
	}
	
	/**
	 * Reverses the operation done by {@link #mixColumns()}. If a block calls
	 * {@link #mixColumns()} and directly afterwards calls
	 * {@link #inverseMixColumns()}, then it should be reverted to its original
	 * state.
	 */
	public void inverseMixColumns()
	{
		if (this.blockType != AESBlockType.BIT_128)
		{
			// TODO: add support for 192 and 256-bit AES
			throw new NotImplementedException();
		}
		
		int numColumns = blockType.numberOfColumns();
		int numRows = blockType.numberOfRows();
		for (int column = 0; column < numColumns; column++)
		{
			byte[] mixedColumn = new byte[numRows];
			for (int row = 0; row < numRows; row++)
			{
				int nextRow = (row + 1) % numRows;
				int row2 = (row + 2) % numRows;
				int row3 = (row + 3) % numRows;
				
				byte mixedValue;
				byte intermediateValue;
				
				// @formatter:off
				mixedValue = RijndaelField.multiply(0x0E, data[row][column]);
				
				intermediateValue = RijndaelField.multiply(0x0B, data[nextRow][column]);
				mixedValue = RijndaelField.add(mixedValue, intermediateValue);

				intermediateValue = RijndaelField.multiply(0x0D, data[row2][column]);
				mixedValue = RijndaelField.add(mixedValue, intermediateValue);

				intermediateValue = RijndaelField.multiply(0x09, data[row3][column]);
				mixedValue = RijndaelField.add(mixedValue, intermediateValue);
				// @formatter:on
				
				mixedColumn[row] = mixedValue;
			}
			
			// Copy mixedColumn into data column
			for (int row = 0; row < numRows; row++)
			{
				data[row][column] = mixedColumn[row];
			}
		}
	}
	
	/**
	 * Returns the contents of this block as a byte array. The data will be
	 * stored in a manner such that creating a new AES block with the byte[]
	 * returned by this method should produce a block with Equivalent contents.
	 * 
	 * More specifically, the data will be transferred from top to bottom, left
	 * to right. data[0][0] will be stored in index 0 of the byte[], data[0][1]
	 * will be stored in index 1, data[1][0] will be stored in index 4, etc.
	 * 
	 * @return The contents of this block as a byte array.
	 */
	public byte[] toByteArray()
	{
		byte[] data = new byte[blockType.numberOfBytes];
		
		int numRows = blockType.numberOfRows();
		int numColumns = blockType.numberOfColumns();
		
		// Move data from byte[][] into byte[], top to bottom, L to R
		for (int column = 0, index = 0; column < numColumns; column++)
		{
			for (int row = 0; row < numRows; row++, index++)
			{
				if (index < data.length)
					data[index] = this.data[row][column];
				else
					break;
			}
		}
		
		return data;
	}
	
	/**
	 * Perform a 1-1 addition operation between this block and the specified
	 * roundKey. Each (row, column) of the given key will be added to the (row,
	 * column) of this block.
	 * 
	 * @param roundKey
	 */
	public void addRoundKey(AESBlock roundKey)
	{
		if (this.blockType != AESBlockType.BIT_128)
		{
			// TODO: add support for 192 and 256-bit AES
			throw new NotImplementedException();
		}
		
		byte[][] roundKeyArray = roundKey.getData();
		for (int row = 0; row < roundKeyArray.length; row++)
		{
			for (int column = 0; column < roundKeyArray[0].length; column++)
			{
				data[row][column] ^= roundKeyArray[row][column];
			}
		}
	}
}
