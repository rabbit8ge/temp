package com.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.honeytag1.R;
import com.example.utils.gif.AnimatedGifDrawable;
import com.example.utils.gif.AnimatedImageSpan;

public class ExpressionUtil {

	public static SpannableStringBuilder prase(Context mContext, final TextView gifTextView, String content) {
		SpannableStringBuilder sb = new SpannableStringBuilder(content);
		String regex = "\\[[^\\]]+\\]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String tempText = m.group();
			try {
				String png = tempText.substring("[".length(), tempText.length() - "]".length());
				Bitmap bitmap = BitmapFactory.decodeStream(mContext.getAssets().open(png));// 调节主页列表数据的表情大小
				// DisplayMetrics dm = new DisplayMetrics();
				// dm = mContext.getResources().getDisplayMetrics();
				// float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
				// int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
				// float xdpi = dm.xdpi;
				// float ydpi = dm.ydpi;
				// Logger.i("Utils.sp2px(mContext, 22)+",Utils.sp2px(mContext,
				// 26)+"");
				bitmap = Bitmap.createScaledBitmap(bitmap, Utils.sp2px(mContext, 24), Utils.sp2px(mContext, 24), true);
				sb.setSpan(new ImageSpan(mContext, bitmap), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb;
	}

	public static SpannableStringBuilder praseToEditText(Context mContext, final EditText gifTextView, String content) {
		SpannableStringBuilder sb = new SpannableStringBuilder(content);
		String regex = "\\[[^\\]]+\\]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String tempText = m.group();
			try {
				String png = tempText.substring("[".length(), tempText.length() - "]".length());
				Bitmap bitmap = BitmapFactory.decodeStream(mContext.getAssets().open(png));// 调节发表话题页的表情大小
				// DisplayMetrics dm = new DisplayMetrics();
				// dm = mContext.getResources().getDisplayMetrics();
				// float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
				// int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
				// float xdpi = dm.xdpi;
				// float ydpi = dm.ydpi;
				// Logger.i("densityDPI+", densityDPI + "");
				bitmap = Bitmap.createScaledBitmap(bitmap, Utils.sp2px(mContext, 20), Utils.sp2px(mContext, 20), true);
				sb.setSpan(new ImageSpan(mContext, bitmap), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb;
	}

	/*
	 * params str 获取带有表情字符中表情所占字符串的总长度
	 */
	public static int getFaceLong(String str) {
		int length = 0;
		SpannableStringBuilder sb = new SpannableStringBuilder(str);
		String regex = "\\[[^\\]]+\\]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);

		while (m.find()) {
			String tempText = m.group();
			length += tempText.length();

		}

		return length;

	}

	public static SpannableStringBuilder getFace(Context mContext, String png) {
		SpannableStringBuilder sb = new SpannableStringBuilder();
		try {
			/**
			 * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
			 * 所以这里对这个tempText值做特殊处理
			 * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
			 */
			String tempText = "[" + png + "]";
			sb.append(tempText);
			Bitmap bitmap = BitmapFactory.decodeStream(mContext.getAssets().open(png));// 调节输入框中的的表情大小
			// DisplayMetrics dm = new DisplayMetrics();
			// dm = mContext.getResources().getDisplayMetrics();
			// float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
			// int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
			// float xdpi = dm.xdpi;
			// float ydpi = dm.ydpi;
			// Logger.i("densityDPI+", densityDPI + "");
			bitmap = Bitmap.createScaledBitmap(bitmap, Utils.sp2px(mContext, 20), Utils.sp2px(mContext, 20), true);
			sb.setSpan(new ImageSpan(mContext, bitmap), sb.length() - tempText.length(), sb.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}

	/**
	 * 向输入框里添加表情
	 */
	public static void insert(EditText input, CharSequence text) {
		int iCursorStart = Selection.getSelectionStart((input.getText()));
		int iCursorEnd = Selection.getSelectionEnd((input.getText()));
		if (iCursorStart != iCursorEnd) {
			input.getText().replace(iCursorStart, iCursorEnd, "");
		}
		int iCursor = Selection.getSelectionEnd((input.getText()));
		input.getText().insert(iCursor, text);
	}

	/**
	 * 删除图标执行事件
	 * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
	 */
	public static void delete(EditText input) {
		if (input.getText().length() != 0) {
			int iCursorEnd = Selection.getSelectionEnd(input.getText());
			int iCursorStart = Selection.getSelectionStart(input.getText());
			if (iCursorEnd > 0) {
				if (iCursorEnd == iCursorStart) {
					if (isDeletePng(input, iCursorEnd)) {
						String st = "[p/_000.png]";
						input.getText().delete(iCursorEnd - st.length(), iCursorEnd);
					} else {
						input.getText().delete(iCursorEnd - 1, iCursorEnd);
					}
				} else {
					input.getText().delete(iCursorStart, iCursorEnd);
				}
			}
		}
	}

	/**
	 * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
	 **/
	public static boolean isDeletePng(EditText input, int cursor) {
		String st = "[p/_000.png]";
		String content = input.getText().toString().substring(0, cursor);
		if (content.length() >= st.length()) {
			String checkStr = content.substring(content.length() - st.length(), content.length());
			String regex = "\\[[^\\]]+\\]";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(checkStr);
			return m.matches();
		}
		return false;
	}

	public static View viewPagerItem(final Context context, int position, List<String> staticFacesList, int columns,
			int rows, final EditText editText) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.face_gridview, null);// 表情布局
		GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
		/**
		 * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns * rows － 1; 空出最后一个位置给删除图标
		 */
		List<String> subList = new ArrayList<String>();
		subList.addAll(staticFacesList.subList(position * (columns * rows - 1),
				(columns * rows - 1) * (position + 1) > staticFacesList.size() ? staticFacesList.size()
						: (columns * rows - 1) * (position + 1)));
		/**
		 * 末尾添加删除图标
		 */
		subList.add("_del.png");
		FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, context);
		gridview.setAdapter(mGvAdapter);
		gridview.setNumColumns(columns);
		// 单击表情执行的操作
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					String png = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
					if (!png.contains("_del")) {// 如果不是删除图标
						ExpressionUtil.insert(editText, ExpressionUtil.getFace(context, png));
					} else {
						ExpressionUtil.delete(editText);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return gridview;
	}

	/**
	 * 根据表情数量以及GridView设置的行数和列数计算Pager数量
	 * 
	 * @return
	 */
	public static int getPagerCount(int listsize, int columns, int rows) {
		return listsize % (columns * rows - 1) == 0 ? listsize / (columns * rows - 1)
				: listsize / (columns * rows - 1) + 1;
	}

	/**
	 * 初始化表情列表staticFacesList
	 */
	public static List<String> initStaticFaces(Context context) {
		List<String> facesList = null;
		try {
			facesList = new ArrayList<String>();
			String[] faces = context.getAssets().list("p");
			// 将Assets中的表情名称转为字符串一一添加进staticFacesList
			for (int i = 0; i < faces.length; i++) {
				facesList.add(faces[i]);
			}

			// 去掉删除图片
			facesList.remove("_del.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return facesList;
	}
}
