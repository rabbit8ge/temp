// package com.example.view;
//
// import com.example.honeytag1.R;
//
// import android.content.Context;
// import android.graphics.Bitmap;
// import android.graphics.BitmapFactory;
// import android.graphics.Canvas;
// import android.graphics.Color;
// import android.graphics.Matrix;
// import android.graphics.Paint;
// import android.view.MotionEvent;
// import android.view.View;
//
// public class ScalImgView extends View {
//
// Bitmap bitmap = null;
//
// Bitmap bitmapDisplay = null;
//
// Matrix mMatrix;
//
// int mHeight = 0;
// int mWidth = 0;
//
// int mPosX = 200;
// int mPosY = 200;
//
// // 偏移量
// float mAngle = 0.0f;
// // 缩放比例 1表示原样
// float mScale = 1f;
//
// public ScalImgView(Context context) {
// super(context);
//
// bitmap = BitmapFactory.decodeResource(this.getResources(),
// R.drawable.hero_down_a);
//
// bitmapDisplay = bitmap;
// mMatrix = new Matrix();
// // 获取图片的高度
// mHeight = bitmap.getHeight();
// mWidth = bitmap.getWidth();
// }
//
// public void setPosLeft() {
// mPosX -= 10;
// }
//
// public void setPosRight() {
// mPosX += 10;
// }
//
// public void setRotaionLeft() {
// mAngle--;
// setAngle();
// }
//
// public void setRotaionRight() {
// mAngle++;
// setAngle();
// }
//
//
// public void setAngle() {
// mMatrix.reset();
// mMatrix.setRotate(mAngle);
// bitmapDisplay = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight,
// mMatrix, true);
// }
//
// public void setEnlarge() {
// if (mScale < 2) {
// mScale += 0.1;
// setScale();
// }
// }
//
// public void setScale() {
// mMatrix.reset();
// // 缩放x,y
// mMatrix.postScale(mScale, mScale);
// bitmapDisplay = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight,
// mMatrix, true);
//
// }
//
//
// public boolean onKeyDown(int keyCode, KeyEvent event) {
//
// setEnlarge();
//
// return super.onKeyDown(keyCode, event);
// }
//
// public boolean onTouchEvent(MotionEvent event) {
//
// setRotaionLeft();
//
// return super.onTouchEvent(event);
// }
//
// protected void onDraw(Canvas canvas) {
// // TODO Auto-generated method stub
// super.onDraw(canvas);
// Paint paint = new Paint();
// paint.setColor(Color.RED);
// canvas.drawBitmap(bitmapDisplay, mPosX, mPosY, paint);
// canvas.drawBitmap(bitmap, 100, 200, paint);
// invalidate();
// }
// }
//
/// *-----------------------------动画效果--------------------------------------*/
//
//// public class AnimationView extends View {
////
//// final int ANIM_COUNT = 4;
////
//// Bitmap[] bitmaps = new Bitmap[4];
////
//// Paint paint;
////
//// long startime = 0;
//// int playId = 0;
//// int mHeight = 0;
////
//// public AnimationView(Context context, int height) {
//// super(context);
////
//// mHeight = height;
////
//// bitmaps[0] = BitmapFactory.decodeResource(this.getResources(),
//// R.drawable.hero_down_a);
//// bitmaps[1] = BitmapFactory.decodeResource(this.getResources(),
//// R.drawable.hero_down_b);
//// bitmaps[2] = BitmapFactory.decodeResource(this.getResources(),
//// R.drawable.hero_down_c);
//// bitmaps[3] = BitmapFactory.decodeResource(this.getResources(),
//// R.drawable.hero_down_d);
////
//// startime = System.currentTimeMillis();
//// paint = new Paint();
//// }
////
//// int x = 200;
//// int y = 1;
////
//// protected void onDraw(Canvas canvas) {
//// // TODO Auto-generated method stub
//// super.onDraw(canvas);
////
//// paint.setColor(Color.RED);
//// paint.setFlags(Paint.ANTI_ALIAS_FLAG);
//// // canvas.drawText("展示动画", 10,100, paint);
////
//// long nowTime = System.currentTimeMillis();
//// if (nowTime - startime > 20) {
//// startime = nowTime;
//// y++;
//// playId++;
//// if (playId >= ANIM_COUNT) {
//// playId = 0;
//// }
//// // 循环图片针
//// canvas.drawBitmap(bitmaps[playId], x, y, paint);
////
//// if (y > 400 && y < 500) {
//// canvas.drawText("我是哇哈哈，你懂的", x + 40, y + 20, paint);
//// }
////
//// if (y > mHeight) {
//// y = 1;
//// }
//// }
//// invalidate();
//// }
////
//// @Override
//// public boolean onTouchEvent(MotionEvent event) {
//// // TODO Auto-generated method stub
//// return super.onTouchEvent(event);
//// }
////
//// }