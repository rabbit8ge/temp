package com.example.setup;

import info.wangchen.simplehud.SimpleHUD;
import org.json.JSONObject;
import com.example.baseactivity.BaseActivity;
import com.example.dialog.SpotsDialog;
import com.example.honeytag1.R;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.view.NavigationView.ClickCallbackLeft;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @Description描述:意见反馈
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class FeedbackActivity extends BaseActivity implements OnClickListener {

	private EditText mEtFeed;
	private Button mBtnSubmit; // 提交
	private SpotsDialog mSpotsDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mSpotsDialog = new SpotsDialog(FeedbackActivity.this);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_feedback);
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void initView() {
		super.initView();
		initTopView();

		mRelNavigationView.setTitle(getString(R.string.feedBack));
		mEtFeed = (EditText) findViewById(R.id.feed_et);
		mBtnSubmit = (Button) findViewById(R.id.feed_feed);

		setColor("", mBtnSubmit);

		mBtnSubmit.setOnClickListener(this);

		mRelNavigationView.setClickCallbackLeft(new ClickCallbackLeft() {

			@Override
			public void onBackClick() {
				MyApplication.getInstance().popActivity(FeedbackActivity.this);

			}
		});
		mEtFeed.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				setColor(s.toString(), mBtnSubmit);

			}
		});

	}

	/*
	 * 当输入框中是否有内容的时候设置提交按钮的状态
	 */
	public void setColor(String s, Button mSubmit) {

		if (TextUtils.isEmpty(s)) {
			GradientDrawable myGrad = (GradientDrawable) mSubmit.getBackground();// 获取shape中button的背景颜色
			myGrad.setStroke((int) 0.5, getResources().getColor(R.color.huise9));
			myGrad.setColor(getResources().getColor(R.color.huise9));
			mSubmit.setClickable(false);
			mSubmit.setEnabled(false);
		} else {
			GradientDrawable myGrad = (GradientDrawable) mSubmit.getBackground();// 获取shape中button的背景颜色
			myGrad.setStroke((int) 0.5, getResources().getColor(R.color.blue2));
			myGrad.setColor(getResources().getColor(R.color.blue2));
			mSubmit.setClickable(true);
			mSubmit.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.feed_feed:
			String content;
			content = mEtFeed.getText().toString();
			RequestParams params = new RequestParams();
			params.addBodyParameter("content", content);
			feedBack(PublicStaticURL.FEED, params);
			break;

		default:
			break;
		}
	}

	// 建议反馈

	private void feedBack(String str, RequestParams params) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				mSpotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				mSpotsDialog.dismiss();
				SimpleHUD.showInfoMessage(FeedbackActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				mSpotsDialog.dismiss();
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i(str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");
					if ("2".equals(code)) {
						finish();
						Toast.makeText(FeedbackActivity.this, getString(R.string.suggestion), Toast.LENGTH_SHORT)
								.show();
					}
					if ("3".equals(code)) {

						SimpleHUD.showInfoMessage(FeedbackActivity.this, getString(R.string.tell_suggestion));
					}

				} catch (Exception e) {
					SimpleHUD.showInfoMessage(FeedbackActivity.this, getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		umengResume(this, getClass().toString());
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		umengPause(this, getClass().toString());
	}

	@Override
	protected void onDestroy() {

		if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
			mSpotsDialog.cancel();
			mSpotsDialog = null;
		}
		super.onDestroy();
	}

}
