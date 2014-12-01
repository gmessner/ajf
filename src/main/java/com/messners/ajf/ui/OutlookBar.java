package com.messners.ajf.ui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JViewport;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 * This class emulates the panel control found in MS Outlook.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class OutlookBar extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	protected JViewport viewport   = null;

	protected ArrayList<JButton> buttons   = new ArrayList<JButton>();
	protected ArrayList<JComponent> panels = new ArrayList<JComponent>();

	protected JPanel topPanel    = null;
	protected JPanel bottomPanel = null;

	protected int panelIndex = -1;
	protected ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();

	protected Insets insets = new Insets(0, 0, 0, 0);

	protected JPopupMenu popup;
	protected PopupListener popupListener;


	/**
	 * Default constructor. Creates an instance ready to add new panels
	 * using addPanel().
	 */
	public OutlookBar () {

		super();
		setLayout(new BorderLayout());

		ColumnLayout layout = new ColumnLayout(0, 0);
		layout.setExpandWidth(true);
		layout.setVgap(0);

		topPanel = new JPanel(layout);
		add(topPanel, BorderLayout.NORTH);

		JScrollPane scroller = new JScrollPane();
		scroller.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		viewport = scroller.getViewport();
		add(scroller, BorderLayout.CENTER);
	
		bottomPanel = new JPanel(layout);
		add(bottomPanel, BorderLayout.SOUTH);

		/*
		 * Setup a mouse listener to listen for possible popup-menu actions
		 */
		popupListener = new PopupListener();
		addMouseListener(popupListener);
		viewport.addMouseListener(popupListener);
	}


	/**
	 * Gets the index of the currently selectewd panel.
	 *
	 * @return the index of the currently selected panel
	 */
	public int getSelectedIndex () {
		return (panelIndex);
	}


	/**
	 * Gets the index for the specified component.  Will return -1 if the 
	 * contained is not contained by this instance.
	 *
	 * @return the index of the specified component, -1 if component
	 * is not contained by this instance
	 */
	public int getIndex (JComponent component) {

		int index = panels.indexOf(component);
		if (index != -1) {
			return (index);
		}

		return (buttons.indexOf(component));
	}


	/**
	 * Gets the index for the specified panel.  Will return -1 if the 
	 * panel is not contained by this instance.
	 *
	 * @param  panel the panel to get the index for
	 * @return the index of the specified panel, -1 if panel 
	 * is not contained by this instance
	 */
	public int getPanelIndex (JComponent panel) {
		return (panels.indexOf(panel));
	}


	/**
	 * Gets the index for the specified button.  Will return -1 if the 
	 * button is not contained by this instance.
	 *
	 * @param  button  the JButton instance to get the index for
	 * @return the index of the specified button, -1 if button
	 * is not contained by this instance
	 */
	public int getButtonIndex (JButton button) {
		return (buttons.indexOf(button));
	}


	/**
	 * Get the count of contained panels.
	 *
	 * @return  the count of the contained panels
	 */
	public int getPanelCount () {
		return (panels.size());
	}


	/**
	 * Gets the the JComponent for the panel at the specified index.
	 *
	 * @param  index  the index of the panel to get
	 * @return the JComponent for the panel at the specified index
	 */
	public JComponent getPanelAt (int index) {

		if (index < 0 || index >= panels.size()) {
			throw new IndexOutOfBoundsException();
		}

		return panels.get(index);
	}


	/**
	 * Gets the the JButton for the button at the specified index.
	 *
	 * @param  index  the index of the button to get
	 * @return the JButton for the button at the specified index
	 */
	public JButton  getButtonAt (int index) {

		if (index < 0 || index >= panels.size()) {
			throw new IndexOutOfBoundsException();
		}

		return buttons.get(index);
	}


	/**
	 * Gets the the JComponent for the currently selected panel.
	 *
	 * @return  the JComponent for the currently selected panel
	 */
	public JComponent getSelectedPanel () {

		if (panelIndex < 0) {
			return (null);
		}

		return (getPanelAt(panelIndex));
	}


	/**
	 * Gets the the JButton for the currently selected panel.
	 *
	 * @return  the JButton for the currently selected panel
	 */
	public JButton getSelectedButton () {

		if (panelIndex < 0) {
			return (null);
		}

		return (getButtonAt(panelIndex));
	}


	/**
	 * Show the specified panel. This may also cause the
	 * buttons to shift around on the OutlookBar.
	 *
	 * @param  panel the panel to show
	 */
	public void setSelectedPanel (JComponent panel) {

		if (panel == null) {
			return;
		}

		int index = panels.indexOf(panel);
		setSelectedIndex(index);
	}


	/**
	 * Show the panel at the specified index. This may also cause the
	 * buttons to shift around on the OutlookBar.
	 *
	 * @param  index  the index of the panel to show
	 */
	public void setSelectedIndex (int index) {

		if (index < 0 || index >= panels.size()) {
			return;
		}

		if (index == panelIndex) {
			return;
		}

		viewport.setView(panels.get(index));
		
		if (index < panelIndex) {

			for (int i = panelIndex; i > index; i--) {
				topPanel.remove(i);
				bottomPanel.add(buttons.get(i), 0);
			}

		} else if (panelIndex > -1) {

			for (int i = panelIndex + 1; i <= index; i++) {
				bottomPanel.remove(0);
				topPanel.add(buttons.get(i));
			}
		}

		viewport.setView(panels.get(index));
		panelIndex = index;

		topPanel.validate();
		bottomPanel.validate();
		validate();
	}
			

	/**
	 * Set the button margin. This is the space between the button border
	 * and the label.
	 */
	public void setButtonMargin (Insets insets) {

		this.insets = insets;
	}

	/**
	 * Add a panel to the OutlookBar with the specified label on its
	 * control button.
	 *
	 * @param  label  the label for the panel button
	 * @param  panel  the panel to add
	 */
	public void addPanel (String label, JComponent panel) {

		JButton btn = new JButton(label);
		if (insets != null) {
			btn.setMargin(insets);
		}

		addPanel(btn, panel);
	}

			

	/**
	 * Add a panel to the OutlookBar with the specified label on its
	 * control button.
	 *
	 * @param  btn    the JButton for the panel button
	 * @param  panel  the panel to add
	 */
	public void addPanel (JButton btn, JComponent panel) {

		btn.addActionListener(this);

		if (panelIndex == -1) {

			topPanel.add(btn);
			viewport.setView(panel);
			panelIndex = panels.size();

		} else {

			bottomPanel.add(btn);
		}

		panels.add(panel);
		buttons.add(btn);

		panel.addMouseListener(popupListener);
		btn.addMouseListener(popupListener);

		topPanel.validate();
		bottomPanel.validate();
		validate();
	}


	/**
	 * Removes the specified panel and associated button.
	 *
	 * @param  panel the panel to remove
	 */
	public void removePanel (JComponent panel) {

		int index = getPanelIndex(panel);
		if (index != -1) {
			removePanelAt(index);
		}
	}


	/**
	 * Removes the panel and button at the specified index.
	 *
	 * @param  index  the index of the panel to remove
	 */
	public void removePanelAt (int index) {

		JButton btn = getButtonAt(index);
		if (index > panelIndex) {
			bottomPanel.remove(btn);
		} else {
			topPanel.remove(btn);
		}

		panels.remove(index);
		buttons.remove(index);

		int panelIndex = this.panelIndex;
		if (panelIndex < index) {

			topPanel.validate();
			bottomPanel.validate();
			validate();

		} else if (panelIndex > index) {

			panelIndex--;
			this.panelIndex = -1;
			setSelectedIndex(panelIndex);

		} else {

			index--;
			this.panelIndex = -1;

			if (index < 0) {
			
				viewport.setView(null);
				topPanel.validate();
				bottomPanel.validate();
				validate();

			} else {

				setSelectedIndex(index);
			}
		}	
	}


	/**
	 * Listener for button presses on the OutlookBar. Simply shows the
	 * panel associated with the pressed button.
	 */
	public void actionPerformed (ActionEvent evt) {

		JButton btn = (JButton)evt.getSource();
		int index = buttons.indexOf(btn);
		if (index != panelIndex) {
			setSelectedIndex(index);
			fireStateChanged();
		}
	}


	/**
	 * Adds a listener for change (panel selection) events.
	 *
	 * @param  l  the listener to add
	 */
	public synchronized void addChangeListener (ChangeListener l) {

		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}


	/**
	 * Removes a listener for change (panel selection) events.
	 *
	 * @param  l  the listener to remove
	 */
	public synchronized void removeChangeListener (ChangeListener l) {
		listeners.remove(l);
	}


	/**
	 * Fires a stateChanged() event.
	 */
	public void fireStateChanged () {

		/*
		 * Make a copy of the listener object vector so that it cannot
		 * be changed while we are firing events
		 */
		ArrayList<ChangeListener> fireList;
		synchronized(this) {
			if (this.listeners.size() < 1) {
				return;
			}
			
			fireList = new ArrayList<ChangeListener>(this.listeners.size());
			fireList.addAll(fireList);
		}

		/*
		 * Create the event object
		 */
		ChangeEvent evt = new ChangeEvent(this);

		/*
		 * Fire the event to all listeners
		 */
		for (ChangeListener l : fireList) {
			l.stateChanged(evt);
		}
	}


	/**
	 * Sets the popup for this instance.
	 *
	 * @param popup  the JPopupMenu to invoke for this instance
	 */
	public void setPopup (JPopupMenu popup) {

		this.popup = popup;
		if (popup == null) {
			return;
		}

		popup.setInvoker(this);
	}


	/**
	 * This class listens for mouse events and displays the popup
	 * menu if the system specific popup mouse event occurs.
	 */
	private class PopupListener implements MouseListener {


		public void mouseEntered (MouseEvent e) {
			checkPopup(e);
		}

		public void mouseExited (MouseEvent e) {
			checkPopup(e);
		}

		public void mouseClicked (MouseEvent e) {
			checkPopup(e);
		}

		public void mousePressed (MouseEvent e) {
			checkPopup(e);
		}

		public void mouseReleased (MouseEvent e) {
			checkPopup(e);
		}

		private void checkPopup (MouseEvent evt) {

			if (popup == null || !evt.isPopupTrigger()) {
				return;
			}

			if (popupNotify(popup, evt)) {
				popup.show((Component)evt.getSource(), evt.getX(), evt.getY());
			}
		}
	}


	/**
	 * This method is called just prior to this instances popup menu
	 * being popuped up. Override this method if you need to do anything
	 * to the popup prior to it being displayed. Return "true" if it is
	 * OK to show the popup, otherwise return "false".
	 */
	public boolean popupNotify (JPopupMenu popup, MouseEvent evt) {
		return (true);
	}
}
