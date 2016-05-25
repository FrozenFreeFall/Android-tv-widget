package com.open.androidtvwidget.utils;

import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;

public class GenerateViewId {
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
	private static GenerateViewId sGenerateViewId = new GenerateViewId();

	public static GenerateViewId getSingleton() {
		return sGenerateViewId;
	}

	/**
	 * 动态生成View ID API LEVEL 17 以上View.generateViewId()生成 API LEVEL 17 以下需要手动生成
	 */
	@SuppressLint("NewApi")
	public int generateViewId() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
			for (;;) {
				final int result = sNextGeneratedId.get();
				// aapt-generated IDs have the high byte nonzero; clamp to the
				// range under that.
				int newValue = result + 1;
				if (newValue > 0x00FFFFFF)
					newValue = 1; // Roll over to 1, not 0.
				if (sNextGeneratedId.compareAndSet(result, newValue)) {
					return result;
				}
			}
		} else {
			return View.generateViewId();
		}

	}

}
