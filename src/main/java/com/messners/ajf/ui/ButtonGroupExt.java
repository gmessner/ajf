package com.messners.ajf.ui;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;


/**
 * This class simply extends ButtonGroup making it easier to get and set the
 * contained RadioButtonExt buttons.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ButtonGroupExt extends ButtonGroup {

	private static final long serialVersionUID = 1L;
	protected String _value;
	

    /**
     * Creates a new ButtonGroup.
     */
    public ButtonGroupExt() {}


	/**
	 * Gets the value string.
	 */
	public Object getSelectedValue () {

		RadioButtonExt erb = getSelectedButton();
		if (erb != null) {
			return (erb.getValue());
		}

		return null;
	}


	/**
	 * Gets the selected RadioButtonExt.
	 */
	public RadioButtonExt getSelectedButton () {

		Enumeration<?> e = getElements();

		while (e.hasMoreElements()) {
			RadioButtonExt erb = (RadioButtonExt)e.nextElement();
			if (erb.isSelected()) {
				return (erb);
			}
		}

		return null;
	}


	/**
	 * Sets the selected button.
	 *
	 * @param value  the value string that will be associated with the
	 *               radio button
	 */
	public void setSelectedValue (Object value) {

		if (value == null) {
			return;
		}

		RadioButtonExt rbe = getButton(value);
		if (rbe != null) {
			rbe.setSelected(true);
		} else {
			System.err.println("error: no match was found for: " + value);
		}
	}


	/**
	 * Sets the enabled state of the button associated with the specified value.
	 *
	 * @param value  the value string that is associated with the
	 *               radio button
	 * @param flag   the enabled state to set the button to
	 */
	public void setEnabledValue (Object value, boolean flag) {

		if (value == null) {
			return;
		}

		RadioButtonExt rbe = getButton(value);
		if (rbe != null) {
			rbe.setEnabled(flag);
		} else {
			System.err.println("error: no match was found for: " + value);
		}
	}


	/**
	 * Gets the RadioButtonExt that has the specified value associated
	 * with it.
	 *
	 * @param value  the value object to look for
	 */
	public RadioButtonExt getButton (Object value) {
		return (getButton(value, true));
	}

	private RadioButtonExt getButton (Object value, boolean recheck) {

		if (value == null) {
			return (null);
		}

		Enumeration<?> e = getElements();
		while (e.hasMoreElements()) {

			AbstractButton ab = (AbstractButton)e.nextElement();
			if (ab instanceof RadioButtonExt) {

				RadioButtonExt erb = (RadioButtonExt)ab;
				Object value1 = erb.getValue();
				if (value1 == null) {
					continue;
				} else if (value1.equals(value)) {

					return (erb);
				}
			}
		}

		if (recheck && value instanceof String) {

			char c = ((String)value).charAt(0);
			if (Character.isUpperCase(c)) {

				StringBuffer buf = new StringBuffer();
				buf.append(Character.toLowerCase(c));
				buf.append(((String)value).substring(1));
				return (getButton(buf.toString(), false));

			} else if (Character.isLowerCase(c)) {

				StringBuffer buf = new StringBuffer();
				buf.append(Character.toUpperCase(c));
				buf.append(((String)value).substring(1));
				return (getButton(buf.toString(), false));
			}
		}

		return (null);
	}
}
