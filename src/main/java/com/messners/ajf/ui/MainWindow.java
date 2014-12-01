package com.messners.ajf.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.border.Border;


/**
 * This class creates a main window with a menubar, toolbar, workarea, and
 * status bar. It is meant to be used as the content pane for JRootPane.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class MainWindow extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final String LEFT_PANEL = "left-panel";
	public static final String STATUS_MESSAGE = "status-message";

	protected JPanel menutoolbar;
	protected JMenuBar menubar;
	protected JToolBar toolbar;

	protected JComponent workArea;

	protected JComponent statusBar;
	protected JLabel statusMsg;
	protected JProgressBar progessBar;


	/**
	 * Constructs a MainWindow with a menubar, toolbar, work area,
	 * and status bar.
	 *
	 * @param  loader       the ResourceLoader to load resources with
	 * @param  menubarName  the name of the menubar in the resource file
	 * @param  toolbarName  the name of the toolbar in the resource file
	 */
	public MainWindow (
			ResourceLoader loader, String menubarName, String toolbarName) {

		super();
		setOpaque(true);
		setLayout(new BorderLayout());

		/*
		 * Create the menu and tool bars
		 */
		ColumnLayout cl = new ColumnLayout(0, 0);
		cl.setStretchWidth(true);
		cl.setExpandWidth(true);
		cl.setVgap(0);
		menutoolbar = new JPanel(cl);
		add(menutoolbar, BorderLayout.NORTH);


		if (menubarName != null) {
			menubar = (JMenuBar)loader.load(menubarName);
			menubar.setBorderPainted(false);
			menubar.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
			menutoolbar.add(new LineSeparator(LineSeparator.HORIZONTAL, 0, 0));
			menutoolbar.add(menubar);
		}

		if (toolbarName != null) {
			toolbar = (JToolBar)loader.load(toolbarName);
			menutoolbar.add(new LineSeparator(LineSeparator.HORIZONTAL, 0, 0));
			menutoolbar.add(toolbar);
		}


		/*
		 * Create the default workArea
		 */
		JPanel workArea = new JPanel();
		workArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		setWorkArea(workArea);


		/*
		 * Create the status bar
		 */
		statusBar = new JPanel(new BorderLayout());
		statusBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		statusMsg = new JLabel(" Ready");
		statusMsg.setOpaque(true);
		statusMsg.setName(STATUS_MESSAGE);
		Border border = BorderFactory.createLoweredBevelBorder();
		Border margin = BorderFactory.createEmptyBorder(1, 0, 0, 0);
		Border compound = BorderFactory.createCompoundBorder(margin, border);
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setName(LEFT_PANEL);
		leftPanel.setBorder(compound);
		statusBar.add(leftPanel, BorderLayout.CENTER);
		leftPanel.add(statusMsg, BorderLayout.CENTER);

		
		/*
		 * Finally create the progress bar
		 */
		progessBar = new JProgressBar();
		progessBar.setBorder(compound);
		statusBar.add(progessBar, BorderLayout.EAST);

		add(statusBar, BorderLayout.SOUTH);
	}


	/**
	 * Sets the message in the status bar.
	 *
	 * @param  status  the new message for the status bar
	 */
	public void setStatus (String status) {
		statusMsg.setText(" " + status);
	}


	/**
	 * Sets the background of the status message.
	 *
	 * @param bg the new status message background color
	 */
	public void setStatusBackground (Color bg) {
		statusMsg.setBackground(bg);
	}


	/**
	 * Set the progress bar to either processing or not.
	 *
	 * @param  flag  if true the progress bar will display an
	 * indicator which continuously bounces from left to right,
	 * if false not indicator is displayed
	 */
	public void setProcessing (boolean flag) {
		progessBar.setIndeterminate(flag);
	}


	/**
	 * Get the work area for this MainWindow.  The work area is the panel
	 * below the toolbar and above the status bar.
	 *
	 * @return  the work area for this MainWindow instance
	 */
	public JComponent getWorkArea () {
		return (workArea);
	}


	/**
	 * Set the work area for this MainWindow.  The work area is the panel
	 * below the toolbar and above the status bar.
	 *
	 * @param  workArea  the new work area
	 */
	public void setWorkArea (JComponent workArea) {

		if (this.workArea != null) {
			remove(this.workArea);
		}

		this.workArea = workArea;

		if (this.workArea != null) {
			add(this.workArea, BorderLayout.CENTER);
		}
	}


	/**
	 * Gets the component that is displayed as the status bar along the
	 * bottom of the main window.
	 *
	 * @return  the status bar component
	 */
	public JComponent getStatusBar () {
		return (statusBar);
	}


	/**
	 * Sets the component that is displayed as the status bar along the
	 * bottom of the main window.
	 *
	 * @param  statusBar the new status bar
	 */
	public void setStatusBar (JComponent statusBar) {

		if (this.statusBar!= null) {
			remove(this.statusBar);
		}

		this.statusBar = statusBar;

		if (this.statusBar!= null) {
			add(this.statusBar, BorderLayout.SOUTH);
		}
	}


	/**
	 * Gets the container for the JMenuBar and JToolBar components.
	 *
	 * @return the JPanel container for the JMenuBar and JToolBar components
	 */
	public JPanel getMenuToolBar () {
		return (menutoolbar);
	}


	/**
	 * Gets the JMenuBar.
	 *
	 * @return  the JMenuBar for this MainWindow
	 */
	public JMenuBar getMenuBar () {
		return (menubar);
	}


	/**
	 * Gets the JToolBar.
	 *
	 * @return  the JToolBar for this MainWindow
	 */
	public JToolBar getToolBar () {
		return (toolbar);
	}


	/**
	 * Shows/hides the labels on each button on the toolbar.
	 *
	 * @param show  if true, the labels will be shown
	 */
	public void setToolBarLabelsVisible (boolean show) {

		if (toolbar != null) {
			Utilities.setButtonLabelsVisible(toolbar, show);
		}
	}
}
