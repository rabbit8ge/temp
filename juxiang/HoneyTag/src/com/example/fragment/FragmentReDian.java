package com.example.fragment;

import info.wangchen.simplehud.SimpleHUD;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONObject;

import com.example.adapter.MainAdapter;
import com.example.dialog.CustomDialog;
import com.example.dialog.SpotsDialog;
import com.example.dto.ACache;
import com.example.dto.ArticleDTO;
import com.example.honeytag1.R;
import com.example.honeytag1.PublishActivity;
import com.example.honeytag1.TopicDetailsActivity;
import com.example.honeytag1.WebViewActivity;
import com.example.my.LoginActivity;
import com.example.utils.ExpressionUtil;
import com.example.utils.Logger;
import com.example.utils.PublicStaticURL;
import com.example.utils.VocieAnswersUtil;
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
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.Html.ImageGetter;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.waps.AppConnect;
import cn.waps.br;

@SuppressLint("NewApi")
public class FragmentReDian extends Fragment
		implements OnClickListener, OnLoadListener, OnRefreshListener, OnItemClickListener {

	// private HashMap<Integer, Integer> colorPosition = new HashMap<Integer,
	// Integer>();
	/**
	 * 发现页面
	 */
	RefreshLayout fragment2_wipe_refresh_widget;
	private ListView fragment2_main_listview; // 列表
	List<ArticleDTO> artilist = new ArrayList<ArticleDTO>();
	MainAdapter myadapter;
	SpotsDialog spotsDialog; // dialog
	private int fragment2_page = 1; // 当前页数
	private int fragment2_pageSize; // 总页数
	// private int fragment2_pos = 0; // 记录当前滑动位置
	private Animation animation;
	private LayoutAnimationController controller;
	private CustomDialog.Builder ibuilder;
	LinearLayout fragment2_wuwife, chongzhijia2;
	ImageButton fragment2_wuwife_btn;
	ImageView fragment2_jia;
	BroadcastReceiver receiver;
	// Typeface font;
	ACache aCache; // 本地缓存
	private int backColor[] = { R.color.youmi_main_a, R.color.youmi_main_b, R.color.youmi_main_c, R.color.youmi_main_d,
			R.color.youmi_main_e, R.color.youmi_main_f, R.color.youmi_main_g, R.color.youmi_main_h };
	private TextView fragment2_title;
	View view;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
			return view;
		}
		view = inflater.inflate(R.layout.fragment2, container,

				false);
		// 初始化控件
		aCache = ACache.get(getActivity());
		fragment2_jia = (ImageView) view.findViewById(R.id.fragment2_jia);
		fragment2_title = (TextView) view.findViewById(R.id.fragment2_title);
		fragment2_wipe_refresh_widget = (RefreshLayout) view.findViewById(R.id.fragment2_wipe_refresh_widget); // 下拉刷新
		fragment2_wipe_refresh_widget.setColorScheme(R.color.lanse, R.color.huise, R.color.lanse, R.color.huise);
		fragment2_wipe_refresh_widget.setOnRefreshListener(this);
		fragment2_wipe_refresh_widget.setOnLoadListener(this);
		fragment2_wipe_refresh_widget.setSize(SwipeRefreshLayout.DEFAULT);
		fragment2_wipe_refresh_widget.setProgressViewEndTarget(true, 100);
		// 这句话是为了，第一次进入页面的时候显示加载进度条
		fragment2_wipe_refresh_widget.setProgressViewOffset(false, 0,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics()));

		fragment2_main_listview = (ListView) view.findViewById(R.id.fragment2_main_listview);
		fragment2_main_listview.setOnItemClickListener(this);
		cacheData();
		fragment2_wuwife = (LinearLayout) view.findViewById(R.id.fragment2_wuwife);
		fragment2_wuwife_btn = (ImageButton) view.findViewById(R.id.fragment2_wuwife_btn);
		fragment2_wuwife_btn.setOnClickListener(this);
		fragment2_jia.setOnClickListener(this);
		spotsDialog = new SpotsDialog(getActivity());
		initReceiver();
		ViewURL();

		return view;
	}

	/**
	 * 离线缓存
	 */
	private void cacheData() {
		String cacheObject = aCache.getAsString("FRAGMENT2_CACHE");
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
				fragment2_main_listview.setAdapter(myadapter);
			} else {
				myadapter.notifyDataSetChanged();
			}
		} else {
			LogUtils.d("离线数据为空" + cacheObject);
		}

	}

	public void ViewURL() {
		// AssetManager assetManager = getActivity().getAssets();
		// font = Typeface.createFromAsset(assetManager, "fonts/STXIHEI.TTF");
		fragment2_wipe_refresh_widget.setRefreshing(true);
		onRefresh();
	}

	// 获取发现列表数据

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
				fragment2_wipe_refresh_widget.setRefreshing(false);
				// spotsDialog.dismiss();
				// fragment2_wuwife.setVisibility(View.VISIBLE);
				// fragment2_wuwife_btn.setVisibility(View.VISIBLE);
				SimpleHUD.showInfoMessage(getActivity(), getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("热点列表数据："+str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						spotsDialog.dismiss();
						fragment2_wipe_refresh_widget.setRefreshing(false);
						String result = json.getString("result");
						aCache.put("FRAGMENT2_CACHE", result);
						String page_count = json.getString("page_count");
						fragment2_pageSize = Integer.parseInt(page_count);
						Gson gson = new Gson();
						Type type = new TypeToken<List<ArticleDTO>>() {
						}.getType();
						artilist = gson.fromJson(result, type);

						if (myadapter == null) {
							if (artilist == null) {
								return;
							}
							myadapter = new MainAdapter(getActivity(), artilist);
							fragment2_main_listview.setAdapter(myadapter);
						} else {
							myadapter.setList(artilist);
							myadapter.notifyDataSetChanged();
						}

					}

				} catch (Exception e) {
					fragment2_wipe_refresh_widget.setRefreshing(false);
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
				fragment2_wipe_refresh_widget.setRefreshing(false);
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

						fragment2_wipe_refresh_widget.setRefreshing(false);
						String result = json.getString("result");
						String countmm = json.getString("count");
						Gson gson = new Gson();
						Type type = new TypeToken<List<ArticleDTO>>() {
						}.getType();
						artilist = gson.fromJson(result, type);
						// Toast.makeText(getActivity(),
						// "artilist:"+artilist.size(),
						// Toast.LENGTH_LONG).show();
						myadapter.notifyDataSetChanged();

					}

				} catch (Exception e) {
					fragment2_wipe_refresh_widget.setRefreshing(false);

					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	// 屏蔽文章

	// private void DELETE(String str) {
	// HttpUtils httpUtils = new HttpUtils();
	// httpUtils.configCurrentHttpCacheExpiry(0);
	// httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
	// @Override
	// public void onStart() {
	// super.onStart();
	//
	// }
	//
	// @Override
	// public void onFailure(
	// com.lidroid.xutils.exception.HttpException arg0, String arg1) {
	// // TODO Auto-generated method stub
	//
	// SimpleHUD.showInfoMessage(getActivity(),
	// getString(R.string.server_connect_failed));
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> responseInfo) {
	//
	// String str1 = responseInfo.result;// 接口返回的数据
	// Logger.i(str1);
	// JSONObject json;
	// try {
	// json = new JSONObject(str1);
	// String code = json.getString("code");
	// if ("2".equals(code)) {
	//
	// }
	// if ("3".equals(code)) {
	//
	// SimpleHUD.showInfoMessage(getActivity(), "屏蔽失败");
	// }
	//
	// } catch (Exception e) {
	// SimpleHUD.showInfoMessage(getActivity(),
	// getString(R.string.app_exception));
	// Log.e("message", "走catch了");
	// e.printStackTrace();
	// }
	// }
	// });
	//
	// }

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
				// fragment2_wipe_refresh_widget.setLoading(false);
				fragment2_wipe_refresh_widget.mListViewFooter.setVisibility(View.GONE);
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
							fragment2_wipe_refresh_widget.mListViewFooter.setVisibility(View.GONE);
						} else {
							String page_count = json.getString("page_count");
							Gson gson = new Gson();
							List<ArticleDTO> fragment2_list = new ArrayList<ArticleDTO>();
							Type type = new TypeToken<List<ArticleDTO>>() {
							}.getType();

							fragment2_list = gson.fromJson(result, type);
							int posi = artilist.size();
							// Toast.makeText(getActivity(), "posi:"+posi,
							// Toast.LENGTH_LONG).show();
							artilist.addAll(fragment2_list);
							myadapter.notifyDataSetChanged();
							fragment2_wipe_refresh_widget.mListViewFooter.setVisibility(View.GONE);
						}

					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});

	}

	/**
	 * 监听事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.fragment2_wuwife_btn:
			fragment2_wuwife.setVisibility(View.GONE);
			fragment2_wuwife_btn.setVisibility(View.GONE);
			spotsDialog.show();
			onRefresh();
			break;
		case R.id.fragment2_jia:
			if (PublicStaticURL.IsLogin == true) {
				startActivity(new Intent(getActivity(), PublishActivity.class));
			} else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}

			break;
		case R.id.chongzhijia2:
			if (PublicStaticURL.IsLogin == true) {
				startActivity(new Intent(getActivity(), PublishActivity.class));
			} else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}

			break;

		default:
			break;
		}
	}

	// 下拉刷新
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

		// if (PublicStaticURL.IsLogin == true) {
		// fragment2_page = 1;
		// SHUAXINARictl(PublicStaticURL.ZuiRELIST + "&p=" + fragment2_page +
		// "&uid=" + PublicStaticURL.userid);
		// } else {
		fragment2_page = 1;
		SHUAXINARictl(PublicStaticURL.ZuiRELIST + "&p=" + fragment2_page);
		// Toast.makeText(getActivity(), "fragment2_page:"+fragment2_page,
		// Toast.LENGTH_LONG).show();
		// }

	}

	// 上拉加载
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		// if (PublicStaticURL.IsLogin == true) {
		// fragment2_page++;
		// Reference(PublicStaticURL.ZuiRELIST + "&p=" + fragment2_page +
		// "&uid=" + PublicStaticURL.userid);
		// } else {
		fragment2_page++;
		Reference(PublicStaticURL.ZuiRELIST + "&p=" + fragment2_page);
		// }

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ArticleDTO articleDTO = artilist.get(arg2);
		Intent intent = new Intent();
		PublicStaticURL.pid = articleDTO.getPid(); // 将文章id存上 用调去详情
		startActivity(new Intent(getActivity(), TopicDetailsActivity.class));
	}

	private void initReceiver() {
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction() == "com.example.set.details") {
					LogUtils.d("收到广播");
					if (PublicStaticURL.IsLogin == true) {
						SHUAXINARictl1(
								PublicStaticURL.ZuiRELIST + "&per_number=999" + "&uid=" + PublicStaticURL.userid);
					}
				}
				if (intent.getAction() == "com.example.set.referesh") {
					onRefresh();
					fragment2_main_listview.setSelection(0);
				}
				if (intent.getAction() == "com.example.set.delete") {
					fragment2_page = 1;
					SHUAXINARictl(PublicStaticURL.ZuiRELIST + "&p=" + fragment2_page);
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
	public void onDestroy() {
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

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub

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
