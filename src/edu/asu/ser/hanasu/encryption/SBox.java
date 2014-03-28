package edu.asu.ser.hanasu.encryption;

import java.net.URL;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Base representation of a substitution box, generally associated with block
 * ciphers. All S-boxes are designed for byte substitution, and as such must
 * have 256 entries (one for each byte).
 * 
 * In addition to the forward substitution, an inverted table is provided, to be
 * used in decryption.
 * 
 * @see RijndaelSBox
 * @author Moore, Zachary
 * 
 */
public class SBox
{
	/**
	 * Contents of this box, used for substitution. The size of boxValues will
	 * always be 256, as it contains a substitution for every byte value (total
	 * combinations: 256)
	 */
	protected final byte[] boxValues;
	
	/**
	 * Inverse of this box, used for inverse substitution. The size of
	 * inverseValues will always be the size of {@link #boxValues}
	 */
	protected final byte[] inverseValues;
	
	/**
	 * Base constructor for all SBox implementations. Ensures the size of
	 * boxValues is appropriate (256). The size of boxValues cannot be altered.
	 * 
	 * Protected to ensure that all SBoxes follow the rules of an SBox (namely
	 * that it is generated from a galois field).
	 * 
	 * @see #generateSBox(int, int)
	 */
	protected SBox()
	{
		// Box must have a substitution value for each value of a byte
		boxValues = new byte[16 * 16];
		inverseValues = new byte[boxValues.length];
	}
	
	/**
	 * Loads an SBox from the given filePath. Accepted formats are a written
	 * SBox object, or a written array, or a raw array, formatted as
	 * "[value0, value1, value2...]" or "{value0, value1, value2...}"
	 * 
	 * The inverse of the SBox will be loaded from the file only if the file
	 * contains an SBox object. Otherwise, the inverse will be generated using
	 * {@link #setupInverseBox()}
	 * 
	 * @param filePath
	 * @return
	 */
	public static SBox loadSBox(URL filePath)
	{
		// TODO: implement loading from file URL
		throw new NotImplementedException();
	}
	
	/**
	 * This method will return a call to {@link #loadSBox(URL)} after creating a
	 * URL from the given resource path.
	 * 
	 * @see #loadSBox(URL)
	 */
	public static SBox loadSBox(String filePath)
	{
		// TODO: implement loading from file
		throw new NotImplementedException();
	}
	
	/**
	 * Generates an SBox from the provided Galois Field (i.e. Finite Field),
	 * GF(base**exponent).
	 * 
	 * @param galoisBase
	 *            The Galois prime to be used in generating a Galois Field
	 * @param galoisExponent
	 *            The Galois prime to be used in generating a Galois Field
	 * @return An SBox that was generated from the provided Galois Field
	 *         Information.
	 */
	public static SBox generateSBox(int galoisBase, int galoisExponent)
	{
		// TODO: implement SBox generation
		throw new NotImplementedException();
	}
	
	/**
	 * Creates an inverseBox using the values currently in {@link #boxValues}.
	 * 
	 * More specifically, it updates the values in {@link #inverseValues} such
	 * that calling invert(substitute(inputByte)) will return inputByte.
	 * 
	 * For each index (index) and value (value) such that boxValues[index] =
	 * value, the inverseValues array will be set such that inverseValues[value]
	 * = index.
	 */
	public void setupInverseBox()
	{
		// Setup inverse box such that inverse[value] = index applies to all
		// boxValue[index] = value
		for (int index = 0; index < boxValues.length; index++)
		{
			byte baseValue = boxValues[index];
			
			// Make value unsigned by masking off all but the bottom 8 bits
			int value = ((int) baseValue) & 0xFF;
			inverseValues[value] = (byte) index;
		}
	}
	
	/**
	 * @return The number of entries in this SBox.
	 */
	public int size()
	{
		return boxValues.length;
	}
	
	/**
	 * Substitutes a byte with its corresponding SBox value. This can be undone
	 * with {@link #invert(byte)}
	 * 
	 * @param inputByte
	 *            The byte to substitute
	 * @return The byte corresponding to the input byte (i.e. its substitution)
	 */
	public byte substitute(byte inputByte)
	{
		return boxValues[(((int) inputByte) & 0xFF)];
	}
	
	/**
	 * Equivalent to a call to {@link #substitute(byte)}.
	 * 
	 * @param inputByte
	 *            The byte to substitute, as a char
	 * @return The byte corresponding to the input byte (i.e. its substitution)
	 */
	public byte substitute(char inputByte)
	{
		return substitute((byte) inputByte);
	}
	
	/**
	 * Substitutes a byte with its inverse value, specified by
	 * {@link #inverseValues}.
	 * 
	 * Note: {@link #invert(byte)} and {@link #substitute(byte)} are inverse
	 * operations, and as such, calling {@link #substitute(byte)} and then
	 * {@link #invert(byte)} will return the original byte.
	 * 
	 * @param inputByte
	 *            The byte to invert
	 * @return The inverse of the input byte. That is, a value produces the
	 *         input value when {@link #substitute(byte)} is called
	 */
	public byte invert(byte inputByte)
	{
		return inverseValues[(((int) inputByte) & 0xFF)];
	}
	
	/**
	 * Equivalent to a call to {@link #invert(byte)}.
	 * 
	 * @param inputByte
	 *            The byte to invert, as a char
	 * @return The inverse of the input byte. That is, a value produces the
	 *         input value when {@link #substitute(byte)} is called
	 */
	public byte invert(char inputByte)
	{
		return invert((byte) inputByte);
	}
}
