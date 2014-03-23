package edu.asu.ser.hanasu.encryption;

public enum AESBlockType
{
	// TODO extend support to 192 and 256-bit AES
	BIT_128(16);
	
	/**
	 * The length of each word, in bytes. Note: each row of an AES block
	 * contains one word. Thus, {@link #WORD_LENGTH} implicitly represents the
	 * number of columns in each AESBlock (including the key blocks).
	 */
	private static final int WORD_LENGTH = 4;
	
	/** Total number of bytes in blocks of this size */
	int numberOfBytes;
	
	/**
	 * Enumeration constructor. Private to prevent outside access.
	 * 
	 * @param numberOfBytes
	 *            Size of this block, in bytes. Note: the size should always be
	 *            a multiple of {@value #WORD_LENGTH}.
	 */
	private AESBlockType(int numberOfBytes)
	{
		assert (numberOfBytes % WORD_LENGTH == 0) : "Expected numberOfBytes to be a multiple of "
				+ WORD_LENGTH;
		
		this.numberOfBytes = numberOfBytes;
	}
	
	/**
	 * Returns the first block type that matches the given size.
	 * 
	 * @param numberOfBytes
	 *            The size of the desired block type, in bytes.
	 * @return A block type of the specified size.
	 * @throws InvalidBlockSizeException
	 *             Indicates that the specified size is not supported by AES.
	 */
	public static AESBlockType getType(int numberOfBytes)
			throws InvalidBlockSizeException
	{
		for (AESBlockType type : AESBlockType.values())
		{
			if (type.size() == numberOfBytes)
			{
				return type;
			}
		}
		
		throw new InvalidBlockSizeException();
	}
	
	/**
	 * @return The total number of bytes that a block of this size can hold.
	 */
	public int size()
	{
		return this.numberOfBytes;
	}
	
	/**
	 * @return The length of each word, in bytes.
	 */
	public int wordLength()
	{
		return WORD_LENGTH;
	}
	
	/**
	 * Included for completeness and potential readability benefits. This is
	 * identical to a call to {@link #wordLength()}
	 * 
	 * @return The number of columns in a block of this size.
	 */
	public int numberOfColumns()
	{
		return wordLength();
	}
	
	/**
	 * @return Number of words in a block of this size.
	 */
	public int wordCount()
	{
		return numberOfBytes / WORD_LENGTH;
	}
	
	/**
	 * Included for completeness and potential readability benefits. This is
	 * identical to a call to {@link #wordCount()}
	 * 
	 * @return The number of rows in a block of this size.
	 */
	public int numberOfRows()
	{
		return wordCount();
	}
	
}
