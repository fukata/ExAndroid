package org.fukata.android.exandroid.loader.process;


/**
 * バックグラウンド用処理リクエスト
 * 
 * @author Tatsuya Fukata
 * 
 */
public interface LoadRequest {
	/**
	 * 実行する処理
	 *  
	 * @param loader 
	 */
	public void processRequest(ProcessLoader loader);

	/**
	 * スキップ時に実行する処理
	 * 
	 * @param loader
	 */
	public void skipRequest(ProcessLoader loader);
}
