package com.open.androidtvwidget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;import android.view.View;
import android.widget.GridView;

/**
 * GridView TV版本.
 * @author hailongqiu 356752238@qq.com
 *
 */
public class GridViewTV extends GridView {

	public GridViewTV(Context context) {
		super(context);
		init(context, null);
	}

	public GridViewTV(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	
	/**
	 * 崩溃了.
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		try {
			super.dispatchDraw(canvas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(Context context, AttributeSet attrs) {
		setClipChildren(false);
		setClipToPadding(false);
		setChildrenDrawingOrderEnabled(true);
	}

	private int position = 0;

	@Override
	public void bringChildToFront(View child) {
		position = indexOfChild(child);
		if (position != -1) {
			postInvalidate();
		}
	}

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
