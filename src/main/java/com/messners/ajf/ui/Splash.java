package com.messners.ajf.ui;

import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;


/**
 * This class provides a component to draw a splash graphic during
 * program startup.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class Splash extends JWindow implements Serializable {

	private static final long serialVersionUID = 1L;
	protected transient JLabel _label  = null;
	protected transient JLabel _status = null;


	/**
	 * No args constructor.
	 */
	public Splash () {
		super();
		init();
	}


	/**
	 * This contructor sets the image filename for the
	 * splash screen.
	 *
	 * @param  image_filename  the filename for the image of the
	 *                         splash screen
	 */
	public Splash (String image_filename) {

		super();
		init();
		drawImage(new ImageIcon(image_filename));
		pack();
	}


	/**
	 * This contructor sets the title, image URL 
	 * for the splash screen.
	 *
	 * @param  location  the URL for the image of the splash screen
	 */ 
	public Splash (java.net.URL location) {

		super();
		init();
		drawImage(new ImageIcon(location));
		pack();
	}


	/**
	 * This contructor sets the title, image URL and icon
	 * URL for the splash screen.
	 *
	 * @param  image_icon 
	 */
	public Splash (ImageIcon image_icon) {

		super();
		init();

		if (image_icon != null) {
			drawImage(image_icon);
		}

		pack();
	}


	protected void init () {

		JPanel pane = (JPanel)getContentPane();

		pane.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createRaisedBevelBorder(),
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(2, 2, 2, 2),
				BorderFactory.createLoweredBevelBorder())));
				
		_label = new JLabel();
		pane.add(_label, BorderLayout.CENTER);

		_status = new JLabel(" ", javax.swing.SwingConstants.CENTER);
		_status.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		_status.setOpaque(false);
		pane.add(_status, BorderLayout.SOUTH);
	}


	/**
	 * Specifies a new image URL to draw on the splash screen.
	 *
	 * @param  location  the URL of an image
	 */
	public void setImage (java.net.URL location) {
		drawImage(new ImageIcon(location));
	}


	/**
	 * Specifies a new image filename to draw on the splash screen.
	 *
	 * @param  filename  the filename of an image
	 */
	public void setImage (String filename) {
		drawImage(new ImageIcon(filename));
	}

	
	/**
	 * Draw the specified image on the splash screen window.
	 *
	 * @param  image  the image to draw
	 */
	protected void drawImage (ImageIcon image) {
	
		_label.setIcon(image);

		Dimension size = _status.getPreferredSize();
		int width  = image.getIconWidth();
		int height = image.getIconHeight() + size.height;
		setSize(width, height);
		Dimension screenSize =
			Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - width / 2,
			screenSize.height / 2 - height / 2);
	}


	/**
	 * Shows/hides the splash screen.
	 */
	public void setVisible (boolean flag) {

		super.setVisible(flag);

		JComponent root = getRootPane();
		root.paintImmediately(
		    javax.swing.SwingUtilities.getLocalBounds(root));
	}


	public void setStatus (String label) {

		Worker worker = new Worker(label);
		worker.start();
	}


	class Worker extends SwingWorker {

		String label;
		Worker (String label) {
			this.label = label;
		}


		public Object construct () {
			_status.setText(label);
			return (label);
		}
	}


	public Color getStatusBackground () {
		return (_status.getBackground());
	}


	public void setStatusBackground (Color color) {
		_status.setBackground(color);
	}


	public Color getStatusForeground () {
		return (_status.getForeground());
	}


	public void setStatusForeground (Color color) {
		_status.setForeground(color);
	}
}

