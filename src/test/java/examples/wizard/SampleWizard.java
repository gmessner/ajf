package examples.wizard;

import com.messners.ajf.ui.*;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JPanel;


public class SampleWizard extends WizardManager {
	
	private static final long serialVersionUID = 1L;

	public final String NAME = "sample-wizard";
	protected ResourceLoader _ui_loader;

	public SampleWizard () {

		super();
		_ui_loader = new ResourceLoader(this.getClass());

		setHelpButtonVisible(true);
		setLastButtonVisible(true);

		setTitle(_ui_loader.getResource(NAME + ".title"));

		Icon bmp = _ui_loader.getIconResource(NAME + ".bitmap");
		if (bmp != null) {
        		setBitmapIcon(bmp);
		        setBitmapVisible(true);
		}

		addPanel(new OverviewPanel());
		addPanel(new ConfirmationPanel());
	}



	class OverviewPanel extends JPanel implements WizardPanel {
		
		private static final long serialVersionUID = 1L;

		static final String _name = "overview";

		OverviewPanel () {

			setName(_name);
			ColumnLayout cl = new ColumnLayout();
			cl.setExpandWidth(true);
			setLayout(cl);
			add(_ui_loader.load("overview"));
		}


		public boolean leave (int direction) {
			return (true);
		}


		public void enter (int direction) {
		}


		public Component getComponent () {
			return ((Component)this);
		}
	}


	class ConfirmationPanel extends JPanel implements WizardPanel, Helpable {
		
		private static final long serialVersionUID = 1L;

		String _name = "confirmation";

		ConfirmationPanel () {

			setName(_name);
		}


		public boolean leave (int direction) {
			return (true);
		}


		public void enter (int direction) {
		}


		public Component getComponent () {
			return ((Component)this);
		}


		public void help (Object help_on) {
			System.err.println("HELP ME");
		}
	}
}
