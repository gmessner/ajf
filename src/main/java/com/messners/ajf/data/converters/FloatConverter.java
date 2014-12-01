package com.messners.ajf.data.converters;

import java.lang.Float;

import com.messners.ajf.data.ConversionException;
import com.messners.ajf.data.Converter;


/**
 * Default {@link Converter} implementation to convert a value object
 * into a <code>java.lang.Float</code> instance.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public final class FloatConverter implements Converter<Float> {

	private Float defaultValue;
	private boolean returnDefault;


	/**
	 * <p>Create a {@link Converter} to convert to and from
	 * <code>java.lang.Float</code>.</p>
	 *
	 * Instances created with this constructor will throw a
	 * <code>ConversionException</code> if an error occurs during conversion.
	 */
	public FloatConverter () {

		this.defaultValue  = null;
		this.returnDefault = false;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Float</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the Float instance to use as the default value
	 */
	public FloatConverter (Float defaultValue) {

		this.defaultValue = defaultValue;
		this.returnDefault = true;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Float</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param defaultValue the float vlaue to use as the default value
	 */
	public FloatConverter (float defaultValue) {

		this.defaultValue = new Float(defaultValue);
		this.returnDefault = true;
	}


	/**
	 * Convert the provided object into an object of the specified type.
	 *
	 * @param value the value to be converted
	 * @exception ConversionException if an error occurs during conversion
	 */
	public Float convert (Object value) throws ConversionException {

		if (value == null) {

			if (returnDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(new NullPointerException());
			}
		}

		if (value instanceof Float) {
			return (Float) (value);
		}

		try {

			if (value instanceof String) {
				return (new Float((String)value));
			} else {
				return (new Float(value.toString()));
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

			if (value instanceof Float) {
				return (value.toString());
			} else if (value instanceof String) {
				return (new Float((String)value).toString());
			} else {
				return (new Float(value.toString()).toString());
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}
}
