package com.example.honeytag1;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.dialog.CustomDialog;
import com.example.dialog.SpotsDialog;
import com.example.dto.StrongPlayDTO;
import com.example.my.LoginActivity;
import com.example.notification.MyNotificationManager;
import com.example.receiver.HomeKeyEventReceiver;
import com.example.utils.Logger;
import com.example.utils.MyApplication;
import com.example.utils.PublicStaticURL;
import com.example.utils.MyApplication.PushListener;
import com.umeng.analytics.MobclickAgent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnKeyListener;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity implements PushListener {
	public static final String MNT = Environment.getExternalStorageDirectory().toString();
	public static final String ROOT = MNT + File.separator + "行家说" + File.separator ;
	private IntentFilter filter;
	private HomeKeyEventReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		loadXml();
		loadData();
		MyApplication.getInstance().setPushListener(this);// 强踢下线
		filter = new IntentFilter();
		filter.addAction("APK_FILE_ALREALY_DONWLOAD");
		receiver = new HomeKeyEventReceiver();
		registerReceiver(receiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
	}

	public abstract void loadXml();

	public abstract void loadData();

	public void umengResume(Context context, String s) {
		MobclickAgent.onResume(context);
		MobclickAgent.onPageStart(s);
	}

	public void umengPause(Context context, String s) {
		MobclickAgent.onPause(context);
		MobclickAgent.onPageEnd(s);
	}

	

	private  void showUpdateDialog(final Context context,final String name) {
		 Toast.makeText(context, PublicStaticURL.versiontype+"", Toast.LENGTH_SHORT).show();
			if (PublicStaticURL.versiontype==2) {
				new CustomDialog.Builder(context).setTitle(context.getString(R.string.notify))
						.setMessage(context.getString(R.string.find_new_verssion))
						.setNegativeButton(context.getString(R.string.wait_say), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								// Toast.makeText(context, "强制更新",
								// Toast.LENGTH_SHORT).show();
								MyApplication.getInstance().exit();
							}
						})
						.setPositiveButton(context.getString(R.string.refresh), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								installApp(context,name);
							}
						}).create(false).show();

			} else {
				new CustomDialog.Builder(context).setTitle(context.getString(R.string.notify))
						.setMessage(context.getString(R.string.find_new_verssion))
						.setNegativeButton(context.getString(R.string.wait_say), null)
						.setPositiveButton(context.getString(R.string.refresh), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								installApp(context,name);
							}
						}).create().show();

			}
	}

	BroadcastReceiver onLoadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("APK_FILE_ALREALY_DONWLOAD")) {
				Logger.i("收到广播了。。。");
				String apkname =  intent.getStringExtra("filename");
				showUpdateDialog(context,apkname);
			}
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(onLoadReceiver);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		MyApplication.getInstance().setPushListener(this);
		registerReceiver(onLoadReceiver, filter);
		MyApplication.getInstance().homekeyFlag = "0";
	
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public void notice(String str) {
		if (TextUtils.isEmpty(str)) {
			return;
		}

		try {
			JSONObject ob = new JSONObject(str);
			String type = ob.getString("type");
			String result = ob.getString("result");
			if (type.equals("2")) {
				StrongPlayDTO mStrongPlayDTO = StrongPlayDTO.getStrongPlayDTO(result);
				if (MyApplication.getInstance().currentActivity() != null) {

					CustomDialog.Builder ibuilder = new CustomDialog.Builder(this);

					ibuilder.setTitle("提示");
					ibuilder.setMessage(mStrongPlayDTO.getContent());
					ibuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(MyApplication.getInstance().currentActivity(),
									LoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							// MySharedPreference.saveUser(context, new User());
							// PublicStaticURL.IsLogin = false;
							arg0.dismiss();
						}
					});

					CustomDialog dialog = ibuilder.create();
					dialog.setCanceledOnTouchOutside(false);
					// dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
					dialog.show();
					dialog.setOnKeyListener(new OnKeyListener() {

						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

							if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
								return true;
							} else {
								return false;
							}
						}
					});
				}
			}
			if (type.equals("4")) {// 扣分
				showVipGradeBottomWindow(this);
			}
			if (type.equals("6")) {// 升级
				showVipGradeTopWindow(this);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showVipGradeTopWindow(Context context) {
		new CustomDialog.Builder(context).setMessage("恭喜您升到2级")
				.setNegativeButton(context.getString(R.string.confirm), null).create().show();
	}

	public void showVipGradeBottomWindow(Context context) {
		new CustomDialog.Builder(context).setMessage("您因发送暴力色情消息，\n根据规则您将被扣除10分！\n累计3次直接清零！")
				.setNegativeButton(context.getString(R.string.confirm), null).create().show();
	}
	/**
	 * 安装新版本应用
	 */
	public static void installApp(Context context,String apkname) {
		File appFile = new File(ROOT+apkname);
		if (!appFile.exists()) {
			return;
		}
		// 跳转到新版本应用安装页面
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
