package com.messners.ajf.ui;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JPanel;


/**
 * Abstract base class for all preference panels.  This class exists for 
 * convience so that all child classes don't have to implement every 
 * interface in PrefsPanel.  The default layout manager for this panel
 * is the ColumnLayout.
 */
public abstract class DefaultPrefsPanel extends JPanel implements PrefsPanel, UIConstants {

	private static final long serialVersionUID = 1L;

	/**
	 * Construct a DefaultPrefsPanel with the specified name and
	 * resource loader used to load the title and icon properties.
	 *
	 * @param name Unique identifier for this panel.
	 * @param uiLoader Resource loader to load the title and icon
	 * from.  The name for the title property is [name].title, where
	 * [name] is the first parameter to this method (a.k.a. unique
	 * identifier for this panel).  Similarly, the name for the
	 * icon property is [name].icon, where [name] is the same as the
	 * first parameter.
	 */
	protected DefaultPrefsPanel (String name, ResourceLoader uiLoader) {
	   
		this(name, uiLoader.getResource(name + ".title"), 
		      uiLoader.getIconResource(name + ".icon"));
	}

	/**
	 * Construct a DefaultPrefsPanel with the specified name and title.
	 *
	 * @param name Unique identifier for the panel.
	 * @param title Title displayed in the GUI.
	 */
	protected DefaultPrefsPanel (String name, String title, Icon icon) {

		// Set the name of this panel, required for navigation.
		setName(name);
		this.title = title;
		this.icon = icon;

		ColumnLayout layout = new ColumnLayout(0, 0);
		layout.setVgap(VGAP);
		layout.setExpandWidth(true);
		setLayout(layout);
	}

	// From PrefsPanel.
	public void enter () { }

	// From PrefsPanel.
	public Component getComponent () {

		return this;
	}

	// From PrefsPanel.
	public Icon getIcon () {

		return icon;
	}

	// From PrefsPanel.
	public String getTitle () {

		return title;
	}

	// From PrefsPanel.
	public boolean leave () { 
	
		return true;
	}

	/** Icon for the panel. */
	protected Icon icon = null;

	/** Name of the panel. */
	protected String name = null;

	/** Title of the panel. */
	protected String title = null;
}
