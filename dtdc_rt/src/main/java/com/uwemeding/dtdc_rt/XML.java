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
import com.uwemeding.dtdc_rt.parser.XMLLex;
import com.uwemeding.dtdc_rt.parser.XMLParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XML<T> implements XMLTypes {

	private final static boolean DEBUG = false;
	/**
	 * Description of the Field
	 */
	private XMLTagHandler th;
	private boolean forceNewTagHandlers;

	/**
	 * Create an XML context
	 *
	 */
	public XML() {
		forceNewTagHandlers = true;
	}

	/**
	 * Add a compiled dtd handler to the XML parser system
	 *
	 * @param cdtd is the compiled dtd handle
	 * @exception Exception Description of the Exception
	 */
	public static void addCompiledDTD(XMLCompiledDTD cdtd) throws Exception {
		XMLTagHandlerFactory.registerCompiledDTD(cdtd);
	}

	/**
	 * Description of the Method
	 */
	private void init() {
	}

	/**
	 * Read an XML tagged file
	 *
	 * @param in Description of the Parameter
	 * @return Description of the Return Value
	 * @exception IOException Description of the Exception
	 * @exception XMLException Description of the Exception
	 * @exception Exception Description of the Exception
	 */
	public T readFile(Reader in)
			throws Exception, IOException, XMLException {
		init();
		XMLCParser<T> parser = new XMLCParser<>();
		return (parser.parseXML(in));
	}

	/**
	 * Read an XML tagged file
	 *
	 * @param in Description of the Parameter
	 * @return Description of the Return Value
	 * @exception IOException Description of the Exception
	 * @exception XMLException Description of the Exception
	 * @exception Exception Description of the Exception
	 */
	public T readFile(InputStream in)
			throws Exception, IOException, XMLException {
		init();
		XMLCParser<T> parser = new XMLCParser<>();
		return (parser.parseXML(in));
	}

	/**
	 * read a XML tagged file, from the string we will take a guess and try to
	 * analyze the file type
	 *
	 * @param string Description of the Parameter
	 * @return Description of the Return Value
	 * @exception IOException Description of the Exception
	 * @exception XMLException Description of the Exception
	 * @exception Exception Description of the Exception
	 */
	public T readFile(String string)
			throws Exception, IOException, XMLException {
		InputStream in;
		// see whether we have some kind of URL
		if (string.indexOf("://") >= 0) {
			URL url = new URL(string);
			URLConnection connection = url.openConnection();
			in = connection.getInputStream();
		} else {
			in = new FileInputStream(string);
		}

		init();
		T o = readFile(in);
		in.close();
		return (o);
	}

	/**
	 * Read from a DOM
	 *
	 * @param document Description of the Parameter
	 * @return Description of the Return Value
	 * @exception IOException Description of the Exception
	 * @exception XMLException Description of the Exception
	 * @exception Exception Description of the Exception
	 */
	public T readDOM(Document document)
			throws Exception, IOException, XMLException {
		T o = parseFile(document);
		return (o);
	}

	/**
	 * Parse content from a DOM
	 *
	 * @param document Description of the Parameter
	 * @return Description of the Return Value
	 * @exception IOException Description of the Exception
	 * @exception XMLException Description of the Exception
	 * @exception Exception Description of the Exception
	 */
	private T parseFile(Document document)
			throws Exception, IOException, XMLException {
		Element element = document.getDocumentElement();
		if (element == null) {
			throw new XMLException("DOM parser: no top element found");
		}

		th = XMLTagHandlerFactory.findXMLTagHandler(element.getTagName(), forceNewTagHandlers);
		forceNewTagHandlers = false;

		if (th == null) {
			th = XMLTagHandlerFactory.getDefaultXMLTagHandler();
		}
		XMLParser<T> parser = th.getParser();
		T o = parser.parseXML(th, document);

		return (o);
	}

	/*
	 *  Some conversion routines
	 */
	/**
	 * Description of the Method
	 *
	 * @param text Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static synchronized int cvtint(String text) {
		int i = 0;
		try {
			i = Integer.valueOf(text).intValue();
		} catch (Exception e) {
		}
		return (i);
	}

	/**
	 * Description of the Method
	 *
	 * @param text Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static synchronized double cvtdouble(String text) {
		double d = 0.0;
		try {
			d = Double.valueOf(text).doubleValue();
		} catch (Exception e) {
		}
		return (d);
	}

	/**
	 * Description of the Method
	 *
	 * @param object Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static synchronized Object cvtObject(Object object) {
		return (object);
	}
}
