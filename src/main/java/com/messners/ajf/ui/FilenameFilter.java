package com.messners.ajf.ui;

import java.io.File;
import java.util.HashMap;
import javax.swing.filechooser.FileFilter;


/**
 * This class provides a basic FileFilter implementation.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class FilenameFilter extends FileFilter {

	protected String description   = null;
	protected HashMap<String, String> extensionMap = new HashMap<String, String>();

	/**
	 * Creates a FilenameFilter taht will accept the specified extensions.
	 *
	 * @param  description the description for the FileFileter
	 * @param  extensions  the array of acceptable file extensions
	 */
	public FilenameFilter (String description, String extensions[]) {

		this.description = description;

		if (extensions != null) {

			int numExtensions = extensions.length;
			for (int i = 0; i < numExtensions; i++) {
				if (extensions[i] != null) {
					String e = extensions[i].toLowerCase();
					extensionMap.put(e, e);
				}
			}
		}
	}


	public boolean accept (File f) {

		if (f.isDirectory()) {
			return (true);
		}

		String filename = f.getName();
		int index = filename.lastIndexOf('.');
		if (index < 1) {
			return (false);
		}

		if (index >= filename.length() - 1) {
			return (false);
		}

		String extension = filename.substring(index + 1).toLowerCase();
		return (extensionMap.containsKey(extension));
	}


	public String getDescription() {
		return (description);
	}
}
