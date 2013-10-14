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

import java.util.*;

/**
 * XML array list
 */
public class XMLArrayList<E> extends ArrayList<E> {

	/**
	 * Create an XML array list
	 */
	public XMLArrayList() {
		super();
	}

	/**
	 * Create an XML array list
	 *
	 * @param size is a pre-allocated size
	 */
	public XMLArrayList(int size) {
		super(size);
	}

	/**
	 * Add an element to the list
	 *
	 * @param obj is the element
	 */
	public void addElement(E obj) {
		super.add(obj);
	}

	/**
	 * Get an iterator
	 *
	 * @return the iterator
	 */
	public Iterator<E> elements() {
		return super.iterator();
	}

	/**
	 * Remove all elements in the list
	 */
	public void removeAllElements() {
		super.clear();
	}

	/**
	 * Remove a known element
	 *
	 * @param obj is the element to be removed
	 * @return true if the element existed
	 */
	public boolean removeElement(E obj) {
		return (super.remove(obj));
	}

	/**
	 * Remove an element at a given position
	 *
	 * @param pos is the position
	 */
	public void removeElementAt(int pos) {
		super.remove(pos);
	}
}
