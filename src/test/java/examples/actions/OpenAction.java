
package examples.actions;

import com.messners.ajf.ui.AbstractAppAction;

import examples.SampleApp;

import java.awt.event.ActionEvent;


public class OpenAction extends AbstractAppAction {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "open";
	SampleApp _app = null;

	public OpenAction (SampleApp app) {
		super(NAME);
		_app = app;
	}


	public void actionPerformed (ActionEvent e) {
	}
}
