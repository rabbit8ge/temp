package com.example.my;

import info.wangchen.simplehud.SimpleHUD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import zhy.com.highlight.util.L;

import com.example.dialog.SpotsDialog;
import com.example.honeytag1.R;
import com.example.honeytag1.BaseActivity;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.hanvon.HWCloudManager;
import com.hanvon.utils.BitmapUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 获取名片上信息
 * 
 * @author Administrator
 * 
 */
public class HanWangRegistActivity extends BaseActivity implements OnClickListener {

	/**
	 * 调用汉王接口，属于非付费模式，每天2000次扫描名片，超过上限扫描失败！
	 */
	private LinearLayout hanwang_fanhui;// 返回按钮
	private ImageButton hanwang_btn; // 返回按钮
	private ImageView hanwang_image; // 名片图片
	private Button hanwang_wancheng; // 完成注册

	private String path = "";
	private Uri photoUri;
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	public static final String separator = File.separator;// 分隔符
	public static final String MNT = Environment.getExternalStorageDirectory().toString();
	public static final String ROOT = MNT + separator + "有秘";

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
	private String photoImagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
	}

	@Override
	public void loadXml() {
		setContentView(R.layout.activity_han_wang_regist);
		// TODO Auto-generated method stub
		// hwCloudManagerBcard = new HWCloudManager(this,
		// "69805d8f-cbdc-4130-884d-49464ddeed1c");
		// hwCloudManagerBcard = new HWCloudManager(this,
		// "6c5da758-1a18-4c43-b491-0ba48e68e0b4");
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
		myGrad.setStroke((int) 0.5, getResources().getColor(R.color.huoqu_hui));
		myGrad.setColor(getResources().getColor(R.color.huoqu_hui));
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		hanwang_wancheng.setClickable(false);
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

			startActivity(new Intent(HanWangRegistActivity.this, LoginActivity.class));
			finish();
			break;
		case R.id.hanwang_btn:

			startActivity(new Intent(HanWangRegistActivity.this, LoginActivity.class));
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
			SpotsDialog spotsDialog;
			spotsDialog = new SpotsDialog(HanWangRegistActivity.this);
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
			if (resultCode == -1) {// 拍照
				picPath = getRealPathFromURI(this, photoUri);
				// Bitmap bitmap1 = BitmapFactory.decodeFile(picPath);
				// hanwang_image.setScaleType(ScaleType.FIT_CENTER);

				final BitmapFactory.Options options1 = new BitmapFactory.Options();
				options1.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(picPath, options1);
				options1.inSampleSize = BitmapUtil.calculateInSampleSize(options1, 1280, 720);
				options1.inJustDecodeBounds = false;
				Bitmap bitmap1 = BitmapFactory.decodeFile(picPath, options1);
				hanwang_image.setScaleType(ScaleType.FIT_CENTER);
				hanwang_image.setImageBitmap(bitmap1);

				GradientDrawable myGrad = (GradientDrawable) hanwang_wancheng.getBackground();// 获取shape中button的背景颜色
				myGrad.setStroke((int) 0.5, getResources().getColor(R.color.blue2));
				myGrad.setColor(getResources().getColor(R.color.blue2));

				hanwang_wancheng.setClickable(true);
			}

			break;

		case PHOTO_REQUEST_GALLERY:
			if (data != null) {
				Uri uri = data.getData();

				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null,
							null, null);
					if (null == cursor) {
						Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
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
				GradientDrawable myGrad = (GradientDrawable) hanwang_wancheng.getBackground();// 获取shape中button的背景颜色
				myGrad.setStroke((int) 0.5, getResources().getColor(R.color.blue2));
				myGrad.setColor(getResources().getColor(R.color.blue2));

				hanwang_wancheng.setClickable(true);
			}
			break;
		case PHOTO_REQUEST_CUT:
			if (data != null)
				getImageToView(data);
			GradientDrawable myGrad = (GradientDrawable) hanwang_wancheng.getBackground();// 获取shape中button的背景颜色
			myGrad.setStroke((int) 0.5, getResources().getColor(R.color.blue2));
			myGrad.setColor(getResources().getColor(R.color.blue2));

			hanwang_wancheng.setClickable(true);
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
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

	// /**
	// * 裁剪图片方法实现
	// *
	// * @param uri
	// */
	// public void startPhotoZoom(Uri uri, int size) {
	//
	// Intent intent = new Intent("com.android.camera.action.CROP");
	// intent.setDataAndType(uri, "image/*");
	// // 设置裁剪
	// intent.putExtra("crop", "true");
	// // aspectX aspectY 是宽高的比例
	// intent.putExtra("aspectX", 1);
	// intent.putExtra("aspectY", 1);
	// // outputX outputY 是裁剪图片宽高
	// intent.putExtra("outputX", size);
	// intent.putExtra("outputY", size);
	// intent.putExtra("return-data", true);
	// startActivityForResult(intent, PHOTO_REQUEST_CUT);
	// }

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
					result = hwCloudManagerBcard.cardLanguage("chns", picPath);
					String aa = result;
					Logger.i("+++++" + "走了");
				}

			} catch (Exception e) {
			}

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
			JSONObject json;
			try {
				json = new JSONObject(responce);
				LogUtils.d(responce);
				String code = json.getString("code");
				if ("0".equals(code)) {
					// post方法将汉王返回数据传到服务器

					RequestParams params = new RequestParams();
					params.addBodyParameter("tel", PublicStaticURL.tel);

					params.addBodyParameter("password", PublicStaticURL.password);
					params.addBodyParameter("card_json",
							Base64.encodeToString(responce.getBytes("UTF-8"), Base64.DEFAULT));
					params.addBodyParameter("spread_tel", "");
					params.addBodyParameter("pic", new File(picPath));

					GETHANWANG(PublicStaticURL.REGIST, params);
					L.e("tel--------" + PublicStaticURL.tel + "password---" + PublicStaticURL.password
							+ "card_json========" + responce);
				} else {
					SimpleHUD.showInfoMessage(HanWangRegistActivity.this, "无法获取名片信息");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	// 名片信息提交到服务器
	private void GETHANWANG(String str, RequestParams params) {
		L.e("url-----" + str + "params---" + params.toString());
		HttpUtils httpUtils = new HttpUtils(5000);
		httpUtils.send(HttpMethod.POST, str, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				spotsDialog = new SpotsDialog(HanWangRegistActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				spotsDialog.dismiss();
				L.e("联网失败-------------" + arg1);
				SimpleHUD.showErrorMessage(HanWangRegistActivity.this, "网络连接失败，请检查网络");
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
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);
						PublicStaticURL.Youmi_phone = json1.getString("tel");
						PublicStaticURL.Youmi_password = json1.getString("password");
						Toast.makeText(HanWangRegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
						finish();
						startActivity(new Intent(HanWangRegistActivity.this, LoginActivity.class));

					}
					if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(HanWangRegistActivity.this, "提交失败");
					}
					if ("4".equals(code)) {
						SimpleHUD.showInfoMessage(HanWangRegistActivity.this, "手机号已注册");
					}

					if ("6".equals(code)) {

						SimpleHUD.showErrorMessage(HanWangRegistActivity.this, "注册成功，名片上传失败！");
						getData(json);

						startActivity(new Intent(HanWangRegistActivity.this, PhoneVerificationActivity.class));

					}
					if ("7".equals(code)) {
						getData(json);

						SimpleHUD.showErrorMessage(HanWangRegistActivity.this, "注册成功，名片上传失败！");

					}
				} catch (Exception e) {
					Log.e("message", "走catch了");
					SimpleHUD.showErrorMessage(HanWangRegistActivity.this, "网络连接失败，请检查网络");
					e.printStackTrace();
				}
			}

		});
	}

	public void getData(JSONObject json) throws Exception {

		String result = json.getString("result");
		JSONObject json1 = new JSONObject(result);
		if (json1.has("tel")) {
			PublicStaticURL.Regist_tel = json1.getString("tel");
		} else {
			PublicStaticURL.Regist_tel = "";
		}

		PublicStaticURL.Regist_id = json1.getString("uid");

	}

	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
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
