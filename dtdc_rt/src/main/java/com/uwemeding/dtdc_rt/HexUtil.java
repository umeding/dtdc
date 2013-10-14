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
 * Static methods for converting to and from hexadecimal strings.
 */
public class HexUtil {

	private HexUtil() {
	}
	private static final char[] hexDigits = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	};

	/**
	 * Returns a string of hexadecimal digits from a byte array. Each byte is
	 * converted to 2 hex symbols.
	 *
	 * If offset and length are omitted, the whole array is used.
	 */
	public static String toString(byte[] ba, int offset, int length) {
		char[] buf = new char[length * 2];
		int j = 0;
		int k;

		for (int i = offset; i < offset + length; i++) {
			k = ba[i];
			buf[j++] = hexDigits[(k >>> 4) & 0x0F];
			buf[j++] = hexDigits[ k & 0x0F];
		}
		return new String(buf);
	}

	public static String toString(byte[] ba) {
		return toString(ba, 0, ba.length);
	}

	/**
	 * Returns a string of hexadecimal digits from an integer array. Each int is
	 * converted to 4 hex symbols.
	 *
	 * If offset and length are omitted, the whole array is used.
	 */
	public static String toString(int[] ia, int offset, int length) {
		char[] buf = new char[length * 8];
		int j = 0;
		int k;

		for (int i = offset; i < offset + length; i++) {
			k = ia[i];
			buf[j++] = hexDigits[(k >>> 28) & 0x0F];
			buf[j++] = hexDigits[(k >>> 24) & 0x0F];
			buf[j++] = hexDigits[(k >>> 20) & 0x0F];
			buf[j++] = hexDigits[(k >>> 16) & 0x0F];
			buf[j++] = hexDigits[(k >>> 12) & 0x0F];
			buf[j++] = hexDigits[(k >>> 8) & 0x0F];
			buf[j++] = hexDigits[(k >>> 4) & 0x0F];
			buf[j++] = hexDigits[ k & 0x0F];
		}
		return new String(buf);
	}

	public static String toString(int[] ia) {
		return toString(ia, 0, ia.length);
	}

	/**
	 * Returns a string of hexadecimal digits in reverse order from a byte array
	 * (i.e. the least significant byte is first, but within each byte the most
	 * significant hex digit is before the least significant hex digit).
	 *
	 * If offset and length are omitted, the whole array is used.
	 */
	public static String toReversedString(byte[] b, int offset, int length) {
		char[] buf = new char[length * 2];
		int j = 0;

		for (int i = offset + length - 1; i >= offset; i--) {
			buf[j++] = hexDigits[(b[i] >>> 4) & 0x0F];
			buf[j++] = hexDigits[ b[i] & 0x0F];
		}
		return new String(buf);
	}

	public static String toReversedString(byte[] b) {
		return toReversedString(b, 0, b.length);
	}

	/**
	 * Returns a byte array from a string of hexadecimal digits.
	 */
	public static byte[] fromString(String hex) {
		int len = hex.length();
		byte[] buf = new byte[((len + 1) / 2)];

		int i = 0, j = 0;
		if ((len % 2) == 1) {
			buf[j++] = (byte) fromDigit(hex.charAt(i++));
		}

		while (i < len) {
			buf[j++] = (byte) ((fromDigit(hex.charAt(i++)) << 4)
					| fromDigit(hex.charAt(i++)));
		}
		return buf;
	}

	/**
	 * Returns a byte array from a string of hexadecimal digits in reverse order
	 * (i.e. the least significant byte is first, but within each byte the most
	 * significant hex digit is before the least significant hex digit). The
	 * string must have an even number of digits.
	 *
	 * This is not really either little nor big-endian; it's just obscure. It is
	 * here because it is the format used for the SPEED certification data.
	 */
	public static byte[] fromReversedString(String hex) {
		int len = hex.length();
		byte[] buf = new byte[((len + 1) / 2)];

		int j = 0;
		if ((len % 2) == 1) {
			throw new IllegalArgumentException(
					"string must have an even number of digits");
		}

		while (len > 0) {
			buf[j++] = (byte) (fromDigit(hex.charAt(--len))
					| (fromDigit(hex.charAt(--len)) << 4));
		}
		return buf;
	}

	/**
	 * Returns the hex digit corresponding to a number n, from 0 to 15.
	 */
	public static char toDigit(int n) {
		try {
			return hexDigits[n];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException(n
					+ " is out of range for a hex digit");
		}
	}

	/**
	 * Returns the number from 0 to 15 corresponding to the hex digit ch.
	 */
	public static int fromDigit(char ch) {
		if (ch >= '0' && ch <= '9') {
			return ch - '0';
		}
		if (ch >= 'A' && ch <= 'F') {
			return ch - 'A' + 10;
		}
		if (ch >= 'a' && ch <= 'f') {
			return ch - 'a' + 10;
		}

		throw new IllegalArgumentException("invalid hex digit '" + ch + "'");
	}

	/**
	 * Returns a string of 2 hexadecimal digits (most significant digit first)
	 * corresponding to the lowest 8 bits of n.
	 */
	public static String byteToString(int n) {
		char[] buf = {hexDigits[(n >>> 4) & 0x0F], hexDigits[n & 0x0F]};
		return new String(buf);
	}

	/**
	 * Returns a string of 4 hexadecimal digits (most significant digit first)
	 * corresponding to the lowest 16 bits of n.
	 */
	public static String shortToString(int n) {
		char[] buf = {hexDigits[(n >>> 12) & 0x0F], hexDigits[(n >>> 8) & 0x0F],
			hexDigits[(n >>> 4) & 0x0F], hexDigits[ n & 0x0F]};
		return new String(buf);
	}

	/**
	 * Returns a string of 8 hexadecimal digits (most significant digit first)
	 * corresponding to the integer n, which is treated as unsigned.
	 */
	public static String intToString(int n) {
		char[] buf = new char[8];

		for (int i = 7; i >= 0; i--) {
			buf[i] = hexDigits[n & 0x0F];
			n >>>= 4;
		}
		return new String(buf);
	}

	/**
	 * Returns a string of 16 hexadecimal digits (most significant digit first)
	 * corresponding to the long n, which is treated as unsigned.
	 */
	public static String longToString(long n) {
		char[] buf = new char[16];

		for (int i = 15; i >= 0; i--) {
			buf[i] = hexDigits[(int) n & 0x0F];
			n >>>= 4;
		}
		return new String(buf);
	}

	/**
	 * Dump a byte array as a string, in a format that is easy to read for
	 * debugging. The string m is prepended to the start of each line.
	 *
	 * If offset and length are omitted, the whole array is used. If m is
	 * omitted, nothing is prepended to each line.
	 */
	public static String dumpString(byte[] data, int offset, int length, String m) {
		if (data == null) {
			return m + "null\n";
		}

		StringBuilder sb = new StringBuilder(length * 3);
		if (length > 32) {
			sb.append(m).append("Hexadecimal dump of ").append(length)
					.append(" bytes...\n");
		}

		// each line will list 32 bytes in 4 groups of 8 each
		int end = offset + length;
		String s;
		int l = Integer.toString(length).length();
		if (l < 4) {
			l = 4;
		}
		for (; offset < end; offset += 32) {
			if (length > 32) {
				s = "         " + offset;
				sb.append(m).append(s.substring(s.length() - l)).append(": ");
			}
			int i = 0;
			for (; i < 32 && offset + i + 7 < end; i += 8) {
				sb.append(toString(data, offset + i, 8)).append(' ');
			}

			if (i < 32) {
				for (; i < 32 && offset + i < end; i++) {
					sb.append(byteToString(data[offset + i]));
				}
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	public static String dumpString(byte[] data) {
		return (data == null) ? "null\n"
				: dumpString(data, 0, data.length, "");
	}

	public static String dumpString(byte[] data, String m) {
		return (data == null) ? "null\n"
				: dumpString(data, 0, data.length, m);
	}

	public static String dumpString(byte[] data, int offset, int length) {
		return dumpString(data, offset, length, "");
	}

	/**
	 * Dump an int array as a string, in a format that is easy to read for
	 * debugging. The string m is prepended to the start of each line.
	 *
	 * If offset and length are omitted, the whole array is used. If m is
	 * omitted, nothing is prepended to each line.
	 */
	public static String dumpString(int[] data, int offset, int length, String m) {
		if (data == null) {
			return m + "null\n";
		}

		StringBuilder sb = new StringBuilder(length * 3);
		if (length > 8) {
			sb.append(m).append("Hexadecimal dump of ").append(length)
					.append(" integers...\n");
		}

		// each line will list 32 bytes in 8 groups of 4 each (1 int)
		int end = offset + length;
		String s;
		int x = Integer.toString(length).length();
		if (x < 8) {
			x = 8;
		}
		for (; offset < end;) {
			if (length > 8) {
				s = "         " + offset;
				sb.append(m).append(s.substring(s.length() - x)).append(": ");
			}
			for (int i = 0; i < 8 && offset < end; i++) {
				sb.append(intToString(data[offset++])).append(' ');
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	public static String dumpString(int[] data) {
		return dumpString(data, 0, data.length, "");
	}

	public static String dumpString(int[] data, String m) {
		return dumpString(data, 0, data.length, m);
	}

	public static String dumpString(int[] data, int offset, int length) {
		return dumpString(data, offset, length, "");
	}
// Test methods
//...........................................................................
//
//    public static void main(String[] args)
//    {
//        self_test(new PrintWriter(System.out, true));
//    }
//
//    public static void self_test(PrintWriter out)
//    {
//        String test = "Hello. This is a test string with more than 32 characters.";
//        byte[] buf = new byte[test.length()];
//        for (int i = 0; i < test.length(); i++)
//	{
//            buf[i] = (byte) test.charAt(i);
//        }
//        String s;
//        byte[] buf2;
//
//        s = toString(buf);
//        out.println("HexUtil.toString(buf) = " + s);
//        buf2 = fromString(s);
//        if (!ConvertArray.areEqual(buf, buf2))
//            System.out.println("buf != buf2");
//
//        s = toReversedString(buf);
//        out.println("HexUtil.toReversedString(buf) = " + s);
//        buf2 = fromReversedString(s);
//        if (!ConvertArray.areEqual(buf, buf2))
//            out.println("buf != buf2");
//
//        out.print("HexUtil.dumpString(buf, 0, 28) =\n" + dumpString(buf, 0, 28));
//        out.print("HexUtil.dumpString(null) =\n" + dumpString((byte[]) null));
//        out.print(dumpString(buf, "+++"));
//
//        out.flush();
//    }
}
