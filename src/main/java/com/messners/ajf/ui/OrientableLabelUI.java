package com.messners.ajf.ui;

// java core classes needed
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.text.View;

import java.awt.FontMetrics;


/**
** A UI that can render a label vertically as well as horizontally
**/
public class OrientableLabelUI extends BasicLabelUI {
	
	 static{
		 // override what superclass returns for createUI(JComponent c)
		 labelUI = new OrientableLabelUI();
	 }
	 
	 /* the orientation we are rendering at, VERTICAL or HORIZONTAL */
	 private int orientation = JLabel.VERTICAL;

	/* override to avoid having our text truncated to an ellipsis (...) */
   protected String layoutCL( JLabel label, FontMetrics fontMetrics, 
        String text, Icon icon, Rectangle viewR, Rectangle iconR, 
        Rectangle textR){
		//
		super.layoutCL(label, fontMetrics, text, icon, viewR, iconR, textR);
		return text;
   }

	/**
	* sets the label's orientation
	* @param newOrientation either JLabel.HORIZONTAL or JLabel.VERTICAL
	*/
	public void setOrientation( int newOrientation){
		if(orientation == newOrientation) return; // no change
		if(newOrientation == JLabel.HORIZONTAL){
			orientation = newOrientation;
		}
		// otherwise default to vertical
		else orientation = JLabel.VERTICAL;
	}
	
	/**
	* gets the label's orientation
	*/
	public int getOrientation(){
		return orientation;
	}

   /* override */ 
   public void paint(Graphics g, JComponent c) {
		if(orientation == JLabel.VERTICAL){
			JLabel label = (JLabel)c;
			Graphics2D g2D = (Graphics2D)g;
			AffineTransform savedXForm = g2D.getTransform();
			Insets insets = c.getInsets();
			Rectangle innerBounds = new Rectangle();
			// FontMetrics fm = g.getFontMetrics();
			//
			innerBounds.x = insets.left;
			innerBounds.y = insets.top;
			innerBounds.width = label.getWidth() - (insets.left + insets.right);
			innerBounds.height = label.getHeight() - (insets.top + insets.bottom);
         double rX = insets.left +innerBounds.getWidth()*1.38;
			if(label.getIcon() != null){
				rX += label.getIconTextGap();
			}
			double rY = insets.top +innerBounds.getHeight() *0.69;
			//System.out.println("rX: " +rX);
			//System.out.println("rY: " +rY);
			g2D.transform(AffineTransform.getRotateInstance(-Math.PI/2f, rX, rY));
			super.paint(g,label);
			if(savedXForm != null){
				g2D.setTransform(savedXForm);
			}
		}
		else super.paint(g,c);
   }
 
    /*
     * Paint the label text in the foreground color, if the label
     * is opaque then paint the entire background with the background
     * color.  The Label text is drawn by paintEnabledText() or
     * paintDisabledText().  The locations of the label parts are computed
     * by layoutCL.
     * 
     * @see #paintEnabledText
     * @see #paintDisabledText
     * @see #layoutCL
    public void paint(Graphics g, JComponent c) 
    {
        JLabel label = (JLabel)c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

        if ((icon == null) && (text == null)) {
            return;
        }

        FontMetrics fm = g.getFontMetrics();
        Insets insets = c.getInsets(paintViewInsets);

        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = c.getWidth() - (insets.left + insets.right);
        paintViewR.height = c.getHeight() - (insets.top + insets.bottom);

        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

        String clippedText = 
            layoutCL(label, fm, text, icon, paintViewR, paintIconR, paintTextR);

        if (icon != null) {
            icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
        }

        if (text != null) {
	    View v = (View) c.getClientProperty(BasicHTML.propertyKey);
	    if (v != null) {
		v.paint(g, paintTextR);
	    } else {
		int textX = paintTextR.x;
		int textY = paintTextR.y + fm.getAscent();
		
		if (label.isEnabled()) {
		    paintEnabledText(label, g, clippedText, textX, textY);
		}
		else {
		    paintDisabledText(label, g, clippedText, textX, textY);
		}
	    }
        }
    }
     */

	/* override */
   public Dimension getPreferredSize(JComponent c){
		if(orientation != JLabel.VERTICAL){
			return super.getPreferredSize(c);
		}
		// swap height and width arguments
		return swap(super.getPreferredSize(c));
   }
	private Dimension swap(Dimension size){
		int tmp = size.width;
		size.width = size.height;
		size.height = tmp;
		return size;
	}

	/* override */
   public Dimension getMinimumSize(JComponent c) {
      Dimension d = getPreferredSize(c);
		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		if (v != null) {
			 //d.width -= v.getPreferredSpan(View.X_AXIS) - v.getMinimumSpan(View.X_AXIS);
			 d.height -= v.getPreferredSpan(View.X_AXIS) - v.getMinimumSpan(View.X_AXIS);
		}
		return d;
   }

	/* override */
   public Dimension getMaximumSize(JComponent c) {
      Dimension d = getPreferredSize(c);
		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		if (v != null) {
			 //d.width += v.getMaximumSpan(View.X_AXIS) - v.getPreferredSpan(View.X_AXIS);
			 d.height += v.getMaximumSpan(View.X_AXIS) - v.getPreferredSpan(View.X_AXIS);
		}
		return d;
   }
}
