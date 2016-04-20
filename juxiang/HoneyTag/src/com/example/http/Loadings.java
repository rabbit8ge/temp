package com.example.http;

import org.json.JSONObject;

import com.example.config.Constants;
import com.example.config.MySharedPreference;
import com.example.fragment.FragmentShouYe;
import com.example.honeytag1.R;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.message.UmengRegistrar;

import android.graphics.Color;
import android.util.Log;

public class Loadings {

	/*
	 * 获得消息列表数量
	 */
	public static void getCount(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

				// SimpleHUD.showInfoMessage(context,
				// context.getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("消息" + str1);

				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						String result = json.getString("result");
						// if (result.equals("")) {
						// main_tab_unread_tv.setBackgroundResource(R.drawable.circle_gray);
						// main_tab_unread_tv.setText("0");
						// main_tab_unread_tv.setTextColor(Color.GRAY);
						//
						// } else {
						// if (PublicStaticURL.IsLogin == true) {
						// main_tab_unread_tv.setBackgroundResource(R.drawable.circle_red);
						// main_tab_unread_tv.setTextColor(Color.RED);
						// main_tab_unread_tv.setText(result); // 提醒数量
						// }
						// }

						if (result.equals("")) {
							if (FragmentShouYe.main_tab_unread_tv != null) {
								FragmentShouYe.main_tab_unread_tv.setBackgroundResource(R.drawable.circle_gray);
								FragmentShouYe.main_tab_unread_tv.setText("0");
								FragmentShouYe.main_tab_unread_tv.setTextColor(Color.GRAY);
							}

						} else {
							if (PublicStaticURL.IsLogin == true && FragmentShouYe.main_tab_unread_tv != null) {
								FragmentShouYe.main_tab_unread_tv.setBackgroundResource(R.drawable.circle_red);
								FragmentShouYe.main_tab_unread_tv.setTextColor(Color.RED);
								FragmentShouYe.main_tab_unread_tv.setText(result); // 提醒数量
							}
						}
						MyApplication.getInstance().count = result;

					}
					if ("3".equals(code)) {

						// SimpleHUD.showInfoMessage(context,
						// context.getString(R.string.get_notifycation_failed));
					}

				} catch (Exception e) {
					// SimpleHUD.showInfoMessage(context,
					// context.getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				} finally {

				}
			}
		});

	}

	/*
	 * 将device_token提交给服务器
	 */
	public static void send(String device_token) {

		RequestParams params = new RequestParams();
		params.addBodyParameter("device_type", Constants.deviceType);
		// params.addBodyParameter("device_token",
		// "Ait2Lq1w_uyUCYr5h6RvJ9VVQK5G4A2285SLVTx0tFV1");
		params.addBodyParameter("device_token", device_token);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, PublicStaticURL.PUSH, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i(str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {

					}
					if ("3".equals(code)) {

					}

				} catch (Exception e) {
					Log.e("message", "走catch了");

					e.printStackTrace();
				}
			}

		});

	}
	
	/* 阅读栏目 */
	public static void readSection(String str, RequestParams params) {

		HttpUtils httpUtils =MyApplication.getInstance().httpUtils;
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i(str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {// 栏目关注成功

					}

					if ("3".equals(code)) {// 栏目关注失败

					}
					if ("4".equals(code)) {// 栏目已被关注

					}

				} catch (Exception e) {

					// SimpleHUD.showInfoMessage(getActivity(),
					// getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

}
