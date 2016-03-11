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
	}

	public void setCurrentPosition(int pos) {
		this.position = pos;
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		View currentFocusView = getFocusedChild();
		int pos = indexOfChild(currentFocusView);
		if (pos >= 0 && pos < childCount)
			setCurrentPosition(pos);
		if (i == childCount - 1) { // 这是最后一个需要刷新的item
			return position;
		}
		if (i == position) { // 这是原本要在最后一个刷新的item
			return childCount - 1;
		}
		return i;// 正常次序的item
	}

}
