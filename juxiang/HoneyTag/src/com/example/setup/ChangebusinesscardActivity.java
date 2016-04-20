package com.example.setup;

import info.wangchen.simplehud.SimpleHUD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;

import com.example.dialog.SpotsDialog;
import com.example.honeytag1.AboutActivity;
import com.example.honeytag1.BaseActivity;
import com.example.honeytag1.R;
import com.example.honeytag1.R.layout;
import com.example.honeytag1.R.menu;
import com.example.my.HanWangRegistActivity;
import com.example.my.LoginActivity;
import com.example.my.PhoneVerificationActivity;
import com.example.my.HanWangRegistActivity.DiscernHandler;
import com.example.my.HanWangRegistActivity.DiscernThread;

import com.example.utils.CustmUtils;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.hanvon.HWCloudManager;
import com.hanvon.utils.BitmapUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class ChangebusinesscardActivity extends BaseActivity implements OnClickListener {
	/**
	 * 更改名片
	 */
	/**
	 * 调用汉王接口，属于非付费模式，每天2000次扫描名片，超过上限扫描失败！
	 */
	private LinearLayout hanwang_fanhui;// 返回按钮
	private ImageButton hanwang_btn; // 返回按钮
	private ImageView hanwang_image; // 名片图片
	private Button hanwang_wancheng; // 完成注册
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
	String picPath = null;
	String result = null;
	String code = null;
	private HWCloudManager hwCloudManagerBcard; // 名片
	private SpotsDialog spotsDialog;
	public static boolean Image = false;
	Bitmap bitmap1;
	private ImageButton hw_xiangce, hw_xiangji;
	TextView hanwang_title;
	Bitmap bitmapoptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
	}

	@Override
	public void loadXml() {
		// TODO Auto-generated method stub
		// hwCloudManagerBcard = new HWCloudManager(this,
		// "69805d8f-cbdc-4130-884d-49464ddeed1c");
		setContentView(R.layout.activity_han_wang_regist);
		hwCloudManagerBcard = new HWCloudManager(this, getString(R.string.hanwang_key));
		discernHandler = new DiscernHandler();
		hanwang_title = (TextView) this.findViewById(R.id.hanwang_title);
		hanwang_title.setText(R.string.genghuan_card);
		hanwang_wancheng = (Button) this.findViewById(R.id.hanwang_wancheng);
		hanwang_image = (ImageView) this.findViewById(R.id.hanwang_image);
		hanwang_btn = (ImageButton) this.findViewById(R.id.hanwang_btn);
		hanwang_fanhui = (LinearLayout) this.findViewById(R.id.hanwang_fanhui);
		hw_xiangce = (ImageButton) this.findViewById(R.id.hw_xiangce);
		hw_xiangji = (ImageButton) this.findViewById(R.id.hw_xiangji);
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		// 绑定监听
		hanwang_wancheng.setOnClickListener(this);
		hanwang_btn.setOnClickListener(this);
		hanwang_fanhui.setOnClickListener(this);
		hw_xiangce.setOnClickListener(this);
		hw_xiangji.setOnClickListener(this);
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
			Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 指定调用相机拍照后照片的储存路径
			if (CustmUtils.hasSdcard()) {
				intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
				Logger.i("---------------------sd卡可用");
			} else {
				Logger.i("++++++++++++++++++++++sd卡不可用");
			}
			startActivityForResult(intent1, PHOTO_REQUEST_TAKEPHOTO);
			break;

		case R.id.hanwang_wancheng:
			SpotsDialog spotsDialog;
			spotsDialog = new SpotsDialog(ChangebusinesscardActivity.this);
			DiscernThread discernThread = new DiscernThread();
			new Thread(discernThread).start();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:
			// // bitmap1 = BitmapFactory.decodeFile(IMG);
			// // hanwang_image.setImageBitmap(bitmap1);
			// BitmapFactory.Options options1 = new BitmapFactory.Options();
			// options1.inSampleSize = 25;
			// options1.inJustDecodeBounds = false;
			// bitmapoptions = BitmapFactory.decodeFile(IMG, options1);
			// //hanwang_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
			// /*
			// * 不知道为什么，如果不重新设置ImageView的宽和高，选中以后的缩略图大小不一。
			// */
			// // 获得图片的宽高
			// int width = bitmapoptions.getWidth();
			// int height = bitmapoptions.getHeight();
			// // 计算缩放比例
			// float scaleWidth = ((float) 300) / width;
			// float scaleHeight = ((float) 150) / height;
			// // 取得想要缩放的matrix参数
			// Matrix matrix = new Matrix();
			// matrix.postScale(scaleWidth, scaleHeight);
			// // 得到新的图片
			// Bitmap newbm = Bitmap.createBitmap(bitmapoptions, 0, 0, width,
			// height, matrix, true);
			// hanwang_image.setImageBitmap(newbm);
			//

			Bitmap bitmap1 = BitmapFactory.decodeFile(IMG);
			hanwang_image.setScaleType(ScaleType.CENTER_CROP);
			hanwang_image.setImageBitmap(bitmap1);

			break;

		case PHOTO_REQUEST_GALLERY:
			if (data != null) {
				Uri uri = data.getData();

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
					Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
					hanwang_image.setImageBitmap(bitmap);

				} else {

					picPath = uri.getPath();
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(picPath, options);
					options.inSampleSize = BitmapUtil.calculateInSampleSize(options, 1280, 720);
					options.inJustDecodeBounds = false;
					Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
					hanwang_image.setImageBitmap(bitmap);

				}
			}
			break;
		case PHOTO_REQUEST_CUT:
			if (data != null)
				getImageToView(data);
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
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
					result = hwCloudManagerBcard.cardLanguage("chns", bitmapoptions);
					Logger.i("+++++" + "走了");
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			String aa = result;
			Bundle mBundle = new Bundle();
			mBundle.putString("responce", result);
			Message msg = new Message();
			msg.setData(mBundle);
			discernHandler.sendMessage(msg);
		}
	}

	public class DiscernHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			String responce = bundle.getString("responce");
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
					params.addBodyParameter("card_json", responce);
					// GETHANWANG(PublicStaticURL.Changbusiness,
					// params);//HANWANG
					GETHANWANG(PublicStaticURL.HANWANG, params);
				} else {
					SimpleHUD.showInfoMessage(ChangebusinesscardActivity.this, getString(R.string.get_cardinfo_failed));
				}
			} catch (Exception e) {
				// TODO: handle exception
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

				spotsDialog = new SpotsDialog(ChangebusinesscardActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showErrorMessage(ChangebusinesscardActivity.this, getString(R.string.server_connect_failed));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				spotsDialog.dismiss();
				String str1 = responseInfo.result;// 接口返回的数据
				Logger.i(str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
					String code = json.getString("code");

					if ("2".equals(code)) {

						Toast.makeText(ChangebusinesscardActivity.this, getString(R.string.add_card_succeed),
								Toast.LENGTH_SHORT).show();
						finish();

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(ChangebusinesscardActivity.this, getString(R.string.add_card_failed));
					}

					if ("4".equals(code)) {

						SimpleHUD.showErrorMessage(ChangebusinesscardActivity.this, getString(R.string.phone_not_same));

					}

					// if ("2".equals(code)) {
					//
					// Toast.makeText(ChangebusinesscardActivity.this, "更换成功",
					// Toast.LENGTH_SHORT).show();
					// finish();
					//
					// }
					// if ("3".equals(code)) {
					// SimpleHUD.showInfoMessage(ChangebusinesscardActivity.this,
					// "提交失败");
					// }
					//
					// if ("6".equals(code)) {
					//
					// String result = json.getString("result");
					// JSONObject json1 = new JSONObject(result);
					// PublicStaticURL.Regist_tel = json1.getString("tel");
					// startActivity(new Intent(ChangebusinesscardActivity.this,
					// VerificationcardActivity.class));
					//
					// finish();
					// }
					// if ("7".equals(code)) {
					//
					// SimpleHUD.showErrorMessage(ChangebusinesscardActivity.this,
					// "未获取到您名片上的信息，从新上传");
					//
					// }

				} catch (Exception e) {
					Log.e("message", "走catch了");
					SimpleHUD.showErrorMessage(ChangebusinesscardActivity.this,
							getString(R.string.server_connect_failed));
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

}
