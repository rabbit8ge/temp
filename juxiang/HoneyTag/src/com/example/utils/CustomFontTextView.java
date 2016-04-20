package com.example.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mrni-mac on 14-11-25.
 */
public class CustomFontTextView extends TextView {

	public CustomFontTextView(Context context) {
		super(context);
		init(context);
	}

	public CustomFontTextView(Context context, AttributeSet attrs) {
		super(context);
		init(context);
	}

	public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context);
		init(context);
	}

	public void init(Context context) {
		// AssetManager assetManager = context.getAssets();
		// Typeface font = Typeface.createFromAsset(assetManager,
		// "fonts/DroidSansFallback.ttf");
		// setTypeface(font);
	}

}