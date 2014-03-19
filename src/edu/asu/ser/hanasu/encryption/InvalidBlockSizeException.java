package edu.asu.ser.hanasu.encryption;

public class InvalidBlockSizeException extends Exception
{
	private static final long serialVersionUID = -7951057370633114075L;

	public InvalidBlockSizeException()
	{
		super();
	}

	public InvalidBlockSizeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidBlockSizeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidBlockSizeException(String message)
	{
		super(message);
	}

	public InvalidBlockSizeException(Throwable cause)
	{
		super(cause);
	}
	
	
}
