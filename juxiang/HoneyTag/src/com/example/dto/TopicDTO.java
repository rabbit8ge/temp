package com.example.dto;

import java.io.Serializable;

import com.google.gson.Gson;

import android.text.TextUtils;

/**
 * @Description描述:话题的系统通知类
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class TopicDTO implements Serializable {

	private String title;
	private String content;
	private String column_title;
	private String tid;

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

	public String getColumn_title() {
		return column_title;
	}

	public void setColumn_title(String column_title) {
		this.column_title = column_title;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	@Override
	public String toString() {
		return "TopicDTO [title=" + title + ", content=" + content + ", column_title=" + column_title + ", tid=" + tid
				+ "]";
	}

	/*
	 * 从json中获取系统通知的信息
	 */
	public static TopicDTO getTopicDTO(String json) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}

		Gson gson = new Gson();
		TopicDTO mTopicDTO = gson.fromJson(json, TopicDTO.class);

		return mTopicDTO;

	}

}
