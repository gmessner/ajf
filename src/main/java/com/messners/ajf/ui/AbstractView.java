package com.messners.ajf.ui;

import java.util.HashMap;

import javax.swing.Icon;

import com.messners.ajf.app.Preferences;


/**
 * This class provides an abstract inplementation of View.  Implementations
 * must implement the getComponent() method to complete this abstract class.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public abstract class AbstractView implements View {

	protected static HashMap<Class<?>, ResourceLoader> resourceLoaders = new HashMap<Class<?>, ResourceLoader>();
	
	protected Icon icon;
	protected String name;
	protected Icon titleIcon;
	protected String title;


	/**
	 * Protected default no-args constructor.
	 */
	protected AbstractView () {
	}


	/**
	 * Creates a AbstractView of the specified icon and name.
	 */
	public AbstractView (Icon icon, String name) {

		this.icon = icon;
		this.name = name;
		title = name;
		titleIcon = icon;
	}


	/**
	 * Creates a AbstractView of the specified icon and name.
	 */
	public AbstractView (Icon icon, String name, Icon titleIcon, String title) {

		this.icon = icon;
		this.name = name;
		this.title = title;
		this.titleIcon = titleIcon;
	}


	/**
	 * Configures (loads) the icon, name, titleIcon, and title properties 
	 * using a <code>ResourceLoader</code> retrieved based on the class.
	 */
	public void configure () {
		configure(null);
	}


	/**
	 * Configures (loads) the icon, name, titleIcon, and title properties 
	 * using a <code>ResourceLoader</code> retrieved based on the class.
	 *
	 * @param  name    the resource name to load the resources for, this
	 * is the resource name prefix (may be null for no prefix)
	 */
	public void configure (String name) {

		ResourceLoader loader = getResourceLoader();
		configure(loader, name);
	}


	/**
	 * Configures (loads) the icon, name, titleIcon, and title properties 
	 * using the provided <code>ResourceLoader</code>.
	 *
	 * @param  loader  the ResourceLoader to load the properties from
	 * @param  name    the resource name to load the resources for
	 */
	public void configure (ResourceLoader loader, String name) {

		if (name == null || name.length() == 0) {
			name = "";
		} else {
			name += ".";
		}

		String viewName = loader.getResource(name + "name");
		if (viewName == null) {
			throw new RuntimeException("View name cannot be null");
		}

		setName(viewName);
		setIcon(loader.getIconResource(name + "icon"));
		setTitle(loader.getResource(name + "title"));
		setTitleIcon(loader.getIconResource(name + "title-icon"));
	}


	/**
	 * Gets the cached ResourceLoader for this Class.
	 */
	public ResourceLoader getResourceLoader () {

		Class<?> cls = getClass();
		ResourceLoader loader = resourceLoaders.get(cls);
		if (loader != null) {
			return (loader);
		}

		loader = new ResourceLoader(cls);
		resourceLoaders.put(cls, loader);
		return (loader);
	}


	/**
	 * Gets the title to display for this View instance.
	 *
	 * @return  the title to display for this instance
	 */
	public String getTitle () {
		return (title);
	}


	/**
	 * Sets the title to display for this View instance.
	 *
	 * @param title the title to display for this instance
	 */
	public void setTitle (String title) {
		this.title = title;
	}


	/**
	 * Gets the Icon to display with the title for this View instance.
	 *
	 * @return  the title Icon to display for this instance
	 */
	public Icon getTitleIcon () {
		return (titleIcon);
	}


	/**
	 * Sets the Icon to display with the title for this View instance.
	 *
	 * @param icon the title Icon to display for this instance
	 */
	public void setTitleIcon (Icon icon) {
		titleIcon = icon;
	}


	/**
	 * Gets the name to display in the tree for this View instance.
	 *
	 * @return  the name to display for this instance
	 */
	public String getName () {
		return (name);
	}


	/**
	 * Sets the name to display in the tree for this View instance.
	 *
	 * @param  name  the name to display for this instance
	 */
	public void setName (String name) {
		this.name = name;
	}


	/**
	 * Gets the Icon to display int the tree for this View instance.
	 *
	 * @return  the Icon to display for this instance
	 */
	public Icon getIcon () {
		return (icon);
	}


	/**
	 * Sets the Icon to display int the tree for this View instance.
	 *
	 * @param  icon  the Icon to display for this instance
	 */
	public void setIcon (Icon icon) {
		this.icon = icon;
	}


	/**
	 * Restore this View instance preferences.
	 *
	 * @param prefs the Preferences instance to restore from
	 */
	public void restorePreferences (Preferences prefs, String prefix) {
	}


	/**
	 * Saves this View instance preferences.
	 *
	 * @param prefs the Preferences instance to save to
	 */
	public void savePreferences (Preferences prefs, String prefix) {
	}


	/**
	 * Disposes of resources when the instance is removed from a container.
	 * This implementation does nothing and should be overridden if the
	 * implementation class needs to free resources.
	 */
	public void dispose () {
	}
}
