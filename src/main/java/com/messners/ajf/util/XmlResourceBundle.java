package com.messners.ajf.util;

import java.io.InputStream;
import java.io.IOException;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;


/**
 * <code>XmlResourceBundle</code> is a concrete subclass of
 * <code>ResourceBundle</code> that manages resources for a locale
 * using a set of static strings from a property file.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class XmlResourceBundle extends ResourceBundle {

   private HashMap<String, Object> lookup;

	/**
     * Creates an XMLPropertyResourceBundle.
	 *
	 * @param stream property file to read from.
	 */
	public XmlResourceBundle (InputStream stream) throws IOException {

		/*
		 * See if this is an XML file
		 */
		if (stream.markSupported()) {

			stream.mark(0);
			byte buf[] = new byte[32];	
			int numRead = stream.read(buf);
			if (numRead < 32) {
				throw new IOException("file is not XML");
			}

			String s = new String(buf);
			if (!s.startsWith("<?xml")) {
				throw new IOException("file is not XML");
			}

			stream.reset();
		}


      XmlProperties properties = new XmlProperties();
      properties.load(stream);
      lookup = new HashMap<String, Object>(properties.size());
      for (final String name: properties.stringPropertyNames()) {
    	    lookup.put(name, properties.getProperty(name));
      }  
   }


	/**
	 * Gets an object for the given key from this resource bundle.
	 *
	 * @param  key  the key for the desired object
	 * @return  the object for the given key or null
	 */
	public Object handleGetObject (String key) {

        if (key == null) {
            throw new NullPointerException();
        }

        return (lookup.get(key));
    }


    /**
     * Gets an enumeration of the keys.
	 *
	 * @return  an <code>Enumeration</code> of the keys
     */
    public Enumeration<String> getKeys () {
		return (Collections.enumeration(lookup.keySet()));
    }
}
