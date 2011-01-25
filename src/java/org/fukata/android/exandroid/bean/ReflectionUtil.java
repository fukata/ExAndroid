package org.fukata.android.exandroid.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * リフレクションを使用し、クラスのインスタンスを生成する。 指定するクラスは、内部クラスではなく、外部クラスであること。
 * 
 * @author Tatsuya Fukata
 * 
 */

public enum ReflectionUtil {
	;
	/** getterメソッドのキャッシュ */
	private static WeakHashMap<Class<?>, WeakHashMap<String, Method>> mCacheReadbleMethodMap = new WeakHashMap<Class<?>, WeakHashMap<String,Method>>();
	/** setterメソッドのキャッシュ */
	private static WeakHashMap<Class<?>, WeakHashMap<String, Method>> mCacheWriteableMethodMap = new WeakHashMap<Class<?>, WeakHashMap<String,Method>>();
	/** アクセス可能フィールドのキャッシュ */
	private static WeakHashMap<Class<?>, WeakHashMap<String, Object>> mCacheSimplePropertyMap = new WeakHashMap<Class<?>, WeakHashMap<String,Object>>();
	/**
	 * 指定されたクラスのインスタンスを生成し、返す。
	 * 
	 * @param <T>
	 * @param clazz
	 *            生成対象のクラス
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> T newInstance(Class<T> clazz)
			throws IllegalAccessException, InstantiationException {
		return clazz.newInstance();
	}

	/**
	 * 引数ありコンストラクタを使用しインスタンスを生成し、返す。
	 * 
	 * @param <T>
	 * @param clazz
	 *            生成対象のクラス
	 * @param parameterTypes
	 *            コンストラクタのパラメータの型
	 * @param params
	 *            コンストラクタのパラメータ
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> clazz, Class[] parameterTypes,
			Object[] args) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<T> constructor = clazz.getConstructor(parameterTypes);
		T instance = constructor.newInstance(args);
		return instance;
	}

	/**
	 * インスタンスの引数ありのメソッドを実行する。
	 * 
	 * @param obj
	 *            メソッドを実行するインスタンス
	 * @param methodName
	 *            実行するメソッド名
	 * @param parameterTypes
	 *            実行するメソッドの引数型リスト
	 * @param args
	 *            引数(空の場合はnull)
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeMethod(Object obj, String methodName,
			Class[] parameterTypes, Object[] args) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Class<? extends Object> clazz = obj.getClass();
		Method method = clazz.getMethod(methodName, parameterTypes);
		return method.invoke(obj, args);
	}

	/**
	 * インスタンスの引数なしのメソッドを実行する。
	 * 
	 * @param obj
	 * @param methodName
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokeMethod(Object obj, String methodName)
			throws SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		return invokeMethod(obj, methodName, null, null);
	}

	/**
	 * 引数ありのクラスメソッドを実行する。
	 * 
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @param args
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeMethod(Class<? extends Object> clazz,
			String methodName, Class[] parameterTypes, Object[] args)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Method method = clazz.getMethod(methodName, parameterTypes);
		return method.invoke(null, args);
	}

	/**
	 * 引数なしのクラスメソッドを実行する。
	 * 
	 * @param clazz
	 * @param methodName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokeMethod(Class<? extends Object> clazz,
			String methodName) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		return invokeMethod(clazz, methodName, null, null);
	}
	/**
	 * インスタンスの引数ありのメソッドを実行する。
	 * <p>
	 * 指定されたメソッド名がprivateメソッドの場合、
	 * 可能な限りアクセスを試みる。
	 * </p>
	 * 
	 * @param obj
	 *            メソッドを実行するインスタンス
	 * @param methodName
	 *            実行するメソッド名
	 * @param parameterTypes
	 *            実行するメソッドの引数型リスト
	 * @param args
	 *            引数(空の場合はnull)
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeDeclaredMethod(Object obj, String methodName,
			Class[] parameterTypes, Object[] args) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Class<? extends Object> clazz = obj.getClass();
		Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
		method.setAccessible(true);
		return method.invoke(obj, args);
	}
	
	/**
	 * インスタンスの引数なしのメソッドを実行する。
	 * <p>
	 * 指定されたメソッド名がprivateメソッドの場合、
	 * 可能な限りアクセスを試みる。
	 * </p>
	 * 
	 * @param obj
	 * @param methodName
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokeDeclaredMethod(Object obj, String methodName)
	throws SecurityException, IllegalArgumentException,
	NoSuchMethodException, IllegalAccessException,
	InvocationTargetException {
		return invokeDeclaredMethod(obj, methodName, null, null);
	}
	
	/**
	 * 引数ありのクラスメソッドを実行する。
 	 * <p>
	 * 指定されたメソッド名がprivateメソッドの場合、
	 * 可能な限りアクセスを試みる。
	 * </p>
	 * 
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @param args
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeDeclaredMethod(Class<? extends Object> clazz,
			String methodName, Class[] parameterTypes, Object[] args)
	throws SecurityException, NoSuchMethodException,
	IllegalArgumentException, IllegalAccessException,
	InvocationTargetException {
		Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
		return method.invoke(null, args);
	}
	
	/**
	 * 引数なしのクラスメソッドを実行する。
	 * <p>
	 * 指定されたメソッド名がprivateメソッドの場合、
	 * 可能な限りアクセスを試みる。
	 * </p>
	 * 
	 * @param clazz
	 * @param methodName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokeDeclaredMethod(Class<? extends Object> clazz,
			String methodName) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		return invokeDeclaredMethod(clazz, methodName, null, null);
	}

	/**
	 * プロパティの値を取得する。 アクセスレベルがpublicの場合は、直接プロパティにアクセスし、そうでない場合は、getterから値を取得する。
	 * 
	 * @param obj 取得対象のインスタンス
	 * @param name 取得対象のプロパティ名
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object getSimpleProperty(Object obj, String name)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		return getSimpleProperty(obj.getClass(), obj, name);
	}

	/**
	 * Staticプロパティの値を取得する。
	 * 
	 * @param clazz 取得対象のクラス
	 * @param name 取得対象のプロパティ名
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object getSimpleProperty(Class<? extends Object> clazz,
			String name) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		return getSimpleProperty(clazz, null, name);
	}

	/**
	 * プロパティの値を取得する。
	 * 
	 * @param clazz 取得対象のクラス
	 * @param obj 取得対象のインスタンス
	 * @param name 取得対象のプロパティ名
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static Object getSimpleProperty(Class<? extends Object> clazz,
			Object obj, String name) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		// キャッシュに登録されていれば、それを返す。
		if (existsCacheSimpleProperty(clazz, name)) {
			return getCacheSimpleProperty(clazz, name);
		}
		
		// フィールドから取得
		Object value = getPublicProperty(clazz, obj, name);
		if (value == null) {
			// メソッドから取得
			Method method = getReadbleMethod(clazz, name);
			if (method != null) {
				value = method.invoke(obj);
			}
		} else {
			setCacheSimpleProperty(clazz, name, value);
		}
		return value;
	}

	/**
	 * アクセスレベル「public」のフィールドから値を取得する。
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	private static Object getPublicProperty(Class<? extends Object> clazz,
			String name) {
		return getPublicProperty(clazz, null, name);
	}

	/**
	 * アクセスレベル「public」のフィールドから値を取得する。
	 * 
	 * @param clazz
	 * @param obj
	 * @param name
	 * @return
	 */
	private static Object getPublicProperty(Class<? extends Object> clazz,
			Object obj, String name) {
		Object value = null;
		do {

			try {
				Field field = clazz.getField(name);
				value = field.get(obj);
			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}

			clazz = clazz.getSuperclass();
		} while (clazz != null);
		return value;
	}

	/**
	 * getterメソッドを取得する。
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method getReadbleMethod(Class<? extends Object> clazz,
			String name) {
		// キャッシュに登録されている場合、それを返す。
		if (existsCacheReadbleMethod(clazz, name)) {
			return getCacheReadbleMethod(clazz, name);
		}
		
		Method[] methods = clazz.getMethods();
		Method method = null;
		for (Method m : methods) {
			if (!isAccessible(m)) {
				continue;
			}
			String getterName = m.getName();
			// 指定したプロパティのgetterかどうか判定
			String regexPattern = "^(is|get)" + toHeadUpperCase(name) + "$";
			Pattern compile = Pattern.compile(regexPattern);
			Matcher matcher = compile.matcher(getterName);
			if (matcher.matches()) {
				method = m;
				break;
			}
		}

		// キャッシュに登録
		setCacheReadbleMethod(clazz, name, method);
		return method;
	}

	/**
	 * setterメソッドを取得する。
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method getWriteableMethod(Class<? extends Object> clazz,
			String name) {
		// キャッシュに登録されている場合、それを返す。
		if (existsCacheWriteableMethod(clazz, name)) {
			return getCacheWriteableMethod(clazz, name);
		}
		
		Method[] methods = clazz.getMethods();
		Method method = null;
		for (Method m : methods) {
			if (!isAccessible(m)) {
				continue;
			}
			String getterName = m.getName();
			// 指定したプロパティのsetterかどうか判定
			String regexPattern = "^set" + toHeadUpperCase(name) + "$";
			Pattern compile = Pattern.compile(regexPattern);
			Matcher matcher = compile.matcher(getterName);
			if (matcher.matches()) {
				method = m;
				break;
			}
		}
		
		// キャッシュに登録
		setCacheWriteableMethod(clazz, name, method);
		return method;
	}

	/**
	 * 対象のメソッドのアクセス修飾子がpublicかどうか。
	 * 
	 * @param method
	 * @return
	 */
	private static boolean isAccessible(Method method) {
		return Modifier.isPublic(method.getModifiers());
	}

	/**
	 * 先頭文字を大文字に変換して返す。
	 * 
	 * @param name
	 * @return
	 */
	private static String toHeadUpperCase(String name) {
		char[] charArray = name.toCharArray();
		if (charArray.length > 0) {
			String head = String.valueOf(charArray[0]);
			charArray[0] = head.toUpperCase().charAt(0);
		}
		return String.valueOf(charArray);
	}

	/**
	 * 読み込み用メソッドをキャッシュに登録する。
	 * @param clazz
	 * @param field
	 * @param method
	 */
	private static void setCacheReadbleMethod(Class<?> clazz, String field, Method method) {
		WeakHashMap<String, Method> map = getCacheReadbleMethodMap(clazz);
		if (!map.containsKey(field)) {
			map.put(field, method);
		}
	}

	/**
	 * 書き込み用メソッドをキャッシュに登録する。
	 * @param clazz
	 * @param field
	 * @param method
	 */
	private static void setCacheWriteableMethod(Class<?> clazz, String field, Method method) {
		WeakHashMap<String, Method> map = getCacheWriteableMethodMap(clazz);
		if (!map.containsKey(field)) {
			map.put(field, method);
		}
	}

	/**
	 * キャッシュから書き込み用メソッドマップを取得する。
	 * @param clazz
	 * @return
	 */
	private static WeakHashMap<String, Method> getCacheWriteableMethodMap(Class<?> clazz) {
		WeakHashMap<String, Method> map;
		if (mCacheWriteableMethodMap.containsKey(clazz)) {
			map = mCacheWriteableMethodMap.get(clazz);
		} else {
			map = new WeakHashMap<String, Method>();
			mCacheWriteableMethodMap.put(clazz, map);
		}
		return map;
	}

	/**
	 * キャッシュから読み込み用メソッドマップを取得する。
	 * @param clazz
	 * @return
	 */
	private static WeakHashMap<String, Method> getCacheReadbleMethodMap(Class<?> clazz) {
		WeakHashMap<String, Method> map;
		if (mCacheReadbleMethodMap.containsKey(clazz)) {
			map = mCacheReadbleMethodMap.get(clazz);
		} else {
			map = new WeakHashMap<String, Method>();
			mCacheReadbleMethodMap.put(clazz, map);
		}
		return map;
	}
	
	/**
	 * キャッシュから読み込み用メソッドを取得する。
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	private static Method getCacheReadbleMethod(Class<?> clazz, String fieldName) {
		WeakHashMap<String, Method> map = getCacheReadbleMethodMap(clazz);
		if (map.containsKey(fieldName)) {
			return map.get(fieldName);
		} else {
			return null;
		}
	}

	/**
	 * キャッシュから書き込み用メソッドを取得する。
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	private static Method getCacheWriteableMethod(Class<?> clazz, String fieldName) {
		WeakHashMap<String, Method> map = getCacheWriteableMethodMap(clazz);
		if (map.containsKey(fieldName)) {
			return map.get(fieldName);
		} else {
			return null;
		}
	}

	/**
	 * 対象のフィールド名の読み込み用メソッドがキャッシュに登録されているかを返す。
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	private static boolean existsCacheReadbleMethod(Class<?> clazz, String fieldName) {
		WeakHashMap<String, Method> map = getCacheReadbleMethodMap(clazz);
		return map.containsKey(fieldName);
	}
	
	/**
	 * 対象のフィールド名の書き込み用メソッドがキャッシュに登録されているかを返す。
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	private static boolean existsCacheWriteableMethod(Class<?> clazz, String fieldName) {
		WeakHashMap<String, Method> map = getCacheWriteableMethodMap(clazz);
		return map.containsKey(fieldName);
	}

	/**
	 * アクセス可能フィールド値のキャッシュマップを取得する。
	 * @param clazz
	 * @return
	 */
	private static WeakHashMap<String, Object> getCacheSimplePropertyMap(Class<?> clazz) {
		WeakHashMap<String, Object> map;
		if (mCacheSimplePropertyMap.containsKey(clazz)) {
			map = mCacheSimplePropertyMap.get(clazz);
		} else {
			map = new WeakHashMap<String, Object>();
			mCacheSimplePropertyMap.put(clazz, map);
		}
		return map;
	}
	
	/**
	 * アクセス可能フィールド値をキャッシュから取得する。
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	private static Object getCacheSimpleProperty(Class<?> clazz, String fieldName) {
		WeakHashMap<String, Object> map = getCacheSimplePropertyMap(clazz);
		if (map.containsKey(fieldName)) {
			return map.get(fieldName);
		} else {
			return null;
		}
	}
	
	/**
	 * アクセス可能フィールド値のキャッシュを設定する。
	 * @param clazz
	 * @param fieldName
	 * @param value
	 */
	private static void setCacheSimpleProperty(Class<?> clazz, String fieldName, Object value) {
		WeakHashMap<String,Object> map = getCacheSimplePropertyMap(clazz);
		if (!map.containsKey(fieldName)) {
			map.put(fieldName, value);
		}
	}
	
	/**
	 * アクセス可能フィールド値がキャッシュに登録されているかを返す。
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	private static boolean existsCacheSimpleProperty(Class<?> clazz, String fieldName) {
		WeakHashMap<String, Object> map = getCacheSimplePropertyMap(clazz);
		return map.containsKey(fieldName);
	}
}
