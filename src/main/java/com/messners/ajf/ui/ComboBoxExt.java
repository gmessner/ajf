package com.messners.ajf.ui;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.ComboBoxModel;

import java.util.Vector;


/**
 * This class simply extends JComboBox so it can monitor for
 * property changes and set its enabled flag if a "enabled"
 * property change is recieved.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ComboBoxExt extends JComboBox<Object>
        implements java.beans.PropertyChangeListener {

	private static final long serialVersionUID = 1L;


	/**
	 */
	public ComboBoxExt () {
		super();
	}

	/**
	 *
	 * @param  model  the ComboBoxModel to use
	 */
	public ComboBoxExt (ComboBoxModel<Object> model) {
		super(model);
	}
	

	/**
	 * @param  items  the items to display in the ComboBox
	 */
	public ComboBoxExt (Object items[]) {
		super(items);
	}
	

	/**
	 *
	 * @param  items  the items to display in the ComboBox
	 */
	public ComboBoxExt (Vector<Object> items) {
		super(items);
	}
	

	public void notifyActionListeners () {

		Object[] listeners = listenerList.getListenerList();
		ActionEvent evt = new ActionEvent(this, 0, "identify");
		for (int i = listeners.length - 2; i >= 0; i -= 2) {

			if (listeners[i + 1] instanceof AppAction) {
				AppAction action = (AppAction)listeners[i + 1];
				action.actionPerformed(evt);
			}              
		}
	}
	

	public void propertyChange (java.beans.PropertyChangeEvent e) {

		if (e.getPropertyName().equals("enabled")) {
			setEnabled(((Boolean)e.getNewValue()).booleanValue());
		}
	}
	

	/**
	 * Get the text in the text component
	 */
	public String getText () {

		Object text = getSelectedItem();
		if (text == null) {
			return (null);
		}

		return (text.toString());
	}


	/**
	 * Set the text in the text component
	 */
	public void setText (String text) {

		setSelectedItem(text);
	}
}
