package com.messners.ajf.xml;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;

import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class wraps the standard <code>org.xml.sax.XMLReader</code> class
 * and integrates it with the MJF XML unmarsalling framework.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public abstract class XmlParser extends DefaultHandler {


	/**
	 * The <code>XmlUnmarshallerFactory</code> associated with this instance.
	 */
	protected XmlUnmarshallerFactory unmarshallerFactory;


	/**
	 * This stack is used to keep on the XmlUnmarshallerr instances.
	 */
	protected Stack<XmlUnmarshaller> unmarshallerStack;


	/**
	 * This holds the current XmlUnmarshaller, may be null.
	 */
	protected XmlUnmarshaller currentUnmarshaller;


	/**
	 * This holds the default XmlUnmarshaller, may be null.
	 */
	protected XmlUnmarshaller defaultUnmarshaller;


	/**
	 * The XMLReader this instance is associated with.
	 */
	protected XMLReader xmlReader;	


	/**
	 * The current location in the document.
	 */
	Locator locator;


	/**
	 * Holds the results from the root unmarshaller when it is popped off of
	 * the stack.  This object will be returned as the result of the parse.
	 */
	protected Object results;	


	/**
	 * Constructor for <code>XmlParser</code> which sets up the parser
	 * to utilize the provided {@link XmlUnmarshallerFactory}.
	 *
	 * @param  unmarshallerFactory  the <code>XmlUnmarshallerfactory</code>
	 * that is to be used to get <code>XmlUnmarshaller</code> instances
	 */
	public XmlParser (XmlUnmarshallerFactory unmarshallerFactory) {

		unmarshallerStack = new Stack<XmlUnmarshaller>();
		setUnmarshallerFactory(unmarshallerFactory);
	}

	/**
	 * Constructor for <code>XmlParser</code> which sets up the parser
	 * to use the provided {@link XmlUnmarshaller} to do the unmarshalling.
	 *
	 * @param  unmarshaller the <code>XmlUnmarshaller</code> to utilize
	 */
	public XmlParser (XmlUnmarshaller unmarshaller) {

		unmarshallerStack = new Stack<XmlUnmarshaller>();
		defaultUnmarshaller = unmarshaller;
		reset();
	}


	/**
	 * Parse the given <code>File</code> and return the resultant object.
	 *
	 * @param  f  the <code>File</code> instance to parse
	 * @return  the resultant object
	 * @exception  IOException if an I/O exception occurs
	 * @exception  UnmarshalException if a parsing exception occurs
	 */
	public abstract Object parse (File f)
			throws IOException, UnmarshalException;

	
	/**
	 * Parse the given <code>Reader</code> and return the resultant object.
	 *
	 * @param  reader  the <code>Reader</code> to parse
	 * @return the resultant object
	 * @exception  IOException if an I/O exception occurs
	 * @exception  Exception if a parsing exception occurs
	 */
	public abstract Object parse (Reader reader)
			throws IOException, UnmarshalException;

	
	/**
	 * Parse the given <code>InputStream</code> and return the resultant object.
	 *
	 * @param  in  the <code>InputStream</code> to parse
	 * @return  the resultant object
	 * @exception  IOException if an I/O exception occurs
	 * @exception  Exception if a parsing exception occurs
	 */
	public abstract Object parse (InputStream in)
			throws IOException, UnmarshalException;
	

	/**
	 * Parse the given <code>InputSource</code> and return the resultant object.
	 *
	 * @param  in  the <code>InputSource</code> to parse
	 * @return  the resultant object
	 * @exception  IOException if an I/O exception occurs
	 * @exception  UnmarshalException if a parsing exception occurs
	 */
	public abstract Object parse (InputSource in)
			throws IOException, UnmarshalException;


	/**
	 * Resets this instance to it's starting state, ready to parse again.
	 * This method should be called prior to reusing this instance to parse
	 * another XML document.  This method is automatically called by all
	 * of the parse() methods.
	 */
	public void reset () {

		results = null;
		unmarshallerStack.clear();

		currentUnmarshaller = defaultUnmarshaller;
		if (currentUnmarshaller != null) {
			currentUnmarshaller.reset();
			pushUnmarshaller(currentUnmarshaller);
		}
	}


	/**
	 * Gets the <code>XmlUnmarshallerFactory</code> associated
	 * with this instance.
	 *
	 * @return the <code>XmlUnmarshallerfactory</code> that is to
	 * used to get <code>XmlUnmarshaller</code> instances
	 */
	public XmlUnmarshallerFactory getUnmarshallerFactory () {
		return (unmarshallerFactory);
	}


	/**
	 * Sets the <code>XmlUnmarshallerFactory</code> for this instance.
	 *
	 * @param  unmarshallerFactory  the <code>XmlUnmarshallerfactory</code>
	 * that is to be used to get <code>XmlUnmarshaller</code> instances
	 */
	public void setUnmarshallerFactory (
				XmlUnmarshallerFactory unmarshallerFactory) {

		if (unmarshallerFactory == null) {
			throw new NullPointerException(
							"XmlUnmarshallerFactory cannot be null");
		}

		this.unmarshallerFactory = unmarshallerFactory;
	}


	/**
	 * Gets the <code>XMLReader</code> associated with this instance.
	 *
	 * @return  the <code>XMLReader</code> associated with this instance
	 */
	public XMLReader getXMLReader () {

		return (xmlReader);
	} 


	/**
	 * Sets the <code>XMLReader</code> associated with this instance.
	 *
	 * @param xmlReader the <code>XMLReader</code> to
	 *                  associate with this instance
	 */
	public void setXMLReader (XMLReader xmlReader) {

		this.xmlReader = xmlReader;

		/*
		 * If we have a current unmarshaller set it as the contentHandler
		 * for the XMLReader
		 */
		if (xmlReader != null && currentUnmarshaller != null) {
			xmlReader.setContentHandler(currentUnmarshaller);
		}
	} 


	/**
	 * Pops the last <code>XmlUnmarshaller</code> off of the stack. The side
	 * effect of this is that the unmarshaller at the top of the stack becomes
	 * the current unmarshaller, if the stack is empty then the
	 * currentUnmarshaller is set to null and this <code>XmlParser</code>
	 * instance becomes the contentUnmarshaller for the <code>XMLReader</code>.
	 *
	 * @param  unmarshaller  this is used to ensure state, it is an exception
	 * if this does not match the unmarshaller on the top of the stack
	 * @exception UnmarshalException  if the provided unmarshaller does not
	 * match the one on the top of the stack
	 */
	 public void popUnmarshaller (XmlUnmarshaller unmarshaller) 
			throws UnmarshalException {

		if (unmarshallerStack.empty()) {
			throw new UnmarshalException(
				"unmarshaller stack is empty");
		}

		if (currentUnmarshaller != unmarshaller) {
			throw new UnmarshalException(
				"unmarshaller is not on the top of the stack");
		}

	
		/*
		 * Pop this unmarshaller off of the stack, we do this now and peek at
		 * the top to find out who the current unmarshaller should be
		 */	
		unmarshallerStack.pop();


		/*
		 * Setup the new currentUnmarshaller by peeking at the stack
		 */
		if (!unmarshallerStack.empty()) {

			currentUnmarshaller = unmarshallerStack.peek();
			xmlReader.setContentHandler(currentUnmarshaller);

		} else {

			currentUnmarshaller = null;
			xmlReader.setContentHandler(this);
		}


		/*
		 * Process the popped unmarshaller depending on whether there is a 
		 * parent XmlUnmarshaller or not
		 */
		XmlUnmarshaller parent = unmarshaller.getParent();
		if (parent != null) {

			parent.processChild(unmarshaller);

		} else {

			results = unmarshaller.getResults();
		}
	}


	/**
	 * Pushes the provided <code>XmlUnmarshaller</code> on to this instances's
	 * stack and makes it the current unmarshaller, after pushing the
	 * unmarshaller onto the stack it is reset and the has its 
	 * startElement() method invoked.
	 *
	 * @param  unmarshaller  the <code>XmlUnmarshaller</code> to push on to
	 * the stack and make current
	 * @param  parent  the <code>XmlUnmarshaller</code> to set as the parent
	 * of the pushed unmarshaller
	 * @param namespaceUri
	 * @param localName
	 * @param qName
	 * @param attrs
	 * @exception SAXException if any error occurs
	 */
	public void pushUnmarshaller (
				XmlUnmarshaller unmarshaller, XmlUnmarshaller parent,
				String namespaceUri, String localName,
				String qName, Attributes attrs) throws SAXException {

		pushUnmarshaller(unmarshaller, parent);
		unmarshaller.reset();
		unmarshaller.startElement(namespaceUri, localName, qName, attrs);
	}


	/**
	 * Pushes the provided <code>XmlUnmarshaller</code> on to this instances's
	 * stack and makes it the current unmarshaller.
	 *
	 * @param  unmarshaller  the <code>XmlUnmarshaller</code> to push on to
	 * the stack and make current
	 */
	public void pushUnmarshaller (XmlUnmarshaller unmarshaller) {
		pushUnmarshaller(unmarshaller, null);
	}


	/**
	 * Pushes the provided <code>XmlUnmarshaller</code> on to this instances's
	 * stack and makes it the current unmarshaller.
	 *
	 * @param  unmarshaller  the <code>XmlUnmarshaller</code> to push on to
	 * the stack and make current
	 * @param  parent  the <code>XmlUnmarshaller</code> to set as the parent
	 * of the pushed unmarshaller
	 */
	public void pushUnmarshaller (
			XmlUnmarshaller unmarshaller, XmlUnmarshaller parent) {

		unmarshallerStack.push(unmarshaller);
		unmarshaller.setXmlParser(this);
		unmarshaller.setParent(parent);
		currentUnmarshaller = unmarshaller;

		if (xmlReader != null) {
			xmlReader.setContentHandler(currentUnmarshaller);
		}
	}


	/**
	 * Gets the results of the parse if any.
	 *
	 * @return  the resultant object of the parse
	 */
	public Object getResults () {
		return (results);
	}


	/**
	 * Sets the results of the parse if any.
	 *
	 * @param  results  the object to set as the resultant object of the parse
	 */
	public void setResults (Object results) {
		this.results = results;
	}


	/**
	 * Override the <code>Defaultunmarshaller.startElement()</code> method to
	 * dispatch to the proper <code>XmlUnmarshaller</code>.
	 */
	public void startElement (String namespaceUri, String localName,
					String qName, Attributes attrs) throws SAXException {

		/*
		 * If we do not have a current unmarshaller get one from the 
		 * unmarshallerFactory.
		 */
		if (currentUnmarshaller == null) {

			if (unmarshallerFactory == null) {
			
				throw new UnmarshalException(
					"no XmlUnmarshallerFactory provided");
			}

			/*
			 * This will throw a UnmarshalException if the
			 * unmarshaller is not found
			 */
			XmlUnmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(
									namespaceUri, localName, qName, attrs);
			pushUnmarshaller(unmarshaller, null);
		}


		/*
		 * Now set the contentUnmarshaller on the XMLReader instance and
		 * re-invoke the startElement() method
		 */
		xmlReader.setContentHandler(currentUnmarshaller);
		currentUnmarshaller.startElement(namespaceUri, localName, qName, attrs);
	}


	/**
	 * Gets the origin of the last SAX event. The locator field can be
	 * used when reporting errors or for debugging.
	 *
	 * @return  the location of the SAX event within the document
	 */
	public Locator getDocumentLocator () {
		return (locator);
	}


	/**
	 * Set the origin of the last SAX event. The locator field can be
	 * used when reporting errors or for debugging.
	 *
	 * @param  locator  the location of the SAX event within the document
	 */
	public void setDocumentLocator (Locator locator) {
		this.locator = locator;
	}
}
