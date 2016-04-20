package com.example.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import com.example.honeytag1.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DownloadAsyncTask extends AsyncTask<String, Integer, Long> {
	private static final String TAG = "DownloadAsyncTask";
	private static String mUrl;
	// private static DownloadAsyncTask downloadTask;
	NotificationManager manager;
	private static Context context;
	// 下载路径
	public static final String MNT = Environment.getExternalStorageDirectory().toString();
	public static final String downloadPath = MNT + File.separator + "行家说";
	NotificationCompat.Builder nb;
	private int thispercent;
	private int lastpercent;
	private SharedPreferences pref = null;
	private long fileSize;

	public DownloadAsyncTask(String url, Context context) {
		this.mUrl = url;
		this.context = context;
	}

	@Override
	protected Long doInBackground(String... params) {
		Log.i(TAG, "downloading");
		if (mUrl == null) {
			return null;
		}
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(mUrl);
		HttpResponse response = null;
		InputStream is = null;
		RandomAccessFile fos = null;
		OutputStream output = null;

		try {
			// 创建存储文件夹
			File dir = new File(downloadPath);
			if (!dir.exists()) {
				dir.mkdir();
			}
			// 本地文件
			File file = new File(downloadPath + File.separator + mUrl.substring(mUrl.lastIndexOf("/") + 1));
			if (!file.exists()) {
				Log.i(TAG, "file不存在");
				// 创建文件输出流
				output = new FileOutputStream(file);
				// 获取下载输入流
				response = client.execute(request);
				is = response.getEntity().getContent();
				// 写入本地
				file.createNewFile();
				byte buffer[] = new byte[1024];
				int inputSize = -1;
				// 获取文件总大小，用于计算进度
				long total = response.getEntity().getContentLength();
				int count = 0; // 已下载大小
				while ((inputSize = is.read(buffer)) != -1) {
					output.write(buffer, 0, inputSize);
					count += inputSize;
					// 更新进度
					this.publishProgress((int) ((count / (float) total) * 100));
					// 一旦任务被取消则退出循环，否则一直执行，直到结束
					if (isCancelled()) {
						output.flush();
						return null;
					}
				}
				output.flush();
				return total;
			} else {
				long size = file.length(); // 文件大小，即已下载大小
				response = client.execute(request);
				fileSize = response.getEntity().getContentLength();
				if (size == fileSize) {

				} else {
					// 设置下载的数据位置XX字节到XX字节
					Header header_size = new BasicHeader("Range", "bytes=" + size + "-" + fileSize);
					request.addHeader(header_size);
					response = client.execute(request);
					is = response.getEntity().getContent();
					if (is == null) {
						throw new RuntimeException("stream is null");
					}

					fos = new RandomAccessFile(file, "rw");
					// 从文件的size以后的位置开始写入，其实也不用，直接往后写就可以。有时候多线程下载需要用
					fos.seek(size);
					byte buf[] = new byte[1024];
					long downloadfilesize = 0;
					do {
						int numread = is.read(buf);
						if (numread <= 0) {
							break;
						}
						fos.write(buf, 0, numread);
						downloadfilesize += numread;
						publishProgress((int) ((downloadfilesize + size) / fileSize * 100));
					} while (true);
				}
				return fileSize;
			}
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (output != null) {
					output.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		Log.i(TAG, "download begin ");
		super.onPreExecute();
		manager = (NotificationManager) MyApplication.getContextObject()
				.getSystemService(MyApplication.getContextObject().NOTIFICATION_SERVICE);
		nb = new NotificationCompat.Builder(MyApplication.getContextObject());
		if (Utils.getNetworkType(MyApplication.getContextObject()) == 2) {// 流量下

			nb.setContentTitle("行家说更新包下载");
			nb.setContentText("下载中...0%");
			nb.setSmallIcon(R.drawable.logo);
			nb.setWhen(System.currentTimeMillis());
			manager.notify(1, nb.build());
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		Log.i(TAG, "downloading  " + values[0]);
		thispercent = values[0];
		if (thispercent > lastpercent) {
			if (Utils.getNetworkType(MyApplication.getContextObject()) == 2) {// 流量下
				nb.setContentText("下载中..." + values[0] + "%");
				manager.notify(1, nb.build());
			}
			lastpercent = thispercent;
		}
	}

	@Override
	protected void onPostExecute(Long aLong) {
		Log.i(TAG, "download success " + aLong);
		super.onPostExecute(aLong);
		PublicStaticURL.isDownloading=false;
		if (aLong != null) {
			if (Utils.getNetworkType(MyApplication.getContextObject()) == 2) {// 流量环境下
				installApp(context, mUrl.substring(mUrl.lastIndexOf("/") + 1));
				nb.setContentText("下载完成。");
				manager.notify(1, nb.build());

			} else if (Utils.getNetworkType(MyApplication.getContextObject()) == 1) {// wifi下
				Intent intent = new Intent("APK_FILE_ALREALY_DONWLOAD");
				intent.putExtra("filename", mUrl.substring(mUrl.lastIndexOf("/") + 1));
				MyApplication.getContextObject().sendBroadcast(intent);

			}
		}

	}

	/**
	 * 安装新版本应用
	 */
	public static void installApp(Context context, String apkname) {
		if (apkname == null) {
			apkname = mUrl.substring(mUrl.lastIndexOf("/") + 1);
		}
		File appFile = new File(downloadPath + File.separator + apkname);
		if (!appFile.exists()) {
			return;
		}
		// 跳转到新版本应用安装页面
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
