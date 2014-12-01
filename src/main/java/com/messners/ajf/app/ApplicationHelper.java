package com.messners.ajf.app;

import com.messners.ajf.app.FilePreferences;
import com.messners.ajf.logging.Logging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;

/**
 * This class provides static helper methods for computing the
 * user application directory and for loading user preferences.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ApplicationHelper {

	/**
	 * Computes the users application directory.  This directory is used to
	 * save information about the application specific to the user. If
	 * directory does not exist it will be created.</p>
	 *
	 * NOTE: The user's home directory is prepended to this directory.
	 *
	 * @param  userAppDir the directory to set the user application directory to
	 */
	public static String getUserApplicationDirectory (String userAppDir) {
		return (getUserApplicationDirectory(userAppDir, true));
	}


	/**
	 * Computes the users application directory.  This directory is used to
	 * save information about the application specific to the user. If
	 * directory does not exist it will be created.
	 *
	 * @param  userAppDir the directory to set the user application directory to
	 * @param  prependHome if true prepends System.getProperty("user.home")
	 */
	public static String getUserApplicationDirectory (String userAppDir, boolean prependHome) {

		/*
		 * Default to the current directory
		 */
		if (userAppDir == null || userAppDir.length() == 0) {
			userAppDir = System.getProperty("user.dir");
		}

		if (prependHome) {

			String userHome = System.getProperty("user.home");
			if (userHome != null) {
				File d = new File(userHome, userAppDir);
				userAppDir = d.getAbsolutePath();
			}
		}
		
		File d = new File(userAppDir);
		if (d.exists()) {

			if (!d.isDirectory()) {
				throw new RuntimeException("Application directory cannot be a regular file.");
			}

		} else {
			d.mkdirs();
		}

		return (d.getAbsolutePath());
	}


	/**
	 * <p>Loads the preferences from a file that is named after the name of the
	 * top level application class and is assumed to be in the
	 * userApplicationDir property.</p>
	 *
	 * @param  app        the Application instance to load the preferences for
	 */
	public static FilePreferences loadPreferences (Application app) {

		String name = app.getClass().getName();
		int index = name.lastIndexOf('.');
		if (index > 0) {
			name = name.substring(index + 1);
		} 

		name = name + ".prefs";
		return (loadPreferences(app, name));
	}


	/**
	 * Loads the preferences from the provided filename.  Filename is assumed
	 * to be in the userApplicationDir property.
	 *
	 * @param  app        the Application instance to load the preferences for
	 * @param  filename   the name of the file in the user application directory
	 * to load the preferences from.
	 */
	public static FilePreferences loadPreferences (Application app, String filename) {

		/*
		 * First try to load the defaults.
		 */
		Class<? extends Application> cls = app.getClass();
		String defaults = cls.getName();
		int index = defaults.lastIndexOf('.');
		if (index > 0) {
			defaults = defaults.substring(index + 1);
		}
	
		defaults += ".app-defaults";
		FilePreferences preferences = new FilePreferences();
		try {

	        InputStream in = cls.getResourceAsStream(defaults);
			if (in != null) {
		        preferences.load(in);
			}

	    } catch (IOException ignore) {
	    }

		
		/*
		 * Now load the user preferences
		 */
		String userAppDir = app.getUserApplicationDirectory();
		File f = new File(userAppDir, filename);
		preferences.setFilename(f.getAbsolutePath());
		try {

	        preferences.load();

	    } catch (IOException ignore) {
	    }

		return (preferences);
	}

	/**
	 * Initializes the java.util.logging package specifically for the
	 * Application. This method read the logging configuration from
	 * the applications app-defaults file and then from the applications
	 * preferences file passing them to 
	 * <code>com.messners.ajf.logging.Logging.initialize()</code> to be used
	 * as the configuration input. 
	 *
	 *
	 * @param app the Application instance to set logging up for
	 * @param dir the directory name to put the log files in, if null
	 * defaults to the userApplicationDirectory/logs.
	 * @see com.messners.ajf.logging.Logging#initialize(InputStream) initialize
	 */
	public static void initializeLogging (Application app, String dir) {

		/*
		 * First read the preferences for the application
		 */
		FilePreferences prefs = loadPreferences(app);

		/*
		 * Setup the filename for the log file if not already set
		 */
		String pattern = prefs.getProperty(
					"java.util.logging.FileHandler.pattern");
		if (pattern == null || pattern.length() == 0) {

			File userDir = new File(app.getUserApplicationDirectory());

			String name = app.getClass().getName();
			int index = name.lastIndexOf('.');
			if (index > 0) {
				name = name.substring(index + 1);
			} 

			File f;
			if (dir == null) {
				f = new File(userDir, "logs");
			} else {

				if (dir.startsWith("null/")) {
					dir = dir.substring(5);
				}

				f = new File(dir);
			}

			f = new File(f, name + "-%g.log");
			pattern = f.getAbsolutePath(); 
			prefs.put("java.util.logging.FileHandler.pattern", pattern);
		}
		

		/*
		 * Now actually initialize the logging using the application
		 * Preferences
		 */
		try {

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			prefs.store(bout, null);

			ByteArrayInputStream bin = 
					new ByteArrayInputStream(bout.toByteArray());
			Logging.initialize(bin);

			bout.close();
			bin.close();

		} catch (IOException ioe) {
		}
	}


	/**
	 * Hide the constructor as this class is not meant to be instanciated.
	 */
	private ApplicationHelper () {
	}
}
