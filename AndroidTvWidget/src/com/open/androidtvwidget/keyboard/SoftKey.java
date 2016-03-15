package com.open.androidtvwidget.keyboard;

import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * 按键的键值.
 * 
 * @author hailong.qiu 356752238@qq.com
 *
 */
public class SoftKey {

	private Drawable mKeySelectDrawable;
	private Drawable mKeyPressDrawable;
	private Drawable mKeyBgDrawable;
	private Drawable mKeyIcon;
	private String mKeyLabel;
	private int mKeyCode; // 自定义值.

	private float mLeftF;
	private float mRightF;
	private float mTopF;
	private float mBottomF;
	private float mTextSize; // 字体大小.
	private int mTextColor; // 字体颜色.

	private boolean isSelected;
	private boolean isPressed; // 是否按下.

	public Drawable getKeyPressDrawable() {
		return mKeyPressDrawable;
	}

	public void setKeyPressDrawable(Drawable keyPressDrawable) {
		this.mKeyPressDrawable = keyPressDrawable;
	}

	public boolean isKeyPressed() {
		return isPressed;
	}

	public void setKeyPressed(boolean isPressed) {
		this.isPressed = isPressed;
	}

	public int getKeyCode() {
		return mKeyCode;
	}

	public void setKeyCode(int keyCode) {
		this.mKeyCode = keyCode;
	}

	public Drawable getKeyIcon() {
		return mKeyIcon;
	}

	public void setKeyIcon(Drawable mKeyIcon) {
		this.mKeyIcon = mKeyIcon;
	}

	public int getTextColor() {
		return mTextColor;
	}

	public void setTextColor(int textColor) {
		this.mTextColor = textColor;
	}

	public float getTextSize() {
		return mTextSize;
	}

	public void setTextSize(float textSize) {
		this.mTextSize = textSize;
	}

	public Drawable getKeyBgDrawable() {
		return mKeyBgDrawable;
	}

	public void setKeyBgDrawable(Drawable keyBgDrawable) {
		this.mKeyBgDrawable = keyBgDrawable;
	}

	public void setKeySelectDrawable(Drawable d) {
		this.mKeySelectDrawable = d;
	}

	public Drawable getKeySelectDrawable() {
		return this.mKeySelectDrawable;
	}

	public RectF getRectF() {
		return new RectF(mLeftF, mTopF, mRightF, mBottomF);
	}

	public Rect getRect() {
		return new Rect((int) mLeftF, (int) mTopF, (int) mRightF, (int) mBottomF);
	}

	public float getLeftF() {
		return mLeftF;
	}

	public int getLeft() {
		return (int) this.mLeftF;
	}

	public float getRightF() {
		return mRightF;
	}

	public int getRight() {
		return (int) this.mRightF;
	}

	public float getTopF() {
		return mTopF;
	}

	public int getTop() {
		return (int) this.mTopF;
	}

	public float getBottomF() {
		return mBottomF;
	}

	public int getBottom() {
		return (int) this.mBottomF;
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

	public String getKeyLabel() {
		return mKeyLabel;
	}

	/**
	 * 是否選中. 键值要高亮.
	 */
	public void setKeySelected(boolean is) {
		isSelected = is;
	}

	public boolean isKeySelected() {
		return this.isSelected;
	}

	@Override
	public String toString() {
		return "SoftKey [mKeyIcon=" + mKeyIcon + ", mKeyLabel=" + mKeyLabel + ", mKeyCode=" + mKeyCode + "]";
	}

}
