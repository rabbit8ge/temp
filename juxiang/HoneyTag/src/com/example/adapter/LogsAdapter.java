package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.dto.LogDTO;
import com.example.honeytag1.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LogsAdapter extends BaseAdapter {
	private Context mContext;
	private List<LogDTO> mList = new ArrayList<LogDTO>();

	public LogsAdapter(Context context) {
		this.mContext = context;
	}

	public void reFresh(List<LogDTO> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_logs, null);
			mHolder = new Holder();

			mHolder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			mHolder.mTvTime = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(mHolder);

		} else {
			mHolder = (Holder) convertView.getTag();
		}

		mHolder.mTvTitle.setText(mList.get(position).getVersionname());
		mHolder.mTvTime.setText(mList.get(position).getCreate_time());
		return convertView;
	}

	static class Holder {
		TextView mTvTitle, mTvTime;

	}

}
