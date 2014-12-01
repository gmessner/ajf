package com.messners.ajf.net;

import java.net.Socket;
import java.util.EventObject;


/**
 * This class specifies the event for ServerListener.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ServerEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	protected Object eventData;

	public ServerEvent (Server server, Object eventData) {

		super(server);
		this.eventData = eventData;
	}


	/**
	 * Gets the Server instance that was the source of the event.
	 *
	 * @return the Server instance that was created from the message
	 */
	public Server getServer () {
		return ((Server)getSource());
	}


	/**
	 * Gets the Socket associated with a connection event.
	 *
	 * @return  the Socket instance associated with a connection event
	 */
	public Socket getConnection () {

		if (eventData instanceof Socket) {
			return ((Socket)eventData);
		}

		return (null);
	}


	/**
	 * Gets the Exception associated with an exception event.
	 *
	 * @return  the Exception instance associated with an exception event
	 */
	public Exception getException () {

		if (eventData instanceof Exception) {
			return ((Exception)eventData);
		}

		return (null);
	}
}
