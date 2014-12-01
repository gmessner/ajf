package com.messners.ajf.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.messners.ajf.data.MetaConverter;

/**
 * This class provides a simple way of mapping named data to a set method
 * in a particular class.  Use this class insteadof doing string comparisons
 * to decide which set method to call.
 *
 * A mechanism is provided to do automatic type conversion of the argument
 * passed in with the set method.  If performance is paramount it is suggested
 * that the application code convert the argument prior to a set call.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class SetMethods {

	private Class<?> containerClass;
	private HashMap<String, MethodInfo> map;


	/**
	 * Creates an instance set up to call methods on the provided Class.
	 *
	 * @param  containerClass  the Class that the methods are called on
	 */
	public SetMethods (Class<?> containerClass) {

		this.containerClass = containerClass;
		map     = new HashMap<String, MethodInfo>();
	}


	/**
	 * Add a set method to the map. 
	 *
	 * @param  name  the name of the data
	 * @param  method  the set method name
	 * @param  argType  the type for the argument passed in the set call
	 * @see #set
	 * @see #convertAndSet
	 */
	public void add (String name, String method, Class<?> argType) 
			throws Exception {

		MethodInfo info = new MethodInfo(method, argType);
		map.put(name, info);
	}


	/**
	 * Call a set method converting the value to the correct type. 
	 * This allows set methods to be called by simply providing the name
	 * of the data an instance of the container class and the Object to 
	 * pass as the value to the set call.
	 *
	 * @param  name  the name of the data
	 * @param  value  the value for the data
	 * @param  container  the container for the data
	 * @return true if the set method was called successfully
	 * @see #set
	 * @see #add
	 */
	public boolean convertAndSet (String name, Object value, Object container) {

		MethodInfo info = map.get(name);
		if (info == null) {
			return (false);
		}

		try {
		
			value = info.convert(value);
			info.invoke(container, value);
			return (true);

		} catch (Exception e) {

			return (false);
		}
	}


	/**
	 * Call a set method assuming the value is already the correct type. 
	 * This allows set methods to be called by simply providing the name
	 * of the data an instance of the container class and the Object to 
	 * pass as the value to the set call.
	 *
	 * @param  name  the name of the data
	 * @param  value  the value for the data
	 * @param  container  the container for the data
	 * @return true if the set method was called successfully
	 * @see #convertAndSet
	 * @see #add
	 */
	public boolean set (String name, Object value, Object container) {

		MethodInfo info = map.get(name);
		if (info == null) {
			return (false);
		}

		try {
		
			info.invoke(container, value);
			return (true);

		} catch (Exception e) {

			return (false);
		}
	}


	/**
	 * This class is used to manage the information for a set call.
	 * It also provides the code that does the actual invocation of
	 * the set method and a method to convert an object to the proper
	 * type for the set method call.
	 */
	private class MethodInfo {

		Class<?> argType;
		Method method;
		Object args[];

		public MethodInfo (String methodName, Class<?> argType) 
				throws NoSuchMethodException, IllegalAccessException,
					   InvocationTargetException {

			args = new Object[1];
			this.argType = argType;
			method = MethodUtils.getMethod(containerClass, methodName, argType);
		}

		public void invoke (Object container, Object value) throws Exception {
			args[0] = value;
			method.invoke(container, args);
		}

		public Object convert (Object value) throws Exception {
			return (MetaConverter.convert(value, argType));
		}
	}
}
