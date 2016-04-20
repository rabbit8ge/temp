package com.example.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import com.example.dto.User;
import com.example.dto.UserInfo;
import com.example.utils.TimeUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;

/**
 * @Description描述:设置
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class MySharedPreference {

	/**
	 * 保存了用户信息(已登录)
	 * 
	 * @param context
	 * @param user
	 * 
	 */

	public static void saveUser(Context context, User user) {
		SharedPreferences preferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			// 创建对象输出流，并封装字节流
			oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(user);
			// 将字节流编码成base64的字符窜
			// String oAuth_Base64 = new String(Base64.encodeBase64(baos
			// .toByteArray()));

			String oAuth_Base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
			// String oAuth_Base64 = oos.toString();
			Editor editor = preferences.edit();
			editor.putString("user", oAuth_Base64);

			editor.commit();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Log.i("ok", "存储成功");
	}

	/**
	 * 获取了用户信息
	 * 
	 * @param context
	 * @param user
	 * 
	 */
	public static User readUser(Context context) {
		User user = null;
		SharedPreferences preferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
		String productBase64 = preferences.getString("user", "");
		// 读取字节
		byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);

		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		ObjectInputStream bis = null;
		try {
			// 再次封装
			bis = new ObjectInputStream(bais);
			try {
				// 读取对象
				user = (User) bis.readObject();
				bis.close();
				bais.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (bais != null) {
				try {
					bais.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return user;
	}

	/**
	 * 保存了用户信息(已登录)
	 * 
	 * @param context
	 * @param user
	 * 
	 */

	public static void saveUserInfo(Context context, UserInfo user) {
		SharedPreferences preferences = context.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			// 创建对象输出流，并封装字节流
			oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(user);
			// 将字节流编码成base64的字符窜
			// String oAuth_Base64 = new String(Base64.encodeBase64(baos
			// .toByteArray()));

			String oAuth_Base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
			// String oAuth_Base64 = oos.toString();
			Editor editor = preferences.edit();
			editor.putString("userinfo", oAuth_Base64);

			editor.commit();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Log.i("ok", "存储成功");
	}

	/**
	 * 获取了用户信息
	 * 
	 * @param context
	 * @param user
	 * 
	 */
	public static synchronized UserInfo readUserInfo(Context context) {
		UserInfo user = null;
		SharedPreferences preferences = context.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
		String productBase64 = preferences.getString("userinfo", "");
		// 读取字节
		byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);

		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		ObjectInputStream bis = null;
		try {
			// 再次封装
			bis = new ObjectInputStream(bais);
			try {
				// 读取对象
				user = (UserInfo) bis.readObject();
				bis.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return user;
	}

	/*
	 * ====================================通知开关状态===============================
	 * ===============
	 */

	/**
	 * 保存新消息通知的总开关的状态
	 * 
	 * @param context
	 * @param bool
	 * 
	 */

	public static void saveNewNotifications(Context context, Boolean bool) {
		SharedPreferences preferences = context.getSharedPreferences("NEWNOTIFICATIONS", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(MySharedPreference.readUser(context).getUid() + "newNotifications", bool);
		editor.commit();

		Log.i("ok", "存储成功");
	}

	/**
	 * 获取新消息通知的总开关的状态
	 * 
	 * @param context
	 * 
	 * 
	 */
	public static Boolean readNewNotifications(Context context) {
		User mUser = MySharedPreference.readUser(context);
		String uid = mUser.getUid();
		SharedPreferences preferences = context.getSharedPreferences("NEWNOTIFICATIONS", Context.MODE_PRIVATE);
		Boolean bool = preferences.getBoolean(uid + "newNotifications", true);
		return bool;
	}

	/**
	 * 保存通知声音状态
	 * 
	 * @param context
	 * @param bool
	 * 
	 */

	public static void saveVoiceSwitch(Context context, Boolean bool) {
		SharedPreferences preferences = context.getSharedPreferences("VOICE", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(MySharedPreference.readUser(context).getUid() + "voice", bool);
		editor.commit();

		Log.i("ok", "存储成功");
	}

	/**
	 * 获取了通知声音状态
	 * 
	 * @param context
	 * 
	 * 
	 */
	public static synchronized Boolean readVoiceSwitch(Context context, User mUser) {

		String uid = mUser.getUid();
		SharedPreferences preferences = context.getSharedPreferences("VOICE", Context.MODE_PRIVATE);
		Boolean bool = preferences.getBoolean(uid + "voice", true);
		return bool;
	}

	/**
	 *
	 * 保存通知震动状态
	 * 
	 * @param context
	 * @param bool
	 * 
	 */

	public static void saveVibration(Context context, Boolean bool) {
		SharedPreferences preferences = context.getSharedPreferences("VIBRATION", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(MySharedPreference.readUser(context).getUid() + "vibration", bool);
		editor.commit();

		Log.i("ok", "存储成功");
	}

	/**
	 * 获取了通知震动状态
	 * 
	 * @param context
	 * 
	 * 
	 */
	public static Boolean readVibration(Context context, User mUser) {

		String uid = mUser.getUid();
		SharedPreferences preferences = context.getSharedPreferences("VIBRATION", Context.MODE_PRIVATE);
		Boolean bool = preferences.getBoolean(uid + "vibration", true);
		return bool;

	}

	/**
	 *
	 * 保存通知时间段状态
	 * 
	 * @param context
	 * @param bool
	 * 
	 */

	public static void savePeriodTime(Context context, Boolean bool) {
		SharedPreferences preferences = context.getSharedPreferences("PERIODTIME", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(MySharedPreference.readUser(context).getUid() + "PeriodTime", bool);
		editor.commit();

		Log.i("ok", "存储成功");
	}

	/**
	 * 获取了通知时间段状态
	 * 
	 * @param context
	 * 
	 * 
	 */
	public static Boolean readPeriodTime(Context context, User mUser) {

		String uid = mUser.getUid();
		SharedPreferences preferences = context.getSharedPreferences("PERIODTIME", Context.MODE_PRIVATE);
		Boolean bool = preferences.getBoolean(uid + "PeriodTime", true);
		return bool;
	}

	/**
	 *
	 * 保存点击栏目菜单的时间
	 * 
	 * @param context
	 * @param
	 * 
	 */

	public static void saveTime(Context context, String time) {
		SharedPreferences preferences = context.getSharedPreferences("TIME", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("time", time);
		editor.commit();
		Log.i("ok", "存储成功");
	}

	/**
	 * 获取了点击栏目菜单的时间
	 * 
	 * @param context
	 * 
	 * 
	 */
	public static String readTime(Context context) {

		SharedPreferences preferences = context.getSharedPreferences("TIME", Context.MODE_PRIVATE);
		String time = preferences.getString("time", TimeUtils.getStringToDate("1970年1月1日") + "");
		return time;
	}

}
