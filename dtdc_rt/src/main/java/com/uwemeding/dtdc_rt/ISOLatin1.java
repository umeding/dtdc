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

/**
 * This class contains static methods for converting between Strings and byte
 * arrays. There are more machine-independend ways of performing such
 * conversions in Java 1.1/1.2, where there are byte-to-char converters, and the
 * Reader and Writer classes take care of most of the more involved details.
 *
 * Anyway, this class exists to accomodate (the simple) ISOLatin1 conversions to
 * and from byte arrays and avaoids having to fuss with the char vs. byte issues
 */
public class ISOLatin1 {

	private ISOLatin1() {
	} // static methods only

	/**
	 * Converts a String to an ISO-Latin-1 encoded byte array.
	 */
	public static byte[] toByteArray(String s, int offset, int length) {
		byte[] buf = new byte[length];
		for (int i = 0; i < length; i++) {
			buf[i] = (byte) s.charAt(offset + i);
		}

		return buf;
	}

	/**
	 * Equivalent to toByteArray(s, 0, s.length())
	 */
	public static byte[] toByteArray(String s) {
		return toByteArray(s, 0, s.length());
	}

	/**
	 * This method is similar to toByteArray(s, offset, length) but throws an
	 * IllegalArgumentException if any of the characters in s are outside the
	 * range &#92;u0000 to &#92;u00FF.
	 */
	public static byte[] toByteArrayLossless(String s, int offset, int length) {
		byte[] buf = new byte[length];
		char c;
		for (int i = 0; i < length; i++) {
			c = s.charAt(offset + i);
			if (c > '\u00FF') {
				throw new IllegalArgumentException("non-ISO-Latin-1 character in input: \\"
						+ "u" + HexUtil.shortToString(c));
			}
			buf[i] = (byte) c;
		}

		return buf;
	}

	/**
	 * This method is similar to toByteArray(s), but throws an
	 * IllegalArgumentException if any of the characters in s are outside the
	 * range &#92;u0000 to &#92;u00FF.
	 */
	public static byte[] toByteArrayLossless(String s) {
		return toByteArrayLossless(s, 0, s.length());
	}

	/**
	 * Converts an ISO-Latin-1 encoded byte array to a String.
	 */
	public static String toString(byte[] b, int offset, int length) {
		char[] cbuf = new char[length];
		for (int i = 0; i < length; i++) {
			cbuf[i] = (char) (b[i + offset] & 0xFF);
		}

		return new String(cbuf);
	}

	/**
	 * Equivalent to toString(b, 0, b.length)
	 */
	public static String toString(byte[] b) {
		return toString(b, 0, b.length);
	}
}
