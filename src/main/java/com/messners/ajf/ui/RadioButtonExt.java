package com.messners.ajf.ui;

import javax.swing.JRadioButton;
import javax.swing.Icon;


/**
 * This class simply extends JRadioButton so it can monitor for
 * property changes and set its enabled flag if a "enabled"
 * property change is recieved.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class RadioButtonExt extends JRadioButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Object _value = null;
	

    /**
     * Creates an initially unselected radio button
     * with no set text.
     */
    public RadioButtonExt () {
        super();
    }
     
    /**
     * Creates an initially unselected radio button
     * with the specified image but no text.
     *
     * @param icon  the image that the button should display
     */
    public RadioButtonExt(Icon icon) {
        super(icon);
    }

    /**
     * Creates a radio button with the specified image
     * and selection state, but no text.
     *   
     * @param icon  the image that the button should display
     * @param selected  if true, the button is initially selected;
     *                  otherwise, the button is initially unselected
     */
    public RadioButtonExt(Icon icon, boolean selected) {
        super(icon, selected);
    }
    
    /**
     * Creates an unselected radio button with the specified text.
     * 
     * @param text  the string displayed on the radio button
     */
    public RadioButtonExt (String text) {
        super(text);
    }

    /**
     * Creates a radio button with the specified text
     * and selection state.
     * 
     * @param text  the string displayed on the radio button
     * @param selected  if true, the button is initially selected;
     *                  otherwise, the button is initially unselected
     */
    public RadioButtonExt (String text, boolean selected) {
        super(text, selected);
    }

    /**
     * Creates a radio button that has the specified text and image,
     * and that is initially unselected.
     *
     * @param text  the string displayed on the radio button 
     * @param icon  the image that the button should display
     */
    public RadioButtonExt(String text, Icon icon) {
        super(text, icon);
    }

    /**
     * Creates a radio button that has the specified text, image,
     * and selection state.
     *
     * @param text  the string displayed on the radio button 
     * @param icon  the image that the button should display
     */
    public RadioButtonExt (String text, Icon icon, boolean selected) {
        super(text, icon, selected);
    }


	/**
	 * Gets the value.
	 */
	public Object getValue () {
		return _value;
	}


	/**
	 * Sets the value string.
	 *
	 * @param value  the value object that will be associated with the
	 *               radio button
	 */
	public void setValue (Object value) {
		_value = value;
	}
}
