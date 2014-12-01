package com.messners.ajf.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


/**
 * This class defines a pull-right menu component  used to select a color.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ColorMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	public static final String COLOR_CHANGE = "color-change";

	protected JDialog _chooser_dialog  = null;
	protected JColorChooser _chooser   = null;
	protected ColorMenu _me  = null;
	protected MenuItem _menuitem;
	protected String _title  = "Pick a color";
	protected String _other_label = "Other...";
	protected JLabel _color_label;
	protected JButton _other_btn = null;

	protected boolean _need_focus = false;

	protected Color _selected_color = null;
	protected ArrayList<ColorChangeListener> _listeners = new ArrayList<ColorChangeListener>();
	protected ArrayList<ActionListener> _action_listeners = new ArrayList<ActionListener>();

	protected static Border _border = null;
	protected static Border _highlight_border = null;
	static {
		Border inner = BorderFactory.createCompoundBorder(
			BorderFactory.createEmptyBorder(1, 1, 1, 1),
			BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		_border = BorderFactory.createCompoundBorder(
			BorderFactory.createEmptyBorder(2, 2, 2, 2), inner);
	
		_highlight_border = BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.black), inner);
	}
	

	protected static Color _colors[] = {
		new Color(0,     0,   0),
		new Color(128, 128, 128),
		new Color(128,   0,   0),
		new Color(0,   128,   0),
		new Color(128, 128,   0),
		new Color(0,     0, 128),
		new Color(128,   0, 128),
		new Color(0,   128, 128),
		new Color(255, 255, 255),
		new Color(192, 192, 192),
		new Color(255,   0,   0),
		new Color(0,   255,   0),
		new Color(255, 255,   0),
		new Color(0,     0, 255),
		new Color(255,   0, 255),
		new Color(0,   255, 255)
	};



	public ColorMenu () {

		ResourceBundle resources = ResourceBundle.getBundle(
			ColorMenu.class.getName(),
			java.util.Locale.getDefault(),
			ColorMenu.class.getClassLoader());
			
		_title = resources.getString("title");
		_other_label = resources.getString("other-btn.label");

		setLayout(new BorderLayout());

		_me = this;
		Container c = this;
		c.setLayout(new BorderLayout());

		JPanel label_container = new JPanel(new BorderLayout());
		label_container.setBorder(BorderFactory.createEmptyBorder(4, 6, 0, 6));
		_color_label = new JLabel(" ");
		_color_label.setOpaque(true);
		_color_label.setBorder(
				BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		label_container.add(_color_label, BorderLayout.CENTER);
		c.add(label_container, BorderLayout.NORTH);

		JPanel buttons = new JPanel(new GridLayout(4, 4, 2 ,2));
		buttons.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		ColorBox box;

		int index = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				box  = new ColorBox(_colors[index]);
				box.setBorder(_border);
				buttons.add(box);
				index++;
			}
		}

		c.add(buttons, BorderLayout.CENTER);

		_other_btn = new JButton(_other_label);
		_other_btn.addActionListener(new OtherListener());

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		panel.add(_other_btn);
		c.add(panel, BorderLayout.SOUTH);

		Dimension size = getPreferredSize();
		setSize(size);
	}


	public void setLocationUnderComponent (Component c) {

		if (c == null) {
			return;
		}

		int h = c.getHeight();
		Point location = new Point(0, h + 2);
		SwingUtilities.convertPointToScreen(location, c);
		setLocation(location);
	}


	public void setLocationUnderInvoker () {

		Component invoker = getInvoker();
		if (invoker == null) {
			return;
		}

		int h = invoker.getHeight();
		Point location = new Point(0, h + 2);
		SwingUtilities.convertPointToScreen(location, invoker);
		setLocation(location);
	}


	public void setOtherButtonLabel (String text) {

		if (text == null) {
			return;
		}

		_other_label = text;
		_other_btn.setText(text);
	}


	public void setDialogTitle (String title) {

		if (title == null) {
			return;
		}

		_title = title;
		if (_chooser_dialog != null) {
			_chooser_dialog.setTitle(title);
		}
	}



	/**
	 *
	 */
	public synchronized void addColorChangeListener (
		ColorChangeListener l) {

		if (!_listeners.contains(l)) {
			_listeners.add(l);
		}
	}


	/**
	 *
	 */
	public synchronized void removeColorChangeListener (
		ColorChangeListener l) {

		if (!_listeners.contains(l)) {
			_listeners.remove(l);
		}
	}


	/**
	 *
	 */
	public void fireColorChangeEvent (Color new_color) {

		/*
		 * Make a copy of the listener object vector so that it cannot
		 * be changed while we are firing events
		 */
		ArrayList<ColorChangeListener> fireList;
		synchronized(this) {
			if (_listeners.size() < 1) {
				return;
			}

			fireList = new ArrayList<ColorChangeListener>(_listeners.size());
			fireList.addAll(_listeners);
		}

		/*
		 * Create the event object
		 */
		ColorChangeEvent evt = new ColorChangeEvent(this, new_color);

		/*
		 * Fire the event to all listeners
		 */
		for (ColorChangeListener listener : fireList) {
			listener.colorChange(evt);
		}
	}


	public Color getSelectedColor () {
		return (_selected_color);
	}


	public void setSelectedColor (Color c) {

		_selected_color = c;
		if (c != null) {

			_color_label.setBackground(c);

		} else {

		}
	}


	class OtherListener extends AbstractAction {
		
		private static final long serialVersionUID = 1L;

		public void actionPerformed (ActionEvent evt) {

			if (_chooser_dialog == null) {
				createDialog((Component)evt.getSource());
				_chooser_dialog.pack();
			}

			_chooser_dialog.setVisible(true);
		}
	}


	class OkListener extends AbstractAction {
		
		private static final long serialVersionUID = 1L;

	    public void actionPerformed (ActionEvent evt) {

			if (_menuitem != null && _menuitem.isVisible()) {
				_menuitem.getParent().setVisible(false);
				_menuitem.cancel();
			}

			_me.setVisible(false);
			_chooser_dialog.setVisible(false);
			setSelectedColor(_chooser.getColor());
			fireColorChangeEvent(_selected_color);
			fireActionEvent();
		}
	}



	JDialog createDialog (Component c) {

		_chooser = new JColorChooser();
        	OkListener ok_listener  = new OkListener();
        	_chooser_dialog = JColorChooser.createDialog(c,
			_title,
			true,
			_chooser,
			ok_listener,
			null);
	
		return (_chooser_dialog);
	}



	class ColorBox extends JComponent implements MouseListener {
		
		private static final long serialVersionUID = 1L;

		int _color_width  = 18;
		int _color_height = 18;
		Color _color = null;

		ColorBox (Color color) {
			super();
			addMouseListener(this);
			_color = color;
		}

		ColorBox (Color color, int width, int height) {
			super();
			_color_width  = width;
			_color_height = height;
			addMouseListener(this);
			_color = color;
		}

		public Color getColor () {
			return (_color);
		}

		public Dimension getPreferredSize () {
			return (new Dimension(
				_color_width + 6,
				_color_height + 6));
		}

		public void drawHighlight () {
			setBorder(_highlight_border);
			repaint();
		}

		public void undrawHighlight () {

			setBorder(_border);
			repaint();
		}

		public void paintComponent (Graphics g) {

			g.setColor(_color);
			g.fillRect(3, 3, getWidth() - 6, _color_height - 2);
		}


		/**
		 * Invoked when the mouse has been clicked on a component.
		 */
		public void mouseClicked (MouseEvent e) {


			if (_menuitem != null && _menuitem.isVisible()) {
				_menuitem.cancel();
			}

			setSelectedColor(getColor());
			_me.setVisible(false);
			undrawHighlight();
			fireColorChangeEvent(_selected_color);
			fireActionEvent();
		}


		/**
		 * Invoked when the mouse enters a component.
		 */
		public void mouseEntered (MouseEvent e) {
	
			drawHighlight();
		}
	
	
		/**
		 * Invoked when the mouse exits a component.
		 */
		public void mouseExited (MouseEvent e) {
	
			undrawHighlight();
		}


		/**
		 * Invoked when a mouse button has been pressed on a component.
		 */
		public void mousePressed (MouseEvent e) {
		}
	

		/**
		 * Invoked when a mouse button has been released on a component.
		 */
		public void mouseReleased (MouseEvent e) {
		}
	}



	/**
	 *
	 */
	public synchronized void addActionListener (ActionListener l) {

		if (!_action_listeners.contains(l)) {
			_action_listeners.add(l);
		}
	}


	/**
	 *
	 */
	public synchronized void removeActionListener (ActionListener l) {
		_action_listeners.remove(l);
	}


	/**
	 *
	 */
	public void fireActionEvent () {

		/*
		 * Make a copy of the listener object vector so that it cannot
		 * be changed while we are firing events
		 */
		ArrayList<ActionListener> fireList;
		synchronized(this) {
			if (_action_listeners.size() < 1) {
				return;
			}

			fireList = new ArrayList<ActionListener>(_action_listeners.size());
			fireList.addAll(_action_listeners);
		}

		/*
		 * Create the event object
		 */
		ActionEvent evt = new ActionEvent(
			this, 0, ColorMenu.COLOR_CHANGE);

		/*
		 * Fire the event to all listeners
		 */
		for (ActionListener l : fireList) {
			l.actionPerformed(evt);
		}
	}


	public JMenu createMenuItem (String label) {

		MenuItem menuitem = new MenuItem(label, this);
		_menuitem = menuitem;
		return (menuitem);
	}


	public class MenuItem extends JMenu implements MenuListener, ActionListener {
		
		private static final long serialVersionUID = 1L;

		ColorMenu cm;
		Timer timer;

		public MenuItem (String label, ColorMenu cm) {

			super(label);
			this.cm = cm;
			addMenuListener(this);
			createWinListener(cm);
  			timer = new Timer(getDelay(), this);
			timer.setRepeats(false);
		}


		public void cancel () {
			fireMenuCanceled();
		}


		/**
		 * Invoked when the menu is canceled. 
		 */
		public void menuCanceled (MenuEvent e) {
			timer.stop();
			cm.setVisible(false);
		}


		/**
		 * Invoked when the menu is deselected. 
		 */
		public void menuDeselected (MenuEvent e) {
			timer.stop();
			cm.setVisible(false);
		}


		/**
		 * Invoked when a menu is selected. 
		 */
		public void menuSelected (MenuEvent e) {

  			timer.start();
		}


		public void actionPerformed (ActionEvent evt) {

			Point p = getPopupMenuOrigin();
			cm.show(this, p.x, p.y);
		}
	}
}
