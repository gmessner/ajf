package com.messners.ajf.util;

import java.util.List;


/**
 * This interface defines a listener that listens on the WorkQueue for
 * work to be done.
 * 
 * @author  Greg Messner <greg@messners.com>
 */
public interface WorkQueueListener {

	/**
	 * Performs the work specified in the workList.
	 *
	 * @param  workList  the List of work items to be done
	 */
	public void doWork (List<Object> workList);
}
