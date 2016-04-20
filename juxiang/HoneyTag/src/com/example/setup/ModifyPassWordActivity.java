package com.example.setup;

import com.example.dialog.SpotsDialog;
import com.example.honeytag1.BaseActivity;
import com.example.honeytag1.PublishActivity;
import com.example.honeytag1.R;
import com.example.honeytag1.R.id;
import com.example.honeytag1.R.layout;
import com.example.my.LoginActivity;
import com.example.utils.MyApplication;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * 修改密码页面
 * 
 * @author Administrator
 * 
 */
public class ModifyPassWordActivity extends BaseActivity implements OnClickListener {

	private LinearLayout modify_fanhui; // 返回按钮
	private ImageButton modify_btn;// 返回按钮
	private EditText modify_pwdone, modify_pwdtwo, modify_pwdthree; // 用户名，密码
	private Button modify_modify; // 登录

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}

		MyApplication.getInstance().addActivity(this);
	}

	@Override
	public void loadXml() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_modify_pass_word);
		modify_fanhui = (LinearLayout) this.findViewById(R.id.modify_fanhui);
		modify_btn = (ImageButton) this.findViewById(R.id.modify_btn);
		modify_pwdone = (EditText) this.findViewById(R.id.modify_pwdone);
		modify_pwdtwo = (EditText) this.findViewById(R.id.modify_pwdtwo);
		modify_pwdthree = (EditText) this.findViewById(R.id.modify_pwdthree);
		modify_modify = (Button) this.findViewById(R.id.modify_modify);
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		// 绑定监听事件
		modify_fanhui.setOnClickListener(this);
		modify_btn.setOnClickListener(this);
		modify_modify.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.modify_modify:

			new SpotsDialog(ModifyPassWordActivity.this).show();
			break;
		case R.id.modify_fanhui:
			finish();

			break;
		case R.id.modify_btn:
			finish();

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

}
