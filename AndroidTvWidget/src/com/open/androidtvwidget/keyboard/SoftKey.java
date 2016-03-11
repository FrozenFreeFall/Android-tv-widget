package com.open.androidtvwidget.keyboard;

import android.graphics.drawable.Drawable;

/**
 * 按键的键值.
 * 
 * @author hailong.qiu 356752238@qq.com
 *
 */
public class SoftKey {

	private Drawable mKeyIcon;
	private Drawable mKeyIconPopup;
	private String mKeyLabel;
	private int mKeyCode;

	private float mLeftF;
	private float mRightF;
	private float mTopF;
	private float mBottomF;

	public void setKeyDimensions(float left, float top, float right, float bottom) {
		mLeftF = left;
		mTopF = top;
		mRightF = right;
		mBottomF = bottom;
	}

	public Drawable getKeyIcon() {
		return mKeyIcon;
	}

	public Drawable getKeyIconPopup() {
		if (null != mKeyIconPopup) {
			return mKeyIconPopup;
		}
		return mKeyIcon;
	}

	public String getKeyLabel() {
		return mKeyLabel;
	}

	@Override
	public String toString() {
		return "SoftKey [mKeyIcon=" + mKeyIcon + ", mKeyIconPopup=" + mKeyIconPopup + ", mKeyLabel=" + mKeyLabel
				+ ", mKeyCode=" + mKeyCode + "]";
	}

}
