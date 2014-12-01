
package com.messners.ajf.net;

import java.io.InputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


/**
 * This class defines a server that connects to a server an relays all
 * data to multiple client connections on a different port.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class SocketMux extends Server implements ServerListener {

	/**
	 * Manages the listeners for data 
	 */
	protected ArrayList<SocketMuxConnectionHandler> handlers;

	protected String serverHost;
	protected int serverPort;

	protected int bufSize = 1024;


	/**
	 * throws IOException if an I/O error occurs when opening the socket
	 */
	public SocketMux (String serverHost, int serverPort, int clientPort)
			throws IOException {

		super(clientPort);
		addListener(this);

		this.serverHost = serverHost;
		this.serverPort = serverPort;

		setConnectionHandlerClass(SocketMuxConnectionHandler.class);
		handlers = new ArrayList<SocketMuxConnectionHandler>();
	}

	
	/**
	 * This methods is called when a Server accepted a new connection.
	 *
	 * @param  event  holds all pertinent info for the connection
	 */
	public void connection (ServerEvent event) {

		Socket s = event.getConnection();
		System.err.println("Client connected: " + s);
	}

	
	/**
	 * This methods is called when a Server has throw an exception.
	 *
	 * @param  event  holds all pertinent info for the exception
	 */
	public void exception (ServerEvent event) {

		Exception e = event.getException();
		System.err.println("Exception: " + e.getMessage());
	}


	/**
	 * Start this SocketMux instance.  This will create and connect the client
	 * connection to the server and relay any data read to multiple client
	 * connections.
	 */
	public synchronized void start () throws Exception {

		/*
		 * First thing we need to do is setup a reader for the server we are
		 * connecting to as a client.
		 */
		Socket clientSocket = new Socket(serverHost, serverPort);
		ClientThread ct = new ClientThread(clientSocket);
		ct.start();

		super.start();
	}


	public synchronized void addHandler (SocketMuxConnectionHandler handler) {
		handlers.add(handler);
	}


	public synchronized void removeHandler (
			SocketMuxConnectionHandler handler) {

		handlers.remove(handler);
	}


	/**
	 * This class is responsible for reading data from the server and queueing
	 * it up for each client connection.
	 */
	private class ClientThread extends Thread {

		private Socket socket;

		public ClientThread (Socket socket) {
			this.socket = socket;
		}


		public void run () {


			InputStream in = null;
			try {

				in = socket.getInputStream();

			} catch (IOException ioe) {

				fireException(ioe);
				return;
			}

			byte buf[] = new byte[bufSize];

			try {
				while (true) {	
				
					int nread = in.read(buf);
					if (nread < 1) {
						continue;
					}	

					byte tmpBuf[] = new byte[nread];
					System.arraycopy(buf, 0, tmpBuf, 0, nread);

					synchronized (handlers) {

						int numHandlers = handlers.size();
						for (int i = 0; i < numHandlers; i++) {
						
							SocketMuxConnectionHandler handler = 
								handlers.get(i);
							handler.queueData(tmpBuf);
						}
					}
				} 

			} catch (IOException ioe) {
				fireException(ioe);
			}	
		}
	}

	
	/**
	 * Creates a SocketMux for the specified server and then listens for client
	 * connections relaying data read from the server to the client connection.
	 */
	public static void main (String args[]) {


		if (args.length != 3) {

			System.err.println(
				"Usage: SocketMux serverHost serverPort clientPort");
			System.exit(1);
		}


		String serverHost = args[0];

		int serverPort = 0;
		try {
			serverPort = Integer.parseInt(args[1]);
		} catch (NumberFormatException nfe) {
		}

		if (serverPort < 1) {
			System.err.println("Invalid serverPort: " + args[1]);
			System.exit(1);
		}

	
		int clientPort = 0;
		try {
			clientPort = Integer.parseInt(args[2]);
		} catch (NumberFormatException nfe) {
		}
	
		if (clientPort < 1) {
			System.err.println("Invalid clientPort: " + args[2]);
			System.exit(1);
		}

		SocketMux mux = null;
		try {

			mux = new SocketMux(serverHost, serverPort, clientPort);
			mux.start();

		} catch (Exception e) {

			System.err.println("ERROR: " + e.getMessage());
			System.exit(1);
		}
	}
}
