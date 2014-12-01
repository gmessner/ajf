package com.messners.ajf.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;


/**
 * This class provides an extensible framework used to simplify
 * construction of dialogs.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public abstract class DialogForm extends JPanel implements UIConstants {

	private static final long serialVersionUID = 1L;

	/**
	 * Constant returned by popup() to indicate that the "OK" button on the
	 * dialog was selected
	 */
	public static final int OK = 1;


	/**
	 * Constant returned by popup() to indicate that the "Cancel" button on the
	 * dialog was selected
	 */
	public static final int CANCEL = 0;
	

	protected JDialog dialog = null;
	protected boolean dialogResizable;
	protected Point dialogLocation;

	/**
	 * Title for the dialog.
	 */
	protected String title = "";


	/**
	 * Icon for the dialog title bar 
	 */
	protected Image icon;


	/**
	 * The component to display at the top of the content pane.
	 */
	protected JComponent titleComponent;


	/**
	 * The built-in ChangeListener.
	 */
	protected ChangeListener changeListener = new FormChangeListener();


	/**
	 * Help button in the button panel.  Maybe null if dialog doesn't
	 * implement Helpable interface.
	 */
	protected JButton helpButton;
	protected boolean helpButtonEnabled = true;

	protected boolean okOnly = false;
	protected JButton okButton;
	protected boolean okButtonEnabled = true;
	protected String okButtonText;
	protected String okButtonTooltip;
	protected char okButtonMnemonic;

	protected JButton cancelButton;
	protected boolean cancelButtonEnabled = true;

	protected JButton applyButton;
	protected boolean applyButtonEnabled = true;

	protected JPanel buttonPanel = null;
	protected ArrayList<JButton> extraButtons = new ArrayList<JButton>();
	protected int returnCode = OK;

	protected boolean isModal = false;
	protected HashMap<Component, JDialog> ownerMap = new HashMap<Component, JDialog>();

	/**
	 * Default constructor.
	 */
	public DialogForm () {
		isModal = true;
	}


	/**
	 * Creates a DialogForm instance that can be modal.
	 */
	public DialogForm (boolean modal) {
		isModal = modal;
	}

	
	/**
	 * This method is called to reset the settings on the dialog form
	 * to the initial values when poped up or when "Reset" is selected.
	 */
	public abstract void reset ();


	/**
	 * This method is called to fetch and apply the settings from the
	 * dialog form.  It is called whenever the "OK" or "Apply" button
	 * is selected. Return true if the apply was successful, otherwise
	 * return false and the dialog will not be dismissed.
	 *
	 * @return  true if the apply was successfull, otherwise returns
	 *          false so the dialog will not be dismissed
	 */
	public abstract boolean apply ();


	/**
	 * Gets the image to be displayed on the title bar.
	 *
	 * @return  the Image to be displayed on the title bar
	 */
	public Image getIconImage () {
		return (icon);
	}


	/**
	 * Sets the image to be displayed on the title bar.
	 *
	 * @param  icon  the Image to be displayed on the title bar
	 */
	public void setIconImage (Image icon) {
			this.icon = icon;
	}


	/**
	 * Sets the component to display at the top of the content pane.
	 */
	public void setTitleComponent (JComponent c) {

		if (titleComponent != null && dialog != null) {
			Container container = dialog.getContentPane();
			container.remove(titleComponent);
		}

		titleComponent = c;

		if (titleComponent != null && dialog != null) {
			Container container = dialog.getContentPane();
			container.add(titleComponent, BorderLayout.NORTH);
		}
	}


	/**
	 * Gets the title for the dialog frame.
	 *
	 * @return the title
	 */
	public String getTitle () {
		return (title);
	}


	/**
	 * Sets the title for the dialog frame.
	 *
	 * @param  title  the title
	 */
	public void setTitle (String title) {

		this.title = title;
		if (dialog != null) {
			dialog.setTitle(this.title);
		}
	}


	public void setResizable (boolean flag) {

		dialogResizable = flag;
		if (dialog != null) {
			dialog.setResizable(flag);
		}
	}


	public Point getDialogLocation () {

		if (dialog == null) {
			
			if (dialogLocation == null) {
				dialogLocation = new Point(-1, -1);
			}

		} else {
			dialogLocation = dialog.getLocation();
		}

		return (dialogLocation);
	}


	public void setDialogLocation (Point location) {

		dialogLocation = location;
		if (dialog != null && dialogLocation != null && 
						dialogLocation.x != -1 && dialogLocation.y != -1) {
			dialog.setLocation(dialogLocation);
		}
	}


	/**
	 * Gets the JDialog container for this instance.
	 *
	 * @return  the JDialog container for this instance
	 */
	public JDialog getDialog () {
		return (dialog);
	}


	/**
	 * Create and return the JDialog that contains this DialogForm instance.
	 *
	 * @param  parent  the component to center the dialog over
	 * @return  the created JDialog instance
	 */
	protected JDialog createDialog (Component parent) {

		ResourceBundle resources = ResourceBundle.getBundle(
			DialogForm.class.getName(),
			java.util.Locale.getDefault(),
			DialogForm.class.getClassLoader());

		java.awt.Frame frame;
		if (parent != null) {
			frame = Utilities.getFrame(parent);
		} else {
			frame = null;
		}

		dialog = new JDialog(frame, title, isModal);
		java.awt.Frame frame1 = com.messners.ajf.ui.Utilities.getFrame(dialog);

		/*
		 * Put the icon on the title bar
		 */
		if (icon == null && frame != null) {
			icon = frame.getIconImage();
		}

		if (icon != null && frame1 != null) {
			frame1.setIconImage(icon);
		}

		if (title != null) {
			dialog.setTitle(title);
		}


		/*
		 * Create the panel to hold the dialog buttons
		 */
		buttonPanel = new JPanel();
		int numExtraButtons = extraButtons.size();
		int numButtons = numExtraButtons + 1 + ((isModal && !okOnly) ? 1: 0);
		if (this instanceof Applyable) {
			numButtons++;
		}

		buttonPanel.setLayout(new GridLayout(1, numButtons, 
			COMMAND_BUTTON_SPACING, 0));


		if (isModal) {

			okButton = loadButton(resources, "ok");
			if (okButtonText != null) {
				okButton.setText(okButtonText);
				okButton.setMnemonic(okButtonMnemonic);
				okButton.setToolTipText(okButtonTooltip);
			}

			okButton.setEnabled(okButtonEnabled);
			OkAction okAction = new OkAction();
			okButton.addActionListener(okAction);
			buttonPanel.add(okButton);
			dialog.getRootPane().setDefaultButton(okButton);

			if (okOnly) {
				dialog.addWindowListener(new DialogCloser(okAction));
			} else {

				cancelButton = loadButton(resources, "cancel");
				cancelButton.setEnabled(cancelButtonEnabled);
				CancelAction cancelAction = new CancelAction();
				cancelButton.addActionListener(cancelAction);
				buttonPanel.add(cancelButton);
				dialog.addWindowListener(new DialogCloser(cancelAction));
			}

		} else {

			okButton = loadButton(resources, "close");
			if (okButtonText != null) {
				okButton.setText(okButtonText);
				okButton.setToolTipText(okButtonTooltip);
			}

			okButton.setEnabled(okButtonEnabled);
			CloseAction closeAction = new CloseAction();
			okButton.addActionListener(closeAction);
			buttonPanel.add(okButton);
			dialog.getRootPane().setDefaultButton(okButton);
			dialog.addWindowListener(new DialogCloser(closeAction));
		}


		if (this instanceof Applyable) {

			applyButton = loadButton(resources, "apply");
			applyButton.setEnabled(applyButtonEnabled);
			applyButton.addActionListener(new ApplyAction());
			buttonPanel.add(applyButton);
		}

		for (int i = 0; i < numExtraButtons; i++) {
			JButton btn = extraButtons.get(i);
			buttonPanel.add(btn);
		}

		if (this instanceof Helpable) {

			helpButton = loadButton(resources, "help");
			helpButton.addActionListener(new HelpAction());
			helpButton.setEnabled(helpButtonEnabled);
			buttonPanel.add(helpButton);
		}

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.setBorder(BorderFactory.createEmptyBorder(
			COMMAND_BUTTONS_TOP_MARGIN, 0, 0, 0));

		p.add(buttonPanel);

		p.setBorder(BorderFactory.createEmptyBorder(0, 12, 11, 11));
		setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));

		Container container = dialog.getContentPane();
		container.setLayout(new BorderLayout());
		container.add(p, BorderLayout.SOUTH);
		container.add(this, BorderLayout.CENTER);

		if (titleComponent != null) {
			container.add(titleComponent, BorderLayout.NORTH);
		}

		dialog.setResizable(dialogResizable);
		if (dialogLocation != null && 
						dialogLocation.x != -1 && dialogLocation.y != -1) {
			dialog.setLocation(dialogLocation);
		}

		return (dialog);
	}


	/**
	 * Sets the text for the OK button.
	 *
	 * @param text the text for the OK button
	 */
	public void setOkButtonText (String text, char mnemonic) {
		setOkButtonText(text, mnemonic, null);
	}


	/**
	 * Sets the text for the OK button.
	 *
	 * @param text the text for the OK button
	 */
	public void setOkButtonText (String text, char mnemonic, String tooltip) {

		okButtonText = text;
		okButtonTooltip = tooltip;
		okButtonMnemonic = mnemonic;
		if (okButton != null) {
			okButton.setText(text);
			okButton.setMnemonic(mnemonic);
			okButton.setToolTipText(tooltip);
		}
	}


	/**
	 * Enables/Disables the help button.
	 *
	 * @param enable If true, help button is enabled.
	 */
	protected void setHelpButtonEnabled (boolean enable) {

		helpButtonEnabled = enable;
		if (helpButton != null) {
			helpButton.setEnabled(helpButtonEnabled);
		}
	}


	/**
	 * Enables/Disables the ok button.
	 *
	 * @param enable If true, ok button is enabled.
	 */
	protected void setOkButtonEnabled (boolean enable) {

		okButtonEnabled = enable;
		if (okButton != null) {
			okButton.setEnabled(okButtonEnabled);
		}
	}


	/**
	 * Enables/Disables the cancel button.
	 *
	 * @param enable If true, cancel button is enabled.
	 */
	protected void setCancelButtonEnabled (boolean enable) {

		cancelButtonEnabled = enable;
		if (cancelButton != null) {
			cancelButton.setEnabled(cancelButtonEnabled);
		}
	}


	/**
	 * Enables/Disables the apply button.
	 *
	 * @param enable If true, apply button is enabled.
	 */
	protected void setApplyButtonEnabled (boolean enable) {

		applyButtonEnabled = enable;
		if (applyButton != null) {
			applyButton.setEnabled(applyButtonEnabled);
		}
	}


	/**
	 * Add a button to the button panel of the dialog.
	 *
	 * @param  btn  the button to add
	 */
	public void addButton (JButton btn) {

		if (btn == null) {
			return;
		}

		extraButtons.add(btn);

		if (buttonPanel == null) {
			return;
		}

		int numButtons = buttonPanel.getComponentCount() + 1;
		buttonPanel.setLayout(
				new GridLayout(1, numButtons, COMMAND_BUTTON_SPACING, 0));
		buttonPanel.add(btn);
	}


	/**
	 * Configures the dialog to show only the OK button (no cancel).
	 * Must be set prior to creatDialog() or popup() is called.
	 */
	protected void setOkButtonOnly (boolean flag) {
		okOnly = flag;
	}


	/**
	 * Popup the dialog in the center of the screen.  Will create the 
	 * the containg dialog frame if not already created.
	 */
	public int popup () {
		return (popup(null));	
	}


	/**
	 * Popup the dialog in the center of the specified component.  Will create
	 * the the containg dialog frame if not already created.
	 *
	 * @param  parent  the component to center the dialog over, if not a
	 * top-level frame will determine the toplevel frame and center over it.
	 *
	 * @return  <code>OK</code> if the "OK" button was selected on thje dialog,
	 * otherwise returns <code>CANCEL</code> if "Cancel" was selected
	 */
	public int popup (Object parent) {

		Component frame;
		if (parent instanceof Component) {

			frame = Utilities.getFrame((Component)parent);
			if (frame == null) {
				frame = (Component)parent;
			}

		} else {
			frame = null;
		}

		JDialog oldDialog = dialog;

		/*
		 * See if the owner of the dialog has changed, and if so make sure
		 * to use the JDialog instance for that owner.
		 */
		if (dialog != null && frame != null) {

			JDialog ownedDialog = ownerMap.get(frame);
			if (ownedDialog != null) {
				dialog = ownedDialog;
			} else {
				dialog = null;
			}
		}


		if (dialog == null) {

			createDialog(frame);
			reset();
			dialog.pack();

			/*
			 * Save the mapping of owner to JDialog instance we might
			 * need it later.
			 */
			if (frame != null) {
				ownerMap.put(frame, dialog);
			}

			/*
			 * If we are creating a JDialog instance for a new owner
			 * restore the location of the last JDialog isnatnce
			 */
			if (oldDialog != null) {

				dialog.setLocation(oldDialog.getLocation());

			} else {

				if (frame != null && frame.isVisible()) {
					com.messners.ajf.ui.Utilities.centerDialog(dialog, frame);
				} else {
					com.messners.ajf.ui.Utilities.centerDialog(dialog);
				}

				restorePreferences();
			}

		} else {

			reset();
			dialog.pack();
		}

		dialog.setVisible(true);
		return (returnCode);
	}


	protected void popdown () {
	}


	/**
	 * Override this to restore location and size when the dialog is popped up.
	 */
	public void restorePreferences () {
	}


	/**
	 * Override this to save info about the dialog, such as location and size.
	 */
	public void savePreferences () {
	}


	protected class CancelAction implements ActionListener {

		CancelAction () {
		}

		public void actionPerformed(ActionEvent e) {
			returnCode = CANCEL;
			popdown();
			dialog.setVisible(false);
			savePreferences();
		}
	}


	protected class CloseAction implements ActionListener {

		CloseAction () {
		}

		public void actionPerformed(ActionEvent e) {
			returnCode = OK;
			popdown();
			dialog.setVisible(false);
			savePreferences();
		}
	}


	protected class OkAction implements ActionListener {

		OkAction () {
		}

		public void actionPerformed(ActionEvent e) {

			returnCode = OK;
			if (apply()) {
				popdown();
				dialog.setVisible(false);
				savePreferences();
			}
		}
	}


	protected class ApplyAction implements ActionListener {

		ApplyAction () {
		}

		public void actionPerformed (ActionEvent e) {
			apply();
		}
	}


	protected class HelpAction implements ActionListener {

		HelpAction () {
		}

		public void actionPerformed (ActionEvent e) {

			if (DialogForm.this instanceof Helpable) {
				((Helpable)DialogForm.this).help(DialogForm.this);
			}
		}
	}


	protected JButton loadButton (ResourceBundle resources, String name) {

		String label = resources.getString(name + ".label");
		if (label == null) {
			System.err.println("button label is null: " + name);
			return (null);
		}

		int mnemonic = getMnemonic(label);
		String tooltip = resources.getString(name + ".tooltip");

		JButton btn;
		btn = new JButton(getLabel(label));

		if (mnemonic != -1) {
			btn.setMnemonic((char)mnemonic);
		}

		if (tooltip != null) {
			btn.setToolTipText(tooltip);
		}

		return (btn);
	}

	/**
	 * Get a JButton mnemonic for the specified String
	 *
	 * @return  the mnemonic for the button label or -1 if none present
	 */

	protected int getMnemonic (String s) {

		int index = s.indexOf('&');
		if (index != -1 && s.length() - index > 1) {
			return ((int)Character.toLowerCase(s.charAt(++index)));
		}

		return (-1);
	}


	/**
	 * Get a JButton label for the specified String by removing the mnemonic
	 * specifier if present.  
	 *
	 * @return  the cleaned up String for the JButton label
	 */
	 protected String getLabel (String s) {
		
		int index = s.indexOf('&');
		if (index != -1 && s.length() - index > 1) {
			return (s.substring(0, index) + s.substring(++index));
		}

		return (s);
	}


	/**
	 * Gets the built-in form component change listener.
	 *
	 * @return  the built-in ChangeListener
	 */
	public ChangeListener getChangeListener () {
		return (changeListener);
	}


	/**
	 * Adds a component as a listener for changes.
	 *
	 * @param obj the component to listen for changes on
	 */
	public void addChangeListener (Object obj) {
		changeListener.addChangeListener(obj);
	}


	/**
	 * Override this method to be notified of changes for registered components.
	 *
	 * @param  source the source of the event
	 * @param  event  the event for the change that ocurred
	 */
	public void changeOccurred (Object source, Object event) {
	}


	/**
	 * Listens for changes on a component and calls the changeOccured() method.
	 */
	private class FormChangeListener extends ChangeListener {

		public void change (Object source, Object event) {
			changeOccurred(source, event);
		}
	}


	/**
	 * Monitors for the windowClosing message and calls the exit()
	 * method when received.
	 */
	private final class DialogCloser extends WindowAdapter  {

		private ActionListener listener;

		public DialogCloser (ActionListener listener) {
			this.listener = listener;
		}

		public void windowClosing (WindowEvent e) {
			listener.actionPerformed(null);	
		}
	}
}
