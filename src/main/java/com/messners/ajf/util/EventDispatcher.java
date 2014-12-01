package com.messners.ajf.util;

import java.util.EventListener;
import java.util.EventObject;


/**
 * This interface defines the method called to fire off events by 
 * instances of {@link ListenerManager}.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface EventDispatcher {

	/**
	 * Fire an event to the specified EventListener with the specified
	 * EventObject.
	 *
	 * @param  l    the EventListener to fire the event to
	 * @param  evt  the EventObject to pass as a parameter to the listener
	 */
	public void dispatchEvent (EventListener l, EventObject evt);
}
