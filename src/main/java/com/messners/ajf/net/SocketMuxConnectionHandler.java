package com.messners.ajf.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.messners.ajf.util.Queue;


/**
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class SocketMuxConnectionHandler implements ConnectionHandler {

	private Queue<byte[]> queue;
	private OutputStream out;

	public SocketMuxConnectionHandler () {
		queue = new Queue<byte[]>();
	}


	/**
	 * The handler simply reads data from a Queue and writes it
	 * to the client connection.
	 */
	public void process (Server server, Socket connection) {

		
		/*
		 * Get ther OutputStream for the client connection
		 */
		try {

			out = connection.getOutputStream();

		} catch (IOException ioe) {

			server.fireException(ioe);
			return;
		}

		
		((SocketMux)server).addHandler(this);

		while (true) {

			try {

				Object data = queue.waitForObject();
				if (data == null) {
					break;
				}

				out.write((byte[])data);

			} catch (Exception e) {

				server.fireException(e);
				break;
			}
		}

		((SocketMux)server).removeHandler(this);
	}


	/**
	 * Queues data for relaying to the client connection.
	 *
	 * @param  data  the data to be queued
	 */
	public void queueData (byte data[]) {
		queue.put(data);
	}
}
