package com.example.fragment;

import info.wangchen.simplehud.SimpleHUD;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.MainAdapter;
import com.example.config.MySharedPreference;
import com.example.dialog.CustomDialog;
import com.example.dialog.SpotsDialog;
import com.example.dto.ACache;
import com.example.dto.ArticleDTO;
import com.example.dto.User;
import com.example.honeytag1.R;
import com.example.honeytag1.PublishActivity;
import com.example.honeytag1.RemindActivity;
import com.example.honeytag1.TopicDetailsActivity;

import com.example.honeytag1.WelcomeActivity;
import com.example.http.Loadings;
import com.example.my.LoginActivity;
import com.example.utils.Logger;
import com.example.utils.PublicStaticURL;
import com.example.utils.ShareUtils;
import com.example.utils.Utils;
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

@SuppressLint("NewApi")
public class FragmentSheQu extends Fragment
		implements OnClickListener, OnRefreshListener, OnItemClickListener, OnLoadListener {

	/**
	 * 首页页面
	 */
	RefreshLayout mSwipeRefreshWidget;
	private ListView main_listview; // 列表
	private ImageView main_jia; // 发布按钮
	private ImageView main_xiaoxi; // 消息按钮
	MainAdapter myadapter;
	public static TextView main_tab_unread_tv; // 显示消息数量
	public TextView fragemnt_title;
	List<ArticleDTO> artilist = new ArrayList<ArticleDTO>();
	List<ArticleDTO> list = new ArrayList<ArticleDTO>();
	SpotsDialog spotsDialog; // dialog
	private int page = 1; // 当前页数
	private int pageSize; // 总页数
	// private int pos=0; //记录当前滑动位置
	private CustomDialog.Builder ibuilder;
	private LayoutAnimationController controller;
	// Typeface font; // 设置字体
	String mata;
	int position1;
	LinearLayout wuwife;
	ImageButton wuwife_btn;
	int tempcount = 0;
	int tempcount1 = 0;
	TextView count_text;
	BroadcastReceiver receiver;
	private DataUI bateui = new DataUI();
	private BroadcastReceiver bReceiver;
	ACache aCache; // 本地缓存
	View view;
	//private View fragment_one;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
			return view;
		}
		view = inflater.inflate(R.layout.fragment, container, false);
		aCache = ACache.get(getActivity());
		User user = MySharedPreference.readUser(getActivity());
		if (user != null) {
			if (("").equals(ShareUtils.getSharePreStr(getActivity(), "countss"))) {

			} else {
				tempcount = Integer.parseInt(ShareUtils.getSharePreStr(getActivity(), "countss"));

			}
		}
		if (PublicStaticURL.IsLogin == false) {
			if (("").equals(ShareUtils.getSharePreStr(getActivity(), "countssfalse"))) {

			} else {
				tempcount = Integer.parseInt(ShareUtils.getSharePreStr(getActivity(), "countssfalse"));
			}
		}
		//fragment_one = inflater.inflate(R.layout.fragment_one, null, false);
		mSwipeRefreshWidget = (RefreshLayout) view.findViewById(R.id.swipe_refresh_widget); // 下拉刷新
		mSwipeRefreshWidget.setColorScheme(R.color.lanse, R.color.huise, R.color.lanse, R.color.huise);
		mSwipeRefreshWidget.setOnRefreshListener(this);
		mSwipeRefreshWidget.setOnLoadListener(this);
		mSwipeRefreshWidget.setSize(SwipeRefreshLayout.DEFAULT);
		mSwipeRefreshWidget.setProgressViewEndTarget(true, 100);
		// 这句话是为了，第一次进入页面的时候显示加载进度条
		mSwipeRefreshWidget.setProgressViewOffset(false, 0,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics()));

		wuwife = (LinearLayout) view.findViewById(R.id.wuwife);
		wuwife_btn = (ImageButton) view.findViewById(R.id.wuwife_btn);
		wuwife_btn.setOnClickListener(this);
		count_text = (TextView) view.findViewById(R.id.count_text);

		main_listview = (ListView) view.findViewById(R.id.main_listview);
		main_listview.setOnItemClickListener(this);
		cacheData();
		mSwipeRefreshWidget.setRefreshing(true);
		spotsDialog = new SpotsDialog(getActivity());
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.example.set.statue");
		getActivity().registerReceiver(bateui, filter);
		ViewinitURL();
		initReceiver();
		return view;
	}

	/**
	 * 离线缓存
	 */
	private void cacheData() {
		String cacheObject = aCache.getAsString("FRAGMENT_CACHE");
		if (!("null").equals(cacheObject) && !("").equals(cacheObject) && cacheObject != null) {
			LogUtils.d("得到的离线数据是" + cacheObject);
			Gson gson = new Gson();
			Type type = new TypeToken<List<ArticleDTO>>() {
			}.getType();
			artilist = gson.fromJson(cacheObject, type);
			if (myadapter == null) {
				if (artilist == null) {
					return;
				}
				myadapter = new MainAdapter(getActivity(), artilist);
				main_listview.setAdapter(myadapter);
			} else {
				if (artilist == null) {
					return;
				}
				myadapter.setList(artilist);
				myadapter.notifyDataSetChanged();
			}
		} else {
			LogUtils.d("离线数据为空" + cacheObject);
		}

	}

	public void ViewinitURL() {
		// /**
		// * 设置字体
		// */
		// AssetManager assetManager = getActivity().getAssets();
		// font = Typeface.createFromAsset(assetManager, "fonts/STXIHEI.TTF");

		mSwipeRefreshWidget.setRefreshing(true);
		if (Utils.isNetworkAvailable(getActivity())) {
			onRefresh();
		} else {
			mSwipeRefreshWidget.setRefreshing(false);
			SimpleHUD.showInfoMessage(getActivity(), getString(R.string.wuwangluo));
		}

	}

	// 刷新主页列表数据

	private void SHUAXINARictl(String str) {

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
				mSwipeRefreshWidget.setRefreshing(false);
				spotsDialog.dismiss();
				// wuwife.setVisibility(View.VISIBLE);
				// wuwife_btn.setVisibility(View.VISIBLE);
				SimpleHUD.showInfoMessage(getActivity(), getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("主页数据列表" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						spotsDialog.dismiss();
						wuwife.setVisibility(View.GONE);
						wuwife_btn.setVisibility(View.GONE);
						mSwipeRefreshWidget.setRefreshing(false);
						String result = json.getString("result");
						aCache.put("FRAGMENT_CACHE", result);
						String page_count = json.getString("page_count");
						String countmm = json.getString("count");
						LogUtils.e("----countmm------" + countmm);
						// 登录状态下提醒新消息数量
						if (PublicStaticURL.IsLogin == true) {
							Loadings.getCount(PublicStaticURL.ZONGmessage + "&uid=" + PublicStaticURL.userid);
							if (("").equals(ShareUtils.getSharePreStr(getActivity(), "countss"))) {
								ShareUtils.putSharePre(getActivity(), "countss", countmm);
							} else {
								tempcount = Integer.parseInt(ShareUtils.getSharePreStr(getActivity(), "countss"));

							}
							int visviableInt = 0;
							if (tempcount <= 0) {

							} else {

								visviableInt = Integer.parseInt(countmm) - tempcount;

								if (visviableInt < 0) {

								} else if (visviableInt == 0) {

									count_text.setVisibility(View.VISIBLE);
									count_text.setText(R.string.refresh_notice);
									Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),
											R.anim.textview_enter);
									count_text.startAnimation(mAnimation);
									new Handler().postDelayed(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub

											Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),
													R.anim.textview_exit);
											count_text.startAnimation(mAnimation);
											count_text.setVisibility(View.GONE);
										}
									}, 3000);

								} else {
									count_text.setVisibility(View.VISIBLE);
									count_text.setText(getString(R.string.new_topic) + visviableInt
											+ getString(R.string.new_topic_tiao));
									Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),
											R.anim.textview_enter);
									count_text.startAnimation(mAnimation);
									new Handler().postDelayed(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub

											Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),
													R.anim.textview_exit);
											count_text.startAnimation(mAnimation);
											count_text.setVisibility(View.GONE);
										}
									}, 3000);

								}
								ShareUtils.clearSharePreA(getActivity());
								ShareUtils.putSharePre(getActivity(), "countss", countmm);
							}

						}
						// 未登录状态下提醒新消息数量
						if (PublicStaticURL.IsLogin == false) {
							if (("").equals(ShareUtils.getSharePreStr(getActivity(), "countssfalse"))) {

								ShareUtils.putSharePre(getActivity(), "countssfalse", countmm);

							} else {
								tempcount1 = Integer.parseInt(ShareUtils.getSharePreStr(getActivity(), "countssfalse"));
							}
							int visviableInt1 = 0;
							if (tempcount1 <= 0) {

							} else {
								visviableInt1 = Integer.parseInt(countmm) - tempcount1;

								if (visviableInt1 < 0) {

								} else if (visviableInt1 == 0) {

									count_text.setVisibility(View.VISIBLE);
									count_text.setText(getString(R.string.refresh_notice));
									Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),
											R.anim.textview_enter);
									count_text.startAnimation(mAnimation);
									new Handler().postDelayed(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub

											Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),
													R.anim.textview_exit);
											count_text.startAnimation(mAnimation);
											count_text.setVisibility(View.GONE);
										}
									}, 3000);

								} else {
									count_text.setVisibility(View.VISIBLE);
									count_text.setText(getString(R.string.new_topic) + visviableInt1
											+ getString(R.string.new_topic_tiao));
									Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),
											R.anim.textview_enter);
									count_text.startAnimation(mAnimation);
									new Handler().postDelayed(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub

											Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),
													R.anim.textview_exit);
											count_text.startAnimation(mAnimation);
											count_text.setVisibility(View.GONE);
										}
									}, 3000);

								}
								ShareUtils.clearSharePreA(getActivity());

								ShareUtils.putSharePre(getActivity(), "countssfalse", countmm);
							}

						}
						pageSize = Integer.parseInt(page_count);
						Gson gson = new Gson();
						Type type = new TypeToken<List<ArticleDTO>>() {
						}.getType();
						artilist = gson.fromJson(result, type);
						if (myadapter == null) {
							if (artilist == null) {
								return;
							}
							myadapter = new MainAdapter(getActivity(), artilist);
							main_listview.setAdapter(myadapter);
						} else {
							if (artilist == null) {
								return;
							}
							myadapter.setList(artilist);
							myadapter.notifyDataSetChanged();
						}

					}

				} catch (Exception e) {
					mSwipeRefreshWidget.setRefreshing(false);
					SimpleHUD.showInfoMessage(getActivity(), getString(R.string.app_exception));
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
				mSwipeRefreshWidget.setRefreshing(false);
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

						mSwipeRefreshWidget.setRefreshing(false);
						String result = json.getString("result");
						String countmm = json.getString("count");
						Gson gson = new Gson();
						Type type = new TypeToken<List<ArticleDTO>>() {
						}.getType();
						artilist = gson.fromJson(result, type);
						Logger.i("size：", artilist.size() + "");
						myadapter.setList(artilist);
						myadapter.notifyDataSetChanged();

					}

				} catch (Exception e) {
					mSwipeRefreshWidget.setRefreshing(false);

					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	// 加载更多

	private void Reference(String str) {
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
				mSwipeRefreshWidget.setLoading(false);
				SimpleHUD.showInfoMessage(getActivity(), getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("加载更多" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {

						String result = json.getString("result");
						if ("".equals(result)) {
							Toast.makeText(getActivity(), getString(R.string.not_more_data), Toast.LENGTH_SHORT).show();
							mSwipeRefreshWidget.setLoading(false);
						} else {
							String page_count = json.getString("page_count");
							Gson gson = new Gson();
							Type type = new TypeToken<List<ArticleDTO>>() {
							}.getType();
							list = gson.fromJson(result, type);
							artilist.addAll(list);
							myadapter.notifyDataSetChanged();
							mSwipeRefreshWidget.setLoading(false);
						}

					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});

	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.main_xiaoxi) {
			if (PublicStaticURL.IsLogin == true) {
				startActivity(new Intent(getActivity(), RemindActivity.class));

			} else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
		}
		if (v.getId() == R.id.main_jia) {
			if (PublicStaticURL.IsLogin == false) {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			} else {
				startActivity(new Intent(getActivity(), PublishActivity.class));

			}

		}
		if (v.getId() == R.id.wuwife_btn) {
			wuwife.setVisibility(View.GONE);
			wuwife_btn.setVisibility(View.GONE);

			spotsDialog.show();
			onRefresh();
		}
		if (v.getId() == R.id.xiaoxi_l) {
			if (PublicStaticURL.IsLogin == true) {
				// Intent intent=new Intent(getActivity(),
				// RemindActivity.class);
				// intent.putExtra(name, value)
				startActivity(new Intent(getActivity(), RemindActivity.class));

			} else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
		}
	}

	/**
	 * 下拉刷新监听
	 */
	@Override
	public void onRefresh() {
		if (PublicStaticURL.IsLogin == true) {
			page = 1;
			SHUAXINARictl(PublicStaticURL.ARTICLE + "&p=" + page + "&uid=" + PublicStaticURL.userid);
		} else {
			page = 1;
			SHUAXINARictl(PublicStaticURL.ARTICLE + "&p=" + page);
		}

	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

		if (Utils.isNetworkAvailable(getActivity())) {
			if (PublicStaticURL.IsLogin == true) {
				page++;
				Reference(PublicStaticURL.ARTICLE + "&p=" + page + "&uid=" + PublicStaticURL.userid);
				mSwipeRefreshWidget.setRefreshing(false);
				Logger.i("加载更多！！！！！！！！");
			} else {
				page++;
				Reference(PublicStaticURL.ARTICLE + "&p=" + page);
				mSwipeRefreshWidget.setRefreshing(false);
				Logger.i("加载更多！！！！！！！！");
			}
		} else {
			mSwipeRefreshWidget.mListViewFooter.setVisibility(View.GONE);
			SimpleHUD.showInfoMessage(getActivity(), getString(R.string.wuwangluo));
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ArticleDTO articleDTO = artilist.get(arg2);
		PublicStaticURL.pid = articleDTO.getPid(); // 将文章id存上 用调取详情
		PublicStaticURL.URL = articleDTO.getUrl();
		startActivity(new Intent(getActivity(), TopicDetailsActivity.class));
	}

	class DataUI extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (arg1.getAction().equals("com.example.set.statue")) {
				if (artilist.size() == 0) {

				} else {
					myadapter.notifyDataSetChanged();
				}
			}
		}

	}

	private void initReceiver() {
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction() == "com.example.set.details") {
					LogUtils.d("收到广播");
					if (PublicStaticURL.IsLogin == true) {
						SHUAXINARictl1(PublicStaticURL.ARTICLE + "&per_number=999" + "&uid=" + PublicStaticURL.userid);
					}
				}
				if (intent.getAction() == "com.example.set.referesh") {
					onRefresh();
				}
				if (intent.getAction() == "com.example.set.delete") {
					page = 1;
					SHUAXINARictl1(PublicStaticURL.ARTICLE + "&p=" + page + "&uid=" + PublicStaticURL.userid);
					SimpleHUD.showInfoMessage(getActivity(), getString(R.string.already_delete));
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

		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		if (bReceiver != null) {
			getActivity().unregisterReceiver(bateui);
		}
		if (receiver != null) {
			try {
				getActivity().unregisterReceiver(receiver);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}

	private Drawable getCachedDrawable() {
		// TODO Auto-generated method stub
		return null;
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
