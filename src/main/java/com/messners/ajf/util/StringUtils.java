package com.messners.ajf.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;


/**
 * <P>Provides common string utilities not provided by the String object.
 *    NOTE:  There's no constructor so you can't instantiate this object.</P>
 */
public class StringUtils {

	/**
	 * This class cannot be instantiated, hide the the no-args constructor
	 * by declaring it private.
	 */
	private StringUtils () {
	}

	/**
	 * Determines if a string contains only alpha characters.
	 * @param text String to check.
	 * @return True if string contains only alpha characters.
	 */
	public static boolean isAlpha(String text) {
	    return (isAlpha(text, false));
	}

	/**
	 * Determines if a string contains only alpha characters.
	 * @param text String to check.
	 * @param ignoreWS If true, ignore whitespace characters.
	 * @return True if string contains only alpha characters.
	 */
	public static boolean isAlpha(String text, boolean ignoreWS) {

	    boolean textIsAlpha = false;
	    if (text.length() > 0) {
	        textIsAlpha = true;
	        char[] textChars = text.toCharArray();
	        for (int i=0; i < text.length(); i++) {
	            char ch = textChars[i];
	            if (!Character.isLetter(ch)) {
	                if (!(ignoreWS && Character.isWhitespace(ch))) {
	                    textIsAlpha = false;
	                    break;
	                }
	            }
	        }
	    }

	    return (textIsAlpha);
	}

	/**
	 * Determines if a string is empty (e&#046;g&#046; length == 0) or
	 * contains only whitespace characters.
	 *
	 * @param text String to check
	 * @return True if string contains only whitespace characters.
	 */
	public static boolean isBlank(String text) {

	    boolean textIsBlank = true;
	    if (text.length() > 0) {
	        char[] textChars = text.toCharArray();
	        for(int i=0; i < text.length(); i++) {
	            if (!Character.isWhitespace(textChars[i])) {
	                textIsBlank = false;
	                break;
	            }
	        }
	    }

	    return (textIsBlank);
	}

	/**
	 * Determines if a string contains only numeric characters.
	 *
	 * @param text String to check.
	 * @return True if string contains only numeric characters.
	 */
	public static boolean isNumeric(String text) {
	    return (isNumeric(text, 10, false));
	}

	/**
	 * Determines if a string contains only numeric characters with
     * the specified radix.
	 *
	 * @param text String to check.
         * @param radix Radix of numeric character to accept.
	 * @param ignoreWS If true, ignore whitespace characters.
	 * @return True if string contains only numeric characters.
	 */
	public static boolean isNumeric(String text, int radix, boolean ignoreWS) {

	    boolean textIsNumeric = false;
	    if (text.length() > 0) {
	        textIsNumeric = true;
	        char[] textChars = text.toCharArray();
	        for(int i=0; i < text.length(); i++) {
	            char ch = textChars[i];
	            if (!Character.isDigit(ch) || 
                            (Character.digit(ch, radix) == -1)) {
	                if (!(ignoreWS && Character.isWhitespace(ch))) {
	                    textIsNumeric = false;
    	                    break;
	                }
	            }
	        }
	    }

	    return (textIsNumeric);
	}

	/**
	 * Determines if a string contains only alpha numeric characters.
	 * @param text String to check.
	 * @return True if string contains only alpha numeric characters.
	 */
	public static boolean isAlphaNumeric(String text) {
	    return (isAlphaNumeric(text, false));
	}

	/**
	 * Determines if a string contains only alpha numeric characters.
	 * @param text String to check.
	 * @param ignoreWS If true, ignore whitespace characters.
	 * @return True if string contains only alpha numeric characters.
	 */
	public static boolean isAlphaNumeric(String text, boolean ignoreWS) {
	    boolean textIsAlphaNumeric = false;
	    if (text.length() > 0) {
	        textIsAlphaNumeric = true;
	        char[] textChars = text.toCharArray();
	        for (int i=0; i < text.length(); i++) {
	            char ch = textChars[i];
	            if (!Character.isLetterOrDigit(ch)) {
	                if (!(ignoreWS && Character.isWhitespace(ch))) {
	                    textIsAlphaNumeric = false;
	                    break;
	                }
	            }
	        }
	    }

	    return (textIsAlphaNumeric);
	}

	/**
	 * Compact all the whitespace from a string.
	 *
	 * @param  str  the String to compact
	 * @return the compacted String
	 */
	public static String compactWhitespace (String str) {

		if (str == null) {
			return (null);
		}

		int len = str.length();
		StringBuffer buf = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
				char c = str.charAt(i);
				if (!Character.isWhitespace(c)) {
						buf.append(c);
				}
		}

		return (buf.toString());
	}

	/**
	 * Remove the beginning and trailing quotes from a string.
	 *
	 * @param  in  the string to remove the quotes from.
	 * @return  The de-quotified string. If the string is malformed 
	 * (begins with but does not end with a quote) null is returned.
	 */
	public static String removeQuotes (String in) {

		if (in == null) {
			return (null);
		}

		/*
		 * Parse off quotes
		 */
		in = in.trim();
		int index = in.indexOf("\"");
		if (index == 0) {
			index = in.indexOf("\"", 1);
			if (index < 0) {
				return (null);
			}

			in = in.substring(1, index);
		}

		return (in);
	}



	/**
	 * Splits up a delimited string into an array of strings. This 
	 * differentiates itself from StringTokenizer in that the
	 * entire delimiter string is considered as the delimiter
	 * where StringTokenizer will tokenize on any character in
	 * the delimiter.
	 *
	 * @param  list  The delimited string to split up.
	 * @param  delimiter  The delimiter to split the string up on.
	 * @return a String[] containing the split string
	 */
	public static String [] splitDelimitedString (
		String list, String delimiter) {

		if (list == null) {
			return (null);
		}

		ArrayList<String> fields = new ArrayList<String>();

		list = list.trim();
		if (delimiter == null) {
			delimiter = ";";
		}

		int delimeter_len = delimiter.length();

		int first_delim = 0;
		int next_delim = list.indexOf(delimiter, 1);
		while (next_delim >= 0) {

			fields.add(list.substring(first_delim, next_delim));
			first_delim = next_delim + delimeter_len;
			next_delim = list.indexOf(delimiter, first_delim);
		}

		/*
		 * Add the last (or only) field
		 */
		fields.add(list.substring(first_delim));

		/*
		 * Convert the ArrayList into String []
		 */
		String strings[] = new String[fields.size()];
		fields.toArray(strings);
		return (strings);
	}

	/**
	 * <p>Reads characters from a Reader and creates a String
	 * from those characters.</p>
	 *
	 * NOTE: This method will terminate the String if a null character
	 * is encountered.
	 *
	 * @param reader  the Reader to read characters from
	 * @return the built up String
	 * @throws IOException on any error
	 */
	public static String toString (Reader reader) throws IOException {

		StringBuffer buf = new StringBuffer(1024);
		while (true) {

			int c = reader.read();
			if (c <= 0) {
				break;
			}


			buf.append(c);
		}

		return (buf.toString());
	}

	/**
	 * <p>Reads characters from a Reader and creates an array of String
	 * from those characters, terminating each String when a newline or
	 * carriage return is read.</p>
	 *
	 * NOTE: A null character is also considered an EOL marker.
	 *
	 * @param reader  the Reader to read characters from
	 * @return the built up String
	 * @throws IOException on any error
	 */
	public static String[] toStringArray (Reader reader) throws IOException {

		ArrayList<String> list = new ArrayList<String>();
		String line;
		while ((line = readLine(reader)) != null) {

			if (line.length() > 0) {
				list.add(line);
			}
		}

		String strings[] = new String[list.size()];
		return list.toArray(strings);
	}


	/**
	 * <p>Reads characters from a Reader until EOL or null is encountered.</p>
	 *
	 * NOTE: This method will terminate the read if a null character
	 * is encountered.
	 *
	 * @param reader  the Reader to read characters from
	 * @return the built up String
	 * @throws IOException on any error
	 *
	 */
	public static String readLine (Reader reader) throws IOException {

		StringBuffer buf = new StringBuffer(80);
		boolean eol = false;
		boolean lastWasCr = false;
		int nread = 0;
		boolean markSupported = reader.markSupported();

		do {

			int c = reader.read();
			switch (c) {

				case -1:
					eol = true;
					break;

				case 0:
					eol = true;
					nread++;
					break;

				case '\r':

					nread++;
					if (markSupported) {
						reader.mark(1);
						lastWasCr = true;
					} else {
						eol = true;
					}

					break;

				case '\n':
					nread++;
					eol = true;
					break;

				default:

					nread++;

					if (lastWasCr) {
						reader.reset();
						eol = true;
					} else {
						buf.append((char)c);
					}

					break;
			}

		} while (!eol);


		if (nread == 0) {
			return (null);
		}

		return (buf.toString());
	}

	/**
	 * <p>Reads characters from an InputStream until 
	 * EOL or an exception is encountered.</p>
	 *
	 * NOTE: This method will terminate the read if a null character
	 * is encountered.
	 *
	 * @param in the InputStream to read characters from
	 * @return the built up String
	 * @throws IOException on any error
	 *
	 */
	public static String readLine (InputStream in) throws IOException {

		StringBuffer buf = new StringBuffer(80);
		boolean markSupported = in.markSupported();
		boolean eol = false;
		boolean lastWasCr = false;
		int nread = 0;

		do {

			int c = in.read();
			switch (c) {

				case -1:
					eol = true;
					break;

				case 0:
					eol = true;
					nread++;
					break;

				case '\r':
					nread++;

					if (markSupported) {
						in.mark(1);
						lastWasCr = true;
					} else {
						eol = true;
					}

					break;

				case '\n':
					nread++;
					eol = true;
					break;

				default:

					nread++;

					if (lastWasCr) {
						in.reset();
						eol = true;
					} else {
						buf.append((char)c);
					}

					break;
			}

		} while (!eol);


		if (nread == 0) {
			return (null);
		}

		return (buf.toString());
	}

	/**
	 * Looks thru the provided String to see if it contains the specified
	 * word (only complete words are considered).
	 *
	 * @param  s  the String to check
	 * @param  word  the word to look for
	 * @return true if s contains word, false otherwise
	 */
	public static boolean containsWord (String s, String word) {

		if (s == null || word == null) {
			return (false);
		}

		int index = s.indexOf(word);
		if (index < 0) {
			return (false);
		}

		if (index > 0) {

			char c = s.charAt(index - 1);
			if (!Character.isWhitespace(c) && 
					c != '.' && c != ':' && c != ';' &&
					c != '!' && c != '(' && c != '[') { 

				return (false);
			}
		}

		int len = word.length();
		if (index + len == s.length()) {
			return (true);
		}
		

		char c = s.charAt(index + len);
		if (!Character.isWhitespace(c) && 
				c != '.' && c != ':' && c != ';' &&
				c != '!' && c != ')' && c != ']') { 

			return (false);
		}
		
		return (true);
	}

	/**
	 * Gets a String containing the stack trace for the specified Throwable.
	 *
	 * @param t the Throwable to get the stack trace for
	 * @return the stack trace as a String
	 */
	public static final String stackTraceToString (Throwable t) {

		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(buf);
		t.printStackTrace(out);
		out.flush();
		return (buf.toString());
	}
}
