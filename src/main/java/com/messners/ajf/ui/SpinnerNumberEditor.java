package com.messners.ajf.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;


/**
 * This class provides an editor for Integer and Double values in a JSpinner.
 *
 * @author  Greg Messner <greg@messners.com>
 */

public class SpinnerNumberEditor extends JPanel 
		implements ChangeListener, DocumentListener, 
		FocusListener, LayoutManager {

	private static final long serialVersionUID = 1L;
	private static final Double DOUBLE_ZERO = new Double(0);

	private JTextField textField;
	private NumberFormat numberFormat;
	private boolean updatingSpinner;
	private Number defaultValue;
	private int radix;


	/**
	 * Constructs an editor component for the specified <code>JSpinner</code>.
	 * This <code>SpinnerNumberEditor</code> is it's own layout manager and 
	 * it is added to the spinner's <code>ChangeListener</code> list.
	 * The constructor creates a single <code>JTextField<code> child,
	 * initializes it's value to be the spinner model's current value
	 * and adds it to <code>this</code> <code>SpinnerNumberEditor</code>.  
	 * 
	 * @param spinner the spinner whose model <code>this</code> editor
	 * will monitor
	 */
	public SpinnerNumberEditor (JSpinner spinner) {

	    super(null);
		init(spinner, null, 10);
	}


	public SpinnerNumberEditor (JSpinner spinner, int radix) {

	    super(null);
		init(spinner, null, radix);
	}


	/**
	 * Constructs an editor component for the specified <code>JSpinner</code>.
	 * This <code>SpinnerNumberEditor</code> is it's own layout manager and 
	 * it is added to the spinner's <code>ChangeListener</code> list.
	 * The constructor creates a single <code>JTextField<code> child,
	 * initializes it's value to be the spinner model's current value
	 * and adds it to <code>this</code> <code>SpinnerNumberEditor</code>.  
	 * 
	 * @param spinner the spinner whose model <code>this</code> editor
	 * will monitor
	 * @param format  the NumberFormat for the text in the JTextField
	 */
	public SpinnerNumberEditor (JSpinner spinner, NumberFormat format) {

	    super(null);
		init(spinner, format, 10);
	}


	/**
	 * Constructs an editor component for the specified <code>JSpinner</code>.
	 * This <code>SpinnerNumberEditor</code> is it's own layout manager and 
	 * it is added to the spinner's <code>ChangeListener</code> list.
	 * The constructor creates a single <code>JTextField<code> child,
	 * initializes it's value to be the spinner model's current value
	 * and adds it to <code>this</code> <code>SpinnerNumberEditor</code>.  
	 * 
	 * @param spinner the spinner whose model <code>this</code> editor
	 * will monitor
	 * @param pattern the pattern for the NumberFormat for the text in the
	 * JTextField
	 */
	public SpinnerNumberEditor (JSpinner spinner, String pattern) {

	    super(null);
		NumberFormat format = new DecimalFormat(pattern);
		init(spinner, format, 10);
	}


	/**
	 * Initialize the instance and create and configure the JTextField
	 * utilized as the editor.
	 */
	private void init (JSpinner spinner, NumberFormat format, int radix) {

	    if (!(spinner.getModel() instanceof SpinnerNumberModel)) {
			throw new IllegalArgumentException(
				"model not a SpinnerNumberModel");
	    }

	    setLayout(this);
		numberFormat = format;
		this.radix = radix;
	    textField = new JTextField();

		Document doc;
		SpinnerNumberModel model = (SpinnerNumberModel)spinner.getModel();
		Object value = model.getValue();

		if (value instanceof Integer) {

			doc = new IntegerDocument(model, radix);
			defaultValue = (Number)value;

		} else if (value instanceof Double) {

			doc = new DoubleDocument(model);
			defaultValue = (Number)value;

		} else {

			doc = new DoubleDocument();

			if (value instanceof Number) {

				defaultValue = (Number)value;
				
			} else {

				defaultValue = DOUBLE_ZERO;
				value = defaultValue;
			}
		}

		textField.setDocument(doc);
		doc.addDocumentListener(this);
	    textField.setEditable(true);
        textField.setHorizontalAlignment(JTextField.RIGHT);
	   	textField.setText(format(value));
		textField.addFocusListener(this);

	    add(textField);
	    spinner.addChangeListener(this);
	}


	/**
	 * Returns the <code>JSpinner</code> ancestor of this editor or null.  
	 * Typically the editor's parent is a <code>JSpinner</code> however 
	 * subclasses of <codeJSpinner</code> may override the
	 * the <code>createEditor</code> method and insert one or more containers
	 * between the <code>JSpinner</code> and it's editor.
	 * 
     * @return <code>JSpinner</code> ancestor
	 */
	public JSpinner getSpinner() {

	    for (Component c = this; c != null; c = c.getParent()) {
			if (c instanceof JSpinner) {
			    return ((JSpinner)c);
			}
	    }

	    return (null);
	}
	

	/**
	 * Returns the <code>JTextField</code> child of this 
	 * editor.  By default the text field is the first and only 
	 * child of editor.
	 * 
	 * @return the <code>JTextField</code> that gives the user
	 *     access to the <code>SpinnerDateModel's</code> value.
	 * @see #getSpinner
	 */
	public JTextField getTextField () {
	    return (textField);
	}


	/**
	 * Sets the number of columns displayed by the JTextField.
	 *
	 * @param  numColumns the number of columns to display
	 */
	public void setColumns (int numColumns) {
		textField.setColumns(numColumns);
	}


	public void setMaximum (Number max) {

		Document doc = textField.getDocument();
		if (doc instanceof IntegerDocument) {

			((IntegerDocument)doc).setMaximumValue(max.intValue());
			
		} else if (doc instanceof DoubleDocument) {

			((DoubleDocument)doc).setMaximumValue(max.doubleValue());
		}
	}


	public void setMinimum (Number min) {

		Document doc = textField.getDocument();
		if (doc instanceof IntegerDocument) {

			((IntegerDocument)doc).setMinimumValue(min.intValue());

		} else if (doc instanceof DoubleDocument) {

			((DoubleDocument)doc).setMinimumValue(min.doubleValue());
		}
	}


	/**
	 * This method is called when the spinner's model's state changes.
	 * It sets the <code>value</code> of the text field to the current
	 * value of the spinners model.
	 * 
	 * @param e not used
	 * @see #getTextField
	 * @see JSpinner#getValue
	 */
	public void stateChanged (ChangeEvent e) {

		/*
		 * Update the text field if we are not updating the JSpinner.
		 */
		if (!updatingSpinner) {
		    JSpinner spinner = (JSpinner)(e.getSource());
		    textField.setText(format(spinner.getValue()));
		}
	}


	/**
	 * Notification that an attribute has changed in the JTextField document.
	 */
	public void changedUpdate (DocumentEvent e) {
		updateSpinner();
	}


	/**
	 * Notification that text has been inserted into the JTextField document.
	 */
	public void insertUpdate (DocumentEvent e) {
		updateSpinner();
	}


	/**
	 * Notification that text has been removed from the JTextField document.
	 */
	public void removeUpdate (DocumentEvent e) {
		updateSpinner();
	}


	/**
	 * Update the spinner with the currrent text in the JTextField.
	 */
	private void updateSpinner () {

		JSpinner spinner = getSpinner();

      	/*
		 * Indicates we aren't installed anywhere.
		 */
		if (spinner == null) {
   	    	return;
		}

		updatingSpinner = true;
		String text = textField.getText();


		/*
		 * Try to set the new value
		 */
        try {

			if (text == null || text.length() < 1) {

				spinner.setValue(defaultValue);

			} else if (defaultValue instanceof Integer) {

				Integer num = new Integer(text);
				spinner.setValue(num);

			} else if (defaultValue instanceof Double) {

				Double num = new Double(text);
				spinner.setValue(num);
			}

		} catch (IllegalArgumentException iae) {

       		/*
			 * SpinnerModel didn't like new value
			 */
       	}

		updatingSpinner = false;
	}


	/**
	 * This method monitors for focus gained on the JTextField and does nothing.
	 */
	public void focusGained (FocusEvent e) {
	}


	/**
	 * This method monitors for focus lost on the JTextField and formats
	 * the JSpinner value with the current NumberFormatter if any.
	 */
	public void focusLost (FocusEvent e) {

		if (numberFormat == null) {
			return;
		}

		Object value = getSpinner().getValue();
		if (value == null) {
			value = "0";
		}

		textField.setText(format(value));
	}


	/**
	 * Formats the specified text using the NumberFormat if any.
	 *
	 * @param  text  the text to format
	 * @return the formatted string
	 */
	private String format (Object value) {

		if (value == null) {
			return (null);
		}

		if (numberFormat == null) {

			if (value instanceof String) {

				return ((String)value);

			} else {

				if (radix == 8 && value instanceof Number) {
					return (Integer.toOctalString(((Number)value).intValue()));
				} else {
					return (value.toString());
				}
			}
		}


		try {

			if (value instanceof Number) {
				return (numberFormat.format(value));
			} else if (value instanceof String) {
				return (numberFormat.format(new Double((String)value)));
			} else {
				return (numberFormat.format(new Double(value.toString())));
			}

		} catch (Exception e) {

			return (value.toString());
		}
	}


	/**
	 * This <code>LayoutManager</code> method does nothing.  We're 
	 * only managing a single child and there's no support 
	 * for layout constraints.
	 * 
	 * @param name ignored
	 * @param child ignored
	 */
	public void addLayoutComponent(String name, Component child) {
	}


	/**
	 * This <code>LayoutManager</code> method does nothing.  There
	 * isn't any per-child state.
	 * 
	 * @param child ignored
	 */
	public void removeLayoutComponent(Component child) {
	}


	/**
	 * Returns the size of the parents insets.
	 */
	private Dimension insetSize(Container parent) {

	    Insets insets = parent.getInsets();
	    int w = insets.left + insets.right;
	    int h = insets.top + insets.bottom;
	    return (new Dimension(w, h));
	}


	/**
	 * Returns the preferred size of first (and only) child plus the
	 * size of the parents insets.
	 * 
	 * @param parent the Container that's managing the layout
     * @return the preferred dimensions to lay out the subcomponents
     *          of the specified container.
	 */
	public Dimension preferredLayoutSize(Container parent) {

	    Dimension preferredSize = insetSize(parent);
	    if (parent.getComponentCount() > 0) {

			Dimension childSize = getComponent(0).getPreferredSize();
			preferredSize.width += childSize.width;
			preferredSize.height += childSize.height;
	    }

	    return (preferredSize);
	}


	/**
	 * Returns the minimum size of first (and only) child plus the
	 * size of the parents insets.
	 * 
	 * @param parent the Container that's managing the layout
	 * @return  the minimum dimensions needed to lay out the subcomponents
	 *          of the specified container.
	 */
	public Dimension minimumLayoutSize(Container parent) {

	    Dimension minimumSize = insetSize(parent);
	    if (parent.getComponentCount() > 0) {
			Dimension childSize = getComponent(0).getMinimumSize();
			minimumSize.width += childSize.width;
			minimumSize.height += childSize.height;
	    }

	    return (minimumSize);
	}


	/**
	 * Resize the one (and only) child to completely fill the area
	 * within the parents insets.
	 */
	public void layoutContainer(Container parent) {

	    if (parent.getComponentCount() > 0) {
			Insets insets = parent.getInsets();
			int w = parent.getWidth() - (insets.left + insets.right);
			int h = parent.getHeight() - (insets.top + insets.bottom);
			getComponent(0).setBounds(insets.left, insets.top, w, h);
	    }
	}
}
