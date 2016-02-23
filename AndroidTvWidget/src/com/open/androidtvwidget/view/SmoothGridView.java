package com.open.androidtvwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class SmoothGridView extends GridView {

	public SmoothGridView(Context context) {
		super(context);
	}

	public SmoothGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(false);
	}

	public SmoothGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFocusable(false);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
