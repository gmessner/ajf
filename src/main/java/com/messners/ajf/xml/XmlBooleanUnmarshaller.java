package com.messners.ajf.xml;

import java.util.HashMap;

/**
 * This class is intended to be used as the base class for all unmarshallers
 * that have Boolean PCDATA content.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public class XmlBooleanUnmarshaller extends XmlTextUnmarshaller {


	public static final HashMap<String, Boolean> booleanValues;
	static {

		booleanValues = new HashMap<String, Boolean>();
		booleanValues.put("true", Boolean.TRUE);
		booleanValues.put("1", Boolean.TRUE);
		booleanValues.put("false", Boolean.FALSE);
		booleanValues.put("0", Boolean.FALSE);
	}


	/**
	 * Gets the resultant object of this <code>Unmarshaller</code>.  This
	 * method is called by containg <code>Unmarshaller</code> instances
	 * and by the <code>Parser</code> to get the final results of a parse.
	 * In this case a <code>Boolean</code> is returned containing the text
	 * content of the XML element.
	 *
	 * @return resultant object for this instance
	 * @exception UnmarshalException if the content of the element is
	 * not a proper boolean 
	 */
	public Object getResults () throws UnmarshalException {
		return (getBoolean());
	}

	
	/**
	 * Gets the resultant object of this <code>Unmarshaller</code>.
	 *
	 * @return resultant Boolean instance
	 * @exception UnmarshalException if the content of the element is
	 * not a proper boolean 
	 */
	public Boolean getBoolean () throws UnmarshalException {
		return (Boolean.valueOf(parseBoolean(getText())));
	}

	
	/**
	 * Gets the results of this unmarshal as a primitive boolean.
	 *
	 * @return resultant long for this instance
	 * @exception UnmarshalException if the content of the element is
	 * not a proper boolean 
	 */
	public boolean booleanValue () throws UnmarshalException {
		return (parseBoolean(getText()));
	}


	/**
	 * Parse the provided string into a boolean value.</p>
	 *
	 * NOTE: 0 and 1 will be accepted as false and true
	 *
	 * @param s the String holding the boolean value to parse
	 * @return true if the String is a value representing true, false
	 * if the String is a value representing false
	 * @exception UnmarshalException if the content of the element is
	 * not a proper boolean 
	 */
	public static boolean parseBoolean (String s) throws UnmarshalException {
			
		Boolean b = (Boolean)booleanValues.get(s);
		if (b != null) {
			return (b.booleanValue());
		}

		String s1 = s.toLowerCase();
		b = (Boolean)booleanValues.get(s1);
		if (b != null) {
			return (b.booleanValue());
		}

		throw new UnmarshalException("\"" + s +
			"\" is not a valid boolean string");
	}
}
