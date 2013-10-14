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

import com.uwemeding.dtdc_rt.XMLException;

/**
 * Convenience base class for XMLProcessor handlers. <p>
 *
 * This base class implements the XMLHandler interface with (mostly empty)
 * default handlers. You are not required to use this, but if you need to handle
 * only a few events, you might find it convenient to extend this class rather
 * than implementing the entire interface. This example overrides only the
 * <code>charData</code> method, using the defaults for the others:
 * <pre>
 * import dtdc_rt.parser.XMLHandlerAdapter;
 *
 * public class MyHandler extends XMLHandlerAdapter {
 *   public void charData (char ch[], int start, int length)
 *   {
 *     System.out.println("Data: " + new String (ch, start, length));
 *   }
 * }
 * </pre>
 *
 * @author uwe
 * @see XMLHandler
 * @see XMLException
 */
public class XMLHandlerAdapter implements XMLHandler {

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
	 * The default implementation simply returns the supplied system identifier.
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
		return null;
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
	}

	/**
	 * Handle the start of an element. <p>
	 *
	 * The default implementation does nothing.
	 *
	 * @param elname Description of the Parameter
	 * @exception Exception Derived methods may throw exceptions.
	 * @see dtdc_rt.parser.XMLHandler#startElement
	 */
	@Override
	public void startElement(String elname)
			throws Exception {
	}

	/**
	 * Handle the end of an element. <p>
	 *
	 * The default implementation does nothing.
	 *
	 * @param elname Description of the Parameter
	 * @exception Exception Derived methods may throw exceptions.
	 * @see dtdc_rt.parser.XMLHandler#endElement
	 */
	@Override
	public void endElement(String elname)
			throws Exception {
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
	@Override
	public void processingInstruction(String target, String data)
			throws Exception {
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
