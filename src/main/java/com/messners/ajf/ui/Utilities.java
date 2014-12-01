package com.messners.ajf.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.messners.ajf.app.Preferences;


/**
 * This class contains static utility methods for manipulating GUI components.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class Utilities implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Center the dialog over the specified component
	 *
	 * @param  dialog  the dialog to center
	 * @param  parent  the component to center the dialog over. If this
	 *				 parameter is null, the dialog will be centered
	 *				 on the screen.
	 */
	public static final void centerDialog (Component dialog, Component parent) {

		if (parent == null || !parent.isVisible()) {
			centerDialog(dialog);
			return;
		}

		Dimension dsize = dialog.getSize();
		Dimension ssize = dialog.getToolkit().getScreenSize();
		Dimension psize = parent.getSize();

		Point p = parent.getLocationOnScreen();
		int x = p.x + psize.width / 2 - dsize.width / 2;
		if (x < 0) {
			x = 0;
		} else if (x + dsize.width > ssize.width) {
			x = ssize.width - dsize.width;
		}

		int y = p.y + psize.height / 2 - dsize.height / 2;
		if (y < 0) {
			y = 0;
		} else if (y + dsize.height > ssize.height) {
			y = ssize.height - dsize.height;
		}

		dialog.setLocation(x, y);
	}


	/**
	 * Center the dialog on the screen.
	 *
	 * @param  dialog  the dialog to center
	 */
	public static final void centerDialog (Component dialog) {

		Dimension screenSize = dialog.getToolkit().getScreenSize();
		Dimension size = dialog.getSize();
		screenSize.height = screenSize.height / 2;
		screenSize.width = screenSize.width / 2;
		size.height = size.height / 2;
		size.width = size.width / 2;
		int y = screenSize.height - size.height;
		int x = screenSize.width - size.width;
		dialog.setLocation(x, y);
	}


	/**
	 * Sets the enabled property on a Component.
	 *
	 * @param  c  the Component to set 
	 * @param  flag  the enabled flag
	 */
	public static final void setEnabled (Component c, boolean flag) {

		if (c != null) {
			c.setEnabled(flag);
		}
	}


	/**
	 * Set the enabled state of a container and all its children.
	 *
	 * @param  c		the container of the children
	 * @param enabled   the enabled flag
	 */
	public static final void setChildrenEnabled (Container c, boolean enabled) {

		if (c instanceof JMenu) {
			setChildrenEnabled((JMenu)c, enabled);
			return;
		}

		Component child[] = c.getComponents();
		int num_children = child.length;

		if (c instanceof JComponent) {
			((JComponent)c).setEnabled(enabled);
		}
		
		
		for (int i = 0; i < num_children; i++) {

			if (child[i] instanceof JComponent) {
				((JComponent)child[i]).setEnabled(enabled);
			}

			if (child[i] instanceof Container) {
				setChildrenEnabled(
					(Container)child[i], enabled);
			}
		}
	}

	/**
	 * Set the enabled state of a JMenu and all its children.
	 *
	 * @param menu     the JMenu of the children
	 * @param enabled  the enabled flag
	 */
	public static final void setChildrenEnabled (JMenu menu, boolean enabled) {

		Component child[] = menu.getMenuComponents();
		int num_children = child.length;

		Component me = menu.getComponent();
		if (me instanceof JComponent) {
			((JComponent)me).setEnabled(enabled);
		}
		
		for (int i = 0; i < num_children; i++) {

			if (child[i] instanceof JComponent) {
				((JComponent)child[i]).setEnabled(enabled);
			}

			if (child[i] instanceof Container) {
				setChildrenEnabled(
					(Container)child[i], enabled);
			}
		}
	}


	/**
	 * Set the font in all the children that are instances of any of
	 * the classes in the classes array.
	 *
	 * @param  c		the container of the children to set the font on
	 * @param  classes  an array of Class objects which specify
	 *				  which object classes to set the font on
	 * @param f		 the font to set the children to
	 */
	public static final void setChildrenFont (
		Container c, Class<?> classes[], Font f) {

		Component child[] = c.getComponents();
		int num_children = child.length;
		int num_classes = (classes == null ? 0 : classes.length);
		
		for (int i = 0; i < num_children; i++) {

			if (num_classes == 0) {
				child[i].setFont(f);
			} else {

				for (int j = 0; j < num_classes; j++) {
					if (classes[j] != null &&
						classes[j].isInstance(child[i])) {
						child[i].setFont(f);
						break;
					}
				}
			}

			if (child[i] instanceof Container) {
				setChildrenFont((Container)child[i],
					classes, f);
			}
		}
	}


	/**
	 * Set the background of all the children that are instances of any of
	 * the classes in the classes array.
	 *
	 * @param  c		the container of the children to set the background
	 * @param  classes  an array of Class objects which specify
	 *				  which object classes to set the background for
	 * @param color	 the new background color to set the children to
	 */
	public static final void setChildrenBackground (
		Container c, Class<?> classes[], Color color) {

		Component child[] = c.getComponents();
		int num_children = child.length;
		int num_classes = (classes == null ? 0 : classes.length);
		
		for (int i = 0; i < num_children; i++) {

			if (num_classes == 0) {
				child[i].setBackground(color);
			} else {
				for (int j = 0; j < num_classes; j++) {
					if (classes[j] != null &&
						classes[j].isInstance(child[i])) {
						child[i].setBackground(color);
						break;
					}
				}
			}

			if (child[i] instanceof Container) {
				setChildrenBackground((Container)child[i],
					classes, color);
			}
		}
	}


	/**
	 * Set the foreground of all the children that are instances of any of
	 * the classes in the classes array.
	 *
	 * @param  c		the container of the children to set the foreground
	 * @param  classes  an array of Class objects which specify
	 *				  which object classes to set the foreground for
	 * @param color	 the new foreground color to set the children to
	 */
	public static final void setChildrenForeground (
		Container c, Class<?> classes[], Color color) {

		Component child[] = c.getComponents();
		int num_children = child.length;
		int num_classes = (classes == null ? 0 : classes.length);
		
		for (int i = 0; i < num_children; i++) {

			if (num_classes == 0) {
				child[i].setForeground(color);
			} else {

				for (int j = 0; j < num_classes; j++) {
					if (classes[j] != null &&
						classes[j].isInstance(child[i])) {
						child[i].setForeground(color);
						break;
					}
				}
			}

			if (child[i] instanceof Container) {
				setChildrenForeground((Container)child[i],
					classes, color);
			}
		}
	}


	/**
	 * Find the hosting frame for the component.
	 *
	 * @param  c  the component to find the hosting frame for
	 * @return	the hosting frame for the specified component
	 */
	public static final Frame getFrame (Component c) {

		if (c instanceof Frame) {
			return ((Frame)c);
		}

		if (c == null) {
			return (null);
		}

		Container p = c.getParent();
		for (; p != null; p = p.getParent()) {
			if (p instanceof Frame) {
				return (Frame) p;
			}
		}

		return (null);
	}


	/**
	 * Shows or hides the labels for each button on the specified toolbar.
	 *
	 */
	public static final void setButtonLabelsVisible (
				JToolBar toolbar, boolean show) {

		setButtonLabelsVisible((Container)toolbar, show);
	}


	private static final void setButtonLabelsVisible (
					Container c, boolean show) {

		Component children[] = c.getComponents();
		int numChildren = children.length;

		for (int i = 0; i < numChildren; i++) {

			Component child = children[i];
			if (child instanceof ButtonExt) {
				((ButtonExt)child).setShowToolBarLabel(show);
			} else if (child instanceof ToggleButtonExt) {
				((ToggleButtonExt)child).setShowToolBarLabel(show);
			} else if (child instanceof Container) {
				setButtonLabelsVisible((Container)child, show);
			}
		}
	}



	/**
	 * Find the hosting Window for the component.
	 *
	 * @param  c  the component to find the hosting frame for
	 * @return	the hosting frame for the specified component
	 */
	public static final Window getWindow (Component c) {

		if (c instanceof Window) {
			return ((Window)c);
		}

		if (c == null) {
			return (null);
		}

		Container p = c.getParent();
		for (; p != null; p = p.getParent()) {
			if (p instanceof Window) {
				return (Window) p;
			}
		}

		return (null);
	}


	/**
	 * Find the child Component specified by name.
	 *
	 * @param  c		the container of the child
	 * @param  name     the name of the child
	 *
	 * @return  the child Component with the specified name
	 * or null if not found
	 */
	public static final Component findChild (Container c, String name) {

		Component child[] = c.getComponents();
		int num_children = child.length;
		
		for (int i = 0; i < num_children; i++) {

			if (name.equals(child[i].getName())) {
				return (child[i]);
			}
		}

		for (int i = 0; i < num_children; i++) {

			if (child[i] instanceof Container) {

				Component mychild = findChild((Container)child[i], name);
				if (mychild != null) {
					return (mychild);
				}


				if (child[i] instanceof JMenu) {

					mychild = ((JMenu)child[i]).getPopupMenu();
					mychild = findChild((Container)mychild, name);
					if (mychild != null) {
						return (mychild);
					}
				}
			}
		}

		return (null);
	}



	/**
	 * 'cleans up' a menu item label by removing the '&' sign and the
	 * training ellipisis, if any. This can be used to process the
	 * contents of an <i>action</i>.label property.
	 */
	public static final String cleanupMenuLabel (String label) {

		if (label == null) {
			return (null);
		}


		int index = label.indexOf('&');
		if (index != -1) {
			label = label.substring(0,index)
				.concat(label.substring(index + 1));
		}

		if (label.endsWith("...")) {
			label = label.substring(0, label.length() - 3);
		}

		return (label);
	}


	/**
	 * Converts a string to a keystroke. The string should be of the
	 * form <i>modifiers</i>+<i>shortcut</i> where <i>modifiers</i>
	 * is any combination of A for Alt, C for Control, S for Shift
	 * or M for Meta, and <i>shortcut</i> is either a single character,
	 * or a keycode name from the <code>KeyEvent</code> class, without
	 * the <code>VK_</code> prefix.
	 *
	 * @param keyStroke A string description of the key stroke
	 */
	public static final KeyStroke parseKeyStroke (String keyStroke) {

		if (keyStroke == null) {
			return (null);
		}

		int modifiers = 0;
		int ch = '\0';
		int index = keyStroke.indexOf('+');
		for(int i = 0; i < index; i++) {

			switch(Character.toUpperCase(keyStroke.charAt(i))) {
			case 'A':
				modifiers |= InputEvent.ALT_MASK;
				break;
			case 'C':
				modifiers |= InputEvent.CTRL_MASK;
				break;
			case 'M':
				modifiers |= InputEvent.META_MASK;
				break;
			case 'S':
				modifiers |= InputEvent.SHIFT_MASK;
				break;
			}
		}

		String key = keyStroke.substring(index + 1);
		if (key.length() == 1) {
			ch = Character.toUpperCase(key.charAt(0));
		} else if (key.length() == 0) {
			System.err.println("Invalid key stroke: " + keyStroke);
			return (null);
		} else {
			try {
				ch = KeyEvent.class.getField("VK_".concat(key))
					.getInt(null);
			} catch(Exception e) {
				System.err.println("Invalid key stroke: "
					+ keyStroke);
				return (null);
			}
		}		

		return (KeyStroke.getKeyStroke(ch, modifiers));
	}


	/**
	 * Gets the BorderLayout constraint for the given component
	 * within the the given container.
	 *
	 * @return the BorderLayout for the child in the container
	 */
    public static final String getDockingConstraint (Component c, Component child, int orientation, int x, int y) {

		String s = BorderLayout.NORTH;
		if (c.contains(x, y)) {

			int dockingSensitivity = 0;
			dockingSensitivity = (orientation == SwingUtilities.HORIZONTAL) ?
				child.getSize().height : child.getSize().width;

		    if (y >= c.getSize().height - dockingSensitivity) {
				s = BorderLayout.SOUTH;
			} 

		    // West  (Base distance on height for now!)
		    if (x < dockingSensitivity) {
				s = BorderLayout.WEST;
			}

		    // East  (Base distance on height for now!)
		    if (x >= c.getSize().width - dockingSensitivity) {
				s = BorderLayout.EAST;
			}

		    // North  (Base distance on height for now!)
		    if (y < dockingSensitivity) {
				s = BorderLayout.NORTH;
			}

		}

		return (s);
    }


	/**
	 * Restores the column ordering and widths for a JTable from
	 * a Preferences instance.
	 *
	 * @param  prefs  the Preferences instance to restore from
	 * @param  name   the preferences name to restore from
	 * @param  table  the JTable to restore
	 */
	public static final void restoreTableColumns (
			Preferences prefs, String name, JTable table) {

		TableColumnModel tcm = table.getColumnModel();
		int numColumns = tcm.getColumnCount();

		int viewIndexes[] = new int[numColumns];
		int columnWidths[] = new int[numColumns];
		TableColumn columns[] = new TableColumn[numColumns];

		int resizeMode = table.getAutoResizeMode();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		int w;
		int index;
		TableColumn tc;
		boolean dontMove = false;
		for (int i = 0; i < numColumns; i++) {

			String columnName = name + ".column[" + i + "]";
			tc = tcm.getColumn(i);

			w = prefs.get(columnName + ".width", -1);
			index = prefs.get(columnName + ".view-index", -1);
			if (index != -1) {

				viewIndexes[i] = index;
				columnWidths[i] = w;
				columns[i] = tc;

			} else {
				dontMove = true;
			}
		}


		if (!dontMove) {

			for (int i = 0; i < numColumns; i++) {
				table.removeColumn(columns[i]);
			}

			for (int i = 0; i < numColumns; i++) {
				for (int j = 0; j < numColumns; j++) {
					if (viewIndexes[j] == i) {

						tc = columns[j];
						table.addColumn(tc);
						tc.setPreferredWidth(columnWidths[j]);
					}
				}
			}
		}

		table.setAutoResizeMode(resizeMode);
	}



	/**
	 * Saves the column ordering and widths for a JTable to
	 * a Preferences instance.
	 *
	 * @param  prefs  the Preferences instance to save to
	 * @param  name   the preferences name to save to
	 * @param  table  the JTable to save
	 */
	public static final void saveTableColumns (
			Preferences prefs, String name, JTable table) {

		TableColumnModel tcm = table.getColumnModel();
		int numColumns = tcm.getColumnCount();

		int index;
		TableColumn tc;
		for (int i = 0; i < numColumns; i++) {

			String columnName = name + ".column[" + i + "]";
			index = table.convertColumnIndexToView(i);
			tc = tcm.getColumn(index);
			
			prefs.put(columnName + ".width", tc.getWidth());
			prefs.put(columnName + ".view-index", index);
		}
	}


	/**
	 * Sets the width of the given JTable column to the width of its
	 * widest contained value.
	 *
	 * @param table  the JTable that contains the column
	 * @param column the column index to set the width of
	 */
	public static final void setDefaultColumnWidth (JTable table, int column) {

		int resizeMode = table.getAutoResizeMode();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		TableCellRenderer renderer = table.getCellRenderer(0, column);
		int max = 0;
		TableModel tableModel = table.getModel();
		int rows = tableModel.getRowCount();
		for (int i = 0; i < rows; i++) {

			Object obj = tableModel.getValueAt(i, column);
			Component c = renderer.getTableCellRendererComponent(
				table, obj, true, true, i, column);
			max = Math.max(max, c.getPreferredSize().width);
		}

		TableColumnModel tcm = table.getColumnModel();
		TableColumn tc = tcm.getColumn(column);
		tc.setPreferredWidth(max + 4);

		table.setAutoResizeMode(resizeMode);
	}


	/**
	 * Expand all the children nodes of the specified TreeNode.
	 *
	 * @param tree the JTree containing the TreeNodes
	 * @param root the TreeNode to expand the children
	 */
	public static void expandAll (JTree tree, DefaultMutableTreeNode root) {

		Enumeration<?> nodes = root.children();
		while (nodes.hasMoreElements()) {

			Object node = nodes.nextElement();
			if (node instanceof DefaultMutableTreeNode) {

				TreePath path = 
					new TreePath(((DefaultMutableTreeNode)node).getPath());
				if (!tree.isExpanded(path)) {
					tree.expandPath(path);
				}
			}
		}
	}


	/**
	 * Collapse all the children nodes of the specified TreeNode.
	 *
	 * @param tree the JTree containing the TreeNodes
	 * @param root the TreeNode to collapse the children
	 */
	public static void collapseAll (JTree tree, DefaultMutableTreeNode root) {

		Enumeration<?> nodes = root.children();
		while (nodes.hasMoreElements()) {

			Object node = nodes.nextElement();
			if (node instanceof DefaultMutableTreeNode) {

				TreePath path = 
					new TreePath(((DefaultMutableTreeNode)node).getPath());
				if (tree.isExpanded(path)) {
					tree.collapsePath(path);
				}
			}
		}
	}
}
