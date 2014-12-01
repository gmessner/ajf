package com.messners.ajf.ui;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;


/**
 * This component provides a JTextField with a "Browse" button to the right.
 * It is for entering a filename, the browse button displays a JFileChooser
 * for selecting a file.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class FilenameTextField extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField urlText;
	private JFileChooser fileChooser = null;
	private String dialogTitle;
	private boolean mustExist = true;
	private FileFilter fileFilter;
	private File currentDirectory;


	/**
	 * Creates a FilenameTextField component with the resources from
	 * the specified ResourceLoader and name.
	 *
	 * @param  loader  the ResourceLoader for the resources
	 * @param  name    the name of the resource
	 */
	public FilenameTextField (ResourceLoader loader, String name) {

		this();
		setName(name);
		setFileChooserTitle(loader.getResource(name + ".title"));

		String s = loader.getResource(name + ".file-must-exist");
		if ("false".equals(s)) {
			setFileMustExist(false);
		}

		String extensions[] = loader.splitResource(name + ".filter.extensions");
		if (extensions != null && extensions.length > 0) {

			String filterDescription = loader.getResource(
				name + ".filter.description");
			setFileFilter(new FilenameFilter(filterDescription, extensions));
		}

		s = loader.getResource(name + ".width");
		if (s != null) {

			try {
				urlText.setColumns(Integer.parseInt(s));
			} catch (NumberFormatException nfe) {
			}
		}
	}


	/**
	 * Creates a FilenameTextField instance.
	 */
	public FilenameTextField () {
		
		setLayout(new BorderLayout(2, 0));

		JButton btn = new JButton("Browse...");
		btn.setMargin(new Insets(0, 4, 0, 4));
		btn.addActionListener(new BrowseAction());
		urlText = new JTextField(20);
	
		add(urlText, BorderLayout.CENTER);
		add(btn, BorderLayout.EAST);
	}


	/**
	 * Gets the current directory from the JFileChooser
	 *
	 * @return  the current directory from the JFileChooser
	 */
	public File getCurrentDirectory () {

		if (fileChooser != null) {
			return (fileChooser.getCurrentDirectory());
		}

		return (currentDirectory);
	}


	/**
	 * Sets the current directory for the JFileChooser.
	 *
	 * @param  dir  the directory
	 */
	public void setCurrentDirectory (File dir) {

		currentDirectory = dir;
		if (fileChooser != null) {
			fileChooser.setCurrentDirectory(dir);
		}
	}


	/**
	 * Sets the title for the JFileChooser.
	 *
	 * @param title  the title for the JFileChooser
	 */
	public void setFileChooserTitle (String title) {

		dialogTitle = title;
		if (fileChooser != null) {
			fileChooser.setDialogTitle(dialogTitle);
		}
	}


	/**
	 * Sets the file must exist flag.  This effects how the JFileChooser
	 * allows the user to select files.
	 *
	 * @param  flag  if true the JFileChooser will only select files that exist
	 */
	public void setFileMustExist (boolean flag) {
		mustExist = flag;
	}


	/**
	 * Gets the entered filename.
	 *
	 * @return  the entered filename
	 */
	public String getFilename () {
		return (urlText.getText());
	}


	/**
	 * Gets a File instance based on the entered filename.
	 */
	public File getFile () {
		return (new File(urlText.getText()));
	}


	/**
	 * Sets the text of the filename entry field.
	 *
	 * @param  filename  the text for the entry field
	 */
	public void setFilename (String filename) {
		urlText.setText(filename);
	}


	/**
	 * Sets the text (File.getAbsolutePath()) of the filename entry field.
	 *
	 * @param  file  the text for the entry field
	 */
	public void setFile (File file) {

		if (file != null) {
			urlText.setText(file.getAbsolutePath());
		} else {
			urlText.setText(null);
		}
	}


	/**
	 * Sets the FileFilter for the JFileChooser.
	 *
	 * @param fileFilter  the FileFilter instance for the JFileChooser
	 */
	public void setFileFilter (FileFilter fileFilter) {

		this.fileFilter = fileFilter;
		if (fileChooser != null) {
			fileChooser.setFileFilter(fileFilter);
		}
	}


	/**
	 * Gets the JTextField component.
	 */
	public JTextField getTextField () {
		return (urlText);
	}


	/**
	 * Gets the JFileChooser component.
	 *
	 * @return  the owned JFileChooser
	 */
	public JFileChooser getFileChooser () {

		if (fileChooser == null) {

			fileChooser = new JFileChooser();
			if (fileFilter != null) {
				fileChooser.setFileFilter(fileFilter);
			}

			if (dialogTitle != null) {
				fileChooser.setDialogTitle(dialogTitle);
			}

			if (currentDirectory != null) {
				fileChooser.setCurrentDirectory(currentDirectory);
			}
		}

		return (fileChooser);
	}


	/**
	 * This class is the ActionListener for the "Browse" button.  It simply
	 * configures and pops up the JFileChooser.
	 */
	private final class BrowseAction implements ActionListener {

		public void actionPerformed (ActionEvent evt) {

			if (fileChooser == null) {
				getFileChooser();
			} else {
				fileChooser.rescanCurrentDirectory();
			}

			int rc;
			if (mustExist) {
				rc = fileChooser.showOpenDialog(FilenameTextField.this);
			} else {
				rc = fileChooser.showSaveDialog(FilenameTextField.this);
			}

			if (rc == JFileChooser.APPROVE_OPTION) {
				File f = fileChooser.getSelectedFile();
				urlText.setText(f.getAbsolutePath());
			}
		}
	}
}
