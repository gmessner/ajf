package com.messners.ajf.ui.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;


/**
 * This class provides a strip chart display with bar readouts for displaying
 * 1 or more traces of data.  It is modeled after the chart and bar graph found
 * on the Windows task manager.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class StripChart extends JComponent implements Runnable {

	private static final long serialVersionUID = 1L;
	private Font font;
	private Color textColor;
	private int ascent;
	private int descent;
	private int textMargin = 4;
	private int yMinimumTextWidth;
	private String yMinimumText = "0.0";
	private int yMaximumTextWidth;
	private String yMaximumText = "10.0";
	private int yLabelTextWidth;
	private String yLabelTextLine1 = null;
	private String yLabelTextLine2 = null;
	private int fixedTextWidth = -1;

	private boolean autoScale = false;
	private float autoScaleIncrement = 1.0f;
	private DecimalFormat yTextFormatter;

	private BufferedImage image;
	private Graphics2D imageGraphics;
	private int width = 0;
	private int height = 0;
	private int chartXOffset;
	private int chartYOffset;
	private int barLabelOffset;

	private float yMaximum = 10.0f;
	private int gridOffset = 0;
	private Color gridColor;
	private Color chartColor;

	private float gridHeight = 12.0f;
	private float gridWidth = 12.0f;
	private int minGridLines = 5;

	private ArrayList<DataSet> dataSets;

	private Thread privateWorkThread;
	private int sleepMillis = 1000;
	private boolean started = false;
	private boolean isMember = false;

	private static Thread groupWorkThread;
	private static int groupSleepMillis = 1000;
	private static List<StripChart> groupCharts;
	static {
		groupCharts = Collections.synchronizedList(
						new ArrayList<StripChart>());
	}

	private int numBlocks;
	private float blockWidth  = 16;
	private float blockHeight = 6;
	private int blockSpace    = 2;
	private int blockMargin   = 5;

	private Rectangle2D chartRect  = new Rectangle2D.Float();
	private Rectangle2D noDataRect = new Rectangle2D.Float();
	private Rectangle2D dataRect   = new Rectangle2D.Float();

	private ArrayList<ChartListener> listeners;
	private float lastDataValues[];
	private ChartDataEvent chartDataEvent;
	private ChartDataProvider dataProvider;
	
	private Object extraData;


	/**
	 * The border stroke.
	 */
	protected final static Stroke BORDER_STROKE = new BasicStroke(2);

	/**
	 * The curve stroke.
	 */
	protected final static Stroke CURVE_STROKE =
	    new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	/**
	 * The grid lines stroke.
	 */
	protected final static Stroke GRID_LINES_STROKE = new BasicStroke(.1f);


	/**
	 * Creates an instance using the specified ChartDataProvider.
	 *
	 * @param provider the ChartDataProvider that will provide data to the chart
	 */
	public StripChart (ChartDataProvider provider) {

		dataProvider = provider;
		font = new Font("SansSerif", Font.PLAIN, 12);
		dataSets   = new ArrayList<DataSet>();
		listeners  = new ArrayList<ChartListener>();
		chartColor = Color.white;
		textColor  = Color.black;
		gridColor  = Color.lightGray;
		
		setPreferredSize(new Dimension(160, 96));
	}


	/**
	 * Default no-args constructor.
	 */
	public StripChart () {
		this(null);
	}
	
	/**
	 * Get the extra data associated with this HistoryChart.
	 * 
	 * @return the extra data associated with this HistoryChart instance
	 */
	public Object getExtraData () {
		return (extraData);
	}

	/**
	 * Set the extra data associated with this HistoryChart instance.
	 * 
	 * @param extraData the extra data to associate with this HistoryChart instance
	 */
	public void setExtraData (Object extraData) {
		this.extraData = extraData;
	}
	
	public static void addGroupChart (StripChart chart) {

		synchronized (groupCharts) {
	
			chart.isMember = true;
			groupCharts.add(chart);

			if (groupWorkThread == null) {
				groupWorkThread = new Thread(new GroupThread());
				groupWorkThread.start();
			}
		}
	}


	public static void removeGroupChart (StripChart chart) {

		synchronized (groupCharts) {
	
			chart.isMember = false;
			groupCharts.remove(chart);
		}
	}


	protected void paintComponent (Graphics g) {

		if (image == null) {
			return;
		}

		/*
		 * Clear the buffered image
		 */
		Color bg = getBackground();
		imageGraphics.setBackground(bg);
		imageGraphics.clearRect(0, 0, width, height);

		/*
		 * Calculate the maximum x point on the chart
		 */
		int numDataSets = dataSets.size();
		float maxX = (float)(width - blockMargin -
				(blockWidth * numDataSets) - 
				(blockSpace * (numDataSets - 1)));


		/*
		 * Paint the chart backgound if it is different than the component's
		 */
		if (!bg.equals(chartColor)) {

			chartRect.setRect(chartXOffset, chartYOffset, maxX - chartXOffset, 
				height - chartYOffset * 2 - barLabelOffset);
			imageGraphics.setPaint(chartColor);
			imageGraphics.fill(chartRect);
		}

		/*
		 * Draw the Y Axis minimum label
		 */
	    imageGraphics.setColor(textColor);
		if (yMinimumText != null) {

			imageGraphics.drawString(yMinimumText,
				chartXOffset - textMargin - yMinimumTextWidth,
				height - descent - barLabelOffset);
		}

		/*
		 * Draw the Y Axis label
		 */
		if (yLabelTextLine1 != null && yLabelTextLine2 != null) {

			imageGraphics.drawString(yLabelTextLine1,
				chartXOffset - textMargin - yLabelTextWidth,
				(height / 2) - descent - barLabelOffset);

			imageGraphics.drawString(yLabelTextLine2,
				chartXOffset - textMargin - yLabelTextWidth,
				height / 2  + ascent - barLabelOffset);

		} else if (yLabelTextLine1 != null) {

			imageGraphics.drawString(yLabelTextLine1,
				chartXOffset - textMargin - yLabelTextWidth,
				height / 2 - (descent + ascent) / 2 - barLabelOffset);
		}

		/*
		 * Draw the Y Axis maximum label
		 */
		if (yMaximumText != null) {

			imageGraphics.drawString(yMaximumText,
				chartXOffset - textMargin - yMaximumTextWidth,
				ascent);
		}

		/*
		 * Draw the labels for the bars
		 */
		for (int i = 0; i < numDataSets; i++) {

			DataSet dataSet = getDataSet(i);
			String bl = dataSet.barLabel;

			if (bl != null) {

				int x = (int)(width - (blockWidth * (numDataSets - i)) - 
					(blockSpace * (numDataSets - i - 1)) +
					blockWidth / 2 - dataSet.barLabelXOffset);
				imageGraphics.drawString(bl, x, height - descent);
			}
		}

		/*
		 * Draw the Y axis grid lines
		 */
		Line2D.Float line = new Line2D.Float();
	    imageGraphics.setPaint(gridColor);
	    imageGraphics.setStroke(GRID_LINES_STROKE);
		float y = height - gridHeight - chartYOffset - barLabelOffset;
		while (y > chartYOffset) {
			
			line.setLine(chartXOffset, y, maxX, y);
			imageGraphics.draw(line);
			y -= gridHeight;
	    }

		/*
		 * Draw the X axis grid lines
		 */
		y = height - chartYOffset - barLabelOffset;
		int x = (int)maxX - gridOffset;
	    while (x > chartXOffset) {

			line.setLine(x, chartYOffset, x, y);
			imageGraphics.draw(line);
			x -= gridWidth;
	    }

		/*
		 * Paint the chart and bar for each DataSet
		 */
		for (int i = 0; i < numDataSets; i++) {
			paintDataSet(i);
		}

		/*
		 * Draw a rectangle border around the chart to clean it up
		 */
	    imageGraphics.setColor(bg);
	    imageGraphics.setStroke(BORDER_STROKE);
		imageGraphics.draw3DRect(chartXOffset, chartYOffset,
				(int)maxX - chartXOffset - 1,
				height - chartYOffset * 2 - barLabelOffset,
				false);


		/*
		 * And finally copy the buffered image to the screen
		 */
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(image, 0, 0, this);
	}


	/**
	 * Paints both the chart and bar for a particular DataSet instance
	 * which is specified by dataSetIndex.
	 *
	 * @param  dataSetIndex  the index of the DataSet to paint
	 */
	protected void paintDataSet (int dataSetIndex) {

		int numDataSets = dataSets.size();
		DataSet dataSet = getDataSet(dataSetIndex);

		imageGraphics.clip(chartRect);
	    imageGraphics.setPaint(dataSet.dataColor);
	    imageGraphics.setStroke(CURVE_STROKE);
	    imageGraphics.draw(dataSet.dataPath);
		imageGraphics.setClip(null);

		int currentValue = (int)((dataSet.lastData / yMaximum) * numBlocks);
		if (currentValue == 0) {
			currentValue = -1;
		}

		int x = (int)(width - (blockWidth * (numDataSets - dataSetIndex)) - 
				(blockSpace * (numDataSets - dataSetIndex - 1)));


	    imageGraphics.setColor(dataSet.dataColor);
		int y = height - chartYOffset - barLabelOffset - (int)blockHeight;
		int i = 0;
		for ( ;i <= currentValue; i++) {

			dataRect.setRect(x, y, blockWidth, blockHeight - 1);
			y -= blockHeight;
			imageGraphics.fill(dataRect);
		}

		imageGraphics.setColor(dataSet.noDataColor);
		for ( ; i < numBlocks; i++) {

			noDataRect.setRect(x, y, blockWidth, blockHeight - 1);
			y -= blockHeight;
			imageGraphics.fill(noDataRect);
		}
	}


	/**
	 * Turns on/off the auto-scale functionality.
	 *
	 * @param  autoScale  the new auto-scale flag
	 */
	public void setAutoScale (boolean autoScale) {

		this.autoScale = autoScale;
		if (autoScale && yTextFormatter == null) {
			setYTextFormat("########0.0");
		}
	}


	/**
	 * Sets the minimum increment to use when auto-scaling.
	 *
	 * @param  autoScaleIncrement  the new auto-scale increment
	 */
	public void setAutoScaleIncrement (float autoScaleIncrement) {
		this.autoScaleIncrement = autoScaleIncrement;
	}


	public void setYTextFormat (String format) {
		yTextFormatter = new DecimalFormat(format);
	}


	/**
	 * Sets the width of the Y axis text labels to the width of the specified
	 * String.  This is useful when auto-scale is being used so that the chart
	 * area does not shift due to the Y axis text changing.
	 *
	 * @param  text  the String to set the fixed width using the width
	 *               of the drawn String
	 */
	public void setFixedYTextWidth (String text) {

		if (text == null) {
			fixedTextWidth = -1;
			return;
		}

		FontMetrics fm;
		Graphics g;
		if (imageGraphics == null) {
       	
			BufferedImage image = new BufferedImage(
				1, 1, BufferedImage.TYPE_INT_RGB);
	       	g = image.createGraphics();
   	    	g.setFont(font);

		} else {

			g = imageGraphics;
		}

		fm = g.getFontMetrics(font);

		Rectangle2D bounds = fm.getStringBounds(text, g);
		fixedTextWidth = (int)bounds.getWidth();

		if (privateWorkThread != null && imageGraphics != null) {
			computeSizes(getSize());
		}
	}


	/**
	 * Sets the width of each block in the bar graph.
	 *
	 * @param  blockWidth the new block width
	 */
	public void setBlockWidth (int blockWidth) {

		this.blockWidth = blockWidth;

		if (privateWorkThread != null && imageGraphics != null) {
			computeSizes(getSize());
		}
	}


	/**
	 * Sets the height of each block in the bar graph.
	 *
	 * @param  blockHeight  the new block height
	 */
	public void setBlockHeight (int blockHeight) {

		this.blockHeight = blockHeight;
	}


	/**
	 * Sets the update rate for the chart.
	 *
	 * @param millis the update rate in milli seconds
	 */
	public void setUpdateRate (int millis) {
		sleepMillis = millis;
	}


	/**
	 * Sets the Color of the chart background.
	 *
	 * @param  chartColor  the new Color to paint the chart background with
	 */
	public void setChartColor (Color chartColor) {
		this.chartColor = chartColor;
	}


	/**
	 * Sets the Color to use for the chart grid.
	 *
	 * @param  gridColor  the new Color to paint the grid with
	 */
	public void setGridColor (Color gridColor) {
		this.gridColor = gridColor;
	}


	/**
	 * Sets the Color to use for the Y axis labels.
	 *
	 * @param  textColor  the new Color to paint the text with
	 */
	public void setTextColor (Color textColor) {
		this.textColor = textColor;
	}


	/**
	 * Sets the width of a cell in the grid.
	 *
	 * @param  gridWidth the new width for a cell in the grid
	 */
	public void setGridWidth (float gridWidth) {
		this.gridWidth = gridWidth;
	}


	/**
	 * Sets the height of a cell in the grid.
	 *
	 * @param  gridHeight  the new height for a cell in the grid
	 */
	public void setGridHeight (float gridHeight) {
		this.gridHeight = gridHeight;
	}


	/**
	 * Sets the minimum number of grids lines. Used to compute the
	 * preferred size for theis instance.
	 *
	 * @param  minGridLines the new value for the minimum number of grids lines
	 */
	public void setMinGridLines (int minGridLines) {
		this.minGridLines = minGridLines;
	}


	/**
	 * Sets the maximum value for the Y axis.  The minimum value is always 0.
	 *
	 * @param  yMaximum  the maximum value for the Y axis
	 */
	public void setYMaximumValue (float yMaximum) {
		this.yMaximum = yMaximum;
		computeSizes(getSize());
	}


	/**
	 * Sets the text to display for the maximum value of the Y axis.
	 *
	 * @param  text  the text for the maximum value of the Y axis
	 */
	public void setYMaximumValueText (String text) {
		yMaximumText = text;
		computeSizes(getSize());
	}


	/**
	 * Sets the text to display for the minimum value of the Y axis.
	 *
	 * @param  text  the text for the minimum value of the Y axis
	 */
	public void setYMinimumValueText (String text) {
		yMinimumText = text;
		computeSizes(getSize());
	}


	/**
	 * Sets the text to display for the label of the Y axis.
	 *
	 * @param  text  the text for the label of the Y axis
	 */
	public void setYLabelText (String text) {

		int index = text.indexOf('\n');
		if (index > 0) {

			yLabelTextLine1 = text.substring(0, index);
			yLabelTextLine2 = text.substring(index + 1);

		} else {

			yLabelTextLine1 = text;
			yLabelTextLine2 = null;
		}
	
		computeSizes(getSize());
	}


	/**
	 * Gets the ChartDataProvider that provides data to this chart.
	 *
	 * @return the ChartDataProvider that provides data to the chart
	 */
	public ChartDataProvider getDataProvider () {
		return (dataProvider);
	}


	/**
	 * Sets the ChartDataProvider that will provide data to this chart.
	 *
	 * @param provider the ChartDataProvider that will provide data to the chart
	 */
	public void setDataProvider (ChartDataProvider provider) {
		dataProvider = provider;
	}


	/**
	 * Clears the data from all the DataSets and chart.
	 */
	public void clear () {

		int numDataSets = dataSets.size();
		DataSet dataSet;
		for (int i = 0; i < numDataSets; i++) {

			dataSet = getDataSet(i);

			dataSet.clear();
			dataSet.dataPath = new GeneralPath();
			dataSet.maxData  = 0.0f;
			dataSet.lastData = 0.0f;
		}

		if (imageGraphics != null) {
			repaint();
		}
	}


	/**
	 * Adds a DataSet to the chart.
	 *
	 * @param  dataColor  the color to use for charting this DataSet
	 * @return the created DataSet instance
	 */
	public DataSet addDataSet (String barLabel, Color dataColor) {

		DataSet dataSet = new DataSet(barLabel, dataColor);
		dataSets.add(dataSet);

		/*
		 * Create the event object we do this now so we don't actually
		 * allocate a new event each time
		 */
		lastDataValues = new float[dataSets.size()];
		chartDataEvent = new ChartDataEvent(this, lastDataValues);
		
		return (dataSet);
	}


	/**
	 * Adds a DataSet to the chart.
	 *
	 * @param  dataColor  the color to use for charting this DataSet
	 * @return the created DataSet instance
	 */
	public DataSet addDataSet (Color dataColor) {
		return (addDataSet(null, dataColor));
	}


	/**
	 * Adds a data point to the trace of the DataSet specified by
	 * dataSetIndex.
	 *
	 * @param  dataSetIndex  the index of the DataSet to add the data point to
	 * @param  value         the value for the data point to add
	 */
	public void addData (int dataSetIndex, float value) {

		DataSet dataSet = getDataSet(dataSetIndex);
		dataSet.lastData = value;
		lastDataValues[dataSetIndex] = value;
		dataSet.add(new Float(value));

		if (width <= 0) {
			dataSet.dataPath = new GeneralPath();
			return;
		}

		int numDataSets = dataSets.size();
		int maxPoints = (int)(width - chartXOffset - blockMargin -
				(blockWidth * numDataSets) - 
				(blockSpace * (numDataSets - 1)));
		maxPoints = Math.max(maxPoints, 0);

		int dataSize = dataSet.size();
		while (dataSize > maxPoints) {
			dataSet.removeFirst();
			dataSize--;
		}

		Iterator<Float> iterator = dataSet.iterator();
		if (!isShowing() || !iterator.hasNext()) {	
			dataSet.dataPath = new GeneralPath();
			return;
		}

		int x = maxPoints - dataSize + chartXOffset;
	    float sy = (float)(height - 2 - chartYOffset * 2 -
						barLabelOffset) / yMaximum;

		float y = ((Float)iterator.next()).floatValue();
		float chartY0 = height - chartYOffset - barLabelOffset;
		GeneralPath path = new GeneralPath();
		path.moveTo(x, chartY0 - y * sy);

		float max = 0.0f;

		while (iterator.hasNext()) {	

			x++;
			y = ((Float)iterator.next()).floatValue();
			path.lineTo(x, chartY0 - y * sy);

			if (y > max) {
				max = y;
			}
		}

		dataSet.maxData = max;
		dataSet.dataPath = path;
	}


	/**
	 * Checks to see if the chart needs to be auto scaled.  An auto scale
	 * will be done if the data exceeds the maximum Y axis value or if the
	 * max data value is 50% below the current maximum Y axis value.
	 */
	private void autoScaleIfNeeded () {

		if (!autoScale) {
			return;
		}

		/*
		 * Go thru each of the DataSets to looking for the maximum value
		 */
		float max = 0.0f;
		int numDataSets = dataSets.size();
		DataSet dataSet;
		for (int i = 0; i < numDataSets; i++) {

			dataSet = getDataSet(i);
			if (dataSet.maxData > max) {
				max = dataSet.maxData;
			}
		}


		/*
		 * Don't autoscale if the max is 0
		 */
		if (max == 0.0f) {
			return;
		}


		/*
		 * Do we need to auto scale?
		 */
		if (max > yMaximum || max < yMaximum / 2.0f) {

			float newYMaximum = (float)Math.round(max + .5f);
			if (newYMaximum > 0.0f && newYMaximum != yMaximum) {

				float oldMax = yMaximum;

				float leftover = newYMaximum % autoScaleIncrement;
				yMaximum = newYMaximum + (autoScaleIncrement - leftover);
				setYMaximumValueText(yTextFormatter.format(yMaximum));

				fireAutoScaleEvent(yMaximum, oldMax);

					
				for (int i = 0; i < numDataSets; i++) {

					dataSet = getDataSet(i);
					scaleDataSet(dataSet);
				}
			}
		}
	}


	/**
	 * Scale the DataSet to the new Y maximum value.
	 */
	private void scaleDataSet (DataSet dataSet) {

		int numDataSets = dataSets.size();
		int maxPoints = (int)(width - chartXOffset - blockMargin -
				(blockWidth * numDataSets) - 
				(blockSpace * (numDataSets - 1)));

		int dataSize = dataSet.size();
		int x = maxPoints - dataSize + chartXOffset;
	    float sy = (float)(height - 2 - chartYOffset * 2 -
						barLabelOffset) / yMaximum;

		Iterator<Float> iterator = dataSet.iterator();
		if (!isShowing() || !iterator.hasNext()) {	
			dataSet.dataPath = new GeneralPath();
			return;
		}

		float y = ((Float)iterator.next()).floatValue();
		float chartY0 = height - chartYOffset - barLabelOffset;
		GeneralPath path = new GeneralPath();
		path.moveTo(x, chartY0 - y * sy);

		while (iterator.hasNext()) {	

			x++;
			y = ((Float)iterator.next()).floatValue();
			path.lineTo(x, chartY0 - y * sy);
		}

		dataSet.dataPath = path;
	}


	/**
	 * Gets the DataSet at the specified index.
	 *
	 * @param  dataSetIndex  the index of the DataSet to fetch
	 * @return  the DataSet at the specified index
	 */
	protected DataSet getDataSet (int dataSetIndex) {
		return dataSets.get(dataSetIndex);
	}


	/**
	 * Starts the charting of data. 
	 */
	public void start () {

		if (isMember) {
			started = true;
			return;
		}

		if (privateWorkThread != null) {
			return;
		}

		started = true;
		privateWorkThread = new Thread(this);
		privateWorkThread.setPriority(Thread.MIN_PRIORITY);
		privateWorkThread.setName("HistoryChart");
		privateWorkThread.start();
	}


	/**
	 * Stops the charting of data.
	 */
   public synchronized void stop () {

		if (isMember) {
			started = false;
			return;
		}

		if (privateWorkThread == null) {
			return;
		}

		started = false;
		Thread t = privateWorkThread;
   		privateWorkThread = null;
   		t.notify();
   	}


    public void run () {

    	Thread me = Thread.currentThread();

       	while (privateWorkThread == me && getSize().width <= 0) {

			try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
				return;
			}
		}
    
        while (privateWorkThread == me) {


			processChart(this);	
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
				break;
            }
		}

        privateWorkThread = null;
    }


	public static void setGroupUpdateRate (int millis) {
		groupSleepMillis = millis;
	}


	private static class GroupThread implements Runnable {

	    public void run () {

   		 	Thread me = Thread.currentThread();
        	while (groupWorkThread == me) {

				synchronized (groupCharts) {

					int numCharts = groupCharts.size();
					for (int i = 0; i < numCharts; i++) {
						processChart(groupCharts.get(i));	
					}
				}

            	try {
					Thread.sleep(groupSleepMillis);
				} catch (InterruptedException e) {
					break;
            	}
			}

        	groupWorkThread = null;
		}
    }


	private static void processChart (StripChart chart) {

		if (!chart.started) {
			return;
		}

		if (chart.dataProvider != null) {
			chart.dataProvider.provideData(chart);
		}

		Dimension size = chart.getSize();
   	    if (size.width <= 0) {
			return;
		}

    	if (size.width != chart.width || size.height != chart.height) {

			chart.computeSizes(size);
			if (chart.width <= 0) {
				return;
			}
		}

		chart.gridOffset++;
		if (chart.gridOffset > chart.gridWidth) {
			chart.gridOffset = 1;
		}

		if (chart.isShowing()) {

			chart.autoScaleIfNeeded();
   			chart.repaint();
			chart.fireChartDataEvent();
		}
    }


	public Dimension getPreferredSize () {

		if (chartYOffset == 0) {
			computeFontOffsets();
		}

		int numDataSets = dataSets.size();
		Dimension d = new Dimension();
		d.width = (int)(chartXOffset + minGridLines * gridWidth + blockMargin + 
				blockSpace * (numDataSets - 1) + blockWidth * numDataSets);
		d.height = (int)(chartYOffset * 2 + barLabelOffset +
				minGridLines * gridHeight);
		return (d);
	}


	/**
	 * Computes the values for margins and offsets associated
	 * with the Y axis labels.
	 */
	private final void computeFontOffsets () {

		Graphics2D g;
		if (imageGraphics == null) {
       	
			BufferedImage image = new BufferedImage(
				1, 1, BufferedImage.TYPE_INT_RGB);
	       	g = image.createGraphics();
   	    	g.setFont(font);

		} else {
			g = imageGraphics;
		}

		FontMetrics fm = g.getFontMetrics(font);
        ascent = (int)fm.getAscent();
        descent = (int)fm.getDescent();
		chartYOffset = (int)((ascent + descent + .5f) / 2);

		Rectangle2D bounds;
		barLabelOffset = 0;
		int numDataSets = dataSets.size();
		DataSet dataSet;
		for (int i = 0; i < numDataSets; i++) {

			dataSet = getDataSet(i);
			if (dataSet.barLabel != null) {
				barLabelOffset = chartYOffset;
				bounds = fm.getStringBounds(dataSet.barLabel, imageGraphics);
				dataSet.barLabelXOffset = (int)bounds.getWidth() / 2;
			}
		}

		if (yMinimumText != null) {
			bounds = fm.getStringBounds(yMinimumText, imageGraphics);
			yMinimumTextWidth = (int)bounds.getWidth();
		} else {
			yMinimumTextWidth = 0;
		}

		if (yMaximumText != null) {
			bounds = fm.getStringBounds(yMaximumText, imageGraphics);
			yMaximumTextWidth = (int)bounds.getWidth();
		} else {
			yMaximumTextWidth = 0;
		}

		if (yLabelTextLine1 != null) {

			bounds = fm.getStringBounds(yLabelTextLine1, imageGraphics);
			yLabelTextWidth = (int)bounds.getWidth();

			if (yLabelTextLine2 != null) {

				bounds = fm.getStringBounds(yLabelTextLine2, imageGraphics);
				int n = (int)bounds.getWidth();
				yLabelTextWidth = Math.max(n, yLabelTextWidth);
			}

		} else {
			yLabelTextWidth = 0;
		}


		if (fixedTextWidth > 0) {

			chartXOffset = fixedTextWidth + textMargin;

		} else {

			chartXOffset = Math.max(yMinimumTextWidth, yMaximumTextWidth);
			chartXOffset = Math.max(yLabelTextWidth, chartXOffset);
			chartXOffset += textMargin;
		}
	}


	/**
	 * Computes sizes for the chart.
	 *
	 * @param  size  the physical size of the chart on screen
	 */
	private void computeSizes (Dimension size) {

		if (size.height <= 0 || size.width <= 0) {
			width = height = 0;
			image = null;
			return;
		}

       	image = (BufferedImage)createImage(size.width, size.height);
		if (image == null) {
			width = height = 0;
			return;
		}

       	width  = size.width;
       	height = size.height;

       	imageGraphics = image.createGraphics();
       	imageGraphics.setFont(font);

		computeFontOffsets();

		numBlocks = (height - chartYOffset * 2 -
					barLabelOffset) / (int)blockHeight;
   	}


	/**
	 * Adds a listener for new data events. This event is fired after
	 * each repaint of the chart.
	 *
	 * @param  l  the listener to add
	 */
	public synchronized void addListener (ChartListener l) {

		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}


	/**
	 * Removes a listener for new data events.
	 *
	 * @param  l  the listener to remove
	 */
	public synchronized void removeListener (ChartListener l) {
		listeners.remove(l);
	}


	/**
	 * Fires a newChartData() event.
	 */
	public void fireChartDataEvent () {

		int count = listeners.size();
		if (count < 1) {
			return;
		}

		/*
		 * Fire the event to all listeners
		 */
		for (int i = 0; i < count; i++) {
			ChartListener l = listeners.get(i);
			l.newChartData(chartDataEvent);
		}
	}


	/**
	 * Fires a autoScale() event.
	 */
	public void fireAutoScaleEvent (float newMax, float oldMax) {

		int count = listeners.size();
		if (count < 1) {
			return;
		}


		/*
		 * Create the event
		 */
		ChartAutoScaleEvent e = new ChartAutoScaleEvent(this, newMax, oldMax);

		/*
		 * Fire the event to all listeners
		 */
		for (int i = 0; i < count; i++) {
			ChartListener l = listeners.get(i);
			l.autoScale(e);
		}
	}


	/**
	 * This class is used to keep track of all the data and settings for
	 * a set of data in the chart.
	 */
	public final class DataSet extends ArrayDeque<Float> {

		private static final long serialVersionUID = 1L;
		protected float lastData;
		protected float maxData;
		protected Color dataColor;
		protected Color noDataColor;
		protected GeneralPath dataPath;
		protected String barLabel;
		protected int barLabelXOffset;

		public DataSet (String barLabel, Color dataColor) {

			super();
			this.barLabel = barLabel;
			this.dataColor = dataColor;
			noDataColor = dataColor.darker().darker();
			dataPath = new GeneralPath();
		}


		public DataSet (Color dataColor) {
			this(null, dataColor);
		}
		
		
		public void setColor (Color color) {
			dataColor = color;
			noDataColor = color.darker().darker();
		}
	}
}
