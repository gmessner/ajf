package com.messners.ajf.reflect;

import java.util.HashMap;


/**
 * This class provides static methods for working with and creating
 * <code>java.lang.Class</code> instances.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ClassUtils {

	/**
	 * Hide the constructor.
	 */
	private ClassUtils () {
	}


	/**
	 * Loads the class for the specified class name.
	 *
	 * @param  className  the namer of the class to load.
	 * @return the loaded Class instance, or one of the predefined
	 * java.lang.xxx.TYPE classes
	 * @throws ClassNotFoundException on any error
	 */
	public static Class<? extends Object> loadClass (String className) throws
			ClassNotFoundException {

		/*
		 * First see if this class name is one of the primitive wrappers
		 */
		Class<? extends Object>  c = getWrapperClass(className);
		if (c != null) {
			return (c);
		}

		ClassLoader classLoader;
		classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = ClassUtils.class.getClassLoader();
		}

		return (classLoader.loadClass(className));
	}


	/**
	 * Holds the primitive wrapper name to class map.
	 */
	private static HashMap<String, Class<? extends Object>> wrapperMap;
	static {

		wrapperMap = new HashMap<String, Class<?>>();
		wrapperMap.put("java.lang.Boolean.TYPE", Boolean.TYPE);
		wrapperMap.put("java.lang.Byte.TYPE", Byte.TYPE);
		wrapperMap.put("java.lang.Character.TYPE", Character.TYPE);
		wrapperMap.put("java.lang.Double.TYPE", Double.TYPE);
		wrapperMap.put("java.lang.Float.TYPE", Float.TYPE);
		wrapperMap.put("java.lang.Integer.TYPE", Integer.TYPE);
		wrapperMap.put("java.lang.Long.TYPE", Long.TYPE);
		wrapperMap.put("java.lang.Short.TYPE", Short.TYPE);
		wrapperMap.put("Boolean", Boolean.TYPE);
		wrapperMap.put("byte", Byte.TYPE);
		wrapperMap.put("char", Character.TYPE);
		wrapperMap.put("double", Double.TYPE);
		wrapperMap.put("float", Float.TYPE);
		wrapperMap.put("int", Integer.TYPE);
		wrapperMap.put("long", Long.TYPE);
		wrapperMap.put("short", Short.TYPE);
		wrapperMap.put("java.lang.String", String.class);
		wrapperMap.put("String", String.class);
		wrapperMap.put("Boolean", Boolean.class);
		wrapperMap.put("Byte", Byte.class);
		wrapperMap.put("Character", Character.class);
		wrapperMap.put("Double", Double.class);
		wrapperMap.put("Float", Float.class);
		wrapperMap.put("Integer", Integer.class);
		wrapperMap.put("Long", Long.class);
		wrapperMap.put("Short", Short.class);
	}


	/**
	 * Gets the pre-defined TYPE class for the java.lang types.
	 *
	 * @param  className  the name of the class to get the java.lang.xxxx.TYPE
	 *                    class for
	 * @return  the java.lang.xxxx.TYPE class for the specified class name
	 *          or null if the class name is not a java.lang.xxxx.TYPE class 
	 */
	public static Class<? extends Object> getWrapperClass (String className) {
		return (wrapperMap.get(className));
	}
}
