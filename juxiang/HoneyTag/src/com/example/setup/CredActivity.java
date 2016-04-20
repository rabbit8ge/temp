package com.example.setup;

import com.example.honeytag1.AboutActivity;
import com.example.honeytag1.BaseActivity;
import com.example.honeytag1.R;
import com.example.honeytag1.R.id;
import com.example.honeytag1.R.layout;
import com.example.utils.MyApplication;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class CredActivity extends BaseActivity implements OnClickListener {

	/**
	 * 信用值说明
	 */
	private LinearLayout start_fanhui;
	private ImageButton start_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyApplication.getInstance().addActivity(this);
	}

	@Override
	public void loadXml() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_cred);
		start_fanhui = (LinearLayout) findViewById(R.id.start_fanhui);
		start_btn = (ImageButton) findViewById(R.id.start_btn);

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		start_fanhui.setOnClickListener(this);
		start_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.start_fanhui:
			finish();

			break;

		case R.id.start_btn:
			finish();

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
