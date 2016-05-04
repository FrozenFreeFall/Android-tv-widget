package com.open.androidtvwidget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
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

	WidgetTvViewBring mWidgetTvViewBring;
	
	private void init(Context context, AttributeSet attrs) {
		this.setChildrenDrawingOrderEnabled(true);
		mWidgetTvViewBring = new WidgetTvViewBring(this); 
	}

	@Override
	public void bringChildToFront(View child) {
		mWidgetTvViewBring.bringChildToFront(this, child);
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		// position = getSelectedItemPosition() - getFirstVisiblePosition();
		return mWidgetTvViewBring.getChildDrawingOrder(childCount, i);
	}

}
