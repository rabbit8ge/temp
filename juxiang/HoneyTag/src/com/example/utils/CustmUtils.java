package com.example.utils;

import android.content.Context;
import android.os.Environment;

public class CustmUtils {

	public static void postLocation(Context context) {

	}

	/**
	 * 
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
