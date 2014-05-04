package unit_tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.asu.ser.hanasu.encryption.AESBlockType;
import edu.asu.ser.hanasu.encryption.AESCryptographer;

public class TestAESCryptographer
{
	@Test
	public void testCryptography()
	{
		for (int keyIndex = 0; keyIndex < 100; keyIndex++)
		{
			AESBlockType type = AESBlockType.BIT_128;
			byte[] key = TestAESBlock.generateRandomByteArray(type.size());
			
			for (int stringLength = 0; stringLength < 100; stringLength++)
			{
				for (int testNumber = 0; testNumber < 1; testNumber++)
				{
					//@formatter:off
					byte[] stringData = TestAESBlock.generateRandomByteArray(stringLength);
					for (int i = 0; i < stringData.length; i++)
					{
						// Force plain text to be in the std character range
						stringData[i] = (byte) (stringData[i] % 42 + 48);
					}
					AESCryptographer cryptographer = new AESCryptographer(type,key);
					//@formatter:on
					
					String string = new String(stringData);
					byte[] encryptedData = cryptographer.encrypt(string);
					
					String decryptedData = cryptographer.decrypt(encryptedData);
					assertEquals(string.trim(), decryptedData.trim());
				}
			}
		}
		
	}
	
}
