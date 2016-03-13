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

	private boolean isSelected;

	public float getLeftF() {
		return mLeftF;
	}

	public float getRightF() {
		return mRightF;
	}

	public float getTopF() {
		return mTopF;
	}

	public float getBottomF() {
		return mBottomF;
	}

	public float getWidth() {
		return mRightF - mLeftF;
	}

	public float getHeight() {
		return mBottomF - mTopF;
	}

	public void setKeyDimensions(float left, float top, float right, float bottom) {
		mLeftF = left;
		mTopF = top;
		mRightF = right;
		mBottomF = bottom;
	}

	public void setKeyLabel(String label) {
		this.mKeyLabel = label;
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

	/**
	 * 是否選中.
	 * 键值要高亮.
	 */
	public void setKeySelected(boolean is) {
		isSelected = is;
	}

	public boolean isKeySelected() {
		return this.isSelected;
	}

	@Override
	public String toString() {
		return "SoftKey [mKeyIcon=" + mKeyIcon + ", mKeyIconPopup=" + mKeyIconPopup + ", mKeyLabel=" + mKeyLabel
				+ ", mKeyCode=" + mKeyCode + "]";
	}

}
