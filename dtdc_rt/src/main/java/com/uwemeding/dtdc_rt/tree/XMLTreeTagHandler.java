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
package com.uwemeding.dtdc_rt.tree;

import com.uwemeding.dtdc_rt.TagPeer;
import com.uwemeding.dtdc_rt.XMLException;
import com.uwemeding.dtdc_rt.XMLTagHandler;
import com.uwemeding.dtdc_rt.parser.XMLParser;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic tag handler
 */
public class XMLTreeTagHandler implements XMLTagHandler {

	private final Map<String, TagPeer> tagPeers;
	private final XMLTreeParser parser;

	public XMLTreeTagHandler(XMLTreeParser parser) {
		this.tagPeers = new HashMap<>();
		this.parser = parser;
	}

	@Override
	public TagPeer findTagPeer(String name) throws XMLException {
		return tagPeers.get(name);
	}

	@Override
	public void addHandler(String name, TagPeer tagPeer) {
		tagPeers.put(name.toUpperCase(), tagPeer);
	}

	@Override
	public XMLParser getParser() {
		return parser;
	}

	@Override
	public void initHandler() {
	}
}
