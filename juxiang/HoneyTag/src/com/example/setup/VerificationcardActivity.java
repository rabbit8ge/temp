package com.example.setup;

import org.json.JSONObject;

import info.wangchen.simplehud.SimpleHUD;

import com.example.dialog.SpotsDialog;
import com.example.honeytag1.BaseActivity;
import com.example.honeytag1.PublishActivity;
import com.example.honeytag1.R;
import com.example.honeytag1.R.layout;
import com.example.honeytag1.R.menu;
import com.example.my.LoginActivity;
import com.example.my.PhoneVerificationActivity;
import com.example.setup.ForgetActivity.TimeCount;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VerificationcardActivity extends BaseActivity implements OnClickListener {
	/**
	 * 手机验证
	 */
	private LinearLayout phone_root, phone_fanhui;
	private ImageButton phone_btn;
	private TextView phone_phone, phone_huoqu;
	private EditText phone_yanzhengma;
	private Button phone_regist;
	private TimeCount time; // 验证码时间
	private SpotsDialog spotsDialog;
	private String phone, captcha;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SimpleHUD.showInfoMessage(VerificationcardActivity.this, "名片手机号与注册号不一致,需要验证");
		MyApplication.getInstance().addActivity(this);
	}

	@Override
	public void loadXml() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_phone_verification);
		phone_fanhui = (LinearLayout) findViewById(R.id.phone_fanhui);
		phone_btn = (ImageButton) findViewById(R.id.phone_btn);
		phone_phone = (TextView) findViewById(R.id.phone_phone);
		phone_phone.setText(PublicStaticURL.Regist_tel);
		phone_yanzhengma = (EditText) findViewById(R.id.phone_yanzhengma);
		phone_huoqu = (TextView) findViewById(R.id.phone_huoqu);
		phone_regist = (Button) findViewById(R.id.phone_regist);

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		phone_fanhui.setOnClickListener(this);
		phone_btn.setOnClickListener(this);
		phone_huoqu.setOnClickListener(this);
		phone_regist.setOnClickListener(this);
		time = new TimeCount(60000, 1000);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.phone_fanhui:
			finish();
			break;
		case R.id.phone_btn:
			finish();

			break;
		case R.id.phone_huoqu:

			phone = phone_phone.getText().toString();
			GETYANZHENGMA(PublicStaticURL.YANZHENGMA + "&tel=" + phone);
			break;
		case R.id.phone_regist:
			captcha = phone_yanzhengma.getText().toString();
			GETCODE(PublicStaticURL.Verification + "&id=" + PublicStaticURL.userid + "&tel="
					+ PublicStaticURL.Regist_tel + "&captcha=" + captcha);
			break;

		}
	}

	// 获取验证码

	private void GETYANZHENGMA(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(VerificationcardActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showInfoMessage(VerificationcardActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				spotsDialog.dismiss();
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
						SimpleHUD.showSuccessMessage(VerificationcardActivity.this, "验证码已发送，请注意查收");

					}
					if ("3".equals(code)) {

						SimpleHUD.showInfoMessage(VerificationcardActivity.this, "验证码发送失败");
					}
					if ("4".equals(code)) {

						SimpleHUD.showInfoMessage(VerificationcardActivity.this, "手机号被占用");

					}
				} catch (Exception e) {
					SimpleHUD.showInfoMessage(VerificationcardActivity.this, getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	// 注册

	private void GETCODE(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(VerificationcardActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showErrorMessage(VerificationcardActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				spotsDialog.dismiss();
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i(str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);
						PublicStaticURL.Youmi_phone = json1.getString("tel");

						Toast.makeText(VerificationcardActivity.this, "更换名片成功", Toast.LENGTH_SHORT).show();
						finish();

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(VerificationcardActivity.this, "发送验证码失败");
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(VerificationcardActivity.this, "验证码错误！");
					}

				} catch (Exception e) {
					SimpleHUD.showInfoMessage(VerificationcardActivity.this, getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
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
			phone_huoqu.setText(R.string.button_get);
			phone_huoqu.setClickable(true);
			phone_huoqu.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			phone_huoqu.setClickable(false);
			phone_huoqu.setText(millisUntilFinished / 1000 + "秒");
			phone_huoqu.setEnabled(false);

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

}
