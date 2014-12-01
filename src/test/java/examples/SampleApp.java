package examples;

import com.messners.ajf.ui.DefaultGuiApplication;

public class SampleApp extends DefaultGuiApplication {
	
	private static final long serialVersionUID = 1L;

	public SampleApp () {

		/*
		 * Need to do this first to load the application resource file
		 */
		super();


		/*
		 * Display the splash screen and set the message on it.
		 */
		setSplashVisible(true);
		setSplashStatus(getResource("app.building-gui"));

		/*
		 * Load the preferences
		 */
		loadPreferences(".SampleApp");


		/*
		 * Build the application window, the two parameters specifiy
		 * the name of the menu bar and toolbar respectively. Always
		 * do this prior to creating any of your application specific
		 * UI.
		 */
		build("menubar", "toolbar");


		/*
		 * Restore the application preferences
		 */
		restorePreferences();		
		

		/*
		 * Hide the splash screen
		 */
		setSplashVisible(false);
	}


	public void exit () {

		/*
		 * Provide any pre-exit functionality here
		 */
		super.exit();
	}


	public void restorePreferences () {

		/*
		 * The default implementation restores the application's size and
		 * position, if you need to restore other preferences do it here
		 */
		super.restorePreferences();
	}


	public void savePreferences () {

		/*
		 * The default implementation saves the application's size and
		 * position, if you need to save other preferences do it here
		 */
		super.savePreferences();
	}


	public static void main (String[] args) {
		final SampleApp app = new SampleApp();
		app.setVisible(true);
	}
}


