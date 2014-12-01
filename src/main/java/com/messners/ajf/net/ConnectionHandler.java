package com.messners.ajf.net;

import java.net.Socket;


/**
 * This interface defines a method that will be called to handle
 * a single Socket connection to a client.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface ConnectionHandler {

	/**
	 * Processes the client Socket connection from a Server instance.
	 *
	 * @param server the Server instance that accepted the client connection
	 * @param connection  the actual Socket connection to the client
	 */
	public void process (Server server, Socket connection);
}
