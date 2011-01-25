package org.fukata.android.exandroid.loader.process;

import java.util.concurrent.LinkedBlockingQueue;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * バックグラウンドで処理を実行する。
 * 
 * @author Tatsuya Fukata
 *
 */
public class ProcessLoader {
	/** 処理をロードするActivity */
	protected Activity mActivity;
	/** コンテキスト */
	protected Context mContext;
	/** ハンドラ */
	protected Handler mHandler;
	/** リクエストキュー */
	protected LinkedBlockingQueue<LoadRequest> mLoaderQueue;
	/** 処理用スレッド */
	protected LoaderThread mLoaderThread;
	
	public ProcessLoader(Activity activity) {
		mActivity = activity;
		mContext = activity.getApplicationContext();
		mHandler = new Handler();
		mLoaderQueue = new LinkedBlockingQueue<LoadRequest>();
	}
	
    /**
     * Call this from the activity's onResume()
     */
    public void startBackgroundThread() {
        mLoaderThread = new LoaderThread(mLoaderQueue, this);
        mLoaderThread.start();
    }
    
    /**
     * Call this from the activity's onPause()
     */
    public void stopBackgroundThread() {
        mLoaderThread.shutdown();
    }
    
    public void load(LoadRequest request) {
    	try {
    		this.mLoaderQueue.put(request);
    	} catch (InterruptedException e) {
    		Log.e(getClass().getSimpleName(), "mLoaderQueue interrupted.");
		}
    }

	/**
	 * @return the mContext
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * @return the mHandler
	 */
	public Handler getHandler() {
		return mHandler;
	}

	/**
	 * @return the mLoaderQueue
	 */
	public LinkedBlockingQueue<LoadRequest> getLoaderQueue() {
		return mLoaderQueue;
	}

	/**
	 * @return the mLoaderThread
	 */
	public LoaderThread getLoaderThread() {
		return mLoaderThread;
	}

	/**
	 * @return the mActivity
	 */
	public Activity getActivity() {
		return mActivity;
	}
}
