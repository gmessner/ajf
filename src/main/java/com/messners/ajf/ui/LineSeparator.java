package com.messners.ajf.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.UIManager;


/**
 * This component is used as a visual separator on the toolbar.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class LineSeparator extends JComponent {

	private static final long serialVersionUID = 1L;
	public static final int VERTICAL   = 0;
	public static final int HORIZONTAL = 1;


	protected int _margin_width  = 0;
	protected int _margin_height = 0;
	protected int _orientation   = VERTICAL;
	protected Color _highlight   = null;
	protected Color _shadow      = null;


	public LineSeparator (
		int orientation, int margin_width, int margin_height) {
	
		_orientation   = orientation;
		_margin_width  = margin_width;
		_margin_height = margin_height;

		_shadow = UIManager.getColor("Separator.shadow");
		_highlight = UIManager.getColor("Separator.highlight");
	}

	public LineSeparator (int margin_width, int margin_height) {
	
		_orientation   = VERTICAL;
		_margin_width  = margin_width;
		_margin_height = margin_height;

		_shadow = UIManager.getColor("Separator.shadow");
		_highlight = UIManager.getColor("Separator.highlight");
	}

	public Dimension getPreferredSize () {
		if (_orientation == VERTICAL) {
			return (new Dimension(2 + _margin_width * 2, 
				getParent().getHeight() - _margin_height * 2));
		} else {
			return (new Dimension(
				getParent().getWidth() - _margin_width * 2,
				2 + _margin_height * 2));
		}
	}


	public void paintComponent (Graphics g) {

		if (_orientation == VERTICAL) {

			int height = getHeight() - _margin_height * 2;

			g.setColor(_shadow);
			g.drawLine(_margin_width, _margin_height,
				_margin_width, height);
			g.setColor(_highlight);
			g.drawLine(_margin_width + 1, _margin_height,
				_margin_width + 1, height);
		} else {

			int width = getWidth() - _margin_width * 2;

			g.setColor(_shadow);
			g.drawLine(_margin_width, _margin_height,
				width, _margin_height);
			g.setColor(_highlight);
			g.drawLine(_margin_width, _margin_height + 1,
				width, _margin_height + 1);
		}
	}
}


