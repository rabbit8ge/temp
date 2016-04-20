package com.example.setup;

import com.example.baseactivity.BaseActivity;
import com.example.honeytag1.R;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.view.NavigationView.ClickCallbackLeft;
import com.readystatesoftware.viewbadger.BadgeView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @Description描述:设置
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */
public class SettingActivity extends BaseActivity {
	private BadgeView badgeView, mBadgeView;
	private TextView mTvMyAccount,mTvLevel, mTvIdentity, mTvOtherSetting;
	private int hight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_setting);
		super.onCreate(savedInstanceState);
		
	}



	@Override
	protected void initView() {
		super.initView();
		initTopView();
		mRelNavigationView.setTitle(getString(R.string.setting));

		mTvMyAccount=(TextView) findViewById(R.id.mTvMyAccount);
		mTvLevel = (TextView) findViewById(R.id.tv_level);
		mTvIdentity = (TextView) findViewById(R.id.tv_identity);
		mTvOtherSetting = (TextView) findViewById(R.id.tv_otherSetting);
		mBadgeView = new BadgeView(this, mTvIdentity);
		mBadgeView.setText(".");
		mBadgeView.setTextSize(10);
		mBadgeView.setBackgroundResource(R.drawable.radio_shape_enter3);
		mBadgeView.setTextColor(getResources().getColor(R.color.transparent));
		mBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		mBadgeView.setAlpha(1f);
		mBadgeView.show();

		badgeView = new BadgeView(this, mTvOtherSetting);
		badgeView.setText(".");
		badgeView.setTextSize(10);
		badgeView.setBackgroundResource(R.drawable.radio_shape_enter3);
		badgeView.setTextColor(getResources().getColor(R.color.transparent));
		badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView.setAlpha(1f);
		badgeView.show();

		mRelNavigationView.setClickCallbackLeft(new ClickCallbackLeft() {

			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(SettingActivity.this);

			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		hight = mTvIdentity.getHeight();

		int h = mBadgeView.getHeight();
		mBadgeView.setBadgeMargin(80, hight / 2 - h / 2);

		int h1 = badgeView.getHeight();
		badgeView.setBadgeMargin(80, hight / 2 - h1 / 2);
		if (0 == PublicStaticURL.hasFirstCard) {
			mBadgeView.show();
		}
		if (1 == PublicStaticURL.hasFirstCard) {
			mBadgeView.hide();
		}

		if (0 == PublicStaticURL.isNewVersion) {
			badgeView.show();
		}
		if (1 == PublicStaticURL.isNewVersion) {
			badgeView.hide();
		}

		super.onWindowFocusChanged(hasFocus);
	}

	Intent intent;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mTvMyAccount://我的账户
			startActivity(new Intent(SettingActivity.this, MyAccountActivity.class));
			break;
		case R.id.tv_level:// 我的等级
			startActivity(new Intent(SettingActivity.this, MyGradesActivity.class));
			break;
		case R.id.tv_identity:// 我的身份

			startActivity(new Intent(SettingActivity.this, IdentityActivity.class));

			break;
		case R.id.tv_newAlerts:// 新消息通知

			startActivity(new Intent(SettingActivity.this, NewAlertsActivity.class));

			break;

		case R.id.tv_otherSetting:// 其它设置界面
			intent = new Intent(this, MoreActivity.class);
			startActivity(intent);
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
