package com.example.honeytag1;

import java.io.File;
import com.example.config.MySharedPreference;
import com.example.dialog.CustomDialog;
import com.example.dto.User;
import com.example.honeytag1.R;
import com.example.http.Loadings;
import com.example.my.LoginActivity;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.socialize.PlatformConfig;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

/**
 * 欢迎界面
 * 
 * @author Administrator
 * 
 */
public class WelcomeActivity extends BaseActivity {

	private RelativeLayout welcome_image; // 图片
	String tel_phone, tel_password;
	SharedPreferences sp;
	public static final String separator = File.separator;// 分隔符
	public static final String MNT = Environment.getExternalStorageDirectory().toString();
	public static final String ROOT = MNT + separator + "行家说"; // 创建保存图片文件夹
	public static final String APP_FILE = MNT + separator + "行家说" + separator + "91major.apk"; // APK文件
	private CustomDialog.Builder ibuilder;
	AlphaAnimation animation;
	private String device_token;
	// private SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		mPushAgent.setDebugMode(true);

	}

	@Override
	public void loadXml() {
		setContentView(R.layout.activity_welcome);
		// TODO Auto-generated method stub
		File file1 = new File(ROOT);
		if (!file1.exists()) {
			file1.mkdirs();
		}

		// 自动登录
		sp = getSharedPreferences("dl", MODE_PRIVATE);
		tel_phone = sp.getString("yh", null);
		tel_password = sp.getString("ps", null);

		// //(版本更新)更新app后自动删除apk文件
		// pref = getSharedPreferences("delete",MODE_PRIVATE);
		// boolean flag = pref.getBoolean("flag", false);

		// if (!flag) {
		// //是第一次登录
		// pref.edit().putBoolean("flag", true).commit();
		// File file = new File(APP_FILE);
		// if (file.exists()) {
		// file.delete();
		// PublicStaticURL.Percent=0.0f;
		// }
		// }

	}

	@Override
	public void loadData() {

		welcome_image = (RelativeLayout) this.findViewById(R.id.welcome_image);
		// 三秒钟之后进入login

		/** 加载缩小与放大动画 **/
		Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);

		scaleAnimation.setFillAfter(true);
		welcome_image.startAnimation(scaleAnimation);
		scaleAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				MyApplication.getInstance().device_token = UmengRegistrar.getRegistrationId(WelcomeActivity.this);
				startActivity(new Intent(WelcomeActivity.this, MainActivity.class));

				MyApplication.getInstance().popActivity(WelcomeActivity.this);

			}
		});

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
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
