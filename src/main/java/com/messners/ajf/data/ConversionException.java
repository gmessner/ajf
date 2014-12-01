package com.messners.ajf.data;

/**
 * This class is used to report exceptions that occur during data conversion.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ConversionException extends Exception {

	private static final long serialVersionUID = 1L;


	/**
	 * Create a new <code>ConversionException</code>.
	 *
	 * @param message the error or warning message
	 */
	public ConversionException (String message) {

		super(message);
	}
	
	
	/**
	 * Create a new <code>ConversionException</code> wrapping an
	 * existing exception.
	 *
	 * <p>The existing exception will be embedded in the new
	 * one, and its message will become the default message for
	 * the ConversionException.</p>
	 *
	 * @param e the exception to be wrapped in a ConversionException
	 */
	public ConversionException (Exception e) {

		super(e);
	}
	
	
	/**
	 * Create a new <code>ConversionException</code> from an existing exception.
	 *
	 * <p>The existing exception will be embedded in the new
	 * one, but the new exception will have its own message.</p>
	 *
	 * @param message the detail message
	 * @param e the exception to be wrapped in a ConversionException.
	 */
	public ConversionException (String message, Exception e) {

		super(message, e);
	}
}
