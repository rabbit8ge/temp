package com.example.utils;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dialog.CustomDialog;
import com.example.dialog.SpotsDialog;
import com.example.honeytag1.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.readystatesoftware.viewbadger.BadgeView;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;
import info.wangchen.simplehud.SimpleHUD;

/**
 * @Description描述:设置
 * @Author作者:dbj
 * @Date日期:2016-2-22 上午:10:28
 */

public class VersionManagement {

	private int versionCode;// 版本号
	private String versionName;// 版本名
	private static VersionManagement mVersionManagement;
	private SpotsDialog spotsDialog;
	public static final String MNT = Environment.getExternalStorageDirectory().toString();
	public static final String ROOT = MNT + File.separator + "行家说" + File.separator;
	private long size;
	private DownloadAsyncTask downloadTask = null;
	private static String apkname;
	private File file;
	protected Dialog mMyBadgeView;

	/*
	 * 获得当前对象的实例
	 */
	public static VersionManagement getInstance() {
		if (mVersionManagement == null) {
			mVersionManagement = new VersionManagement();
		}

		return mVersionManagement;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/*
	 * 获取当前版本名号
	 */
	public int getVersionCode(Context context) {
		// 获取packagemanager的实例
		try {
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int version = packInfo.versionCode;
			Log.e("+++++++++++++===", version + "");
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	/*
	 * 获取当前版本名号
	 */
	public String getVersionName(Context context) {
		// 获取packagemanager的实例
		try {
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			String versionnname = packInfo.versionName;
			Log.e("+++++++++++++===", versionnname + "");
			return versionnname;
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}

	}

	/*
	 * 检测更新
	 */
	public void checkUpdate(final Context context, String str) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				try {
					spotsDialog = new SpotsDialog(context);
					spotsDialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				try {
					spotsDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (spotsDialog != null) {
						spotsDialog.cancel();
						spotsDialog = null;
					}
				}
				SimpleHUD.showInfoMessage(context, context.getString(R.string.server_connect_failed));
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
			}

			public void onSuccess(ResponseInfo<String> responseInfo) {
				spotsDialog.dismiss();
				PublicStaticURL.FRISTOPEN = 1;
				String pro = responseInfo.result;// 接口返回的数据
				Logger.i("版本更新：" + pro);
				JSONObject json;
				try {
					json = new JSONObject(pro);
					String code = json.getString("code");
					if ("2".equals(code)) {
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);
						String version = json1.getString("versioncode");
						PublicStaticURL.versiontype = json1.getInt("versiontype");
						int ver = Integer.parseInt(version);
						apkname = json1.getString("name");
						file = new File(ROOT + apkname);
						PublicStaticURL.downloadURl = "http://www.i2dd.com/upload/" + apkname;
						versionCode = getVersionCode(context);
						if (ver > versionCode) {
							if (file.exists() && getWebAppSize() == file.length()) {
								new CustomDialog.Builder(context).setTitle(context.getString(R.string.notify))
										.setMessage(context.getString(R.string.find_new_verssion))
										.setNegativeButton(context.getString(R.string.wait_say), null)
										.setPositiveButton(context.getString(R.string.refresh),
												new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										installApp(context);
									}
								}).create().show();
							} else {
								showUpdateDialog(context, apkname);
							}
						}else{
							new CustomDialog.Builder(context).setTitle(context.getString(R.string.notify))
							.setMessage("当前已是最新版本!").setNegativeButton("确定", null).create().show();
						}
					} else {
						SimpleHUD.showInfoMessage(context, context.getString(R.string.get_version_failed));
					}
				} catch (JSONException e) {
					SimpleHUD.showInfoMessage(context, context.getString(R.string.app_exception));
					e.printStackTrace();
				}
			}

		});
	}

	/*
	 * 检测更新
	 */
	public void checkUpdate(final Context context, String str, final BadgeView mMyBadgeView) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, str, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				try {
					spotsDialog = new SpotsDialog(context);
					spotsDialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				try {
					spotsDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (spotsDialog != null) {
						spotsDialog.cancel();
						spotsDialog = null;
					}
				}
				SimpleHUD.showInfoMessage(context, context.getString(R.string.server_connect_failed));
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
			}

			public void onSuccess(ResponseInfo<String> responseInfo) {
				spotsDialog.dismiss();
				PublicStaticURL.FRISTOPEN = 1;
				String pro = responseInfo.result;// 接口返回的数据
				Logger.i("版本更新：" + pro);
				JSONObject json;
				try {
					json = new JSONObject(pro);
					String code = json.getString("code");
					if ("2".equals(code)) {
						String result = json.getString("result");
						JSONObject json1 = new JSONObject(result);
						String version = json1.getString("versioncode");
						PublicStaticURL.versiontype = json1.getInt("versiontype");
						int ver = Integer.parseInt(version);
						apkname = json1.getString("name");
						file = new File(ROOT + apkname);
						PublicStaticURL.downloadURl = "http://www.i2dd.com/upload/" + apkname;
						versionCode = getVersionCode(context);
						if (ver > versionCode) {
							PublicStaticURL.isNewVersion = 0;
							if (0 == PublicStaticURL.isNewVersion || 0 == PublicStaticURL.hasFirstCard) {
								mMyBadgeView.show();
							} else {
								mMyBadgeView.hide();
							}
							if (file.exists() && getWebAppSize() == file.length()) {
								new CustomDialog.Builder(context).setTitle(context.getString(R.string.notify))
										.setMessage(context.getString(R.string.find_new_verssion))
										.setNegativeButton(context.getString(R.string.wait_say), null)
										.setPositiveButton(context.getString(R.string.refresh),
												new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										installApp(context);
									}
								}).create().show();
							} else {
								showUpdateDialog(context, apkname);
							}
						} else {
							PublicStaticURL.isNewVersion = 1;
							if (1 == PublicStaticURL.isNewVersion || 0 == PublicStaticURL.hasFirstCard) {
								mMyBadgeView.show();
							} else {
								mMyBadgeView.hide();
							}
							mMyBadgeView.hide();
						}
					} else {
						SimpleHUD.showInfoMessage(context, context.getString(R.string.get_version_failed));
					}
				} catch (JSONException e) {
					SimpleHUD.showInfoMessage(context, context.getString(R.string.app_exception));
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * 安装新版本应用
	 */
	public static void installApp(Context context) {
		File appFile = new File(ROOT + apkname);
		if (!appFile.exists()) {
			return;
		}
		// 跳转到新版本应用安装页面
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public void showUpdateDialog(final Context context, final String apkname) {
		if (Utils.getNetworkType(context) == 2) {
			new CustomDialog.Builder(context).setTitle(context.getString(R.string.notify))
					.setMessage(context.getString(R.string.find_new_verssion))
					.setNegativeButton(context.getString(R.string.wait_say), null)
					.setPositiveButton(context.getString(R.string.refresh), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

							new CustomDialog.Builder(context).setTitle(context.getString(R.string.notify))
									.setMessage("现在在非wifi网络环境下，继续下载？").setNegativeButton("取消", null)
									.setPositiveButton("确定", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									if (PublicStaticURL.isDownloading) {
										Toast.makeText(context, "正在下载中。。。", Toast.LENGTH_SHORT).show();
									} else {
										downloadTask = new DownloadAsyncTask(PublicStaticURL.downloadURl, context);
										downloadTask.execute();
										PublicStaticURL.isDownloading = true;
									}
								}
							}).create().show();
						}
					}).create().show();
		} else if (Utils.getNetworkType(context) == 1) {
			if (PublicStaticURL.isDownloading) {
				Toast.makeText(context, "正在下载中。。。", Toast.LENGTH_SHORT).show();
			} else {
				downloadTask = new DownloadAsyncTask(PublicStaticURL.downloadURl, context);
				downloadTask.execute();
				PublicStaticURL.isDownloading = true;
			}
		}
	}

	private long getWebAppSize() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(PublicStaticURL.downloadURl);
					HttpResponse response = client.execute(request);
					size = response.getEntity().getContentLength();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();

		return size;
	}
}
