package com.messners.ajf.ui;

import javax.swing.JPanel;


/**
 * Default user input panel.
 */
public class DefaultInputPanel extends JPanel implements InputPanel, UIConstants {

	private static final long serialVersionUID = 1L;


	// From InputPanel.
	public void clear () { }
   

	// From InputPanel.
	public void populate (Object input) { }
   

	// From InputPanel.
	public Object getInput (Object data) {

		return data;
	}


	// From InputPanel.
	public boolean validateInput () {

		return true;
	}


	/**
	 * Quickly check user input.  This check should be quick and non-
	 * verbose; that is, perform any lengthy database queries or raise 
	 * any error dialogs.
    * 
	 * @return <code>true</code> if input is okay.
	 */
	public boolean checkInput () {

		return true;
	}


   /**
    * Checks if the user input has changed.
    *
    * @return <code>true</code> if user input has changed.
    */
   public boolean inputChanged () {

      return false;
   }


	/**
	 * Sets the change listener for the panel.
	 * @param lstnr Change listener set.
	 */
	public void setChangeListener (ChangeListener lstnr) { }
}
