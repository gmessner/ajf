package com.messners.ajf.ui;

import java.awt.Component;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;


/**
 * This class implements a component that can be used as the row header
 * for a JTable in a JScrollPane.  It adapts itself to the TableModel
 * of the JTable to get the text for each row by calling 
 * TableModel.getValueAt(row, -1), notice that the column is set to
 * -1 this indicates to the TableModel that it should return the text
 * for the row header.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public class RowHeader extends JList<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Border globalBorder = BorderFactory.createCompoundBorder(
		UIManager.getBorder("TableHeader.cellBorder"),
		BorderFactory.createEmptyBorder(0, 2, 0, 4));

	private TableModel tableModel;

	public RowHeader (JTable table) {

		super();
		setBackground(UIManager.getColor("TableHeader.cellBackground"));
		setFixedCellHeight(table.getRowHeight());
    	setCellRenderer(new RowHeaderRenderer(table));
		setSelectionModel(table.getSelectionModel());

		tableModel = table.getModel();
		TableModelAdapter modelAdapter = new TableModelAdapter();
		tableModel.addTableModelListener(modelAdapter);
		setModel(modelAdapter);
	}


	public RowHeader (JTable table, Object listData[]) {

		super(listData);
		setBackground(UIManager.getColor("TableHeader.cellBackground"));
		setFixedCellHeight(table.getRowHeight());
    	setCellRenderer(new RowHeaderRenderer(table));
		setSelectionModel(table.getSelectionModel());
	}



	private class RowHeaderRenderer extends JLabel implements ListCellRenderer<Object> {
  
  		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;


		public RowHeaderRenderer (JTable table) {

			JTableHeader header = table.getTableHeader();
			setOpaque(true);
			setBorder(globalBorder);
			setHorizontalAlignment(LEFT);
			setForeground(header.getForeground());
			setBackground(header.getBackground());
			setFont(header.getFont());
		}

  
		public Component getListCellRendererComponent (
			JList<?> list, Object value, int index, 
			boolean isSelected, boolean cellHasFocus) {

			if (tableModel instanceof ToolTipTableModel) {
				ToolTipTableModel tttm = (ToolTipTableModel)tableModel;
				setToolTipText(tttm.getToolTipAt(index, -1));
			}

			setText((value == null) ? "" : value.toString());
			return (this);
		}
	}


	private class TableModelAdapter extends AbstractListModel<Object> 
			implements TableModelListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;


		public int getSize () {
			return (tableModel.getRowCount());
		}


		public Object getElementAt (int index) {
			return (tableModel.getValueAt(index, -1));
		}


		public void tableChanged (TableModelEvent e) {

			int firstRow = e.getFirstRow();
			int lastRow = e.getLastRow();

			switch (e.getType()) {

				case TableModelEvent.INSERT:
					fireIntervalAdded(this, firstRow, lastRow);
					break;

				case TableModelEvent.DELETE:
					fireIntervalRemoved(this, firstRow, lastRow);
					break;

				case TableModelEvent.UPDATE:
					fireContentsChanged(this, firstRow, lastRow);
					break;
			}
		}
	}
}

