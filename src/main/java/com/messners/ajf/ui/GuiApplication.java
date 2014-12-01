package com.messners.ajf.ui;

import javax.swing.Icon;
import javax.swing.JComponent;


/**
 * This class defines an interface for a standard set of
 * GUI application services.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface GuiApplication extends com.messners.ajf.app.Application {


	/**
	 * Causes the application to exit.
	 */
	public void exit ();


	/**
	 * Registers an action with this GuiApplication.
	 *
	 * @param action the action name
	 */
	public void addAction (AppAction action);


	/**
	 * Returns a named action.
	 *
	 * @param action  the action name
	 */
	public AppAction getAction (String action);


	/**
	 * Sets the enabled state of an AppAction.
	 */
	public void setActionEnabled (AppAction action, boolean flag);


	/**
	 * Sets the enabled state of an AppAction.
	 */
	public void setActionEnabled (String action, boolean flag);


	/**
	 * Set the status display to the specified string.
	 *
	 * @param  status  the new status string
	 */
	public void setStatus (String status);


	/**
	 * Set the processing state of the application.
	 *
	 * @param  flag  true if the state is processing
	 */
	public void setProcessing (boolean flag);


	/**
	 * Gets the <code>ResourceLoader</code> associated with this instance.
	 *
	 * @return  the <code>ResourceLoader</code> associated with this instance
	 */
	public ResourceLoader getResourceLoader ();


	/**
	 * Load the resource specified by the resource name.
	 *
	 * @param name the resource name
	 * @return  a JComponent instance of the specified resource
	 */
	public JComponent load (String name);


	/**
	 * Get an icon resource, returning null if it's not defined.
	 *
	 * @param name the icon resource name
	 * @return  an Icon instance of the specified resource
	 */
	public Icon getIconResource (String name);
}
