package com.messners.ajf.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * This class needs Javadocs
 * 
 * 
 * @author Greg Messner <greg@messners.com>
 */
public class InternalFrame extends JPanel implements WindowConstants {

	private static final long serialVersionUID = 1L;
	protected String title;
	protected Icon frameIcon;
	protected InternalFrameTitle internalFrameTitle;
	protected boolean shadeIsUp;
	protected boolean closable;
	protected boolean shadable;
	protected Container contentPane;
	protected boolean contentPaneCheckingEnable = true;
	protected int defaultCloseOperation = DISPOSE_ON_CLOSE;

	public InternalFrame() {
		closable = true;
		shadable = true;
		createPanes();
	}

	public InternalFrame(String title) {
		this.title = title;
		closable = true;
		shadable = true;
		createPanes();
	}

	public InternalFrame(String title, boolean closable) {
		this.title = title;
		this.closable = closable;
		shadable = true;
		createPanes();
	}

	protected void createPanes() {

		BorderLayout layout = new BorderLayout();
		setLayout(layout);

		contentPaneCheckingEnable = false;
		internalFrameTitle = new InternalFrameTitle(this);
		add(internalFrameTitle, BorderLayout.NORTH);

		contentPane = new JPanel(new BorderLayout());
		add(contentPane, BorderLayout.CENTER);
		contentPaneCheckingEnable = true;
	}

	/**
	 * Fires an <code>INTERNAL_FRAME_CLOSING</code> event and then performs the
	 * action specified by the internal frame's default close operation. This
	 * method is typically invoked by the look-and-feel-implemented action
	 * handler for the internal frame's close button.
	 * 
	 * @since 1.3
	 * @see #setDefaultCloseOperation
	 * @see javax.swing.event.InternalFrameEvent#INTERNAL_FRAME_CLOSING
	 */
	public void doDefaultCloseAction() {

		switch (defaultCloseOperation) {

		case DO_NOTHING_ON_CLOSE:
			break;

		case HIDE_ON_CLOSE:
			setVisible(false);
			break;

		case DISPOSE_ON_CLOSE:
			setVisible(false);
			Container c = getParent();
			c.remove(this);
			break;

		default:
			break;
		}
	}

	/**
	 * Sets the operation that will happen by default when the user initiates a
	 * "close" on this internal frame. The possible choices are:
	 * <p>
	 * <dl>
	 * <dt><code>DO_NOTHING_ON_CLOSE</code>
	 * <dd>Do nothing. This requires the program to handle the operation in the
	 * <code>windowClosing</code> method of a registered
	 * <code>InternalFrameListener</code> object.
	 * <dt><code>HIDE_ON_CLOSE</code>
	 * <dd>Automatically make the internal frame invisible.
	 * <dt><code>DISPOSE_ON_CLOSE</code>
	 * <dd>Automatically dispose of the internal frame.
	 * </dl>
	 * <p>
	 * The default value is <code>DISPOSE_ON_CLOSE</code>. Before performing the
	 * specified close operation, the internal frame fires an
	 * <code>INTERNAL_FRAME_CLOSING</code> event.
	 * 
	 * @param operation
	 *            one of the following constants defined in
	 *            <code>javax.swing.WindowConstants</code> (an interface
	 *            implemented by <code>JInternalFrame</code>):
	 *            <code>DO_NOTHING_ON_CLOSE</code>, <code>HIDE_ON_CLOSE</code>,
	 *            or <code>DISPOSE_ON_CLOSE</code>
	 * 
	 * @see #getDefaultCloseOperation
	 * @see #setVisible
	 */
	public void setDefaultCloseOperation(int operation) {
		this.defaultCloseOperation = operation;
	}

	/**
	 * Returns the default operation that occurs when the user initiates a
	 * "close" on this internal frame.
	 * 
	 * @return the operation that will occur when the user closes the internal
	 *         frame
	 * @see #setDefaultCloseOperation
	 */
	public int getDefaultCloseOperation() {
		return (defaultCloseOperation);
	}

	/**
	 * Returns the title of the <code>JInternalFrame</code>.
	 * 
	 * @return a <code>String</code> containing this internal frame's title
	 * @see #setTitle
	 */
	public String getTitle() {
		return (title);
	}

	/**
	 * Sets the <code>JInternalFrame</code> title. <code>title</code> may have a
	 * <code>null</code> value.
	 * 
	 * @see #getTitle
	 */
	public void setTitle(String title) {

		this.title = title;
		if (internalFrameTitle != null && isShowing()) {
			internalFrameTitle.repaint();
		}
	}

	public static void setTitle(Component c, String title) {

		InternalFrame f = findInternalFrame(c);
		if (f != null) {
			f.setTitle(title);
		}
	}

	public static InternalFrame findInternalFrame(Component c) {

		while (c != null && !(c instanceof InternalFrame)) {
			c = c.getParent();
		}

		if (c instanceof InternalFrame) {
			return ((InternalFrame) c);
		}

		return (null);
	}

	public void setIcon(Icon icon) {
		setFrameIcon(icon);
	}

	public static void setIcon(Component c, Icon icon) {

		InternalFrame f = findInternalFrame(c);
		if (f != null) {
			f.setFrameIcon(icon);
		}
	}

	/**
	 * Sets an image to be displayed in the titlebar of this internal frame
	 * (usually in the top-left corner).
	 * 
	 * Passing <code>null</code> to this function is valid, but the look and
	 * feel can choose the appropriate behavior for that situation, such as
	 * displaying no icon or a default icon for the look and feel.
	 * 
	 * @param icon
	 *            the <code>Icon</code> to display in the title bar
	 * @see #getFrameIcon
	 */
	public void setFrameIcon(Icon icon) {

		if (icon == frameIcon) {
			return;
		}

		// Resize to 16x16 if necessary.
		if (icon instanceof ImageIcon
				&& (icon.getIconWidth() > 16 || icon.getIconHeight() > 16)) {

			Image img = ((ImageIcon) icon).getImage();
			icon = new ImageIcon(img.getScaledInstance(16, 16,
					Image.SCALE_SMOOTH));
		}

		if (internalFrameTitle != null && isShowing()) {

			frameIcon = icon;
			internalFrameTitle.repaint();

		} else {

			frameIcon = icon;
		}
	}

	/**
	 * Returns the image displayed in the title bar of this internal frame
	 * (usually in the top-left corner).
	 * 
	 * @return the <code>Icon</code> displayed in the title bar
	 * @see #setFrameIcon
	 */
	public Icon getFrameIcon() {
		return (frameIcon);
	}

	/**
	 * Sets whether this <code>JInternalFrame</code> can be closed by some user
	 * action.
	 * 
	 * @param b
	 *            a boolean value, where <code>true</code> means this internal
	 *            frame can be closed
	 */
	public void setClosable(boolean b) {
		closable = b;
	}

	/**
	 * Returns whether this <code>JInternalFrame</code> can be closed by some
	 * user action.
	 * 
	 * @return <code>true</code> if this internal frame can be closed
	 */
	public boolean isClosable() {
		return (closable);
	}

	/**
	 * Sets whether this <code>JInternalFrame</code> can be shaded by some user
	 * action.
	 * 
	 * @param b
	 *            a boolean value, where <code>true</code> means this internal
	 *            frame can be shaded
	 */
	public void setShadable(boolean b) {
		shadable = b;
	}

	/**
	 * Returns whether this <code>JInternalFrame</code> can be shaded by some
	 * user action.
	 * 
	 * @return <code>true</code> if this internal frame can be shaded
	 */
	public boolean isShadable() {
		return (shadable);
	}

	/**
	 * Returns the content pane for this internal frame.
	 * 
	 * @return the content pane
	 */
	public Container getContentPane() {
		return (contentPane);
	}

	/**
	 * Sets this <code>JInternalFrame</code>'s <code>contentPane</code>
	 * property.
	 * 
	 * @param c
	 *            the content pane for this internal frame
	 * 
	 * @exception java.awt.IllegalComponentStateException
	 *                (a runtime exception) if the content pane parameter is
	 *                <code>null</code>
	 */
	public void setContentPane(Container c) {

		if (c == null) {
			throw new java.awt.IllegalComponentStateException(
					"contentPane cannot be null");
		}

		contentPane = c;
	}

	/**
	 * 
	 * @param comp
	 *            the <code>Component</code> to be added
	 * @param constraints
	 *            the object containing the constraints, if any
	 * @param index
	 *            the index
	 */
	protected void addImpl(Component comp, Object constraints, int index) {

		if (!contentPaneCheckingEnable) {
			super.addImpl(comp, constraints, index);
		} else {

			String type = getClass().getName();
			throw new Error("Do not use " + type + ".add() use " + type
					+ ".getContentPane().add() instead");
		}
	}

	/**
	 * Toggles the state of the frame shade.
	 */
	public void toggleShade() {

		if (!shadeIsUp) {
			shade();
		} else {
			unshade();
		}
	}

	/**
	 * Gets the state of the shade.
	 * 
	 * @return true if currently shaded
	 */
	public boolean isShadeUp() {
		return (shadeIsUp);
	}

	/**
	 * Sets the state of the shade.
	 * 
	 * @param shadeIsUp
	 *            the new value for the shade position
	 */
	public void setShadeIsUp(boolean shadeIsUp) {

		if (this.shadeIsUp == shadeIsUp) {
			return;
		}

		if (!this.shadeIsUp) {
			shade();
		} else {
			unshade();
		}
	}

	/**
	 * Shade this frame. When shaded only the title bar is visible.
	 */
	public void shade() {

		if (shadeIsUp) {
			return;
		}

		shadeIsUp = true;
		remove(contentPane);

		if (getParent() != null && getParent().getParent() != null) {
			getParent().getParent().validate();
		}

		internalFrameTitle.setShadeButtonIcon();
	}

	/**
	 * Unshade this frame. When shaded only the title bar is visible.
	 */
	public void unshade() {

		if (!shadeIsUp) {
			return;
		}

		shadeIsUp = false;
		contentPaneCheckingEnable = false;
		add(contentPane, BorderLayout.CENTER);
		contentPaneCheckingEnable = true;
		getParent().getParent().validate();

		internalFrameTitle.setShadeButtonIcon();
	}

	public void addMouseListener(MouseListener l) {

		super.addMouseListener(l);
		internalFrameTitle.addMouseListener(l);
	}

	public void removeMouseListener(MouseListener l) {

		super.removeMouseListener(l);
		internalFrameTitle.removeMouseListener(l);
	}
}
