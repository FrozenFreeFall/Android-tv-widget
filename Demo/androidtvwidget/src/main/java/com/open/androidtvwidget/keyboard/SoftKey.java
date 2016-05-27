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
	// 移动的存储 (避免和LeftF冲突，因为要绘制文字和其它的东西)
	private float mMoveLeftF;
	private float mMoveRightF;
	private float mMoveTopF;
	private float mMoveBottomF;

	private float mTextSize; // 字体大小.
	private int mTextColor; // 字体颜色.

	private boolean isSelected;
	private boolean isPressed; // 是否按下.

	/**
	 * 快速定位按键.
	 */
	public class SaveSoftKey {
		public SoftKey key;
		public int row;
		public int index;
	}

	private SaveSoftKey mNextRightKey = new SaveSoftKey();
	private SaveSoftKey mNextLeftKey = new SaveSoftKey();
	private SaveSoftKey mNextTopKey = new SaveSoftKey();
	private SaveSoftKey mNextBottomKey = new SaveSoftKey();

	public SaveSoftKey getNextRightKey() {
		return mNextRightKey;
	}

	public void setNextRightKey(SoftKey nextRightKey, int row, int index) {
		this.mNextRightKey.key = nextRightKey;
		this.mNextRightKey.row = row;
		this.mNextRightKey.index = index;
	}

	public SaveSoftKey getNextLeftKey() {
		return mNextLeftKey;
	}

	public void setNextLeftKey(SoftKey nextLeftKey, int row, int index) {
		this.mNextLeftKey.key = nextLeftKey;
		this.mNextLeftKey.row = row;
		this.mNextLeftKey.index = index;
	}

	public SaveSoftKey getNextTopKey() {
		return mNextTopKey;
	}

	public void setNextTopKey(SoftKey nextTopKey, int row, int index) {
		this.mNextTopKey.key = nextTopKey;
		this.mNextTopKey.row = row;
		this.mNextTopKey.index = index;
	}

	public SaveSoftKey getNextBottomKey() {
		return mNextBottomKey;
	}

	public void setNextBottomKey(SoftKey nextBottomKey, int row, int index) {
		this.mNextBottomKey.key = nextBottomKey;
		this.mNextBottomKey.row = row;
		this.mNextBottomKey.index = index;
	}

	public Drawable getKeyPressDrawable() {
		return mKeyPressDrawable;
	}

	public void setKeyPressDrawable(Drawable keyPressDrawable) {
		this.mKeyPressDrawable = keyPressDrawable;
	}

	/**
	 * 判断是否为自定义按键. 不能大于0的keycode.
	 */
	public boolean isUserDefKey() {
		return (mKeyCode < 0);
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

	public Rect getMoveRect() {
		return new Rect((int) mMoveLeftF, (int) mMoveTopF, (int) mMoveRightF, (int) mMoveBottomF);
	}

	public RectF getMoveRectF() {
		return new RectF(mMoveLeftF, mMoveTopF, mMoveRightF, mMoveBottomF);
	}

	public float getLeftF() {
		return mLeftF;
	}

	public int getLeft() {
		return (int) this.mLeftF;
	}

	public void setMoveLeft(float left) {
		this.mMoveLeftF = left;
	}
	public int getMoveLeft() {
		return (int) this.mMoveLeftF;
	}

	public float getRightF() {
		return mRightF;
	}

	public int getRight() {
		return (int) this.mRightF;
	}

	public void setMoveRight(float right) {
		this.mMoveRightF = right;
	}
	public int getMoveRight() {
		return (int) this.mMoveRightF;
	}

	public float getTopF() {
		return mTopF;
	}

	public int getTop() {
		return (int) this.mTopF;
	}

	public void setMoveTop(float top) {
		this.mMoveTopF = top;
	}
	public int getMoveTop() {
		return (int) this.mMoveTopF;
	}

	public float getBottomF() {
		return mBottomF;
	}

	public int getBottom() {
		return (int) this.mBottomF;
	}

	public void setMoveBottom(float bottom) {
		this.mMoveBottomF = bottom;
	}
	public int getMoveBottom() {
		return (int) this.mMoveBottomF;
	}

	public float getWidth() {
		return mRightF - mLeftF;
	}

	public float getHeight() {
		return mBottomF - mTopF;
	}

	public void setKeyDimensions(float left, float top, float right, float bottom) {
		mLeftF = mMoveLeftF = left;
		mTopF = mMoveTopF = top;
		mRightF = mMoveRightF = right;
		mBottomF = mMoveBottomF = bottom;
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

	/**
	 * 大小写切换.
	 */
	public void changeCase(boolean upperCase) {
		if (null != mKeyLabel) {
			if (upperCase) { // 判断是否大写字母
				mKeyLabel = mKeyLabel.toUpperCase();
			} else { // 小写.
				mKeyLabel = mKeyLabel.toLowerCase();
			}
		}
	}

	@Override
	public String toString() {
		return "SoftKey [mKeyIcon=" + mKeyIcon + ", mKeyLabel=" + mKeyLabel + ", mKeyCode=" + mKeyCode + "]";
	}

}
