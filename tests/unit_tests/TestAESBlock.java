package unit_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import edu.asu.ser.hanasu.encryption.AESBlock;
import edu.asu.ser.hanasu.encryption.AESBlockType;
import edu.asu.ser.hanasu.encryption.InvalidBlockSizeException;

public class TestAESBlock
{
	Random random = new Random();
	
	@Test
	public void testEmptyConstruction()
	{
		AESBlockType type = AESBlockType.BIT_128;
		AESBlock block = new AESBlock(type);
		byte[] data = block.toByteArray();
		
		// Verify that the new block contains the appropriate amount of data
		assertEquals(type.size(), data.length);
		
		// Verify that the data is defaulted to 0
		for (byte element : data)
		{
			assertEquals(0, element);
		}
	}
	
	@Test
	public void testTargetedConstructionProperSize()
	{
		AESBlockType type = AESBlockType.BIT_128;
		
		// Run 1000 tests on AESBlock(byte[], AESBlockType)
		for (int i = 0; i < 1000; i++)
		{
			// Generate a properly sized array (length == type.size())
			byte[] intendedData = generateRandomByteArray(type.size());
			AESBlock block = new AESBlock(intendedData, type);
			byte[] data = block.toByteArray();
			
			// Verify that the new block contains the appropriate amount of data
			assertEquals(type.size(), data.length);
			
			// Verify that the data is as intended
			for (int index = 0; index < data.length; index++)
			{
				assertEquals(intendedData[index], data[index]);
			}
		}
	}
	
	@Test
	public void testTargetedConstructionTooSmall()
	{
		AESBlockType type = AESBlockType.BIT_128;
		
		// Run 1000 tests on AESBlock(byte[], AESBlockType)
		for (int i = 0; i < 1000; i++)
		{
			// Generate an array smaller than the block size
			int offset = random.nextInt(type.size() - 1);
			byte[] intendedData = generateRandomByteArray(type.size() - offset);
			AESBlock block = new AESBlock(intendedData, type);
			byte[] data = block.toByteArray();
			
			// Verify that the new block contains the appropriate amount of data
			assertEquals(type.size(), data.length);
			
			// Verify that the data is as intended
			for (int index = 0; index < intendedData.length; index++)
			{
				assertEquals(intendedData[index], data[index]);
			}
			
			for (int index = intendedData.length; index < data.length; index++)
			{
				assertEquals(0, data[index]);
			}
		}
	}
	
	@Test
	public void testInvalidConstruction()
	{
		AESBlockType type = AESBlockType.BIT_128;
		
		// Run 1000 tests on AESBlock(byte[], AESBlockType)
		for (int i = 0; i < 1000; i++)
		{
			byte[] intendedData = generateRandomByteArray(type.size() + 1);
			assertInvalidBlockSizeException(intendedData, type);
		}
	}
	
	private void assertInvalidBlockSizeException(byte[] data, AESBlockType type)
	{
		try
		{
			new AESBlock(data, type);
			fail("expected exception to be thrown " + Arrays.toString(data));
		}
		catch (InvalidBlockSizeException e)
		{
			// proper exception was thrown
		}
		catch (Exception e)
		{
			fail("unexpected exception");
		}
	}
	
	static byte[] generateRandomByteArray(int length)
	{
		byte[] bytes = new byte[length];
		Random random = new Random();
		
		for (int index = 0; index < length; index++)
		{
			bytes[index] = (byte) (random.nextInt(256) - 128);
		}
		
		return bytes;
	}
	
	@Test
	public void testParseBlocks()
	{
		AESBlockType type = AESBlockType.BIT_128;
		
		// Run 1000 tests
		for (int i = 0; i < 1000; i++)
		{
			// Test various sizes, up to 5* the block size
			for (int numBytes = 0; numBytes < 5 * type.size(); numBytes++)
			{
				double byteSizeRatio = (double) numBytes / (double) type.size();
				int expectedNumberOfBlocks = (int) Math.ceil(byteSizeRatio);
				
				byte[] intendedData = generateRandomByteArray(numBytes);
				AESBlock[] blocks = AESBlock.parseBlocks(intendedData, type);
				
				assertEquals(expectedNumberOfBlocks, blocks.length);
				
				// Verify block contents
				for (int blockIndex = 0; blockIndex < expectedNumberOfBlocks; blockIndex++)
				{
					byte[] blockContents = blocks[blockIndex].toByteArray();
					for (int index = 0; index < type.size(); index++)
					{
						int dataIndex = blockIndex * type.size() + index;
						
						//@formatter:off
						if (dataIndex < intendedData.length)
							assertEquals(intendedData[dataIndex], blockContents[index]);
						else
							assertEquals(0, blockContents[index]);
						//@formatter:on
					}
				}
			}
			
		}
	}
	
	@Test
	public void testData()
	{
		AESBlockType type = AESBlockType.BIT_128;
		
		// Test various data sizes
		for (int dataSize = 0; dataSize <= type.size(); dataSize++)
		{
			// Generate an array of size dataSize
			byte[] intendedData = generateRandomByteArray(dataSize);
			AESBlock block = new AESBlock(intendedData, type);
			byte[][] data = block.getData();
			
			// Verify that the block is rectangular and matches type
			for (int rowIndex = 0; rowIndex < type.numberOfRows(); rowIndex++)
			{
				// Test data[][]
				assertEquals(type.numberOfColumns(), data[rowIndex].length);
				
				// Test word(index)
				assertEquals(type.wordLength(), block.getWord(rowIndex).length);
			}
			
			// Verify that the block contains the appropriate number of elements
			assertEquals(type.numberOfRows(), data.length);
			assertEquals(type.numberOfColumns(), data[0].length);
			
			// Verify that the data is as intended
			for (int index = 0; index < intendedData.length; index++)
			{
				int rowIndex = index % type.numberOfRows();
				int columnIndex = index / type.numberOfColumns();
				assertEquals(intendedData[index], data[rowIndex][columnIndex]);
			}
			
			for (int index = intendedData.length; index < data.length; index++)
			{
				int rowIndex = index % type.numberOfRows();
				int columnIndex = index / type.numberOfColumns();
				assertEquals(0, data[rowIndex][columnIndex]);
			}
		}
	}
	
}
