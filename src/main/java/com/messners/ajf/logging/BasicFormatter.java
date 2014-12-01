package com.messners.ajf.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;


/**
 * This class needs javadoc.
 */

public class BasicFormatter extends Formatter {

	/**
	 * Message StringBuffer
	 */
	private StringBuffer buffer = new StringBuffer();

	/**
 	 * System's idea of line end 
	 */
	private static final String newLine = System.getProperty("line.separator");


	synchronized public String format (LogRecord logRecord) {

		buffer.setLength(0);

		buffer.append('[');
		buffer.append(logRecord.getLoggerName());
		buffer.append(']');
		buffer.append(' ');
			
		buffer.append(logRecord.getMessage());
		buffer.append(newLine);

		return (buffer.toString());
	}
}

