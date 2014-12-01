package com.messners.ajf.data.converters;

import com.messners.ajf.data.ConversionException;
import com.messners.ajf.data.Converter;
import com.messners.ajf.data.Octal;


/**
 * Default {@link Converter} implementation to convert a value object
 * into a <code>java.lang.Integer</code> instance.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public final class OctalConverter implements Converter<Octal> {


	private Octal defaultValue;
	private boolean returnDefault;


	/**
	 * <p>Create a {@link Converter} to convert to and from
	 * <code>Octal</code>.</p>
	 *
	 * Instances created with this constructor will throw a
	 * <code>ConversionException</code> if an error occurs during conversion.
	 */
	public OctalConverter () {

		this.defaultValue  = null;
		this.returnDefault = false;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>Octal</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the Octal instance to use as the default value
	 */
	public OctalConverter (Octal defaultValue) { 

		this.defaultValue = defaultValue;
		this.returnDefault = true;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>Octal</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param defaultValue the int value to use as the default value
	 */
	public OctalConverter (int defaultValue) {

		this.defaultValue = new Octal(defaultValue);
		this.returnDefault = true;
	}


	/**
	 * Convert the provided object into an object of the specified type.
	 *
	 * @param value the value to be converted
	 * @exception ConversionException if an error occurs during conversion
	 */
	public Octal convert (Object value) throws ConversionException {

		if (value == null) {

			if (returnDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(new NullPointerException());
			}
		}

		if (value instanceof Octal) {
			return (Octal) (value);
		}

		try {

			if (value instanceof String) {
				return (new Octal((String)value));
			} else {
				return (new Octal(value.toString()));
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
		return (convert(value).toString());
	}
}
