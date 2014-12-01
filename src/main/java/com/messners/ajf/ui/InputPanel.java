package com.messners.ajf.ui;


/**
 * Interface for user input panels.
 */
public interface InputPanel {

	/**
	 * Removes user input from the panel.
	 */
	public void clear();

	/**
	 * Populates the panel with user input.
	 * @param input User input to update panel with.
	 */
	public void populate(Object input);

	/**
	 * Gets user input from the panel. 
	 * @param data Container to populate with user input.
	 * @return Container populated with user input.
	 */
	public Object getInput(Object data);

	/**
	 * Validates user input.
	 * @return <code>true</code> if inputs are valid.
	 */
	public boolean validateInput();

}
