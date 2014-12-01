package com.messners.ajf.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;


/**
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class OrientableTableHeaderCellRenderer
		extends OrientableLabel implements TableCellRenderer {
	
	private static final long serialVersionUID = 1L;

	public OrientableTableHeaderCellRenderer () {

		super();
		setOrientation(HORIZONTAL);
		
		setFont(UIManager.getFont("TableHeader.font"));
		setBackground(UIManager.getColor("TableHeader.background"));
		setForeground(UIManager.getColor("TableHeader.foreground"));
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
	}


	/**
	 * This method interface intereted by TableCellRenderer.
	 */
   	public Component getTableCellRendererComponent (JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column ) {
			
		if (value instanceof String) {
			setText((String)value);
		} else if (value != null) {
			setText(value.toString());
		} else {
			setText(null);
		}

   		return (this);
	}
}

