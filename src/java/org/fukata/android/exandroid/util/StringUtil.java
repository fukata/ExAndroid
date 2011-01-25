package org.fukata.android.exandroid.util;

import java.util.Date;

/**
 * 文字列ユーティリティ
 * 
 * @author Tatsuya Fukata
 */
public enum StringUtil {
	;
	public static final String EMPTY = "";
	
	/**
	 * 文字列が空文字かどうか判定する。
	 * @param value
	 * @return
	 */
	public static boolean isBlank(String value) {
		
		if (value == null) {
			return true;
		}
		
		if (value.trim().length()>0) {
			return false;
		}
		
		return true;
	}

	/**
	 * 文字列が空文字ではないか判定する。
	 * @param value
	 * @return
	 */
	public static boolean isNotBlank(String value) {
		return !isBlank(value);
	}
	
	/**
	 * 文字列を比較する。
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean equals(String value1, String value2) {
		
		if (value1==null || value2==null) {
			return false;
		}
		
		if (!value1.trim().equals(value2.trim())) {
			return false;
		}
		
		return true;
	}

	/**
	 * 大小区別せずに文字列を比較する。
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean equalsIgnoreCase(String value1, String value2) {
		
		if (value1==null || value2==null) {
			return false;
		}
		
		if (!value1.trim().equalsIgnoreCase(value2.trim())) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 日付文字かどうか判定する。
	 * @param str
	 * @return
	 */
	public static boolean isDate(String str) {
		if (isBlank(str)) {
			return false;
		}
		
		try {
			new Date(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 対象がNULLの場合でも空文字として出力する。
	 * @param value
	 * @return
	 */
	public static String toString(Object value) {
		return value==null ? EMPTY : value.toString();
	}
	
	/**
	 * 整数値形式かどうか判定する。
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNumeric(String value) {
		try {
			Long.parseLong(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 浮動小数点数形式かどうか判定する。
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isFloat(String value) {
		try {
			Float.parseFloat(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
