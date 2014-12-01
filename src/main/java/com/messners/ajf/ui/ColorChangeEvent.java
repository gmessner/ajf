package com.messners.ajf.ui;

import java.awt.Color;


/**
 * This event is fired when a new color is selected from the ColorMenu.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ColorChangeEvent extends java.util.EventObject {

	private static final long serialVersionUID = 1L;
	
	public static final int UNKNOWN    = -1;
	public static final int BACKGROUND = 0;
	public static final int FOREGROUND = 1;

	protected Color _selected  = null;
	protected int _which_color = UNKNOWN;


	/*
	 * Constructor for a color change event.
	 *
	 * @param  source  the source of the event
	 * @param  selected  the newly selected color
	 */
	public ColorChangeEvent (Object source, Color selected) {
		super(source);
		_selected = selected;
	}

	/*
	 * Constructor for a color change event.
	 *
	 * @param  source  the source of the event
	 * @param  selected  the newly selected color
	 */
	public ColorChangeEvent (Object source, Color selected, int which) {
		super(source);
		_selected = selected;
		_which_color = which;
	}

	
	/**
	 * Get the new color from the event
	 *
	 * @return the new Color from the event
	 */
	public Color getColor () { 
		return (_selected);
	}


	/**
	 *
	 */
	public int getWhichColor () {
		return (_which_color);
	}
}
