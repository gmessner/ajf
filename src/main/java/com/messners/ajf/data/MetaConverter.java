package com.messners.ajf.data;

import com.messners.ajf.data.converters.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;


/**
 * This class provides a convert() method to convert an object
 * to any of the types supported by the converters in the
 * <code>com.messners.data.converters</code> package.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class MetaConverter {

	private static HashMap<Class<?>, Converter<?>> converters;
	static {

		converters = new HashMap<Class<?>, Converter<?>>();
        converters.put(BigDecimal.class, new BigDecimalConverter());
        converters.put(BigInteger.class, new BigIntegerConverter());
        converters.put(Boolean.TYPE, new BooleanConverter(false));
        converters.put(Boolean.class,  new BooleanConverter(false));
        converters.put(Byte.TYPE, new ByteConverter((byte)0));
        converters.put(Byte.class, new ByteConverter((byte)0));
        converters.put(Character.TYPE, new CharacterConverter((char)0));
        converters.put(Character.class, new CharacterConverter((char)0));
        converters.put(Class.class, new ClassConverter());
        converters.put(Double.TYPE, new DoubleConverter(0.0));
        converters.put(Double.class, new DoubleConverter(0.0));
        converters.put(Float.TYPE, new FloatConverter(0.0f));
        converters.put(Float.class, new FloatConverter(0.0f));
        converters.put(Integer.TYPE, new IntegerConverter(0));
        converters.put(Integer.class, new IntegerConverter(0));
        converters.put(Long.TYPE, new LongConverter(0L));
        converters.put(Long.class, new LongConverter(0L));
        converters.put(Octal.class, new OctalConverter(0));
        converters.put(Short.TYPE, new ShortConverter((short)0));
        converters.put(String.class, new StringConverter());
    }


	private static HashMap<Class<?>, Class<?>> wrapperTypeMap;
	static {

		wrapperTypeMap = new HashMap<Class<?>, Class<?>>();
		wrapperTypeMap.put(java.lang.Boolean.TYPE, java.lang.Boolean.class);
		wrapperTypeMap.put(java.lang.Byte.TYPE, java.lang.Byte.class);
		wrapperTypeMap.put(java.lang.Character.TYPE, java.lang.Character.class);
		wrapperTypeMap.put(java.lang.Double.TYPE, java.lang.Double.class);
		wrapperTypeMap.put(java.lang.Float.TYPE, java.lang.Float.class);
		wrapperTypeMap.put(java.lang.Integer.TYPE, java.lang.Integer.class);
		wrapperTypeMap.put(java.lang.Long.TYPE, java.lang.Long.class);
		wrapperTypeMap.put(java.lang.Short.TYPE, java.lang.Short.class);
	}

	private static final Converter<Integer> binaryConverter = new IntegerConverter(0, 2);
	private static final Converter<Integer> octalConverter = new IntegerConverter(0, 8);
	private static final Converter<Integer> hexConverter = new IntegerConverter(0, 16);


	/**
	 * Gets the {@link Converter} for the specified Java Class.
	 * @param <T>
	 *
	 * @param  javaClass the Class to get the Converter for
	 * @return  the Converter mapped to the specified Class,
	 *  or null if not mapped
	 */
	@SuppressWarnings("unchecked")
	public static <T> Converter<T> getConverter (Class<T> javaClass) {
		return (Converter<T>) converters.get(javaClass);
	}


	/**
	 * Gets the Integer {@link Converter} for the specified Java Class and
	 * radix.
	 *
	 * @param  javaClass the Class to get the Converter for, must be
	 * Integer.class or Integer.TYPE.
	 * @param  radix     the numeric radix used for conversions
	 * @return  the Converter mapped to the specified Class,
	 *  or null if not mapped
	 */
	public static Converter<Integer> getConverter (Class<?> javaClass, int radix) {

		if (javaClass != java.lang.Integer.class &&
				javaClass != java.lang.Integer.TYPE) {
			return (null);
		}

		if (radix == 2) {
			return (binaryConverter);
		} else if (radix == 8) {
			return (octalConverter);
		} else if (radix == 16) {
			return (hexConverter);
		} else if (radix == 10) {
			@SuppressWarnings("unchecked")
			Converter<Integer> converter = (Converter<Integer>) (getConverter(javaClass));
			return converter;
		}

		return (null);
	}


    /**
     * Converts the value object into a String.
     *
     * @param value  the object value to convert
	 * return the converted instance of String
     */
    public static String convert (Object value) {

        if (value == null) {
            return ((String)null);
		}

		return (value.toString());
    }


    /**
     * Converts the value object to an instance of the specified class.  If
	 * no converter is configured for the specified class will convert
	 * the value object to a String.
     * @param <T>
     *
	 * @return  an instance of the class specified by <code>convertTo</code>
	 * @exception ConversionException if an error ocurrs during conversion
     */
    @SuppressWarnings("unchecked")
	public static <T> T convert (Object value, Class<T> convertTo) 
			throws ConversionException {

        Converter<T> converter = (Converter<T>) converters.get(convertTo);
        if (converter == null) {
            converter = (Converter<T>) converters.get(String.class);
        }

        return ((T)converter.convert(value));
    }


	/**
	 * Checks to see if the value is an instance of the specified Class.
	 *
	 * @param  type  the Class to check if value is an instance of
	 * @param  value the instance to check
	 * @return  true if the value is an instance of the specified Class,
	 * otherwise returns false
	 */
	public static boolean isInstance (Class<?> type, Object value) {

		if (type.isInstance(value)) {
			return (true);
		}

		type = wrapperTypeMap.get(type);
		if (type != null) {
			return (type.isInstance(value));
		}

		return (false);
	}


	/**
	 * Hide the constructor, this class is not meant to be instanciated.
	 */
	private MetaConverter () {
	}
}
