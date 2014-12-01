package com.messners.ajf.app;

import com.messners.ajf.util.XmlResourceBundle;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * This class encapsulates loading and accessing the data in a ResourceBundle.
 *
 * @author  Greg Messner <greg@messners.com>
 */

public class Resources {

	protected ResourceBundle resources;
	protected ArrayList<Resources> includes;
	protected int numIncludes;

	/**
	 * Default no args constructor.
	 */
	public Resources () {
		this(null, (Locale)null);
	}


	/**
	 * Construct that takes the ResourceBundle to load resources from
	 * as a parameter.
	 *
	 * @param  resources  the ResourceBundle to load resources from
	 */
	public Resources (ResourceBundle resources) {

		this.resources = resources;
	}



	/**
	 * Construct that takes the ResourceBundle to load resources from
	 * as a parameter.
	 *
	 * @param  owner  the Class to get the resource bundle for
	 * @param  resources  the ResourceBundle to load resources from
	 */
	public Resources (Class<? extends Object> owner, ResourceBundle resources) {

		this.resources = resources;
		processIncludes(owner, null);
	}


	/**
	 * Load the ResourceBundle for the specified class using the default Locale.
	 *
	 * @param  owner  the Class to get the resource bundle for
	 * @exception  MissingResourceException if no resource bundle for the
	 * specified Class can be found.
	 */
	public Resources (Class<? extends Object> owner) throws MissingResourceException {
		this(owner, (Locale)null);
	}


	/**
	 * Load the ResourceBundle for the specified class using
	 * the specified Locale.
	 *
	 * @param  owner  the Class to get the resource bundle for
	 * @param  locale the Locale to get the ResourceBundle for
	 * @exception  MissingResourceException if no resource bundle for the
	 * specified Class can be found.
	 */
	public Resources (Class<? extends Object> owner, Locale locale) throws MissingResourceException {

		if (owner == null) {
			owner = getClass();
		}

		if (locale == null) {
			locale = Locale.getDefault();
		}


		/*
		 * First try and load the resource as an XML ResourceBundle
		 */
		try {

			resources = XmlResourceBundle.getBundle(
				owner.getName(),
				locale,
				owner.getClassLoader());

		} catch (Exception e) {

			resources = ResourceBundle.getBundle(
				owner.getName(),
				locale,
				owner.getClassLoader());
		}

		processIncludes(owner, locale);
	}


	/**
	 * Includes resources from other files. Looks for a resource named
	 * "%includes%" which is a whitespace delimited list of resources
	 * to include.
	 */
	protected void processIncludes (Class<? extends Object> owner, Locale locale) {

		String includeNames[] = splitResource("%includes%");
		numIncludes = (includeNames == null ? 0 : includeNames.length);
		if (numIncludes == 0) {
			return;
		}

		if (locale == null) {
			locale = Locale.getDefault();
		}


		includes = new ArrayList<Resources>(numIncludes);
		for (int i = 0; i < numIncludes; i++) {

			try {

				ResourceBundle bundle = ResourceBundle.getBundle(
							includeNames[i], locale, owner.getClassLoader());
				includes.add(new Resources(bundle));

			} catch (Exception e) {
			}
		}

		numIncludes = includes.size();
	}


	/**
	 * Gets the associated ResourceBundle.
	 */
	public ResourceBundle getResourceBundle () {
		return (resources);
	}


	/**
	 * Sets the associated ResourceBundle.
	 *
	 * @param  resources  the ResourceBundle to load resources from
	 */
	public void setResourceBundle (ResourceBundle resources) {
		this.resources = resources;
	}

	
	/**
	 * Get the named resource (property), returning null if it's not defined.
	 *
	 * @param name  the resource name
	 * @return the resource value as a String, or null if not found
	 */
	public String getResource (String name) {

		try {
			return (resources.getString(name));
		} catch (Exception ignore) {
			return (getResourceFromIncludes(name));
		}
	}


	/**
	 * Tries to get the resource from an included resource.
	 */
	private String getResourceFromIncludes (String name) {

		if (numIncludes == 0) {
			return (null);
		}

		for (int i = numIncludes - 1; i >= 0; i--) {

			Resources resources = includes.get(i);
			String s = resources.getResource(name);
			if (s != null) {
				return (s);
			}
		}

		return (null);
	}


	/**
	 * Get the named resource (property), returning the default value
	 * if it's not defined.
	 *
	 * @param name		 the resource name
	 * @param defaultVal the default value
	 * @return the resource value as a String
	 */
	public String getResource (String name, String defaultVal) {

		String s = getResource(name);
		return (s == null ? defaultVal : s);
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
		return (getResourceMessage(name, name, args));
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
	public String getResourceMessage (
			String name, String defaultMsg, Object[] args) {

	   if (name == null) {
		   return (null);
	   } else if (args == null) {
		   return (getResource(name, defaultMsg));
	   }

	   String value   = getResource(name, defaultMsg);
	   String message = MessageFormat.format(value, args);
	   return (message);
	}


	/**
	 * Get the specified resource and splits the value on space boundaries.
	 *
	 * @param  name  the resource name
	 */
	public String [] splitResource (String name) {

		return (splitResource(name, null));
	}


	/**
	 * Get the specified resource and splits the value on space boundaries.
	 *
	 * @param  name  the resource name
	 * @param  defaultValue  the default value for the resource
	 */
	public String [] splitResource (String name, String defaultValue) {

		String value = getResource(name, defaultValue);
		if (value == null) {
			return (null);
		}

		ArrayList<String> values_list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(value);
		while (st.hasMoreTokens()) {
			value = st.nextToken();
			values_list.add(value);
		}

		String values[] = new String[values_list.size()];
		values_list.toArray(values);
		return (values);
	}
}
