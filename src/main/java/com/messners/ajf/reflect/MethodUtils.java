package com.messners.ajf.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This class provides static methods for working with and creating
 * <code>java.lang.reflect.Method</code> instances.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class MethodUtils {

	/**
	 * Hide the constructor.
	 */
	private MethodUtils () {
	}


	/**
	 * Gets the <code>Method</code> for the specified method signature.
	 *
	 * @param className  the name of the class that the method is
	 * associated with
	 * @param methodName the name of the method
	 * @param singleParamType  the type for the one and only parameter
	 * @return  a <code>Method</code> for the specified method signature
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the
	 *  method invoked
	 * @throws IllegalAccessException if the requested method is not accessible
	 *  via reflection
	 */
	public static Method getMethod (String className,
			String methodName, Class<?> singleParamType)
			throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {

		Class<?> c = ClassUtils.loadClass(className);
		Class<?> types[];
		if (singleParamType == null) {
			types = null;
		} else {
			types = new Class[]{ singleParamType };
		}

		return (getMethod(c, methodName, types));
	}


	/**
	 * Gets the <code>Method</code> for the specified method signature.
	 *
	 * @param c  the class that the method is associated with
	 * @param methodName the name of the method
	 * @param singleParamType  the type for the one and only parameter
	 * @return  a <code>Method</code> for the specified method signature
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the
	 *  method invoked
	 * @throws IllegalAccessException if the requested method is not accessible
	 *  via reflection
	 */
	public static Method getMethod (Class<?> c,
			String methodName, Class<?> singleParamType)
			throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {

		Class<?> types[];
		if (singleParamType == null) {
			types = null;
		} else {
			types = new Class[]{ singleParamType };
		}
		
		return (getMethod(c, methodName, types));
	}


	/**
	 * Gets the <code>Method</code> for the specified method signature.
	 *
	 * @param className  the name of the class that the method is
	 * associated with
	 * @param methodName the name of the method
	 * @param types	  an array of <code>Class</code> representing the
	 * types for the method parameters
	 * @return  a <code>Method</code> for the specified method signature
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the
	 *  method invoked
	 * @throws IllegalAccessException if the requested method is not accessible
	 *  via reflection
	 */
	public static Method getMethod (String className,
			String methodName, Class<?> types[])
			throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {

		Class<?> c = ClassUtils.loadClass(className);
		return (getMethod(c, methodName, types));
	}


	/**
	 * Gets the <code>Method</code> for the specified method signature.
	 *
	 * @param c  the class that the method is associated with
	 * @param methodName the name of the method
	 * @param types	  an array of <code>Class</code> representing the
	 * types for the method parameters
	 * @return  a <code>Method</code> for the specified method signature
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the
	 *  method invoked
	 * @throws IllegalAccessException if the requested method is not accessible
	 *  via reflection
	 */
	public static Method getMethod (Class<?> c, String methodName, Class<?> types[])
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {

		Method m = c.getMethod(methodName, types);	
		return (getAccessibleMethod(m));
	}



	/**
	 * <p>Return an accessible method (that is, one that can be invoked via
	 * reflection) with given name and parameters.  If no such method
	 * can be found, return <code>null</code>.
	 * This is just a convenient wrapper for
	 * {@link #getAccessibleMethod(Method method)}.</p>
	 *
	 * @param c get method from this class
	 * @param methodName get method with this name
	 * @param parameterTypes with these parameters types
	 */
	public static Method getAccessibleMethod (
			Class<?> c, String methodName, Class<?> parameterTypes[]) {

		try {
			return getAccessibleMethod(c.getMethod(methodName, parameterTypes));
		} catch (NoSuchMethodException e) {
			return (null);
		}
	}


	/**
	 * <p>Return an accessible method (that is, one that can be invoked via
	 * reflection) that implements the specified Method.  If no such method
	 * can be found, return <code>null</code>.</p>
	 *
	 * @param method The method that we wish to call
	 */
	public static Method getAccessibleMethod (Method method) {

		/*
		 * Make sure we have a method to check
		 */
		if (method == null) {
			return (null);
		}

		/*
		 * If the requested method is not public we cannot call it
		 */
		if (!Modifier.isPublic(method.getModifiers())) {
			return (null);
		}

		/*
		 * If the declaring class is public, we are done
		 */
		Class<?> c = method.getDeclaringClass();
		if (Modifier.isPublic(c.getModifiers())) {
			return (method);
		}

		/*
		 * Check the implemented interfaces and subinterfaces
		 */
		method = getAccessibleMethodFromInterfaceNest(
				c, method.getName(), method.getParameterTypes());
		return (method);
	}


	/**
	 * <p>Return an accessible method (that is, one that can be invoked via
	 * reflection) that implements the specified method, by scanning through
	 * all implemented interfaces and subinterfaces.  If no such method
	 * can be found, return <code>null</code>.</p>
	 *
	 * @param c Parent class for the interfaces to be checked
	 * @param methodName Method name of the method we wish to call
	 * @param parameterTypes The parameter type signatures
	 */
	private static Method getAccessibleMethodFromInterfaceNest
			(Class<?> c, String methodName, Class<?> parameterTypes[]) {

		Method method = null;

		/*
		 * Search up the superclass chain
		 */
		for (; c != null; c = c.getSuperclass()) {

			/*
			 * Check the implemented interfaces of the parent class
			 */
			Class<?> interfaces[] = c.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {

				/*
				 * Is this interface public?
				 */
				if (!Modifier.isPublic(interfaces[i].getModifiers())) {
					continue;
				}

				/*
				 * Does the method exist on this interface?
				 */
				try {

					method = interfaces[i].getDeclaredMethod(
									methodName, parameterTypes);

				} catch (NoSuchMethodException e) {
				}

				if (method != null) {
					break;
				}

				/*
				 * Recursively check our parent interfaces
				 */
				method = getAccessibleMethodFromInterfaceNest(
						interfaces[i], methodName, parameterTypes);
				if (method != null) {
					break;
				}
			}
		}

		return (method);
	}
}
