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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Tag {

	/**
	 * End of file indicator
	 */
	public static final int THE_EOF = 0;
	/**
	 * Start tag
	 */
	public static final int STAG = 1;
	/**
	 * End tag
	 */
	public static final int ETAG = 2;
	/**
	 * Text of some kind
	 */
	public static final int STRING = 3;
	/**
	 * Special type, like <!ENTITY ...>
	 */
	public static final int SPECIAL = 4;
	/**
	 * XML type
	 */
	public static final int XML = 5;
	// ===========================
	public String name;     /* tag name */

	public int type;     /* tag type (start, end, string etc.) */

	public boolean hasImplicitEndTag;
	public Tag ref;      /* Reference tag, for a start tag that would an end tag.  */

	public int pos;      /* Position of tag in internal db */

	private int nAttr;    /* Count special attributes */

	private final Map<String, Attribute> attributes;

	public Tag(int tag_type, String tag_text) {
		name = tag_text;
		type = tag_type;
		hasImplicitEndTag = false;
		nAttr = 0;
		attributes = new HashMap<>();
	}

	public void addAttribute(Attribute attribute) {
		attributes.put(attribute.name, attribute);
	}

	public void addAttribute(String theName, String theValue) {
		addAttribute(new Attribute(theName, theValue));
	}

	public void addSpecialAttribute(String value) {
		addAttribute("" + nAttr, value);
		nAttr++;
	}

	public Attribute findAttribute(String name) {
		return attributes == null ? null : attributes.get(name);
	}

	public void clearAttributes() {
		if (attributes != null) {
			attributes.clear();
		}
	}

	public boolean equals(String name, int tag_type) {
		return (tag_type == type) && name.equals(name);
	}

	public boolean equalsIgnoreCase(String name, int tag_type) {
		return (tag_type == type) && name.equalsIgnoreCase(name);
	}

	public Iterator<Attribute> attributeElements() {
		return attributes.values().iterator();
	}

	@Override
	public String toString() {
		String text;
		switch (type) {
			case THE_EOF:
				text = "<eof>";
				break;
			case STAG:
				text = "<start-tag>";
				break;
			case ETAG:
				text = "<end-tag>";
				break;
			case STRING:
				text = "<string>";
				break;
			case SPECIAL:
				text = "<special>";
				break;
			case XML:
				text = "<xml>";
				break;
			default:
				text = "<huh-noz>";
				break;
		}
		String t = name + " " + text;
		for (Attribute attr : attributes.values()) {
			t += " " + attr;
		}

		return t;
	}
}
