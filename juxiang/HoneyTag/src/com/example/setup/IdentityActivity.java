package com.example.setup;

import info.wangchen.simplehud.SimpleHUD;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.adapter.CardListAdapter;
import com.example.baseactivity.BaseActivity;
import com.example.config.MySharedPreference;
import com.example.dialog.SpotsDialog;
import com.example.dto.User;
import com.example.entity.CardInfo;
import com.example.honeytag1.R;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 身份标签
 * 
 * @author Administrator
 * 
 */
public class IdentityActivity extends BaseActivity implements OnClickListener {

	private ListView mListView;
	private LinearLayout identity_fanhui, identity_plus; // 返回按钮
	private LinearLayout mLlNoNetwork;
	private ImageButton identity_btn, identity_plus_btn;// 返回按钮
	private ImageButton mBtnNoNetwork;
	private TextView mTvNoCard;

	private CardListAdapter adapter;
	private List<CardInfo> mList;
	private SpotsDialog mSpotsDialog;
	private User mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_identity);
		mSpotsDialog = new SpotsDialog(this);
		mUser = MySharedPreference.readUser(IdentityActivity.this);
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void initView() {
		super.initView();

		identity_plus = (LinearLayout) findViewById(R.id.identity_plus);
		identity_fanhui = (LinearLayout) findViewById(R.id.identity_fanhui);
		mLlNoNetwork = (LinearLayout) findViewById(R.id.linear_noNetwork);
		identity_plus_btn = (ImageButton) findViewById(R.id.identity_plus_btn);
		identity_btn = (ImageButton) this.findViewById(R.id.identity_btn);
		mBtnNoNetwork = (ImageButton) findViewById(R.id.btn_noNetwork);
		mTvNoCard = (TextView) findViewById(R.id.tv_noCard);
		mListView = (ListView) findViewById(R.id.listView1);

		identity_fanhui.setOnClickListener(this);
		identity_btn.setOnClickListener(this);
		identity_plus.setOnClickListener(this);
		identity_plus_btn.setOnClickListener(this);
		mBtnNoNetwork.setOnClickListener(this);
		mTvNoCard.setOnClickListener(this);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (adapter != null && adapter.position1 != -1) {

					adapter.closeItem(adapter.position1);
				}

			}
		});
		mListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (adapter != null && adapter.position1 != -1) {

					adapter.closeItem(adapter.position1);
				}

				return false;
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.identity_plus:

			startActivity(new Intent(IdentityActivity.this, AddbusinesscardActivity.class));
			break;
		case R.id.identity_plus_btn:
			startActivity(new Intent(IdentityActivity.this, AddbusinesscardActivity.class));
			break;
		case R.id.identity_fanhui:
			MyApplication.getInstance().popActivity(this);

			break;
		case R.id.identity_btn:
			MyApplication.getInstance().popActivity(IdentityActivity.this);

			break;
		case R.id.btn_noNetwork:
			getCardList(PublicStaticURL.HUOQUHANWANG + "&uid=" + PublicStaticURL.userid);
			break;

		default:

			break;

		}
	}

	/*
	 * 
	 * 获取名片列表信息
	 * 
	 */
	public void getCardList(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
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
				SimpleHUD.showInfoMessage(IdentityActivity.this, getString(R.string.server_connect_failed));
				fillAdapter();

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String str1 = responseInfo.result;// 接口返回的数据

				JSONObject json;

				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {

						// String result = json.getString("result");
						if (json.equals("")) {

						} else {

							JSONArray array = json.getJSONArray("result");
							mACache.put(mUser.getUid() + "identityTags", array.toString());

						}

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(IdentityActivity.this, getString(R.string.getinfo_failed));
					}

				} catch (Exception e) {
					mLlNoNetwork.setVisibility(View.VISIBLE);
					SimpleHUD.showInfoMessage(IdentityActivity.this, getString(R.string.getinfo_failed));
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

	/* 填充适配器 */
	public void fillAdapter() {
		String result = mACache.getAsString(mUser.getUid()+"identityTags");
		if (TextUtils.isEmpty(result)) {
			return;
		}
		mList = new ArrayList<CardInfo>();

		mList = CardInfo.getCardInfoList(result);
		if (mList.size() > 0) {
			PublicStaticURL.hasFirstCard = 1;
		} else {
			PublicStaticURL.hasFirstCard = 0;
		}

		if (mList != null && mList.size() == 0) {
			mTvNoCard.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
		if (mList != null && mList.size() > 0) {
			mTvNoCard.setVisibility(View.GONE);
			mLlNoNetwork.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			adapter = new CardListAdapter(IdentityActivity.this, mList);

			mListView.setAdapter(adapter);
		}

	}

	// 字符串截取
	private String Transformation(String s) {
		String str = s.replace("[", "");
		String str1 = str.replace("]", "");

		if (str1.indexOf("\"") == -1) {
			return str1;
		}
		String str2 = str1.replace("\"", "");
		return str2;
	}

	@Override
	protected void onResume() {
		fillAdapter();
		if (mUser != null) {
			getCardList(PublicStaticURL.HUOQUHANWANG + "&uid=" + PublicStaticURL.userid);
		}

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

}
