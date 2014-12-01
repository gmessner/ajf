package com.messners.ajf.ui;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;


/**
 * This class listens for changes in the forms and calls the change() method
 * with the event.
 */
public abstract class ChangeListener
	implements javax.swing.event.DocumentListener,
	java.awt.event.ActionListener,
	java.awt.event.ItemListener,
	javax.swing.event.ChangeListener {

	private boolean enabled = true;


	/**
	 * This method is called when a change occurs that this listener
	 * is listening for.
	 *
	 * @param  source  the source of the event
	 * @param  event   the event object for the event
	 */
	public abstract void change (Object source, Object event);


	/**
	 * Gets the listener enabled flag. If false the change() method will not be
	 * called when a change occurs.
	 *
	 * @return  the listener enabled flag
	 */
	public boolean isEnabled () {
		return (enabled);
	}


	/**
	 * Sets the listener enabled flag. If false the change() method will not be
	 * called when a change occurs.
	 *
	 * @param  flag  the new listener enabled flag
	 */
	public void setEnabled (boolean flag) {
		enabled = flag;
	}


	public void actionPerformed (java.awt.event.ActionEvent e) {

		if (isEnabled()) {
			change(e.getSource(), e);
		}
	}


	public void changedUpdate (javax.swing.event.DocumentEvent e) {

		if (isEnabled()) {
			change(e.getDocument(), e);
		}
	}


	public void insertUpdate (javax.swing.event.DocumentEvent e) {

		if (isEnabled()) {
			change(e.getDocument(), e);
		}
	}


	public void removeUpdate(javax.swing.event.DocumentEvent e) {

		if (isEnabled()) {
			change(e.getDocument(), e);
		}
	}


	public void stateChanged(javax.swing.event.ChangeEvent e) {

		if (isEnabled()) {
			change(e.getSource(), e);
		}
	}


	public void itemStateChanged(java.awt.event.ItemEvent e) {

		if (isEnabled()) {
			change(e.getSource(), e);
		}
	}



	/**
	 * Adds a JComboBox as a component to listener for changes on.
	 *
	 * @param  cb  the component to listen for changes on
	 */
	public void addChangeListener (JComboBox<?> cb) {
		cb.addActionListener(this);
	}


	/**
	 * Adds a JToggleButton as a component to listener for changes on.
	 *
	 * @param  cb  the component to listen for changes on
	 */
	public void addChangeListener (JToggleButton cb) {
		cb.addChangeListener(this);
	}


	/**
	 * Adds a JCheckBox as a component to listener for changes on.
	 *
	 * @param  cb  the component to listen for changes on
	 */
	public void addChangeListener (JCheckBox cb) {
		cb.addItemListener(this);
	}


	/**
	 * Adds a JSlider as a component to listener for changes on.
	 *
	 * @param  slider  the component to listen for changes on
	 */
	public void addChangeListener (JSlider slider) {
		slider.addChangeListener(this);
	}


	/**
	 * Adds a ButtonGroup as a component to listener for changes on.
	 *
	 * @param  bg  the component to listen for changes on
	 */
	public void addChangeListener (ButtonGroup bg) {

		Enumeration<?> buttons = bg.getElements();
		while (buttons.hasMoreElements()) {

			AbstractButton btn = (AbstractButton)buttons.nextElement();
			btn.addItemListener(this);
		}
	}


	/**
	 * Adds a ButtonGroup from a PanelExt as a component to
	 * listener for changes on.
	 *
	 * @param  p  the component to listen for changes on
	 */
	public void addChangeListener (PanelExt p) {

		ButtonGroup bg = p.getButtonGroup();
		if (bg != null) {
			addChangeListener(bg);
		}
	}


	/**
	 * Adds a JTextField as a component to listener for changes on.
	 *
	 * @param  tf  the component to listener for changes on
	 */
	public void addChangeListener (JTextField tf) {

		javax.swing.text.Document doc = tf.getDocument();
		doc.addDocumentListener(this);
	}


	/**
	 * Adds a JSpinner as a component to listener for changes on.
	 *
	 * @param  s  the component to listener for changes on
	 */
	public void addChangeListener (JSpinner s) {

		JComponent c = s.getEditor();
		if (c instanceof JSpinner.DefaultEditor) {
			c = ((JSpinner.DefaultEditor)c).getTextField();
		}

		if (c instanceof JTextField) {
			addChangeListener((JTextField)c);
		} else {
			s.addChangeListener(this);
		}
	}


	/**
	 * Adds a JRadioButton as a component to listener for changes on.
	 *
	 * @param  rb  the component to listener for changes on
	 */
	public void addChangeListener (JRadioButton rb) {
		rb.addChangeListener(this);
	}


	/**
	 * Adds a component as a listener for changes.
	 *
	 * @param obj the component to listen for changes on
	 */
	public void addChangeListener (Object obj) {

		if (obj instanceof JTextField) {
			addChangeListener((JTextField)obj);
		} else if (obj instanceof JToggleButton) {
			addChangeListener((JToggleButton)obj);
		} else if (obj instanceof JCheckBox) {
			addChangeListener((JCheckBox)obj);
		} else if (obj instanceof JComboBox) {
			addChangeListener((JComboBox<?>)obj);
		} else if (obj instanceof ButtonGroup) {
			addChangeListener((ButtonGroup)obj);
		} else if (obj instanceof PanelExt) {
			addChangeListener((PanelExt)obj);
		} else if (obj instanceof JSpinner) {
			addChangeListener((JSpinner)obj);
		} else if (obj instanceof JRadioButton) {
			addChangeListener((JRadioButton)obj);
		} else if (obj instanceof JSlider) {
			addChangeListener((JSlider)obj);
		} else if (obj != null) {
		   throw new RuntimeException(obj.getClass() + 
			 " is not a supported component.");
		}
	}
}
