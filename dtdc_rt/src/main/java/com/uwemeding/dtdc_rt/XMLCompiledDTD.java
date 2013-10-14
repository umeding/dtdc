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

import com.uwemeding.dtdc_rt.parser.XMLParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

public interface XMLCompiledDTD {

	/**
	 * Get the formal public identifier fro this compiled DTD.
	 *
	 * @return the formal public identifier for this compiled DTD.
	 */
	String getFormalPublicIdentifier();

	/**
	 * Get the name of this compiled DTD.
	 *
	 * @return the name of this DTD.
	 */
	String getName();

	/**
	 * Get the class that is represented by this compiled DTD.
	 *
	 * @return the class of this compiled DTD.
	 * @throws CLassNotFoundException if the class cannot be found.
	 */
	Class getCompiledDTDClass() throws ClassNotFoundException;

	/**
	 * Get the tag handler for this compiled DTD.
	 *
	 * @return the tag handler for the compiled DTD.
	 * @throws XMLException if something goes wrong.
	 */
	<T> XMLTagHandler getTagHandler(XMLParser<T> parser) throws XMLException;

	/**
	 * Get the notation by this name.
	 *
	 * @return the notation by this name.
	 */
	XMLNotation getNotation(String name);

	/**
	 * Get the XML serialized object for the XML object.
	 *
	 * @return a string containing the XML descripton of the XMLObject.
	 * @throws XMLValidationException if we encounter an XML validation error.
	 */
	String getXML(XMLObject o, boolean validate) throws XMLValidationException;

	/**
	 * Get the XML object as a DOM tree.
	 *
	 * @return a DOM tree containing the XML descripton of the XMLObject.
	 * @throws XMLValidationException if we encounter an XML validation error.
	 * @throws DOMException for any type of DOM creation exception.
	 */
	Document getDOM(XMLObject o, boolean validate) throws XMLValidationException, DOMException;
}
