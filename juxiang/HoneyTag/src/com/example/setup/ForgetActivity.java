package com.example.setup;

import info.wangchen.simplehud.SimpleHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import com.example.config.Constants;
import com.example.config.MySharedPreference;
import com.example.database.DBHelper;
import com.example.dialog.SpotsDialog;
import com.example.dto.User;
import com.example.dto.UserInfo;
import com.example.honeytag1.BaseActivity;
import com.example.honeytag1.MainActivity;
import com.example.honeytag1.R;
import com.example.http.Loadings;
import com.example.my.LoginActivity;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.utils.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.UmengRegistrar;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 忘记密码页面
 * 
 * @author Administrator
 * 
 */
public class ForgetActivity extends BaseActivity implements OnClickListener {

	private LinearLayout forget_fanhui; // 返回按钮
	private RelativeLayout mRlPasswordEye, mRlPasswordOkEye;
	private ImageView forget_btn;// 返回按钮
	private ImageButton mBtnPasswordEye, mBtnPasswordOkEye;
	private EditText forget_phone, forget_password, forget_yanzhengma, forget_quepassword; // 所有输入框
	private TextView forget_huoqu, psw_title; // 获取验证码
	private Button forget_forget; // 注册按钮

	private TimeCount time; // 验证码时间

	private DBHelper mDBHelper;
	SpotsDialog spotsDialog; // dialog
	String tel_phone, tel_password, tel_repassword, tel_yanzhengma;
	private int activityFalg = -1;
	private List<UserInfo> mList = new ArrayList<UserInfo>();
	private String device_token;
	private boolean rPassword = false, okPassword = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		device_token = UmengRegistrar.getRegistrationId(this);
		mDBHelper = new DBHelper(this);
		activityFalg = getIntent().getIntExtra("activityFlag", -1);
		MyApplication.getInstance().addActivity(this);
		time = new TimeCount(60000, 1000);
		if (getIntent().getStringExtra("flag").equals(getString(R.string.modify_psw))) {
			psw_title.setText(R.string.modify_psw);
		} else {
			forget_phone.setText(getIntent().getStringExtra("flag"));
		}
		GradientDrawable myGrad = (GradientDrawable) forget_forget.getBackground();// 每次进入该页面，都是灰色，
																					// 获取shape中button的背景颜色
		myGrad.setColor(getResources().getColor(R.color.tijiao_hei));

	}

	@Override
	public void loadXml() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_forget);
		mRlPasswordEye = (RelativeLayout) findViewById(R.id.rela_passwordEye);
		mRlPasswordOkEye = (RelativeLayout) findViewById(R.id.rela_passwordOkEye);
		mBtnPasswordEye = (ImageButton) this.findViewById(R.id.btn_passwordEye);
		mBtnPasswordOkEye = (ImageButton) findViewById(R.id.btn_passwordOkEye);
		forget_fanhui = (LinearLayout) this.findViewById(R.id.forget_fanhui);
		forget_btn = (ImageView) this.findViewById(R.id.forget_btn);
		forget_phone = (EditText) this.findViewById(R.id.forget_phone);
		forget_password = (EditText) this.findViewById(R.id.forget_password);
		forget_yanzhengma = (EditText) this.findViewById(R.id.forget_yanzhengma);
		forget_quepassword = (EditText) this.findViewById(R.id.forget_quepassword);
		forget_huoqu = (TextView) this.findViewById(R.id.forget_huoqu);
		psw_title = (TextView) this.findViewById(R.id.psw_title);
		forget_forget = (Button) this.findViewById(R.id.forget_forget);
		UserInfo mUserInfo = MySharedPreference.readUserInfo(ForgetActivity.this);
		if (mUserInfo != null) {

			forget_phone.setText(mUserInfo.getUsername());
		}

		findViewById(R.id.btn_passwordEye).setBackgroundResource(R.drawable.password_01);

		GradientDrawable myGrad = (GradientDrawable) forget_forget.getBackground();// 获取shape中button的背景颜色
		myGrad.setStroke((int) 0.5, getResources().getColor(R.color.blue2));
		myGrad.setColor(getResources().getColor(R.color.blue2));

		GradientDrawable myGrad1 = (GradientDrawable) forget_huoqu.getBackground();// 获取shape中button的背景颜色
		myGrad1.setStroke((int) 0.5, getResources().getColor(R.color.huoqu_hei));
		myGrad1.setColor(getResources().getColor(R.color.huoqu_hei));

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		forget_forget.setClickable(false);
		forget_huoqu.setClickable(false);

		// 绑定监听事件
		mRlPasswordEye.setOnClickListener(this);
		mRlPasswordOkEye.setOnClickListener(this);
		forget_fanhui.setOnClickListener(this);
		forget_btn.setOnClickListener(this);
		forget_huoqu.setOnClickListener(this);
		forget_forget.setOnClickListener(this);
		forget_phone.addTextChangedListener(new MyTextWatcher(0));
		forget_yanzhengma.addTextChangedListener(new MyTextWatcher(1));
		forget_password.addTextChangedListener(new MyTextWatcher(2));
		forget_quepassword.addTextChangedListener(new MyTextWatcher(3));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if (v.getId() == R.id.rela_passwordEye) {
			if (rPassword == true) {
				forget_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				mBtnPasswordEye.setBackgroundResource(R.drawable.password_01);
				rPassword = false;

			} else {
				forget_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				mBtnPasswordEye.setBackgroundResource(R.drawable.password_02);
				rPassword = true;
			}
			forget_password.postInvalidate();
			// 切换后将EditText光标置于末尾
			CharSequence charSequence = forget_password.getText();
			if (charSequence instanceof Spannable) {
				Spannable spanText = (Spannable) charSequence;
				Selection.setSelection(spanText, charSequence.length());
			}

		}

		if (v.getId() == R.id.rela_passwordOkEye) {
			if (okPassword == true) {
				forget_quepassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				mBtnPasswordOkEye.setBackgroundResource(R.drawable.password_01);
				okPassword = false;

			} else {
				forget_quepassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				mBtnPasswordOkEye.setBackgroundResource(R.drawable.password_02);
				okPassword = true;
			}
			forget_quepassword.postInvalidate();
			// 切换后将EditText光标置于末尾
			CharSequence charSequence = forget_password.getText();
			if (charSequence instanceof Spannable) {
				Spannable spanText = (Spannable) charSequence;
				Selection.setSelection(spanText, charSequence.length());
			}

		}
		if (v.getId() == R.id.forget_fanhui) {
			MyApplication.getInstance().popActivity(ForgetActivity.this);

		}
		if (v.getId() == R.id.forget_btn) {
			MyApplication.getInstance().popActivity(ForgetActivity.this);

		}
		if (v.getId() == R.id.forget_huoqu) {
			tel_phone = forget_phone.getText().toString();
			getVerificationCode(PublicStaticURL.YANZHENGMAXIUGAIMIMA + "&tel=" + tel_phone);
		}
		if (v.getId() == R.id.forget_forget) {

			tel_phone = forget_phone.getText().toString();
			tel_password = forget_password.getText().toString();
			tel_repassword = forget_quepassword.getText().toString();
			tel_yanzhengma = forget_yanzhengma.getText().toString();
			// 判断输入密码是否是全数字
			Pattern p = Pattern.compile("[0-9]*");
			Matcher m = p.matcher(tel_password);

			// 判断输入密码是否是全字母
			Pattern pz = Pattern.compile("[a-zA-Z]*");
			/**
			 * ModifyBy YangXiaohan
			 */
			Matcher mz = pz.matcher(tel_password);
			if (forget_phone.getText().toString().length() == 0 || forget_password.getText().toString().length() == 0
					|| forget_quepassword.getText().toString().length() == 0
					|| forget_yanzhengma.getText().toString().length() == 0) {
				SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.info_canot_null));
			} else if (!tel_password.equals(tel_repassword)) {
				SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.psw_notsame));
			} else if (forget_password.getText().toString().length() < 6) {
				SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.password_atleast6));
			} else if (m.matches()) {
				SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.psw_canot_number));
			} else if (mz.matches()) {
				SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.psw_canot_letter));
			}
			// else if (forget_phone.getText().toString().length() < 11) {
			else if (!Utils.isMObilehone(forget_phone.getText().toString())) {
				SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.input_code));
			} else {
//				changePassword(PublicStaticURL.XIUGAIPASSWORD + "&tel=" + tel_phone + "&captcha=" + tel_yanzhengma
//						+ "&password=" + tel_password + "&repassword=" + tel_repassword);
				
				Logger.i(PublicStaticURL.XIUGAIPASSWORD + "&tel=" + tel_phone + "&captcha=" + tel_yanzhengma
						+ "&password=" + tel_password );
			}

		}
	}

	/*
	 * 获取验证码
	 */
	private void getVerificationCode(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(ForgetActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.server_connect_failed));
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
						SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.code_alrealy_send));

					}
					if ("3".equals(code)) {

						SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.code_send_failed));
					}
					if ("4".equals(code)) {

						SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.regist_no));

					}
				} catch (Exception e) {
					SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	/*
	 * 修改密码
	 */
	private void changePassword(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(ForgetActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showErrorMessage(ForgetActivity.this, getString(R.string.server_connect_failed));
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

						Toast.makeText(ForgetActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
						if (activityFalg == 0) {

//							login(PublicStaticURL.LOGIN + "&tel=" + forget_phone.getText().toString() + "&password="
//									+ forget_password.getText().toString() + "&device_type=" + Constants.deviceType
//									+ "&device_token=" + "Ait2Lq1w_uyUCYr5h6RvJ9VVQK5G4A2285SLVTx0tFV1");
							
							MyApplication.getInstance().popActivity(ForgetActivity.this);
							MobclickAgent.onProfileSignIn(tel_phone);// 友盟账号统计
						} else {

							MyApplication.getInstance().popActivity(ForgetActivity.this);
						}

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.phone_psw_error));
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.regist_no));
					}

				} catch (Exception e) {
					SimpleHUD.showInfoMessage(ForgetActivity.this, getString(R.string.app_exception));
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
			forget_huoqu.setText(getString(R.string.get_code));
			forget_huoqu.setClickable(true);
			forget_huoqu.setEnabled(true);
			GradientDrawable myGrad = (GradientDrawable) forget_huoqu.getBackground();// 获取shape中button的背景颜色
			myGrad.setColor(getResources().getColor(R.color.green));
		}

		@Override
		public void onTick(long millisUntilFinished) {
			forget_huoqu.setClickable(false);
			forget_huoqu.setText(millisUntilFinished / 1000 + "s 重新获取");
			forget_huoqu.setEnabled(false);
			GradientDrawable myGrad = (GradientDrawable) forget_huoqu.getBackground();// 获取shape中button的背景颜色
			myGrad.setColor(getResources().getColor(R.color.huise9));
		}

	}

	/*
	 * 登录
	 */
	private void login(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(ForgetActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showInfoMessage(ForgetActivity.this, "网络连接失败，请检查网络");
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

						Toast.makeText(ForgetActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);
						UserInfo mUserInfo = new UserInfo();
						mUserInfo.setUsername(forget_phone.getText().toString());
						mUserInfo.setPassword(forget_password.getText().toString());
						MySharedPreference.saveUserInfo(ForgetActivity.this, mUserInfo);
						User mUser = User.getUser(json1.toString());
						MySharedPreference.saveUser(ForgetActivity.this, mUser);
						User user = MySharedPreference.readUser(ForgetActivity.this);

						PublicStaticURL.userid = user.getUid(); // 将用户ID储存，
						PublicStaticURL.ablity = user.getAbility(); // 用户能力值储存
						PublicStaticURL.credit = user.getCredit(); // 用户信用值储存
						PublicStaticURL.Login_phone = user.getTel(); // 用户名
						Intent intent = new Intent();
						SharedPreferences.Editor share = getSharedPreferences("dl", MODE_PRIVATE).edit();
						share.putString("yh", forget_phone.getText().toString());
						share.putString("ps", forget_password.getText().toString());
						share.commit();
						PublicStaticURL.IsLogin = true;
						mDBHelper.insertOrUpdate(forget_phone.getText().toString(),
								forget_password.getText().toString(), 0);
						Loadings.getCount(PublicStaticURL.ZONGmessage + "&uid=" + user.getUid());
						Intent intent1 = new Intent(ForgetActivity.this, MainActivity.class);
						Constants.ACTIVITYFLAG = 0;// 指定首页当前的显示的位置
						intent1.putExtra("ACTIVITYFLAG", 0);
						startActivity(intent1);
						sendBroadcast(new Intent("com.example.set.referesh"));
						MyApplication.getInstance().popActivity(LoginActivity.class);
						MyApplication.getInstance().popActivity(ForgetActivity.this);

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(ForgetActivity.this, "手机号或密码错误");
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(ForgetActivity.this, "手机号未注册");
					}

					if ("5".equals(code)) {
						SimpleHUD.showInfoMessage(ForgetActivity.this, "此用户已在黑名单中");
					}
					if ("6".equals(code)) {
						SimpleHUD.showInfoMessage(ForgetActivity.this, "登录失败");
					}
					if ("10".equals(code)) {
						PublicStaticURL.IsLogin = true;
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);
						UserInfo mUserInfo = new UserInfo();
						mUserInfo.setUsername(forget_phone.getText().toString());
						mUserInfo.setPassword(forget_password.getText().toString());
						MySharedPreference.saveUserInfo(ForgetActivity.this, mUserInfo);
						User mUser = User.getUser(json1.toString());
						MySharedPreference.saveUser(ForgetActivity.this, mUser);
						User user = MySharedPreference.readUser(ForgetActivity.this);

						PublicStaticURL.userid = user.getUid(); // 将用户ID储存，
						PublicStaticURL.ablity = user.getAbility(); // 用户能力值储存
						PublicStaticURL.credit = user.getCredit(); // 用户信用值储存
						PublicStaticURL.Login_phone = user.getTel(); // 用户名

						mDBHelper.insertOrUpdate(forget_phone.getText().toString(),
								forget_password.getText().toString(), 0);
						Intent intent = new Intent();
						SharedPreferences.Editor share = getSharedPreferences("dl", MODE_PRIVATE).edit();
						share.putString("yh", forget_phone.getText().toString());
						share.putString("ps", forget_password.getText().toString());
						Loadings.getCount(PublicStaticURL.ZONGmessage + "&uid=" + user.getUid());
						share.commit();
						Intent intent1 = new Intent(ForgetActivity.this, MainActivity.class);
						Constants.ACTIVITYFLAG = 0;// 指定首页当前的显示的位置
						intent1.putExtra("ACTIVITYFLAG", 0);
						startActivity(intent1);
						sendBroadcast(new Intent("com.example.set.referesh"));
						MyApplication.getInstance().popActivity(LoginActivity.class);
						MyApplication.getInstance().popActivity(ForgetActivity.this);

					}

				} catch (Exception e) {
					SimpleHUD.showInfoMessage(ForgetActivity.this, "登录失败!");
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

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
					GradientDrawable myGrad = (GradientDrawable) forget_huoqu.getBackground();// 获取shape中button的背景颜色
					myGrad.setStroke((int) 0.5, getResources().getColor(R.color.huise9));
					myGrad.setColor(getResources().getColor(R.color.huise9));

					forget_huoqu.setClickable(false);
				} else {
					GradientDrawable myGrad = (GradientDrawable) forget_huoqu.getBackground();// 获取shape中button的背景颜色
					myGrad.setStroke((int) 0.5, getResources().getColor(R.color.green));
					myGrad.setColor(getResources().getColor(R.color.green));
					forget_huoqu.setClickable(true);
				}

				if (TextUtils.isEmpty(s.toString()) || TextUtils.isEmpty(forget_yanzhengma.getText().toString())
						|| TextUtils.isEmpty(forget_password.getText().toString())
						|| TextUtils.isEmpty(forget_quepassword.getText().toString())) {
					setColor(0, false);

				} else {
					setColor(1, true);

				}

				break;
			case 1:
				if (TextUtils.isEmpty(s.toString()) || TextUtils.isEmpty(forget_phone.getText().toString())
						|| TextUtils.isEmpty(forget_password.getText().toString())
						|| TextUtils.isEmpty(forget_quepassword.getText().toString())) {
					setColor(0, false);

				} else {
					setColor(1, true);
				}

				break;
			case 2:
				if (TextUtils.isEmpty(s.toString()) || TextUtils.isEmpty(forget_phone.getText().toString())
						|| TextUtils.isEmpty(forget_yanzhengma.getText().toString())
						|| TextUtils.isEmpty(forget_quepassword.getText().toString())) {
					setColor(0, false);

				} else {
					setColor(1, true);
				}

				break;
			case 3:
				if (TextUtils.isEmpty(s.toString()) || TextUtils.isEmpty(forget_phone.getText().toString())
						|| TextUtils.isEmpty(forget_yanzhengma.getText().toString())
						|| TextUtils.isEmpty(forget_password.getText().toString())) {
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
			GradientDrawable myGrad = (GradientDrawable) forget_forget.getBackground();// 获取shape中button的背景颜色
			myGrad.setStroke((int) 0.5, getResources().getColor(R.color.huise9));
			myGrad.setColor(getResources().getColor(R.color.huise9));
			forget_forget.setClickable(bool);
		}

		if (1 == flag) {
			GradientDrawable myGrad = (GradientDrawable) forget_forget.getBackground();// 获取shape中button的背景颜色
			myGrad.setStroke((int) 0.5, getResources().getColor(R.color.blue2));
			myGrad.setColor(getResources().getColor(R.color.blue2));
			forget_forget.setClickable(bool);
		}

	}

	Boolean isHidden = true;

//	public void show(View view) {
//		if (isHidden == true) {
//			forget_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//			findViewById(R.id.password_value).setBackgroundResource(R.drawable.password_01);
//			isHidden = false;
//
//		} else {
//			forget_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//			findViewById(R.id.password_value).setBackgroundResource(R.drawable.password_02);
//			isHidden = true;
//		}
//		
//		
//		
//		
//		forget_password.postInvalidate();
//		// 切换后将EditText光标置于末尾
//		CharSequence charSequence = forget_password.getText();
//		if (charSequence instanceof Spannable) {
//			Spannable spanText = (Spannable) charSequence;
//			Selection.setSelection(spanText, charSequence.length());
//		}
//
//	}

	@Override
	public void onResume() {
		super.onResume();
		if (mList != null && mList.size() > 0) {
			mList.clear();
		}
		mList = mDBHelper.queryAllUser();
		if (mList.size() > 0) {
			forget_phone.setText(mList.get(0).getUsername());

		}
		umengResume(this, getClass().toString());
	}

	@Override
	protected void onDestroy() {
		mDBHelper.cleanup();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		umengPause(this, getClass().toString());
	}

}
