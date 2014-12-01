package com.messners.ajf.ui;

import com.messners.ajf.util.StringUtils;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


/**
 * Document that accepts octal characters for use in 
 * <code>JTextComponent</code> instances.
 *
 * @see javax.swing.text.JTextComponent
 */
public class OctalDocument extends PlainDocument {

	private static final long serialVersionUID = 1L;
	
	// Maximum length of valid text.
    private int maxCharacters = -1;
    private boolean ignoreWhitespaces = false;

    /**
     * Default constructor.
     */
    public OctalDocument() {
        this (-1, false);
    }

    /**
     * Construct a document with specified attributes.
     * @param maxChars Maximum length (in characters) of valid text.
     * @param ignoreWs Ignore whitespaces (e.g. accept it as valid).
     */
    public OctalDocument(int maxChars, boolean ignoreWs) {
        maxCharacters = maxChars;
        ignoreWhitespaces = ignoreWs;
    }

    /**
     * Gets the maximum length of valid text.
     * @return Maximum length in characters.  Negative value if there's 
     *         no maximum.
     */
    public int getMaxCharacters() {
        return maxCharacters;
    }

    /**
     * Sets the maximum length of valid text.
     * @param maxChars Maximum length (in characters) of valid text.
     */
    public void setMaxCharacters(int maxChars) {
        maxCharacters = maxChars;
    }

    /**
     * Checks if document ignores whitespaces.
     * @return True if document ignores whitespaces.
     */
    public boolean getIgnoreWhitespaces() {
        return ignoreWhitespaces;
    }

    /**
     * Set document to ignore whitespaces.
     * @param ignoreWs If true, document ignores whitespaces.
     */
    public void setIgnoreWhitespaces(boolean ignoreWs) {
        ignoreWhitespaces = ignoreWs;
    }

    //***********************************************************************
    // Overidden Methods (More documentation in the parent class).
    //***********************************************************************

    public void insertString(int offs, String str, AttributeSet a) 
                    throws BadLocationException {
        boolean strValid = true;
        if (maxCharacters > 0) {
            strValid = (getLength() + str.length()) <= maxCharacters;
        }
        if (strValid && StringUtils.isNumeric(str, 8, ignoreWhitespaces)) {
            super.insertString(offs, str, a);
        } else {
            // Ignore the special case where the text field is being cleared
            // (e.g. setText(""))
            if ((offs == 0) && (str.length() == 0)) {
                super.insertString(offs, str, a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    //***********************************************************************
    // Internal Classes & Methods.
    //***********************************************************************

    // None.
}
