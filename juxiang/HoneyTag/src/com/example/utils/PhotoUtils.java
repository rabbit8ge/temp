package com.example.utils;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

public class PhotoUtils {

	/** 从给定路径加载图片 */
	public static Bitmap loadBitmap(String imgpath, Options option) {

		if (option != null) {
			return BitmapFactory.decodeFile(imgpath, option);
		} else {
			return BitmapFactory.decodeFile(imgpath);
		}

	}

	/** 从给定的路径加载图片，并指定是否自动旋转方向 */
	public static Bitmap loadBitmap(String imgpath, Options option, boolean adjustOritation) {
		String aa = imgpath;
		if (!adjustOritation) {

			return loadBitmap(imgpath, option);

		} else {
			Bitmap bm = loadBitmap(imgpath, option);
			int digree = 0;
			ExifInterface exif = null;
			try {
				exif = new ExifInterface(imgpath);
			} catch (IOException e) {
				e.printStackTrace();
				exif = null;
			}

			if (exif != null) {
				// 读取图片中相机方向信息
				// int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				// ExifInterface.ORIENTATION_UNDEFINED);
				int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
				// 计算旋转角度
				switch (ori) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					digree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					digree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					digree = 270;
					break;
				default:
					digree = 0;
					break;
				}
			}
			Log.i("digree", digree + "");
			if (digree != 0) {
				// 旋转图片
				Matrix m = new Matrix();
				m.postRotate(digree);
				bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
			}

			return bm;
		}
	}

	/*
	 * 对Bitmap进行旋转
	 */
	public static Bitmap rotatingBitmap(Bitmap bm, int digree) {

		if (digree != 0) {
			// 旋转图片
			Matrix m = new Matrix();
			m.postRotate(digree);
			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
		}

		return bm;
	}

	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;

		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			// MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
		}

		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;

				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;

				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				default:
					break;
				}
			}
		}

		return degree;
	}

}
