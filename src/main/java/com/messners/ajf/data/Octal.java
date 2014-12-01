package com.messners.ajf.data;

import java.io.Serializable;

/**
 * This class defines a Number for octal values.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class Octal extends Number implements Comparable<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	private Integer value;


	/**
	 * Constructs a newly allocated <code>Octal</code> object that represents
	 * the specified <code>int</code> value.
	 *
	 * @param  value  the value to be represented by this Octal object
	 */
	public Octal (int value) {
		this.value = new Integer(value);
	}


	/**
	 * Constructs a newly allocated <code>Octal</code> object that represents
	 * the specified <code>Number</code> value.
	 *
	 * @param  value  the value to be represented by this Octal object
	 */
	public Octal (Number value) {
		this.value = new Integer(value.intValue());
	}

	/**
	 * Constructs a newly allocated <code>Octal</code> object that represents
	 * the <code>int</code> value indicated by the String parameter. The string
	 * is converted to an int value in exactly the manner used by the parseInt
	 * method for radix 8.
	 *
	 * @param  s the String to be converted to an Octal
	 * @throws NumberFormatException if the String does not contain a 
	 * parsable octal value
	 */
	public Octal (String s) throws NumberFormatException {
		this.value = Integer.valueOf(s, 8);
	}


	/**
	 * Gets the octal String representation of this instance.
	 *
	 * @return an octal String representing this Octal instance
	 */
	public String toString () {
		return (Integer.toOctalString(value.intValue()));
	}


	/**
	 * Returns the value of this instance as a byte.
	 *
	 * @return  the value of this instance as a byte
	 */
	public byte byteValue () {
		return (value.byteValue());
	}


	/**
	 * Returns the value of this instance as a double.
	 *
	 * @return  the value of this instance as a double 
	 */
	public double doubleValue () {
		return (value.doubleValue());
	}


	/**
	 * Returns the value of this instance as a float.
	 *
	 * @return  the value of this instance as a float
	 */
	public float floatValue () {
		return (value.floatValue());
	}


	/**
	 * Returns the value of this instance as an int.
	 *
	 * @return  the value of this instance as an int 
	 */
	public int intValue () {
		return (value.intValue());
	}


	/**
	 * Returns the value of this instance as a long.
	 *
	 * @return  the value of this instance as a long
	 */
	public long longValue () {
		return (value.longValue());
	}


	/**
	 * Returns the value of this instance as a short.
	 *
	 * @return  the value of this instance as a short
	 */
	public short shortValue () {
		return (value.shortValue());
	}


	/**
	 * Compares this object with the specified object for order.
	 *
	 * @param octal  the object to compare to
	 * @return the value 0 if this instance is equal to the argument Integer;
	 * a value less than 0 if this Integer is numerically less than the
	 * argument Integer; and a value greater than 0 if this Integer is
	 * numerically greater than the argument Integer
	 */
	public int compareTo (Octal octal) {
		return (value.compareTo(octal.value));
	}


	/**
	 * Compares this object with the specified object for order.
	 *
	 * @param i the Integer  object to compare to
	 * @return the value 0 if this instance is equal to the argument Integer;
	 * a value less than 0 if this Integer is numerically less than the
	 * argument Integer; and a value greater than 0 if this Integer is
	 * numerically greater than the argument Integer
	 */
	public int compareTo (Integer i) {
		return (value.compareTo(i));
	}


	/**
	 * Compares this object with the specified object for order.
	 *
	 * @param obj  the object to compare to
	 * @return the value 0 if this instance is equal to the argument;
	 * a value less than 0 if this instance is numerically less than the
	 * argument; and a value greater than 0 if this instance is
	 * numerically greater than the argument 
	 *
	 * @throws ClassCastException if o is not an Octal or Integer instance
	 */
	public int compareTo (Object obj) {

		if (obj instanceof Integer) {
			return (compareTo((Integer)obj));
		} else {
			return (compareTo((Octal)obj));
		}
	}
}
