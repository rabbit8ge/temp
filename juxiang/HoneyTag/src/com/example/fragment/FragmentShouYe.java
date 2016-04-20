package com.example.fragment;

import java.util.ArrayList;
import java.util.List;
import com.example.adapter.CommunityAndFoundAdapter;
import com.example.config.MySharedPreference;
import com.example.dto.User;
import com.example.honeytag1.R;
import com.example.honeytag1.PublishActivity;
import com.example.honeytag1.RemindActivity;
import com.example.http.Loadings;
import com.example.my.LoginActivity;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.umeng.analytics.MobclickAgent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @Description描述:
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class FragmentShouYe extends Fragment implements OnClickListener {
	private ViewPager mViewPager;
	private RadioGroup mRadioGroup;
	private RelativeLayout mRelaRemind;
	private LinearLayout mLinearPost;
	public static TextView main_tab_unread_tv;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// View view = inflater.inflate(R.layout.fragment_one, null);
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
			return view;
		}
		view = inflater.inflate(R.layout.fragment_shouye, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		init();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	private void init() {
		initView();
		fillAdapter();

	}

	private void initView() {
		// List<Fragment> mList = new ArrayList<Fragment>();
		mRadioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup1);
		mViewPager = (ViewPager) getView().findViewById(R.id.viewpager1);
		mRelaRemind = (RelativeLayout) getView().findViewById(R.id.rela_remind);
		mLinearPost = (LinearLayout) getView().findViewById(R.id.linear_post);
		main_tab_unread_tv = (TextView) getView().findViewById(R.id.main_tab_unread_tv);
		mRelaRemind.setOnClickListener(this);
		mLinearPost.setOnClickListener(this);

		((RadioButton) mRadioGroup.findViewById(R.id.radio0)).setChecked(true);
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (2 == arg0) {
					switch (mViewPager.getCurrentItem()) {
					case 0:
						((RadioButton) mRadioGroup.findViewById(R.id.radio0)).setChecked(true);

						break;

					default:
						((RadioButton) mRadioGroup.findViewById(R.id.radio1)).setChecked(true);

						break;
					}
				}

			}
		});

		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio0:
					mViewPager.setCurrentItem(0, true);
					((RadioButton) mRadioGroup.findViewById(R.id.radio0))
							.setTextColor(getResources().getColor(R.color.black));
					((RadioButton) mRadioGroup.findViewById(R.id.radio1))
							.setTextColor(getResources().getColor(R.color.huise4));

					break;

				default:
					mViewPager.setCurrentItem(1, true);
					((RadioButton) mRadioGroup.findViewById(R.id.radio0))
							.setTextColor(getResources().getColor(R.color.huise4));
					((RadioButton) mRadioGroup.findViewById(R.id.radio1))
							.setTextColor(getResources().getColor(R.color.black));
					break;
				}

			}
		});

	}

	private void fillAdapter() {
		CommunityAndFoundAdapter mCommunityAndFoundAdapter = new CommunityAndFoundAdapter(getChildFragmentManager(),
				getFragmentList());
		mViewPager.setAdapter(mCommunityAndFoundAdapter);

	}

	/*
	 * 获得Fragment列表
	 */
	private List<Fragment> getFragmentList() {
		List<Fragment> mList = new ArrayList<Fragment>();
		mList.add(new FragmentSheQu());
		mList.add(new FragmentReDian());
		return mList;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rela_remind:// 提醒
			if (PublicStaticURL.IsLogin == true) {
				startActivity(new Intent(getActivity(), RemindActivity.class));
			} else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}

			break;
		case R.id.linear_post:// 发表话题
			if (PublicStaticURL.IsLogin == false) {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			} else {
				startActivity(new Intent(getActivity(), PublishActivity.class));
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onResume() {
		if (TextUtils.isEmpty(MyApplication.getInstance().count)) {
			main_tab_unread_tv.setBackgroundResource(R.drawable.circle_gray);
			main_tab_unread_tv.setText("0");
			main_tab_unread_tv.setTextColor(Color.GRAY);

		} else {
			if (PublicStaticURL.IsLogin == true) {
				main_tab_unread_tv.setBackgroundResource(R.drawable.circle_red);
				main_tab_unread_tv.setTextColor(Color.RED);
				main_tab_unread_tv.setText(MyApplication.getInstance().count); // 提醒数量
			}
		}
		User mUser = MySharedPreference.readUser(getActivity());
		if (mUser != null) {
			Loadings.getCount(PublicStaticURL.ZONGmessage + "&uid=" + mUser.getUid());

		}
		super.onResume();

		MobclickAgent.onPageStart(getClass().toString()); // 统计页面，"MainScreen"为页面名称，可自定义
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().toString());
	}
}
