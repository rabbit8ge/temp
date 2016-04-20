package com.example.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.text.TextUtils;

public class LogDTO implements Serializable {

	private String versionno;
	private String versionname;
	private String create_time;
	private String content;

	public String getVersionno() {
		return versionno;
	}

	public void setVersionno(String versionno) {
		this.versionno = versionno;
	}

	public String getVersionname() {
		return versionname;
	}

	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "LogDTO [versionno=" + versionno + ", versionname=" + versionname + ", create_time=" + create_time
				+ ", content=" + content + "]";
	}

	/*
	 * 从json中获取日志消息
	 */
	public static LogDTO getLogDTO(String json) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}

		Gson gson = new Gson();
		LogDTO mLogDTO = gson.fromJson(json, LogDTO.class);

		return mLogDTO;

	}

	public static <T> List<T> getLogDTOList(String jsonString) {
		List<T> list = new ArrayList<T>();

		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString, new TypeToken<List<LogDTO>>() {
			}.getType());
		} catch (Exception e) {
		}
		return list;
	}

}
