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
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class AbilltyActivity extends BaseActivity implements OnClickListener {

	private LinearLayout abilly_fanhui;
	private ImageButton abilly_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
	}

	@Override
	public void loadXml() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_abillty);
		abilly_fanhui = (LinearLayout) findViewById(R.id.abilly_fanhui);
		abilly_btn = (ImageButton) findViewById(R.id.abilly_btn);
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		abilly_btn.setOnClickListener(this);
		abilly_fanhui.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.abilly_fanhui:
			finish();

			break;

		case R.id.abilly_btn:
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
