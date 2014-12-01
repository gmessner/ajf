package com.messners.ajf.xml;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This abstract class is intended to be used as the base class for all
 * <code>XmlUnmarshaller</code> implementations.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public abstract class XmlBaseUnmarshaller
			extends DefaultHandler implements XmlUnmarshaller {


	/**
	 * This is the <code>XmlParser</code> owner.
	 */
	protected XmlParser xmlParser;


	/**
	 * This is the parent <code>XmlUnmarshaller</code> instance.
	 */
	protected XmlUnmarshaller parent;


	/**
	 * The XML namespace URI for the current element being unmarshalled.
	 */
	protected String namespaceUri;


	/**
	 * The XML local name (un-prefixed node name) for the
	 * current element being unmarshalled.
	 */
	protected String localName;


	/**
	 * The XML qualified name (prefixed node name) for the
	 * current element being unmarshalled.
	 */
	protected String qName;


	/**
	 * Sets up this instance's state with the provided <code>XmlParser</code>
	 * owner. Implementations of this interface will need this to maintain
	 * unmarshaller state using <code>XmlParser.pushUnmarshaller()</code>
	 * and <code>XmlParser.popUnmarshaller()</code>.
	 *
	 * @param  xmlParser    the owning <code>XmlParser</code> instance
	 */
	public void setXmlParser (XmlParser xmlParser) {

		this.xmlParser = xmlParser;
	}


	/**
	 * Gets the <code>XmlParser</code> owner of this instance. 
	 * Implementations of this interface will need this to maintain
	 * unmarshaller state using <code>XmlParser.pushUnmarshaller()</code>
	 * and <code>XmlParser.popUnmarshaller()</code>.
	 *
	 * @return the owning <code>XmlParser</code> instance
	 */
	public XmlParser getXmlParser () {
		return (xmlParser);
	}


	/**
	 * <p>Sets up this instance's state with the provided parent
	 * <code>XmlUnmarshaller</code> instance. This is can and is
	 * used be used in the unmarshalling process to propogate
	 * information upwards while building up an object.
	 *
	 * @param  parent  the parent <code>XmlUnmarshaller</code> instance,
	 * this should be null if this instance is at the root of the unmarshal
	 * process
	 */
	public void setParent (XmlUnmarshaller parent) {

		this.parent = parent;
	}


	/**
	 * Gets the parent <code>XmlUnmarshaller</code> for this instance.
	 * This is can be used in the unmarshalling process to propogate
	 * information upwards while building up an object.
	 *
	 * @return the parent <code>XmlUnmarshaller</code> instance
	 */
	public XmlUnmarshaller getParent () {
		return (parent);
	}


	/**
	 * Process the specified child <code>XmlUnmarshaller</code>instance.
	 * This is* can be used in the unmarshalling process to propogate
	 * information upwards while building up an object.
	 *
	 * @param  childUnmarshaller  the child <code>XmlUnmarshaller</code>
	 * @exception UnmarshalException if there is any exception generated 
	 * while processing the child
	 */
	public void processChild (XmlUnmarshaller childUnmarshaller) 
			throws UnmarshalException {

		// GMM: Should this base implementation throw an exception by default?
	}


	/**
	 * Resets the state of this <code>XmlBaseUnmarshaller</code> instance. This
	 * is usually called by the <code>XmlUnmarshallerFactory</code> instances
	 * when it re-uses (caches) <code>XmlUnmarshaller</code> instances.
	 */
	public void reset () {

		namespaceUri = null;
		localName = null;
		qName = null;
	}	


	/**
	 * Gets the XML namespace URI for the current element being unmarshalled.
	 */
	public String getNamespaceUri () {
		return (namespaceUri);
	}


	/**
	 * Gets the XML local name (un-prefixed node name) for the
	 * current element being unmarshalled.
	 */
	public String getLocalName () {
		return (localName);
	}


	/**
	 * Gets the XML qualified name (prefixed node name) for the
	 * current element being unmarshalled.
	 */
	public String getQName () {
		return (qName);
	}


	/**
	 * If sub-classes of this class override <code>startElement()</code>
	 * they <strong>must</strong> call this method to ensure that the state
	 * is setup correctly.
	 */
	public void startElement (String namespaceUri, String localName,
					String qName, Attributes attrs) throws SAXException {

		this.namespaceUri = namespaceUri;
		this.localName = localName;
		this.qName = qName;
	}


	/**
	 * Set the origin of the last SAX event. The locator field can be
	 * used when reporting errors or for debugging.
	 *
	 * @param  locator  the location of the SAX event within the document
	 */
	public void setDocumentLocator (Locator locator) {

		if (xmlParser != null) {
			setDocumentLocator(locator);
		}
	}


	/**
	 * Create an UnmarshalException with the specified message.  Will
	 * append location information if available.
	 *
	 * @param  msg  the message for the exception
	 * @return  a new UnmarshalException instance
	 */
	protected UnmarshalException createUnmarshalException (String msg) {

		Locator locator = (xmlParser == null ? null :
				xmlParser.getDocumentLocator());
		if (locator == null) {

			return (new UnmarshalException(msg));

		} else {

			return (new UnmarshalException(msg + 
				" (line: " +  locator.getLineNumber() +
				", column: " + locator.getColumnNumber() + ")"));
		}
	}
}
