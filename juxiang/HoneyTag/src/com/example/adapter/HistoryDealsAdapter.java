package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.dto.HistoryDealsDTO;
import com.example.honeytag1.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @Description描述:历史交易adapter
 * @Author作者:dbj
 * @Date日期:2016-2-01 上午:10:28
 */

public class HistoryDealsAdapter extends BaseAdapter {

	private Context mContext;
	private List<HistoryDealsDTO> mList = new ArrayList<HistoryDealsDTO>();

	public HistoryDealsAdapter(Context mContext) {
		this.mContext = mContext;
	}

	public void reFresh(List<HistoryDealsDTO> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public Object getItem(int position) {

		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder = null;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_history_deals, null);
			convertView.setTag(mHolder);

		} else {
			mHolder = (Holder) convertView.getTag();
		}

		return convertView;
	}

	static class Holder {

	}

}
