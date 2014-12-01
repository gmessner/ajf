package com.messners.ajf.xml;

import java.util.HashMap;

import org.xml.sax.Attributes;

/**
 * This <code>XmlUnmarshallerFactory</code> provides the methods for
 * mapping a namespace/localName pair to an <code>XmlUnmarshaller</code>
 * instance.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class DefaultUnmarshallerFactory implements XmlUnmarshallerFactory {


	/**
	 * Keeps the mappings of namespaceURI/localName to XmlUnmarshaller.
	 */
	protected HashMap<String, XmlUnmarshaller> unmarshallerMap;


	/**
	 * No args constructor. Initializes the namespaceURI/localName map.
	 */
	public DefaultUnmarshallerFactory () {

		unmarshallerMap = new HashMap<String, XmlUnmarshaller>();
	}
	

	/**
	 * Gets the handler assiciated with the namespace and local name.
	 * The attributes are provided for implementations that wish to
	 * base the handler on the xsi:type attribute.
	 *
	 * @param  namespaceUri  the namesapce of the element
	 * @param  localName     the local name for the element (no prefix)
	 * @param  qName         the fully qualified XML element name
	 * @param  attrs         the attributes associated with the element
	 * @return the handler assiciated with the namespace and local name
	 * @exception UnmarshalException if no unmarshaller is found
	 */
	public XmlUnmarshaller getUnmarshaller (
			String namespaceUri, String localName,
			String qName, Attributes attrs)
			throws UnmarshalException {

		
		return (getUnmarshaller(namespaceUri, localName, qName));
	}


	/**
	 * Gets the lookup key for the namespace URI/local name pair.
	 *
	 * @param  namespaceUri  the namespace URI
	 * @param  localName     the local un-prefixed XML node name
	 * @param  qName         the fully qualified XML element name
	 * @return  the key for the namespaceURI/localName pair.
	 */
	public String getKey (String namespaceUri, String localName, String qName) {

		if (namespaceUri != null && namespaceUri.length() > 0) {

			return ("[" + namespaceUri + "]" + localName);

		} else {

			return (qName);
		}
	}


	/**
	 * Gets the <code>XmlUnmarshaller</code> instance for the specified
	 * namespaceURI/localName pair.
	 *
	 * @param  namespaceUri  the namespace URI
	 * @param  localName     the local un-prefixed XML node name
	 * @param  qName         the fully qualified XML element name
	 * @return  the <code>XmlUnmarshaller</code> instance for the specified
	 * namespaceURI/localName pair
	 * @exception UnmarshalException if no unmarshaller is found
	 */
	public XmlUnmarshaller getUnmarshaller (
				String namespaceUri, String localName, String qName)
				throws UnmarshalException {

		String key = getKey(namespaceUri, localName, qName);
		XmlUnmarshaller unmarshaller =
				unmarshallerMap.get(key);
		if (unmarshaller != null) {
			return (unmarshaller);
		}
	
		throw new UnmarshalException("unmarshaller not found");
	}


	/**
	 * Adds the <code>XmlUnmarshaller</code> instance for the specified
	 * qualified Name. This assumes namespaces are not to be found in the
	 * XML.
	 *
	 * @param  qName         the fully qualified (prefixed) XML node name
	 * @param  unmarshaller  the <code>XmlUnmarshaller</code> instance to map
	 * to the namespaceURI/localName pair
	 */
	public void addUnmarshaller (
			String qName, XmlUnmarshaller unmarshaller) {

		unmarshallerMap.put(qName, unmarshaller);
	}


	/**
	 * Adds the <code>XmlUnmarshaller</code> instance for the specified
	 * namespaceURI/localName pair.
	 *
	 * @param  namespaceUri  the namespace URI
	 * @param  localName     the local un-prefixed XML node name
	 * @param  unmarshaller  the <code>XmlUnmarshaller</code> instance to map
	 * to the namespaceURI/localName pair
	 */
	public void addUnmarshaller (
		String namespaceUri, String localName, XmlUnmarshaller unmarshaller) {

		String key = getKey(namespaceUri, localName, null);
		unmarshallerMap.put(key, unmarshaller);
	}
}
