package com.messners.ajf.ui.chart;


/**
 * Implement this interface in order to recieve notification of
 * of new data and auto-scales in a HistoryChart.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface ChartListener extends java.util.EventListener {

	/**
	 * This method will be called after new data has been plotted in
	 * the HistoryChart.
	 *
	 * @param  evt  the ChartDataEvent
	 */
	public void newChartData (ChartDataEvent evt);


	/**
	 * This method will be called when the chart is about to be auto scaled.
	 *
	 * @param  evt  the ChartAutoScaleEvent
	 */
	public void autoScale (ChartAutoScaleEvent evt);
}
