package com.open.androidtvwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class MainLayout extends RelativeLayout {

	private int position = 0;

	public MainLayout(Context context) {
		super(context, null, 0);
		init(context);
	}

	public MainLayout(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init(context);
	}

	public MainLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		setClipChildren(false);
		setClipToPadding(false);
		setChildrenDrawingOrderEnabled(true);
//		getViewTreeObserver()
//		.addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
//			@Override
//			public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//				position = indexOfChild(newFocus);
//				if (position != -1) {
//					invalidate();
//				}
//			}
//		});
		// 添加焦点监听事件.
//		for (int i = 0; i < getChildCount(); i++) {
//			getChildAt(i).setOnFocusChangeListener(new OnFocusChangeListener() {
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//				}
//			});
//		}
	}
	
	@Override
	public void bringChildToFront(View child) {
		position = indexOfChild(child);
		if (position != -1) {
//			invalidate();
			postInvalidate();
		}
	}
	
	/**
	 * 此函数 dispatchDraw 中调用. <br>
	 * 原理就是和最后一个要绘制的view，交换了位置. <br>
	 * 因为dispatchDraw最后一个绘制的view是在最上层的. <br>
	 * 这样就避免了使用 bringToFront 导致焦点错乱问题. <br>
	 * 不得不赞一个，这里要赞群里大家的互相学习和一起进步. <br>
	 */
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if (position != -1) {
			if (i == childCount - 1)
				return position;
			if (i == position) 
				return childCount - 1;
		}
		return i;
	}

}
