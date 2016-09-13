package com.open.androidtvwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 如果有控件放大被挡住，可以使用 MainLayout,
 * <p>
 * 它继承于 FrameLayout.
 * <p>
 * 使用方式就和FrameLayout是一样的
 * <p>
 * 如果你其它的控件也被挡住了，但是这里没有，你可以使用WidgetTvViewBring
 * <p>
 * 
 * @author hailongqiu
 *
 */
public class FrameMainLayout extends FrameLayout {

	public FrameMainLayout(Context context) {
		super(context);
		init(context);
	}

	public FrameMainLayout(Context context, AttributeSet attrs) {
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
