package com.open.androidtvwidget.menu;

import java.util.ArrayList;

/**
 * 菜单接口.
 *
 * @author hailongqiu
 */
public interface OpenMenu {

    static final int NONE = 0;

    /**
     * 添加菜单项.
     */
    public OpenMenuItem add(CharSequence title);

    /**
     * 添加子菜单.
     */
    public OpenMenu addSubMenu(int pos, OpenMenu openSubMenu); // 添加子菜单到某个位置的菜单上.

    public OpenMenu addSubMenu(OpenMenuItem menuItem, OpenMenu openSubMenu);

    public OpenMenu setTextSize(int size); // 全局设置菜单字体.

    public ArrayList<OpenMenuItem> getMenuDatas(); // 获取菜单数据.

    public void setParentMenu(OpenMenu openMenu);

    public OpenMenu getParentMenu();

    /**
     * 注册/注销----观察者
     */
    void registerDataSetObserver(MenuSetObserver observer);

    void unregisterDataSetObserver(MenuSetObserver observer);

    MenuDataObservable getMenuDataObservable(); // 获取监听者.

    void showMenu();

    void hideMenu();

    /**
     * 获取层级.
     */
    int getTreeDepth();

    /**
     * 数据更新.
     */
    public void notifyChanged();

}
