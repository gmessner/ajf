
package examples.actions;

import com.messners.ajf.ui.AbstractAppAction;

import examples.SampleApp;

import java.awt.event.ActionEvent;


public class HorizontalAlignmentAction extends AbstractAppAction {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "horizontal-alignment";
	SampleApp _app = null;

	public HorizontalAlignmentAction (SampleApp app) {
		super(NAME);
		_app = app;
	}


	public void actionPerformed (ActionEvent e) {
	}
}
