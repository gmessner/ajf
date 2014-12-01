package com.messners.ajf.xml;

/**
 * This class implements an <code>XmlUnmarshaller</code> that unmarshal
 * elements that contain a single double as content.  This single
 * double value will be returned as a <code>java.lang.Double</code>
 * instance from getResults().  Use <code>getDoubleValue()</code> to have
 * the value returned as an <code>double</code>.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class XmlDoubleUnmarshaller extends XmlTextUnmarshaller {


	/**
	 * Gets the resultant object of this <code>XmlUnmarshaller</code>.  This
	 * method is called by containg <code>XmlUnmarshaller</code> instances
	 * and by the <code>XmlParser</code> to get the final results of a parse.
	 * In this case an Double is returned containing the text content of the 
	 * XML element represented as an Double.
	 *
	 * @return resultant object for this instance
	 * @exception UnmarshalException if the content of the element is
	 * not a proper Double
	 */
	public Object getResults () throws UnmarshalException {

		return (getDouble());
	}


	/**
	 * Gets the resultant Double of this <code>XmlUnmarshaller</code>.
	 *
	 * @return resultant Double instance
	 * @exception UnmarshalException if the content of the element is
	 * not a proper Double
	 */
	public Double getDouble () throws UnmarshalException {
		
		try {

			String text = getText();
			text = (text != null ? text.trim() : null);
			return (new Double(text));

		} catch (NumberFormatException nfe) {

			UnmarshalException ue = new UnmarshalException(nfe);
			ue.fillInStackTrace();
			throw (ue);
		}
	}


	/**
	 * Gets the results of this unmarshal as a primitive double.
	 *
	 * @return resultant double for this instance
	 * @exception UnmarshalException if the content of the element is
	 * not a proper double
	 */
	public double getDoubleValue () throws UnmarshalException {

		Double d = getDouble();
		return (d.doubleValue());
	}


	/**
	 * Gets the results of this unmarshal as a primitive float.
	 *
	 * @return resultant float for this instance
	 * @exception UnmarshalException if the content of the element is
	 * not a proper float
	 */
	public float getFloatValue () throws UnmarshalException {

		Double d = getDouble();
		return (d.floatValue());
	}
}
