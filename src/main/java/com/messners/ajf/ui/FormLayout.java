package com.messners.ajf.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.Vector;


/**
 * This layout manager does simple form layout.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class FormLayout implements LayoutManager, java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String LABEL = "label";
	public static final String FIELD = "field";

	int _margin_width  = 0;
	int _margin_height = 0;
	int _y_spacing     = 8;
	boolean _stretch_width = true;
	boolean _expand_width  = true;

	Vector<Component> _labels = new Vector<Component>();


	/**
	 * No args constructor. Both height and width margins default to 0 and
	 * spacing between form items is 8.
	 */
	public FormLayout () {
	}


	/**
	 * Margin setting constructor.
	 *
	 * @param margin_width   width of the space between the manager and
	 *			 its components
	 * @param margin_height  height of the space between the manager and
	 *			 its components
	 */
	public FormLayout (int margin_width, int margin_height) {
		_margin_width = margin_width;
		_margin_height = margin_height;
	}


	/**
	 * Sets the width of the space (in pixels) between the manager
	 * and its components.
	 *
	 * @param width          width of the space between the manager and
	 *			 its components
	 */
	public void setMarginWidth (int width) {
		_margin_width = width;
	}


	/**
	 * Sets the height of the space (in pixels) between the manager
	 * and its components.
	 *
	 * @param height         height of the space between the manager and
	 *			 its components
	 */
	public void setMarginHeight (int height) {
		_margin_height = height;
	}


	/**
	 * Sets the amount of the vertical space (in pixels) between
	 * each component.
	 *
	 * @param y_spacing      the amount of the vertical space (in pixels)
	 *                       between each component
	 */
	public void setYSpacing (int y_spacing) {
		_y_spacing = y_spacing;
	}


	public void setVgap (int gap) {
		_y_spacing = gap;
	}


	public boolean getStretchWidth () {
		return (_stretch_width);
	}


	public void setStretchWidth (boolean stretch_width) {
		_stretch_width = stretch_width;
	}


	public boolean getExpandWidth () {
		return (_expand_width);
	}


	public void setExpandWidth (boolean flag) {
		_expand_width = flag;
	}


	/**
	 * Uses this layout manager to layout the specified container.
	 *
	 * @param  c  the container to layout
	 */
	public void layoutContainer(Container c) {
		Insets insets = c.getInsets();
		int height = _margin_height + insets.top;
	  
		Component[] children = c.getComponents();
		Dimension size = null;

		int max_width = -1;

		if (_expand_width) {
			size = c.getSize();
			max_width = size.width
				- _margin_width - _margin_width
				- insets.left - insets.right;
		} else if (_stretch_width) {
			for (int i = 0; i < children.length; i++) {
			        size = children[i].getPreferredSize();
			        if (size.width > max_width) {
			                max_width = size.width;
			        }
			}
		}


		int margin_width = _margin_width + insets.left;
		for (int i = 0; i < children.length; i++) {

			Component child = children[i];
			size = child.getPreferredSize();

			if (max_width > 0) {
				child.setSize(max_width, size.height);
			} else {
				child.setSize(size.width, size.height);
			}

			child.setLocation(margin_width, height);

			/*
			 * Adjust the height dependent on whether the child
			 * is a label or a field
			 */
			if (_labels.contains(child)) {
				height += size.height;
			} else {
				height += size.height + _y_spacing;
			}
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

		Insets insets = c.getInsets();
		int height = _margin_height + insets.top;
	  
		Component[] children = c.getComponents();

		int width_extra = insets.left + 
			_margin_width + _margin_width + insets.right;
		int width = width_extra;
		int num_children = children.length;
		for (int i = 0; i < num_children; i++) {

			Component child = children[i];
			Dimension size = child.getPreferredSize();
			if (size.width + width_extra > width) {
				width = size.width + width_extra;
			}

			height += size.height;
			if (i < num_children - 1 && !_labels.contains(child)) {
				height += _y_spacing;
			}
		}

		height += _margin_height + insets.bottom;
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

		if (name.equalsIgnoreCase(LABEL)) {
			_labels.addElement(comp);
		} 
	}


	/**
	 * Removes the specified component from the layout.
	 *
	 * @param  comp  the component to be removed
	 */
	public void removeLayoutComponent (Component comp) {
		_labels.removeElement(comp);
	}
}

