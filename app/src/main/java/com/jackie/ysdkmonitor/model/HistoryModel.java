package com.jackie.ysdkmonitor.model;

import android.content.Context;
import android.util.Log;

import com.jackie.ysdkmonitor.util.Const;
import com.youzu.yad.YAD;
import com.youzu.yad.model.Identity;

import java.util.HashMap;
import java.util.Map;

public class HistoryModel {
	private static final String TAG = "HistoryModel";

	private int gameId = Const.GAME_ID;
	private int opId = Const.OP_ID;
	private String gameVersion = Const.GAME_VERSION;
	private YAD.Debug debug = YAD.Debug.DEBUG;
	private String CUSTOM_KEY_TIMES = "times";

	public HistoryModel(Context context) {
		Log.d(TAG, "====== Initialize YAD ======" + "\nGameId: " + gameId + "\nOpId: " + opId
				+ "\nGameVersion: " + gameVersion + "\nDebug: " + debug + "\n==============================");
		YAD.init(context, gameId, opId, gameVersion, debug);
	}

	public void callApi(int currTimes) {
		Identity identity = new Identity("testaccount" + currTimes, "testserver", 1, "testid" + currTimes);
		Map<String, Integer> custom = new HashMap<>();
		custom.put(CUSTOM_KEY_TIMES, currTimes);
		YAD.activeEvent(identity, custom);
		Log.d(TAG, "=====> Call active api for " + currTimes + " times");
	}
}
