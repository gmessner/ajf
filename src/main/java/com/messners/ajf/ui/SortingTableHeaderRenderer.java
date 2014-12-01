package com.messners.ajf.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * Table column header renderer.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public abstract class SortingTableHeaderRenderer 
			extends DefaultTableCellRenderer implements MouseListener {

	private static final long serialVersionUID = 1L;
	protected static Icon UP_ICON = new UpIcon();
	protected static Icon DOWN_ICON = new DownIcon();;

	protected Icon upIcon = UP_ICON;
	protected Icon downIcon = DOWN_ICON;

	protected JTable table;
	protected TableColumnModel columnModel;
	protected boolean ascending;
	protected int sortColumn;

	protected boolean showArrow = false;

	public SortingTableHeaderRenderer (JTable table) {

		super();

		this.table = table;
		columnModel = table.getColumnModel();
		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.addMouseListener(this);
		sortColumn = -1;
		ascending = true;

		setFont(UIManager.getFont("TableHeader.font"));
		setBackground(UIManager.getColor("TableHeader.background"));
		setForeground(UIManager.getColor("TableHeader.foreground"));
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		setHorizontalAlignment(SwingConstants.CENTER);
		setHorizontalTextPosition(SwingConstants.LEADING);
		setVerticalTextPosition(SwingConstants.CENTER);
		setIconTextGap(3);
	}


	/**
	 * This method interface intereted by TableCellRenderer.
	 */
   	public Component getTableCellRendererComponent (JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column ) {
			
		int modelColumn = table.convertColumnIndexToModel(column);
		setText(table.getModel().getColumnName(modelColumn));
		if (showArrow && modelColumn == getSortColumn()) {

			if (isAscending()) {
				setIcon(upIcon);
			} else {
				setIcon(downIcon);
			} 

		} else {
			setIcon(null); 
		}

   		return (this);
	}


	/**
	 * Sets whether or not to display the sort direction arrow.
	 */
	public void setArrowVisible (boolean flag) {
		showArrow = flag;
	}


	/**
	 * Gets the is ascending sort flag. 
	 *
	 * @return  the is ascending sort flag
	 */
	public boolean isAscending () {
		return (ascending);
	}


	/**
	 * Sets the is ascending sort flag. 
	 *
	 * @param  flag  the new is ascending sort flag
	 */
	public void setAscending (boolean flag) {
		ascending = flag;
	}

	
	/**
	 * Gets the column index that is the sorted column.
	 *
	 * @return  the index of the sorted column, or -1 if none are sorted
	 */
	public int getSortColumn () {
		return (sortColumn);
	}


	/**
	 * Sets the column index that is the sorted column.
	 *
	 * @param column set the index of the sorted column, set to -1 for none are sorted
	 */
	public void setSortColumn (int column) {
		sortColumn = column;
	}


	/**
	 * Sort by the specified column index.
	 *
	 * @param  column  the column index to sort on
	 */
	public abstract void sort (JTable table, int column, boolean ascending);


	/**
	 * Checks a mouse click in the table header for a sort command.
	 *
     * @param  e  the MouseEvent to check
     */
	public void mouseClicked (MouseEvent e) {

		int viewColumn = columnModel.getColumnIndexAtX(e.getX());
		int column = table.convertColumnIndexToModel(viewColumn);
       	if ((e.getClickCount() == 1) && (column != -1)) {

            if (column == getSortColumn()) {
                setAscending(!isAscending());
            } else {
				setSortColumn(column);
                setAscending(true);
            }

            sort(table, column, ascending);
        }
	}


	/**
	 * Invoked when the mouse enters a component. 
	 */
	public void mouseEntered (MouseEvent e)  {
	}


	/**
	 * Invoked when the mouse exits a component. 
	 */
	public void mouseExited (MouseEvent e) {
	}


	/**
	 * Invoked when a mouse button has been pressed on a component. 
	 */
	public void mousePressed (MouseEvent e) {
	}


	/**
	 * Invoked when a mouse button has been released on a component. 
	 */
	public void mouseReleased (MouseEvent e) {
	}


	static private class UpIcon implements Icon {
			
		public void paintIcon (Component c, Graphics g, int x, int y) {
       		int dy = y + 2;
            g.translate(x, dy);
            g.setColor(Color.BLACK);
            g.drawLine(4, 0, 4, 0);
            g.drawLine(3, 1, 5, 1);
            g.drawLine(2, 2, 6, 2);
            g.drawLine(1, 3, 7, 3);
            g.drawLine(0, 4, 8, 4);
            g.translate(-x, -dy);
        }

        public int getIconWidth () {
            return (5);
        }

        public int getIconHeight () {
            return (9);
        }
    }


    // Down arrow icon that indicates descending sort.
    static private class DownIcon implements Icon {

        public void paintIcon (Component c, Graphics g, int x, int y) {

            int dy = y + 2;
            g.translate(x, dy);
            g.setColor(Color.BLACK);
            g.drawLine(0, 0, 8, 0);
            g.drawLine(1, 1, 7, 1);
            g.drawLine(2, 2, 6, 2);
            g.drawLine(3, 3, 5, 3);
            g.drawLine(4, 4, 4, 4);
            g.translate(-x, -dy);
        }

        public int getIconWidth () {
            return (5);
        }

        public int getIconHeight () {
            return (9);
        }
    }
}

