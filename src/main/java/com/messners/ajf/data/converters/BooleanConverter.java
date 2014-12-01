package com.messners.ajf.data.converters;

import com.messners.ajf.data.ConversionException;
import com.messners.ajf.data.Converter;

import java.util.HashMap;


/**
 * Default {@link Converter} implementation to convert a value object
 * into a <code>Boolean</code> instance.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public final class BooleanConverter implements Converter<Boolean> {


	private Boolean defaultValue;
	private boolean returnDefault;


	static private HashMap<String, Boolean> boolMap;
	static {

		boolMap = new HashMap<String, Boolean>();
		boolMap.put("true",  Boolean.TRUE);
		boolMap.put("yes",   Boolean.TRUE);
		boolMap.put("on",	 Boolean.TRUE);
		boolMap.put("y",	 Boolean.TRUE);
		boolMap.put("1",	 Boolean.TRUE);
		boolMap.put("false", Boolean.FALSE);
		boolMap.put("off",   Boolean.FALSE);
		boolMap.put("no",	 Boolean.FALSE);
		boolMap.put("n",	 Boolean.FALSE);
		boolMap.put("0",	 Boolean.FALSE);
	}


	/**
	 * <p>Create a {@link Converter} to convert to and from
	 * <code>java.lang.Boolean</code>.</p>
	 *
	 * Instances created with this constructor will throw a
	 * <code>ConversionException</code> if an error occurs during conversion.
	 */
	public BooleanConverter () {

		this.defaultValue  = null;
		this.returnDefault = false;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Boolean</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the Boolean instance to use as the default value
	 */
	public BooleanConverter (Boolean defaultValue) {

		this.defaultValue = defaultValue;
		this.returnDefault = true;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Boolean</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the boolean value to use as the default value
	 */
	public BooleanConverter (boolean defaultValue) {

		this.defaultValue = new Boolean(defaultValue);
		this.returnDefault = true;
	}


	/**
	 * Convert the provided object into an object of the specified type.
	 *
	 * @param value the value to be converted
	 * @exception ConversionException if an error occurs during conversion
	 */
	public Boolean convert (Object value) throws ConversionException {

		if (value == null) {

			if (returnDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(new NullPointerException());
			}
		}

		if (value instanceof Boolean) {
			return (Boolean) (value);
		}

		Boolean bool = (Boolean)boolMap.get(value);
		if (bool != null) {
			return (bool);
		}

		String s = value.toString().toLowerCase();
		bool = (Boolean)boolMap.get(s);
		if (bool != null) {
			return (bool);
		}

		if (returnDefault) {
			return (defaultValue);
		} else {
			throw new ConversionException(s + " is not a valid boolean value");
		}
	}


	/**
	 * Convert the provided object to a Boolean instance.
	 *
	 * @param  value the object to convert
	 * @return the converted Boolean instance
	 * @exception ConversionException if value is not a boolean String
	 */
	public static Boolean toBoolean (Object value) 
			throws ConversionException {
	
		if (value instanceof Boolean) {
			return ((Boolean)value);
		}

		Boolean bool = (Boolean)boolMap.get(value);
		if (bool != null) {
			return (bool);
		}

		String s = value.toString().toLowerCase();
		bool = (Boolean)boolMap.get(s);
		if (bool != null) {
			return (bool);
		}

		throw new ConversionException(s + " is not a valid boolean value");
	}


	/**
	 * Convert the passed value to a String.
	 *
	 * @return the converted String
	 * @throws ConversionException if value is not the proper type
	 */
	public String toString (Object value) throws ConversionException {

		Boolean bool = toBoolean(value);
		return (bool.toString());
	}
}
