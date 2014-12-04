package com.messners.ajf.xml;

import com.messners.ajf.util.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class implements an <code>XmlUnmarshaller</code> that unmarshal
 * elements that contain a parent map element with contained name/value pairs.
 * The map will be returned as an Object of <code>Map<String, Serializable></code>
 * instance from getResults().  Use <code>getMap()</code> to have
 * the value returned as a <code>Map<String, Serializable></code>.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class XmlMapUnmarshaller extends XmlBaseUnmarshaller
		implements MapConstants {

	protected Map<String, Serializable> results;
	protected Map<String, Serializable> map;
	protected StringBuffer buffer;
	protected Stack<Map<String, Serializable>> mapStack;
	protected Stack<StringBuffer> bufferStack;
	protected String mapName;


	/**
	 * Used to cache StringBuffers to try and reduce memory allocations.
	 */
	private static Stack<StringBuffer> bufferCache = new Stack<StringBuffer>();


	/**
	 * Default no args constructor. Defaults to "Map" for inlined maps 
	 * and "Element" for map elements.
	 */
	public XmlMapUnmarshaller () {
		this(MAP_ELEMENT);
	}


	/**
	 * Constructor that uses the specified mapName for inlined maps.
	 * 
	 * @param  mapName  the XML element name for inlined maps
	 */
	public XmlMapUnmarshaller (String mapName) {

		super();
		this.mapName = mapName;
		mapStack = new Stack<Map<String, Serializable>>();
		bufferStack = new Stack<StringBuffer>();
	}


	/**
	 * Resets the state of this <code>XmlMapUnmarshaller</code> instance.
	 * This is usually called by the <code>XmlUnmarshallerFactory</code>
	 * instances when it re-uses (caches) <code>XmlUnmarshaller</code>
	 * instances.
	 */
	public void reset () {

		super.reset();
		results = null;
		map = null;
		mapStack.clear();

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
	 * If sub-classes of this class override <code>startElement()</code>
	 * they <strong>must</strong> call this method to ensure that the state
	 * is setup correctly.
	 */
	public void startElement (String namespaceUri, String localName,
					String qName, Attributes attrs) throws SAXException {

		if (results == null) {

			super.startElement(namespaceUri, localName, qName, attrs);
			results = new LinkedHashMap<String, Serializable>();
			map = results;

		} else if (mapName.equals(qName)) {
		
			String name = attrs.getValue(NAME_ATTR);
			if (name == null) {
				throw new UnmarshalException("map name cannot be null");
			}

			HashMap<String, Serializable> contained = new LinkedHashMap<String, Serializable>();
			map.put(name, contained);
			mapStack.push(map);
			map = contained;

			bufferStack.push(buffer);
			buffer = null;

		} else {

			mapStack.push(map);
			map = null;
			bufferStack.push(buffer);
			buffer = null;
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

		if (map == null || buffer == null) {

			if (!mapStack.empty()) {
				map = (HashMap<String, Serializable>)mapStack.pop();
			}

			if (!bufferStack.empty()) {
				buffer = bufferStack.pop();
			}

			return;
		}

		try {

			StringReader reader = new StringReader(buffer.toString());
			String[] lines = StringUtils.toStringArray(reader);
			int numLines = lines.length;
			for (int i = 0; i < numLines; i++) {

				String line = lines[i];
				int index = line.indexOf('=');
				if (index == -1) {
					continue;
				}

				String name  = line.substring(0, index).trim();
				String value = line.substring(index + 1);
				map.put(name, value);
			}

		} catch (IOException ignore) {
		}


		/*
		 * Push this instances buffer on to the buffer cache.
		 */
		synchronized (bufferCache) {

			buffer.setLength(0);
			bufferCache.push(buffer);
			buffer = null;
		}


		if (mapStack.empty()) {

			getXmlParser().popUnmarshaller(this);

		} else {

			map = mapStack.pop();
			buffer = bufferStack.pop();
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
		 * If we are not in a map element just return
		 */
		if (map == null) {
			return;
		}


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
	 * Gets the resultant object of this <code>XmlUnmarshaller</code>.  This
	 * method is called by containg <code>XmlUnmarshaller</code> instances
	 * and by the <code>XmlParser</code> to get the final results of a parse.
	 * In this case a List is returned containing the list of text content
	 * of the XML element.
	 *
	 * @return resultant object for this instance
	 */
	public Object getResults () throws UnmarshalException {
		return (results);
	}


	/**
	 * Gets the resultant List of this <code>XmlUnmarshaller</code>.
	 *
	 * @return the resultant List instance
	 */
	public Map<String, Serializable> getMap () throws UnmarshalException {
		return (results);	
	}



	/**
	 * To be as fast as possible we cache our StringBuffers on a stack.
	 * This method will pop a StringBuffers if available otherwise it will
	 * create a new one.
	 *
	 * @return  a new or properly reset <code>StringBuffer</code>
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
