package com.messners.ajf.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;


/**
 * This TableCellRenderer implementation provides a look that is good
 * for JTables that are used to display name/value pairs.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class NameValueTableCellRenderer extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 1L;

	private Border border;
	private Font nameFont;
	private Font valueFont;
	private boolean isTableLook;

	private static Color tableBg = new JTable().getBackground();
	private static Color labelBg = new JLabel().getBackground();

	public NameValueTableCellRenderer () {

		super();
		isTableLook = false;
		border = BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(0, 2, 0, 2));

		Font font = getFont();
		font = new Font(font.getFontName(), Font.BOLD, font.getSize());
		valueFont = font;
		nameFont  = font;
	}


	public boolean isTableLook () {
		return (isTableLook);
	}


	public void setIsTableLook (boolean flag) {
		isTableLook = flag;
	}


	public void setValueBorder (Border border) {
		this.border = border;
	}


	public void setNameFont (Font font) {
		nameFont = font;
	}


	public void setValueFont (Font font) {
		valueFont = font;
	}


	public void configureTable (JTable table) {

		if (isTableLook) {

			table.setShowGrid(true);
			table.setBackground(tableBg);

		} else {

			table.setShowGrid(false);
			table.setBackground(labelBg);
		}

		table.setDefaultRenderer(String.class, this);
		table.setDefaultRenderer(Object.class, this);

		Component c = getTableCellRendererComponent(
					table, "ABC012", false, false, 0, 1);
		Dimension d = c.getPreferredSize();
		table.setRowHeight(d.height);

		int maxWidth = 0;
		int numRows = table.getRowCount();
		for (int i = 0; i < numRows; i++) {

			c = getTableCellRendererComponent(
					table, table.getValueAt(i, 0), false, false, i, 0);
			d = c.getPreferredSize();
			if (d.width > maxWidth) {
				maxWidth = d.width;
			}
		}

		Dimension spacing = table.getIntercellSpacing();
		maxWidth += spacing.width + 2;
		
		TableColumn tc = table.getColumnModel().getColumn(0);
		tc.setPreferredWidth(maxWidth);
		tc.setWidth(maxWidth);
		tc.setMinWidth(maxWidth);
		tc.setMaxWidth(maxWidth);
	}
	

	public Component getTableCellRendererComponent (
			JTable table, Object value, boolean isSelected, 
			boolean hasFocus, int row, int column) {

		JLabel l = (JLabel)super.getTableCellRendererComponent(
			table, value, isSelected, 
			hasFocus, row, column);

		if (isTableLook) {
			return (l);
		}

		if (column == 0) {
			l.setBorder(null);
			setFont(nameFont);
		} else {
			l.setBorder(border);
			setFont(valueFont);
		}

		return (l);
	}
}
