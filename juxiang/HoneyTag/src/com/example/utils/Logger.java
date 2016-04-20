package com.example.utils;

import android.util.Log;

/**
 * 日志工具类
 * 
 * @author huwei
 * 
 */

public class Logger {

	private static final String TAG = "Logger";
	public static final boolean debugOn = true;// 日志打印开关

	public static void i(String sMessage) {
		if (debugOn) {
			i(TAG, sMessage);
		}
	}

	public static void i(String sTag, String sMessage) {
		if (debugOn) {
			if (null != sMessage) {
				Log.i(sTag, sMessage);
			}
		}
	}

	// Warning Info
	public static void w(String sTag, String sMessage) {
		if (debugOn) {
			if (null != sMessage) {
				Log.w(sTag, sMessage);
			}
		}
	}

	// Error Info
	public static void e(String sMessage) {
		if (debugOn) {
			if (null != sMessage) {
				e(TAG, sMessage);
			}
		}
	}

	public static void e(String sTag, String sMessage) {
		if (debugOn) {
			if (null != sMessage) {
				Log.e(sTag, sMessage);
			}
		}
	}

}
