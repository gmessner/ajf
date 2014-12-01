package com.messners.ajf.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.UIManager;
import javax.swing.border.Border;


/**
 * This class defines a component that can be put on a toolbar for choosing 
 * foreground and background colors.
 *
 * @author  Greg Messner <greg@messners.com>
 */

public class ColorChooser extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;

	public static final String COLOR_CHANGE = "color-change";


	JDialog _chooser_dialog  = null;
	JColorChooser _chooser   = null;

	protected int _side_length = 12;

	protected String _background_tip;
	protected String _foreground_tip;
	protected String _title;

	protected ColorChooserBox _background_btn = null;
	protected ColorChooserBox _foreground_btn = null;
	protected ColorChooserBox _invoker = null;

	protected Color _background_color = Color.black;
	protected Color _foreground_color = Color.white;

	protected Color _shadow    = UIManager.getColor("Separator.shadow");
	protected Color _highlight = UIManager.getColor("Separator.highlight");

	protected ArrayList<ColorChangeListener> _listeners = new ArrayList<ColorChangeListener>();
	protected ArrayList<ActionListener> _action_listeners = new ArrayList<ActionListener>();
	

	protected static Color _colors[] = {
		new Color(0,     0,   0),
		new Color(128, 128, 128),
		new Color(128,   0,   0),
		new Color(128, 128,   0),
		new Color(0,   128,   0),
		new Color(0,     0, 128),
		new Color(128,   0, 128),
		new Color(0,   128, 128),
		new Color(255, 255, 255),
		new Color(192, 192, 192),
		new Color(255,   0,   0),
		new Color(255, 255,   0),
		new Color(0,   255,   0),
		new Color(0,     0, 255),
		new Color(255,   0, 255),
		new Color(0,   255, 255)
	};


	public ColorChooser () {
		this(12);
	}


	public ColorChooser (int side_length) {

		_side_length = side_length;

		ResourceBundle resources = ResourceBundle.getBundle(
			ColorChooser.class.getName(),
			java.util.Locale.getDefault(),
			ColorChooser.class.getClassLoader());
			
		_title = resources.getString("title");
		_background_tip = resources.getString("background.tooltip");
		_foreground_tip = resources.getString("foreground.tooltip");

		setLayout(new BorderLayout());
		Border border = BorderFactory.createEtchedBorder();
		setBorder(border);

		JPanel colors = new JPanel(new GridLayout(2, 8, 1 ,1));
		colors.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		ColorChooserBox box;

		int index = 0;
		side_length = _side_length - (_side_length / 6);
		for (int i = 0; i < 4; i++) {

			for (int j = 0; j < 4; j++) {

				box  = new ColorChooserBox(_colors[index],
						side_length, side_length);
				colors.add(box);
				index++;
			}
		}

		add(colors, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		buttons.setLayout(new ColorChooserLayout(buttons));
		
		_foreground_btn = new ColorChooserBox(
				_foreground_color, _side_length, _side_length);
		_foreground_btn.setToolTipText(_foreground_tip);
		buttons.add(_foreground_btn);
		_background_btn = new ColorChooserBox(
				_background_color, _side_length, _side_length);
		_background_btn.setToolTipText(_background_tip);
		buttons.add(_background_btn);
		add(buttons, BorderLayout.WEST);
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
	public void fireActionEvent (int which_color) {

		/*
		 * Make a copy of the listener object vector so that it cannot
		 * be changed while we are firing events
		 */
		ArrayList<ActionListener> v;
		synchronized(this) {
			if (_action_listeners.size() < 1) {
				return;
			}
			
			v = new ArrayList<ActionListener>(_action_listeners.size());
			v.addAll(_action_listeners);
		}

		/*
		 * Create the event object
		 */
		ActionEvent evt = new ActionEvent(
			this, which_color, ColorChooser.COLOR_CHANGE);

		/*
		 * Fire the event to all listeners
		 */
		int count = v.size();
		for (int i = 0; i < count; i++) {
			ActionListener l = v.get(i);
			l.actionPerformed(evt);
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
		_listeners.remove(l);
	}


	/**
	 *
	 */
	public void fireColorChangeEvent (Color new_color, int which_color) {

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
		ColorChangeEvent evt = new ColorChangeEvent(
			this, new_color, which_color);

		/*
		 * Fire the event to all listeners
		 */
		for (ColorChangeListener l : fireList) {
			l.colorChange(evt);
		}
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


	public Color getBackgroundColor () {
		return (_background_color);
	}


	public void setBackgroundColor (Color color) {
		_background_btn.setColor(color);
	}


	public Color getForegroundColor () {
		return (_foreground_color);
	}


	public void setForegroundColor (Color color) {
		_foreground_btn.setColor(color);
	}


	public void popupDialog (ColorChooserBox invoker) {

		if (_chooser_dialog == null) {
			createDialog((Component)invoker);
			_chooser_dialog.pack();
		}

		_invoker = invoker;
		_chooser.setColor(invoker.getColor());
		_chooser_dialog.setVisible(true);
	}


	JDialog createDialog (Component c) {

		Frame frame = Utilities.getFrame(this);
		Cursor cursor = frame.getCursor();
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		frame.update(frame.getGraphics());

		_chooser = new JColorChooser();
		OkListener ok_listener  = new OkListener();
		_chooser_dialog = JColorChooser.createDialog(
			c, _title, true, _chooser, ok_listener, null);

		frame.setCursor(cursor);
		return (_chooser_dialog);
	}


	class OkListener extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed (ActionEvent evt) {

			_chooser_dialog.setVisible(false);
			
			if (_invoker == _background_btn) {
				_background_btn.setColor(_chooser.getColor());
			} else if (_invoker == _foreground_btn) {
				_foreground_btn.setColor(_chooser.getColor());
			}
		}
	}



	class ColorChooserBox extends JComponent implements MouseListener {
		
		private static final long serialVersionUID = 1L;
		int _width  = 10;
		int _height = 10;
		Color _color = null;

		ColorChooserBox (Color color) {
			super();
			addMouseListener(this);
			_color = color;
			setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			setTip();
		}

		ColorChooserBox (Color color, int width, int height) {
			super();
			_width  = width;
			_height = height;
			addMouseListener(this);
			_color = color;
			setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			setTip();
		}


		protected void setTip () {
			if (_color == null) {
				return;
			}

			setToolTipText(new String("RGB(" + _color.getRed() +
				", " + _color.getGreen() + ", " +
				_color.getBlue() + ")"));
		}
			

		public Color getColor () {
			return (_color);
		}


		public void setColor (Color color) {

			if (_color != color) {
				_color = color;
				repaint();

				if (this == _background_btn) {
					_background_color = color;
					fireActionEvent(
						ColorChangeEvent.BACKGROUND);
					fireColorChangeEvent(color,
						ColorChangeEvent.BACKGROUND);
				} else {
					_foreground_color = color;
					fireActionEvent(
						ColorChangeEvent.FOREGROUND);
					fireColorChangeEvent(color,
						ColorChangeEvent.FOREGROUND);
				}
			}
		}

		public Dimension getPreferredSize () {
			return (new Dimension(_width, _height));
		}


		public void drawHighlight (Graphics g) {

			g.setColor(Color.black);
			g.drawRect(0, 0, _width - 1, _height - 1);
		}


		public void undrawHighlight (Graphics g) {

			int w = _width  - 1;
			int h = _height - 1;

			if (this == _background_btn 
					|| this == _foreground_btn) {
				g.setColor(_highlight);
			} else {
				g.setColor(_shadow);
			}

			g.drawLine(0, 0, w, 0);
			g.drawLine(0, 0, 0, h);

			if (this == _background_btn 
					|| this == _foreground_btn) {
				g.setColor(_shadow);
			} else {
				g.setColor(_highlight);
			}

			g.drawLine(h, 0, h, h);
			g.drawLine(w, h, 0, h);
		}


		public void paintComponent (Graphics g) {

			g.setColor(_color);
			g.fillRect(1, 1, _width - 2, _height - 2);
			undrawHighlight(g);
		}


		/**
		 * Invoked when the mouse has been clicked on a component.
		 */
		public void mouseClicked (MouseEvent e) {

			if (!isEnabled()) {
				return;
			}

			if (this == _background_btn) {
				popupDialog(this);		
			} else if (this == _foreground_btn) {
				popupDialog(this);		
			}
		}


		/**
		 * Invoked when the mouse enters a component.
		 */
		public void mouseEntered (MouseEvent e) {

			if (!isEnabled()) {
				return;
			}

			if (this != _background_btn 
					&& this != _foreground_btn) {

				drawHighlight(getGraphics());
			}
		}

	
	
		/**
		 * Invoked when the mouse exits a component.
		 */
		public void mouseExited (MouseEvent e) {

			if (!isEnabled()) {
				return;
			}

			if (this != _background_btn 
					&& this != _foreground_btn) {

				undrawHighlight(getGraphics());
			}
		}


		/**
		 * Invoked when a mouse button has been pressed on a component.
		 */
		public void mousePressed (MouseEvent e) {

			if (!isEnabled()) {
				return;
			}

			if (this != _background_btn 
					&& this != _foreground_btn) {

				if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == 0) {
					_background_btn.setColor(_color);
				} else {
					_foreground_btn.setColor(_color);
				}
			}
		}
	

		/**
		 * Invoked when a mouse button has been released on a component.
		 */
		public void mouseReleased (MouseEvent e) {
		}
	}



	class ColorChooserLayout extends OverlayLayout {

		private static final long serialVersionUID = 1L;

		public ColorChooserLayout (Container c) {
			super(c);
		}


		public void layoutContainer (Container c) {

			Component[] children = c.getComponents();
			if (children == null) {
				return;
			}
			
			for (int i = 0; i < children.length; i++) {
				if (children[i] == _background_btn) {
					layoutBackgroundButton(c);
				} else if (children[i] == _foreground_btn) {
					layoutForegroundButton(c);
				}
			}
		}



		void layoutBackgroundButton (Container c) {

			Dimension bsize = _background_btn.getPreferredSize();
			_background_btn.setSize(bsize);
			_background_btn.setLocation(11, bsize.height - 3);
		}


		void layoutForegroundButton (Container c) {

			Dimension bsize = _foreground_btn.getPreferredSize();
			_foreground_btn.setSize(bsize);
			_foreground_btn.setLocation(4, 3);
		}


		public Dimension minimumLayoutSize (Container c) {

			Dimension size = _background_btn.getPreferredSize();
			size.width  = size.width  + size.width + 2;
			size.height = size.height + size.height - 1;
			return (size);
		}
  

		public Dimension preferredLayoutSize (Container c) {
			return minimumLayoutSize(c);
		}
	}


	public void propertyChange (java.beans.PropertyChangeEvent e) {
		if (e.getPropertyName().equals("enabled")) {
			setEnabled(((Boolean)e.getNewValue()).booleanValue());
		}
	}


	public void setEnabled (boolean flag) {

		if (super.isEnabled() != flag) {
			super.setEnabled(flag);
			Utilities.setChildrenEnabled(this, flag);
		}
	}
}
