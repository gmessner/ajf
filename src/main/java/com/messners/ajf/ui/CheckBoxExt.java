/* *************************************************************************
 *
 *  Copyright (c) 2000-2009 Greg Messner. All Rights Reserved.
 *
 * ************************************************************************* */

package com.messners.ajf.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;


/**
 * This class simply extends JCheckBox so it can monitor for
 * property changes and set its enabled flag if a "enabled"
 * property change is recieved.  It also implements an optional
 * hiding border rollover effect.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public class CheckBoxExt extends JCheckBox
		implements java.beans.PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	protected String toolbarLabel;
	protected boolean _mouse_over_me = false;
	protected boolean _is_rollover = false;
	protected RolloverListener _listener = new RolloverListener();


	/**
	 * Creates a button with no set text or icon.
	 */
	public CheckBoxExt () {
		super();
	}


	/**
	 * Creates a button using the specified Action.
	 */
	public CheckBoxExt (Action action) {
		super(action);
	}


	/**
	 * Creates a button with an icon.
	 *
	 * @param icon the Icon image to display on the button
	 */
	public CheckBoxExt (Icon icon) {
		super(icon);
	}


	/**
	 * Creates a button with text.
	 *
	 * @param text  the text of the button
	 */
	public CheckBoxExt (String text) {
		super(text);
	}


	/**
	 * Creates a button with initial text and an icon.
	 *
	 * @param text  the text of the button.
	 * @param icon  the Icon image to display on the button
	 */
	public CheckBoxExt (String text, Icon icon) {
		super(text, icon);
	}



	public void propertyChange (java.beans.PropertyChangeEvent e) {

		String name = e.getPropertyName();

		if (name.equals("selected")) {
			
			setSelected(Boolean.TRUE.equals(e.getNewValue()));

		} else if (name.equals("enabled")) {

			setEnabled(((Boolean)e.getNewValue()).booleanValue());
		}
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


	public boolean isRollover () {
		return (_is_rollover);
	}


	public void setSelected (boolean flag) {

		if (_is_rollover) {
			setBorderPainted(flag);
		}

		super.setSelected(flag);
	}


	public void setRollover (boolean flag) {

		if (flag && !_is_rollover) {
			addMouseListener(_listener);
			addItemListener(_listener);

			if (isSelected()) {
				setBorderPainted(true);
			} else {
				setBorderPainted(false);
			}

			_is_rollover = flag;
		} else if (!flag && _is_rollover) {
			removeMouseListener(_listener);
			removeItemListener(_listener);
			setBorderPainted(true);
			_is_rollover = flag;
		}
	}


	class RolloverListener extends MouseAdapter implements ItemListener {


		public void itemStateChanged (ItemEvent evt) {

			if (evt.getStateChange() == ItemEvent.DESELECTED) {
				setBorderPainted(_mouse_over_me);
				repaint();
			} else if (evt.getStateChange() == ItemEvent.SELECTED) {
				setBorderPainted(true);
				repaint();
			}
		}

		public void mouseEntered (MouseEvent evt) {

			if (_is_rollover) {
				setBorderPainted(true);
				repaint();
			}

			_mouse_over_me = true;
		}

		public void mouseExited (MouseEvent evt) {

			if (_is_rollover) {
				if (!isSelected()) {
					setBorderPainted(false);
					repaint();
				}
			}

			_mouse_over_me = false;
		}
	}
}
