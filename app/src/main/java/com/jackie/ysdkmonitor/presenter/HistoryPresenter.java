package com.jackie.ysdkmonitor.presenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.jackie.ysdkmonitor.entity.HistoryEntity;
import com.jackie.ysdkmonitor.service.ApiService;
import com.jackie.ysdkmonitor.view.IHistoryView;
import com.jackie.ysdkmonitor.view.MainActivity;

import java.lang.ref.WeakReference;

public class HistoryPresenter {
	private static final String TAG = "HistoryPresenter";
	public static final int MSG_UPDATE_UI = 1;

	private MainActivity activity;
	private static Handler uiHandler;
	private IHistoryView iHistoryView;

	public HistoryPresenter(IHistoryView iHistoryView) {
		this.iHistoryView = iHistoryView;
		this.activity = (MainActivity)iHistoryView;
		uiHandler = new Handler(new HandlerCallback(this));
	}

	public void startService() {
		activity.startService(new Intent(activity, ApiService.class));
	}

	public void stopService() {
		activity.stopService(new Intent(activity, ApiService.class));
	}

	public static Handler getUIHandler() {
		return uiHandler;
	}

	private static class HandlerCallback implements Handler.Callback {
		private WeakReference<HistoryPresenter> historyPresenterWeakReference;

		private HandlerCallback(HistoryPresenter historyPresenter) {
			this.historyPresenterWeakReference = new WeakReference<>(historyPresenter);
		}

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_UPDATE_UI: {
					Object[] data = (Object[])msg.obj;
					Integer count = (Integer) data[0];
					Long timestamp = (Long) data[1];
					HistoryPresenter historyPresenter = historyPresenterWeakReference.get();
					if (historyPresenter != null) {
						historyPresenter.iHistoryView.onDataChanged(new HistoryEntity(count, timestamp));
					}
					break;
				}
			}
			return false;
		}
	}
}
