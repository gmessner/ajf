package com.messners.ajf.ui;

import javax.swing.JComponent;

/**
 * The interface all Application actions must extend. 
 *
 * Actions can be added at run-time with the <code>GuiApplication.addAction()
 * </code> method.
 *
 * An array of available actions can be obtained with the
 * <code>GuiApplication.getActions()</code> method.<p>
 *
 * The following properties relate to actions:
 * <ul>
 * <li><code><i>internal name</i>.label</code> - the label of the
 * action appearing in the menu bar. This is also used as the tooltip
 * for tool bar buttons if <code><i>internal name</i>.tooltip</code>
 * is not set.</li>
 *
 * <li><code><i>internal name</i>.shortcut</code> - the keyboard
 * shortcut of the action. The action must be in a menu for this
 * to work; you can't have keyboard-only actions.</li>
 *
 * <li><code><i>internal name</i>.tooltip</code> - the tooltip
 * to use for the buttons utilizing this action.  The default is the 
 * text from <code><i>internal name</i>.label</code>.</li>
 *
 * Format is described in the documentation for the
 * <code>GuiApplication</code> class.
 * </ul>
 *
 * @author Greg Messner <greg@messners.com>
 */
public interface AppAction extends javax.swing.Action {
	
	
	/**
	 * Get the internal name of this action.
	 */
	public String getName ();


	/**
	 * This method is called when a component is added to this AppAction
	 * by the instantiating GuiApplication.<p>
	 *
	 * @param  c  the component added.
	 */
	public void componentAdded (JComponent c);
}
