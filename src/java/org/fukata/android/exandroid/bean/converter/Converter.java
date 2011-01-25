package org.fukata.android.exandroid.bean.converter;

/**
 * BeanUtilで使用するコンバーター
 * 
 * @author Tatsuya Fukata
 * 
 */
public interface Converter {

	/**
	 * 変換処理後の値を返す。
	 * 
	 * @param type
	 *            出力先オブジェクトのパラメータタイプ
	 * @param value
	 *            入力元オブジェクトが持つ、変換対象のフィールドの値
	 * @return
	 */
	Object convert(Class<?> type, Object value);
}
