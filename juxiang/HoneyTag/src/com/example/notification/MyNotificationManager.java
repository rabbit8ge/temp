package com.example.notification;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.example.config.Constants;
import com.example.config.MySharedPreference;

import com.example.dto.RemindDTO;
import com.example.dto.RemindNoticeDTO;
import com.example.dto.StrongPlayDTO;
import com.example.dto.TopicDTO;
import com.example.dto.User;
import com.example.honeytag1.ColumnDetailsActivity;
import com.example.honeytag1.MainActivity;
import com.example.honeytag1.R;
import com.example.honeytag1.RemindActivity;
import com.example.honeytag1.TopicDetailsActivity;
import com.example.honeytag1.WelcomeActivity;
import com.example.utils.PublicStaticURL;
import com.example.utils.TimeUtils;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Button;

public class MyNotificationManager {
	private static final int NOTIFYID00 = 0;// 强踢下线
	private static final int NOTIFYID01 = 1;// 我的话题被收录到栏目
	private static final int NOTIFYID02 = 2;// 我关注的栏目收录了新的话题

	static MyNotificationManager mMyNotificationManager;
	/** Notification管理 */
	public NotificationManager mNotificationManager;
	/** 点击跳转到指定的界面 */
	private Button btn_show_intent_act;
	/** 点击打开指定的界apk */
	private Button btn_show_intent_apk;
	/** Notification构造器 */
	NotificationCompat.Builder mBuilder;
	/** Notification的ID */
	int notifyId = 100;

	public MyNotificationManager() {

	}

	public static MyNotificationManager getInstance() {
		if (mMyNotificationManager == null) {
			mMyNotificationManager = new MyNotificationManager();
		}

		return mMyNotificationManager;
	}

	/** 初始化通知栏 */
	public void initNotify(Context context) {
		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle("测试标题").setContentText("测试内容")
				.setContentIntent(getDefalutIntent(context, 1, Notification.FLAG_AUTO_CANCEL, new Intent()))
				// .setNumber(number)//显示数量
				.setTicker("测试通知来啦")// 通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
				.setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
				// .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
				.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				// .setDefaults(Notification.DEFAULT_VIBRATE)//
				// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
				// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
				// requires VIBRATE permission
				.setSmallIcon(R.drawable.logo);
	}

	/* 从通知栏跳转到对应的activity */
	private PendingIntent getDefalutIntent(Context context, int requestCode, int flags, Intent intent) {
		PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, flags);
		return pendingIntent;

	}

	/* 从通知栏跳转到对应的activity */
	private PendingIntent getDefalutIntent(Context context, int requestCode, int flags, Intent[] intents) {
		PendingIntent pendingIntent = PendingIntent.getActivities(context, requestCode, intents, flags);
		return pendingIntent;

	}

	/** 话题评论通知栏 */
	int count = 0;
	Map<String, String> map = new HashMap<String, String>();

	public void showNotify(Context context, RemindNoticeDTO mRemindDTO, User mUser, String type) {
		int count = 0;
		String content = map.get(mRemindDTO.getPid());
		if (TextUtils.isEmpty(content)) {
			count = 0;
		} else {
			count = Integer.parseInt(content);
		}
		count++;
		map.put(mRemindDTO.getPid(), count + "");

		Intent intent = new Intent(context, MainActivity.class);
		Intent intent1 = new Intent(context, RemindActivity.class);
		Intent intent2 = new Intent(context, TopicDetailsActivity.class);

		PublicStaticURL.pid = mRemindDTO.getPid(); // 将文章id存上 用调去详情
		PublicStaticURL.remind_cid = mRemindDTO.getCid(); // 将文章id存上 用调去详情
		PublicStaticURL.remindto_cid = mRemindDTO.getTo_cid(); // 将文章id存上
		// 用调去详情
		PublicStaticURL.ColumnTitle = mRemindDTO.getTitle();
		if (type.equals("1")) {
			intent2.putExtra("flag", "TX");
		}
		if (type.equals("7")) {
			intent2.putExtra("flag", "TX_WD");
		}

		Intent[] intents = { intent, intent1, intent2 };
		// intent.putExtra("ACTIVITYFLAG", 3);
		mBuilder.setContentTitle(mRemindDTO.getTitle()).setContentText(mRemindDTO.getContent())
				.setContentIntent(getDefalutIntent(context, 1, Notification.FLAG_AUTO_CANCEL, intents)).setNumber(count)// 显示数量
				.setTicker(mRemindDTO.getTitle());// 通知首次出现在通知栏，带上升动画效果的
		Notification mNotification = mBuilder.build();
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

		mNotification = setVoice(context, mNotification, mUser);
		mNotificationManager.notify(1000000 + Integer.parseInt(mRemindDTO.getPid()), mNotification);
		// mNotification.notify(getResources().getString(R.string.app_name),
		// notiId, mBuilder.build());
	}

	/* 设置通知声音 */
	public Notification setVoice(Context context, Notification mNotification, User mUser) {
		if (MySharedPreference.readPeriodTime(context, mUser) && !TimeUtils.isPeriodTime(0, 0, 8, 0)) {
			if (MySharedPreference.readVoiceSwitch(context, mUser)
					&& MySharedPreference.readVibration(context, mUser)) {
				mNotification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
			} else {
				if (MySharedPreference.readVoiceSwitch(context, mUser)) {
					mNotification.defaults |= Notification.DEFAULT_SOUND;
				}
				if (MySharedPreference.readVibration(context, mUser)) {
					mNotification.defaults |= Notification.DEFAULT_VIBRATE;
				}
			}
		} else {
			if (MySharedPreference.readVoiceSwitch(context, mUser)
					&& MySharedPreference.readVibration(context, mUser)) {
				mNotification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
			} else {
				if (MySharedPreference.readVoiceSwitch(context, mUser)) {
					mNotification.defaults |= Notification.DEFAULT_SOUND;
				}
				if (MySharedPreference.readVibration(context, mUser)) {
					mNotification.defaults |= Notification.DEFAULT_VIBRATE;
				}
			}
		}
		return mNotification;
	}

	/** 强踢下线显示通知栏 */
	int count1 = 0;

	public void showNotify(Context context, StrongPlayDTO mStrongPlayDTO, User mUser) {
		count1++;
		Intent intent = new Intent(context, WelcomeActivity.class);

		mBuilder.setContentTitle(mStrongPlayDTO.getTitle()).setContentText(mStrongPlayDTO.getContent())
				.setContentIntent(getDefalutIntent(context, 1, Notification.FLAG_AUTO_CANCEL, intent))
				// .setNumber(count1)// 显示数量
				.setTicker(mStrongPlayDTO.getTitle());// 通知首次出现在通知栏，带上升动画效果的
		Notification mNotification = mBuilder.build();
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		if (MySharedPreference.readVoiceSwitch(context, mUser) && MySharedPreference.readVibration(context, mUser)) {
			mNotification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
		} else {
			if (MySharedPreference.readVoiceSwitch(context, mUser)) {
				mNotification.defaults |= Notification.DEFAULT_SOUND;
			}
			if (MySharedPreference.readVibration(context, mUser)) {
				mNotification.defaults |= Notification.DEFAULT_VIBRATE;
			}
		}

		mNotificationManager.notify(NOTIFYID00, mNotification);

		// mNotification.notify(getResources().getString(R.string.app_name),
		// notiId, mBuilder.build());
	}

	/** 我的话题被收录到栏目和我关注的栏目收录了新话题 */
	int count2 = 0;

	public void showNotifyColumn(Context context, TopicDTO mTopicDTO, User mUser) {
		PublicStaticURL.mTopicDTO = mTopicDTO;
		PublicStaticURL.ColumnTitle = mTopicDTO.getColumn_title();
		count2++;

		Intent intent = new Intent(context, MainActivity.class);
		Intent intent1 = new Intent(context, ColumnDetailsActivity.class);
		Intent[] intents = { intent, intent1 };
		intent.putExtra("id", mTopicDTO.getTid());
		intent1.putExtra("id", mTopicDTO.getTid());
		mBuilder.setContentTitle(mTopicDTO.getTitle()).setContentText(mTopicDTO.getContent())
				.setContentIntent(getDefalutIntent(context, 1, Notification.FLAG_AUTO_CANCEL, intents))
				.setNumber(count2)// 显示数量
				.setTicker(mTopicDTO.getTitle());// 通知首次出现在通知栏，带上升动画效果的
		Notification mNotification = mBuilder.build();
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		mNotification = setVoice(context, mNotification, mUser);
		mNotificationManager.notify(10000 + Integer.parseInt(mTopicDTO.getTid()), mNotification);

		// mNotification.notify(getResources().getString(R.string.app_name),
		// notiId, mBuilder.build());
	}

	/** 我关注的栏目收录了新的话题 */
	int count3 = 0;

	public void showNotifyAttentionColumnFromTopic(Context context, TopicDTO mTopicDTO, User mUser) {
		PublicStaticURL.mTopicDTO = mTopicDTO;
		PublicStaticURL.ColumnTitle = mTopicDTO.getColumn_title();
		count3++;

		Intent intent = new Intent(context, MainActivity.class);
		Intent intent1 = new Intent(context, ColumnDetailsActivity.class);
		Intent[] intents = { intent, intent1 };
		mBuilder.setContentTitle(mTopicDTO.getTitle()).setContentText(mTopicDTO.getContent())
				.setContentIntent(getDefalutIntent(context, 1, Notification.FLAG_AUTO_CANCEL, intents))
				.setNumber(count3)// 显示数量
				.setTicker(mTopicDTO.getTitle());// 通知首次出现在通知栏，带上升动画效果的
		Notification mNotification = mBuilder.build();
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		mNotification = setVoice(context, mNotification, mUser);
		mNotificationManager.notify(100000 + Integer.parseInt(mTopicDTO.getTid()), mNotification);

		// mNotification.notify(getResources().getString(R.string.app_name),
		// notiId, mBuilder.build());
	}

	/**
	 * 清除当前创建的通知栏
	 */
	public void clearNotify(int notifyId) {
		mNotificationManager.cancel(notifyId);// 删除一个特定的通知ID对应的通知
		// mNotification.cancel(getResources().getString(R.string.app_name));
	}

	/**
	 * 清除所有通知栏
	 */
	public void clearAllNotify() {

		count = 0;
		count1 = 0;
		count2 = 0;
		count3 = 0;
		if (map != null) {
			map.clear();
		}
		PublicStaticURL.activityFlag = -1;
		Constants.logoffNotification = -1;
		PublicStaticURL.columnFlag = -1;
		mNotificationManager.cancelAll();// 删除你发的所有通知
	}

	/**
	 * @获取默认的pendingIntent,为了防止2.3及以下版本报错
	 * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT 点击去除：
	 *           Notification.FLAG_AUTO_CANCEL
	 */
	public PendingIntent getDefalutIntent(Context context, int flags) {
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(), flags);
		return pendingIntent;
	}

}
