
package com.messners.ajf.app;

import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class specifies an interface that all (both GUI and console)
 * applications implement.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public interface Application {

	public static final String APP_VERSION      = "app.version";
	public static final String APP_BUILD_NUMBER = "app.build-number";
	public static final String APP_BUILD_DATE   = "app.build-date";

 	/**
 	 * Get the Application version as a human-readable string.
	 *
	 * @return the application version as a human-readable String
	 */
	public String getVersion ();


	/**
	 * Get the internal build number. 
	 *
	 * @return the application build number
	 */
	public int getBuildNumber ();


	/**
	 * Get the date of internal build.
	 */
	public String getBuildDate ();


	/**
	 * Returns the app's resource properties. Resources are static values
	 * used to construct the application.
	 */
	public ResourceBundle getResourceBundle ();
	
	
	/**
	 * Get the application's preferences. Preferences are used to keep
	 * runtime configurations that change as the application is used.
	 */
	public Preferences getPreferences ();


	/**
	 * Get the named resource (property), returning null if it's not defined.
	 *
	 * @param name  the resource name
	 *
	 * @return the resource value as a String, or null if not found
	 */
	public String getResource (String name);


	/**
	 * Get the named resource (property), returning the default value
	 * if it's not defined.
	 *
	 * @param name	     the resource name
	 * @param defaultVal the default value
	 *
	 * @return the resource value as a String
	 */
	public String getResource (String name, String defaultVal);


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
	public String getResourceMessage (String name, Object[] args);


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
	public String getResourceMessage (String name, String defaultMsg, Object[] args);


	/**
	 * Get the specified resource and splits the value on space boundaries.
	 *
	 * @param  name  the resource name
	 */
	public String [] splitResource (String name);


	/**
	 * Get the specified resource and splits the value on space boundaries.
	 *
	 * @param  name  the resource name
	 * @param  defaultValue  the default value for the resource
	 */
	public String [] splitResource (String name, String defaultValue);


	/**
	 * Get a named parameter from the application. A parameter is basically
	 * a name/value pair stored on the application context.
	 *
	 * @param  name  the name of the parameter to get
	 */
	public String getParameter (String name);


	/**
	 * Set a named string parameter on the application. A parameter is
	 * basically a name/value pair stored on the application context.
	 *
	 * @param  name  the name of the parameter to set
	 * @param  value the new String value for the parameter
	 */
	public void setParameter (String name, String value);


	/**
	 * Get a named object from the application. An attribute is basically
	 * a named object stored on the application context.
	 *
	 * @param  name  the name of the attribute to get
	 */
	public Object getAttribute (String name);


	/**
	 * Set a named object parameter on the application. An attribute is
	 * basically a named object stored on the application context.
	 *
	 * @param  name  the name of the attribute to set
	 * @param  value the new value for the attribute  
	 */
	public void setAttribute (String name, Object value);


	/**
	 * Gets the users application directory.  This directory is used to
	 * save information about the application specific to the user.
	 *
	 * @return  the user application directory
	 */
	public String getUserApplicationDirectory ();


	/**
	 * Initialize the java.util.logging package.
	 *
	 * @param dir the directory name to put the log files in, if null
	 * defaults to the user application directory.
	 */
	public void initializeLogging (String dir);


	/**
	 * Gets the Application's Logger instance.
	 *
	 * @return the Applications ogger instance
	 */
	public Logger getLogger ();
}
