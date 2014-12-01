package com.messners.ajf.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * This class needs javadoc.
 *
 * @author  Greg Messner <greg@messners.com>
 */

public class Pipe implements Runnable {

	private InputStream in;
	private OutputStream out;
	private boolean autoShutOff = true;

	/*
	 * Compromise between BufferedInputStream's 2048 and 
	 * BufferedOutputStream's 512
	 */
	private int blockSize = 1024;
	private Exception exception;

	public Pipe () {
	}

	public Pipe (InputStream input, OutputStream output) {
		in = input;
		out = output;
	}

	public Pipe (InputStream input, OutputStream output, boolean shutoff) {
		in = input;
		out = output;
		autoShutOff = shutoff;
	}

	public synchronized Thread start () {
		Thread t = new Thread(this);
		t.start();
		return t;
	}

	public void run() {

		final BufferedInputStream bis;
		final BufferedOutputStream bos;
		final boolean shutoff;
		final int readSize;

		try {

			synchronized (this) {

				bis = new BufferedInputStream(in);
				bos = new BufferedOutputStream(out);
				shutoff = autoShutOff;
				readSize  = this.blockSize;
			}

			byte[] buf = new byte[readSize];
			int numRead = bis.read(buf);

			while (numRead != -1) {

				bos.write(buf, 0, numRead);
				numRead = bis.read(buf);
			}

			bis.close();
			bos.flush();

			if (shutoff) {
				bos.close();
			}

		} catch (IOException ioe) {

			synchronized (this) {
				exception = ioe;
			}
		}
	}

	public InputStream getInputStream () {
		return (in);
	}

	public void setInputStream (InputStream in) {
		this.in = in;
	}

	public OutputStream getOutputStream () {
		return (out);
	}

	public void setOutputStream (OutputStream out) {
		this.out = out;
	}

	public int getBlockSize () {
		return (blockSize);
	}

	public void setBlockSize (int blockSize) {
		this.blockSize = blockSize;
	}

	public Exception getException () {
		return (exception);
	}
}
