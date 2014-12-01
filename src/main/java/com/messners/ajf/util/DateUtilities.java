package com.messners.ajf.util;

import java.util.Calendar;
import java.util.TimeZone;


/**
 * This class provides static utility methods for working with 
 * <code>java.util.Date</code> and other date types.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class DateUtilities {

	/**
	 * TimeZone instance for GMT.
	 */
	public static final TimeZone GMT_TZ;


	/**
	 * TimeZone instance for UTC.
	 */
	public static final TimeZone UTC_TZ;


	/**
	 * Constant for the number of milli seconds in a day.
	 */
	public static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000;


	private static Calendar midnightTodayCalendar;
	private static long midnightTodayInMillis = 0;
	private static long midnightTomorrowInMillis = 0;


	/*
	 * Initialize everything
	 */
	static {

		UTC_TZ = TimeZone.getTimeZone("UTC");
		GMT_TZ = TimeZone.getTimeZone("GMT");
		resetMidnight();
	}
	


	/**
	 * Gets the number of milli seconds since EPOCH for today at midnight.
	 *
	 * @return the number of milli seconds since EPOCH for today at midnight
	 */
	public static long getTimeAtMidnightToday () {

		if (System.currentTimeMillis() >= midnightTomorrowInMillis) {
			resetMidnight();
		}

		return (midnightTodayInMillis);
	}

	/**
	 * Resets internal state on initialization or when the date has changed.
	 */
	private static synchronized void resetMidnight () {		

		midnightTodayCalendar = Calendar.getInstance(UTC_TZ);
		midnightTodayCalendar.set(Calendar.MILLISECOND, 0);
		midnightTodayCalendar.set(Calendar.SECOND, 0);
		midnightTodayCalendar.set(Calendar.MINUTE, 0);
		midnightTodayCalendar.set(Calendar.HOUR_OF_DAY, 0);

		midnightTodayInMillis = midnightTodayCalendar.getTimeInMillis();
		midnightTomorrowInMillis = midnightTodayInMillis + DAY_IN_MILLIS;
	}


	private static final StringBuffer timeOfDayBuf = new StringBuffer(8);

	/**
	 * Gets a time of day String for the specified hours, minutes, and 
	 * seconds.
	 *
	 * @param hours   the hours value
	 * @param minutes the minutes value
	 * @param seconds the seconds value
	 * @return a formatted time of day (HH:MM:SS)
	 */
	public static final String getTimeOfDay (
					int hours, int minutes, int seconds) {

		synchronized (timeOfDayBuf) {

			timeOfDayBuf.setLength(0);

			if (hours < 10) {
				timeOfDayBuf.append("0");
			}
			timeOfDayBuf.append(hours);
			timeOfDayBuf.append(':');
	
			if (minutes < 10) {
				timeOfDayBuf.append("0");
			}
			timeOfDayBuf.append(minutes);
			timeOfDayBuf.append(':');
	
			if (seconds < 10) {
				timeOfDayBuf.append("0");
			}
			timeOfDayBuf.append(seconds);

			return (timeOfDayBuf.toString());
		}
	}


	/**
	 * Hide the constructor.
	 */
	private DateUtilities () {
	}
}
