package com.example.honeytag1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import info.wangchen.simplehud.SimpleHUD;
import com.example.config.Constants;
import com.example.config.MySharedPreference;
import com.example.dialog.CustomDialog;
import com.example.dto.ACache;
import com.example.dto.TopicDTO;
import com.example.dto.User;
import com.example.entity.CardInfo;
import com.example.fragment.FragmentLanMu;
import com.example.honeytag1.R;
import com.example.http.Loadings;
import com.example.fragment.FragmentMy;
import com.example.fragment.FragmentShouYe;
import com.example.fragment.FragmentLiaoTian;
import com.example.my.LoginActivity;
import com.example.notification.MyNotificationManager;
import com.example.setup.IdentityActivity;
import com.example.setup.MyGradesActivity;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.utils.Utils;
import com.example.utils.VersionManagement;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.readystatesoftware.viewbadger.BadgeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements OnClickListener {

	// App代码由 北京鑫兴宝网络科技有限公司（网络白武士）编写
	// 技术支持网站 http://www.baiwushi.cn/
	// LiuH
	/**
	 * 主页面
	 */

	private LinearLayout map_footer_btn1, map_footer_btn2, map_footer_btn3, map_footer_btn4; // 导航按钮
	private ImageButton map_footer_btn5, map_footer_btn6, map_footer_btn7, map_footer_btn8; // 导航按钮
	private Fragment mCurrentContainerFragment;
	private long mExitTime;// 退出时间
	private FragmentShouYe fragment1;
	private FragmentLanMu fragment2;
	private FragmentLiaoTian fragment3;
	private FragmentMy fragment4;
	private FragmentManager fm;
	// 页面列表
	private ArrayList<Fragment> fragmentList;
	private ViewPager m_vp;
	private CustomDialog.Builder ibuilder;
	private LinearLayout main_root;
	private TextView mTvHome, mTvColumn, mTvChat, mTvMy, main_tab_unread_tv_layout;
	private BadgeView badgeView, mMyBadgeView;

	private MyChangeFragmentReceiver receiver;
	private IntentFilter filter;
	private MyViewPagerAdapter pageAdapter;

	private int activityFlag = -1;// 0,代表从忘记密码页面跳转到主页;1,代表从注册页面跳转到首页;3,代表不经过欢迎界面直接进入到主页
	private String device_token;
	private User user;
	HttpUtils httpUtils;
	private ACache aCache; // 本地缓存

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		httpUtils = MyApplication.getInstance().httpUtils;
		aCache = ACache.get(MainActivity.this);
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		mPushAgent.setDebugMode(true);
		user = MySharedPreference.readUser(MainActivity.this);

		// user = MySharedPreference.readUser(MainActivity.this);
		if (user == null) {
			MyApplication.getInstance().device_token = UmengRegistrar.getRegistrationId(MainActivity.this);
			if (!TextUtils.isEmpty(MyApplication.getInstance().device_token)) {
				Loadings.send(MyApplication.getInstance().device_token);
			}
		} else {
			PublicStaticURL.userid = user.getUid(); // 将用户ID储存，
			PublicStaticURL.ablity = user.getAbility(); // 用户能力值储存
			PublicStaticURL.credit = user.getCredit(); // 用户信用值储存
			PublicStaticURL.Login_phone = user.getTel(); // 用户名
			PublicStaticURL.IsLogin = true;
			Loadings.getCount(PublicStaticURL.ZONGmessage + "&uid=" + PublicStaticURL.userid);
			// startActivity(new Intent(WelcomeActivity.this,
			// MainActivity.class));
		}
		if (Utils.isNetworkAvailable(MainActivity.this)) {
			VersionManagement.getInstance().checkUpdate(MainActivity.this, PublicStaticURL.VERSION, mMyBadgeView);
		} else {
			SimpleHUD.showInfoMessage(MainActivity.this, getString(R.string.wuwangluo));
		}

	}

	@Override
	public void loadXml() {
		// TODO Auto-generated method stub

		// ViewURL();
		// 默认界面
		// main_tab_unread_tv_layout = (TextView)
		// findViewById(R.id.main_tab_unread_tv_layout);
		setContentView(R.layout.activity_main);
		m_vp = (ViewPager) findViewById(R.id.viewpager);
		map_footer_btn1 = (LinearLayout) this.findViewById(R.id.map_footer_btn1);
		map_footer_btn2 = (LinearLayout) this.findViewById(R.id.map_footer_btn2);
		map_footer_btn3 = (LinearLayout) this.findViewById(R.id.map_footer_btn3);
		map_footer_btn4 = (LinearLayout) this.findViewById(R.id.map_footer_btn4);

		map_footer_btn5 = (ImageButton) this.findViewById(R.id.map_footer_btn5);
		map_footer_btn6 = (ImageButton) this.findViewById(R.id.map_footer_btn6);
		map_footer_btn7 = (ImageButton) this.findViewById(R.id.map_footer_btn7);
		map_footer_btn8 = (ImageButton) this.findViewById(R.id.map_footer_btn8);

		mTvHome = (TextView) findViewById(R.id.tv_home);
		mTvColumn = (TextView) findViewById(R.id.tv_column);
		badgeView = new BadgeView(this, map_footer_btn6);
		badgeView.setText(".");
		badgeView.setTextSize(10);
		badgeView.setBackgroundResource(R.drawable.radio_shape_enter3);
		badgeView.setTextColor(getResources().getColor(R.color.transparent));
		badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView.setAlpha(1f);
		badgeView.setBadgeMargin(40, 0);

		mMyBadgeView = new BadgeView(this, map_footer_btn8);
		mMyBadgeView.setText(".");
		mMyBadgeView.setTextSize(10);
		mMyBadgeView.setBackgroundResource(R.drawable.radio_shape_enter3);
		mMyBadgeView.setTextColor(getResources().getColor(R.color.transparent));
		mMyBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		mMyBadgeView.setAlpha(1f);
		mMyBadgeView.setBadgeMargin(0, 0);

		mTvChat = (TextView) findViewById(R.id.tv_chat);
		mTvMy = (TextView) findViewById(R.id.tv_my);
	}

	@Override
	public void loadData() {

		map_footer_btn1.setOnClickListener(this);
		map_footer_btn2.setOnClickListener(this);
		map_footer_btn3.setOnClickListener(this);
		map_footer_btn4.setOnClickListener(this);
		map_footer_btn5.setOnClickListener(this);
		map_footer_btn6.setOnClickListener(this);
		map_footer_btn7.setOnClickListener(this);
		map_footer_btn8.setOnClickListener(this);
		fragment1 = new FragmentShouYe();
		fragment2 = new FragmentLanMu();
		// fragment3 = new FragmentThree();
		fragment4 = new FragmentMy();
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(fragment1);
		fragmentList.add(fragment2);
		// fragmentList.add(fragment3);
		fragmentList.add(fragment4);

		filter = new IntentFilter();
		filter.addAction("com.honeytag.changefragment");
		receiver = new MyChangeFragmentReceiver();
		registerReceiver(receiver, filter);
		m_vp.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {

					map_footer_btn5.setBackgroundResource(R.drawable.home_enter);
					map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
					map_footer_btn7.setBackgroundResource(R.drawable.chat_default);
					map_footer_btn8.setBackgroundResource(R.drawable.mine_default);

					mTvHome.setTextColor(getResources().getColor(R.color.black));
					mTvColumn.setTextColor(getResources().getColor(R.color.huise3));
					mTvChat.setTextColor(getResources().getColor(R.color.huise3));
					mTvMy.setTextColor(getResources().getColor(R.color.huise3));

				} else if (arg0 == 1) {
					if (0 == MyApplication.getInstance().columnRedDotFlag) {

						badgeView.show();
					}
					if (1 == MyApplication.getInstance().columnRedDotFlag
							|| -1 == MyApplication.getInstance().columnRedDotFlag) {
						MySharedPreference.saveTime(MainActivity.this, System.currentTimeMillis() / 1000 + "");
						badgeView.hide();
					}
					m_vp.setCurrentItem(1);
					map_footer_btn5.setBackgroundResource(R.drawable.home_default);
					map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_a);
					map_footer_btn7.setBackgroundResource(R.drawable.chat_default);
					map_footer_btn8.setBackgroundResource(R.drawable.mine_default);

					mTvHome.setTextColor(getResources().getColor(R.color.huise3));
					mTvColumn.setTextColor(getResources().getColor(R.color.black));
					mTvChat.setTextColor(getResources().getColor(R.color.huise3));
					mTvMy.setTextColor(getResources().getColor(R.color.huise3));
					// } else if (arg0 == 2) {
					// map_footer_btn5.setBackgroundResource(R.drawable.home_default);
					// map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
					// map_footer_btn7.setBackgroundResource(R.drawable.chat_enter);
					// map_footer_btn8.setBackgroundResource(R.drawable.mine_default);
					//
					// mTvHome.setTextColor(getResources().getColor(R.color.huise3));
					// mTvColumn.setTextColor(getResources().getColor(R.color.huise3));
					// mTvChat.setTextColor(getResources().getColor(R.color.black));
					// mTvMy.setTextColor(getResources().getColor(R.color.huise3));

				} else if (arg0 == 2) {
					User mUser = MySharedPreference.readUser(MainActivity.this);
					if (mUser == null) {
						startActivity(new Intent(MainActivity.this, LoginActivity.class));
						m_vp.setCurrentItem(1);
					} else {
						map_footer_btn5.setBackgroundResource(R.drawable.home_default);
						map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
						map_footer_btn7.setBackgroundResource(R.drawable.chat_default);
						map_footer_btn8.setBackgroundResource(R.drawable.mine_enter);

						mTvHome.setTextColor(getResources().getColor(R.color.huise3));
						mTvColumn.setTextColor(getResources().getColor(R.color.huise3));
						mTvChat.setTextColor(getResources().getColor(R.color.huise3));
						mTvMy.setTextColor(getResources().getColor(R.color.black));

					}
				}
				Constants.ACTIVITYFLAG = m_vp.getCurrentItem();

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		pageAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
		m_vp.setAdapter(pageAdapter);
		m_vp.setOffscreenPageLimit(4);
		// 默认显示的fragment
		// fm = getFragmentManager();
		// showFragment(1);

	}

	public class MyViewPagerAdapter extends FragmentPagerAdapter {
		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		// 都是大大
		@Override
		public int getCount() {
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	/*
	 * 用户首次注册成功之后弹出引导用户是否去添加身份标签
	 */

	public void show() {

		ibuilder = new CustomDialog.Builder(MainActivity.this);
		ibuilder.setTitle("提示");
		ibuilder.setMessage("是否去添加名片?");
		ibuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent intent = new Intent(MainActivity.this, IdentityActivity.class);
				startActivity(intent);
				arg0.dismiss();
			}
		});
		ibuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});

		ibuilder.create().show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Constants.ACTIVITYFLAG = m_vp.getCurrentItem();

		if (v.getId() == R.id.map_footer_btn1) {

			m_vp.setCurrentItem(0);
			map_footer_btn5.setBackgroundResource(R.drawable.home_enter);
			map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
			map_footer_btn7.setBackgroundResource(R.drawable.chat_default);
			map_footer_btn8.setBackgroundResource(R.drawable.mine_default);
			mTvHome.setTextColor(getResources().getColor(R.color.black));
			mTvColumn.setTextColor(getResources().getColor(R.color.huise3));
			mTvChat.setTextColor(getResources().getColor(R.color.huise3));
			mTvMy.setTextColor(getResources().getColor(R.color.huise3));
		}
		if (v.getId() == R.id.map_footer_btn2) {
			if (0 == MyApplication.getInstance().columnRedDotFlag) {
				badgeView.show();
			}
			if (1 == MyApplication.getInstance().columnRedDotFlag
					|| -1 == MyApplication.getInstance().columnRedDotFlag) {
				MySharedPreference.saveTime(MainActivity.this, System.currentTimeMillis() / 1000 + "");
				badgeView.hide();
			}
			m_vp.setCurrentItem(1);
			map_footer_btn5.setBackgroundResource(R.drawable.home_default);
			map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_a);
			map_footer_btn7.setBackgroundResource(R.drawable.chat_default);
			map_footer_btn8.setBackgroundResource(R.drawable.mine_default);

			mTvHome.setTextColor(getResources().getColor(R.color.huise3));
			mTvColumn.setTextColor(getResources().getColor(R.color.black));
			mTvChat.setTextColor(getResources().getColor(R.color.huise3));
			mTvMy.setTextColor(getResources().getColor(R.color.huise3));
		}
		if (v.getId() == R.id.map_footer_btn3) {

			m_vp.setCurrentItem(2);

			map_footer_btn5.setBackgroundResource(R.drawable.home_default);
			map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
			map_footer_btn7.setBackgroundResource(R.drawable.chat_enter);
			map_footer_btn8.setBackgroundResource(R.drawable.mine_default);

			mTvHome.setTextColor(getResources().getColor(R.color.huise3));
			mTvColumn.setTextColor(getResources().getColor(R.color.huise3));
			mTvChat.setTextColor(getResources().getColor(R.color.black));
			mTvMy.setTextColor(getResources().getColor(R.color.huise3));
		}
		if (v.getId() == R.id.map_footer_btn4) {
			User mUser = MySharedPreference.readUser(MainActivity.this);
			if (mUser == null) {
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
				m_vp.setCurrentItem(m_vp.getCurrentItem());
			} else {
				m_vp.setCurrentItem(2);

				map_footer_btn5.setBackgroundResource(R.drawable.home_default);
				map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
				map_footer_btn7.setBackgroundResource(R.drawable.chat_default);
				map_footer_btn8.setBackgroundResource(R.drawable.mine_enter);

				mTvHome.setTextColor(getResources().getColor(R.color.huise3));
				mTvColumn.setTextColor(getResources().getColor(R.color.huise3));
				mTvChat.setTextColor(getResources().getColor(R.color.huise3));
				mTvMy.setTextColor(getResources().getColor(R.color.black));
			}

		}

		if (v.getId() == R.id.map_footer_btn5) {

			m_vp.setCurrentItem(0);

			map_footer_btn5.setBackgroundResource(R.drawable.home_enter);
			map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
			map_footer_btn7.setBackgroundResource(R.drawable.chat_default);
			map_footer_btn8.setBackgroundResource(R.drawable.mine_default);

			mTvHome.setTextColor(getResources().getColor(R.color.black));
			mTvColumn.setTextColor(getResources().getColor(R.color.huise3));
			mTvChat.setTextColor(getResources().getColor(R.color.huise3));
			mTvMy.setTextColor(getResources().getColor(R.color.huise3));
		}
		if (v.getId() == R.id.map_footer_btn6) {
			m_vp.setCurrentItem(1);
			map_footer_btn5.setBackgroundResource(R.drawable.home_default);
			map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_a);
			map_footer_btn7.setBackgroundResource(R.drawable.chat_default);
			map_footer_btn8.setBackgroundResource(R.drawable.mine_default);

			mTvHome.setTextColor(getResources().getColor(R.color.huise3));
			mTvColumn.setTextColor(getResources().getColor(R.color.black));
			mTvChat.setTextColor(getResources().getColor(R.color.huise3));
			mTvMy.setTextColor(getResources().getColor(R.color.huise3));
		}
		if (v.getId() == R.id.map_footer_btn7) {

			m_vp.setCurrentItem(2);
			map_footer_btn5.setBackgroundResource(R.drawable.home_default);
			map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
			map_footer_btn7.setBackgroundResource(R.drawable.chat_enter);
			map_footer_btn8.setBackgroundResource(R.drawable.mine_default);

			mTvHome.setTextColor(getResources().getColor(R.color.huise3));
			mTvColumn.setTextColor(getResources().getColor(R.color.huise3));
			mTvChat.setTextColor(getResources().getColor(R.color.black));
			mTvMy.setTextColor(getResources().getColor(R.color.huise3));
		}
		if (v.getId() == R.id.map_footer_btn8) {
			User mUser = MySharedPreference.readUser(MainActivity.this);
			if (mUser == null) {
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
				m_vp.setCurrentItem(m_vp.getCurrentItem());
			} else {
				m_vp.setCurrentItem(2);
				map_footer_btn5.setBackgroundResource(R.drawable.home_default);
				map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
				map_footer_btn7.setBackgroundResource(R.drawable.chat_default);
				map_footer_btn8.setBackgroundResource(R.drawable.mine_enter);

				mTvHome.setTextColor(getResources().getColor(R.color.huise3));
				mTvColumn.setTextColor(getResources().getColor(R.color.huise3));
				mTvChat.setTextColor(getResources().getColor(R.color.huise3));
				mTvMy.setTextColor(getResources().getColor(R.color.black));
			}
		}
		// if (v.getId() == R.id.map_footer_btn1) {
		//
		// m_vp.setCurrentItem(0);
		// map_footer_btn5.setBackgroundResource(R.drawable.main_shouye_a);
		// map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
		// map_footer_btn7.setBackgroundResource(R.drawable.main_faxian_b);
		// map_footer_btn8.setBackgroundResource(R.drawable.main_my_b);
		// }
		// if (v.getId() == R.id.map_footer_btn2) {
		//
		// m_vp.setCurrentItem(1);
		// map_footer_btn5.setBackgroundResource(R.drawable.main_shouye_b);
		// map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_a);
		// map_footer_btn7.setBackgroundResource(R.drawable.main_faxian_b);
		// map_footer_btn8.setBackgroundResource(R.drawable.main_my_b);
		// }
		// if (v.getId() == R.id.map_footer_btn3) {
		//
		// m_vp.setCurrentItem(2);
		// map_footer_btn5.setBackgroundResource(R.drawable.main_shouye_b);
		// map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
		// map_footer_btn7.setBackgroundResource(R.drawable.main_faxian_a);
		// map_footer_btn8.setBackgroundResource(R.drawable.main_my_b);
		// }
		// if (v.getId() == R.id.map_footer_btn4) {
		// if (PublicStaticURL.IsLogin == false) {
		// startActivity(new Intent(MainActivity.this, LoginActivity.class));
		// } else {
		// m_vp.setCurrentItem(3);
		// map_footer_btn5.setBackgroundResource(R.drawable.main_shouye_b);
		// map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
		// map_footer_btn7.setBackgroundResource(R.drawable.main_faxian_b);
		// map_footer_btn8.setBackgroundResource(R.drawable.main_my_a);
		// }
		//
		// }
		//
		// if (v.getId() == R.id.map_footer_btn5) {
		//
		// m_vp.setCurrentItem(0);
		//
		// map_footer_btn5.setBackgroundResource(R.drawable.main_shouye_a);
		// map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
		// map_footer_btn7.setBackgroundResource(R.drawable.main_faxian_b);
		// map_footer_btn8.setBackgroundResource(R.drawable.main_my_b);
		// }
		// if (v.getId() == R.id.map_footer_btn6) {
		//
		// m_vp.setCurrentItem(1);
		// map_footer_btn5.setBackgroundResource(R.drawable.main_shouye_b);
		// map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_a);
		// map_footer_btn7.setBackgroundResource(R.drawable.main_faxian_b);
		// map_footer_btn8.setBackgroundResource(R.drawable.main_my_b);
		// }
		// if (v.getId() == R.id.map_footer_btn7) {
		//
		// m_vp.setCurrentItem(2);
		// map_footer_btn5.setBackgroundResource(R.drawable.main_shouye_b);
		// map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
		// map_footer_btn7.setBackgroundResource(R.drawable.main_faxian_a);
		// map_footer_btn8.setBackgroundResource(R.drawable.main_my_b);
		// }
		// if (v.getId() == R.id.map_footer_btn8) {
		// if (PublicStaticURL.IsLogin == false) {
		// startActivity(new Intent(MainActivity.this, LoginActivity.class));
		// } else {
		// m_vp.setCurrentItem(3);
		// map_footer_btn5.setBackgroundResource(R.drawable.main_shouye_b);
		// map_footer_btn6.setBackgroundResource(R.drawable.faxian_lanmu_b);
		// map_footer_btn7.setBackgroundResource(R.drawable.main_faxian_b);
		// map_footer_btn8.setBackgroundResource(R.drawable.main_my_a);
		// }
		// }
	}

	/**
	 * 管理Fragment状态
	 * 
	 * @param index
	 */

	// @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	// @SuppressLint("NewApi")
	// public void showFragment(int index) {
	// FragmentTransaction ft = fm.beginTransaction();
	//
	// // 想要显示一个fragment,先隐藏所有fragment，防止重叠
	// hideFragments(ft);
	//
	// switch (index) {
	// case 1:
	// // 如果fragment1已经存在则将其显示出来
	// if (fragment1 != null)
	// ft.show(fragment1);
	// // 否则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
	// else {
	// fragment1 = new Fragment1();
	// ft.add(R.id.container, fragment1);
	// }
	// break;
	// case 2:
	// if (fragment2 != null)
	// ft.show(fragment2);
	// else {
	// fragment2 = new Fragment2();
	// ft.add(R.id.container, fragment2);
	// }
	// break;
	// case 3:
	// if (fragment3 != null)
	// ft.show(fragment3);
	// else {
	// fragment3 = new Fragment3();
	// ft.add(R.id.container, fragment3);
	// }
	// break;
	// case 4:
	// if (fragment4 != null)
	//
	// ft.show(fragment4);
	//
	// else {
	// fragment4 = new Fragment4();
	// ft.add(R.id.container, fragment4);
	// }
	// break;
	// }
	// ft.commit();
	// }
	//
	// // 当fragment已被实例化，相当于发生过切换，就隐藏起来
	// public void hideFragments(FragmentTransaction ft) {
	// if (fragment1 != null)
	// ft.hide(fragment1);
	// if (fragment2 != null)
	// ft.hide(fragment2);
	// if (fragment3 != null)
	// ft.hide(fragment3);
	// if (fragment4 != null)
	// ft.hide(fragment4);
	// }

	// public void ViewURL() {
	// if (PublicStaticURL.IsLogin == true) {
	// MESSAGE(PublicStaticURL.ZONGmessage + "&uid=" + PublicStaticURL.userid);
	// } else {
	//
	// }
	// }

	/*
	 * // 消息列表 private void MESSAGE(String str) { HttpUtils httpUtils = new
	 * HttpUtils(); httpUtils.send(HttpMethod.GET, str, new
	 * RequestCallBack<String>() {
	 * 
	 * @Override public void onStart() { super.onStart();
	 * 
	 * }
	 * 
	 * @Override public void
	 * onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
	 * // TODO Auto-generated method stub
	 * 
	 * SimpleHUD.showInfoMessage(MainActivity.this,
	 * getString(R.string.server_connect_failed)); }
	 * 
	 * @Override public void onSuccess(ResponseInfo<String> responseInfo) {
	 * 
	 * String str1 = responseInfo.result;// 接口返回的数据 Logger.i(str1); JSONObject
	 * json; try { json = new JSONObject(str1); String code =
	 * json.getString("code"); if ("2".equals(code)) { String result =
	 * json.getString("result"); if (result.equals("")) {
	 * main_tab_unread_tv_layout.setVisibility(View.GONE); } else { if
	 * (PublicStaticURL.IsLogin == true) {
	 * main_tab_unread_tv_layout.setVisibility(View.VISIBLE); }
	 * 
	 * // main_tab_unread_tv.setText(result); // 提醒数量 }
	 * 
	 * } if ("3".equals(code)) {
	 * 
	 * SimpleHUD.showInfoMessage(MainActivity.this,
	 * getString(R.string.get_notifycation_failed)); }
	 * 
	 * } catch (Exception e) { SimpleHUD.showInfoMessage(MainActivity.this,
	 * getString(R.string.app_exception)); Log.e("message", "走catch了");
	 * e.printStackTrace(); } } });
	 * 
	 * }
	 */

	/**
	 * 返回键两次退出
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				Toast.makeText(this, getString(R.string.keydown2_exit), Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				MyApplication.getInstance().exit();
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 解决退出登录后，直接进入fragment1，即首页，而不是fragment4，我的界面。
	class MyChangeFragmentReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			m_vp.setCurrentItem(0);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	public void onResume() {

		user = MySharedPreference.readUser(MainActivity.this);

		if (MyApplication.getInstance().isAddCard) {// 第一注册成功之后是否弹出提示去添加名片的对话框
			show();
			MyApplication.getInstance().isAddCard = false;

		}

		// if (0 == PublicStaticURL.activityFlag) {// 从话题通知点击进入到提醒界面
		// if (user != null) {
		// startActivity(new Intent(this, RemindActivity.class));
		// }
		//
		// PublicStaticURL.activityFlag = -1;
		// }

		if (0 == PublicStaticURL.columnFlag) {// 从“我的话题被收录到栏目”通知界面进入到栏目的话题列表界面;

			// TopicDTO mTopicDTO = (TopicDTO)
			// getIntent().getSerializableExtra("TopicDTO");
			// Intent intent = new Intent(this, ColumnDetailsActivity.class);
			//
			// startActivity(intent);

			if (user != null && PublicStaticURL.mTopicDTO != null) {
				RequestParams params = new RequestParams();
				params.addBodyParameter("uid", user.getUid());
				params.addBodyParameter("tid", PublicStaticURL.mTopicDTO.getTid());
				Loadings.readSection(PublicStaticURL.READSECTION, params);
			}

			m_vp.setCurrentItem(1);
			PublicStaticURL.columnFlag = -1;
		}

		if (0 == PublicStaticURL.isNewVersion || 0 == PublicStaticURL.hasFirstCard) {
			mMyBadgeView.show();
		} else {
			mMyBadgeView.hide();
		}

		// 从忘记页面跳转到首页
		m_vp.setCurrentItem(Constants.ACTIVITYFLAG);
		if (Constants.logoffNotification == 0) {
			m_vp.setCurrentItem(0);
			Constants.logoffNotification = -1;
		}

		if (!TextUtils.isEmpty(MyApplication.getInstance().device_token)) {
			device_token = UmengRegistrar.getRegistrationId(MainActivity.this);
		}
		// 栏目红点
		columnRed();

		MobclickAgent.onResume(this);
		MyNotificationManager.getInstance().initNotify(this);
		MyNotificationManager.getInstance().clearAllNotify();
		super.onResume();
	}

	/*
	 * 
	 * 获取名片列表信息
	 * 
	 */
	public void getCardList(String str) {

		httpUtils.configCurrentHttpCacheExpiry(0);
		// 设置当前请求的缓存时间
		httpUtils.configCurrentHttpCacheExpiry(0 * 1000);
		// 设置默认请求的缓存时间
		httpUtils.configDefaultHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				function();
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

							aCache.put("identityTags", array.toString());

						}

					}
					if ("3".equals(code)) {

					}

				} catch (Exception e) {

					Log.e("message", "走catch了");
					e.printStackTrace();
				} finally {
					function();
				}
			}

		});

	}

	public void function() {
		if (user == null) {
			return;
		}
		String result = aCache.getAsString(user.getUid() + "identityTags");
		if (TextUtils.isEmpty(result)) {
			return;
		}
		List<CardInfo> mList = new ArrayList<CardInfo>();

		mList = CardInfo.getCardInfoList(result);
		if (mList.size() > 0) {
			
			if(PublicStaticURL.isNewVersion==0){
				mMyBadgeView.show();
			}else{
				mMyBadgeView.hide();
			}
			PublicStaticURL.hasFirstCard = 1;
		} else {
			mMyBadgeView.show();
			PublicStaticURL.hasFirstCard = 0;
		}
	}

	/* 栏目红点 */
	private void columnRed() {

		RequestParams params = new RequestParams();
		params.addBodyParameter("start_time",
				MySharedPreference.readTime(MyApplication.getInstance().currentActivity()));
		params.addBodyParameter("end_time", System.currentTimeMillis() / 1000 + "");

		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, PublicStaticURL.COLUMNRED, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// 没有第一张名片时红点提示
				if (user != null) {//
					getCardList(PublicStaticURL.HUOQUHANWANG + "&uid=" + user.getUid());
				} else {
					user = MySharedPreference.readUser(MainActivity.this);
					if (user != null) {
						getCardList(PublicStaticURL.HUOQUHANWANG + "&uid=" + user.getUid());
					}

				}

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i(str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					String result = json.getString("result");
					if ("2".equals(code)) {
						if (!TextUtils.isEmpty(result)) {
							badgeView.show();
						} else {
							badgeView.hide();
						}

					}
					if ("3".equals(code)) {

					}

				} catch (Exception e) {
					Log.e("message", "走catch了");

					e.printStackTrace();
				} finally {
					// 没有第一张名片时红点提示
					if (user != null) {//
						getCardList(PublicStaticURL.HUOQUHANWANG + "&uid=" + user.getUid());
					} else {
						user = MySharedPreference.readUser(MainActivity.this);
						if (user != null) {
							getCardList(PublicStaticURL.HUOQUHANWANG + "&uid=" + user.getUid());
						}

					}
				}
			}

		});

	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// must store the new intent unless getIntent() will return the old one
		setIntent(intent);

	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	// @Override
	// public void notice(String str) {
	//
	// try {
	// JSONObject ob = new JSONObject(str);
	// String result = ob.getString("result");
	// StrongPlayDTO mStrongPlayDTO = StrongPlayDTO.getStrongPlayDTO(result);
	// if (MyApplication.getInstance().currentActivity() != null) {
	//
	// CustomDialog.Builder ibuilder = new
	// CustomDialog.Builder(MyApplication.getInstance().currentActivity());
	//
	// ibuilder.setTitle("提示");
	// ibuilder.setMessage(mStrongPlayDTO.getContent());
	// ibuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface arg0, int arg1) {
	// Intent intent = new Intent(MyApplication.getInstance().currentActivity(),
	// LoginActivity.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// startActivity(intent);
	// // MySharedPreference.saveUser(context, new User());
	// // PublicStaticURL.IsLogin = false;
	// arg0.dismiss();
	// }
	// });
	//
	// CustomDialog dialog = ibuilder.create();
	// dialog.setCanceledOnTouchOutside(false);
	// dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	// dialog.show();
	// dialog.setOnKeyListener(new OnKeyListener() {
	//
	// @Override
	// public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
	// {
	//
	// if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	// });
	// }
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

}
