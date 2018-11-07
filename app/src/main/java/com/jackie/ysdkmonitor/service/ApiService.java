package com.jackie.ysdkmonitor.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jackie.ysdkmonitor.MainActivity;
import com.jackie.ysdkmonitor.util.Const;
import com.jackie.ysdkmonitor.util.MyHandlerThread;
import com.youzu.yad.YAD;
import com.youzu.yad.model.Identity;

import java.util.HashMap;
import java.util.Map;

public class ApiService extends Service implements Handler.Callback {
	private static final String TAG = "ApiService";
	private static final int MSG_CALL_API = 0;
	private int gameId = Const.GAME_ID;
	private int opId = Const.OP_ID;
	private String gameVersion = Const.GAME_VERSION;
	private YAD.Debug debug = YAD.Debug.DEBUG;
	private String CUSTOM_KEY_TIMES = "times";
	private Handler uiHandler;
	private Handler workHandler;
	private int counter = 0;


	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "ApiService started.");

		uiHandler = MainActivity.getUIHandler();
		workHandler = MyHandlerThread.newHandler(this);

		testYAD();
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
				callApi(counter);
				// Send message to update UI
				Message message = Message.obtain();
				message.what = MainActivity.MSG_UPDATE_UI;
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

	private void testYAD() {
		Log.d(TAG, "====== Initialize YAD ======" + "\nGameId: " + gameId + "\nOpId: " + opId
				+ "\nGameVersion: " + gameVersion + "\nDebug: " + debug + "\n==============================");
		YAD.init(this, gameId, opId, gameVersion, debug);

		workHandler.sendEmptyMessage(MSG_CALL_API);
	}

	private void callApi(int currTimes) {
		Identity identity = new Identity("testaccount" + currTimes, "testserver", 1, "testid" + currTimes);
		Map<String, Integer> custom = new HashMap<>();
		custom.put(CUSTOM_KEY_TIMES, currTimes);
		YAD.activeEvent(identity, custom);
		Log.d(TAG, "=====> Call active api for " + currTimes + " times");
	}
}
