package com.messners.ajf.ui.chart;


/**
 * This event is fired when a HistoryChart is about to be auto-scaled.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ChartAutoScaleEvent extends java.util.EventObject {

	private static final long serialVersionUID = 1L;
	protected StripChart chart;
	protected float oldMax;
	protected float newMax;


	/*
	 * Constructor for a chart data event.
	 *
	 * @param  source  the HistoryChart source of the event
	 * @param  newValues  the data values just plotted by the HistoryChart
	 */
	public ChartAutoScaleEvent (Object source, float newMax, float oldMax) {

		super(source);
		this.newMax = newMax;
		this.oldMax = oldMax;
	}

	
	/**
	 * Gets the new value for the Y Axis maximum.
	 *
	 * @return the new value for the Y Axis maximum
	 */
	public float getNewYMaximum () {
		return (newMax);
	}


	/**
	 * Gets the old value for the Y Axis maximum.
	 *
	 * @return the new value for the Y Axis maximum
	 */
	public float getOldYMaximum () {
		return (oldMax);
	}


	/**
	 * Gets the source HistoryChart.
	 *
	 * @return the source HistoryChart
	 */
	public StripChart getChart () { 
		return (chart);
	}
}
