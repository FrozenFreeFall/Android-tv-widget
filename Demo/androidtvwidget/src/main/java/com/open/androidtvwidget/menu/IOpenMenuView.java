package com.open.androidtvwidget.menu;

import android.view.View;
import android.widget.AdapterView;

public interface IOpenMenuView {
	/**
	 * 菜单View回调事件.
	 */
	public interface OnMenuListener {
		public boolean onMenuItemClick(AdapterView<?> parent, View view, int position, long id);

		public boolean onMenuItemSelected(AdapterView<?> parent, View view, int position, long id);
	}

	/**
	 * 菜单item接口函数.
	 */
	public interface ItemView {
		public void initialize(IOpenMenuItem itemData, int menuType);
	}

	public IOpenMenuView setMenuData(OpenMenu openMenu);
	/**
	 * 设置菜单view事件.
	 */
	public IOpenMenuView setOnMenuListener(OnMenuListener cb);
}
