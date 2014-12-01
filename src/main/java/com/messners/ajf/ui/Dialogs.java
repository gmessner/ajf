package com.messners.ajf.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.Document;

import com.messners.ajf.app.Resources;


/**
 * This class contains methods (static) to display various dialog boxes that
 * are basically wrappers around the JOptionPane dialogs.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class Dialogs {


	public static final int INFORMATION_MESSAGE =
		JOptionPane.INFORMATION_MESSAGE;
	public static final int WARNING_MESSAGE =
		JOptionPane.WARNING_MESSAGE;
	public static final int ERROR_MESSAGE =
		JOptionPane.ERROR_MESSAGE;

	public static final int YES_OPTION    = JOptionPane.YES_OPTION;
	public static final int NO_OPTION     = JOptionPane.NO_OPTION;
	public static final int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;
	public static final int OK_OPTION     = JOptionPane.OK_OPTION;
	public static final int CLOSED_OPTION = JOptionPane.CLOSED_OPTION;


	/**
	 * Displays a dialog box. The title of the dialog is fetched from
	 * the <code><i>name</i>.title</code> property. The message is fetched
	 * from the <code><i>name</i>.message</code> property. The message
	 * is formatted by the property manager with <code>args</code> as
	 * positional parameters.
	 *
	 * @param frame The frame to display the dialog for
	 * @param name The name of the dialog
	 * @param args Positional parameters to be substituted into the
	 * message text
	 */
	public static void about (ResourceLoader loader,
			Component frame, String name, Object[] args) {

		Icon icon = loader.getIconResource(name + ".icon");

		JPanel contents = new JPanel(new BorderLayout(0, 8));
		contents.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

		String message = loader.getResourceMessage(name + ".message", args);
		String msg_type = loader.getResource(name + ".message-type");
		if ("html".equals(msg_type)) {
		   contents.add(new JLabel(message), BorderLayout.CENTER);
		} else {
		   contents.add(new TextLabel(message), BorderLayout.CENTER);
		}

		int message_type;

		if (icon == null) {
			message_type = JOptionPane.INFORMATION_MESSAGE;			
		} else {
    		contents.add(new JLabel(icon), BorderLayout.NORTH);
			message_type = JOptionPane.PLAIN_MESSAGE;
		}

		Component addComponent = null;
		String componentName = loader.getResource(name + ".add-component");
		if (componentName != null) {

			try {

				ClassLoader classLoader =
					Thread.currentThread().getContextClassLoader();
				if (classLoader == null) {
					classLoader = Dialogs.class.getClassLoader();
				}
	
				Class<?> c = classLoader.loadClass(componentName);
				Constructor<?> constructor = 
						c.getConstructor(new Class[]{GuiApplication.class});
				addComponent = (Component)constructor.newInstance(
							new Object[]{ loader.getApplication() });

			} catch (Exception e) {
				System.err.println(
					"error loading " + componentName + "[" + 
					e.getMessage() + "]");
			}
		}

		JButton moreButton = null;
		String actionName = loader.getResource(name + ".more.action");
		ActionManager actionMgr = loader.getActionManager();
		if (actionName != null && actionMgr != null) {

			AppAction action = actionMgr.getAction(actionName);
			if (action != null) {

				String more = loader.getResource(name + ".more.label");
				if (more == null) {

					if (action instanceof Action) {
						moreButton = new JButton((Action)action);
					} else {
						moreButton = new JButton("More info...");
					}

				} else {
					moreButton = new JButton(more);
				}

				moreButton.addActionListener(action);

			} else {
				System.err.println("couldn't find " + actionName + " action");
			}
		}


		/*
		 * Now add the extra components
		 */
		if (addComponent != null && moreButton != null) {

			JPanel p = new JPanel(new BorderLayout());
			p.add(addComponent, BorderLayout.CENTER);
			p.add(moreButton, BorderLayout.SOUTH);
			contents.add(p, BorderLayout.SOUTH);

		} else if (addComponent != null) {
			contents.add(addComponent, BorderLayout.SOUTH);
		} else if (moreButton != null) {
			contents.add(moreButton, BorderLayout.SOUTH);
		}

		String title = loader.getResourceMessage(name + ".title", args);
		JOptionPane.showMessageDialog(frame, contents, title, message_type);
	}


	/**
	 * Displays a message dialog box. The title of the dialog is fetched from
	 * the <code><i>name</i>.title</code> property. The message is fetched
	 * from the <code><i>name</i>.message</code> property. The message
	 * is formatted by the property manager with <code>args</code> as
	 * positional parameters.
	 *
	 * @param frame The frame to display the dialog for
	 * @param name The name of the dialog
	 * @param args Positional parameters to be substituted into the
	 * message text
	 */
	public static void message (Resources loader,
			Component frame, String name, Object[] args) {

		JOptionPane.showMessageDialog(frame,
			loader.getResourceMessage(name + ".message", args),
			loader.getResourceMessage(name + ".title",   args),
			JOptionPane.INFORMATION_MESSAGE);
	}


	/**
	 * Displays an error dialog box. The title of the dialog is fetched from
	 * the <code><i>name</i>.title</code> property. The message is fetched
	 * from the <code><i>name</i>.message</code> property. The message
	 * is formatted by the property manager with <code>args</code> as
	 * positional parameters.
	 *
	 * @param frame the frame to display the dialog for
	 * @param name  the name of the dialog
	 * @param args  positional parameters to be substituted into the
	 *                message text
	 */
	public static void error (Resources loader,
		Component frame, String name, Object[] args) {

		JOptionPane.showMessageDialog(frame,
			loader.getResourceMessage(name.concat(".message"), args),
			loader.getResourceMessage(name.concat(".title"),   args),
			JOptionPane.ERROR_MESSAGE);
	}


	/**
	 * Displays an error dialog box. The title of the dialog is fetched from
	 * the <code><i>name</i>.title</code> property. The message is fetched
	 * from the <code><i>name</i>.message</code> property. The message
	 * is formatted by the property manager with <code>args</code> as
	 * positional parameters.
	 *
	 * @param frame the frame to display the dialog for
	 * @param name  the name of the dialog
	 * @param args  positional parameters to be substituted into the
	 *                message text
	 */
	public static void error (Resources loader,
		Component frame, String name, Object[] args, Throwable error) {


		String msg = loader.getResourceMessage(name.concat(".message"), args);
		String title = loader.getResourceMessage(name.concat(".title"),   args);

		ByteArrayOutputStream stack = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(stack);
		error.printStackTrace(writer);
		writer.close();

		String stack_trace;
		try {
			stack_trace = stack.toString();
		} catch (Exception ae) {
			stack_trace = "";
		}

		extendedMessage(frame, title,
			JOptionPane.ERROR_MESSAGE,
			msg, stack_trace);
	}


	/**
	 * Displays an error dialog box. The title of the dialog is fetched from
	 * the <code><i>name</i>.title</code> property. The message is fetched
	 * from the <code><i>name</i>.message</code> property. The message
	 * is formatted by the property manager with <code>args</code> as
	 * positional parameters.
	 *
	 * @param frame the frame to display the dialog for
	 * @param name  the name of the dialog
	 * @param args  positional parameters to be substituted into the
	 *                message text
	 */
	public static void error (Resources loader, Component frame,
		String name, Object[] args, String error_output, Throwable error) {


		String msg = loader.getResourceMessage(name.concat(".message"), args);
		String title = loader.getResourceMessage(name.concat(".title"),   args);

		ByteArrayOutputStream stack = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(stack);
		error.printStackTrace(writer);
		writer.close();

		String stack_trace;
		try {
			stack_trace = stack.toString();
		} catch (Exception ae) {
			stack_trace = "";
		}

		if (error_output != null) {
			stack_trace = error_output + "\n\n" + stack_trace;
		}

		extendedMessage(frame, title,
			JOptionPane.ERROR_MESSAGE,
			msg, stack_trace);
	}


	/**
	 * Displays a warning dialog box. The title of the dialog is fetched
	 * from the <code><i>name</i>.title</code> property. The message is
	 * fetched from the <code><i>name</i>.message</code> property. The
	 * message is formatted by the property manager with <code>args</code>
	 * as positional parameters.
	 *
	 * @param frame the frame to display the dialog for
	 * @param name  the name of the dialog
	 * @param args  positional parameters to be substituted into the
	 *                message text
	 */
	public static void warning (Resources loader,
		Component frame, String name, Object[] args) {

		JOptionPane.showMessageDialog(frame,
			loader.getResourceMessage(name.concat(".message"), args),
			loader.getResourceMessage(name.concat(".title"),   args),
			JOptionPane.WARNING_MESSAGE);
	}


	/**
	 * Displays an input dialog box and returns any text the user entered.
	 * The title of the dialog is fetched from
	 * the <code><i>name</i>.title</code> property. The message is fetched
	 * from the <code><i>name</i>.message</code> property.
	 *
	 * @param frame      the frame to display the dialog for
	 * @param name       the name of the dialog
	 * @param defaultVal the text to display by default in the input field
	 */
	public static String input (Resources loader,
		Component frame, String name, Object args[], Object defaultVal) {

		String retVal = (String)JOptionPane.showInputDialog(frame,
			loader.getResourceMessage(name.concat(".message"), args),
			loader.getResourceMessage(name.concat(".title"), args),
			JOptionPane.QUESTION_MESSAGE, null, null, defaultVal);

		return (retVal);
	}


	/**
	 * Displays an input dialog box and returns any text the user entered.
	 * The title of the dialog is fetched from
	 * the <code><i>name</i>.title</code> property. The message is fetched
	 * from the <code><i>name</i>.message</code> property.
	 *
	 * @param frame      the frame to display the dialog for
	 * @param name       the name of the dialog
	 * @param defaultVal the text to display by default in the input field
	 */
	public static String input (Resources loader,
		Component frame, String name, Object args[], 
		Document doc, Object defaultVal) {

		if (doc == null) {
			return (input(loader, frame, name, args, defaultVal));
		}

		ColumnLayout cl = new ColumnLayout(16, 0);
		cl.setStretchWidth(true);
		JPanel panel = new JPanel(cl);

		String msg = loader.getResourceMessage(
					name.concat(".message"), args);
		if (msg != null) {
			JLabel hdr = new JLabel(msg);
			panel.add(hdr);
		}

		JTextField field = new JTextField(20);
		field.setDocument(doc);
		if (defaultVal != null) {
			field.setText(defaultVal.toString());
		}

		panel.add(field);
		
		int retVal = JOptionPane.showConfirmDialog(frame, panel,
			loader.getResourceMessage(name.concat(".title"),   args),
			JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE);

		if (retVal == JOptionPane.OK_OPTION) {
			return (field.getText());
		} 

		return (null);
	}


	/**
	 * Displays a warning dialog box and returns true if OK was selected.
	 * The title of the dialog is fetched from
	 * the <code><i>name</i>.title</code> property. The message is fetched
	 * from the <code><i>name</i>.message</code> property.
	 *
	 * @param frame      the frame to display the dialog for
	 * @param name       the name of the dialog
	 * @param args       positional parameters to be substituted into the
	 *                     message text
	 */
	public static boolean confirmWithWarning (
		Resources loader, Component frame, String name, Object args[]) {

		int retVal = JOptionPane.showConfirmDialog(frame,
			loader.getResourceMessage(name.concat(".message"), args),
			loader.getResourceMessage(name.concat(".title"),   args),
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE);

		if (retVal == JOptionPane.OK_OPTION) {
			return (true);
		} 

		return (false);
	}


	/**
	 * Displays a warning dialog box with a "Cancel" button added  and
	 * returns the return code from showConfirmDialog.  The title of the
	 * dialog is fetched from the <code><i>name</i>.title</code> property.
	 * The message is fetched from the <code><i>name</i>.message</code>
	 * property.
	 *
	 * @param frame      the frame to display the dialog for
	 * @param name       the name of the dialog
	 * @param args       positional parameters to be substituted into the
	 *                     message text
	 */
	public static int confirmWithCancel (
		Resources loader, Component frame, String name, Object args[]) {

		int retVal = JOptionPane.showConfirmDialog(frame,
			loader.getResourceMessage(name.concat(".message"), args),
			loader.getResourceMessage(name.concat(".title"),   args),
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.WARNING_MESSAGE);

		return (retVal);
	}


	/**
	 * Displays a message dialog  with a possibly multiline label at the top
	 * and a non-editable text area immediately below it.
	 *
	 * @param  parent  the parent component
	 * @param  title   the title of the dialog
	 * @param  message_type  the type of message (ERROR_MESSAGE,
	 * WARNING_MESSAGE, or INFORMATION_MESSAGE).
	 * @param  heading  the text for the label
	 * @param  message  the text for the text area
	 *
	 */
	public static void extendedMessage (Component parent,
			String title, int message_type, 
			String heading, String message) {

		ColumnLayout cl = new ColumnLayout(16, 0);
		cl.setStretchWidth(true);
		JPanel pane = new JPanel(cl);

		JLabel hdr = new JLabel(heading);
		pane.add(hdr);
		
		String lines[] = com.messners.ajf.util.StringUtils.splitDelimitedString(
						message, "\n");
		int num_lines = (lines != null ? lines.length : 0);
		JTextArea text = new JTextArea(message, 
			(num_lines > 20 ? 20 : (num_lines < 6 ? 6 : num_lines)), 80);
		text.setEditable(false);
		JScrollPane scroller = new JScrollPane();
		scroller.getViewport().add(text);
		pane.add(scroller);
				
		JOptionPane.showMessageDialog(
				parent, pane, title, message_type);
	}


	/**
	 * This class is not meant to be instantiated, hide it.
	 */
	private Dialogs () {
	}
}
