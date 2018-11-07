package com.jackie.ysdkmonitor.entity;

import com.jackie.ysdkmonitor.util.Util;

public class HistoryEntity extends BaseEntity {
	private int times;
	private String timestampDisp;
	private long timestamp;

	public HistoryEntity(int times, long timestamp) {
		this.times = times;
		this.timestampDisp = Util.formatDate(timestamp);
		this.timestamp = timestamp;
	}

	public int getTimes() {
		return times;
	}

	public String getTimestampDisp() {
		return timestampDisp;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
