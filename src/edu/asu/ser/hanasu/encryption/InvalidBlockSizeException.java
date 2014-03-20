package edu.asu.ser.hanasu.encryption;

/**
 * Exception related to BlockCyphers and Block encryption techniques. This
 * exception indicates an invalid block size, which generally indicates an
 * unsupported block size (e.g. AES only supports sizes of 128-bits, 192-bits,
 * and 256-bits).
 * 
 * @author Moore, Zachary
 * 
 */
public class InvalidBlockSizeException extends Exception
{
	private static final long serialVersionUID = -7951057370633114075L;
	
	/**
	 * @see Exception#Exception()
	 */
	public InvalidBlockSizeException()
	{
		super();
	}
	
	/**
	 * @see Exception#Exception(String, Throwable, boolean, boolean)
	 */
	public InvalidBlockSizeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public InvalidBlockSizeException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	/**
	 * @see Exception#Exception(String)
	 */
	public InvalidBlockSizeException(String message)
	{
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public InvalidBlockSizeException(Throwable cause)
	{
		super(cause);
	}
	
}
