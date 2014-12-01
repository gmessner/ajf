package com.messners.ajf.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


/**
 * This component creates a title for the Views in the ViewBrowser.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ViewTitle extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final int LEFT  = SwingConstants.LEFT;
	public static final int RIGHT = SwingConstants.RIGHT;

	protected JLabel titleLabel;
	protected JLabel rightIconLabel;
	protected JLabel leftIconLabel;
	protected int iconPosition;
	protected Component rightComponent;


	/**
	 * Default no-args constructor.
	 */
	public ViewTitle () {
		this(LEFT);
	}


	/**
	 * Creates an instance ready to display the icon on the specified side.
	 */
	public ViewTitle (int iconPosition) {

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		setOpaque(true);

		titleLabel = new JLabel();
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
		titleLabel.setOpaque(true);
		leftIconLabel = new JLabel();
		leftIconLabel.setOpaque(true);
		rightIconLabel = new JLabel();
		rightIconLabel.setOpaque(true);
		rightComponent = rightIconLabel;

		Color bg = titleLabel.getBackground().darker();
		setBackground(bg);
		titleLabel.setBackground(bg); 
		leftIconLabel.setBackground(bg); 
		rightIconLabel.setBackground(bg); 

		titleLabel.setForeground(Color.white);
		rightIconLabel.setForeground(Color.white);
		Font font = titleLabel.getFont();
		font = new Font(font.getFontName(), Font.BOLD, font.getSize());
		titleLabel.setFont(font);
		rightIconLabel.setFont(font);
		leftIconLabel.setFont(font);
		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		leftIconLabel.setHorizontalAlignment(SwingConstants.LEFT);
		rightIconLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		add(leftIconLabel, BorderLayout.WEST);
		add(titleLabel, BorderLayout.CENTER);
		add(rightIconLabel, BorderLayout.EAST);
		this.iconPosition = iconPosition;
	}


	/**
	 * Sets the title and icon.
	 */
	public void setTitle (String title, Icon icon) {

		if (iconPosition == RIGHT) {
			leftIconLabel.setIcon(null);
			titleLabel.setText(title);
			rightIconLabel.setIcon(icon);
		} else {
			leftIconLabel.setIcon(icon);
			titleLabel.setText(title);
			rightIconLabel.setIcon(null);
		}
	}


	/**
	 * Sets the title text.
	 */
	public void setTitleText (String title) {

		if (iconPosition == RIGHT) {
			titleLabel.setText(title);
		} else {
			titleLabel.setText(title);
		}
	}


	/**
	 * Sets the position of the view title icon.
	 *
	 * @param  position  the icon position constant
	 */
	public void setIconPosition (int position) {
		iconPosition = position;
	}


	/**
	 * Gets the JLabel component used for the title. This JLabel is also used
	 * to display the icon when positioned on the left.
	 *
	 * @return the JLabel for the title component
	 */
	public JLabel getTitleLabel () {
		return (titleLabel);
	}


	/**
	 * Gets the JLabel component used to display the icon when
	 * it is positioned on the left.
	 *
	 * @return the JLabel for the title component
	 */
	public JLabel getLeftIconLabel () {
		return (leftIconLabel);
	}


	/**
	 * Sets the component to display on the right side of the title.
	 */
	public void setRightComponent (Component c) {

		if (rightComponent != null) {
			remove(rightComponent);
		}

		if (c == null) {
			rightComponent = rightIconLabel;
		} else {
			rightComponent = c;
		}

		add(rightComponent, BorderLayout.EAST);
	}


	/**
	 * Gets the JLabel component used to display the icon when
	 * it is positioned on the right.
	 *
	 * @return the JLabel for the title component
	 */
	public JLabel getRightIconLabel () {
		return (rightIconLabel);
	}


	/**
	 * Sets the background of all the components that make up this ViewTitle
	 * instance.
	 */
	public void setTitleBackground (Color bg) {

		setBackground(bg);
		titleLabel.setBackground(bg); 
		leftIconLabel.setBackground(bg); 
		rightIconLabel.setBackground(bg); 
	}


	/**
	 * Sets the foreground of all the components that make up this ViewTitle
	 * instance.
	 */
	public void setTitleForeground (Color fg) {


		setForeground(fg);
		titleLabel.setForeground(fg); 
		leftIconLabel.setForeground(fg); 
		rightIconLabel.setForeground(fg); 
	}


	public void setEnabled (boolean flag) {

		super.setEnabled(flag);
		titleLabel.setEnabled(flag); 
		leftIconLabel.setEnabled(flag); 
		rightIconLabel.setEnabled(flag); 
	}
}
