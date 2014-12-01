
package examples.actions;

import com.messners.ajf.ui.AbstractAppAction;

import examples.SampleApp;

import java.awt.event.ActionEvent;


public class ComboAction extends AbstractAppAction {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "combo";
	SampleApp _app = null;

	public ComboAction (SampleApp app) {

		super(NAME);
		_app = app;
	}


	public void actionPerformed (ActionEvent e) {
	}
}
