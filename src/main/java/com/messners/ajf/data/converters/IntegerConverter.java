package com.messners.ajf.data.converters;

import java.lang.Integer;

import com.messners.ajf.data.ConversionException;
import com.messners.ajf.data.Converter;


/**
 * Default {@link Converter} implementation to convert a value object
 * into a <code>java.lang.Integer</code> instance.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public final class IntegerConverter implements Converter<Integer> {


	private int radix;
	private Integer defaultValue;
	private boolean returnDefault;


	/**
	 * <p>Create a {@link Converter} to convert to and from
	 * <code>java.lang.Integer</code>.</p>
	 *
	 * Instances created with this constructor will throw a
	 * <code>ConversionException</code> if an error occurs during conversion.
	 */
	public IntegerConverter () {

		this.defaultValue  = null;
		this.returnDefault = false;
		radix = 10;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Integer</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the Integer instance to use as the default value
	 */
	public IntegerConverter (Integer defaultValue) { 

		this.defaultValue = defaultValue;
		this.returnDefault = true;
		radix = 10;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Integer</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param defaultValue the int value to use as the default value
	 */
	public IntegerConverter (int defaultValue) {

		this.defaultValue = new Integer(defaultValue);
		this.returnDefault = true;
		radix = 10;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Integer</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the Integer instance to use as the default value
	 * @param radix        the radix to use when doing String conversions
	 */
	public IntegerConverter (Integer defaultValue, int radix) { 

		this.defaultValue = defaultValue;
		this.returnDefault = true;
		this.radix = radix;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Integer</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param defaultValue the int value to use as the default value
	 * @param radix        the radix to use when doing String conversions
	 */
	public IntegerConverter (int defaultValue, int radix) {

		this.defaultValue = new Integer(defaultValue);
		this.returnDefault = true;
		this.radix = radix;
	}


	/**
	 * Convert the provided object into an object of the specified type.
	 *
	 * @param value the value to be converted
	 * @exception ConversionException if an error occurs during conversion
	 */
	public Integer convert (Object value) throws ConversionException {

		if (value == null) {

			if (returnDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(new NullPointerException());
			}
		}

		if (value instanceof Integer) {
			return ((Integer)value);
		}

		try {

			if (value instanceof String) {
				return (Integer.valueOf((String)value, radix));
			} else {
				return (Integer.valueOf(value.toString(), radix));
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

		Integer intValue = (Integer)convert(value);

		if (radix == 10 || radix == 0) {
			return (intValue.toString());
		}

		return (Integer.toString(intValue.intValue(), radix));
	}
}
