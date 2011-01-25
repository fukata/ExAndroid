package org.fukata.android.exandroid.bean.converter;

import java.sql.Timestamp;
import java.util.Date;

/**
 * java.util.Dateクラスに対するコンバーター
 * 
 * @author Tatsuya Fukata
 *
 */
public class DateConverter implements Converter {

	public Object convert(Class<?> type, Object value) {
		Object converted = value;
		
		if (value instanceof Timestamp) {
			converted = convertTimestamp(type, value);
		} else if (value instanceof java.sql.Date) {
			converted = convertJavaSqlDate(type, value);
		} else if (value instanceof Long) {
			converted = convertLong(type, value);
		}
		
		return converted;
	}

	/**
	 * 入力元の値がTimestampクラスの場合にjava.util.Dateに変換する。
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	private Object convertTimestamp(Class<?> type, Object value) {
		return new Date(((Timestamp)value).getTime());
	}
	
	/**
	 * 入力元の値がjava.sql.Dateクラスの場合にjava.util.Dateに変換する。
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	private Object convertJavaSqlDate(Class<?> type, Object value) {
		return new Date(((java.sql.Date)value).getTime());
	}
	
	/**
	 * 入力元の値がLongクラスの場合にjava.util.Dateに変換する。
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	private Object convertLong(Class<?> type, Object value) {
		return new Date(((Long)value));
	}
}
