package com.messners.ajf.ui;

import com.messners.ajf.util.ObjectLoader;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import java.util.Hashtable;

/**
 * This class provides an AppAction loader and cache that can be used to help
 * construct GuiApplication implementations. It provides a KeyListener that
 * can be used to licsten for muti-key shortcuts.
 *
 * @author  Greg Messner <greg@messners.com>
 */

public class ActionManager implements KeyListener {

	protected Hashtable<String, AppAction> _actions;
	protected Hashtable<KeyStroke, Object> _bindings;
	protected Hashtable<KeyStroke, Object> _currentPrefix;

	protected GuiApplication _app;
	protected ObjectLoader _action_loader;


	/**
	 * Construct a ActionManager instance associated with the GuiApplication.
	 */
	public ActionManager (GuiApplication app) {

		_app = app;

		String pkg = app.getClass().getPackage().getName();
		if (pkg != null && pkg.length() > 0) {
			pkg = pkg + ".actions";
		} else {
			pkg = "actions";
		}


		_action_loader = new ObjectLoader(
    	    app.getClass().getClassLoader(), pkg, "Action");

		_actions = new Hashtable<String, AppAction>();
		_bindings = new Hashtable<KeyStroke, Object>();
		_currentPrefix = _bindings;
	}


	/**
	 * Get the GuiApplication instance associated with this ActionManager.
	 *
	 * @return  the GuiApplication associated with this ActionManager
	 */
	public GuiApplication getApplication () {
		return (_app);
	}


	/**
	 * Registers an action with the application.
	 *
	 * @param action the action name
	 */
	public void addAction (AppAction action) {

		_actions.put(action.getName(), action);
	}


	/**
	 * Returns a named action.
	 *
	 * @param actionName the action name
	 */
	public AppAction getAction (String actionName) {

		if (actionName == null) {
			return (null);
		}

		actionName = actionName.replace('_', '-');
		AppAction action = _actions.get(actionName);
		if (action != null) {
			return (action);
    	}

		try {

			action = (AppAction)_action_loader.loadObject(
								actionName, _app.getClass(), _app);

			if (action instanceof RunnableAppAction) {

				ActionRunner runner = new ActionRunner(action);	
				addAction(runner);
				return (runner);

			} else {

        		addAction(action);
			}

	    } catch (Exception ignore) {
	    }

		return (action);
	}


	/**
	 * Sets the enabled state of an AppAction.
	 */
	public void setActionEnabled (AppAction action, boolean flag) {

		if (action != null) {
			action.setEnabled(flag);
		}
	}


	/**
	 * Sets the enabled state of an AppAction.
	 */
	public void setActionEnabled (String action, boolean flag) {

		setActionEnabled(getAction(action), flag);
	}


	/**
	 * Add a key binding to this Application.
	 *
	 * @param key  the key stroke
	 * @param cmd  the action command name
	 */
	public void addKeyBinding (javax.swing.KeyStroke key, String cmd) {
		_bindings.put(key, cmd);
	}


	/**
	 * Add a multi-keystroke key binding to this Application.
	 *
	 * @param key1 The first key stroke
	 * @param key2 The second key stroke
	 * @param cmd The action command
	 */
	@SuppressWarnings("unchecked")
	public void addKeyBinding (KeyStroke key1, KeyStroke key2, String cmd) {

 		Object o = _bindings.get(key1);
		if (!(o instanceof Hashtable)) {
			o = new Hashtable<KeyStroke, Object>();
			_bindings.put(key1, o);
		}

		((Hashtable<KeyStroke, Object>)o).put(key2, cmd);
	}


	/**
	 * Invoked when a key has been pressed. 
	 */
	public void keyPressed (KeyEvent evt) {
		handleKeyEvent(evt);
   	}


	/**
	 * Invoked when a key has been released. 
	 */
	public void keyReleased (KeyEvent e)  {
	}


	/**
	 * Invoked when a key has been typed. 
	 */
	public void keyTyped (KeyEvent e) {
	}


	/**
	 * This method handles the multi-key shortcuts.
	 */
	@SuppressWarnings("unchecked")
	protected void handleKeyEvent (KeyEvent evt) {

		int keyCode = evt.getKeyCode();
		int modifiers = evt.getModifiers();

		if ((modifiers & ~InputEvent.SHIFT_MASK) != 0
				|| evt.isActionKey()
				|| keyCode == KeyEvent.VK_TAB
				|| keyCode == KeyEvent.VK_ENTER) {

			KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode,
				modifiers);
			Object o = _currentPrefix.get(keyStroke);
			if (o == null && _currentPrefix != _bindings) {

				Object source = evt.getSource();
				if (source instanceof Component) {
					((Component)source).getToolkit().beep();
				}

				_currentPrefix = _bindings;
				evt.consume();
				return;

			} else if (o instanceof String) {

				String s = (String)o;
				int index = s.indexOf('@');
				String cmd;
				if (index != -1) {
					cmd = s.substring(index+1);
					s = s.substring(0,index);
				} else {
					cmd = null;
				}

				AppAction action = getAction(s);
				if (action == null) {
					System.err.println(
						"Invalid key binding: " + s);
					_currentPrefix = _bindings;
					evt.consume();
					return;
				}

				action.actionPerformed(
					new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, cmd));

				_currentPrefix = _bindings;
				evt.consume();
				return;

			} else if (o instanceof Hashtable) {

				_currentPrefix = (Hashtable<KeyStroke, Object>)o;
				evt.consume();
				return;
			}

		} else if (keyCode != KeyEvent.VK_SHIFT
				&& keyCode != KeyEvent.VK_CONTROL
				&& keyCode != KeyEvent.VK_ALT) {
			_currentPrefix = _bindings;
		}
	}
}
