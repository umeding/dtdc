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

import com.uwemeding.dtdc_rt.XMLTagHandler;
import org.w3c.dom.Document;

/**
 * The XML Parser interface
 */
public interface XMLParser<E> {

	/**
	 * Parses the contents of an XML file
	 *
	 * @param lex the lexical analyzer to be used
	 * @returns a pointer to the resulting object
	 */
	E parseXML(XMLTagHandler tagHandler, XMLLex lex) throws Exception;

	/**
	 * Parses the contents of an XML file from a DOM
	 *
	 * @param document the DOM to be used
	 * @returns a pointer to the resulting object
	 */
	E parseXML(XMLTagHandler tagHandler, Document document) throws Exception;
}
