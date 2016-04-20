package com.example.utils;

import com.example.dto.TopicDTO;

public class PublicStaticURL {

	// 120.55.164.20
	// http://i2dd.com/gumi/index.php

	// public static final String YOUMIURL =
	// "http://120.55.164.20/youmi/index.php?";
	// public static final String YOUMIURL =
	// "http://demo.baiwushi.cn/youmi/index.php?";


	 public static final String YOUMIURL =
	 "http://i2dd.com/gumi/index.php?";// 在线服务器
	//public static final String YOUMIURL = "http://i2dd.com/gumidev/index.php?";// 测试服务器


	public static final String YANZHENGMA = YOUMIURL + "m=api&a=index&cmd=1"; // 获取验证码(注册)
	public static final String YANZHENGMAXIUGAIMIMA = YOUMIURL + "m=api&a=index&cmd=400"; // 获取验证码(修改密码)

	public static final String REGIST = YOUMIURL + "m=api&a=index&cmd=2"; // 注册

	public static final String LOGIN = YOUMIURL + "m=api&a=index&cmd=3"; // 登录

	public static final String XIUGAIPASSWORD = YOUMIURL + "m=api&a=index&cmd=4"; // 登录

	public static final String VERSION = YOUMIURL + "m=api&a=index&cmd=100"; // 获取版本号

	public static final String FABU = YOUMIURL + "m=api&a=index&cmd=20"; // 发布话题

	public static final String ARTICLE = YOUMIURL + "m=api&a=index&cmd=21"; // 首页文章列表

	public static final String HANWANG = YOUMIURL + "m=api&a=index&cmd=5"; // 名片信息

	public static final String HUOQUHANWANG = YOUMIURL + "m=api&a=index&cmd=6"; // 获取名片信息

	public static final String DETAILS = YOUMIURL + "m=api&a=index&cmd=22"; // 首页文章详情

	public static final String ZAN = YOUMIURL + "m=api&a=index&cmd=25"; // 点赞

	public static final String PINGLUN = YOUMIURL + "m=api&a=index&cmd=23"; // 发表评论

	public static final String PINGLUNLIST = YOUMIURL + "m=api&a=index&cmd=24"; // 评论列表

	public static final String ZANPINGLUN = YOUMIURL + "m=api&a=index&cmd=27"; // 赞评论
	public static final String QXPINGLUN = YOUMIURL + "m=api&a=index&cmd=270"; // 取消评论

	public static final String TIXING = YOUMIURL + "m=api&a=index&cmd=28"; // 提醒列表
	public static final String TIXINGZHUANGTAIQB = YOUMIURL + "m=api&a=index&cmd=280"; // 全部已读
	public static final String TIXINGZHUANGTAI = YOUMIURL + "m=api&a=index&cmd=29"; // 提醒状态已读

	public static final String ZONGmessage = YOUMIURL + "m=api&a=index&cmd=30"; // 提醒数量

	public static final String ZuiRELIST = YOUMIURL + "m=api&a=index&cmd=62"; // 最热列表&发现列表

	public static final String FEED = YOUMIURL + "m=api&a=index&cmd=66"; // 建议反馈

	public static final String MYDE = YOUMIURL + "m=api&a=index&cmd=63"; // 我发布的话题

	public static final String PART = YOUMIURL + "m=api&a=index&cmd=64"; // 我参与的话题

	public static final String DELETEAC = YOUMIURL + "m=api&a=index&cmd=67"; // 屏蔽文章
	public static final String DELETEPL = YOUMIURL + "m=api&a=index&cmd=670"; // 屏蔽评论
	public static final String REPORTPL = YOUMIURL + "m=api&a=index&cmd=660"; // 举报评论

	public static final String captcha = YOUMIURL + "m=api&a=index&cmd=8"; // 验证码是否正确
	public static final String SHAREAC = YOUMIURL + "m=api&a=index&cmd=80"; // 分享文章

	public static final String GUANZHU = YOUMIURL + "m=api&a=index&cmd=65"; // 我关注的文章

	public static final String JINGCAIPINGLUN = YOUMIURL + "m=api&a=index&cmd=68"; // 精彩评论
	public static final String WDPINGLUN = YOUMIURL + "m=api&a=index&cmd=240"; // 我的评论
	public static final String GZPINGLUN = YOUMIURL + "m=api&a=index&cmd=241"; // 关注评论

	public static final String Verification = YOUMIURL + "m=api&a=index&cmd=7"; // 验证名片手机号

	public static final String CreadRecord = YOUMIURL + "m=api&a=index&cmd=60"; // 能力记录

	public static final String AbilitRecord = YOUMIURL + "m=api&a=index&cmd=61"; // 信用记录

	public static final String Column = YOUMIURL + "m=api&a=index&cmd=70"; // 栏目

	public static final String ColumnDetails = YOUMIURL + "m=api&a=index&cmd=71"; // 栏目话题列表

	public static final String Changbusiness = YOUMIURL + "m=api&a=index&cmd=9"; // 修改名片
	public static final String MASKNOTICE = YOUMIURL + "m=api&a=index&cmd=90"; // 屏蔽消息通知
	public static final String QXMASKNOTICE = YOUMIURL + "m=api&a=index&cmd=901"; // 取消屏蔽消息通知

	public static final String Addbusinesscard = YOUMIURL + "m=api&a=index&cmd=500"; // 身份标签中添加名片
	public static final String BUSINESSCARDERROR = YOUMIURL + "m=api&a=index&cmd=660"; // 身份标签中的名片信息有错误时用于向后台报错用的
	public static final String BUSINESSCARDDELETE = YOUMIURL + "m=api&a=index&cmd=501"; // 删除名片
	public static final String PUSH = YOUMIURL + "m=api&a=index&cmd=301"; // 把device_token传给后台
	public static final String PAYATTENTIONCOLUMN = YOUMIURL + "m=api&a=index&cmd=700"; // 关注栏目
	public static final String CANCELATTENTION = YOUMIURL + "m=api&a=index&cmd=701"; // 取消栏目关注
	public static final String READSECTION = YOUMIURL + "m=api&a=index&cmd=710"; // 阅读栏目
	public static final String COLUMNRED = YOUMIURL + "m=api&a=index&cmd=281"; // 栏目红点
	public static final String LOG = YOUMIURL + "m=api&a=index&cmd=101"; // 更新日志

	public static final String COLUMNBLOCK = YOUMIURL + "m=api&a=index&cmd=90"; // 栏目屏蔽
	public static final String COLUMNOPEN = YOUMIURL + "m=api&a=index&cmd=901"; // 栏目打开
	public static final String TAGPINGLUN = YOUMIURL + "m=api&a=index&cmd=242"; // 加载之前的评论或之后的评论

	public static final String MYGRADES = YOUMIURL + "m=api&a=index&cmd=102"; // 我的等级
	public static final String RULES = YOUMIURL + "m=api&a=index&cmd=103"; // 等级规则

	public static boolean IsLogin = false; // 登录标识

	public static int FRISTOPEN = 0; // 第一次登陆

	// public static boolean Image = false; // 发布图片是否为空

	public static String userid = null; // 用户ID

	public static String ablity = "0"; // 用户能力值

	public static String credit = null; // 用户信用值

	public static String pid = null; // 文章id

	public static String URL = null; // 話題URL

	public static String commzan = null; // 评论赞
	public static String plid = null; // 评论列表id

	public static String qname = null; // @name

	public static String qid = null; // @对方的id

	public static String title = null; // 职业

	public static String industry = null; // 行业

	public static String card_name = null; // 汉王返回名片姓名

	public static String card_title = null; // 汉王返回名片职位

	public static String card_tel = null; // 汉王返回名片座机号

	public static String card_fax = null; // 汉王返回名片传真号

	public static String card_eamil = null; // 汉王返回名片邮箱

	public static String card_comp = null; // 汉王返回名片公司名字

	public static String card_addr = null; // 汉王返回名片公司地址

	public static String card_phone = null; // 汉王返回名片手机号

	public static String tel = null; // 注册手机号

	public static String password = null; // 注册密码

	public static String spread_tel = null; // 注册推广手机号

	public static String Regist_tel = null; // 注册临时手机号

	public static String Regist_id = null; // 注册临时id

	public static String ColumnTitle = null; // 栏目话题Title

	public static String Youmi_phone = null; // 用户登录phone

	public static String Youmi_password = null; // 用户登录password

	public static String Login_phone = null; // 用户名

	public static int isNewVersion = 1;// 是否有新的版本 (0代表有，1代表没有)
	public static int hasFirstCard = 1;// 是否上传了第一张名片(0代表有，1代表没有)

	public static TopicDTO mTopicDTO = null;// 栏目通知
	/* ===============================通知相关的============================== */
	public static int activityFlag = -1; // 0,从通知界面进入到主界面;
	public static int columnFlag = -1; // 从关注栏目的通知点击进入栏目列表
										// (0,从“我的话题被收录到栏目”通知界面进入到栏目的话题列表界面;1,从“我关注的栏目收录了新的话题
										// ”通知界面进入到栏目的话题列表界面)

	public static String remind_cid = null;// 提醒页新发表的评论或回复id

	public static String remindto_cid = null;// 提醒页表示关注、回复的评论对象id


	public static String downloadURl="http://www.i2dd.com/upload/";

	public static int versiontype=1;//（1是普通更新，2是重大更新）默认为普通更新，
	public static  boolean isDownloading=false;
}
