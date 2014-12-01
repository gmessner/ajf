package com.messners.ajf.util;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * This class provides management and dispatch capability for
 * both non-filtered and object-filtered event listeners.  There
 * is one listener list for the non-filtered events and one for 
 * each filter object.  There are two EventDispatcher's: 
 * one for all filtered events and one for non-filtered events.
 *
 * On firing with no filter, the non-filtered EventDispatcher is
 * called with listeners from the non-filtered listener list.
 * On firing with a filter object, the filtered EventDispatcher is
 * called with listeners from the filter object's listener list.
 *
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ListenerManager {

	/**
	 * List for the non-filtered event listeners.
	 */
	protected ArrayList<EventListener> listeners;

	/**
	 * HashMap for the filtered event listeners.
	 */
	protected HashMap<Object, ArrayList<EventListener>> listenerMap;

	/**
	 * The EventDispatcher for non-filtered events.
	 */
	protected EventDispatcher eventDispatcher;

	/**
	 * The EventDispatcher for filtered events.
	 */
	protected EventDispatcher filteredEventDispatcher;

	/**
	 * Creates an instance with no event dispatchers.
	 */
	public ListenerManager () {
	}
	
	/**
	 * Creates an instance for managing non-filtered listeners.
	 *
	 * @param  eventDispatcher  the EventDispatcher for non-filtered events
	 */
	public ListenerManager (EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

	/**
	 * Creates an instance for managing both non-filtered and
	 * filtered listeners.
	 *
	 * @param  eventDispatcher  the EventDispatcher for non-filtered events
	 * @param  filteredEventDispatcher  the EventDispatcher for filtered events
	 */
	public ListenerManager (EventDispatcher eventDispatcher,
			EventDispatcher filteredEventDispatcher) {

		this.eventDispatcher = eventDispatcher;
		this.filteredEventDispatcher = filteredEventDispatcher;
	}

	/**
	 * Sets the EventDispatcher for dispatching to non-filtered listeners.
	 *
	 * @param  eventDispatcher  the EventDispatcher for non-filtered events
	 */
	public void setEventDispatcher (EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

	/**
	 * Sets the EventDispatcher for dispatching to filtered listeners.
	 *
	 * @param  filteredEventDispatcher  the EventDispatcher for filtered events
	 */
	public void setFilteredEventDispatcher (
			EventDispatcher filteredEventDispatcher) {
		this.filteredEventDispatcher = filteredEventDispatcher;
	}

	/**
	 * Checks to see if the listener is managed.
	 *
	 * @param  l  the listener to check
	 */
	public synchronized boolean contains (EventListener l) {

		if (listeners == null) {
			return (false);
		}

		return (listeners.contains(l));
	}

	/**
	 * Adds a non-filtered listener.
	 *
	 * @param  l  the listener to add
	 */
	public synchronized void addListener (EventListener l) {

		if (listeners == null) {
			listeners = new ArrayList<EventListener>();
		}

		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * Removes a non-filtered listener.
	 *
	 * @param  l  the listener to remove
	 */
	public synchronized void removeListener (EventListener l) {

		if (listeners == null) {
			return;
		}

		listeners.remove(l);
	}

	/**
	 * Fires a non-filtered event.
	 *
	 * @param event  the <code>EventObject</code> to pass to the listeners
	 * @throws RuntimeException if (unfiltered) EventDispatcher not set
	 */
	public void fire (EventObject event) {

		/*
		 * Return if we don't have any listeners for non-filtered events.
		 * Complain if we have listeners and no dispatcher.
		 */
		if (this.listeners == null || this.listeners.size() == 0) {
			return;
		}
		
		if (eventDispatcher == null ) {
			throw new RuntimeException(
				"No EventDispatcher set for unfiltered event fire");
		}

		/*
		 * Make a copy of the listener object List so that it cannot
		 * be changed while we are firing events
		 */
		int size = this.listeners.size();
		ArrayList<EventListener> listeners = new ArrayList<EventListener>(size);
		synchronized(this) {
			listeners.addAll(this.listeners);
		}

		/*
		 * Fire the event to all listeners
		 */		
		for (int i = 0; i < size; i++) {
			EventListener l = listeners.get(i);
			eventDispatcher.dispatchEvent(l, event);
		}
	}


	/**
	 * Checks to see if the filtered listener is managed.
	 *
	 * @param  filter  the filter the listener belongs to
	 * @param  l  the listener to check
	 */
	public synchronized boolean contains (Object filter, EventListener l) {

		if (filter == null) {
			return (contains(l));
		}

		if (listenerMap == null) {
			return (false);
		}

		List<EventListener> listeners = listenerMap.get(filter);
		if (listeners == null) {
			return (false);
		}

		return (listeners.contains(l));
	}


	/**
	 * Adds a filtered listener.
	 *
	 * @param  filter  the filter the listener will belong to
	 * @param  l  the listener to add
	 */
	public synchronized void addFilteredListener (
			Object filter, EventListener l) {

		if (listenerMap == null) {
			listenerMap = new HashMap<Object, ArrayList<EventListener>>();
		}

		ArrayList<EventListener> listeners = listenerMap.get(filter);
		if (listeners == null) {

			listeners = new ArrayList<EventListener>();
			listenerMap.put(filter, listeners);
			listeners.add(l);

		} else if (!listeners.contains(l)) {

			listeners.add(l);
		}
	}


	/**
	 * Removes a filtered listener.
	 *
	 * @param  filter  the filter the listener belongs to
	 * @param  l  the listener to remove
	 */
	public synchronized void removeFilteredListener (Object filter, EventListener l) {

		List<EventListener> listeners = getFilterListeners(filter);
		if (listeners == null) {
			return;
		}

		listeners.remove(l);
	}


	/**
	 * Gets the List of listeners for the specified filter object.
	 *
	 * @param  filter  the filter object to gbet the listeners for
	 * @return a List with the listeners or null if no listeners are
	 * registered for the given filter object
	 */
	public List<EventListener> getFilterListeners (Object filter) {

		if (listenerMap == null) {
			return (null);
		}

		return listenerMap.get(filter);
	}


	/**
	 * Returns true if this instance has listeners for the given
	 * filter object.
	 *
	 * @param  filter  the filter object to look for listeners for
	 * @return  true if listeners are found for the given filter, false if not
	 */
	public boolean hasFilterListeners (Object filter) {
		List<EventListener> listeners = getFilterListeners(filter);
		return (listeners != null && listeners.size() > 0);
	}


	/**
	 * Fires a filtered event.
	 *
	 * @param  filter  the filter the listeners belong to
	 * @param event  the <code>EventObject</code> to pass to the listeners.
	 * @throws RuntimeException if (filtered) EventDispatcher not set
	 */
	public void fire (Object filter, EventObject event) {

		/*
		 * Complain if we have no dispatcher.
		 */
		if (filteredEventDispatcher == null) {
			throw new RuntimeException( "No EventDispatcher set for filtered event fire" );
		}

		/*
		 * Return if we don't have any listeners for the filter
		 */
		ArrayList<EventListener> filterListeners = (ArrayList<EventListener>)getFilterListeners(filter);
		if (filterListeners == null) {
			return;
		}

		/*
		 * Make a copy of the listener object List so that it cannot
		 * be changed while we are firing events
		 */
		int size = filterListeners.size();
		ArrayList<EventListener> listeners = new ArrayList<EventListener>(size);
		synchronized(this) {
			listeners.addAll(filterListeners);
		}

		/*
		 * Fire the event to all listeners
		 */		
		for (int i = 0; i < size; i++) {
			EventListener l = (EventListener)listeners.get(i);
			filteredEventDispatcher.dispatchEvent(l, event);
		}
	}


	public synchronized void dumpListenerInfo () {

		if (listeners == null || listeners.size() == 0) {
			System.err.println("No Non-filtered Listeners");
		} else {

			System.err.println("Non-filtered Listeners:");
			int numListeners = listeners.size();
			for (int i = 0; i < numListeners; i++) {
				System.err.println(listeners.get(i));
			}

			System.err.println();
		}

	
		if (listenerMap == null || listenerMap.size() == 0) {
			return;
		}

		System.err.println("Filtered Listeners:");

		Iterator<Object> filters = listenerMap.keySet().iterator();
		while (filters.hasNext()) {

			Object filter = filters.next();
			System.err.println("Filter: " + filter);

			ArrayList<EventListener> listeners = (ArrayList<EventListener>)getFilterListeners(filter);
			if (listeners != null && listeners.size() > 0) {

				int numListeners = listeners.size();
				for (int i = 0; i < numListeners; i++) {
					System.err.println(listeners.get(i));
				}

				System.err.println();
			}

		}
	}
}
