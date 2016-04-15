package com.open.androidtvwidget.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 菜单ListView.
 * @author hailongqiu
 *
 */
public class OpenListMenuView extends ListView implements OpenMenuView {
	
	public OpenListMenuView(Context context) {
		super(context);
	}
	
	public OpenListMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void initialize(OpenMenuBuilder menu) {
	}
	
}
