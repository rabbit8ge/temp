package com.example.setup;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import com.example.adapter.LogsAdapter;
import com.example.dialog.SpotsDialog;
import com.example.dto.LogDTO;
import com.example.honeytag1.R;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.utils.VersionManagement;
import com.example.view.NavigationView;
import com.example.view.NavigationView.ClickCallbackLeft;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @Description描述:设置
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class UpdateLogActivity extends com.example.baseactivity.BaseActivity implements OnItemClickListener {

	private ListView mListView;
	private LogsAdapter mLogsAdapter;
	private List<LogDTO> mList = new ArrayList<LogDTO>();
	private SpotsDialog mSpotsDialog; // dialog

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_updatelog);
		mSpotsDialog = new SpotsDialog(this);
		super.onCreate(savedInstanceState);

		init();

	}

	@Override
	protected void init() {
		super.init();
		initView();
		fillAdapter();
		getLogs();
	}

	public void fillAdapter() {
		mLogsAdapter = new LogsAdapter(UpdateLogActivity.this);
		mListView.setAdapter(mLogsAdapter);
	}

	protected void initView() {
		super.initView();
		initTopView();

		mRelNavigationView.setTitle(getString(R.string.update_log));
		mListView = (ListView) findViewById(R.id.listView);
		mListView.setOnItemClickListener(this);
		mRelNavigationView.setClickCallbackLeft(new ClickCallbackLeft() {

			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(UpdateLogActivity.this);

			}
		});
	}

	/* 栏目红点 */
	private void getLogs() {

		RequestParams params = new RequestParams();
		params.addBodyParameter("version", VersionManagement.getInstance().getVersionCode(UpdateLogActivity.this) + "");

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, PublicStaticURL.LOG, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				if (mSpotsDialog != null) {
					mSpotsDialog.show();
				}

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
					mSpotsDialog.dismiss();
				}

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i(str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						String result = json.getString("result");
						mList = LogDTO.getLogDTOList(result);
						mLogsAdapter.reFresh(mList);

					}
					if ("3".equals(code)) {

					}

				} catch (Exception e) {
					Log.e("message", "走catch了");

					e.printStackTrace();
				} finally {
					if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
						mSpotsDialog.dismiss();

					}
				}
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

	@Override
	protected void onDestroy() {

		if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
			mSpotsDialog.cancel();
			mSpotsDialog = null;
		}
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		LogDTO mLogDTO = (LogDTO) mLogsAdapter.getItem(position);

		Intent intent = new Intent(UpdateLogActivity.this, UpdateLogDetailActivity.class);
		intent.putExtra("LogDTO", mLogDTO);
		startActivity(intent);

	}

}
