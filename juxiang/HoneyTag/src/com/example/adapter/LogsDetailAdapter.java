package com.example.adapter;

import com.example.honeytag1.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LogsDetailAdapter extends BaseAdapter {
	private Context mContext;
	private String[] mList = new String[] {};

	public LogsDetailAdapter(Context context, String[] mList) {
		this.mContext = context;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList[position];
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_logs_detail, null);
			mHolder = new Holder();

			mHolder.mTvContent = (TextView) convertView.findViewById(R.id.tv_content);

			convertView.setTag(mHolder);

		} else {
			mHolder = (Holder) convertView.getTag();
		}

		mHolder.mTvContent.setText(mList[position]);

		return convertView;
	}

	static class Holder {
		TextView mTvContent;

	}

}
