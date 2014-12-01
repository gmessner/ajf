package com.messners.ajf.ui;

import javax.swing.Icon;


/**
 * This class provides constants for the most commonly used LEDs.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class LedIcons {

	public static final Icon RED_LED;
	public static final Icon RED_BLINKING_LED;
	public static final Icon YELLOW_LED;
	public static final Icon YELLOW_BLINKING_LED;
	public static final Icon GREEN_LED;
	public static final Icon GRAY_LED;

	static {

		RED_LED = ResourceLoader.getIcon(
					LedIcons.class, "res/leds/red-led.gif");
		RED_BLINKING_LED = ResourceLoader.getIcon(
					LedIcons.class, "res/leds/red-blinking-led.gif");
		YELLOW_LED = ResourceLoader.getIcon(
					LedIcons.class, "res/leds/yellow-led.gif");
		YELLOW_BLINKING_LED = ResourceLoader.getIcon(
					LedIcons.class, "res/leds/yellow-blinking-led.gif");
		GREEN_LED = ResourceLoader.getIcon(
					LedIcons.class, "res/leds/green-led.gif");
		GRAY_LED = ResourceLoader.getIcon(
					LedIcons.class, "res/leds/gray-led.gif");
	}
}
