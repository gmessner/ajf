package com.messners.ajf.reflect;


/**
 * This class defines a parameter that is used for defining method invokations.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class Parameter {


	private Class<?> type;
	private Object value;


	/*
	 * Create a new <code>Paramter</code> with the type specified and value.
	 *
	 * @param  type   the <code>Class</code> representing the type
	 * for this parameter
	 * @param   value the <code>Object</code> representing the value
	 * for this parameter
	 *
	 */
	public Parameter (Class<?> type, Object value) {
		this.type  = type;
		this.value = value;
	}
	

	/**
	 * Create a new <code>Paramter</code> with the type set to the class name
	 * specified.
	 *
	 * @param  type  the name of the <code>Class</code> representing the type
	 * @param   value the <code>Object</code> representing the value
	 * for this parameter
	 * throws Exception  if an error occurs while converting the name to a Class
	 */
	public Parameter (String type, Object value) throws Exception {
		this.type  = Class.forName(type);
		this.value = value;
	}


	/**
	 * Create a new <code>Paramter</code> with the type specified.
	 *
	 * @param  type  the <code>Class</code> representing the type
	 * for this parameter
	 */
	public Parameter (Class<?> type) {
		this.type  = type;
	}


	/**
	 * Create a new <code>Paramter</code> with the type set to the class name
	 * specified.
	 *
	 * @param  type  the name of the <code>Class</code> representing the type
	 * for this parameter
	 * throws Exception  if an error occurs while converting the name to a Class
	 */
	public Parameter (String type) throws Exception {

		this.type  = Class.forName(type);
	}


	/**
	 * Gets the <code>Class</code> representing the type for this parameter.
	 *
	 * @return the <code>Class</code> representing the type for this parameter
	 */
	public Class<?> getType () {
		return (type);
	}
	

	/**
	 * Gets the <code>Object</code> representing the value for this parameter.
	 *
	 * @return the <code>Object</code> representing the value for this parameter
	 */
	public Object getValue () {
		return (value);
	}
}
