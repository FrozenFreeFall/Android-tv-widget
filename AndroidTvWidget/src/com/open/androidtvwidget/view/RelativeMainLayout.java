package com.open.androidtvwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 
 * @author hailongqiu
 *
 */
public class RelativeMainLayout extends RelativeLayout {

	public RelativeMainLayout(Context context) {
		super(context);
		init(context);
	}

	public RelativeMainLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	WidgetTvViewBring mWidgetTvViewBring;

	private void init(Context context) {
		this.setChildrenDrawingOrderEnabled(true);
		mWidgetTvViewBring = new WidgetTvViewBring(this);
	}

	@Override
	public void bringChildToFront(View child) {
		mWidgetTvViewBring.bringChildToFront(this, child);
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		return mWidgetTvViewBring.getChildDrawingOrder(childCount, i);
	}
}
