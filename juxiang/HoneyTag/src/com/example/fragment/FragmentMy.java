package com.example.fragment;

import info.wangchen.simplehud.SimpleHUD;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.honeytag1.R;
//import com.example.honeytag1.MainActivity.MyViewPagerAdapter;
import com.example.setup.SettingActivity;
import com.example.utils.PublicStaticURL;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class FragmentMy extends Fragment implements OnClickListener {

	/**
	 * 我的页面
	 */
	private ImageView my_shezhi; // 设置按钮
	private FragmentHuaTi fragmentHuaTi;
	private FragmentParticipateIn fragmentShouCang;
	private FragmentGuanZhu fragmentGuanZhu;
	private android.app.FragmentManager fm;
	private LinearLayout my_huati, my_shoucang, my_guanzhu; // 话题，收藏，关注
	private TextView txt_huati, txt_shoucang, txt_guanzhu, mytext;
	private CheckBox mCheckMyTopic, mCheckParticipate, mCheckFocus;
	private ImageView mIvTopic, mIvParticipation, mIvFocusOn;
	// Typeface font;
	View view;
	private ViewPager m_vp;
	private FragmentGuanZhu fGuanZhu;
	private FragmentHuaTi fHuaTi;
	private FragmentParticipateIn fParticipateIn;
	private ArrayList<Fragment> fragmentList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null)

			view = inflater.inflate(R.layout.fragment_my, null);
		else {
			ViewGroup group = (ViewGroup) view.getParent();
			group.removeView(view);
		}
		// 初始化控件
		m_vp = (ViewPager) view.findViewById(R.id.viewpager4);
		mytext = (TextView) view.findViewById(R.id.mytext);
		// AssetManager assetManager = getActivity().getAssets();
		// font = Typeface.createFromAsset(assetManager, "fonts/STXIHEI.TTF");
		// mytext.setTypeface(font);
		// txt_huati = (TextView) view.findViewById(R.id.txt_huati);
		// txt_shoucang = (TextView) view.findViewById(R.id.txt_shoucang);
		// txt_guanzhu = (TextView) view.findViewById(R.id.txt_guanzhu);

		mCheckMyTopic = (CheckBox) view.findViewById(R.id.checkBox01);
		mCheckParticipate = (CheckBox) view.findViewById(R.id.checkBox02);
		mCheckFocus = (CheckBox) view.findViewById(R.id.checkBox03);

		mIvTopic = (ImageView) view.findViewById(R.id.iv_topic);
		mIvParticipation = (ImageView) view.findViewById(R.id.iv_participation);
		mIvFocusOn = (ImageView) view.findViewById(R.id.iv_focusOn);

		my_huati = (LinearLayout) view.findViewById(R.id.my_huati);
		my_shoucang = (LinearLayout) view.findViewById(R.id.my_shoucang);
		my_guanzhu = (LinearLayout) view.findViewById(R.id.my_guanzhu);
		my_shezhi = (ImageView) view.findViewById(R.id.my_shezhi);

		fGuanZhu = new FragmentGuanZhu();
		fHuaTi = new FragmentHuaTi();
		fParticipateIn = new FragmentParticipateIn();
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(fHuaTi);
		fragmentList.add(fParticipateIn);
		fragmentList.add(fGuanZhu);

		if (mCheckMyTopic.isChecked()) {
			mCheckMyTopic.setTextColor(getResources().getColor(R.color.black));
			mCheckParticipate.setTextColor(getResources().getColor(R.color.huise5));
			mCheckFocus.setTextColor(getResources().getColor(R.color.huise5));
		}
		if (mCheckParticipate.isChecked()) {
			mCheckMyTopic.setTextColor(getResources().getColor(R.color.huise5));
			mCheckParticipate.setTextColor(getResources().getColor(R.color.black));
			mCheckFocus.setTextColor(getResources().getColor(R.color.huise5));
		}
		if (mCheckFocus.isChecked()) {
			mCheckMyTopic.setTextColor(getResources().getColor(R.color.huise5));
			mCheckParticipate.setTextColor(getResources().getColor(R.color.huise5));
			mCheckFocus.setTextColor(getResources().getColor(R.color.black));
		}

		m_vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

				if (arg0 == 0) {
					// txt_huati.setBackgroundResource(R.drawable.my_huati);
					// txt_shoucang.setBackgroundResource(R.drawable.my_shoucang_b);
					// txt_guanzhu.setBackgroundResource(R.drawable.my_guanzhu_b);
					// mCheckMyTopic.setChecked(true);
					// mCheckParticipate.setChecked(false);
					// mCheckFocus.setChecked(false);
					mIvTopic.setVisibility(View.GONE);
					mIvParticipation.setVisibility(View.GONE);
					mIvFocusOn.setVisibility(View.GONE);

					mCheckMyTopic.setTextColor(getResources().getColor(R.color.black));
					mCheckParticipate.setTextColor(getResources().getColor(R.color.huise5));
					mCheckFocus.setTextColor(getResources().getColor(R.color.huise5));
				} else if (arg0 == 1) {
					// txt_huati.setBackgroundResource(R.drawable.my_huati_b);
					// txt_shoucang.setBackgroundResource(R.drawable.my_shoucang);
					// txt_guanzhu.setBackgroundResource(R.drawable.my_guanzhu_b);
					// mCheckMyTopic.setChecked(false);
					// mCheckParticipate.setChecked(true);
					// mCheckFocus.setChecked(false);

					mIvTopic.setVisibility(View.GONE);
					mIvParticipation.setVisibility(View.GONE);
					mIvFocusOn.setVisibility(View.GONE);

					mCheckMyTopic.setTextColor(getResources().getColor(R.color.huise5));
					mCheckParticipate.setTextColor(getResources().getColor(R.color.black));
					mCheckFocus.setTextColor(getResources().getColor(R.color.huise5));
				} else if (arg0 == 2) {
					// txt_huati.setBackgroundResource(R.drawable.my_huati_b);
					// txt_shoucang.setBackgroundResource(R.drawable.my_shoucang_b);
					// txt_guanzhu.setBackgroundResource(R.drawable.my_guanzhu);
					// mCheckMyTopic.setChecked(false);
					// mCheckParticipate.setChecked(false);
					// mCheckFocus.setChecked(true);

					mIvTopic.setVisibility(View.GONE);
					mIvParticipation.setVisibility(View.GONE);
					mIvFocusOn.setVisibility(View.GONE);
					mCheckMyTopic.setTextColor(getResources().getColor(R.color.huise5));
					mCheckParticipate.setTextColor(getResources().getColor(R.color.huise5));
					mCheckFocus.setTextColor(getResources().getColor(R.color.black));
				}
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

		m_vp.setAdapter(new MyViewPagerAdapter(getChildFragmentManager()));
		m_vp.setOffscreenPageLimit(3);
		Viewinit();

		return view;
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

	@SuppressLint("NewApi")
	public void Viewinit() {
		// 默认显示的fragment
		// fm = getFragmentManager();
		// showFragment(1);
		// 绑定监听事件
		my_shezhi.setOnClickListener(this);
		my_huati.setOnClickListener(this);
		my_shoucang.setOnClickListener(this);
		my_guanzhu.setOnClickListener(this);

	}

	/**
	 * 监听事件
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.my_shezhi:
			if (PublicStaticURL.IsLogin == false) {
				SimpleHUD.showInfoMessage(getActivity(), getString(R.string.not_login));
			} else {
				startActivity(new Intent(getActivity(), SettingActivity.class));
			}

			break;
		case R.id.my_huati:
			if (PublicStaticURL.IsLogin == false) {
				SimpleHUD.showInfoMessage(getActivity(), getString(R.string.not_login));
			} else {
				m_vp.setCurrentItem(0);
				// txt_huati.setBackgroundResource(R.drawable.my_huati);
				// txt_shoucang.setBackgroundResource(R.drawable.my_shoucang_b);
				// txt_guanzhu.setBackgroundResource(R.drawable.my_guanzhu_b);

				// mCheckMyTopic.setChecked(true);
				// mCheckParticipate.setChecked(false);
				// mCheckFocus.setChecked(false);

				mIvTopic.setVisibility(View.GONE);
				mIvParticipation.setVisibility(View.GONE);
				mIvFocusOn.setVisibility(View.GONE);

				mCheckMyTopic.setTextColor(getResources().getColor(R.color.black));
				mCheckParticipate.setTextColor(getResources().getColor(R.color.huise5));
				mCheckFocus.setTextColor(getResources().getColor(R.color.huise5));
			}

			break;
		case R.id.my_shoucang:
			if (PublicStaticURL.IsLogin == false) {
				SimpleHUD.showInfoMessage(getActivity(), getString(R.string.not_login));
			} else {
				m_vp.setCurrentItem(1);
				// txt_huati.setBackgroundResource(R.drawable.my_huati_b);
				// txt_shoucang.setBackgroundResource(R.drawable.my_shoucang);
				// txt_guanzhu.setBackgroundResource(R.drawable.my_guanzhu_b);

				// mCheckMyTopic.setChecked(false);
				// mCheckParticipate.setChecked(true);
				// mCheckFocus.setChecked(false);
				mIvTopic.setVisibility(View.GONE);
				mIvParticipation.setVisibility(View.GONE);
				mIvFocusOn.setVisibility(View.GONE);
				mCheckMyTopic.setTextColor(getResources().getColor(R.color.huise5));
				mCheckParticipate.setTextColor(getResources().getColor(R.color.black));
				mCheckFocus.setTextColor(getResources().getColor(R.color.huise5));
			}
			break;
		case R.id.my_guanzhu:
			if (PublicStaticURL.IsLogin == false) {
				SimpleHUD.showInfoMessage(getActivity(), getString(R.string.not_login));
			} else {
				m_vp.setCurrentItem(2);
				// txt_huati.setBackgroundResource(R.drawable.my_huati_b);
				// txt_shoucang.setBackgroundResource(R.drawable.my_shoucang_b);
				// txt_guanzhu.setBackgroundResource(R.drawable.my_guanzhu);

				// mCheckMyTopic.setChecked(false);
				// mCheckParticipate.setChecked(false);
				// mCheckFocus.setChecked(true);
				mIvTopic.setVisibility(View.GONE);
				mIvParticipation.setVisibility(View.GONE);
				mIvFocusOn.setVisibility(View.GONE);

				mCheckMyTopic.setTextColor(getResources().getColor(R.color.huise5));
				mCheckParticipate.setTextColor(getResources().getColor(R.color.huise5));
				mCheckFocus.setTextColor(getResources().getColor(R.color.black));
			}
			break;

		}

	}

	// /**
	// * 管理Fragment状态
	// *
	// * @param index
	// */
	// @SuppressLint("NewApi")
	// public void showFragment(int index) {
	// android.app.FragmentTransaction ft = fm.beginTransaction();
	//
	// // 想要显示一个fragment,先隐藏所有fragment，防止重叠
	// hideFragments(ft);
	//
	// switch (index) {
	// case 1:
	// // 如果fragment1已经存在则将其显示出来
	// if (fragmentHuaTi != null)
	// ft.show(fragmentHuaTi);
	// // 否则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
	// else {
	// fragmentHuaTi = new FragmentHuaTi();
	// ft.add(R.id.my_fragment, fragmentHuaTi);
	// }
	// break;
	//
	// case 2:
	// if (fragmentShouCang != null)
	// ft.show(fragmentShouCang);
	// else {
	// fragmentShouCang = new FragmentParticipateIn();
	// ft.add(R.id.my_fragment, fragmentShouCang);
	// }
	// break;
	//
	// case 3:
	// // 如果fragment1已经存在则将其显示出来
	// if (fragmentGuanZhu != null)
	// ft.show(fragmentGuanZhu);
	// // 否则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
	// else {
	// fragmentGuanZhu = new FragmentGuanZhu();
	// ft.add(R.id.my_fragment, fragmentGuanZhu);
	// }
	// break;
	// }
	// ft.commit();
	// }
	//
	// // 当fragment已被实例化，相当于发生过切换，就隐藏起来
	// @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	// @SuppressLint("NewApi")
	// public void hideFragments(android.app.FragmentTransaction ft) {
	// if (fragmentHuaTi != null)
	// ft.hide(fragmentHuaTi);
	//
	// if (fragmentShouCang != null)
	// ft.hide(fragmentShouCang);
	//
	// if (fragmentGuanZhu != null)
	// ft.hide(fragmentGuanZhu);
	//
	// }
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().toString()); // 统计页面，""为页面名称，可自定义
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().toString());
	}
}
