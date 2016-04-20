package com.example.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.entity.CardInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.text.TextUtils;

public class GradesDTO {

	private String level;
	private String name;
	private String content;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	public static GradesDTO getGradesDTO(String json) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		Gson gson = new Gson();
		GradesDTO mGradesDTO = gson.fromJson(json, GradesDTO.class);

		return mGradesDTO;

	}

	public static <T> List<T> getGradesDTOList(String jsonString) {
		List<T> list = new ArrayList<T>();

		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString, new TypeToken<List<GradesDTO>>() {
			}.getType());
		} catch (Exception e) {
		}
		return list;
	}

}
