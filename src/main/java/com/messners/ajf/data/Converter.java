package com.messners.ajf.data;

/**
 * This interface defines a type conversion interface to simplify
 * converting between various types.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface Converter<T> {


	/**
	 * Convert the provided object into an object of the specified type.
	 *
	 * @param value the value to be converted
	 * @exception ConversionException if an error occurs during conversion
	 */
    public T convert (Object value) throws ConversionException ;
	
	/**
	 * Convert the passed value to a String.
	 *
	 * @param  value the Object to convert to a String
	 * @return the converted String
	 * @throws ConversionException if value is not the proper type
	 */
	public String toString (Object value) throws ConversionException;
}
