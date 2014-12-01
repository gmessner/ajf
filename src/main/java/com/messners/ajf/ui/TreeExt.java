package com.messners.ajf.ui;

import java.util.Enumeration;

import java.awt.Color;
import java.awt.Component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;


/**
 * This class extends JTree and provides some useful utility methods.
 */
public class TreeExt extends JTree {

	private static final long serialVersionUID = 1L;

	/**
	 * Construct a TreeExt with the specified root node.
	 * @param root The root node.
	 */
	public TreeExt (DefaultMutableTreeNode root) {

		super (root);
		this.root = root;

		/*
		 * Set cell renderer that handles components not handled 
		 * by the default cell renderer.
		 */
		setCellRenderer(new TreeCellRendererExt());

		/*
		 * Add a mouse listener to the tree to handle selections
		 * not handled by the detault mouse adapter.
		 */
		addMouseListener(new MouseAdapterExt());
	}

	/**
	 * Expands every node in the tree.
	 */
	public void expandAllNodes () {

		DefaultMutableTreeNode node = null;
		for (Enumeration<?> e = root.depthFirstEnumeration(); 
			e.hasMoreElements(); ) {
			node = (DefaultMutableTreeNode) e.nextElement();
			if (node.getChildCount() > 0) {
				expandPath(new TreePath(node.getPath()));
			}
		}
	}

	/**
	 * Collapses every node in the tree.
	 */
	public void collapseAllNodes () {

		DefaultMutableTreeNode node = null;
		for (Enumeration<?> e = root.depthFirstEnumeration();
			e.hasMoreElements(); ) {
			node = (DefaultMutableTreeNode) e.nextElement();
			if (node.getChildCount() > 0) {
				collapsePath(new TreePath(node.getPath()));
			}
		}
	}
	
	/**
	 * Gets the root node of the tree.
	 * @return The root node of the tree.
	 */
	public DefaultMutableTreeNode getRootNode () {

		return root;
	}

	/**
	 * Sets the icon for open leaf nodes in the tree.
	 * @param open Open leaf node icon.
	 */
	public void setOpenIcon (Icon open) {

		((DefaultTreeCellRenderer) getCellRenderer()).setOpenIcon(open);
	}

	/**
	 * Sets the icon for closed leaf nodes in the tree.
	 * @param closed Closed leaf node icon.
	 */
	public void setClosedIcon (Icon closed) {

		((DefaultTreeCellRenderer) getCellRenderer()).setClosedIcon(closed);
	}
	
	/**
	 * Sets the icon for leaf nodes in the tree.
	 * @param leaf Leaf node icon.
	 */
	public void setLeafIcon (Icon leaf) {

		((DefaultTreeCellRenderer) getCellRenderer()).setLeafIcon(leaf);
	}
	
	/**
	 * Extended tree cell renderer.
	 */
	private class TreeCellRendererExt extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 1L;

		// From DefaultTreeCellRenderer.
		public Component getTreeCellRendererComponent (JTree tree, 
			   Object value, boolean selected, boolean expanded, 
			   boolean leaf, int row, boolean hasFocus) {

			if (value instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				Object userObject = node.getUserObject();
				if (userObject instanceof JCheckBox) {
					JCheckBox cb = (JCheckBox) userObject;
					/*
					 * TODO: Need to find better way to 
					 * handle the case where the checkbox 
					 * background isn't the same as the 
					 * parent's background.
					 */
					cb.setBackground(Color.WHITE);
					return cb;
				} else if (userObject instanceof JLabel) {
					JLabel lbl = (JLabel) userObject;
					value = lbl.getText();
				} else if (userObject instanceof PrefsPanel) {
					PrefsPanel pnl = (PrefsPanel)userObject;
					value = pnl.getTitle();
				}
			}
			return super.getTreeCellRendererComponent(tree, 
				 value, selected, expanded, leaf, row, hasFocus);
		}
	}

	/**
	 * Extended mouse adapter.
	 */
	private class MouseAdapterExt extends MouseAdapter {

		// From MouseAdapter  
		public void mouseClicked(MouseEvent e) {

			TreePath path = getPathForLocation(e.getX(), e.getY()); 
			if (path != null) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (node != null) {
					Object userObject = node.getUserObject();
					if (userObject instanceof JCheckBox) {
						JCheckBox cb = (JCheckBox) userObject;
						cb.setSelected(!cb.isSelected());
						repaint();
					}
				}
			}
		}
	}

	/** The root node, since there's no easy way to get it from a JTree. */
	private DefaultMutableTreeNode root = null;
}
