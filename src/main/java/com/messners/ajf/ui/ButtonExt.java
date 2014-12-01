package com.messners.ajf.ui;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * This class simply extends JButton so it can monitor for
 * property changes and set its enabled flag if a "enabled"
 * property change is recieved. It also implements an optional
 * hiding border rollover effect.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ButtonExt extends JButton
		implements java.beans.PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean _is_rollover = false;
	protected RolloverListener _listener = new RolloverListener();
	protected String toolbarLabel;


	/**
	 * Creates a button with no set text or icon.
	 */
	public ButtonExt () {
		super();
	}


	/**
	 * Creates a button using the specified Action.
	 */
	public ButtonExt (Action action) {
		super(action);
	}


	/**
	 * Creates a button with an icon.
	 *
	 * @param icon the Icon image to display on the button
	 */
	public ButtonExt (Icon icon) {
		super(icon);
	}


	/**
	 * Creates a button with text.
	 *
	 * @param text  the text of the button
	 */
	public ButtonExt (String text) {
		super(text);
	}


	/**
	 * Creates a button with initial text and an icon.
	 *
	 * @param text  the text of the button.
		 * @param icon  the Icon image to display on the button
	 */
	public ButtonExt (String text, Icon icon) {
		super(text, icon);
	}


	public void propertyChange (java.beans.PropertyChangeEvent e) {

		if (e.getPropertyName().equals("enabled")) {
			setEnabled(((Boolean)e.getNewValue()).booleanValue());
		}
	}


	public boolean isRollover () {
		return (_is_rollover);
	}


	/**
	 * Sets whether to show the label when on a toolbar or not.
	 *
	 * @param flag  if true will show the label, false hides it
	 */
	public void setShowToolBarLabel (boolean flag) {

		if (!flag) {

			String s = getText();
			if (s != null) {
				toolbarLabel = getText();
			}

			setText(null);

		} else {

			String s = getText();
			if (s == null) {
				setText(toolbarLabel);
			}
		}
	}


	public void setRollover (boolean flag) {

		if (flag && !_is_rollover) {
			addMouseListener(_listener);
			setBorderPainted(false);
			_is_rollover = flag;
		} else if (!flag && _is_rollover) {
			removeMouseListener(_listener);
			setBorderPainted(true);
			_is_rollover = flag;
		}
	}


	class RolloverListener extends MouseAdapter {

		public void mouseEntered (MouseEvent evt) {

			if (_is_rollover) {
				setBorderPainted(true);
				repaint();
			}
		}

		public void mouseExited (MouseEvent evt) {

			if (_is_rollover) {
				setBorderPainted(false);
				repaint();
			}
		}
	}
}
