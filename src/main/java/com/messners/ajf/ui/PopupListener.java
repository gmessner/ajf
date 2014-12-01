package com.messners.ajf.ui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

/**
 * This class sets up a MouseListener to listen for popup trigger events
 * and will popup a JPopupMenu instance.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class PopupListener implements MouseListener {


	/**
	 * The JPopupMenu instance to popup when a trigger event occurs.
	 */
	protected JPopupMenu popup;


	/**
	 * Default no-args constructor.
	 */
	public PopupListener () {
		popup = null;
	}


	/**
	 * Create an instance to popup the specified JPopupMenu.
	 *
	 * @param popup  The JPopupMenu to popup
	 */
	public PopupListener (JPopupMenu popup) {
		this.popup = popup;
	}


	/**
	 * Gets the JPopupMenu instance.  Implementations can override this
	 * method to return a situational specific menu if desired, or null
	 * to not popup anything.
	 *
	 * @param  e  the MouseEvent to get the popup for
	 * @return the JPopupMenu instance to popup
	 */
	public JPopupMenu getPopup (MouseEvent e) {
		return (popup);
	}


	/**
	 * Sets the JPopupMenu instance to popup when a popup trigger event
	 * has occured.
	 *
	 * @param  popup  the JPopupMenu instance
	 */
	public void setPopup (JPopupMenu popup) {
		this.popup = popup;
	}


	/**
	 * Called to check an event for a popup trigger and pops up the 
	 * JPopupMenu instance if event is a trigger.
	 *
	 * @param  evt  the MouseEvent to check for a trigger
	 */
	protected void checkPopup (MouseEvent evt) {

		if (!evt.isPopupTrigger()) {
			return;
		}

		JPopupMenu popup = getPopup(evt);
		if (popup != null) {
			popup.show((Component)evt.getSource(), evt.getX(), evt.getY());
		}
	}


	/*
	 * The following methods are for MouseListener
	 */

	public void mouseEntered (MouseEvent e) {
	}


	public void mouseExited (MouseEvent e) {
	}


	public void mouseClicked (MouseEvent e) {
		checkPopup(e);
	}


	public void mousePressed (MouseEvent e) {
		checkPopup(e);
	}


	public void mouseReleased (MouseEvent e) {
		checkPopup(e);
	}
}
