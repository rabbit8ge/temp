package com.example.honeytag1;

import android.os.Bundle;

import com.example.honeytag1.R;
import com.example.utils.MyApplication;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class AboutActivity extends BaseActivity implements OnClickListener {

	LinearLayout about_fanhui;
	ImageButton about_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void loadXml() {
		setContentView(R.layout.activity_about);
		about_fanhui = (LinearLayout) findViewById(R.id.about_fanhui);
		about_btn = (ImageButton) findViewById(R.id.about_btn);
	}

	public void loadData() {
		about_fanhui.setOnClickListener(this);
		about_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.about_fanhui:
			MyApplication.getInstance().popActivity(AboutActivity.class);
			break;
		case R.id.about_btn:
			MyApplication.getInstance().popActivity(AboutActivity.class);
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
