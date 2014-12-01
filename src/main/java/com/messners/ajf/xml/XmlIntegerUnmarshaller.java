package com.messners.ajf.xml;

/**
 * This class implements an <code>XmlUnmarshaller</code> that unmarshal
 * elements that contain a single integer as content.  This single
 * interger value will be returned as a <code>java.lang.Integer</code>
 * instance from getResults().  Use <code>getIntValue()</code> to have
 * the value returned as an <code>int</code>.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class XmlIntegerUnmarshaller extends XmlTextUnmarshaller {


	/**
	 * The radix to use for String to int conversions. Defaults to 10.
	 */
	private int radix = 10;


	/**
	 * Default no args constructor setup to parse signed decimal strings.
	 */
	public XmlIntegerUnmarshaller () {

		super();
		radix = 10;
	}


	/**
	 * Creates an instance of <code>XmlIntegerUnmarshaller</code>
	 * that will parse a string with the specified radix.
	 *
	 * @param  radix  the radix to be used for interpreting the strings
	 */
	public XmlIntegerUnmarshaller (int radix) {

		super();
		this.radix = radix;
	}


	/**
	 * Gets the resultant object of this <code>XmlUnmarshaller</code>.  This
	 * method is called by containg <code>XmlUnmarshaller</code> instances
	 * and by the <code>XmlParser</code> to get the final results of a parse.
	 * In this case an Integer is returned containing the text content of the 
	 * XML element represented as an Integer.
	 *
	 * @return resultant object for this instance
	 * @exception UnmarshalException if the content of the element is
	 * not a proper Integer
	 */
	public Object getResults () throws UnmarshalException {

		return (getInteger());
	}


	/**
	 * Gets the resultant Integer of this <code>XmlUnmarshaller</code>.
	 *
	 * @return resultant Integer instance
	 * @exception UnmarshalException if the content of the element is
	 * not a proper Integer
	 */
	public Integer getInteger () throws UnmarshalException {
		
		try {

			String text = getText();
			text = (text != null ? text.trim() : null);
			return (Integer.valueOf(text, radix));

		} catch (NumberFormatException nfe) {

			UnmarshalException ue = new UnmarshalException(nfe);
			ue.fillInStackTrace();
			throw (ue);
		}
	}


	/**
	 * Gets the resultant Integer of this <code>XmlUnmarshaller</code>.
	 *
	 * @param  radix the radix to use for converting the content to Integer
	 * @return resultant Integer instance
	 * @exception UnmarshalException if the content of the element is
	 * not a proper Integer
	 */
	public Integer getInteger (int radix) throws UnmarshalException {
		
		try {

			String text = getText();
			text = (text != null ? text.trim() : null);
			return (Integer.valueOf(text, radix));

		} catch (NumberFormatException nfe) {

			UnmarshalException ue = new UnmarshalException(nfe);
			ue.fillInStackTrace();
			throw (ue);
		}
	}


	/**
	 * Gets the results of this unmarshal as a primitive int.
	 *
	 * @return resultant int for this instance
	 * @exception UnmarshalException if the content of the element is
	 * not a proper int
	 */
	public int getIntValue () throws UnmarshalException {

		Integer integer = getInteger();
		return (integer.intValue());
	}
}
