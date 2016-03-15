package com.open.androidtvwidget.keyboard;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

/**
 * 软键盘绘制控件.(主软键盘，弹出键盘)
 * 
 * @author hailong.qiu 356752238@qq.com
 *
 */
public class SoftKeyboardView extends View {

	private static final String TAG = "SoftKeyboardView";

	public SoftKeyboardView(Context context) {
		super(context);
		init(context, null);
	}

	public SoftKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SoftKeyboardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mFmi = mPaint.getFontMetricsInt();
	}

	private SoftKeyboard mSoftKeyboard;

	/*
	 * 传入需要绘制的键盘(从XML读取出来的).
	 */
	public void setSoftKeyboard(SoftKeyboard softSkb) {
		this.mSoftKeyboard = softSkb;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mSoftKeyboard == null)
			return;
		// 绘制键盘背景.
		drawKeyboardBg(canvas);
		// 绘制键盘的按键.
		int rowNum = this.mSoftKeyboard.getRowNum();
		for (int row = 0; row < rowNum; row++) {
			KeyRow keyRow = this.mSoftKeyboard.getKeyRowForDisplay(row);
			if (keyRow == null)
				continue;
			List<SoftKey> softKeys = keyRow.getSoftKeys();
			int keyNum = softKeys.size();
			for (int i = 0; i < keyNum; i++) {
				SoftKey softKey = softKeys.get(i);
				drawSoftKey(canvas, softKey);
			}
		}
	}

	private Paint mPaint;

	/**
	 * 绘制键盘的背景.
	 */
	private void drawKeyboardBg(Canvas canvas) {
		Drawable bg = mSoftKeyboard.getKeyboardBg();
		Rect rect = new Rect(0, 0, getWidth(), getHeight());
		if (bg != null) {
			bg.setBounds(rect);
			bg.draw(canvas);
		} else {
			Paint paint = new Paint();
			canvas.drawRect(rect, paint);
		}
	}

	/**
	 * 绘制键值.
	 */
	private void drawSoftKey(Canvas canvas, SoftKey softKey) {
		// 绘制按键背景.
		drawSoftKeyBg(canvas, softKey);
		// 绘制选中状态.
		if (softKey.isKeySelected()) {
			drawSoftKeySelectState(canvas, softKey);
		}
		if (softKey.isKeyPressed()) {
			drawSoftKeyPressState(canvas, softKey);
		}
		// 绘制按键内容.
		String keyLabel = softKey.getKeyLabel();
		Drawable keyIcon = softKey.getKeyIcon();
		if (keyIcon != null) {
			drawSoftKeyIcon(canvas, softKey, keyIcon);
		} else if (!TextUtils.isEmpty(keyLabel)) {
			drawSoftKeyText(canvas, softKey, keyLabel);
		}
	}

	/**
	 * 绘制按键的图片.
	 */
	private void drawSoftKeyIcon(Canvas canvas, SoftKey softKey, Drawable keyIcon) {
		int marginLeft = (int) ((softKey.getWidth() - keyIcon.getIntrinsicWidth()) / 2);
		int marginRight = (int) (softKey.getWidth() - keyIcon.getIntrinsicWidth() - marginLeft);
		int marginTop = (int) ((softKey.getHeight() - keyIcon.getIntrinsicHeight()) / 2);
		int marginBottom = (int) (softKey.getHeight() - keyIcon.getIntrinsicHeight() - marginTop);
		keyIcon.setBounds(softKey.getLeft() + marginLeft, softKey.getTop() + marginTop,
				softKey.getRight() - marginRight, softKey.getBottom() - marginBottom);
		keyIcon.draw(canvas);
	}

	private FontMetricsInt mFmi;

	/**
	 * 绘制按键的文本字符.
	 */
	private void drawSoftKeyText(Canvas canvas, SoftKey softKey, String keyLabel) {
		mPaint.setTextSize(softKey.getTextSize()); // 文本大小.
		mPaint.setColor(softKey.getTextColor()); // 文本颜色.
		mFmi = mPaint.getFontMetricsInt();
		int fontHeight = mFmi.bottom - mFmi.top; // 字體的高度.
		float fontWidth = mPaint.measureText(keyLabel);
		Log.d(TAG, "drawSoftKeyText fontHeight:" + fontHeight);
		float marginX = (softKey.getWidth() - fontWidth) / 2.0f;
		float marginY = (softKey.getHeight() - fontHeight) / 2.0f;
		float x = softKey.getLeftF() + marginX;
		// float y = softKey.getTopF() - (mFmi.top) + marginY;
		/**
		 * +1，绘制文字的地方才不会出现问题。
		 */
		float y = softKey.getTopF() - (mFmi.top + 1) + marginY;
		canvas.drawText(keyLabel, x, y, mPaint);
	}

	/**
	 * 绘制按键背景.
	 */
	private void drawSoftKeyBg(Canvas canvas, SoftKey softKey) {
		Drawable bgDrawable = softKey.getKeyBgDrawable();
		if (bgDrawable != null) {
			bgDrawable.setBounds(softKey.getRect());
			bgDrawable.draw(canvas);
		}
	}

	/**
	 * 绘制按键的选中状态.
	 */
	private void drawSoftKeySelectState(Canvas canvas, SoftKey softKey) {
		Drawable selectDrawable = softKey.getKeySelectDrawable();
		if (selectDrawable != null) {
			selectDrawable.setBounds(softKey.getRect());
			selectDrawable.draw(canvas);
		}
	}
	
	/**
	 * 绘制按下的状态.
	 */
	private void drawSoftKeyPressState(Canvas canvas, SoftKey softKey) {
		Drawable pressDrawable = softKey.getKeyPressDrawable();
		if (pressDrawable != null) {
			pressDrawable.setBounds(softKey.getRect());
			pressDrawable.draw(canvas);
		}
	}

	public SoftKeyboard getSoftKeyboard() {
		return mSoftKeyboard;
	}
	
	public void setSoftKeyPress(boolean isPress) {
		SoftKey softKey = mSoftKeyboard.getSelectSoftKey();
		softKey.setKeyPressed(isPress);
		invalidate();
	}
	
	/**
	 * 按键移动. <br>
	 * 感觉按照left,top,right,bottom区域<br>
	 * 来查找按键,会影响效率.<br>
	 * 所以使用了最简单的 行,列概念.
	 */
	public boolean moveToNextKey(int direction) {
		int currentRow = mSoftKeyboard.getSelectRow();
		int currentIndex = mSoftKeyboard.getSelectIndex();

		KeyRow keyRow = mSoftKeyboard.getKeyRowForDisplay(currentRow);
		if (keyRow == null)
			return false;
		List<SoftKey> softKeys = keyRow.getSoftKeys();
		if (softKeys == null)
			return false;
		SoftKey softKey = null;

		switch (direction) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			currentIndex--;
			if (currentIndex < 0)
				currentIndex = softKeys.size() - 1;
			softKey = softKeys.get(currentIndex);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			currentIndex++;
			if (currentIndex > (softKeys.size() - 1))
				currentIndex = 0;
			softKey = softKeys.get(currentIndex);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			currentRow++;
			if (currentRow > (mSoftKeyboard.getRowNum() - 1))
				currentRow = 0;
			keyRow = mSoftKeyboard.getKeyRowForDisplay(currentRow);
			softKeys = keyRow.getSoftKeys();
			currentIndex = Math.max(Math.min(currentIndex, softKeys.size() - 1), 0);
			softKey = softKeys.get(currentIndex);
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			currentRow--;
			if (currentRow < 0)
				currentRow = (mSoftKeyboard.getRowNum() - 1);
			keyRow = mSoftKeyboard.getKeyRowForDisplay(currentRow);
			softKeys = keyRow.getSoftKeys();
			currentIndex = Math.max(Math.min(currentIndex, softKeys.size() - 1), 0);
			softKey = softKeys.get(currentIndex);
			break;
		default:
			break;
		}
		if (softKey != null) {
			mSoftKeyboard.setOneKeySelected(softKey);
			mSoftKeyboard.setSelectRow(currentRow);
			mSoftKeyboard.setSelectIndex(currentIndex);
			invalidate();
		}
		return true;
	}

}
