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

public class XMLStack {

	private final static int MAX_STACK = 100;
	private final Object[] stack;
	private int sp;

	public XMLStack() {
		stack = new Object[MAX_STACK];
		sp = -1;
	}

	public Object push(Object o) {
		if (sp < MAX_STACK) {
			sp++;
			stack[sp] = o;
		} else {
			throw new ArrayIndexOutOfBoundsException(sp + " >= " + MAX_STACK);
		}
		return o;
	}

	public Object peek() {
		return stack[sp];
	}

	public Object pop() {
		Object o;
		if (sp >= 0) {
			o = stack[sp];
			stack[sp] = null;
			sp--;
		} else {
			throw new ArrayIndexOutOfBoundsException(sp + " < 0");
		}
		return o;
	}

	public boolean empty() {
		return sp < 0;
	}

	public Object[] getObjects() {
		return stack;
	}

	/**
	 * return the stack pointer
	 */
	public int getSP() {
		return sp;
	}
}
