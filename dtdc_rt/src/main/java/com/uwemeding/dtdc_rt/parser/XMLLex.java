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

import com.uwemeding.dtdc_rt.Tag;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * This class implements the XML lexical analyzer
 */
public class XMLLex {

	private Tag pushBack;
	private Reader reader;
	private InputStream in;

	/**
	 * Construct the lexical analyzer
	 */
	public XMLLex(InputStream in) {
		this.in = in;
	}

	public XMLLex(Reader fp) {
		this.reader = fp;
	}

	public InputStream getInputStream() {
		return in;
	}

	public Reader getReader() {
		return reader;
	}

	/**
	 * Pushback one tag
	 */
	public void pushBack(Tag tag) {
		pushBack = tag;
	}

	/**
	 * Return the next tag from the input
	 */
	public Tag getNextTag() throws IOException {
		if (pushBack == null) {
			return null;
		} else {
			Tag tag = pushBack;
			pushBack = null;
			return tag;
		}
	}
}
