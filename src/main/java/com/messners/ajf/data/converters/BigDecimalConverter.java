package com.messners.ajf.data.converters;

import java.math.BigDecimal;

import com.messners.ajf.data.ConversionException;
import com.messners.ajf.data.Converter;


/**
 * Default {@link Converter} implementation to convert a value object
 * into a <code>java.math.BigDecimal</code> instance.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public final class BigDecimalConverter implements Converter<BigDecimal> {


	private BigDecimal defaultValue;
	private boolean returnDefault;


	/**
	 * <p>Create a {@link Converter} to convert to and from
	 * <code>java.math.BigDecimal</code>.</p>
	 *
	 * Instances created with this constructor will throw a
	 * <code>ConversionException</code> if an error occurs during conversion.
	 */
	public BigDecimalConverter () {

		this.defaultValue  = null;
		this.returnDefault = false;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.math.BigDecimal</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the BigDecimal instance to use as the default value
	 */
	public BigDecimalConverter (BigDecimal defaultValue) {

		this.defaultValue = defaultValue;
		this.returnDefault = true;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.math.BigDecimal</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the double value to use as the default value
	 */
	public BigDecimalConverter (double defaultValue) {

		this.defaultValue = new BigDecimal(defaultValue);
		this.returnDefault = true;
	}


	/**
	 * Convert the provided object into an object of the specified type.
	 *
	 * @param value the value to be converted
	 * @exception ConversionException if an error occurs during conversion
	 */
	public BigDecimal convert (Object value) 
			throws ConversionException {

		if (value == null) {

			if (returnDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(new NullPointerException());
			}
		}

		if (value instanceof BigDecimal) {
			return ((BigDecimal)value);
		}

		try {

			if (value instanceof String) {
				return (new BigDecimal((String)value));
			} else {
				return (new BigDecimal(value.toString()));
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
