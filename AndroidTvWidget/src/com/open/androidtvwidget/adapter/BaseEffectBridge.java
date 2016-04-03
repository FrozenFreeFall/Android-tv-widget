package com.open.androidtvwidget.adapter;

import com.open.androidtvwidget.view.MainUpView;

import android.graphics.Canvas;
import android.view.View;

/**
 * Anim Bridge 抽象类.
 * ----桥接模式.
 * 
 * @author hailongqiu 356752238@qq.com
 *
 */
public abstract class BaseEffectBridge {

	public abstract void onInitBridge(MainUpView view);

	/**
	 * 需要绘制的东西.
	 */
	public abstract boolean onDrawMainUpView(Canvas canvas);

	/**
	 * 老的焦点View.
	 */
	public abstract void onOldFocusView(View oldFocusView, float scaleX, float scaleY);

	/**
	 * 新的焦点View.
	 */
	public abstract void onFocusView(View focusView, float scaleX, float scaleY);

	public void setMainUpView(MainUpView view) {

	}

	/**
	 * 最上层移动的view.
	 */
	public MainUpView getMainUpView() {
		return null;
	}

}
