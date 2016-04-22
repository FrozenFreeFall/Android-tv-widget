package com.open.androidtvwidget.menu;

import com.open.androidtvwidget.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 菜单ListView.
 * 
 * @author hailongqiu
 *
 */
public class OpenListMenuView extends RelativeLayout implements OpenMenuView {

	public OpenListMenuView(Context context) {
		super(context);
		init(context, null);
	}

	public OpenListMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private ListView mMenuListView;

	private void init(Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.open_listview_view, this, true);
		mMenuListView = (ListView) findViewById(R.id.menu_listview);
	}

	public ListView getMenuListView() {
		return mMenuListView;
	}
	
	public View getMainView() {
		return findViewById(R.id.menu_rlayt);
	}
	
	@Override
	public void initialize(OpenMenuBuilder menu) {
	}

}
