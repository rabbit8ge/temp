package com.example.dto;

import com.google.gson.Gson;

import android.text.TextUtils;

/**
 * @Description描述:强踢下线的通知信息
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class StrongPlayDTO {

	private String title;
	private String content;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/*
	 * 从json中获取到用户信息
	 */
	public static StrongPlayDTO getStrongPlayDTO(String json) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}

		Gson gson = new Gson();
		StrongPlayDTO mStrongPlayDTO = gson.fromJson(json, StrongPlayDTO.class);

		return mStrongPlayDTO;

	}

}
