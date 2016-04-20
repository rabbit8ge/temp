package com.example.utils;

import com.example.honeytag1.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MyActivityAPP extends Activity {

	LinearLayout main_root;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		main_root = (LinearLayout) findViewById(R.id.main_root);
		if (Utils.checkDeviceHasNavigationBar(this) == true) {
			main_root.setPadding(0, 0, 0, Utils.getNavigationBarHeight(this));
		}
	}

}
