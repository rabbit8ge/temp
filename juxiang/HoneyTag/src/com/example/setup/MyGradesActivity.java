package com.example.setup;

import java.util.List;
import org.json.JSONObject;
import com.example.config.MySharedPreference;
import com.example.dialog.SpotsDialog;
import com.example.dto.GradesDTO;
import com.example.dto.User;
import com.example.honeytag1.R;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.view.NavigationView.ClickCallbackLeft;
import com.example.view.NavigationView.ClickCallbackRight;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import info.wangchen.simplehud.SimpleHUD;

/**
 * @Description描述:设置
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class MyGradesActivity extends com.example.baseactivity.BaseActivity {

	private TextView mTvIntegral, mTvLevel1;
	private TextView mTvLevel01, mTvLevel02, mTvLevel03, mTvLevel04;
	private TextView mTvRestrictedUser, mTvPrimaryUser, mTvIntermediateUsers, mTvAdvancedUsers;// 受限用户，初级用户，中级用户，高级用户，
	private CheckBox mCbLevel01, mCbLevel02, mCbLevel03, mCbLevel04;
	private List<GradesDTO> mList;
	private  SpotsDialog mSpotsDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_mygrades);
		mSpotsDialog=new SpotsDialog(this);
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void init() {
		super.init();

		getData();

	}

	@Override
	protected void initView() {
		super.initView();
		initTopView();

		mRelNavigationView.setTitle(getString(R.string.setting));
		mTvRight.setVisibility(View.VISIBLE);
		mTvRight.setText("规则");

		mTvIntegral = (TextView) findViewById(R.id.tv_integral);
		mTvLevel1 = (TextView) findViewById(R.id.tv_level1);
		mTvRestrictedUser = (TextView) findViewById(R.id.tv_restrictedUser);
		mTvPrimaryUser = (TextView) findViewById(R.id.tv_primaryUser);
		mTvIntermediateUsers = (TextView) findViewById(R.id.tv_intermediateUsers);
		mTvAdvancedUsers = (TextView) findViewById(R.id.tv_advancedUsers);
		mTvLevel01 = (TextView) findViewById(R.id.tv_Level1);
		mTvLevel02 = (TextView) findViewById(R.id.tv_Level2);
		mTvLevel03 = (TextView) findViewById(R.id.tv_Level3);
		mTvLevel04 = (TextView) findViewById(R.id.tv_Level4);

		mCbLevel01 = (CheckBox) findViewById(R.id.checkBox01);
		mCbLevel02 = (CheckBox) findViewById(R.id.checkBox02);
		mCbLevel03 = (CheckBox) findViewById(R.id.checkBox03);
		mCbLevel04 = (CheckBox) findViewById(R.id.checkBox04);

		fillData();

	
		mRelNavigationView.setClickCallbackLeft(new ClickCallbackLeft() {

			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(MyGradesActivity.this);

			}
		});

		mRelNavigationView.setClickCallbackRight(new ClickCallbackRight() {

			@Override
			public void onRightClick() {

				startActivity(new Intent(MyGradesActivity.this, MyGradesRules.class));

			}
		});
	}

	public void initIntegral() {
		if (TextUtils.isEmpty(PublicStaticURL.ablity)) {
			return;
		}
		mTvIntegral.setText(Float.parseFloat(PublicStaticURL.ablity) + "");

		if (Float.parseFloat(PublicStaticURL.ablity) >= 0 && Float.parseFloat(PublicStaticURL.ablity) < 20) {
			mTvLevel1.setText("1级");
			mCbLevel01.setChecked(true);
			mTvRestrictedUser.setTextColor(getResources().getColor(R.color.huise10));
			mTvRestrictedUser.setTextSize((float) 18);

			mTvPrimaryUser.setTextColor(getResources().getColor(R.color.huise12));
			mTvPrimaryUser.setTextSize((float) 16);
			mTvIntermediateUsers.setTextColor(getResources().getColor(R.color.huise12));
			mTvIntermediateUsers.setTextSize((float) 16);
			mTvAdvancedUsers.setTextColor(getResources().getColor(R.color.huise12));
			mTvAdvancedUsers.setTextSize((float) 16);

		}
		if (Float.parseFloat(PublicStaticURL.ablity) >= 20 && Float.parseFloat(PublicStaticURL.ablity) < 50) {
			mTvLevel1.setText("2级");
			mCbLevel02.setChecked(true);
			mTvRestrictedUser.setTextColor(getResources().getColor(R.color.huise12));
			mTvRestrictedUser.setTextSize((float) 16);

			mTvPrimaryUser.setTextColor(getResources().getColor(R.color.huise10));
			mTvPrimaryUser.setTextSize((float) 18);

			mTvIntermediateUsers.setTextColor(getResources().getColor(R.color.huise12));
			mTvIntermediateUsers.setTextSize((float) 16);

			mTvAdvancedUsers.setTextColor(getResources().getColor(R.color.huise12));
			mTvAdvancedUsers.setTextSize((float) 16);
		}
		if (Float.parseFloat(PublicStaticURL.ablity) >= 50 && Float.parseFloat(PublicStaticURL.ablity) < 150) {
			mTvLevel1.setText("3级");
			mCbLevel03.setChecked(true);
			mTvRestrictedUser.setTextColor(getResources().getColor(R.color.huise12));
			mTvRestrictedUser.setTextSize((float) 16);

			mTvPrimaryUser.setTextColor(getResources().getColor(R.color.huise12));
			mTvPrimaryUser.setTextSize((float) 16);

			mTvIntermediateUsers.setTextColor(getResources().getColor(R.color.huise10));
			mTvIntermediateUsers.setTextSize((float) 18);

			mTvAdvancedUsers.setTextColor(getResources().getColor(R.color.huise12));
			mTvAdvancedUsers.setTextSize((float) 16);
		}
		if (Float.parseFloat(PublicStaticURL.ablity) >= 150) {
			mTvLevel1.setText("4级");
			mCbLevel04.setChecked(true);
			mTvRestrictedUser.setTextColor(getResources().getColor(R.color.huise12));
			mTvRestrictedUser.setTextSize((float) 16);

			mTvPrimaryUser.setTextColor(getResources().getColor(R.color.huise12));
			mTvPrimaryUser.setTextSize((float) 16);

			mTvIntermediateUsers.setTextColor(getResources().getColor(R.color.huise12));
			mTvIntermediateUsers.setTextSize((float) 16);

			mTvAdvancedUsers.setTextColor(getResources().getColor(R.color.huise10));
			mTvAdvancedUsers.setTextSize((float) 16);
		}

	}

	public void setValue(List<GradesDTO> mList) {
		if (mList == null || mList.size() == 0) {
			return;
		}

		mTvRestrictedUser.setText(mList.get(0).getName());
		mTvPrimaryUser.setText(mList.get(1).getName());
		mTvIntermediateUsers.setText(mList.get(2).getName());
		mTvAdvancedUsers.setText(mList.get(3).getName());
		mTvLevel01.setText(mList.get(0).getContent().replace("\\n", "\n"));
		mTvLevel02.setText(mList.get(1).getContent().replace("\\n", "\n"));
		mTvLevel03.setText(mList.get(2).getContent().replace("\\n", "\n"));
		mTvLevel04.setText(mList.get(3).getContent().replace("\\n", "\n"));

	}

	public void fillData() {
		String detail = mACache.getAsString("grades");
		if (TextUtils.isEmpty(detail)) {
			return;
		}
		mList = GradesDTO.getGradesDTOList(detail);
		setValue(mList);
	}

	/*
	 * 获取积分
	 */
	private void getIntegral(String str, RequestParams params) {

		mHttpUtils.configCurrentHttpCacheExpiry(0);
		mHttpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				if (mSpotsDialog != null) {
					mSpotsDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
					mSpotsDialog.show();
				}

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
					mSpotsDialog.dismiss();
				}
				SimpleHUD.showInfoMessage(MyGradesActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
					mSpotsDialog.dismiss();
				}
				String str1 = responseInfo.result;// 接口返回的数据
				JSONObject json;

				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {

						String result = json.getString("result");

						PublicStaticURL.ablity = json.getString("ability");

						initIntegral();

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(MyGradesActivity.this, getString(R.string.getinfo_failed));
					}

				} catch (Exception e) {
					SimpleHUD.showInfoMessage(MyGradesActivity.this, getString(R.string.getinfo_failed));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	/*
	 * 获取规则
	 */
	private void getData() {

		mHttpUtils.configCurrentHttpCacheExpiry(0);
		mHttpUtils.send(HttpMethod.POST, PublicStaticURL.MYGRADES, null, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				if (mSpotsDialog != null) {
					mSpotsDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
					mSpotsDialog.show();
				}

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
					mSpotsDialog.dismiss();
				}
				SimpleHUD.showInfoMessage(MyGradesActivity.this, getString(R.string.server_connect_failed));
				fillData();

				// initIntegral();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
					mSpotsDialog.dismiss();
				}
				String str1 = responseInfo.result;// 接口返回的数据
				JSONObject json;

				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {

						JSONObject ob = json.getJSONObject("result");
						String detail = ob.getString("detail");
						mACache.put("grades", detail);

					}

				} catch (Exception e) {
					SimpleHUD.showInfoMessage(MyGradesActivity.this, getString(R.string.getinfo_failed));
					Log.e("message", "走catch了");
					e.printStackTrace();
				} finally {
					fillData();

					// initIntegral();
				}
			}

		});

	}

	@Override

	public void onResume() {
		initIntegral();
		User mUser = MySharedPreference.readUser(MyGradesActivity.this);
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", mUser.getUid());
		getIntegral(PublicStaticURL.CreadRecord, params);
		umengResume(this, getClass().toString());

		super.onResume();
	}

	@Override
	protected void onPause() {
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

}
