package com.messners.ajf.ui;


/**
 * This interface is implemented to indicate that this WizardPanel has
 * a step associated with it.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface WizardStep extends WizardPanel {

	/**
	 * Get the string that describes the step associated with this WizardStep.
	 *
	 * @return  the string for the step
	 */
	public String getStep ();
}
