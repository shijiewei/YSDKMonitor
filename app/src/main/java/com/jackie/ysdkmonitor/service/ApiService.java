package com.jackie.ysdkmonitor.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jackie.ysdkmonitor.model.HistoryModel;
import com.jackie.ysdkmonitor.presenter.HistoryPresenter;
import com.jackie.ysdkmonitor.util.Const;
import com.jackie.ysdkmonitor.util.MyHandlerThread;

public class ApiService extends Service implements Handler.Callback {
	private static final String TAG = "ApiService";
	private static final int MSG_CALL_API = 0;
	private Handler uiHandler;
	private Handler workHandler;
	private int counter = 0;
	private HistoryModel historyModel;


	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "ApiService started.");

		historyModel = new HistoryModel(this);

		uiHandler = HistoryPresenter.getUIHandler();
		workHandler = MyHandlerThread.newHandler(this);
		workHandler.sendEmptyMessage(MSG_CALL_API);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "ApiService destroyed.");

		workHandler.removeMessages(MSG_CALL_API);
		workHandler = null;
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case MSG_CALL_API: {
				counter++;
				// Call target api
				historyModel.callApi(counter);
				// Send message to update UI
				Message message = Message.obtain();
				message.what = HistoryPresenter.MSG_UPDATE_UI;
				Object[] obj = new Object[2];
				obj[0] = counter;
				obj[1] = System.currentTimeMillis();
				message.obj = obj;
				if (uiHandler != null) {
					uiHandler.sendMessage(message);
				}
				// Get into next invocation after specified seconds
				if (workHandler != null) {
					workHandler.sendEmptyMessageDelayed(MSG_CALL_API, Const.CALL_API_INTERVAL);
				}
				break;
			}
		}
		return false;
	}
}
