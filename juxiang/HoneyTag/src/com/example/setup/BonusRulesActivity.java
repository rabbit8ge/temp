package com.example.setup;

import com.example.honeytag1.BaseActivity;
import com.example.honeytag1.R;
import com.example.utils.MyApplication;
import com.example.view.NavigationView;
import com.example.view.NavigationView.ClickCallback;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @Description描述:加分规则
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class BonusRulesActivity extends BaseActivity {
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
		setContentView(R.layout.activity_bonus_rules);
		mRelNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mRelNavigationView.setTitle(getString(R.string.jiafen));

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
				MyApplication.getInstance().popActivity(BonusRulesActivity.this);

			}

			@Override
			public void onRightTVClick() {
				// TODO Auto-generated method stub

			}
		});
	}

	Intent intent;

	public void onClick(View v) {
		// switch (v.getId()) {
		// case R.id.tv_level:
		//
		// break;
		// case R.id.tv_identity:
		//
		// startActivity(new Intent(MyGradesActivity.this,
		// IdentityActivity.class));
		//
		// break;
		// case R.id.tv_otherSetting:// 其它设置界面
		// intent = new Intent(this, MoreActivity.class);
		// startActivity(intent);
		//
		// break;
		//
		// default:
		// break;
		// }

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
