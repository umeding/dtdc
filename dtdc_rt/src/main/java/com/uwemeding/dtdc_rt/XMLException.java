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

public class XMLException extends Exception {

	private String message;
	private String systemId;
	private int line;
	private int column;

	public XMLException() {
		super();
	}

	public XMLException(Throwable t) {
		super(t);
	}

	public XMLException(String message) {
		this(message, null, 0, 0);
	}

	/**
	 * Construct a new XML parsing exception.
	 *
	 * @param message The error message from the parser.
	 * @param systemId The URI of the entity containing the error.
	 * @param line The line number where the error appeared.
	 * @param column The column number where the error appeared.
	 */
	public XMLException(String message, String systemId, int line, int column) {
		this.message = message;
		this.systemId = systemId;
		this.line = line;
		this.column = column;
	}

	/**
	 * Get the error message from the parser.
	 *
	 * @return A string describing the error.
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * Get the URI of the entity containing the error.
	 *
	 * @return The URI as a string.
	 */
	public String getSystemId() {
		return systemId;
	}

	/**
	 * Get the line number containing the error.
	 *
	 * @return The line number as an integer.
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Get the column number containing the error.
	 *
	 * @return The column number as an integer.
	 */
	public int getColumn() {
		return column;
	}
}
