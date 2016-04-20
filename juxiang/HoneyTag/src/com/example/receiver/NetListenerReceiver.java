package com.example.receiver;

import com.example.config.MySharedPreference;
import com.example.dto.User;
import com.example.http.Loadings;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.utils.VersionManagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * @Description描述:设置
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class NetListenerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conn.getActiveNetworkInfo();
		if (networkInfo != null) {
			User user = MySharedPreference.readUser(context);
			if (user != null && !TextUtils.isEmpty(MyApplication.getInstance().device_token)) {
				Loadings.send(MyApplication.getInstance().device_token);
			}
//			if (networkInfo.isAvailable()) {
//				VersionManagement.getInstance().checkUpdate(context, PublicStaticURL.VERSION);
//			}
		}

	}

}
