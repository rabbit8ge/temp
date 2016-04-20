package com.example.utils;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.example.honeytag1.R;
import com.example.my.LoginActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Toast;

public class Utils {
	public static int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			// String extraInfo = networkInfo.getExtraInfo();
			// if(!StringUtils.isEmpty(extraInfo)){
			// if (extraInfo.toLowerCase().equals("cmnet")) {
			// netType = 2;
			// } else {
			// netType = 2;
			// }
			// }
			netType = 2;
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = 1;
		}
		return netType;
	}

	/**
	 * 当前是否有网络
	 * 
	 * @param activity
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		// Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					Logger.i(i + "===状态===" + networkInfo[i].getState());
					Logger.i(i + "===类型===" + networkInfo[i].getTypeName());
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * 监听键盘状态
	 * 
	 * @param root
	 */
	public static void KeyBrodLisenert(final View root, final View scrollToView) {

		root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {

				Rect rect = new Rect();
				// 获取root在窗体的可视区域
				root.getWindowVisibleDisplayFrame(rect);
				// 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
				int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
				// 若不可视区域高度大于100，则键盘显示
				if (rootInvisibleHeight > 100) {
					Logger.i("键盘开启了");
					int[] location = new int[2];
					// 获取scrollToView在窗体的坐标
					scrollToView.getLocationInWindow(location);
					// 计算root滚动高度，使scrollToView在可见区域
					int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
					root.scrollTo(0, srollHeight);
				} else {
					// 键盘隐藏
					Logger.i("键盘关闭了");

					root.scrollTo(0, 0);
				}
			}
		});
	}

	/**
	 * 检测是否有虚拟按键
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkDeviceHasNavigationBar(Context context) {
		boolean hasNavigationBar = false;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
		if (id > 0) {
			hasNavigationBar = rs.getBoolean(id);
		}
		try {
			Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
			Method m = systemPropertiesClass.getMethod("get", String.class);
			String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
			if ("1".equals(navBarOverride)) {
				hasNavigationBar = false;
			} else if ("0".equals(navBarOverride)) {
				hasNavigationBar = true;
			}
		} catch (Exception e) {
		}

		return hasNavigationBar;

	}

	/**
	 * 获取虚拟案件高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getNavigationBarHeight(Context context) {
		int navigationBarHeight = 0;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
		if (id > 0 && checkDeviceHasNavigationBar(context)) {
			navigationBarHeight = rs.getDimensionPixelSize(id);
		}
		return navigationBarHeight;
	}

	/**
	 * 验证是否是手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证手机格式是否正确 13[0-9] 147[0-9] 15[0-9] 18[0-9]
	 * 
	 * @param no
	 *            "^1(3[4-9]|4[7]|5[012789]|8[2378])\\d{8}$" //第一个是匹配手机号码�?
	 *            ^1是文章的�?头为1 �?3 匹配3 �?4-9匹配4-9 组合�?134,135,136,137,138,139
	 *            |是或�?147 或�??150,151�?152�?157,158,159，或�?182,183,187,188�?�?
	 *            \\\d{8}$' \\d是数字�?�{8}�?8位数字�??$结束�?
	 * 
	 *            "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"
	 *            ://�?13为开头的号码，以15�?头的号码除了154的号码，
	 *            18�?头的号码[180.185.186.187.188.189].8位号�?
	 * 
	 * 
	 *            "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
	 * 
	 * @return
	 */
	public static boolean isMObilehone(String no) {
		// 以13为开头的号码，以15为开头的号码除了154的号码，18为头的号码[180.185.186.187.188.189].8位号码
		String regex = "^1(3[0-9]|4[0-9]|5[0-9]|8[0-9])\\d{8}$"; // 手机
		return match(regex, no);
	}

	/**
	 * @param regex
	 *            正则表达
	 * @param str
	 *            要匹配的字符
	 * @return
	 */
	private static boolean match(String regex, String str) {
		if (str == null || str.equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 对时间的处理
	 */
	public static String timeSpan(String time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		try {
			Date data = df.parse(df.format(new Date()));
			Date intime = df.parse(time);
			// Logger.i(data+"。。。"+intime);
			long outtime = data.getTime() - intime.getTime();
			int days = (int) (outtime / (1000 * 60 * 60 * 24));
			int hours = (int) (outtime / (1000 * 60 * 60));
			int minute = (int) (outtime / (1000 * 60));
			int second = (int) (outtime / (1000));
			if (days > 0) {
				return days + "天前";
			}
			if (hours > 0) {
				return hours + "小时前";
			}
			if (minute > 0) {
				return minute + "分钟前";
			}
			if (second > 0) {
				return second + "秒前";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "刚刚";

	}

	// 字符串截取
	public static String Transformation(String s) {
		String str = s.replace("Φ", "");
		String str1 = str.replace("Φ", "");
		return str1;
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/*
	 * 跳转到登录界面
	 */
	public static void toLogin(Context context) {
		Toast.makeText(context, context.getString(R.string.please_first_login), Toast.LENGTH_SHORT).show();
		context.startActivity(new Intent(context, LoginActivity.class));
	}
}
