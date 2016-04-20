package com.example.setup;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.HistoryDealsAdapter;
import com.example.baseactivity.BaseActivity;
import com.example.dto.HistoryDealsDTO;
import com.example.honeytag1.R;
import com.example.utils.MyApplication;
import com.example.view.NavigationView.ClickCallbackLeft;
import com.readystatesoftware.viewbadger.BadgeView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @Description描述:我的账户
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */
public class MyAccountActivity extends BaseActivity {

	private BadgeView badgeView, mBadgeView;
	private TextView mTvMyAccount, mTvLevel, mTvIdentity, mTvOtherSetting;
	private ListView mListView;
	private HistoryDealsAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_my_account);
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void init() {
		super.init();
		fillAdapter();

	}

	@Override
	protected void initView() {
		super.initView();
		initTopView();
		mRelNavigationView.setTitle(getString(R.string.myAcount));
		mListView = (ListView) findViewById(R.id.listView);

		// mTvMyAccount=(TextView) findViewById(R.id.mTvMyAccount);
		// mTvLevel = (TextView) findViewById(R.id.tv_level);
		// mTvIdentity = (TextView) findViewById(R.id.tv_identity);
		// mTvOtherSetting = (TextView) findViewById(R.id.tv_otherSetting);

		mRelNavigationView.setClickCallbackLeft(new ClickCallbackLeft() {

			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(MyAccountActivity.this);

			}
		});
	}

	/* 填充适配器 */
	public void fillAdapter() {
		mAdapter = new HistoryDealsAdapter(mContext);
		mListView.setAdapter(mAdapter);
		List<HistoryDealsDTO> mList = new ArrayList<HistoryDealsDTO>();
		mList.add(new HistoryDealsDTO());
		mList.add(new HistoryDealsDTO());
		mList.add(new HistoryDealsDTO());
		mAdapter.reFresh(mList);

	}

	Intent intent;

	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.mTvMyAccount://我的账户
		// break;
		// case R.id.tv_level:// 我的等级
		// startActivity(new Intent(MyAccountActivity.this,
		// MyGradesActivity.class));
		// break;
		// case R.id.tv_identity:// 我的身份
		//
		// startActivity(new Intent(MyAccountActivity.this,
		// IdentityActivity.class));
		//
		// break;
		// case R.id.tv_newAlerts:// 新消息通知
		//
		// startActivity(new Intent(MyAccountActivity.this,
		// NewAlertsActivity.class));
		//
		// break;
		//
		// case R.id.tv_otherSetting:// 其它设置界面
		// intent = new Intent(this, MoreActivity.class);
		// startActivity(intent);
		// break;

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
