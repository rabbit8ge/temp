package com.example.setup;

import java.util.List;
import org.json.JSONObject;
import com.example.adapter.RulesAdapter;
import com.example.baseactivity.BaseActivity;
import com.example.dialog.SpotsDialog;
import com.example.dto.RulesDTO;
import com.example.honeytag1.R;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.view.NavigationView.ClickCallbackLeft;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import info.wangchen.simplehud.SimpleHUD;

/**
 * @Description描述:设置
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class MyGradesRules extends BaseActivity {
	private ListView mListView;
	private  SpotsDialog mSpotsDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_rules);
		mSpotsDialog=new SpotsDialog(this);
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();

		fillAdapter();
		getData();
	}

	@Override
	protected void initView() {
		super.initView();
		initTopView();

		mRelNavigationView.setTitle(getString(R.string.rules));
		mListView = (ListView) findViewById(R.id.listView);

		mRelNavigationView.setClickCallbackLeft(new ClickCallbackLeft() {

			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(MyGradesRules.this);

			}
		});

	}

	/*
	 * 获取规则
	 */
	private void getData() {

		mHttpUtils.configCurrentHttpCacheExpiry(0);
		mHttpUtils.send(HttpMethod.POST, PublicStaticURL.RULES, null, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				if (mSpotsDialog != null) {
					mSpotsDialog.show();
				}

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
					mSpotsDialog.dismiss();
				}
				SimpleHUD.showInfoMessage(MyGradesRules.this, getString(R.string.server_connect_failed));
				// fillAdapter();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String str1 = responseInfo.result;// 接口返回的数据
				JSONObject json;

				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {

						JSONObject ob = json.getJSONObject("result");
						String detail = ob.getString("detail");
						mACache.put("gradesRules", detail);

					}

				} catch (Exception e) {
					SimpleHUD.showInfoMessage(MyGradesRules.this, getString(R.string.getinfo_failed));
					Log.e("message", "走catch了");
					e.printStackTrace();
				} finally {
					if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
						mSpotsDialog.dismiss();
					}
					fillAdapter();

				}
			}

		});

	}

	public void fillAdapter() {
		String detail = mACache.getAsString("gradesRules");
		if (TextUtils.isEmpty(detail)) {
			return;
		}
		List<RulesDTO> mList = RulesDTO.getRulesDTOList(detail);
		RulesAdapter adapter = new RulesAdapter(MyGradesRules.this, mList);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(getClass().toString());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(getClass().toString());
	}
	
	@Override
	protected void onDestroy() {

		if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
			mSpotsDialog.cancel();
			mSpotsDialog = null;
		}
		super.onDestroy();
	}

}
