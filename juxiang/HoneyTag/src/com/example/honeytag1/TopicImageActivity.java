package com.example.honeytag1;

import com.example.utils.MyApplication;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import net.tsz.afinal.FinalBitmap;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.FinishCallBack;

public class TopicImageActivity extends BaseActivity implements FinishCallBack {
	private PhotoView photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void loadXml() {
		// TODO Auto-generated method stub
		setContentView(R.layout.photo_layout);
		photo = (PhotoView) findViewById(R.id.photo);
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		photo.setFinishCallBack(this);
		String imageUrl = getIntent().getStringExtra("imageUrl");
		if (imageUrl != null) {
			FinalBitmap finalBitmap = FinalBitmap.create(TopicImageActivity.this);
			finalBitmap.display(photo, imageUrl);
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
	public void finishActivity() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
