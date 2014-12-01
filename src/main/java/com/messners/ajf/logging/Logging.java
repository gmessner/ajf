package com.messners.ajf.logging;

import com.messners.ajf.util.ObjectLoader;
import com.messners.ajf.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.LogManager;


/**
 * This class provides static methods for initializing the java.util.logging
 * package to a pre-determined state and for getting Logger instances.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class Logging {

	private static boolean initialized = false;

	/**
	 * Gets the initialized flag.
	 *
	 * @return true if the Logging package has been initialized
	 */
	public static synchronized boolean isInitialized () {
		return (initialized);
	}


	/**
	 * Initialize the logging setup with the specified InputStream.  Prior
	 * to reading the logging configuration a default configuration will
	 * be read from the file specified by java.util.logging.config.file and
	 * then from com/messners/ajf/logging/logging.properties, once all the 
	 * configuration has been read any property found in the System properties
	 * that begin with "java.util.logging" will override any read configuration.
	 *
	 * @param in  the InputStream holding the logging configuration
	 * @throws IOException if any IO error occurs
	 */
	public static synchronized void initialize (InputStream in) 
			throws IOException {

		Properties config = new Properties();

		/*
		 * Load the configuration from the file specified by the
		 * java.util.logging.config.file property.
		 */
		String configFile = System.getProperty("java.util.logging.config.file");
		if (configFile != null) {

			FileInputStream fin = new FileInputStream(configFile);
			config.load(fin);
			fin.close();
		}


		/*
		 * Now load the defaults from com/messners/logging/logging.properties 
		 */
		InputStream rin;
		rin = Logging.class.getResourceAsStream("logging.properties");
		config.load(rin);
		rin.close();


		/*
		 * Load the passed in InputStream
		 */
		if (in != null) {
			config.load(in);
		}


		/*
		 * Finally look for appropriate System properties and add 
		 * them to the config
		 */
		Properties systemProperties = System.getProperties();
		Enumeration<?> names = systemProperties.propertyNames();
		while (names.hasMoreElements()) {

			String name = (String)names.nextElement();
			if (name.startsWith("java.util.logging")) {
				config.setProperty(name, systemProperties.getProperty(name));
			} else if (name.startsWith(Logging.class.getPackage().getName())) {
				config.setProperty(name, systemProperties.getProperty(name));
			} else if (name.equals("handlers")) {
				config.setProperty(name, systemProperties.getProperty(name));
			} else if (name.equals(".level")) {
				config.setProperty(name, systemProperties.getProperty(name));
			}
		}


		/*
		 * If the java.util.logging.FileHandler.pattern property is set
		 * and the FileHandler is one of the handlers make sure the 
		 * directory exists
		 */
		String handlers = config.getProperty("handlers");
		if (handlers != null) {

			if (handlers.indexOf("java.util.logging.FileHandler") != -1) {

				String pattern = config.getProperty(
					"java.util.logging.FileHandler.pattern");

				if (pattern != null && pattern.length() > 0) {

					pattern = pattern.replace('\\', '/');
					String userHome = System.getProperty("user.home");
					userHome = userHome.replace('\\', '/');
					pattern = pattern.replaceAll("%h", userHome);

					File f = new File(pattern);
					f = f.getParentFile();
					if (!f.exists()) {
						f.mkdirs();
					}
				}
			}
		}

		/*
		 * Now write the config Properties out so we can get an InputStream
		 * representing it to pass to LogManager.readConfiguration()
		 */
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		config.store(bout, null);
		bout.flush();
		ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());


		/*
		 * Lastly get the LogManager singleton and call readConfiguration()
		 * on it with the assembled configuration
		 */
		LogManager logManager = LogManager.getLogManager();
		logManager.readConfiguration(bin);

		bout.close();
		bin.close();

		initialized = true;
	}


	/**
	 * Initialize the logging configuration with the properties in the
	 * specified filename.
	 *
	 * @param  filename  the properties filename
	 * @throws IOException if any IO error occurs
	 * @see #initialize(InputStream in)
	 */
	public static synchronized void initialize (String filename) 
			throws IOException {


		FileInputStream in;
		try {
			in = new FileInputStream(filename);
		} catch (IOException ioe) {
			in = null;
		}

		initialize(in);

		if (in != null) {

			try {
				in.close();
				in = null;
			} catch (IOException ignore) {
			}
		}
	}


	/**
	 * Initialize the logging configuration. 
	 *
	 * @throws IOException if any IO error occurs
	 * @see #initialize(InputStream in)
	 */
	public static synchronized void initialize () throws IOException {

		if (!initialized) {
			initialize((InputStream)null);
		}
	}


	/**
	 * Gets a Logger instance using the specified name
	 * as the name for the Logger instance. If not already
	 * initialized, the java.util.logging.LogManager will
	 * be configured using a call to initialize().
	 *
	 * @param  name  the name for the logger
	 * @return the Logger instance
	 */
	public static synchronized Logger getLogger (String name) {

		if (!initialized) {

			try {
				initialize((InputStream)null);
			} catch (IOException ignore) {
			}
		}

		return (Logger.getLogger(name));
	}


	/**
	 * Gets a Logger instance using the name of the specified package
	 * as the name for the Logger instance. If not already initialized,
	 * the java.util.logging.LogManager will be configured using a call
	 * to initialize().
	 *
	 * @param  pkg  the Package instance to get a Logger for
	 * @return the Logger instance
	 */
	public static Logger getLogger (Package pkg) {
		return (getLogger(pkg.getName()));
	}


	/**
	 * Gets a Logger instance using the package name of the specified
	 * class as the name for the Logger instance. If not already
	 * initialized, the java.util.logging.LogManager will
	 * be configured using a call to initialize().
	 *
	 * @param  cls  the Class instance to get a Logger for
	 * @return the Logger instance
	 */
	public static Logger getLogger (Class<ObjectLoader> cls) {

		if (cls == null) {
			return (getLogger(""));
		} else if (cls.getPackage() == null) {
			return (getLogger(cls.getName()));
		} else {
			return (getLogger(cls.getPackage().getName()));
		}
	}


	/**
	 * Gets a Logger instance using the package name of the class of the
	 * specified object as the name for the Logger instance. If not already
	 * initialized, the java.util.logging.LogManager will be configured using
	 * a call to initialize().
	 *
	 * @param  obj the Object instance to get a Logger for
	 * @return the Logger instance
	 */
	public static Logger getLogger (Object obj) {
		return (getLogger(obj.getClass().getPackage().getName()));
	}


	/**
	 * This method will create and return  PrintStream that is redirected to
	 * the named Logger instance.
	 *
	 * @param  name  the name of the Logger to redirect the PrintStream to
	 */
	public static PrintStream redirect (String name) {

		Logger logger = getLogger(name);
		return (redirect(logger));
	}


	/**
	 * This method will create and return PrintStream that is redirected to
	 * the specified Logger instance.
	 *
	 * @param  logger the Logger to redirect the PrintStream to
	 */
	public static PrintStream redirect (Logger logger) {

		PipedOutputStream pout = new PipedOutputStream();
		PrintStream ps = new PrintStream(pout);

		RedirectPipeThread rpt = new RedirectPipeThread(logger, pout);
		rpt.start();

		return (ps);
	}


	private static class RedirectPipeThread extends Thread {

		private PipedInputStream pin;
		private PipedOutputStream pout;
		private Logger logger;

		public RedirectPipeThread (
				Logger logger, PipedOutputStream pout) {

			pin = new PipedInputStream();
			this.pout = pout;
			this.logger = logger;
		}


		public void run () {

			try {

				pin.connect(pout);
				while (true) {

					String line = StringUtils.readLine(pin);
					if (line != null && line.length() > 0) {
						logger.info(line);
					}
				}

			} catch (IOException ioe) {
			}
		}
	}


	/**
	 * This method will read all input from the specified InputStream and
	 * log it to the specified logger on a line basis.
	 *
	 * @param  in     the InputStream to redirect to the logger
	 * @param  logger the Logger to redirect to
	 */
	public static void redirect (InputStream in, Logger logger) {

		RedirectInputThread rit = new RedirectInputThread(in, logger);
		rit.start();
	}


	private static class RedirectInputThread extends Thread {

		private InputStream in;
		private Logger logger;

		public RedirectInputThread (InputStream in, Logger logger) {

			this.in = in;
			this.logger = logger;
		}


		public void run () {

			try {

				while (true) {

					String line = StringUtils.readLine(in);
					if (line != null && line.length() > 0) {
						logger.info(line);
					}
				}

			} catch (IOException ioe) {
			}
		}
	}
}

