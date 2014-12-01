package com.messners.ajf.ui;

import java.awt.*;

import java.util.*;


/**
 * TableLayout is a layout manager that arranges components in rows and columns
 * like a spreadsheet.  TableLayout allows each row or column to be a different
 * size.  A row or column can be given an absolute size in pixels, a percentage
 * of the available space, or it can grow and shrink to fill the remaining space
 * after other rows and columns have been resized.
 *
 * <p>Using spreadsheet terminology, a cell is the intersection of a row and
 * column.  Cells have finite, non-negative sizes measured in pixels.  The
 * dimensions of a cell depend solely upon the dimensions of its row and column.
 * </p>
 *
 * <p>A component occupies a rectangular group of one or more cells.  If the
 * component occupies more than one cell, the component is resized to fit
 * perfectly in the rectangular region of cells.  If the component occupies a
 * single cell, it can be aligned in four ways within that cell.</p>
 *
 * <p>A single cell component can be stretched horizontally to fit the cell
 * (full justification), or it can be placed in the center of the cell.  The
 * component could also be left justified or right justified.  Similarly, the
 * component can be full, center, top, or bottom justified in the vertical.</p>
 *
 * <pre>
 * public static void main (String args[]) {
 * 
 *     // Create a frame
 *     Frame frame = new Frame("Example of TableLayout");
 *     frame.setBounds (100, 100, 300, 300);
 *
 *     // Create a TableLayout for the frame
 *     double border = 10;
 *     double size[][] =
 *         {{border, 0.10, 20, TableLayout.FILL, 20, 0.20, border},  // Columns
 *          {border, 0.20, 20, TableLayout.FILL, 20, 0.20, border}}; // Rows
 *
 *     frame.setLayout (new TableLayout(size));
 *
 *     // Create some buttons
 *     String label[] = {"Top", "Bottom", "Left", "Right", "Center", "Overlap"};
 *     Button button[] = new Button[label.length];
 *
 *     for (int i = 0; i < label.length; i++)
 *         button[i] = new Button(label[i]);
 *
 *     // Add buttons
 *     frame.add (button[0], "1, 1, 5, 1"); // Top
 *     frame.add (button[1], "1, 5, 5, 5"); // Bottom
 *     frame.add (button[2], "1, 3      "); // Left
 *     frame.add (button[3], "5, 3      "); // Right
 *     frame.add (button[4], "3, 3, c, c"); // Center
 *     frame.add (button[5], "3, 3, 3, 5"); // Overlap
 *
 *     // Allow user to close the window to terminate the program
 *     frame.addWindowListener
 *         (new WindowListener()
 *             {
 *                 public void windowClosing (WindowEvent e)
 *                 {
 *                     System.exit (0);
 *                 }
 *
 *                 public void windowOpened (WindowEvent e) {}
 *                 public void windowClosed (WindowEvent e) {}
 *                 public void windowIconified (WindowEvent e) {}
 *                 public void windowDeiconified (WindowEvent e) {}
 *                 public void windowActivated (WindowEvent e) {}
 *                 public void windowDeactivated (WindowEvent e) {}
 *             }
 *         );
 *
 *     // Show frame
 *     frame.show();
 * }
 * </pre>
 *
 */
public class TableLayout
    implements java.awt.LayoutManager2,
	          java.io.Serializable,
	          TableLayoutConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Default row/column size */
    protected static final double[][] defaultSize = { {  }, {  } };

	/** Widths of columns expressed in absolute and relative terms */
    protected double[] columnSpec;

	/** Heights of rows expressed in absolute and relative terms */
    protected double[] rowSpec;

	/** Widths of columns in pixels */
    protected int[] columnSize;

	/** Heights of rows in pixels */
    protected int[] rowSize;

	/** Offsets of columns in pixels.  The left boarder of column n is at
 	 columnOffset[n] and the right boarder is at columnOffset[n + 1] for all
 	 columns including the last one.  columnOffset.length = columnSize.length + 1 */
    protected int[] columnOffset;

	/** Offsets of rows in pixels.  The left boarder of row n is at
 	 rowOffset[n] and the right boarder is at rowOffset[n + 1] for all
 	 rows including the last one.  rowOffset.length = rowSize.length + 1 */
    protected int[] rowOffset;

	protected int vgap = 0;
	protected int hgap = 0;

	/** List of components and their sizes */
    protected LinkedList<Entry> list;

	/** Indicates whether or not the size of the cells are known for the last known
 	 size of the container.  If dirty is true or the container has been resized,
 	 the cell sizes must be recalculated using calculateSize. */
    protected boolean dirty;

	/** Previous known width of the container */
    protected int oldWidth;

	/** Previous known height of the container */
    protected int oldHeight;

	/**
	 * Constructs an instance of TableLayout.  This TableLayout will have one row
	 * and one column.
	 */
	public TableLayout() {
	   this(defaultSize);
	}

   /**
	 * Constructs an instance of TableLayout.
	 *
	 * @param size    widths of columns and heights of rows in the format,
	 *                {{col0, col1, col2, ..., colN}, {row0, row1, row2, ..., rowM}}
	 *                If this parameter is invalid, the TableLayout will have
	 *                exactly one row and one column.
	 */
   public TableLayout(double[][] size) {

	   if ((size != null) && (size.length == 2)) {

	       double[] tempCol = size[0];
	       double[] tempRow = size[1];

	       columnSpec = new double[tempCol.length];
	       rowSpec = new double[tempRow.length];
	       System.arraycopy(tempCol, 0, columnSpec, 0, columnSpec.length);
	       System.arraycopy(tempRow, 0, rowSpec, 0, rowSpec.length);

	       for (int counter = 0; counter < columnSpec.length; counter++) {

	           if ((columnSpec[counter] < 0.0) && 
	               (columnSpec[counter] != FILL) && 
	               (columnSpec[counter] != PREFERRED) && 
	               (columnSpec[counter] != MINIMUM)) {
	               columnSpec[counter] = 0.0;
	           }
	       }

	       for (int counter = 0; counter < rowSpec.length; counter++) {

	           if ((rowSpec[counter] < 0.0) && (rowSpec[counter] != FILL) && 
	               (rowSpec[counter] != PREFERRED) && 
	               (rowSpec[counter] != MINIMUM)) {
	               rowSpec[counter] = 0.0;
	           }
	       }
	   } else {

	       double[] tempCol = { FILL };
	       double[] tempRow = { FILL };

	       setColumn(tempCol);
	       setRow(tempRow);
	   }

	   list = new LinkedList<Entry>();
	   dirty = true;
	}

	/**
	 * Gets the constraints of a given component.
	 *
	 * @param component    desired component
	 *
	 * @return If the given component is found, the constraints associated with
	 *         that component.  If the given component is null or is not found,
	 *         null is returned.
	 */
	public TableLayoutConstraints getConstraints (Component component) {

	   ListIterator<Entry> iterator = list.listIterator(0);

	   while (iterator.hasNext()) {

	       Entry entry = iterator.next();

	       if (entry.component == component) {

	           return new TableLayoutConstraints(entry.col1, entry.row1, 
	                                             entry.col2, entry.row2, 
	                                             entry.hAlign, entry.vAlign);
	       }
	   }

	   return null;
	}

	/**
	 * Sets the constraints of a given component.
	 *
	 * @param component     desired component.  This parameter cannot be null.
	 * @param constraint    new set of constraints.  This parameter cannot be null.
	 */
	public void setConstraints (Component component, 
	                          TableLayoutConstraints constraint) {

	   if (component == null) {
	       throw new IllegalArgumentException("Parameter component cannot be null.");
	   } else if (constraint == null) {
	       throw new IllegalArgumentException("Parameter constraint cannot be null.");
	   }

	   ListIterator<Entry> iterator = list.listIterator(0);

	   while (iterator.hasNext()) {

	       Entry entry = iterator.next();

	       if (entry.component == component) {
	           iterator.set(new Entry(component, constraint));
	       }
	   }
	}

	/**
	 * Adjusts the number and sizes of rows in this layout.  After calling this
	 * method, the caller should request this layout manager to perform the
	 * layout.  This can be done with the following code:
	 *
	 * <pre>
	 *     layout.layoutContainer(container);
	 *     container.repaint();
	 * </pre>
	 *
	 * or
	 *
	 * <pre>
	 *     window.pack()
	 * </pre>
	 *
	 * If this is not done, the changes in the layout will not be seen until the
	 * container is resized.
	 *
	 * @param column    heights of each of the columns
	 *
	 * @see #getColumn
	 */
	public void setColumn (double[] column) {
	   columnSpec = new double[column.length];
	   System.arraycopy(column, 0, columnSpec, 0, columnSpec.length);

	   for (int counter = 0; counter < columnSpec.length; counter++) {

	       if ((columnSpec[counter] < 0.0) && 
	           (columnSpec[counter] != FILL) && 
	           (columnSpec[counter] != PREFERRED) && 
	           (columnSpec[counter] != MINIMUM)) {
	           columnSpec[counter] = 0.0;
	       }
	   }

	   dirty = true;
	}

	/**
	 * Adjusts the number and sizes of rows in this layout.  After calling this
	 * method, the caller should request this layout manager to perform the
	 * layout.  This can be done with the following code:
	 *
	 * <code>
	 *     layout.layoutContainer(container);
	 *     container.repaint();
	 * </code>
	 *
	 * or
	 *
	 * <pre>
	 *     window.pack()
	 * </pre>
	 *
	 * If this is not done, the changes in the layout will not be seen until the
	 * container is resized.
	 *
	 * @param row    widths of each of the rows.  This parameter cannot be null.
	 *
	 * @see #getRow
	 */
	public void setRow (double[] row) {
	   rowSpec = new double[row.length];
	   System.arraycopy(row, 0, rowSpec, 0, rowSpec.length);

	   for (int counter = 0; counter < rowSpec.length; counter++) {

	       if ((rowSpec[counter] < 0.0) && (rowSpec[counter] != FILL) && 
	           (rowSpec[counter] != PREFERRED) && 
	           (rowSpec[counter] != MINIMUM)) {
	           rowSpec[counter] = 0.0;
	       }
	   }

	   dirty = true;
	}

	/**
	 * Adjusts the width of a single column in this layout.  After calling this
	 * method, the caller should request this layout manager to perform the
	 * layout.  This can be done with the following code:
	 *
	 * <code>
	 *     layout.layoutContainer(container);
	 *     container.repaint();
	 * </code>
	 *
	 * or
	 *
	 * <pre>
	 *     window.pack()
	 * </pre>
	 *
	 * If this is not done, the changes in the layout will not be seen until the
	 * container is resized.
	 *
	 * @param i       zero-based index of column to set.  If this parameter is not
	 *                valid, an ArrayOutOfBoundsException will be thrown.
	 * @param size    width of the column.  This parameter cannot be null.
	 *
	 * @see #getColumn
	 */
	public void setColumn(int i, double size) {

	   if ((size < 0.0) && (size != FILL) && (size != PREFERRED) && 
	       (size != MINIMUM)) {
	       size = 0.0;
	   }

	   columnSpec[i] = size;
	   dirty = true;
	}

	/**
	 * Adjusts the height of a single row in this layout.  After calling this
	 * method, the caller should request this layout manager to perform the
	 * layout.  This can be done with the following code:
	 *
	 * <code>
	 *     layout.layoutContainer(container);
	 *     container.repaint();
	 * </code>
	 *
	 * or
	 *
	 * <pre>
	 *     window.pack()
	 * </pre>
	 *
	 * If this is not done, the changes in the layout will not be seen until the
	 * container is resized.
	 *
	 * @param i       zero-based index of row to set.  If this parameter is not
	 *                valid, an ArrayOutOfBoundsException will be thrown.
	 * @param size    height of the row.  This parameter cannot be null.
	 *
	 * @see #getRow
	 */
	public void setRow(int i, double size) {

	   if ((size < 0.0) && (size != FILL) && (size != PREFERRED) && 
	       (size != MINIMUM)) {
	       size = 0.0;
	   }

	   rowSpec[i] = size;
	   dirty = true;
	}

	/**
	 * Gets the sizes of columns in this layout.
	 *
	 * @return widths of each of the columns
	 *
	 * @see #setColumn
	 */
	public double[] getColumn() {

	   double[] column = new double[columnSpec.length];

	   System.arraycopy(columnSpec, 0, column, 0, column.length);

	   return column;
	}

	/**
	 * Gets the height of a single row in this layout.
	 *
	 * @return height of the requested row
	 *
	 * @see #setRow
	 */
	public double[] getRow() {

	   double[] row = new double[rowSpec.length];

	   System.arraycopy(rowSpec, 0, row, 0, row.length);

	   return row;
	}

	/**
	 * Gets the width of a single column in this layout.
	 *
	 * @param i    zero-based index of row to get. If this parameter is
	 * not valid, an ArrayOutOfBoundsException will be thrown.
	 * @return width of the requested column
	 *
	 * @see #setRow
	 */
	public double getColumn(int i) {

	   return columnSpec[i];
	}

	/**
	 * Gets the sizes of a row in this layout.
	 *
	 * @param i    zero-based index of row to get.  If this parameter is not valid,
	 *             an ArrayOutOfBoundsException will be thrown.
	 *
	 * @return height of each of the requested row
	 *
	 * @see #setRow
	 */
	public double getRow(int i) {

	   return rowSpec[i];
	}

	/**
	 * Gets the number of columns in this layout.
	 *
	 * @return the number of columns
	 */
	public int getNumColumn() {

	   return columnSpec.length;
	}

	/**
	 * Gets the number of rows in this layout.
	 *
	 * @return the number of rows
	 */
	public int getNumRow() {

	   return rowSpec.length;
	}

	/**
	 * Inserts a column in this layout.  All components to the right of the
	 * insertion point are moved right one column.  The container will need to
	 * be laid out after this method returns.  See <code>setColumn</code>.
	 *
	 * @param i       zero-based index at which to insert the column.
	 * @param size    size of the column to be inserted
	 *
	 * @see #setColumn
	 * @see #deleteColumn
	 */
	public void insertColumn(int i, double size) {

	   if ((i < 0) || (i > columnSpec.length)) {
	       throw new IllegalArgumentException("Parameter i is invalid.  i = " + i + 
	                                          ".  Valid range is [0, " + 
	                                          columnSpec.length + "].");
	   }

	   if ((size < 0.0) && (size != FILL) && (size != PREFERRED) && 
	       (size != MINIMUM)) {
	       size = 0.0;
	   }

	   double[] column = new double[columnSpec.length + 1];

	   System.arraycopy(columnSpec, 0, column, 0, i);
	   System.arraycopy(columnSpec, i, column, i + 1, columnSpec.length - i);
	   column[i] = size;
	   columnSpec = column;

	   ListIterator<Entry> iterator = list.listIterator(0);

	   while (iterator.hasNext()) {

	       Entry entry = iterator.next();

	       if (entry.col1 >= i) {
	           entry.col1++;
	       }

	       if (entry.col2 >= i) {
	           entry.col2++;
	       }
	   }

	   dirty = true;
	}

	/**
	 * Inserts a row in this layout.  All components below the insertion point
	 * are moved down one row.  The container will need to be laid out after this
	 * method returns.  See <code>setRow</code>.
	 *
	 * @param i       zero-based index at which to insert the column.
	 * @param size    size of the row to be inserted
	 *
	 * @see #setRow
	 * @see #deleteRow
	 */
	public void insertRow(int i, double size) {

	   if ((i < 0) || (i > rowSpec.length)) {
	       throw new IllegalArgumentException("Parameter i is invalid.  i = " + i + 
	                                          ".  Valid range is [0, " + 
	                                          rowSpec.length + "].");
	   }

	   if ((size < 0.0) && (size != FILL) && (size != PREFERRED) && 
	       (size != MINIMUM)) {
	       size = 0.0;
	   }

	   double[] row = new double[rowSpec.length + 1];

	   System.arraycopy(rowSpec, 0, row, 0, i);
	   System.arraycopy(rowSpec, i, row, i + 1, rowSpec.length - i);
	   row[i] = size;
	   rowSpec = row;

	   ListIterator<Entry> iterator = list.listIterator(0);

	   while (iterator.hasNext()) {

	       Entry entry = iterator.next();

	       if (entry.row1 >= i) {
	           entry.row1++;
	       }

	       if (entry.row2 >= i) {
	           entry.row2++;
	       }
	   }

	   dirty = true;
	}

	/**
	 * Deletes a column in this layout.  All components to the right of the
	 * deletion point are moved left one column.  The container will need to
	 * be laid out after this method returns.  See <code>setColumn</code>.
	 *
	 * @param i    zero-based index of column to delete
	 *
	 * @see #setColumn
	 * @see #deleteColumn
	 */
	public void deleteColumn(int i) {

	   if ((i < 0) || (i >= columnSpec.length)) {
	       throw new IllegalArgumentException("Parameter i is invalid.  i = " + i + 
	                                          ".  Valid range is [0, " + 
	                                          (columnSpec.length - 1) + 
	                                          "].");
	   }

	   double[] column = new double[columnSpec.length - 1];

	   System.arraycopy(columnSpec, 0, column, 0, i);
	   System.arraycopy(columnSpec, i + 1, column, i, 
	                    columnSpec.length - i - 1);
	   columnSpec = column;

	   ListIterator<Entry> iterator = list.listIterator(0);

	   while (iterator.hasNext()) {

	       Entry entry = iterator.next();

	       if (entry.col1 >= i) {
	           entry.col1--;
	       }

	       if (entry.col2 >= i) {
	           entry.col2--;
	       }
	   }

	   dirty = true;
	}

	/**
	 * Deletes a row in this layout.  All components below the deletion point are
	 * moved up one row.  The container will need to be laid out after this method
	 * returns.  See <code>setRow</code>.  There must be at least two rows in order
	 * to delete a row.
	 *
	 * @param i    zero-based index of column to delete
	 *
	 * @see #setRow
	 * @see #deleteRow
	 */
	public void deleteRow(int i) {

	   if ((i < 0) || (i >= rowSpec.length)) {
	       throw new IllegalArgumentException("Parameter i is invalid.  i = " + i + 
	                                          ".  Valid range is [0, " + 
	                                          (rowSpec.length - 1) + "].");
	   }

	   double[] row = new double[rowSpec.length - 1];

	   System.arraycopy(rowSpec, 0, row, 0, i);
	   System.arraycopy(rowSpec, i + 1, row, i, rowSpec.length - i - 1);
	   rowSpec = row;

	   ListIterator<Entry> iterator = list.listIterator(0);

	   while (iterator.hasNext()) {

	       Entry entry = iterator.next();

	       if (entry.row1 >= i) {
	           entry.row1--;
	       }

	       if (entry.row2 >= i) {
	           entry.row2--;
	       }
	   }

	   dirty = true;
	}

	/**
	 * Converts this TableLayout to a string.
	 *
	 * @return a string representing the columns and row sizes in the form
	 *         "{{col0, col1, col2, ..., colN}, {row0, row1, row2, ..., rowM}}"
	 */
	public String toString() {

	   int counter;
	   String value = "TableLayout {{";

	   if (columnSpec.length > 0) {

	       for (counter = 0; counter < columnSpec.length - 1; counter++) {
	           value += columnSpec[counter] + ", ";
	       }

	       value += columnSpec[columnSpec.length - 1] + "}, {";
	   } else {
	       value += "}, {";
	   }

	   if (rowSpec.length > 0) {

	       for (counter = 0; counter < rowSpec.length - 1; counter++) {
	           value += rowSpec[counter] + ", ";
	       }

	       value += rowSpec[rowSpec.length - 1] + "}}";
	   } else {
	       value += "}}";
	   }

	   return value;
	}

	/**
	 * Draws a grid on the given container.  This is useful for seeing where the
	 * rows and columns go.  In the container's paint method, call this method.
	 *
	 * @param container    container using this TableLayout
	 * @param g            graphics content of container (can be offscreen)
	 */
	public void drawGrid(Container container, Graphics g) {

	   Dimension d = container.getSize();

	   if (dirty || (d.width != oldWidth) || (d.height != oldHeight)) {
	       calculateSize(container);
	   }

	   int y = 0;

	   for (int row = 0; row < rowSize.length; row++) {

	       int x = 0;

	       for (int column = 0; column < columnSize.length; column++) {

	           Color color = new Color((int)(Math.random() * 0xFFFFFFL));

	           g.setColor(color);
	           g.fillRect(x, y, columnSize[column], rowSize[row]);
	           x += columnSize[column];
	       }

	       y += rowSize[row];
	   }
	}

	/**
	 * Determines whether or not there are any hidden components.  A hidden
	 * component is one that will not be shown with this layout's current
	 * configuration.  Such a component is, at least partly, in an invalid row
	 * or column.  For example, on a table with five rows, row -1 and row 5 are both
	 * invalid.  Valid rows are 0 through 4, inclusively.
	 *
	 * @return    True, if there are any hidden components.  False, otherwise.
	 *
	 * @see #overlapping
	 */
	public boolean hidden() {

	   boolean hidden = false;
	   ListIterator<Entry> iterator = list.listIterator(0);

	   while (iterator.hasNext()) {

	       Entry entry = iterator.next();

	       if ((entry.row1 < 0) || (entry.col1 < 0) || 
	           (entry.row2 > rowSpec.length) || 
	           (entry.col2 > columnSpec.length)) {
	           hidden = true;

	           break;
	       }
	   }

	   return hidden;
	}

	/**
	 * Determines whether or not there are any overlapping components.  Two
	 * components overlap if they cover at least one common cell.
	 *
	 * @return  true, if there are any overlapping components.  false, otherwise
	 *
	 * @see #hidden
	 */
	public boolean overlapping() {

	   int numEntry = list.size();

	   if (numEntry == 0) {

	       return false;
	   }

	   boolean overlapping = false;
	   Entry[] entry = list.toArray(new Entry[numEntry]);

	   for (int knowUnique = 1; knowUnique < numEntry; knowUnique++) {

	       for (int checking = knowUnique - 1; checking >= 0; checking--) {

	           if (((entry[checking].col1 >= entry[knowUnique].col1) && 
	                   (entry[checking].col1 <= entry[knowUnique].col2) && 
	                   (entry[checking].row1 >= entry[knowUnique].row1) && 
	                   (entry[checking].row1 <= entry[knowUnique].row2)) || 
	               ((entry[checking].col2 >= entry[knowUnique].col1) && 
	                   (entry[checking].col2 <= entry[knowUnique].col2) && 
	                   (entry[checking].row2 >= entry[knowUnique].row1) && 
	                   (entry[checking].row2 <= entry[knowUnique].row2))) {
	               overlapping = true;

	               break;
	           }
	       }
	   }

	   return overlapping;
	}

	/**
	 * Calculates the sizes of the rows and columns based on the absolute and
	 * relative sizes specified in <code>rowSpec</code> and
	 * <code>columnSpec</code> and the size of the container.  The result is
	 * stored in <code>rowSize</code> and <code>columnSize</code>.
	 *
	 * @param container    container using this TableLayout
	 */
    protected void calculateSize(Container container) {

	   int counter;
	   int numColumn = columnSpec.length;
	   int numRow = rowSpec.length;

	   columnSize = new int[numColumn];
	   rowSize = new int[numRow];

	   Insets inset = container.getInsets();
	   Dimension d = container.getSize();
	   int totalWidth = d.width - inset.left - inset.right;
	   int totalHeight = d.height - inset.top - inset.bottom;
	   int availableWidth = totalWidth;
	   int availableHeight = totalHeight;

	   for (counter = 0; counter < numColumn; counter++) {

	       if ((columnSpec[counter] >= 1.0) || 
	           (columnSpec[counter] == 0.0)) {
	           columnSize[counter] = (int)(columnSpec[counter] + 0.5);
	           availableWidth -= columnSize[counter];
	       }
	   }

	   for (counter = 0; counter < numRow; counter++) {

	       if ((rowSpec[counter] >= 1.0) || (rowSpec[counter] == 0.0)) {
	           rowSize[counter] = (int)(rowSpec[counter] + 0.5);
	           availableHeight -= rowSize[counter];
	       }
	   }

	   for (counter = 0; counter < numColumn; counter++) {

	       if ((columnSpec[counter] == PREFERRED) || 
	           (columnSpec[counter] == MINIMUM)) {

	           int maxWidth = 0;
	           ListIterator<Entry> iterator = list.listIterator(0);

	           while (iterator.hasNext()) {

	               Entry entry = iterator.next();

	               if ((entry.col1 == counter) && (entry.col2 == counter)) {

	                   Dimension p = (columnSpec[counter] == PREFERRED)
	                                     ? entry.component.getPreferredSize()
	                                     : entry.component.getMinimumSize();
	                   int width = (p == null) ? 0 : p.width;

	                   if (maxWidth < width) {
	                       maxWidth = width;
	                   }
	               }
	           }

	           columnSize[counter] = maxWidth;
	           availableWidth -= maxWidth;
	       }
	   }

	   for (counter = 0; counter < numRow; counter++) {

	       if ((rowSpec[counter] == PREFERRED) || 
	           (rowSpec[counter] == MINIMUM)) {

	           int maxHeight = 0;
	           ListIterator<Entry> iterator = list.listIterator(0);

	           while (iterator.hasNext()) {

	               Entry entry = iterator.next();

	               if ((entry.row1 == counter) && (entry.row2 == counter)) {

	                   Dimension p = (rowSpec[counter] == PREFERRED)
	                                     ? entry.component.getPreferredSize()
	                                     : entry.component.getMinimumSize();
	                   int height = (p == null) ? 0 : p.height;

	                   if (maxHeight < height) {
	                       maxHeight = height;
	                   }
	               }
	           }

	           rowSize[counter] = maxHeight;
	           availableHeight -= maxHeight;
	       }
	   }

	   int relativeWidth = availableWidth;
	   int relativeHeight = availableHeight;

	   if (relativeWidth < 0) {
	       relativeWidth = 0;
	   }

	   if (relativeHeight < 0) {
	       relativeHeight = 0;
	   }

	   for (counter = 0; counter < numColumn; counter++) {

	       if ((columnSpec[counter] > 0.0) && (columnSpec[counter] < 1.0)) {
	           columnSize[counter] = (int)(columnSpec[counter] * relativeWidth + 
	                                 0.5);
	           availableWidth -= columnSize[counter];
	       }
	   }

	   for (counter = 0; counter < numRow; counter++) {

	       if ((rowSpec[counter] > 0.0) && (rowSpec[counter] < 1.0)) {
	           rowSize[counter] = (int)(rowSpec[counter] * relativeHeight + 
	                              0.5);
	           availableHeight -= rowSize[counter];
	       }
	   }

	   if (availableWidth < 0) {
	       availableWidth = 0;
	   }

	   if (availableHeight < 0) {
	       availableHeight = 0;
	   }

	   int numFillWidth = 0;
	   int numFillHeight = 0;

	   for (counter = 0; counter < numColumn; counter++) {

	       if (columnSpec[counter] == FILL) {
	           numFillWidth++;
	       }
	   }

	   for (counter = 0; counter < numRow; counter++) {

	       if (rowSpec[counter] == FILL) {
	           numFillHeight++;
	       }
	   }

	   int slackWidth = availableWidth;
	   int slackHeight = availableHeight;

	   for (counter = 0; counter < numColumn; counter++) {

	       if (columnSpec[counter] == FILL) {
	           columnSize[counter] = availableWidth / numFillWidth;
	           slackWidth -= columnSize[counter];
	       }
	   }

	   for (counter = 0; counter < numRow; counter++) {

	       if (rowSpec[counter] == FILL) {
	           rowSize[counter] = availableHeight / numFillHeight;
	           slackHeight -= rowSize[counter];
	       }
	   }

	   for (counter = numColumn - 1; counter >= 0; counter--) {

	       if (columnSpec[counter] == FILL) {
	           columnSize[counter] += slackWidth;

	           break;
	       }
	   }

	   for (counter = numRow - 1; counter >= 0; counter--) {

	       if (rowSpec[counter] == FILL) {
	           rowSize[counter] += slackHeight;

	           break;
	       }
	   }

	   columnOffset = new int[numColumn + 1];
	   columnOffset[0] = inset.left;

	   for (counter = 0; counter < numColumn; counter++) {
	       columnOffset[counter + 1] = columnOffset[counter] + 
	                                   columnSize[counter];
	   }

	   rowOffset = new int[numRow + 1];
	   rowOffset[0] = inset.top;

	   for (counter = 0; counter < numRow; counter++) {
	       rowOffset[counter + 1] = rowOffset[counter] + rowSize[counter];
	   }

	   dirty = false;
	   oldWidth = totalWidth;
	   oldHeight = totalHeight;
	}

	/**
	 * To lay out the specified container using this layout.  This method
	 * reshapes the components in the specified target container in order
	 * to satisfy the constraints of all components.
	 *
	 * <p>User code should not have to call this method directly.</p>
	 *
	 * @param container    container being served by this layout manager
	 */
	public void layoutContainer(Container container) {

	   int x;
	   int y;
	   int w;
	   int h;
	   Dimension d = container.getSize();

	   if (dirty || (d.width != oldWidth) || (d.height != oldHeight)) {
	       calculateSize(container);
	   }

	   Component[] component = container.getComponents();

	   for (int counter = 0; counter < component.length; counter++) {

	       try {

	           ListIterator<Entry> iterator = list.listIterator(0);
	           Entry entry = null;

	           while (iterator.hasNext()) {
	               entry = iterator.next();

	               if (entry.component == component[counter]) {

	                   break;
	               } else {
	                   entry = null;
	               }
	           }

	           if (entry == null) {

	               break;
	           }

	           if (entry.singleCell) {

	               int preferredWidth = 0;
	               int preferredHeight = 0;

	               if ((entry.hAlign != FULL) || (entry.vAlign != FULL)) {

	                   Dimension preferredSize = component[counter].getPreferredSize();

	                   preferredWidth = preferredSize.width;
	                   preferredHeight = preferredSize.height;
	               }

	               int cellWidth = columnSize[entry.col1];
	               int cellHeight = rowSize[entry.row1];

	               if ((entry.hAlign == FULL) || 
	                   (cellWidth < preferredWidth)) {
	                   w = cellWidth;
	               } else {
	                   w = preferredWidth;
	               }

	               switch (entry.hAlign) {

	                   case LEFT:
	                       x = columnOffset[entry.col1];

	                       break;

	                   case RIGHT:
	                       x = columnOffset[entry.col1 + 1] - w;

	                       break;

	                   case CENTER:
	                       x = columnOffset[entry.col1] + 
	                           ((cellWidth - w) >> 1);

	                       break;

	                   case FULL:
	                       x = columnOffset[entry.col1];

	                       break;

	                   default:
	                       x = 0;
	               }

	               if ((entry.vAlign == FULL) || 
	                   (cellHeight < preferredHeight)) {
	                   h = cellHeight;
	               } else {
	                   h = preferredHeight;
	               }

	               switch (entry.vAlign) {

	                   case TOP:
	                       y = rowOffset[entry.row1];

	                       break;

	                   case BOTTOM:
	                       y = rowOffset[entry.row1 + 1] - h;

	                       break;

	                   case CENTER:
	                       y = rowOffset[entry.row1] + 
	                           ((cellHeight - h) >> 1);

	                       break;

	                   case FULL:
	                       y = rowOffset[entry.row1];

	                       break;

	                   default:
	                       y = 0;
	               }
	           } else {
	               x = columnOffset[entry.col1];
	               y = rowOffset[entry.row1];
	               w = columnOffset[entry.col2 + 1] - 
	                   columnOffset[entry.col1];
	               h = rowOffset[entry.row2 + 1] - rowOffset[entry.row1];
	           }

	           component[counter].setBounds(x, y, w, h);
	       } catch (Exception error) {

	           continue;
	       }
	   }
	}

	/**
	 * Determines the preferred size of the container argument using this layout.
	 * The preferred size is the smallest size that, if used for the container's
	 * size, will ensure that all components are at least as large as their
	 * preferred size.  This method cannot guarantee that all components will be
	 * their preferred size.  For example, if component A and component B are
	 * each allocate half of the container's width and component A wants to be
	 * 10 pixels wide while component B wants to be 100 pixels wide, they cannot
	 * both be accommodated.  Since in general components rather be larger than
	 * their preferred size instead of smaller, component B's request will be
	 * fulfilled.  The preferred size of the container would be 200 pixels.
	 *
	 * @param container    container being served by this layout manager
	 *
	 * @return a dimension indicating the container's preferred size
	 */
	public Dimension preferredLayoutSize(Container container) {

	   Dimension size;
	   int scaledWidth = 0;
	   int scaledHeight = 0;
	   int temp;
	   int counter;
	   double fillWidthRatio = 1.0;
	   double fillHeightRatio = 1.0;
	   int numFillWidth = 0;
	   int numFillHeight = 0;

	   for (counter = 0; counter < columnSpec.length; counter++) {

	       if ((columnSpec[counter] > 0.0) && (columnSpec[counter] < 1.0)) {
	           fillWidthRatio -= columnSpec[counter];
	       } else if (columnSpec[counter] == FILL) {
	           numFillWidth++;
	       }
	   }

	   for (counter = 0; counter < rowSpec.length; counter++) {

	       if ((rowSpec[counter] > 0.0) && (rowSpec[counter] < 1.0)) {
	           fillHeightRatio -= rowSpec[counter];
	       } else if (rowSpec[counter] == FILL) {
	           numFillHeight++;
	       }
	   }

	   if (numFillWidth > 1) {
	       fillWidthRatio /= numFillWidth;
	   }

	   if (numFillHeight > 1) {
	       fillHeightRatio /= numFillHeight;
	   }

	   if (fillWidthRatio < 0.0) {
	       fillWidthRatio = 0.0;
	   }

	   if (fillHeightRatio < 0.0) {
	       fillHeightRatio = 0.0;
	   }

	   int[] columnPrefMin = new int[columnSpec.length];

	   for (counter = 0; counter < columnSpec.length; counter++) {

	       if ((columnSpec[counter] == PREFERRED) || 
	           (columnSpec[counter] == MINIMUM)) {

	           int maxWidth = 0;
	           ListIterator<Entry> iterator = list.listIterator(0);

	           while (iterator.hasNext()) {

	               Entry entry = iterator.next();

	               if ((entry.col1 == counter) && (entry.col2 == counter)) {

	                   Dimension p = (columnSpec[counter] == PREFERRED)
	                                     ? entry.component.getPreferredSize()
	                                     : entry.component.getMinimumSize();
	                   int width = (p == null) ? 0 : p.width;

	                   if (maxWidth < width) {
	                       maxWidth = width;
	                   }
	               }
	           }

	           columnPrefMin[counter] = maxWidth;
	       }
	   }

	   int[] rowPrefMin = new int[rowSpec.length];

	   for (counter = 0; counter < rowSpec.length; counter++) {

	       if ((rowSpec[counter] == PREFERRED) || 
	           (rowSpec[counter] == MINIMUM)) {

	           int maxHeight = 0;
	           ListIterator<Entry> iterator = list.listIterator(0);

	           while (iterator.hasNext()) {

	               Entry entry = iterator.next();

	               if ((entry.row1 == counter) && (entry.row1 == counter)) {

	                   Dimension p = (rowSpec[counter] == PREFERRED)
	                                     ? entry.component.getPreferredSize()
	                                     : entry.component.getMinimumSize();
	                   int height = (p == null) ? 0 : p.height;

	                   if (maxHeight < height) {
	                       maxHeight = height;
	                   }
	               }
	           }

	           rowPrefMin[counter] += maxHeight;
	       }
	   }

	   ListIterator<Entry> iterator = list.listIterator(0);

	   while (iterator.hasNext()) {

	       Entry entry = iterator.next();

	       if ((entry.col1 < 0) || (entry.col1 >= columnSpec.length) || 
	           (entry.col2 >= columnSpec.length) || (entry.row1 < 0) || 
	           (entry.row1 >= rowSpec.length) || 
	           (entry.row2 >= rowSpec.length)) {

	           continue;
	       }

	       size = entry.component.getPreferredSize();

	       int scalableWidth = size.width;
	       int scalableHeight = size.height;

	       for (counter = entry.col1; counter <= entry.col2; counter++) {

	           if (columnSpec[counter] >= 1.0) {
	               scalableWidth -= columnSpec[counter];
	           } else if ((columnSpec[counter] == PREFERRED) || 
	                      (columnSpec[counter] == MINIMUM)) {
	               scalableWidth -= columnPrefMin[counter];
	           }
	       }

	       for (counter = entry.row1; counter <= entry.row2; counter++) {

	           if (rowSpec[counter] >= 1.0) {
	               scalableHeight -= rowSpec[counter];
	           } else if ((rowSpec[counter] == PREFERRED) || 
	                      (rowSpec[counter] == MINIMUM)) {
	               scalableHeight -= rowPrefMin[counter];
	           }
	       }

	       double relativeWidth = 0.0;

	       for (counter = entry.col1; counter <= entry.col2; counter++) {

	           if ((columnSpec[counter] > 0.0) && 
	               (columnSpec[counter] < 1.0)) {
	               relativeWidth += columnSpec[counter];
	           } else if ((columnSpec[counter] == FILL) && 
	                      (fillWidthRatio != 0.0)) {
	               relativeWidth += fillWidthRatio;
	           }
	       }

	       if (relativeWidth == 0) {
	           temp = 0;
	       } else {
	           temp = (int)(scalableWidth / relativeWidth + 0.5);
	       }

	       if (scaledWidth < temp) {
	           scaledWidth = temp;
	       }

	       double relativeHeight = 0.0;

	       for (counter = entry.row1; counter <= entry.row2; counter++) {

	           if ((rowSpec[counter] > 0.0) && (rowSpec[counter] < 1.0)) {
	               relativeHeight += rowSpec[counter];
	           } else if ((rowSpec[counter] == FILL) && 
	                      (fillHeightRatio != 0.0)) {
	               relativeHeight += fillHeightRatio;
	           }
	       }

	       if (relativeHeight == 0) {
	           temp = 0;
	       } else {
	           temp = (int)(scalableHeight / relativeHeight + 0.5);
	       }

	       if (scaledHeight < temp) {
	           scaledHeight = temp;
	       }
	   }

	   int totalWidth = scaledWidth;

	   for (counter = 0; counter < columnSpec.length; counter++) {

	       if (columnSpec[counter] >= 1.0) {
	           totalWidth += (int)(columnSpec[counter] + 0.5);
	       } else if ((columnSpec[counter] == PREFERRED) || 
	                  (columnSpec[counter] == MINIMUM)) {
	           totalWidth += columnPrefMin[counter];
	       }
	   }

	   int totalHeight = scaledHeight;

	   for (counter = 0; counter < rowSpec.length; counter++) {

	       if (rowSpec[counter] >= 1.0) {
	           totalHeight += (int)(rowSpec[counter] + 0.5);
	       } else if ((rowSpec[counter] == PREFERRED) || 
	                  (rowSpec[counter] == MINIMUM)) {
	           totalHeight += rowPrefMin[counter];
	       }
	   }

	   Insets inset = container.getInsets();

	   totalWidth += inset.left + inset.right;
	   totalHeight += inset.top + inset.bottom;

	   return new Dimension(totalWidth, totalHeight);
	}

	/**
	 * Determines the minimum size of the container argument using this layout.
	 * The minimum size is the smallest size that, if used for the container's
	 * size, will ensure that all components are at least as large as their
	 * minimum size.  This method cannot guarantee that all components will be
	 * their minimum size.  For example, if component A and component B are each
	 * allocate half of the container's width and component A wants to be 10
	 * pixels wide while component B wants to be 100 pixels wide, they cannot
	 * both be accommodated.  Since in general components rather be larger than
	 * their minimum size instead of smaller, component B's request will be
	 * fulfilled. The minimum size of the container would be 200 pixels.
	 *
	 * @param container    container being served by this layout manager
	 *
	 * @return a dimension indicating the container's minimum size
	 */
	public Dimension minimumLayoutSize(Container container) {

	   Dimension size;
	   int scaledWidth = 0;
	   int scaledHeight = 0;
	   int temp;
	   int counter;
	   double fillWidthRatio = 1.0;
	   double fillHeightRatio = 1.0;
	   int numFillWidth = 0;
	   int numFillHeight = 0;

	   for (counter = 0; counter < columnSpec.length; counter++) {

	       if ((columnSpec[counter] > 0.0) && (columnSpec[counter] < 1.0)) {
	           fillWidthRatio -= columnSpec[counter];
	       } else if (columnSpec[counter] == FILL) {
	           numFillWidth++;
	       }
	   }

	   for (counter = 0; counter < rowSpec.length; counter++) {

	       if ((rowSpec[counter] > 0.0) && (rowSpec[counter] < 1.0)) {
	           fillHeightRatio -= rowSpec[counter];
	       } else if (rowSpec[counter] == FILL) {
	           numFillHeight++;
	       }
	   }

	   if (numFillWidth > 1) {
	       fillWidthRatio /= numFillWidth;
	   }

	   if (numFillHeight > 1) {
	       fillHeightRatio /= numFillHeight;
	   }

	   if (fillWidthRatio < 0.0) {
	       fillWidthRatio = 0.0;
	   }

	   if (fillHeightRatio < 0.0) {
	       fillHeightRatio = 0.0;
	   }

	   ListIterator<Entry> iterator = list.listIterator(0);

	   while (iterator.hasNext()) {

	       Entry entry = iterator.next();

	       if ((entry.col1 < 0) || (entry.col1 >= columnSpec.length) || 
	           (entry.col2 >= columnSpec.length) || (entry.row1 < 0) || 
	           (entry.row1 >= rowSpec.length) || 
	           (entry.row2 >= rowSpec.length)) {

	           continue;
	       }

	       size = entry.component.getMinimumSize();

	       int scalableWidth = size.width;
	       int scalableHeight = size.height;

	       for (counter = entry.col1; counter <= entry.col2; counter++) {

	           if (columnSpec[counter] >= 1.0) {
	               scalableWidth -= columnSpec[counter];
	           }
	       }

	       for (counter = entry.row1; counter <= entry.row2; counter++) {

	           if (rowSpec[counter] >= 1.0) {
	               scalableHeight -= rowSpec[counter];
	           }
	       }

	       double relativeWidth = 0.0;

	       for (counter = entry.col1; counter <= entry.col2; counter++) {

	           if ((columnSpec[counter] > 0.0) && 
	               (columnSpec[counter] < 1.0)) {
	               relativeWidth += columnSpec[counter];
	           } else if ((columnSpec[counter] == FILL) && 
	                      (fillWidthRatio != 0.0)) {
	               relativeWidth += fillWidthRatio;
	           }
	       }

	       if (relativeWidth == 0) {
	           temp = 0;
	       } else {
	           temp = (int)(scalableWidth / relativeWidth + 0.5);
	       }

	       if (scaledWidth < temp) {
	           scaledWidth = temp;
	       }

	       double relativeHeight = 0.0;

	       for (counter = entry.row1; counter <= entry.row2; counter++) {

	           if ((rowSpec[counter] > 0.0) && (rowSpec[counter] < 1.0)) {
	               relativeHeight += rowSpec[counter];
	           } else if ((rowSpec[counter] == FILL) && 
	                      (fillHeightRatio != 0.0)) {
	               relativeHeight += fillHeightRatio;
	           }
	       }

	       if (relativeHeight == 0) {
	           temp = 0;
	       } else {
	           temp = (int)(scalableHeight / relativeHeight + 0.5);
	       }

	       if (scaledHeight < temp) {
	           scaledHeight = temp;
	       }
	   }

	   int totalWidth = scaledWidth;

	   for (counter = 0; counter < columnSpec.length; counter++) {

	       if (columnSpec[counter] >= 1.0) {
	           totalWidth += (int)(columnSpec[counter] + 0.5);
	       } else if ((columnSpec[counter] == PREFERRED) || 
	                  (columnSpec[counter] == MINIMUM)) {

	           int maxWidth = 0;

	           iterator = list.listIterator(0);

	           while (iterator.hasNext()) {

	               Entry entry = iterator.next();

	               if ((entry.col1 == counter) && (entry.col2 == counter)) {

	                   Dimension p = (columnSpec[counter] == PREFERRED)
	                                     ? entry.component.getPreferredSize()
	                                     : entry.component.getMinimumSize();
	                   int width = (p == null) ? 0 : p.width;

	                   if (maxWidth < width) {
	                       maxWidth = width;
	                   }
	               }
	           }

	           totalWidth += maxWidth;
	       }
	   }

	   int totalHeight = scaledHeight;

	   for (counter = 0; counter < rowSpec.length; counter++) {

	       if (rowSpec[counter] >= 1.0) {
	           totalHeight += (int)(rowSpec[counter] + 0.5);
	       } else if ((rowSpec[counter] == PREFERRED) || 
	                  (rowSpec[counter] == MINIMUM)) {

	           int maxHeight = 0;

	           iterator = list.listIterator(0);

	           while (iterator.hasNext()) {

	               Entry entry = iterator.next();

	               if ((entry.row1 == counter) && (entry.row1 == counter)) {

	                   Dimension p = (rowSpec[counter] == PREFERRED)
	                                     ? entry.component.getPreferredSize()
	                                     : entry.component.getMinimumSize();
	                   int height = (p == null) ? 0 : p.height;

	                   if (maxHeight < height) {
	                       maxHeight = height;
	                   }
	               }
	           }

	           totalHeight += maxHeight;
	       }
	   }

	   Insets inset = container.getInsets();

	   totalWidth += inset.left + inset.right;
	   totalHeight += inset.top + inset.bottom;

	   return new Dimension(totalWidth, totalHeight);
	}

	/**
	 * Adds the specified component with the specified name to the layout.
	 *
	 * @param name         indicates entry's position and anchor
	 * @param component    component to add
	 */
	public void addLayoutComponent(String name, Component component) {
	   addLayoutComponent(component, name);
	}

   /**
    * Adds the specified component with the specified name to the layout.
    *
    * @param component    component to add
    * @param constraint   indicates entry's position and alignment
    */
   public void addLayoutComponent(Component component, Object constraint) {

	   if (constraint instanceof String) {
	       constraint = new TableLayoutConstraints((String)constraint);
	       list.add(new Entry(component, (TableLayoutConstraints)constraint));
	   } else if (constraint instanceof TableLayoutConstraints) {
	       list.add(new Entry(component, (TableLayoutConstraints)constraint));
	   } else if (constraint == null) {
	       throw new IllegalArgumentException("No constraint for the component");
	   } else {
	       throw new IllegalArgumentException("Cannot accept a constraint of class " + 
	                                          constraint.getClass());
	   }
	}

   /**
    * Removes the specified component from the layout.
    *
    * @param component    component being removed
    */
   public void removeLayoutComponent(Component component) {
	   list.remove(component);
   }

   /**
    * Returns the maximum dimensions for this layout given the components in the
    * specified target container.
    *
    * @param target the component which needs to be laid out
    *
    * @return unconditionally, a Dimension of Integer.MAX_VALUE by
    *         Integer.MAX_VALUE since TableLayout does not limit the
    *         maximum size of a container
    */
	public Dimension maximumLayoutSize(Container target) {

	   return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
   }

	/**
	 * Returns the alignment along the x axis.  This specifies how the component
	 * would like to be aligned relative to other components.  The value should
	 * be a number between 0 and 1 where 0 represents alignment along the origin,
	 * 1 is aligned the furthest away from the origin, 0.5 is centered, etc.
	 *
	 * @return unconditionally, 0.5
	 */
	public float getLayoutAlignmentX(Container parent) {

	   return 0.5f;
   }

	/**
	 * Returns the alignment along the y axis.  This specifies how the component
	 * would like to be aligned relative to other components.  The value should
	 * be a number between 0 and 1 where 0 represents alignment along the origin,
	 * 1 is aligned the furthest away from the origin, 0.5 is centered, etc.
	 *
	 * @return unconditionally, 0.5
	 */
	public float getLayoutAlignmentY(Container parent) {

	   return 0.5f;
   }

	/**
	 * Invalidates the layout, indicating that if the layout manager has cached
	 * information it should be discarded.
	 */
	public void invalidateLayout(Container target) {
	   dirty = true;
   }

    protected class Entry
	   extends TableLayoutConstraints {

	   /** Component bound by the constraints */
	   protected Component component;

	   /** Does the component occupy a single cell */
	   protected boolean singleCell;

	   /**
	    * Constructs an Entry that binds a component to a set of constraints.
	    *
	    * @param component     component being bound
	    * @param constraint    constraints being applied
	    */
	   public Entry(Component component, TableLayoutConstraints constraint) {
	       super(constraint.col1, constraint.row1, constraint.col2, 
	             constraint.row2, constraint.hAlign, constraint.vAlign);
	       singleCell = ((row1 == row2) && (col1 == col2));
	       this.component = component;
	   }

	   /**
	    * Determines whether or not two entries are equal.
	    *
	    * @param object    object being compared to; must be a Component if it
	    *                  is equal to this TableLayoutConstraints.
	    *
	    * @return    True, if the entries refer to the same component object.
	    *            False, otherwise.
	    */
	   public boolean equals(Object object) {

	       boolean equal = false;

	       if (object instanceof Component) {

	           Component component = (Component)object;

	           equal = (this.component == component);
	       }

	       return equal;
	   }
	}
}
