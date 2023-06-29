package org.spray.infinity.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class StringUtil {
	
	public static String replaceNull(String str) {
		return str == null ? "" : str + " ";
	}

	public static String DF(Number value, int maxvalue) {
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(maxvalue);
		return df.format(value);
	}

}
