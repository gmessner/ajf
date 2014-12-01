package com.messners.ajf.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * DateFormat that only supports date strings conforming to ISO8601.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ISO8601DateFormat extends DateFormat {

	private static final long serialVersionUID = 1L;
	
	/*
	 * Constants
	 */
	public static final int DATETIME = 0;
	public static final int DATE	 = 1;
	public static final int TIME	 = 2;

	public static final int BASIC_DATETIME	  = 3;
	public static final int EXTENDED_DATETIME = 4;
	public static final int BASIC_DATE		  = 5;
	public static final int EXTENDED_DATE	  = 6;
	public static final int BASIC_TIME		  = 7;
	public static final int EXTENDED_TIME	  = 8;

	protected static int NUM_FORMATS = 9;

	public static final String EXTENDED_DATE_FORMAT = "yyyy-MM-dd";
	public static final String BASIC_DATE_FORMAT    = "yyyyMMdd";
	public static final String EXTENDED_TIME_FORMAT = "HH:mm:ss";
	public static final String BASIC_TIME_FORMAT	= "HHmmss";

	public static final String EXTENDED_DATETIME_FORMAT =
			EXTENDED_DATE_FORMAT + "'T'" + EXTENDED_TIME_FORMAT;
	public static final String BASIC_DATETIME_FORMAT = 
			BASIC_DATE_FORMAT + "'T'" + BASIC_TIME_FORMAT;

	protected static boolean defaultLenient = true;
	protected boolean lenient = true;


	/**
	 * The SimpleDateFormat used to format the dates
	 */
	protected transient SimpleDateFormat formatter;
	protected transient SimpleDateFormat formatter1;


	/**
	 * The SimpleDateFormat used to parse the date strings
	 */
	protected transient SimpleDateFormat parser;
	protected transient SimpleDateFormat parser1;


	protected int format;
	protected TimeZone tz = TimeZone.getDefault();
	protected static boolean defaultIncludeTz = false;
	protected boolean includeTz = false;


	protected static ISO8601DateFormat instances[] =
							new ISO8601DateFormat[NUM_FORMATS];


	public static synchronized ISO8601DateFormat getInstance (int format) {


		if (format < 0 || format >= NUM_FORMATS) {
			format = EXTENDED_DATETIME;
		}

		ISO8601DateFormat df = instances[format];
		if (df != null) {
			return (df);
		}

		df = new ISO8601DateFormat(format);
		instances[format] = df;
		return (df);
	}


	/**
	 * Create a formatter for extended dateTime "yyyy-MM-ddTHH:mm:ss,SSS".
	 */
	public ISO8601DateFormat () {

		lenient = defaultLenient;
		includeTz = defaultIncludeTz;
		setFormat(EXTENDED_DATETIME);
	}


	/**
	 * Create a formatter for the specified portion of ISO8601.
	 *
	 * @param format  the type of format to use:<pre>
	 *
	 *		DATETIME          "yyyyMMdd'T'HHmmss" also parses
	 *                              "yyyy-MM-dd'T'HH:mm:ss"
	 *		DATE              "yyyyMMdd" also parses "yyyy-MM-dd"
	 *		TIME              "HHmmss" also parses "HH:mm:ss"
	 *		BASIC_DATETIME    "yyyyMMdd'T'HHmmss"
	 *		EXTENDED_DATETIME "yyyy-MM-dd'T'HH:mm:ss"
	 *		BASIC_DATE        "yyyyMMdd'T'HHmmss"
	 *		EXTENDED_DATE     "yyyy-MM-dd"
	 *		BASIC_TIME        "HHmmss"
	 *		EXTENDED_TIME     "HH:mm:ss"
	 * </pre>
	 */
	public ISO8601DateFormat (int format) {

		lenient = defaultLenient;
		includeTz = defaultIncludeTz;
		setFormat(format);
	}


	/**
	 * Gets the format constant for this formatter.
	 */
	public int getFormat () {
		return (format);
	}


	/**
	 * Create a SimpleDateFormat to do the actual formatting.
	 */
	protected void setFormat (int format) {

		String format_str;
		if (format == DATETIME || format == BASIC_DATETIME) {

			format_str = BASIC_DATETIME_FORMAT;
			formatter = new SimpleDateFormat(format_str);

			format_str = BASIC_DATETIME_FORMAT + ".SSS";
			formatter1 = new SimpleDateFormat(format_str);

			format_str = BASIC_DATETIME_FORMAT + ".SSSSSSSSSS";
			parser = new SimpleDateFormat(format_str);

			format_str = BASIC_DATETIME_FORMAT;
			parser1 = new SimpleDateFormat(format_str);

		} else if (format == DATE || format == BASIC_DATE) {

			format_str = BASIC_DATE_FORMAT;
			formatter = new SimpleDateFormat(BASIC_DATE_FORMAT);
			parser = new SimpleDateFormat(BASIC_DATE_FORMAT);

		} else if (format == TIME || format == BASIC_TIME) {

			format_str = BASIC_TIME_FORMAT;
			formatter = new SimpleDateFormat(format_str);

			format_str = BASIC_TIME_FORMAT + ".SSS";
			formatter1 = new SimpleDateFormat(format_str);

			format_str = BASIC_TIME_FORMAT + ".SSSSSSSSSS";
			parser = new SimpleDateFormat(format_str);

			format_str = BASIC_TIME_FORMAT;
			parser1 = new SimpleDateFormat(format_str);

		} else if (format == EXTENDED_DATETIME) {

			format_str = EXTENDED_DATETIME_FORMAT;
			formatter = new SimpleDateFormat(format_str);

			format_str = EXTENDED_DATETIME_FORMAT + ".SSS";
			formatter1 = new SimpleDateFormat(format_str);

			format_str = BASIC_DATETIME_FORMAT + ".SSSSSSSSSS";
			parser = new SimpleDateFormat(format_str);

			format_str = BASIC_DATETIME_FORMAT;
			parser1 = new SimpleDateFormat(format_str);

		} else if (format == EXTENDED_DATE) {

			formatter = new SimpleDateFormat(EXTENDED_DATE_FORMAT);
			parser = new SimpleDateFormat(BASIC_DATE_FORMAT);

		} else if (format == EXTENDED_TIME) {

			format_str = EXTENDED_TIME_FORMAT;
			formatter = new SimpleDateFormat(format_str);

			format_str = EXTENDED_TIME_FORMAT + ".SSS";
			formatter1 = new SimpleDateFormat(format_str);

			format_str = BASIC_TIME_FORMAT + ".SSSSSSSSSS";
			parser = new SimpleDateFormat(format_str);

			format_str = BASIC_TIME_FORMAT;
			parser1 = new SimpleDateFormat(format_str);

		} else {

			throw new RuntimeException("invalid ISO8601DateFormat");
		}

		this.format = format;
		formatter.setTimeZone(tz);

		if (parser != null) {
			parser.setTimeZone(tz);
		}

		if (parser1 != null) {
			parser1.setTimeZone(tz);
		}

		if (formatter1 != null) {
			formatter1.setTimeZone(tz);
		}

		setLenient(lenient);
	}


	/**
	 * Change the timezone.
	 *
	 * @param tz The new timezone.
	 */
	public void setTimeZone (TimeZone tz) {

		this.tz = tz;
		formatter.setTimeZone(tz);

		if (parser != null) {
			parser.setTimeZone(tz);
		}

		if (parser1 != null) {
			parser1.setTimeZone(tz);
		}

		if (formatter1 != null) {
			formatter1.setTimeZone(tz);
		}
	}


	/**
	 * Tell whether date/time parsing is to be lenient.
	 */
	public boolean isLenient () {
		return (lenient);
	}


	/**
	 * Specify whether or not date/time parsing is to be lenient. With
	 * lenient parsing, the parser may use heuristics to interpret inputs
	 * that do not precisely match this object's format. With strict parsing,
	 * inputs must match this object's format.
	 *
	 * @param  flag  when true, parsing is lenient
	 */
	public void setLenient (boolean flag) {

		lenient = flag;

		formatter.setLenient(flag);

		if (parser != null) {
			parser.setLenient(flag);
		}

		if (parser1 != null) {
			parser1.setLenient(flag);
		}

		if (formatter1 != null) {
			formatter1.setLenient(flag);
		}
	}


	/**
	 * Set the default for whether or not date/time parsing is to be lenient.
	 * With lenient parsing, the parser may use heuristics to interpret inputs
	 * that do not precisely match this object's format. With strict parsing,
	 * inputs must match this object's format.
	 *
	 * @param  flag  when true, by default parsing is lenient
	 */
	public static void setDefaultLenient (boolean flag) {
		defaultLenient = flag;
	}


	/**
	 * Get the default for whether or not date/time parsing is to be lenient.
	 * With lenient parsing, the parser may use heuristics to interpret inputs
	 * that do not precisely match this object's format. With strict parsing,
	 * inputs must match this object's format.
	 */
	public static boolean getDefaultLenient () {
		return (defaultLenient);
	}


	/**
	 * Indicate whether thet timezone should be included in the format.
	 * Default is false.
	 *
	 * @param flag  true if the timezone is part of the date format,
	 *              otherwise false.
	 */
	public void setIncludeTimeZone (boolean flag) {
		includeTz = flag;
	}


	public static void setDefaultIncludeTimeZone (boolean flag) {
		defaultIncludeTz = flag;
	}


	public static boolean getDefaultIncludeTimeZone () {
		return (defaultIncludeTz);
	}


	/**
	 * Get the flag indicating whether the timezone should be included in
	 * the format.
	 *
	 * @return true if the timezone is part of the date format,
	 *         otherwise return false.
	 */
	public boolean getIncludeTimeZone () {
		return (includeTz);
	}


	/**
	 * Get the current timezone.
	 *
	 * @return The current timezone.
	 */
	public TimeZone getTimeZone() {
		return (tz);
	}


	public synchronized StringBuffer format (
				Date date, StringBuffer buffer, FieldPosition pos) {

		/*
		 * Do most of the work via SimpleDateFormat
		 */
		if (formatter1 != null) {

			long time = date.getTime();
			if ((time % 1000) != 0) {
				buffer.append(formatter1.format(date));
			} else {
				buffer.append(formatter.format(date));
			}

		} else {

			buffer.append(formatter.format(date));
		}


		/*
		 * Append the current timezone if required.
		 */
		if (includeTz) {

			Calendar cal = Calendar.getInstance(tz);
			cal.setTime(date);

			int tzOffset = cal.get(Calendar.ZONE_OFFSET)
							+ cal.get(Calendar.DST_OFFSET);

			if (tzOffset == 0) {
				buffer.append("Z");
			} else {

				int hours = tzOffset/3600000;
				if (hours >= 0) {

					buffer.append('+');
					if (hours < 10) {
						buffer.append('0');
					}
					buffer.append(hours);

				} else {

					buffer.append('-');
					if (hours > -10) {
						buffer.append('0');
					}
					buffer.append(hours * -1);
				}


				int minutes = (tzOffset % 3600000) / 60000;
				if (minutes < 0) {
					minutes *= -1;
				}

				if (format == EXTENDED_DATETIME ||
						format == EXTENDED_DATE ||
						format == EXTENDED_TIME) {
					buffer.append(':');
				}

				if (minutes < 10) {
					buffer.append('0');
				}
				buffer.append(minutes);
			}
		}

		return (buffer);
	}


	public synchronized Date parse (String text, ParsePosition pos) {
		text.substring(pos.getIndex());
		return (parse(text));
	}


	public synchronized Date parse (String text) {

		boolean reset_tz = false;
		TimeZone mytz = null;
		if (text.endsWith("Z")) {

			mytz = DateUtilities.UTC_TZ;

		} else {

			int len = text.length();

			if (len > 10) {
	
				if (text.charAt(len - 3) == ':') {

					char c = text.charAt(len - 5);
					char c1 = text.charAt(len - 6);

					if (c == '-' || c == '+') {

						mytz = TimeZone.getTimeZone(
									"GMT" + text.substring(len - 5));
						
					} else if (c1 == '-' || c1 == '+') {

						mytz = TimeZone.getTimeZone(
									"GMT" + text.substring(len - 6));
					}
				}
			}
		}

		if (mytz != null) {

			reset_tz = true;

			if (parser != null) {
				parser.setTimeZone(mytz);
			}

			if (parser1!= null) {
				parser1.setTimeZone(mytz);
			}
		}


		StringBuffer tbuf = new StringBuffer();
		int len = text.length();
		if (reset_tz) {
			len -= 7;
		}

		for (int i = 0; i < len; i++) {

			char c = text.charAt(i);	
			if (c != '-' && c != ':') {
				tbuf.append(c);
			}
		}

		text = tbuf.toString();
		Date d = parse(parser, text);
		if (d == null && parser1 != null) {
			d = parse(parser1, text);
		}

		if (reset_tz) {

			if (parser != null) {
				parser.setTimeZone(tz);
			}

			if (parser1 != null) {
				parser1.setTimeZone(tz);
			}
		}

		if (format == TIME || format == BASIC_TIME || format == EXTENDED_TIME) {

			Calendar cal = Calendar.getInstance(getTimeZone());
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);

			d.setTime(d.getTime() + cal.getTimeInMillis()
				+ getTimeZone().getRawOffset());

		}

		return (d);
	}


	protected Date parse (
		SimpleDateFormat parser, String text) {

			if (parser == null) {
				return (null);
			}

			try {

				return (parser.parse(text));

			} catch (Exception ignore) {

				return (null);
			}
	}


	/**
	 * Formats the given Date instance with the specified format.
	 *
	 * @param  date   the Date instance to format
	 * @param  format the format to use
	 * @return a formatted date string
	 */
	public static String format (Date date, int format) {

		if (date == null) {
			return (null);
		}

		return (getInstance(format).format(date));
	}


	/**
	 * Parses the given String using the specified format.
	 *
	 * @param  s      the String to format
	 * @param  format the format to use
	 * @return a the parsed Date instance
	 */
	public static Date parse (String s, int format) {

		if (s == null || s.length() == 0) {
			return (null);
		}

		return (getInstance(format).parse(s));
	}
}
