package com.jackie.ysdkmonitor.history;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jackie.ysdkmonitor.R;
import com.jackie.ysdkmonitor.entity.HistoryEntity;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryVH> {
	private List<HistoryEntity> data;

	public HistoryAdapter(@Nullable List<HistoryEntity> data) {
		if (data == null) {
			this.data = new ArrayList<>();
		} else {
			this.data = data;
		}
	}

	@NonNull
	@Override
	public HistoryVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_history_item, viewGroup, false);
		return new HistoryVH(v);
	}

	@Override
	public void onBindViewHolder(@NonNull HistoryVH historyVH, int i) {
		historyVH.times.setText(String.valueOf(data.get(i).getTimes()));
		historyVH.timestampDisp.setText(data.get(i).getTimestampDisp());
		historyVH.timestamp.setText(String.valueOf(data.get(i).getTimestamp()));
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public void setData(@NonNull List<HistoryEntity> newData) {
		this.data = newData;
		notifyDataSetChanged();
	}

	public void appendDatas(@NonNull List<HistoryEntity> data) {
		if (this.data == null) {
			this.data = new ArrayList<>();
		}
		this.data.addAll(data);
		notifyDataSetChanged();
	}

	public void appendData(@NonNull HistoryEntity data) {
		if (this.data == null) {
			this.data = new ArrayList<>();
		}
		this.data.add(data);
		notifyDataSetChanged();
	}

	public int getSize() {
		return this.data != null ? this.data.size() : 0;
	}

	public static class HistoryVH extends RecyclerView.ViewHolder {
		public final TextView times;
		public final TextView timestampDisp;
		public final TextView timestamp;

		public HistoryVH(@NonNull View itemView) {
			super(itemView);
			times = itemView.findViewById(R.id.history_item_times_tv);
			timestampDisp = itemView.findViewById(R.id.history_item_timestampDisp_tv);
			timestamp = itemView.findViewById(R.id.history_item_timestamp_tv);
		}
	}
}
