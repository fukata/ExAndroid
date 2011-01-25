package org.fukata.android.exandroid.loader.process;


/**
 * バックグラウンド用処理リクエストの雛形
 * 
 * @author Tatsuya Fukata
 * 
 */
abstract public class BaseRequest implements LoadRequest {
	/** 処理正常終了時コールバック */
	protected Runnable mSuccessCallback;
	/** 処理キャンセル時コールバック */
	protected Runnable mCancelCallback;

	public BaseRequest(final Runnable successCallback,
			final Runnable cancelCallback) {
		mSuccessCallback = successCallback;
		mCancelCallback = cancelCallback;
	}

	public void processRequest(ProcessLoader loader) {
		loader.getHandler().post(mSuccessCallback);
	}

	public void skipRequest(ProcessLoader loader) {
		if (mCancelCallback != null) {
			loader.getHandler().post(mCancelCallback);
		}
	}

}
