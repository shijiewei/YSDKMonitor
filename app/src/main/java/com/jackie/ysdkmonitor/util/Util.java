package com.jackie.ysdkmonitor.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	public static String formatDate(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		return sdf.format(new Date(timestamp));
	}
}
