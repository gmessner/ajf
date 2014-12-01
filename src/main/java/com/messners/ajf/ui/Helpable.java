package com.messners.ajf.ui;


/**
 * This interface is implemented to indicate that the component has Help
 * enabled.  
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface Helpable {

	/**
	 * This method is called when help is to be displayed.
	 */
	public void help (Object help_on);
}
