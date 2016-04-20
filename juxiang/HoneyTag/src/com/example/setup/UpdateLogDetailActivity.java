package com.example.setup;

import com.example.adapter.LogsDetailAdapter;
import com.example.baseactivity.BaseActivity;
import com.example.dto.LogDTO;
import com.example.honeytag1.R;
import com.example.utils.MyApplication;
import com.example.view.NavigationView.ClickCallbackLeft;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @Description描述:设置
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class UpdateLogDetailActivity extends BaseActivity {

	private TextView mTvLogTitle;
	private ListView mListView;
	private LogsDetailAdapter mAdapter;
	private LogDTO mLogDTO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		setContentView(R.layout.activity_updatelogdetail);
		mLogDTO = (LogDTO) getIntent().getSerializableExtra("LogDTO");
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void init() {
	
		super.init();
		
	
		String[] arr = mLogDTO.getContent().split("\r\n");
		mAdapter = new LogsDetailAdapter(UpdateLogDetailActivity.this, arr);
		mListView.setAdapter(mAdapter);

	}

	@Override
	protected void initView() {
		super.initView();
		initTopView();

		mRelNavigationView.setTitle(getString(R.string.update_logDetail));
		mListView = (ListView) findViewById(R.id.listView);
		mTvLogTitle = (TextView) findViewById(R.id.tv_title01);
		mTvLogTitle.setText(mLogDTO.getVersionname() + "（" + mLogDTO.getCreate_time() + "）");
		mRelNavigationView.setClickCallbackLeft(new ClickCallbackLeft() {

			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(UpdateLogDetailActivity.this);

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
