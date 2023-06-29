package org.spray.heaven.util;

public class StringUtil {

	public static String replaceNull(String text) {
		return text == null ? "" : text + "";
	}

	public static final Character getOrNull(CharSequence chr, int index) {
		return (index >= 0 && index <= chr.length() - 1) ? Character.valueOf(chr.charAt(index)) : null;
	}

	public static boolean isDouble(String d) {
		try {
			Double.parseDouble(d);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

}
