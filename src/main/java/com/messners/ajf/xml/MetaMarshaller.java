package com.messners.ajf.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import java.util.HashMap;

/**
 * This class provides the methods for mapping a Class to a XmlMarshaller and
 * convienence methods for marshalling objects using the mapped XmlMarshallers.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class MetaMarshaller {


	/**
	 * Keeps the mappings of Class to XmlMarshaller.
	 */
	protected HashMap<Class<?>, XmlMarshaller> map;


	/**
	 * No args constructor. Initializes the Class/XmlMarshaller map.
	 */
	public MetaMarshaller () {
		map = new HashMap<Class<?>, XmlMarshaller>();
	}
	

	/**
	 * Gets the XmlMarshaller associated with the specified Class.
	 *
	 * @param c the Class to get the XmlMarshaller for
	 * @return  the XmlMarshaller mapped to the Class or null if Class is
	 * not mapped
	 */
	public XmlMarshaller getMarshaller (Class<?> c) {
		return map.get(c);
	}


	/**
	 * Adds a mapping for the specified <code>Class</code> / 
	 * <code>XmlMarshaller</code> pair.
	 *
	 * @param  c  the Class to map the XmlMarshaller to
	 * @param  marshaller  the <code>XmlMarshaller</code> instance to map
	 * class to
	 */
	public void addMarshaller (Class<?> c, XmlMarshaller marshaller) {
		map.put(c, marshaller);
	}


	/**
	 * Marshals the specified object using the provided XmlWriter.
	 *
	 * @param obj  the object to marshal
	 * @param out  the XmlWriter to use to do the marshalling
	 * @throws IOException if an I/O error occurs or if no XmlMarshaller
	 * is mapped to the <code>Class</code> for <code>obj</code>
	 */
	public void marshal (Object obj, XmlWriter out) throws IOException {

		Class<?> c = obj.getClass();
		XmlMarshaller marshaller = getMarshaller(c);
		if (marshaller == null) {

			throw new IOException(
				"no XmlMarshaller is mapped to " + c.getName());
		}

		marshaller.marshal(obj, out);
	}


	/**
	 * Marshals the specified object using the provided Writer.
	 *
	 * @param obj  the object to marshal
	 * @param out  the Writer to do the output
	 * @throws IOException if an I/O error occurs or if no XmlMarshaller
	 * is mapped to the <code>Class</code> for <code>obj</code>
	 */
	public void marshal (Object obj, Writer out) throws IOException {

		Class<?> c = obj.getClass();
		XmlMarshaller marshaller = getMarshaller(c);
		if (marshaller == null) {

			throw new IOException(
				"no XmlMarshaller is mapped to " + c.getName());
		}

		marshaller.marshal(obj, out);
	}


	/**
	 * Marshals the specified object using the provided OutputStream.
	 *
	 * @param obj  the object to marshal
	 * @param out  the OutputStream to do the output
	 * @throws IOException if an I/O error occurs or if no XmlMarshaller
	 * is mapped to the <code>Class</code> for <code>obj</code>
	 */
	public void marshal (Object obj, OutputStream out) throws IOException {

		Class<?> c = obj.getClass();
		XmlMarshaller marshaller = getMarshaller(c);
		if (marshaller == null) {

			throw new IOException(
				"no XmlMarshaller is mapped to " + c.getName());
		}

		marshaller.marshal(obj, out);
	}
}
