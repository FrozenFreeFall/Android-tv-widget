package com.androidtv.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class ImageUtils {

	/**
	 * 获取指定区域的图片位置.
	 */
	public static Bitmap getBitmapRectangle(View view, Bitmap bkg) {
		return getBitmapRectangle(view, bkg, 1);
	}

	/**
	 * 获取指定区域的图片位置.
	 */
	public static Bitmap getBitmapRectangle(View view, Bitmap bkg,
			float scaleFactor) {
		Bitmap overlay = Bitmap.createBitmap(
				(int) (view.getMeasuredWidth() / scaleFactor),
				(int) (view.getMeasuredHeight() / scaleFactor),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
				/ scaleFactor);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(bkg, 0, 0, paint);
		return overlay;
	}

}
