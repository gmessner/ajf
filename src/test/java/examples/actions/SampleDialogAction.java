package examples.actions;

import com.messners.ajf.ui.AbstractAppAction;
import com.messners.ajf.ui.RunnableAppAction;

import examples.SampleApp;
import examples.dialog.SampleDialog;

import java.awt.event.ActionEvent;


public class SampleDialogAction extends AbstractAppAction
					implements RunnableAppAction {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "sample-dialog";
	SampleApp app = null;

	public SampleDialogAction (SampleApp app) {
		super(NAME);
		this.app = app;
	}


	SampleDialog dialog = null;

	public void actionPerformed (ActionEvent e) {

		if (dialog == null) {
			dialog = new SampleDialog();
		}

		dialog.popup(app);
	}
}


