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

import com.uwemeding.dtdc_rt.Attribute;
import com.uwemeding.dtdc_rt.Tag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Store a generic XML tree
 */
public class XMLTree {

	private final List<Tag> tags;
	private final Map<String,Tag> tagsById;
	private int pos = 0;

	public XMLTree() {
		tags = new ArrayList<>();
		tagsById = new HashMap<>();
	}

	/**
	 * Adds a tag to the list of tags
	 */
	public void addTag(Tag tag) {
		tag.pos = pos++;
		tags.add(tag);
		Attribute attr = tag.findAttribute("ID");
		if (attr != null) {
			tagsById.put(attr.value, tag);
		}
	}

	/**
	 * return the number of elements
	 */
	public int getTagCount() {
		return pos;
	}

	/**
	 * Iterate the list of tags
	 */
	public Iterator<Tag> tagElements() {
		return tags.iterator();
	}

	/**
	 * Iterate to a reference tag
	 */
	public Iterator<Tag> tagElements(final Tag tag) {
		return new Iterator<Tag>() {
			private final Tag startTag = tag;
			private int pos = tag.pos;
			private Tag currentTag;

			@Override
			public boolean hasNext() {
				if (startTag.ref == null) {
					return (false);
				} else {
					currentTag = tags.get(pos);
					if (currentTag == startTag.ref) {
						return (false);
					} else {
						return (true);
					}
				}
			}

			@Override
			public Tag next() {
				Tag tag = currentTag;
				currentTag = null;
				if (tag == null) {
					tag = tags.get(pos);
				}
				pos++;
				return tag;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Not supported.");
			}
		};
	}

	/**
	 * Find a tag by id
	 */
	public Tag findTagWithId(String id) {
		return tagsById.get(id);
	}

	/**
	 * show what we have
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Number of tags=").append(pos);
		for (Tag tag : tags) {
			sb.append(tag).append("\n-----------\n");
		}
		return (sb.toString());
	}
}
