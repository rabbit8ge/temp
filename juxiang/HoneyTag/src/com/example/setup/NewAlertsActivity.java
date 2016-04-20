package com.example.setup;

import com.example.baseactivity.BaseActivity;
import com.example.config.MySharedPreference;
import com.example.dto.User;
import com.example.honeytag1.R;
import com.example.utils.MyApplication;
import com.example.view.NavigationView.ClickCallbackLeft;
import com.umeng.message.PushAgent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

/**
 * @Description描述:设置
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class NewAlertsActivity extends BaseActivity {

	private LinearLayout mLlContent;
	private CheckBox mCbNewNotifications, mCbVoice, mCbVibration, mCbPeriodTime;
	private PushAgent mPushAgent;
	private User mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		mPushAgent.setDebugMode(true);

		mUser = MySharedPreference.readUser(this);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_newalerts);
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void init() {

		super.init();
	}

	@Override
	protected void initView() {
		super.initView();
		initTopView();

		mRelNavigationView.setTitle(getString(R.string.newAlerts));
		mLlContent = (LinearLayout) findViewById(R.id.linear_content);

		mCbNewNotifications = (CheckBox) findViewById(R.id.check_newNotifications);
		mCbVoice = (CheckBox) findViewById(R.id.check_voice);
		mCbVibration = (CheckBox) findViewById(R.id.check_vibration);
		mCbPeriodTime = (CheckBox) findViewById(R.id.check_periodTime);
		if (MySharedPreference.readNewNotifications(NewAlertsActivity.this)) {
			mCbNewNotifications.setChecked(true);

			mLlContent.setVisibility(View.VISIBLE);
		} else {
			mCbNewNotifications.setChecked(false);
			mLlContent.setVisibility(View.GONE);

		}
		if (MySharedPreference.readVoiceSwitch(NewAlertsActivity.this, mUser)) {
			mCbVoice.setChecked(true);
		} else {
			mCbVoice.setChecked(false);
		}
		if (MySharedPreference.readVibration(NewAlertsActivity.this, mUser)) {
			mCbVibration.setChecked(true);
		} else {
			mCbVibration.setChecked(false);
		}

		if (MySharedPreference.readPeriodTime(NewAlertsActivity.this, mUser)) {
			mCbPeriodTime.setChecked(true);
		} else {
			mCbPeriodTime.setChecked(false);
		}

		mCbNewNotifications.setOnCheckedChangeListener(new MyOnCheckedChangeListener(0));
		mCbVoice.setOnCheckedChangeListener(new MyOnCheckedChangeListener(1));
		mCbVibration.setOnCheckedChangeListener(new MyOnCheckedChangeListener(2));
		mCbPeriodTime.setOnCheckedChangeListener(new MyOnCheckedChangeListener(3));

		mRelNavigationView.setClickCallbackLeft(new ClickCallbackLeft() {

			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(NewAlertsActivity.this);

			}
		});

	}

	Intent intent;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_level:// 我的等级
			startActivity(new Intent(NewAlertsActivity.this, MyGradesActivity.class));
			break;
		case R.id.tv_identity:// 我的身份

			startActivity(new Intent(NewAlertsActivity.this, IdentityActivity.class));

			break;
		case R.id.tv_newAlerts:// 新消息通知

			startActivity(new Intent(NewAlertsActivity.this, IdentityActivity.class));

			break;

		case R.id.tv_otherSetting:// 其它设置界面
			intent = new Intent(this, MoreActivity.class);
			startActivity(intent);

			break;

		default:
			break;
		}

	}

	class MyOnCheckedChangeListener implements OnCheckedChangeListener {
		int position;

		public MyOnCheckedChangeListener(int position) {
			this.position = position;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (position) {
			case 0:
				if (isChecked) {
					mLlContent.setVisibility(View.VISIBLE);
					MySharedPreference.saveNewNotifications(NewAlertsActivity.this, true);

				} else {
					mLlContent.setVisibility(View.GONE);
					MySharedPreference.saveNewNotifications(NewAlertsActivity.this, false);

				}

				break;
			case 1:
				if (isChecked) {

					MySharedPreference.saveVoiceSwitch(NewAlertsActivity.this, true);

				} else {

					MySharedPreference.saveVoiceSwitch(NewAlertsActivity.this, false);
				}

				break;
			case 2:
				if (isChecked) {

					MySharedPreference.saveVibration(NewAlertsActivity.this, true);

				} else {

					MySharedPreference.saveVibration(NewAlertsActivity.this, false);
				}
				break;

			default:
				if (isChecked) {

					MySharedPreference.savePeriodTime(NewAlertsActivity.this, true);

				} else {

					MySharedPreference.savePeriodTime(NewAlertsActivity.this, false);
				}

				break;
			}
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
