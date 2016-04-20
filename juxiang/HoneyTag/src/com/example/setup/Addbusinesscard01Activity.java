package com.example.setup;

import java.io.File;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.baseactivity.BaseActivity;
import com.example.dialog.SpotsDialog;
import com.example.honeytag1.R;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.utils.Utils;
import com.example.view.NavigationView.ClickCallback;
import com.example.view.NavigationView.ClickCallbackLeft;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import info.wangchen.simplehud.SimpleHUD;

/**
 * @Description描述:
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class Addbusinesscard01Activity extends BaseActivity {

	private  SpotsDialog mSpotsDialog;
	private EditText mETPhone, mETverification;
	private Button mBtnget, mBtncommit;
	private TimeCount time; // 验证码时间
	private String responce;// 名片信息
	private String mobile;// 手机号
	private String picPath;// 图片路径
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		responce = getIntent().getStringExtra("responce");
		picPath = getIntent().getStringExtra("picPath");
		mobile = getMobile(responce);
		time = new TimeCount(60000, 1000);
		setContentView(R.layout.activity_cardverification);
		mSpotsDialog=new SpotsDialog(this);
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void initView() {
		super.initView();
		initTopView();
		mRelNavigationView.setTitle(getString(R.string.card_yanzheng));

		mETPhone = (EditText) findViewById(R.id.regist_phone);
		mETverification = (EditText) findViewById(R.id.regist_yanzhengma);

		mBtnget = (Button) findViewById(R.id.regist_huoqu);
		mBtncommit = (Button) findViewById(R.id.regist_regist);

		mETPhone.setText(mobile);

		GradientDrawable myGrad = (GradientDrawable) mBtncommit.getBackground();// 获取shape中button的背景颜色
		myGrad.setStroke((int) 0.5, getResources().getColor(R.color.blue2));
		myGrad.setColor(getResources().getColor(R.color.blue2));

		GradientDrawable myGrad1 = (GradientDrawable) mBtnget.getBackground();// 获取shape中button的背景颜色
		myGrad1.setStroke((int) 0.5, getResources().getColor(R.color.huoqu_hei));
		myGrad1.setColor(getResources().getColor(R.color.huoqu_hei));

		mRelNavigationView.setClickCallbackLeft(new ClickCallbackLeft() {
			
			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(Addbusinesscard01Activity.this);
				
			}
		});

	}

	/*
	 * 获得手机号码
	 */
	public String getMobile(String str) {
		String mobile = "";
		if (TextUtils.isEmpty(str)) {
			return mobile;
		}

		try {
			JSONObject object = new JSONObject(str);
			mobile = object.getString("mobile");
			mobile = mobile.substring(2, mobile.length() - 2);
		} catch (JSONException e) {

			e.printStackTrace();
			return mobile;
		} finally {
			return mobile;
		}

	}

	// 获取验证码

	private void getVerificationCode(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				mSpotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				mSpotsDialog.dismiss();
				SimpleHUD.showInfoMessage(Addbusinesscard01Activity.this, getString(R.string.server_connect_failed));
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
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);
						String captcha = json1.getString("captcha");
						time.start();
						SimpleHUD.showSuccessMessage(Addbusinesscard01Activity.this,
								getString(R.string.code_alrealy_send));

					}
					if ("3".equals(code)) {

						SimpleHUD.showInfoMessage(Addbusinesscard01Activity.this, getString(R.string.code_send_failed));
					}
					if ("4".equals(code)) {

						SimpleHUD.showInfoMessage(Addbusinesscard01Activity.this, getString(R.string.phone_occupy));

					}
				} catch (Exception e) {
					SimpleHUD.showInfoMessage(Addbusinesscard01Activity.this, getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				} finally {
					mSpotsDialog.dismiss();
				}
			}

		});

	}

	/**
	 * 倒计时
	 */

	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			mBtnget.setText(R.string.get_code);
			mBtnget.setClickable(true);
			mBtnget.setEnabled(true);
			GradientDrawable myGrad = (GradientDrawable) mBtnget.getBackground();// 获取shape中button的背景颜色
			myGrad.setColor(getResources().getColor(R.color.huoqu_hei));
		}

		@Override
		public void onTick(long millisUntilFinished) {
			mBtnget.setClickable(false);
			mBtnget.setText(millisUntilFinished / 1000 + "s 重新获取");
			mBtnget.setEnabled(false);
			GradientDrawable myGrad = (GradientDrawable) mBtnget.getBackground();// 获取shape中button的背景颜色
			myGrad.setColor(getResources().getColor(R.color.huoqu_hui));

		}

	}

	/*
	 * 添加名片
	 */
	String card_json;
	String captcha;

	public void submit(String str, RequestParams params) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);

		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				mSpotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				mSpotsDialog.dismiss();
				SimpleHUD.showInfoMessage(Addbusinesscard01Activity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				mSpotsDialog.dismiss();
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i(str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						time.start();
						SimpleHUD.showSuccessMessage(Addbusinesscard01Activity.this,
								getString(R.string.add_card_succeed));

					}
					if ("3".equals(code)) {

						SimpleHUD.showInfoMessage(Addbusinesscard01Activity.this, getString(R.string.add_card_failed));
					}

					if ("4".equals(code)) {

						SimpleHUD.showInfoMessage(Addbusinesscard01Activity.this, getString(R.string.code_error));

					}

				} catch (Exception e) {
					SimpleHUD.showInfoMessage(Addbusinesscard01Activity.this, getString(R.string.add_card_failed));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	Intent intent;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regist_huoqu:// 获取验证码
			if (!Utils.isMObilehone(mETPhone.getText().toString())) {
				SimpleHUD.showInfoMessage(Addbusinesscard01Activity.this, getString(R.string.input_phone));
			} else {

				getVerificationCode(PublicStaticURL.YANZHENGMA + "&tel=" + mETPhone.getText().toString() + "&uid="
						+ PublicStaticURL.userid);

			}

			break;
		case R.id.regist_regist:// 提交

			if (!Utils.isMObilehone(mETPhone.getText().toString())) {
				SimpleHUD.showInfoMessage(Addbusinesscard01Activity.this, getString(R.string.input_phone));
			} else if (TextUtils.isEmpty(mETverification.getText().toString())) {
				SimpleHUD.showInfoMessage(Addbusinesscard01Activity.this, getString(R.string.input_code));
			} else {

				try {

					RequestParams params = new RequestParams();
					params.addBodyParameter("uid", PublicStaticURL.userid);
					params.addBodyParameter("tel", mETPhone.getText().toString());
					params.addBodyParameter("captcha", mETverification.getText().toString());

					params.addBodyParameter("card_json",
							Base64.encodeToString(responce.getBytes("UTF-8"), Base64.DEFAULT));
					params.addBodyParameter("pic", new File(picPath));

					submit(PublicStaticURL.Addbusinesscard, params);

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			break;

		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
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

		if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
			mSpotsDialog.cancel();
			mSpotsDialog = null;
		}
		super.onDestroy();
	}

}
