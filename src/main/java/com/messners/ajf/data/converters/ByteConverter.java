package com.messners.ajf.data.converters;

import com.messners.ajf.data.ConversionException;
import com.messners.ajf.data.Converter;

/**
 * Default {@link Converter} implementation to convert a value object
 * into a <code>java.lang.Byte</code> instance.
 *
 * @author  Greg Messner <gmessner@messners.com>
 * @version $Revision: 1.6 $
 */
public final class ByteConverter implements Converter<Byte> {


	private Byte defaultValue;
	private boolean returnDefault;


	/**
	 * <p>Create a {@link Converter} to convert to and from
	 * <code>java.lang.Byte</code>.</p>
	 *
	 * Instances created with this constructor will throw a
	 * <code>ConversionException</code> if an error occurs during conversion.
	 */
	public ByteConverter () {

		this.defaultValue  = null;
		this.returnDefault = false;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Byte</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the Byte instance to use as the default value
	 */
	public ByteConverter (Byte defaultValue) {

		this.defaultValue = defaultValue;
		this.returnDefault = true;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Byte</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the byte value to use as the default value
	 */
	public ByteConverter (byte defaultValue) {

		this.defaultValue = new Byte(defaultValue);
		this.returnDefault = true;
	}


	/**
	 * Convert the provided object into an object of the specified type.
	 *
	 * @param value the value to be converted
	 * @exception ConversionException if an error occurs during conversion
	 */
	public Byte convert (Object value) throws ConversionException {

		if (value == null) {

			if (returnDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(new NullPointerException());
			}
		}

		if (value instanceof Byte) {
			return (Byte) (value);
		}

		try {

			if (value instanceof String) {
				return (new Byte((String)value));
			} else {
				return (new Byte(value.toString()));
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

			if (value instanceof Byte) {
				return (value.toString());
			} else if (value instanceof String) {
				return (new Byte((String)value).toString());
			} else {
				return (new Byte(value.toString()).toString());
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}
}
