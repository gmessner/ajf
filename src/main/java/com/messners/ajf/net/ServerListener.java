package com.messners.ajf.net;

import java.util.EventListener;


/**
 * This interface defines a listener for ServerEvents.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface ServerListener extends EventListener {

	/**
	 * This methods is called when a Server accepted a new connection.
	 *
	 * @param  event  holds all pertinent info for the connection
	 */
	public void connection (ServerEvent event);

	/**
	 * This methods is called when a Server has throw an exception.
	 *
	 * @param  event  holds all pertinent info for the exception
	 */
	public void exception (ServerEvent event);
}
