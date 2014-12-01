package com.messners.ajf.ui;

import java.awt.Color;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.HashMap;


/**
 * This class provides static methods for converting strings to Color instances
 * and Color instances to HTML formatted hex color strings.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ColorUtils {

	/**
	 * Used to mapacolor strings to Color instances.
	 */
	private static HashMap<String, Color> nameToColorMap = null;

	/**
	 * Hide the constructor, this class is not ment to be instanciated.
	 */
	private ColorUtils () {
	}

    /**
     * Converts a type Color to a hex string in the format "#RRGGBB"
	 *
	 * @param color the Color to get the HTML hex color for
	 * @return the resulting HTML hex formatted color string
     */
    public static String colorToHtmlHex (Color color) {

		if (color == null) {
			return ("#000000");
		}

		StringBuffer buf = new StringBuffer(7);
		buf.append('#');

		/*
		 * Red
		 */
		String s = Integer.toHexString(color.getRed());
		if (s.length() > 2) {
			buf.append(s.substring(0, 2));
		} else if (s.length() < 2) {
			buf.append('0');
			buf.append(s);
		} else {
			buf.append(s);
		}

		/*
		 * Green
		 */
		s = Integer.toHexString(color.getGreen());
		if (s.length() > 2) {
			buf.append(s.substring(0, 2));
		} else if (s.length() < 2) {
			buf.append('0');
			buf.append(s);
		} else {
			buf.append(s);
		}

		/*
		 * Blue
		 */
		s = Integer.toHexString(color.getBlue());
		if (s.length() > 2) {
			buf.append(s.substring(0, 2));
		} else if (s.length() < 2) {
			buf.append('0');
			buf.append(s);
		} else {
			buf.append(s);
		}

		return (buf.toString());
    }


	/**
	 * Convert a "#FFFFFF" hex string to a Color. If the color specification 
	 * is bad, an attempt will be made to fix it up.
	 *
	 * @param value the hex formatted color string (may begin with '#')
	 * @return the resulting Color instance
	 */
    public static final Color hexToColor (String value) {

		String digits;
		if (value.startsWith("#")) {
		    digits = value.substring(1, Math.min(value.length(), 7));
		} else {
		    digits = value;
		}

		String hstr = "0x" + digits;
		Color c;
		try {
			c = Color.decode(hstr);
		} catch (NumberFormatException nfe) {
			  	c = null;
		}

	 	return (c);
    }


    /**
     * Convert a color string such as "RED" or "#NNNNNN" or "rgb(r, g, b)"
     * to a Color.
	 *
	 * @param str the color string
	 * @return the resulting Color instance
     */
    public static final Color stringToColor (String str) {

		if (str == null || str.length() == 0) {
			return (null);
		} 

		if (str.startsWith("rgb(")) {
			return (rgbToColor(str));
		}

		char c = str.charAt(0);
		if (c == '#') {
			return (hexToColor(str));
		}

		if (Character.digit(c, 16) != -1) {

			Color color = hexToColor(str);
			if (color != null) {
				return (color);
			}

		}

		return (nameToColor(str));
    }


	/**
	 * Gets the Color for the specified color name.  The color name
	 * must be in the rgb.txt file.
	 *
	 * @param name the color name to get the Color for
	 * @return the Color instance for the specified color name
	 */
	public static final Color nameToColor (String name) {

		if (nameToColorMap == null) {

			synchronized (ColorUtils.class) {

				if (nameToColorMap == null) {
					populateColorMap();
				}
			}
		}

		name = name.toLowerCase();
		return nameToColorMap.get(name);
	}


	/**
	 * Populates the name to color map with the data from the rgb.txt file.
	 */
	private static final void populateColorMap () {

		nameToColorMap = new HashMap<String, Color>();

		InputStream in = ColorUtils.class.getResourceAsStream("rgb.txt");
		if (in == null) {
			return;
		}


		InputStreamReader reader = new InputStreamReader(in);
		StreamTokenizer parser = new StreamTokenizer(reader);
		try {

			int type;
			int rgb[] = new int[3];
			int rgbIndex = 0;
			StringBuffer nameBuf = new StringBuffer();
			while ((type = parser.nextToken()) != StreamTokenizer.TT_EOF) {

				switch (type) {

					case StreamTokenizer.TT_NUMBER:

						/*
						 * If we have 3 colors then we are ready to add 
						 * another color name to the map, just do it!
						 */
						if (rgbIndex == 3) {

							if (nameBuf.length() != 0) {

								String name = nameBuf.toString().trim();
								Color color = new Color(rgb[0], rgb[1], rgb[2]);
								nameToColorMap.put(name, color);
								nameBuf.setLength(0);
							}

							rgbIndex = 0;
						}

						rgb[rgbIndex] = (int)parser.nval;
						rgbIndex++;
						break;

					case StreamTokenizer.TT_WORD:

						if (rgbIndex == 3) {

							String name = parser.sval.trim();
							if (nameBuf.length() > 0) {
								nameBuf.append(' ');
							}

							nameBuf.append(name);
						}

						break;
				}
			}

		} catch (IOException ioe) {

		} finally {

			try {
				reader.close();
			} catch (IOException ignore) {
			}

			reader = null;
		}
	}


    /**
     * Parses a String in the format <code>rgb(r, g, b)</code> where
     * each of the Color components is either an integer, or a floating number
     * with a % after indicating a percentage value of 255. Values are
     * constrained to fit with 0-255. The resulting Color is returned.
	 *
	 * @param string  the rgb string to parse
	 * @return the resulting Color
     */
    private static Color rgbToColor (String string) {

		/*
		 * Find the next numeric char
		 */
		int[] index = new int[1];

		index[0] = 4;
		int red = getColorComponent(string, index);
		int green = getColorComponent(string, index);
		int blue = getColorComponent(string, index);

		return (new Color(red, green, blue));
    }


    /**
     * Returns the next integer value from <code>string</code> starting
     * at <code>index[0]</code>. The value can either can an integer, or
     * a percentage (floating number ending with %), in which case it is
     * multiplied by 255.
     */
    private static int getColorComponent(String string, int[] index) {

		int length = string.length();
		char c;

		/*
		 * Skip non-decimal chars
		 */
		while(index[0] < length && (c = string.charAt(index[0])) != '-' &&
				!Character.isDigit(c) && c != '.') {

			index[0]++;
		}

		int start = index[0];
		if (start < length && string.charAt(index[0]) == '-') {
			index[0]++;
		}

		while(index[0] < length &&
				 Character.isDigit(string.charAt(index[0]))) {
			index[0]++;
		}

		if (index[0] < length && string.charAt(index[0]) == '.') {

			/*
			 * Decimal value
			 */
			index[0]++;
			while(index[0] < length &&
					Character.isDigit(string.charAt(index[0]))) {
			    index[0]++;
			}
		}

		if (start != index[0]) {
			try {
			    float value = Float.parseFloat(
							string.substring(start, index[0]));

			    if (index[0] < length && string.charAt(index[0]) == '%') {
					index[0]++;
			  		value = value * 255f / 100f;
		    	}
		    
				return (Math.min(255, Math.max(0, (int)value)));

			} catch (NumberFormatException nfe) {
			    // treat as 0
			}
		}

		return (0);
    }
}

