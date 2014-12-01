package com.messners.ajf.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JLabel;
import javax.swing.Timer;


/**
 * <p>Constructs a JLabel that will automatically updated with the system time
 * once per second.</p>
 *
 * Utilizes java.text.SimpleDateFormat to format the date.
 *
 * @author  Greg Messner <greg@messners.com>
 */

public class ClockLabel extends JLabel implements ActionListener {

	private static final long serialVersionUID = 1L;

	/**
	 * The format for the system time display
	 */
	private SimpleDateFormat clockFormat;


	/**
	 * Used to hold the current Date instance.
	 */
	private static Date date;
	static {
		date = new Date();
	}


	/**
	 */
	private static Timer timer;
	static {
		timer = new Timer(1000, new DateUpdater());
		timer.start();
	}


	/**
	 * Creates a ClockLabel instance with "HH;mm:ss" as the date format.
	 */
	public ClockLabel () {
		this(null);
	}


	/**
	 * Creates a ClockLabel instance with the specified date format.
	 *
	 * @param format the date format to use, see java.text.SimpleDateFormat,
	 * if format is null, "HH:mm:ss" will be used as the format
	 */
	public ClockLabel (String format) {

		super();

		if (format == null || format.length() == 0) {
			format = "HH:mm:ss";
		}

		clockFormat  = new SimpleDateFormat(format);
		setText(clockFormat.format(date));
		
		synchronized (timer) {
			timer.addActionListener(this);
		}
	}


	public void finalize () {

		synchronized (timer) {
			timer.removeActionListener(this);
		}
	}


	/**
	 * Sets the TimeZone to use while formatting.
	 */
	public void setTimeZone (TimeZone tz) {
		clockFormat.setTimeZone(tz);
	}


	/**
	 * Sets the TimeZone to use while formatting.
	 */
	public void setTimeZone (String tz) {
		clockFormat.setTimeZone(TimeZone.getTimeZone(tz));
	}


	/**
	 * This is the handler for the system clock.
	 */
	public void actionPerformed (ActionEvent evt) {

		synchronized (date) {
			setText(clockFormat.format(date));
		}
	}


	private static class DateUpdater implements ActionListener {

		public void actionPerformed (ActionEvent evt) {

			synchronized (date) {
				date.setTime(System.currentTimeMillis());	
			}
		}
	}


	/**
	 * Add a listener for the Timer, this allows external components
	 * to be synched with the ClockLabel instances.
	 */
	public static void addClockTimerListener (ActionListener l) {

		synchronized (timer) {
			timer.addActionListener(l);
		}
	}


	public static void removeClockTimerListener (ActionListener l) {

		synchronized (timer) {
			timer.removeActionListener(l);
		}
	}
}
