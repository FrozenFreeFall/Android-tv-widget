package com.open.androidtvwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SmoothListView extends ListView {
	public SmoothListView(Context context) {
		super(context);
	}

	public SmoothListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(false);
	}

	public SmoothListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFocusable(false);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
}
