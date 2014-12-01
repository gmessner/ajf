package com.messners.ajf.ui;

import java.awt.Component;
import javax.swing.Icon;


/**
 * This interface is required for all panels in the PrefsForm object.  
 * Each PrefsPanel is uniquely identified by its getName method.
 * The PrefsPanel name property is used internally by PrefsForm to keep track
 * of the panel.  This string is not displayed to the user.  The string that 
 * appears to the user (in the dialog) is aquired by calling the getTitle()
 * method. 
 *
 * @see PrefsForm
 */
public interface PrefsPanel {

	/**
	 * Gets the name of this panel.  This is used internally by the
	 * PrefsForm container.
	 *
	 * @return  the name of this panel
	 */
	public String getName ();


	/**
	 * Gets the title of this panel.  This is used as the string to be 
	 * displayed in the PrefsForm tree and as the title on above the panel.
	 *
	 * @return  the title of this panel
	 */
	public String getTitle ();


	/**
	 * Gets the <code>Icon</code> for this panel.  If non-null it will
	 * be displayed along with the string returned by getTitle() in the
	 * title bar above the panel.
	 *
	 * @return  the <code>Icon</code> for this panel
	 */
	public Icon getIcon ();


	/**
	 * Called when the PrefsForm wishes to leave this PrefsPanel instance,
	 * the instance should return <code>true</code> if it is OK to leave 
	 * the panel or <code>false</code> otherwise.
	 *
	 * @return  true if OK to leave the panel, otherwise return false.
	 */
	public boolean leave();


	/**
	 * Called by the PrefsForm when the panel is entered.
	 */
	public void enter();


	/**
	 * Return the Component for this PrefsPanel.  This is called
	 * by the PrefsForm to get the component to be displayed.
	 * @return  the Component associated with this PrefsPanel. 
	 */
	public Component getComponent();
}
