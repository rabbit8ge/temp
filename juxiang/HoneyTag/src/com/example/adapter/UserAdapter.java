package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.config.MySharedPreference;
import com.example.database.DBHelper;
import com.example.dto.UserInfo;
import com.example.honeytag1.R;
import com.example.my.LoginActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter {
	private LoginActivity mActivity;
	private List<UserInfo> list = new ArrayList<UserInfo>();

	// private List<HashMap<String, Object>> list=new
	// ArrayList<HashMap<String,Object>>();
	private DBHelper dbHelper;

	public UserAdapter(LoginActivity mActivity, DBHelper dbHelper) {
		this.mActivity = mActivity;
		this.dbHelper = dbHelper;
	}

	public void reFresh(List<UserInfo> list) {

		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_user, null);
			holder.linear_userType = (LinearLayout) convertView.findViewById(R.id.linear_userType);
			holder.linear_userName = (LinearLayout) convertView.findViewById(R.id.linear_userName);
			holder.tv_userType = (TextView) convertView.findViewById(R.id.tv_userType);
			holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
			holder.img_clear = (ImageView) convertView.findViewById(R.id.img_clear);
			convertView.setTag(holder);
		} else {

			holder = (Holder) convertView.getTag();
		}
		// holder.tv_userName.setText((String)list.get(position).get("name"));
		// holder.img_clear.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		//
		// String[] usernames = dbHelper.queryAllUserName();
		// if (usernames.length > 0) {
		// dbHelper.delete(usernames[position]);
		// }
		// String[] newusernames = dbHelper.queryAllUserName();
		//
		// list.remove(position);
		//
		// reFresh(list);
		//
		//
		// }
		//
		// });

		// if (0 == position) {
		// holder.linear_userType.setVisibility(View.VISIBLE);
		// holder.tv_userType.setText("前台");
		//
		// } else if (20 == position) {
		// holder.linear_userType.setVisibility(View.VISIBLE);
		// holder.tv_userType.setText("中后台");
		//
		// } else {
		// holder.linear_userType.setVisibility(View.GONE);
		// }
		holder.tv_userName.setText((String) list.get(position).getUsername());
		// holder.img_clear.setVisibility(View.INVISIBLE);
		holder.img_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String[] usernames = dbHelper.queryAllUserName();
				if (usernames.length > 0) {
					dbHelper.delete(usernames[position]);
					UserInfo mUserInfo = MySharedPreference.readUserInfo(mActivity);
					if (mUserInfo.getUsername().equals(usernames[position])) {
						MySharedPreference.saveUserInfo(mActivity, new UserInfo());
					}
				}
				String[] newusernames = dbHelper.queryAllUserName();

				list.remove(position);

				reFresh(list);

			}

		});
		holder.linear_userName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserInfo account = (UserInfo) getItem(position);
				// mActivity.userName = account.getUserName();
				mActivity.login_name.setText(account.getUsername());
				mActivity.login_password.setText(account.getPassword());
				mActivity.login_name.requestFocus();
				mActivity.login_password.clearFocus();
				mActivity.mPopupWindow.dismiss();

			}

		});

		return convertView;
	}

	static class Holder {
		LinearLayout linear_userType, linear_userName;
		TextView tv_userName, tv_userType;
		ImageView img_clear;
	}

}
