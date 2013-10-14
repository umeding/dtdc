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

/**
 * XML Processing Interface. <p>
 *
 * Whenever you parse an XML document, you must provide an object from a class
 * that implements this interface to receive the parsing events. <p>
 *
 * If you do not want to implement this entire interface, you can extend the
 * <code>XMLHandler</code> convenience class and then implement only what you
 * need. <p>
 *
 * @see XMLProcessor
 * @see XMLHandler
 */
public interface XMLHandler {

	/**
	 * Start the document. <p>
	 *
	 * The XML parser will call this method just before it attempts to read the
	 * first entity (the root of the document). It is guaranteed that this will
	 * be the first method called.
	 *
	 * @exception Exception The handler may throw any exception.
	 * @see #endDocument
	 */
	void startDocument() throws Exception;

	/**
	 * End the document. <p>
	 *
	 * The XML parser will call this method once, when it has finished parsing
	 * the XML document. It is guaranteed that this will be the last method
	 * called.
	 *
	 * @exception Exception The handler may throw any exception.
	 * @see #startDocument
	 */
	void endDocument() throws Exception;

	/**
	 * Resolve an External Entity. <p>
	 *
	 * Give the handler a chance to redirect external entities to different
	 * URIs. The XML parser will call this method for the top-level document
	 * entity, for external text (XML) entities, and the external DTD subset (if
	 * any).
	 *
	 * @param publicId The public identifier, or null if none was supplied.
	 * @param systemId The system identifier.
	 * @return The replacement system identifier, or null to use the default.
	 * @exception Exception The handler may throw any exception.
	 * @see #startExternalEntity
	 * @see #endExternalEntity
	 */
	Object resolveEntity(String publicId, String systemId) throws Exception;

	/**
	 * Begin an external entity. <p>
	 *
	 * The XML parser will call this method at the beginning of each external
	 * entity, including the top-level document entity and the external DTD
	 * subset (if any). <p>
	 *
	 * If necessary, you can use this method to track the location of the
	 * current entity so that you can resolve relative URIs correctly.
	 *
	 * @param systemId The URI of the external entity that is starting.
	 * @exception Exception The handler may throw any exception.
	 * @see #endExternalEntity
	 * @see #resolveEntity
	 */
	void startExternalEntity(String systemId) throws Exception;

	/**
	 * End an external entity. <p>
	 *
	 * The XML parser will call this method at the end of each external entity,
	 * including the top-level document entity and the external DTD subset. <p>
	 *
	 * If necessary, you can use this method to track the location of the
	 * current entity so that you can resolve relative URIs correctly.
	 *
	 * @param systemId The URI of the external entity that is ending.
	 * @exception Exception The handler may throw any exception.
	 * @see #startExternalEntity
	 * @see #resolveEntity
	 */
	void endExternalEntity(String systemId) throws Exception;

	/**
	 * Document type declaration. <p>
	 *
	 * The XML parser will call this method when or if it encounters the
	 * document type (DOCTYPE) declaration. <p>
	 *
	 * Please note that the public and system identifiers will not always be a
	 * reliable indication of the DTD in use.
	 *
	 * @param name The document type name.
	 * @param publicId The public identifier, or null if unspecified.
	 * @param systemId The system identifier, or null if unspecified.
	 * @exception Exception The handler may throw any exception.
	 */
	void doctypeDecl(String name, String publicId, String systemId) throws Exception;

	/**
	 * Attribute. <p>
	 *
	 * The XML parser will call this method once for each attribute (specified
	 * or defaulted) before reporting a startElement event. It is up to your
	 * handler to collect the attributes, if necessary. <p>
	 *
	 * You may use XMLProcessor.getAttributeType() to find the attribute's
	 * declared type.
	 *
	 * @param value The value of the attribute, or null if the attribute
	 * is <code>#IMPLIED</code>.
	 * @param isSpecified True if the value was specified, false if it was
	 * defaulted from the DTD.
	 * @param aname Description of the Parameter
	 * @exception Exception The handler may throw any exception.
	 * @see #startElement
	 * @see XMLProcessor#declaredAttributes
	 * @see XMLProcessor#getAttributeType
	 * @see XMLProcessor#getAttributeDefaultValue
	 */
	void attribute(String aname, String value, boolean isSpecified) throws Exception;

	/**
	 * Start an element. <p>
	 *
	 * The XML parser will call this method at the beginning of each element. By
	 * the time this is called, all of the attributes for the element will
	 * already have been reported using the
	 * <code>attribute</code> method.
	 *
	 * @param elname The element type name.
	 * @exception Exception The handler may throw any exception.
	 * @see #attribute
	 * @see #endElement
	 * @see XMLProcessor#declaredElements
	 * @see XMLProcessor#getElementContentType
	 */
	void startElement(String elname) throws Exception;

	/**
	 * End an element. <p>
	 *
	 * The XML parser will call this method at the end of each element
	 * (including EMPTY elements).
	 *
	 * @param elname The element type name.
	 * @exception Exception The handler may throw any exception.
	 * @see #startElement
	 * @see XMLProcessor#declaredElements
	 * @see XMLProcessor#getElementContentType
	 */
	void endElement(String elname) throws Exception;

	/**
	 * Character data. <p>
	 *
	 * The XML parser will call this method once for each chunk of character
	 * data found in the contents of elements. Note that the parser may break up
	 * a long sequence of characters into smaller chunks and call this method
	 * once for each chunk. <p>
	 *
	 * Do <em>not</em> attempt to read more than <var>length</var> characters
	 * from the array, or to read before the <var>start</var> position.
	 *
	 * @param ch The character data.
	 * @param start The starting position in the array.
	 * @param length The number of characters available.
	 * @exception Exception The handler may throw any exception.
	 */
	void charData(char ch[], int start, int length) throws Exception;

	/**
	 * Ignorable whitespace. <p>
	 *
	 * The XML parser will call this method once for each sequence of ignorable
	 * whitespace in element content (never in mixed content). <p>
	 *
	 * For details, see section 2.10 of the XML 1.0 recommendation. <p>
	 *
	 * Do <em>not</em> attempt to read more than <var>length</var> characters
	 * from the array or to read before the <var>start</var> position.
	 *
	 * @param ch The literal whitespace characters.
	 * @param start The starting position in the array.
	 * @param length The number of whitespace characters available.
	 * @exception Exception The handler may throw any exception.
	 */
	void ignorableWhitespace(char ch[], int start, int length) throws Exception;

	/**
	 * Processing instruction. <p>
	 *
	 * The XML parser will call this method once for each processing
	 * instruction. Note that processing instructions may appear outside of the
	 * top-level element. The
	 *
	 * @param target The target (the name at the start of the PI).
	 * @param data The data, if any (the rest of the PI).
	 * @exception Exception The handler may throw any exception.
	 */
	void processingInstruction(String target, String data) throws Exception;

	/**
	 * Fatal XML parsing error. <p>
	 *
	 * The XML parser will call this method whenever it encounters a serious
	 * error. The parser will attempt to continue past this point so that you
	 * can find more possible error points, but if this method is called you
	 * should assume that the document is corrupt and you should not try to use
	 * its contents. <p>
	 *
	 * Note that you can use the
	 * <code>XMLException</code> class to encapsulate all of the information
	 * provided, though the use of the class is not mandatory.
	 *
	 * @param message The error message.
	 * @param systemId The system identifier of the entity that contains the
	 * error.
	 * @param line The approximate line number of the error.
	 * @param column The approximate column number of the error.
	 * @exception Exception The handler may throw any exception.
	 * @see XMLException
	 */
	void error(String message, String systemId, int line, int column) throws Exception;
}
