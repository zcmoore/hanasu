package edu.asu.ser.hanasu.encryption;

import java.util.Arrays;

/**
 * Object used to encrypt and decrypt data, using AES encryption. As of version
 * 0.1A, only 128-bit AES is supported.
 * 
 * AES treats all data as blocks (i.e. 2D arrays) of bytes, with a row length of
 * 4 columns each, and a column length determined by the type of AES (128-bit
 * has 4 rows x 4 columns, for a total of 16 bytes = 128 bits). The encryption
 * of this data consists of a number of rounds, which is determined by the size
 * of the data and key blocks. More precisely, the number of rounds is equal to
 * Max(dataBlock.rows, keyBlock.rows) + 6. Thus, AES that uses a 128-BIT (4
 * bytes) key and 128-BIT blocks will consist of 10 rounds = Max(4, 4) + 6.
 * 
 * In addition to the data, the key is also is treated as a block of bytes.
 * Note: prior to encryption or decryption, the key is expanded in order to
 * create a number of different keys equal to the number of rounds + 1. For
 * specifics on the key expansion algorithm, see {@link #expandKey(AESBlock)}.
 * The generated keys are used in the processing of each round (one key per
 * round). The base key (i.e. the key provided by the construction of this
 * cryptographer) is key 0, and is added to the data blocks prior to the
 * encryption rounds.
 * 
 * Each round of encryption consists of 4 basic steps, with the exception of the
 * last round, which does not include the mixColumns step. The steps of each
 * round are as follows: 
 * (1) substitute each byte of data with its corresponding value in {@link #sBox}
 * (2) shift each row by a specified amount, dependent on the row index. See {@link AESBlock#shiftRows()}
 * (3) mix columns, using column-wise operations specified by {@link AESBlock#mixColumns()}
 * (4) add the round key to each block of data.
 * 
 * Decryption consists of the above steps, inverted and done in reverse order.
 * 
 * 
 * @author Moore, Zachary
 * 
 */
public class AESCryptographer
{
	/** Used for substituting bytes during the diffusion process in each round */
	public static SBox sBox = new RijndaelSBox();
	
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
	
	/**
	 * Initiates a new Cryptographer based on the provided specifications. These
	 * specifications will be used throughout the lifespan of this object, and
	 * cannot be changed.
	 * 
	 * This object's {@link #keyType} will be determined from the length of the
	 * given key (see {@link AESBlockType#getType(int)}). If the key length is
	 * not currently supported, an InvalidBlockSizeException will be thrown.
	 * 
	 * Supported blocks are: {@value AESBlockType#values()}
	 * 
	 * @param blockSize
	 *            The size of the blocks that this cryptographer will use. All
	 *            supported types are available in AESBlockType.
	 * @param key
	 *            They key that will be used for all encryption/decryption
	 *            throughout the lifespan of this object.
	 * @throws InvalidBlockSizeException
	 *             Indicates that the given key size is not supported.
	 * @see AESBlockType#values()
	 */
	public AESCryptographer(AESBlockType blockSize, byte[] key)
			throws InvalidBlockSizeException
	{
		this.blockType = blockSize;
		this.keyType = AESBlockType.getType(key.length);
		
		AESBlock baseKey = new AESBlock(key, keyType);
		this.key = expandKey(baseKey);
	}
	
	/**
	 * Encrypts the given String, and returns the encrypted data as a byte[].
	 * This protocol is equivalent to {@link #encrypt(byte[])}
	 * 
	 * @param message
	 *            The data to encrypt, as a string
	 * @return Encrypted message, as a byte[]
	 * @see #encrypt(byte[])
	 */
	public byte[] encrypt(String message)
	{
		byte[] data = message.getBytes();
		return encrypt(data);
	}
	
	/**
	 * Encrypts the given data using AES protocol, with the {@link #key} and
	 * {@link #blockType} associated with this AESCryptographer.
	 * 
	 * If the data is smaller than {@link #blockType}, then it will be
	 * automatically padded. If data is larger than {@link #blockType} permits,
	 * then the data will be divided into multiple blocks.
	 * 
	 * @param data
	 *            Data to be encrypted
	 * @return Encrypted data as a byte[]
	 */
	public byte[] encrypt(byte[] data)
	{
		// Divide data into blocks for processing
		AESBlock[] dataBlocks = AESBlock.parseBlocks(data, blockType);
		
		for (AESBlock block : dataBlocks)
		{
			block.addRoundKey(key[0]);
		}
		
		// Process each block until all keys are used
		for (int round = 1; round < key.length - 1; round++)
		{
			for (AESBlock block : dataBlocks)
			{
				block.substituteBytes();
				block.shiftRows();
				block.mixColumns();
				block.addRoundKey(key[round]);
			}
		}
		
		for (AESBlock block : dataBlocks)
		{
			block.substituteBytes();
			block.shiftRows();
			block.addRoundKey(key[key.length - 1]);
		}
		
		int numberOfBlocks = dataBlocks.length;
		int bytesPerBlock = blockType.numberOfBytes;
		byte[] encryptedData = new byte[bytesPerBlock * numberOfBlocks];
		
		for (int index = 0; index < numberOfBlocks; index++)
		{
			AESBlock block = dataBlocks[index];
			byte[] dataArray = block.toByteArray();
			int position = index * bytesPerBlock;
			System.arraycopy(dataArray, 0, encryptedData, position,
					bytesPerBlock);
		}
		
		return encryptedData;
	}
	
	/**
	 * Decrypts the given data using AES protocol, with the {@link #key} and
	 * {@link #blockType} associated with this AESCryptographer.
	 * 
	 * @param data
	 *            Data to be decrypted
	 * @return Decrypted data as a String
	 */
	public String decrypt(byte[] data)
	{
		// Divide data into blocks for processing
		AESBlock[] dataBlocks = AESBlock.parseBlocks(data, blockType);
		
		// Process first round without mixColumns
		for (AESBlock block : dataBlocks)
		{
			block.addRoundKey(key[key.length - 1]);
			block.inverseShiftRows();
			block.invertBytes();
		}
		
		// Process each block until all keys are used
		// Process keys backwards
		for (int round = key.length - 2; round >= 1; round--)
		{
			for (AESBlock block : dataBlocks)
			{
				block.addRoundKey(key[round]);
				block.inverseMixColumns();
				block.inverseShiftRows();
				block.invertBytes();
			}
		}
		
		// Add first round key
		for (AESBlock block : dataBlocks)
		{
			block.addRoundKey(key[0]);
		}
		
		int numberOfBlocks = dataBlocks.length;
		int bytesPerBlock = blockType.numberOfBytes;
		byte[] decryptedData = new byte[bytesPerBlock * numberOfBlocks];
		
		for (int index = 0; index < numberOfBlocks; index++)
		{
			AESBlock block = dataBlocks[index];
			byte[] dataArray = block.toByteArray();
			int position = index * bytesPerBlock;
			System.arraycopy(dataArray, 0, decryptedData, position,
					bytesPerBlock);
		}
		
		// return Blocks.convertByteArrayToString(decryptedData);
		return new String(decryptedData);
	}
	
	/**
	 * Included for completeness.
	 * 
	 * @see #decrypt(byte[])
	 */
	public String decrypt(AESBlock data)
	{
		return decrypt(data.toByteArray());
	}
	
	/**
	 * Determines the number of rounds to be used for this type of encryption
	 * (determined by 6 + the max number of words in the key or data blocks),
	 * then expands the provided base key to generate one key for each round (a
	 * total of 1 + numberOfRounds, when including the base key)
	 * 
	 * @param key
	 *            Base key to be expanded
	 * @return Expanded key, as an AESBlock[] of round keys
	 * @throws InvalidBlockSizeException
	 *             Indicates an invalid {@link #keyType}
	 */
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
				currentBlock[word] = Blocks.xor(currentBlock[word - 1],
						previousBlock.getWord(word));
			}
		}
		
		return expandedKey;
	}
	
	/**
	 * This function was introduced in AES to ensure there are no weak keys.
	 * 
	 * Function G is a combination of three operations: --shift word left by 1;
	 * --substitute bytes using the SBox; --add the round constant to the
	 * current word (see {@link #getRelevantRoundConstant(int)})
	 * 
	 * Note: this method works on a copy of the provided byte array. As such,
	 * the changes to the input word will not be reflected in the given object.
	 * Changes will only be reflected in the returned array.
	 * 
	 * @param word
	 * @param relevantRoundConstant
	 * @return
	 */
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
	
	/**
	 * The round constant (Rcon) in AES is a word (4 bytes) in which the bottom
	 * three bytes are 0. As the bottom bytes are well known, this method is not
	 * designed to return them. Rather, it returns only the left-most (most
	 * relevant) byte (i.e. the only non-0 byte).
	 * 
	 * There is one round constant for each round of AES.
	 * 
	 * The most significant byte of a round constant is recursively defined as:
	 * Rcon[1] = 1; Rcon[index] = 2 * Rcon[i - 1];
	 * 
	 * @param roundIndex
	 * @return
	 */
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

	public AESBlock getKey()
	{
		return key[0];
	}
	
	
	
}
