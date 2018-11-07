package com.jackie.ysdkmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.jackie.ysdkmonitor.entity.HistoryEntity;
import com.jackie.ysdkmonitor.history.HistoryAdapter;
import com.jackie.ysdkmonitor.service.ApiService;
import com.jackie.ysdkmonitor.util.Util;

import java.lang.ref.WeakReference;
import java.util.Date;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static Handler uiHandler;
	public static final int MSG_UPDATE_UI = 1;

	private TextView timesTv;
	private TextView timestampTv;
	private RecyclerView historyRv;
	private HistoryAdapter historyAdapter;
	private LinearLayoutManager layoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		uiHandler = new Handler(new HandlerCallback(this));
		startService(new Intent(this, ApiService.class));
	}

	long preTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long currentTime = new Date().getTime();
			// 如果时间间隔大于2秒，不处理
			if ((currentTime - preTime) > 1000) {
				// 显示消息
				Toast.makeText(this, "再按一次退出！", Toast.LENGTH_SHORT).show();
				//更新时间
				preTime = currentTime;
				//截获事件，不再处理
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(new Intent(this, ApiService.class));
	}

	private void initView() {
		timesTv = findViewById(R.id.times_tv);
		timestampTv = findViewById(R.id.timestamp_tv);

		historyAdapter = new HistoryAdapter(null);
		layoutManager = new LinearLayoutManager(this);
		historyRv = findViewById(R.id.history_rv);
		historyRv.setLayoutManager(layoutManager);
		historyRv.setAdapter(historyAdapter);
	}

	public static Handler getUIHandler() {
		return uiHandler;
	}

	private static class HandlerCallback implements Handler.Callback {
		private WeakReference<MainActivity> mainActivityWeakReference;

		public HandlerCallback(MainActivity mainActivity) {
			this.mainActivityWeakReference = new WeakReference<>(mainActivity);
		}

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_UPDATE_UI: {
					Object[] data = (Object[])msg.obj;
					Integer count = (Integer) data[0];
					Long timestamp = (Long) data[1];
					MainActivity mainActivity = mainActivityWeakReference.get();
					if (mainActivity != null) {

						String times = String.valueOf(count);
						mainActivity.timesTv.setText(times);
						mainActivity.timestampTv.setText(Util.formatDate(timestamp));
						mainActivity.historyAdapter.appendData(new HistoryEntity(count, timestamp));
						mainActivity.layoutManager.scrollToPosition(mainActivity.historyAdapter.getSize() - 1);
					}
					break;
				}
			}
			return false;
		}
	}
}
