package com.messners.ajf.xml;

import org.xml.sax.ContentHandler;

/**
 * <p>This interface is used by the object building XmlParser which delegates
 * content handling on the contained <code>XMLReader</code> instance to
 * an implementation of this interface.</p>
 *
 * <p>The <code>org.xml.sax.ContentHandler</code> interface is extended so that
 * implementations of this interface can be used as the content handler
 * for <code>XMLReader</code>.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public interface XmlUnmarshaller extends ContentHandler {

	/**
	 * Sets up this instance's state with the provided <code>XmlParser</code>
	 * owner. Implementations of this interface will need this to maintain
	 * unmarshaller state using <code>XmlParser.pushUnmarshaller()</code>
	 * and <code>XmlParser.popUnmarshaller()</code>.
	 *
	 * @param  xmlParser    the owning <code>XmlParser</code> instance
	 */
	public void setXmlParser (XmlParser xmlParser);


	/**
	 * Gets the <code>XmlParser</code> owner of this instance. 
	 * Implementations of this interface will need this to maintain
	 * unmarshaller state using <code>XmlParser.pushUnmarshaller()</code>
	 * and <code>XmlParser.popUnmarshaller()</code>.
	 *
	 * @return the owning <code>XmlParser</code> instance
	 */
	public XmlParser getXmlParser ();


	/**
	 * <p>Sets up this instance's state with the provided parent
	 * <code>XmlUnmarshaller</code> instance. This is can and is
	 * used be used in the unmarshalling process to propogate
	 * information upwards while building up an object.
	 *
	 * @param  parent  the parent <code>XmlUnmarshaller</code> instance,
	 * this should be null if this instance is at the root of the unmarshal
	 * process
	 */
	public void setParent (XmlUnmarshaller parent);


	/**
	 * Gets the parent <code>XmlUnmarshaller</code> for this instance.
	 * This is can be used in the unmarshalling process to propogate
	 * information upwards while building up an object.
	 *
	 * @return the parent <code>XmlUnmarshaller</code> instance
	 */
	public XmlUnmarshaller getParent ();


	/**
	 * Gets the resultant object of this <code>XmlUnmarshaller</code>.  This
	 * method is called by containg <code>XmlUnmarshaller</code> instances
	 * and by the <code>XmlParser</code> to get the final results of a parse.
	 *
	 * @return resultant object of this <code>XmlUnmarshaller</code>
	 * @exception UnmarshalException if there is any exception generated 
	 * during unmarshalling
	 */
	public Object getResults () throws UnmarshalException;


	/**
	 * Process the specified child <code>XmlUnmarshaller</code>instance.
	 * This is and can be used in the unmarshalling process to propogate
	 * information upwards while building up an object.
	 *
	 * @param  child  the child <code>XmlUnmarshaller</code>
	 * @exception UnmarshalException if there is any exception generated 
	 * while processing the child
	 */
	public void processChild (XmlUnmarshaller child)
			throws UnmarshalException;


	/**
	 * Resets the state of this <code>XmlUnmarshaller</code> instance. This
	 * is usually called by the <code>XmlUnmarshallerFactory</code> instances
	 * when it re-uses (caches) <code>XmlUnmarshaller</code> instances.
	 */
	public void reset ();
}
