package com.messners.ajf.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;


/**
 * <p>This class can be used to create a basic shell of an application.  To use
 * this class simply extend it and build your application with code
 * similar to the following:</p>
 *
 *<pre>
 *
 * 	// Need to do this first to load the application resource file
 * 	// and set the preferences filename, which in this case the
 * 	// name of the preferences file is ".SampleApp"
 *	super(".SampleApp");
 * 
 * 
 * 	// Display the splash screen and set the message on it.
 * 	setSplashVisible(true);
 *	setSplashStatus(getResource("app.building-gui", "Building UI..."));
 *	
 *	
 *	// Build the application window, the two parameters specifiy
 *	// the name of the menu bar and toolbar respectively. Always
 *	// do this prior to creating any of your application specific
 *	// UI.
 *	build("menubar", "toolbar");
 *
 *
 *	// Restore the application preferences
 *	restorePreferences();
 *
 *
 * 	// Hide the splash screen
 *	setSplashVisible(false);
 *
 *</pre>
 *
 *
 * @author  Greg Messner <greg@messners.com>
 */

public class DefaultGuiApplication extends JFrameApplication {

	private static final long serialVersionUID = 1L;

	public static final String SHOW_TOOLBAR_LABELS = "show-toolbar-labels";

	protected boolean showToolbarLabels;
	protected Splash splash;
	protected MainWindow contentPane;


	/**
	 * Create the DefaultGuiApplication instance and load the
	 * application resource file.
	 */
	public DefaultGuiApplication (Class<?> resourceOwner) {

		super(resourceOwner);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new AppCloser());
	}


	/**
	 * Create the DefaultGuiApplication instance and load the
	 * application resource file.
	 */
	public DefaultGuiApplication () {
		super();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new AppCloser());
	}


	/**
	 * <p>Builds the application window, the two parameters specifiy
	 * the name of the menu bar and toolbar respectively. Always
	 * do this prior to creating any of your application specific
	 * UI.</p>
	 *
	 * When the application window is created the "app.icon" resource
	 * specifies the icon for the application and "app.title" specifies
	 * the string for the title bar.
	 *
	 * @param  menubar_name  the name of the menu bar in the resource file
	 * @param  toolbar_name  the name of the toolbar in the resource file
	 */
	public void build (String menubar_name, String toolbar_name) {

		/*
		 * Put the icon on the application
		 */
		ImageIcon app_icon = (ImageIcon)getIconResource("app.icon");
		if (app_icon == null) {
			app_icon = (ImageIcon)getIconResource("icon");
		}

		if (app_icon != null) {
			setIconImage(app_icon.getImage());
		}

		/*
		 * Put the title on the title bar
		 */
		String title = getResource("app.title");
		if (title == null) {
			title = getResource("title");
		}

		if (title != null) {
			setTitle(title);
		}


		contentPane = new MainWindow(
			getResourceLoader(), menubar_name, toolbar_name);
		setContentPane(contentPane);
		contentPane.setName(getName());
	}


	/**
	 * Gets the container for the JMenuBar and JToolBar components.
	 *
	 * @return the JPanel container for the JMenuBar and JToolBar components
	 */
	public JPanel getMenuToolBar () {
		return (contentPane.getMenuToolBar());
	}


	/**
	 * Returns the MainWindow component.
	 *
	 * @return  the MainWindow for this DefaultGuiApplication
	 */
	public MainWindow getMainWindow () {
		return (contentPane);
	}


	/**
	 * Get the work area for the application.  The work area is the panel
	 * below the toolbar and above the status bar.
	 *
	 * @return  the work area for the application
	 */
	public JComponent getWorkArea () {
		return (contentPane.getWorkArea());
	}


	/**
	 * Set the work area for the application.  The work area is the panel
	 * below the toolbar and above the status bar.
	 *
	 * @param  c the new work area for the application
	 */
	public void setWorkArea (JComponent c) {
		contentPane.setWorkArea(c);
	}


	/**
	 * Gets the component that is displayed as the status bar along the
	 * bottom of the main application window.
	 *
	 * @return  the status bar component
	 */
	public JComponent getStatusBar () {
		return (contentPane.getStatusBar());
	}


	/**
	 * Set the visibility of the splash screen.  If the Splash component
	 * has not been created yet it will be constructed using the image
	 * specified by the "app.splash.icon" resource.
	 *
	 * @param  flag  the new visibility for the splash screen
	 */
	public void setSplashVisible (boolean flag) {


		if (splash != null) {

			splash.setVisible(flag);
			return;

		} else if (!flag) {

			return;
		}

		/*
		 * Display the splash screen
		 */
		ImageIcon splash_icon = (ImageIcon)getIconResource("app.splash.icon");
		splash = new Splash(splash_icon);
		splash.setVisible(true);
	}


	/**
	 * Set the message on the splash screen.
	 *
	 * @param  message  the message for the splash screen
	 */
	public void setSplashStatus (String message) {

		if (splash == null) {
			return;
		}

		splash.setStatus(message);
	}


	/**
	 * Exit the application. This default implementation hides the
	 * application window, saves the preferences and calls System.exit() 
	 * to exit the application.  Override this method if this behavior
	 * is not sufficient.
	 */
	public void exit () {

		/*
		 * If we haven't created the UI just exit
		 */
		if (contentPane == null) {
			System.exit(0);
		}

		setVisible(false);
		savePreferences();
		System.exit(0);
	}


	/**
	 * Set the message in the status bar.
	 *
	 * @param  status  the new message for the status bar
	 */
	public void setStatus (String status) {
		contentPane.setStatus(status);
	}


	/**
	 * Sets the background of the status message.
	 *
	 * @param bg the new status message background color
	 */
	public void setStatusBackground (Color bg) {
		contentPane.setStatusBackground(bg);
	}


	/**
	 * Set the progress bar to either processing or not.
	 *
	 * @param  flag  if true the progress bar will display an
	 * indicator which continuously bounces from left to right,
	 * if false not indicator is displayed
	 */
	public void setProcessing (boolean flag) {
		contentPane.setProcessing(flag);
	}


	/**
	 * Shows/hides the labels on each button on the toolbar.
	 *
	 * @param show  if true, the labels will be shown
	 */
	public void setToolBarLabelsVisible (boolean show) {

		showToolbarLabels = show;
		contentPane.setToolBarLabelsVisible(show);
	}


	/**
	 * Saves the application preferences.  This default implementation
	 * saves the application position and size.  Override this method
	 * if your application has additional needs.
	 */
	public void savePreferences () {

		setAppSize(getSize());
		setAppLocation(getLocation());
		
		getPreferences().put(SHOW_TOOLBAR_LABELS, showToolbarLabels);

		try {

			getPreferences().store();

		} catch (Exception ignore) {
		}
	}
	

	/**
	 * Restore the application preferences.  This default implementation
	 * restores the application position and size.  Override this method
	 * if your application has additional needs.
	 */
	public void restorePreferences () {

		/*
		 * Set the geometry for the application
		 */
		Dimension d = getContentPane().getPreferredSize();
		((JComponent)getContentPane()).setMinimumSize(d);

		d = getAppSize();
		if (d.width > 0) {
			setSize(d);
			setLocation(getAppLocation());
		}

		setToolBarLabelsVisible(
			getPreferences().get(SHOW_TOOLBAR_LABELS, true));
	}


	/**
	 * Monitors for the windowClosing message and calls the exit()
	 * method when received.
	 */
	private final class AppCloser extends WindowAdapter  {

		public void windowClosing (WindowEvent e) {
			exit();
		}
	}
}
