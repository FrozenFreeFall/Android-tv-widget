package com.open.androidtvwidget.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseTabTitleAdapter extends BaseAdapter {

	@Override
	public int getCount() {
		return 0;
	}
	
	public Integer getTitleWidgetID(int index) {
		return 0;
	}
	
	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
