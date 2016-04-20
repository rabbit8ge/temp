package com.example.fragment;

import info.wangchen.simplehud.SimpleHUD;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import com.example.adapter.MainAdapter;
import com.example.dialog.SpotsDialog;
import com.example.dto.ACache;
import com.example.dto.ArticleDTO;
import com.example.honeytag1.R;
import com.example.honeytag1.TopicDetailsActivity;
import com.example.utils.Logger;
import com.example.utils.PublicStaticURL;
import com.example.view.RefreshLayout;
import com.example.view.RefreshLayout.OnLoadListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我的关注
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class FragmentGuanZhu extends Fragment
		implements OnItemClickListener, OnRefreshListener, OnLoadListener, OnClickListener {

	RefreshLayout gz_wipe_refresh_widget;
	private ListView gz_main_listview; // 列表
	List<ArticleDTO> gz_artilist = new ArrayList<ArticleDTO>();
	List<ArticleDTO> gz_list = null;
	SpotsDialog spotsDialog; // dialog
	private int gz_page = 1; // 当前页数
	private int gz_pageSize; // 总页数
	private int gz_pos = 0; // 记录当前滑动位置
	MainAdapter gzadapter;
	private Animation animation;
	LinearLayout gz_wuwife;
	ImageButton gz_wuwife_btn;
	private BroadcastReceiver receiver;
	TextView layout_biaoqinga;
	ACache aCache; // 本地缓存
	View view;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null)

			view = inflater.inflate(R.layout.fragment_guanzhu, null);
		else {
			ViewGroup group = (ViewGroup) view.getParent();
			group.removeView(view);
		}
		aCache = ACache.get(getActivity());
		gz_wipe_refresh_widget = (RefreshLayout) view.findViewById(R.id.gz_wipe_refresh_widget);
		gz_wipe_refresh_widget.setColorScheme(R.color.lanse, R.color.huise, R.color.lanse, R.color.huise);
		gz_wipe_refresh_widget.setOnRefreshListener(this);
		gz_wipe_refresh_widget.setOnLoadListener(this);
		gz_wipe_refresh_widget.setSize(SwipeRefreshLayout.DEFAULT);
		;
		gz_wipe_refresh_widget.setProgressViewEndTarget(true, 100);
		// 这句话是为了，第一次进入页面的时候显示加载进度条
		gz_wipe_refresh_widget.setProgressViewOffset(false, 0,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics()));
		layout_biaoqinga = (TextView) view.findViewById(R.id.layout_biaoqinga);
		gz_main_listview = (ListView) view.findViewById(R.id.gz_main_listview);
		gz_main_listview.setOnItemClickListener(this);
		cacheData();
		gz_wuwife = (LinearLayout) view.findViewById(R.id.gz_wuwife);
		gz_wuwife_btn = (ImageButton) view.findViewById(R.id.gz_wuwife_btn);
		gz_wuwife_btn.setOnClickListener(this);
		spotsDialog = new SpotsDialog(getActivity());
		ViewURL();
		initReceiver();
		return view;
	}

	public void ViewURL() {
		// AssetManager assetManager = getActivity().getAssets();
		// font = Typeface.createFromAsset(assetManager, "fonts/STXIHEI.TTF");
		gz_wipe_refresh_widget.setRefreshing(true);
		onRefresh();
	}

	/**
	 * 离线缓存
	 */
	private void cacheData() {
		String cacheObject = aCache.getAsString("FRAGMENTGZ_CACHE");
		if (!("null").equals(cacheObject) && !("").equals(cacheObject) && cacheObject != null) {
			LogUtils.d("得到的离线数据是" + cacheObject);
			Gson gson = new Gson();
			Type type = new TypeToken<List<ArticleDTO>>() {
			}.getType();
			gz_artilist = gson.fromJson(cacheObject, type);
			if (gzadapter == null) {
				if (gz_artilist == null) {
					return;
				}
				gzadapter = new MainAdapter(getActivity(), gz_artilist);
				gz_main_listview.setAdapter(gzadapter);
			} else {
				if (gz_artilist == null) {
					return;
				}
				gzadapter.setList(gz_artilist);
				gzadapter.notifyDataSetChanged();
			}
		} else {
			LogUtils.d("离线数据为空" + cacheObject);
		}

	}

	// 刷新列表数据

	private void SHUAXINGZ(String str) {
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
				gz_wipe_refresh_widget.setRefreshing(false);
				// spotsDialog.dismiss();
				// gz_wuwife.setVisibility(View.VISIBLE);
				// gz_wuwife_btn.setVisibility(View.VISIBLE);
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
						gz_wipe_refresh_widget.setRefreshing(false);
						String result = json.getString("result");
						aCache.put("FRAGMENTGZ_CACHE", result);
						if (result.equals("")) {
							layout_biaoqinga.setVisibility(View.VISIBLE);
							gz_artilist.clear();
							if (gzadapter == null) {
								gzadapter = new MainAdapter(getActivity(), gz_artilist);
								gz_main_listview.setAdapter(gzadapter);
							} else {
								gzadapter.setList(gz_artilist);
								gzadapter.notifyDataSetChanged();
							}
						} else {
							layout_biaoqinga.setVisibility(View.GONE);
							String page_count = json.getString("page_count");
							gz_pageSize = Integer.parseInt(page_count);
							Gson gson = new Gson();
							Type type = new TypeToken<List<ArticleDTO>>() {
							}.getType();
							gz_artilist = gson.fromJson(result, type);
							if (gzadapter == null) {
								if (gz_artilist == null) {
									return;
								}
								gzadapter = new MainAdapter(getActivity(), gz_artilist);
								gz_main_listview.setAdapter(gzadapter);
							} else {
								if (gz_artilist == null) {
									return;
								}
								gzadapter.setList(gz_artilist);
								gzadapter.notifyDataSetChanged();
							}

						}
					}

				} catch (Exception e) {
					gz_wipe_refresh_widget.setRefreshing(false);
					// SimpleHUD.showInfoMessage(getActivity(),
					// getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	private void SHUAXINARictl1(String str) {

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
				gz_wipe_refresh_widget.setRefreshing(false);
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

						gz_wipe_refresh_widget.setRefreshing(false);
						String result = json.getString("result");
						// String countmm = json.getString("count");
						Gson gson = new Gson();
						Type type = new TypeToken<List<ArticleDTO>>() {
						}.getType();
						gz_artilist = gson.fromJson(result, type);
						gzadapter.notifyDataSetChanged();

					}

				} catch (Exception e) {
					gz_wipe_refresh_widget.setRefreshing(false);

					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	// 加载更多

	private void FragmentGZLoad(String str) {
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

						String result = json.getString("result");
						if ("".equals(result)) {
							Toast.makeText(getActivity(), getString(R.string.not_more_data), Toast.LENGTH_SHORT).show();
							gz_wipe_refresh_widget.setLoading(false);
						} else {
							String page_count = json.getString("page_count");
							Gson gson = new Gson();
							Type type = new TypeToken<List<ArticleDTO>>() {
							}.getType();
							gz_list = gson.fromJson(result, type);
							// for (int i = 0; i < gz_list.size(); i++) {
							// gz_list.get(i)
							// .setColors(
							// backColor[(int) Math.round(Math
							// .random() * 7)]);
							// }
							gz_artilist.addAll(gz_list);
							gzadapter.notifyDataSetChanged();
							gz_wipe_refresh_widget.setLoading(false);
						}

					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ArticleDTO articleDTO = gz_artilist.get(arg2);
		PublicStaticURL.pid = articleDTO.getPid(); // 将文章id存上 用调去详情
		Intent myhuati = new Intent(getActivity(), TopicDetailsActivity.class);
		myhuati.putExtra("flag", "GZ");
		getActivity().startActivity(myhuati);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		gz_page = 1;
		SHUAXINGZ(PublicStaticURL.GUANZHU + "&uid=" + PublicStaticURL.userid + "&p=" + gz_page);
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		gz_page++;
		FragmentGZLoad(PublicStaticURL.GUANZHU + "&uid=" + PublicStaticURL.userid + "&p=" + gz_page);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.gz_wuwife_btn:
			gz_wuwife.setVisibility(View.GONE);
			gz_wuwife_btn.setVisibility(View.GONE);
			spotsDialog.show();
			onRefresh();

			break;
		}
	}

	private void initReceiver() {
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction() == "com.example.set.details") {
					LogUtils.d("收到广播");
					if (PublicStaticURL.IsLogin == true) {
						SHUAXINARictl1(PublicStaticURL.GUANZHU + "&uid=" + PublicStaticURL.userid + "&per_number=999");

					}
				}
				if (intent.getAction() == "com.example.set.referesh") {
					LogUtils.d("收到广播");
					if (PublicStaticURL.IsLogin == true) {
						onRefresh();
					}
				}
				if (intent.getAction() == "com.example.set.delete") {
					gz_page = 1;
					SHUAXINGZ(PublicStaticURL.GUANZHU + "&p=" + gz_page + "&uid=" + PublicStaticURL.userid);
				}
			}
		};
		// 注册广播接收者
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction("com.example.set.details");
		mFilter.addAction("com.example.set.referesh");
		mFilter.addAction("com.example.set.delete");
		getActivity().registerReceiver(receiver, mFilter);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub

		if (receiver != null) {
			getActivity().unregisterReceiver(receiver);
		}
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().toString()); // 统计页面，"MainScreen"为页面名称，可自定义
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().toString());
	}
}
