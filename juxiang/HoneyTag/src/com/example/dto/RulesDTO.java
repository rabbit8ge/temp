package com.example.dto;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.text.TextUtils;

public class RulesDTO {

	private String title;

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

	private String content;

	/*
	 * 从json中获取到用户信息
	 */
	public static RulesDTO getRulesDTO(String json) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		Gson gson = new Gson();
		RulesDTO mRulesDTO = gson.fromJson(json, RulesDTO.class);

		return mRulesDTO;

	}

	public static <T> List<T> getRulesDTOList(String jsonString) {
		List<T> list = new ArrayList<T>();

		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString, new TypeToken<List<RulesDTO>>() {
			}.getType());
		} catch (Exception e) {
		}
		return list;
	}

}
