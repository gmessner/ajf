package com.messners.ajf.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;


/**
 * This class needs javadoc.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class PullRightButton extends ButtonExt {

	private static final long serialVersionUID = 1L;
	private static final Icon defaultArrowIcon = new RightArrowIcon();
	private static final Color GRAY = new Color(127, 127, 127);

	private Icon arrowIcon = defaultArrowIcon;
	private Icon disabledArrowIcon = defaultArrowIcon;
	private int arrowGap = 8;

	private JPopupMenu popup;


	/**
	 * Creates a button with no set text or icon.
	 */
	public PullRightButton () {
		super();
	}


	/**
	 * Creates a button using the specified Action.
	 */
	public PullRightButton (Action action) {
		super(action);
	}


	/**
	 * Creates a button with an icon.
	 *
	 * @param icon the Icon image to display on the button
	 */
	public PullRightButton (Icon icon) {
		super(icon);
	}


	/**
	 * Creates a button with text.
	 *
	 * @param text  the text of the button
	 */
	public PullRightButton (String text) {
		super(text);
	}


	/**
	 * Creates a button with initial text and an icon.
	 *
	 * @param text  the text of the button.
		 * @param icon  the Icon image to display on the button
	 */
	public PullRightButton (String text, Icon icon) {
		super(text, icon);
	}


	public JPopupMenu getPopup () {
		return (popup);
	}


	public void setPopup (JPopupMenu popup) {
		this.popup = popup;
	}
	

	public Dimension getPreferredSize () {

		Dimension size = super.getPreferredSize();
		size.width += arrowGap + arrowIcon.getIconWidth();
		return (size);
	}


	public void paint (Graphics g) {

		super.paint(g);

		// Paint the arrow Icon
		paintArrowIcon(g, this);
	}

	
	protected void paintArrowIcon (Graphics g, AbstractButton b){

		ButtonModel model = b.getModel();
		if (arrowIcon == null) {
		   return;
		}

		Icon icon = arrowIcon;
		if (!model.isEnabled()) {
			icon = (Icon)getDisabledArrowIcon();
		}

		if (icon == null) {
			return;
		}

        Insets insets = b.getInsets();
        Insets margin = b.getMargin();
		int x = b.getWidth() -  icon.getIconWidth() - 
				insets.right + margin.right;
		int y = b.getHeight() / 2 - icon.getIconHeight() / 2;

		if (model.isPressed() && model.isArmed()) {
			icon.paintIcon(b, g, x, y);
		} else {
			icon.paintIcon(b, g, x, y);
		}
	}

   
	/**
	 * Returns the arrow icon used by the button when it's disabled.
	 * If no disabled arrow icon has been set, this method constructs
	 * one from the default arrow icon. 
	 *
	 * @return the <code>disabledArrowIcon</code> property
	 */
	public Icon getDisabledArrowIcon () {
			
		if (disabledArrowIcon == null) {

			if (arrowIcon != null && arrowIcon instanceof ImageIcon) {
				disabledArrowIcon = new ImageIcon(
					GrayFilter.createDisabledImage(
						((ImageIcon)arrowIcon).getImage()));
			}
		}

		return (disabledArrowIcon);
	}


   static private class RightArrowIcon implements Icon {

         public void paintIcon (Component c, Graphics g, int x, int y) {

            g.translate(x, y);

			if (c.isEnabled()) {
	            g.setColor(Color.BLACK);
			} else {
	            g.setColor(GRAY);
			}
            g.drawLine(0, 0, 0, 6);
            g.drawLine(1, 1, 1, 5);
            g.drawLine(2, 2, 2, 4);
            g.drawLine(3, 3, 3, 3);
            g.translate(-x, -y);
        }

        public int getIconWidth () {
            return (4);
        }

        public int getIconHeight () {
            return (7);
        }
    }
}
