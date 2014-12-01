package com.messners.ajf.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * This class defines a default implementation of the XmlWriter class.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class DefaultWriter extends XmlWriter {


	/**
	 * Pretty print OFF mode flag.
	 */
	public static final int PRETTY_PRINT_OFF = 0x01;

	/**
	 * Pretty print ON mode flag.
	 */
	public static final int PRETTY_PRINT_ON = 0x02;

	/**
	 * Pretty print ON with spaces indent mode flag.
	 */
	public static final int USE_SPACES_INDENT = PRETTY_PRINT_ON | 0x04;

	/**
	 * Pretty print ON with tab indent mode flag.
	 */
	public static final int USE_TAB_INDENT = PRETTY_PRINT_ON | 0x08;


	private Writer writer;
	private boolean inStartElement;
	private String currentNodeName;
	private Stack<String> nodeStack = new Stack<String>();

	private boolean bypassEscape = false;


	/*
	 * Pretty print configuration and state variables.
	 */
	private int prettyPrintMode = PRETTY_PRINT_ON | USE_SPACES_INDENT;
	private int indentWidth     = 2;
	private int depth           = 0;


	/**
	 * Used for pretty print indenting when USE_TAB_INDENT is set.
	 */
	private static final char tabs[];
	static {

		tabs = new char[80];
		for (int i = 0; i < 80; i++) {
			tabs[i] = '\t';
		}
	}


	/**
	 * Used for pretty print indenting when USE_SPACES_INDENT is set.
	 */
	private static final char spaces[];
	static {

		spaces = new char[512];
		for (int i = 0; i < 512; i++) {
			spaces[i] = ' ';
		}
	}



	/**
	 * Construct an instance that will output to the specified
	 * <code>Writer</code>. The prettyPrintMode is set to 
	 * <code>USE_SPACES_INDENT</code>.
	 *
	 * @param  writer  the Writer to output to
	 */
	public DefaultWriter (Writer writer) {

		super();

		if (writer == null) {
			throw new NullPointerException("writer cannot be null");
		}

		this.writer = writer;
	}


	/**
	 * Construct an instance that will output to the specified
	 * <code>OutputStream</code>. The prettyPrintMode is set to 
	 * <code>USE_SPACES_INDENT</code>.
	 *
	 * @param  out the OutputStream to output to
	 */
	public DefaultWriter (OutputStream out) {

		super();
		writer = new OutputStreamWriter(out);
	}


	/**
	 * Construct an instance that will output to the specified filename.
	 * The prettyPrintMode is set to <code>USE_SPACES_INDENT</code>.
	 *
	 * @param  filename  the name of the file to write to
	 * @throws IOException if any errors occur
	 */
	public DefaultWriter (String filename) throws IOException {

		super();
		writer = new FileWriter(filename);
	}


	/**
	 * Construct an instance that will output to the specified File instance.
	 * The prettyPrintMode is set to <code>USE_SPACES_INDENT</code>.
	 *
	 * @param  file  the File to write to
	 * @throws IOException if any errors occur
	 */
	public DefaultWriter (File file) throws IOException {

		super();
		writer = new FileWriter(file);
	}


	/**
	 * Gets the contained <code>Writer</code> instance. Should be used
	 * with great caution.
	 *
	 * @return the contained Writer instance
	 */
	public Writer getContainedWriter () {
		return (writer);
	}


	/**
	 * <p>Sets the pretty print mode.  When pretty print is on newlines
	 * will be added between elements and each level of XML elements
	 * can be optionally indented from its containg element. Mode 
	 * is an ORed set of flags as follows:</p>
	 * <pre>
	 *		PRETTY_PRINT_OFF
	 *		PRETTY_PRINT_ON
	 *		USE_TAB_INDENT
	 *		USE_SPACES_INDENT
	 * </pre>
	 *
	 * <code>PRETTY_PRINT_ON</code> and <code>PRETTY_PRINT_OFF</code> are
	 * mutually exclusive as are <code>USE_TAB_INDENT</code> and
	 * <code>USE_SPACES_INDENT</code>.
	 *
	 * @param  mode  the new prettyPrint mode
	 * @see #setSpaceIndentWidth(int indentWidth)
	 */
	public void setPrettyPrintMode (int mode) {
		this.prettyPrintMode = mode;
	}


	/**
	 * Sets the number of spaces to use for each indent level.  Only
	 * used when prettyPrintMode = USE_SPACES_INDENT.
	 *
	 * @param  indentWidth  the number of spaces to use for each indent level
	 * @see #setPrettyPrintMode(int mode)
	 */
	public void setSpaceIndentWidth (int indentWidth) {

		this.indentWidth = indentWidth;
	}


	/**
	 * Write the element with the specified String as the PCDATA. Element
	 * will not be written if <code>text</code> is <code>null</code>.
	 *
	 * @param  nodeName  the name for the element
	 * @param  text      the String for the PCDATA
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, String text) throws IOException {

		if (text == null) {
			return;
		}

		/*
		 * Complete the end of the start element if needed
		 */
		if (inStartElement) {
			writer.write('>');
			inStartElement = false;

			if ((prettyPrintMode & PRETTY_PRINT_ON) == PRETTY_PRINT_ON) {
				writer.write(newLine);
			}

		}

		indent();

		writer.write('<');
		writer.write(nodeName);
		writer.write('>');

		escapeAndWrite(text);

		writer.write("</");
		writer.write(nodeName);
		writer.write('>');

		if ((prettyPrintMode & PRETTY_PRINT_ON) == PRETTY_PRINT_ON) {
			writer.write(newLine);
		}
	}


	/**
	 * Indents the output according to the prettyPrintMode.
	 *
	 * @throws IOException if any errors occur
	 */
	protected void indent () throws IOException {

		switch (prettyPrintMode & (USE_SPACES_INDENT | USE_TAB_INDENT)) {
				
			case USE_SPACES_INDENT:
				writer.write(spaces, 0, depth * indentWidth);
				break;

			case USE_TAB_INDENT:
				writer.write(tabs, 0, depth);
				break;
		}
	}


	/*
	 * Write a start element tag with the specified element name.
	 *
	 * @param nodeName the name of the XML element to write, if null nothing
	 *                 will be written
	 * @throws IOException if any errors occur
	 */
	public void startElement (String nodeName) throws IOException {

		if (nodeName == null) {
			return;
		}

		/*
		 * Complete the end of the start element if needed
		 */
		if (inStartElement) {

			writer.write('>');
		
			if ((prettyPrintMode & PRETTY_PRINT_ON) == PRETTY_PRINT_ON) {
				writer.write(newLine);
			}

		} else {

			inStartElement = true;
		}


		/*
		 * Push the current node name on to the node name stack
		 */
		if (currentNodeName != null) {
			nodeStack.push(currentNodeName);
		}

		indent();

		currentNodeName = nodeName;
		writer.write('<');
		writer.write(nodeName);

		depth++;
	}


	/**
	 * Writes out the specified map using the specified mapName 
	 * for the XML node name. This implementation overrides the
	 * one in XmlWriter to provide indentation of the name/vaue pairs.
	 *
	 * @param  map  the Map to write
	 * @param  mapName
	 * @throws IOException if any errors occur
	 */
	protected void writeMap (Map<?, ?> map, String mapName) throws IOException {

		StringBuffer buf = new StringBuffer();

		boolean firstOne = true;
		Iterator<?> keys = map.keySet().iterator();
		while (keys.hasNext()) {

			Object key = keys.next();
			Object value = map.get(key);

			if (value instanceof Map) {

				if (firstOne) {
					firstOne = false;
				}

				startElement(mapName);

				if (key instanceof String) {
					writeAttr(MapConstants.NAME_ATTR, (String)key);
				} else {
					writeAttr(MapConstants.NAME_ATTR, key.toString());
				} 

				writeMap((Map<?, ?>)value, mapName);
				endElement();

			} else {

				if (firstOne) {
					write(newLine);
					firstOne = false;
				}

				buf.setLength(0);
				if (key instanceof String) {
					buf.append((String)key); 
				} else {
					buf.append(key.toString()); 
				}

				buf.append('=');

				if (value instanceof String) {
					buf.append((String)value); 
				} else {
					buf.append(value.toString()); 
				}

				indent();
				write(buf.toString());
				writeRaw(newLine);
			}
		}
	}


	/**
	 * Write the specified attribute name and value. Must be called immediately
	 * following a call to <code>startElement()</code> or an
	 * <code>IOException</code> will be thrown.
	 *
	 * @param  attrName  the name of the attribute
	 * @param  attrValue the value for the attribute
	 * @throws IOException if any errors occur
	 */
	public void writeAttr (String attrName, String attrValue)
			throws IOException {

		if (!inStartElement) {
			throw new IOException("not in start element");
		}

		writer.write(' ');
		writer.write(attrName);
		writer.write("=\"");
		escapeAndWriteAttribute(attrValue);
		writer.write('"');
	}


	/*
	 * Closes the current XML element. Will either out put a close tag or
	 * an empty tag dependent on the state of the output.
	 *
	 * @throws IOException if any errors occur
	 */
	public void endElement () throws IOException {

		if (currentNodeName == null) {
			throw new IOException("invalid state");
		}

		depth--;

		if (inStartElement) {

			writer.write(" />");
			inStartElement = false;

		} else {

			indent();
			writer.write("</");
			writer.write(currentNodeName);
			writer.write('>');
		}

		if ((prettyPrintMode & PRETTY_PRINT_ON) == PRETTY_PRINT_ON) {
			writer.write(newLine);
		}

		if (nodeStack.isEmpty()) {
			currentNodeName = null;
		} else {
			currentNodeName = nodeStack.pop();
		}
	}


	/*
	 * Writes the specified text escaping any characters to make the 
	 * text valid XML.
	 *
	 * @param text  the text to escape and write
	 * @throws IOException if any errors occur
	 */
	public void write (String text) throws IOException {

		if (inStartElement) {
			writer.write('>');
			inStartElement = false;
		}

		if (text != null) {
			escapeAndWrite(text);
		}
	}


	/*
	 * Writes the specified text as is without escaping any invalid
	 * XML characters.
	 *
	 * @param s the String write as is
	 * @throws IOException if any errors occur
	 */
	public void writeRaw (String s) throws IOException {
		writer.write(s);
	}


	/**
	 * Close this DefaultWriter.
	 *
	 * @throws IOException if any errors occur
	 */
	public void close () throws IOException {
		writer.close();
	}


	/**
	 * Flush this DefaultWriter.
	 *
	 * @throws IOException if any errors occur
	 */
	public void flush () throws IOException {
		writer.flush();
	}


	/**
	 * Escapes any characters that need to be escaped prior to writting them.
	 *
	 * @param  s  the String to escape and write
	 * @throws IOException if any errors occur
	 */
	protected void escapeAndWrite (String s) throws IOException {

		if (bypassEscape) {
			writer.write(s);
			return;
		}

		int len = (s != null ? s.length() : 0);
		for (int i = 0; i < len; i++) {

			char c = s.charAt(i);

			switch (c) {

				case '<':
            		writer.write("&lt;");
		            break;
       
				case '&':
					writer.write("&amp;");
		            break;

				default:
					writer.write(c);
			}
		}
	}


	/**
	 * Escapes any characters that need to be escaped prior to writting them
	 * out as an attribute value.
	 *
	 * @param  s  the String to escape and write
	 * @throws IOException if any errors occur
	 */
	protected void escapeAndWriteAttribute (String s) throws IOException {

		int len = (s != null ? s.length() : 0);
		for (int i = 0; i < len; i++) {

			char c = s.charAt(i);

			switch (c) {

				case '<':
            		writer.write("&lt;");
		            break;
       
      			case '>': 
            		writer.write("&gt;");
		            break;

				case '&':
					writer.write("&amp;");
		            break;

				case '"':
					writer.write("&quot;");
					break;
       
				default:
					writer.write(c);
			}
		}
	}
}
