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
 * This layout manager simply lays out its children in two columns
 * evenly distributing the children between the two columns.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class TwoColumnLayout implements LayoutManager, UIConstants, Serializable {

	private static final long serialVersionUID = 1L;
	protected int marginWidth = 0;
	protected int marginHeight = 0;
	protected int vgap = VGAP;
	protected int hgap = HGAP;
	protected boolean stretchWidth = false;
	protected boolean expandWidth = false;


	/**
	 * No args constructor. Both height and width margins default to 0.
	 */
	public TwoColumnLayout () {
	}


	public TwoColumnLayout (boolean stretchWidth) {
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
	public TwoColumnLayout (int marginWidth, int marginHeight) {
		this.marginWidth = marginWidth;
		this.marginHeight = marginHeight;
	}


	public TwoColumnLayout (int marginWidth, int marginHeight, boolean expand) {
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
	public TwoColumnLayout (int margin_width, int margin_height, 
					int hgap, int vgap) {

		marginWidth = margin_width;
		marginHeight = margin_height;
		this.hgap = hgap;
		this.vgap = vgap;
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
	 * Sets the amount of the horizontal space (in pixels) between
	 * each column.
	 *
	 * @param hgap the amount of the horizontal space (in pixels)
	 * between the columns
	 */
	public void setHgap (int hgap) {
		this.hgap = hgap;
	}


	/**
	 * Sets the amount of the vertical space (in pixels) between
	 * each component.
	 *
	 * @param vgap the amount of the vertical space (in pixels) between each row
	 */
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

		int height = marginHeight + insets.top;
	  
		Component[] children = c.getComponents();
		int numChildren = children.length;
		int numRows = (numChildren + 1) / 2;
		Dimension size = null;

		int max_width1 = -1;
		int max_width2 = -1;

		if (expandWidth) {

			size = c.getSize();
			max_width1 = (size.width
				- marginWidth - marginWidth
				- insets.left - insets.right - hgap) / 2;
			max_width2 = max_width1;

		} else if (stretchWidth) {

				
			for (int i = 0; i < numRows && i < numChildren; i++) {

				if (!children[i].isVisible()) {
					continue;
				}

		        size = children[i].getPreferredSize();
		        if (size.width > max_width1) {
		                max_width1 = size.width;
		        }
			}	

			for (int i = numRows; i < numChildren; i++) {

				if (!children[i].isVisible()) {
					continue;
				}

		        size = children[i].getPreferredSize();
		        if (size.width > max_width2) {
		                max_width2 = size.width;
		        }
			}
		}


		int x = marginWidth + insets.left;
		int y = height;
		int max_width = max_width1;
		for (int i = 0; i < numRows && i < numChildren; i++) {

			if (!children[i].isVisible()) {
				continue;
			}

			size = children[i].getPreferredSize();

			if (max_width1 > 0) {
				children[i].setSize(max_width1, size.height);
			} else {

				if (size.width > max_width) {
					max_width = size.width;
				}

				children[i].setSize(size.width, size.height);
			}

			children[i].setLocation(x, y);
			y += size.height + vgap;
		}

		x = marginWidth + insets.left + hgap + max_width;
		y = height;
		for (int i = numRows; i < numChildren; i++) {

			if (!children[i].isVisible()) {
				continue;
			}

			size = children[i].getPreferredSize();

			if (max_width2 > 0) {
				children[i].setSize(max_width2, size.height);
			} else {
				children[i].setSize(size.width, size.height);
			}

			children[i].setLocation(x, y);
			y += size.height + vgap;
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

	  
		Component[] children = c.getComponents();
		int numChildren = children.length;
		int numRows = (numChildren + 1) / 2;

		int height1 = 0;
		int maxWidth1 = 0;
		for (int i = 0; i < numRows && i < numChildren; i++) {

			if (!children[i].isVisible()) {
				continue;
			}

			Dimension size = children[i].getPreferredSize();
			if (size.width > maxWidth1) {
				maxWidth1 = size.width;
			}

			height1 += size.height;
			if (i != 0) {
				height1 += vgap;
			}
		}

		int maxWidth2 = 0;
		int height2   = 0;
		for (int i = numRows; i < numChildren; i++) {

			if (!children[i].isVisible()) {
				continue;
			}

			Dimension size = children[i].getPreferredSize();
			if (size.width > maxWidth2) {
				maxWidth2 = size.width;
			}

			height2 += size.height;
			if (i != numRows) {
				height2 += vgap;
			}
		}

		if (expandWidth) {

			if (maxWidth1 > maxWidth2) {
				maxWidth2 = maxWidth1;
			} else {
				maxWidth1 = maxWidth2;
			}
		}



		int width = marginWidth + insets.left + maxWidth1 + hgap +
				maxWidth2 + insets.right + marginWidth + 2;
		int height = Math.max(height1, height2) + 
			marginHeight + marginHeight + insets.bottom + insets.top;
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
