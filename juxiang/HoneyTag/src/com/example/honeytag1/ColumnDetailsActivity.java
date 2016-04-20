package com.example.honeytag1;

import info.wangchen.simplehud.SimpleHUD;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONObject;

import com.example.baseactivity.BaseActivity;
import com.example.config.MySharedPreference;
import com.example.dialog.CustomDialog;
import com.example.dialog.SpotsDialog;
import com.example.dto.ACache;
import com.example.dto.ArticleDTO;
import com.example.dto.User;
import com.example.honeytag1.R;
import com.example.my.LoginActivity;
import com.example.setup.MyGradesActivity;
import com.example.setup.MyGradesRules;
import com.example.utils.ExpressionUtil;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.view.NavigationView;
import com.example.view.RefreshLayout;
import com.example.view.NavigationView.ClickCallback;
import com.example.view.NavigationView.ClickCallbackLeft;
import com.example.view.NavigationView.ClickCallbackRight;
import com.example.view.RefreshLayout.OnLoadListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 栏目子页面
 * 
 * @author Administrator
 * 
 */
public class ColumnDetailsActivity extends BaseActivity
		implements OnClickListener, OnItemClickListener, OnRefreshListener, OnLoadListener {

	private Context context;
	private View contentView;

	private TextView mTvLevel, mTvIdentity, mTvOtherSetting;// 顶部导航栏
	LinearLayout column_fanhui; // 返回按钮
	ImageButton column_btn;// 返回按钮
	private ListView column_listview;
	RefreshLayout cswipe_refresh_widget; // 下拉刷新
	List<ArticleDTO> columnlist = new ArrayList<ArticleDTO>();
	List<ArticleDTO> list = new ArrayList<ArticleDTO>();
	ColumnAdapter columnadapter;
	private Animation animation;
	private LayoutAnimationController controller;
	TextView colunm_details_title; // title
	LinearLayout cool_wuwife;
	ImageButton cool_wuwife_btn;
	SpotsDialog spotsDialog;
	// Typeface font, font1;
	TextView coulumn_biaoqinga;
	private ImageView mIvMore;
	ACache aCache; // 本地缓存
	BroadcastReceiver receiver;
	private int backColor[] = { R.color.youmi_main_a, R.color.youmi_main_b, R.color.youmi_main_c, R.color.youmi_main_d,
			R.color.youmi_main_e, R.color.youmi_main_f, R.color.youmi_main_g, R.color.youmi_main_h };

	private CustomDialog.Builder ibuilder;
	private PopupWindow popup;
	private TextView mask_topic_text;
	private TextView delete_text;
	private View delete_text_line;

	private String id;// 栏目id
	private int page;// 页数
	private int colunm_details_page = 10; // 当前页数
	private User mUser;
	String has_shield = "";// 1，代表屏蔽，0代表没屏蔽

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_column_details);
		MyApplication.getInstance().addActivity(this);
		context = this;
		mUser = MySharedPreference.readUser(context);

		id = getIntent().getStringExtra("id");

		if (0 == PublicStaticURL.columnFlag && PublicStaticURL.mTopicDTO != null) {// 从关注栏目的通知点击进入栏目列表
			// Toast.makeText(context, "niqueding", Toast.LENGTH_SHORT).show();
			id = PublicStaticURL.mTopicDTO.getTid();
		}
		// cacheData();
		initReceiver();
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void initView() {
		super.initView();

		initTopView();
		aCache = ACache.get(ColumnDetailsActivity.this);
		coulumn_biaoqinga = (TextView) findViewById(R.id.coulumn_biaoqinga);
		cswipe_refresh_widget = (RefreshLayout) findViewById(R.id.cswipe_refresh_widget);
		column_listview = (ListView) findViewById(R.id.column_listview);
		cool_wuwife = (LinearLayout) findViewById(R.id.cool_wuwife);
		cool_wuwife_btn = (ImageButton) findViewById(R.id.cool_wuwife_btn);
		spotsDialog = new SpotsDialog(ColumnDetailsActivity.this);

		mRelNavigationView.setTitle(PublicStaticURL.ColumnTitle + getString(R.string.topic));

		mRelNavigationView.getBackView().setVisibility(View.VISIBLE);
		mRelNavigationView.getRightTextView().setVisibility(View.INVISIBLE);
		mRelNavigationView.getRightTextView().setText("关注");
		mIvMore = mRelNavigationView.getRightView();
		mIvMore.setImageResource(R.drawable.more);
		mIvMore.setVisibility(View.VISIBLE);
		contentView = LayoutInflater.from(this).inflate(R.layout.popwindow, null);
		mask_topic_text = (TextView) contentView.findViewById(R.id.mask_topic_text);
		delete_text = (TextView) contentView.findViewById(R.id.delete_text);
		delete_text_line = (View) contentView.findViewById(R.id.delete_text_line);
		delete_text.setVisibility(View.GONE);
		delete_text_line.setVisibility(View.GONE);
		popup = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable());

		column_listview.setOnItemClickListener(this);
		cool_wuwife_btn.setOnClickListener(this);
		cswipe_refresh_widget.setOnRefreshListener(this);
		cswipe_refresh_widget.setOnLoadListener(this);
		cswipe_refresh_widget.setColorScheme(R.color.lanse, R.color.huise, R.color.lanse, R.color.huise);

		cswipe_refresh_widget.setSize(SwipeRefreshLayout.DEFAULT);
		cswipe_refresh_widget.setProgressViewEndTarget(true, 100);
		// 这句话是为了，第一次进入页面的时候显示加载进度条
		cswipe_refresh_widget.setProgressViewOffset(false, 0,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics()));
		// colunm_details_title.setText(PublicStaticURL.ColumnTitle +
		// getString(R.string.topic));

		if (mUser != null) {
			mIvRight.setVisibility(View.VISIBLE);
		} else {
			mIvRight.setVisibility(View.GONE);
		}

		mask_topic_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mUser != null) {

					if (has_shield.equals("0")) {
						columnBlock(PublicStaticURL.COLUMNBLOCK + "&uid=" + mUser.getUid() + "&tid=" + id);
					}
					if (has_shield.equals("1")) {
						columnOpen(PublicStaticURL.COLUMNOPEN + "&uid=" + mUser.getUid() + "&tid=" + id);
					}

				} else {
					startActivity(new Intent(ColumnDetailsActivity.this, LoginActivity.class));
				}

			}
		});

		mRelNavigationView.setClickCallbackLeft(new ClickCallbackLeft() {

			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(ColumnDetailsActivity.this);

			}
		});

		mRelNavigationView.setClickCallbackRight(new ClickCallbackRight() {

			@Override
			public void onRightClick() {
				if (mUser != null) {
					mIvRight.setVisibility(View.VISIBLE);
					popup.showAsDropDown(mIvMore);
				} else {
					mIvRight.setVisibility(View.GONE);
				}

			}
		});

		// mRelNavigationView.setClickCallback(new ClickCallback() {
		//
		// @Override
		// public void onRightClick() {
		// // T Toast.makeText(context, "aaaaa",
		// // Toast.LENGTH_SHORT).show();
		// popup.showAsDropDown(mIvMore);
		//
		// }
		//
		// @Override
		// public void onBackClick() {
		// MyApplication.getInstance().popActivity(ColumnDetailsActivity.this);
		//
		// }
		//
		// @Override
		// public void onRightTVClick() {
		//
		// }
		// });

	}

	/**
	 * 离线缓存
	 */
	private void cacheData() {
		String cacheObject = aCache.getAsString("COLUMN_CACHE");
		if (!("null").equals(cacheObject) && !("").equals(cacheObject) && cacheObject != null) {
			LogUtils.d("得到的离线数据是" + cacheObject);
			Gson gson = new Gson();
			Type type = new TypeToken<List<ArticleDTO>>() {
			}.getType();
			columnlist = gson.fromJson(cacheObject, type);

			if (columnadapter == null) {
				columnadapter = new ColumnAdapter(ColumnDetailsActivity.this);
				column_listview.setAdapter(columnadapter);
			} else {
				columnadapter.notifyDataSetChanged();
			}
		} else {
			LogUtils.d("离线数据为空" + cacheObject);
		}

	}

	public void ViewinitURL() {
		// AssetManager assetManager = ColumnDetailsActivity.this.getAssets();
		// font = Typeface.createFromAsset(assetManager, "fonts/STXIHEI.TTF");
		cswipe_refresh_widget.setRefreshing(true);
		onRefresh();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cool_wuwife_btn:
			cool_wuwife.setVisibility(View.GONE);
			cool_wuwife_btn.setVisibility(View.GONE);
			spotsDialog.show();
			onRefresh();
			break;

		}
	}

	/* 栏目屏蔽 */
	public void columnBlock(String str) {
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
				SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("屏蔽栏目消息：====" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						has_shield = "1";
						spotsDialog.dismiss();
						mask_topic_text.setText("打开提醒");
						SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, "不再接受此栏目的提醒");
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, "屏蔽失败");
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, "之前已经屏蔽过");
						has_shield = "1";
						spotsDialog.dismiss();
						mask_topic_text.setText("打开提醒");
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}

	/* 栏目打开 */
	public void columnOpen(String str) {
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
				SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("屏蔽栏目消息：====" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						spotsDialog.dismiss();
						has_shield = "0";
						mask_topic_text.setText("不再提醒");
						SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, "接受此栏目的提醒");
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, "打开失败");
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, "之前已经打开过");
						has_shield = "1";
						mask_topic_text.setText("打开提醒");
						SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, "不再接受此栏目的提醒");
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}

	// 刷新栏目列表数据

	private void getColumnData(String str) {

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
				cswipe_refresh_widget.setRefreshing(false);
				// spotsDialog.dismiss();
				// cool_wuwife.setVisibility(View.VISIBLE);
				// cool_wuwife_btn.setVisibility(View.VISIBLE);
				SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("栏目详情列表" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						has_shield = json.getString("has_shield");
						if (has_shield.equals("0")) {
							mask_topic_text.setText("不再提醒");
						}
						if (has_shield.equals("1")) {

							mask_topic_text.setText("开启提醒");
						}

						spotsDialog.dismiss();
						cswipe_refresh_widget.setRefreshing(false);
						String result = json.getString("result");
						aCache.put("COLUMN_CACHE", result);
						if (result.equals("")) {
							// column_listview.setVisibility(View.GONE);
							coulumn_biaoqinga.setVisibility(View.VISIBLE);
							cswipe_refresh_widget.setRefreshing(false);
						} else {
							coulumn_biaoqinga.setVisibility(View.GONE);
							Gson gson = new Gson();
							Type type = new TypeToken<List<ArticleDTO>>() {
							}.getType();
							columnlist = gson.fromJson(result, type);

							if (columnadapter == null) {
								columnadapter = new ColumnAdapter(ColumnDetailsActivity.this);
								column_listview.setAdapter(columnadapter);
							} else {
								columnadapter.notifyDataSetChanged();
							}
						}
					}

				} catch (Exception e) {
					cswipe_refresh_widget.setRefreshing(false);
					SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, getString(R.string.app_exception));
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
				cswipe_refresh_widget.setRefreshing(false);
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
						has_shield = json.getString("has_shield");
						if (has_shield.equals("0")) {
							mask_topic_text.setText("不再提醒");
						}
						if (has_shield.equals("1")) {

							mask_topic_text.setText("开启提醒");
						}

						cswipe_refresh_widget.setRefreshing(false);
						String result = json.getString("result");
						String countmm = json.getString("count");
						Gson gson = new Gson();
						Type type = new TypeToken<List<ArticleDTO>>() {
						}.getType();
						columnlist = gson.fromJson(result, type);
						columnadapter.notifyDataSetChanged();

					}

				} catch (Exception e) {
					cswipe_refresh_widget.setRefreshing(false);

					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	// 加载更多

	private void ColunmLoad(String str) {
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

				SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, getString(R.string.server_connect_failed));
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
						has_shield = json.getString("has_shield");
						if (has_shield.equals("0")) {
							mask_topic_text.setText("不再提醒");
						}
						if (has_shield.equals("1")) {

							mask_topic_text.setText("开启提醒");
						}
						String result = json.getString("result");
						if ("".equals(result)) {

							Toast.makeText(ColumnDetailsActivity.this, getString(R.string.not_more_data),
									Toast.LENGTH_SHORT).show();

							cswipe_refresh_widget.setLoading(false);
						} else {
							String page_count = json.getString("page_count");
							Gson gson = new Gson();
							Type type = new TypeToken<List<ArticleDTO>>() {
							}.getType();
							list = gson.fromJson(result, type);
							// for (int i = 0; i < list.size(); i++) {
							// list.get(i).setColors(backColor[(int) Math
							// .round(Math.random() * 7)]);
							// }
							columnlist.addAll(list);
							columnadapter.notifyDataSetChanged();
							cswipe_refresh_widget.setLoading(false);
						}

					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});

	}

	public class ColumnAdapter extends BaseAdapter {
		FinalBitmap finalBitmap;

		public ColumnAdapter(Context context) {
			super();
			finalBitmap = FinalBitmap.create(context);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return columnlist.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return columnlist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			HolderView holderView;
			if (convertView == null) {
				convertView = LayoutInflater.from(ColumnDetailsActivity.this).inflate(R.layout.fragment_item, null);
				holderView = new HolderView();
				holderView.lianjie_logo = (ImageView) convertView.findViewById(R.id.lianjie_logo);
				holderView.lianjie_wenzi = (TextView) convertView.findViewById(R.id.lianjie_wenzi);
				holderView.zan_b = (ImageView) convertView.findViewById(R.id.zan_b);
				holderView.pinglun_b = (ImageView) convertView.findViewById(R.id.pinglun_b);
				holderView.fragment_item_lin = (LinearLayout) convertView.findViewById(R.id.fragment_item_lin);
				holderView.fragment_txtname = (TextView) convertView // 链接地址
						.findViewById(R.id.fragment_txtname);
				// holderView.fragment_txtname.setTypeface(font);
				holderView.fragment_item_pinglun = (TextView) convertView // 评论
						.findViewById(R.id.fragment_item_pinglun);
				holderView.fragment_item_zan = (TextView) convertView // 赞
						.findViewById(R.id.fragment_item_zan);

				holderView.item_txt2 = (TextView) convertView // text2
						.findViewById(R.id.item_txt2);
				holderView.item_txt3 = (TextView) convertView // text3
						.findViewById(R.id.item_txt3);
				holderView.main_item_neirong = (TextView) convertView // 内容
						.findViewById(R.id.main_item_neirong);
				// holderView.main_item_neirong.setTypeface(font);
				holderView.fragment_item_image = (ImageView) convertView.findViewById(R.id.fragment_item_image); // 图片

				// holderView.item_btn = (TextView) convertView
				// .findViewById(R.id.item_btn); // 刪除按鈕

				convertView.setTag(holderView);

			} else {
				holderView = (HolderView) convertView.getTag();
			}
			// if (columnlist.get(position).getUrl_title().equals("")) {
			// holderView.lianjie_wenzi.setVisibility(View.GONE);
			// holderView.lianjie_logo.setVisibility(View.GONE);
			// } else {
			// holderView.lianjie_wenzi.setVisibility(View.VISIBLE);
			// holderView.lianjie_logo.setVisibility(View.GONE);
			// holderView.lianjie_wenzi.setText(Html.fromHtml("<u>"
			// + columnlist.get(position).getUrl_title() + "</u>"));
			//
			// if (columnlist.get(position).getUrl_ico().equals("no_pic")) {
			// holderView.lianjie_logo
			// .setBackgroundResource(R.drawable.ielogo);
			// } else {
			// finalBitmap.display(holderView.lianjie_logo, columnlist
			// .get(position).getUrl_ico());
			// }
			// }
			holderView.item_txt2.setText(columnlist.get(position).getCard_industry());
			holderView.item_txt3.setText(columnlist.get(position).getCard_title());

			if (columnlist.get(position).getCard_industry().equals("''")
					|| columnlist.get(position).getCard_industry() == null) {
				holderView.fragment_txtname.setText("自由职业");
			} else {
				holderView.fragment_txtname.setText(
						columnlist.get(position).getCard_industry() + " | " + columnlist.get(position).getCard_title());
			}
			// holderView.main_item_neirong.setTypeface(font1);
			holderView.main_item_neirong.setText(Transformation(columnlist.get(position).getContent()));

			holderView.main_item_neirong.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			SpannableStringBuilder sb = ExpressionUtil.prase(ColumnDetailsActivity.this, holderView.main_item_neirong,
					columnlist.get(position).getContent());// 对内容做处理
			holderView.main_item_neirong.setText(sb);
			// Linkify.addLinks(holderView.main_item_neirong, Linkify.ALL);

			// String txtname = columnlist.get(position).getContent() + "#"
			// + columnlist.get(position).getTitle() + "#";
			// SpannableString styledText = new SpannableString(txtname);
			// styledText.setSpan(new TextAppearanceSpan(
			// ColumnDetailsActivity.this, R.style.style0), columnlist
			// .get(position).getContent().length(), txtname.length(),
			// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// holderView.main_item_neirong.setText(styledText,
			// TextView.BufferType.SPANNABLE);

			if (TextUtils.isEmpty(columnlist.get(position).getPost_like())) {
				holderView.fragment_item_zan.setText("0");
			} else {
				holderView.fragment_item_zan.setText(columnlist.get(position).getPost_like());
			}

			// if (columnlist.get(position).getColor().equals("9")) {
			// convertView.setBackgroundResource(R.color.white);
			// } else if (columnlist.get(position).getColor().equals("1")) {
			// convertView.setBackgroundResource(R.color.youmi_main_d);
			// } else if (columnlist.get(position).getColor().equals("2")) {
			// convertView.setBackgroundResource(R.color.youmi_main_c);
			// } else if (columnlist.get(position).getColor().equals("3")) {
			// convertView.setBackgroundResource(R.color.youmi_main_g);
			// } else if (columnlist.get(position).getColor().equals("4")) {
			// convertView.setBackgroundResource(R.color.youmi_main_h);
			// } else if (columnlist.get(position).getColor().equals("5")) {
			// convertView.setBackgroundResource(R.color.youmi_main_f);
			// } else if (columnlist.get(position).getColor().equals("6")) {
			// convertView.setBackgroundResource(R.color.youmi_main_a);
			// } else if (columnlist.get(position).getColor().equals("7")) {
			// convertView.setBackgroundResource(R.color.youmi_main_e);
			// } else if (columnlist.get(position).getColor().equals("8")) {
			// convertView.setBackgroundResource(R.color.youmi_main_b);
			// }

			if (columnlist.get(position).getColor().equals("9")) {
				convertView.setBackgroundResource(R.color.white);
				holderView.pinglun_b.setBackgroundResource(R.drawable.chat1);
				holderView.zan_b.setBackgroundResource(R.drawable.collect1);
				holderView.fragment_item_pinglun.setTextColor(context.getResources().getColor(R.color.white_bg_text));
				holderView.fragment_item_zan.setTextColor(context.getResources().getColor(R.color.white_bg_text));
				holderView.fragment_txtname.setAlpha(0.7f);
			} else {
				holderView.pinglun_b.setBackgroundResource(R.drawable.chat);
				holderView.zan_b.setBackgroundResource(R.drawable.collect);
				holderView.fragment_item_pinglun.setTextColor(context.getResources().getColor(R.color.white));
				holderView.fragment_item_zan.setTextColor(context.getResources().getColor(R.color.white));
				holderView.fragment_txtname.setAlpha(0.5f);
				if (columnlist.get(position).getColor().equals("1")) {
					convertView.setBackgroundResource(R.color.youmi_main_a);
				} else if (columnlist.get(position).getColor().equals("2")) {

					convertView.setBackgroundResource(R.color.youmi_main_b);
				} else if (columnlist.get(position).getColor().equals("3")) {

					convertView.setBackgroundResource(R.color.youmi_main_c);
				} else if (columnlist.get(position).getColor().equals("4")) {

					convertView.setBackgroundResource(R.color.youmi_main_d);
				} else if (columnlist.get(position).getColor().equals("5")) {

					convertView.setBackgroundResource(R.color.youmi_main_e);
				} else if (columnlist.get(position).getColor().equals("6")) {

					convertView.setBackgroundResource(R.color.youmi_main_f);
				} else if (columnlist.get(position).getColor().equals("7")) {

					convertView.setBackgroundResource(R.color.youmi_main_g);
				} else if (columnlist.get(position).getColor().equals("8")) {

					convertView.setBackgroundResource(R.color.youmi_main_h);
				}
			}

			if (columnlist.get(position).getColor().equals("9")) {

				holderView.main_item_neirong.setTextColor(context.getResources().getColor(R.color.black));

			} else {
				holderView.main_item_neirong.setTextColor(context.getResources().getColor(R.color.white));
			}

			holderView.fragment_item_pinglun.setText(columnlist.get(position).getComment_count());
			if (columnlist.get(position).getAvatar().equals("no_pic")) {
				holderView.fragment_item_image.setVisibility(View.GONE);
			} else {
				holderView.fragment_item_image.setVisibility(View.VISIBLE);
				finalBitmap.display(holderView.fragment_item_image, columnlist.get(position).getAvatar());
			}
			holderView.main_item_neirong.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ArticleDTO articleDTO = columnlist.get(position);
					Intent intent = new Intent();
					PublicStaticURL.pid = articleDTO.getPid(); // 将文章id存上
																// 用调去详情
					PublicStaticURL.URL = articleDTO.getUrl();
					startActivity(new Intent(ColumnDetailsActivity.this, TopicDetailsActivity.class));

				}
			});

			// holderView.item_btn.setOnClickListener(new View.OnClickListener()
			// {
			//
			// @Override
			// public void onClick(View arg0) {
			// if (PublicStaticURL.IsLogin == true) {
			//
			// ibuilder = new CustomDialog.Builder(ColumnDetailsActivity.this);
			// ibuilder.setTitle(getString(R.string.notify));
			// ibuilder.setMessage("您确定屏蔽此话题吗?");
			// ibuilder.setPositiveButton(R.string.confirm,
			// new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface arg0,
			// int arg1) {
			// // TODO Auto-generated method stub
			// ArticleDTO articleDTO = columnlist
			// .get(position);
			// DELETE(PublicStaticURL.DELETE
			// + "&uid="
			// + PublicStaticURL.userid
			// + "&pid="
			// + columnlist.get(position)
			// .getPid());
			// columnlist.remove(position);
			// notifyDataSetChanged();
			//
			// arg0.dismiss();
			// }
			// });
			// ibuilder.setNegativeButton(R.string.cancel, null);
			// ibuilder.create().show();
			//
			// } else {
			//
			// Toast.makeText(ColumnDetailsActivity.this, "您还没有登录,不可以屏蔽文章",
			// Toast.LENGTH_SHORT).show();
			// }
			// }
			// });
			return convertView;
		}
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
	// @Override
	// public void onFailure(
	// com.lidroid.xutils.exception.HttpException arg0, String arg1) {
	// // TODO Auto-generated method stub
	//
	// SimpleHUD.showInfoMessage(ColumnDetailsActivity.this,
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
	// SimpleHUD.showInfoMessage(ColumnDetailsActivity.this, "屏蔽失败");
	// }
	//
	// } catch (Exception e) {
	// SimpleHUD.showInfoMessage(ColumnDetailsActivity.this,
	// getString(R.string.app_exception));
	// Log.e("message", "走catch了");
	// e.printStackTrace();
	// }
	// }
	// });

	// }

	// 定义view缓存对象
	static class HolderView {
		TextView fragment_itme_time, fragment_txtname, item_btn, fragment_item_pinglun, fragment_item_zan, item_txt1,
				item_txt2, item_txt3, item_title, main_item_neirong, lianjie_wenzi;
		ImageView fragment_item_image, zan_b, pinglun_b, lianjie_logo;
		LinearLayout fragment_item_lin;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ArticleDTO articleDTO = columnlist.get(arg2);
		Intent intent = new Intent();
		PublicStaticURL.pid = articleDTO.getPid(); // 将文章id存上 用调去详情
		PublicStaticURL.URL = articleDTO.getUrl();
		startActivity(new Intent(ColumnDetailsActivity.this, TopicDetailsActivity.class));
	}

	@Override
	public void onRefresh() {

		page = 1;

		if (mUser != null) {
			getColumnData(PublicStaticURL.ColumnDetails + "&p=" + page + "&per_number=" + colunm_details_page + "&uid="
					+ mUser.getUid() + "&id=" + id + "&title=" + PublicStaticURL.ColumnTitle);
		} else {
			getColumnData(PublicStaticURL.ColumnDetails + "&p=" + page + "&per_number=" + colunm_details_page + "&uid="
					+ "" + "&id=" + id + "&title=" + PublicStaticURL.ColumnTitle);
		}

	}

	// 字符串截取
	private String Transformation(String s) {
		String str = s.replace("Φ", "");
		String str1 = str.replace("Φ", "");

		return str1;
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

		page = page + 1;

		if (mUser != null) {
			ColunmLoad(PublicStaticURL.ColumnDetails + "&p=" + page + "&per_number=" + colunm_details_page + "&uid="
					+ mUser.getUid() + "&id=" + id + "&title=" + PublicStaticURL.ColumnTitle);
		} else {
			ColunmLoad(PublicStaticURL.ColumnDetails + "&p=" + page + "&per_number=" + colunm_details_page + "&uid="
					+ "" + "&id=" + id + "&title=" + PublicStaticURL.ColumnTitle);
		}
	}

	private void initReceiver() {
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction() == "com.example.set.details") {
					LogUtils.d("收到广播");
					if (PublicStaticURL.IsLogin == true) {
						SHUAXINARictl1(PublicStaticURL.ColumnDetails + "&p=" + colunm_details_page + "&title="
								+ PublicStaticURL.ColumnTitle);

					}
				}
			}
		};
		// 注册广播接收者
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction("com.example.set.details");
		registerReceiver(receiver, mFilter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		umengResume(this, getClass().toString());
		ViewinitURL();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		umengPause(this, getClass().toString());
	}

}
