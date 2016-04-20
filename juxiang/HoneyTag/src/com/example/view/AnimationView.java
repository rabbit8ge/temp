// package com.example.view;
//
// import com.example.honeytag1.R;
//
// import android.content.Context;
// import android.graphics.Bitmap;
// import android.graphics.BitmapFactory;
// import android.graphics.Canvas;
// import android.graphics.Color;
// import android.graphics.Paint;
// import android.view.MotionEvent;
// import android.view.View;
//
// public class AnimationView extends View {
//
// final int ANIM_COUNT = 4;
//
// Bitmap[] bitmaps = new Bitmap[4];
//
// Paint paint;
//
// long startime = 0;
// int playId = 0;
// int mHeight = 0;
//
// public AnimationView(Context context, int height) {
// super(context);
//
// mHeight = height;
//
// bitmaps[0] = BitmapFactory.decodeResource(this.getResources(),
// R.drawable.hero_down_a);
// bitmaps[1] = BitmapFactory.decodeResource(this.getResources(),
// R.drawable.hero_down_b);
// bitmaps[2] = BitmapFactory.decodeResource(this.getResources(),
// R.drawable.hero_down_c);
// bitmaps[3] = BitmapFactory.decodeResource(this.getResources(),
// R.drawable.hero_down_d);
//
// startime = System.currentTimeMillis();
// paint = new Paint();
// }
//
// int x = 200;
// int y = 1;
//
// protected void onDraw(Canvas canvas) {
// // TODO Auto-generated method stub
// super.onDraw(canvas);
//
// paint.setColor(Color.RED);
// paint.setFlags(Paint.ANTI_ALIAS_FLAG);
// // canvas.drawText("展示动画", 10,100, paint);
//
// long nowTime = System.currentTimeMillis();
// if (nowTime - startime > 20) {
// startime = nowTime;
// y++;
// playId++;
// if (playId >= ANIM_COUNT) {
// playId = 0;
// }
// // 循环图片针
// canvas.drawBitmap(bitmaps[playId], x, y, paint);
//
// if (y > 400 && y < 500) {
// canvas.drawText("我是哇哈哈，你懂的", x + 40, y + 20, paint);
// }
//
// if (y > mHeight) {
// y = 1;
// }
// }
// invalidate();
// }
//
// @Override
// public boolean onTouchEvent(MotionEvent event) {
// // TODO Auto-generated method stub
// return super.onTouchEvent(event);
// }
//
// }
