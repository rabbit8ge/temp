package com.example.setup;

import info.wangchen.simplehud.SimpleHUD;
import net.tsz.afinal.FinalHttp;
import zhy.com.highlight.util.L;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.json.JSONObject;
import com.example.baseactivity.BaseActivity;
import com.example.dialog.ActionSheetDialog;
import com.example.dialog.SpotsDialog;
import com.example.dialog.ActionSheetDialog.OnSheetItemClickListener;
import com.example.dialog.ActionSheetDialog.SheetItemColor;
import com.example.honeytag1.R;
import com.example.picture.CropImageActivity;
import com.example.picture.RotatingActivity;
import com.example.utils.Logger;
import com.example.utils.PhotoUtils;
import com.example.utils.PublicStaticURL;
import com.hanvon.HWCloudManager;
import com.hanvon.utils.BitmapUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 更改名片
 */

public class AddbusinesscardActivity extends BaseActivity implements OnClickListener {

	/**
	 * 调用汉王接口，属于非付费模式，每天2000次扫描名片，超过上限扫描失败！
	 */
	private LinearLayout hanwang_fanhui;// 返回按钮
	private ImageButton hanwang_btn, hw_xiangce, hw_xiangji; // 返回按钮
	private ImageView hanwang_image; // 名片图片
	private Button hanwang_wancheng; // 完成注册
	private TextView hanwang_title;
	private SpotsDialog mSpotsDialog;
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	public static final String separator = File.separator;// 分隔符
	public static final String MNT = Environment.getExternalStorageDirectory().toString();
	public static final String ROOT = MNT + separator + "行家说";

	/* 头像名称 */
	public static final String IMAGE_FILE_NAME = ROOT + separator + "ddfaceImage.jpg";
	/* 中专图片名称 */
	public static final String IMG = ROOT + separator + "ddphoto.jpg";
	File tempFile = new File(IMG);

	private DiscernHandler discernHandler;
	private String picPath = null;
	private String result = null;
	private String path = "";
	private String code = null;
	public static boolean Image = false;

	private Uri photoUri;
	private HWCloudManager hwCloudManagerBcard; // 名片
	private Bitmap bitmap1;
	private Bitmap bitmapoptions;

	// private String photoImagePath;
	private int type = -1;// 区分是相册图片还是拍照图片
	private FinalHttp fh = new FinalHttp();
	private Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_add_businesscard);
		mSpotsDialog = new SpotsDialog(this);
		super.onCreate(savedInstanceState);

		fh.configTimeout(10 * 1000);
	}

	@Override
	protected void initView() {
		super.initView();
		// TODO Auto-generated method stub
		// hwCloudManagerBcard = new HWCloudManager(this,
		// "69805d8f-cbdc-4130-884d-49464ddeed1c");
		hwCloudManagerBcard = new HWCloudManager(this, "3c862c84-c669-4fa4-8ae5-ea067a744b72");
		discernHandler = new DiscernHandler();
		hanwang_title = (TextView) this.findViewById(R.id.hanwang_title);
		hanwang_title.setText("添加名片");
		hanwang_wancheng = (Button) this.findViewById(R.id.hanwang_wancheng);
		hanwang_image = (ImageView) this.findViewById(R.id.hanwang_image);
		hanwang_btn = (ImageButton) this.findViewById(R.id.hanwang_btn);
		hanwang_fanhui = (LinearLayout) this.findViewById(R.id.hanwang_fanhui);
		hw_xiangce = (ImageButton) this.findViewById(R.id.hw_xiangce);
		hw_xiangji = (ImageButton) this.findViewById(R.id.hw_xiangji);

		GradientDrawable myGrad = (GradientDrawable) hanwang_wancheng.getBackground();// 获取shape中button的背景颜色
		myGrad.setStroke((int) 0.5, getResources().getColor(R.color.blue2));
		myGrad.setColor(getResources().getColor(R.color.blue2));

		// 绑定监听
		hanwang_wancheng.setOnClickListener(this);
		hanwang_btn.setOnClickListener(this);
		hanwang_fanhui.setOnClickListener(this);
		hw_xiangce.setOnClickListener(this);
		hw_xiangji.setOnClickListener(this);
		hanwang_image.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.hanwang_fanhui:

			finish();
			break;
		case R.id.hanwang_btn:

			finish();
			break;
		case R.id.hw_xiangce:

			Image = true;
			Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
			openAlbumIntent.setType("image/*");
			startActivityForResult(openAlbumIntent, PHOTO_REQUEST_GALLERY);
			break;
		case R.id.hw_xiangji:

			Image = false;

			// 调用系统的拍照功能
			photo();
			break;

		case R.id.hanwang_wancheng:
			// SpotsDialog spotsDialog;

			mSpotsDialog.show();
		
			mSpotsDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
						return true;
					} else {
						return false;
					}
				}
			});
			DiscernThread discernThread = new DiscernThread();
			new Thread(discernThread).start();
			break;
		case R.id.hanwang_image:
			if (TextUtils.isEmpty(picPath)) {
				Toast.makeText(AddbusinesscardActivity.this, R.string.selectPicture, Toast.LENGTH_SHORT).show();
				return;
			}

			new ActionSheetDialog(AddbusinesscardActivity.this).builder().setCancelable(true)
					.setCanceledOnTouchOutside(true)
					.addSheetItem(getString(R.string.tailoring), SheetItemColor.Blue, new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {

							Intent intent = new Intent(AddbusinesscardActivity.this, CropImageActivity.class);
							intent.putExtra("picPath", picPath);
							startActivityForResult(intent, PHOTO_REQUEST_CUT);

						}
					}).builder().setCancelable(true).setCanceledOnTouchOutside(true)
					.addSheetItem(getString(R.string.rotating), SheetItemColor.Blue, new OnSheetItemClickListener() {

						@Override
						public void onClick(int which) {
							Intent intent = new Intent(AddbusinesscardActivity.this, RotatingActivity.class);
							intent.putExtra("picPath", picPath);
							startActivityForResult(intent, PHOTO_REQUEST_CUT);
						}
					}).show();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:

			if (resultCode == -1) {// 拍照
				type = 0;
				picPath = getRealPathFromURI(this, photoUri);

				final BitmapFactory.Options options1 = new BitmapFactory.Options();
				options1.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(picPath, options1);
				options1.inSampleSize = BitmapUtil.calculateInSampleSize(options1, 1280, 720);
				options1.inJustDecodeBounds = false;
				// Bitmap bitmap1 = BitmapFactory.decodeFile(,
				// options1);
				Bitmap bitmap1 = PhotoUtils.loadBitmap(picPath, options1, true);
				// hanwang_image.setScaleType(ScaleType.FIT_CENTER);
				hanwang_image.setImageBitmap(bitmap1);
				// Intent intent = new Intent(AddbusinesscardActivity.this,
				// CropImageActivity.class);
				// intent.putExtra("picPath", picPath);
				// startActivityForResult(intent, PHOTO_REQUEST_CUT);

				// cropImageUri(photoUri, 1280, 720, 03);
			}

			break;

		case PHOTO_REQUEST_GALLERY:
			if (data != null) {
				type = 1;
				uri = data.getData();

				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null,
							null, null);
					if (null == cursor) {
						Toast.makeText(this, getString(R.string.photo_load_failed), Toast.LENGTH_SHORT).show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

					picPath = path;
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(picPath, options);
					options.inSampleSize = BitmapUtil.calculateInSampleSize(options, 1280, 720);
					options.inJustDecodeBounds = false;
					// Bitmap bitmap = BitmapFactory.decodeFile(picPath,
					// options);
					Bitmap bitmap = PhotoUtils.loadBitmap(picPath, options, true);
					hanwang_image.setImageBitmap(bitmap);

				} else {

					picPath = uri.getPath();
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(picPath, options);
					options.inSampleSize = BitmapUtil.calculateInSampleSize(options, 1280, 720);
					options.inJustDecodeBounds = false;
					// Bitmap bitmap = BitmapFactory.decodeFile(picPath,
					// options);
					Bitmap bitmap = PhotoUtils.loadBitmap(picPath, options, true);
					hanwang_image.setImageBitmap(bitmap);
				}
			}
			break;
		case PHOTO_REQUEST_CUT:
			if (data != null) {
				// getImageToView(data);

				picPath = getRealPathFromURI(this, photoUri);

				final BitmapFactory.Options options1 = new BitmapFactory.Options();
				options1.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(picPath, options1);
				options1.inSampleSize = BitmapUtil.calculateInSampleSize(options1, 1280, 720);
				options1.inJustDecodeBounds = false;
				// Bitmap bitmap1 = BitmapFactory.decodeFile(,
				// options1);
				Bitmap bitmap1 = PhotoUtils.loadBitmap(picPath, options1, true);
				// hanwang_image.setScaleType(ScaleType.FIT_CENTER);
				hanwang_image.setImageBitmap(bitmap1);
			}
			break;
		}
		if (4 == resultCode) {

			if (0 == type) {
				picPath = getRealPathFromURI(this, photoUri);
			}
			if (1 == type && uri != null) {
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null,
							null, null);
					if (null == cursor) {
						Toast.makeText(this, getString(R.string.photo_load_failed), Toast.LENGTH_SHORT).show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
				} else {
					picPath = uri.getPath();

				}

			}

			final BitmapFactory.Options options1 = new BitmapFactory.Options();
			options1.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(picPath, options1);
			options1.inSampleSize = BitmapUtil.calculateInSampleSize(options1, 1280, 720);
			options1.inJustDecodeBounds = false;
			// Bitmap bitmap1 = BitmapFactory.decodeFile(,
			// options1);
			Bitmap bitmap1 = PhotoUtils.loadBitmap(picPath, options1, true);
			// hanwang_image.setScaleType(ScaleType.FIT_CENTER);
			hanwang_image.setImageBitmap(bitmap1);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/*
	 * 裁剪图片
	 */
	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {

		Intent intent = new Intent("com.android.camera.action.CROP");

		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 1);

		intent.putExtra("aspectY", 1);

		intent.putExtra("outputX", outputX);

		intent.putExtra("outputY", outputY);

		intent.putExtra("scale", true);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

		intent.putExtra("return-data", false);

		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

		intent.putExtra("noFaceDetection", true); // no face detection

		startActivityForResult(intent, requestCode);

	}

	/**
	 * 拍照
	 */
	public void photo() {

		try {
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = android.os.Environment.getExternalStorageDirectory().getPath() + "/tempImage/";
			File file = null;
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				// 有sd卡，是否有myImage文件夹
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// 是否有headImg文件
				file = new File(sdcardPathDir + System.currentTimeMillis() + ".JPEG");
			}
			if (file != null) {
				path = file.getPath();
				photoUri = Uri.fromFile(file);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

				startActivityForResult(openCameraIntent, PHOTO_REQUEST_TAKEPHOTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到真正的文件路径
	 * 
	 * @param context
	 * @param contentUri
	 * @return
	 */
	public static String getRealPathFromURI(Context context, Uri contentUri) {
		String imagePath = "";
		if (!TextUtils.isEmpty(contentUri.getAuthority())) {
			String[] filePathColumns = new String[] { MediaStore.Images.Media.DATA };
			Cursor c = context.getContentResolver().query(contentUri, filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			imagePath = c.getString(columnIndex);
			c.close();
		} else {
			imagePath = contentUri.getPath();
		}
		L.e("imagePath-----" + imagePath);
		return imagePath;
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {

			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			hanwang_image.setImageDrawable(drawable);
			// 保存图片到本地
			File file = new File(IMAGE_FILE_NAME);
			if (file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * 汉王名片识别
	 * 
	 * @author Administrator
	 * 
	 */
	public class DiscernThread implements Runnable {
		// JSONObject json;
		@Override
		public void run() {
			try {
				/**
				 * 调用汉王云名片识别方法
				 */
				if (Image == true) {
					result = hwCloudManagerBcard.cardLanguage("chns", picPath);

				} else {

					// result = hwCloudManagerBcard.cardLanguage("chns",
					// bitmapoptions);
					result = hwCloudManagerBcard.cardLanguage("chns", picPath);
					Logger.i("+++++" + "走了");
				}

			} catch (Exception e) {
				// TODO: handle exception
				mSpotsDialog.dismiss();
			}

			String aa = result;
			Bundle mBundle = new Bundle();
			mBundle.putString("responce", result);
			Message msg = new Message();
			msg.setData(mBundle);
			discernHandler.sendMessage(msg);
		}
	}

	String responce;

	public class DiscernHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			responce = bundle.getString("responce");
			String aa = responce;
			JSONObject json;
			try {
				json = new JSONObject(responce);
				String code = json.getString("code");
				LogUtils.e(responce);
				if ("0".equals(code)) {
					// post方法将汉王返回数据传到服务器
					RequestParams params = new RequestParams();
					params.addBodyParameter("uid", PublicStaticURL.userid);
					String bb = Base64.encodeToString(responce.getBytes("UTF-8"), Base64.DEFAULT);
					params.addBodyParameter("card_json",
							Base64.encodeToString(responce.getBytes("UTF-8"), Base64.DEFAULT));

					File f = new File(picPath);
					FileInputStream fis = null;
					fis = new FileInputStream(f);
					int size = fis.available();

					params.addBodyParameter("pic", new File(picPath));
					String cc = picPath;
					// params.setBodyEntity(new FileUploadEntity(new
					// File(picPath),"multipart/form-data"));
					String CC = picPath;

					GETHANWANG(PublicStaticURL.HANWANG, params);

				} else {
					mSpotsDialog.dismiss();
					SimpleHUD.showInfoMessage(AddbusinesscardActivity.this, "获取名片信息失败");
				}
			} catch (Exception e) {
				// TODO: handle exception
				mSpotsDialog.dismiss();
				SimpleHUD.showInfoMessage(AddbusinesscardActivity.this, "获取名片信息失败");
			}

		}

	}

	// 名片信息提交到服务器

	private void GETHANWANG(String str, RequestParams params) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				// mSpotsDialog = new
				// mSpotsDialog(AddbusinesscardActivity.this);
				// mSpotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				mSpotsDialog.dismiss();
				SimpleHUD.showErrorMessage(AddbusinesscardActivity.this, getString(R.string.server_connect_failed));
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

						Toast.makeText(AddbusinesscardActivity.this, "添加成功!", Toast.LENGTH_SHORT).show();
						finish();

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(AddbusinesscardActivity.this, "添加失败!");
					}

					if ("4".equals(code)) {

						SimpleHUD.showErrorMessage(AddbusinesscardActivity.this, "注册手机号与名片手机号不一致!");

						Intent intent = new Intent(AddbusinesscardActivity.this, Addbusinesscard01Activity.class);
						intent.putExtra("responce", responce);
						intent.putExtra("picPath", picPath);
						startActivity(intent);

					}
					if ("5".equals(code)) {

						SimpleHUD.showErrorMessage(AddbusinesscardActivity.this, "此名片已添加!");

					}

				} catch (Exception e) {
					Log.e("message", "走catch了");
					SimpleHUD.showErrorMessage(AddbusinesscardActivity.this, getString(R.string.server_connect_failed));
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
