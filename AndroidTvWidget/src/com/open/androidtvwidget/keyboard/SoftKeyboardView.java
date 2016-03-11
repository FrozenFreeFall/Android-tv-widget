package com.open.androidtvwidget.keyboard;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * 软键盘绘制控件.(主软键盘，弹出键盘)
 * 
 * @author hailong.qiu
 *
 */
public class SoftKeyboardView extends View {

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
		int rowNum = this.mSoftKeyboard.getRowNum();
		for (int row = 0; row < rowNum; row++) {
			KeyRow keyRow = this.mSoftKeyboard.getKeyRowForDisplay(row);
			if (keyRow == null)
				continue;
			List<SoftKey> softKeys = keyRow.mSoftKeys;
			int keyNum = softKeys.size();
			for (int i = 0; i < keyNum; i++) {
				SoftKey softKey = softKeys.get(i);
				drawSoftKey(canvas, softKey);
			}
		}
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

		} else if (!TextUtils.isEmpty(keyLabel)) {

		}
	}

}
