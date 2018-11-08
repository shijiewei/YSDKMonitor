package com.jackie.ysdkmonitor.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.jackie.ysdkmonitor.R;
import com.jackie.ysdkmonitor.entity.HistoryEntity;
import com.jackie.ysdkmonitor.history.HistoryAdapter;
import com.jackie.ysdkmonitor.presenter.HistoryPresenter;

import java.util.Date;

public class MainActivity extends Activity implements IHistoryView {
	private static final String TAG = "MainActivity";

	private TextView timesTv;
	private TextView timestampTv;
	private RecyclerView historyRv;
	private HistoryAdapter historyAdapter;
	private LinearLayoutManager layoutManager;
	private HistoryPresenter hPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		hPresenter = new HistoryPresenter(this);
		hPresenter.startService();
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
		hPresenter.stopService();
	}

	@Override
	public void onDataChanged(HistoryEntity data) {
		if (data != null) {
			timesTv.setText(String.valueOf(data.getTimes()));
			timestampTv.setText(data.getTimestampDisp());
			historyAdapter.appendData(data);
			// Force scroll to the latest line
			layoutManager.scrollToPosition(historyAdapter.getSize() - 1);
		}
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
}
