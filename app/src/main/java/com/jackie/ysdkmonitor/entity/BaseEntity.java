package com.jackie.ysdkmonitor.entity;

import com.google.gson.Gson;

public class BaseEntity {
	protected String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
