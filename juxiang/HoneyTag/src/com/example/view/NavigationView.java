package com.example.view;

import com.example.honeytag1.R;

import android.content.Context;
import android.graphics.LinearGradient;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @Description描述:导航栏组件,目前包括返回键,标题,右侧按钮.其中:
 * @Author作者:dbj
 * @Date日期:2016-4-14 上午:10:28
 */
public class NavigationView extends RelativeLayout implements android.view.View.OnClickListener {
	private LinearLayout mLlLeft, mLlRight;
	private ImageView backView;
	private TextView titleView;
	private ImageView rightView;
	private TextView rightTv;

	public NavigationView(Context context) {
		this(context, null);
	}

	public NavigationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.navigation_view, this, true);
		mLlRight = (LinearLayout) view.findViewById(R.id.mLlRight);
		mLlLeft = (LinearLayout) view.findViewById(R.id.mLlLeft);
		backView = (ImageView) view.findViewById(R.id.iv_nav_back);
		titleView = (TextView) view.findViewById(R.id.tv_nav_title);
		rightView = (ImageView) view.findViewById(R.id.iv_nav_right);
		rightTv = (TextView) view.findViewById(R.id.tv_nav_right);

		mLlRight.setOnClickListener(this);
		mLlLeft.setOnClickListener(this);
		//backView.setOnClickListener(this);
		//rightView.setOnClickListener(this);
		//rightTv.setOnClickListener(this);

	}

	/**
	 * 获取返回按钮
	 * 
	 * @return
	 */
	public ImageView getBackView() {
		return backView;
	}

	/**
	 * 获取标题控件
	 * 
	 * @return
	 */
	public TextView getTitleView() {
		return titleView;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		titleView.setText(title);
	}

	/**
	 * 获取右侧按钮,默认不显示
	 * 
	 * @return
	 */
	public ImageView getRightView() {
		return rightView;
	}

	/**
	 * 获取右侧文本按钮,默认不显示
	 * 
	 * @return
	 */
	public TextView getRightTextView() {
		return rightTv;
	}

	private ClickCallback callback;
	private ClickCallbackLeft callbackLeft;
	private ClickCallbackRight callbackRight;

	/**
	 * 设置按钮点击回调接口
	 * 
	 * @param callback
	 */
	public void setClickCallback(ClickCallback callback) {
		this.callback = callback;
	}

	/**
	 * 设置按钮点击回调接口
	 * 
	 * @param callbackLeft
	 */
	public void setClickCallbackLeft(ClickCallbackLeft callbackLeft) {
		this.callbackLeft = callbackLeft;
	}

	/**
	 * 设置按钮点击回调接口
	 * 
	 * @param callback
	 */
	public void setClickCallbackRight(ClickCallbackRight callbackRight) {
		this.callbackRight = callbackRight;
	}

	/**
	 * 导航栏点击回调接口 </br>
	 * 如若需要标题可点击,可再添加
	 * 
	 * @author Asia
	 *
	 */
	public static interface ClickCallback {
		/**
		 * 点击返回按钮回调
		 */
		void onBackClick();

		void onRightClick();

		void onRightTVClick();
	}

	public static interface ClickCallbackLeft {
		/**
		 * 点击返回按钮回调
		 */
		void onBackClick();

	}

	public static interface ClickCallbackRight {
		/**
		 * 右边按钮
		 */

		void onRightClick();

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.iv_nav_back) {
			callback.onBackClick();
			return;
		}
		if (id == R.id.iv_nav_right) {
			callback.onRightClick();
			return;
		}
		if (id == R.id.tv_nav_right) {
			callback.onRightTVClick();
			
			return;
		}
		if (id == R.id.mLlLeft&&callbackLeft!=null) {
			callbackLeft.onBackClick();
			return;
		}

		 if (v.getId() == R.id.mLlRight&&callbackRight!=null) {
		 callbackRight.onRightClick();
		 }

	}

}
