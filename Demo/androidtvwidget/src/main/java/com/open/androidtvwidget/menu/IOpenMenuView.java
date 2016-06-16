package com.open.androidtvwidget.menu;

import android.view.View;
import android.widget.AdapterView;

public interface IOpenMenuView {
    /**
     * 菜单View回调事件.
     */
    public interface OnMenuListener {
        public boolean onMenuItemClick(AdapterView<?> parent, IOpenMenuItem menuItem, View view, int position, long id);

        public boolean onMenuItemSelected(AdapterView<?> parent, IOpenMenuItem menuItem, View view, int position, long id);

        public boolean onMenuItemFocusChange(AdapterView<?> parent, View view);
    }

    /**
     * 菜单item接口函数.
     */
    public interface ItemView {
        public void initialize(IOpenMenuItem itemData);
    }

    public IOpenMenuView setMenuData(IOpenMenu openMenu);

    /**
     * 设置菜单view事件.
     */
    public IOpenMenuView setOnMenuListener(OnMenuListener cb);

    /**
     * 添加移动的view
     */
    public IOpenMenuView setMoveView(View v);
}
