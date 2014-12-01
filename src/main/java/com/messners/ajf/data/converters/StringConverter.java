package com.messners.ajf.data.converters;

import com.messners.ajf.data.ConversionException;
import com.messners.ajf.data.Converter;


/**
 * Default {@link Converter} implementation to convert a value object
 * into a String instance.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public final class StringConverter implements Converter<String> {

    /**
     * Create a {@link Converter} to convert to and from
	  * <code>java.lang.String</code>.
     */
    public StringConverter () {
    }


    /**
     * Convert the provided object into an object of the specified type.
     *
     * @param value the value to be converted
     * @exception ConversionException if an error occurs during conversion
     */
    public String convert (Object value) throws ConversionException {

        if (value == null) {
            return ((String)null);
        } else {
            return (value.toString());
        }
    }


	/**
	 * Convert the passed value to a String.
	 *
	 * @param  value the Object to convert to a String
	 * @return the converted String
	 * @throws ConversionException if value is not the proper type
	 */
	public String toString (Object value) throws ConversionException {

		try {

			if (value instanceof String) {
				return ((String)value);
			} else {
				return (value.toString());
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}
}
