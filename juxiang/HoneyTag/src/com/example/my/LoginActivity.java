package com.example.my;

import info.wangchen.simplehud.SimpleHUD;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.example.adapter.UserAdapter;
import com.example.config.Constants;
import com.example.config.MySharedPreference;
import com.example.database.DBHelper;
import com.example.dialog.SpotsDialog;
import com.example.dto.User;
import com.example.dto.UserInfo;
import com.example.honeytag1.R;
import com.example.http.Loadings;
import com.example.honeytag1.BaseActivity;
import com.example.honeytag1.MainActivity;
import com.example.setup.ForgetActivity;
import com.example.setup.UserAgreementActivity;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.utils.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

/**
 * 登录页面
 * 
 * @author Administrator
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private View contentView, view;
	private CheckBox mCheckBox;
	public PopupWindow mPopupWindow;
	private LinearLayout login_fanhui; // 返回按钮
	private ImageButton login_btn;// 返回按钮
	public EditText login_name, login_password; // 用户名，密码
	private Button login_login; // 登录
	private TextView login_wangji, login_regist; // 忘记密码，注册
	private TextView mTVuseragreement;// 用户协议
	SpotsDialog spotsDialog; // dialog
	String telphone, tel_password;
	private DBHelper mDBHelper;
	private ListView mListView;
	private UserAdapter adapter;
	private List<UserInfo> mList = new ArrayList<UserInfo>();
	private String device_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		mPushAgent.setDebugMode(true);

		MyApplication.getInstance().addActivity(this);
		mDBHelper = new DBHelper(this);

	}

	@Override
	public void loadXml() {
		setContentView(R.layout.activity_login);
		// TODO Auto-generated method stub
		view = findViewById(R.id.view);
		login_fanhui = (LinearLayout) this.findViewById(R.id.login_fanhui);
		login_btn = (ImageButton) this.findViewById(R.id.login_btn);
		login_name = (EditText) this.findViewById(R.id.login_name);
		login_password = (EditText) this.findViewById(R.id.login_password);
		login_login = (Button) this.findViewById(R.id.login_login);
		login_wangji = (TextView) this.findViewById(R.id.login_wangji);
		login_regist = (TextView) this.findViewById(R.id.login_regist);
		login_name.setText(PublicStaticURL.Youmi_phone);
		login_password.setText(PublicStaticURL.Youmi_password);
		mTVuseragreement = (TextView) this.findViewById(R.id.tv_useragreement);
		mCheckBox = (CheckBox) this.findViewById(R.id.checkBox);
		GradientDrawable myGrad = (GradientDrawable) login_login.getBackground();// 获取shape中button的背景颜色
		myGrad.setStroke((int) 0.5, getResources().getColor(R.color.huise9));
		myGrad.setColor(getResources().getColor(R.color.huise9));
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		login_login.setClickable(false);
		login_login.setEnabled(false);

		// 绑定监听事件
		login_fanhui.setOnClickListener(this);
		login_btn.setOnClickListener(this);
		login_login.setOnClickListener(this);
		login_wangji.setOnClickListener(this);
		login_regist.setOnClickListener(this);
		mTVuseragreement.setOnClickListener(this);

		login_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(login_password.getText().toString()) || TextUtils.isEmpty(s.toString())) {
					GradientDrawable myGrad = (GradientDrawable) login_login.getBackground();// 获取shape中button的背景颜色
					myGrad.setStroke((int) 0.5, getResources().getColor(R.color.huise9));
					myGrad.setColor(getResources().getColor(R.color.huise9));
					login_login.setClickable(false);
					login_login.setEnabled(false);
				} else {
					GradientDrawable myGrad = (GradientDrawable) login_login.getBackground();// 获取shape中button的背景颜色
					myGrad.setStroke((int) 0.5, getResources().getColor(R.color.blue2));
					myGrad.setColor(getResources().getColor(R.color.blue2));
					login_login.setClickable(true);
					login_login.setEnabled(true);
				}

			}
		});

		login_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(login_name.getText().toString()) || TextUtils.isEmpty(s.toString())) {
					GradientDrawable myGrad = (GradientDrawable) login_login.getBackground();// 获取shape中button的背景颜色
					myGrad.setStroke((int) 0.5, getResources().getColor(R.color.huise9));
					myGrad.setColor(getResources().getColor(R.color.huise9));
					login_login.setClickable(false);
					login_login.setEnabled(false);

				} else {
					GradientDrawable myGrad = (GradientDrawable) login_login.getBackground();// 获取shape中button的背景颜色
					myGrad.setStroke((int) 0.5, getResources().getColor(R.color.blue2));
					myGrad.setColor(getResources().getColor(R.color.blue2));
					login_login.setClickable(true);
					login_login.setEnabled(true);
				}
			}
		});

		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					if (mPopupWindow != null) {
						mPopupWindow.dismiss();
					}

				} else {
					if (mList.size() > 0) {
						initPopView();
					}
				}

			}
		});

	}

	@Override
	protected void onResume() {
		if (mList != null && mList.size() > 0) {
			mList.clear();
		}
		mList = mDBHelper.queryAllUser();
		umengResume(this, getClass().toString());
		UserInfo mUserInfo = MySharedPreference.readUserInfo(LoginActivity.this);

		if (mUserInfo != null) {
			login_name.setText(mUserInfo.getUsername());
			login_password.setText(mUserInfo.getPassword());
		}

		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		device_token = UmengRegistrar.getRegistrationId(this);

		if (v.getId() == R.id.login_fanhui) {
			MyApplication.getInstance().popActivity(LoginActivity.this);
		}
		if (v.getId() == R.id.login_btn) {
			MyApplication.getInstance().popActivity(LoginActivity.this);
		}
		if (v.getId() == R.id.login_login) {
			// if (login_name.getText().toString().length() > 11) {
			if (!Utils.isMObilehone(login_name.getText().toString())) {
				SimpleHUD.showInfoMessage(LoginActivity.this, getString(R.string.input_phone));

			} else if (login_password.getText().toString().length() == 0) {
				SimpleHUD.showInfoMessage(LoginActivity.this, getString(R.string.input_password));
			} else if (login_password.getText().toString().length() < 6) {
				SimpleHUD.showInfoMessage(LoginActivity.this, getString(R.string.password_atleast6));
			} else if (TextUtils.isEmpty(device_token)) {
				SimpleHUD.showInfoMessage(LoginActivity.this, "登录失败");

			} else {

				telphone = login_name.getText().toString();
				tel_password = login_password.getText().toString();
				// GETLOGIN(PublicStaticURL.LOGIN + "&tel=" + telphone +
				// "&password=" + tel_password + "&device_type="
				// + Constants.deviceType + "&device_token=" +
				// "Ait2Lq1w_uyUCYr5h6RvJ9VVQK5G4A2285SLVTx0tFV1");
				GETLOGIN(PublicStaticURL.LOGIN + "&tel=" + telphone + "&password=" + tel_password + "&device_type="
						+ Constants.deviceType + "&device_token=" + device_token);
				Logger.i(PublicStaticURL.LOGIN + "&tel=" + telphone + "&password=" + tel_password);
				MobclickAgent.onProfileSignIn(telphone);// 友盟账号统计
			}

		}
		if (v.getId() == R.id.login_wangji) {

			// if (login_name.getText().toString().equals("")) {
			// SimpleHUD.showInfoMessage(LoginActivity.this, "请填写注册手机号");
			// } else {

			Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
			intent.putExtra("activityFlag", 0);
			intent.putExtra("flag", login_name.getText().toString());
			startActivity(intent);
			// }

		}

		if (v.getId() == R.id.login_regist) {

			startActivity(new Intent(LoginActivity.this, RegistActivity.class));

			MyApplication.getInstance().popActivity(LoginActivity.this);
		}
		if (v.getId() == R.id.tv_useragreement) {
			startActivity(new Intent(LoginActivity.this, UserAgreementActivity.class));

		}
	}

	// 登录

	private void GETLOGIN(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(LoginActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showInfoMessage(LoginActivity.this, getString(R.string.server_connect_failed));
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

						Toast.makeText(LoginActivity.this, getString(R.string.login_succeed), Toast.LENGTH_SHORT)
								.show();
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);

						Intent intent = new Intent();
						mDBHelper.insertOrUpdate(telphone, tel_password, 0);
						SharedPreferences.Editor share = getSharedPreferences("dl", MODE_PRIVATE).edit();
						share.putString("yh", telphone);
						share.putString("ps", tel_password);
						share.commit();

						UserInfo mUserInfo = new UserInfo();
						mUserInfo.setUsername(telphone);
						mUserInfo.setPassword(tel_password);
						MySharedPreference.saveUserInfo(MyApplication.getInstance(), mUserInfo);
						User mUser = User.getUser(json1.toString());
						MySharedPreference.saveUser(LoginActivity.this, mUser);
						User user = MySharedPreference.readUser(MyApplication.getInstance());

						PublicStaticURL.userid = user.getUid(); // 将用户ID储存，
						PublicStaticURL.ablity = user.getAbility(); // 用户能力值储存
						PublicStaticURL.credit = user.getCredit(); // 用户信用值储存
						PublicStaticURL.Login_phone = user.getTel(); // 用户名
						PublicStaticURL.IsLogin = true;
						Loadings.getCount(PublicStaticURL.ZONGmessage + "&uid=" + user.getUid());
						MyApplication.getInstance().popActivity(LoginActivity.this);
						startActivity(new Intent(LoginActivity.this, MainActivity.class));
						sendBroadcast(new Intent("com.example.set.referesh"));

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(LoginActivity.this, getString(R.string.phone_psw_error));
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(LoginActivity.this, getString(R.string.phone_not_regist));
					}
					if ("5".equals(code)) {
						SimpleHUD.showInfoMessage(LoginActivity.this, "此用户已在黑名单中");
					}
					if ("6".equals(code)) {
						SimpleHUD.showInfoMessage(LoginActivity.this, "登录失败");
					}
					if ("10".equals(code)) {
						PublicStaticURL.IsLogin = true;
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);

						UserInfo mUserInfo = new UserInfo();
						mUserInfo.setUsername(telphone);
						mUserInfo.setPassword(tel_password);
						MySharedPreference.saveUserInfo(LoginActivity.this, mUserInfo);
						User mUser = User.getUser(json1.toString());
						MySharedPreference.saveUser(MyApplication.getInstance(), mUser);
						User user = MySharedPreference.readUser(MyApplication.getInstance());
						user.getUid();

						PublicStaticURL.userid = user.getUid(); // 将用户ID储存，
						PublicStaticURL.ablity = user.getAbility(); // 用户能力值储存
						PublicStaticURL.credit = user.getCredit(); // 用户信用值储存
						PublicStaticURL.Login_phone = user.getTel(); // 用户名

						Intent intent = new Intent();
						mDBHelper.insertOrUpdate(telphone, tel_password, 0);
						SharedPreferences.Editor share = getSharedPreferences("dl", MODE_PRIVATE).edit();
						share.putString("yh", telphone);
						share.putString("ps", tel_password);
						share.commit();
						// ibuilder = new
						// CustomDialog.Builder(LoginActivity.this);
						// ibuilder.setTitle("提示");
						// ibuilder.setMessage("您今天首次登录能力值加+1哦");
						// ibuilder.setPositiveButton("去看看", new
						// DialogInterface.OnClickListener() {
						//
						// @Override
						// public void onClick(DialogInterface arg0, int arg1) {
						// // TODO Auto-generated method stub
						//
						// PublicStaticURL.IsLogin = true;
						// startActivity(new Intent(LoginActivity.this,
						// AbilityvalueActivity.class));
						// finish();
						//
						// arg0.dismiss();
						// }
						// });
						// ibuilder.setNegativeButton("以后在说", new
						// DialogInterface.OnClickListener() {
						//
						// @Override
						// public void onClick(DialogInterface arg0, int arg1) {
						// // TODO Auto-generated method stub
						// PublicStaticURL.IsLogin = true;
						// finish();
						// }
						// });
						//
						// ibuilder.create().show();
						Loadings.getCount(PublicStaticURL.ZONGmessage + "&uid=" + user.getUid());
						MyApplication.getInstance().popActivity(LoginActivity.this);
						startActivity(new Intent(LoginActivity.this, MainActivity.class));
						sendBroadcast(new Intent("com.example.set.referesh"));
					}

				} catch (Exception e) {

					SimpleHUD.showInfoMessage(LoginActivity.this, getString(R.string.app_exception));

					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	/*
	 * 此方法用于展示登录过的用户
	 */
	private void initPopView() {

		int width = view.getWidth();
		int height = view.getHeight();
		float x = view.getX();
		float y = view.getY();
		contentView = getLayoutInflater().inflate(R.layout.poupwindow, null);
		mListView = (ListView) contentView.findViewById(R.id.listView1);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			}

		});

		adapter = new UserAdapter(this, mDBHelper);
		mListView.setAdapter(adapter);
		adapter.reFresh(mList);

		mPopupWindow = new PopupWindow(contentView, width, LayoutParams.WRAP_CONTENT, true);

		mPopupWindow.setTouchable(true);

		mPopupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("mengdd", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}

		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

		// 设置好参数之后再show
		mPopupWindow.showAsDropDown(view, 0, 0);

		// mPopupWindow.showAtLocation(linear_user,Gravity.BOTTOM ,0,-5);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				mCheckBox.setChecked(true);

			}

		});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		umengPause(this, getClass().toString());
	}

}
