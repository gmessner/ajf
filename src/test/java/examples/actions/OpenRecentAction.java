
package examples.actions;

import com.messners.ajf.ui.AbstractAppAction;

import examples.SampleApp;

import java.awt.event.ActionEvent;


public class OpenRecentAction extends AbstractAppAction {
	
	private static final long serialVersionUID = 1L;

	SampleApp _app = null;

	public OpenRecentAction (SampleApp app) {
		super();
		_app = app;
	}


	public void actionPerformed (ActionEvent e) {
	}
}
