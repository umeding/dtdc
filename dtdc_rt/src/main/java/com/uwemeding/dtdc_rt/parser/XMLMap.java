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

public final class XMLMap {

	public static final String lt = "&lt;";
	public static final String gt = "&gt;";
	public static final String amp = "&amp;";
	public static final String apos = "&apos;";
	public static final String quot = "&quot;";

	private XMLMap() {
	}

	/**
	 * Encode a string into something that the XML parser will understand and
	 * not parse as regular XML text
	 */
	public static String encode(String s) {

		StringBuffer sb = new StringBuffer(s);
		int length = sb.length();
		for (int i = 0; i < length; i++) {
			char c = sb.charAt(i);
			switch (c) {
				case '&':
					sb.replace(i, i + 1, amp);
					length = sb.length();
					i += 4;
					break;
				case '<':
					sb.replace(i, i + 1, lt);
					length = sb.length();
					i += 3;
					break;
				case '>':
					sb.replace(i, i + 1, gt);
					length = sb.length();
					i += 3;
					break;
				case '\'':
					sb.replace(i, i + 1, apos);
					length = sb.length();
					i += 5;
					break;
				case '"':
					sb.replace(i, i + 1, quot);
					length = sb.length();
					i += 5;
					break;
			}
		}
		return (new String(sb));
	}

	/**
	 * Encode only a "&" string into "&amp;"... leave other characters alone.
	 */
	public static String encodeAmp(String s) {

		StringBuffer sb = new StringBuffer(s);
		int length = sb.length();
		for (int i = 0; i < length; i++) {
			if ((sb.charAt(i) == '&') && (sb.charAt(i + 1) != '#')) {
				sb.replace(i, i + 1, amp);
				length = sb.length();
				i += 4;
			}
		}
		return (new String(sb));
	}

	/**
	 * Decode a string from the XML parser protect string into something we can
	 * analyze with the XML parser
	 */
	public static String decode(String s) {
		StringBuffer sb = new StringBuffer(s);
		int length = sb.length();
		int start, end;
		int j;
		final int BUFSIZ = 10;
		char[] buffer = new char[BUFSIZ];
		for (int i = 0; i < length; i++) {
			char c = sb.charAt(i);
			switch (c) {
				case '&':
					start = i;
					end = start + BUFSIZ;
					if (end > length) {
						end = length;
					}
					sb.getChars(start, end, buffer, 0);
					String string = new String(buffer);
					if (string.startsWith(amp)) {
						sb.replace(start, start + 5, "&");
					} else if (string.startsWith(apos)) {
						sb.replace(start, start + 6, "'");
					} else if (string.startsWith(gt)) {
						sb.replace(start, start + 4, ">");
					} else if (string.startsWith(lt)) {
						sb.replace(start, start + 4, "<");
					} else if (string.startsWith(quot)) {
						sb.replace(start, start + 6, "\"");
					}
					length = sb.length();
					break;
			}
		}
		return (new String(sb));
	}

	/**
	 * Map a filename to a system compatible filename
	 *
	 * @param is the desired filename
	 * @return the actual filename
	 */
	public static String mapHandlerFilename(String filename) {
		StringBuffer sb = new StringBuffer();
		char[] ca = filename.toCharArray();
		for (int i = 0; i < ca.length; i++) {
			switch (ca[i]) {
				case '#':
				case ':':
					sb.append("#");
					sb.append(Integer.toHexString((int) ca[i]));
					break;
				default:
					sb.append(ca[i]);
			}
		}
		return (new String(sb));
	}

	/*
	 public static void main(String[] av)
	 {
	 String  string = "<<some:tag name=\"humpf\"\" &&test; ''laber'>> more text";
	 String  estring = encode(string);
	 String  dstring = decode(estring);
	 System.out.println("before: "+string+"\nencode: "+estring+"\ndecode: "+dstring);
	 }
	 */
}
