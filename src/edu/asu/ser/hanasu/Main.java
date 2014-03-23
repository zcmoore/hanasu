package edu.asu.ser.hanasu;

import java.util.Arrays;

import edu.asu.ser.hanasu.encryption.AESBlock;
import edu.asu.ser.hanasu.encryption.AESBlockType;
import edu.asu.ser.hanasu.encryption.InvalidBlockSizeException;

public class Main
{
	public static void main(String[] args) throws InvalidBlockSizeException
	{/*
		// byte[] testKey = Blocks.convertStringToByteArray("test key 16bytes");
		byte[] testKey = new byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
		//byte[] testData = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
		AESCryptographer cryptographer = new AESCryptographer(AESBlockType.BIT_128, testKey);
		
		//byte[] encrypted = cryptographer.encrypt(testData);
		byte[] encrypted = cryptographer.encrypt("this is a test");
		System.out.println(Arrays.toString(encrypted));
		
		String decrypted = cryptographer.decrypt(encrypted);
		System.out.println(decrypted);*/
		
		byte[] testData = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
		AESBlock testBlock = new AESBlock(testData, AESBlockType.BIT_128);
		
		System.out.println(Arrays.toString(testBlock.toByteArray()));
		
		testBlock.substituteBytes();
		System.out.println(Arrays.toString(testBlock.toByteArray()));
		
		testBlock.invertBytes();
		System.out.println(Arrays.toString(testBlock.toByteArray()));
		
		testBlock.shiftRows();
		System.out.println(Arrays.toString(testBlock.toByteArray()));
		
		testBlock.inverseShiftRows();
		System.out.println(Arrays.toString(testBlock.toByteArray()));
		
		testBlock.mixColumns();
		System.out.println(Arrays.toString(testBlock.toByteArray()));
		
		testBlock.inverseMixColumns();
		System.out.println(Arrays.toString(testBlock.toByteArray()));
	}
}
