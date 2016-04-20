package com.example.honeytag1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.example.honeytag1.R;
import com.example.utils.Logger;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebViewActivity extends BaseActivity implements OnClickListener {
	private WebView webView;
	private Intent intent;
	private ProgressBar progressBar;
	private TextView web_title;
	private Button web_back_btn;
	private LinearLayout webroot;
	private Context context;
	private LinearLayout web_fanhui;
	private ImageButton web_btn;

	@SuppressLint("JavascriptInterface")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(1);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview_layout);
		context = this;
		webroot = (LinearLayout) findViewById(R.id.web_root);
		progressBar = (ProgressBar) findViewById(R.id.progressbar);
		webView = (WebView) findViewById(R.id.webview);
		web_title = (TextView) findViewById(R.id.title);
		web_fanhui = (LinearLayout) findViewById(R.id.web_fanhui);
		web_btn = (ImageButton) findViewById(R.id.web_btn);
		web_fanhui.setOnClickListener(this);
		web_btn.setOnClickListener(this);
		WebChromeClient wvcc = new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				web_title.setText(title);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (!(newProgress == 100)) {
					progressBar.setVisibility(View.VISIBLE);
					progressBar.setProgress(newProgress);
				} else if (newProgress == 100) {
					progressBar.setVisibility(View.GONE);
				}
			}

		};
		webView.setWebChromeClient(wvcc);
		webView.addJavascriptInterface(new Handler(), "handler");
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				view.loadUrl("javascript:window.handler.show('<html>'+"
						+ "document.getElementsByTagName('html')[0].innerHTML+'</html>');");
			}
		});

		WebSettings sett = webView.getSettings();
		sett.setCacheMode(WebSettings.LOAD_NO_CACHE);
		sett.setJavaScriptEnabled(true);
		sett.setAllowFileAccess(true);
		sett.setSupportZoom(true);
		sett.setBuiltInZoomControls(true);
		intent = getIntent();
		String url = intent.getStringExtra("htmlURL");
		webView.loadUrl(url);
	}

	public static void callMe(String url, Context act) {
		Intent intent = new Intent(act, WebViewActivity.class);
		intent.putExtra("htmlURL", url);
		act.startActivity(intent);
	}

	class Handler {
		@JavascriptInterface
		public void show(String html) {
			Logger.i("抓取html5数据======>：" + html);
			Document doc = Jsoup.parse(html);// 解析HTML字符串返回一个Document实现

			Element ema = doc.getElementById("title");
			Logger.i("抓取隐藏属性 text===>" + ema.val());

		}
	}

	/** 改写物理按键——返回的逻辑 **/
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();// 返回上一页面
				return true;
			} else {
				finish();// 退出程序
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.web_fanhui:
			finish();

			break;

		case R.id.web_btn:
			finish();

			break;
		default:
			break;
		}
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
	public void loadXml() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}
}
