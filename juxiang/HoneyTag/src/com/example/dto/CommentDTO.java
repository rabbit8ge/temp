package com.example.dto;

import java.util.List;

/**
 * 评论列表
 * 
 * @author Administrator
 * 
 */
public class CommentDTO {

	public String cid; // 评论id
	public String to_cid; // 回复评论id
	public List<Reply> replys;
	
	public List<Reply> getReplys() {
		return replys;
	}

	public void setReplys(List<Reply> replys) {
		this.replys = replys;
	}

	public String getTo_cid() {
		return to_cid;
	}

	public void setTo_cid(String to_cid) {
		this.to_cid = to_cid;
	}

	public String from_uid; // 用户id

	public String at_uid; // @id

	public String content; // 内容

	public String nicename; // name

	public String avatar; // 图片

	public String post_like;// 赞数

	public String date; // 时间

	public String at_nicename; // @他人

	public String ability; // 能力值

	public String credit; // 信用值

	public String card_title; // 行业

	public String card_industry; // 职业

	public String haslike;// 是否点赞

	public String getHaslike() {
		return haslike;
	}

	public void setHaslike(String haslike) {
		this.haslike = haslike;
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

	public String getAt_nicename() {
		return at_nicename;
	}

	public void setAt_nicename(String at_nicename) {
		this.at_nicename = at_nicename;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getFrom_uid() {
		return from_uid;
	}

	public void setFrom_uid(String from_uid) {
		this.from_uid = from_uid;
	}

	public String getAt_uid() {
		return at_uid;
	}

	public void setAt_uid(String at_uid) {
		this.at_uid = at_uid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNicename() {
		return nicename;
	}

	public void setNicename(String nicename) {
		this.nicename = nicename;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPost_like() {
		return post_like;
	}

	public void setPost_like(String post_like) {
		this.post_like = post_like;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
