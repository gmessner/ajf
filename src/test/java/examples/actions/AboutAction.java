package examples.actions;

import com.messners.ajf.ui.AbstractAppAction;
import com.messners.ajf.ui.Dialogs;
import com.messners.ajf.ui.RunnableAppAction;

import examples.SampleApp;

import java.awt.event.ActionEvent;


public class AboutAction extends AbstractAppAction implements RunnableAppAction {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "about";
	SampleApp app = null;

	public AboutAction (SampleApp app) {
		super(NAME);
		this.app = app;
	}


	public void actionPerformed (ActionEvent e) {

		
		Object args[] = {
			this.app.getTitle(),
			this.app.getVersion(),
			"" + this.app.getBuildNumber(),
			this.app.getBuildDate(),
			System.getProperty("java.vm.version"),
			System.getProperty("os.name"),
			System.getProperty("os.version"),
			System.getProperty("os.arch")
		};

		Dialogs.about(this.app.getResourceLoader(), this.app, 
			      "about-dialog", args);
	}
}


