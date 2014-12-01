package com.messners.ajf.xml;

import org.xml.sax.Attributes;

/**
 * Implementations of this interface manage a set of XmlUnmarshaller
 * instances that are mapped to specific XML elements.  XmlParser
 * instances will call getUnmarshaller() to fetch the XmlUnmarshaller
 * mapped to a specific element, if no XmlUnmarshaller is mapped
 * for the element getUnmarshaller() must throw an UnmarshalException
 * indicating that the element does not have an XmlUnmarshaller
 * assigned to it.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface XmlUnmarshallerFactory {

	/**
	 * Gets the handler assiciated with the namespace and local name.
	 * The attributes are provided for implementations that wish to
	 * base the handler on the xsi:type attribute.
	 *
	 * @param  namespaceURI  the namesapce of the element
	 * @param  localName     the local name for the element (no prefix)
	 * @param  qName         the fully qualified XML element name
	 * @param  attrs         the attributes associated with the element
	 * @return the handler assiciated with the namespace and local name
	 * @exception UnmarshalException if no handler is found
	 */
	public XmlUnmarshaller getUnmarshaller (
			String namespaceURI, String localName,
			String qName, Attributes attrs)
			throws UnmarshalException;
}
