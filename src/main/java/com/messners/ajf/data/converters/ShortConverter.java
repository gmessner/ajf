package com.messners.ajf.data.converters;

import java.lang.Short;

import com.messners.ajf.data.ConversionException;
import com.messners.ajf.data.Converter;


/**
 * Default {@link Converter} implementation to convert a value object
 * into a <code>java.lang.Short</code> instance.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public final class ShortConverter implements Converter<Short> {

	private Short defaultValue;
	private boolean returnDefault;


	/**
	 * <p>Create a {@link Converter} to convert to and from
	 * <code>java.lang.Short</code>.</p>
	 *
	 * Instances created with this constructor will throw a
	 * <code>ConversionException</code> if an error occurs during conversion.
	 */
	public ShortConverter () {

		this.defaultValue  = null;
		this.returnDefault = false;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Short</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the Short instance to use as the default value
	 */
	public ShortConverter (Short defaultValue) {

		this.defaultValue = defaultValue;
		this.returnDefault = true;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Short</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the short value to use as the default value
	 */
	public ShortConverter (short defaultValue) {

		this.defaultValue = new Short(defaultValue);
		this.returnDefault = true;
	}


	/**
	 * Convert the provided object into an object of the specified type.
	 *
	 * @param value the value to be converted
	 * @exception ConversionException if an error occurs during conversion
	 */
	public Short convert (Object value) throws ConversionException {

		if (value == null) {

			if (returnDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(new NullPointerException());
			}
		}

		if (value instanceof Short) {
			return (Short) (value);
		}

		try {

			if (value instanceof String) {
				return (new Short((String)value));
			} else {
				return (new Short(value.toString()));
			}

		} catch (Exception e) {

			if (returnDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(e);
			}
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

			if (value instanceof Short) {
				return (value.toString());
			} else if (value instanceof String) {
				return (new Short((String)value).toString());
			} else {
				return (new Short(value.toString()).toString());
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}
}
