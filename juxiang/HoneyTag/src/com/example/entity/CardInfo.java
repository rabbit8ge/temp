package com.example.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CardInfo {

	// String name;// 姓名
	// String title;// 职位
	// String mobile;// 手机
	// String email;// 邮箱
	// String fax;// 传真
	// String tel;// 电话
	// String comp;// 公司
	// String addr;// 地址
	// String industry;// 行业

	private String id;// 名片id
	private String card_name;// 姓名
	private String card_title;// 职位
	private String card_industry;// 行业
	private String mobile;// 手机
	private String phone;// 电话
	private String email;// 邮箱
	private String fax;// 传真
	private String comp;// 公司
	private String dept;// 部门
	private String other;// 其它
	private String address;// 地址
	private String add_time;// 时间 0000-00-00 00:00:00

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public String getCard_title() {
		return card_title;
	}

	public void setCard_title(String card_title) {
		this.card_title = card_title;
	}

	public String getCard_industry() {
		return card_industry;
	}

	public void setCard_industry(String card_industry) {
		this.card_industry = card_industry;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getComp() {
		return comp;
	}

	public void setComp(String comp) {
		this.comp = comp;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public static <T> T getCardInfo(String jsonString, Class<T> cls) {
		T t = null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	public static <T> List<T> getCardInfoList(String jsonString) {
		List<T> list = new ArrayList<T>();

		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString, new TypeToken<List<CardInfo>>() {
			}.getType());
		} catch (Exception e) {
		}
		return list;
	}

}
