package com.open.androidtvwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 如果有控件放大被挡住，可以使用 RelativeMainLayout, <p>
 * 它继承于 LinearLayout.<p>
 * 使用方式和LinerLayout是一样的<p>
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
