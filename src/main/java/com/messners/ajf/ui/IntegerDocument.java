package com.messners.ajf.ui;

import java.awt.Toolkit;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;


/**
 * This class defines a Document for accepting integer values only.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class IntegerDocument extends PlainDocument {

	private static final long serialVersionUID = 1L;
	protected int radix;
	protected int maxLength = -1;
	protected boolean signed = false;
	protected boolean beepOnError = true;
	protected Integer minValue = null;
	protected Integer maxValue = null;

	protected boolean dontValidate = false;
	protected boolean validateMin = false;

	protected static Toolkit toolkit = Toolkit.getDefaultToolkit();
	protected static final Integer INTEGER_ZERO = new Integer(0);
	


	/**
	 * Create an <code>IntegerDocument</code> instance that will accept
	 * any unsigned integer values.
	 */
	public IntegerDocument () {

		radix = 10;
	}


	/**
	 * Create an <code>IntegerDocument</code> instance  that will accept
	 * any unsigned integer value with the specified maximum length.
	 *
	 * @param maxLength  maximum length of the integer
	 */
	public IntegerDocument (int maxLength) {

		this.maxLength = maxLength;
		radix = 10;
	}


	public IntegerDocument (int maxLength, int radix) {

		this.maxLength = maxLength;
		this.radix = radix;
	}


	/**
	 * Create an <code>IntegerDocument</code> instance that will accept
	 * the min and max valuies specified by the SpinnerNumberModel.
	 */
	public IntegerDocument (SpinnerNumberModel snm) {

		this(snm, 10);
	}


	public IntegerDocument (SpinnerNumberModel snm, int radix) {

		Object min = snm.getMinimum();
		if (min instanceof Integer) {

			minValue = (Integer)min;
			signed = (minValue.compareTo(INTEGER_ZERO) == -1);
			validateMin = (minValue.compareTo(INTEGER_ZERO) != 1);
		}
			
		Object max = snm.getMaximum();
		if (max instanceof Integer) {
			maxValue = (Integer)max;
		}

		this.radix = radix;
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


	/**
	 * Get the maximum allowable value for this instance.
	 *
	 * @return the maximum allowable value for this instance or null if
	 * no maximum has been set
	 */
	public Integer getMaximumValue () {
		return (maxValue);
	}


	/**
	 * Set the maximum allowable value for this instance.
	 *
	 * @param max  the maximum allowable value for this instance
	 */
	public void setMaximumValue (int max) {
		maxValue = new Integer(max);
	}


	/**
	 * Set the maximum allowable value for this instance.
	 *
	 * @param max  the maximum allowable value for this instance or null
	 * for no maximum
	 */
	public void setMaximumValue (Integer max) {
		maxValue = max;
	}


	/**
	 * Get the minimum allowable value for this instance.
	 *
	 * @return the minimum allowable value for this instance or null if
	 * no minimum has been set
	 */
	public Integer getMinimumValue () {
		return (minValue);
	}


	/**
	 * Set the minimum allowable value for this instance.
	 *
	 * @param min  the minimum allowable value for this instance
	 */
	public void setMinimumValue (int min) {

		minValue = new Integer(min);
		validateMin = (minValue.compareTo(INTEGER_ZERO) != 1);
	}


	/**
	 * Set the minimum allowable value for this instance.
	 *
	 * @param min  the minimum allowable value for this instance or null
	 * for no minimum
	 */
	public void setMinimumValue (Integer min) {
		minValue = min;
		validateMin = (minValue.compareTo(INTEGER_ZERO) != 1);
	}


	/**
	 * Get the allows signed values flag.
	 *
	 * @return the allows signed values flag
	 */
	public boolean getSigned () {
		return (signed);
	}


	public void setMaxLength (int maxLength) {
		this.maxLength = maxLength;
	}


	/**
	 * Gets the radix for String operations.
	 *
	 * @return the radix for String operations
	 */
	public int getRadix () {
		return (radix);
	}


	/**
	 * Set the radix for String operations.
	 *
	 * @param  radix  the radix for String operations
	 */
	public void setRadix (int radix) {
		this.radix = radix;
	}


	/**
	 * Set the allows signed values flag.
	 *
	 * @param  signed the new allows signed values flag
	 */
	public void setSigned (boolean signed) {
		this.signed = signed;
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

		String buf = getText(0, curLength).toLowerCase();
		char first_char = 0;
		if (buf.length() > 0) {
			first_char = buf.charAt(0);
		}

		char c = str.charAt(0);
		if (radix == 10) {

			if (c == '+' || c == '-') {

				/*
				 * only allow a single +/-, and it must be the first character
				 */
				if (!signed || pos != 0 || first_char == '+'
						|| first_char == '-') {
					beep();
					return;
				} 

			} else if (c == 'E' || c == 'e') {

				/*
				 * allow a single E (exponential symbol), but not as the first
				 *  character, and not after the +/- (if present)
				 */
				if (pos == 0 || buf.indexOf("e") > -1) {
					beep();
					return;
				}
			
				if (pos == 1 && (first_char == '+' || first_char == '-')) {
					beep();
					return;
				}

			} else if (!Character.isDigit(c)) {

				beep();
				return;
			}

		} else {

			int value = Character.digit(c, radix);
			if (value == -1 || value >= radix) {

				beep();
				return;
			}
		}


		if (dontValidate || (minValue == null && maxValue == null) ||
				(signed && curLength == 0 && (c == '-' || c == '+'))) {
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

			Integer val = Integer.valueOf(number.toString(), radix);

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


	/**
	 * Get the beep on input error flag.
	 *
	 * @return  true if beep on error is enabled
	 */
	public boolean getBeepOnError () {
		return (beepOnError);
	}


	/**
	 * Set the beep on input error flag.
	 *
	 * @param  flag  the new beep on error flag
	 */
	public void setBeepOnError (boolean flag) {
		beepOnError = flag;
	}


	/**
	 * Do a beep.
	 */
	protected void beep () {

		if (beepOnError) {
			toolkit.beep();
		}
	}
}
