package com.example.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;

/**
 * Created by Maxim Dybarsky | maxim.dybarskyy@gmail.com on 13.01.15 at 14:17
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class AnimatedView extends View {

	private int target;

	@SuppressLint("NewApi")
	public AnimatedView(Context context) {
		super(context);
	}

	public float getXFactor() {
		return getX() / target;
	}

	public void setXFactor(float xFactor) {
		setX(target * xFactor);
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getTarget() {
		return target;
	}
}
