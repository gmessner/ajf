/* *************************************************************************
 *
 *  Copyright (c) 2000-2009 Greg Messner. All Rights Reserved.
 *
 * ************************************************************************* */

package com.messners.ajf.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * This class provides a scaled version of the provided Icon keeping
 * the aspect relation of the original icon intact by offsetting the image.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public class ScaledImageIcon extends ImageIcon {

	private static final long serialVersionUID = 1L;
	
	private int width;
	private int height;

	private int offsetX;
	private int offsetY;


	public ScaledImageIcon (Icon icon, int width, int height) {

		super();

		this.width  = width;
		this.height = height;

		if (icon instanceof ImageIcon) {

			setDescription(((ImageIcon)icon).getDescription());
			Image img = ((ImageIcon)icon).getImage();
			setImage(img);

		} else {

			int iconWidth  = icon.getIconWidth();
			int iconHeight = icon.getIconHeight();
			BufferedImage img = createImage(iconWidth, iconHeight);
			Graphics g = img.createGraphics();
			icon.paintIcon(null, g, 0, 0);
			setImage(img);
        	g.dispose();
		}
	}


	private BufferedImage createImage (int width, int height) {

		BufferedImage imgBuffer;
		try {

			GraphicsEnvironment ge = 
					GraphicsEnvironment.getLocalGraphicsEnvironment();

			/*
			 * Create the buffered image
			 */
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			imgBuffer = gc.createCompatibleImage(
					width, height, Transparency.BITMASK);

      	} catch(Exception e) {
			imgBuffer = null;
      	}

		if (imgBuffer == null) {

			System.out.println("Transparency bitmask failed");

			/*
			 * Create a buffered image using the default color model
			 */
			imgBuffer = new BufferedImage(
							width, height, BufferedImage.TYPE_INT_ARGB);
      	}

		return (imgBuffer);
	}


    /**
     * Gets the width of the icon.
     *
     * @return the width in pixels of this icon
     */
    public int getIconWidth() {
		return (width);
    }
    

    /**
     * Gets the height of the icon.
     *
     * @return the height in pixels of this icon
     */
    public int getIconHeight() {
		return (height);
    }


    /**
     * Sets the image displayed by this icon.
	 *
     * @param img the image
     */
	public void setImage (Image img) {

		int imgWidth  = img.getWidth(null);
		int imgHeight = img.getHeight(null);
		if (imgWidth == width && imgHeight == height) {

			super.setImage(img);
			offsetX = offsetY = 0;
			
		} else {

			/*
			 * Figure out the scale for the width and height 
			 */
			double sx = (double)width  / (double)imgWidth;
			double sy = (double)height / (double)imgHeight;

			/*
			 * Since we wish to keep the aspect ratio intact use the lesser of
			 * scales and compute the x and y offsets used when painting
			 * the icon
			 */
			int scaledWidth  = -1;
			int scaledHeight = -1;
			if (sx < sy) {
				scaledWidth = width;
				sy = sx;
			} else {
				scaledHeight = height;
				sx = sy;
			}

			imgWidth  *= sx;
			imgHeight *= sy;

			offsetX = (int)(((double)(width -  imgWidth) + .5)  / 2.0);
			offsetX = (offsetX > 0 ? offsetX : 0);
			offsetY = (int)(((double)(height - imgHeight) + .5) / 2.0);
			offsetY = (offsetY > 0 ? offsetY : 0);
				
			img = img.getScaledInstance(
					scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

			/*
			 * Do this to make sure that just scaled image is completely loaded
			 */
			img = new ImageIcon(img).getImage();


			super.setImage(img);
		}
	}


	/**
	 * Paints this icon.  We override this so we can offset when
	 * we paint the image so we can keep the aspect ration constant.
	 */
	public void paintIcon (Component c, Graphics g, int x, int y) {
		super.paintIcon(c, g, x + offsetX, y + offsetY);
	}
}
