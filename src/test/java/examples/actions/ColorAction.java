
package examples.actions;

import com.messners.ajf.ui.AbstractAppAction;

import examples.SampleApp;

import java.awt.event.ActionEvent;


public class ColorAction extends AbstractAppAction {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "color";
	SampleApp _app = null;

	public ColorAction (SampleApp app) {

		super(NAME);
		_app = app;
	}


	public void actionPerformed (ActionEvent e) {
	}
}
