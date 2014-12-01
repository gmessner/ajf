package com.messners.ajf.ui;

import java.awt.Font;
import java.awt.font.LineMetrics;
import java.awt.font.FontRenderContext;

import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.UIManager;


/**
 * This border should be used for grouping components on a dialog form
 * It provides the proper spacing IAW the Java Look & Feel Guidelines.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class TitledPanelBorder extends CompoundBorder implements UIConstants {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Used to compute the offset for the inner border top margin.
	 */
	protected static FontRenderContext fontRenderer =
						new FontRenderContext(null, true, true);


	/**
	 * Creates a border with no title and the default inner margins title.
	 */
	public TitledPanelBorder () {
		this((String)null, -1, -1, -1, -1);
	}

	/**
	 * Creates a border with no title and the specified inner margins.
	 *
	 * @param  top     the top inner margin
	 * @param  left    the left inner margin
	 * @param  bottom  the bottom inner margin
	 * @param  right   the right inner margin
	 */
	public TitledPanelBorder (int top, int left, int bottom, int right) {
		this((String)null, top, left, bottom, right);
	}


	/**
	 * Creates a border with the specified title and
	 * default inner margins title.
	 *
	 * @param  title   the title for the border
	 */
	public TitledPanelBorder (String title) {
		this(title, -1, -1, -1, -1);
	}


	/**
	 * Creates a border with the specified title and inner margins.
	 *
	 * @param  title   the title for the border
	 * @param  top     the top inner margin
	 * @param  left    the left inner margin
	 * @param  bottom  the bottom inner margin
	 * @param  right   the right inner margin
	 */
	public TitledPanelBorder (
				String title, int top, int left, int bottom, int right) {

		outsideBorder = BorderFactory.createTitledBorder(title);

		int offset = 0;
		if (title != null) {

			Font font = ((TitledBorder)outsideBorder).getTitleFont();
			if (font == null) {
				font = UIManager.getDefaults().getFont("TitledBorder.font");
			}
			
			LineMetrics lm = font.getLineMetrics(title, fontRenderer);
			offset = (int)lm.getDescent();
		}

		insideBorder  = BorderFactory.createEmptyBorder(
			(top == -1 ? TITLED_PANEL_INNER_TOP_MARGIN - offset : top),
			(left == -1 ? TITLED_PANEL_INNER_LEFT_MARGIN : left),
			(bottom == -1 ? TITLED_PANEL_INNER_BOTTOM_MARGIN : bottom),
			(right == -1 ? TITLED_PANEL_INNER_RIGHT_MARGIN : right));
	}
}
