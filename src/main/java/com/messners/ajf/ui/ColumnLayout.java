package com.messners.ajf.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.border.Border;


/**
 * This layout manager simply lays out its children in a single column.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ColumnLayout
	implements LayoutManager, UIConstants, Serializable {

	private static final long serialVersionUID = 1L;
	protected int marginWidth = 0;
	protected int marginHeight = 0;
	protected int vgap = VGAP;
	protected boolean stretchWidth = false;
	protected boolean expandWidth = false;


	/**
	 * No args constructor. Both height and width margins default to 0.
	 */
	public ColumnLayout () {
	}


	public ColumnLayout (boolean stretchWidth) {
		this.stretchWidth = stretchWidth;
	}



	/**
	 * Margin setting constructor.
	 *
	 * @param marginWidth   width of the space between the manager and
	 *			 its components
	 * @param marginHeight  height of the space between the manager and
	 *			 its components
	 */
	public ColumnLayout (int marginWidth, int marginHeight) {
		this.marginWidth = marginWidth;
		this.marginHeight = marginHeight;
	}


	public ColumnLayout (int marginWidth, int marginHeight, boolean expand) {
		this.marginWidth  = marginWidth;
		this.marginHeight = marginHeight;
		expandWidth = expand;
	}


	/**
	 * Margin and spacing setting constructor.
	 *
	 * @param margin_width   width of the space between the manager and
	 *			 its components
	 * @param margin_height  height of the space between the manager and
	 *			 its components
	 */
	public ColumnLayout (int margin_width, int margin_height, int spacing) {
		marginWidth = margin_width;
		marginHeight = margin_height;
		vgap = spacing;
	}


	/**
	 * Sets the width of the space (in pixels) between the manager
	 * and its components.
	 *
	 * @param width the width of the space between the manager
     * and its components
	 */
	public void setMarginWidth (int width) {
		marginWidth = width;
	}


	/**
	 * Sets the height of the space (in pixels) between the manager
	 * and its components.
	 *
	 * @param height the height of the space between the manager
	 * and its components
	 */
	public void setMarginHeight (int height) {
		marginHeight = height;
	}


	/**
	 * Sets the amount of the vertical space (in pixels) between
	 * each component.
	 *
	 * @param y_spacing      the amount of the vertical space (in pixels)
	 *                       between each component
	 */
	public void setYSpacing (int y_spacing) {
		vgap = y_spacing;
	}


	public void setVgap (int vgap) {
		this.vgap = vgap;
	}


	/**
	 * Gets the stretch width of children to the widest child flag.
	 */
	public boolean getStretchWidth () {
		return (stretchWidth);
	}


	/**
	 * Sets the stretch width of children to the widest child flag.
	 */
	public void setStretchWidth (boolean stretch_width) {
		stretchWidth = stretch_width;
	}


	/**
	 * Gets the expand children to width of container flag.
	 */
	public boolean getExpandWidth () {
		return (expandWidth);
	}


	/**
	 * Sets the expand children to width of container flag.
	 */
	public void setExpandWidth (boolean flag) {
		expandWidth = flag;
	}


	/**
	 * Uses this layout manager to layout the specified container.
	 *
	 * @param  c  the container to layout
	 */
	public void layoutContainer (Container c) {

		Insets insets = c.getInsets();
		int height = marginHeight + insets.top;
	  
		Component[] children = c.getComponents();
		Dimension size = null;

		int max_width = -1;

		if (expandWidth) {

			size = c.getSize();
			max_width = size.width
				- marginWidth - marginWidth
				- insets.left - insets.right;

		} else if (stretchWidth) {

			for (int i = 0; i < children.length; i++) {

				if (!children[i].isVisible()) {
					continue;
				}

		        size = children[i].getPreferredSize();
		        if (size.width > max_width) {
		                max_width = size.width;
		        }
			}
		}


		int margin_width = marginWidth + insets.left;
		for (int i = 0; i < children.length; i++) {

			if (!children[i].isVisible()) {
				continue;
			}

			size = children[i].getPreferredSize();

			if (max_width > 0) {
				children[i].setSize(max_width, size.height);
			} else {
				children[i].setSize(size.width, size.height);
			}

			children[i].setLocation(margin_width, height);
			height += size.height + vgap;
		}
	}


	/**
	 * Calculates the minimum size dimensions for the specified panel
	 * given the components in the specified parent container. 
	 *
	 * @param  c  the component to be laid out 
	 * @return  the  minimum dimesion for the specified panel
	 */
	public Dimension minimumLayoutSize (Container c) {

		Border border;
		if (c instanceof JComponent) {
			border = ((JComponent)c).getBorder();
		} else {
			border = null;
		}

		Insets insets;
		if (border == null) {
			insets = c.getInsets();
		} else {
			insets = border.getBorderInsets(c);
		}

		int height = 0;
	  
		Component[] children = c.getComponents();

		int width_extra = insets.left + 
			marginWidth + marginWidth + insets.right;
		int width = width_extra;
		int num_children = children.length;
		for (int i = 0; i < num_children; i++) {

			if (!children[i].isVisible()) {
				continue;
			}

			Dimension size = children[i].getPreferredSize();
			if (size.width + width_extra > width) {
				width = size.width + width_extra;
			}

			height += size.height;
			if (i < num_children - 1) {
				height += vgap;
			}
		}

		height += marginHeight + insets.top + marginHeight + insets.bottom;
		return new Dimension(width, height);
	}

  
	/**
	 * Calculates the preferred size dimensions for the specified
	 * panel given the components in the specified parent container. 
	 *
	 * @param  c  the component to be laid out 
	 * @return    the  preferred dimesion for the specified panel
	 */
	public Dimension preferredLayoutSize (Container c) {
		return minimumLayoutSize(c);
	}
   

	/**
	 * Adds the specified component with the specified name to the layout.
	 *
	 * @param  name  the component name 
	 * @param  comp  the component to be added
	 */
	public void addLayoutComponent (String name, Component comp) {
	}


	/**
	 * Removes the specified component from the layout.
	 *
	 * @param  comp  the component to be removed
	 */
	public void removeLayoutComponent (Component comp) {
	}
}

