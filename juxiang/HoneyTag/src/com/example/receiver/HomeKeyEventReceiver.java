package com.example.receiver;

import com.example.utils.MyApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @Description描述:设置
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class HomeKeyEventReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
			String reason = intent.getStringExtra("reason");
			if (reason.equals("homekey")) {
				// 表示按了home键,程序到了后台
				// Toast.makeText(getApplicationContext(), "home", 1).show();
				MyApplication.getInstance().homekeyFlag = "1";
			} else if (reason.equals("recentapps")) {
				// 表示长按home键,显示最近使用的程序列表
			}
		}

	}

}
