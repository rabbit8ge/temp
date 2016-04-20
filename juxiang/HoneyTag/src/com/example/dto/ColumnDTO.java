package com.example.dto;

/**
 * ��Ŀ
 * 
 * @author Administrator
 * 
 */
public class ColumnDTO {

	public String id;// 栏目的id
	public String title;// 栏目标题
	public String smeta;
	public String haslike;// 栏目是否被关注
	public String listorder;
	public String title_num;// 栏目中所包含话题的数量
	public String hasnew;// 栏目中收录了新的话题
	public String avatar;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSmeta() {
		return smeta;
	}

	public void setSmeta(String smeta) {
		this.smeta = smeta;
	}

	public String getHaslike() {
		return haslike;
	}

	public void setHaslike(String haslike) {
		this.haslike = haslike;
	}

	public String getListorder() {
		return listorder;
	}

	public void setListorder(String listorder) {
		this.listorder = listorder;
	}

	public String getTitle_num() {
		return title_num;
	}

	public void setTitle_num(String title_num) {
		this.title_num = title_num;
	}

	public String getHasnew() {
		return hasnew;
	}

	public void setHasnew(String hasnew) {
		this.hasnew = hasnew;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "ColumnDTO [id=" + id + ", title=" + title + ", smeta=" + smeta + ", haslike=" + haslike + ", listorder="
				+ listorder + ", title_num=" + title_num + ", hasnew=" + hasnew + ", avatar=" + avatar + "]";
	}
}
