package com.messners.ajf.xml;

import org.xml.sax.SAXException;

/**
 * This class represents an exception that will contain basic error or
 * warning information about the unmarshal process. It is throw instead of
 * SAXException so consumers of this package do not need to be aware of
 * the org.xml.sax package.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class UnmarshalException extends SAXException {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>Create a new <code>UnmarshalException</code>.
	 *
	 * @param  message  the error or warning message
	 */
	public UnmarshalException (String message) {
		super(message);
	}


	/**
	 * <p>Create a new <code>UnmarshalException</code> wrapping an
	 * existing exception.</p>
	 *
	 * <p>The existing exception will be embedded in the new one, and its 
	 * message will become the default message for the new 
	 * <code>UnmarshalException</code>.
	 *
	 * @param  e  the existing exception to wrap
	 */
	public UnmarshalException (Exception e) {
		super(e);
	}


	/**
	 * <p>Create a new <code>UnmarshalException</code> wrapping an
	 * existing exception.</p>
	 *
	 * <p>The existing exception will be embedded in the new one, the new 
	 * exception will have its own message.
	 *
	 * @param  message  the error or warning message
	 * @param  e  the existing exception to wrap
	 */
	public UnmarshalException (String message, Exception e) {
		super(message, e);
	}
}
