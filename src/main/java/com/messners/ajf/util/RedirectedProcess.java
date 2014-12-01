package com.messners.ajf.util;

import com.messners.ajf.logging.Logging;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;


/**
 * This class encapsulates the executing of processes. 
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class RedirectedProcess {

	private Process process = null;
	private String args[];
	private int status = -1;
	private boolean timedOut = false;

	private boolean ownErr;
	private OutputStream err;

	private boolean ownOut;
	private OutputStream out;

	private InputStream in;

	private Logger outLogger = null;
	private Logger errLogger = null;

	private ArrayList<ProcessListener> listeners;


	/**
	 * Creates a RedirectedProcesss instance with the specified command
	 * and arguments. When the process is ran will provides the process
	 * with an empty stdin, and throws away stdout and stderr.
	 *
	 * @param  args      the command and arguments.
	 */
	public RedirectedProcess (String args[]) {
		this(args, (InputStream)null, (OutputStream)null, (OutputStream)null);
	}


	/**
	 * Creates a RedirectedProcesss instance with the specified command
	 * and arguments. When the process is ran will provides the process
	 * with an empty stdin, and redirect stdout to outLogger(info) and
	 * stderr to errLogger.info().
	 *
	 * @param args      the command and arguments.
	 * @param outLogger the Logger to redirect stdout to
	 * @param errLogger the Logger to redirect stderr to
	 */
	public RedirectedProcess (
			String args[], Logger outLogger, Logger errLogger) {

		this(args, null, outLogger, errLogger);
	}


	/**
	 * Creates a RedirectedProcesss instance with the specified command
	 * and arguments. When the process is ran will provides the process
	 * with the specified stdin, and redirect stdout to outLogger(info)
	 * and stderr to errLogger.info().
	 *
	 * @param args      the command and arguments.
	 * @param in        the InputStream to use for stdin
	 * @param outLogger the Logger to redirect stdout to
	 * @param errLogger the Logger to redirect stderr to
	 */
	public RedirectedProcess (
			String args[], InputStream in, Logger outLogger, Logger errLogger) {

		this.in = in;

		if (outLogger == null) {
			this.out = new NullOutputStream();
			ownOut = true;
		} else {
			this.outLogger = outLogger;
			ownOut = false;
		}

		if (errLogger == null) {

			if (!ownOut) {
				this.err = new NullOutputStream();
				ownErr = true;
			} else {
				this.err = this.out;
				ownErr = false;
			}

		} else {
			this.errLogger = errLogger;
			ownErr = false;
		}


		this.args = args;
	}


	/**
	 * Creates a RedirectedProcesss instance with the specified command,
	 * arguments, stdin, stdout, and stderr.
	 *
	 * @param args the command and arguments.
	 * @param in   the InputStream to use for stdin
	 * @param out  the OutputStream to use for stdout
	 * @param err  the OutputStream to use for stderr
	 */
	public RedirectedProcess (
			String args[], InputStream in, OutputStream out, OutputStream err) {

		this.in = in;

		if (out == null) {
			this.out = new NullOutputStream();
			ownOut = true;
		} else {
			this.out = out;
			ownOut = false;
		}

		if (err == null) {

			if (!ownOut) {
				this.err = new NullOutputStream();
				ownErr = true;
			} else {
				this.err = this.out;
				ownErr = false;
			}

		} else {

			this.err = err;
			ownErr = false;
		}

		this.args = args;
	}


	/*
	 * <P>Executes the RedirectedProcess as currently configured.  This method
	 * will return immediately with the created Process instance.</P>
	 *
	 * NOTE: Using this method WILL NOT call the listeners when the process
	 * is done executing
	 *
	 * @param  timeout   the process timeout in seconds.
	 * @return the created Process
	 */
	public Process exec (int timeout) 
			throws IOException, IllegalStateException, 
				   InterruptedException, SecurityException {

		if (process != null) {
			throw new IllegalStateException("process has already been ran");
		}

		ProcessWatcher watcher = null;
		process = Runtime.getRuntime().exec(args);

		if (in != null) {
		    redirect(process.getOutputStream(), in);
		}

		if (outLogger != null) {
	    	Logging.redirect(process.getInputStream(), outLogger);
		} else {
	    	redirect(out, process.getInputStream());
		}

		if (errLogger != null) {
	   		Logging.redirect(process.getErrorStream(), errLogger);
		} else {
	   		redirect(err, process.getErrorStream());
		}

		if (timeout > 0) {
			watcher = new ProcessWatcher(timeout);
			watcher.start();
		}

		return (process);
	}


	/*
	 * Executes the RedirectedProcess as currently configured.  This method
	 * will block until the process is complete, is destroyed or has timed out.
	 *
	 * @param  timeout   the process timeout in seconds.
	 */
	public int execAndWait (int timeout) 
			throws IOException, IllegalStateException, 
				   InterruptedException, SecurityException {

		if (process != null) {
			throw new IllegalStateException("process has already been ran");
		}

		ProcessWatcher watcher = null;

		try {

			process = Runtime.getRuntime().exec(args);

			if (in != null) {
			    redirect(process.getOutputStream(), in);
			}

			if (outLogger != null) {
		    	Logging.redirect(process.getInputStream(), outLogger);
			} else {
		    	redirect(out, process.getInputStream());
			}

			if (errLogger != null) {
		   		Logging.redirect(process.getErrorStream(), errLogger);
			} else {
		   		redirect(err, process.getErrorStream());
			}

			if (timeout > 0) {
				watcher = new ProcessWatcher(timeout);
				watcher.start();
			}

			/*
			 * Wait for the process to exit and get it's exit status
			 */
			status = process.waitFor();

		} finally {

			if (watcher != null) {
				watcher.interrupt();
				watcher = null;
			}	

			if (ownOut) {
				out.close();
				out = null;
			}

			if (ownErr) {
				err.close();
				err = null;
			}
		} 

		return (status);
	}


	/*
	 * Executes the RedirectedProcess as currently configured.  This method
	 * executes the process in the background returns immediately.
	 *
	 * @param  timeout   the process timeout in seconds.
	 */
	public synchronized void backgroundExec (int timeout) 
			throws IOException, IllegalStateException, 
				   InterruptedException, SecurityException {

		if (in == null) {
			throw new IllegalStateException("process has already been ran");
		}

		BackgroundExecThread bet = new BackgroundExecThread(timeout);
		bet.start();
	}


	private class BackgroundExecThread extends Thread {

		private int timeout;

		public BackgroundExecThread (int timeout) {
			this.timeout = timeout;
		}


		public void run () {

			try {
				execAndWait(timeout);
			} catch (Exception e) {
			}

			/*
			 * Dispatch a processDone() message if we have listeners
			 */
			if (listeners != null && listeners.size() > 0) {

				ArrayList<ProcessListener> listenersCopy;
				synchronized (listeners) {
					listenersCopy = new ArrayList<ProcessListener>(listeners.size());
					listenersCopy.addAll(listeners);
				}

				for (ProcessListener listener : listenersCopy) {
					listener.processDone(RedirectedProcess.this);
				}
			}
		}
	}


    private void redirect (OutputStream out, InputStream in) {
		Pipe pipe = new Pipe(in, out);
		pipe.start();
    }


	/**
	 * Get the output from the executed process.
	 */
	public OutputStream getOut () {
		return (out);
	}


	/**
	 * Get the error output from the executed process.
	 */
	public OutputStream getErr () {
		return (err);
	}


	public Process getProcess () {
		return (process);
	}


	/**
	 * Gets the exit status from the executed process.
	 */
	public int getExitStatus () {
		return (status);
	}


	/**
	 * Gets the timed out flag of an executed process.
	 */
	public boolean getTimedOut () {
		return (timedOut);
	}


	/**
	 * Terminates the currently running process.
	 */
	public void terminate () {

		if (process != null) {
			process.destroy();
		}
	}


	/**
	 * This class is used as a timer on a launched process. It simply
	 * waits for the specified time and terminates the process if the
	 * timeout has elapsed.
	 */
	private class ProcessWatcher extends Thread {

		private int timeout = 0;

		ProcessWatcher (int timeout) {
			this.timeout = timeout;
		}

		public void run () {

			if (process == null) {
				return;
			}

			try {

				sleep(timeout * 1000);
				terminate();
				timedOut = true;

			} catch (Exception ignore) {
			}
		}
	}


	/**
	 * This class is used for an empty InputStream.
	 */
	public static class NullInputStream extends InputStream {

		public NullInputStream () {
			super();
		}

		public int read () {
			return (-1);
		}
	}


	/**
	 * This class is used to throw away output.
	 */
	public static class NullOutputStream extends OutputStream {

		public NullOutputStream () {
			super();
		}

		public void write (int b) {
		}
	}
}

