package org.fukata.android.exandroid.bean.converter;

import java.lang.reflect.Method;

/**
 * Stringクラスに対するコンバーター
 * 
 * @author Tatsuya Fukata
 *
 */
public class StringConverter implements Converter {

	@Override
	public Object convert(Class<?> type, Object value) {
		Object converted = value;
		
		if (value instanceof Enum<?>) {
			converted = convertEnum(type, value);
		}
		
		return converted;
	}

	/**
	 * 入力元の値がEnumクラスの時にgetValueメソッドの戻り値を返す。
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	private Object convertEnum(Class<?> type, Object value) {
		Object converted = value;

		try {
			Method method = value.getClass().getMethod("getValue", new Class[] {value.getClass()});
			if (method!=null) {
				converted = method.invoke(null, new Object[] {value});
			}
		} catch (Exception e) {
		}
		
		return converted;
	}
}
