package com.messners.ajf.app;

import com.messners.ajf.logging.Logging;

import java.io.IOException;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Logger;

/**
 * This class implements the Application interface for use by console applications.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class DefaultApplication implements Application {

	protected String userAppDir;

	protected Resources resources;
	protected FilePreferences preferences;

	protected Hashtable<String, String> parameters;
	protected Hashtable<String, Object> attributes;

	protected Logger logger;


	public DefaultApplication (String filename) throws IOException {

		this();

		if (filename != null) {

			try {

				preferences.setFilename(filename);
				preferences.load();

			} catch (IOException ioe) {
			}
		}
	}


	public DefaultApplication () {
	
		/*
		 * Default the user application directory to the current directory
		 */
		userAppDir = System.getProperty("user.dir");

		parameters  = new Hashtable<String, String>();
		attributes  = new Hashtable<String, Object>();
		resources   = new Resources(getClass());
		preferences = new FilePreferences();

		TimeZone.setDefault( java.util.TimeZone.getTimeZone("GMT") );
	}


 	/**
 	 * Get the Application version as a human-readable string.
	 *
	 * @return the application version as a human-readable String
	 */
	public String getVersion () {
		return (getResource(APP_VERSION));
	}


	/**
	 * Get the internal build number. 
	 *
	 * @return the application build number
	 */
	public int getBuildNumber () {

		try {

			String s = getResource(APP_BUILD_NUMBER, "0");
			return (Integer.parseInt(s));

		} catch (NumberFormatException nfe) {

			return (0);
		}
	}


	/**
	 * Get the date of internal build.
	 */
	public String getBuildDate () {
		return (getResource(APP_BUILD_DATE));
	}


	/**
	 * Returns the app's current ResourceBundle. Resources are static values
	 * used to construct the application.
	 */
	public ResourceBundle getResourceBundle () {
		return (resources.getResourceBundle());
	}

	
	/**
	 * Get the application's preferences. Preferences are used to keep
	 * runtime configurations that change as the application is used.
	 */
	public Preferences getPreferences () {
		return (preferences);
	}


	/**
	 * Get the named resource (property), returning null if it's not defined.
	 *
	 * @param name  the resource name
	 *
	 * @return the resource value as a String, or null if not found
	 */
	public String getResource (String name) {
		return (resources.getResource(name));
	}


	/**
	 * Get the named resource (property), returning the default value
	 * if it's not defined.
	 *
	 * @param name		 the resource name
	 * @param defaultVal the default value
	 *
	 * @return the resource value as a String
	 */
	public String getResource (String name, String defaultVal) {
		return (resources.getResource(name, defaultVal));
	}


	/**
	 * Returns the resource (property) with the specified name,
	 * formatting it with the <code>java.text.MessageFormat.format()</code>
	 * method. If the resource is not found null is returned
	 *
	 * @param name the property name
	 * @param args the positional parameters
	 *
	 * @return the formatted resource value as a String, or null if not found
	 */
	public String getResourceMessage (String name, Object[] args) {
		return (resources.getResourceMessage(name, name, args));
	}


	/**
	 * Returns the resource (property) with the specified name,
	 * formatting it with the <code>java.text.MessageFormat.format()</code>
	 * method. If the resource is not found defaultMsg will be used.
	 *
	 * @param name the property name
	 * @param defaultMsg  the default message String if resource is not found
	 * @param args the positional parameters
	 *
	 * @return the formatted resource value as a String
	 */
	public String getResourceMessage (String name, String defaultMsg, Object[] args) {
		return (resources.getResourceMessage(name, defaultMsg, args));
	}


	/**
	 * Get the specified resource and splits the value on space boundaries.
	 *
	 * @param  name  the resource name
	 */
	public String [] splitResource (String name) {
		return (resources.splitResource(name, null));
	}


	/**
	 * Get the specified resource and splits the value on space boundaries.
	 *
	 * @param  name  the resource name
	 * @param  defaultValue  the default value for the resource
	 */
	public String [] splitResource (String name, String defaultValue) {
		return (resources.splitResource(name, defaultValue));
	}


	/**
	 * Get a named parameter from the application. A parameter is basically
	 * a name/value pair stored on the application context.
	 *
	 * @param  name  the name of the parameter to get
	 */
	public String getParameter (String name) {
		return parameters.get(name);
	}


	/**
	 * Set a named string parameter on the application. A parameter is
	 * basically a name/value pair stored on the application context.
	 *
	 * @param  name  the name of the parameter to set
	 * @param  value the new String value for the parameter
	 */
	public void setParameter (String name, String value) {
		parameters.put(name, value);
	}


	/**
	 * Get a named object from the application. A parameter is basically
	 * a named object stored on the application context.
	 *
	 * @param  name  the name of the attribute to get
	 */
	public Object getAttribute (String name) {
		return (attributes.get(name));
	}


	/**
	 * Set a named object parameter on the application. A parameter is
	 * basically a named object stored on the application context.
	 *
	 * @param  name  the name of the attribute to set
	 * @param  value the new value for the attribute  
	 */
	public void setAttribute (String name, Object value) {
		attributes.put(name, value);
	}


	/**
	 * Gets the users application directory.  This directory is used to
	 * save information about the application specific to the user.
	 *
	 * @return  the user application directory
	 */
	public String getUserApplicationDirectory () {
		return (userAppDir);
	}


	/**
	 * Sets the users application directory.  This directory is used to
	 * save information about the application specific to the user. If
	 * directory does not exist it will be created.</p>
	 *
	 * NOTE: The user's home directory is prepended to this directory.
	 *
	 * @param  userAppDir the directory to set the user application directory to
	 */
	public void setUserApplicationDirectory (String userAppDir) {
		this.userAppDir = ApplicationHelper.getUserApplicationDirectory(userAppDir, true);
	}


	/**
	 * Sets the users application directory.  This directory is used to
	 * save information about the application specific to the user. If
	 * directory does not exist it will be created.
	 *
	 * @param  userAppDir the directory to set the user application directory to
	 * @param  prependHome if true prepends System.getProperty("user.home")
	 */
	public void setUserApplicationDirectory (String userAppDir, boolean prependHome) {
		this.userAppDir = ApplicationHelper.getUserApplicationDirectory(userAppDir, prependHome);
	}


	/**
	 * <p>Loads the preferences from a file that is named after the name of the
	 * top level application class and is assumed to be in the
	 * userApplicationDir property.</p>
	 */
	public void loadPreferences () {
		preferences = ApplicationHelper.loadPreferences(this);
	}


	/**
	 * Loads the preferences from the provided filename.  Filename is assumed
	 * to be in the userApplicationDir property.
	 *
	 * @param  filename  the name of the file in the user application directory
	 * to load the preferences from.
	 */
	public void loadPreferences (String filename) {
		preferences = ApplicationHelper.loadPreferences(this, filename);
	}


	/**
	 * Saves the application preferences.  This default implementation
	 * simply stores the application preferences to the data store.
	 * Override this method if your application has additional needs.
	 */
	public void savePreferences () {

		try {
			getPreferences().store();
		} catch (Exception ignore) {
		}
	}
	

	/**
	 * Restore the application preferences.  This default implementation
	 * does nothing.  Override this method if your application has
	 * additional needs.
	 */
	public void restorePreferences () {
	}


	/**
	 * Exit the application. This default implementation saves the preferences
	 * and calls System.exit() to exit the application.  Override this method
	 * if this behavior is not sufficient.
	 */
	public void exit () {
		savePreferences();
		System.exit(0);
	}


	/**
	 * Initialize the java.util.logging package.
	 *
	 * @param dir the directory name to put the log files in, if null
	 * defaults to the user application directory.
	 * @see ApplicationHelper#initializeLogging(Application, String)
	 */
	public void initializeLogging (String dir) {
		ApplicationHelper.initializeLogging(this, dir);
		logger = Logging.getLogger(this);
	}


	/**
	 * Gets the Application's Logger instance.
	 *
	 * @return the Applications ogger instance
	 */
	public synchronized Logger getLogger () {

		if (logger == null) {
			logger = Logging.getLogger(this);
		}

		return (logger);
	}
}
