package com.example.honeytag1;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dialog.ActionSheetDialog;
import com.example.dialog.SpotsDialog;
import com.example.dialog.ActionSheetDialog.OnSheetItemClickListener;
import com.example.dialog.ActionSheetDialog.SheetItemColor;
import com.example.dialog.CustomDialog;
import com.example.dto.CommentDTO;
import com.example.dto.Reply;
import com.example.utils.ExpressionUtil;
import com.example.utils.FaceVPAdapter;
import com.example.utils.JsonParser;
import com.example.utils.KeyBoardUtils;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.utils.Utils;
import com.example.view.CircleImageView2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import info.wangchen.simplehud.SimpleHUD;
import net.tsz.afinal.FinalBitmap;
import zhy.com.highlight.util.L;

public class TopicDetailsActivity extends BaseActivity
		implements OnClickListener, OnItemClickListener, OnTouchListener {
	/**
	 * 首页文章列表详情
	 * 
	 * @author Administrator Modify by DQP
	 */

	// private LinearLayout details_yindao;// 引导
	private LinearLayout details_morel;// 更多
	private LinearLayout chat_yuying;// 语音输入
	private LinearLayout linear_pinlun;// 评论导航
	private TextView details_name, details_content, // 昵称，职位,标题
			details_neirong, details_time, // 内容， 时间
			comment_all, comment_surprise, comment_my, comment_guanzhu;// 全部，精彩，我的，关注
	private ImageView details_image, // 话题内容图片
			comment_all_image, comment_surprise_image, comment_my_image, comment_guanzhu_image, // 向下的箭头
			details_triangle, details_triangle2;// 三角形

	private Button details_fasong;// 评论按钮
	private EditText details_pl_et; // 评论输入框
	private CircleImageView2 details_headimage;
	// 表情图标每页6列4行
	private int columns = 6;
	private int rows = 4;
	// 每页显示的表情view
	private List<View> views = new ArrayList<View>();
	// 表情列表
	private List<String> staticFacesList;
	/** 表情布局 */
	private LinearLayout chat_face_container;
	/** 工具布局 */
	private LinearLayout chat_more;
	/** microphone */
	private LinearLayout chat_microphone;
	/** 表情按钮 */
	// private ImageView face_details;
	/** 表情布局指示器 */
	private LinearLayout mDotsLayout;
	/** 承载表情的viewpage */
	private ViewPager mViewPager;
	private LayoutInflater inflater;
	private RelativeLayout main_root_details;
	private SpotsDialog spotsDialog;
	private ListView detalis_list;// 全部
	private int qbpage = 1;
	private int jcpage = 1;
	private int wdpage = 1;
	private int gzpage = 1;
	private String avatar;
	private String content; // 评论内容
	private String details_uid;
	private List<CommentDTO> list = new ArrayList<CommentDTO>();
	private List<CommentDTO> plist = new ArrayList<CommentDTO>();
	private List<CommentDTO> commlist = new ArrayList<CommentDTO>(); // //全部评论
	private List<CommentDTO> jccommlist = new ArrayList<CommentDTO>();// //精彩评论
	private List<CommentDTO> mycommlist = new ArrayList<CommentDTO>(); // //我的评论
	private List<CommentDTO> gzcommlist = new ArrayList<CommentDTO>(); // //关注评论
	private String qname;
	private String shield;// 屏蔽话题
	public boolean QUAN = false; // 限制只能@一次
	private TopicAdapter mainyadapter;
	private boolean isToDown = true;// 箭头是否向下
	private boolean isMore = true;// 是否能加载更多
	private boolean isLoading;// 是否正在加载中
	private boolean isBottom;// 是否到了底部
	private boolean ismIating = true;// 是否正在录音
	private boolean isUserclick;// 是否是用户点击了全部评论
	private RotateAnimation animation = null;
	private static int PL = 1;
	private static int QBPL = 1;
	private static int JCPL = 2;
	private static int WDPL = 3;
	private static int GZPL = 4;
	private ImageView comment_all_image2;
	private ImageView comment_surprise_image2;
	private ImageView comment_my_image2;
	private ImageView comment_guanzhu_image2;
	private TextView comment_all2;
	private TextView comment_surprise2;
	private TextView comment_my2;
	private TextView comment_guanzhu2;
	private View footerView;
	private int mYDown;
	private int mLastY;
	private int mTouchSlop;
	private SpannableStringBuilder sb;
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	private PopupWindow popup;
	private View contentView;
	private ImageView details_microphone;
	private AlertDialog shareDialog;
	private int microarray[] = { R.drawable.micro0, R.drawable.micro1, R.drawable.micro2, R.drawable.micro3,
			R.drawable.micro4, R.drawable.micro5, R.drawable.micro6 };
	private TextView mask_topic_text;
	private TextView delete_text;
	private String mIntentResult;// 进入话题详情页传值
	private View topic_foot_nomore;
	private View topic_load_before;
	private LinearLayout rem_wuwife;
	private ImageButton top_wuwife_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		new Handler().postDelayed(new Runnable() {// 延缓表情的加载，使进入话题详情页时时间短，不会卡顿
			@Override
			public void run() {
				initFacesList();
			}
		}, 500);
	}

	@Override
	public void loadXml() {
		setContentView(R.layout.activity_topic_details);
		Config.dialog = new SpotsDialog(TopicDetailsActivity.this);// 设置友盟分享时的dialog
		SpeechUtility.createUtility(TopicDetailsActivity.this, "appid=" + getString(R.string.xunfei_appid));// 讯飞语音初始化
		mIat = SpeechRecognizer.createRecognizer(TopicDetailsActivity.this, mInitListener);
		View details_head_lv = View.inflate(this, R.layout.details_head_lv, null);// 头部内容1
		View pinglun_daohang = View.inflate(this, R.layout.pinglun_daohang, null);// 头部内容2
		topic_foot_nomore = View.inflate(this, R.layout.topic_foot_nomore, null);// 尾部内容1
		topic_load_before = View.inflate(this, R.layout.topic_load_before, null);// 尾部内容3
		footerView = LayoutInflater.from(this).inflate(R.layout.listview_footer, null);
		// details_yindao = (LinearLayout) findViewById(R.id.details_yindao);
		linear_pinlun = (LinearLayout) findViewById(R.id.linear_pinlun);
		mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
		main_root_details = (RelativeLayout) findViewById(R.id.main_root_details);
		chat_face_container = (LinearLayout) findViewById(R.id.chat_face_container);
		chat_more = (LinearLayout) findViewById(R.id.chat_more);
		chat_microphone = (LinearLayout) findViewById(R.id.chat_microphone);
		chat_yuying = (LinearLayout) findViewById(R.id.chat_yuying);
		mViewPager = (ViewPager) this.findViewById(R.id.face_viewpager);
		findViewById(R.id.details_fanhui).setOnClickListener(this);
		details_morel = (LinearLayout) findViewById(R.id.details_morel);
		findViewById(R.id.details_btn).setOnClickListener(this);
		findViewById(R.id.setup_jia).setOnClickListener(this);
		details_name = (TextView) details_head_lv.findViewById(R.id.details_name);
		details_content = (TextView) details_head_lv.findViewById(R.id.details_content);
		details_neirong = (TextView) details_head_lv.findViewById(R.id.details_neirong);
		details_time = (TextView) details_head_lv.findViewById(R.id.details_time);
		rem_wuwife = (LinearLayout) findViewById(R.id.rem_wuwife);
		top_wuwife_btn = (ImageButton) findViewById(R.id.top_wuwife_btn);
		comment_all = (TextView) findViewById(R.id.comment_all);
		comment_surprise = (TextView) findViewById(R.id.comment_surprise);
		comment_my = (TextView) findViewById(R.id.comment_my);
		comment_guanzhu = (TextView) findViewById(R.id.comment_guanzhu);
		comment_all2 = (TextView) pinglun_daohang.findViewById(R.id.comment_all);
		comment_surprise2 = (TextView) pinglun_daohang.findViewById(R.id.comment_surprise);
		comment_my2 = (TextView) pinglun_daohang.findViewById(R.id.comment_my);
		comment_guanzhu2 = (TextView) pinglun_daohang.findViewById(R.id.comment_guanzhu);
		comment_all_image = (ImageView) findViewById(R.id.comment_all_image);
		comment_surprise_image = (ImageView) findViewById(R.id.comment_surprise_image);
		comment_my_image = (ImageView) findViewById(R.id.comment_my_image);
		comment_guanzhu_image = (ImageView) findViewById(R.id.comment_guanzhu_image);
		comment_all_image2 = (ImageView) pinglun_daohang.findViewById(R.id.comment_all_image);
		comment_surprise_image2 = (ImageView) pinglun_daohang.findViewById(R.id.comment_surprise_image);
		comment_my_image2 = (ImageView) pinglun_daohang.findViewById(R.id.comment_my_image);
		comment_guanzhu_image2 = (ImageView) pinglun_daohang.findViewById(R.id.comment_guanzhu_image);
		details_microphone = (ImageView) findViewById(R.id.details_microphone);
		contentView = LayoutInflater.from(this).inflate(R.layout.popwindow, null);
		mask_topic_text = (TextView) contentView.findViewById(R.id.mask_topic_text);
		delete_text = (TextView) contentView.findViewById(R.id.delete_text);
		findViewById(R.id.face_details).setOnClickListener(this);
		details_triangle = (ImageView) findViewById(R.id.details_triangle);
		details_triangle2 = (ImageView) pinglun_daohang.findViewById(R.id.details_triangle);
		details_image = (ImageView) details_head_lv.findViewById(R.id.details_image);
		details_headimage = (CircleImageView2) details_head_lv.findViewById(R.id.details_headimage);
		details_fasong = (Button) findViewById(R.id.details_fasong);
		details_pl_et = (EditText) findViewById(R.id.details_pl_et);
		detalis_list = (ListView) findViewById(R.id.detalis_list);
		detalis_list.addHeaderView(details_head_lv);// 添加头部
		detalis_list.addHeaderView(pinglun_daohang);// 添加头部
		initView();
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		popup = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable());
		GradientDrawable myGrad = (GradientDrawable) details_fasong.getBackground();// 默认显示button的颜色为，获取shape中button的背景颜色
		myGrad.setColor(getResources().getColor(R.color.pinlun_true));
	}

	private void initView() {
		mIntentResult = getIntent().getStringExtra("flag");
		Log.i("是否为null", "" + (mIntentResult == null));
		if (mIntentResult != null) {
			if (mIntentResult.equals("GZ")) {
				selectGZ();
			} else if (mIntentResult.equals("WD")) {
				selectWD();
			} else if (mIntentResult.equals("TX")) {
				// Toast.makeText(this,
				// PublicStaticURL.remind_cid,Toast.LENGTH_SHORT).show();
				loadLatterComment(PublicStaticURL.TAGPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + taghPage + "&uid="
						+ PublicStaticURL.userid + "&cid=" + PublicStaticURL.remind_cid + "&direction=DOWN");
			} else if (mIntentResult.equals("TX_WD")) {
				loadLatterComment(PublicStaticURL.TAGPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + taghPage + "&uid="
						+ PublicStaticURL.userid + "&cid=" + PublicStaticURL.remindto_cid + "&direction=DOWN");
			} else {
				getQBCommentList(PublicStaticURL.PINGLUNLIST + "&pid=" + PublicStaticURL.pid + "&p=" + 1 + "&uid="
						+ PublicStaticURL.userid + "&sort=2");// sort=2列表默认正序（发布评论最早的在列表的第一个），sort=1列表默认逆序（发布评论最晚的在列表的第一个）
			}
		} else {
			getQBCommentList(PublicStaticURL.PINGLUNLIST + "&pid=" + PublicStaticURL.pid + "&p=" + 1 + "&uid="
					+ PublicStaticURL.userid + "&sort=2");

		}
		getDetails(PublicStaticURL.DETAILS + "&pid=" + PublicStaticURL.pid + "&from_uid=" + PublicStaticURL.userid);

	}

	@Override
	public void loadData() {
		detalis_list.setOnItemClickListener(this);
		details_fasong.setOnClickListener(this);
		comment_all.setOnClickListener(this);
		comment_surprise.setOnClickListener(this);
		comment_my.setOnClickListener(this);
		comment_guanzhu.setOnClickListener(this);
		comment_all2.setOnClickListener(this);
		comment_surprise2.setOnClickListener(this);
		comment_my2.setOnClickListener(this);
		comment_guanzhu2.setOnClickListener(this);
		detalis_list.setOnTouchListener(this);
		details_image.setOnClickListener(this);
		chat_yuying.setOnClickListener(this);
		details_microphone.setOnClickListener(this);
		details_morel.setOnClickListener(this);
		mask_topic_text.setOnClickListener(this);
		delete_text.setOnClickListener(this);
		topic_load_before.setOnClickListener(this);
		topic_foot_nomore.setOnClickListener(this);
		top_wuwife_btn.setOnClickListener(this);
		/* 输入框获取焦点关闭表情 */
		details_pl_et.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean isFocus) {
				// TODO Auto-generated method stub
				if (PublicStaticURL.IsLogin == true) {
					if (isFocus) {
						Logger.i(isFocus + "isfocus");
						if (chat_face_container.getVisibility() == View.VISIBLE) {
							chat_face_container.setVisibility(View.GONE);
						}
						if (chat_more.getVisibility() == View.VISIBLE) {
							chat_more.setVisibility(View.GONE);
						}
						if (chat_microphone.getVisibility() == View.VISIBLE) {
							chat_microphone.setVisibility(View.GONE);
						}
					}
				} else {
					Utils.toLogin(TopicDetailsActivity.this);
				}
			}
		});
		main_root_details.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				main_root_details.requestFocus();
				if (chat_face_container.getVisibility() == View.VISIBLE) {
					chat_face_container.setVisibility(View.GONE);
				}
				if (chat_more.getVisibility() == View.VISIBLE) {
					chat_more.setVisibility(View.GONE);
				}
				if (chat_microphone.getVisibility() == View.VISIBLE) {
					chat_microphone.setVisibility(View.GONE);
				}

				return false;
			}
		});

		details_pl_et.setOnClickListener(this);
		details_pl_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() == 0) {
					details_fasong.setVisibility(View.GONE);
				} else {
					details_fasong.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		detalis_list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当滑动到底部时
				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
					isBottom = true;
				} else {
					isBottom = false;
				}
				if (isPullUp() && isMore && isBottom && !isLoading) {
					scrollBottomState();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if (firstVisibleItem >= 1) {
					linear_pinlun.setVisibility(View.VISIBLE);
				} else {
					linear_pinlun.setVisibility(View.GONE);
				}

			}
		});

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		final int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// 按下
			mYDown = (int) event.getRawY();
			chat_face_container.setVisibility(View.GONE);
			chat_microphone.setVisibility(View.GONE);
			chat_more.setVisibility(View.GONE);
			KeyBoardUtils.closeInputMethod(TopicDetailsActivity.this, main_root_details);
			break;

		case MotionEvent.ACTION_MOVE:
			// 移动
			mLastY = (int) event.getRawY();
			break;

		case MotionEvent.ACTION_UP:
			// 抬起
			mLastY = (int) event.getRawY();
			break;
		default:
			break;
		}
		return false;
	}
	// 获取文章详情

	private void getDetails(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(TopicDetailsActivity.this);
				spotsDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showErrorMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				spotsDialog.dismiss();
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("话题详情" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);
						details_uid = json1.getString("uid");
						shield = json1.getString("has_shield");
						String content = json1.getString("content");
						String date = json1.getString("date");
						avatar = json1.getString("avatar");
						String personerAvatar = json1.getString("personalavatar");
						Logger.i(personerAvatar);
						String card_title = json1.getString("card_title");
						String card_industry = json1.getString("card_industry");
						String nice_name = json1.getString("nicename");
						if (!personerAvatar.equals("no_pic")) {
							FinalBitmap finalBitmap = null;
							finalBitmap = FinalBitmap.create(TopicDetailsActivity.this);
							finalBitmap.display(details_headimage, personerAvatar);
						}

						if (details_uid.equals(PublicStaticURL.userid)) {
							delete_text.setText("删除");
						} else {
							delete_text.setText("举报");
						}
						if (shield.equals("1")) {// 已屏蔽
							mask_topic_text.setText("开启提醒");
						} else {
							mask_topic_text.setText("不再提醒");
						}
						details_name.setText(nice_name);
						details_neirong.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
						sb = ExpressionUtil.prase(TopicDetailsActivity.this, details_neirong, content);// 对内容做处理
						details_neirong.setText(sb);

						details_time.setText(Utils.timeSpan(date));
						if (card_industry.equals("''")) {
							details_content.setText("自由职业");
						} else {
							details_content.setText(card_industry + " | " + card_title);
						}
						if (avatar.equals("no_pic")) {
							details_image.setVisibility(View.GONE);
						} else {
							FinalBitmap fBitmap = null;
							fBitmap = FinalBitmap.create(TopicDetailsActivity.this);
							fBitmap.display(details_image, avatar);
						}
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.get_details_failed));
					}
					if ("10".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.duoduo_xiulian));
					}
				} catch (Exception e) {
					Log.e("message", "走catch了");
					SimpleHUD.showErrorMessage(TopicDetailsActivity.this, getString(R.string.app_exception));
					e.printStackTrace();
				}
			}

		});

	}

	// 加载更多
	private void referenceQB(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				isLoading = true;
				detalis_list.addFooterView(footerView);
				mainyadapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				mainyadapter.notifyDataSetChanged();
				isLoading = false;
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("加载全部评论" + str1);
				JSONObject json;
				isLoading = false;
				detalis_list.removeFooterView(footerView);
				mainyadapter.notifyDataSetChanged();
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						String result = json.getString("result");
						if ("".equals(result)) {
							Toast.makeText(TopicDetailsActivity.this, getString(R.string.not_more_data),
									Toast.LENGTH_SHORT).show();
							isMore = false;
						} else {
							// String page_count = json.getString("page_count");
							Gson gson = new Gson();
							Type type = new TypeToken<List<CommentDTO>>() {
							}.getType();
							list = gson.fromJson(result, type);
							commlist.addAll(list);
							mainyadapter.notifyDataSetChanged();
						}

					} else if ("3".equals(code)) {
						Toast.makeText(TopicDetailsActivity.this, getString(R.string.load_failed), Toast.LENGTH_SHORT)
								.show();
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});

	}

	// 加载更多
	private void referenceJC(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				isLoading = true;
				detalis_list.addFooterView(footerView);
				mainyadapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				mainyadapter.notifyDataSetChanged();
				isLoading = false;
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("加载精彩评论" + str1);
				JSONObject json;
				isLoading = false;
				detalis_list.removeFooterView(footerView);
				mainyadapter.notifyDataSetChanged();
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						String result = json.getString("result");
						if ("".equals(result)) {
							Toast.makeText(TopicDetailsActivity.this, getString(R.string.not_more_data),
									Toast.LENGTH_SHORT).show();
							isMore = false;
						} else {
							// String page_count = json.getString("page_count");
							Gson gson = new Gson();
							Type type = new TypeToken<List<CommentDTO>>() {
							}.getType();
							list = gson.fromJson(result, type);
							jccommlist.addAll(list);
							mainyadapter.notifyDataSetChanged();
						}

					} else if ("3".equals(code)) {
						Toast.makeText(TopicDetailsActivity.this, getString(R.string.load_failed), Toast.LENGTH_SHORT)
								.show();
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});

	}

	// 加载更多
	private void referenceWD(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				isLoading = true;
				detalis_list.addFooterView(footerView);
				mainyadapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				mainyadapter.notifyDataSetChanged();
				isLoading = false;
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("加载我的评论" + str1);
				JSONObject json;
				isLoading = false;
				detalis_list.removeFooterView(footerView);
				mainyadapter.notifyDataSetChanged();
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						String result = json.getString("result");
						if ("".equals(result)) {
							detalis_list.removeFooterView(footerView);
							Toast.makeText(TopicDetailsActivity.this, getString(R.string.not_more_data),
									Toast.LENGTH_SHORT).show();
							isMore = false;
						} else {
							// String page_count = json.getString("page_count");
							Gson gson = new Gson();
							Type type = new TypeToken<List<CommentDTO>>() {
							}.getType();
							list = gson.fromJson(result, type);
							mycommlist.addAll(list);
							mainyadapter.notifyDataSetChanged();
						}

					} else if ("3".equals(code)) {
						Toast.makeText(TopicDetailsActivity.this, getString(R.string.load_failed), Toast.LENGTH_SHORT)
								.show();
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});

	}

	// 加载更多
	private void referenceGZ(String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				isLoading = true;
				detalis_list.addFooterView(footerView);
				mainyadapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				mainyadapter.notifyDataSetChanged();
				isLoading = false;
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("加载关注评论" + str1);
				JSONObject json;
				isLoading = false;
				detalis_list.removeFooterView(footerView);
				mainyadapter.notifyDataSetChanged();
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						String result = json.getString("result");
						if ("".equals(result)) {
							detalis_list.removeFooterView(footerView);
							Toast.makeText(TopicDetailsActivity.this, getString(R.string.not_more_data),
									Toast.LENGTH_SHORT).show();
							isMore = false;
						} else {
							// String page_count = json.getString("page_count");
							Gson gson = new Gson();
							Type type = new TypeToken<List<CommentDTO>>() {
							}.getType();
							list = gson.fromJson(result, type);
							gzcommlist.addAll(list);
							mainyadapter.notifyDataSetChanged();
						}

					} else if ("3".equals(code)) {
						Toast.makeText(TopicDetailsActivity.this, getString(R.string.load_failed), Toast.LENGTH_SHORT)
								.show();
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});

	}

	// 发表评论

	private void getComment(String str, RequestParams params) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(TopicDetailsActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				spotsDialog.dismiss();
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("发表评论：" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						QUAN = true;
						isMore = true;
						chat_face_container.setVisibility(View.GONE);
						chat_more.setVisibility(View.GONE);
						chat_microphone.setVisibility(View.GONE);
						Toast.makeText(TopicDetailsActivity.this, getString(R.string.pinglun_succeed), 300).show();
						Intent intent = new Intent("com.example.set.details");
						sendBroadcast(intent);/* 发送广播 */
						// 跳转到全部评论
						comment_all_image.setVisibility(View.VISIBLE);
						comment_surprise_image.setVisibility(View.GONE);
						comment_my_image.setVisibility(View.GONE);
						comment_guanzhu_image.setVisibility(View.GONE);
						comment_all_image2.setVisibility(View.VISIBLE);
						comment_surprise_image2.setVisibility(View.GONE);
						comment_my_image2.setVisibility(View.GONE);
						comment_guanzhu_image2.setVisibility(View.GONE);
						comment_surprise_image.clearAnimation();
						comment_my_image.clearAnimation();
						comment_guanzhu_image.clearAnimation();
						comment_surprise_image2.clearAnimation();
						comment_my_image2.clearAnimation();
						comment_guanzhu_image2.clearAnimation();
						// 设置字体颜色
						comment_all.setTextColor(getResources().getColor(R.color.pinlun_hei));
						comment_surprise.setTextColor(getResources().getColor(R.color.pinlun_hui));
						comment_my.setTextColor(getResources().getColor(R.color.pinlun_hui));
						comment_guanzhu.setTextColor(getResources().getColor(R.color.pinlun_hui));
						comment_all2.setTextColor(getResources().getColor(R.color.pinlun_hei));
						comment_surprise2.setTextColor(getResources().getColor(R.color.pinlun_hui));
						comment_my2.setTextColor(getResources().getColor(R.color.pinlun_hui));
						comment_guanzhu2.setTextColor(getResources().getColor(R.color.pinlun_hui));
						// 切换三角形位置
						details_triangle.setBackgroundResource(R.drawable.triangle1);
						details_triangle2.setBackgroundResource(R.drawable.triangle1);
						if (isToDown || comment_all_image.getVisibility() == View.GONE) {
							animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f,
									Animation.RELATIVE_TO_SELF, 0.5f);
							isToDown = false;
							isMore = true;
							animation.setDuration(500);
							animation.setFillAfter(true);
							comment_all_image.setAnimation(animation);
							comment_all_image.startAnimation(animation);
							comment_all_image2.setAnimation(animation);
							comment_all_image2.startAnimation(animation);
						}
						getQBCommentList(PublicStaticURL.PINGLUNLIST + "&pid=" + PublicStaticURL.pid + "&p=" + 1
								+ "&uid=" + PublicStaticURL.userid + "&sort=1");

						details_pl_et.setText("");
						TimerHideKeyboard(details_pl_et);
						if (qname != null) {
							qname = null;
						}
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.pinglun_failed));
					}

				} catch (Exception e) {
					details_pl_et.setText("");
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});

	}

	// 全部评论列表
	private void getQBCommentList(String str) {
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
				spotsDialog.dismiss();
				rem_wuwife.setVisibility(View.VISIBLE);
				top_wuwife_btn.setVisibility(View.VISIBLE);
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("全部评论列表：====" + str1);
				JSONObject json;
				isLoading = false;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						spotsDialog.dismiss();
						String result = json.getString("result");
						Gson gson = new Gson();
						Type type = new TypeToken<List<CommentDTO>>() {
						}.getType();
						if (result.equals("")) {
							commlist.clear();
							plist.clear();
							detalis_list.addFooterView(topic_foot_nomore);
						} else {
							detalis_list.removeFooterView(topic_foot_nomore);
							commlist.clear();
							commlist = gson.fromJson(result, type);
							plist.clear();
							plist.addAll(commlist);
						}
						if (mainyadapter != null) {
							mainyadapter.setdata(commlist);
							mainyadapter.notifyDataSetChanged();
						} else {
							mainyadapter = new TopicAdapter(TopicDetailsActivity.this, commlist);
							detalis_list.setAdapter(mainyadapter);
						}
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.get_pinlun_failed));
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}

	// 精彩评论列表
	private void getJCCommentList(String str) {
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
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("精彩评论列表：====" + str1);
				JSONObject json;
				isLoading = false;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						spotsDialog.dismiss();
						String result = json.getString("result");
						Gson gson = new Gson();
						Type type = new TypeToken<List<CommentDTO>>() {
						}.getType();
						if (result.equals("")) {
							jccommlist.clear();
							plist.clear();
							detalis_list.addFooterView(topic_foot_nomore);
						} else {
							detalis_list.removeFooterView(topic_foot_nomore);
							jccommlist.clear();
							jccommlist = gson.fromJson(result, type);
							plist.clear();
							plist.addAll(jccommlist);
						}

						if (mainyadapter != null) {
							mainyadapter.setdata(jccommlist);
							mainyadapter.notifyDataSetChanged();
						} else {

							mainyadapter = new TopicAdapter(TopicDetailsActivity.this, jccommlist);
							detalis_list.setAdapter(mainyadapter);
						}

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.get_pinlun_failed));
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}

	// 我的评论列表
	private void getWDCommentList(String str) {
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
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("我的评论列表：====" + str1);
				JSONObject json;
				isLoading = false;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						spotsDialog.dismiss();
						String result = json.getString("result");
						Gson gson = new Gson();
						Type type = new TypeToken<List<CommentDTO>>() {
						}.getType();
						if (result.equals("")) {
							mycommlist.clear();
							plist.clear();
							detalis_list.addFooterView(topic_foot_nomore);
						} else {
							detalis_list.removeFooterView(topic_foot_nomore);
							mycommlist.clear();
							mycommlist = gson.fromJson(result, type);
							plist.clear();
							plist.addAll(mycommlist);
						}
						if (mainyadapter != null) {
							mainyadapter.setdata(mycommlist);
							mainyadapter.notifyDataSetChanged();
						} else {
							mainyadapter = new TopicAdapter(TopicDetailsActivity.this, mycommlist);
							detalis_list.setAdapter(mainyadapter);
						}
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.get_pinlun_failed));
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}

	// 关注评论列表
	private void getGZCommentList(String str) {
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
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("关注评论列表：====" + str1);
				JSONObject json;
				isLoading = false;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						spotsDialog.dismiss();
						String result = json.getString("result");
						Gson gson = new Gson();
						Type type = new TypeToken<List<CommentDTO>>() {
						}.getType();
						if (result.equals("")) {
							gzcommlist.clear();
							plist.clear();
							detalis_list.addFooterView(topic_foot_nomore);
						} else {
							detalis_list.removeFooterView(topic_foot_nomore);
							gzcommlist.clear();
							gzcommlist = gson.fromJson(result, type);
							plist.clear();
							plist.addAll(gzcommlist);
						}
						if (mainyadapter != null) {
							mainyadapter.setdata(gzcommlist);
							mainyadapter.notifyDataSetChanged();
						} else {
							mainyadapter = new TopicAdapter(TopicDetailsActivity.this, gzcommlist);
							detalis_list.setAdapter(mainyadapter);
						}
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.get_pinlun_failed));
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}
	// 删除评论

	private void deleteComment(String str) {
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
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("删除评论：====" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						spotsDialog.dismiss();
						switch (PL) {
						case 1:
							qbpage = 1;
							getQBCommentList(PublicStaticURL.PINGLUNLIST + "&pid=" + PublicStaticURL.pid + "&p="
									+ qbpage + "&uid=" + PublicStaticURL.userid);
							break;
						case 2:
							jcpage = 1;
							getJCCommentList(PublicStaticURL.JINGCAIPINGLUN + "&pid=" + PublicStaticURL.pid + "&p="
									+ jcpage + "&uid=" + PublicStaticURL.userid);
							break;
						case 3:
							wdpage = 1;
							getWDCommentList(PublicStaticURL.WDPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + wdpage
									+ "&uid=" + PublicStaticURL.userid);
							break;
						case 4:
							gzpage = 1;
							getGZCommentList(PublicStaticURL.GZPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + gzpage
									+ "&uid=" + PublicStaticURL.userid);
							break;
						}
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.get_pinlun_failed));
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}
	// 举报评论

	private void reportComment(String str, RequestParams params) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("举报评论：====" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						spotsDialog.dismiss();
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, "举报成功");
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, "举报失败");
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}
	// 删除话题

	private void deleteTopic(String str) {
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
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("删除话题：====" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						spotsDialog.dismiss();
						finish();
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.get_pinlun_failed));
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}
	// 屏蔽话题消息

	private void maskTopicMessages(String str) {
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
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("屏蔽话题消息：====" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						spotsDialog.dismiss();
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, "不再接受此话题的提醒");
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, "屏蔽失败");
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, "之前已经屏蔽过");
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}
	// 点击加载之后的评论

	private void loadLatterComment(String str) {
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
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("加载之后的评论列表：====" + str1);
				JSONObject json;
				isLoading = false;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						spotsDialog.dismiss();
						String result = json.getString("result");
						int page_count_rev = json.getInt("page_count_rev");
						if (page_count_rev >= 1 && taghPage == 1) {
							detalis_list.removeHeaderView(topic_load_before);
							detalis_list.addHeaderView(topic_load_before);
						}
						Gson gson = new Gson();
						Type type = new TypeToken<List<CommentDTO>>() {
						}.getType();
						if (result.equals("")) {
							Toast.makeText(TopicDetailsActivity.this, getString(R.string.not_more_data),
									Toast.LENGTH_SHORT).show();
							isMore = false;

						} else {
							detalis_list.removeFooterView(topic_foot_nomore);
							commlist.clear();
							commlist = gson.fromJson(result, type);
							plist.addAll(commlist);
						}
						if (mainyadapter != null) {
							mainyadapter.setdata(plist);
							mainyadapter.notifyDataSetChanged();
						} else {
							mainyadapter = new TopicAdapter(TopicDetailsActivity.this, plist);
							detalis_list.setAdapter(mainyadapter);
						}
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.get_pinlun_failed));
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}
	// 点击加载之前的评论

	private void loadFormerComment(String str) {
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
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("加载之前的评论列表：====" + str1);
				JSONObject json;
				isLoading = false;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						spotsDialog.dismiss();
						String result = json.getString("result");
						Gson gson = new Gson();
						Type type = new TypeToken<List<CommentDTO>>() {
						}.getType();
						if (result.equals("")) {
							commlist.clear();
							detalis_list.removeHeaderView(topic_load_before);
							Toast.makeText(TopicDetailsActivity.this, getString(R.string.not_more_data),
									Toast.LENGTH_SHORT).show();
						} else {
							detalis_list.removeFooterView(topic_foot_nomore);
							commlist.clear();
							commlist = gson.fromJson(result, type);
							plist.addAll(0, commlist);
							if (commlist.size() < 50) {
								detalis_list.removeHeaderView(topic_load_before);
							}
						}
						if (mainyadapter != null) {
							mainyadapter.setdata(plist);
							mainyadapter.notifyDataSetChanged();
						} else {
							mainyadapter = new TopicAdapter(TopicDetailsActivity.this, plist);
							detalis_list.setAdapter(mainyadapter);
						}
						Logger.i("plist.size()", plist.size() + "");
						detalis_list.setSelection(commlist.size());
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.get_pinlun_failed));
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}
	// 取消屏蔽话题消息

	private void cancleMaskTopicMessages(String str) {
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
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("取消屏蔽话题消息：====" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						spotsDialog.dismiss();
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, "有更新时会提醒你");
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, "取消屏蔽失败");
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, "之前已经取消屏蔽");
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}
	
	// 取消关注

	private void cancelPraise(String str, RequestParams params, final int position, final List<CommentDTO> mylist,
			final TextView comment_zan_txt, final ImageView comment_zan_image) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				spotsDialog.dismiss();
				Logger.i("取消关注：====" + str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {
						comment_zan_image.setBackgroundResource(R.drawable.collect1);
						mylist.get(position).setHaslike("false");
						String comment_zan = comment_zan_txt.getText().toString();
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, "取消成功");

						/**
						 * ModifyBy yxh
						 */
						L.e("details_uid-----" + details_uid);// 文章的作者id
						// L.e("bendizan-+++++++-----------" + bendizan);
						if (PublicStaticURL.userid.equals(details_uid)) {
							comment_zan_txt.setText((Integer.parseInt(comment_zan) - 2) + "");
							mylist.get(position).setPost_like((Integer.parseInt(comment_zan) - 2) + "");
						} else {
							comment_zan_txt.setText((Integer.parseInt(comment_zan) - 1) + "");
							mylist.get(position).setPost_like((Integer.parseInt(comment_zan) - 1) + "");
						}
						if (PL == GZPL) {
							getGZCommentList(PublicStaticURL.GZPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + 1
									+ "&uid=" + PublicStaticURL.userid);
						}
					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, "取消失败");
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, "没有赞过，不能取消关注");
					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}

	// 评论点赞
	protected void commentPraise(String str, RequestParams params, final int position, final List<CommentDTO> mylist,
			final TextView comment_zan_txt, final ImageView comment_zan_image) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(TopicDetailsActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				spotsDialog.dismiss();
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i("评论点赞", str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						String comment_zan = comment_zan_txt.getText().toString();
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);
						comment_zan_image.setBackgroundResource(R.drawable.alreadycollect);
						mylist.get(position).setHaslike("true");
						/**
						 * ModifyBy yxh
						 */
						String uid = PublicStaticURL.userid;
						L.e("details_uid-----" + details_uid);// 文章的作者id
						// L.e("bendizan-+++++++-----------" + bendizan);
						if (uid.equals(details_uid)) {
							comment_zan_txt.setText((Integer.parseInt(comment_zan) + 2) + "");
							mylist.get(position).setPost_like((Integer.parseInt(comment_zan) + 2) + "");
						} else {
							comment_zan_txt.setText((Integer.parseInt(comment_zan) + 1) + "");
							mylist.get(position).setPost_like((Integer.parseInt(comment_zan) + 1) + "");
						}
						sendBroadcast(new Intent("com.example.set.referesh"));/* 发送广播 */
						SimpleHUD.showSuccessMessage(TopicDetailsActivity.this, getString(R.string.praise_succeed));

						PublicStaticURL.plid = null;
						MobclickAgent.onEvent(TopicDetailsActivity.this, "praise");
					}
					if ("3".equals(code)) {

						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.praise_failed));
					}
					if ("4".equals(code)) {

						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.praise_already));
					}
					if ("10".equals(code)) {

						SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.duoduo_xiulian));
					}
				} catch (Exception e) {
					SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 初始化表情
	 */
	private void initFacesList() {
		// 获取表情集合
		staticFacesList = ExpressionUtil.initStaticFaces(this);
		Logger.i("表情包大小--" + staticFacesList.size());
		int pagesize = ExpressionUtil.getPagerCount(staticFacesList.size(), columns, rows);
		// 获取页数
		for (int i = 0; i < pagesize; i++) {
			views.add(ExpressionUtil.viewPagerItem(this, i, staticFacesList, columns, rows, details_pl_et));
			LayoutParams params = new LayoutParams(16, 16);
			mDotsLayout.addView(dotsItem(i), params);
		}
		FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
		mViewPager.setAdapter(mVpAdapter);
		/**
		 * 表情页改变时，dots效果也要跟着改变
		 */
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
					mDotsLayout.getChildAt(i).setSelected(false);
				}
				mDotsLayout.getChildAt(arg0).setSelected(true);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mDotsLayout.getChildAt(0).setSelected(true);
	}

	/**
	 * 表情页切换时，底部小圆点
	 * 
	 * @param position
	 * @return
	 */
	private ImageView dotsItem(int position) {
		View layout = inflater.inflate(R.layout.dot_image, null);
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}

	public static void TimerHideKeyboard(final View v) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) v.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
				}
			}
		}, 1);
	}

	/**
	 * 评论列表适配器
	 */

	public class TopicAdapter extends BaseAdapter {
		FinalBitmap finalBitmap;
		List<CommentDTO> mylist;

		public TopicAdapter(Context context, List<CommentDTO> commlist) {
			super();
			finalBitmap = FinalBitmap.create(context);
			this.mylist = commlist;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mylist.size();
		}

		public void setdadta(int position, String postlic) {
			mylist.get(position).setPost_like(postlic);
		}

		public void setdata(List<CommentDTO> commlist) {
			this.mylist = commlist;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mylist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final HolderView holderView;
			final CommentDTO ctDTO = mylist.get(arg0);
			List<Reply> replys = ctDTO.getReplys();
			if (convertView == null) {
				convertView = LayoutInflater.from(TopicDetailsActivity.this).inflate(R.layout.pinglun_item, null);
				holderView = new HolderView();
				holderView.comment_name = (TextView) convertView.findViewById(R.id.comment_name); // 名字
				holderView.comment_hangye = (TextView) convertView.findViewById(R.id.comment_hangye); // 行业
				holderView.comment_content = (TextView) convertView.findViewById(R.id.comment_content); // 内容
				holderView.comment_time = (TextView) convertView.findViewById(R.id.comment_time); // 时间
				holderView.comment_zan_txt = (TextView) convertView.findViewById(R.id.comment_zan_txt); // 赞数
				holderView.comment_zan_image = (ImageView) convertView.findViewById(R.id.comment_zan_image); // 赞
				holderView.tag_view = convertView.findViewById(R.id.tag_view); // 标记
				holderView.comment_zan = (LinearLayout) convertView.findViewById(R.id.comment_zan); // 点赞按钮
				holderView.comment_image = (CircleImageView2) convertView.findViewById(R.id.comment_image); // 头像
				holderView.answer_layout = (LinearLayout) convertView.findViewById(R.id.answer_layout); // 回复listview列表
				holderView.answer_frame = (FrameLayout) convertView.findViewById(R.id.answer_frame); // 回复listview列表
				convertView.setTag(holderView);
			} else {
				holderView = (HolderView) convertView.getTag();
			}
			if (ctDTO.getHaslike() != null) {
				if (ctDTO.getHaslike().equals("true")) {// 显示是否已关注
					holderView.comment_zan_image.setBackgroundResource(R.drawable.alreadycollect);
				} else {
					holderView.comment_zan_image.setBackgroundResource(R.drawable.collect1);
				}
			}
			if (mIntentResult != null && ctDTO.getCid().equals(PublicStaticURL.remind_cid)) {// 重通知页面进入话题进入详情页，是否标记
				if (mIntentResult.equals("TX") || mIntentResult.equals("TX_WD")) {
					holderView.tag_view.setVisibility(View.VISIBLE);
				}
			} else {
				holderView.tag_view.setVisibility(View.GONE);
			}
			if (holderView.answer_layout.getChildCount() != 0) {//保证回复的列表不会重复添加
				holderView.answer_layout.removeAllViews();
			}
			for (int i = 0; i < replys.size(); i++) {//使用一个linearlayout添加子TextView，构建回复列表
				LinearLayout item_answer = (LinearLayout) View.inflate(TopicDetailsActivity.this, R.layout.item_answer,
						null);
				TextView answer_text_user = (TextView) item_answer.findViewById(R.id.answer_text_user);
				TextView answer_text_content = (TextView) item_answer.findViewById(R.id.answer_text_content);
				answer_text_content.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				SpannableStringBuilder sb = ExpressionUtil.prase(TopicDetailsActivity.this, answer_text_content,
						replys.get(i).getRe_content());// 对内容做处理
				answer_text_user.setText(replys.get(i).getRe_nicename() + "：");
				answer_text_content.setText(sb);
				holderView.answer_layout.addView(item_answer);
			}
			if (replys.size() > 0) {
				holderView.answer_frame.setVisibility(View.VISIBLE);
			} else {
				holderView.answer_frame.setVisibility(View.GONE);
			}

			if (ctDTO.getCard_industry().equals("''") || ctDTO.getCard_industry() == null) {//在刚注册的用户没有身份标签就使用“自由职业”
				holderView.comment_hangye.setText("自由职业");
			} else {
				holderView.comment_hangye.setText(ctDTO.getCard_industry() + " | " + ctDTO.getCard_title());
			}

			holderView.comment_name.setText(ctDTO.getNicename());
			// Logger.i("内容："+conten+"//"+ctDTO.getContent());
				//holderView.comment_content.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				SpannableStringBuilder sb = ExpressionUtil.prase(TopicDetailsActivity.this, holderView.comment_content,
						ctDTO.getContent());// 对内容做处理
				holderView.comment_content.setText(sb);
				// Linkify.addLinks(holderView.comment_content, Linkify.ALL);
			
			if (!TextUtils.isEmpty(ctDTO.getDate())) {
				holderView.comment_time.setText(Utils.timeSpan(ctDTO.getDate()));
			} else {
				holderView.comment_time.setText("");
			}

			if (ctDTO.getPost_like().equals("")) {//关注的个数
				holderView.comment_zan_txt.setText("0");
			} else {
				holderView.comment_zan_txt.setText(ctDTO.getPost_like());
			}
			finalBitmap.display(holderView.comment_image, ctDTO.getAvatar());
			// 关注评论
			holderView.comment_zan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (PublicStaticURL.IsLogin == true) {
						PublicStaticURL.plid = ctDTO.getCid();
						RequestParams params = new RequestParams();
						params.addBodyParameter("from_uid", PublicStaticURL.userid);
						params.addBodyParameter("to_pid", PublicStaticURL.pid);
						params.addBodyParameter("to_cid", PublicStaticURL.plid);

						Log.i("====pid", PublicStaticURL.pid);
						Log.i("====plid", PublicStaticURL.plid);
						Log.i("====userid", PublicStaticURL.userid);

						/**
						 * ModifyBy yxh
						 */
						if (ctDTO.getHaslike() != null) {
							if (ctDTO.getHaslike().equals("true")) {
								cancelPraise(PublicStaticURL.QXPINGLUN, params, arg0, mylist, holderView.comment_zan_txt,
										holderView.comment_zan_image);

							} else {
								commentPraise(PublicStaticURL.ZANPINGLUN, params, arg0, mylist,
										holderView.comment_zan_txt, holderView.comment_zan_image);
							}
						}
					} else {
						Utils.toLogin(TopicDetailsActivity.this);

					}
				}
			});
			return convertView;
		}

	}

	int ret = 0; // 函数调用返回值
	private boolean flag = true;// 判断是否显示popupwindow
	private RelativeLayout wxrl;
	private RelativeLayout wxcrl;
	private RelativeLayout qqrl;
	private RelativeLayout qqkrl;
	private RelativeLayout wbrl;
	private TextView cancer_text;
	private UMImage image;
	private int tagqpage = 0;
	private int taghPage = 1;
	protected String at_name;

	@Override
	public void onClick(View v) {
		if (avatar != null) {
			if (("no_pic").equals(avatar)) {
				avatar = "http://i2dd.com/down.png";
			}
			image = new UMImage(TopicDetailsActivity.this, avatar);
		}
		String shareUrl = PublicStaticURL.YOUMIURL + "g=Portal&m=api&a=share&pid=" + PublicStaticURL.pid + "&cid="
				+ PublicStaticURL.plid;

		switch (v.getId()) {
		// case R.id.details_yindao:// 引导
		// details_yindao.setVisibility(View.GONE);
		// ShareUtils.putSharePre(TopicdetailsActivity.this, "IsFalse", true);
		// break;

		case R.id.face_details:// 表情
			if (PublicStaticURL.IsLogin == true) {
				KeyBoardUtils.closeKeybord(details_pl_et, TopicDetailsActivity.this);
				if (chat_face_container.getVisibility() == View.GONE) {
					chat_face_container.setVisibility(View.VISIBLE);
					chat_microphone.setVisibility(View.GONE);
					chat_more.setVisibility(View.GONE);
					details_pl_et.clearFocus();
				} else {
					chat_face_container.setVisibility(View.GONE);
					details_pl_et.requestFocus();
				}
			} else {
				Utils.toLogin(TopicDetailsActivity.this);
			}
			break;
		case R.id.top_wuwife_btn:
			rem_wuwife.setVisibility(View.GONE);
			top_wuwife_btn.setVisibility(View.GONE);
			spotsDialog.show();
			initView();
			break;
		case R.id.details_pl_et:// 输入框
			if (PublicStaticURL.IsLogin == true) {
				if (getWindow()
						.getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
					if (chat_face_container.getVisibility() == View.VISIBLE) {
						chat_face_container.setVisibility(View.GONE);
					}
				}
			} else {
				Utils.toLogin(TopicDetailsActivity.this);
			}
			break;
		case R.id.details_fanhui:// 返回
		case R.id.details_btn:// 返回
			TimerHideKeyboard(details_pl_et);
			finish();
			break;

		case R.id.details_fasong:// 评论按钮
			if (PublicStaticURL.IsLogin == true) {

				content = details_pl_et.getText().toString();
				if (content.contains("@") && PublicStaticURL.plid != null) {// 对评论的回复
					Log.e("----------对评论的回复-", content);
					RequestParams params = new RequestParams();
					params.addBodyParameter("from_uid", PublicStaticURL.userid);// 用户id
					params.addBodyParameter("to_pid", PublicStaticURL.pid);// 文章id
					params.addBodyParameter("content", content);
					params.addBodyParameter("at_uid", PublicStaticURL.qid);// @id
					params.addBodyParameter("to_cid", PublicStaticURL.plid);// 回复评论id
					params.addBodyParameter("at_nicename", qname + "");
					LogUtils.d(params + "");
					getComment(PublicStaticURL.PINGLUN, params);
					PublicStaticURL.plid = null;

				} else {// 对话题的回复
					Log.e("----------对话题的回复-", content + PublicStaticURL.userid + "/" + PublicStaticURL.pid + "/"
							+ PublicStaticURL.qid + "/" + qname);
					RequestParams params = new RequestParams();
					params.addBodyParameter("from_uid", PublicStaticURL.userid);// 用户id
					params.addBodyParameter("to_pid", PublicStaticURL.pid);// 文章id
					params.addBodyParameter("content", content);
					params.addBodyParameter("at_uid", PublicStaticURL.qid);// @id
					params.addBodyParameter("at_nicename", qname + "");
					getComment(PublicStaticURL.PINGLUN, params);
					LogUtils.d(params + "");
				}

			} else {
				Utils.toLogin(TopicDetailsActivity.this);
			}
			break;
		case R.id.delete_text:// 删除话题
			if (PublicStaticURL.IsLogin == true) {
				if (delete_text.getText().equals("删除")) {
					popup.dismiss();
					flag = true;
					new CustomDialog.Builder(TopicDetailsActivity.this).setMessage("删除后不可撤销，确定删除？")
							.setNegativeButton(getString(R.string.cancel), null)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									sendBroadcast(new Intent("com.example.set.delete"));
									deleteTopic(PublicStaticURL.DELETEAC + "&uid=" + PublicStaticURL.userid + "&pid="
											+ PublicStaticURL.pid);
								}
							}).create().show();
				} else {
					popup.dismiss();
					showReport("[{\"pid\":\"" + PublicStaticURL.pid);
				}

			} else {
				Utils.toLogin(TopicDetailsActivity.this);
			}

			break;
		case R.id.mask_topic_text:// 屏蔽话题消息
			if (PublicStaticURL.IsLogin == true) {
				popup.dismiss();
				flag = true;
				if (shield.equals("1")) {// 已屏蔽
					cancleMaskTopicMessages(PublicStaticURL.QXMASKNOTICE + "&uid=" + PublicStaticURL.userid + "&pid="
							+ PublicStaticURL.pid);
					mask_topic_text.setText("不再提醒");
					shield = "0";
				} else {
					maskTopicMessages(PublicStaticURL.MASKNOTICE + "&uid=" + PublicStaticURL.userid + "&pid="
							+ PublicStaticURL.pid);
					mask_topic_text.setText("开启提醒");
					shield = "1";
				}

			} else {
				Utils.toLogin(TopicDetailsActivity.this);
			}

			break;
		case R.id.comment_all:// 全部评论
			selectQB();
			break;
		case R.id.comment_surprise:// 精彩评论
			selectJC();
			break;

		case R.id.comment_my:// 我的评论
			selectWD();
			break;
		case R.id.comment_guanzhu:// 关注评论
			selectGZ();
			break;
		case R.id.details_morel:// 更多
			popup.showAsDropDown(v);
			break;
		case R.id.wbrl:
			new ShareAction(this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener).withText(sb.toString())
					.withMedia(image).withTitle(getString(R.string.app_name)).withTargetUrl(shareUrl).share();
			break;

		case R.id.wxrl:
			new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener).withText(sb.toString())
					.withTitle(getString(R.string.app_name)).withMedia(image).withTargetUrl(shareUrl).share();
			break;
		case R.id.wxcrl:
			new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
					.withText(sb.toString()).withMedia(image).withTitle(getString(R.string.app_name))
					.withTargetUrl(shareUrl).share();
			break;

		case R.id.qqrl:
			new ShareAction(this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener).withText(sb.toString())
					.withTitle(getString(R.string.app_name)).withMedia(image).withTargetUrl(shareUrl).share();
			break;
		case R.id.qqkrl:
			new ShareAction(this).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener).withText(sb.toString())
					.withTargetUrl(shareUrl).withMedia(image).withTitle(getString(R.string.app_name)).share();
			break;
		case R.id.cancer_text:
			shareDialog.dismiss();
			break;

		case R.id.setup_jia:
			if (PublicStaticURL.IsLogin == true) {
				KeyBoardUtils.closeKeybord(details_pl_et, TopicDetailsActivity.this);
				if (chat_more.getVisibility() == View.GONE) {
					chat_more.setVisibility(View.VISIBLE);
					chat_microphone.setVisibility(View.GONE);
					chat_face_container.setVisibility(View.GONE);
					details_pl_et.clearFocus();
				} else {
					chat_more.setVisibility(View.GONE);
					details_pl_et.requestFocus();
				}
			} else {
				Utils.toLogin(TopicDetailsActivity.this);
			}
			break;
		case R.id.details_image:
			Intent ImageUrl = new Intent(TopicDetailsActivity.this, TopicImageActivity.class);
			ImageUrl.putExtra("imageUrl", avatar);
			startActivity(ImageUrl);

			break;
		case R.id.chat_yuying:
			chat_more.setVisibility(View.GONE);
			chat_microphone.setVisibility(View.VISIBLE);
			details_pl_et.setText(null);// 清空显示内容
			mIatResults.clear();
			// 设置参数
			setParam();
			ret = mIat.startListening(mRecognizerListener);
			if (ret != ErrorCode.SUCCESS) {
				Logger.i("听写失败,错误码：" + ret);
			} else {
				Logger.i("开始说话");
			}
			ismIating = true;
			break;
		case R.id.details_microphone:
			// details_pl_et.setText(null);// 清空显示内容
			mIatResults.clear();
			// 设置参数
			setParam();
			if (ismIating) {
				mIat.stopListening();
				details_microphone.setImageResource(microarray[0]);
				try {
					MediaPlayer media = MediaPlayer.create(TopicDetailsActivity.this, R.raw.off);
					media.start();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				ismIating = false;
			} else {

				ret = mIat.startListening(mRecognizerListener);
				if (ret != ErrorCode.SUCCESS) {
					Logger.i("听写失败,错误码：" + ret);
				} else {
					Logger.i("开始说话");
				}

			}

			break;
		case R.id.topic_load:
			tagqpage++;
			Logger.i("page", tagqpage + "");
			loadFormerComment(PublicStaticURL.TAGPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + tagqpage + "&uid="
					+ PublicStaticURL.userid + "&cid=" + PublicStaticURL.remind_cid + "&direction=UP");

			break;
		case R.id.nomore_lr:
			break;
		}

	}

	// 定义view缓存对象
	static class HolderView {
		public FrameLayout answer_frame;

		public TextView comment_name, comment_hangye, comment_zhiye, comment_content, comment_time, comment_zan_txt,
				comment_nicename, answer_text_user, answer_text_content;

		LinearLayout comment_zan;
		CircleImageView2 comment_image;
		ImageView comment_dengji, comment_zan_image;
		LinearLayout answer_layout;
		View tag_view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (PublicStaticURL.IsLogin == false) {
			Utils.toLogin(TopicDetailsActivity.this);
		} else {
			// 当ListView有Header时，onItemClick里的position不正确
			// .http://blog.chengbo.net/2012/03/09/onitemclick-return-wrong-position-when-listview-has-headerview.html
			CommentDTO commenDto = (CommentDTO) parent.getAdapter().getItem(position);
			PublicStaticURL.plid = commenDto.getCid();// 获取回复对象的评论id
			PublicStaticURL.qid = commenDto.getFrom_uid();
			showItemDialog(position, commenDto);
		}
	}

	public void showItemDialog(final int pos, final CommentDTO commenDto) {
		final AlertDialog alertDialog = new AlertDialog.Builder(TopicDetailsActivity.this).create();
		Window window = alertDialog.getWindow();
		alertDialog.show();
		window.setContentView(R.layout.details_return);
		ImageView answer_image = (ImageView) window.findViewById(R.id.answer_image);
		ImageView delete_image = (ImageView) window.findViewById(R.id.delete_image);
		ImageView report_image = (ImageView) window.findViewById(R.id.report_image);
		ImageView close_image = (ImageView) window.findViewById(R.id.close_image);
		ImageView share_image = (ImageView) window.findViewById(R.id.share_image);
		LinearLayout delete_layout = (LinearLayout) window.findViewById(R.id.delete_layout);
		LinearLayout report_layout = (LinearLayout) window.findViewById(R.id.report_layout);

		if (PublicStaticURL.qid.equals(PublicStaticURL.userid)) {
			delete_layout.setVisibility(View.VISIBLE);
			report_layout.setVisibility(View.GONE);
		} else {
			delete_layout.setVisibility(View.GONE);
			report_layout.setVisibility(View.VISIBLE);
		}
		alertDialog.setCanceledOnTouchOutside(true);

		answer_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (PublicStaticURL.IsLogin == true) {
					alertDialog.dismiss();
					details_pl_et.setText("");
					String qname = null;
					qname = "@" + commenDto.getNicename() + " ";
					at_name = commenDto.getNicename();
					LogUtils.d(qname + "");
					Spanned htmlSpanned = Html.fromHtml("<font color='blue'>" + qname + "</font>");
					details_pl_et.setText(htmlSpanned);
					details_pl_et.setSelection(details_pl_et.getText().length());
					details_pl_et.requestFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(details_pl_et, InputMethodManager.RESULT_SHOWN);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
				} else {
					Utils.toLogin(TopicDetailsActivity.this);
				}
			}
		});
		report_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				showReport("[{\"cid\":\"" + PublicStaticURL.plid);
			}
		});
		close_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		delete_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (PublicStaticURL.IsLogin == true) {
					alertDialog.dismiss();
					new CustomDialog.Builder(TopicDetailsActivity.this).setMessage("删除后不可撤销，确定删除？")
							.setNegativeButton(getString(R.string.cancel), null)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							deleteComment(PublicStaticURL.DELETEPL + "&uid=" + PublicStaticURL.userid + "&cid="
									+ PublicStaticURL.plid);
						}
					}).create().show();
				} else {
					Utils.toLogin(TopicDetailsActivity.this);
				}
			}
		});
		share_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				popup.dismiss();
				flag = true;
				shareDialog = new AlertDialog.Builder(TopicDetailsActivity.this, R.style.ActionSheetDialogStyle)
						.create();
				shareDialog.show();
				shareDialog.setCanceledOnTouchOutside(true);
				Window window = shareDialog.getWindow();
				window.setContentView(R.layout.share_layout);
				window.setGravity(Gravity.BOTTOM);
				WindowManager.LayoutParams lp = window.getAttributes();
				WindowManager windowManager = getWindowManager();
				lp.width = windowManager.getDefaultDisplay().getWidth();
				window.setAttributes(lp);
				wxrl = (RelativeLayout) window.findViewById(R.id.wxrl);
				wxcrl = (RelativeLayout) window.findViewById(R.id.wxcrl);
				qqrl = (RelativeLayout) window.findViewById(R.id.qqrl);
				qqkrl = (RelativeLayout) window.findViewById(R.id.qqkrl);
				wbrl = (RelativeLayout) window.findViewById(R.id.wbrl);
				cancer_text = (TextView) window.findViewById(R.id.cancer_text);
				wxrl.setOnClickListener(TopicDetailsActivity.this);
				wxcrl.setOnClickListener(TopicDetailsActivity.this);
				qqrl.setOnClickListener(TopicDetailsActivity.this);
				qqkrl.setOnClickListener(TopicDetailsActivity.this);
				wbrl.setOnClickListener(TopicDetailsActivity.this);
				cancer_text.setOnClickListener(TopicDetailsActivity.this);

			}
		});
	}

	public void selectQB() {
		// detalis_list.removeFooterView(pinglun_kongbai);
		// detalis_list.setAdapter(mainyadapter);
		isUserclick = true;
		detalis_list.removeFooterView(topic_foot_nomore);
		detalis_list.removeHeaderView(topic_load_before);
		qbpage = 1;
		isMore = true;
		if (comment_all_image.getVisibility() == View.GONE) {
			isToDown = true;
			PL = QBPL;
			comment_surprise_image.clearAnimation();
			comment_my_image.clearAnimation();
			comment_guanzhu_image.clearAnimation();
			comment_surprise_image2.clearAnimation();
			comment_my_image2.clearAnimation();
			comment_guanzhu_image2.clearAnimation();
			if (PublicStaticURL.IsLogin == true) {
				// 设置小箭头是否可见
				comment_all_image.setVisibility(View.VISIBLE);
				comment_surprise_image.setVisibility(View.GONE);
				comment_my_image.setVisibility(View.GONE);
				comment_guanzhu_image.setVisibility(View.GONE);
				comment_all_image2.setVisibility(View.VISIBLE);
				comment_surprise_image2.setVisibility(View.GONE);
				comment_my_image2.setVisibility(View.GONE);
				comment_guanzhu_image2.setVisibility(View.GONE);
				// 设置字体颜色
				comment_all.setTextColor(getResources().getColor(R.color.pinlun_hei));
				comment_surprise.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_my.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_guanzhu.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_all2.setTextColor(getResources().getColor(R.color.pinlun_hei));
				comment_surprise2.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_my2.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_guanzhu2.setTextColor(getResources().getColor(R.color.pinlun_hui));
				// 切换三角形位置
				details_triangle.setBackgroundResource(R.drawable.triangle1);
				details_triangle2.setBackgroundResource(R.drawable.triangle1);
				// if (commlist.size() != 0) {
				// if (mainyadapter != null) {
				// mainyadapter.setdata(commlist);
				// mainyadapter.notifyDataSetChanged();
				// } else {
				// mainyadapter = new TopicAdapter(TopicdetailsActivity.this,
				// commlist);
				// detalis_list.setAdapter(mainyadapter);
				// }
				// detalis_list.setSelection(qbposition);
				// } else {
				getQBCommentList(PublicStaticURL.PINGLUNLIST + "&pid=" + PublicStaticURL.pid + "&p=" + qbpage + "&uid="
						+ PublicStaticURL.userid);

				// }

			} else {
				Utils.toLogin(TopicDetailsActivity.this);
			}
		} else {

			if (commlist != null && mainyadapter != null) {

				if (isToDown) {
					animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					getQBCommentList(PublicStaticURL.PINGLUNLIST + "&pid=" + PublicStaticURL.pid + "&p=" + qbpage
							+ "&uid=" + PublicStaticURL.userid + "&sort=1");

					isToDown = false;
				} else {
					animation = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					getQBCommentList(PublicStaticURL.PINGLUNLIST + "&pid=" + PublicStaticURL.pid + "&p=" + qbpage
							+ "&uid=" + PublicStaticURL.userid + "&sort=2");
					isToDown = true;
				}

				animation.setDuration(500);
				animation.setFillAfter(true);
				comment_all_image.setAnimation(animation);
				comment_all_image.startAnimation(animation);
				comment_all_image2.setAnimation(animation);
				comment_all_image2.startAnimation(animation);
			}
		}
	}

	public void selectJC() {
		detalis_list.removeFooterView(topic_foot_nomore);
		detalis_list.removeHeaderView(topic_load_before);
		jcpage = 1;
		isMore = true;
		if (comment_surprise_image.getVisibility() == View.GONE) {
			isToDown = true;
			PL = JCPL;
			isMore = true;
			comment_all_image.clearAnimation();
			comment_my_image.clearAnimation();
			comment_guanzhu_image.clearAnimation();
			comment_all_image2.clearAnimation();
			comment_my_image2.clearAnimation();
			comment_guanzhu_image2.clearAnimation();
			if (PublicStaticURL.IsLogin == true) {
				comment_all_image.setVisibility(View.GONE);
				comment_surprise_image.setVisibility(View.VISIBLE);
				comment_my_image.setVisibility(View.GONE);
				comment_guanzhu_image.setVisibility(View.GONE);
				comment_all_image2.setVisibility(View.GONE);
				comment_surprise_image2.setVisibility(View.VISIBLE);
				comment_my_image2.setVisibility(View.GONE);
				comment_guanzhu_image2.setVisibility(View.GONE);
				comment_all.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_surprise.setTextColor(getResources().getColor(R.color.pinlun_hei));
				comment_my.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_guanzhu.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_all2.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_surprise2.setTextColor(getResources().getColor(R.color.pinlun_hei));
				comment_my2.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_guanzhu2.setTextColor(getResources().getColor(R.color.pinlun_hui));
				details_triangle.setBackgroundResource(R.drawable.triangle2);
				details_triangle2.setBackgroundResource(R.drawable.triangle2);
				getJCCommentList(PublicStaticURL.JINGCAIPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + jcpage
						+ "&uid=" + PublicStaticURL.userid);
			} else {
				Utils.toLogin(TopicDetailsActivity.this);
			}
		} else {

			if (jccommlist != null && mainyadapter != null) {

				if (isToDown) {
					animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					getJCCommentList(PublicStaticURL.JINGCAIPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + jcpage
							+ "&uid=" + PublicStaticURL.userid + "&sort=1");
					isToDown = false;
				} else {
					animation = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					getJCCommentList(PublicStaticURL.JINGCAIPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + jcpage
							+ "&uid=" + PublicStaticURL.userid + "&sort=2");
					isToDown = true;
				}

				animation.setDuration(500);
				animation.setFillAfter(true);
				comment_surprise_image.setAnimation(animation);
				comment_surprise_image.startAnimation(animation);
				comment_surprise_image2.setAnimation(animation);
				comment_surprise_image2.startAnimation(animation);
			}
		}

	}

	public void selectWD() {
		detalis_list.removeFooterView(topic_foot_nomore);
		detalis_list.removeHeaderView(topic_load_before);
		wdpage = 1;
		isMore = true;
		if (comment_my_image.getVisibility() == View.GONE) {
			isToDown = true;
			PL = WDPL;
			isMore = true;
			comment_all_image.clearAnimation();
			comment_surprise_image.clearAnimation();
			comment_guanzhu_image.clearAnimation();
			comment_all_image2.clearAnimation();
			comment_surprise_image2.clearAnimation();
			comment_guanzhu_image2.clearAnimation();
			if (PublicStaticURL.IsLogin == true) {
				comment_all_image.setVisibility(View.GONE);
				comment_surprise_image.setVisibility(View.GONE);
				comment_my_image.setVisibility(View.VISIBLE);
				comment_guanzhu_image.setVisibility(View.GONE);
				comment_all_image2.setVisibility(View.GONE);
				comment_surprise_image2.setVisibility(View.GONE);
				comment_my_image2.setVisibility(View.VISIBLE);
				comment_guanzhu_image2.setVisibility(View.GONE);

				comment_all.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_surprise.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_my.setTextColor(getResources().getColor(R.color.pinlun_hei));
				comment_guanzhu.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_all2.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_surprise2.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_my2.setTextColor(getResources().getColor(R.color.pinlun_hei));
				comment_guanzhu2.setTextColor(getResources().getColor(R.color.pinlun_hui));
				details_triangle.setBackgroundResource(R.drawable.triangle3);
				details_triangle2.setBackgroundResource(R.drawable.triangle3);
				getWDCommentList(PublicStaticURL.WDPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + wdpage + "&uid="
						+ PublicStaticURL.userid);

			} else {
				Utils.toLogin(TopicDetailsActivity.this);
			}
		} else {
			if (mycommlist != null && mainyadapter != null) {

				if (isToDown) {
					animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					getWDCommentList(PublicStaticURL.WDPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + wdpage
							+ "&uid=" + PublicStaticURL.userid + "&sort=1");
					isToDown = false;
				} else {
					animation = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					getWDCommentList(PublicStaticURL.WDPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + wdpage
							+ "&uid=" + PublicStaticURL.userid + "&sort=2");
					isToDown = true;
				}

				animation.setDuration(500);
				animation.setFillAfter(true);
				comment_my_image.setAnimation(animation);
				comment_my_image.startAnimation(animation);
				comment_my_image2.setAnimation(animation);
				comment_my_image2.startAnimation(animation);
			}
		}

	}

	public void selectGZ() {
		detalis_list.removeFooterView(topic_foot_nomore);
		detalis_list.removeHeaderView(topic_load_before);
		gzpage = 1;
		isMore = true;
		if (comment_guanzhu_image.getVisibility() == View.GONE) {
			isToDown = true;
			PL = GZPL;
			isMore = true;
			comment_all_image.clearAnimation();
			comment_my_image.clearAnimation();
			comment_surprise_image.clearAnimation();
			comment_all_image2.clearAnimation();
			comment_my_image2.clearAnimation();
			comment_surprise_image2.clearAnimation();
			if (PublicStaticURL.IsLogin == true) {
				comment_all_image.setVisibility(View.GONE);
				comment_surprise_image.setVisibility(View.GONE);
				comment_my_image.setVisibility(View.GONE);
				comment_guanzhu_image.setVisibility(View.VISIBLE);
				comment_all_image2.setVisibility(View.GONE);
				comment_surprise_image2.setVisibility(View.GONE);
				comment_my_image2.setVisibility(View.GONE);
				comment_guanzhu_image2.setVisibility(View.VISIBLE);
				comment_all.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_surprise.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_my.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_guanzhu.setTextColor(getResources().getColor(R.color.pinlun_hei));
				comment_all2.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_surprise2.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_my2.setTextColor(getResources().getColor(R.color.pinlun_hui));
				comment_guanzhu2.setTextColor(getResources().getColor(R.color.pinlun_hei));
				details_triangle.setBackgroundResource(R.drawable.triangle4);
				details_triangle2.setBackgroundResource(R.drawable.triangle4);
				getGZCommentList(PublicStaticURL.GZPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + gzpage + "&uid="
						+ PublicStaticURL.userid);
			} else {
				Utils.toLogin(TopicDetailsActivity.this);
			}
		} else {
			if (gzcommlist != null && mainyadapter != null) {

				if (isToDown) {
					animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					getGZCommentList(PublicStaticURL.GZPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + gzpage
							+ "&uid=" + PublicStaticURL.userid + "&sort=1");
					isToDown = false;
				} else {
					animation = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					getGZCommentList(PublicStaticURL.GZPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + gzpage
							+ "&uid=" + PublicStaticURL.userid + "&sort=2");
					isToDown = true;
				}

				animation.setDuration(500);
				animation.setFillAfter(true);
				comment_guanzhu_image.setAnimation(animation);
				comment_guanzhu_image.startAnimation(animation);
				comment_guanzhu_image2.setAnimation(animation);
				comment_guanzhu_image2.startAnimation(animation);
			}
		}
	}

	public void scrollBottomState() {
		if (Utils.isNetworkAvailable(TopicDetailsActivity.this)) {
			if (PublicStaticURL.IsLogin == true) {
				switch (PL) {
				case 1:
					if (null != mIntentResult && (mIntentResult.equals("TX") || mIntentResult.equals("TX_WD"))
							&& !isUserclick) {
						taghPage++;
						loadLatterComment(PublicStaticURL.TAGPINGLUN + "&pid=" + PublicStaticURL.pid + "&p=" + taghPage
								+ "&uid=" + PublicStaticURL.userid + "&cid=" + PublicStaticURL.remind_cid
								+ "&direction=DOWN");
					} else {
						qbpage++;
						if (isToDown) {
							referenceQB(PublicStaticURL.PINGLUNLIST + "&uid=" + PublicStaticURL.userid + "&pid="
									+ PublicStaticURL.pid + "&p=" + qbpage + "&sort=2");
						} else {
							referenceQB(PublicStaticURL.PINGLUNLIST + "&uid=" + PublicStaticURL.userid + "&pid="
									+ PublicStaticURL.pid + "&p=" + qbpage + "&sort=1");
						}
					}

					break;
				case 2:
					jcpage++;
					if (isToDown) {
						referenceJC(PublicStaticURL.JINGCAIPINGLUN + "&uid=" + PublicStaticURL.userid + "&pid="
								+ PublicStaticURL.pid + "&p=" + jcpage + "&sort=2");
					} else {
						referenceJC(PublicStaticURL.JINGCAIPINGLUN + "&uid=" + PublicStaticURL.userid + "&pid="
								+ PublicStaticURL.pid + "&p=" + jcpage + "&sort=1");
					}

					break;
				case 3:
					wdpage++;
					if (isToDown) {
						referenceWD(PublicStaticURL.WDPINGLUN + "&uid=" + PublicStaticURL.userid + "&pid="
								+ PublicStaticURL.pid + "&p=" + wdpage + "&sort=2");
					} else {
						referenceWD(PublicStaticURL.WDPINGLUN + "&uid=" + PublicStaticURL.userid + "&pid="
								+ PublicStaticURL.pid + "&p=" + wdpage + "&sort=1");
					}

					break;
				case 4:
					gzpage++;
					if (isToDown) {
						referenceGZ(PublicStaticURL.GZPINGLUN + "&uid=" + PublicStaticURL.userid + "&pid="
								+ PublicStaticURL.pid + "&p=" + gzpage + "&sort=2");
					} else {
						referenceGZ(PublicStaticURL.GZPINGLUN + "&uid=" + PublicStaticURL.userid + "&pid="
								+ PublicStaticURL.pid + "&p=" + gzpage + "&sort=1");
					}

					break;

				}
			} else {
				Utils.toLogin(TopicDetailsActivity.this);
			}
		} else {
			SimpleHUD.showInfoMessage(TopicDetailsActivity.this, getString(R.string.wuwangluo));
		}
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				Logger.i("初始化失败，错误码：" + code);
			}
		}
	};

	/**
	 * 听写监听器。
	 */
	private RecognizerListener mRecognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			// 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
			Logger.i("开始说话");
			try {
				MediaPlayer media = MediaPlayer.create(TopicDetailsActivity.this, R.raw.on);
				media.start();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			ismIating = true;
		}

		@Override
		public void onError(SpeechError error) {
			// Tips：
			// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
			// 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
			Logger.i(error.getPlainDescription(true));
			ismIating = false;

		}

		@Override
		public void onEndOfSpeech() {
			// 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
			Logger.i("结束说话");
			try {
				MediaPlayer media = MediaPlayer.create(TopicDetailsActivity.this, R.raw.off);
				media.start();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			ismIating = false;
			details_microphone.setImageResource(microarray[0]);

		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Logger.i(results.getResultString());
			printResult(results);

			if (isLast) {
				// TODO 最后的结果
			}
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			Logger.i("当前正在说话，音量大小：" + volume);
			details_microphone.setImageResource(microarray[volume / 5]);
			Logger.i("返回音频数据：" + data.length);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			// 若使用本地能力，会话id为null
			// if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			// String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			// Log.d(TAG, "session id =" + sid);
			// }
		}
	};

	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}

		details_pl_et.setText(resultBuffer.toString());
		details_pl_et.setSelection(details_pl_et.length());
	}

	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			Logger.i(error.getPlainDescription(true));
		}

	};

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		String lag = "zh_cn";
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}

		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT, "1");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/行家说/msc/iat.wav");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出时释放连接
		if (spotsDialog != null && spotsDialog.isShowing()) {
			spotsDialog.cancel();
			spotsDialog = null;
		}
		mIat.cancel();
		mIat.destroy();
	}

	@Override
	protected void onResume() {
		// 开放统计 移动数据统计分析
		FlowerCollector.onResume(TopicDetailsActivity.this);
		FlowerCollector.onPageStart(TopicDetailsActivity.class.toString());
		umengResume(this, getClass().toString());
		super.onResume();
	}

	@Override
	protected void onPause() {
		// 开放统计 移动数据统计分析
		FlowerCollector.onPageEnd(TopicDetailsActivity.class.toString());
		FlowerCollector.onPause(TopicDetailsActivity.this);
		umengPause(this, getClass().toString());
		super.onPause();
	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Toast.makeText(TopicDetailsActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(TopicDetailsActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(TopicDetailsActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** attention to this below ,must add this **/
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	private boolean isPullUp() {
		return (mYDown - mLastY) > mTouchSlop;
	}

	public void showReport(final String id) {
		new ActionSheetDialog(TopicDetailsActivity.this).builder().setCancelable(true).setCanceledOnTouchOutside(true)
				.addSheetItem(getString(R.string.error), SheetItemColor.Blue, new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						RequestParams params = new RequestParams();
						params.addBodyParameter("uid", PublicStaticURL.userid);
						params.addBodyParameter("type", "2");
						params.addBodyParameter("params", id + "\",\"reason\":\"1\"}]");// 使用转义字符
						reportComment(PublicStaticURL.REPORTPL, params);
						// REPORTPL(PublicStaticURL.REPORTPL+"&uid="+PublicStaticURL.userid+"&type=2"+"&params="+"[{cid:"+PublicStaticURL.plid+",reason:1}]");
					}
				}).builder().setCancelable(true).setCanceledOnTouchOutside(true)
				.addSheetItem(getString(R.string.sheqingbaoli), SheetItemColor.Blue, new OnSheetItemClickListener() {

					@Override
					public void onClick(int which) {
						RequestParams params = new RequestParams();
						params.addBodyParameter("uid", PublicStaticURL.userid);
						params.addBodyParameter("type", "2");
						params.addBodyParameter("params", id + "\",\"reason\":\"2\"}]");// 使用转义字符
						reportComment(PublicStaticURL.REPORTPL, params);
					}
				}).builder().setCancelable(true).setCanceledOnTouchOutside(true)
				.addSheetItem(getString(R.string.leak_telephone), SheetItemColor.Blue, new OnSheetItemClickListener() {

					@Override
					public void onClick(int which) {
						RequestParams params = new RequestParams();
						params.addBodyParameter("uid", PublicStaticURL.userid);
						params.addBodyParameter("type", "2");
						params.addBodyParameter("params", id + "\",\"reason\":\"3\"}]");// 使用转义字符
						reportComment(PublicStaticURL.REPORTPL, params);
					}
				}).builder().setCancelable(true).setCanceledOnTouchOutside(true)
				.addSheetItem(getString(R.string.shuitie), SheetItemColor.Blue, new OnSheetItemClickListener() {

					@Override
					public void onClick(int which) {
						RequestParams params = new RequestParams();
						params.addBodyParameter("uid", PublicStaticURL.userid);
						params.addBodyParameter("type", "2");
						params.addBodyParameter("params", id + "\",\"reason\":\"4\"}]");// 使用转义字符
						reportComment(PublicStaticURL.REPORTPL, params);
					}

				}).builder().setCancelable(true).setCanceledOnTouchOutside(true)
				.addSheetItem(getString(R.string.chaoxi), SheetItemColor.Blue, new OnSheetItemClickListener() {

					@Override
					public void onClick(int which) {
						RequestParams params = new RequestParams();
						params.addBodyParameter("uid", PublicStaticURL.userid);
						params.addBodyParameter("type", "2");
						params.addBodyParameter("params", id + "\",\"reason\":\"5\"}]");// 使用转义字符
						reportComment(PublicStaticURL.REPORTPL, params);
					}
				}).show();
	}
}
