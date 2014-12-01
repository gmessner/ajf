package com.messners.ajf.data.converters;

import com.messners.ajf.data.ConversionException;
import com.messners.ajf.data.Converter;
import com.messners.ajf.reflect.ClassUtils;


/**
 * Default {@link Converter} implementation to convert a value object
 * into a <code>java.lang.Class</code> instance. It assumes that
 * the value object will provide the name of a class when toString()
 * is called.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public final class ClassConverter implements Converter<Class<?>> {

	private Class<?> defaultValue;
	private boolean returnDefault;


	/**
	 * <p>Create a {@link Converter} to convert to an instance of
	 * <code>java.lang.Class</code>.</p>
	 *
	 * Instances created with this constructor will throw a
	 * <code>ConversionException</code> if an error occurs during conversion
	 */
	public ClassConverter () {

		this.defaultValue  = null;
		this.returnDefault = false;
	}


	/**
	 * Create a {@link Converter} to convert to an instance of
	 * <code>java.lang.Class</code>. This constructor sets up
	 * this converter to return the provided default value if the
	 * value provided to the <code>convert()</code> method is null
	 * or an exception occurs during conversion.
	 *
	 * @param  defaultValue  the Class instance to use as the default value
	 */
	public ClassConverter (Class<?> defaultValue) {

		this.defaultValue = defaultValue;
		this.returnDefault = true;
	}


	/**
	 * Convert the provided object into an object of the specified type.
	 *
	 * @param value the value to be converted
	 * @exception ConversionException if an error occurs during conversion
	 */
	public Class<?> convert (Object value) throws ConversionException {

		if (value == null) {

			if (returnDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(new NullPointerException());
			}
		}

		if (value instanceof Class) {
			return ((Class<?>)value);
		}

		/*
		 * First see if this class name is one of the primitive wrappers
		 */
		String className = value.toString();
		Class<?> c = ClassUtils.getWrapperClass(className);
		if (c != null) {
			return (c);
		}

		try {

			ClassLoader classLoader =
				Thread.currentThread().getContextClassLoader();
			if (classLoader == null) {
				classLoader = ClassConverter.class.getClassLoader();
			}

			return (classLoader.loadClass(className));

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

			if (value instanceof Class) {
				return (value.toString());
			} else {
				return (value.getClass().getName());
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}
}
