package com.messners.ajf.xml;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class wraps the standard <code>javax.xml.parsers.SAXParser</code> class
 * and integrates it with the XML unmarsalling framework.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class XmlSaxParser extends XmlParser {


	/**
	 * This is the actual parser that we use to parse the XML.
	 */
	private SAXParser saxParser;



	/**
	 * Constructor for <code>XmlSaxParser</code> which sets up the parser
	 * to utilize the provided {@link XmlUnmarshallerFactory}.
	 *
	 * @param  unmarshallerFactory  the <code>XmlUnmarshallerfactory</code>
	 * that is to be used to get <code>XmlUnmarshaller</code> instances
	 */
	public XmlSaxParser (XmlUnmarshallerFactory unmarshallerFactory) {

		super(unmarshallerFactory);
	}


	/**
	 * Constructor for <code>XmlSaxParser</code> which sets up the parser
	 * to use the provided {@link XmlUnmarshaller} to do the unmarshalling.
	 *
	 * @param  unmarshaller the <code>XmlUnmarshaller</code> to utilize
	 */
	public XmlSaxParser (XmlUnmarshaller unmarshaller) {

		super(unmarshaller);
	}


	/**
	 * Parse the given <code>File</code> and return the resultant object.
	 *
	 * @param  f  the <code>File</code> instance to parse
	 * @return  the resultant object
	 * @exception  IOException if an I/O exception occurs
	 * @exception  UnmarshalException if a parsing exception occurs
	 */
	public Object parse (File f) throws IOException, UnmarshalException {

		try {

			/*
			 * Create the SAXParser if not already created
			 */
			if (saxParser == null) {
				saxParser = getSAXParser();
				setXMLReader(saxParser.getXMLReader());
			}
		
			reset();

			saxParser.parse(f, this);

		} catch (UnmarshalException ue) {

			throw (ue);

		} catch (SAXException se) {

			String message = se.getMessage();
			Exception e = se.getException();
			throw new UnmarshalException(message, e);
		}

		return (getResults());
	}	

	
	/**
	 * Parse the given <code>InputStream</code> and return the resultant object.
	 *
	 * @param  in  the <code>InputStream</code> to parse
	 * @return  the resultant object
	 * @exception  IOException if an I/O exception occurs
	 * @exception  UnmarshalException if a parsing exception occurs
	 */
	public Object parse (InputStream in)
			throws IOException, UnmarshalException {

		try {

			/*
			 * Create the SAXParser if not already created
			 */
			if (saxParser == null) {
				saxParser = getSAXParser();
				setXMLReader(saxParser.getXMLReader());
			}
		
			reset();

			saxParser.parse(in, this);

		} catch (UnmarshalException ue) {

			throw (ue);

		} catch (SAXException se) {

			UnmarshalException ue = new UnmarshalException(
					se.getMessage(), se.getException());
			throw (ue);
		}

		return (getResults());
	}	
	

	/**
	 * Parse the given <code>Reader</code> and return the resultant object.
	 *
	 * @param  reader  the <code>Reader</code> to parse
	 * @return the resultant object
	 * @exception  IOException if an I/O exception occurs
	 * @exception  Exception if a parsing exception occurs
	 */
	public Object parse (Reader reader) throws IOException, UnmarshalException {
		return (parse(new InputSource(reader)));
	}

	
	/**
	 * Parse the given <code>InputSource</code> and return the resultant object.
	 *
	 * @param  in  the <code>InputSource</code> to parse
	 * @return  the resultant object
	 * @exception  IOException if an I/O exception occurs
	 * @exception  UnmarshalException if a parsing exception occurs
	 */
	public Object parse (InputSource in) 
			throws IOException, UnmarshalException {

		try {

			/*
			 * Create the SAXParser if not already created
			 */
			if (saxParser == null) {
				saxParser = getSAXParser();
				setXMLReader(saxParser.getXMLReader());
			}
		
			reset();

			saxParser.parse(in, this);

		} catch (UnmarshalException ue) {

			throw (ue);

		} catch (SAXException se) {

			UnmarshalException ue = new UnmarshalException(
					se.getMessage(), se.getException());
			ue.fillInStackTrace();
			throw (ue);
		}

		return (getResults());
	}	


	/**
	 * Get a new SAXParser instance.
	 */
	private SAXParser getSAXParser () throws UnmarshalException {

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			return (saxParser);

		} catch (SAXException se) {

			UnmarshalException ue = new UnmarshalException(
					se.getMessage(), se.getException());
			ue.fillInStackTrace();
			throw (ue);

		} catch (ParserConfigurationException pce) {

			UnmarshalException ue = new UnmarshalException(pce);
			ue.fillInStackTrace();
			throw (ue);
		}
	}
}
