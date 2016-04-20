package com.example.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.config.Constants;
import com.example.config.MySharedPreference;
import com.example.dto.RemindNoticeDTO;
import com.example.dto.StrongPlayDTO;
import com.example.dto.TopicDTO;
import com.example.dto.User;
import com.example.honeytag1.R;
import com.example.http.Loadings;
import com.example.notification.MyNotificationManager;
import com.lidroid.xutils.HttpUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.utils.Log;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 将所有activty添加到这里
 * 
 * @author Administrator
 * 
 */
public class MyApplication extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static MyApplication instance;
	private static Context context;
	private PushListener mPushListener;// 一般的消息
	private PushStrongPlayOfflineListener mPushStrongPlayOfflineListener;
	public String count = "";// 提醒数量
	public String device_token;
	public String homekeyFlag = "-1";// 0代表应用处于前台 1，代表应用处于后台
	public Boolean isAddCard = false;// 注册成功在首页是否弹出对话提示“用户去添加名片”(true，代表弹出；false，代表不弹出)
	public int columnRedDotFlag = -1;// 栏目红点 0,显示红点；1,不显示红点
	public HttpUtils httpUtils;
	private User mUser;

	// private
	@Override
	public void onCreate() {
		instance = this;
		context = getApplicationContext();
		initHttpUtils();
		/** 设置是否对日志信息进行加密, 默认false(不加密). */
		AnalyticsConfig.enableEncrypt(true);
		MobclickAgent.setDebugMode(true);// 测试模式
		MobclickAgent.openActivityDurationTrack(false);// 禁止默认的页面统计方式，这样将不会再自动统计Activity
		PlatformConfig.setWeixin("wx51e0127a4d34d959", "efb38753d23cf72804b9a224749f35bb");
		// 微信 appid appsecret
		PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
		// 新浪微博 appkey appsecret
		PlatformConfig.setQQZone("1105216516", "T5LLaNbNoyIHeaA");
		// QQ和Qzone appid appkey
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.setDebugMode(true);
		mPushAgent.enable();

		/**
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				// Toast.makeText(context, msg.custom,
				// Toast.LENGTH_LONG).show();
				Log.i("msg.custom:" + msg.custom);
			}
		};

		mPushAgent.setNotificationClickHandler(notificationClickHandler);

		UmengMessageHandler messageHandler = new UmengMessageHandler() {
			@Override
			public void dealWithCustomMessage(final Context context, final UMessage msg) {
				new Handler(getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 对自定义消息的处理方式，点击或者忽略
						boolean isClickOrDismissed = true;
						if (isClickOrDismissed) {
							// 自定义消息的点击统计
							UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
						} else {
							// 自定义消息的忽略统计
							UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
						}

						// Toast.makeText(context, msg.custom,Toast.LENGTH_LONG).show();
						String aa = msg.custom;
						Log.i("msg.customaaaaaaaaaa:" + msg.custom + "");
						mUser = MySharedPreference.readUser(instance);
						parsing(context, msg.custom);
					}
				});
			}

			@Override
			public Notification getNotification(Context context, UMessage msg) {
				// Toast.makeText(context, msg.title + "|||" + msg.text,
				// Toast.LENGTH_LONG).show();
				return super.getNotification(context, msg);
			}
		};

		mPushAgent.setMessageHandler(messageHandler);
		super.onCreate();

	}

	/* 初始化网络请求 */
	private void initHttpUtils() {
		httpUtils = new HttpUtils();
		// 设置当前请求的缓存时间
		httpUtils.configCurrentHttpCacheExpiry(0 * 1000);
		// 设置默认请求的缓存时间
		httpUtils.configDefaultHttpCacheExpiry(0);

	}

	public void function(String str) {
		
	}

	/*
	 * 解析推送过来的消息
	 */
	public void parsing(final Context context, String str) {
		if (TextUtils.isEmpty(str)) {
			return;
		}

		JSONObject ob;
		try {
			ob = new JSONObject(str);
			String type = ob.getString("type");

			if (type.equals("1") || type.equals("7")) {// 提醒列表

				if (mUser != null && MySharedPreference.readNewNotifications(context)) {
					Loadings.getCount(PublicStaticURL.ZONGmessage + "&uid=" + mUser.getUid());

					try {
						JSONObject object = new JSONObject(str);
						String message = object.getString("message");
						RemindNoticeDTO mRemindNoticeDTO = RemindNoticeDTO.fromJson(message);
						String aa = mRemindNoticeDTO.toString();
						if (mRemindNoticeDTO != null && MySharedPreference.readNewNotifications(context)) {

							User user = MySharedPreference.readUser(context);
							if (user == null) {

							} else {
								if (homekeyFlag.equals("1")) {
									//PublicStaticURL.activityFlag = 0;
									MyNotificationManager.getInstance().initNotify(context);
									MyNotificationManager.getInstance().showNotify(context, mRemindNoticeDTO, mUser,type);
								}

								if (homekeyFlag.equals("0")) {
									if (mPushListener != null) {
										mPushListener.notice(str);
									}
									PublicStaticURL.userid = user.getUid(); // 将用户ID储存，
									PublicStaticURL.userid = user.getUid(); // 将用户ID储存，
									PublicStaticURL.ablity = user.getAbility(); // 用户能力值储存
									PublicStaticURL.credit = user.getCredit(); // 用户信用值储存
									PublicStaticURL.Login_phone = user.getTel(); // 用户名
									PublicStaticURL.IsLogin = true;
								}

							}

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
			if (type.equals("2")) {// 强制踢出对方。。

				if (mUser != null) {
					
					aa();
					ob = new JSONObject(str);
					String result = ob.getString("result");
					StrongPlayDTO mStrongPlayDTO = StrongPlayDTO.getStrongPlayDTO(result);
					MySharedPreference.saveUser(context, null);
					PublicStaticURL.IsLogin = false;
					if (homekeyFlag.equals("1")) {
						Constants.logoffNotification=0;
						MyNotificationManager.getInstance().initNotify(this);
						MyNotificationManager.getInstance().showNotify(this, mStrongPlayDTO, mUser);
					}
					if (homekeyFlag.equals("0")) {
						if (mPushListener != null) {
							mPushListener.notice(str);
						}
					}

				}
			}

			/** 我的话题被收录到栏目 */
			if (type.equals("3")) {

				if (mUser != null && MySharedPreference.readNewNotifications(context)) {
					ob = new JSONObject(str);
					String result = ob.getString("result");
					TopicDTO mTopicDTO = TopicDTO.getTopicDTO(result);

					if (homekeyFlag.equals("1")) {
						PublicStaticURL.columnFlag = 0;
						MyNotificationManager.getInstance().initNotify(context);
						MyNotificationManager.getInstance().showNotifyColumn(context, mTopicDTO, mUser);

					}
					if (homekeyFlag.equals("0")) {
						if (mPushListener != null) {
							mPushListener.notice(str);
						}
					}

				}

			}

			/** 我关注的栏目收录了新的话题 */
			if (type.equals("5")) {

				if (mUser != null && MySharedPreference.readNewNotifications(context)) {

					ob = new JSONObject(str);
					String result = ob.getString("result");
					TopicDTO mTopicDTO = TopicDTO.getTopicDTO(result);
					if (homekeyFlag.equals("1")) {
						PublicStaticURL.columnFlag = 1;
						MyNotificationManager.getInstance().initNotify(context);
//						MyNotificationManager.getInstance().showNotifyAttentionColumnFromTopic(context, mTopicDTO,
//								mUser);
						MyNotificationManager.getInstance().showNotifyColumn(context, mTopicDTO, mUser);

					}
					if (homekeyFlag.equals("0")) {
						if (mPushListener != null) {
							mPushListener.notice(str);
						}
					}

				}

			}

			/* 扣分 */
			if (type.equals("4")) {

				if (mUser != null) {

					if (mPushListener != null) {
						mPushListener.notice(str);
					}
				}

			}

			/* 升级 */
			if (type.equals("6")) {

				if (mUser != null) {

					if (mPushListener != null) {
						mPushListener.notice(str);
					}
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * 判断activity是在前台还是后台运行
	 */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				/*
				 * BACKGROUND=400 EMPTY=500 FOREGROUND=100 GONE=1000
				 * PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
				 */

				Log.i(context.getPackageName(), "此appimportace =" + appProcess.importance
						+ ",context.getClass().getName()=" + context.getClass().getName());
				if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					Log.i(context.getPackageName(), "处于后台" + appProcess.processName);
					return true;
				} else {
					Log.i(context.getPackageName(), "处于前台" + appProcess.processName);
					return false;
				}
			}
		}

		return false;
	}

	// 鍗曚緥妯″紡涓幏鍙栧敮涓?鐨凪yApplication瀹炰緥
	public static MyApplication getInstance() {

		return instance;
	}

	// 娣诲姞Activity鍒板鍣ㄤ腑
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/*
	 * 获得当前activity
	 */
	public Activity currentActivity() {
		Activity activity = null;
		if (activityList != null && activityList.size() > 0) {
			activity = activityList.get(activityList.size() - 1);
		}
		return activity;
	}

	/*
	 * 将最后一个入栈的Activity从栈中移除
	 */
	public void popActivity() {
		Activity activity = currentActivity();
		if (activity != null) {
			activity.finish();
			activity = null;
		}
	}

	/*
	 * 将指定的activity从栈中移除
	 */
	public void popActivity(Activity activity) {
		if (activity != null) {
			activity.finish();
			activityList.remove(activity);
			activity = null;
		}
	}

	/*
	 * 通过类名移除某个Activity
	 */
	public <T> void popActivity(Class<T> cls) {
		if (cls == null) {
			return;
		}
		List<Activity> mList = new ArrayList<Activity>();
		Activity mActivity;
		if (cls != null) {
			for (int i = 0; i < activityList.size(); i++) {
				mActivity = activityList.get(i);
				if (mActivity.getClass() == cls) {

					mActivity.finish();
				}

			}

		}

	}

	/*
	 * 获得某个指定的activity
	 */
	// public <T extends Activity> T getActivity(Class<T> cls) {
	// Activity mActivity;
	//
	// if (cls != null) {
	// for (int i = 0; i < activityList.size(); i++) {
	// mActivity = activityList.get(i);
	// if (mActivity.getClass() == cls) {
	//
	// return (T) mActivity;
	// }
	//
	// }
	//
	// }
	//
	// return null;
	// }

	/*
	 * 获得某个指定的activity
	 */
	// public List<Activity> getActivity(Class<Activity> cls) {
	// List<Activity> mList = new ArrayList<Activity>();
	// Activity mActivity;
	// if (cls != null) {
	// for (int i = 0; i < activityList.size(); i++) {
	// mActivity = activityList.get(i);
	// if (mActivity.getClass() == cls) {
	//
	// mList.add(mActivity);
	// }
	//
	// }
	//
	// }
	//
	// return mList;
	// }

	// 閬嶅巻鎵?鏈堿ctivity骞秄inish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

	/*
	 * 推送监听接口
	 */
	public interface PushListener {
		void notice(String str);
	}

	/* 强踢下线 */
	public interface PushStrongPlayOfflineListener {
		void notice(String str);
	}

	/*
	 * 设置推送监听
	 */
	public void setPushListener(PushListener mPushListener) {
		this.mPushListener = mPushListener;
	}

	/*
	 * 设置推送监听
	 */
	public void setPushStrongPlayOfflineListener(PushStrongPlayOfflineListener mPushStrongPlayOfflineListener) {
		this.mPushStrongPlayOfflineListener = mPushStrongPlayOfflineListener;
	}

	// 返回
	public static Context getContextObject() {
		return context;
	}

	public void aa() {

		PackageManager manager = getPackageManager();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
		
		for(ResolveInfo mResolveInfo:apps){
			if(mResolveInfo.activityInfo.packageName.equals(this.getPackageName())){
				mResolveInfo.icon=R.drawable.ablit_wen;
			}
			
		}
	
	}
}
