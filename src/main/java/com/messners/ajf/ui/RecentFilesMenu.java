package com.messners.ajf.ui;

import javax.swing.JMenu;

import java.util.Hashtable;
import java.util.Vector;
import java.io.File;

import com.messners.ajf.app.Preferences;


/**
 * This class creates and manages recent files menus.
 *
 * @author  Greg Messner <greg@messners.com>
 */

public class RecentFilesMenu {

	protected String  _name = null;
	protected AppAction _action = null;
	protected Preferences _prefs;

	protected Vector<JMenu> _menus = new Vector<JMenu>();
	protected Vector<String> _files = new Vector<String>();
	protected Vector<String> _descs = new Vector<String>();

	protected static Hashtable<String, RecentFilesMenu> _map = new Hashtable<String, RecentFilesMenu>();



	public static RecentFilesMenu add (
		String name, AppAction action, Preferences prefs, JMenu menu) {

		if (name == null || menu == null || prefs == null) {
			return (null);
		}


		/*
		 * See if we already have a RecentFilesMenu for the name, 
		 * if not create one for it
		 */
		RecentFilesMenu rfm = _map.get(name);
		if (rfm == null) {
			rfm = new RecentFilesMenu(name, action, prefs);
			_map.put(name, rfm);
		}


		/*
		 * Add the JMenu to the RecentFilesMenu instance and return it
		 */
		rfm.addMenu(menu);
		return (rfm);
	}



	public static String [] getFilenames (String name) {

		RecentFilesMenu rfm = find(name);
		if (rfm != null) {
			return (rfm.getFilenameStrings());
		}

		return (null);
	}


	public static String [] getDescriptions (String name) {

		RecentFilesMenu rfm = find(name);
		if (rfm != null) {
			return (rfm.getDescriptionStrings());
		}

		return (null);
	}



	/**
	 * Adds a filename to the named "recent files" option menu.
	 * 
	 * @param filename The filename to add
	 */
	public static void addRecentFile (String name, String filename) {

		addRecentFile(name, filename, "");
	}



	public static void addRecentFile (
			String name, String filename, String desc) {

		if (name == null || filename == null) {
			return;
		}

		if (desc == null) {
			desc = "";
		}

		RecentFilesMenu rfm = find(name);
		if (rfm != null) {
			rfm.addFile(filename, desc);
			rfm.rebuild();
		}
	}
	

	/**
	 * Removes a filename from the "recent files" option menu.
	 * 
	 * @param filename The filename to remove
	 */
	public static void removeRecentFile (String name, String filename) {

		if (name == null || filename == null) {
			return;
		}

		RecentFilesMenu rfm = find(name);
		if (rfm != null && rfm.removeFile(filename)) {
			rfm.rebuild();
		}
	}


	/**
	 * Clears the recent files menu and vector.
	 */
	public static void clearRecentFiles (String name) {

		if (name == null) {
			return;
		}

		RecentFilesMenu rfm = find(name);
		if (rfm != null && rfm.clear()) {
			rfm.rebuild();
		}
	}



	public static void remove (String name) {
		_map.remove(name);
	}


	public static RecentFilesMenu find (String name) {
		return _map.get(name);
	}

		
	protected RecentFilesMenu (
					String name, AppAction action, Preferences prefs) {

		_name = name;
		_action = action;
		_prefs = prefs;

		int max_recent = _prefs.get(_name + ".max-count", 8);

		for (int i = 0; i < max_recent; i++) {

			String filename = _prefs.get(
				_name + ".filename." + (i + 1), null);
			if (filename == null) {
				break;
			}

			if (filename.length() > 0 &&
					!_files.contains(filename)) {
				
				String description = _prefs.get(
					_name + ".description." + (i + 1), null);
				_files.addElement(filename);
				_descs.addElement(description);
			}
		}
	}



	public String getName () {
		return (_name);
	}


	public AppAction getAction () {
		return (_action);
	}


	public Vector<String> getFilenames () {
		return (_files);
	}

	public String [] getFilenameStrings () {

		int size = _files.size();
		String filenames[] = new String[size];
		_files.copyInto(filenames);
		return (filenames);
	}


	public Vector<String> getDescriptions () {
		return (_descs);
	}

	public String [] getDescriptionStrings () {

		int size = _descs.size();
		String descriptions[] = new String[size];
		_descs.copyInto(descriptions);
		return (descriptions);
	}


	public Vector<JMenu> getMenus () {
		return (_menus);
	}


	public void addMenu (JMenu menu) {
		_menus.addElement(menu);
	}


	public void removeMenu (JMenu menu) {
		_menus.removeElement(menu);
	}


	/**
	 * Adds a filename to the "recent files" option menu.
	 * 
	 * @param filename the filename to add
	 */
	public void addFile (String filename) {
		addFile(filename, "");
	}


	public void addFile (String filename, String desc) {

		if (filename == null) {
			return;
		}

		if (desc == null) {
			desc = "";
		}

		/*
		 * Remove items from vector which differ only in casing
		 */
		String tempName;
		File f1 = new File(filename);

		for (int i = 0; i < _files.size(); i++) {
			tempName = _files.elementAt(i);
			if (f1.equals(new File(tempName))) {
				_files.removeElementAt(i);
				_descs.removeElementAt(i);
			}
		}


		_files.insertElementAt(filename, 0);
		_descs.insertElementAt(desc, 0);

		/*
		 * Discard the oldest entry if our list is full
		 */
		int max_recent = _prefs.get(_name + ".max-count", 8);
		if (_files.size() > max_recent) {
			_files.setSize(max_recent);
			_descs.setSize(max_recent);
		}
		
		updatePreferences();
	}
	

	/**
	 * Removes a filename from the "recent files" option menu.
	 * 
	 * @param filename the filename to remove
	 */
	public boolean removeFile (String filename) {

		if (filename == null) {
			return (false);
		}

		int index = _files.indexOf(filename);
		if (index >= 0) {
			_files.removeElementAt(index);
			_descs.removeElementAt(index);
			updatePreferences();
			return (true);
		}

		return (false);
	}


	/**
	 * Clears the recent files menu and vector.
	 */
	public boolean clear () {

		if (_files.size() < 1) {
			return (false);
		}

		_files.removeAllElements();
		_descs.removeAllElements();

		updatePreferences();
		return (true);
	}

		

	/**
	 * Update preferences hashtable
	 */
	public void updatePreferences () {

		String filename;
		String desc;
		int max_recent = _prefs.get(_name + ".max-count", 8);

		for (int i = 0; i < 100; i++) {
			if (i < _files.size()) {
				filename = _files.elementAt(i);
				desc	 = _descs.elementAt(i);
				if (desc == null) {
					desc = "";
				}
			} else if (i < max_recent) {
				filename = "";
				desc = "";
			} else {
				Object o = _prefs.remove(
					_name + ".filename." + (i + 1));
				if (o == null) {
					break;
				}

				_prefs.remove(_name + ".description." + (i + 1));
				continue;
			}

			_prefs.put(_name + ".filename." + (i + 1), filename);
			_prefs.put(_name + ".description." + (i + 1), desc);
		}
	}



	public static void rebuild (String name) {

	    RecentFilesMenu rfm = RecentFilesMenu.find(name);
	    if (rfm != null) {
	        rfm.rebuild();
	    }
	}


	public void rebuild () {

	    Vector<JMenu> menus = getMenus();
	    int num_menus = menus.size();
	    for (int i = 0; i < num_menus; i++) {
	        JMenu menu = menus.elementAt(i);
	        rebuild(menu);
	    }
	}


	public void rebuild (JMenu menu) {

	    if (menu == null) {
	        return;
	    }

	    menu.removeAll();
	    Vector<String> files = getFilenames();

	    AppAction a = getAction();
	    for (int i = 0; i < files.size(); i++) {

	        String filename = files.elementAt(i);
	        if (filename != null) {

	            MenuItemExt mi = new MenuItemExt(
	                (i+1) + " " + filename, null);
	            if (a == null) {
	                mi.setEnabled(false);
	            } else {
	                a.addPropertyChangeListener(mi);
	                a.componentAdded(mi);
	                mi.addActionListener(a);
	                mi.setMnemonic(i + '1');
	                mi.setEnabled(a.isEnabled());
	            }

	            menu.add(mi);
	        }
	    }
	}
}
