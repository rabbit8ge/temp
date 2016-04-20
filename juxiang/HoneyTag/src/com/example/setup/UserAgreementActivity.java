package com.example.setup;

import com.example.honeytag1.BaseActivity;
import com.example.honeytag1.PublishActivity;
import com.example.honeytag1.R;
import com.example.utils.MyApplication;
import com.example.view.NavigationView;
import com.example.view.NavigationView.ClickCallback;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @Description描述:设置
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class UserAgreementActivity extends BaseActivity {
	private NavigationView mRelNavigationView;
	private TextView mTvLevel, mTvIdentity, mTvOtherSetting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyApplication.getInstance().addActivity(this);

	}

	@Override
	public void loadXml() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_useragreement);
		mRelNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mRelNavigationView.setTitle("用户协议");

		mTvLevel = (TextView) findViewById(R.id.tv_level);
		mTvIdentity = (TextView) findViewById(R.id.tv_identity);
		mTvOtherSetting = (TextView) findViewById(R.id.tv_otherSetting);

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		mRelNavigationView.setClickCallback(new ClickCallback() {

			@Override
			public void onRightClick() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(UserAgreementActivity.this);

			}

			@Override
			public void onRightTVClick() {
				// TODO Auto-generated method stub

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

}
