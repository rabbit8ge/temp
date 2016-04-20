package com.example.fragment;

import info.wangchen.simplehud.SimpleHUD;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import com.example.adapter.ColumnAdapter;
import com.example.config.MySharedPreference;
import com.example.dialog.SpotsDialog;
import com.example.dto.ACache;
import com.example.dto.ColumnDTO;
import com.example.dto.User;
import com.example.honeytag1.R;
import com.example.http.Loadings;
import com.example.honeytag1.ColumnDetailsActivity;
import com.example.honeytag1.PublishActivity;
import com.example.my.LoginActivity;
import com.example.utils.Logger;
import com.example.utils.PublicStaticURL;
import com.example.view.RefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class FragmentLanMu extends Fragment implements OnItemClickListener, OnRefreshListener, OnClickListener {

	/**
	 * 栏目页面
	 */
	RefreshLayout fragment3_wipe_refresh_widget;
	private ListView mListView; // 列表
	List<ColumnDTO> fragment3_coltilist = new ArrayList<ColumnDTO>();
	ColumnAdapter columnAdapter;
	LinearLayout lm_wuwife, chongzhifanhui3;
	ImageButton lm_wuwife_btn, fragment3_jia;
	SpotsDialog spotsDialog;
	TextView fragment3_title;
	// Typeface font;
	TextView fragment3_biaoqinga;
	View view;
	ACache aCache; // 本地缓存

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null)
			view = inflater.inflate(R.layout.fragment_lanm, null);
		else {
			ViewGroup group = (ViewGroup) view.getParent();
			group.removeView(view);
		}
		init();
		return view;
	}

	/* 初始化 */
	private void init() {
		aCache = ACache.get(getActivity());
		spotsDialog = new SpotsDialog(getActivity());
		initView();
		fillAdapter();
		// ViewURL();
		cacheData();
	}

	/* 初始化控件 */
	private void initView() {
		// 初始化控件
		chongzhifanhui3 = (LinearLayout) view.findViewById(R.id.chongzhifanhui3);
		fragment3_jia = (ImageButton) view.findViewById(R.id.fragment3_jia);
		fragment3_biaoqinga = (TextView) view.findViewById(R.id.fragment3_biaoqinga);
		fragment3_title = (TextView) view.findViewById(R.id.fragment3_title);
		fragment3_wipe_refresh_widget = (RefreshLayout) view.findViewById(R.id.fragment3_wipe_refresh_widget); // 下拉刷新
		fragment3_wipe_refresh_widget.setColorScheme(R.color.lanse, R.color.huise, R.color.lanse, R.color.huise);
		fragment3_wipe_refresh_widget.setOnRefreshListener(this);
		fragment3_wipe_refresh_widget.setSize(SwipeRefreshLayout.DEFAULT);
		;
		fragment3_wipe_refresh_widget.setProgressViewEndTarget(true, 100);
		// 这句话是为了，第一次进入页面的时候显示加载进度条
		fragment3_wipe_refresh_widget.setProgressViewOffset(false, 0,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics()));

		mListView = (ListView) view.findViewById(R.id.fragment3_main_listview);
		mListView.setOnItemClickListener(this);

		lm_wuwife = (LinearLayout) view.findViewById(R.id.lm_wuwife);
		lm_wuwife_btn = (ImageButton) view.findViewById(R.id.lm_wuwife_btn);
		lm_wuwife_btn.setOnClickListener(this);
		fragment3_jia.setOnClickListener(this);
		chongzhifanhui3.setOnClickListener(this);

	}

	/* 填充适配器 */
	private void fillAdapter() {
		columnAdapter = new ColumnAdapter(getActivity());
		mListView.setAdapter(columnAdapter);

	}

	/**
	 * 离线缓存
	 */
	private void cacheData() {
		String cacheObject = aCache.getAsString("FRAGMENT3_CACHE");
		if (!("null").equals(cacheObject) && !("").equals(cacheObject) && cacheObject != null) {
			LogUtils.d("得到的离线数据是" + cacheObject);
			Gson gson = new Gson();
			Type type = new TypeToken<List<ColumnDTO>>() {
			}.getType();
			fragment3_coltilist = gson.fromJson(cacheObject, type);
			columnAdapter.reFresh(fragment3_coltilist);

		} else {
			LogUtils.d("离线数据为空" + cacheObject);
		}

	}

	public void ViewURL() {
		// AssetManager assetManager = getActivity().getAssets();
		// font = Typeface.createFromAsset(assetManager, "fonts/STXIHEI.TTF");
		fragment3_wipe_refresh_widget.setRefreshing(true);
		onRefresh();
	}

	/* 获取发现列表数据 */

	private void getColumnList(String str) {

		String aa = str;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				fragment3_wipe_refresh_widget.setRefreshing(false);
				// spotsDialog.dismiss();
				// lm_wuwife.setVisibility(View.VISIBLE);
				// lm_wuwife_btn.setVisibility(View.VISIBLE);
				SimpleHUD.showInfoMessage(getActivity(), getString(R.string.server_connect_failed));
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
						spotsDialog.dismiss();
						fragment3_wipe_refresh_widget.setRefreshing(false);
						String result = json.getString("result");
						aCache.put("FRAGMENT3_CACHE", result);
						if (result.equals("")) {
							fragment3_biaoqinga.setVisibility(View.VISIBLE);
							// mListView.setVisibility(View.GONE);
						} else {
							fragment3_biaoqinga.setVisibility(View.GONE);
							Gson gson = new Gson();
							Type type = new TypeToken<List<ColumnDTO>>() {
							}.getType();
							fragment3_coltilist = gson.fromJson(result, type);
							columnAdapter.reFresh(fragment3_coltilist);

						}

					}

				} catch (Exception e) {
					fragment3_wipe_refresh_widget.setRefreshing(false);
					SimpleHUD.showInfoMessage(getActivity(), getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}



	ColumnDTO columnDTO;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// ColumnDTO columnDTO = fragment3_coltilist.get(arg2);
		columnDTO = fragment3_coltilist.get(arg2);
		// 已读改变状态
		User mUser = MySharedPreference.readUser(getActivity());
		String hasnew = columnDTO.getHasnew();

		if (mUser != null) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("uid", mUser.getUid());
			params.addBodyParameter("tid", columnDTO.id);
			Loadings.readSection(PublicStaticURL.READSECTION, params);
		}

		Intent intent = new Intent(getActivity(), ColumnDetailsActivity.class);
		PublicStaticURL.ColumnTitle = columnDTO.getTitle();
		String id = columnDTO.getId();
		intent.putExtra("id", columnDTO.getId());
		startActivity(intent);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		User mUser = MySharedPreference.readUser(getActivity());
		if (mUser != null) {
			getColumnList(PublicStaticURL.Column + "&uid=" + mUser.getUid());
		} else {
			getColumnList(PublicStaticURL.Column);
			// fragment3_wipe_refresh_widget.setRefreshing(false);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lm_wuwife_btn:
			lm_wuwife.setVisibility(View.GONE);
			lm_wuwife_btn.setVisibility(View.GONE);
			spotsDialog.show();
			onRefresh();
			break;
		case R.id.fragment3_jia:
			if (PublicStaticURL.IsLogin == true) {
				startActivity(new Intent(getActivity(), PublishActivity.class));
			} else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;
		case R.id.chongzhifanhui3:
			if (PublicStaticURL.IsLogin == true) {
				startActivity(new Intent(getActivity(), PublishActivity.class));
			} else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		fragment3_wipe_refresh_widget.setRefreshing(true);
		onRefresh();
		MobclickAgent.onPageStart(getClass().toString()); // 统计页面，"MainScreen"为页面名称，可自定义
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().toString());
	}
}
