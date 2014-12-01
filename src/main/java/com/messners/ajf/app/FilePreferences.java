package com.messners.ajf.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * This class is the default implementation of the Preferences interface.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class FilePreferences extends Properties implements Preferences {

	private static final long serialVersionUID = 1L;
	protected File prefsFile = null;
	protected String header = "DO NOT EDIT!";


	/**
	 * Create a <code>FilePreferences</code> object. If this constructor is 
	 * used a call to setFilename(String filename) must be made prior to
	 * calling load().
	 */
	public FilePreferences () {
	}
	

	/**
	 * Create a <code>FilePreferences</code> object that will store its 
	 * preferences in <code>filename</code>.
	 *
	 * @param   filename   the name of the preferences file.
	 * @exception  IOException when an error occurs loading the preferences.
	 */
	public FilePreferences (String filename) throws IOException {

		/*
		 * Open and read the passed in preference file
		 */
		prefsFile = new File(filename);
		load();
	}


	/**
	 * Get the filename which holds the preferences.
	 *
	 * @return  the filename which holds the preferences
	 */ 
	public synchronized String getFilename () {

		if (prefsFile == null) {
			return (null);
		}

		return (prefsFile.getAbsolutePath());
	}


	/**
	 * Set the filename which holds the preferences.
	 *
	 * @param  filename the filename which holds the preferences
	 */ 
	public synchronized void setFilename (String filename) {
		prefsFile = new File(filename);
	}


	/**
	 * Set the header (comment) to be displayed at the top of the 
	 * preferences file.
	 *
	 * @param   header   Header (comment) string to display in
	 * prefernces file.
	 */
	public void setFileHeader (String header) {
		this.header = header;
	}


	/**
	 * Load the preferences.
	 *
	 * @exception  IOException when an error occurs loading the preferences.
	 */
	public void load () throws IOException {

		if (prefsFile == null) {
			throw new IOException("no filename specified");
		}

		FileInputStream fis = new FileInputStream(prefsFile);
		load(fis);
		fis.close();
	}


	/**
	 * Save the preferences.
	 *
	 * @exception  IOException when an error saving the preferences.
	 */
	public void store () throws IOException {

		if (prefsFile == null) {
			throw new IOException("no filename specified");
		}


		FileOutputStream fos = new FileOutputStream(prefsFile);
		try {
			store(fos, header);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		} finally {

			try {
				fos.close();
			} catch (Exception ignore) {
			}
		}
	}


	/**
	 * Set a default value for a preference. If this preference was already
	 * found in the preferences file this method does nothing.
	 *
	 * @param  name   the name of the preference
	 * @param  value  the value for the preference
	 */
	public void setDefault (String name, String value) {

		if (value == null) {
			return;
		}

		if (!containsKey(name)) {
			super.put(name, value);
		}
	}


	/**
	 * Set a default value for a preference. If this preference was already
	 * found in the preferences file this method does nothing.
	 *
	 * @param  name   the name of the preference
	 * @param  value  the value for the preference
	 */
	public void setDefault (String name, int value) {

		if (!containsKey(name)) {
			super.put(name, String.valueOf(value));
		}
	}


	/**
	 * Set a default value for a preference. If this preference was already
	 * found in the preferences file this method does nothing.
	 *
	 * @param  name  the name of the preference
	 * @param  flag  the <code>boolean</code>value for the preference
	 */
	public void setDefault (String name, boolean flag) {

		if (!containsKey(name)) {
			super.put(name, (new Boolean(flag).toString()));
		}
	}


	/**
	 * Get the value of the specified key. If the specified key does not
	 * have a value return the specified default value.
	 *
	 * @param  key  the key for the preference
	 * @param  default_value  the default value for the preference
	 */
	public String get (String key, String default_value) {

		String value = getValue(key);
		if (value != null) {
			return (value);
		} else {
			return (default_value);
		}
	}


	/**
	 * Get the value of the specified key. If the specified key does not
	 * have a value return the specified default value.
	 *
	 * @param  key  the key for the preference
	 * @param  default_value  the default value for the preference
	 */
	public double get (String key, double default_value) {

		String value = getValue(key);
		if (value != null) {
			return (Double.parseDouble(value));
		} else {
			return (default_value);
		}
	}


	/**
	 * Get the value of the specified key. If the specified key does not
	 * have a value return the specified default value.
	 *
	 * @param  key  the key for the preference
	 * @param  default_value  the default value for the preference
	 */
	public int get (String key, int default_value) {

		String value = getValue(key);
		if (value != null) {
			return (Integer.parseInt(value));
		} else {
			return (default_value);
		}
	}


	/**
	 * Get the value of the specified key. If the specified key does not
	 * have a value return the specified default value.
	 *
	 * @param  key  the key for the preference
	 * @param  default_value  the default value for the preference
	 */
	public long get (String key, long default_value) {

		String value = getValue(key);
		if (value != null) {
			return (Long.parseLong(value));
		} else {
			return (default_value);
		}
	}


	/**
	 * Get the value of the specified key. If the specified key does not
	 * have a value return the specified default value.
	 *
	 * @param  key  the key for the preference
	 * @param  default_value  the default value for the preference
	 */
	public boolean get (String key, boolean default_value) {

		String value = getValue(key);
		if (value != null) {
			return (Boolean.valueOf(value).booleanValue());
		} else {
			return (default_value);
		}
	}


	/**
	 * Get the value of the specified key. If the specified key is not
	 * found the altKey is checked and if that key is not found the 
	 * specified default value will be returned.
	 *
	 * @param  key  the key for the preference
	 * @param  altKey  the alternate key for the preference if key not found
	 * @param  default_value  the default value for the preference
	 */
	public String get (String key, String altKey, String default_value) {

		String value = getValue(key);
		if (value != null) {
			return (value);
		}
		
		return (get(altKey, default_value));
	}


	/**
	 * Get the value of the specified key. If the specified key is not
	 * found the altKey is checked and if that key is not found the 
	 * specified default value will be returned.
	 *
	 * @param  key  the key for the preference
	 * @param  altKey  the alternate key for the preference if key not found
	 * @param  default_value  the default value for the preference
	 */
	public double get (String key, String altKey, double default_value) {

		String value = getValue(key);
		if (value != null) {
			return (Double.parseDouble(value));
		}

		return (get(altKey, default_value));
	}

	/**
	 * Get the value of the specified key. If the specified key is not
	 * found the altKey is checked and if that key is not found the 
	 * specified default value will be returned.
	 *
	 * @param  key  the key for the preference
	 * @param  altKey  the alternate key for the preference if key not found
	 * @param  default_value  the default value for the preference
	 */
	public int get (String key, String altKey, int default_value) {

		String value = getValue(key);
		if (value != null) {
			return (Integer.parseInt(value));
		}

		return (get(altKey, default_value));
	}

	/**
	 *
	 * @param  key  the key for the preference
	 * @param  altKey  the alternate key for the preference if key not found
	 * @param  default_value  the default value for the preference
	 */
	public long get (String key, String altKey, long default_value) {

		String value = getValue(key);
		if (value != null) {
			return (Long.parseLong(value));
		}

		return (get(altKey, default_value));
	}

	/**
	 * Get the value of the specified key. If the specified key is not
	 * found the altKey is checked and if that key is not found the 
	 * specified default value will be returned.
	 *
	 * @param  key  the key for the preference
	 * @param  altKey  the alternate key for the preference if key not found
	 * @param  default_value  the default value for the preference
	 */
	public boolean get (String key, String altKey, boolean default_value) {

		String value = getValue(key);
		if (value != null) {
			return (Boolean.valueOf(value).booleanValue());
		}

		return (get(altKey, default_value));
	}

	/**
	 * Set the specified key with the specified String value.
	 *
	 * @param  key  the key for the preference
	 * @param  value  the value for the preference
	 */
	public void put (String key, String value) {

		if (key != null && value != null) {
			super.put(key, value);
		}
	}


	/**
	 * Set the specified key with the specified double value.
	 *
	 * @param  key  the key for the preference
	 * @param  value  the value for the preference
	 */
	public void put (String key, double value) {
		super.put(key, String.valueOf(value));
	}


	/**
	 * Set the specified key with the specified int value.
	 *
	 * @param  key  the key for the preference
	 * @param  value  the value for the preference
	 */
	public void put (String key, int value) {
		super.put(key, String.valueOf(value));
	}


	/**
	 * Set the specified key with the specified int value.
	 *
	 * @param  key  the key for the preference
	 * @param  value  the value for the preference
	 */
	public void put (String key, long value) {
		super.put(key, String.valueOf(value));
	}


	/**
	 * Set the specified key with the specified boolean value.
	 *
	 * @param  key  the key for the preference
	 * @param  value  the value for the preference
	 */
	public void put (String key, boolean value) {
		super.put(key, String.valueOf(value));
	}


	/**
	 * Remove the specified preference.
	 *
	 * @param  name  the name of the preference to remove
	 * @return  the value of the preference removed, null if not found
	 */
	public String remove (String name) {
		return ((String)super.remove(name));
	}


	/**
	 * Get the List of the items in the FIFO list specified by name.
	 *
	 * @param  name  the name of the list
	 */
	public List<String> getList (String name) {

		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; ; i++) {

			String key = name + i;
			String value = get(key, (String)null);
			if (value == null) {
				break;
			}

			al.add(value);
		}

		return (al);
	}


	/**
	 * Set the List for the specified list name.
	 *
	 * @param  name   the name of the list
	 * @param  list   the actual List to set as the value for the named list
	 */
	public void setList (String name, List<String> list) {

		if (name == null) {
			return;
		}

		clearList(name);

		if (list == null) {
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			String key = name + i;
			put(key, list.get(i));
		}
	}


	/**
	 * Add an item to the front of FIFO list specified by name.
	 *
	 * @param  name   the name of the list
	 * @param  value  the value to be added to the list.
	 */
	public void addListItem (String name, String value) {

		if (name == null || value == null) {
			return;
		}

		List<String> items = getList(name);
		for (int i = 0; i < items.size(); i++) {

			String tmp_value = items.get(i);
	        if (value.equals(tmp_value)) {
				items.remove(i);
			}
		}

		items.add(0, value);
		for (int i = 0; i < items.size(); i++) {
			String key = name + i;
			remove(key);
		}
		
		for (int i = 0; i < items.size(); i++) {
			String key = name + i;
			put(key, items.get(i));
		}
	}


	/**
	 * Clear the FIFO list specified by name.
	 *
	 * @param  name   the name of the list
	 */
	public void clearList (String name) {

		for (int i = 0; ; i++) {

			String key = name + i;
			if (!contains(key)) {
				break;
			}

			super.remove(key);
		}
	}


	private String getValue (String key) {

		/*
		 * First make sure it is OK to do a getProperty()
		 */
		try {

			SecurityManager security = System.getSecurityManager();
			if (security != null) {
				security.checkPropertyAccess(key);
			}

		} catch (SecurityException se) {
			return (null);
		} catch (Exception e) {
			return (null);
		}

		String value = System.getProperty(key);
		if (value != null) {
			return (value);
		}

		return ((String)get(key));
	}

	/**
	 * Gets an Enumeration of the preference names that start with the 
	 * specified string. If the string is null or "" all names will be
	 * returned in the Enumeration.
	 *
	 * @param  pname  the name to look for
	 * @return an Enumeration of the preference names 
	 */
	public Enumeration<?> getPreferences (String pname) {

		Enumeration<?> allNames = propertyNames();
		if (pname == null || pname.length() == 0) {
			return (allNames);
		}

		ArrayList<String> pnames = new ArrayList<String>();
		while (allNames.hasMoreElements()) {

			String name = (String)allNames.nextElement();
			if (name.startsWith(pname)) {
				pnames.add(name);
			}
		}
		
		return (Collections.enumeration(pnames));
	}
}

