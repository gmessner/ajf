package com.messners.ajf.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;


/**
 * This class defines a basic server for servicing TCP based clients.  
 * When start() is called, it creates a separate thread to accept 
 * connections.  For each connection, it creates an instance of 
 * ConnectionHandler in a thread and starts that thread.  
 * Either stop() or close() stops the accept thread and closes 
 * the server socket.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class Server extends ServerSocket implements Runnable {

	/**
	 * Instances of this class are created and pooled and are used to
	 * process accepted connections.
	 */
	protected Class<? extends ConnectionHandler> connectionHandlerClass;


	/**
	 * This thread is used to wait for connections using accept().
	 */
	protected Thread acceptThread;


	/**
	 * Manages the listeners on this Server instance.
	 */
	protected ArrayList<ServerListener> listeners;


	/**
	 * Where we log our activity
	 */
	protected Logger logger;

	{
		logger = Logger.getLogger( this.getClass().getName() );
	}


	/**
	 * Creates an unbound Server.
	 *
	 * throws IOException if an I/O error occurs when opening the socket
	 */
	public Server () throws IOException {
		super();
	}


	/**
	 * Creates a Server, bound to the specified port.
	 *
	 * @param  port  the port number, or 0 to use any free port
	 * throws IOException if an I/O error occurs when opening the socket
	 */
	public Server (int port) throws IOException {
		super(port);
	}


	/**
	 * Creates a Server and binds it to the specified
	 * local port number, with the specified backlog.
	 *
	 * @param  backlog  the maximum length of the queue
	 * @param  port  the port number, or 0 to use any free port
	 * throws IOException if an I/O error occurs when opening the socket
	 */
	public Server (int port, int backlog) throws IOException {
		super(port, backlog);
	}


	/**
	 * Create a Server with the specified port, listen backlog,
	 * and local IP address to bind to.
	 *
	 * @param  backlog  the maximum length of the queue
	 * @param  port     the port number, or 0 to use any free port
	 * @param  bindAddr the local InetAddress the server will bind to 
	 * throws IOException if an I/O error occurs when opening the socket
	 */
	public Server (int port, int backlog, InetAddress bindAddr)
			throws IOException {
		super(port, backlog, bindAddr);
	}


	/**
	 * Gets the Class for the ConnectionHandler implementation that is used
	 * by this server.
	 *
	 * @return the ConnectionHandler implementation class that is used 
	 * to handle connections to this server
	 */
	public Class<? extends ConnectionHandler> getConnectionHandlerClass () {
		return (connectionHandlerClass);
	}


	/**
	 * Sets the Class for the ConnectionHandler implementation to be used
	 * by this server.
	 *
	 * @param  connectionHandlerClass the ConnectionHandler implementation
	 * class to use to handle connections to this server
	 */
	public void setConnectionHandlerClass (Class<? extends ConnectionHandler> connectionHandlerClass) {
		this.connectionHandlerClass = connectionHandlerClass;
	}


	/**
	 * Sets the Class for the ConnectionHandler implementation to be used
	 * by this server.
	 *
	 * @param  classname the name of the ConnectionHandler implementation
	 *                   class to use to handle connections to this server
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void setConnectionHandlerClass (String classname) 
			throws Exception {

		connectionHandlerClass = (Class<? extends ConnectionHandler>) getClass().forName(classname);
	}


	/**
	 * Adds a listener for ServerEvents. 
	 *
	 * @param  l  the ServerListener to add
	 */
	public synchronized void addListener (ServerListener l) {

		if (listeners == null) {
			listeners = new ArrayList<ServerListener>();
		}

		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}


	/**
	 * Removes a ServerListener.
	 *
	 * @param  l  the ServerListener to remove
	 */
	public synchronized void removeListener (ServerListener l) {
		listeners.remove(l);
	}


	/**
	 * Creates and starts a thread to handle connections for this Server
	 * instance.  
	 * @throws Exception if ConnectionHandler was not already set.
	 */
	public synchronized void start () throws Exception {

		/*
		 * If we already have an accept thread then the server has already
		 * been started, so just return.
		 */
		if (acceptThread != null) {
			// silent discard
			return;
		}

		/*
		 * Create and start the accept thread
		 */
		acceptThread = new Thread(this);
		acceptThread.setName( "accept" );
		acceptThread.start();
	}


	/**
	 * Stops this Server instance and closes the server socket.
	 */
	public synchronized void stop () {

		if (acceptThread == null) {
			// silent discard
			return; 
		}

		Thread t = acceptThread;
		acceptThread = null;

		try {
			super.close();
		} catch (IOException ignore) {
		}

		t.interrupt();
	}


	/**
	 * Overides ServerSocket.close() to stop the Thread that is waiting
	 * for connections and close the socket.
	 *
	 * @throws IOException if an error occurs
	 * @see #stop()
	 */
	public void close () throws IOException {
		stop();
	}


	/**
	 * This run() method listens for connections, and if a connection
	 * handler has been specifed, instantiantes a ConnectionHandler
	 * and starts a thread to call process().  It then fires a connection 
	 * event for each connection accepted.
	 */
	public final void run () {

		Thread t = Thread.currentThread();
		while (t == acceptThread) {

			try {

				logger.info( "Waiting for connections on " + this);
				Socket connection = accept();
				logger.info( "Connection received from host " + 
						connection.getInetAddress().getHostName() );

				if( connectionHandlerClass != null ) {
				    ConnectionHandler handler =
					   (ConnectionHandler)connectionHandlerClass.newInstance();
				    HandlerThread ht = new HandlerThread(this, connection, handler);
				    ht.start();
				    logger.fine( "Starting new connection handler/thread" );
				}

				fireConnection(connection);

			} catch (Exception e) {

				if (acceptThread != null) {
					fireException(e);		
				}
			}
		}
	}


	/**
	 * Fires an exception event to the listeners.
	 *
	 * @param  e  the Exception to fire the event for
	 */
	protected void fireException (Exception e) {

		if (listeners == null) {
			return;
		}

		ArrayList<ServerListener> fireList;
		synchronized (listeners) {
			if (listeners.isEmpty()) {
				return;
			}
			
			fireList = new ArrayList<ServerListener>(listeners.size());
			fireList.addAll(listeners);
		}

		ServerEvent se = new ServerEvent(this, e);
		for (ServerListener l  : fireList) {
			l.exception(se);
		}
	}


	/**
	 * Fires a connection event to the listeners.
	 *
	 * @param  socket  the client connection
	 */
	protected void fireConnection (Socket socket) {

		if (listeners == null) {
			return;
		}

		ArrayList<ServerListener> fireList;
		synchronized (listeners) {
			if (listeners.isEmpty()) {
				return;
			}
			
			fireList = new ArrayList<ServerListener>(listeners.size());
			fireList.addAll(listeners);
		}

		ServerEvent se = new ServerEvent(this, socket);
		for (ServerListener l : fireList) {
			l.connection(se);
		}
	}


	/**
	 * This class defines a thread to contain a single ConnectionHandler
	 * instance.
	 */
	private final class HandlerThread extends Thread {

		private Server server;
		private Socket connection;
		private ConnectionHandler handler;

		public HandlerThread (
				Server server, Socket connection, ConnectionHandler handler) {

			this.server = server;
			this.connection = connection;
			this.handler = handler;
		}


		public void run () {
			handler.process(server, connection);
		}
	}
}
