package com.example.picture;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import com.example.honeytag1.BaseActivity;
import com.example.honeytag1.R;
import com.example.utils.MyApplication;
import com.example.utils.PhotoUtils;
import com.hanvon.utils.BitmapUtil;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class RotatingActivity extends BaseActivity implements OnClickListener {
	private ImageView mIvPicture;
	private Button mBtnRotating, mBtnSave, mBtnDiscard;
	private String picPath;
	private Bitmap mBitmap;
	private Uri mSaveUri = null;
	private ContentResolver mContentResolver;
	private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG; // only
	// used
	// with
	// mSaveUri

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		picPath = getIntent().getStringExtra("picPath");
		setContentView(R.layout.activity_rotating);
		init();

	}

	private void init() {
		final BitmapFactory.Options options1 = new BitmapFactory.Options();
		options1.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picPath, options1);
		options1.inSampleSize = BitmapUtil.calculateInSampleSize(options1, 1280, 720);
		options1.inJustDecodeBounds = false;
		mBitmap = BitmapFactory.decodeFile(picPath, options1);
		File file = new File(picPath);
		mSaveUri = Uri.fromFile(file);
		if (mSaveUri != null) {
			String outputFormatString = Bitmap.CompressFormat.JPEG.toString();
			if (outputFormatString != null) {
				mOutputFormat = Bitmap.CompressFormat.valueOf(outputFormatString);
			}
		}
		mContentResolver = getContentResolver();

		initView();

	}

	private void initView() {
		mIvPicture = (ImageView) findViewById(R.id.iv_picture);
		mBtnRotating = (Button) findViewById(R.id.rotating);

		mIvPicture.setImageBitmap(mBitmap);

		mBtnRotating.setOnClickListener(this);
		findViewById(R.id.save).setOnClickListener(this);
		findViewById(R.id.discard).setOnClickListener(this);

	}

	Thread mThread;

	private void saveOutput(final Bitmap croppedImage) {
		if (mThread != null) {
			mThread.interrupt();
			mThread = null;
		}

		mThread = new Thread() {

			@Override
			public void run() {

				if (mSaveUri != null) {
					OutputStream outputStream = null;
					try {
						outputStream = mContentResolver.openOutputStream(mSaveUri);
						if (outputStream != null) {
							croppedImage.compress(mOutputFormat, 75, outputStream);
						}
					} catch (IOException ex) {
						// TODO: report error to caller
						// Log.e(TAG, "Cannot open file: " + mSaveUri, ex);
					} finally {
						// Util.closeSilently(outputStream);
					}

					setResult(4);
					MyApplication.getInstance().popActivity(RotatingActivity.this);
				}

			}
		};
		mThread.start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:

			saveOutput(mBitmap);
			break;
		case R.id.rotating:
			mBitmap = PhotoUtils.rotatingBitmap(mBitmap, 90);
			mIvPicture.setImageBitmap(mBitmap);

			break;

		default:
		case R.id.discard:
			MyApplication.getInstance().popActivity(RotatingActivity.this);
			break;
		}

	}

	@Override
	public void loadXml() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		umengResume(this, getClass().toString());
		super.onResume();
	}

	@Override
	protected void onPause() {
		umengResume(this, getClass().toString());
		super.onPause();
	}

}
