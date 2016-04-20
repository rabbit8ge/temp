package com.example.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

//打开或关闭软键盘
public class KeyBoardUtils {

	// 点击屏幕使输入框失去焦点 关闭输入法
	public static void closeInputMethod(Context ct, View v) {
		InputMethodManager imm = (InputMethodManager) ct.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		if (isOpen) {
			// imm.toggleSoftInput(0,
			// InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
			imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * @param root
	 *            最外层布局，需要调整的布局
	 * @param scrollToView
	 *            被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
	 */
	public static void controlKeyboardLayout(final View root, final View scrollToView) {

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
	 * 
	 * 监听键盘状态
	 * 
	 * @param root
	 */
	public static void UpdateKeyBrodLisenert(final View root, final View scrollToView) {

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
	 * 打卡软键盘
	 * 
	 * @param mEditText输入框
	 * @param mContext上下文
	 */
	public static void openKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * 关闭软键盘
	 * 
	 * @param mEditText输入框
	 * @param mContext上下文
	 */
	public static void closeKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}
}
