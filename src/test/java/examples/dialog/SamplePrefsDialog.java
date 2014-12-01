package examples.dialog;

import com.messners.ajf.ui.Helpable;
import com.messners.ajf.ui.PrefsForm;
import com.messners.ajf.ui.PrefsPanel;
import com.messners.ajf.ui.ResourceLoader;
import com.messners.ajf.ui.TitledPanelBorder;

import java.awt.Component;
import javax.swing.Icon;

import javax.swing.JPanel;


public class SamplePrefsDialog extends PrefsForm {

	private static final long serialVersionUID = 1L;

	private final static String NAME = "sample-prefs-dialog";

	private static ResourceLoader uiLoader = getResourceLoader();

	public SamplePrefsDialog () {

		/*
		 * Set the name of the component for identification in the 
		 * application framework.
		 */
		setName(NAME);
		
		/*
		 * Create the Appearance preference panel as a child of 
		 * the root node.
		 */
		DummyPrefsPanel apprPnl = new DummyPrefsPanel("apprPnl");

		/*
		 * Create the Navigator preference panel as a child of
		 * the root node.
		 */
		DummyPrefsPanel navPnl = new DummyPrefsPanel("navPnl");

		/*
		 * Create the Composer preference panel as a child of the
		 * root node.
		 */
		DummyPrefsPanel compPnl = new DummyPrefsPanel("compPnl");

		/*
		 * Create the History preference panel as a child of the
		 * Navigator node.
		 */
		DummyPrefsPanel histPnl = new DummyPrefsPanel("histPnl");

		/*
	 	 * Layout the dialog.
		 */
		addPanel(apprPnl);
		addPanel(navPnl);
		addPanel(compPnl);
		addPanel(navPnl, histPnl);
	}

	// From DialogForm.
	public boolean apply() {
		return true;
	}

	// From DialogForm.
	public void reset() {
	}


	/**
	 * Gets an instance of the resource loader for this class.
	 */
	private static ResourceLoader getResourceLoader() {
		ResourceLoader loader = null;
		try {
			String clsNm = "examples.dialog.SamplePrefsDialog";
			loader = new ResourceLoader(Class.forName(clsNm));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return loader;
	}

	/**
	 * Dummy class used to demonstrate preferences panel.
	 */
	private class DummyPrefsPanel extends JPanel 
				      implements PrefsPanel, Helpable {

		private static final long serialVersionUID = 1L;
		private String name;
		private String title;
		private Icon icon;

		/**
		 * Constructs a dummy preferences panel with the specified
		 * display name and path in the tree.
		 * @param name Unique identifier for the panel.
		 */
		public DummyPrefsPanel(String name) {

			super();

			// Set the unique identifier for the panel.
			super.setName(name);
			this.name = name;

			// Set the display name of the panel.
			this.title = uiLoader.getResource(name + ".title");

			// Add a filler to stretch out the dialog.
			add(uiLoader.loadLabel("filler"));

			// Add a title border to make panel look nicer.
			String title = uiLoader.getResource(name + ".border-title");
			setBorder(new TitledPanelBorder(title));
		}

		// From PrefsPanel.
		public Component getComponent() {
			return this;
		}

		// From PrefsPanel.
		public void enter() {
		}

		// From PrefsPanel.
		public boolean leave() {
			return true;
		}

		// From Helpable, causes Help button to be enabled.
		public void help(Object helpOn) {
		}

		// Gets the name of this panel
		public String getName () {
			return name;
		}


		public String getTitle () {
			return title;
		}


		public Icon getIcon () {
			return icon;
		}
		
	}
}
