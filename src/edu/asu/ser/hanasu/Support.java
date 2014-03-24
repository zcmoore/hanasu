package edu.asu.ser.hanasu;

/**
 * General, reusable functions that are not specific to a package or type of
 * support (as is the case with edu.asu.ser.hanasu.encryption#Blocks)
 * 
 * @see edu.asu.ser.hanasu.encryption#Blocks
 * @author Moore, Zachary
 * 
 */
public class Support
{
	/**
	 * Interprets a string as if it were a sequence of bytes, notated in
	 * hexadecimal, and returns the result as a sequence of bytes. Note that
	 * each byte can represent 256 different numbers, which is the same amount
	 * of numbers that can be represented by two hexadecimal digits. As such,
	 * this algorithm will interpret each group of two characters as a single
	 * byte, with the exception of an "odd" character, that will be interpreted
	 * as a byte on its own (see examples below) The case of the string will be
	 * ignored.
	 * 
	 * For example, the input "AB10" would represent two words, representing the
	 * decimal equivalent of 171-016, thus producing the byte array {10101011,
	 * 00010000}
	 * 
	 * Note: the input "ab10" would produce the same result as "AB10"
	 * 
	 * The input "012K" should throw a NumberFormatException (see exceptions
	 * below; the value 'K' is not acceptable)
	 * 
	 * Also Note: This method differs from {@link Integer#decode(String)} in
	 * that this method is not limited to {@link Integer#MAX_VALUE}, and can be
	 * used with or without the "0x" prefix.
	 * 
	 * @param string
	 *            A string representing a sequence of bytes. Acceptable
	 *            characters are 0-9, A-F, and a-f. If the string length is odd
	 *            (e.g. "abc") then the last character will be interpreted as a
	 *            single digit hex-value (e.g. "0x0c"). The string can be
	 *            prefixed with "0x" or be left raw.
	 * @throws NumberFormatException
	 *             Indicates that a character of the provided string was outside
	 *             of the accepted hexadecimal range (acceptable values are the
	 *             numbers 0-9, and the letters A-F or a-f)
	 * @return A byte array representing this string, as if it were written in
	 *         hexadecimal
	 */
	public static byte[] InterpretStringAsHex(String string)
	{
		byte[] bytes;
		
		// Check if string begins with '0x'
		if ((string.length() >= 2) && string.substring(0, 2).equals("0x"))
		{
			// Remove "0x" prefix
			string = string.substring(2);
		}
		
		// Determine number of bytes
		// If string length is odd, account for last character
		int stringLength = string.length();
		int arrayLength;
		if ((stringLength & 1) == 0)
		{
			arrayLength = (stringLength / 2) + 1;
			bytes = new byte[arrayLength];
		}
		else
		{
			arrayLength = stringLength / 2;
			bytes = new byte[arrayLength];
		}
		
		// Determine each byte, where each byte consumes 2 characters
		for (int stringIndex = 0, arrayIndex = 0; stringIndex < stringLength; stringIndex += 2, arrayIndex++)
		{
			String subString = string.substring(stringIndex, stringIndex + 2);
			bytes[arrayIndex] = (byte) Integer.parseInt(subString, 16);
		}
		
		return bytes;
	}
}
