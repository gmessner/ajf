
package com.messners.ajf.ui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;


/**
 * This component uses a JTextArea as a multi-line label.
 *
 * @author Greg Messner <greg@messners.com>
 */
public class TextLabel extends JTextArea {

	private static final long serialVersionUID = 1L;

	public TextLabel (String text) {

		super();
		super.setText(text);
		setBorder(null);
		setWrapStyleWord(true);
		setEditable(false);
		setLineWrap(true);
	
		String propertyName = "Label.background";
		if (UIManager.get(propertyName) != null) {
			setBackground(UIManager.getColor(propertyName));
		}
	
		propertyName = "Label.foreground";
		if (UIManager.get(propertyName) != null) {
			setForeground(UIManager.getColor(propertyName));
		}
	
		propertyName = "Label.font";
		if (UIManager.get(propertyName) != null) {
			setFont(UIManager.getFont(propertyName));
		}
	
		setFocusable(false);
	}


	/**
	 * Calculates the preferred size based on newline delimited text
	 */
	 public Dimension getCalculatedPreferredSize () {
			 
		String label = getText();

		if (label == null) {
			return (super.getPreferredSize());
		}


		FontMetrics fm = getFontMetrics(getFont());
		if (fm == null) {
			return (super.getPreferredSize());
		}

		int numLines = 0;
		ArrayList<String> lines = new ArrayList<String>();
		int firstDelim = 0;
		if (label.charAt(0) == '\n') {
			lines.add("");
		}

		int nextDelim = label.indexOf("\n", 1);

		while (nextDelim >= 0) {
			lines.add(label.substring(firstDelim, nextDelim));
			firstDelim = nextDelim + 1;
			nextDelim = label.indexOf("\n", firstDelim);
		}

		/*
		 * Add the last (or only) field
		 */
		lines.add(label.substring(firstDelim));
		numLines = lines.size();

		int lineHeight = fm.getHeight();
	   	int maxWidth = 0;
	   	for (int i = 0; i < numLines; i++) {

			int lineWidth = fm.stringWidth((String)lines.get(i));
			if (lineWidth > maxWidth) {
				maxWidth = lineWidth;
			}
	   	}

		Border border = getBorder();
		Insets insets;
		if (border == null) {
			insets = getInsets();
		} else {
			insets = border.getBorderInsets(this);
		}

		return (new Dimension(maxWidth + insets.left + insets.right, 
					numLines * lineHeight + insets.top + insets.bottom));
	}
}
