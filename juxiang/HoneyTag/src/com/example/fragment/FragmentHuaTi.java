package com.example.fragment;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import info.wangchen.simplehud.SimpleHUD;


/**
 * 我的话题
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class FragmentHuaTi extends Fragment
		implements OnRefreshListener, OnItemClickListener, OnLoadListener, OnClickListener {

	RefreshLayout myde_wipe_refresh_widget;
	private ListView myde_main_listview; // 列表
	List<ArticleDTO> myde_artilist = new ArrayList<ArticleDTO>();
	List<ArticleDTO> myde_list = new ArrayList<ArticleDTO>();
	SpotsDialog spotsDialog; // dialog
	private int myde_page = 1; // 当前页数
	private int myde_pageSize; // 总页数
	private int myde_pos = 0; // 记录当前滑动位置
	MainAdapter myadapter;
	private Animation animation;
	private BroadcastReceiver receiver;
	LinearLayout ht_wuwife;
	ImageButton ht_wuwife_btn;
	TextView huati_layout_biaoqinga;
	ACache aCache; // 本地缓存
	View view;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null)

			view = inflater.inflate(R.layout.fragment_huati, null);
		else {
			ViewGroup group = (ViewGroup) view.getParent();
			group.removeView(view);
		}
		aCache = ACache.get(getActivity());
		myde_wipe_refresh_widget = (RefreshLayout) view.findViewById(R.id.myde_wipe_refresh_widget);
		myde_wipe_refresh_widget.setColorScheme(R.color.lanse, R.color.huise, R.color.lanse, R.color.huise);
		myde_wipe_refresh_widget.setOnRefreshListener(this);
		myde_wipe_refresh_widget.setOnLoadListener(this);
		myde_wipe_refresh_widget.setSize(SwipeRefreshLayout.DEFAULT);
		;
		myde_wipe_refresh_widget.setProgressViewEndTarget(true, 100);
		// 这句话是为了，第一次进入页面的时候显示加载进度条
		myde_wipe_refresh_widget.setProgressViewOffset(false, 0,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics()));
		huati_layout_biaoqinga = (TextView) view.findViewById(R.id.huati_layout_biaoqinga);
		myde_main_listview = (ListView) view.findViewById(R.id.myde_main_listview);
		myde_main_listview.setOnItemClickListener(this);
		ht_wuwife = (LinearLayout) view.findViewById(R.id.ht_wuwife);
		ht_wuwife_btn = (ImageButton) view.findViewById(R.id.ht_wuwife_btn);
		ht_wuwife_btn.setOnClickListener(this);
		spotsDialog = new SpotsDialog(getActivity());
		ViewURL();
		initReceiver();

		return view;
	}

	/**
	 * 离线缓存
	 */
	private void cacheData() {
		String cacheObject = aCache.getAsString("FRAGMENTHT_CACHE");
		if (!("null").equals(cacheObject) && !("").equals(cacheObject) && cacheObject != null) {
			LogUtils.d("得到的离线数据是" + cacheObject);
			Gson gson = new Gson();
			Type type = new TypeToken<List<ArticleDTO>>() {
			}.getType();
			myde_artilist = gson.fromJson(cacheObject, type);
			if (myadapter == null) {
				if (myde_artilist == null) {
					return;
				}
				myadapter = new MainAdapter(getActivity(), myde_artilist);
				myde_main_listview.setAdapter(myadapter);
			} else {
				if (myde_artilist == null) {
					return;
				}
				myadapter.setList(myde_artilist);
				myadapter.notifyDataSetChanged();
			}
		} else {
			LogUtils.d("离线数据为空" + cacheObject);
		}

	}

	public void ViewURL() {
		// AssetManager assetManager = getActivity().getAssets();
		// font = Typeface.createFromAsset(assetManager, "fonts/STXIHEI.TTF");
		myde_wipe_refresh_widget.setRefreshing(true);
		onRefresh();
	}

	// 刷新列表数据

	private void SHUAXINMYDE(String str) {
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
				myde_wipe_refresh_widget.setRefreshing(false);
				spotsDialog.dismiss();
				ht_wuwife.setVisibility(View.VISIBLE);
				ht_wuwife_btn.setVisibility(View.VISIBLE);
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
						myde_wipe_refresh_widget.setRefreshing(false);
						String result = json.getString("result");
						aCache.put("FRAGMENTHT_CACHE", result);
						if (result.equals("")) {
							huati_layout_biaoqinga.setVisibility(View.VISIBLE);
							myde_artilist.clear();
							if (myadapter == null) {
								myadapter = new MainAdapter(getActivity(), myde_artilist);
								myde_main_listview.setAdapter(myadapter);
							} else {
								myadapter.setList(myde_artilist);
								myadapter.notifyDataSetChanged();
							}
						} else {

							huati_layout_biaoqinga.setVisibility(View.GONE);
							String page_count = json.getString("page_count");
							myde_pageSize = Integer.parseInt(page_count);
							Gson gson = new Gson();
							Type type = new TypeToken<List<ArticleDTO>>() {
							}.getType();
							myde_artilist = gson.fromJson(result, type);
							if (myadapter == null) {
								myadapter = new MainAdapter(getActivity(), myde_artilist);
								myde_main_listview.setAdapter(myadapter);
							} else {
								myadapter.setList(myde_artilist);
								myadapter.notifyDataSetChanged();
							}

						}

					}

				} catch (Exception e) {
					myde_wipe_refresh_widget.setRefreshing(false);
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
				myde_wipe_refresh_widget.setRefreshing(false);
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

						myde_wipe_refresh_widget.setRefreshing(false);
						String result = json.getString("result");
						// String countmm = json.getString("count");
						Gson gson = new Gson();
						Type type = new TypeToken<List<ArticleDTO>>() {
						}.getType();
						myde_artilist = gson.fromJson(result, type);
						if (myadapter == null) {
							if (myde_artilist == null) {
								return;
							}
							myadapter = new MainAdapter(getActivity(), myde_artilist);
							myde_main_listview.setAdapter(myadapter);
						} else {
							if (myde_artilist == null) {
								return;
							}
							myadapter.setList(myde_artilist);
							myadapter.notifyDataSetChanged();
						}
					}

				} catch (Exception e) {
					myde_wipe_refresh_widget.setRefreshing(false);

					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	// 加载更多

	private void Fragment2Load(String str) {
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
							myde_wipe_refresh_widget.setLoading(false);
						} else {
							String page_count = json.getString("page_count");
							Gson gson = new Gson();
							Type type = new TypeToken<List<ArticleDTO>>() {
							}.getType();
							myde_list = gson.fromJson(result, type);
							// for (int i = 0; i < myde_list.size(); i++) {
							// myde_list.get(i)
							// .setColors(
							// backColor[(int) Math.round(Math
							// .random() * 7)]);
							// }
							myde_artilist.addAll(myde_list);
							myadapter.notifyDataSetChanged();
							myde_wipe_refresh_widget.setLoading(false);
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
		ArticleDTO articleDTO = myde_artilist.get(arg2);
		Intent intent = new Intent();
		PublicStaticURL.pid = articleDTO.getPid(); // 将文章id存上 用调去详情
		startActivity(new Intent(getActivity(), TopicDetailsActivity.class));

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		myde_page = 1;
		SHUAXINMYDE(PublicStaticURL.MYDE + "&uid=" + PublicStaticURL.userid + "&p=" + myde_page);
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		myde_page++;
		Fragment2Load(PublicStaticURL.MYDE + "&uid=" + PublicStaticURL.userid + "&p=" + myde_page);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ht_wuwife_btn:
			ht_wuwife.setVisibility(View.GONE);
			ht_wuwife_btn.setVisibility(View.GONE);
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
						SHUAXINARictl1(PublicStaticURL.MYDE + "&uid=" + PublicStaticURL.userid + "&per_number=999");
					}
				}
				if (intent.getAction() == "com.example.set.referesh") {
					LogUtils.d("收到广播");
					if (PublicStaticURL.IsLogin == true) {
						onRefresh();
					}
				}
				if (intent.getAction() == "com.example.set.delete") {
					myde_page = 1;
					SHUAXINARictl1(PublicStaticURL.MYDE + "&p=" + myde_page + "&uid=" + PublicStaticURL.userid);
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
