package com.messners.ajf.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;


/**
 * This component present a Wizard UI and manages a set of panels
 * with a "Next" and "Back" button. Panels displayed by this component
 * must implement the WizardPanel interface.
 *
 * @author  Greg Messner <greg@messners.com>
 * @see WizardPanel
 */
public class WizardManager extends JPanel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	protected transient JPanel cards = null;
	protected transient CardLayout cardLayout = null;

	protected transient JLabel bitmapLabel = null;
	protected boolean bitmapVisible = false;

	protected transient JButton backBtn   = null;
	protected transient JButton nextBtn   = null;
	protected transient JButton lastBtn   = null;
	protected transient JButton cancelBtn = null;
	protected transient JButton helpBtn   = null;

	protected String nextLabel;
	protected int nextMnemonic;
	protected String nextTooltip;

	protected String finishLabel;
	protected int finishMnemonic;
	protected String finishTooltip;

	protected String lastLabel;
	protected int lastMnemonic;
	protected String lastTooltip;

	protected ArrayList<Panel> panels = new ArrayList<Panel>();
	protected int panelIndex = -1;
	protected boolean isSkipping = false;

	protected String titleStr;
	protected Image iconImage;
	protected JDialog dialog;

	/**
	 * The built-in ChangeListener.
	 */
	protected ChangeListener changeListener = new PanelChangeListener();

	public WizardManager () {
		createForm();
	}


	private void createForm () {

		ResourceBundle resources = ResourceBundle.getBundle(
			WizardManager.class.getName(),
			java.util.Locale.getDefault(),
			WizardManager.class.getClassLoader());
			

		setLayout(new GridBagLayout());

		/*
		 * We use a CardLayout to manage the panels
		 */
		cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);
		cards.setBorder(new EmptyBorder(0, 0, 0, 0));

		bitmapLabel = new JLabel("");
		bitmapLabel.setName("Bitmap");
		bitmapLabel.setBackground(ColorUtils.nameToColor("DimGray"));
		bitmapLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		bitmapLabel.setVisible(false);
		

		FlowLayout fl = new FlowLayout(FlowLayout.RIGHT);
		fl.setHgap(0);
		fl.setVgap(0);
		JPanel button_container = new JPanel(fl);
		button_container.setBorder(new EmptyBorder(11, 0, 0, 0));

		GridLayout gl = new GridLayout(1,6);
		gl.setHgap(5);
		gl.setVgap(5);
		JPanel buttons = new JPanel(gl);
		buttons.setBorder(new EmptyBorder(0, 0, 0, 0));
		button_container.add(buttons);

		backBtn = loadButton(resources, "back");
		backBtn.addActionListener(new BackAction());
		backBtn.setEnabled(false);
		buttons.add(backBtn);

		finishLabel = getLabel(resources.getString("finish.label"));
		finishMnemonic = getMnemonic(resources.getString("finish.label"));
		finishTooltip = resources.getString("finish.tooltip");

		nextLabel = getLabel(resources.getString("next.label"));
		nextMnemonic = getMnemonic(resources.getString("next.label"));
		nextTooltip = resources.getString("next.tooltip");

		lastLabel = getLabel(resources.getString("last.label"));
		lastMnemonic = getMnemonic(resources.getString("last.label"));
		lastTooltip = resources.getString("last.tooltip");

		nextBtn = new JButton();
		setNextButtonToFinish();
		nextBtn.addActionListener(new NextAction());
		buttons.add(nextBtn);

		lastBtn = new JButton();
		setLastButtonToLast();
		lastBtn.addActionListener(new LastAction());
		buttons.add(lastBtn);

		buttons.add(Box.createHorizontalStrut(32));

		cancelBtn = loadButton(resources, "cancel");
		CancelAction cal = new CancelAction();
		cancelBtn.addActionListener(cal);
		registerKeyboardAction(cal,
			KeyStroke.getKeyStroke((char)KeyEvent.VK_ESCAPE), 
			WHEN_IN_FOCUSED_WINDOW);
		buttons.add(cancelBtn);

		helpBtn = loadButton(resources, "help");
		helpBtn.addActionListener(new HelpAction());
		helpBtn.setEnabled(false);
		buttons.add(helpBtn);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets.right  = 0;
		gbc.insets.left   = 8;
		gbc.insets.top	= 8;
		gbc.insets.bottom = 0;
		add(bitmapLabel, gbc);

		gbc.insets.right  = 8;
		gbc.insets.left   = 16;
		gbc.insets.top	= 8;
		gbc.insets.bottom = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor  = GridBagConstraints.NORTHWEST;
		gbc.fill	= GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridy   = 0;
		gbc.gridx   = 1;
		add(cards, gbc);

		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets.right  = 8;
		gbc.insets.left   = 8;
		gbc.insets.top	= 8;
		gbc.insets.bottom = 0;
		add(new JSeparator(), gbc);

		gbc.insets.right  = 8;
		gbc.insets.left   = 8;
		gbc.insets.top	  = 8;
		gbc.insets.bottom = 8;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.SOUTHEAST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(button_container, gbc);


		/*
		 * Lock the size of the Next button so it won't resize
		 * between "Next" and "Finish"
		 */
		Dimension sizes[] = new Dimension[6];
		sizes[0] = backBtn.getPreferredSize();

		sizes[1] = nextBtn.getPreferredSize();
		setNextButtonToNext();
		sizes[2] = nextBtn.getPreferredSize();

		sizes[3] = lastBtn.getPreferredSize();
		sizes[4] = cancelBtn.getPreferredSize();
		sizes[5] = helpBtn.getPreferredSize();

		Dimension s = sizes[0];
		for (int i = 1; i < 6; i++) {

			if (sizes[i].width > s.width && sizes[i].width > 0) {	
				s = sizes[i];
			}
		}

		backBtn.setPreferredSize(s);
		nextBtn.setPreferredSize(s);
		lastBtn.setPreferredSize(s);
		cancelBtn.setPreferredSize(s);
		helpBtn.setPreferredSize(s);
	}


	/**
	 * Display the wizrad in a dialog container.  Prior to displaying
	 * the dialog the current panel will be set to the first one and 
	 * the state of the navigation buttons will be proerly setup.
	 *
	 * @param  parent  the component to center the dialog over, if the
	 * dialog has been previously displayed it will be re-displayed in
	 * the last displayed position and size.
	 */
	public void showDialog (Component parent) {

		/*
		 * Create the dialog if not already created
		 */
		if (dialog == null) {

			createDialog(parent);
		}

		/*
		 * Reset the wizard to the first form and display the dialog
		 */
		skipTo(0);
		setNextButtonEnabled(true);
		setBackButtonEnabled(false);
		dialog.setVisible(true);
	}


	/**
	 * Used to create the JDialog container for this WizardManager.
	 *
	 * @param  parent  the parent component for the dialog
	 * @return  the created JDialog instance
	 */
	protected JDialog createDialog (Component parent) {

		if (dialog != null) {
			return (dialog);
		}

		Frame frame = Utilities.getFrame(parent);
		dialog = new JDialog(frame, titleStr, true);

		/*
		 * By default set the title bar icon to that of ther parent.
		 */
		if (frame != null && iconImage == null) {
			iconImage = frame.getIconImage();
		}

		if (iconImage != null) {
			setIconImage(iconImage);
		}
	
		dialog.getContentPane().add(this);
		dialog.pack();

		
		if (parent != null && parent.isVisible()) {
			com.messners.ajf.ui.Utilities.centerDialog(dialog, parent);
		} else {
			com.messners.ajf.ui.Utilities.centerDialog(dialog);
		}

		dialog.setResizable(false);
		return (dialog);
	}


	/**
	 * Get the containing JDialog instance. This will return null if 
	 * createDialog() has not been previously called.
	 *
	 * @return  the containing JDialog instance
	 */
	public JDialog getDialog () {
		return (dialog);
	}


	/**
	 * Get the String that is or will be displayed on the dialog.
	 *
	 * @return  the dialog title
	 */
	public String getTitle () {
		return (titleStr);
	}
	

	public void setTitle (String title) {
		titleStr = title;
	}


	public void setIconImage (Image icon) {

		java.awt.Frame frame = Utilities.getFrame(dialog);
		if (frame != null) {
			frame.setIconImage(icon);
		}

		iconImage = icon;
	}
	

	/**
	 * Skip to the panel specified by name. If you are calling
	 * <code>skipTo()</code> from a <code>WizardPanel.leave()</code> method
	 * return false from the <code>leave()</code> method or the the panel
	 * skipped to will not be displayed.
	 *
	 * @param  name  the name of the panel to show
	 */
	public void skipTo (String name) {

		int index = getPanelIndex(name);
		skipTo(index);
	}

		
	/**
	 * Skip the panel specified by index.  If you are calling
	 * <code>skipTo()</code> from a <code>WizardPanel.leave()</code> method
	 * return false from the <code>leave()</code> method or the the panel
	 * skipped to will not be displayed.
	 *
	 * @param  index the index of the panel to show
	 */
	public void skipTo (int index) {

		int num_panels = getNumPanels();
		if (index < 0 || index > num_panels) {
			return;
		}

		isSkipping = true;
		show(index);
		isSkipping = false;
	}

		
	/**
	 * Show the panel at the specified index.
	 *
	 * @param  index  the index of the panel to show.
	 * @param  force  if true the panel will be displayed regardless
	 * of wether it is the current panel or not.
	 */
	public void show (int index, boolean force) {

		if (force) {
			panelIndex = -1;
		}

		show(index);
	}


	/**
	 * Show the panel at the specified index.
	 *
	 * @param  index  the index of the panel to show.
	 */
	public void show (int index) {

		/*
		 * Return if the panel index hasn't changed
		 */
		if (index == panelIndex) {
			return;
		}

		/*
		 * Validate the index
		 */
		if (index < 0 || index >= getNumPanels()) {
			return;
		}

		WizardPanel wp = getPanelAt(panelIndex);
		int direction;

		if (index > panelIndex) {
			direction = WizardPanel.FORWARD;
		} else {
			direction = WizardPanel.BACK;
		}

		if (isSkipping) {
			isSkipping = false;
		} else if (wp != null && !wp.leave(direction)) {
			return;
		} 

		Panel p = panels.get(index);
		wp = p.getPanel();
		
		helpBtn.setEnabled(wp instanceof Helpable);

		cardLayout.show(cards, p.getName());

		if (index > 0 && panelIndex < 1) {
			backBtn.setEnabled(true);
		} else if (index == 0) {
			backBtn.setEnabled(false);
		} 

		if (index == getNumPanels() - 1) {

			if (lastBtn.isVisible()) {
				setLastButtonToFinish();
				nextBtn.setEnabled(false);
			} else {
				setNextButtonToFinish();
			}

		} else if (panelIndex == getNumPanels() - 1 && getNumPanels() > 1) {

			if (lastBtn.isVisible()) {
				setLastButtonToLast();
				nextBtn.setEnabled(true);
			} else {
				setNextButtonToNext();
			}
		}

		panelIndex = index;

		nextBtn.requestFocus();

		wp.enter(direction);
	}


	/**
	 * Set the label, mnemonic and tooltip for the specified button. Used
	 * to setup the changing navigation buttons.
	 */
	protected void setButton (JButton btn,
		String text, int mnemonic, String tooltip) {

		btn.setText(text);
		btn.setToolTipText(tooltip);

		if (mnemonic != -1) {
			btn.setMnemonic((char)mnemonic);
		}
	}


	/**
	 * Set the text on the "Next" button to "Finish".
	 */
	public void setNextButtonToFinish () {
		setButton(nextBtn, finishLabel, finishMnemonic, finishTooltip);
	}


	/**
	 * Set the text on the "Next" button to "Next >".
	 */
	public void setNextButtonToNext () {
		setButton(nextBtn, nextLabel, nextMnemonic, nextTooltip);
	}


	/**
	 * Set the text on the "Last" button to "Finish".
	 */
	public void setLastButtonToFinish () {
		setButton(lastBtn, finishLabel, finishMnemonic, finishTooltip);
	}


	/**
	 * Set the text on the "Last" button to "Last".
	 */
	public void setLastButtonToLast () {
		setButton(lastBtn, lastLabel, lastMnemonic, lastTooltip);
	}


	/**
	 * Get the WizardPanel at the specified index.
	 *
	 * @param  index  the index to get the WizardPanel for
	 * @return  the WizardPanel at the specified index
	 */
	public WizardPanel getPanelAt (int index) {

		if (index < 0 || index >= getNumPanels()) {
			return (null);
		}

		Panel p = panels.get(index);
		return (p.getPanel());
	}
	

	/**
	 * Go back one panel.
	 */
	public void back () {
		show(panelIndex - 1);
	}


	/**
	 * Go forward one panel.
	 */
	public void next () {

		if (panelIndex == getNumPanels() - 1) {

			WizardPanel wp = getPanelAt(panelIndex);
			if (wp != null && !wp.leave(WizardPanel.FORWARD)) {
				return;
			}

			finish();
			return;
		}

		show(panelIndex + 1);
	}


	/**
	 * This class listens for the "Back" button.
	 */
	class BackAction implements ActionListener, java.io.Serializable {

		private static final long serialVersionUID = 1L;

		BackAction () {
		}

		public void actionPerformed (ActionEvent e) {
			back();
		}
	}


	/**
	 * This class listens for the "Next" button.
	 */
	class NextAction implements ActionListener, java.io.Serializable {

		private static final long serialVersionUID = 1L;

		NextAction () {
		}

		public void actionPerformed (ActionEvent e) {
			next();
		}
	}


	/**
	 * This class listens for the "Last" button.
	 */
	class LastAction implements ActionListener, java.io.Serializable {

		private static final long serialVersionUID = 1L;

		LastAction () {
		}

		public void actionPerformed (ActionEvent e) {

			if (panelIndex == getNumPanels() - 1) {

				WizardPanel wp = getPanelAt(panelIndex);
				if (wp != null && !wp.leave(WizardPanel.FORWARD)) {
					return;
				}

				finish();
				return;
			}

			show(getNumPanels() - 1);
		}
	}


	/**
	 * This class listens for the "Cancel" button.
	 */
	class CancelAction implements ActionListener, java.io.Serializable {

		private static final long serialVersionUID = 1L;

		CancelAction () {
		}

		public void actionPerformed (ActionEvent e) {
			cancel();
		}
	}


	/**
	 * This class listens for the "Help" button.
	 */
	class HelpAction implements ActionListener, java.io.Serializable {

		private static final long serialVersionUID = 1L;

		HelpAction () {
		}

		public void actionPerformed (ActionEvent e) {

			WizardPanel wp = getPanelAt(panelIndex);
			if (wp instanceof Helpable) {
					((Helpable)wp).help(wp);
			}
		}
	}


	/**
	 * This class simply is used as a map of panel component to panel name.
	 */
	class Panel implements java.io.Serializable {

		private static final long serialVersionUID = 1L;
		WizardPanel _panel = null;
		String _name = null;

		Panel (WizardPanel p, String name) {

			_panel = p;
			_name  = name;
		}

		WizardPanel getPanel () {
			return (_panel);
		}

		String getName () {
			return (_name);
		}
	}


	/**
	 * Get the number of managed panels.
	 */
	public int getNumPanels () {
		return (panels.size());
	}


	/**
	 * Get the index of the panel specified by name.
	 *
	 * @param  name  the name of the panel to get the index for
	 * @return the index of the named panel if found, otherwise returns -1
	 */
	public int getPanelIndex (String name) {

		if (name == null) {
			return (-1);
		}

		int num_panels = getNumPanels();
		for (int i = 0; i < num_panels; i++) {

			WizardPanel wp = getPanelAt(i);
			String pname = wp.getComponent().getName();
			if (name.equals(pname)) {
				return (i);
			}
		}

		return (-1);
	}


	/**
	 * Set the Icon that will be displayed as the bitmap on the left side
	 * of the wizard.
	 *
	 * @param  icon  the Icon instance for the wizard bitmap, if null
	 * no bitmap will be displayed
	 */
	public void setBitmapIcon (Icon icon) {

		bitmapLabel.setIcon(icon);

		if (icon == null) {
			return;
		}

		bitmapLabel.setText(null);

		revalidate();
		bitmapLabel.paintImmediately(
			javax.swing.SwingUtilities.getLocalBounds(bitmapLabel));
	}


	/**
	 * Get the Icon that is displayed as the bitmap on the left side
	 * of the wizard.
	 *
	 * @return the Icon instance for the wizard bitmap
	 */
	public Icon getBitmapIcon () {
		return (bitmapLabel.getIcon());
	}


	/**
	 *
	 * @param  flag  if true the wizard bitmap will be displayed
	 * Set the visibility of the wizard bitmap.
	 */
	public void setBitmapVisible (boolean flag) {
		if (flag == bitmapLabel.isVisible()) {
			return;
		}

		bitmapLabel.setVisible(flag);

		Container p = getParent();
		if (p instanceof JComponent) {
			((JComponent)p).revalidate();
		}
	}


	/**
	 * Get the visibility of the wizard bitmap.
	 *
	 * @return  true if the wizard bitmap is to be displayed
	 */
	public boolean isBitmapVisible () {
		return (bitmapLabel.isVisible());
	}


	/**
	 * Remove all panels from the wizard.
	 */
	public void removeAllPanels () {
		panels.clear();
		cards.removeAll();
		panelIndex = -1;
	}



	/**
	 * Add a panel with the specified name to the wizard.
	 *
	 * @param  p  the WizardPanel to add
	 * @param  name  the name for the WizardPanel
	 */
	public void addPanel (WizardPanel p, String name) {

		Panel panel = new Panel(p, name);
		panels.add(panel);
		cards.add(p.getComponent(), name);
	}


	/**
	 * Add a panel to the wizard. Uses the name of the WizardPanel component 
	 * for the name of the panel.
	 *
	 * @param  p  the WizardPanel to add
	 */
	public void addPanel (WizardPanel p) {

		Component c = p.getComponent();
		Panel panel = new Panel(p, c.getName());
		panels.add(panel);
		cards.add(p.getComponent(), c.getName());
	}


	/**
	 * Remove the panel at the specified index.
	 */
	public void removePanelAt (int index) {
		panels.remove(index);
		cards.remove(index);
	}


	/**
	 * Returns true if the "Help" button is setup to be visible.
	 */
	public boolean isHelpButtonVisible () {
		return (helpBtn.isVisible());
	}


	/**
	 * Displays or hides the "Help" button.
	 */
	public void setHelpButtonVisible (boolean flag) {

		if (flag == helpBtn.isVisible()) {
			return;
		}

		helpBtn.setVisible(flag);
		revalidate();
	}


	/**
	 * Returns true if the "Last" button is setup to be visible.
	 */
	public boolean isLastButtonVisible () {
		return (lastBtn.isVisible());
	}



	/**
	 * Displays or hides the "Last" button.
	 */
	public void setLastButtonVisible (boolean flag) {

		if (flag == lastBtn.isVisible()) {
			return;
		}


		lastBtn.setVisible(flag);
		revalidate();
	}


	/**
	 * Set the enabled state of the "Back" button.
	 *
	 * @param  enabled  the new enabled state
	 */
	public void setBackButtonEnabled (boolean enabled) {
		backBtn.setEnabled(enabled);
	}


	/**
	 * Set the enabled state of the "Next" button.
	 *
	 * @param  enabled  the new enabled state
	 */
	public void setNextButtonEnabled (boolean enabled) {
		nextBtn.setEnabled(enabled);
	}


	/**
	 * Set the enabled state of the "Last" button.
	 *
	 * @param  enabled  the new enabled state
	 */
	public void setLastButtonEnabled (boolean enabled) {
		lastBtn.setEnabled(enabled);
	}


	/**
	 * Set the enabled state of the "Cancel" button.
	 *
	 * @param  enabled  the new enabled state
	 */
	public void setCancelButtonEnabled (boolean enabled) {
		cancelBtn.setEnabled(enabled);
	}


	/**
	 * Set the enabled state of the "Help" button.
	 *
	 * @param  enabled  the new enabled state
	 */
	public void setHelpButtonEnabled (boolean enabled) {
		helpBtn.setEnabled(enabled);
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
	 * This method is called when the "Finish" button is pressed.
	 * You extension of WizardManager should override this method to
	 * provide what ever functionality is appropriate when the wizard
	 * is finished.</p>
	 *
	 * NOTE: Will dismiss the dialog if visible.
	 */
	public void finish () {

		if (dialog != null) {
			dialog.setVisible(false);
		}
	}


	/**
	 * This method is called when the "Cancel" button is pressed.
	 * You extension of WizardManager should override this method to
	 * provide what ever functionality is appropriate when the wizard
	 * is cancelled.</p>
	 *
	 * NOTE: Will dismiss the dialog if visible.
	 */
	public void cancel () {
		if (dialog != null) {
			dialog.setVisible(false);
		}
	}


	/**
	 * Gets the built-in panel component change listener.
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
	   
		Object panel = getPanelAt(panelIndex);
		if (panel instanceof DefaultWizardStep) {
			setNextButtonEnabled(((DefaultWizardStep)panel).allowNext());
		}
	}


	/**
	 * Listens for changes on a component and calls the changeOccured() method.
	 */
	private class PanelChangeListener extends ChangeListener {

		public void change (Object source, Object event) {
			changeOccurred(source, event);
		}
	}
}
