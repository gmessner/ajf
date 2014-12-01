package com.messners.ajf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * This class defines miscellaneous utility methods that are used throughout
 * the <em>com.messners.ajf</em> packages.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class Misc {

	private static String systemTmpDir = null;


	/**
	 * This class is not meant to be instantiated.
	 */
	private Misc () {
	}


	/**
	 * Native line separator ("\n" on Unix).
	 */
	protected static String lineSeparator = getProperty("line.separator");


	/**
	 * Get the native line separator ("\n" on Unix).
	 */
	public static String getLineSeparator () {
		return (lineSeparator);
	}


	/**
	 * Native path separator (":" on Unix, ";" on Windows).
	 */
	protected static String pathSeparator = getProperty("path.separator");


	/**
	 * Get the native path separator (":" on Unix, ";" on Windows).
	 */
	public static String getPathSeparator () {
		return (pathSeparator);
	}


	/**
	 * Storage for the is_windows flag, indicating whether we are running
	 * on a Windows OS.
	 */
	protected static boolean isWindows = true;
	static {
		String os = getProperty("os.name");
		if (os != null) {
			os = os.toLowerCase();
			if (os.indexOf("windows") < 0) {
				isWindows = false;
			}
		}
	}


	/**
	 * Return the the is_windows flag, indicating whether we are running
	 * on a Windows OS.
	 */
	public static boolean isWindows () {
		return (isWindows);
	}


	/**
	 * Create a unique file in the specified directory based on
	 * <code>name</code>.
	 *
	 * @param  directory  the directory for the unique file
	 * @param  name	   the original filename for the unique file.
	 * @exception IOException when an exception occurs.
	 */
	public static final synchronized String createUniqueFile (String directory, String name) throws IOException {

		File f = getUniqueFile(directory, name, 0);
		FileOutputStream fos = new FileOutputStream(f);
		fos.close();
		fos = null;
		return (f.getPath());
	}


	/**
	 * Generate a unique filename in the specified directory.
	 *
	 * @param  directory  the directory for the unique file
	 * @param  name	   the original filename for the unique file.
	 */
	public static final synchronized String getUniqueFilename (String directory, String name) {

		File f = getUniqueFile(directory, name, 0);
		return (f.getPath());
	}


	/**
	 * Generate a <code>File</code> object that points to a unique
	 * filename in the specified directory.
	 *
	 * @param  directory  the directory for the unique file
	 * @param  name	   the original filename for the unique file.
	 */
	public static final synchronized File getUniqueFile (
		String directory, String name) {
	
		return (getUniqueFile(directory, name, 0));
	}


	/**
	 * This method does the actual work of generating the unique filename.
	 */
	private static synchronized File getUniqueFile (String directory, String name, int times) {

		if (times == 0) {
			if (name == null) {
				return (null);
			}

			name = getBasename(name);
			File f = new File(directory, name);
			if (!f.exists()) {
				return (f);
			}
		}

		times++;

		String tmpname;
		int index = name.lastIndexOf(".");
		if (index >= 0) {
			tmpname = name.substring(0, index) + times + name.substring(index);
		} else {
			tmpname = name + times;
		}
				
		File f = new File(directory, tmpname);
		if (!f.exists())
			return (f);
			
		return (getUniqueFile(directory, name, times));	
	}


	/**
	 * Gets the temp directory for the OS.
	 */
	public static String getSystemTmpDirectory () {

		if (systemTmpDir != null) {
			return (systemTmpDir);
		}
		
		String dir = getProperty("java.io.tmpdir");
		if (dir == null) {
			dir = getProperty("TMP");
		}
		if (dir == null) {
			dir = getProperty("TEMP");
		}
		if (dir == null) {
			dir = getProperty("TMPDIR");
		}

		if (dir != null) {
			systemTmpDir = dir;
			return (dir);
		}

		
		if (!isWindows()) {
			systemTmpDir = "/tmp";
			return (systemTmpDir);
		}

		String drive = getProperty("SystemDrive");
		if (drive != null) {
			char c = drive.charAt(drive.length() - 1);
			if (c != '\\' && c != '/') {
				systemTmpDir = drive + "\\TEMP";
			} else {
				systemTmpDir = drive + "TEMP";
			}

			return (systemTmpDir);
		} 

		systemTmpDir = "\\TEMP";
		return (systemTmpDir);
	}


	/**
	 * Get the trailing part of a filename.
	 */
	public static String getBasename (String filename) {

		if (filename == null) {
			return (null);
		}

		/*
		 * Look for a slash and if present return everything after it
		 */
		int index = filename.lastIndexOf('/');
		if (index == -1) {
			index = filename.lastIndexOf('\\');
		} else {
			int tmp_index = 
				filename.substring(index + 1).lastIndexOf('\\');
			if (tmp_index != -1) {
				index = tmp_index;
			}
		}

		if (index != -1) {
			return (filename.substring(index + 1));
		}

		return (filename);
	}


	/**
	 * Get the named String property using System.getProperty().
	 * This method captures SecurityExceptions and returns null if
	 * one is thrown.
	 */
	public static String getProperty (String key) {

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

		return (System.getProperty(key));
	}


	/**
	 * Gets a copy of an Object.  This uses serialization so the
	 * object to copy must be Serializable or an exception will
	 * be thrown.
	 *
	 * @param  obj  the Object to copy.
	 * @return a deep copy of the object
	 */
	static public Object copy (Object obj) throws Exception {

        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);

            // serialize and pass the object
            oos.writeObject(obj);
            oos.flush();
            ByteArrayInputStream bin =
                new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bin);

            // return the new object
            return (ois.readObject());

        } finally {

            oos.close();
            ois.close();
        }
   }
}
