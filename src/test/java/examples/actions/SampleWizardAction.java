package examples.actions;

import com.messners.ajf.ui.AbstractAppAction;
import com.messners.ajf.ui.RunnableAppAction;

import examples.SampleApp;
import examples.wizard.SampleWizard;

import java.awt.event.ActionEvent;


public class SampleWizardAction extends AbstractAppAction
					implements RunnableAppAction {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "sample-wizard";
	SampleApp _app = null;

	public SampleWizardAction (SampleApp app) {
		super(NAME);
		_app = app;
	}


	SampleWizard wizard = null;

	public void actionPerformed (ActionEvent e) {

		if (wizard == null) {
			wizard = new SampleWizard();
		}

		wizard.showDialog(_app);
	}
}


