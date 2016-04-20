package com.example.dto;



import com.google.gson.Gson;

import android.text.TextUtils;

/**
 * 消息列表
 * 
 * @author Administrator
 * 
 */
public class RemindDTO {

	public String coid; // coid
	public String tid; // tid
	public String cid; // 新发表的评论或回复
	public String to_cid; // 表示关注、回复的评论对象

	@Override
	public String toString() {
		return "RemindDTO [coid=" + coid + ", title=" + post_title + ", avatar=" + avatar + ", content=" + content
				+ ", post_date=" + post_date + ", pid=" + pid + ", is_read=" + is_read + "]";
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getTo_cid() {
		return to_cid;
	}

	public void setTo_cid(String to_cid) {
		this.to_cid = to_cid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getPost_title() {
		return post_title;
	}

	public void setPost_title(String post_title) {
		this.post_title = post_title;
	}

	public String post_title;// 标题;

	public String avatar; // 图片

	public String content; // 内容

	public String post_date; // time

	public String pid; // 话题ID
	public String is_read; // 是否已读
	public String type;// 消息类型

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIs_read() {
		return is_read;
	}

	public void setIs_read(String is_read) {
		this.is_read = is_read;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPost_date() {
		return post_date;
	}

	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}

	public String getCoid() {
		return coid;
	}

	public void setCoid(String coid) {
		this.coid = coid;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/*
	 * 从json中获取提醒信息
	 */
	public static RemindDTO fromJson(String str) {
		if (TextUtils.isEmpty(str)) {
			return null;
		}

		Gson gson = new Gson();
		RemindDTO mRemindDTO = gson.fromJson(str, RemindDTO.class);

		return mRemindDTO;
	}

}
