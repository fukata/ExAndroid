package org.fukata.android.exandroid.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fukata.android.exandroid.bean.converter.Converter;
import org.fukata.android.exandroid.bean.converter.DateConverter;
import org.fukata.android.exandroid.bean.converter.EnumConverter;
import org.fukata.android.exandroid.bean.converter.StringConverter;

/**
 * Java Beans関連のユーティリティ
 * 
 * @author Tatsuya Fukata
 *
 */
public enum BeanUtil {
	;
	/** コンバーターマップ */
	private static Map<Class<?>, Converter> mConverters;

	// コンバーターの初期化
	static {
		initConverters();
	}
	
	/**
	 * origからdestにプロパティをコピーする。
	 * 
	 * @param dest 出力先オブジェクト
	 * @param orig 出力元オブジェクト
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static void copyProperties(Object dest, Object orig) {
		if (dest == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (orig == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}

		List<Field> fields = new ArrayList<Field>();
		
		// オブジェクトが持つすべてのプロパティを設定
		processAttachAllProperties(orig.getClass(), fields);
		
		for (Field field : fields) {
			String propertyName = field.getName();
			if (!(isReadble(orig, propertyName) && isWriteable(dest, propertyName))) {
				continue;
			}
			
			// 出力先オブジェクトにコピーする値
			Object value = null;
			
			try {
				value = getConvertedValue(dest, orig, propertyName);
				copyProperty(dest, propertyName, value);
			} catch (Exception e) {
				continue;
			}
		}
	}

	/**
	 * コンバーターによる変換後の値を返す。
	 * 
	 * @param dest 出力先オブジェクト
	 * @param orig 出力元オブジェクト
	 * @param propertyName プロパティ名
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static Object getConvertedValue(Object dest, Object orig, String propertyName) 
		throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		Object value = ReflectionUtil.getSimpleProperty(orig, propertyName);
		
		// コンバーターから値を取得
		Method method = ReflectionUtil.getWriteableMethod(dest.getClass(), propertyName);
		
		Class<?>[] parameterTypes = method.getParameterTypes();
		Class<?> type = parameterTypes[0];
		Class<?> clazz = type;

		// 変換対象のクラスがコンバーターに登録されていれば変換した値を設定する
		do {
			if (mConverters.containsKey(clazz)) {
				Converter converter = mConverters.get(clazz);
				value = converter.convert(type, value);
				break;
			}
			clazz = clazz.getSuperclass();
		} while(clazz!=null);
		
		return value;
	}

	/**
	 * コンバーターの初期化
	 */
	protected static void initConverters() {
		mConverters = new HashMap<Class<?>, Converter>();
		// String
		mConverters.put(String.class, new StringConverter());
		// Enum
		mConverters.put(Enum.class, new EnumConverter());
		// java.util.Date
		mConverters.put(java.util.Date.class, new DateConverter());
	}

	/**
	 * 継承関係にあるクラス全てのプロパティを取得する。
	 * 
	 * @param clazz
	 * @param fields
	 */
	private static void processAttachAllProperties(Class<? extends Object> clazz,
			List<Field> fields) {
		if (!isEffectiveClass(clazz)) {
			return;
		}

		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			fields.add(field);
		}

		processAttachAllProperties(clazz.getSuperclass(), fields);
	}

	/**
	 * プロパティを取得するのに有効なクラスかどうか。
	 * 
	 * @param clazz
	 * @return
	 */
	private static boolean isEffectiveClass(Class<? extends Object> clazz) {
		if (clazz == null) {
			return false;
		}

		if (clazz == Class.class) {
			return false;
		}

		if (clazz == Object.class) {
			return false;
		}

		return true;
	}

	/**
	 * 出力先オブジェクトに値をコピーする
	 * 
	 * @param dest 出力先オブジェクト
	 * @param propertyName プロパティ名
	 * @param value コピー値
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static void copyProperty(Object dest, String propertyName, Object value)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		Method method = ReflectionUtil.getWriteableMethod(dest.getClass(), propertyName);
		Object[] values = new Object[1];
		values[0] = value;
		method.invoke(dest, values);
	}

	/**
	 * 指定したプロパティが書き込み可能か。
	 * 
	 * @param dest 出力先オブジェクト
	 * @param propertyName プロパティ名
	 * @return
	 */
	private static boolean isWriteable(Object dest, String propertyName) {
		return ReflectionUtil.getWriteableMethod(dest.getClass(), propertyName) != null;
	}

	/**
	 * 指定したプロパティが読み込み可能か。
	 * 
	 * @param orig 出力元オブジェクト
	 * @param propertyName プロパティ名
	 * @return
	 */
	private static boolean isReadble(Object orig, String propertyName) {
		return ReflectionUtil.getReadbleMethod(orig.getClass(), propertyName) != null;
	}

	/**
	 * コンバーターを追加する。
	 * 
	 * @param clazz 変換対象のクラス
	 * @param converter コンバーター
	 */
	public static void addConverter(Class<?> clazz, Converter converter) {
		if (clazz==null || converter==null) {
			throw new IllegalArgumentException("clazz and converter is specified.");
		}

		mConverters.put(clazz, converter);
	}
	
	/**
	 * コンバーターを追加する。
	 * 
	 * @param clazz 変換対象のクラス
	 * @param converter コンバーター
	 */
	public static void addConverter(Map<Class<?>, Converter> converters) {
		if (converters==null) {
			throw new IllegalArgumentException("converters is specified.");
		}

		mConverters.putAll(converters);
	}
	
	/**
	 * 登録されているコンバーターを除外する
	 * 
	 * @param clazz
	 */
	public static void removeConverter(Class<?> clazz) {
		if (clazz==null) {
			throw new IllegalArgumentException("clazz is specified.");
		}

		mConverters.remove(clazz);
	}
	
	/**
	 * 登録されているコンバーターを除外する
	 * 
	 * @param classes
	 */
	public static void removeConverters(Class<?>[] classes) {
		if (classes==null) {
			throw new IllegalArgumentException("classes is specified.");
		}
		
		for (Class<?> clazz : classes) {
			mConverters.remove(clazz);
		}
	}
	
}
