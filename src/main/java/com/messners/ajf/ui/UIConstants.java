package com.messners.ajf.ui;

import java.awt.Color;
import java.awt.Insets;
import javax.swing.SwingConstants;


/**
 * This interface provides constants which should be utilized when building
 * user interfaces to provide a consistent look and feel.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface UIConstants extends SwingConstants {


	/**
	 * Horizontal space betwwen command buttons on a dialog.
	 */
	public static final int COMMAND_BUTTON_SPACING = 5;

	public static final int COMMAND_BUTTONS_TOP_MARGIN = 17;

	public static final int COMMAND_BUTTONS_LEFT_MARGIN = 12;

	public static final int COMMAND_BUTTONS_BOTTOM_MARGING = 11;

	public static final int COMMAND_BUTTONS_RIGHT_MARGIN = 11;

	public static final int TITLED_PANEL_INNER_TOP_MARGIN = 12;

	public static final int TITLED_PANEL_INNER_LEFT_MARGIN = 12;

	public static final int TITLED_PANEL_INNER_BOTTOM_MARGIN = 11;

	public static final int TITLED_PANEL_INNER_RIGHT_MARGIN = 11;

	public static final int VGAP = 5;

	public static final int HGAP = 12;

	public static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);


	public static final Color LIGHT_RED    = new Color(255, 192, 192);
	public static final Color LIGHT_GREEN  = new Color(192, 255, 192);
	public static final Color LIGHT_BLUE   = new Color(192, 192, 255);
	public static final Color LIGHT_YELLOW = new Color(255, 255, 192); 
	public static final Color LIGHT_GRAY   = new Color(224, 224, 224);

	public static final Color DARK_RED   = Color.RED.darker().darker();
	public static final Color DARK_GREEN = Color.GREEN.darker().darker();
	public static final Color DARK_BLUE  = Color.BLUE.darker().darker();
}
