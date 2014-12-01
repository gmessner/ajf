package com.messners.ajf.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * This class simplifies invoking methods through reflection.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class InvokeMethod {


	private String className;
	private String methodName;
	private ArrayList<Parameter> params;
	private int valueIndex = -1;
	
	private Method method;
	private Object values[];


	/**
	 * No args constructor.
	 */
	public InvokeMethod () {
	}
	

	/**
	 * Sets the name of the class for the method to invoke.
	 *
	 * @param  className  the name of the class for the method to invoke
	 */
	public void setClassName (String className) {

		if (className == null) {

			this.className = null;
			method = null;
		}

		if (!className.equals(this.className)) {
			this.className = className;
			method = null;
		}
	}


	/**
	 * Sets the name of the method to invoke.
	 *
	 * @param  methodName  the name of the method to invoke
	 */
	public void setMethodName (String methodName) {

		if (methodName == null) {

			this.methodName = null;
			method = null;
		}

		if (!methodName.equals(this.methodName)) {
			this.methodName = methodName;
			method = null;
		}
	}


	/**
	 * <p>Adds a parameter for the method invokation. A parameter is specified
	 * by a <code>Class<code> representing its type.
	 *
	 * @param  type  the class representing the type of the parameter
	 */
	public void addParameter (Class<?> type) {

		if (params == null) {
			params = new ArrayList<Parameter>();
		}

		Parameter param = new Parameter(type);
		params.add(param);
	}


	/**
	 * <p>Adds a parameter for the method invokation. A parameter is specified
	 * by a <code>Class<code> representing its type and an <code>Object</code>
	 * representing the fixed value for the parameter,  if you wish the
	 * parameter to utilize the value object to be converted, pass 
	 * <code>null</code> as the fixed value.</p>
	 *
	 * <p><strong>Note: </strong>  Only 1 parameter can be the parameter
	 * representing the value object to be converted.
	 *
	 * @param  type  the class representing the type of the parameter
	 * @param  fixedValue  an object representing the fixed value of this
	 *         parameter,  can be null if the parameter value is not fixed
	 * @exception Exception if more than a single parameter has been added 
	 * as the value paramter
	 */
	public void addParameter (Class<?> type, Object fixedValue) throws Exception {

		if (params == null) {
			params = new ArrayList<Parameter>();
		}


		/*
		 * Make sure that we haven't already added the value object parameter,
		 * if so throw an exception
		 */
		if (fixedValue == null) {

			if (valueIndex != -1) {
				throw new Exception("rule can only have a single value object");
			}

			valueIndex = params.size();
		}

		Parameter param = new Parameter(type, fixedValue);
		params.add(param);
	}


	/**
	 * <p>Validates that the <code>java.lang.reflect.Method</code> instance as
	 * currently configured. You can do this to verify that the current state
	 * of the instance is valid prior to calling invoke().</p>
	 *
	 * @throws  Exception  if the method is not configured correctly
	 */
	public void validate () throws Exception {
	
		/*
		 * Have we created the Method to invoke, no then create it
		 */
		if (method == null) {

			int numParams = params.size();
			Class<?> types[] = new Class[numParams];
			values = new Object[numParams];
			for (int i = 0; i < numParams; i++) {

				Parameter param = params.get(i);
				types[i]  = param.getType();
				values[i] = param.getValue();
			}

			method = MethodUtils.getMethod(className, methodName, types);

			className  = null;
			methodName = null;
			params     = null;
		}
	}


	/**
	 * Invoke the method and return the object as the results of the conversion.
	 * This is a special case invoke() that will set <code>value</code> into
	 * the values array for the method invokation.
	 *
	 * @param  obj    the object the method is invoked from, can be null
	 *                for a static method
	 * @param  value  the value object to be converted
	 * @exception  Exception if an error occurs
	 */
	public Object invoke (Object obj, Object value) throws Exception {

		if (value == null) {
			throw new Exception("value object cannot be null");
		}

		/*
		 * Have we created the Method to invoke, no then validate it, 
		 * this will create the actual Method instance to invoke
		 */
		if (method == null) {
			validate();
		}

		values[valueIndex] = value;
		return (method.invoke(obj, values));
	}
}

