package com.messners.ajf.ui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * This class is utilized by the framework to launch a RunnableAction.
 * It basically acts as a proxy for the associated RunnableAction and
 * invokes it using a SwingWorker.
 *
 * @author Greg Messner <greg@messners.com>
 */
public class ActionRunner implements AppAction {

	protected AppAction action;


	/**
	 * Construct an ActionRunner associated with the specified AppAction.
	 */
	public ActionRunner (AppAction ra) {
		action = ra;
	}


	/**
	 * Get the AppAction associated with this ActionRunner.
	 */
	public AppAction getAction () {
		return (action);
	}


	public void actionPerformed (ActionEvent e) {

		SwingUtilities.invokeLater(new ActionInvoker(e));
	}


	private class ActionInvoker implements Runnable {

		private ActionEvent event;

		public ActionInvoker (ActionEvent e) {
			event = e;
		}

		public void run () {

			Worker worker = new Worker(event);
			worker.start();
		}
	}


	protected final class Worker extends SwingWorker {

		ActionEvent event;

		Worker (ActionEvent e) {
			event = e;
		}

		public Object construct() {

			action.actionPerformed(event);
			return (action.getName() + " done");
   		}
	}


	/**
	 * Get the internal name of this action.
	 */
	public String getName () {
		return (action.getName());
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
		action.componentAdded(c);
	}


	/**
	 * Adds a PropertyChange listener. 
	 */
	public void addPropertyChangeListener (PropertyChangeListener listener) {
		action.addPropertyChangeListener(listener);
	}


	/**
	 * Gets one of this object's properties using the associated key. 
	 */
	public Object getValue (String key) {
		return (action.getValue(key));
	}


	/**
	 * Returns the enabled state of the Action. 
	 */
	public boolean isEnabled () {
		return (action.isEnabled());
	}


	/**
	 * Sets one of this object's properties using the associated key. 
	 */
	public void putValue (String key, Object value) {
		action.putValue(key, value);
	}


	/**
	 * Removes a PropertyChange listener. 
	 */
	public void removePropertyChangeListener (PropertyChangeListener listener) {
		action.removePropertyChangeListener(listener);
	}


	/**
	 * Sets the enabled state of the Action. 
	 */
	public void setEnabled (boolean b) {
		action.setEnabled(b);
	}
}
