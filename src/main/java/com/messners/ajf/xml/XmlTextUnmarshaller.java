package com.messners.ajf.xml;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class is intended to be used as the base class for all unmarshallers
 * that have PCDATA content of one form or another.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class XmlTextUnmarshaller extends XmlBaseUnmarshaller {


	/**
	 * Used to buffer read characters.
	 */
	protected StringBuffer buffer;


	/**
	 * Used to cache CharArrayWriters to try and reduce memory allocations.
	 */
	private static Stack<StringBuffer> bufferCache = new Stack<StringBuffer>();


	/**
	 * Resets the state of this <code>XmlBaseUnmarshaller</code> instance. This
	 * is usually called by the <code>XmlUnmarshallerFactory</code> instances
	 * when it re-uses (caches) <code>XmlUnmarshaller</code> instances.
	 */
	public void reset () {

		super.reset();

		if (buffer != null) {

			/*
			 * Push this instances buffer on to the buffer cache.
			 */
			synchronized (bufferCache) {

				buffer.setLength(0);
				bufferCache.push(buffer);
				buffer = null;
			}
		}
	}	


	/**
	 * Gets the resultant object of this <code>XmlUnmarshaller</code>.  This
	 * method is called by containg <code>XmlUnmarshaller</code> instances
	 * and by the <code>XmlParser</code> to get the final results of a parse.
	 * In this case a String is returned containing the text content of the 
	 * XML element.
	 *
	 * @return resultant object for this instance
	 */
	public Object getResults () throws UnmarshalException {
		return (getText());
	}


	/**
	 * Get the assembled parsed character data (the results)
	 * for this unmarshaller.
	 *
	 * @return  the assembled parsed character data for this unmarshaller which
	 *          was built up by calls to characters()
	 */
	public String getText () {

		if (buffer == null) {
			return (null);
		}

		return (buffer.toString());
	}


	/**
	 * If sub-classes of this class override <code>startElement()</code>
	 * they <strong>must</strong> call this method to ensure that the state
	 * is setup correctly.
	 */
	public void startElement (String namespaceUri, String localName,
			String qName, Attributes attrs) throws SAXException {

		super.startElement(namespaceUri, localName, qName, attrs);

		/*
		 * Do this here because reset() calls super.reset() which
		 * will clear the namespaceUri and localName just set.
		 */
		if (buffer != null) {

			/*
			 * Push this instances buffer on to the buffer cache.
			 */
			synchronized (bufferCache) {

				buffer.setLength(0);
				bufferCache.push(buffer);
				buffer = null;
			}
		}
	}	


	/**
	 * <p>This implementation overrides this method so that it can build up
	 * the content of an element. Sub-classes of this class can call 
	 * <code>getText()</code> to retieve an elements PCDATA content.</p>
	 *
	 * <p><strong>NOTE:</strong><br/>
	 * This functionality is provided becuases there is no guarantee that
	 * the character data for an element will be received in one contiguous
	 * block.</p>
	 *
	 * @param  data   the characters
	 * @param  start  the start position of the actual data in the char array
	 * @param  length the number of characters to use from the char array
	 * @exception  SAXException may be any exception, usually from
	 * String to primitive conversions
	 */
	public void characters (char data[], int start, int length)
			throws SAXException {

		/*
		 * We do this first so that we can use a non-null buffer to indicate
		 * that the this method was called at least once.
		 */
		if (buffer == null) {
			buffer = getBuffer();
		}


		/*
		 * If we have any characters append them to the buffer
		 */
		if (data != null && length > 0) {
			buffer.append(data, start, length);
		}
	}


	/**
	 * We override this method so that we can pop ourself off of the
	 * unmarshal stack in the XmlParser, this will cause the 
	 * processChild() method to be called on our parent
	 * <code>XmlUnmarshaller</code>.
	 */
	public void endElement (
			String namespaceUri, String localName, String qName) 
			throws SAXException {

		getXmlParser().popUnmarshaller(this);
	}


	/**
	 * To be as fast as possible we cache our CharArrayWriters on a stack.
	 * This method will pop a CharArrayWriter if available otherwise it will
	 * create a new one.
	 *
	 * @return  a new or properly reset <code>CharArrayWriter</code>
	 */
	protected static StringBuffer getBuffer () {

		synchronized (bufferCache) {

			if (bufferCache.empty()) {

				return (new StringBuffer(80));

			} else {

				return bufferCache.pop();
			}
		}
	}
}
