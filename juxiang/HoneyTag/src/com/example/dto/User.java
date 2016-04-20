package com.example.dto;

import java.io.Serializable;

import com.google.gson.Gson;

import android.text.TextUtils;

public class User implements Serializable {

	private String uid;// 用户Id
	private String tel;// 手机
	private String nicename;// 用户昵称
	private String password;// 用户密码
	private String sex;// 性别
	private String birthday;// 出生年月日
	private String avatar;// 用户图像
	private String last_login_time;// 最后一次登录时间
	private String ability;// 能力值
	private String credit;// 信用值
	private String card_json;
	private String user_status;// 1在线

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getNicename() {
		return nicename;
	}

	public void setNicename(String nicename) {
		this.nicename = nicename;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
	}

	public String getAbility() {
		return ability;
	}

	public void setAbility(String ability) {
		this.ability = ability;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getCard_json() {
		return card_json;
	}

	public void setCard_json(String card_json) {
		this.card_json = card_json;
	}

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}

	@Override
	public String toString() {
		return "User [uid=" + uid + ", tel=" + tel + ", nicename=" + nicename + ", password=" + password + ", sex="
				+ sex + ", birthday=" + birthday + ", avatar=" + avatar + ", last_login_time=" + last_login_time
				+ ", ability=" + ability + ", credit=" + credit + ", card_json=" + card_json + ", user_status="
				+ user_status + "]";
	}

	/*
	 * 从json中获取到用户信息
	 */
	public static User getUser(String json) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}

		Gson gson = new Gson();
		User mUser = gson.fromJson(json, User.class);

		return mUser;

	}

}
