package com.example.fragment;

import info.wangchen.simplehud.SimpleHUD;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONObject;

import com.example.adapter.MainAdapter;
import com.example.dialog.SpotsDialog;
import com.example.dto.ArticleDTO;
import com.example.honeytag1.R;
import com.example.honeytag1.TopicDetailsActivity;
import com.example.honeytag1.WebViewActivity;
import com.example.utils.ExpressionUtil;
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
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.Html.ImageGetter;
import android.text.style.TextAppearanceSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 参与的话题
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class FragmentParticipateIn extends Fragment
		implements OnItemClickListener, OnRefreshListener, OnLoadListener, OnClickListener {

	RefreshLayout part_wipe_refresh_widget;
	private ListView part_main_listview; // 列表
	List<ArticleDTO> part_artilist = new ArrayList<ArticleDTO>();
	List<ArticleDTO> part_list = new ArrayList<ArticleDTO>();
	SpotsDialog spotsDialog; // dialog
	private int part_page = 1; // 当前页数
	private int part_pageSize; // 总页数
	private int part_pos = 0; // 记录当前滑动位置
	private MainAdapter myadapter;
	LinearLayout cy_wuwife;
	ImageButton cy_wuwife_btn;
	// Typeface font;
	TextView sc_layout_biaoqinga;
	private BroadcastReceiver receiver;
	private int backColor[] = { R.color.youmi_main_a, R.color.youmi_main_b, R.color.youmi_main_c, R.color.youmi_main_d,
			R.color.youmi_main_e, R.color.youmi_main_f, R.color.youmi_main_g, R.color.youmi_main_h };
	View view;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null)

			view = inflater.inflate(R.layout.fragment_shoucang, null);
		else {
			ViewGroup group = (ViewGroup) view.getParent();
			group.removeView(view);
		}
		part_wipe_refresh_widget = (RefreshLayout) view.findViewById(R.id.part_wipe_refresh_widget);
		part_wipe_refresh_widget.setColorScheme(R.color.lanse, R.color.huise, R.color.lanse, R.color.huise);
		part_wipe_refresh_widget.setOnRefreshListener(this);
		part_wipe_refresh_widget.setOnLoadListener(this);
		part_wipe_refresh_widget.setSize(SwipeRefreshLayout.DEFAULT);
		;
		part_wipe_refresh_widget.setProgressViewEndTarget(true, 100);
		// 这句话是为了，第一次进入页面的时候显示加载进度条
		part_wipe_refresh_widget.setProgressViewOffset(false, 0,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics()));
		sc_layout_biaoqinga = (TextView) view.findViewById(R.id.sc_layout_biaoqinga);
		part_main_listview = (ListView) view.findViewById(R.id.part_main_listview);
		part_main_listview.setOnItemClickListener(this);
		cy_wuwife = (LinearLayout) view.findViewById(R.id.cy_wuwife);
		cy_wuwife_btn = (ImageButton) view.findViewById(R.id.cy_wuwife_btn);
		cy_wuwife_btn.setOnClickListener(this);
		spotsDialog = new SpotsDialog(getActivity());
		ViewURL();
		initReceiver();
		return view;
	}

	public void ViewURL() {
		// AssetManager assetManager = getActivity().getAssets();
		// font = Typeface.createFromAsset(assetManager, "fonts/STXIHEI.TTF");

		part_wipe_refresh_widget.setRefreshing(true);
		onRefresh();
	}

	// 刷新列表数据

	private void SHUAXINPART(String str) {
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
				part_wipe_refresh_widget.setRefreshing(false);
				spotsDialog.dismiss();
				cy_wuwife.setVisibility(View.VISIBLE);
				cy_wuwife_btn.setVisibility(View.VISIBLE);
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
						part_wipe_refresh_widget.setRefreshing(false);
						String result = json.getString("result");
						if (result.equals("")) {
							sc_layout_biaoqinga.setVisibility(View.VISIBLE);
							// part_main_listview.setVisibility(View.GONE);
							part_artilist.clear();
							if (myadapter == null) {
								myadapter = new MainAdapter(getActivity(), part_artilist);
								part_main_listview.setAdapter(myadapter);
							} else {
								myadapter.setList(part_artilist);
								myadapter.notifyDataSetChanged();
							}
						} else {
							sc_layout_biaoqinga.setVisibility(View.GONE);
							String page_count = json.getString("page_count");
							part_pageSize = Integer.parseInt(page_count);
							Gson gson = new Gson();
							Type type = new TypeToken<List<ArticleDTO>>() {
							}.getType();
							part_artilist = gson.fromJson(result, type);
							if (myadapter == null) {
								myadapter = new MainAdapter(getActivity(), part_artilist);
								part_main_listview.setAdapter(myadapter);
							} else {
								myadapter.setList(part_artilist);
								myadapter.notifyDataSetChanged();
							}
						}
					}

				} catch (Exception e) {
					part_wipe_refresh_widget.setRefreshing(false);
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
				part_wipe_refresh_widget.setRefreshing(false);
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

						part_wipe_refresh_widget.setRefreshing(false);
						String result = json.getString("result");
						Gson gson = new Gson();
						Type type = new TypeToken<List<ArticleDTO>>() {
						}.getType();
						part_artilist = gson.fromJson(result, type);
						myadapter.notifyDataSetChanged();

					}

				} catch (Exception e) {
					part_wipe_refresh_widget.setRefreshing(false);

					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	// 加载更多

	private void FragmentCY2Load(String str) {
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
							part_wipe_refresh_widget.setLoading(false);
						} else {
							String page_count = json.getString("page_count");
							Gson gson = new Gson();
							Type type = new TypeToken<List<ArticleDTO>>() {
							}.getType();
							part_list = gson.fromJson(result, type);
							// for (int i = 0; i < part_list.size(); i++) {
							// part_list.get(i)
							// .setColors(
							// backColor[(int) Math.round(Math
							// .random() * 7)]);
							// }
							part_artilist.addAll(part_list);
							myadapter.notifyDataSetChanged();
							part_wipe_refresh_widget.setLoading(false);
						}

					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		part_page = 1;
		SHUAXINPART(PublicStaticURL.PART + "&uid=" + PublicStaticURL.userid + "&p=" + part_page);
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		part_page++;
		FragmentCY2Load(PublicStaticURL.PART + "&uid=" + PublicStaticURL.userid + "&p=" + part_page);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ArticleDTO articleDTO = part_artilist.get(arg2);
		PublicStaticURL.pid = articleDTO.getPid(); // 将文章id存上 用调去详情
		Intent myhuati = new Intent(getActivity(), TopicDetailsActivity.class);
		myhuati.putExtra("flag", "WD");
		startActivity(myhuati);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cy_wuwife_btn:
			cy_wuwife.setVisibility(View.GONE);
			cy_wuwife_btn.setVisibility(View.GONE);
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
						SHUAXINARictl1(PublicStaticURL.PART + "&uid=" + PublicStaticURL.userid + "&per_number=999");

					}
				}
				if (intent.getAction() == "com.example.set.referesh") {
					LogUtils.d("收到广播");
					if (PublicStaticURL.IsLogin == true) {
						onRefresh();
					}
				}
				if (intent.getAction() == "com.example.set.delete") {
					part_page = 1;
					SHUAXINPART(PublicStaticURL.PART + "&p=" + part_page + "&uid=" + PublicStaticURL.userid);
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
