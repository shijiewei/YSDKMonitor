package com.jackie.ysdkmonitor.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class MyHandlerThread extends Thread {
	@Override
	public void run() {
		Looper.prepare();
		onLooperPrepared(Looper.myLooper());
		Looper.loop();
	}

	protected void onLooperPrepared(Looper looper){}

	public static Handler newHandler(final Handler.Callback callbck) {
		final Handler[] handler = new Handler[1];
		MyHandlerThread thread = new MyHandlerThread() {
			@Override
			public void onLooperPrepared(Looper looper) {
				synchronized (handler) {
					handler[0] = new Handler(looper, callbck);
					handler.notifyAll();
				}
			}
		};
		synchronized (handler) {
			try {
				thread.start();
				handler.wait();
			} catch (InterruptedException e) {
				Log.e("MyHandlerThread", e.getMessage());
			}
		}
		return handler[0];
	}
}
