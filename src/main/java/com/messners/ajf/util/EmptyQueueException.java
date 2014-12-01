package com.messners.ajf.util;


/**
 * Signals that the queue is empty.
 *
 * @author  Greg Messner <greg@messners.com>
 * @see Queue
 */
public class EmptyQueueException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new EmptyQueueException with no detail message.
	 */
	public EmptyQueueException () {
		super();
	}


	/**
	 * Constructs a new EmptyQueueException with a detail message.
	 *
	 * @param  msg  the message for the exception
	 */
	public EmptyQueueException (String msg) {
		super(msg);
	}
}
