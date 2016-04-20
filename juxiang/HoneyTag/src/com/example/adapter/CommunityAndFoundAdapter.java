package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CommunityAndFoundAdapter extends FragmentPagerAdapter {

	List<Fragment> mList = new ArrayList<Fragment>();

	public CommunityAndFoundAdapter(FragmentManager fm, List<Fragment> mList) {
		super(fm);
		this.mList = mList;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

}
