package com.messners.ajf.ui;


import java.awt.Color;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import java.io.Serializable;
import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * This class provides a visual component that can be used for setting
 * property values.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class PropertyList extends JPanel {

	private static final long serialVersionUID = 1L;
	protected static Insets _btn_insets = new Insets(0, 0, 0, 0);
	protected Hashtable<String, Icon> _icons = new Hashtable<String, Icon>();
	protected ResourceBundle _bundle;
	protected ResourceLoader _loader;

	protected Hashtable<String, Serializable> _components = new Hashtable<String, Serializable>();


	/**
	 *
	 */
	public PropertyList (String name, ResourceLoader loader) {

		_loader = loader;
		_bundle = loader.getResourceBundle();
		
		setName(name);
		ColumnLayout layout = new ColumnLayout(4, 4);
		layout.setExpandWidth(true);
		layout.setStretchWidth(true);
		setLayout(layout);
		setBackground(Color.white);
		

		String items[] = getStrings(name);
		loadComponents(this, null, items);
	}
	
	
	/**
	 *
	 */
	public PropertyList (String name, ResourceBundle bundle) {

		_bundle = bundle;
				
		setName(name);
		ColumnLayout layout = new ColumnLayout(4, 4);
		layout.setExpandWidth(true);
		layout.setStretchWidth(true);
		setLayout(layout);
		setBackground(Color.white);
		

		String items[] = getStrings(name);
		loadComponents(this, null, items);
	}


	public String getStringProperty (String name) {

		Object obj = _components.get(name);
		if (obj == null) {
			throw new RuntimeException(name +": property not found");
		}

		if (obj instanceof JCheckBox) {

			if (((JCheckBox)obj).isSelected()) {
				return ("true");
			} else {
				return ("false");
			}

		} else if (obj instanceof RadioButtonExt) {

			if (((RadioButtonExt)obj).isSelected()) {
				return ("true");
			} else {
				return ("false");
			}

		} else if (obj instanceof JToggleButton) {

			if (((JToggleButton)obj).isSelected()) {
				return ("true");
			} else {
				return ("false");
			}

		} else if (obj instanceof ButtonGroupExt) {
			return (((ButtonGroupExt)obj).getSelectedValue().toString());
		}

		return (obj.toString());
	}

	
	public boolean getBooleanProperty (String name) {

		Object obj = _components.get(name);
		if (obj == null) {
			throw new RuntimeException(name +": property not found");
		}

		if (obj instanceof JCheckBox) {

			return (((JCheckBox)obj).isSelected());

		} else if (obj instanceof RadioButtonExt) {

			return (((RadioButtonExt)obj).isSelected());

		} else if (obj instanceof JToggleButton) {

			return (((JToggleButton)obj).isSelected());
		}

		throw new RuntimeException(name + " is not a boolean property");
	}


	public void setStringProperty (String name, String value) {

		Object obj = _components.get(name);
		if (obj == null) {
			throw new RuntimeException(name +": property not found");
		}

		if (obj instanceof JCheckBox) {

			((JCheckBox)obj).setSelected("true".equals(value));

		} else if (obj instanceof RadioButtonExt) {

			if ("true".equals(value)) {
				((RadioButtonExt)obj).setSelected(true);
				return;
			}

			((RadioButtonExt)obj).setSelected(false);

		} else if (obj instanceof JToggleButton) {

			((JToggleButton)obj).setSelected("true".equals(value));

		} else if (obj instanceof ButtonGroupExt) {

			((ButtonGroupExt)obj).setSelectedValue(value);

		} else if (obj instanceof JTextField) {

			((JTextField)obj).setText(value);
		}
	}

	
	public void setBooleanProperty (String name, boolean flag) {

		Object obj = _components.get(name);
		if (obj == null) {
			throw new RuntimeException(name +": property not found");
		}

		if (obj instanceof JCheckBox) {

			((JCheckBox)obj).setSelected(flag);

		} else if (obj instanceof RadioButtonExt) {

			((RadioButtonExt)obj).setSelected(flag);

		} else if (obj instanceof JToggleButton) {

			((JToggleButton)obj).setSelected(flag);

		} else if (obj instanceof JTextField) {
			
			((JTextField)obj).setText(new Boolean(flag).toString());

		} else {

			throw new RuntimeException(name + " is not a boolean property");
		}
	}


	public int getIntProperty (String name) {

		return (0);
	}


	public double getDoubleProperty (String name) {

		return (0.0);
	}


	/**
	 * Get a URL for the specified icon name.
	 *
	 * @param  name  the name of the icon
	 */
	protected Icon getIconResource (String name) {

		String iconName = getString(name + ".icon");
		if (iconName == null) {
			return (null);
		}

		int char0 = iconName.charAt(0);
		if (char0 != '/' && !iconName.startsWith("res/")) {
			iconName = "res/" + iconName;
		}

		Icon icon = _icons.get(iconName);
		if (icon != null) {	
			return (icon);
		}

		URL url = getClass().getResource(iconName);
		if (url == null) {
			return (_loader != null ? _loader.getIcon(iconName) : null);
		}

		icon = new ImageIcon(url);
		_icons.put(iconName, icon);
		return (icon);
	}


	protected String getString (String name) {

		try {
			return ((String)_bundle.getString(name));
		} catch (Exception e) {
			return (null);
		}
	}


	protected boolean getBoolean (String name) {

		String flag = getString(name);
		return ("true".equals(flag));
	}


	protected String [] getStrings (String name) {

		String value = getString(name);
		if (value == null) {
			return (null);
		}

		Vector<String> values_vec = new Vector<String>();
		StringTokenizer st = new StringTokenizer(value);
		while (st.hasMoreTokens()) {
			value = st.nextToken();
			values_vec.addElement(value);
		}

		String values[] = new String[values_vec.size()];
		values_vec.copyInto(values);
		return (values);
	}


	protected AbstractAction getAction (String name) {
		return (null);
	}


	protected void loadComponents (
			JComponent parent, ButtonGroupExt group, String items[]) {
	
		if (items == null) {
			return;
		}


		for (int i = 0; i < items.length; i++) {

			String name = items[i];

			String type = getString(name + ".type");
			if (name.equals("-")) {

				parent.add(Box.createHorizontalStrut(8));

			} else if (name.equals("|")) {

				parent.add(new JSeparator());

			} else if ("button-group".equals(type)) {

				loadButtonGroup(parent, group, name);

			} else if ("group".equals(type)) {

				loadGroup(parent, group, name);

			} else if ("text-field".equals(type)) {

				loadTextField(parent, name);

			} else {

				JComponent c = loadComponent(name);
				if (c != null) {
					c.setName(name);
					parent.add(c);
					_components.put(name, c);

					if (group != null && c instanceof AbstractButton) {
						group.add((AbstractButton)c);
					}
				}
			}
		}
	}


	protected void loadGroup (
			JComponent parent, ButtonGroupExt group, String name) {

		if (getString(name + ".label") != null || 
				getString(name + ".icon") != null) {
			parent.add(loadLabel(name));
		}

		String group_items[] = getStrings(name);
		ColumnLayout layout = new ColumnLayout(16, 0);
		layout.setExpandWidth(true);
		layout.setStretchWidth(true);
		JPanel p = new JPanel(layout);
		p.setOpaque(false);
		parent.add(p);
		loadComponents(p, group, group_items);
	}


	protected void loadButtonGroup (
			JComponent parent, ButtonGroupExt group, String name) {

		if (getString(name + ".label") != null || 
				getString(name + ".icon") != null) {
			parent.add(loadLabel(name));
		}

		String group_btns[] = getStrings(name);
		ButtonGroupExt new_group = new ButtonGroupExt();

		ColumnLayout layout = new ColumnLayout(16, 0);
		layout.setExpandWidth(true);
		layout.setStretchWidth(true);
		JPanel p = new JPanel(layout);
		p.setOpaque(false);
		parent.add(p);
		loadComponents(p, new_group, group_btns);
		String default_value = getString(name + ".default");
		if (default_value != null) {
			new_group.setSelectedValue(default_value);
		}
			
		_components.put(name, new_group);
	}


	/**
	 * @param name  the name of the component
	 */
	public JComponent loadComponent (String name) {

		String type = getString(name + ".type");
		if ("combobox".equals(type)) {
			return (loadComboBox(name));
		}

		if ("color-chooser".equals(type)) {
			return (loadColorChooser(name));
		}

		if ("label".equals(type)) {
			return (loadLabel(name));
		}

		return (loadButton(name, type));
	}


	protected JComponent loadButton (String name, String type) {

		Icon icon = getIconResource(name);
		String label = getString(name + ".label");
		if (icon == null && label == null) {
			System.err.println("button icon and label are null: " + name);
			return (null);
		}

		AbstractButton btn = null;
		if (type == null) {

			btn = new JButton(label, icon);

		} else if ("checkbox".equals(type)) {

			btn = new JCheckBox(label, icon);
			btn.setOpaque(false);
			if (getBoolean(name + ".default")) {
				((JCheckBox)btn).setSelected(true);
			} 

		} else if ("toggle".equals(type)) {

			btn = new JToggleButton(label, icon);
			btn.setOpaque(false);
			if (getBoolean(name + ".default")) {
				((JToggleButton)btn).setSelected(true);
			} 

		} else if ("radio".equals(type)) {

			btn = new RadioButtonExt(label, icon);
			btn.setOpaque(false);

			String value = getString(name + ".value");
			((RadioButtonExt)btn).setValue(value != null ? value : name);

			if (getBoolean(name + ".default")) {
				((RadioButtonExt)btn).setSelected(true);
			} 

		} else {
			System.err.println("button type is invalid: " + type);
			return (null);
		}


		btn.setMargin(_btn_insets);
		btn.setRequestFocusEnabled(false);

		String tooltip = getString(name + ".tooltip");
		if (tooltip == null) {

			tooltip = getString(name + ".label");
			if (tooltip == null) {
				System.err.println("label is null: " + name);
				return (null);
			}
		}

		String shortcut = getString(name + ".shortcut");
		if (shortcut != null) {
			tooltip = tooltip + " (" + shortcut + ")";
		}
		btn.setToolTipText(tooltip);

		int index = name.indexOf('@');
		String actionCommand;
		if (index != -1) {
			actionCommand = name.substring(index + 1);
			name = name.substring(0, index);
		} else {
			actionCommand = null;
		}

		String actionStr = getString(name + ".action");
		if (actionStr == null) {
			actionStr = name;
		}

		AbstractAction action = getAction(actionStr);
		if (action != null) {
			btn.addActionListener(action);
			btn.setEnabled(action.isEnabled());
			btn.setActionCommand(actionCommand);

			action.addPropertyChangeListener(
				(PropertyChangeListener)btn);
		}

		return (btn);
	}


	protected JComponent loadLabel (String name) {

		String label = getString(name + ".label");
		Icon icon = getIconResource(name);

		if (label == null && icon == null) {
			System.err.println("label icon and label are null: " + name);
			return (null);
		}

		if (icon == null) {
			return(new JLabel(label));
		} else if (label == null) {
			return(new JLabel(icon));
		} 
		
		JLabel l = new JLabel(label);
		l.setIcon(icon);
		return (l);
	}


	protected JComponent loadTextField (JComponent parent, String name) {

		JTextField field = null;
		String label = getString(name + ".label");
		if (label != null) {

			JLabel l = new JLabel(label);
			field = new JTextField();
			LabeledPairLayout layout = new LabeledPairLayout(0, 0);
			layout.setStretchWidth(true);
			JPanel p = new JPanel(layout);
			p.setOpaque(false);
			p.add(l, LabeledPairLayout.LABEL);
			p.add(field, LabeledPairLayout.FIELD);
			parent.add(p);

		} else {
			field = new JTextField();
			parent.add(field);
		}
		
		String default_value = getString(name + ".default");
		if (default_value != null) {
			field.setText(default_value);
		}

		_components.put(name, field);
		return (field);
	}


	/**
	 * Creates a combobox.
	 */
	protected JComponent loadComboBox (String name) {

		String items[] = getStrings(name + ".items");
		ComboBoxExt cb;
		if (items != null) {
			cb = new ComboBoxExt(items);	
		} else {
			cb = new ComboBoxExt();	
		}

		if (getBoolean(name + ".editable")) {
			cb.setEditable(true);
		} else {
			cb.setEditable(false);
		}

		return (cb);
	}


	/**
	 * Creates a ColorChooser.
	 */
	protected JComponent loadColorChooser (String name) {

		ColorChooser cc = new ColorChooser();
		cc.setName(name);
		return (cc);
	}
}
