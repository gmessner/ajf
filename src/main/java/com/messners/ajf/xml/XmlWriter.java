package com.messners.ajf.xml;

import com.messners.ajf.util.ISO8601DateFormat;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * This class defines an abstract class for writing XML.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public abstract class XmlWriter {


	/**
	 * No args constructor.  Sets the format for dates to EXTENDED_DATETIME.
	 */
	public XmlWriter () {
	}


	/**
	 * The String to use as a newline.  Defaults to the system specific
	 * newline retrieced with a call to
	 * <code>System.getproperty("line.separator")</code>.
	 */
	public static String newLine = System.getProperty("line.separator");


	/*
	 * Write a start element tag with the specified element name.
	 *
	 * @param nodeName the name of the XML element to write, if null nothing
	 *                 will be written
	 * @throws IOException if any errors occur
	 */
	public abstract void startElement (String nodeName) throws IOException;


	/**
	 * Write the specified attribute name and value. Must be called immediately
	 * following a call to <code>startElement()</code> or an
	 * <code>IOException</code> will be thrown.
	 *
	 * @param  attrName  the name of the attribute
	 * @param  attrValue the value for the attribute
	 * @throws IOException if any errors occur
	 */
	public abstract void writeAttr (String attrName, String attrValue)
			throws IOException;


	/*
	 * Closes the current XML element. Will either out put a close tag or
	 * an empty tag dependent on the state of the output.
	 *
	 * @throws IOException if any errors occur
	 */
	public abstract void endElement () throws IOException;


	/*
	 * Writes the specified text escaping any characters to make the 
	 * text valid XML.
	 *
	 * @param text  the text to escape and write
	 * @throws IOException if any errors occur
	 */
	public abstract void write (String text) throws IOException;


	/*
	 * Writes the specified text as is without escaping any invalid
	 * XML characters.
	 *
	 * @param s the String to write as is
	 * @throws IOException if any errors occur
	 */
	public abstract void writeRaw (String s) throws IOException;


	/**
	 * Close the XmlAbstractWriter. This method is implemented
	 * to do nothing, so if you wish a subclass of this abstract class to
	 * do something for <code>close()</code> you must override this method.
	 *
	 * @throws IOException if any errors occur
	 */
	public void close () throws IOException {
	}


	/**
	 * Flush the XmlAbstractWriter. This method is implemented
	 * to do nothing, so if you wish a subclass of this abstract class to
	 * do something for <code>flush()</code> you must override this method.
	 *
	 * @throws IOException if any errors occur
	 */
	public void flush () throws IOException {
	}


	/**
	 * Reset the state of the XmlAbstractWriter. This method is implemented
	 * to do nothing, so if you wish a subclass of this abstract class to
	 * do something for <code>reset()</code> you must override this method.
	 *
	 * @throws IOException if any errors occur
	 */
	public void reset () throws IOException {
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

		if (text != null) {
			startElement(nodeName);
			write(text);
			endElement();
		}
	}

	
	/**
	 * Write the element with the specified Boolean as the PCDATA. Element
	 * will not be written if <code>flag</code> is <code>null</code>.
	 *
	 * @param  nodeName  the name for the element
	 * @param  flag      the Boolean for the PCDATA
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, Boolean flag) throws IOException {
  
  		if (flag != null) {
  			write(nodeName, flag.toString());
		}
  	}


	/**
	 * Write the element with the specified Date as the PCDATA. Element
	 * will not be written if <code>date</code> is <code>null</code>.
	 *
	 * @param  nodeName  the name for the element
	 * @param  date      the Date for the PCDATA
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, Date date) throws IOException {

		if (date != null) {
			write(nodeName, ISO8601DateFormat.format(date,
					ISO8601DateFormat.EXTENDED_DATETIME));
		}
	}


	/**
	 * Write the attribute with the specified Date as the value. Element
	 * will not be written if <code>date</code> is <code>null</code>.
	 *
	 * @param  nodeName  the name for the element
	 * @param  date      the Date value
	 * @param  format    the format for the date, see
	 *                   <code>com.messners.ajf.util.ISO8601DateFormat</code>
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, Date date, int format)
			throws IOException {

		if (date != null) {
			write(nodeName, ISO8601DateFormat.format(date, format));
		}
	}


	/**
	 * Write the element with the specified Number as the PCDATA. Element
	 * will not be written if <code>num</code> is <code>null</code>.
	 *
	 * @param  nodeName  the name for the element
	 * @param  num       the Number for the PCDATA
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, Number num) throws IOException {

		if (num == null) {
			return;
		}

		write(nodeName, num.toString());
	}


	/**
	 * Write the element with the <code>int</code> value as the PCDATA.
	 *
	 * @param  nodeName  the name for the element
	 * @param  num       the int value for the PCDATA
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, int num) throws IOException {

		String s = Integer.toString(num);
		write(nodeName, s);
	}


	/**
	 * Write the element with the <code>int</code> value and radix
	 * as the PCDATA. 
	 *
	 * @param  nodeName  the name for the element
	 * @param  num       the int value for the PCDATA
	 * @param  radix     the radix to output the int value with
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, int num, int radix) throws IOException {

		String s = Integer.toString(num, radix);
		write(nodeName, s);
	}


	/**
	 * Write the element with the <code>long</code> value as the PCDATA.
	 *
	 * @param  nodeName  the name for the element
	 * @param  num       the long value for the PCDATA
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, long num) throws IOException {

		String s = Long.toString(num);
		write(nodeName, s);
	}


	/**
	 * Write the element with the <code>float</code> value as the PCDATA.
	 *
	 * @param  nodeName  the name for the element
	 * @param  num       the float value for the PCDATA
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, float num) throws IOException {

		String s = Float.toString(num);
		write(nodeName, s);
	}


	/**
	 * Write the element with the <code>double</code> value as the PCDATA.
	 *
	 * @param  nodeName  the name for the element
	 * @param  num       the double value for the PCDATA
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, double num) throws IOException {

		String s = Double.toString(num);
		write(nodeName, s);
	}


	/**
	 * Write the element with the <code>short</code> value as the PCDATA.
	 *
	 * @param  nodeName  the name for the element
	 * @param  num       the short value for the PCDATA
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, short num) throws IOException {

		String s = Short.toString(num);
		write(nodeName, s);
	}


	/**
	 * Write the element with the <code>boolean</code> value as the PCDATA.
	 *
	 * @param  nodeName  the name for the element
	 * @param  flag      the boolean value for the PCDATA
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, boolean flag) throws IOException {

		write(nodeName, (flag ? "true" : "false"));
	}


	/**
	 * Write the given Collection out as an XML Schema list type.
	 *
	 * @param  nodeName  the name for the element
	 * @param  list      the Collection to write
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, Collection<?> list) throws IOException {

		if (nodeName == null) {
			return;
		}

		if (list == null) {
			write(nodeName, "");
			return;
		}

		int size = list.size();
		if (size == 0) {
			write(nodeName, "");
			return;
		}


		boolean firstTime = true;
		StringBuffer buf = new StringBuffer();
		Iterator<?> iterator = list.iterator();
		while (iterator.hasNext()) {

			Object obj = iterator.next();
			if (obj != null) {
			
				if (!firstTime) {
					buf.append(' ');
				} else {
					firstTime = false;
				}

				if (obj instanceof String) {
					buf.append((String)obj);
				} else {
					buf.append(obj.toString());
				}
			}
		}

		write(nodeName, buf.toString());
	}


	/**
	 * Write the given String array out as an XML Schema list type.
	 *
	 * @param  nodeName  the name for the element
	 * @param  sarray    the String[] to write
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, String sarray[]) throws IOException {

		if (nodeName == null) {
			return;
		}

		if (sarray == null) {
			write(nodeName, "");
			return;
		}

		int size = sarray.length;
		if (size == 0) {
			write(nodeName, "");
			return;
		}


		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < size; i++) {

			String s = sarray[i];
			if (s == null) {
				continue;
			}

			if (i != 0) {
				buf.append(' ');
			}

			buf.append(s);
		}

		write(nodeName, buf.toString());
	}


	/**
	 * Write the given int array out as an XML Schema list type.
	 *
	 * @param  nodeName  the name for the element
	 * @param  iarray    the int[] to write
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, int iarray[]) throws IOException {

		if (nodeName == null) {
			return;
		}

		if (iarray == null) {
			write(nodeName, "");
			return;
		}

		int size = iarray.length;
		if (size == 0) {
			write(nodeName, "");
			return;
		}


		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < size; i++) {

			if (i != 0) {
				buf.append(' ');
			}

			buf.append(iarray[i]);
		}

		write(nodeName, buf.toString());
	}


	/**
	 * Write the given double array out as an XML Schema list type.
	 *
	 * @param  nodeName  the name for the element
	 * @param  darray    the double[] to write
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, double darray[]) throws IOException {

		if (nodeName == null) {
			return;
		}

		if (darray == null) {
			write(nodeName, "");
			return;
		}

		int size = darray.length;
		if (size == 0) {
			write(nodeName, "");
			return;
		}


		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < size; i++) {

			if (i != 0) {
				buf.append(' ');
			}

			buf.append(darray[i]);
		}

		write(nodeName, buf.toString());
	}


	/**
	 * Write the element with the specified Map instance as the data.
	 *
	 * @param  nodeName  the name for the element
	 * @param  map       the Map to write
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, Map<?, ?> map) throws IOException {
		write(nodeName, map, MapConstants.MAP_ELEMENT);
	}


	/**
	 * Write the element with the specified Map instance as the data.
	 *
	 * @param  nodeName  the name for the element
	 * @param  map       the Map to write
	 * @param  mapName
	 * @throws IOException if any errors occur
	 */
	public void write (String nodeName, Map<?, ?> map, String mapName) 
			throws IOException {

		if (nodeName == null || map == null) {
			return;
		}

		startElement(nodeName);
		writeMap(map, mapName);
		endElement();
	}


	/**
	 * Writes out the specified map using the specified mapName 
	 * for the XML node name.
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

				write(buf.toString());
				writeRaw(newLine);
			}
		}
	}


	/**
	 * Write the attribute with the specified Boolean value. Attribute
	 * will not be written if <code>flag</code> is <code>null</code>.
	 *
	 * @param  nodeName  the name for the attribute
	 * @param  flag      the Boolean value
	 * @throws IOException if any errors occur
	 */
	public void writeAttr (String nodeName, Boolean flag) throws IOException {
  
  		if (flag != null) {
  			writeAttr(nodeName, flag.toString());
		}
  	}


	/**
	 * Write the attribute with the specified Date as the value. Attribute
	 * will not be written if <code>date</code> is <code>null</code>.
	 *
	 * @param  nodeName  the name for the attribute
	 * @param  date      the Date value
	 * @throws IOException if any errors occur
	 */
	public void writeAttr (String nodeName, Date date) throws IOException {

		if (date != null) {
			writeAttr(nodeName, ISO8601DateFormat.format(date,
					ISO8601DateFormat.EXTENDED_DATETIME));
		}
	}


	/**
	 * Write the attribute with the specified Date as the value. Attribute
	 * will not be written if <code>date</code> is <code>null</code>.
	 *
	 * @param  nodeName  the name for the attribute
	 * @param  date      the Date value
	 * @param  format    the format for the date, see
	 *                   <code>com.messners.ajf.util.ISO8601DateFormat</code>
	 * @throws IOException if any errors occur
	 */
	public void writeAttr (String nodeName, Date date, int format)
			throws IOException {

		if (date != null) {
			writeAttr(nodeName, ISO8601DateFormat.format(date, format));
		}
	}


	/**
	 * Write the attribute with the specified Number as the value. Attribute
	 * will not be written if <code>num</code> is <code>null</code>.
	 *
	 * @param  nodeName  the name for the attribute
	 * @param  num       the Number value
	 * @throws IOException if any errors occur
	 */
	public void writeAttr (String nodeName, Number num) throws IOException {

		if (num == null) {
			return;
		}

		writeAttr(nodeName, num.toString());
	}


	/**
	 * Write the attribute with the specified <code>int</code> value.
	 *
	 * @param  nodeName  the name for the attribute
	 * @param  num       the int value
	 * @throws IOException if any errors occur
	 */
	public void writeAttr (String nodeName, int num) throws IOException {

		String s = Integer.toString(num);
		writeAttr(nodeName, s);
	}


	/**
	 * Write the attribute with the specified <code>long</code> value.
	 *
	 * @param  nodeName  the name for the attribute
	 * @param  num       the long value
	 * @throws IOException if any errors occur
	 */
	public void writeAttr (String nodeName, long num) throws IOException {

		String s = Long.toString(num);
		writeAttr(nodeName, s);
	}


	/**
	 * Write the attribute with the specified <code>float</code> value.
	 *
	 * @param  nodeName  the name for the attribute
	 * @param  num       the float value
	 * @throws IOException if any errors occur
	 */
	public void writeAttr (String nodeName, float num) throws IOException {

		String s = Float.toString(num);
		writeAttr(nodeName, s);
	}


	/**
	 * Write the attribute with the specified <code>double</code> value.
	 *
	 * @param  nodeName  the name for the attribute
	 * @param  num       the double value
	 * @throws IOException if any errors occur
	 */
	public void writeAttr (String nodeName, double num) throws IOException {

		String s = Double.toString(num);
		writeAttr(nodeName, s);
	}


	/**
	 * Write the attribute with the specified <code>short</code> value.
	 *
	 * @param  nodeName  the name for the attribute
	 * @param  num       the short value
	 * @throws IOException if any errors occur
	 */
	public void writeAttr (String nodeName, short num) throws IOException {

		String s = Short.toString(num);
		writeAttr(nodeName, s);
	}


	/**
	 * Write the attribute with the specified <code>boolean</code> value.
	 *
	 * @param  nodeName  the name for the attribute
	 * @param  flag      the boolean value
	 * @throws IOException if any errors occur
	 */
	public void writeAttr (String nodeName, boolean flag) throws IOException {

		writeAttr(nodeName, (flag ? "true" : "false"));
	}


	/**
	 * Writes the XML prolog using "1.0" as the version. This is usually and
	 * can only be located as the the first thing found in an XML file.
	 *
	 * @throws IOException if any errors occur
	 */
	public void writeXmlProlog () throws IOException {
		writeXmlProlog("1.0", null, false);
	}

	

	/**
	 * Writes the XML prolog. This is usually and can only be located as
	 * the first thing found in an XML file.
	 *
	 * @param version  the version String 
	 * @param encoding the encoding String
	 * @param standAlone if true will include a standalone="true" in the prolog
	 * @throws IOException if any errors occur
	 */
	public void writeXmlProlog (String version, String encoding,
			boolean standAlone) throws IOException {

	
		if (standAlone) {

			if (encoding != null && encoding.length() > 0) {

				writeRaw("<?xml version=\"" + version + 
					"\" encoding=\"" + encoding + "\"" +
					" standalone=\"true\"?>" + newLine);

			} else {

				writeRaw("<?xml version=\"" + version + "\"" +
					" standalone=\"true\"?>" + newLine);
			}

		} else {

			if (encoding != null && encoding.length() > 0) {

				writeRaw("<?xml version=\"" + version + 
					"\" encoding=\"" + encoding + "\"?>" + newLine);

			} else {

				writeRaw("<?xml version=\"" + version + "\"?>" + newLine);
			}
		}
	}
}

