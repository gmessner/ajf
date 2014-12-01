package com.messners.ajf.ui;

import java.awt.Toolkit;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;


/**
 *
 *
 * @author Greg Messner <greg@messners.com>
 */
public class DoubleDocument extends PlainDocument {

    private static final long serialVersionUID = 1L;
    protected int maxLength = -1;
	protected boolean beepOnError = true;
	protected Double minValue = null;
	protected Double maxValue = null;

	protected boolean dontValidate = false;
	protected boolean validateMin = false;

	protected static Toolkit toolkit = Toolkit.getDefaultToolkit();
	protected static final Double DOUBLE_ZERO = new Double(0.0);
	

	/**
	 * Creates an instance that will accept any double value.
	 */
	public DoubleDocument () {
	}


	/**
	 * Creates an instance that will accept any double value up to a maximum
	 * length.
	 *
	 * @param  maxLength  the maximum length of the number
	 */
	public DoubleDocument (int maxLength) {
		this.maxLength = maxLength;
	}


	/**
	 * Create an <code>DoubleDocument</code> instance that will accept
	 * the min and max valuies specified by the SpinnerNumberModel.
	 */
	public DoubleDocument (SpinnerNumberModel snm) {

		Object min = snm.getMinimum();
		if (min instanceof Double) {
			minValue = (Double)min;
			validateMin = (minValue.compareTo(DOUBLE_ZERO) != 1);
		}
			
		Object max = snm.getMaximum();
		if (max instanceof Double) {
			maxValue = (Double)max;
		}
	}


	/**
	 * Set the current contents of this document to the specified text.
	 *
	 * @param text  the text to set into this instance
	 */
	public void setText (String text) {

		if (getLength() > 0) {

			try {
				super.remove(0, getLength());
			} catch (BadLocationException ble) {
			}
		}

		if (text != null) {

			try {
				super.insertString(0, text, null);
			} catch (BadLocationException ble) {
			}
		}
	}


	public void setMaxLength (int maxLength) {
		this.maxLength = maxLength;
	}


	/**
	 * Get the maximum allowable value for this instance.
	 *
	 * @return the maximum allowable value for this instance or null if
	 * no maximum has been set
	 */
	public Double getMaximumValue () {
		return (maxValue);
	}


	/**
	 * Set the maximum allowable value for this instance.
	 *
	 * @param max  the maximum allowable value for this instance
	 */
	public void setMaximumValue (double max) {
		maxValue = new Double(max);
	}


	/**
	 * Set the maximum allowable value for this instance.
	 *
	 * @param max  the maximum allowable value for this instance or null
	 * for no maximum
	 */
	public void setMaximumValue (Double max) {
		maxValue = max;
	}


	/**
	 * Get the minimum allowable value for this instance.
	 *
	 * @return the minimum allowable value for this instance or null if
	 * no minimum has been set
	 */
	public Double getMinimumValue () {
		return (minValue);
	}


	/**
	 * Set the minimum allowable value for this instance.
	 *
	 * @param min  the minimum allowable value for this instance
	 */
	public void setMinimumValue (double min) {
		minValue = new Double(min);
		validateMin = (minValue.compareTo(DOUBLE_ZERO) != 1);
	}


	/**
	 * Set the minimum allowable value for this instance.
	 *
	 * @param min  the minimum allowable value for this instance or null
	 * for no minimum
	 */
	public void setMinimumValue (Double min) {
		minValue = min;
		validateMin = (minValue.compareTo(DOUBLE_ZERO) != 1);
	}


	/**
	 * This method must be implemented to be a Document.
	 * It simply Inserts a string of content.
	 */
	public void insertString (int pos, String str, AttributeSet attr) 
		throws BadLocationException {

		if (str == null) {
			return;
		}

		int len = str.length();
		if (len == 0) {
			return;
		}

		int curLength = getLength();
		if (maxLength > 0) {
			if (curLength + len > maxLength) {
				beep();
				return;
			}
		}


		if (len > 1) {

			dontValidate = true;
			int j = 0;
			for (int i = pos; i < len; i++, j++) {

				if (i == len - 1) {
					dontValidate = false;
				}

				String s = str.substring(j, j + 1);	
				insertString(i, s, attr);
			}

			return;
		}


		/*
		 *  If it's a digit character then go on, otherwise enter the 
		 *  special checking mode
		 */
		char digit_char = str.charAt(0);
		if (!Character.isDigit(digit_char) && digit_char != '-'
			&& digit_char != '+' && digit_char != '.'
			&& digit_char != 'E' && digit_char != 'e') {

			/*
			 *  Invalid data - don't modify
			 */			
			beep();
			return;
		}

		String buf = getText(0, curLength).toLowerCase();
		if (digit_char == '-' || digit_char == '+') {

			if (pos == 0 && buf.length() > 0
					&& (buf.charAt(0) == '-' || buf.charAt(0) == '+')) {
				/*
				 * don't let user begin line with 2 sign characters
				 */
				beep();
				return;
			}
			
			/*
			 * other than the first character, only allow a +/- to
			 *  sign the exponent (i.e. -5.09e-3)
			 */
			boolean sign_okay = 
				(pos == 0) || (pos > 1 && buf.charAt(pos - 1) == 'e');
			if (!sign_okay) {
				beep();
				return;
			}
		}

		if (digit_char == '.' && buf.indexOf(".") > -1) {
			beep();
			return;
		}

		if ((digit_char == 'e' || digit_char == 'E') && buf.indexOf("e") > -1) {
			beep();
			return;
		}


		if (dontValidate || (minValue == null && maxValue == null)) {
			super.insertString(pos, str, attr);
			return;
		}
			

		/*
		 * Build up the string and validate it's numeric value 
		 */
		String s = getText(0, curLength);
		StringBuffer number = new StringBuffer();;
		if (pos > 0) {

	
			if (pos <= curLength) {
				number.append(s.substring(0, pos));
			}

			number.append(str);
		
			if (pos < curLength) {
				number.append(s.substring(pos));
			}

		} else {

			number.append(str);
			if (s != null) {
				number.append(s);
			}
		}


		try {

			Double val = new Double(number.toString());
			if (validateMin && minValue != null &&
					minValue.compareTo(val) > 0) {

				beep();
				return;
			}

			if (maxValue != null && maxValue.compareTo(val) < 0) {

				beep();
				return;
			}

		} catch (NumberFormatException e) {

			beep();
			return;
		}			


		super.insertString(pos, str, attr);
	}


	protected void beep () {
		toolkit.beep();
	}
}
