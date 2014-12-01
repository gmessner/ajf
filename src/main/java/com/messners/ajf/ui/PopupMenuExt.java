package com.messners.ajf.ui;

import javax.swing.JPopupMenu;


/**
 * This class simply extends JPopupMenu so that a user object
 * property can easily be added.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class PopupMenuExt extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	protected Object _object = null;

	public PopupMenuExt () {
		super();
	}


	public PopupMenuExt (String label) {
		super(label);
	}


	public Object getUserObject () {
		return (_object);
	}


	public void setUserObject (Object object) {
		_object = object;
	}
}

