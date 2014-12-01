package com.messners.ajf.app;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

/**
 * This interface defines an interface for storing application preferences.
 * Application preferences are configuration settings that change during
 * runtime and are things such as window location and size.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface Preferences {


	/**
	 * Load the preferences.
	 *
	 * @exception  IOException when an error occurs loading the preferences.
	 */
	public void load () throws IOException;


	/**
	 * Save the preferences.
	 *
	 * @exception  IOException when an error saving the preferences.
	 */
	public void store () throws IOException;


	/**
	 * Clear this instance of all preferences.
	 */
	public void clear ();


	/**
	 * Set a default value for a preference. If this preference was already
	 * found in the preferences file this method does nothing.
	 *
	 * @param  name   the name of the preference
	 * @param  value  the value for the preference
	 */
	public void setDefault (String name, String value);


	/**
	 * Set a default value for a preference. If this preference was already
	 * found in the preferences file this method does nothing.
	 *
	 * @param  name   the name of the preference
	 * @param  value  the value for the preference
	 */
	public void setDefault (String name, int value);


	/**
	 * Set a default value for a preference. If this preference was already
	 * found in the preferences file this method does nothing.
	 *
	 * @param  name  the name of the preference
	 * @param  flag  the <code>boolean</code>value for the preference
	 */
	public void setDefault (String name, boolean flag);


	/**
	 * Get the value of the specified key. If the specified key does not
	 * have a value return the specified default value.
	 *
	 * @param  key  the key for the preference
	 * @param  default_value  the default value for the preference
	 */
	public String get (String key, String default_value);


	/**
	 * Get the value of the specified key. If the specified key does not
	 * have a value return the specified default value.
	 *
	 * @param  key  the key for the preference
	 * @param  default_value  the default value for the preference
	 */
	public double get (String key, double default_value);


	/**
	 * Get the value of the specified key. If the specified key does not
	 * have a value return the specified default value.
	 *
	 * @param  key  the key for the preference
	 * @param  default_value  the default value for the preference
	 */
	public int get (String key, int default_value);


	/**
	 * Get the value of the specified key. If the specified key does not
	 * have a value return the specified default value.
	 *
	 * @param  key  the key for the preference
	 * @param  default_value  the default value for the preference
	 */
	public long get (String key, long default_value);


	/**
	 * Get the value of the specified key. If the specified key does not
	 * have a value return the specified default value.
	 *
	 * @param  key  the key for the preference
	 * @param  default_value  the default value for the preference
	 */
	public boolean get (String key, boolean default_value);


	/**
	 * Get the value of the specified key. If the specified key is not
	 * found the altKey is checked and if that key is not found the 
	 * specified default value will be returned.
	 *
	 * @param  key  the key for the preference
	 * @param  altKey  the alternate key for the preference if key not found
	 * @param  default_value  the default value for the preference
	 */
	public String get (String key, String altKey, String default_value);


	/**
	 * Get the value of the specified key. If the specified key is not
	 * found the altKey is checked and if that key is not found the 
	 * specified default value will be returned.
	 *
	 * @param  key  the key for the preference
	 * @param  altKey  the alternate key for the preference if key not found
	 * @param  default_value  the default value for the preference
	 */
	public double get (String key, String altKey, double default_value);


	/**
	 * Get the value of the specified key. If the specified key is not
	 * found the altKey is checked and if that key is not found the 
	 * specified default value will be returned.
	 *
	 * @param  key  the key for the preference
	 * @param  altKey  the alternate key for the preference if key not found
	 * @param  default_value  the default value for the preference
	 */
	public int get (String key, String altKey, int default_value);


	/**
	 *
	 * @param  key  the key for the preference
	 * @param  altKey  the alternate key for the preference if key not found
	 * @param  default_value  the default value for the preference
	 */
	public long get (String key, String altKey, long default_value);


	/**
	 * Get the value of the specified key. If the specified key is not
	 * found the altKey is checked and if that key is not found the 
	 * specified default value will be returned.
	 *
	 * @param  key  the key for the preference
	 * @param  altKey  the alternate key for the preference if key not found
	 * @param  default_value  the default value for the preference
	 */
	public boolean get (String key, String altKey, boolean default_value);


	/**
	 * Set the specified key with the specified String value.
	 *
	 * @param  key  the key for the preference
	 * @param  value  the value for the preference
	 */
	public void put (String key, String value);


	/**
	 * Set the specified key with the specified double value.
	 *
	 * @param  key  the key for the preference
	 * @param  value  the value for the preference
	 */
	public void put (String key, double value);


	/**
	 * Set the specified key with the specified int value.
	 *
	 * @param  key  the key for the preference
	 * @param  value  the value for the preference
	 */
	public void put (String key, int value);


	/**
	 * Set the specified key with the specified int value.
	 *
	 * @param  key  the key for the preference
	 * @param  value  the value for the preference
	 */
	public void put (String key, long value);


	/**
	 * Set the specified key with the specified boolean value.
	 *
	 * @param  key  the key for the preference
	 * @param  value  the value for the preference
	 */
	public void put (String key, boolean value);


	/**
	 * Remove the specified preference.
	 *
	 * @param  name  the name of the preference to remove
	 * @return  the value of the preference removed, null if not found
	 */
	public String remove (String name);


	/**
	 * Get the List of the items in the FIFO list specified by name.
	 *
	 * @param  name  the name of the list
	 */
	public List<String> getList (String name);


	/**
	 * Set the List for the specified list name.
	 *
	 * @param  name   the name of the list
	 * @param  list   the actual List to set as the value for the named list
	 */
	public void setList (String name, List<String> list);


	/**
	 * Add an item to the front of FIFO list specified by name.
	 *
	 * @param  name   the name of the list
	 * @param  value  the value to be added to the list.
	 */
	public void addListItem (String name, String value);


	/**
	 * Clear the FIFO list specified by name.
	 *
	 * @param  name   the name of the list
	 */
	public void clearList (String name);


	/**
	 * Gets an Enumeration of the preference names that start with the 
	 * specified string. If the string is null or "" all names will be
	 * returned in the Enumeration.
	 *
	 * @param  pname  the name to look for
	 * @return an Enumeration of the preference names 
	 */
	public Enumeration<?> getPreferences (String pname);
}

