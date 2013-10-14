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

public class XMLTag {

	public String name;
	public int enumerator;
	public boolean stm;
	public boolean etm;

	public XMLTag(int enumerator,
			String theName,
			boolean start_tag_minimization,
			boolean end_tag_minimization) {
		this.name = theName;
		this.enumerator = enumerator;
		this.stm = start_tag_minimization;
		this.etm = end_tag_minimization;
	}
}
