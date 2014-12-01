package com.messners.ajf.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.text.Document;
import javax.swing.text.MaskFormatter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.messners.ajf.app.Resources;
import com.messners.ajf.reflect.ClassUtils;


/**
 * This class loads the ResourcveBundle for a class and can
 * then load GUI resources specified in the bundle.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ResourceLoader extends Resources implements UIConstants {

	public static final String CLOCK         = "clock";
	public static final String COLOR_CHOOSER = "color-chooser";
	public static final String COLOR_MENU    = "color-menu";
	public static final String TEXT_LABEL    = "text-label";
	public static final String BUTTON        = "button";
	public static final String CHECKBOX      = "checkbox";
	public static final String COMBOBOX      = "combobox";
	public static final String FILENAME      = "filename";
	public static final String GROUP         = "group";
	public static final String LABEL         = "label";
	public static final String LATITUDE      = "latitude";
	public static final String LONGITUDE     = "longitude";
	public static final String MATRIX        = "matrix";
	public static final String MENU          = "menu";
	public static final String MENUBAR       = "menubar";
	public static final String POPUP         = "popup";
	public static final String PULL_RIGHT    = "pull-right";
	public static final String RADIO_BUTTON  = "radio-button";
	public static final String RADIO_GROUP   = "radio-group";
	public static final String SPINNER       = "spinner";
	public static final String SLIDER        = "slider";
	public static final String TEXT_FIELD    = "text-field";
	public static final String TOGGLE        = "toggle";
	public static final String TOOLBAR       = "toolbar";
	public static final String TREE          = "tree";
	public static final String SPACER        = "-";
	public static final String SEPARATOR     = "|";

	public static final String DEFAULT_INTEGER_FORMAT = "###########0";
	public static final String DEFAULT_DOUBLE_FORMAT = "##########0.0#########";


	/**
	 * Cache for icons
	 */
	protected static HashMap<URL, Icon> _icons;
	static {
		_icons = new HashMap<URL, Icon>();
	}


	/**
	 * Caches for looked up components.
	 */
	protected HashMap<String, JComponent> componentCache = new HashMap<String, JComponent>();

	protected HashMap<JComponent, HashMap<String, JComponent>> containerCache;


	protected static HashMap<String, Method> _loaders;
	static {
		_loaders = new HashMap<String, Method>();

		addMethod(BUTTON,           "loadButton");
		addMethod(CHECKBOX,         "loadCheckBox");
		addMethod(CLOCK,            "loadClock");
		addMethod(COMBOBOX,         "loadComboBox");
		addMethod(FILENAME,         "loadFilenameTextField");
		addMethod(GROUP,            "loadGroupPanel");
		addMethod(LABEL,            "loadLabel");
		addMethod(LATITUDE,         "loadPositionEntry");
		addMethod(LONGITUDE,        "loadPositionEntry");
		addMethod(MATRIX,           "loadMatrix");
		addMethod(MENU,             "loadMenu");
		addMethod(MENUBAR,          "loadMenuBar");
		addMethod(POPUP,            "loadPopupMenu");
		addMethod(PULL_RIGHT,       "loadPullRightButton");
		addMethod(RADIO_BUTTON,     "loadRadioButton");
		addMethod(RADIO_GROUP,      "loadGroupPanel");
		addMethod(SLIDER,           "loadSlider");
		addMethod(SPINNER,          "loadSpinner");
		addMethod(TEXT_LABEL,       "loadTextLabel");
		addMethod(TEXT_FIELD,       "loadTextField");
		addMethod(TOGGLE,           "loadCheckBox");
		addMethod(TOOLBAR,          "loadToolBar");
		addMethod(TREE,             "loadTree");
	}

	protected static void addMethod (String name, String method_name) {

		try {

			Class<ResourceLoader> cls = ResourceLoader.class;
			Class<?> args[] = { String.class };
			Method m = cls.getMethod(method_name, args);
			if (m != null) {
				_loaders.put(name, m);
			}

		} catch (Exception e) {
		}
	}


	protected Class<?> _owner;
	protected ActionManager _action_mgr = null;
	protected static Insets _btn_insets = new Insets(0, 0, 0, 0);


	public ResourceLoader () {
		super();
		_owner = null;
	}


	/**
	 * Constructor that takes the ResourceBundle to load resources from
	 * as a parameter.
	 *
	 * @param  resources  the ResourceBundle to load resources from
	 */
	public ResourceLoader (ResourceBundle resources) {
		super(resources);
	}


	public ResourceLoader (Class<?> owner) {
		super(owner);
		_owner = owner;
	}


	public ResourceLoader (ActionManager action_mgr) {
		super();
		_owner = null;
		_action_mgr = action_mgr;
	}


	public ResourceLoader (Class<?> owner, ActionManager action_mgr) {
		super(owner);
		_owner = owner;
		_action_mgr = action_mgr;
	}


	public GuiApplication getApplication () {
		return (_action_mgr != null ? _action_mgr.getApplication() : null);
	}


	public ActionManager getActionManager () {
		return (_action_mgr);
	}


	public void setActionManager (ActionManager action_mgr) {
		_action_mgr = action_mgr;
	}


	/**
	 * Get an icon resource, returning null if it's not defined.
	 */
	public Icon getIconResource (String name) {
		return (getIcon(_owner, getResource(name)));
	}


	/**
	 * Get an icon from the specified path, returning null if it's not defined.
	 */
	public Icon getIcon (String iconName) {

		return (getIcon(_owner, iconName));
	}


	public static synchronized Icon getIcon (Class<?> owner, String iconName) {

		if (iconName == null || iconName.length() == 0) {
			return (null);
		}

		/*
		 * First use the top-level class to try and load the icon
		 */
		Icon icon = null;
		URL url = null;
		int char0 = iconName.charAt(0);

		if (owner != null) {

			url = owner.getResource(iconName);
			if (url != null) {

				icon = _icons.get(url);
				if (icon != null) {	
					return (icon);
				}
	
				icon = new ImageIcon(url);

			} else if (char0 != '/' && !iconName.startsWith("res/")) {

				url = owner.getResource("res/" + iconName);
				if (url != null) {

					/*
					 * If the Icon is in the cache return it.
					 */
					icon = _icons.get(url);
					if (icon != null) {	
						return (icon);
					}

					icon = new ImageIcon(url);
				}
			}
		}


		/*
		 * Couldn't use the top-level class so try this class
		 */
		if (icon == null) {
		
			url = ResourceLoader.class.getResource(iconName);
			if (url != null) {

				icon = _icons.get(url);
				if (icon != null) {	
					return (icon);
				}

				icon = new ImageIcon(url);

			} else if (char0 != '/' && !iconName.startsWith("res/")) {

				url = ResourceLoader.class.getResource("res/" + iconName);
				if (url != null) {

					icon = _icons.get(url);
					if (icon != null) {	
						return (icon);
					}

					icon = new ImageIcon(url);
				}
			}
		}


		if (icon == null) {
			return (null);
		}


		/*
		 * Cache the icon
		 */
		_icons.put(url, icon);
		return (icon);
	}


	/**
	 * Load the named component.
	 */
	public JComponent load (String name) {

		String type = getResource(name + ".type");
		if (type == null) {
			throw new RuntimeException(name + ".type not set");
		}


		Method m = _loaders.get(type);
		if (m == null) {
			throw new RuntimeException("\"" + type + "\" is not a valid type");
		}

		try {

			Object args[] = { name };
			JComponent c = (JComponent)m.invoke(this, args);
			if (c != null) {
				componentCache.put(name, c);
			}

			return (c);

		} catch (IllegalAccessException iae) {

			String msg = iae.getMessage();
			if (msg == null) {

				msg = "Problem creating component for \"" + name + "\" " +
				"[method=" + m.getName() + "()]";

			} else {

				msg = "Problem creating component for \"" + name + "\" " +
				"[method=" + m.getName() + "(), error=" + msg + "]";
			}

			RuntimeException re = new RuntimeException(msg, iae);
			throw (re);

		} catch (InvocationTargetException ite) {

			String msg;
			Throwable t = ite.getCause();
			if (t == null) {
				msg = ite.getMessage();
			} else {
				msg = t.getMessage();
			}

			if (msg == null) {

				msg = "Problem creating component for \"" + name + "\" " +
				"[method=" + m.getName() + "()]";

			} else {

				msg = "Problem creating component for \"" + name + "\" " +
				"[method=" + m.getName() + "(), error=" + msg + "]";
			}

			RuntimeException re = new RuntimeException(
					msg, (t != null ? t : ite));
			throw (re);
		}
	}


	/**
	 * Loads a ClockLabel instance.
	 */
	public ClockLabel loadClock (String name) {

		String format = getResource(name + ".format");
		ClockLabel clockLabel = new ClockLabel(format);
		setHorizontalAlignment(clockLabel, name);
		setBorder(clockLabel, name);
		clockLabel.setName(name);
		return (clockLabel);
	}


	/**
	 * Load a label from the resource file.
	 */
	public TextLabel loadTextLabel (String name) {
		
		String labelString = getResource(name + ".label");
		if (labelString == null) {
			throw new RuntimeException("label text is null: " + name);
		}

		TextLabel l = new TextLabel(labelString);
		setBorder(l, name);
		l.setName(name);
		return (l);
	}
		

	/**
	 * Load a label from the resource file.
	 */
	public JLabel loadLabel (String name) {
		
		Icon icon = getIconResource(name + ".icon");
		String text = getResource(name + ".label");
		return (loadLabel(name, text, icon));
	}


	protected void setIcon(JLabel label, String name, Icon icon) {

		if (icon == null) {
			return;
		}

		label.setIcon(icon);

		String s = getResource(name + ".gap");
		if (s != null) {

			try {

				label.setIconTextGap(Integer.parseInt(s));
			} catch (NumberFormatException ignore) {
			}
		}
		
		s = getResource(name + ".vertical-position");
		if (s != null) {
			if ("top".equals(s)) {
				label.setVerticalTextPosition(SwingConstants.TOP);
			} else if ("bottom".equals(s)) {
				label.setVerticalTextPosition(SwingConstants.BOTTOM);
			} else if ("center".equals(s)) {
				label.setVerticalTextPosition(SwingConstants.CENTER);
			}
		}
		
		s = getResource(name + ".horizontal-position");
		if (s != null) {
			if ("left".equals(s)) {
				label.setHorizontalTextPosition(SwingConstants.LEFT);
			} else if ("center".equals(s)) {
				label.setHorizontalTextPosition(SwingConstants.CENTER);
			} else if ("right".equals(s)) {
				label.setHorizontalTextPosition(SwingConstants.RIGHT);
			} else if ("leading".equals(s)) {
				label.setHorizontalTextPosition(SwingConstants.LEADING);
			} else if ("trailing".equals(s)) {
				label.setHorizontalTextPosition(SwingConstants.TRAILING);
			}
		}
	}

	protected void setHorizontalAlignment (JComponent c, String name) {
		
		String align = getResource(name + ".alignment");
		if (align == null) {
			return;
		}

		int alignment;
		if ("right".equals(align)) {
			alignment = SwingConstants.RIGHT;
		} else if ("center".equals(align)) {
			alignment = SwingConstants.CENTER;
		} else if ("left".equals(align)) {
			alignment = SwingConstants.LEFT;
		} else if ("leading".equals(align)) {
			alignment = SwingConstants.LEADING;
		} else if ("trailing".equals(align)) {
			alignment = SwingConstants.TRAILING;
		} else {
			return;
		}

		if (c instanceof JLabel) {
			((JLabel)c).setHorizontalAlignment(alignment);
		} else if (c instanceof AbstractButton) {
			((AbstractButton)c).setHorizontalAlignment(alignment);
		} else if (c instanceof JTextField) {
			((JTextField)c).setHorizontalAlignment(alignment);
		}
	}


	public void setColors (JComponent c, String name) {

		String bg = getResource(name + ".background");
		if (bg != null) {
			Color color = ColorUtils.stringToColor(bg);
			if (color != null) {
				c.setBackground(color);
				c.setOpaque(true);
			}
		}

		String fg = getResource(name + ".foreground");
		if (fg != null) {
			Color color = ColorUtils.stringToColor(fg);
			if (color != null) {
				c.setForeground(color);
			}
		}
	}


	public void setBorder (JComponent c, String name) {
		
		String border = getResource(name + ".border");
		if (border == null || border.length() == 0) {
			return;
		}

		if ("etched".equals(border)) {

			c.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEtchedBorder(),
				BorderFactory.createEmptyBorder(0, 2, 0, 2)));

		} else if ("raised-bevel".equals(border)) {

			c.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createEmptyBorder(0, 2, 0, 2)));

		} else if ("lowered-bevel".equals(border)) {

			c.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(0, 2, 0, 2)));

		} else if ("line".equals(border)) {

			Border outerBorder;
			String colorStr = getResource(name + ".line-color");
			if (colorStr != null) {
				
				try {

					Color color = Color.decode(colorStr);
					outerBorder = BorderFactory.createLineBorder(color);

				} catch (NumberFormatException nfe) {

					outerBorder =
						BorderUIResource.getBlackLineBorderUIResource();
				}

			} else {

				outerBorder = BorderUIResource.getBlackLineBorderUIResource();
			}

			c.setBorder(BorderFactory.createCompoundBorder(
				outerBorder, BorderFactory.createEmptyBorder(0, 2, 0, 2)));

		} else if ("titled".equals(border)) {

			String title = getResource(name + ".label");
			if (title == null) {
				title = getResource(name + ".title");
			}
		
			c.setBorder(new TitledPanelBorder(title));

		} else if ("none".equals(border)) {

			String title = getResource(name + ".label");
			if (title == null) {
				title = getResource(name + ".title");
			}

			if (title != null && title.length() > 0) {

				Border b  = BorderFactory.createEmptyBorder(
					TITLED_PANEL_INNER_TOP_MARGIN,
					TITLED_PANEL_INNER_LEFT_MARGIN,
					TITLED_PANEL_INNER_BOTTOM_MARGIN,
					TITLED_PANEL_INNER_RIGHT_MARGIN);
				
				c.setBorder(BorderFactory.createTitledBorder(b, title));
			}
		}
	}


	/**
	 * Load a label from the resource file.
	 */
	public JLabel loadLabel (String name, Object args[]) {

		Icon icon = getIconResource(name + ".icon");
		String text = getResourceMessage(name + ".label", null, args);
		return (loadLabel(name, text, icon));
	}


	protected JLabel loadLabel (String name, String text, Icon icon) {

		JLabel label;

		if (text != null) {

			int index = text.indexOf('&');
			if (index != -1 && text.length() - index > 1) {

				label = new JLabel(text.substring(0,index)
			   		.concat(text.substring(++index)));
				label.setDisplayedMnemonic(
				    Character.toLowerCase(text.charAt(index)));

			} else {

				label = new JLabel(text);
			}
		
			setIcon(label, name, icon);

		} else if (icon == null) {

			throw new RuntimeException(
				"label text and icon are null: " + name);

		} else {

			label = new JLabel(icon);
		}

		setColors(label, name);
		setToolTipText(label, name);
		setHorizontalAlignment(label, name);
		setBorder(label, name);
		label.setName(name);
		return (label);
	}


	/**
	 * Load and assign a tooltip.
	 */
	public void setToolTipText (JComponent c, String name) {

		String tooltip = getResource(name + ".tooltip");
		if (tooltip != null) {
			c.setToolTipText(tooltip);
		}
	}


	/**
	 * Load a button from the resource file.
	 */
	public JButton loadButton (String name) {
		
		String label = getResource(name + ".label");
		String tooltip = getResource(name + ".tooltip");

		JButton btn = loadButton(label, tooltip);
		Icon icon = getIconResource(name + ".icon");
		if (icon != null) {
			btn.setIcon(icon);
		}
		btn.setName(name);

		String str = getResource(name + ".margin");
		if (str != null) {
			int margin = Integer.parseInt(str);
			btn.setMargin(new Insets(margin, margin, margin, margin));
		}

		return (btn);
	}



	public static JButton loadButton (String label, String tooltip) {

		if (label == null) {
			throw new RuntimeException("button label is null");
		}

		JButton btn;
		int index = label.indexOf('&');
		if (index != -1 && label.length() - index > 1) {

			btn = new ButtonExt(label.substring(0,index)
				.concat(label.substring(++index)));
			btn.setMnemonic(Character.toLowerCase(label.charAt(index)));

		} else {

			btn = new ButtonExt(label);
		}

		if (tooltip != null) {
			btn.setToolTipText(tooltip);
		}

		return (btn);
	}


	public AbstractButton loadButton (String name, Action action) {

		if (action == null) {
			return (loadButton(name));
		}


		boolean isRollover = true;
		String str = getResource(name + ".rollover");
		if (str != null) {
			isRollover = Boolean.valueOf(str).booleanValue();
		}

		AbstractButton btn;
		
		Object selectable = action.getValue("selectable");
		if (selectable != null && "true".equals(selectable.toString())) {

			btn = new ToggleButtonExt(action);
			((ToggleButtonExt)btn).setRollover(isRollover);
			((ToggleButtonExt)btn).setRolloverEnabled(isRollover);

		} else {
			
			String type = getResource(name + ".type");
			if (TOGGLE.equals(type)) {

				btn = new ToggleButtonExt(action);
				((ToggleButtonExt)btn).setRollover(isRollover);
				((ToggleButtonExt)btn).setRolloverEnabled(isRollover);

			} else {

				btn = new ButtonExt(action);
				((ButtonExt)btn).setRollover(isRollover);
				((ButtonExt)btn).setRolloverEnabled(isRollover);
			}
		}

		str = getResource(name + ".margin");
		if (str != null) {
			int margin = Integer.parseInt(str);
			btn.setMargin(new Insets(margin, margin, margin, margin));
		}

		Icon icon = getIconResource(name + ".icon");
		if (icon != null) {
			btn.setIcon(icon);
		}

		setToolTipText(btn, name);

		action.addPropertyChangeListener((PropertyChangeListener)btn);

		return (btn);
	}


	/**
	 * Load a pull-right button from the resource file.
	 */
	public PullRightButton loadPullRightButton (String name) {
		
		String label = getResource(name + ".label");
		String tooltip = getResource(name + ".tooltip");

		if (label == null) {
			throw new RuntimeException("button label is null");
		}

		Icon icon = getIconResource(name + ".icon");

		PullRightButton btn;
		int index = label.indexOf('&');
		if (index != -1 && label.length() - index > 1) {

			btn = new PullRightButton(label.substring(0,index)
				.concat(label.substring(++index)), icon);
			btn.setMnemonic(Character.toLowerCase(label.charAt(index)));

		} else {

			btn = new PullRightButton(label, icon);
		}

		if (tooltip != null) {
			btn.setToolTipText(tooltip);
		}


		JPopupMenu popup = loadPopupMenu(name);
		if (popup != null) {

			AppAction action = new PullRightButtonAction(popup);
			btn.setPopup(popup);
			btn.addActionListener(action);
			action.componentAdded(btn);
			btn.setEnabled(action.isEnabled());
			action.addPropertyChangeListener(btn);
		}

		setHorizontalAlignment(btn, name);
		return (btn);
	}



	/**
	 * Load an radio button from the resource file.
	 */
	public JRadioButton loadRadioButton (String name) {

		String label = getResource(name + ".label");
		if (label == null) {
			throw new RuntimeException(
				"radio button label is null: " + name);
		}

		RadioButtonExt btn;
		int index = label.indexOf('&');
		if (index != -1 && label.length() - index > 1) {

			btn = new RadioButtonExt(label.substring(0,index)
				.concat(label.substring(++index)));
			btn.setMnemonic(Character.toLowerCase(label.charAt(index)));

		} else {
			btn = new RadioButtonExt(label);
		}

		setToolTipText(btn, name);

		String value = getResource(name + ".value");
		if (value != null) {
			btn.setValue(value);
		}

		btn.setName(name);
		return (btn);
	}


	/**
	 * Load a checkbox from the resource file.
	 */
	public JCheckBox loadCheckBox (String name) {

		String label = getResource(name + ".label");
		if (label == null) {

			throw new RuntimeException("checkbox label is null: " + name);
		}

		JCheckBox btn;
		int index = label.indexOf('&');
		if (index != -1 && label.length() - index > 1) {
			btn = new JCheckBox(label.substring(0,index)
				.concat(label.substring(++index)));
			btn.setMnemonic(Character.toLowerCase(label.charAt(index)));
		} else {
			btn = new JCheckBox(label);
		}

		setToolTipText(btn, name);

		return (btn);
	}



	/**
	 * Load a JTextField from the resource file.
	 */
	public JTextField loadTextField (String name) {

		int width;
		String s = getResource(name + ".width", null);
		if (s == null) {

			width = -1;

		} else {
		
			try {

				width = Integer.parseInt(s);

			} catch (NumberFormatException nfe) {

				throw new RuntimeException(
					"invalid textfield width (" + s + "): " + name);
			}
		}
			
		String mask = getResource(name + ".mask");
		MaskFormatter formatter;
		if (mask != null) {

			try {

				formatter = new MaskFormatter(mask);

			} catch (Exception e) {

				throw new RuntimeException(
						"formatted text mask is invalid: " + name);
			}

		} else {

			formatter = null;
		}

		String valid_chars = getResource(name + ".valid-chars");
		if (valid_chars != null) {

			if (formatter == null) {
				formatter = new MaskFormatter();
			}

			formatter.setValidCharacters(valid_chars);
		}


		JTextField fld;
		if (formatter != null) {

			fld = new JFormattedTextField(formatter);
			if (width > 0) {
				fld.setColumns(width);
			}

		} else if (width == -1) {

			fld = new JTextField();
			setDocument(fld, name);

		} else {

			fld = new JTextField(width);
			setDocument(fld, name);
		}


		String editable = getResource(name + ".editable");
		if ("false".equals(editable) || "no".equals(editable)) {
			fld.setEditable(false);
		} else {
			fld.setEditable(true);
		}


		setHorizontalAlignment(fld, name);

		String value = getResource(name + ".value");
		if (value != null) {
			fld.setText(value);
		}

		setToolTipText(fld, name);
		fld.setName(name);
		return (fld);
	}


	/**
	 * Setup the specified document for the text field.
	 */
	protected void setDocument (JTextField fld, String name) {

		String document = getResource(name + ".document");
		if (document == null) {
			return;
		}

		int maxWidth;
		String s = getResource(name + ".max-width", null);
		if (s == null) {

			maxWidth = -1;

		} else {
		
			try {

				maxWidth = Integer.parseInt(s);

			} catch (NumberFormatException nfe) {

				throw new RuntimeException(
					"invalid textfield max-width (" + s + "): " + name);
			}
		}



		if (!document.equals("alpha-numeric") && 
					!document.equals("double") &&
					!document.equals("integer")) {

			try {

				Class<?> docClass = ClassUtils.loadClass(document);
				if (docClass != null) {
					Document doc = (Document)docClass.newInstance();
					fld.setDocument(doc);
				}

			} catch (Exception e) {
				throw new RuntimeException(
					"invalid Document class " + document);
			}
			
			return;
		}


		if ("alpha-numeric".equals(document)) {

			AlphaNumericDocument doc = new AlphaNumericDocument();
			if (maxWidth > 0) {
				doc.setMaxCharacters(maxWidth);
			}

			fld.setDocument(doc);
			return;
		}


		Double maxValue;
		s = getResource(name + ".max-value", null);
		if (s == null) {

			maxValue = null;

		} else {
		
			try {

				maxValue = new Double(s);

			} catch (NumberFormatException nfe) {

				throw new RuntimeException(
					"invalid textfield max-value(" + s + "): " + name);
			}
		}

		Double minValue;
		s = getResource(name + ".min-value", null);
		if (s == null) {

			minValue = null;

		} else {
		
			try {

				minValue = new Double(s);

			} catch (NumberFormatException nfe) {

				throw new RuntimeException(
					"invalid textfield min-value(" + s + "): " + name);
			}
		}
			

		if ("integer".equals(document)) {
			
			int radix;
			s = getResource(name + ".radix", null);
			if (s == null) {

				radix = -1;

			} else {
		
				try {

					radix = Integer.parseInt(s);

				} catch (NumberFormatException nfe) {

					throw new RuntimeException(
						"invalid textfield radix (" + s + "): " + name);
				}
			}

			IntegerDocument doc = new IntegerDocument();
			if (radix > 0) {
				doc.setRadix(radix);
			}

			if (minValue != null) {
				doc.setMinimumValue(minValue.intValue());
			}

			if (maxValue != null) {
				doc.setMaximumValue(maxValue.intValue());
			}

			if (maxWidth > 0) {
				doc.setMaxLength(maxWidth);
			}

			fld.setDocument(doc);

		} else if ("double".equals(document)) {
			
			DoubleDocument doc = new DoubleDocument();

			if (minValue != null) {
				doc.setMinimumValue(minValue);
			}

			if (maxValue != null) {
				doc.setMaximumValue(maxValue);
			}

			if (maxWidth > 0) {
				doc.setMaxLength(maxWidth);
			}

			fld.setDocument(doc);

		} else {

			throw new RuntimeException(
				"invalid textfield document (" + document + "): " + name);
		}
	}


	/**
	 * Creates a combo box from the resource file.
	 */
	public JComboBox<?> loadComboBox (String name) {

		String items[] = splitResource(name + ".items");
		ComboBoxExt cb;
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				items[i] = items[i].replace('_', ' ');
			}

			cb = new ComboBoxExt(items);	

		} else {
			cb = new ComboBoxExt();	
		}

		String editable = getResource(name + ".editable");
		if ("true".equals(editable) || "yes".equals(editable)) {
			cb.setEditable(true);
		} else {
			cb.setEditable(false);
		}

		cb.setName(name);
		return (cb);
	}

	
	/**
	 * Creates a JSlider.
	 */
	public JSlider loadSlider (String name) {


		JSlider s;

		String orientation = getResource(name + ".orientation");
		if (orientation == null || !orientation.equalsIgnoreCase("vertical")) {

			s = new JSlider(JSlider.HORIZONTAL);

		} else {

			s = new JSlider(JSlider.VERTICAL);
		}
		
		s.setName(name);
		Hashtable<Integer, Component> labels = new Hashtable<Integer, Component>();
		
		String min   = getResource(name + ".min");
		if (min != null) {
			s.setMinimum(Integer.parseInt(min));
		}
		
		String max   = getResource(name + ".max");
		if (max != null) {
			s.setMaximum(Integer.parseInt(max));
			
		}
		
		String value = getResource(name + ".value");
		if (value != null) {
			s.setValue(Integer.parseInt(value));
		}
		
		if (labels.size() > 0) {
			s.setLabelTable(labels);
			s.setPaintLabels(true);
		}
		
		return (s);
	}



	/**
	 * Creates a JSpinner and sets up the model if needed.
	 */
	public JSpinner loadSpinner (String name) {


		JSpinner s;
		String model = getResource(name + ".spinner-model");
		if (model == null) {

			s = new JSpinner();
			s.setName(name);

		} else if ("integer".equals(model)) {

			s = createIntegerSpinner(name);

		} else if ("double".equals(model)) {

			s = createDoubleSpinner(name);

		} else if ("date".equals(model)) {

			s = createDateSpinner(name);

		} else {

			s = new JSpinner();
			s.setName(name);
		}

		setSpinnerWidth(s, name);
		return (s);
	}


	protected void setSpinnerWidth (JSpinner s, String name) {

		String w = getResource(name + ".width");
		if (w == null) {
			return;
		}

		try {

			int width = Integer.parseInt(w);

			JComponent editor = s.getEditor();
			if (editor instanceof JSpinner.DefaultEditor) {

				((JSpinner.DefaultEditor)editor).getTextField().
					setColumns(width);

			} else if (editor instanceof SpinnerNumberEditor) {

				((SpinnerNumberEditor)editor).setColumns(width);
			}

		} catch (NumberFormatException nfe) {
		}
	}


	protected JSpinner createIntegerSpinner (String name) {

		String value = getResource(name + ".value");
		String min   = getResource(name + ".min");
		String max   = getResource(name + ".max");
		String increment = getResource(name + ".increment");
		
		SpinnerNumberModel snm = new SpinnerNumberModel();
		if (value != null) {
			snm.setValue(new Integer(value));
		}

		if (min != null) {
			snm.setMinimum(new Integer(min));
		}

		if (max != null) {
			snm.setMaximum(new Integer(max));
		}

		if (increment != null) {
			snm.setStepSize(new Integer(increment));
		}

		JSpinner s = new JSpinner(snm);
		s.setName(name);

		String format = getResource(name + ".format");
		if (format != null && format.length() > 0) {
			s.setEditor(new SpinnerNumberEditor(s, format));
		} else {
			s.setEditor(new SpinnerNumberEditor(s, DEFAULT_INTEGER_FORMAT));
		}

		return (s);
	}


	protected JSpinner createDoubleSpinner (String name) {

		String value = getResource(name + ".value");
		String min   = getResource(name + ".min");
		String max   = getResource(name + ".max");
		String increment = getResource(name + ".increment");
		
		SpinnerNumberModel snm = new SpinnerNumberModel();
		if (value != null) {
			snm.setValue(new Double(value));
		}

		if (min != null) {
			snm.setMinimum(new Double(min));
		}

		if (max != null) {
			snm.setMaximum(new Double(max));
		}

		if (increment != null) {
			snm.setStepSize(new Double(increment));
		}

		JSpinner s = new JSpinner(snm);

		String format = getResource(name + ".format");
		if (format != null && format.length() > 0) {
			s.setEditor(new SpinnerNumberEditor(s, format));
		} else {
			s.setEditor(new SpinnerNumberEditor(s, DEFAULT_DOUBLE_FORMAT));
		}

		return (s);
	}


	protected JSpinner createDateSpinner (String name) {

		String value = getResource(name + ".value");
		String min   = getResource(name + ".min");
		String max   = getResource(name + ".max");
		String increment = getResource(name + ".increment");
		
		SpinnerDateModel sdm = new SpinnerDateModel();
		if (value != null) {
			sdm.setValue(value);
		}

		if (min != null) {
		}

		if (max != null) {
		}

		if (increment != null) {
		}

		JSpinner s = new JSpinner(sdm);

		String format = getResource(name + ".format");
		if (format != null && format.length() > 0) {
			s.setEditor(new JSpinner.DateEditor(s, format));
		}

		return (s);
	}


	/**
	 * Loads a group from the resource file.
	 *
	 * @param name the group property name 
	 */
	public PanelExt loadGroupPanel (String name) {

		if (name == null) {
			return (null);
		}


		PanelExt group  = new PanelExt();
		group.setName(name);
		setBorder(group, name);


		/*
		 * If there is a "heading" property then create the heading label
		 * and add it to the left of the component group.
		 */
		PanelExt p;
		String heading = getResource(name + ".heading");
		if (heading != null) {

			/*
			 * We need to create the panel to hold the actual group and the
			 * layout the group panel with the heading on the left and the
			 * component group on the right
			 */
			double sizes[][] = {
				{TableLayout.PREFERRED, 12, TableLayout.PREFERRED},
				{TableLayout.PREFERRED}
			};

			TableLayout layout = new TableLayout(sizes);
			TableLayoutConstraints tlc = new TableLayoutConstraints();
			group.setLayout(layout);
			
			tlc.col1 = tlc.col2 = 0;
			tlc.row1 = tlc.row2 = 0;
			tlc.hAlign = TableLayout.RIGHT;
			tlc.vAlign = TableLayout.TOP;
			JLabel l = new JLabel(heading);
			group.add(l, tlc);

			tlc.col1 = tlc.col2 = 2;
			tlc.row1 = tlc.row2 = 0;
			tlc.hAlign = TableLayout.LEFT;
			tlc.vAlign = TableLayout.TOP;

			p = new PanelExt();
			group.add(p, tlc);

		} else {

			p = group;
		}
		

		/*
		 * Set the layout based on the number of columns specified as follows:
		 *
		 * layout specified: the specified layout (currently only
		 *                   LabeledPairLayout is supported).
		 * columns < 2 || not set: layout is ColumnLayout
		 * columns > 1: layout is GridLayout
		 */
		int numColumns = 1;
		String s = getResource(name + ".columns");
		if (s != null) {

			try {

				numColumns = Integer.parseInt(s);

			} catch (NumberFormatException nfe) {
			}
		}


		int numRows = 0;
		s = getResource(name + ".rows");
		if (s != null) {

			try {

				numRows = Integer.parseInt(s);

			} catch (NumberFormatException nfe) {
			}
		}


		ButtonGroupExt btnGroup;
		String type = getResource(name + ".type");
		if ("radio-group".equals(type)) {

			btnGroup = new ButtonGroupExt();
			group.setButtonGroup(btnGroup);

		} else {

			btnGroup = null;
		}


		/*
		 * Get the items that are in this group, use the count to
		 * help compute the layout and then create and add the items
		 */
		String items[] = splitResource(name + ".items");
		int numItems = (items == null ? 0 : items.length);


		boolean isLabeledPair = false;
		Component spacer = null;
		String layout = getResource(name + ".layout");
		if ("labeled-pair".equals(layout)) {

			isLabeledPair = true;
			spacer = Box.createVerticalStrut(11);
			LabeledPairLayout lpl = new LabeledPairLayout();
			lpl.setStretchWidth(true);
			p.setLayout(lpl);

			if (numItems == 0) {
				return (group);
			}

		} else if (numColumns < 2) {

			p.setLayout(new ColumnLayout());

			if (numItems == 0) {
				return (group);
			}

		} else {

			if (numItems == 0 && numRows == 0) {
				return (group);
			}

			if (numRows < 1) {
				numRows = Math.round((float)numItems / (float)numColumns);
			}

			p.setLayout(new GridLayout(numRows, numColumns, 12, 5));

			if (numItems == 0) {
				return (group);
			}
		}

		for (int i = 0; i < numItems; i++) {

			String itemName = items[i];

			if (SPACER.equals(itemName)) {

				if (isLabeledPair) {
					p.add(spacer, LabeledPairLayout.SPACE);
				} else {
					p.add(Box.createVerticalStrut(11));
				}

			} else if (name.equals(SEPARATOR)) {

				if (!isLabeledPair) {

					p.add(new LineSeparator(LineSeparator.HORIZONTAL, 2, 6));

				} else {

					p.add(new LineSeparator(LineSeparator.HORIZONTAL, 0, 0),
						LabeledPairLayout.LABEL);
					p.add(new LineSeparator(LineSeparator.HORIZONTAL, 0, 0),
						LabeledPairLayout.FIELD);
				}

			} else {

				JComponent c = load(itemName);

				if (c != null) {

					if (c instanceof JLabel || c instanceof TextLabel) {

						if (isLabeledPair) {

							p.add(spacer, LabeledPairLayout.LABEL);
							p.add(c, LabeledPairLayout.FIELD);

						} else {

							p.add(c);
						}

					} else if (isLabeledPair) {

						String label = getResource(itemName + ".label");
						if (label != null) {

							JLabel l = loadLabel(itemName);
							l.setName(null);
							p.add(l, LabeledPairLayout.LABEL);
							p.add(c, LabeledPairLayout.FIELD);

						} else {

							p.add(spacer, LabeledPairLayout.LABEL);
							p.add(c, LabeledPairLayout.FIELD);
						}

					} else {

						p.add(c);
					}

					if (btnGroup != null && c instanceof AbstractButton) {
						btnGroup.add((AbstractButton)c);
					}
				}
			}
		}

		return (group);
	}



	/**
	 * Loads a menubar from the resource file.
	 *
	 * @param name the property with the white space separated
	 *                list of menus
	 */
	public JMenuBar loadMenuBar (String name) {

		if (name == null) {
			return (null);
		}

		JMenuBar menuBar = new JMenuBar();
		menuBar.setName(name);
		menuBar.setBorderPainted(true);

		String menus[] = splitResource(name + ".items");
		if (menus != null) {
			for (int i = 0; i < menus.length; i++) {

				name = menus[i];
				JMenu menu = loadMenu(name);
				if (menu == null) {
					continue;
				}

				menuBar.add(menu);

				/* NOT YET IMPLEMENTED BY JMenuBar
				String type = getResource(name + ".type");
				if ("help-menu".equals(type)) {
					menuBar.setHelpMenu(menu);
				}
				*/
			}
		}

		return (menuBar);
	}


	/**
	 * Loads a menu from the resource file.
	 * The white space separated list of menu items is obtained from
	 * the property named <code><i>name</i></code>. The menu label is
	 * obtained from the <code><i>name</i>.label</code> property.
	 *
	 * @param name the menu name
	 * @return  a JMenu 
	 */
	public JMenu loadMenu (String name) {

		if (name == null) {
			return (null);
		}
		
		String label = getResource(name + ".label");
		if (label == null) {
			throw new RuntimeException("menu label is null: " + name);
		}


		JMenu menu;
		String type = getResource(name + ".type");

		int index = label.indexOf('&');
		if ("color-menu".equals(type)) {

			ColorMenu cm = new ColorMenu();
			if (index != -1 && label.length() - index > 1) {

				menu = cm.createMenuItem(label.substring(0,index)
					.concat(label.substring(++index)));
				menu.setMnemonic(Character.toLowerCase(label.charAt(index)));

			} else {

				menu = cm.createMenuItem(label);
			}
		
			AppAction appaction = _action_mgr.getAction(name);
			if (appaction != null) {
				cm.addActionListener(appaction);
			} else {
				menu.setEnabled(false);
			}

			menu.setName(name);
			return (menu);
		}


		if (index != -1 && label.length() - index > 1) {

			menu = new JMenu(label.substring(0,index)
				.concat(label.substring(++index)));
			
			menu.setMnemonic(Character.toLowerCase(label.charAt(index)));

		} else {

			menu = new JMenu(label);
		}
		

		/*
		 * Check for "recent-files" menu.
		 */
		if ("recent-files".equals(type)) {

			RecentFilesMenu rfm = RecentFilesMenu.add(
				name, _action_mgr.getAction(name),
				_action_mgr.getApplication().getPreferences(), menu);
			rfm.rebuild(menu);
			menu.setName(name);
			return (menu);

		}


		ButtonGroupExt group = null;
		if ("button-group".equals(type)) {
			group = new ButtonGroupExt();
		}
		
		String menuItems[] = splitResource(name + ".items");
		if (menuItems != null) {

			for (int i = 0; i < menuItems.length; i++) {

				String menuItemName = menuItems[i];
				if (menuItemName.equals("-")) {

					menu.addSeparator();

				} else {

					loadMenuItem(menu, menuItemName, group);
				}
			}
		}

		menu.setName(name);
		return (menu);
	}


	/**
	 * Loads a toolbar from the resource file.
	 *
	 * @param name The property with the white space separated list
	 * of tool bar buttons
	 */
	public JToolBar loadToolBar (String name) {

		JToolBar toolBar = new JToolBar();
		toolBar.setBorderPainted(false);
		toolBar.setFloatable(false);

		int orientation;
		String orient = getResource(name + ".orientation");
		JComponent mgr1;
		JComponent mgr2;

		if (orient == null || !orient.equalsIgnoreCase("vertical")) {

			orientation = JToolBar.HORIZONTAL;
			toolBar.setLayout(new BorderLayout());

			mgr1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
			mgr2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
			toolBar.add(mgr1, BorderLayout.WEST);
			toolBar.add(mgr2, BorderLayout.EAST);

		} else {

			orientation = JToolBar.VERTICAL;
			mgr1 = toolBar;
			mgr2 = toolBar;
			ColumnLayout cl = new ColumnLayout(0, 3);
			cl.setYSpacing(2);
			toolBar.setLayout(cl);
			toolBar.add(Box.createVerticalGlue());
		}

		toolBar.setOrientation(orientation);
		toolBar.setMargin(_btn_insets);

		String buttons[] = splitResource(name + ".items");
		addToolComponents(mgr1, mgr2, orientation, null, buttons);

		toolBar.setName(name);
		return (toolBar);
	}


	protected void addToolComponents (
		JComponent mgr1, JComponent mgr2, int orientation,
		ButtonGroup group, String buttons[]) {
	
		if (buttons == null) {
			return;
		}


		for (int i = 0; i < buttons.length; i++) {

			String name = buttons[i];

			String type = getResource(name + ".type");
			if (name.equals(SPACER)) {

				mgr1.add(Box.createHorizontalStrut(11));

			} else if (name.equals(SEPARATOR)) {

				if (orientation == JToolBar.VERTICAL) {
					mgr1.add(new LineSeparator(
							LineSeparator.HORIZONTAL, 2, 6));
				} else {
					mgr1.add(new LineSeparator(
							LineSeparator.VERTICAL, 6, 2));
				}

			} else if ("button-group".equals(type)) {

				String group_btns[] = splitResource(name + ".items");
				ButtonGroup new_group = new ButtonGroup();
				addToolComponents(mgr1, mgr2,
					orientation, new_group, group_btns);
			
			} else {

				JComponent c = loadToolComponent(name);
				if (c != null) {

					c.setName(name);
					String alignment = getResource(name + ".alignment");

					if (c instanceof AbstractButton &&
							"right".equals(alignment)) {

						mgr2.add(c);
						((AbstractButton)c).setHorizontalAlignment(
													SwingConstants.RIGHT);

					} else {

						mgr1.add(c);
					}
				}
				
				if (c instanceof AbstractButton) {
					if (group != null) {
						group.add((AbstractButton)c);
					}
				}
			}
		}
	}


	/**
	 * Loads a tool bar button. The tooltip is constructed from
	 * the <code><i>name</i>.label</code> and
	 * <code><i>name</i>.shortcut</code> properties and the icon is loaded
	 * from the resource named by the <code><i>name</i>.toolbar-icon</code>
	 * property, if not found then the icon is specified by the
	 * <code><i>name</i>.icon</code> property.
	 *
	 * @param name  the name of the button
	 */
	public JComponent loadToolComponent (String name) {

		String type = getResource(name + ".type");
		if ("combobox".equals(type)) {
			return (loadToolComboBox(name));
		}

		if ("color-chooser".equals(type)) {
			return (loadToolColorChooser(name));
		}

		String iconName;
		iconName = getResource(name + ".toolbar-icon");
		if (iconName == null) {
			iconName = getResource(name + ".icon");
		}

		Icon icon = null;
		String label = null;
		if (iconName != null) {
			icon = getIcon(iconName);
		}

		String str = getResource(name + ".show-label");
		boolean showLabel = 
				(str != null && Boolean.valueOf(str).booleanValue());

		label = Utilities.cleanupMenuLabel(
					getResource(name + ".toolbar-label"));
		if (showLabel && label == null) {
			label = Utilities.cleanupMenuLabel(getResource(name + ".label"));
		}

		boolean noLabelOrIcon = (label == null && icon == null);

		AbstractButton btn = null;
		str = getResource(name + ".rollover");
		boolean is_rollover = true;
		if (str != null) {
			is_rollover = Boolean.valueOf(str).booleanValue();
		}

		String pullright = getResource(name + ".pull-right");
		if (type == null) {

			if ("true".equals(pullright)) {

				btn = new PullRightButton(label, icon);
				btn.setHorizontalAlignment(JLabel.LEFT);
				((PullRightButton)btn).setRollover(is_rollover);
				((PullRightButton)btn).setRolloverEnabled(is_rollover);
				((PullRightButton)btn).setShowToolBarLabel(showLabel);

			} else {

				btn = new ButtonExt(label, icon);
				((ButtonExt)btn).setRollover(is_rollover);
				((ButtonExt)btn).setRolloverEnabled(is_rollover);
				((ButtonExt)btn).setShowToolBarLabel(showLabel);
			}


		} else if ("checkbox".equals(type)) {

			btn = new CheckBoxExt(label, icon);
			((CheckBoxExt)btn).setRollover(is_rollover);
			((CheckBoxExt)btn).setRolloverEnabled(is_rollover);
			((CheckBoxExt)btn).setShowToolBarLabel(showLabel);

			Icon pressedIcon = getIconResource(name + ".pressed-icon");
			if (pressedIcon != null) {
				btn.setPressedIcon(pressedIcon);
			}


			Icon selectedIcon = getIconResource(name + ".selected-icon");
			if (selectedIcon != null) {
				btn.setSelectedIcon(selectedIcon);
			}

			str = getResource(name + ".default");
			if ("true".equals(str)) {
				btn.setSelected(true);
			}

		} else if ("toggle".equals(type)) {

			btn = new ToggleButtonExt(label, icon);
			((ToggleButtonExt)btn).setRollover(is_rollover);
			((ToggleButtonExt)btn).setRolloverEnabled(is_rollover);
			((ToggleButtonExt)btn).setShowToolBarLabel(showLabel);

			str = getResource(name + ".default");
			if ("true".equals(str)) {
				btn.setSelected(true);
			}

		} else {

			System.err.println("Tool button type is invalid: " + type);
			return (null);
		}

		btn.setMargin(_btn_insets);
		btn.setRequestFocusEnabled(false);

		String tooltip = getResource(name + ".tooltip");
		if (tooltip == null) {

			if (label != null) {
				tooltip = label;
			} else {
				tooltip = getResource(name + ".label");
			}
		}

		String shortcut = getResource(name + ".shortcut");

		if (tooltip != null) {
			tooltip = Utilities.cleanupMenuLabel(tooltip);
			if (shortcut != null) {
				tooltip = tooltip + " (" + shortcut + ")";
			}
			btn.setToolTipText(tooltip);
		}

		int index = name.indexOf('@');
		String actionCommand;
		if (index != -1) {

			actionCommand = name.substring(index + 1);
			name = name.substring(0, index);

		} else {

			actionCommand = null;
		}

		String actionStr = getResource(name + ".action");
		if (actionStr == null) {
			actionStr = name;
		}

		AppAction action;
		if ("true".equals(pullright)) {

			JPopupMenu popup = loadPopupMenu(name);
			if (popup != null) {
				action = new PullRightButtonAction(popup);
				btn.setHorizontalAlignment(JLabel.LEFT);
				((PullRightButton)btn).setPopup(popup);
			} else {
				action = null;
			}
				
		} else {
				
			action = _action_mgr.getAction(actionStr);
		}

		if (action == null) {

			if (noLabelOrIcon) {
				System.err.println("Tool button icon and label null: " + name);
				return (null);
			}

			btn.setEnabled(false);

		} else {

			if (noLabelOrIcon) {

				icon = (Icon)action.getValue(AppAction.SMALL_ICON);
				label = (String)action.getValue(AppAction.NAME);
				if (icon != null) {
					btn.setIcon(icon);
				} else if (label != null) {
					btn.setText(label);
				} else {
					System.err.println(
						"Tool button icon and label null: " + name);
					return (null);
				}

				if (tooltip == null) {
					tooltip = (String)action.getValue(
									AppAction.SHORT_DESCRIPTION );
					btn.setToolTipText(tooltip);
				}
			}

			btn.addActionListener(action);
			action.componentAdded(btn);
			btn.setEnabled(action.isEnabled());
			btn.setActionCommand(actionCommand);

			action.addPropertyChangeListener(
				(PropertyChangeListener)btn);
		}

		return (btn);
	}


	/**
	 * Creates a combobox to put on a toolbar.
	 */
	protected ComboBoxExt loadToolComboBox (String name) {

		String items[] = splitResource(name + ".items");
		ComboBoxExt cb;
		if (items != null) {
			cb = new ComboBoxExt(items);	
		} else {
			cb = new ComboBoxExt();	
		}

		String editable = getResource(name + ".editable");
		if ("true".equals(editable) || "yes".equals(editable)) {
			cb.setEditable(true);
		} else {
			cb.setEditable(false);
		}

		String actionStr = getResource(name + ".action");
		if (actionStr == null) {
			actionStr = name;
		}

		AppAction action = _action_mgr.getAction(actionStr);
		if (action == null) {
			cb.setEnabled(false);
		} else {
			cb.addActionListener(action);
			action.componentAdded(cb);
			cb.setEnabled(action.isEnabled());
			action.addPropertyChangeListener(cb);
		}

		return (cb);
	}


	/**
	 * Loads a FilenameTextField from the resources.
	 *
	 * @param  name  the name of the resource
	 * @return the created FilenameTextField
	 */
	public FilenameTextField loadFilenameTextField (String name) {
		return (new FilenameTextField(this, name));
	}


	/**
	 * Loads a NameValueTableModel for use with a JTable.
	 *
	 * @param  model  the name of the model resource
	 * @return the created NameValueTableModel
	 */
	public NameValueTableModel loadNameValueTableModel (String model) {

		String names[] = splitResource(model);
		if (names == null || names.length == 0) {
			return (null);
		}

		return (new NameValueTableModel(this, model));
	}

	
	/**
	 * Creates a ColorChooser to put on a toolbar.
	 */
	protected ColorChooser loadToolColorChooser (String name) {

		ColorChooser cc = new ColorChooser();
		cc.setName(name);

		AppAction action = _action_mgr.getAction(name);
		if (action == null) {

			cc.setEnabled(false);

		} else {

			cc.addActionListener(action);
			action.componentAdded(cc);
			cc.setEnabled(action.isEnabled());
			action.addPropertyChangeListener(cc);
		}

		return (cc);
	}


	/**
	 *
	 *
	 * @param name The property with the white space separated list of
	 * menu items
	 */
	public PopupMenuExt loadPopupMenu (String name) {

		PopupMenuExt menu = new PopupMenuExt();
		String title = getResource(name + ".title");
        if (title != null) {
				
			JLabel l = new JLabel(title);   
			l.setName(name + ".title");   
			setColors(l, name + ".title");   
			l.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));   
    
			String iconName = getResource(name + ".title.icon");   
			if (iconName != null) {   
				Icon icon = getIcon(iconName);   
				if (icon != null) {   
					l.setIcon(icon);   
				}   
			}   

			setHorizontalAlignment(l, name + ".title");
			menu.add(l);   
		} 

		String menuItems[] = splitResource(name + ".items");
		if (menuItems != null) {

			for (int i = 0; i < menuItems.length; i++) {

				String menuItemName = menuItems[i];
				if (menuItemName.equals("-")) {

					menu.addSeparator();
					continue;
				} 

				loadMenuItem(menu, menuItemName, null);
			}
		}

		menu.setName(name);
		return (menu);
	}


	/**
	 * Loads a menu item from the resource file.
	 * The menu label is obtained from the <code><i>name</i>.label</code>
	 * property. The keyboard shortcut is obtained from the
	 * <code><i>name</i>.shortcut</code> property.
	 *
	 * @param name The menu item name
	 */
	public JMenuItem loadMenuItem (
		JComponent menu, String name, ButtonGroup btn_group) {

		/*
		 * Is this an option menu?
		 */
		if (name.startsWith("%")) {
			JMenuItem mi = loadMenu(name.substring(1));
			if (mi != null) {
				menu.add(mi);
			}

			return (mi);
		}

		String arg;
		int index = name.indexOf('@');
		if (index != -1) {
			arg = name.substring(index + 1);
			name = name.substring(0, index);
		} else {
			arg = null;
		}

		String action = getResource(name.concat(".action"));
		if (action == null) {
			action = name;
		}

		JMenuItem mi;
		boolean bIsToggle = false;
		boolean bIsRadio  = false;
		String type = getResource(name.concat(".type"));

		if (type != null && type.equals("button-group")) {

			String btns[] = splitResource(name + ".items");
			if (btns == null) {
				return (null);
			}

			ButtonGroup group = new ButtonGroup();
			int num_buttons = btns.length;
			for (int i = 0; i < num_buttons; i++) {
				mi = loadMenuItem(menu, btns[i], group);
				if (mi != null) {
					menu.add(mi);
				}
			}
			
			return (null);
		}
		
		String label = getResource(name.concat(".label"));
		String keyStroke = getResource(name.concat(".shortcut"));
		if (label == null) {
			System.err.println("Menu item label is null: " + name);
			return (null);
		}

		if (type != null) {

			if (type.equals("toggle") || type.equals("checkbox")) {
				bIsToggle = true;
			} else if (type.equals("radio") || type.equals("radio-button")) {
				bIsRadio = true;
			}
		}

		if (keyStroke != null && keyStroke.length() != 0) {
			index = keyStroke.indexOf(' ');
			if (index == -1) {
				_action_mgr.addKeyBinding(
							Utilities.parseKeyStroke(keyStroke), name);
			} else {
				_action_mgr.addKeyBinding(
					Utilities.parseKeyStroke(keyStroke.substring(0,index)), 
					Utilities.parseKeyStroke(keyStroke.substring(index+1)),
					name);
			}
		}


		index = label.indexOf('&');
		if (index != -1 && label.length() - index > 1) {

			/*
			 * Create the menu item
			 */
			if (bIsToggle) {

				mi = new ToggleMenuItemExt(
					label.substring(0, index)
					.concat(label.substring(++index)),
					keyStroke);

				String str = getResource(name + ".default");
				if ("true".equals(str)) {
					mi.setSelected(true);
				}

				if (btn_group != null) {
					btn_group.add(mi);
				}

			} else if (bIsRadio) {

				mi = new RadioMenuItemExt(
					label.substring(0, index)
					.concat(label.substring(++index)),
					keyStroke);

				String str = getResource(name + ".default");
				if ("true".equals(str)) {
					mi.setSelected(true);
				}

				if (btn_group != null) {
					btn_group.add(mi);
				}

				String value = getResource(name + ".value");
				if (value != null) {
					((RadioMenuItemExt)mi).setValue(value);
				}


			} else {
	
				String iconName = getResource(name + ".icon");
				Icon icon = null;
				if (iconName != null) {
					icon = getIcon(iconName);
				}

				mi = new MenuItemExt(
					label.substring(0, index)
					.concat(label.substring(++index)),
					icon, keyStroke);
			}

			mi.setMnemonic(Character.toLowerCase(label.charAt(index)));

		} else {


			/*
			 * Create the menu item
			 */
			if (bIsToggle) {

				mi = new ToggleMenuItemExt(label, keyStroke);

				String str = getResource(name + ".default");
				if ("true".equals(str)) {
					mi.setSelected(true);
				}

				if (btn_group != null) {
					btn_group.add(mi);
				}

			} else if (bIsRadio) {

				mi = new RadioMenuItemExt(label, keyStroke);

				String str = getResource(name + ".default");
				if ("true".equals(str)) {
					mi.setSelected(true);
				}

				String value = getResource(name + ".value");
				if (value != null) {
					((RadioMenuItemExt)mi).setValue(value);
				}

				if (btn_group != null) {
					btn_group.add(mi);
				}

			} else {
	
				String iconName = getResource(name + ".icon");
				Icon icon = null;
				if (iconName != null) {
					icon = getIcon(iconName);
				}

				mi = new MenuItemExt(label, icon, keyStroke);
			}
		}

		mi.setActionCommand(arg);

		AppAction a;
		if (_action_mgr != null) {
			a = _action_mgr.getAction(action);
		} else {
			a = null;
		}

		if (a == null) {

			if (!"none".equals(action)) {
				mi.setEnabled(false);
			}

		} else {

			if (mi instanceof ToggleMenuItemExt) {
				a.addPropertyChangeListener((ToggleMenuItemExt)mi);
			} else if (mi instanceof RadioMenuItemExt) {
				a.addPropertyChangeListener((RadioMenuItemExt)mi);
			} else {
				a.addPropertyChangeListener((MenuItemExt)mi);
			}

			a.componentAdded(mi);
			mi.addActionListener(a);
			mi.setEnabled(a.isEnabled());
		}

		setToolTipText(mi, name);
		mi.setName(name);
		menu.add(mi);
		return (mi);
	}


	/**
	 * Loads a tree from the resource file.
	 *
	 * @param name The property with the white space separated list
	 * of tree nodes
	 */
	public TreeExt loadTree (String name) {

		TreeExt tree = new TreeExt(loadNode(name, true));
		tree.setName(name);
		tree.putClientProperty("JTree.lineStyle", "None");
		tree.setShowsRootHandles(false);
		tree.setRootVisible(getResource(name + ".label") != null);
		tree.expandPath(tree.getPathForRow(0));
		tree.getSelectionModel().setSelectionMode(
			TreeSelectionModel.SINGLE_TREE_SELECTION);

		String editable = getResource(name + ".editable");
		if ("true".equals(editable) || "yes".equals(editable)) {
			tree.setEditable(true);
		} else {
			tree.setEditable(false);
		}

		if ("true".equals(editable) || "yes".equals(editable)) {
			tree.setDragEnabled(true);
		} else {
			tree.setDragEnabled(false);
		}

		Icon openIcon = getIconResource(name + ".icon.open");
		openIcon = (openIcon == null) ? getIcon(null, "DownTriangle16.gif") : openIcon;
		tree.setOpenIcon(openIcon);

		Icon closedIcon = getIconResource(name + ".icon.closed");
		closedIcon = (closedIcon == null) ? getIcon(null, "RightTriangle16.gif") : closedIcon;
		tree.setClosedIcon(closedIcon);

		Icon leafIcon = getIconResource(name + ".icon.leaf");
		leafIcon = (leafIcon == null) ? getIcon(null, "Blank16.gif") : leafIcon;
		tree.setLeafIcon(leafIcon);

		// Should we make this a resource?
		tree.expandAllNodes();

		return (tree);
	}


	/**
	 * Loads a tree node from resource file.
	 *
	 * @param name The tree node name.
	 * @param isRoot Indicates root node.
	 */
	private DefaultMutableTreeNode loadNode (String name, boolean isRoot) {

		DefaultMutableTreeNode node = null;
		if (isRoot) {
			String rootLabel = getResource(name + ".label");
			node = new DefaultMutableTreeNode(
				(rootLabel == null) ? "" : rootLabel);
		} else {
			node = new DefaultMutableTreeNode(load(name));
		}
		String nodes[] = splitResource(name + ".nodes");
		if (nodes != null) {
			for (int i = 0; i < nodes.length; i++) {
				node.add(loadNode(nodes[i], false));
			}
		}
		return node;
	}


	/**
	 * Loads a matrix from the resource file.
	 *
	 * @param name Name of the matrix resource.
	 */
	public PanelExt loadMatrix (String name) {

		PanelExt matrix = new PanelExt();
		matrix.setName(name);
		setBorder(matrix, name);

		String colsRes = getResource(name + ".cols");
		int numCols = (colsRes == null) ? 0 : Integer.parseInt(colsRes);
		
		String rowsRes = getResource(name + ".rows");
		int numRows = (rowsRes == null) ? 0 : Integer.parseInt(rowsRes);

		String[] colHdrs = splitResource(name + ".cols.hdrs");
		if (numCols == 0) {
			if (colHdrs == null) {
				throw new RuntimeException(name + ".cols not set");
			} else {
				numCols = colHdrs.length;
			}
		} 
		
		String[] rowHdrs = splitResource(name + ".rows.hdrs");
		if (numRows == 0) {
			if (rowHdrs == null) {
				throw new RuntimeException(name + ".rows not set");
			} else {
				numRows = rowHdrs.length;
			}
		}

		String spcRes = getResource(name + ".spacing");
		int spc = (spcRes == null) ? 0 : Integer.parseInt(spcRes);

		double cols[] = (rowHdrs == null) ? new double[2*numCols] : new double[2*(numCols + 1)];
		for (int i = 0; i < cols.length; i++) {
			cols[i] = ((i % 2) == 0) ? TableLayout.PREFERRED : spc;
		}

		double rows[] = (colHdrs == null) ? new double[2*numRows] : new double[2*(numRows + 1)];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = ((i % 2) == 0) ? TableLayout.PREFERRED : spc;
		}

		double size[][] = { cols, rows };
		matrix.setLayout(new TableLayout(size));

		TableLayoutConstraints cons = new TableLayoutConstraints();
		cons.row1 = cons.row2 = 0;
		if (colHdrs != null) {
			cons.col1 = cons.col2 = (rowHdrs == null) ? 0 : 2;
			JComponent[] hdrs = new JComponent[colHdrs.length];
			int prefWidth = 0;
			for (int i = 0; i < colHdrs.length; i++) {
				hdrs[i] = load(colHdrs[i]);
				matrix.add(hdrs[i], cons);
				cons.col1 = cons.col2 += 2;
				int width = hdrs[i].getPreferredSize().width;
				prefWidth = (width > prefWidth) ? width : prefWidth;
			}
			for (int i = 0; i < hdrs.length; i++) {
				int height = hdrs[i].getPreferredSize().height;
				hdrs[i].setPreferredSize(new Dimension(prefWidth, height));
			}
		}

		cons.col1 = cons.col2 = 0;
		if (rowHdrs != null) {
			cons.row1 = cons.row2 = (colHdrs == null) ? 0 : 2;
			for (int i = 0; i < rowHdrs.length; i++) {
				matrix.add(load(rowHdrs[i]), cons);
				cons.row1 = cons.row2 += 2;
			}

		}

		cons.row1 = cons.row2 = (colHdrs == null) ? 0 : 2;
		ArrayList<ArrayList<JToggleButton>> rowComps = new ArrayList<ArrayList<JToggleButton>>();
		for (int i = 0; i < numRows; i++) {
			cons.col1 = cons.col2 = (rowHdrs == null) ? 0 : 2;
			ArrayList<JToggleButton> colComps = new ArrayList<JToggleButton>();
			for (int j = 0; j < numCols; j++) {
				// TODO:  Add support for other components inside the matrix cell.
				JToggleButton btn = new JToggleButton();
				btn.setName("toggle[" + i + ", " + j + "]");
				colComps.add(btn);
				matrix.add(btn, cons);
				cons.col1 = cons.col2 += 2;
			}
			cons.row1 = cons.row2 += 2;
			rowComps.add(colComps);
		}
		matrix.setUserObject(rowComps);

		return (matrix);
	}
	

	/**
	 * Loads a dialog with an indefinite JProgressBar and a cancel button.
	 *
	 * @param  owner the owning component, the JDialog will be originally
	 * centered over the owner
	 * @param  name  the resource name for the dialog
	 * @return the JDialog instance
	 */
	public JDialog loadProgressBarDialog (Component owner, String name) {

		String message = getResource(name + ".message");
		if (message == null) {
			throw new RuntimeException("message not found");
		}

		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);

		ColumnLayout layout = new ColumnLayout(16, 16);
		layout.setStretchWidth(true);
		JPanel p = new JPanel(layout);
		p.add(new JLabel(message));
		p.add(progressBar);

		JOptionPane progressPane = new JOptionPane(
				p, JOptionPane.PLAIN_MESSAGE, JOptionPane.CANCEL_OPTION);
		Object options[] = { getResource(name + ".cancel", "Cancel") };
		progressPane.setOptions(options);
		JDialog dialog = progressPane.createDialog(
			owner, getResource(name + ".title"));

		return (dialog);
	}


	/**
	 * Gets the component associated with the specified name.  Assumes that
	 * the component was created by calling load(String name) on this
	 * ResourceLoader instance and that <code>name</code> is unique.
	 *
	 * @param  name  the name of the component to lookup
	 * @return the found component, null if not found
	 */
	public JComponent lookup (String name) {
		return componentCache.get(name);
	}

		
	/**
	 * Gets the component associated with the specified name.  Assumes that
	 * the component s contained by the provided container and that
	 * <code>name</code> is unique.
	 *
	 * @param  container the container to lookup the component in
	 * @param  name  the name of the component to lookup
	 * @return the found component, null if not found
	 */
	public JComponent lookup (JComponent container, String name) {

		JComponent c;
		if (containerCache == null) {
			containerCache = new HashMap<JComponent, HashMap<String, JComponent>>();
		}


		/*
		 * First look in the container cache, then thru the component hierarchy
		 */
		HashMap<String, JComponent> cache = containerCache.get(container);
		if (cache != null) {

			c = cache.get(name);
			if (c != null) {
				return (c);
			}
		}


		if (cache == null) {
			cache = new HashMap<String, JComponent>();
			containerCache.put(container, cache);
		}

		c = (JComponent)Utilities.findChild(container, name);
		if (c != null) {
			cache.put(name, c);
		}

		return (c);
	}


	/**
	 * Adds the specified component as the field in a LabeledPairLayout,
	 * the label for the pair is also created if the resource has a
	 * label assigned.
	 *
	 * @param  p  the container for the component
	 * @param  name  the name of the component
	 * @return  the created component
	 */
	public JComponent addLabeledPair (JPanel p, String name) {

		JComponent c = load(name);

		if (c == null) {
			return (c);
		}


		if (c instanceof JLabel || c instanceof TextLabel) {

			Component spacer = Box.createVerticalStrut(11);
			p.add(spacer, LabeledPairLayout.LABEL);
			p.add(c, LabeledPairLayout.FIELD);

		} else {

			String label = getResource(name + ".label");
			if (label != null) {

				JLabel l = loadLabel(name);
				p.add(l, LabeledPairLayout.LABEL);
				p.add(c, LabeledPairLayout.FIELD);

			} else {

				Component spacer = Box.createVerticalStrut(11);
				p.add(spacer, LabeledPairLayout.LABEL);
				p.add(c, LabeledPairLayout.FIELD);
			}
		}

		return (c);
	}


	/**
	 * This class is an ActionListener that is used to popup a menu
	 * next to a pull right toolbar button.
	 */
	private class PullRightButtonAction extends AbstractAppAction {

        private static final long serialVersionUID = 1L;
        private JPopupMenu popup;

		public PullRightButtonAction (JPopupMenu popup) {
			this.popup = popup;
		}

		/**
		 * This method is called when a component with this action is activated.
	 	 */
		public void actionPerformed (ActionEvent e) {

			Component source = (Component)e.getSource();
			popup.show(source, source.getWidth(), 0);
			popup.setInvoker(source);
		}
	}
}
