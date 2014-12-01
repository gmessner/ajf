package examples.actions;

import com.messners.ajf.ui.AbstractAppAction;
import com.messners.ajf.ui.RunnableAppAction;

import examples.SampleApp;
import examples.dialog.SamplePrefsDialog;

import java.awt.event.ActionEvent;


public class SamplePrefsDialogAction extends AbstractAppAction
					implements RunnableAppAction {

	private static final long serialVersionUID = 1L;

	/*
	 * Component identifier in the application framework.
	 */
	public static final String NAME = "sample-prefs-dialog";

	/*
	 * Reference to the parent application.
	 */
	private SampleApp app = null;

	private SamplePrefsDialog dialog = null;


	/**
	 * Simple constructor.
	 */
	public SamplePrefsDialogAction (SampleApp app) {
		super(NAME);
		this.app = app;
	}

	// From AbstractAppAction.
	public void actionPerformed (ActionEvent e) {

		if (dialog == null) {
			dialog = new SamplePrefsDialog();
		}

		dialog.popup(app);
	}
}
