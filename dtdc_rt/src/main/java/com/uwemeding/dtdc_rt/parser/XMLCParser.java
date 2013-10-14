/*
 * Copyright (c) 1995-2008 Uwe B. Meding <uwe@uwemeding.com>
 *
 * This file is part of DTDC
 * This PCA software is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DTDC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DTDC.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.uwemeding.dtdc_rt.parser;

import com.uwemeding.dtdc_rt.StringPeer;
import com.uwemeding.dtdc_rt.Tag;
import com.uwemeding.dtdc_rt.TagPeer;
import com.uwemeding.dtdc_rt.XMLException;
import com.uwemeding.dtdc_rt.XMLStack;
import com.uwemeding.dtdc_rt.XMLTagHandler;
import com.uwemeding.dtdc_rt.XMLTagHandlerFactory;
import com.uwemeding.dtdc_rt.XMLTypes;
import com.uwemeding.dtdc_rt.tree.XMLTreeTagHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This is extended by the compiled parsers
 */
public class XMLCParser<E> implements XMLParser<E>, XMLTypes {

	private final static boolean DEBUG = false;
	private boolean haveCompiledParser = false;

	/**
	 * Description of the Method
	 *
	 * @param in Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception Description of the Exception
	 */
	public synchronized E parseXML(InputStream in)
			throws Exception {
		if (DEBUG) {
			System.out.println("XMLCParser: parsing from stream");
		}
		XMLParserHandler<E> handler = new XMLParserHandler<>();
		XMLProcessor proc = new XMLProcessor();
		proc.setHandler(handler);
		proc.parse(null, null, in, null);
		return handler.getObject();
	}

	/**
	 * Description of the Method
	 *
	 * @param in Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception Description of the Exception
	 */
	public synchronized E parseXML(Reader in)
			throws Exception {
		XMLParserHandler<E> handler = new XMLParserHandler<>();
		XMLProcessor proc = new XMLProcessor();
		proc.setHandler(handler);
		proc.parse(null, null, in);
		return handler.getObject();
	}

	/**
	 * Description of the Method
	 *
	 * @param th Description of the Parameter
	 * @param lex Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception Description of the Exception
	 */
	@Override
	public synchronized E parseXML(XMLTagHandler th, XMLLex lex)
			throws Exception {
		TagPeer tagPeer;
		TagPeer currentTagPeer = null;
		Tag tag;
		E object = null;
		XMLStack stack = new XMLStack();

		// show the contents of the tag handler
		while ((tag = lex.getNextTag()) != null) {
			if (DEBUG) {
				System.out.println(this + " XMLCParser: Tag=" + tag);
			}
			/*
			 *  Process this tag, also process text in between tags
			 */
			switch (tag.type) {
				case STAG:
					tagPeer = th.findTagPeer(tag.name);
					if (DEBUG) {
						System.out.println("Tag Peer: " + tagPeer);
					}
					if (tagPeer != null) {
						currentTagPeer = tagPeer;
						tagPeer.assignSlots(stack, tag);
						if (tag.hasImplicitEndTag) {
							object = (E) tagPeer.validateEntity(stack);
							// need to reset otherwise possible PCDATA is assigned wrong
							currentTagPeer = null;
						}
					}
					break;
				case ETAG:
					tagPeer = th.findTagPeer(tag.name);
					if (DEBUG) {
						System.out.println("Tag Peer/End tag: " + tagPeer);
					}
					if (tagPeer != null) {
						object = (E) tagPeer.validateEntity(stack);
					}
					currentTagPeer = null;
					break;
				default:
					if (currentTagPeer != null && currentTagPeer instanceof StringPeer) {
						StringPeer stringPeer = (StringPeer) currentTagPeer;
						if (DEBUG) {
							System.out.println("Applying string to " + stringPeer);
						}
						stringPeer.applyString(stack, tag);
						currentTagPeer = null;
					}
					break;
			}

		}
		return object;
	}

	/**
	 * Parse content from a DOM
	 *
	 * @param th Description of the Parameter
	 * @param document Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception Description of the Exception
	 */
	@Override
	public synchronized E parseXML(XMLTagHandler th, Document document)
			throws Exception {
		if (DEBUG) {
			System.err.println("**** Parse stuff from a DOM: " + th);
		}
		XMLStack stack = new XMLStack();
		//      DocumentImpl doc = (DocumentImpl)document;
		Element topElement = document.getDocumentElement();

		/*
		 *  Create a  simple array with all the args we need access to
		 *  we do this in lieu of yet another full blown class just for
		 *  holding several object.
		 */
		Object[] args = new Object[4];
		args[0] = null;
		// we expect the parsed object here
		args[1] = stack;
		args[2] = th;
		args[3] = null;
		// current tag peer we need to preserve

		Object[] result = walkTree(topElement, args);

		return (E) result[0];
	}

	/**
	 * Description of the Method
	 *
	 * @param node Description of the Parameter
	 * @param args Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception Description of the Exception
	 */
	private Object[] walkTree(Node node, Object[] args)
			throws Exception {
		XMLStack stack = (XMLStack) args[1];
		XMLTagHandler th = (XMLTagHandler) args[2];
		TagPeer currentTagPeer = (TagPeer) args[3];
		TagPeer tagPeer;
		Tag tag;
		Object object = null;

		int type = node.getNodeType();
		switch (type) {
			case Node.ELEMENT_NODE:
				/*
				 *  Create a tag for this element
				 */
				tag = new Tag(Tag.STAG, node.getNodeName());
				Attr attrs[] = sortAttributes(node.getAttributes());
				for (int i = 0; i < attrs.length; i++) {
					Attr attr = attrs[i];
					tag.addAttribute(attr.getNodeName(), attr.getNodeValue());
				}

				tagPeer = th.findTagPeer(tag.name);
				if (DEBUG) {
					System.out.println("Tag Peer: " + tagPeer);
				}
				if (tagPeer != null) {
					currentTagPeer = tagPeer;
					tagPeer.assignSlots(stack, tag);
					if (tag.hasImplicitEndTag) {
						object = tagPeer.validateEntity(stack);
					}

				}

				// recurse through all the children
				NodeList children = node.getChildNodes();
				if (children != null) {
					int len = children.getLength();
					for (int i = 0; i < len; i++) {
						args[3] = currentTagPeer;
						args = walkTree(children.item(i), args);
					}
				}
				break;
			case Node.CDATA_SECTION_NODE:
			case Node.TEXT_NODE:
				tag = new Tag(Tag.STRING, node.getNodeValue());
				if (currentTagPeer != null && currentTagPeer instanceof StringPeer) {
					StringPeer stringPeer = (StringPeer) currentTagPeer;
					if (DEBUG) {
						System.out.println("Applying string to " + stringPeer);
					}
					stringPeer.applyString(stack, tag);
					currentTagPeer = null;
				}

				break;
		}

		if (type == Node.ELEMENT_NODE) {
			tag = new Tag(Tag.ETAG, node.getNodeName());
			tagPeer = th.findTagPeer(tag.name);
			if (DEBUG) {
				System.out.println("Tag Peer/End tag: " + tagPeer);
			}
			if (tagPeer != null) {
				object = tagPeer.validateEntity(stack);
			}
			currentTagPeer = null;
		}

		args[0] = object;
		args[3] = currentTagPeer;
		return args;
	}

	/**
	 * Returns a sorted list of attributes.
	 *
	 * @param attrs Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected Attr[] sortAttributes(NamedNodeMap attrs) {

		int len = (attrs != null) ? attrs.getLength() : 0;
		Attr array[] = new Attr[len];
		for (int i = 0; i < len; i++) {
			array[i] = (Attr) attrs.item(i);
		}

		for (int i = 0; i < len - 1; i++) {
			String name = array[i].getNodeName();
			int index = i;
			for (int j = i + 1; j < len; j++) {
				String curName = array[j].getNodeName();
				if (curName.compareTo(name) < 0) {
					name = curName;
					index = j;
				}
			}
			if (index != i) {
				Attr temp = array[i];
				array[i] = array[index];
				array[index] = temp;
			}
		}

		return array;
	}

	// sortAttributes(NamedNodeMap):Attr[]
	/**
	 * Description of the Method
	 *
	 * @param message Description of the Parameter
	 * @exception XMLException Description of the Exception
	 */
	public void error(String message)
			throws XMLException {
		System.out.println("XMLError: " + message);
	}

	/**
	 * Description of the Class
	 *
	 * @author meding
	 * @created August 1, 2002
	 */
	private class XMLParserHandler<E> implements XMLHandler {

		E object;
		Tag tag;
		XMLTagHandler th;
		XMLStack stack;
		TagPeer tagPeer;
		TagPeer currentTagPeer;

		/**
		 * Constructor for the XMLParserHandler object
		 *
		 * @exception Exception Description of the Exception
		 */
		public XMLParserHandler()
				throws Exception {
			tag = new Tag(Tag.THE_EOF, "");
			stack = new XMLStack();
			th = XMLTagHandlerFactory.getDefaultXMLTagHandler();
		}

		/**
		 * Return the parsed object
		 *
		 * @return The object value
		 */
		public E getObject() {
			return object;
		}

		/**
		 * Handle the start of the document. <p>
		 *
		 * The default implementation does nothing.
		 *
		 * @exception Exception Derived methods may throw exceptions.
		 * @see dtdc_rt.parser.XMLHandler#startDocument
		 */
		@Override
		public void startDocument()
				throws Exception {
		}

		/**
		 * Handle the end of the document. <p>
		 *
		 * The default implementation does nothing.
		 *
		 * @exception Exception Derived methods may throw exceptions.
		 * @see dtdc_rt.parser.XMLHandler#endDocument
		 */
		@Override
		public void endDocument()
				throws Exception {
		}

		/**
		 * Resolve an external entity. <p>
		 *
		 * @param publicId Description of the Parameter
		 * @param systemId Description of the Parameter
		 * @return Description of the Return Value
		 * @exception Exception Derived methods may throw exceptions.
		 * @see dtdc_rt.parser.XMLHandler#resolveEntity
		 */
		@Override
		public Object resolveEntity(String publicId, String systemId)
				throws Exception {
			// we don'e care about empty system references
			if (systemId == null || systemId.trim().length() == 0) {
				return new StringReader("");
			}

			// @todo: should we enforce urls? we could do some getURL
			// magic.
			File file = null;
			try {
				file = new File(systemId);
				Reader reader = new BufferedReader(new FileReader(file));
				return (reader);
			} catch (Exception e) {
				// we really need to do something different here
				System.out.println("Unable to open file " + file + ", continuing");
			}
			return new StringReader("");
		}

		/**
		 * Handle the start of an external entity. <p>
		 *
		 * The default implementation does nothing.
		 *
		 * @param systemId Description of the Parameter
		 * @exception Exception Derived methods may throw exceptions.
		 * @see dtdc_rt.parser.XMLHandler#startExternalEntity
		 */
		@Override
		public void startExternalEntity(String systemId)
				throws Exception {
		}

		/**
		 * Handle the end of an external entity. <p>
		 *
		 * The default implementation does nothing.
		 *
		 * @param systemId Description of the Parameter
		 * @exception Exception Derived methods may throw exceptions.
		 * @see dtdc_rt.parser.XMLHandler#endExternalEntity
		 */
		@Override
		public void endExternalEntity(String systemId)
				throws Exception {
		}

		/**
		 * Handle a document type declaration. <p>
		 *
		 * The default implementation does nothing.
		 *
		 * @param name Description of the Parameter
		 * @param publicId Description of the Parameter
		 * @param systemId Description of the Parameter
		 * @exception Exception Derived methods may throw exceptions.
		 * @see dtdc_rt.parser.XMLHandler#doctypeDecl
		 */
		@Override
		public void doctypeDecl(String name, String publicId, String systemId)
				throws Exception {
			if (DEBUG) {
				System.out.println("DOCTYPE: name=" + name + " pid=" + publicId + " sid=" + systemId);
			}

			if (haveCompiledParser) {
				if (DEBUG) {
					System.out.println("*** have compiled parser already.");
				}
				return;
			}

			XMLTagHandler proposed;
			proposed = XMLTagHandlerFactory.findXMLTagHandler(name);
			if (DEBUG) {
				System.out.println("Current th=" + proposed);
			}
			if (proposed == null || proposed instanceof XMLTreeTagHandler) {
				// if we are a DTDC, get the compiled stuff
				th = getCompiledTagHandler(publicId);
				if (DEBUG) {
					System.out.println("Got the compiled shtuff: " + th);
				}
			} else {
				th = proposed;
			}

			if (th != null) {
				haveCompiledParser = true;
			}

			if (DEBUG) {
				System.out.println("---> TH = " + th.getClass());
			}
		}

		/**
		 * Gets the compiledTagHandler attribute of the XMLParserHandler object
		 *
		 * @param publicId Description of the Parameter
		 * @return The compiledTagHandler value
		 */
		private XMLTagHandler getCompiledTagHandler(String publicId) {
			if (DEBUG) {
				System.out.println("Get compiled tag handler: " + publicId);
			}
			XMLTagHandler proposed = null;

			if (null == publicId) {
				throw new IllegalStateException("XML error: public ID can not be null!");
			}

			try {
				String[] av = publicId.split(" ");
				if (DEBUG) {
					System.out.println("len=" + av.length + " av[0]=\"" + av[0] + "\"");
				}
				if (av.length >= 3 && haveExpectedFPIDCompanyName(av[0])) {
					proposed = proposeCompiledDTDHandler(publicId, av[1], av[2]);

//                    String name = XMLMap.mapHandlerFilename("dtdc_rt/cpool/resource/" + av[1] + ".properties");
//                    String type = av[1] + " " + av[2];
//                    // define the loose name
//
//                    if (DEBUG)
//                    {
//                        System.out.println("loading resource: [" + name + "]");
//                    }
//                    InputStream in = ClassLoader.getSystemResourceAsStream(name);
//                    if (in != null)
//                    {
//                        Properties props = new Properties();
//                        props.load(in);
//
//                        String pkg = props.getProperty(publicId, null);
//                        if (pkg == null)
//                        {
//                            pkg = props.getProperty(type, null);
//                        }
//                        else
//                        {
//                            type = publicId;
//                        }
//                        // use the more specific name
//                        if (DEBUG)
//                        {
//                            System.out.println("type=" + type + " pkg=" + pkg);
//                        }
//                        proposed = XMLTagHandlerFactory.findFixedXMLTagHandler(type, pkg);
//                    }
				}
			} catch (Exception e) {
//                e.printStackTrace();
				proposed = null;
			}
			return (proposed);
		}

		/**
		 * Based on the parameters, propose a compiled dtd/xml handler
		 */
		private XMLTagHandler proposeCompiledDTDHandler(String publicId, String dtdName, String dtdVersion)
				throws Exception {
			XMLTagHandler handler = null;
			String type = dtdName + " " + dtdVersion;
			String handlerPath = System.getProperty("dtdc.xml.handlers");
			handlerPath += "|dtdc.xml.cpool";
			StringTokenizer st = new StringTokenizer(handlerPath, "|");
			while (handler == null && st.hasMoreTokens()) {
				String packageName = st.nextToken().trim();
				packageName = packageName.replace('.', '/');
				String propName = XMLMap.mapHandlerFilename(packageName + "/resource/" + dtdName + ".properties");

				if (DEBUG) {
					System.out.println("Attempting to load '" + propName + "'");
				}

				InputStream in = ClassLoader.getSystemResourceAsStream(propName);

				if (DEBUG) {
					System.out.println("  resource stream " + in);
				}

				if (in != null) {
					Properties props = new Properties();
					props.load(in);
					String pkg = props.getProperty(publicId, null);
					if (pkg == null) {
						pkg = props.getProperty(type, null);
					} else {
						type = publicId;
					}

					if (DEBUG) {
						System.out.println("type=" + type + " pkg=" + pkg);
					}

					handler = XMLTagHandlerFactory.findFixedXMLTagHandler(type, pkg);
				}
			}
			return handler;
		}

		/**
		 * Determine if the fpid contains a company name for which we can expect
		 * to have a compiled dtd
		 */
		private boolean haveExpectedFPIDCompanyName(String name) {
			final Map<String, String> ht = buildExpectedCompanyNames();
			name = name.toUpperCase();

			if (DEBUG) {
				System.out.println("finding company name " + name);
				for (Map.Entry<String, String> entry : ht.entrySet()) {
					String n = entry.getKey();
					String v = entry.getValue();
					System.out.println("ht: n=" + n + " v=" + v);
				}

				System.out.println("found: " + ht.get(name));
			}

			return ht.get(name) != null;
		}

		/**
		 * Get the expected company names from the environment
		 */
		private Map<String, String> buildExpectedCompanyNames() {
			Map<String, String> ht = new HashMap<>();
			String cNames = System.getProperty("dtdc.company.names");
			cNames += "|MedingSoftwareTechnik";
			StringTokenizer st = new StringTokenizer(cNames, "|");
			while (st.hasMoreTokens()) {
				String company = st.nextToken().trim().toUpperCase();
				String snippet = "-//" + company + "//DTD";
				ht.put(snippet, company);
			}
			return ht;
		}

		/**
		 * Handle an attribute assignment. <p>
		 *
		 * The default implementation does nothing.
		 *
		 * @param aname Description of the Parameter
		 * @param value Description of the Parameter
		 * @param isSpecified Description of the Parameter
		 * @exception Exception Derived methods may throw exceptions.
		 * @see dtdc_rt.parser.XMLHandler#attribute
		 */
		@Override
		public void attribute(String aname, String value, boolean isSpecified)
				throws Exception {
			tag.addAttribute(aname, value);
		}

		/**
		 * Handle the start of an element. <p>
		 *
		 * The default implementation does nothing.
		 *
		 * @param elementName Description of the Parameter
		 * @exception Exception Derived methods may throw exceptions.
		 * @see dtdc_rt.parser.XMLHandler#startElement
		 */
		@Override
		public void startElement(String elementName)
				throws Exception {
			tag.name = elementName;
			tag.type = Tag.STAG;

			if (null == th) {
				throw new IllegalStateException("Tag Handler is null!");
			}
			tagPeer = th.findTagPeer(tag.name);
			if (tagPeer != null) {
				currentTagPeer = tagPeer;
				tagPeer.assignSlots(stack, tag);
			}
		}

		/**
		 * Handle the end of an element. <p>
		 *
		 * The default implementation does nothing.
		 *
		 * @param elementName Description of the Parameter
		 * @exception Exception Derived methods may throw exceptions.
		 * @see dtdc_rt.parser.XMLHandler#endElement
		 */
		@Override
		public void endElement(String elementName)
				throws Exception {
			tag.clearAttributes();
			tag.type = Tag.ETAG;

			tagPeer = th.findTagPeer(elementName);
			if (tagPeer != null) {
				object = (E) tagPeer.validateEntity(stack);
			}
			currentTagPeer = null;
		}

		/**
		 * Handle character data. <p>
		 *
		 * The default implementation does nothing.
		 *
		 * @param ch Description of the Parameter
		 * @param start Description of the Parameter
		 * @param length Description of the Parameter
		 * @exception Exception Derived methods may throw exceptions.
		 * @see dtdc_rt.parser.XMLHandler#charData
		 */
		@Override
		public void charData(char ch[], int start, int length)
				throws Exception {
			tag.clearAttributes();
			tag.type = Tag.STRING;
			tag.name = new String(ch, start, length);

			tagPeer = null;
			if (currentTagPeer != null && currentTagPeer instanceof StringPeer) {
				StringPeer stringPeer = (StringPeer) currentTagPeer;
				stringPeer.applyString(stack, tag);
				currentTagPeer = null;
			}
		}

		/**
		 * Handle ignorable whitespace. <p>
		 *
		 * The default implementation does nothing.
		 *
		 * @param ch Description of the Parameter
		 * @param start Description of the Parameter
		 * @param length Description of the Parameter
		 * @exception Exception Derived methods may throw exceptions.
		 * @see dtdc_rt.parser.XMLHandler#ignorableWhitespace
		 */
		@Override
		public void ignorableWhitespace(char ch[], int start, int length)
				throws Exception {
		}

		/**
		 * Handle a processing instruction. <p>
		 *
		 * The default implementation does nothing.
		 *
		 * @param target Description of the Parameter
		 * @param data Description of the Parameter
		 * @exception Exception Derived methods may throw exceptions.
		 * @see dtdc_rt.parser.XMLHandler#processingInstruction
		 */
		@SuppressWarnings({"DeadBranch", "UnusedAssignment"})
		@Override
		public void processingInstruction(String target, String data)
				throws Exception {
			if (DEBUG) {
				System.out.println("processing instr: " + target + " " + data);
			}

			if (haveCompiledParser) {
				if (DEBUG) {
					System.out.println("*** have compiled parser already.");
				}
				return;
			}

			if (data.startsWith("type=")) {
				StringTokenizer st = new StringTokenizer(data, " \t=\"");
				if (st.hasMoreTokens()) {
					st.nextToken();
				}
				if (st.hasMoreTokens()) {
					String handlerName = st.nextToken();
					if (DEBUG) {
						System.out.println("the handler name: " + handlerName);
					}

					XMLTagHandler proposed;
					proposed = XMLTagHandlerFactory.findXMLTagHandler(handlerName);
					if (DEBUG) {
						System.out.println("the handler: " + proposed);
					}
					if (proposed != null) {
						th = proposed;
					}

					/*
					 *  @todo: re-enable when we use the new dtd compiler for all dtd's
					 */
					if (false) {
						String name = XMLMap.mapHandlerFilename("dtdc_rt/cpool/resource/" + handlerName + ".properties");
						if (DEBUG) {
							System.out.println("loading resource: [" + name + "]");
						}
						String pkg;
						InputStream in = ClassLoader.getSystemResourceAsStream(name);
						if (in == null) {
							pkg = handlerName;
						} else {
							Properties props = new Properties();
							props.load(in);

							pkg = props.getProperty(handlerName, null);
						}
						// use the more specific name
						if (DEBUG) {
							System.out.println("type=" + handlerName + " pkg=" + pkg);
						}
						proposed = XMLTagHandlerFactory.findFixedXMLTagHandler(handlerName, pkg);

						if (DEBUG) {
							System.out.println("proposed handler=" + proposed);
						}

						if (proposed != null) {
							th = proposed;
						}
					}
				}
			}

			if (th != null) {
				haveCompiledParser = true;
			}
		}

		/**
		 * Throw an exception for a fatal error. <p>
		 *
		 * The default implementation throws
		 * <code>XMLException</code>.
		 *
		 * @param message Description of the Parameter
		 * @param systemId Description of the Parameter
		 * @param line Description of the Parameter
		 * @param column Description of the Parameter
		 * @exception Exception Derived methods may throw exceptions.
		 * @exception XMLException Description of the Exception
		 * @see dtdc_rt.parser.XMLHandler#error
		 */
		@Override
		public void error(String message, String systemId, int line, int column)
				throws XMLException, Exception {
			throw new XMLException(message, systemId, line, column);
		}
	}
}
