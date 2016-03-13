package com.open.androidtvwidget.keyboard;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

/**
 * 软键盘绘制控件.(主软键盘，弹出键盘)
 * 
 * @author hailong.qiu
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
		// drawbg.
		drawKeyboardBg(canvas);
		// draw softkey.
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
		//
	}

	private Paint mPaint;

	private void drawKeyboardBg(Canvas canvas) {
		Paint paint = new Paint();
		canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
	}

	/**
	 * 绘制键值.
	 */
	private void drawSoftKey(Canvas canvas, SoftKey softKey) {
		String keyLabel = softKey.getKeyLabel();
		Drawable keyIcon = softKey.getKeyIcon();
		if (keyIcon != null) {
			drawSoftKeyIcon(canvas, softKey, keyIcon);
		} else if (!TextUtils.isEmpty(keyLabel)) {
			drawSoftKeyText(canvas, softKey, keyLabel);
		}
		if (softKey.isKeySelected()) {
			drawSoftKeyRoundRect(canvas, softKey);
		}
	}

	/**
	 * 绘制按键的图片.
	 */
	private void drawSoftKeyIcon(Canvas canvas, SoftKey softKey, Drawable keyIcon) {
		Rect rect = new Rect();
		keyIcon.setBounds(rect);
		keyIcon.draw(canvas);
	}

	private FontMetricsInt mFmi;

	/**
	 * 绘制按键的文本字符.
	 */
	private void drawSoftKeyText(Canvas canvas, SoftKey softKey, String keyLabel) {
		mPaint.setTextSize(30);
		int fontHeight = mFmi.bottom - mFmi.top; // 字體的高度.
		float marginY = (softKey.getHeight() - fontHeight) / 2.0f;
		float marginX = (softKey.getWidth() - mPaint.measureText(keyLabel)) / 2.0f;
		float x = softKey.getLeftF() + marginX;
		float y = softKey.getTopF() + fontHeight + marginY;
		mPaint.setColor(Color.RED);
		canvas.drawText(keyLabel, x, y, mPaint);
	}

	/**
	 * 绘制键值的背景的矩形.
	 */
	private void drawSoftKeyRoundRect(Canvas canvas, SoftKey softKey) {
		float left = softKey.getLeftF();
		float top = softKey.getTopF();
		float right = softKey.getRightF();
		float bottom = softKey.getBottomF();
		int round = 4;
		RectF outerRect = new RectF(left, top, right, bottom);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRoundRect(outerRect, round, round, paint);
	}
	
	public SoftKeyboard getSoftKeyboard() {
		return mSoftKeyboard;
	}
	
	/**
	 * 按键移动. <br>
	 * 感觉按照left,top,right,bottom<br>
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
			if (currentIndex > (softKeys.size() -1))
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
