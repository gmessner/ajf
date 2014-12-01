package com.messners.ajf.util;

import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * XmlProperties extends the <code>java&#046;util&#046;Properties</code> class
 * and stores the properties in a hierarchial XML fashion vs&#046; the
 * dot notation typically used in a Properties file.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class XmlProperties extends java.util.Properties {

	private static final long serialVersionUID = 1L;

	/**
	 * Default character used to separate the sections in the hierarchy.
	 */
    public static final String DEFAULT_SEPARATOR = ".";

	/**
	 * The default root node name when outputing the XML properties.
	 */
	public static final String DEFAULT_ROOT_NODE = "properties";

	/**
	 * The root node for the XML output.
	 */
	protected String rootNode = DEFAULT_ROOT_NODE;

	/**
	 * This DocumentBuilder is used internally to build, parse, and\
	 * output DOM Documents.
	 */
	protected DocumentBuilder builder;
    

    /**
	 * Creates an XmlProperties instance.
     */
    public XmlProperties () {

        super();
    }
    
    /**
	 * Creates an XmlProperties object using the provided defaults.
	 *
	 * @param  defaults  the default properties
     */
    public XmlProperties (Properties defaults){
        super(defaults);
    }


	/**
	 * Gets the root node for the output XML.
	 */
	public String getRootNode () {
		return (rootNode);
	}


	/**
	 * Sets the root node for the output XML. If rootNode is null
	 * will be set to <code>DEFUALT_ROOT_NODE</code>.
	 */
	public void setRootNode (String rootNode) {

		if (rootNode == null || rootNode.length() == 0) {
			this.rootNode = DEFAULT_ROOT_NODE;
		} else {
			this.rootNode = rootNode;
		}
	}


    /**
	 * Stores this set of properties in standard Properties format (not XML)
	 * to the specified output stream, with no header.
	 *
	 * @param out the OutputStream to store to
     */
    public void storeAsProperties (OutputStream out) throws IOException {
		storeAsProperties(out, null);
	}


    /**
	 * Stores this set of properties in standard Properties format (not XML)
	 * to the specified output stream, with the specified header.
	 *
	 * @param out the OutputStream to store to
     * @param header the header information to include at the top of the file
     */
    public void storeAsProperties (
				OutputStream out, String header) throws IOException {
		super.store(out, header);
	}

    
    /**
	 * Reads an XML formatted property list from the <code>InputStream</code>.
	 *
     * @param in  the <code>InputStream</code> to read the properties from
     */
    public void load (InputStream in) throws IOException {
        merge(null, in);
    }

    
    /**
	 * Merges the properties from the specified Properties instance with this
	 * instance under the specified section.
     *
     * @param section the name of the to merge these properties, use null or ""
	 * to merge at the root level
     * @param  props the <code>Properties</code> instance to merge
	 */
    public void merge (String section, Properties props) {

		if (section == null) {
			section = "";
		}

		Enumeration<?> names = props.propertyNames();
		while (names.hasMoreElements()) {

      		String name = (String)names.nextElement();
       		put(section + DEFAULT_SEPARATOR + name, props.getProperty(name));
    	} 
    }
    

    /**
	 *
	 * Merges the properties from the provided <code>InputStream</code> with
	 * this instance under thew specified section.
     *
     * @param section the name of the to merge these properties, use null or ""
	 * to merge at the root level
     * @param in  the <code>InputStream</code> to read the properties from
     */
    public void merge (String section, InputStream in) throws IOException {

		try {

        	SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(in, new SaxHandler(section));

        } catch (SAXException se) {

			IOException ioe = new IOException(se.getMessage());
			ioe.fillInStackTrace();
			throw (ioe);

        } catch (ParserConfigurationException pce) {

			IOException ioe = new IOException(pce.getMessage());
			ioe.fillInStackTrace();
			throw (ioe);
        }
    }

    
    /**
	 * Stores this set of properties to the specified output stream, 
	 * with no header.
	 *
	 * @param out the OutputStream to store to
     */
    public void store (OutputStream out) throws IOException {
        store(out, null);
    }
    

    /**
	 * Stores this set of properties to the specified output stream, 
	 * with the specified header.
	 *
	 * @param out the OutputStream to store to
     * @param header the header information to include at the top of the file
     */
    public void store (OutputStream out, String header) throws IOException {

        PrintWriter writer = new PrintWriter(out);
		writer.println("<?xml version=\"1.0\"?>");


		/*
		 * Output the header as a comment at the top of the file
		 */
		if (header != null) {

            writer.print("<!-- ");
  			writer.print(header);
            writer.println(" -->");
		}
            
        Document doc = getDocument();
        write("", doc, writer);
        writer.flush();
		writer = null;
    }


    /**
     * Converts this instance back into a DOM Document. This is done by
     * splitting property names using the DEFAULT_SEPARATOR as a delimiter.
     *
     * @param doc the Document to insert this section into
     * @param element  the element to append the elements to
     * @param section the whole property key, for lookup purposes.
     * @param offset the current division offset from the start
     * of the key string. This 
     * used to mark the level of nesting that is currently being processed.
     * @return The completely built structure with elem as the base Element.
     */
    protected Element buildSection (Document doc, Element element,
			String section, int offset) {

      	if (offset < 0){
			return (null);
		}

      	int newOffset = offset;
      	int dividerPos = -1;
      	String currentPart = null;
      	String remainder = section.substring(offset, section.length());
        

      	if (remainder.length() > 0){

            dividerPos = remainder.indexOf(DEFAULT_SEPARATOR);
            if(dividerPos < 0){

                newOffset = section.length();
                currentPart = remainder;

            } else{

                newOffset = offset + dividerPos +1;
                currentPart = remainder.substring(0, dividerPos);
            }

      	} else {

            Node node = doc.createTextNode(getProperty(section));
            element.appendChild(node);
            return (element);
      	}

		NodeList list = element.getChildNodes();
		int numChildren = list.getLength();
   		boolean found = false;
   		for(int i = 0; i < numChildren; i++) {

         Node node = list.item(i);
         if(node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
            
         if(node.getNodeName().equals(currentPart)) {
                found = true;
                element.appendChild(buildSection(
					doc, (Element)node, section, newOffset));
            }
        }

        if (!found) {
            Element e = doc.createElement(currentPart);
            element.appendChild(buildSection(doc, e, section, newOffset));
        }
        
        return (element);
    }

    
    /**
	 * Gets a DOM Document representation of the contained properties.
	 *
	 * @return  a DOM <code>Document</code> representing the
	 * contained properties
     */
    public Document getDocument () throws IOException {

		if (builder == null) {
			builder = getBuilder();
		}

        try {

	        Document doc = builder.newDocument();
   			Element element = doc.createElement(rootNode);
            Enumeration<?> names = propertyNames();
            while (names.hasMoreElements()) {

                String name = (String)names.nextElement();
                element = buildSection(doc, element, name, 0);
            }

            doc.appendChild(element);
            return (doc);

        } catch (DOMException de) {

            IOException ioe = new IOException(de.getMessage());
			ioe.fillInStackTrace();
			throw (ioe);
        }
    }
    
    /**
	 * Gets the specified sub-section of this instance the form of another 
     * <code>XmlProperties</code> instance.
	 *
     * @param section the section name to retrieve
     * @return  the specified section as a new <code>XmlProperties</code>
	 * instance
     */
    public XmlProperties getSection (String section) throws Exception {

        XmlProperties props = new XmlProperties();
        Enumeration<?> names = propertyNames();
        while(names.hasMoreElements()) {

            String name = (String)names.nextElement();
            if (name.startsWith(section)) {

                props.put(
					name.substring(section.length() + 1, name.length()), 
					getProperty(name));
            }
        }
        

        if (props.isEmpty()) {
			throw new Exception (section + " not found");
		}
        
		return (props);
    }
    

	/**
	 * Get the DocumentBuilder to use with this instance.
	 */    
	protected DocumentBuilder getBuilder () throws IOException {

		try {

			DocumentBuilderFactory factory = 
				DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
			return (builder);
	
		} catch (ParserConfigurationException pce) {

			IOException ioe = new IOException(pce.getMessage());
			ioe.fillInStackTrace();
			throw (ioe);
		}
	}


	/**
	 * Output the specified node with a pretty print format.
	 *
	 * @param  tab   the current indent string
	 * @param  node  the Node to output
	 * @param  out   the PrintWriter to output to
	 */
	protected void write (
		String tab, Node node, PrintWriter out) throws IOException {

		if (tab == null) {
			tab = "";
		}

		switch (node.getNodeType()) {

			case Node.DOCUMENT_NODE:

				NodeList list = node.getChildNodes();
				int numNodes = list.getLength();
				for (int i = 0; i < numNodes; i++) {
					write(tab, list.item(i), out);
				}

				break;


			case Node.TEXT_NODE:

				normalizeAndPrint(node.getNodeValue(), out);
				break;


			case Node.ELEMENT_NODE:

				out.print(tab + '<' + node.getNodeName());
				NamedNodeMap attributes = node.getAttributes();
				int numAttributes = attributes.getLength();
				for (int i = 0; i < numAttributes; i++){
				
					Node attr = attributes.item(i);
					out.print(" " + attr.getNodeName() + "=\"" + 
						attr.getNodeValue() + "\"");
				}

			
				NodeList children = node.getChildNodes();
				int numChildren = children.getLength();
				if (numAttributes < numChildren) {
				
					out.print('>');
					boolean textDone = false;
					boolean elementDone = false;
					for (int i = 0; i < numChildren; i++){

						Node child = children.item(i);
						short type = child.getNodeType();
						if (type == Node.TEXT_NODE){

							if (textDone) {
								continue;
							} else {
								textDone = true;
							}
						
							if (elementDone) {
								out.print(tab);
								elementDone = false;
							}

							normalizeAndPrint(child.getNodeValue(), out);
							continue;

						} else if (type == Node.ELEMENT_NODE) {

							if (!elementDone && !textDone) {
								out.println();
							}

							elementDone = true;
							textDone = false;
							write(tab + "\t", child, out);
						}
					}

					if (elementDone) {
						out.print(tab);
					}

					out.println("</" + node.getNodeName() + '>');

				} else {

					out.println(" />");
				}

				break;
		}
	}



    /**
	 * Normalizes and prints the given array of characters.
	 */
    protected void normalizeAndPrint (String s, PrintWriter out) {

		if (s == null) {
			return;
		}

		int length = s.length();
        for (int i = 0; i < length; i++) {
            normalizeAndPrint(s.charAt(i), out);
        }
    }

    /**
	 * Normalizes and print the given character.
	 */
    protected void normalizeAndPrint (char c, PrintWriter out) {

        switch (c) {
            case '<':
                out.print("&lt;");
                break;

            case '>':
                out.print("&gt;");
                break;

            case '&':
                out.print("&amp;");
                break;

            default:
                out.print(c);
        }

    }

	/**
	 * This class is the hndler for the SAX events when the XML properties file
	 * is read.
	 */
	protected class SaxHandler extends DefaultHandler {

		private StringBuffer charBuffer;
		private Stack<String> nameStack = new Stack<String>();
		private boolean needRoot;


		/**
		 * Create a SaxHandler instance to handle the SAX events adding
		 * properties at the specified section name.
		 */
		protected SaxHandler (String name) {

			needRoot = false;
			if (name != null && name.length() > 0) {

				nameStack.push(name);

				Object obj = get(name);
				if (obj instanceof StringBuffer) {
					charBuffer = (StringBuffer)obj;
				} else if (obj != null) {
					charBuffer = new StringBuffer(obj.toString());
				} else {
					charBuffer = new StringBuffer();
				}
					
				put(name, charBuffer);		
			}
		}


		public void startDocument () throws SAXException {

			needRoot = true;
		}


		/**
		 * Receive notification of the beginning of a document.
		 */
	    public void startElement (String namespaceUri, String localName,
				String qName, Attributes attrs) throws SAXException {

			if (needRoot) {

				needRoot = false;
				if (qName.equalsIgnoreCase(getRootNode())) {
					setRootNode(qName);
					return;
				}
			}

			nameStack.push(qName);
			String name = getCurrentName();
			charBuffer = new StringBuffer();
			put(name, charBuffer);		
	   	}


		/**
		 * Receive notification of the end of an element.
		 */
	  	public void endElement (String namespaceUri,
				String localName, String qName) throws SAXException {

			if (nameStack.size() == 0) {
				return;
			}

			String name = getCurrentName();
			remove(name);
			nameStack.pop();
			String data = charBuffer.toString();
			setProperty(name, data);

			name = getCurrentName();
			if (name != null) {
				charBuffer = (StringBuffer)get(name);
			} else {
				charBuffer = null;
			}
	   	}


		/**
		 *  Receive notification of character data.
		 */
	   	public void characters (char buf[], int offset, int len)
				throws SAXException {

			if (charBuffer != null) {
	        	charBuffer.append(buf, offset, len);
			}
	   	}


		/**
		 * Gets a name (key) based on the element names that have been
		 * pushed on the name Stack.
		 */
		protected String getCurrentName () {

			int numNames = nameStack.size();
			if (numNames < 1) {
				return (null);
			}

			StringBuffer nameBuf = new StringBuffer();
			for (int i = 0; i < numNames; i++) {

				if (i > 0) {
					nameBuf.append(DEFAULT_SEPARATOR);
				}

				nameBuf.append(nameStack.get(i));
			}

			return (nameBuf.toString());
		}
	}
}
