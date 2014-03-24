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
	
	public AESBlock(AESBlockType blockType) throws InvalidBlockSizeException
	{
		this(new byte[0], blockType);
	}
	
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
	
	public void shiftRows()
	{
		// Shift all words in this block by an amount equal to their index
		for (int wordIndex = 0; wordIndex < blockType.numberOfRows(); wordIndex++)
		{
			byte[] word = getWord(wordIndex);
			Blocks.shiftWordLeft(word, wordIndex);
		}
	}
	
	public void inverseShiftRows()
	{
		// Shift all words in this block by an amount equal to their index
		for (int wordIndex = 0; wordIndex < blockType.numberOfRows(); wordIndex++)
		{
			byte[] word = getWord(wordIndex);
			Blocks.shiftWordRight(word, wordIndex);
		}
	}
	
	public void mixColumns()
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
