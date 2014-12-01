package com.messners.ajf.ui;

import java.awt.LayoutManager;
import javax.swing.JPanel;


/**
 * This class simply extends JPanel so it can keep track of
 * an ButtonGroupExt instance.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class PanelExt extends JPanel {

	private static final long serialVersionUID = 1L;
	private Object userObject;
	private ButtonGroupExt buttonGroup;


    /**
     * Default no args constructor.
     */
    public PanelExt () {
        super();
    }

     
    /**
	 * Creates a new instance with the specified double buffering policy.
	 *
	 * @param isDoubleBuffered  the double buffer policy
     */
    public PanelExt (boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }


    /**
	 * Creates a new instance with the specified LayoutManager.
	 *
	 * @param layout            the LayoutManager
     */
    public PanelExt (LayoutManager layout) {
        super(layout);
    }


    /**
	 * Creates a new instance with the specified LayoutManager and 
	 * double buffering policy.
	 *
	 * @param layout            the LayoutManager
	 * @param isDoubleBuffered  the double buffer policy
     */
    public PanelExt (LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }


	/**
	 * Gets the ButtonGroupsExt for this PanelExt instance.
	 *
	 * @return  the ButtonGroupExt instance associated with this instance
	 */
	public ButtonGroupExt getButtonGroup () {

		return (buttonGroup);
	}


	/**
	 * Sets the ButtonGroupsExt for this PanelExt instance.
	 *
	 * @param buttonGroup the ButtonGroupExt instance to associat
	 * with this instance
	 */
	public void setButtonGroup (ButtonGroupExt buttonGroup) {

		this.buttonGroup = buttonGroup;
	}


	/**
	 * Gets the value of the associated ButtonGroupExt.
	 */
	public Object getSelectedValue () {

		return (buttonGroup != null ? buttonGroup.getSelectedValue() : null);
	}


	/**
	 * Sets the value of the associated ButtonGroupExt.
	 */
	public void setSelectedValue (Object value) {

		if (buttonGroup != null) {
			buttonGroup.setSelectedValue(value);
		}
	}


	/**
	 * Gets the selected RadioButtonExt.
	 */
	public RadioButtonExt getSelectedButton () {

		if (buttonGroup != null) {
			return (buttonGroup.getSelectedButton());
		}

		return (null);
	}


	/**
	 * Sets the enabled state of the button associated with the specified value.
	 *
	 * @param value  the value string that is associated with the
	 *               radio button
	 * @param flag   the enabled state to set the button to
	 */
	public void setEnabledValue (Object value, boolean flag) {

		if (buttonGroup != null) {
			buttonGroup.setEnabledValue(value, flag);
		}
	}


	/**
	 * Gets the RadioButtonExt that has the specified value associated
	 * with it.
	 *
	 * @param value  the value object to look for
	 */
	public RadioButtonExt getButton (Object value) {

		if (buttonGroup != null) {
			return (buttonGroup.getButton(value));
		} else {
			return (null);
		}
	}


	/**
	 * Gets the user object for this instance.
	 */
	public Object getUserObject () {
		return (userObject);
	}


	/**
	 * Sets the user object for this instance.
	 */
	public void setUserObject (Object obj) {
		userObject = obj;
	}
}
