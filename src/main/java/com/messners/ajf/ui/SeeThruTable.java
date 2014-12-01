package com.messners.ajf.ui;

import java.awt.AlphaComposite;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;


/**
 * This class implements a SeeThruTable.
 *
 * @author  Greg Messner <gmessner@messners.com>
 */
public class SeeThruTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Composite seeThruComposite;
	private Renderer tableRenderer;
	private boolean seeThru = true;

	public SeeThruTable () {

		super();
		getTableHeader().setDefaultRenderer(new HeaderRenderer());
		tableRenderer = new Renderer();
		seeThruComposite = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.75f);
	}


	public SeeThruTable (TableModel model) {

		super(model);
		getTableHeader().setDefaultRenderer(new HeaderRenderer());
		tableRenderer = new Renderer();
		seeThruComposite = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.75f);
	}


	public void setSeeThru (boolean flag) {
		seeThru = flag;
	}


	public boolean isSeeThru () {
		return (seeThru);
	}


	public void paint (Graphics g) {

		if (!seeThru) {
			super.paint(g);
			return;
		}

		if (g instanceof Graphics2D) {

			Graphics2D g2d = (Graphics2D)g;
			Composite composite = g2d.getComposite();
			g2d.setComposite(seeThruComposite);
			super.paint(g2d);
			g2d.setComposite(composite);

		} else {

			super.paint(g);
		}
	}


	public TableCellRenderer getDefaultRenderer (Class<?> columnClass) {
		return (tableRenderer);
	}


	public Composite getSeeThruComposite () {
		return (seeThruComposite);
	}


	public void setSeeThruComposite (Composite composite) {
		seeThruComposite = composite;
	}


	public class Renderer extends DefaultTableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Renderer () {
			super();
		}

		public void paint (Graphics g) {

			if (!seeThru) {
				super.paint(g);
				return;
			}

			if (g instanceof Graphics2D) {

				Graphics2D g2d = (Graphics2D)g;
				Composite composite = g2d.getComposite();
				g2d.setComposite(seeThruComposite);
				super.paint(g2d);
				g2d.setComposite(composite);

			} else {
				super.paint(g);
			}
		}
	}


	private class HeaderRenderer extends DefaultTableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public HeaderRenderer () {

			super();
			setFont(UIManager.getFont("TableHeader.font"));
			setBackground(UIManager.getColor("TableHeader.background"));
			setForeground(UIManager.getColor("TableHeader.foreground"));
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			setHorizontalAlignment(SwingConstants.CENTER);
			setHorizontalTextPosition(SwingConstants.LEADING);
			setVerticalTextPosition(SwingConstants.CENTER);
		}

		public void paint (Graphics g) {

			if (!seeThru) {
				super.paint(g);
				return;
			}

			if (g instanceof Graphics2D) {

				Graphics2D g2d = (Graphics2D)g;
				Composite composite = g2d.getComposite();
				g2d.setComposite(seeThruComposite);
				super.paint(g2d);
				g2d.setComposite(composite);

			} else {
				super.paint(g);
			}
		}
	}
}

