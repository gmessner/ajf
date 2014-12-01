package com.messners.ajf.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.messners.ajf.app.Application;
import com.messners.ajf.app.ApplicationHelper;
import com.messners.ajf.app.FilePreferences;
import com.messners.ajf.app.Preferences;
import com.messners.ajf.logging.Logging;


/**
 * This abstract class implements the GuiApplication interface and is intended
 * to be used as the base class for an application.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public abstract class JFrameApplication extends JFrame implements GuiApplication {

	private static final long serialVersionUID = 1L;
	protected String userAppDir;
	protected FilePreferences preferences;
	protected ActionManager actionMgr;
	protected ResourceLoader loader;

	protected Hashtable<String, Object> attributes;
	protected Hashtable<String, String> parameters;

	protected Cursor defaultCursor;
	protected Cursor waitCursor;

	protected Logger logger;


	/**
	 * Exit the application. This default implementation hides the
	 * application window and calls System.exit() to exit the application.
	 * Override this method if this behavior is not sufficient.
	 */
	public void exit () {

		setVisible(false);
		System.exit(0);
	}


	/**
	 * Gets the <code>ResourceLoader</code> associated with this instance.
	 *
	 * @return  the <code>ResourceLoader</code> associated with this instance
	 */
	public ResourceLoader getResourceLoader () {
		return (loader);
	}


	/**
	 * Gets the app's current ResourceBundle. Resources are static values
	 * used to construct the application.
	 */
	public ResourceBundle getResourceBundle () {
		return (loader.getResourceBundle());
	}

	
	/**
	 * Get the application's preferences. Preferences are used to keep
	 * runtime configurations that change as the application is used.
	 */
	public Preferences getPreferences () {
		return (preferences);
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
	 * Get the named resource (property), returning null if it's not defined.
	 *
	 * @param name  the resource name
	 *
	 * @return the resource value as a String, or null if not found
	 */
	public String getResource (String name) {

		return (loader.getResource(name));
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

		return (loader.getResource(name, defaultVal));
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
		return (loader.getResourceMessage(name, name, args));
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

		return (loader.getResourceMessage(name, defaultMsg, args));
	}


	/**
	 * Get the specified resource and splits the value on space boundaries.
	 *
	 * @param  name  the resource name
	 */
	public String [] splitResource (String name) {

		return (loader.splitResource(name, null));
	}


	/**
	 * Get the specified resource and splits the value on space boundaries.
	 *
	 * @param  name  the resource name
	 * @param  defaultValue  the default value for the resource
	 */
	public String [] splitResource (String name, String defaultValue) {

		return (loader.splitResource(name, defaultValue));
	}


	/**
	 * Get an icon resource, returning null if it's not defined.
	 */
	public Icon getIconResource (String name) {
		return (loader.getIconResource(name));
	}


	/**
	 * Load the resource specified by the resource name.
	 *
	 * @param name the resource name
	 * @return  a JComponent instance of the specified resource
	 */
	public JComponent load (String name) {
		return (loader.load(name));
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
	 * Registers an action with the application.
	 *
	 * @param action the action name
	 */
	public void addAction (AppAction action) {
		actionMgr.addAction(action);
	}


	/**
	 * Returns a named action.
	 *
	 * @param actionName  the action name to get
	 */
	public AppAction getAction (String actionName) {
		return (actionMgr.getAction(actionName));
	}


	/**
	 * Sets the enabled state of an AppAction.
	 */
	public void setActionEnabled (AppAction action, boolean flag) {

		if (action != null) {
			action.setEnabled(flag);
		}
	}


	/**
	 * Sets the enabled state of an AppAction.
	 */
	public void setActionEnabled (String action, boolean flag) {

		setActionEnabled(getAction(action), flag);
	}


	/**
	 * Set the status display to the specified string.
	 *
	 * @param  status  the new status string
	 */
	public void setStatus (String status) {
	}


	/**
	 * Set the status display to the named resource message.
	 *
	 * @param  name  the name of the resource message
	 * @param  args  the argument objects for the message
	 */
	public void setStatus (String name, Object args[]) {

		String status = getResourceMessage(name, args);
		setStatus(status);
	}


	/**
	 * Set the processing state of the application.
	 *
	 * @param  flag  true if the state is processing
	 */
	public void setProcessing (boolean flag) {
	}


	/**
	 * Sets the cursor on this instance.
	 *
	 * @param  cursor  the new cursor for this application.
	 */
	public void setCursor (Cursor cursor) {

		if (cursor != null) {
			super.setCursor(cursor);
		}
	}


	/**
	 * Gets the default cursor for this instance.
	 *
	 * @return the default cursor for this instance
	 */
	public Cursor getDefaultCursor () {
		return (defaultCursor);
	}	


	/**
	 * Gets the wait cursor for this instance. Normally the cursor
	 * defined by Cursor.WAIT_CURSOR.
	 *
	 * @return the wait cursor for this instance
	 */
	public Cursor getWaitCursor () {
		return (waitCursor);
	}	


	/**
	 * Sets the cursor to the default cursor.
	 */
	public void setCursorToDefault () {
		super.setCursor(defaultCursor);
	}


	/**
	 * Sets the cursor to the wait cursor.
	 */
	public void setCursorToWait () {
		super.setCursor(waitCursor);
	}


	/**
	 * Get the RecentFilesMenu specified by name.
	 *
	 * @param  name  the name of the RecentFilesMenu to get
	 * @return  the RecentFilesMenu associated with name
	 */
	public RecentFilesMenu getRecentFileMenu (String name) {
		return (RecentFilesMenu.find(name));
	}


	/**
	 * Gets the saved location for the application.
	 *
	 * @return  the saved location of the application
	 */
	public Point getAppLocation () {
		return (new Point(getAppX(), getAppY()));
	}


	/**
	 * Sets the saved location for the application.
	 *
	 * @param  location  the new saved location for the application
	 */
	public void setAppLocation (Point location) {

		if (location != null) {
			setAppX(location.x);
			setAppY(location.y);
		}
	}


	/**
	 * Gets the saved dimensions for the application.
	 *
	 * @return  the saved dimensions location of the application
	 */
	public Dimension getAppSize () {
		return (new Dimension(getAppWidth(), getAppHeight()));
	}


	/**
	 * Sets the saved dimension for the application.
	 *
	 * @param  size the new saved dimension for the application
	 */
	public void setAppSize (Dimension size) {

		if (size != null) {
			setAppWidth(size.width);
			setAppHeight(size.height);
		}
	}


	/**
	 * Gets the saved X location for the application.
	 *
	 * @return  the saved X location of the application
	 */
	public int getAppX () {
		return (getPreferences().get("x", 0));
	}


	/**
	 * Sets the saved X location for the application.
	 *
	 * @param  x  the new saved X location for the application
	 */
	public void setAppX (int x) {
		getPreferences().put("x", x);
	}


	/**
	 * Gets the saved Y location for the application.
	 *
	 * @return  the saved Y location of the application
	 */
	public int getAppY () {
		return (getPreferences().get("y", 0));
	}


	/**
	 * Sets the saved Y location for the application.
	 *
	 * @param  y  the new saved Y location for the application
	 */
	public void setAppY (int y) {
		getPreferences().put("y", y);
	}


	/**
	 * Gets the saved width for the application.
	 *
	 * @return  the saved width of the application
	 */
	public int getAppWidth () {
		return (getPreferences().get("width", 100));
	}


	/**
	 * Sets the saved width for the application.
	 *
	 * @param  width  the new saved width for the application
	 */
	public void setAppWidth (int width) {
		getPreferences().put("width", width);
	}


	/**
	 * Gets the saved height for the application.
	 *
	 * @return  the saved height of the application
	 */
	public int getAppHeight () {
		return (getPreferences().get("height", 100));
	}


	/**
	 * Sets the saved height for the application.
	 *
	 * @param  height  the new saved height for the application
	 */
	public void setAppHeight (int height) {
		getPreferences().put("height", height);
	}


	public boolean isFocusable () {
		return (true);
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

		this.userAppDir = ApplicationHelper.getUserApplicationDirectory(
								userAppDir, true);
	}


	/**
	 * Sets the users application directory.  This directory is used to
	 * save information about the application specific to the user. If
	 * directory does not exist it will be created.
	 *
	 * @param  userAppDir the directory to set the user application directory to
	 * @param  prependHome if true prepends System.getProperty("user.home")
	 */
	public void setUserApplicationDirectory (
				String userAppDir, boolean prependHome) {

		this.userAppDir = ApplicationHelper.getUserApplicationDirectory(
								userAppDir, prependHome);
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


	public JFrameApplication () {

		super();
		init(getClass());
	}


	public JFrameApplication (Class<?> resourceOwner) {
		super();
		init(resourceOwner);
	}


	private void init (Class<?> resourceOwner) {

		parameters  = new Hashtable<String, String>();
		attributes  = new Hashtable<String, Object>();
		actionMgr   = new ActionManager(this);
		loader      = new ResourceLoader(resourceOwner, actionMgr);

		waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		defaultCursor = getCursor();

		preferences = new FilePreferences();
		addKeyListener(actionMgr);

		/*
		 * Default the user application directory to the current directory
		 */
		userAppDir = System.getProperty("user.dir");
	}
}
