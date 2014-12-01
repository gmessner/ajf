package com.messners.ajf.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * This abstract class provides a base for XmlMarshaller implementations.
 * An implementation simply needs to implement the
 * <code>marshall(Object obj, XmlWriter out)</code> method from XmlMarshaller.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public abstract class XmlBaseMarshaller implements XmlMarshaller {


	/**
	 * Marshals the specified object using the provided Writer.
	 *
	 * @param obj  the object to marshal
	 * @param out  the Writer to do the output
	 * @throws IOException if an I/O error occurs or if this
	 * XmlMarshaller instance does not handle the type for <code>obj</code>
	 */
	public void marshal (Object obj, Writer out) throws IOException {

		XmlWriter writer = new DefaultWriter(out);
		marshal(obj, writer);
		writer.flush();
	}


	/**
	 * Marshals the specified object using the provided OutputStream.
	 *
	 * @param obj  the object to marshal
	 * @param out  the OutputStream to do the output
	 * @throws IOException if an I/O error occurs or if this
	 * XmlMarshaller instance does not handle the type for <code>obj</code>
	 */
	public void marshal (Object obj, OutputStream out) throws IOException {

		XmlWriter writer = new DefaultWriter(out);
		marshal(obj, writer);
		writer.flush();
	}
}
