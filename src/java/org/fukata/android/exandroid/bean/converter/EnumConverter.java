package org.fukata.android.exandroid.bean.converter;

import java.lang.reflect.Method;

/**
 * Enumクラスに対するコンバーター
 * 
 * @author Tatsuya Fukata
 *
 */
public class EnumConverter implements Converter {

	public Object convert(Class<?> type, Object value) {
		Object converted = value;
		
		converted = convertFind(type, value);
		
		return converted;
	}
	
	/**
	 * 入力元の値がEnumクラスの時にfindメソッドを実行し値を取得する
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	private Object convertFind(Class<?> type, Object value) {
		Object converted = value;
		
		try {
			Method method = type.getMethod("find", new Class[] { String.class });
			if (method != null) {
				converted = method.invoke(null, value);
			}
		} catch (Exception e) {
		}
		
		return converted;
	}

}
