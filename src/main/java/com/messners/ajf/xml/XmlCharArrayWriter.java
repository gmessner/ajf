package com.messners.ajf.xml;

import java.io.CharArrayWriter;

/**
 * This class creates a DefaultWriter instance that writes the
 * a specified CharArrayWriter.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class XmlCharArrayWriter extends DefaultWriter {


	private CharArrayWriter caw;

	public XmlCharArrayWriter (CharArrayWriter caw) {
		super(caw);
		this.caw = caw;
	}


	/**
	 * Get the char[] of written XML.
	 */
	public char [] toCharArray () {
		return (caw.toCharArray());
	}


	/**
	 * Resets the contained CharArrayWriter.
	 */
	public void reset () {
		caw.reset();
	}
}

