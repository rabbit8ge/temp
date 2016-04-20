package com.example.my;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zhy.com.highlight.util.L;
import org.json.JSONObject;
import info.wangchen.simplehud.SimpleHUD;
import com.example.config.Constants;
import com.example.config.MySharedPreference;
import com.example.database.DBHelper;
import com.example.dialog.SpotsDialog;
import com.example.dto.User;
import com.example.dto.UserInfo;
import com.example.honeytag1.R;
import com.example.http.Loadings;
import com.example.setup.IdentityActivity;
import com.example.setup.UserAgreementActivity;
import com.example.honeytag1.BaseActivity;
import com.example.honeytag1.MainActivity;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.UmengRegistrar;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 注册页面
 * 
 * @author Administrator
 * 
 */
public class RegistActivity extends BaseActivity implements OnClickListener {
	private LinearLayout regist_fanhui; // 返回按钮
	private RelativeLayout mRlPasswordEye, mRlPasswordOkEye;
	private ImageButton regist_btn;// 返回按钮
	private EditText regist_phone, regist_password, regist_yanzhengma, regist_quepassword; // 所有输入框
	private TextView regist_huoqu; // 获取验证码
	private TextView mTVuseragreement;// 用户协议
	private Button regist_regist; // 注册按钮
	private CheckBox mCheckchoose;
	private TimeCount time; // 验证码时间
	SpotsDialog spotsDialog; // dialog
	private String phone, password, yanzhengma, quepassword; // 手机号,密码
	private String device_token;
	private ImageButton mBtnPasswordEye, mBtnPasswordOkEye;
	private boolean rPassword = false, okPassword = false;
	private DBHelper mDBHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		device_token = UmengRegistrar.getRegistrationId(this);
		MyApplication.getInstance().addActivity(this);
		mDBHelper = new DBHelper(this);
		time = new TimeCount(60000, 1000);
	}

	/**
	 * 初始化
	 */
	@Override
	public void loadXml() {
		setContentView(R.layout.activity_regist);
		// TODO Auto-generated method stub
		mRlPasswordEye = (RelativeLayout) findViewById(R.id.rela_passwordEye);
		mRlPasswordOkEye = (RelativeLayout) findViewById(R.id.rela_passwordOkEye);
		mBtnPasswordEye = (ImageButton) this.findViewById(R.id.btn_passwordEye);
		mBtnPasswordOkEye = (ImageButton) findViewById(R.id.btn_passwordOkEye);
		regist_fanhui = (LinearLayout) this.findViewById(R.id.regist_fanhui);
		regist_btn = (ImageButton) this.findViewById(R.id.regist_btn);
		regist_phone = (EditText) this.findViewById(R.id.regist_phone);
		regist_password = (EditText) this.findViewById(R.id.regist_password);
		regist_yanzhengma = (EditText) this.findViewById(R.id.regist_yanzhengma);
		regist_quepassword = (EditText) this.findViewById(R.id.regist_quepassword);
		regist_huoqu = (TextView) this.findViewById(R.id.regist_huoqu);
		regist_regist = (Button) this.findViewById(R.id.regist_regist);
		mTVuseragreement = (TextView) this.findViewById(R.id.tv_useragreement);
		mCheckchoose = (CheckBox) this.findViewById(R.id.check_choose);

		GradientDrawable myGrad = (GradientDrawable) regist_regist.getBackground();// 获取shape中button的背景颜色
		myGrad.setStroke((int) 0.5, getResources().getColor(R.color.huise9));
		myGrad.setColor(getResources().getColor(R.color.huise9));

		GradientDrawable myGrad1 = (GradientDrawable) regist_huoqu.getBackground();// 获取shape中button的背景颜色
		myGrad1.setStroke((int) 0.5, getResources().getColor(R.color.huise9));
		myGrad1.setColor(getResources().getColor(R.color.huise9));

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		regist_regist.setClickable(false);
		regist_huoqu.setClickable(false);
		regist_regist.setEnabled(false);
		regist_huoqu.setEnabled(false);

		/**
		 * 绑定监听事件
		 */
		mRlPasswordEye.setOnClickListener(this);
		mRlPasswordOkEye.setOnClickListener(this);
		regist_fanhui.setOnClickListener(this);
		regist_btn.setOnClickListener(this);
		regist_huoqu.setOnClickListener(this);
		regist_regist.setOnClickListener(this);
//		mBtnPasswordEye.setOnClickListener(this);
		
		mTVuseragreement.setOnClickListener(this);

		regist_phone.addTextChangedListener(new MyTextWatcher(0));
		regist_yanzhengma.addTextChangedListener(new MyTextWatcher(1));
		regist_password.addTextChangedListener(new MyTextWatcher(2));
		regist_quepassword.addTextChangedListener(new MyTextWatcher(3));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rela_passwordEye) {
			if (rPassword == true) {
				regist_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				mBtnPasswordEye.setBackgroundResource(R.drawable.password_01);
				rPassword = false;

			} else {
				regist_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				mBtnPasswordEye.setBackgroundResource(R.drawable.password_02);
				rPassword = true;
			}
		}

		if (v.getId() == R.id.rela_passwordOkEye) {
			if (okPassword == true) {
				regist_quepassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				mBtnPasswordOkEye.setBackgroundResource(R.drawable.password_01);
				okPassword = false;

			} else {
				regist_quepassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				mBtnPasswordOkEye.setBackgroundResource(R.drawable.password_02);
				okPassword = true;
			}
		}
		if (v.getId() == R.id.regist_fanhui) {
			startActivity(new Intent(RegistActivity.this, LoginActivity.class));
			finish();
		}
		if (v.getId() == R.id.regist_btn) {
			startActivity(new Intent(RegistActivity.this, LoginActivity.class));
			finish();
		}
		if (v.getId() == R.id.regist_huoqu) {
			phone = regist_phone.getText().toString();
			if (regist_phone.getText().length() < 11 || !isMobileNO(regist_phone.getText().toString())) {
				SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.input_phone));
			} else {

				getVerificationCode(PublicStaticURL.YANZHENGMA + "&tel=" + phone);
			}

		}
		if (v.getId() == R.id.regist_regist) {
			phone = regist_phone.getText().toString();
			password = regist_password.getText().toString();
			yanzhengma = regist_yanzhengma.getText().toString();
			quepassword = regist_quepassword.getText().toString();
			// 判断输入密码是否是全数字
			Pattern p = Pattern.compile("[0-9]*");
			Matcher m = p.matcher(password);

			// 判断输入密码是否是全字母
			Pattern pz = Pattern.compile("[a-zA-Z]*");
			/**
			 * ModifyBy YangXiaohan
			 */
			Matcher mz = pz.matcher(password);
			if (regist_phone.getText().length() < 11 || !isMobileNO(regist_phone.getText().toString())) {
				SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.input_phone));

			} else if (regist_yanzhengma.getText().toString().length() == 0) {
				SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.input_code1));
			} else if (regist_password.getText().toString().length() == 0) {
				SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.input_password));
			} else if (!password.equals(quepassword)) {
				SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.psw_notsame));
			} else if (regist_password.getText().toString().length() < 6) {
				SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.password_atleast6));

			} else if (m.matches()) {
				SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.psw_canot_number));
			} else if (mz.matches()) {
				SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.psw_canot_letter));
			} else if (mCheckchoose.isChecked() == false) {
				SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.select_xieyi));

			} else {
				// verifyVerificationCode(PublicStaticURL.REGIST + "&tel=" +
				// phone +
				// "&captcha=" + yanzhengma + "&password=" + password +
				// "&spread_tel=" + spend_tel);
				verifyVerificationCode(PublicStaticURL.captcha + "&tel=" + phone + "&captcha=" + yanzhengma);
			}

		}

		if (v.getId() == R.id.tv_useragreement) {// 用户
			startActivity(new Intent(RegistActivity.this, UserAgreementActivity.class));
		}
	}

	/* 获取验证码 */
	private void getVerificationCode(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(RegistActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.server_connect_failed));
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
						SimpleHUD.showSuccessMessage(RegistActivity.this, getString(R.string.code_alrealy_send));

					}
					if ("3".equals(code)) {

						SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.code_send_failed));
					}
					if ("4".equals(code)) {

						SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.phone_occupy));

					}
				} catch (Exception e) {
					SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	/* 验证验证码 */
	private void verifyVerificationCode(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(RegistActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showErrorMessage(RegistActivity.this, getString(R.string.server_connect_failed));
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
						PublicStaticURL.tel = phone;
						PublicStaticURL.password = password;
						// Intent intent = new Intent();
						// startActivity(new Intent(RegistActivity.this,
						// HanWangRegistActivity.class));
						RequestParams params = new RequestParams();
						params.addBodyParameter("tel", PublicStaticURL.tel);

						params.addBodyParameter("password", PublicStaticURL.password);
						params.addBodyParameter("card_json", "");
						params.addBodyParameter("spread_tel", "");
						// params.addBodyParameter("pic", new File(picPath));

						register(PublicStaticURL.REGIST, params);
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.regist_failed));
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.code_error));
					}
				} catch (Exception e) {
					SimpleHUD.showInfoMessage(RegistActivity.this, getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	/* 注册 */
	private void register(String str, RequestParams params) {
		L.e("url-----" + str + "params---" + params.toString());
		HttpUtils httpUtils = new HttpUtils(5000);
		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				spotsDialog = new SpotsDialog(RegistActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				spotsDialog.dismiss();
				L.e("联网失败-------------" + arg1);
				SimpleHUD.showErrorMessage(RegistActivity.this, "网络连接失败，请检查网络");
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
						PublicStaticURL.Youmi_password = json1.getString("password");
					
						//SimpleHUD.showInfoMessage(RegistActivity.this, "注册成功");

						login(PublicStaticURL.LOGIN + "&tel=" + regist_phone.getText().toString() + "&password="
								+ regist_password.getText().toString() + "&device_type=" + Constants.deviceType
								+ "&device_token=" + "Ait2Lq1w_uyUCYr5h6RvJ9VVQK5G4A2285SLVTx0tFV1");
						MobclickAgent.onProfileSignIn(regist_phone.getText().toString());// 友盟账号统计
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(RegistActivity.this, "提交失败");
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(RegistActivity.this, "手机号已注册");
					}

					if ("6".equals(code)) {

						
						SimpleHUD.showInfoMessage(RegistActivity.this, "注册成功");
						getData(json);
						login(PublicStaticURL.LOGIN + "&tel=" + regist_phone.getText().toString() + "&password="
								+ regist_password.getText().toString() + "&device_type=" + Constants.deviceType
								+ "&device_token=" + "Ait2Lq1w_uyUCYr5h6RvJ9VVQK5G4A2285SLVTx0tFV1");

						// startActivity(new Intent(RegistActivity.this,
						// PhoneVerificationActivity.class));

					}
					if ("7".equals(code)) {
						getData(json);
						SimpleHUD.showInfoMessage(RegistActivity.this, "注册成功");
						login(PublicStaticURL.LOGIN + "&tel=" + regist_phone.getText().toString() + "&password="
								+ regist_password.getText().toString() + "&device_type=" + Constants.deviceType
								+ "&device_token=" + "Ait2Lq1w_uyUCYr5h6RvJ9VVQK5G4A2285SLVTx0tFV1");

					}
				} catch (Exception e) {
					Log.e("message", "走catch了");
					SimpleHUD.showErrorMessage(RegistActivity.this, "网络连接失败，请检查网络");
					e.printStackTrace();
				}
			}

		});
	}

	public void getData(JSONObject json) throws Exception {

		String result = json.getString("result");
		JSONObject json1 = new JSONObject(result);
		if (json1.has("tel")) {
			PublicStaticURL.Regist_tel = json1.getString("tel");
		} else {
			PublicStaticURL.Regist_tel = "";
		}

		PublicStaticURL.Regist_id = json1.getString("uid");

	}

	/* 登录 */
	private void login(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(RegistActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showInfoMessage(RegistActivity.this, "网络连接失败，请检查网络");
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

						//Toast.makeText(RegistActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);

						UserInfo mUserInfo = new UserInfo();
						mUserInfo.setUsername(regist_phone.getText().toString());
						mUserInfo.setPassword(regist_password.getText().toString());
						MySharedPreference.saveUserInfo(RegistActivity.this, mUserInfo);
						User mUser = User.getUser(json1.toString());
						MySharedPreference.saveUser(RegistActivity.this, mUser);
						User user = MySharedPreference.readUser(RegistActivity.this);

						PublicStaticURL.userid = user.getUid(); // 将用户ID储存，
						PublicStaticURL.ablity = user.getAbility(); // 用户能力值储存
						PublicStaticURL.credit = user.getCredit(); // 用户信用值储存
						PublicStaticURL.Login_phone = user.getTel(); // 用户名
						Intent intent = new Intent();
						mDBHelper.insertOrUpdate(regist_phone.getText().toString(),
								regist_password.getText().toString(), 0);
						SharedPreferences.Editor share = getSharedPreferences("dl", MODE_PRIVATE).edit();
						share.putString("yh", regist_phone.getText().toString());
						share.putString("ps", regist_password.getText().toString());
						share.commit();
						PublicStaticURL.IsLogin = true;
						MyApplication.getInstance().isAddCard = true;
						Intent intent1 = new Intent(RegistActivity.this, MainActivity.class);
						Constants.ACTIVITYFLAG = 0;// 指定首页当前的显示的位置
						startActivity(intent1);
						Loadings.getCount(PublicStaticURL.ZONGmessage + "&uid=" + user.getUid());
						sendBroadcast(new Intent("com.example.set.referesh"));
						MyApplication.getInstance().popActivity(LoginActivity.class);
						MyApplication.getInstance().popActivity(RegistActivity.this);

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(RegistActivity.this, "手机号或密码错误");
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(RegistActivity.this, "手机号未注册");
					}
					if ("5".equals(code)) {
						SimpleHUD.showInfoMessage(RegistActivity.this, "此用户已在黑名单中");
					}
					if ("6".equals(code)) {
						SimpleHUD.showInfoMessage(RegistActivity.this, "登录失败");
					}
					if ("10".equals(code)) {
						PublicStaticURL.IsLogin = true;
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);
						UserInfo mUserInfo = new UserInfo();
						mUserInfo.setUsername(regist_phone.getText().toString());
						mUserInfo.setPassword(regist_password.getText().toString());
						MySharedPreference.saveUserInfo(RegistActivity.this, mUserInfo);
						User mUser = User.getUser(json1.toString());
						MySharedPreference.saveUser(RegistActivity.this, mUser);
						User user = MySharedPreference.readUser(RegistActivity.this);

						PublicStaticURL.userid = user.getUid(); // 将用户ID储存，
						PublicStaticURL.ablity = user.getAbility(); // 用户能力值储存
						PublicStaticURL.credit = user.getCredit(); // 用户信用值储存
						PublicStaticURL.Login_phone = user.getTel(); // 用户名

						mDBHelper.insertOrUpdate(regist_phone.getText().toString(),
								regist_password.getText().toString(), 0);
						Intent intent = new Intent();
						SharedPreferences.Editor share = getSharedPreferences("dl", MODE_PRIVATE).edit();
						share.putString("yh", regist_phone.getText().toString());
						share.putString("ps", regist_password.getText().toString());
						share.commit();
						Loadings.getCount(PublicStaticURL.ZONGmessage + "&uid=" + user.getUid());
						sendBroadcast(new Intent("com.example.set.referesh"));

						MyApplication.getInstance().isAddCard = true;
						Intent intent1 = new Intent(RegistActivity.this, MainActivity.class);
						Constants.ACTIVITYFLAG = 0;// 指定首页当前的显示的位置

						startActivity(intent1);
						MyApplication.getInstance().popActivity(LoginActivity.class);
						MyApplication.getInstance().popActivity(RegistActivity.this);

					}

				} catch (Exception e) {
					SimpleHUD.showInfoMessage(RegistActivity.this, "登录失败!");
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
			regist_huoqu.setText(R.string.get_code);
			regist_huoqu.setClickable(true);
			regist_huoqu.setEnabled(true);
			GradientDrawable myGrad = (GradientDrawable) regist_huoqu.getBackground();// 获取shape中button的背景颜色
			myGrad.setColor(getResources().getColor(R.color.green));
		}

		@Override
		public void onTick(long millisUntilFinished) {
			regist_huoqu.setClickable(false);
			regist_huoqu.setText(millisUntilFinished / 1000 + "s 重新获取");
			regist_huoqu.setEnabled(false);
			GradientDrawable myGrad = (GradientDrawable) regist_huoqu.getBackground();// 获取shape中button的背景颜色
			myGrad.setColor(getResources().getColor(R.color.huise9));

		}

	}

	// 判断手机格式是否正确
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	class MyTextWatcher implements TextWatcher {
		int position;// 标示当前是哪一个EditText;

		public MyTextWatcher(int position) {
			this.position = position;

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			switch (position) {
			case 0:
				if (TextUtils.isEmpty(s.toString())) {
					GradientDrawable myGrad = (GradientDrawable) regist_huoqu.getBackground();// 获取shape中button的背景颜色
					myGrad.setStroke((int) 0.5, getResources().getColor(R.color.huise9));
					myGrad.setColor(getResources().getColor(R.color.huise9));

					regist_huoqu.setClickable(false);
					regist_huoqu.setEnabled(false);
				} else {
					GradientDrawable myGrad = (GradientDrawable) regist_huoqu.getBackground();// 获取shape中button的背景颜色
					myGrad.setStroke((int) 0.5, getResources().getColor(R.color.green));
					myGrad.setColor(getResources().getColor(R.color.green));
					regist_huoqu.setClickable(true);
					regist_huoqu.setEnabled(true);
				}

				if (TextUtils.isEmpty(s.toString()) || TextUtils.isEmpty(regist_yanzhengma.getText().toString())
						|| TextUtils.isEmpty(regist_password.getText().toString())
						|| TextUtils.isEmpty(regist_quepassword.getText().toString())) {
					setColor(0, false);

				} else {
					setColor(1, true);

				}

				break;
			case 1:
				if (TextUtils.isEmpty(s.toString()) || TextUtils.isEmpty(regist_phone.getText().toString())
						|| TextUtils.isEmpty(regist_password.getText().toString())
						|| TextUtils.isEmpty(regist_quepassword.getText().toString())) {
					setColor(0, false);

				} else {
					setColor(1, true);
				}

				break;
			case 2:
				if (TextUtils.isEmpty(s.toString()) || TextUtils.isEmpty(regist_phone.getText().toString())
						|| TextUtils.isEmpty(regist_yanzhengma.getText().toString())
						|| TextUtils.isEmpty(regist_quepassword.getText().toString())) {
					setColor(0, false);

				} else {
					setColor(1, true);
				}

				break;
			case 3:
				if (TextUtils.isEmpty(s.toString()) || TextUtils.isEmpty(regist_phone.getText().toString())
						|| TextUtils.isEmpty(regist_yanzhengma.getText().toString())
						|| TextUtils.isEmpty(regist_password.getText().toString())) {
					setColor(0, false);

				} else {
					setColor(1, true);
				}

				break;

			default:
				break;
			}

		}

	}

	/*
	 * 设置btn在不同状态下的背景色
	 * 
	 * @params flag:0代表灰色，1代表蓝色
	 */
	public void setColor(int flag, Boolean bool) {
		if (0 == flag) {
			GradientDrawable myGrad = (GradientDrawable) regist_regist.getBackground();// 获取shape中button的背景颜色
			myGrad.setStroke((int) 0.5, getResources().getColor(R.color.huise9));
			myGrad.setColor(getResources().getColor(R.color.huise9));
			regist_regist.setClickable(bool);
			regist_regist.setEnabled(bool);

		}

		if (1 == flag) {
			GradientDrawable myGrad = (GradientDrawable) regist_regist.getBackground();// 获取shape中button的背景颜色
			myGrad.setStroke((int) 0.5, getResources().getColor(R.color.blue2));
			myGrad.setColor(getResources().getColor(R.color.blue2));
			regist_regist.setClickable(bool);
			regist_regist.setEnabled(bool);
		}

	}

	@Override
	protected void onDestroy() {
		mDBHelper.cleanup();
		super.onDestroy();
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
