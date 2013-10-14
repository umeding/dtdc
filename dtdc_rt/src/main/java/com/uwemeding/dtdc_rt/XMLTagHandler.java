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

/**
 * XML tag handler
 *
 * @author uwe
 */
public interface XMLTagHandler {

	/**
	 * Find the tag peer
	 *
	 * @param name tag name
	 * @return the tag peer
	 * @throws XMLException
	 */
	TagPeer findTagPeer(String name) throws XMLException;

	/**
	 * Add a tag handler
	 *
	 * @param name handler name
	 * @param tagPeer tag peer
	 */
	void addHandler(String name, TagPeer tagPeer);

	/**
	 * Initialize the handler
	 */
	void initHandler();

	/**
	 * Get the parser for this handler
	 *
	 * @param <T> the parser type
	 * @return the parser
	 */
	<T> XMLParser<T> getParser();
}
