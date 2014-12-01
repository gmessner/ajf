package com.messners.ajf.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * <p>This interface simply defines method signatures for marshalling
 * Java class instances to XML.</p>
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface XmlMarshaller {


	/**
	 * Marshals the specified object using the provided XmlWriter.
	 *
	 * @param obj  the object to marshal
	 * @param out  the XmlWriter to use to do the marshalling
	 * @throws IOException if an I/O error occurs or if this
	 * XmlMarshaller instance does not handle the type for <code>obj</code>
	 */
	public void marshal (Object obj, XmlWriter out) throws IOException;


	/**
	 * Marshals the specified object using the provided Writer.
	 *
	 * @param obj  the object to marshal
	 * @param out  the Writer to do the output
	 * @throws IOException if an I/O error occurs or if this
	 * XmlMarshaller instance does not handle the type for <code>obj</code>
	 */
	public void marshal (Object obj, Writer out) throws IOException;


	/**
	 * Marshals the specified object using the provided OutputStream.
	 *
	 * @param obj  the object to marshal
	 * @param out  the OutputStream to do the output
	 * @throws IOException if an I/O error occurs or if this
	 * XmlMarshaller instance does not handle the type for <code>obj</code>
	 */
	public void marshal (Object obj, OutputStream out) throws IOException;
}
