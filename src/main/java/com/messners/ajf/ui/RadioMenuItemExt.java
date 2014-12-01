package com.messners.ajf.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;


/**
 * This class simply extends JRadioButtonMenuItem so it can monitor for
 * property changes and set its enabled flag if a "enabled"
 * property change is recieved. It also adds shortcut rendering
 * to the paint() method.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class RadioMenuItemExt extends JRadioButtonMenuItem
		implements java.beans.PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _keyBinding = null;
	private Font _acceleratorFont = null;
	private Color _acceleratorForeground = null;
	private Color _acceleratorSelectionForeground = null;
	private int _acceleratorWidth = 0;
	private Object _value;

	protected static FontRenderContext _font_renderer =
		new FontRenderContext(null, true, true);

	public RadioMenuItemExt (String label, String keyBinding) {

		super(label);
		_keyBinding = keyBinding;

		_acceleratorFont = UIManager
			.getFont("MenuItem.acceleratorFont");
		_acceleratorForeground = UIManager
			.getColor("MenuItem.acceleratorForeground");
		_acceleratorSelectionForeground = UIManager
			.getColor("MenuItem.acceleratorSelectionForeground");

		if (_keyBinding != null) {
			Rectangle2D r = _acceleratorFont.getStringBounds(
					_keyBinding, _font_renderer);
			_acceleratorWidth = (int)r.getWidth() + 32;
		}
	}


	public Dimension getPreferredSize() {

		Dimension d = super.getPreferredSize();
		if (_keyBinding != null) {
			d.width += _acceleratorWidth;
		}

		return (d);
	}


	public void paint (Graphics g) {

		super.paint(g);
		if (_keyBinding != null) {

			g.setFont(_acceleratorFont);
			g.setColor(getModel().isArmed() ?
				_acceleratorSelectionForeground :
				_acceleratorForeground);
			FontMetrics fm = g.getFontMetrics();
			Insets insets = getInsets();
			g.drawString(_keyBinding,getWidth() - (fm.stringWidth(
				_keyBinding) + insets.right + insets.left),
				getFont().getSize() + (insets.top - 1)
				/* XXX magic number */);
		}
	}


	public void propertyChange (java.beans.PropertyChangeEvent e) {

		if (e.getPropertyName().equals("enabled")) {
			setEnabled(((Boolean)e.getNewValue()).booleanValue());
		}
	}


	/**
	 * Gets the value.
	 */
	public Object getValue () {
		return _value;
	}


	/**
	 * Sets the value string.
	 *
	 * @param value  the value object that will be associated with the
	 *               radio button
	 */
	public void setValue (Object value) {
		_value = value;
	}
}
