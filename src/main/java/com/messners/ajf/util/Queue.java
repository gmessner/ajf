package com.messners.ajf.util;

import java.util.ArrayList;


/**
 * Class for a simple FIFO queue.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class Queue<T> extends ArrayList<T> {

	private static final long serialVersionUID = 1L;


	/**
	 * Put an item into the queue.
	 *
	 * @param   obj   the object to be put into the queue
	 */
	public synchronized T put (T obj) {

		add(obj);
		notify();
		return (obj);
	}


	/**
	 * Gets an item from the front of the queue.
	 *
	 * @throws EmptyQueueException when the queue is empty
	 */
	public synchronized T get () {

		T obj = peek();
		remove(0);
		return (obj);
	}


	/**
	 * Peeks at the front of the queue.
	 *
	 * @return the object at the front of the queue without removing it
	 * @throws EmptyQueueException when the queue is empty.
	 */
	public synchronized T peek () {

		if (size() == 0) {
	 		throw new EmptyQueueException();
		}

		return (get(0));
	}


	/**
	 * Returns true if the queue is empty.
	 *
	 * @return  true if the Queue is empty, otherwise return false
	 */
	public synchronized boolean empty () {

		return (size() == 0);
	}


	/**
	 * Searches for the first occurence of the given argument, testing
	 * for equality using the equals method. 
	 *
	 * @param   obj   the desired object
	 * @return the index of object, or -1 if it is not found
	 */
	public synchronized int search (T obj) {
		return (indexOf(obj));
	}


	/**
	 * Removes an object from the queue.
	 *
	 * @param  obj  the object to remove
	 * @return true if the object is queued, otherwise false
	 */
	public synchronized boolean remove (Object obj) {
		return (super.remove(obj));
	}

	/**
	 * Wait until a count of objects are available on the queue or waitTime
	 * has elapsed, whichever occurs first.
	 *
	 * @param count     the number of objects to wait for
	 * @param waitTime  the time in milliseconds to wait
	 * @throws InterruptedException if the current thread is interrupted
	 */
	public synchronized void waitForObjects (int count, long waitTime) 
				throws InterruptedException {

		long stopTime = System.currentTimeMillis() + waitTime;

		while (size() < count) {

			if (size() > 0) {

				waitTime = stopTime - System.currentTimeMillis();
				if (waitTime <= 0) {
					return;
				}

				wait(waitTime);

			} else {

				wait();
			}
		}
	}

	/**
	 * Wait until the object appears in the queue, then return it.
	 *
	 * @param waitTime  the time in milliseconds to wait
	 * @return the object from the queue. null if the wait state was interrupted
	 * @see #get
	 */
	public synchronized T waitForObject (long waitTime) {

		while (empty()) {

			try {
				wait(waitTime);
			} catch (InterruptedException ie) {
				return (null);
			}
		}

		return (get());
	}


	/**
	 * Wait indefinetly until the object appears in the queue, then return it.
	 *
	 * @return the object from the queue. null if the wait state was interrupted
	 * @see #get
	 */
	public synchronized T waitForObject () {
		return (waitForObject(0));
	}


	/**
	 * Removes all the objects in the queue.
	 */
	public synchronized void clear () {
		super.clear();
	}
}
