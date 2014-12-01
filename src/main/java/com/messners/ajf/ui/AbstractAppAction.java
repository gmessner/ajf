/* *************************************************************************
 *
 *  Copyright (c) 2000-2009 Greg Messner. All Rights Reserved.
 *
 * ************************************************************************* */

package com.messners.ajf.ui;

import javax.swing.AbstractButton;
import javax.swing.JPopupMenu;
import javax.swing.JComponent;
import java.awt.Component;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * The class provides an abstract class that Application actions can extend.
 * It is an <code>ActionListener</code> implementation with support for finding
 * out the <code>GuiApplication</code> that invoked the action.<p>
 *
 * The <i>internal</i> name of an action is the string passed to the
 * AppAction constructor. An action instance can be obtained from it's
 * internal name with the <code>GuiApplication.getAction()</code> method. An
 * action's internal name can be obtained with the <code>getName()</code>
 * method.<p>
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
 * @author Greg Messner <gmessner@messners.com>
 */
public abstract class AbstractAppAction
		extends javax.swing.AbstractAction
		implements AppAction {
	
	private static final long serialVersionUID = 1L;
	protected String _name = null;
	protected List<JComponent> _components = null;


	/**
	 * Creates a new <code>AppAction</code> computing the name
	 * of the action fom the class name.
	 */
	public AbstractAppAction () {

		super();
		_name = classNameToActionName(getClass().getName());
		putValue(SHORT_DESCRIPTION, _name);
	}
	
	
	/**
	 * Creates a new <code>AppAction</code>.
	 *
	 * @param name The name of the action
	 */
	public AbstractAppAction (String name) {
		super(name);
		_name = name;
	}


	/**
	 * Get the internal name of this action.
	 */
	public String getName () {
		return (_name);
	}


	/**
	 * Get the Application instance the action was invoked from.
	 */
	public static GuiApplication getApplication (EventObject evt) {

		if (evt == null) {
			return (null);
		}

		Object o = evt.getSource();
		if (o instanceof Component) {

			Component c = (Component)o;
			for (;;) {
				if (c instanceof GuiApplication) {
					return ((GuiApplication)c);
				} else if (c instanceof JPopupMenu) {
					c = ((JPopupMenu)c)
						.getInvoker();
				} else if (c == null) {
					break;
				} else {
					c = c.getParent();
				}
			}
		}

		/*
		 * This should never happen!
		 */
		System.err.println("BUG: getApplication() returning null");
		return (null);
	}


	/**
	 * Get the name of the class for the specified action name.
	 *
	 * @param  name  the name of the action
	 */
	public static String actionNameToClassName (String name) {

		if (name == null) {
			return (null);
		}

		name = name.substring(0, 1).toUpperCase() + name.substring(1);

		int i = 0;
		while (true) {
		        i = name.indexOf("-", i);
        		if (i > 0) {
                		String tmpname = name.substring(0, i);
                		name = tmpname + name.substring(i + 1, i + 2).
                        		toUpperCase() + name.substring(i + 2);
        		} else {
                		break;
        		}
		}

		if (name.endsWith("Action")) {
			return (name);
		}
		
		name = name + "Action";
		return (name);
	}


	/**
	 * Get the name of the action from the class name
	 *
	 * @param  name  the name of the class
	 */
	public static String classNameToActionName (String name) {

		if (name == null) {
			return (null);
		}

		if (name.endsWith("Action")) {
			name = name.substring(0, name.length() - 6);
		}

		int index = name.lastIndexOf('.');
		if (index > 0) {
			name = name.substring(index + 1);
		}

		StringBuffer action = new StringBuffer();
		action.append(Character.toLowerCase(name.charAt(0)));

		int len = name.length();
		for (int i = 1; i < len; i++) {

			char c = name.charAt(i);
			if (Character.isUpperCase(c)) {

				action.append('-');
				action.append(Character.toLowerCase(c));

			} else {

				action.append(c);
			}
		}

		return (action.toString());
	}


	/**
	 * This method is called when a component is added to this AppAction
	 * by the instantiating GuiApplication.<p>
	 *
	 * Override this method to get notification of added component.
	 *
	 * @param  c  the component added.
	 */
	public void componentAdded (JComponent c) {

		if (_components == null) {
			_components = new ArrayList<JComponent>();
		}

		_components.add(c);
	}


	/**
	 * Get the List of all the Components that have this action
	 * listener listening for actionPerformed().
	 */
	public List<JComponent> getComponents () {
	
		return (_components);
	}


	/**
	 * Get the number of Components that have this action
	 * listener listening for actionPerformed().
	 */
	public int getNumComponents () {

		if (_components == null) {
			return (0);
		}

		return (_components.size());
	}


	/**
	 * Get the Components that has this action
	 * listener listening for actionPerformed() at the specified index.
	 */
	public JComponent getComponentAt (int index) {

		if (_components == null || index < 0 ||
			index >= _components.size()) {
				return (null);
		}

		return _components.get(index);
	}


	/**
	 * This method is called to get the selected state of the value
	 * the action toggles. Override and return the actual flag if
	 * your action needs to support this.
	 */
	public boolean getSelected () {

		int numComponents = getNumComponents();
		for (int i = 0; i < numComponents; i++) {

			Object c = getComponentAt(i);
			if (c instanceof AbstractButton) {
				return (((AbstractButton)c).isSelected());
			}
		}

		throw new RuntimeException(getName() + 
			": does not support getSelected()");
	}


	/**
	 * This method is called to set the selected state of the value
	 * the action toggles. Overide this method if
	 * your action needs to support this.
	 */
	public void setSelected (boolean flag) {

		int numComponents = getNumComponents();
		for (int i = 0; i < numComponents; i++) {

			Object c = getComponentAt(i);
			if (c instanceof AbstractButton) {

				((AbstractButton)c).setSelected(flag);

			} else {
	
				throw new RuntimeException(getName() + 
					": does not support setSelected(boolean)");
			}
		}

		if (numComponents == 0) {
			throw new RuntimeException(getName() + 
				": does not support getSelected()");
		}
	}



	/**
	 * This method is called to set the tooltip on the attached components.
	 */
	public void setToolTip (String tooltip) {

		int numComponents = getNumComponents();
		for (int i = 0; i < numComponents; i++) {

			Object c = getComponentAt(i);
			if (c instanceof JComponent) {
				((JComponent)c).setToolTipText(tooltip);
			}
		}
	}



	/**
	 * This method is called to get the string value for the 
	 * component the action is associated with. Overide and
	 * return the actual value if your action needs to support this.
	 */
	public String getValue () {

		throw new RuntimeException(getName() + 
			": does not support getValue()");
	}


	/**
	 * This method is called to set the string value of the 
	 * component the action is associated with. Overide and
	 * set the actual value if your action needs to support this.
	 */
	public void setValue (String value) {

		throw new RuntimeException(getName() + 
			": does not support setValue(String)");
	}
}
