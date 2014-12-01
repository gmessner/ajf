package com.messners.ajf.ui.chart;


/**
 * This event is fired when a new data has been plotted on a HistoryChart.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ChartDataEvent extends java.util.EventObject {

	private static final long serialVersionUID = 1L;
	protected StripChart chart;
	protected float lastDataValues[];


	/*
	 * Constructor for a chart data event.
	 *
	 * @param  source  the HistoryChart source of the event
	 * @param  newValues  the data values just plotted by the HistoryChart
	 */
	public ChartDataEvent (Object source, float newValues[]) {

		super(source);
		lastDataValues = newValues;
	}

	
	/**
	 * Gets the data that was just plotted by the HistoryChart.
	 *
	 * @return the just plotted data
	 */
	public float [] getData () {
		return (lastDataValues);
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
