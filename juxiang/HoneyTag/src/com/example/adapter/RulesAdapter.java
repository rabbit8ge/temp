package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.dto.RulesDTO;
import com.example.honeytag1.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RulesAdapter extends BaseAdapter {
	private Context mContext;
	private List<RulesDTO> mList = new ArrayList<RulesDTO>();
	private String[] bonusPoint = new String[] {};
	private String[] points = new String[] {};
	private String[] arr = new String[] {};

	public RulesAdapter(Context context, List<RulesDTO> mList) {
		this.mContext = context;
		this.mList = mList;
		bonusPoint = mList.get(0).getContent().split("\r\n");
		points = mList.get(1).getContent().split("\r\n");
		int aa = bonusPoint.length;
		int bb = points.length;
		int length = bonusPoint.length + points.length;
		arr = new String[length];
		for (int i = 0; i < length; i++) {
			if (i < bonusPoint.length) { // 如果i<len,则赋值为one中的元素;
				arr[i] = bonusPoint[i];
				continue;
			}
			int t = i - bonusPoint.length; // t 从0开始
			arr[i] = points[t]; //
		}

	}

	@Override
	public int getCount() {
		return arr.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arr[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_rules, null);
			mHolder = new Holder();
			mHolder.mTvContent = (TextView) convertView.findViewById(R.id.tv_content);
			mHolder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			mHolder.mView = convertView.findViewById(R.id.view);
			convertView.setTag(mHolder);

		} else {
			mHolder = (Holder) convertView.getTag();
		}

		if (0 == position) {
			mHolder.mTvTitle.setText(mList.get(0).getTitle()+"：");
			mHolder.mView.setVisibility(View.GONE);
			mHolder.mTvTitle.setVisibility(View.VISIBLE);
		} else if (bonusPoint.length == position) {
			mHolder.mTvTitle.setText(mList.get(1).getTitle()+"：");
			mHolder.mTvTitle.setVisibility(View.VISIBLE);
			mHolder.mView.setVisibility(View.VISIBLE);

		} else {
			mHolder.mTvTitle.setText("");
			mHolder.mView.setVisibility(View.GONE);
			mHolder.mTvTitle.setVisibility(View.GONE);
		}
		mHolder.mTvContent.setText(arr[position].replace("\\n", "\n    "));

		return convertView;
	}

	static class Holder {
		View mView;
		TextView mTvTitle, mTvContent;

	}

}
