package com.messners.ajf.ui;

import java.awt.Component;


/**
 * This interface specifies a single panel contained by a WizardManager.
 *
 * @author  Greg Messner <greg@messners.com>
 * @see WizardManager
 */
public interface WizardPanel {

	public static final int FORWARD =  1;
	public static final int BACK    = -1;


	/**
	 * Called when the WizardManager wishes to leave this WizardPanel instance,
	 * the instance should return <code>true</code> if it is OK to leave the
	 * panel or <code>false</code> otherwise.
	 *
	 * @param  direction  will either be FORWARD or BACK
	 * @return  true if OK to leave the panel, otherwise return false.
	 */
	public boolean leave (int direction);


	/**
	 * Called by the WizardManager when the panel is entered.
	 *
	 * @param  direction  will either be FORWARD or BACK
	 */
	public void enter (int direction);


	/**
	 * Return the Component for this WizardPanel.  This is called
	 * by the WizardManager to get the component to be displayed.
	 *
	 * @return  the Component associated with this WizardPanel. 
	 */
	public Component getComponent ();
}

