package com.example.honeytag1;

import info.wangchen.simplehud.SimpleHUD;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dialog.ActionSheetDialog;
import com.example.dialog.ActionSheetDialog.OnSheetItemClickListener;
import com.example.dialog.ActionSheetDialog.SheetItemColor;
import com.example.honeytag1.R;
import com.example.dialog.SpotsDialog;
import com.example.utils.CustmUtils;
import com.example.utils.ExpressionUtil;
import com.example.utils.FaceVPAdapter;
import com.example.utils.KeyBoardUtils;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.utils.Utils;
import com.example.view.ClipboarEditText;
import com.hanvon.utils.BitmapUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

/**
 * 用户发表页面
 * 
 * @author Administrator Modify by DQP
 */
public class PublishActivity extends BaseActivity implements OnClickListener {

	private ImageView publish_btn; // 返回按钮
	private ImageButton publish_biaoti, publish_lianjie, publish_biaoqing, publish_btn_img; // 底部
	// 图片，标题，连接
	private TextView publish_tijiao, public_fabiao, tv_count; // 提交,删除按钮
	private EditText publish_huati, publish_url; // 内容 // 话题 //
	private EditText publish_neirong;
	// 链接
	private ImageView publish_image, publish_delete; // 图片
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
	private LinearLayout root, publish_face_container;
	private RelativeLayout loot;
	SpotsDialog spotsDialog; // dialog
	private String content;
	private String title;
	// private String url;
	String picPath = null;
	// String conversation; // 话题
	String huati_url; // URL链接
	// Typeface font;
	public static boolean flag = false;
	public static boolean Image = false;
	Bitmap bitmap1;
	// 表情图标每页6列4行
	private int columns = 6;
	private int rows = 4;
	// 每页显示的表情view
	private List<View> views = new ArrayList<View>();
	// 表情列表
	private List<String> staticFacesList;
	/** 表情按钮 */
	private ImageView face_details;
	/** 表情布局指示器 */
	private LinearLayout mDotsLayout;
	/** 承载表情的viewpage */
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				initFacesList();
			}
		}, 500);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public void loadXml() {
		setContentView(R.layout.activity_publish);
		publish_btn_img = (ImageButton) findViewById(R.id.publish_btn_img);
		publish_face_container = (LinearLayout) findViewById(R.id.publish_face_container);
		mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
		publish_biaoqing = (ImageButton) findViewById(R.id.publish_biaoqing);
		public_fabiao = (TextView) findViewById(R.id.public_fabiao);
		publish_btn = (ImageView) this.findViewById(R.id.publish_btn);
		publish_tijiao = (TextView) this.findViewById(R.id.publish_tijiao);
		tv_count = (TextView) this.findViewById(R.id.tv_count);
		publish_delete = (ImageView) this.findViewById(R.id.publish_delete);
		publish_neirong = (ClipboarEditText) this.findViewById(R.id.publish_neirong);
		publish_url = (EditText) this.findViewById(R.id.publish_url);
		huati_url = publish_url.getText().toString();
		// publish_huati = (EditText) this.findViewById(R.id.publish_huati);
		// conversation = publish_huati.getText().toString();
		publish_image = (ImageView) this.findViewById(R.id.publish_image);
		face_details = (ImageView) this.findViewById(R.id.face_details);
		publish_biaoti = (ImageButton) this.findViewById(R.id.publish_biaoti);
		mViewPager = (ViewPager) this.findViewById(R.id.face_viewpager);
		// publish_lianjie = (ImageButton)
		// this.findViewById(R.id.publish_lianjie);
		root = (LinearLayout) findViewById(R.id.root);
		loot = (RelativeLayout) findViewById(R.id.loot);
		Utils.KeyBrodLisenert(root, loot); // 控制底部按钮随键盘移动
		// AssetManager assetManager = PublishActivity.this.getAssets();
		// font = Typeface.createFromAsset(assetManager, "fonts/STXIHEI.TTF");
		publish_tijiao.setTextColor(getResources().getColor(R.color.huise5));
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		// public_fabiao.setTypeface(font);
		publish_btn.setOnClickListener(this);
		publish_tijiao.setOnClickListener(this);
		publish_biaoti.setOnClickListener(this);
		publish_btn_img.setOnClickListener(this);
		// publish_lianjie.setOnClickListener(this);
		// publish_image.setOnClickListener(this);
		publish_biaoqing.setOnClickListener(this);
		publish_neirong.setOnClickListener(this);
		publish_delete.setOnClickListener(this);
		// publish_huati.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before, int
		// count) {
		// // TODO Auto-generated method stub
		// if (!TextUtils.isEmpty(s) &&
		// !TextUtils.isEmpty(publish_neirong.getText())) {
		// publish_tijiao.setTextColor(getResources().getColor(R.color.black));
		// publish_tijiao.setEnabled(true);
		// } else {
		// publish_tijiao.setTextColor(getResources().getColor(R.color.huise5));
		// publish_tijiao.setEnabled(false);
		// }
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		publish_neirong.addTextChangedListener(new TextWatcher() {

			String tmp = "";
			String digits = " ";

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					publish_tijiao.setTextColor(getResources().getColor(R.color.black));
					publish_tijiao.setEnabled(true);
				} else {
					publish_tijiao.setTextColor(getResources().getColor(R.color.huise5));
					publish_tijiao.setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				tmp = s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				Logger.i("edittext", str);
				if (TextUtils.isEmpty(str)) {
					tv_count.setText(
							str.length() - ExpressionUtil.getFaceLong(str) + ExpressionUtil.getFaceLong(str) / 12 + "");

					return;
				}

				if (str.length() - ExpressionUtil.getFaceLong(str) + ExpressionUtil.getFaceLong(str) / 12 <= 140) {
					// SimpleHUD.showInfoMessage(PublishActivity.this,
					// "内容描述长度为8到140个字符(包括8和140),表情长度除外:)");

					tv_count.setText(
							str.length() - ExpressionUtil.getFaceLong(str) + ExpressionUtil.getFaceLong(str) / 12 + "");

					tv_count.setTextColor(getResources().getColor(R.color.huise1));

					// ExpressionUtil.delete(publish_neirong);
				} else {

					tv_count.setText(
							str.length() - ExpressionUtil.getFaceLong(str) + ExpressionUtil.getFaceLong(str) / 12 + "");

					tv_count.setTextColor(getResources().getColor(R.color.hongse));
				}

			}

		});

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.publish_neirong:
			if (publish_face_container.getVisibility() == View.VISIBLE) {
				publish_face_container.setVisibility(View.GONE);

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(publish_neirong, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

			}
			break;
		case R.id.publish_biaoqing:
			if (publish_face_container.getVisibility() == View.VISIBLE) {
				publish_face_container.setVisibility(View.GONE);

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(publish_neirong, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

			} else {
				KeyBoardUtils.closeKeybord(publish_neirong, PublishActivity.this);
				publish_neirong.requestFocus();
				if (publish_face_container.getVisibility() == View.GONE) {
					publish_face_container.setVisibility(View.VISIBLE);
				} else {

				}
			}
			break;
		case R.id.publish_btn:
			finish();

			break;

		case R.id.publish_tijiao:

			if (publish_neirong.getText().toString().length()
					- ExpressionUtil.getFaceLong(publish_neirong.getText().toString())
					+ ExpressionUtil.getFaceLong(publish_neirong.getText().toString()) / 12 < 8
					|| publish_neirong.getText().toString().length()
							- ExpressionUtil.getFaceLong(publish_neirong.getText().toString())
							+ ExpressionUtil.getFaceLong(publish_neirong.getText().toString()) / 12 > 140) {
				SimpleHUD.showInfoMessage(PublishActivity.this, getString(R.string.content_length));
			} else {

				uploadImgURL();
				KeyBoardUtils.closeKeybord(publish_neirong, PublishActivity.this);
				// ((InputMethodManager)
				// getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				// PublishActivity.this.getCurrentFocus().getWindowToken(),
				// InputMethodManager.HIDE_NOT_ALWAYS);
			}

			break;
		case R.id.publish_btn_img:

			//
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					PublishActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

			new ActionSheetDialog(PublishActivity.this).builder().setCancelable(true).setCanceledOnTouchOutside(true)
					.addSheetItem(getString(R.string.take_photo), SheetItemColor.Blue, new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {

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

						}
					}).builder().setCancelable(true).setCanceledOnTouchOutside(true).addSheetItem(
							getString(R.string.choose_photo), SheetItemColor.Blue, new OnSheetItemClickListener() {

								@Override
								public void onClick(int which) {
									// TODO Auto-generated method stub
									Image = true;
									Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
									openAlbumIntent.setType("image/*");
									startActivityForResult(openAlbumIntent, PHOTO_REQUEST_GALLERY);
								}
							})
					.show();

			break;
		// case R.id.publish_biaoti:
		//
		// final AlertDialog dlg = new
		// AlertDialog.Builder(PublishActivity.this).create();
		// dlg.show();
		// Window window = dlg.getWindow();
		// window.setContentView(R.layout.publish_dialog_biaoti);
		// Button dialog_queding = (Button)
		// window.findViewById(R.id.dialog_queding);
		// TextView text = (TextView) window.findViewById(R.id.text);
		// final EditText dialog_biaoti = (EditText)
		// window.findViewById(R.id.dialog_biaoti);
		// dialog_biaoti.setTextColor(R.color.btntextcolor);
		// dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		// dialog_queding.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		//
		// if (dialog_biaoti.getText().toString().length() == 0) {
		// SimpleHUD.showInfoMessage(PublishActivity.this,
		// getString(R.string.title_canot_null));
		// } else {
		// title = dialog_biaoti.getText().toString();
		// Spanned htmlSpanned = Html.fromHtml("<font color='green'>" + title +
		// "</font>");
		// publish_huati.append(htmlSpanned);
		// publish_huati.setText(title);
		// publish_huati.setSelection(dialog_biaoti.getText().length());
		// dlg.dismiss();
		// }
		// }
		// });
		// break;
		case R.id.publish_delete:
			publish_image.setVisibility(View.GONE);
			publish_delete.setVisibility(View.GONE);
			flag = false;
			// case R.id.publish_lianjie:
			//
			// final AlertDialog dlg_lianjie = new AlertDialog.Builder(
			// PublishActivity.this).create();
			// dlg_lianjie.show();
			// Window window_lianjie = dlg_lianjie.getWindow();
			// window_lianjie.setContentView(R.layout.publish_dialog_lianjie);
			// Button dialog_btn = (Button) window_lianjie
			// .findViewById(R.id.dialog_btn);
			// final EditText dialog_lianjie = (EditText) window_lianjie
			// .findViewById(R.id.dialog_lianjie);
			// dialog_lianjie.setTextColor(R.color.btntextcolor);
			// dlg_lianjie.getWindow().clearFlags(
			// WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			// dialog_btn.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			//
			// if (dialog_lianjie.getText().toString().length() == 0) {
			// SimpleHUD.showInfoMessage(PublishActivity.this,
			// "链接不能为空");
			// } else {
			// url = dialog_lianjie.getText().toString();
			// Spanned htmlSpanned = Html
			// .fromHtml("<font color='blue'>" + url
			// + "</font>");
			// publish_url.append(htmlSpanned);
			// publish_url.setText(url);
			// publish_url.setSelection(dialog_lianjie.getText()
			// .length());
			// dlg_lianjie.dismiss();
			// }
			// }
			// });
			// break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {

		case PHOTO_REQUEST_TAKEPHOTO:
			if (resultCode == RESULT_OK) {
				// startPhotoZoom(Uri.fromFile(tempFile), 800);

				BitmapFactory.Options options1 = new BitmapFactory.Options();
				options1.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(IMG, options1);
				options1.inSampleSize = BitmapUtil.calculateInSampleSize(options1, 320, 480);
				options1.inJustDecodeBounds = false;
				Bitmap bitmapoptions = BitmapFactory.decodeFile(IMG, options1);
				/*
				 * 不知道为什么，如果不重新设置ImageView的宽和高，选中以后的缩略图大小不一。
				 */
				publish_image.setVisibility(View.VISIBLE);
				publish_delete.setVisibility(View.VISIBLE);
				//publish_image.setImageBitmap(zoomOut(bitmapoptions));
				publish_image.setImageBitmap(bitmapoptions);
					saveFile(bitmapoptions);
				flag = true;
			}

			break;

		case PHOTO_REQUEST_GALLERY:
			if (data != null) {
				Uri uri = data.getData();
				Logger.i("***********" + data.getData());

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
					options.inSampleSize = BitmapUtil.calculateInSampleSize(options, 320, 480);
					// options.inSampleSize = 25;
					options.inJustDecodeBounds = false;
					Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
					publish_image.setVisibility(View.VISIBLE);
					publish_delete.setVisibility(View.VISIBLE);
				//	publish_image.setImageBitmap(zoomOut(bitmap));
					publish_image.setImageBitmap(bitmap);
					saveFile(bitmap);
					flag = true;
				} else {

					picPath = uri.getPath();
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(picPath, options);
					options.inSampleSize = BitmapUtil.calculateInSampleSize(options, 320, 480);
					options.inJustDecodeBounds = false;
					Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
					publish_image.setVisibility(View.VISIBLE);
					publish_delete.setVisibility(View.VISIBLE);
				//	publish_image.setImageBitmap(zoomOut(bitmap));
					publish_image.setImageBitmap(bitmap);
					saveFile(bitmap);
					flag = true;
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

	private void uploadImgURL() {
		String sub = "http://";
		String ss1 = null;
		content = Utils.Transformation(publish_neirong.getText().toString());
		// conversation = publish_huati.getText().toString();
		// content=EmojiUtil.emoji2Text(content);
		// huati_url = publish_url.getText().toString();
		if (flag == true && Image == true) {// 选取图片
			RequestParams params = new RequestParams();
			try {
				params.addBodyParameter("uid", PublicStaticURL.userid);
				params.addBodyParameter("content", URLEncoder.encode(content, "UTF-8"));
				// params.addBodyParameter("title",
				// URLEncoder.encode(conversation, "UTF-8"));
				params.addBodyParameter("file", "ddfaceImage.jpg");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			Logger.i(new File(picPath).getAbsolutePath());
			params.addBodyParameter("file", new File(IMAGE_FILE_NAME));
			uploadMethod(params, PublicStaticURL.FABU);
		} else if (flag == true && Image == false) {// 拍照
			LogUtils.e("走这了");
			try {
				RequestParams params = new RequestParams();
				params.addBodyParameter("uid", PublicStaticURL.userid);
				params.addBodyParameter("content", URLEncoder.encode(content, "UTF-8"));
				// params.addBodyParameter("title",
				// URLEncoder.encode(conversation, "UTF-8"));
				params.addBodyParameter("file", "ddfaceImage.jpg");
				Logger.i(new File(IMG).toString());
				params.addBodyParameter("file", new File(IMAGE_FILE_NAME));
				uploadMethod(params, PublicStaticURL.FABU);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		else if (flag == false) {

			RequestParams params = new RequestParams();
			try {
				params.addBodyParameter("uid", PublicStaticURL.userid);
				params.addBodyParameter("content", URLEncoder.encode(content, "UTF-8"));
				// params.addBodyParameter("title",
				// URLEncoder.encode(conversation, "UTF-8"));
				Logger.i("content", URLEncoder.encode(content, "UTF-8"));
				Logger.i("content", content);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			uploadMethod(params, PublicStaticURL.FABU);
		}
	}

	public void uploadMethod(final RequestParams params, final String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, uploadHost, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();

				spotsDialog = new SpotsDialog(PublishActivity.this);
				spotsDialog.show();
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				spotsDialog.dismiss();
				SimpleHUD.showInfoMessage(PublishActivity.this, getString(R.string.fabu_failed_wangluo));
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
						publish_face_container.setVisibility(View.GONE);
						Toast.makeText(PublishActivity.this, getString(R.string.fabu_succeed), Toast.LENGTH_SHORT)
								.show();

						// ((InputMethodManager)
						// getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
						// PublishActivity.this.getCurrentFocus().getWindowToken(),
						// InputMethodManager.HIDE_NOT_ALWAYS);
						finish();
						Logger.i("图片上传成功");
						sendBroadcast(new Intent("com.example.set.referesh"));
						flag = false;

					} else if ("3".equals(code)) {
						SimpleHUD.showInfoMessage(PublishActivity.this, getString(R.string.fabu_failed));
						flag = false;
					} else if ("5".equals(code)) {
						SimpleHUD.showInfoMessage(PublishActivity.this, getString(R.string.not_upload_image));
						flag = false;
					} else if ("6".equals(code)) {
						SimpleHUD.showInfoMessage(PublishActivity.this, getString(R.string.user_error));
						flag = false;
					} else if ("7".equals(code)) {
						SimpleHUD.showInfoMessage(PublishActivity.this, getString(R.string.content_canot_null));
						flag = false;
					}

				} catch (Exception e) {
					SimpleHUD.showInfoMessage(PublishActivity.this, getString(R.string.app_exception));
					Log.e("message", "走catch了");
					e.printStackTrace();
				}
			}

		});

	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri, int size) {

		Intent intent = new Intent("com.android.camera.action.CROP");
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
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {

			bitmap1 = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(bitmap1);
			publish_image.setVisibility(View.VISIBLE);
			publish_delete.setVisibility(View.VISIBLE);
			publish_image.setImageDrawable(drawable);
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
				bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				flag = true;
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
	 * 初始化表情
	 */
	private void initFacesList() {
		// 获取表情集合
		staticFacesList = ExpressionUtil.initStaticFaces(this);
		Logger.i("表情包大小--" + staticFacesList.size());
		int pagesize = ExpressionUtil.getPagerCount(staticFacesList.size(), columns, rows);
		// 获取页数
		for (int i = 0; i < pagesize; i++) {
			views.add(ExpressionUtil.viewPagerItem(this, i, staticFacesList, columns, rows, publish_neirong));
			LayoutParams params = new LayoutParams(16, 16);
			mDotsLayout.addView(dotsItem(i), params);
		}
		FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
		mViewPager.setAdapter(mVpAdapter);
		/**
		 * 表情页改变时，dots效果也要跟着改变
		 */
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
					mDotsLayout.getChildAt(i).setSelected(false);
				}
				mDotsLayout.getChildAt(arg0).setSelected(true);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		mDotsLayout.getChildAt(0).setSelected(true);
	}

	/**
	 * 表情页切换时，底部小圆点
	 * 
	 * @param position
	 * @return
	 */
	private ImageView dotsItem(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dot_image, null);
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}

//	private void setImage(Uri mImageCaptureUri) {
//
//		// 不管是拍照还是选择图片每张图片都有在数据中存储也存储有对应旋转角度orientation值
//		// 所以我们在取出图片是把角度值取出以便能正确的显示图片,没有旋转时的效果观看
//
//		ContentResolver cr = this.getContentResolver();
//		Cursor cursor = cr.query(mImageCaptureUri, null, null, null, null);// 根据Uri从数据库中找
//		if (cursor != null) {
//			cursor.moveToFirst();// 把游标移动到首位，因为这里的Uri是包含ID的所以是唯一的不需要循环找指向第一个就是了
//			String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));// 获取图片路
//			String orientation = cursor.getString(cursor.getColumnIndex("orientation"));// 获取旋转的角度
//			cursor.close();
//			if (filePath != null) {
//				Bitmap bitmap = BitmapFactory.decodeFile(filePath);// 根据Path读取资源图片
//				int angle = 0;
//				if (orientation != null && !"".equals(orientation)) {
//					angle = Integer.parseInt(orientation);
//				}
//				if (angle != 0) {
//					// 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
//					Matrix m = new Matrix();
//					int width = bitmap.getWidth();
//					int height = bitmap.getHeight();
//					m.setRotate(angle); // 旋转angle度
//					bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
//
//				}
//				publish_image.setVisibility(View.VISIBLE);
//				publish_image.setImageBitmap(bitmap);
//			}
//		}
//	}

	/*
	 * 图片缩放
	 */
	public Bitmap zoomOut(Bitmap bitmapoptions) {
		// publish_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// 获得图片的宽高
		int width = bitmapoptions.getWidth();
		int height = bitmapoptions.getHeight();
		// 获得屏幕宽高
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int pwidth = wm.getDefaultDisplay().getWidth();
		int pheight = wm.getDefaultDisplay().getHeight();

		// 计算缩放比例
		float scaleWidth = ((float) pheight / 4) / width;
		float scaleHeight = ((float) pwidth / 4) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		return Bitmap.createBitmap(bitmapoptions, 0, 0, width, height, matrix, true);
	}

	/**
	 * 获得指定文件的byte数组
	 */
	private byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
	public void saveFile(Bitmap bm) {   
		     File myCaptureFile = new File(IMAGE_FILE_NAME); 
			try {
				if(myCaptureFile.exists()){    
					myCaptureFile.createNewFile();    
				} 
				BufferedOutputStream bos;
				bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
				bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);    
				bos.flush();    
				bos.close();    
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
