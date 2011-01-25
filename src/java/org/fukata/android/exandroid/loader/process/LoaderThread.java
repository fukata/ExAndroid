package org.fukata.android.exandroid.loader.process;

import java.util.concurrent.LinkedBlockingQueue;


import android.os.Process;
import android.util.Log;

/**
 * バックグラウンド処理実行用スレッド
 * 
 * @author Tatsuya Fukata
 * 
 */
public class LoaderThread extends Thread {
	/** 処理キュー */
	LinkedBlockingQueue<LoadRequest> mQueue;
	/** 処理ローダー */
	ProcessLoader mLoader;

	public LoaderThread(LinkedBlockingQueue<LoadRequest> queue,
			ProcessLoader loader) {
		mQueue = queue;
		mLoader = loader;
	}

	public void shutdown() {
		try {
			mQueue.put(new ShutdownRequest());
		} catch (InterruptedException ex) {
			// The put() method fails with InterruptedException if the
			// queue is full. This should never happen because the queue
			// has no limit.
			// Log.e("Cal", "LoaderThread.shutdown() interrupted!");
		}
	}

	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		while (true) {
			try {
				// Wait for the next request
				LoadRequest request = mQueue.take();

				// If there are a bunch of requests already waiting, then
				// skip all but the most recent request.
				while (!mQueue.isEmpty()) {
					// Let the request know that it was skipped
					request.skipRequest(mLoader);

					// Skip to the next request
					request = mQueue.take();
				}

				// シャットダウンリクエストの場合、停止
				if (request instanceof ShutdownRequest) {
					return;
				}
				
				request.processRequest(mLoader);
			} catch (InterruptedException ex) {
				Log.e("Cal", "background LoaderThread interrupted!");
			}
		}
	}
}