package edu.asu.ser.hanasu;

import java.awt.EventQueue;

import edu.asu.ser.hanasu.screens.ClientContainer;
import edu.asu.ser.hanasu.screens.ScreenManager;

public class Main
{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new SetupRunnable());
	}
	
	private static class SetupRunnable implements Runnable
	{
		public void run()
		{
			ScreenManager initialManager = ScreenManager.createManager();
		}
	}
	
	/*
	 * public static void main(String[] args) throws InvalidBlockSizeException {
	 * // byte[] testKey = Blocks.convertStringToByteArray("test key 16bytes");
	 * byte[] testKey = new byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
	 * //byte[] testData = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
	 * 14, 15, 16}; byte[] testData = new byte[]{1, 2, 3, 4, 0, 0, 0, 0, 0, 0,
	 * 0, 0, 0, 0, 0, 0}; AESCryptographer cryptographer = new
	 * AESCryptographer(AESBlockType.BIT_128, testKey);
	 * 
	 * //byte[] encrypted = cryptographer.encrypt(testData); byte[] encrypted =
	 * cryptographer.encrypt("this is a test");
	 * System.out.println(Arrays.toString(encrypted));
	 * 
	 * String decrypted = cryptographer.decrypt(encrypted);
	 * System.out.println(decrypted);
	 * 
	 * //byte[] testData = Blocks.convertStringToByteArray("this is a test");
	 * AESBlock testBlock = new AESBlock(testData, AESBlockType.BIT_128);
	 * 
	 * System.out.println(Arrays.toString(testBlock.toByteArray()));
	 * 
	 * testBlock.substituteBytes();
	 * System.out.println(Arrays.toString(testBlock.toByteArray()));
	 * 
	 * testBlock.invertBytes();
	 * System.out.println(Arrays.toString(testBlock.toByteArray()));
	 * 
	 * testBlock.shiftRows();
	 * System.out.println(Arrays.toString(testBlock.toByteArray()));
	 * 
	 * testBlock.inverseShiftRows();
	 * System.out.println(Arrays.toString(testBlock.toByteArray()));
	 * 
	 * testBlock.mixColumns();
	 * System.out.println(Arrays.toString(testBlock.toByteArray()));
	 * 
	 * testBlock.inverseMixColumns();
	 * System.out.println(Arrays.toString(testBlock.toByteArray()));
	 * 
	 * System.out.println("\n\n");
	 * 
	 * 
	 * testBlock = new AESBlock(testData, AESBlockType.BIT_128);
	 * System.out.println(Arrays.toString(testBlock.toByteArray()));
	 * 
	 * 
	 * String rawData =
	 * "I love unicorns, and goats, and dragons, and ima lochness monster";
	 * byte[] key = new String("naw, ima bear222").getBytes(); AESCryptographer
	 * crypto = new AESCryptographer(AESBlockType.BIT_128, key); byte[]
	 * encrypted = crypto.encrypt(rawData);
	 * 
	 * System.out.println("Raw: " + rawData); System.out.println("Encrypted: " +
	 * new String(encrypted)); System.out.println("Decrypted: " +
	 * crypto.decrypt(encrypted));
	 * 
	 * }
	 */
}
