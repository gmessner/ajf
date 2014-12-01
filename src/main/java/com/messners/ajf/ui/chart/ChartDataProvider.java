package com.messners.ajf.ui.chart;


/**
 * Implement this interface in order to provide data for a HistoryChart
 * instance.  
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface ChartDataProvider {

	/**
	 * This method will be called by a HistoryChart instance when it 
	 * wants data to be fed to it, which is done by calling addData()
	 * on the HistoryChart instance.
	 *
	 * @param  chart  the HistoryChart instance wanting data
	 */
	public void provideData (StripChart chart);
}
