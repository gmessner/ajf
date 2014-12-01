package com.messners.ajf.util;


/**
 * This interface defines a listener for Processes that are executed by
 * RedirectedProcess.
 * 
 * @author  Greg Messner <greg@messners.com>
 */
public interface ProcessListener {

	/**
	 * Called when the process is done.  Use getExitStatus() and
	 * getTimedOut() to figure out whether the process completed
	 * normally or not.
	 *
	 * @param  process  the RedirectedProcess that completed.
	 */
	public void processDone (RedirectedProcess process);
}
