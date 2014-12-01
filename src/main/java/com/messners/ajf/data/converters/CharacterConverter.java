package com.messners.ajf.data.converters;

import com.messners.ajf.data.ConversionException;
import com.messners.ajf.data.Converter;


/**
 * Default {@link Converter} implementation to convert a value object
 * into a <code>java.lang.Character</code> instance.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public final class CharacterConverter implements Converter<Character> {


	private Character defaultValue;
	private boolean returnDefault;


	/**
	 * <p>Create a {@link Converter} to convert to and from
	 * <code>java.lang.Character</code>.</p>
	 *
	 * Instances created with this constructor will throw a
	 * <code>ConversionException</code> if an error occurs during conversion.
	 */
	public CharacterConverter () {

		this.defaultValue  = null;
		this.returnDefault = false;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Character</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param defaultValue the Character instance to use as the default value
	 */
	public CharacterConverter (Character defaultValue) {

		this.defaultValue = defaultValue;
		this.returnDefault = true;
	}


	/**
	 * Create a {@link Converter} to convert to and from
	 * <code>java.lang.Character</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param defaultValue the char value to use as the default value
	 */
	public CharacterConverter (char defaultValue) {

		this.defaultValue = new Character(defaultValue);
		this.returnDefault = true;
	}


	/**
	 * Convert the provided object into an object of the specified type.
	 *
	 * @param value the value to be converted
	 * @exception ConversionException if an error occurs during conversion
	 */
	public Character convert (Object value) 
			throws ConversionException {

		if (value == null) {

			if (returnDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(new NullPointerException());
			}
		}

		if (value instanceof Character) {
			return (Character) (value);
		}

		try {

			if (value instanceof String) {
				return (new Character(((String)value).charAt(0)));
			} else {
				return (new Character(value.toString().charAt(0)));
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

			if (value instanceof Character) {
				return (value.toString());
			} else if (value instanceof String) {
				return (new Character(((String)value).charAt(0)).toString());
			} else {
				return (new Character(value.toString().charAt(0)).toString());
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}
}
