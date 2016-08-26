/*
Copyright 2016 The Open Source Project

Author: hailongqiu <356752238@qq.com>
Maintainer: hailongqiu <356752238@qq.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.open.androidtvwidget.menu;

import com.open.androidtvwidget.utils.OPENLOG;

import java.util.ArrayList;

/**
 * 菜单.
 *
 * @author hailongqiu
 */
public class OpenMenuImpl implements OpenMenu {

    private static final String TAG = "OpenMenu";

    private final MenuDataObservable mMenuDataObservable = new MenuDataObservable(); // 观察者.

    private ArrayList<OpenMenuItem> mItems;
    private OpenMenu mParent;

    private int mTextSize = OpenMenuItem.DEFAULT_TEXT_SIZE;

    public OpenMenuImpl() {
        init();
    }

    @Override
    public void setParentMenu(OpenMenu openMenu) {
        mParent = openMenu;
    }

    @Override
    public OpenMenu getParentMenu() {
        return this.mParent;
    }

    @Override
    public void registerDataSetObserver(MenuSetObserver observer) {
        mMenuDataObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(MenuSetObserver observer) {
        mMenuDataObservable.unregisterObserver(observer);
    }

    @Override
    public MenuDataObservable getMenuDataObservable() {
        return this.mMenuDataObservable;
    }

    @Override
    public void showMenu() {
        if (mMenuDataObservable != null)
            mMenuDataObservable.nofityShow(this);
    }

    @Override
    public void hideMenu() {
        if (mMenuDataObservable != null)
            mMenuDataObservable.notifyHide(this);
    }

    @Override
    public int getTreeDepth() {
        return this.mParent == null ? 0 : this.mParent.getTreeDepth() + 1;
    }

    @Override
    public void notifyChanged() {
        if (mMenuDataObservable != null)
            mMenuDataObservable.notifyChanged(this);
    }

    private void init() {
        this.mItems = new ArrayList<OpenMenuItem>();
    }

    @Override
    public OpenMenu setTextSize(int size) {
        this.mTextSize = size;
        return this;
    }

    private OpenMenuItem addInternal(int itemId, CharSequence title) {
        final OpenMenuItem item = new OpenMenuItemImpl(this, itemId, title);
        item.setTextSize(mTextSize);
        mItems.add(item);
        return item;
    }

    @Override
    public OpenMenuItem add(CharSequence title) {
        return addInternal(0, title);
    }

    /**
     * 添加子菜单.
     */
    @Override
    public OpenMenu addSubMenu(int pos, OpenMenu openSubMenu) {
        if (mItems != null && pos < mItems.size()) {
            OpenMenuItem menuItem = mItems.get(pos);
            addSubMenu(menuItem, openSubMenu);
        }
        return openSubMenu;
    }

    public OpenMenu addSubMenu(OpenMenuItem menuItem, OpenMenu openSubMenu) {
        menuItem.addSubMenu(openSubMenu);
        return openSubMenu;
    }

    /**
     * 设置数据.
     */
    @Override
    public String toString() {
        for (OpenMenuItem item : mItems) {
            String title = item.getTitle().toString();
            OPENLOG.E("menu item:" + title + " 深度:" + item.getMenu().getTreeDepth());
            OpenMenu submenu = item.getSubMenu();
            if (submenu != null) {
                OPENLOG.E("=======sub menu======start start start start");
                submenu.toString();
                OPENLOG.E("=======sub menu======end end end end");
            }
        }
        return super.toString();
    }

    @Override
    public ArrayList<OpenMenuItem> getMenuDatas() {
        return mItems;
    }

}
