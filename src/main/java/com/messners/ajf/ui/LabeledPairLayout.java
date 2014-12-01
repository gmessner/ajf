package com.messners.ajf.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * This layout manager aranges its children in a label/field way.
 * The vertical and horizontal spacing defaults to the Java Look
 * and Feel guidelines.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class LabeledPairLayout
	implements LayoutManager, UIConstants, Serializable {

	private static final long serialVersionUID = 1L;
	public static final String FIELD = "field";
	public static final String LABEL = "label";
	public static final String SPACE = "space";

	protected ArrayList<Component> labels = new ArrayList<Component>();
	protected ArrayList<Component> fields = new ArrayList<Component>();
  
	protected int vgap = VGAP;
	protected int hgap = HGAP;

	protected int marginWidth = 0;
	protected int marginHeight = 0;

	protected boolean expandWidth  = false;
	protected boolean stretchWidth = false;

	protected Container container;


	/**
	 * No args constructor. Both height and width margins default to 0,
	 * and vgap defaults to 5 and hgap defaults to 12.
	 */
	public LabeledPairLayout () {
	}


	/**
	 * Constructor for the specified container. 
	 * Both height and width margins default to 0, 
	 * and vgap defaults to 5 and hgap defaults to 12.
	 */
	public LabeledPairLayout (Container c) {
		container = c;
	}


	/**
	 * Margin setting constructor. The vgap defaults to 5 and
	 * hgap defaults to 12.
	 *
	 * @param margin_width   width of the space between the manager and
	 *			 its components
	 * @param margin_height  height of the space between the manager and
	 *			 its components
	 */
	public LabeledPairLayout (int margin_width, int margin_height) {
		marginWidth = margin_width;
		marginHeight = margin_height;
	}


	/**
	 * Container and margin setting constructor. The vgap defaults to 5
	 * and hgap defaults to 12.
	 *
	 * @param marginWidth   width of the space between the manager and
	 *			 its components
	 * @param marginHeight  height of the space between the manager and
	 *			 its components
	 */
	public LabeledPairLayout (Container c, int marginWidth, int marginHeight) {

		container = c;
		this.marginWidth = marginWidth;
		this.marginHeight = marginHeight;
	}


	/**
	 * Gets the stretch width of fields to the widest field flag.
	 */
	public boolean getStretchWidth () {
		return (stretchWidth);
	}


	/**
	 * Sets the stretch width of fields to the widest field flag.
	 */
	public void setStretchWidth (boolean flag) {
		stretchWidth = flag;
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
	 * Set the width of the space (in pixels) between the manager
	 * and its components.
	 *
	 * @param width width of the space between the manager and
	 *	 its components
	 */
	public void setMarginWidth (int width) {
		marginWidth = width;
	}


	/**
	 * Set the height of the space (in pixels) between the manager
	 * and its components.
	 *
	 * @param height height of the space between the manager and
	 * its components
	 */
	public void setMarginHeight (int height) {
		marginHeight = height;
	}


	/**
	 * Set the amount of the horizontal space (in pixels) between
	 * each component.
	 *
	 * @param hgap the amount of the horizontal space (in pixels)
	 *                  between each component
	 */
	public void setHgap (int hgap) {
		this.hgap = hgap;
	}

	public void setXSpacing (int spacing) {
		hgap = spacing;
	}


	/**
	 * Set the amount of the vertical space (in pixels) between
	 * each component.
	 *
	 * @param vgap the amount of the vertical space (in pixels)
	 *                  between each component
	 */
	public void setVgap (int vgap) {
		this.vgap = vgap;
	}

	public void setYSpacing (int spacing) {
		vgap = spacing;
	}



	/**
	 * Uses this layout manager to layout the specified container.
	 *
	 * @param  c  the container to layout
	 */
  	public void layoutContainer (Container c) {

		if (container == null) {
			container = c;
		} else if (c != container) {
			throw new RuntimeException(
				"LayoutManager can only be used with a single Container.");
		}


  		Insets insets = c.getInsets();
  
  		int labelWidth = 0;
  		Iterator<Component> labelIter = labels.iterator();
  		while(labelIter.hasNext()) {

			Component comp = labelIter.next();
			labelWidth = Math.max(labelWidth, comp.getPreferredSize().width);
  		}


		int field_width = -1;
		Dimension size = null;

		if (expandWidth) {

			size = c.getSize();
			field_width = size.width - marginWidth - marginWidth -
					(labelWidth + hgap + insets.left + insets.right);

		} else if (stretchWidth) {

  			Iterator<Component> fieldIter = fields.iterator();
  			while (fieldIter.hasNext()) {

	      		Component field = fieldIter.next();
				if (!field.isVisible()) {
					continue;
				}

		        size = field.getPreferredSize();
		        if (size.width > field_width) {
		                field_width = size.width;
		        }
			}
		}


  		int y = insets.top + marginHeight;
  		Iterator<Component> fieldIter = fields.iterator();
  		labelIter = labels.iterator();
  		while(labelIter.hasNext() && fieldIter.hasNext()) {

	     	Component label = labelIter.next();
	     	Component field = fieldIter.next();
	     	int height = Math.max(label.getPreferredSize().height,
			field.getPreferredSize().height);
	     	label.setBounds(marginWidth + insets.left, 
				y, labelWidth, height); 

			if (field_width > 0) {

		    	field.setBounds(marginWidth + insets.left + 
					labelWidth + hgap, y, field_width, height); 

			} else {

		     	field.setBounds(marginWidth + insets.left + 
					labelWidth + hgap, y, 
			 		field.getPreferredSize().width, height);
			}

			y += (height + vgap);
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
	  	int y = marginHeight + insets.top;
  		int fieldWidth = 0;
  		int labelWidth = 0;

	  	Iterator<Component> labelIter = labels.iterator();
	  	Iterator<Component> fieldIter = fields.iterator();
	  	while (labelIter.hasNext() && fieldIter.hasNext()) {

	  		Component label = labelIter.next();
  			Component field = fieldIter.next();
	  		int height = Math.max(label.getPreferredSize().height,
				field.getPreferredSize().height);
	  		y += (height + vgap);
	 	  	labelWidth = Math.max(labelWidth, label.getPreferredSize().width);
	 	  	fieldWidth = Math.max(fieldWidth, field.getPreferredSize().width);
	 	}

	 	return new Dimension(labelWidth + fieldWidth  + (2 * marginWidth) +
			insets.right + insets.left + hgap,
			y + marginHeight + insets.bottom);
	}

  
	/**
	 * Calculates the preferred size dimensions for the specified
	 * panel given the components in the specified parent container. 
	 *
	 * @param  c  the component to be laid out 
	 * @return    the  preferred dimesion for the specified panel
	 */
	public Dimension preferredLayoutSize (Container c) {
	 	return (minimumLayoutSize(c));
	}

   
	/**
	 * Adds the specified component with the specified name to the layout.
	 *
	 * @param  name  the component name 
	 * @param  comp  the component to be added
	 */
	public void addLayoutComponent(String name, Component comp) {

		if (name.equals(LABEL)) {
			labels.add(comp);
		}  else if (name.equals(FIELD)) {
			fields.add(comp);
		}  else {
			labels.add(comp);
			fields.add(comp);
		}
	}


	/**
	 * Removes the specified component from the layout.
	 *
	 * @param  comp  the component to be removed
	 */
	public void removeLayoutComponent(Component comp) {

		labels.remove(comp);
		fields.remove(comp);
	}
}

