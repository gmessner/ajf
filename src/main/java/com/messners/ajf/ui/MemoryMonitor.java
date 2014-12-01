package com.messners.ajf.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This component displays memory usage and history.
 * 
 * @author Greg Messner <greg@messners.com>
 */
public class MemoryMonitor extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * The Panel instance.
	 */
	protected Panel panel;

	/**
	 * Creates a new memory monitor frame. The time between two repaints is 1s.
	 */
	public MemoryMonitor() {
		this(1000);
	}

	/**
	 * Creates a new memory monitor frame.
	 * 
	 * @param time
	 *            The time between two repaints in ms.
	 */
	public MemoryMonitor(long time) {

		super("Memory Monitor");
		panel = new Panel(time);

		getContentPane().add(panel);
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Memory Usage & History"));

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton collectButton = new JButton("Collect");
		collectButton.addActionListener(new CollectButtonAction());
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new CloseButtonAction());
		p.add(collectButton);
		p.add(closeButton);
		getContentPane().add("South", p);

		pack();

		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				RepaintThread t = panel.getRepaintThread();
				if (!t.isAlive()) {
					t.start();
				} else {
					t.safeResume();
				}
			}

			public void windowClosing(WindowEvent ev) {
				panel.getRepaintThread().safeSuspend();
			}

			public void windowDeiconified(WindowEvent e) {
				panel.getRepaintThread().safeResume();
			}

			public void windowIconified(WindowEvent e) {
				panel.getRepaintThread().safeSuspend();
			}
		});
	}

	/**
	 * The action associated with the 'Collect' button of the memory monitor.
	 */
	protected class CollectButtonAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			System.gc();
		}
	}

	/**
	 * The action associated with the 'Close' button of the memory monitor.
	 */
	protected class CloseButtonAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			panel.getRepaintThread().safeSuspend();
			dispose();
		}
	}

	/**
	 * A panel composed of a Usage instance and a History instance.
	 */
	public static class Panel extends JPanel {

		private static final long serialVersionUID = 1L;

		/**
		 * The repaint thread.
		 */
		protected RepaintThread repaintThread;

		/**
		 * Creates a new memory monitor panel, composed of a Usage instance and
		 * a History instance. The time beetween two repaints is 1s.
		 */
		public Panel() {
			this(1000);
		}

		/**
		 * Creates a new memory monitor panel, composed of a Usage instance and
		 * a History instance. The repaint thread must be started by hands.
		 * 
		 * @param time
		 *            The time beetween two repaints in ms.
		 */
		public Panel(long time) {

			super(new GridBagLayout());

			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 5, 5, 5);

			List<JComponent> l = new ArrayList<JComponent>();
			JPanel p = new JPanel(new BorderLayout());
			p.setBorder(BorderFactory.createLoweredBevelBorder());
			JComponent c = new Usage();
			p.add(c);
			constraints.weightx = 0.3;
			constraints.weighty = 1;
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			add(p, constraints);
			l.add(c);

			p = new JPanel(new BorderLayout());
			p.setBorder(BorderFactory.createLoweredBevelBorder());
			c = new MemoryMonitor.History();
			p.add(c);
			constraints.weightx = 0.7;
			constraints.gridx = 1;
			constraints.gridy = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			add(p, constraints);
			l.add(c);

			repaintThread = new RepaintThread(time, l);
		}

		/**
		 * Returns the repaint thread.
		 */
		public RepaintThread getRepaintThread() {
			return repaintThread;
		}
	}

	/**
	 * Displays the current memory usage.
	 */
	public static class Usage extends JPanel implements MemoryChangeListener {

		private static final long serialVersionUID = 1L;

		/**
		 * The preferred width.
		 */
		public final static int PREFERRED_WIDTH = 90;

		/**
		 * The preferred height.
		 */
		public final static int PREFERRED_HEIGHT = 100;

		/**
		 * The units string.
		 */
		protected final static String UNITS = "K";

		/**
		 * The total string.
		 */
		protected final static String TOTAL = "total";

		/**
		 * The used string.
		 */
		protected final static String USED = "used";

		/**
		 * The text position.
		 */
		protected final static boolean POSTFIX = true;

		/**
		 * The font size.
		 */
		protected final static int FONT_SIZE = 9;

		/**
		 * The blocks margin.
		 */
		protected final static int BLOCK_MARGIN = 10;

		/**
		 * The number of blocks.
		 */
		protected final static int BLOCKS = 15;

		/**
		 * The blocks width.
		 */
		protected final static double BLOCK_WIDTH = PREFERRED_WIDTH
				- BLOCK_MARGIN * 2;

		/**
		 * The blocks height.
		 */
		protected final static double BLOCK_HEIGHT = ((double) PREFERRED_HEIGHT
				- (3 * FONT_SIZE) - BLOCKS)
				/ BLOCKS;

		/**
		 * The blocks type.
		 */
		protected final static int[] BLOCK_TYPE = { 0, 0, 0, 1, 1, 1, 1, 1, 2,
				2, 2, 2, 2, 2, 2 };

		/**
		 * The color of the used blocks for each block type.
		 */
		protected Color[] usedColors = { new Color(255, 0, 0),
				new Color(255, 165, 0), new Color(0, 255, 0) };

		/**
		 * The color of the free blocks for each block type.
		 */
		protected Color[] freeColors = { new Color(130, 0, 0),
				new Color(130, 90, 0), new Color(0, 130, 0) };

		/**
		 * The font used to draw the strings.
		 */
		protected Font font = new Font("SansSerif", Font.BOLD, FONT_SIZE);

		/**
		 * The text color.
		 */
		protected Color textColor = Color.green;

		/**
		 * The total memory.
		 */
		protected long totalMemory;

		/**
		 * The free memory.
		 */
		protected long freeMemory;

		/**
		 * Creates a new Usage object.
		 */
		public Usage() {
			this.setBackground(Color.black);
			setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		}

		/**
		 * Indicates that the memory state has changed.
		 * 
		 * @param total
		 *            The total amount of memory.
		 * @param free
		 *            The free memory.
		 */
		public void memoryStateChanged(long total, long free) {
			totalMemory = total;
			freeMemory = free;
		}

		/**
		 * Sets the text color.
		 */
		public void setTextColor(Color c) {
			textColor = c;
		}

		/**
		 * Sets the low used memory block color.
		 */
		public void setLowUsedMemoryColor(Color c) {
			usedColors[2] = c;
		}

		/**
		 * Sets the medium used memory block color.
		 */
		public void setMediumUsedMemoryColor(Color c) {
			usedColors[1] = c;
		}

		/**
		 * Sets the high used memory block color.
		 */
		public void setHighUsedMemoryColor(Color c) {
			usedColors[0] = c;
		}

		/**
		 * Sets the low free memory block color.
		 */
		public void setLowFreeMemoryColor(Color c) {
			freeColors[2] = c;
		}

		/**
		 * Sets the medium free memory block color.
		 */
		public void setMediumFreeMemoryColor(Color c) {
			freeColors[1] = c;
		}

		/**
		 * Sets the high free memory block color.
		 */
		public void setHighFreeMemoryColor(Color c) {
			freeColors[0] = c;
		}

		/**
		 * To paint the component.
		 */
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			// Sets the transform
			Dimension dim = getSize();
			double sx = ((double) dim.width) / PREFERRED_WIDTH;
			double sy = ((double) dim.height) / PREFERRED_HEIGHT;
			g2d.transform(AffineTransform.getScaleInstance(sx, sy));

			// Turns the antialiasing on
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// Draw the memory blocks
			int nfree = (int) Math.round(((double) BLOCKS) * freeMemory
					/ totalMemory);

			for (int i = 0; i < nfree; i++) {
				Rectangle2D rect = new Rectangle2D.Double(10, i * BLOCK_HEIGHT
						+ i + FONT_SIZE + 5, BLOCK_WIDTH, BLOCK_HEIGHT);
				g2d.setPaint(freeColors[BLOCK_TYPE[i]]);
				g2d.fill(rect);
			}

			for (int i = nfree; i < 15; i++) {
				Rectangle2D rect = new Rectangle2D.Double(10, i * BLOCK_HEIGHT
						+ i + FONT_SIZE + 5, BLOCK_WIDTH, BLOCK_HEIGHT);
				g2d.setPaint(usedColors[BLOCK_TYPE[i]]);
				g2d.fill(rect);
			}

			// Draw the memory usage text
			g2d.setPaint(textColor);
			g2d.setFont(font);

			long total = totalMemory / 1024;
			long used = (totalMemory - freeMemory) / 1024;
			String totalText;
			String usedText;
			if (POSTFIX) {
				totalText = total + UNITS + " " + TOTAL;
				usedText = used + UNITS + " " + USED;
			} else {
				totalText = TOTAL + " " + total + UNITS;
				usedText = USED + " " + used + UNITS;
			}

			g2d.drawString(totalText, 10, 10);
			g2d.drawString(usedText, 10, PREFERRED_HEIGHT - 3);
		}
	}

	/**
	 * Displays the memory usage history in a chart.
	 */
	public static class History extends JPanel implements MemoryChangeListener {

		private static final long serialVersionUID = 1L;

		/**
		 * The preferred width.
		 */
		public final static int PREFERRED_WIDTH = 200;

		/**
		 * The preferred height.
		 */
		public final static int PREFERRED_HEIGHT = 100;

		/**
		 * The grid lines stroke.
		 */
		protected final static Stroke GRID_LINES_STROKE = new BasicStroke(1);

		/**
		 * The curve stroke.
		 */
		protected final static Stroke CURVE_STROKE = new BasicStroke(2,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

		/**
		 * The border stroke.
		 */
		protected final static Stroke BORDER_STROKE = new BasicStroke(2);

		/**
		 * The grid lines color.
		 */
		protected Color gridLinesColor = new Color(0, 130, 0);

		/**
		 * The curve color.
		 */
		protected Color curveColor = Color.yellow;

		/**
		 * The border color.
		 */
		protected Color borderColor = Color.green;

		/**
		 * The data.
		 */
		protected List<Long> data = new LinkedList<Long>();

		/**
		 * The vertical lines shift.
		 */
		protected int xShift = 0;

		/**
		 * The total memory.
		 */
		protected long totalMemory;

		/**
		 * The free memory.
		 */
		protected long freeMemory;

		/**
		 * The curve representation.
		 */
		protected GeneralPath path = new GeneralPath();

		/**
		 * Creates a new History object.
		 */
		public History() {
			this.setBackground(Color.black);
			setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		}

		/**
		 * Indicates that the memory state has changed.
		 * 
		 * @param total
		 *            The total amount of memory.
		 * @param free
		 *            The free memory.
		 */
		public void memoryStateChanged(long total, long free) {
			totalMemory = total;
			freeMemory = free;

			// Add a new point to the data
			data.add(new Long(totalMemory - freeMemory));
			if (data.size() > 190) {
				data.remove(0);
				xShift = (xShift + 1) % 10;
			}

			// Create the path that match the data
			Iterator<Long> it = data.iterator();
			GeneralPath p = new GeneralPath();
			long l = it.next().longValue();
			p.moveTo(5, ((float) (totalMemory - l) / totalMemory) * 80 + 10);
			int i = 6;
			while (it.hasNext()) {
				l = it.next().longValue();
				p.lineTo(i, ((float) (totalMemory - l) / totalMemory) * 80 + 10);
				i++;
			}
			path = p;
		}

		/**
		 * To paint the component.
		 */
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			// Turns the antialiasing on
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// Sets the transform
			Dimension dim = getSize();
			double sx = ((double) dim.width) / PREFERRED_WIDTH;
			double sy = ((double) dim.height) / PREFERRED_HEIGHT;
			g2d.transform(AffineTransform.getScaleInstance(sx, sy));

			// The vertical lines
			g2d.setPaint(gridLinesColor);
			g2d.setStroke(GRID_LINES_STROKE);
			for (int i = 1; i < 20; i++) {
				int lx = i * 10 + 5 - xShift;
				g2d.draw(new Line2D.Double(lx, 5, lx, PREFERRED_HEIGHT - 5));
			}

			// The horizontal lines
			for (int i = 1; i < 9; i++) {
				int ly = i * 10 + 5;
				g2d.draw(new Line2D.Double(5, ly, PREFERRED_WIDTH - 5, ly));
			}

			// The curve.
			g2d.setPaint(curveColor);
			g2d.setStroke(CURVE_STROKE);

			g2d.draw(path);

			// The border
			g2d.setStroke(BORDER_STROKE);
			g2d.setPaint(borderColor);
			g2d.draw(new Rectangle2D.Double(5, 5, PREFERRED_WIDTH - 10,
					PREFERRED_HEIGHT - 10));

		}
	}

	/**
	 * This interface allows the RepaintThread to notify an object that the
	 * current memory state has changed.
	 */
	public interface MemoryChangeListener {
		/**
		 * Indicates that the memory state has changed.
		 * 
		 * @param total
		 *            The total amount of memory.
		 * @param free
		 *            The free memory.
		 */
		void memoryStateChanged(long total, long free);
	}

	/**
	 * This thread repaints a list of components.
	 */
	public static class RepaintThread extends Thread {
		/**
		 * The repaint timeout
		 */
		protected long timeout;

		/**
		 * The components to repaint.
		 */
		protected List<JComponent> components;

		/**
		 * The runtime.
		 */
		protected Runtime runtime = Runtime.getRuntime();

		/**
		 * Whether or not the thread was supended.
		 */
		protected boolean suspended;

		/**
		 * Creates a new Thread.
		 * 
		 * @param timeout
		 *            The time between two repaint in ms.
		 * @param components
		 *            The components to repaint.
		 */
		public RepaintThread(long timeout, List<JComponent> components) {
			this.timeout = timeout;
			this.components = components;
			setPriority(Thread.MIN_PRIORITY);
		}

		/**
		 * The thread main method.
		 */
		public void run() {
			for (;;) {
				long free = runtime.freeMemory();
				long total = runtime.totalMemory();
				Iterator<JComponent> it = components.iterator();
				while (it.hasNext()) {
					Component c = it.next();
					((MemoryChangeListener) c).memoryStateChanged(total, free);
					c.repaint();
				}
				try {
					sleep(timeout);
					if (suspended) {
						synchronized (this) {
							while (suspended) {
								wait();
							}
						}
					}
				} catch (InterruptedException e) {
				}
			}
		}

		/**
		 * Suspends the thread.
		 */
		public void safeSuspend() {
			if (!suspended) {
				suspended = true;
			}
		}

		/**
		 * Resumes the thread.
		 */
		public synchronized void safeResume() {
			if (suspended) {
				suspended = false;
				notify();
			}
		}
	}
}
