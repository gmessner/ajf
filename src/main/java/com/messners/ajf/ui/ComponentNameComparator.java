package com.messners.ajf.ui;

import java.util.Comparator;
import java.awt.Component;


/**
 * Provides an implementation of Comparator for sorting Component
 * instances by name.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ComponentNameComparator implements Comparator<Object> {

	private boolean ascending;

	public ComponentNameComparator () {
		ascending = true;
	}


	public void setAscending (boolean flag) {
		ascending = flag;
	}


	public int compare (Object obj1, Object obj2) {

		Component c1 = (Component)obj1;
		Component c2 = (Component)obj2;
		int results = c1.getName().compareTo(c2.getName());
		return (ascending ? results : -results);
	}
}
