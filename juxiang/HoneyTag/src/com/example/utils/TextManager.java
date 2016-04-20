package com.example.utils;

import com.example.dto.ArticleDTO;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * 文本操作工具籿
 * 
 * @author zihao
 * 
 */
@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class TextManager {

	/**
	 * 复制文本
	 * 
	 * @param context
	 * @param articleDTO
	 *            // 被复制的文本
	 */
	public static void copyText(Context context, ArticleDTO articleDTO) {
		// 获取剪贴板管理服势
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		// 将文本数据复制到剪贴板
		cmb.setText(articleDTO.getContent());
	}

	/**
	 * 粘贴文本
	 * 
	 * @param context
	 * @return
	 */
	public static String pasteText(Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		return cmb.getText().toString().trim();
	}

}