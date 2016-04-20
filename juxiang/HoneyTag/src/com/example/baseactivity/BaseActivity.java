package com.example.baseactivity;


import com.example.dto.ACache;
import com.example.honeytag1.R;
import com.example.utils.MyApplication;
import com.example.view.NavigationView;
import com.example.view.NavigationView.ClickCallbackLeft;
import com.lidroid.xutils.HttpUtils;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Description描述:包含了activity公共的属性和方法
 * @Author作者:dbj
 * @Date日期:2016-4-14 上午:10:28
 */
public class BaseActivity extends com.example.honeytag1.BaseActivity {
	public Context mContext;
	public NavigationView mRelNavigationView;
	public LinearLayout mLlLeft, mLlRight;
	public ImageView mIvLeft, mIvRight;
	public TextView mTvRight, mTvTitle;
	public ACache mACache; // 本地缓存
	public HttpUtils mHttpUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		mContext = this;
		mACache = ACache.get(this);
		mHttpUtils = MyApplication.getInstance().httpUtils;
		
		MyApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		init();// 初始化
	}

	protected void init() {
		initView();

	}

	protected void initView() {

	}
	
	/*初始化顶部导航栏*/
	protected void initTopView() {
		// TODO Auto-generated method stu
		mRelNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mTvTitle = mRelNavigationView.getRightTextView();
		mTvRight = mRelNavigationView.getRightTextView();
		mIvLeft = mRelNavigationView.getBackView();
		mIvRight = mRelNavigationView.getRightView();

		mRelNavigationView.setClickCallbackLeft(new ClickCallbackLeft() {

			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(MyApplication.getInstance().currentActivity());

			}
		});

	}

	@Override
	public void loadXml() {

	}

	@Override
	public void loadData() {

	}



}
