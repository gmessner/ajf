package com.messners.ajf.ui;


/**
 * Implement this interface in order to recieve notification of
 * color changes in the ColorMenu
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface ColorChangeListener extends java.util.EventListener {

	/**
	 * This method a new Color has been selected from the Colormenu.
	 */
	public void colorChange (ColorChangeEvent evt);
}
