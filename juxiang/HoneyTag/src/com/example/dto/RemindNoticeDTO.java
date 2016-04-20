package com.example.dto;

import com.google.gson.Gson;

import android.text.TextUtils;

public class RemindNoticeDTO {
	@Override
	public String toString() {
		return "RemindNoticeDTO [mid=" + mid + ", title=" + title + ", avatar=" + avatar + ", content=" + content
				+ ", post_date=" + post_date + ", pid=" + pid + ", cid=" + cid + ", is_read=" + is_read + ", to_cid="
				+ to_cid + "]";
	}

	private String mid;

	private String title;
	private String avatar;
	private String content;
	private String post_date;
	private String pid;
	private String cid;
	private String is_read;
	public String to_cid; // 表示关注、回复的评论对象

	public String getTo_cid() {
		return to_cid;
	}

	public void setTo_cid(String to_cid) {
		this.to_cid = to_cid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getPost_date() {
		return post_date;
	}

	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getIs_read() {
		return is_read;
	}

	public void setIs_read(String is_read) {
		this.is_read = is_read;
	}

	/*
	 * 从json中获取提醒信息
	 */
	public static RemindNoticeDTO fromJson(String str) {
		if (TextUtils.isEmpty(str)) {
			return null;
		}

		Gson gson = new Gson();
		RemindNoticeDTO mRemindNoticeDTO = gson.fromJson(str, RemindNoticeDTO.class);

		return mRemindNoticeDTO;
	}

}
