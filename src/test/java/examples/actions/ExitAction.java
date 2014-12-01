
package examples.actions;

import com.messners.ajf.ui.AbstractAppAction;

import examples.SampleApp;

import java.awt.event.ActionEvent;


public class ExitAction extends AbstractAppAction {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "exit";
	SampleApp _app = null;

	public ExitAction (SampleApp app) {
		super(NAME);
		_app = app;
	}


	public void actionPerformed (ActionEvent e) {

		/*
		 * Exit the app
		 */
		_app.exit();
	}
}
