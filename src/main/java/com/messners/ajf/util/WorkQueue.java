package com.messners.ajf.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.messners.ajf.app.Preferences;


/**
 * This class provides services for processing data (usually messages)
 * for interaction with a Swing GUI.
 * 
 * @author  Greg Messner <greg@messners.com>
 */
public class WorkQueue implements Runnable {


	/**
	 * Default maximum read block size = 10.
	 */
	public static final int DEFAULT_MAX_READ_BLOCK_COUNT = 10;


	/**
	 * Default max refresh time = 1000 milliseconds. This is the maximum time
	 * between the dispatching of work to th UI.
	 */
	public static final long DEFAULT_MAX_REFRESH_TIME = 1000L;


	/**
	 * Stores the name of the WorkQueue instance
	 */
	private String name;


	/**
	 * If this flag is true SwingUtilities.invokeLater() will be used
	 * when calling the WorkQueueListeners.
	 */
	private boolean isGui;


	/**
	 * Queue is the work queue for work, usually messages.
	 */
	private Queue<Object> queue;
	private int readBlockCount;
	private long refreshTime;


	/**
	 * Flag that indicates this WorkQueue has been stopped.
	 */
	private boolean stopped;


	/**
	 * This is the Thread this WorkQueue is running in
	 */
	private Thread workThread;


	/**
	 * This is the List of WorkQueueListeners
	 */
	ArrayList<WorkQueueListener> listeners;


	/**
	 * Constructs a WorkQueue instance with the specified name.
	 *
	 * @param  name  the name for the WorkQueue
	 */
	public WorkQueue (String name) {

		this.name = name;
		this.isGui = true;


		/*
		 * Setup the work queue
		 */
		queue = new Queue<Object>();
		readBlockCount = DEFAULT_MAX_READ_BLOCK_COUNT;
		refreshTime = DEFAULT_MAX_REFRESH_TIME;
	}


	/**
	 * Gets the name of this WorkQueue instance.
	 *
	 * @return the name of this instance
	 */
	public String getName () {
		return (name);
	}


	/**
	 * Sets the name of this WorkQueue instance.
	 *
	 * @param  name  the new name for this instance
	 */
	public void setName (String name) {
		this.name = name;
	}


	/**
	 * Gets the is GUI client flag.  When this flag is true
	 * SwingUtilities.invokeLater() will be used when calling
	 * the WorkQueueListeners.
	 *
	 * @return the is GUI flag
	 */
	public boolean getIsGui () {
		return (isGui);
	}


	/**
	 * Sets the is GUI client flag.  When this flag is true
	 * SwingUtilities.invokeLater() will be used when calling
	 * the WorkQueueListeners.
	 *
	 * @param isGui  the new is GUI flag
	 */
	public void setIsGui (boolean isGui) {
		this.isGui = isGui;
	}


	/**
	 * Sets the read block count.  This is the maximum number of items that
	 * will be pulled from the queue for processing at one time.
	 *
	 * @param  readBlockCount  the read block count
	 */
	public void setReadBlockCount (int readBlockCount) {
		this.readBlockCount = readBlockCount;
	}


	/**
	 * Set the maximum refresh time. This is the maximum time
	 * between the dispatching of work.
	 *
	 * @param  refreshTime  the maximum refresh time in milliseconds
	 */
	public void setRefreshTime (long refreshTime) {
		this.refreshTime = refreshTime;
	}


	/**
	 * Adds a WorkQueueListener.
	 *
	 * @param  l  the listener to add
	 */
	public synchronized void addListener (WorkQueueListener l) {

		if (listeners == null) {
			listeners = new ArrayList<WorkQueueListener>();
		}

		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}


	/**
	 * Removes WorkQueueListener.
	 *
	 * @param  l  the listener to remove
	 */
	public synchronized void removeListener (WorkQueueListener l) {

		if (listeners == null) {
			return;
		}

		listeners.remove(l);
	}


	/**
	 * Starts this WorkQueue instance. Until started no work will be 
	 * dispatched to the listeners.
	 */
	public synchronized void start () {

		if (workThread != null) {
			return;
		}

		stopped = false;
		workThread = new Thread(this);
		workThread.start();
	}


	/**
	 * Stops this WorkQueue instance.
	 */
	public synchronized void stop () {

		if (workThread == null) {
			return;
		}

		stopped = true;
		workThread.interrupt();
		workThread = null;
	}


	/**
	 * Restores the configuration of this instance.
	 *
	 * @param  prefs  the preferences instance to restore settings from
	 */
	public void restorePreferences (Preferences prefs) {

		refreshTime  = prefs.get(name + ".refresh-time",  refreshTime);
		readBlockCount = prefs.get(name + ".read-block-count", readBlockCount);
	}


	/**
	 * Saves the configuration of this instance.
	 *
	 * @param  prefs  the preferences instance to save settings to
	 */
	public void savePreferences (Preferences prefs) {

		prefs.put(name + ".refresh-time",  refreshTime);
		prefs.put(name + ".read-block-count", readBlockCount);
	}


	/**
	 * Queue an object for later processing.
	 *
	 * @param  work  the item to be added to the work queue
	 */
	public void queue (Object work) {
		queue.put(work);
	}


	/**
	 * Queue a List of work items for later processing.
	 *
	 * @param  workList  the List of work items to be added to the work queue
	 */
	public void queue (List<Object> workList) {

		/*
		 * If the listeners are associated with a Swing GUI use 
		 * SwingUtilites.invokeLater() to call the WorkQueueListeners,
		 * otherwise just invoke them now.
		 */
		if (isGui) {

			Dispatcher dispatcher = new Dispatcher(workList);
			SwingUtilities.invokeLater(dispatcher);
	
		} else {

			fireWork(workList);
		}
	}


	/**
	 * Gets the size of the current queue.
	 *
	 * @return  the size of the current queue
	 */
	public synchronized int size () {
		return (queue.size());
	}


	/**
	 * Gets the item from at the specified index.
	 *
	 * @param  index  the index to get the item for
	 * @return the item at the specified index
	 */
	public synchronized Object get (int index) {
		return (queue.get(index));
	}


	/**
	 * Removes an object from the queued work list.
	 *
	 * @param  index the index of the object to remove
	 * @return the removed object
	 */
	public synchronized Object dequeue (int index) {
		return (queue.remove(index));
	}


	/**
	 * Removes an object from the queued work list.
	 *
	 * @param  obj  the object to remove
	 * @return true if the object was queued, otherwise returns false
	 */
	public synchronized boolean dequeue (Object obj) {
		return (queue.remove(obj));
	}


	/**
	 * This is the run() method for Runnable, it waits for a configurable
	 * number of messages to be available or a refresh interval has passed
	 * and then processes any queued messages.
	 */
	public void run () {

		while (!stopped) {

			try {

				if (readBlockCount < 2 || refreshTime == 0) {

					Object obj = queue.waitForObject(refreshTime);
					processQueuedWork(obj);

				} else {

					queue.waitForObjects(readBlockCount, refreshTime);
					processQueuedWork();
				}

			} catch (InterruptedException ie) {
			}
		}
	}


	/**
	 * Dispatch a single work item.
	 */
	private synchronized void processQueuedWork (Object obj) {

		if (obj != null) {

			ArrayList<Object> workList = new ArrayList<Object>(1);
			workList.add(obj);
			queue(workList);
		}
	}


	/**
	 * Dispatch all the currently queued messages.
	 */
	private synchronized void processQueuedWork () {

		/*
		 * Setup a list to hold all the work currently available
		 */
		int available = queue.size();
		ArrayList<Object> workList = new ArrayList<Object>(available);


		/*
		 * Build up the list by pulling each work item off the queue
		 */
		for (int i = 0; i < available; i++) {

			try {

				Object work = queue.get();
				workList.add(work);

			} catch (EmptyQueueException eqe) {
			}
		}


		/*
		 * Queue this list
		 */
		queue(workList);
	}


	/**
	 * This class provides a Runnable to be used with
	 * SwingUtilities.invokeLater() to dispatch the 
	 * actual work to the WorkQueueListeners.
	 */
	private class Dispatcher implements Runnable {

		private List<Object> workList;
		
		public Dispatcher (List<Object> workList) {
			this.workList = workList;
		}

		public void run () {
			fireWork(workList);
			workList = null;
		}
	}


	/**
	 * Fire the work list off to the listeners.
	 *
	 * @param  workList  the list of work items for the listener to process
	 */
	private void fireWork (List<Object> workList) {

		/*
		 * NOTE: Normally a copy of the listeners would be made so
		 * listeners could be added and deleted at anytime, we don't
		 * not do this for effieciencies sake and only allow listeners
		 * to be added or removed when this WorkQueue is stopped.
		 */
		if (listeners == null) {
			return;
		}

		/*
		 * Fire the event to all listeners
		 */
		int count = listeners.size();
		for (int i = 0; i < count; i++) {

			WorkQueueListener l = listeners.get(i);
			l.doWork(workList);
		}
	}
}
