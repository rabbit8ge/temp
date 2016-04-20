package com.example.view;

import com.example.utils.Logger;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/*自定义圆形的ImageView,可以直接当当组件在布局中使用
 * 
 */

public class CircleImageView2 extends ImageView {

	private Paint paint;

	public CircleImageView2(Context context) {
		// super(context);
		this(context, null);
	}

	public CircleImageView2(Context context, AttributeSet attrs) {
		// super(context, attrs);
		// TODO Auto-generated constructor stub
		this(context, attrs, 0);
	}

	public CircleImageView2(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (drawable != null && drawable.getIntrinsicHeight() != 0) {

			Bitmap bitmap = drawableToBitamp(drawable);
			if (bitmap != null) {
				Bitmap b = getCircleBitmap(bitmap, 14);
				final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
				final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
				paint.reset();
				canvas.drawBitmap(b, rectSrc, rectDest, paint);
			} else {
				super.onDraw(canvas);
			}

		} else {
			super.onDraw(canvas);
		}

	}

	/*
	 * 获得圆形的Bitmap
	 */
	private Bitmap getCircleBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		int x = bitmap.getWidth();

		canvas.drawCircle(x / 2, x / 2, x / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/*
	 * 功能:将将Drawable转成Bitmap
	 * 
	 */
	private Bitmap drawableToBitamp(Drawable drawable) {

		Bitmap bitmap;

		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		if (w <= 0 || h <= 0) {
			return null;
		}

		// Log.i("aaaaaaaaaa", w + "||" + h);
		// Logger.i("Drawable转Bitmap");
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		bitmap = Bitmap.createBitmap(w, h, config);
		// 注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

}
