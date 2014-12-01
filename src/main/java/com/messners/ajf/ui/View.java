package com.messners.ajf.ui;

import com.messners.ajf.app.Preferences;

import javax.swing.Icon;
import javax.swing.JComponent;


/**
 * This interface defines a view component to be used with the ViewBrowser
 * component. This interface is defined in such a way that it can also be used
 * outside of the ViewBrowser.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface View {

	/**
	 * Gets the title to display for this View instance.
	 *
	 * @return  the title to display for this instance
	 */
	public String getTitle ();


	/**
	 * Gets the Icon to display with the title for this View instance.
	 *
	 * @return  the title Icon to display for this instance
	 */
	public Icon getTitleIcon ();


	/**
	 * Gets the name to display int the tree for this View instance.
	 *
	 * @return  the name to display for this instance
	 */
	public String getName ();


	/**
	 * Gets the Icon to display int the tree for this View instance.
	 *
	 * @return  the Icon to display for this instance
	 */
	public Icon getIcon ();


	/**
	 * Gets the JComponent to render for this View instance.
	 *
	 * @return the JComponent to render for this View instance.
	 */
	public JComponent getComponent ();


	/**
	 * Restore this View instance preferences.
	 *
	 * @param prefs the Preferences instance to restore from
	 */
	public void restorePreferences (Preferences prefs, String prefix);


	/**
	 * Saves this View instance preferences.
	 *
	 * @param prefs the Preferences instance to save to
	 */
	public void savePreferences (Preferences prefs, String prefix);


	/**
	 * Disposes of resources when the instance is removed from a container.
	 */
	public void dispose ();
}
