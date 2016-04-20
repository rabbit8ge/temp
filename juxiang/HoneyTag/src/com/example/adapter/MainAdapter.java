package com.example.adapter;

import java.util.List;

import com.example.dto.ArticleDTO;
import com.example.honeytag1.R;
import com.example.utils.ExpressionUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.tsz.afinal.FinalBitmap;

public class MainAdapter extends BaseAdapter {

	FinalBitmap finalBitmap;
	List<ArticleDTO> artilist;
	Context context;

	public MainAdapter(Context context, List<ArticleDTO> artilist) {
		super();
		finalBitmap = FinalBitmap.create(context);
		this.artilist = artilist;
		this.context = context;
	}

	public void setList(List<ArticleDTO> artilist) {
		this.artilist = artilist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return artilist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return artilist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint({ "ResourceAsColor", "NewApi" })
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		HolderView holderView;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.fragment_item, null);

			holderView = new HolderView();
			holderView.lianjie_logo = (ImageView) convertView.findViewById(R.id.lianjie_logo);
			holderView.lianjie_wenzi = (TextView) convertView.findViewById(R.id.lianjie_wenzi);
			holderView.zan_b = (ImageView) convertView.findViewById(R.id.zan_b);
			holderView.pinglun_b = (ImageView) convertView.findViewById(R.id.pinglun_b);
			holderView.fragment_item_lin = (LinearLayout) convertView.findViewById(R.id.fragment_item_lin);
			holderView.fragment_txtname = (TextView) convertView // 标题
					.findViewById(R.id.fragment_txtname);
			holderView.fragment_item_pinglun = (TextView) convertView // 评论
					.findViewById(R.id.fragment_item_pinglun);
			holderView.fragment_item_zan = (TextView) convertView // 赞
					.findViewById(R.id.fragment_item_zan);
			holderView.main_item_neirong = (TextView) convertView // 内容
					.findViewById(R.id.main_item_neirong);
			holderView.fragment_item_image = (ImageView) convertView.findViewById(R.id.fragment_item_image); // 图片


			convertView.setTag(holderView);

		} else {
			holderView = (HolderView) convertView.getTag();
		}

		if (artilist.get(position).getColor().equals("9")) {
			convertView.setBackgroundResource(R.color.white);
			holderView.pinglun_b.setBackgroundResource(R.drawable.chat1);
			holderView.zan_b.setBackgroundResource(R.drawable.collect1);
			holderView.fragment_item_pinglun.setTextColor(context.getResources().getColor(R.color.white_bg_text));
			holderView.fragment_item_zan.setTextColor(context.getResources().getColor(R.color.white_bg_text));
			holderView.fragment_txtname.setAlpha(0.7f);
			holderView.main_item_neirong.setTextColor(context.getResources().getColor(R.color.black));
		} else {
			holderView.pinglun_b.setBackgroundResource(R.drawable.chat);
			holderView.zan_b.setBackgroundResource(R.drawable.collect);
			holderView.fragment_item_pinglun.setTextColor(context.getResources().getColor(R.color.white));
			holderView.fragment_item_zan.setTextColor(context.getResources().getColor(R.color.white));
			holderView.fragment_txtname.setAlpha(0.5f);
			holderView.main_item_neirong.setTextColor(context.getResources().getColor(R.color.white));
			if (artilist.get(position).getColor().equals("1")) {
				convertView.setBackgroundResource(R.color.youmi_main_a);
			} else if (artilist.get(position).getColor().equals("2")) {
				convertView.setBackgroundResource(R.color.youmi_main_b);
			} else if (artilist.get(position).getColor().equals("3")) {
				convertView.setBackgroundResource(R.color.youmi_main_c);
			} else if (artilist.get(position).getColor().equals("4")) {
				convertView.setBackgroundResource(R.color.youmi_main_d);
			} else if (artilist.get(position).getColor().equals("5")) {
				convertView.setBackgroundResource(R.color.youmi_main_e);
			} else if (artilist.get(position).getColor().equals("6")) {
				convertView.setBackgroundResource(R.color.youmi_main_f);
			} else if (artilist.get(position).getColor().equals("7")) {
				convertView.setBackgroundResource(R.color.youmi_main_g);
			} else if (artilist.get(position).getColor().equals("8")) {
				convertView.setBackgroundResource(R.color.youmi_main_h);
			}
		}

		if(artilist.get(position).getCard_industry().equals("''")||artilist.get(position).getCard_industry()==null){
			holderView.fragment_txtname
			.setText("自由职业");
		}else{
			holderView.fragment_txtname
			.setText(artilist.get(position).getCard_industry() + " | " + artilist.get(position).getCard_title());
		}

		holderView.main_item_neirong.setText(artilist.get(position).getContent());

		holderView.main_item_neirong.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		SpannableStringBuilder sb = ExpressionUtil.prase(context, holderView.main_item_neirong,
				artilist.get(position).getContent());// 对内容做处理
		holderView.main_item_neirong.setText(sb);

		// String userName = "<font color=\'#858585\'><b><U>" +
		// artilist.get(position).getContent()+ "</U></b></font>";
		// Spanned s=Html.fromHtml(userName);
		//
		// SpannableStringBuilder sb = ExpressionUtil.prase(getActivity(),
		// holderView.main_item_neirong,
		// s.toString());// 对内容做处理

		// Linkify.addLinks(holderView.main_item_neirong, Linkify.ALL);

		if (artilist.get(position).getPost_like().equals("")) {
			holderView.fragment_item_zan.setText("0");
		} else {
			holderView.fragment_item_zan.setText(artilist.get(position).getPost_like());
		}
		holderView.fragment_item_pinglun.setText(artilist.get(position).getComment_count());
		if (artilist.get(position).getAvatar().equals("no_pic")) {
			holderView.fragment_item_image.setVisibility(View.GONE);
		}else{
			holderView.fragment_item_image.setVisibility(View.VISIBLE);
			finalBitmap.display(holderView.fragment_item_image, artilist.get(position).getAvatar());
		} 

		// 跳转webview
		/*
		 * holderView.main_item_neirong.setOnClickListener(new OnClickListener()
		 * {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub ArticleDTO articleDTO = artilist.get(position); Intent
		 * intent = new Intent(); PublicStaticURL.pid = articleDTO.getPid(); //
		 * 将文章id存上 // 用调去详情 PublicStaticURL.URL = articleDTO.getUrl();
		 * //context.startActivity(new Intent(context,
		 * TopicDetailsActivity.class));
		 * 
		 * } });
		 */

		return convertView;
	}

	// 定义view缓存对象
	static class HolderView {

		TextView fragment_itme_time, fragment_txtname, main_item_neirong, item_btn, fragment_item_pinglun,
				fragment_item_zan, item_title, lianjie_wenzi;
		ImageView fragment_item_image, zan_b, pinglun_b, lianjie_logo;
		LinearLayout fragment_item_lin;
	}

}
