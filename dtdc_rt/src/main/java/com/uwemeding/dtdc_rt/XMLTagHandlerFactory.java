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
package com.uwemeding.dtdc_rt;

import com.uwemeding.dtdc_rt.parser.XMLCParser;
import com.uwemeding.dtdc_rt.parser.XMLParser;
import com.uwemeding.dtdc_rt.tree.XMLTreeParser;
import com.uwemeding.dtdc_rt.tree.XMLTreeTagHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * The XML tag handler factory
 *
 * @author uwe
 */
public class XMLTagHandlerFactory {

	private final static boolean DEBUG = false;
	private static final Map<String, XMLTagHandler> tagHandlers;

	static {
		tagHandlers = new HashMap<>();
	}

	/**
	 * Constructor for the XMLTagHandlerFactory object
	 */
	public XMLTagHandlerFactory() {
	}

	/**
	 * Description of the Method
	 *
	 * @param name Description of the Parameter
	 * @param forceNew Description of the Parameter
	 * @exception XMLException Description of the Exception
	 */
	public static synchronized void createXMLTagHandler(String name, boolean forceNew)
			throws XMLException {
	}

	/**
	 * Description of the Method
	 *
	 * @param name Description of the Parameter
	 * @return Description of the Return Value
	 * @exception XMLException Description of the Exception
	 */
	public static synchronized XMLTagHandler findXMLTagHandler(String name)
			throws XMLException {
		return findXMLTagHandler(name, false);
	}

	/**
	 * Description of the Method
	 *
	 * @param name Description of the Parameter
	 * @param forceNew Description of the Parameter
	 * @return Description of the Return Value
	 * @exception XMLException Description of the Exception
	 */
	public static synchronized XMLTagHandler findXMLTagHandler(String name, boolean forceNew)
			throws XMLException {
		if (DEBUG) {
			System.out.println("trying to find '" + name + "'");
		}
		XMLTagHandler th = tagHandlers.get(name);
		if (th == null) {
			locateTagHandler(name);
			// try it again
			th = tagHandlers.get(name);
			if (th == null) {
				th = getDefaultXMLTagHandler();
			}
		}

		if (th != null) {
			th.initHandler();
		}

		if (DEBUG) {
			System.out.println("Tag handler: " + th);
		}

		return th;
	}

	/**
	 * Description of the Method
	 *
	 * @param name Description of the Parameter
	 */
	private static synchronized void locateTagHandler(String name) {
		if (DEBUG) {
			System.out.println("Locating tag handler: " + name);
		}
		name = mapName(name);

		if (DEBUG) {
			System.out.println("---->: " + name);
		}

		XMLCompiledDTD handler = null;
		String handlerPath = System.getProperty("xml.cpool.handlers");
		handlerPath += "|dtdc.xml.cpool";
		StringTokenizer st = new StringTokenizer(handlerPath, "|");
		while (handler == null && st.hasMoreTokens()) {
			String packageName = st.nextToken().trim();

			String className = packageName + "." + name + ".CompiledDTD";
			if (DEBUG) {
				System.out.println("Looking for " + className);
			}

			Class clazz = null;
			try {
				try {
					clazz = Class.forName(className);
				} catch (ClassNotFoundException e) {
					if (DEBUG) {
						e.printStackTrace(System.out);
					}

					ClassLoader cl = ClassLoader.getSystemClassLoader();
					if (cl != null) {
						clazz = cl.loadClass(className);
					}
				}
				if (clazz != null) {
					handler = (XMLCompiledDTD) clazz.newInstance();
					registerCompiledDTD(handler);
				}
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | XMLException e) {
			}
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param name Description of the Parameter
	 * @param packageName Description of the Parameter
	 * @return Description of the Return Value
	 * @exception XMLException Description of the Exception
	 */
	public static synchronized XMLTagHandler findFixedXMLTagHandler(String name, String packageName)
			throws XMLException {
		if (DEBUG) {
			System.out.println("trying to find '" + name + "' in " + packageName);
		}

		XMLTagHandler th = tagHandlers.get(name);
		if (th != null) {
			return th;
		}

		XMLCompiledDTD handler;
		/// @todo: loacate the Compiled DTD in the package and register

		String className = packageName + ".CompiledDTD";
		if (DEBUG) {
			System.out.println("Looking for " + className);
		}

		Class clazz = null;
		try {
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				if (DEBUG) {
					e.printStackTrace(System.out);
				}

				ClassLoader cl = ClassLoader.getSystemClassLoader();
				if (cl != null) {
					clazz = cl.loadClass(className);
				}
			}
			if (clazz != null) {
				handler = (XMLCompiledDTD) clazz.newInstance();
				registerCompiledDTD(handler, name);
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | XMLException e) {
		}
		th = (XMLTagHandler) tagHandlers.get(name);
		if (th != null) {
			th.initHandler();
		}
		return th;
	}

	/**
	 * Description of the Method
	 *
	 * @param name Description of the Parameter
	 * @return Description of the Return Value
	 */
	private static String mapName(String name) {
		char[] buffer = new char[name.length()];
		name.getChars(0, name.length(), buffer, 0);
		StringBuilder sb = new StringBuilder();

		int length = buffer.length;
		boolean makeUC = false;
		for (int index = 0; index < length; index++) {
			if (Character.isLetterOrDigit(buffer[index])) {
				if (makeUC) {
					sb.append(Character.toUpperCase(buffer[index]));
					makeUC = false;
				} else {
					sb.append(buffer[index]);
				}
			} else {
				makeUC = true;
			}
		}


		String mapped = sb.toString();
		char c = mapped.charAt(0);
		if (Character.isUpperCase(c)) {
			c = Character.toLowerCase(c);
		}
		mapped = "" + c + mapped.substring(1);
		return mapped;
	}

	/**
	 * Check if the named element is registered.
	 *
	 * @param name Description of the Parameter
	 * @return The registered value
	 */
	public static synchronized boolean isRegistered(String name) {
		return tagHandlers.get(name) != null;
	}

	/**
	 * set up a compiled DTD so we can parse it, throws an XMLException if the
	 * setup did not complete
	 *
	 * @param cdtd Description of the Parameter
	 * @exception XMLException Description of the Exception
	 */
	public static synchronized void registerCompiledDTD(XMLCompiledDTD cdtd)
			throws XMLException {
		registerCompiledDTD(cdtd, new XMLCParser(), null);
	}

	/**
	 * set up a compiled DTD so we can parse it, throws an XMLException if the
	 * setup did not complete
	 *
	 * @param cdtd Description of the Parameter
	 * @param name Description of the Parameter
	 * @exception XMLException Description of the Exception
	 */
	public static synchronized void registerCompiledDTD(XMLCompiledDTD cdtd, String name)
			throws XMLException {
		registerCompiledDTD(cdtd, new XMLCParser(), name);
	}

	/**
	 * Description of the Method
	 *
	 * @param cdtd Description of the Parameter
	 * @param parser Description of the Parameter
	 * @exception XMLException Description of the Exception
	 */
	public static synchronized void registerCompiledDTD(XMLCompiledDTD cdtd, XMLParser parser)
			throws XMLException {
		registerCompiledDTD(cdtd, parser, null);
	}

	/**
	 * Description of the Method
	 *
	 * @param cdtd Description of the Parameter
	 * @param parser Description of the Parameter
	 * @param name Description of the Parameter
	 * @exception XMLException Description of the Exception
	 */
	public static synchronized void registerCompiledDTD(XMLCompiledDTD cdtd, XMLParser parser, String name)
			throws XMLException {
		if (name == null) {
			name = cdtd.getName();
			if (name == null) {
				throw new XMLException("Compiled DTD has neither an FPID nor a name");
			}
		}

		if (DEBUG) {
			System.out.println("registering compiled DTD: " + name);
		}

		// make sure we don't override an exisiting DTD, if we know
		// about it already we simply return
		Object o = tagHandlers.get(name);
		if (o != null) {
			return;
			//throw new XMLException("Attempt to override existing DTD '" + name + "'");
		}

		XMLTagHandler th = cdtd.getTagHandler(parser);
		if (th == null) {
			throw new XMLException("Compiled DTD does not define a tag handler");
		}

		if (DEBUG) {
			System.out.println("---> register: " + name + " " + th);
		}
		// finally register the new tag handler
		tagHandlers.put(name, th);
	}

	/**
	 * Return the default tag handler
	 *
	 * @return The defaultXMLTagHandler value
	 * @exception XMLException Description of the Exception
	 */
	public static synchronized XMLTagHandler getDefaultXMLTagHandler()
			throws XMLException {
		final XMLTreeTagHandler treeTagHandler = new XMLTreeTagHandler(new XMLTreeParser());
		treeTagHandler.initHandler();

		return treeTagHandler;
	}
}
