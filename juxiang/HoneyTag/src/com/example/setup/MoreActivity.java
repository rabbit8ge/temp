package com.example.setup;

import java.io.File;

import org.json.JSONObject;

import com.example.baseactivity.BaseActivity;
import com.example.config.MySharedPreference;
import com.example.dialog.CustomDialog;
import com.example.dialog.SpotsDialog;
import com.example.dto.User;
import com.example.fragment.FragmentShouYe;
import com.example.honeytag1.R;
import com.example.my.LoginActivity;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.utils.Utils;
import com.example.utils.VersionManagement;
import com.example.view.NavigationView.ClickCallbackLeft;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.readystatesoftware.viewbadger.BadgeView;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import info.wangchen.simplehud.SimpleHUD;

/**
 * @Description描述:设置
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class MoreActivity extends BaseActivity {
	public static final String MNT = Environment.getExternalStorageDirectory().toString();
	public static final String ROOT = MNT + File.separator + "行家说" + File.separator + "91major.apk";

	private TextView mTvCurrentVersion, mTvExit, mTvCheckUpdate;
	private BadgeView mBadgeView;
	private SpotsDialog spotsDialog; // dialog
	private int hight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);
		spotsDialog = new SpotsDialog(this);
		setContentView(R.layout.activity_more);
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void init() {
		super.init();
	}

	protected void initView() {
		super.initView();

		initTopView();

		mRelNavigationView.setTitle(getString(R.string.other_setting));

		mTvCheckUpdate = (TextView) findViewById(R.id.tv_checkUpdate);
		mTvCurrentVersion = (TextView) findViewById(R.id.tv_currentVersion);
		mTvCurrentVersion.setText(getString(R.string.version) + VersionManagement.getInstance().getVersionName(this));
		mTvExit = (TextView) findViewById(R.id.tv_exit);

		GradientDrawable myGrad = (GradientDrawable) mTvExit.getBackground();// 获取shape中button的背景颜色
		myGrad.setStroke((int) 0.5, getResources().getColor(R.color.blue2));
		myGrad.setColor(getResources().getColor(R.color.blue2));

		mBadgeView = new BadgeView(this, mTvCheckUpdate);
		mBadgeView.setText(".");
		mBadgeView.setTextSize(10);
		mBadgeView.setBackgroundResource(R.drawable.radio_shape_enter3);
		mBadgeView.setTextColor(getResources().getColor(R.color.transparent));
		mBadgeView.setBadgePosition(mBadgeView.POSITION_TOP_RIGHT);
		mBadgeView.setAlpha(1f);
		mBadgeView.setBadgeMargin(80, 25);

		mRelNavigationView.setClickCallbackLeft(new ClickCallbackLeft() {

			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(MoreActivity.this);

			}
		});

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		hight = mTvCheckUpdate.getHeight();

		int h = mBadgeView.getHeight();
		mBadgeView.setBadgeMargin(80, hight / 2 - h / 2);

		if (0 == PublicStaticURL.isNewVersion) {
			mBadgeView.show();
		}
		if (1 == PublicStaticURL.isNewVersion) {
			mBadgeView.hide();
		}

		super.onWindowFocusChanged(hasFocus);
	}

	int versionCode;
	private CustomDialog.Builder ibuilder;

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_feedback:// 意见反馈
			startActivity(new Intent(MoreActivity.this, FeedbackActivity.class));
			break;
		case R.id.linear_currentVersion:
			break;
		case R.id.tv_checkUpdate:

			if (Utils.isNetworkAvailable(MoreActivity.this)) {
				// getVersion(PublicStaticURL.VERSION);
				VersionManagement.getInstance().checkUpdate(MoreActivity.this, PublicStaticURL.VERSION);
			} else {
				SimpleHUD.showInfoMessage(MoreActivity.this, getString(R.string.wuwangluo));

			}
			break;
		case R.id.tv_changePassword:// 修改密码
			Intent intent = new Intent(MoreActivity.this, ForgetActivity.class);
			intent.putExtra("flag", getString(R.string.modify_psw));
			startActivity(intent);
			break;

		case R.id.tv_exit:
			User mUser = MySharedPreference.readUser(MoreActivity.this);
			if (mUser != null) {

				ibuilder = new CustomDialog.Builder(MoreActivity.this);
				ibuilder.setTitle(getString(R.string.notify));
				ibuilder.setMessage(getString(R.string.are_you_loginout));
				ibuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

						loginOut();
						arg0.dismiss();
					}
				});
				ibuilder.setNegativeButton(R.string.cancel, null);
				ibuilder.create().show();

			} else {
				SimpleHUD.showInfoMessage(MoreActivity.this, getString(R.string.not_login));
			}

			break;
		case R.id.tv_updateLog:// 更新日志
			startActivity(new Intent(MoreActivity.this, UpdateLogActivity.class));

			break;

		default:
			break;
		}

	}

	/*
	 * 登出
	 */
	private void loginOut() {
		RequestParams params = new RequestParams();
		User mUser = MySharedPreference.readUser(MoreActivity.this);
		params.addBodyParameter("tel", mUser.getTel());
		params.addBodyParameter("uid", mUser.getUid());
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, PublicStaticURL.PUSH, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				if (spotsDialog != null) {
					spotsDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
					spotsDialog.show();
				}

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {

				if (spotsDialog != null && spotsDialog.isShowing()) {
					spotsDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
					spotsDialog.dismiss();
				}
				cleanData();
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
				} finally {

					if (spotsDialog != null && spotsDialog.isShowing()) {
						spotsDialog.dismiss();
					}
					cleanData();

				}

			}

		});
	}

	/*
	 * 清空用户信息
	 */
	public void cleanData() {
		SharedPreferences settings = getSharedPreferences("dl", MODE_PRIVATE);

		SharedPreferences.Editor editor = settings.edit();

		editor.remove("yh");

		editor.remove("ps");

		editor.commit();
		PublicStaticURL.IsLogin = false;
		FragmentShouYe.main_tab_unread_tv.setBackgroundResource(R.drawable.circle_gray);// 更改主页通知状态
		FragmentShouYe.main_tab_unread_tv.setText("0");
		FragmentShouYe.main_tab_unread_tv.setTextColor(Color.GRAY);
		Toast.makeText(MoreActivity.this, getString(R.string.you_loginout), Toast.LENGTH_SHORT).show();
		MySharedPreference.saveUser(MoreActivity.this, null);
		startActivity(new Intent(MoreActivity.this, LoginActivity.class));

		MyApplication.getInstance().popActivity(MoreActivity.this);
		MyApplication.getInstance().popActivity(SettingActivity.class);
		sendBroadcast(new Intent("com.honeytag.changefragment"));// 发送广播，将fragment4切换到fragment1.

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (0 == PublicStaticURL.isNewVersion) {
			mBadgeView.show();
		}
		if (1 == PublicStaticURL.isNewVersion) {
			mBadgeView.hide();
		}
		super.onResume();
		umengResume(this, getClass().toString());
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		umengPause(this, getClass().toString());

	}

	@Override
	protected void onDestroy() {
		if (spotsDialog != null && spotsDialog.isShowing()) {
			spotsDialog.cancel();
			spotsDialog = null;
		}
		super.onDestroy();
	}

}
