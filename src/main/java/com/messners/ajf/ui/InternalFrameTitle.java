package com.messners.ajf.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


/**
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class InternalFrameTitle extends JComponent {

	private static final long serialVersionUID = 1L;
	protected InternalFrame frame;
	protected JButton closeButton;
	protected JButton shadeButton;

	protected Color selectedTitleColor;
	protected Color selectedTextColor;
	protected Color notSelectedTitleColor;
	protected Color notSelectedTextColor;

	protected Action closeAction;
	protected ActionListener shadeAction;

	protected static final Icon minIcon;
	protected static final Icon maxIcon;
	protected static final Icon closeIcon;
	static {
		minIcon = ResourceLoader.getIcon(InternalFrameTitle.class,
						"frame-minimize.gif");
		maxIcon = ResourceLoader.getIcon(InternalFrameTitle.class,
						"frame-maximize.gif");
		closeIcon = ResourceLoader.getIcon(InternalFrameTitle.class,
						"frame-close.gif");
	}

	protected static final String CLOSE_CMD =
		UIManager.getString("InternalFrameTitlePane.closeButtonText");

	private String minimizeButtonToolTip;
	private String maximizeButtonToolTip;
	private String closeButtonToolTip;

	public InternalFrameTitle(InternalFrame f) {

		frame = f;
		installTitlePane();
	}

	protected void installTitlePane () {

		installDefaults();
		
		createActions();
		enableActions();

		setLayout(createLayout());


		createCloseButton();
		createShadeButton();
		setShadeButtonIcon();

		addSubComponents();
	}


	protected void addSubComponents () {
		add(shadeButton);
		add(closeButton);
	}


	protected void createActions () {
		closeAction = new CloseAction();

		shadeAction = new ShadeListener();
		addMouseListener((MouseListener)shadeAction);
	}

	protected void enableActions () {
		closeAction.setEnabled(frame.isClosable());
	}


	protected void installDefaults() {


		selectedTitleColor = UIManager.getColor(
								"InternalFrame.activeTitleBackground");
		selectedTextColor = UIManager.getColor(
								"InternalFrame.activeTitleForeground");
		notSelectedTitleColor = UIManager.getColor(
								"InternalFrame.inactiveTitleBackground");
		notSelectedTextColor = UIManager.getColor(
								"InternalFrame.inactiveTitleForeground");
		setFont(UIManager.getFont(
								"InternalFrame.titleFont"));
		closeButtonToolTip = UIManager.getString(
								"InternalFrame.closeButtonToolTip");
		minimizeButtonToolTip = UIManager.getString(
								"InternalFrame.iconButtonToolTip");
		maximizeButtonToolTip = UIManager.getString(
								"InternalFrame.maxButtonToolTip");
	}


	protected void createCloseButton () {

		closeButton = new NoFocusButton();  
		closeButton.addActionListener(closeAction);
		if (closeButtonToolTip != null && closeButtonToolTip.length() != 0) {
			closeButton.setToolTipText(closeButtonToolTip);
		}

		closeButton.setIcon(closeIcon);
	}


	protected void createShadeButton () {

		shadeButton = new NoFocusButton();  
		shadeButton.addActionListener(shadeAction);
	}


	public void setShadeButtonIcon () {

		if (!frame.isShadable()) {
			return;
		}

		String tip;
		if (frame.isShadeUp()) {

			shadeButton.setIcon(maxIcon);
			tip = maximizeButtonToolTip;
		
		} else {

			shadeButton.setIcon(minIcon);
			tip = minimizeButtonToolTip;
		}

		if (tip != null && tip.length() != 0) {
			shadeButton.setToolTipText(tip);
		}
	}


	public void paintComponent (Graphics g)  {

		paintTitleBackground(g);

		if (frame.getTitle() != null) {


			boolean isSelected = true;
			Font f = g.getFont();
			paintIcon(g);

			g.setFont(getFont());
			if (isSelected) {
				g.setColor(selectedTextColor);
			} else {
				g.setColor(notSelectedTextColor);
			}


			/*
			 * Center text vertically.
			 */
			FontMetrics fm = g.getFontMetrics();
			int baseline = (getHeight() + fm.getAscent() -
					fm.getLeading() - fm.getDescent()) / 2;

			Rectangle r = new Rectangle(0, 0, 0, 0);
			if (frame.isShadable()) {
				r = shadeButton.getBounds();
			} else if (frame.isClosable()) {
				r = closeButton.getBounds();
			}

		
			int titleX;
			int titleW;
			String title = frame.getTitle();
			if (frame.getComponentOrientation().isLeftToRight()) {

			  if (r.x == 0) {
				  r.x = frame.getWidth() - frame.getInsets().right;
				}

			  titleX = 20;
			  titleW = r.x - titleX - 3;
			  title = getTitle(frame.getTitle(), fm, titleW);

			} else {
				titleX = - 2 - SwingUtilities.computeStringWidth(fm, title);
			}
			
			g.drawString(title, titleX, baseline);
			g.setFont(f);
		}
	}


	/**
	 * Invoked from paintComponent.  Paints the background of the titlepane.
	 * All text and icons will then be rendered on top of this background.
	 *
	 * @param g the graphics to use to render the background
	 */
	protected void paintTitleBackground (Graphics g) {

		boolean isSelected = true;

		if (isSelected) {
			g.setColor(selectedTitleColor);
		} else {
			g.setColor(notSelectedTitleColor);
		}

		g.fillRect(0, 0, getWidth(), getHeight());
	}


	protected String getTitle(String text, FontMetrics fm, int availTextWidth) {

		if ( (text == null) || (text.equals(""))) {
			return ("");
		}

		int textWidth = SwingUtilities.computeStringWidth(fm, text);
		String clipString = "...";
		if (textWidth > availTextWidth) {

			int totalWidth = SwingUtilities.computeStringWidth(fm, clipString);
			int nChars;
			for (nChars = 0; nChars < text.length(); nChars++) {

				totalWidth += fm.charWidth(text.charAt(nChars));
				if (totalWidth > availTextWidth) {
					break;
				}
			}

			text = text.substring(0, nChars) + clipString;
		}

		return (text);
	  }



	protected LayoutManager createLayout() {
		return (new TitlePaneLayout());
	}


	/**
	 * This inner class is marked &quot;public&quot; due to a compiler bug.
	 * This class should be treated as a &quot;protected&quot; inner class.
	 * Instantiate it only within subclasses of <Foo>.
	 */
	public class TitlePaneLayout implements LayoutManager {

		public void addLayoutComponent(String name, Component c) {
		}

		public void removeLayoutComponent(Component c) {
		}	

		public Dimension preferredLayoutSize(Container c)  {
			return minimumLayoutSize(c);
		}
	
		public Dimension minimumLayoutSize(Container c) {

			/*
			 * Calculate width.
			 */
			int width = 22;

			if (frame.isClosable()) {
				width += 19;
			}

			if (frame.isShadable()) {
				width += 19;
			}

			FontMetrics fm = getFontMetrics(getFont());
			String frameTitle = frame.getTitle();
			int title_w = frameTitle != null ? fm.stringWidth(frameTitle) : 0;
			int title_length = frameTitle != null ? frameTitle.length() : 0;

			/*
			 * Leave room for three characters in the title.
			 */
			if (title_length > 3) {
				int subtitle_w =
					fm.stringWidth(frameTitle.substring(0, 3) + "...");
				width += (title_w < subtitle_w) ? title_w : subtitle_w;
			} else {
				width += title_w;
			}

			/*
			 * Calculate height.
			 */
			Icon icon = frame.getFrameIcon();
			int fontHeight = fm.getHeight();
			fontHeight += 2;
			int iconHeight = 0;
			if (icon != null) {
				// SystemMenuBar forces the icon to be 16x16 or less.
				iconHeight = Math.min(icon.getIconHeight(), 16);
			}

			iconHeight += 2;
			int height = Math.max(fontHeight, iconHeight);

			Dimension dim = new Dimension(width, height);

			/*
			 * Take into account the border insets if any.
			 */
			if (getBorder() != null) {
				Insets insets = getBorder().getBorderInsets(c);
				dim.height += insets.top + insets.bottom;
				dim.width += insets.left + insets.right;
			}

			return (dim);
		}

	
		public void layoutContainer (Container c) {

			boolean leftToRight = 
				frame.getComponentOrientation().isLeftToRight();
			
			int w = getWidth();
			int h = getHeight();

			int buttonHeight = closeButton.getIcon().getIconHeight();
			int x = (leftToRight) ? w - 16 - 2 : 2;
			
			if (frame.isClosable()) {
				closeButton.setBounds(x, (h - buttonHeight) / 2, 16, 14);
				x += (leftToRight) ? - (16 + 2) : 16 + 2;
			}

			if (frame.isShadable()) {
				shadeButton.setBounds(x, (h - buttonHeight) / 2, 16, 14);
			} 
		}
	}


	/**
	 * This inner class is marked &quot;public&quot; due to a compiler bug.
	 * This class should be treated as a &quot;protected&quot; inner class.
	 * Instantiate it only within subclasses of <Foo>.
	 */  
	public class CloseAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CloseAction() {
			super(CLOSE_CMD);
		}

		public void actionPerformed(ActionEvent e) {
			if(frame.isClosable()) {
				frame.doDefaultCloseAction();
			}
		}	  
	}


	private class NoFocusButton extends JButton {
		
		private static final long serialVersionUID = 1L;

		public NoFocusButton () {
			setFocusPainted(false);
			setMargin(new Insets(0,0,0,0));
		}

		public boolean isFocusable () {
			return (false);
		}

		public void requestFocus () {
		}

		public boolean isOpaque () {
			return (true);
		}
	}


	public void paintIcon (Graphics g) {

	    Icon icon = frame.getFrameIcon();
	    if (icon == null) {
	      icon = UIManager.getIcon("InternalFrame.icon");
	    }

	    if (icon != null) {
			icon.paintIcon(this, g, 0, 0);
    	}
	}


	/**
	 * This class listens for mouse events and shades or unshades the 
	 * containing frame.
	 */
	private class ShadeListener extends MouseAdapter implements ActionListener {

		public void mouseClicked (MouseEvent e) {

			if (!frame.isShadable() || e.getClickCount() < 2) {
				return;
			}

			frame.toggleShade();
		}


		public void actionPerformed(ActionEvent e) {

			if(frame.isShadable()) {
				frame.toggleShade();
			}
		}	  
	}
}

