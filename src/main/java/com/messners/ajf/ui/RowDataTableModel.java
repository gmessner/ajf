package com.messners.ajf.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.event.TableModelListener;

/**
 * This interface defines a mechanism for maintaining row based data to
 * be used with TableModels.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class RowDataTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * ArrayList to hold the row data.
	 */
	protected ArrayList<Object> rowData;


	/**
	 * Default no args constructor.
	 */
	public RowDataTableModel () {
		rowData = new ArrayList<Object>();
	}


	/**
	 * Creates a RowDataTableModel with the initial capicity of row as
	 * specified.
	 *
	 * @param initialCapacity  the initial capacity of rows
	 */
	public RowDataTableModel (int initialCapacity) {
		rowData = new ArrayList<Object>(initialCapacity);
	}


	public List<Object> getRowData () {
		return (rowData);
	}


	/**
	 * Gets the count of rows in this RowDataProvider instance.
	 *
	 * @return  the count of rows in this instance
	 */
	public int getRowCount () {
		return (rowData.size());
	}


	/**
	 * Clears the data from all the rows.
	 */
	public void clearAllRows () {

		synchronized (rowData) {
			rowData.clear();
		}

		fireTableDataChanged();
	}


	/**
	 * Adds a row of data to this instance.
	 *
	 * @param  row the data row to add
	 * @return the new number of rows
	 */
	public int addRow (Object row) {

		int numRows;

		synchronized (rowData) {

			numRows = rowData.size();
			rowData.add(row);
		}

		fireTableRowsInserted(numRows, numRows);
		return (numRows);
	}



	/**
	 * Gets the row of data specified by rowData.
	 *
	 * @param  obj the row object to get the row for
	 * @return the index of the row
	 */
	public int getRow (Object obj) {

		synchronized (rowData) {
			return (rowData.indexOf(obj));
		}
	}


	/**
	 * Removes the row of data specified by rowData.
	 *
	 * @param  row the row object to remove
	 * @return the index of the row removed
	 */
	public int removeRow (Object row) {

		int rowIndex;
		synchronized (rowData) {

			rowIndex = rowData.indexOf(row);
			if (rowIndex != -1) {
				rowData.remove(rowIndex);
			}
		}

		if (rowIndex != -1) {
			fireTableRowsDeleted(rowIndex, rowIndex);
		}

		return (rowIndex);
	}


	/**
	 * Replaces the row of data.
	 *
	 * @param  row  the old row data object 
	 * @param  newRow  the new row data object 
	 * @return the index of the row rerowData the row object to removlaced
	 */
	public int replaceRow (Object row, Object newRow) {

		int rowIndex;
		synchronized (rowData) {

			rowIndex = rowData.indexOf(row);
			if (rowIndex != -1) {
				rowData.set(rowIndex, newRow);
			}
		}

		if (rowIndex != -1) {
			fireTableRowsUpdated(rowIndex, rowIndex);
		}

		return (rowIndex);
	}

	/**
	 * Replaces the row of data at the specifies index.
	 *
	 * @param  index  the index of the row to replace
	 * @param  row    the replacement Object data
	 * @return the data that previously occupide the row
	 */
	public Object replaceRowAt (int index, Object row) {

		Object oldRow;
		synchronized (rowData) {
			oldRow = rowData.set(index, row);
		}

		fireTableRowsUpdated(index, index);
		return (oldRow);
	}


	/**
	 * Removes the row of data at the specifies index.
	 *
	 * @param  index  the index of the row to remove
	 * @return  the Object instance at the removed row
	 */
	public Object removeRowAt (int index) {

		Object row;
		synchronized (rowData) {
			row = rowData.remove(index);
		}

		fireTableRowsDeleted(index, index);
		return (row);
	}


	/**
	 * Gets the row of data at the specified index.
	 *
	 * @param  index  the index of the row to get the data for
	 * @return  the data at the specified index
	 */
	public Object getRowAt (int index) {

		synchronized (rowData) {
			return (rowData.get(index));
		}
	}


	/**
	 * Gets the value at the specified row andf column. This method is
	 * implemented to simply throw an exception so that this class can
	 * be used outside of the Swing JTable architecture.  To use as a
	 * real TableModel  you must override this method.
	 */
	public Object getValueAt (int row, int column) {
		throw (new RuntimeException("Not implemented."));
	}


	/**
	 * Gets the count of columns.  This method is implemented to simply
	 * throw an exception so that this class can be used outside of the
	 * Swing JTable architecture.  To use as a real TableModel  you must
	 * override this method.
	 */
	public int getColumnCount () {
		throw (new RuntimeException("Not implemented."));
	}


	/**
	 * Sorts the current row data using the specified Comparator.
	 *
	 * @param comparator  the Comparator to do the sort with
	 */
	public void sort (Comparator<Object> comparator) {

		synchronized (rowData) {
			Collections.sort(rowData, comparator);
		}

		fireTableDataChanged();
	}


	/**
	 * Removes all the current TableModelListeners.
	 */
	public void removeAllTableModelListeners () {

		TableModelListener listeners[] = getTableModelListeners();
		if (listeners == null) {
			return;
		}

		int numListeners = listeners.length;
		for (int i = 0; i < numListeners; i++) {
			removeTableModelListener(listeners[i]);
		}
	}
}
