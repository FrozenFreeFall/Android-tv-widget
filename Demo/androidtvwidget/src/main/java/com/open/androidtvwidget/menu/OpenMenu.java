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

import java.util.ArrayList;

import com.open.androidtvwidget.utils.OPENLOG;

import android.graphics.Rect;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;

/**
 * 菜单.
 * 
 * @author hailongqiu
 *
 */
public class OpenMenu implements IOpenMenu {

	private static final String TAG = "OpenMenu";

	private ArrayList<IOpenMenuItem> mItems;
	private OpenMenu mParent;
	private AbsListView mAbsListView;
	/**
	 * 菜单属性.
	 */
	private int mMenuWidth;
	private int mMenuHeight;
	private int mTextSize = IOpenMenuItem.DEFAULT_TEXT_SIZE;
	private int mMenuItemLayoutID = DEFAULT_LAYOUT_ID;
	private int mId;
	private int mGravity = Gravity.TOP;
	private Rect mMarginRect; // 增加菜单距离.
	private LayoutAnimationController mLoadAnimation;
	private Animation mShowAnimation;

	public OpenMenu() {
		init();
	}

	public void setParentMenu(OpenMenu openMenu) {
		mParent = openMenu;
	}

	public OpenMenu getParentMenu() {
		return this.mParent;
	}

	private void init() {
		this.mItems = new ArrayList<IOpenMenuItem>();
	}

	@Override
	public OpenMenu setTextSize(int size) {
		this.mTextSize = size;
		return this;
	}

	public IOpenMenuItem addInternal(int itemId, CharSequence title) {
		final IOpenMenuItem item = new OpenMenuItem(this, itemId, title);
		item.setTextSize(mTextSize);
		mItems.add(item);
		return item;
	}

	@Override
	public IOpenMenuItem add(CharSequence title) {
		return addInternal(0, title);
	}

	/**
	 * 添加子菜单.
	 */
	@Override
	public OpenSubMenu addSubMenu(int pos, OpenSubMenu openSubMenu) {
		if (mItems != null && pos < mItems.size()) {
			IOpenMenuItem menuItem = mItems.get(pos);
			return addSubMenu(menuItem, openSubMenu);
		}
		return openSubMenu;
	}

	public OpenSubMenu addSubMenu(IOpenMenuItem menuItem, OpenSubMenu openSubMenu) {
		if (menuItem != null) {
			menuItem.setSubMenu(openSubMenu);
			// 添加父菜单.
			if (openSubMenu != null) {
				openSubMenu.setParentMenu(OpenMenu.this);
			}
		}
		return openSubMenu;
	}

	/**
	 * 设置数据.
	 */
	@Override
	public String toString() {
		for (IOpenMenuItem item : mItems) {
			String title = item.getTitle().toString();
			OPENLOG.E("menu item:" + title);
			OpenSubMenu submenu = item.getSubMenu();
			if (submenu != null) {
				OPENLOG.E("=======sub menu======start start start start");
				submenu.toString();
				OPENLOG.E("=======sub menu======end end end end");
			}
		}
		return super.toString();
	}

	@Override
	public ArrayList<IOpenMenuItem> getMenuDatas() {
		return mItems;
	}

	@Override
	public IOpenMenu setMenuView(AbsListView absListView) {
		if (!(absListView instanceof AbsListView))
			throw new AssertionError("absListView is not AbsListView!!");
		this.mAbsListView = absListView;
		return this;
	}

	@Override
	public AbsListView getMenuView() {
		return this.mAbsListView;
	}

	@Override
	public IOpenMenu setMenuWidth(int width) {
		this.mMenuWidth = width;
		return this;
	}

	@Override
	public int getMenuWidth() {
		return this.mMenuWidth;
	}

	@Override
	public IOpenMenu setMenuHeight(int height) {
		this.mMenuHeight = height;
		return this;
	}

	@Override
	public int getMenuHeight() {
		return this.mMenuHeight;
	}

	@Override
	public IOpenMenu setLayoutID(int id) {
		this.mMenuItemLayoutID = id;
		return this;
	}

	@Override
	public int getLayoutID() {
		return this.mMenuItemLayoutID;
	}

	@Override
	public IOpenMenu setId(int id) {
		this.mId = id;
		return this;
	}

	@Override
	public int getId() {
		return this.mId;
	}

	@Override
	public IOpenMenu setGravity(int gravity) {
		this.mGravity = gravity;
		return this;
	}

	@Override
	public int getGravity() {
		return this.mGravity;
	}

	@Override
	public IOpenMenu setMenuLoadAnimation(LayoutAnimationController animation) {
		this.mLoadAnimation = animation;
		return this;
	}

	@Override
	public LayoutAnimationController getMenuLoadAnimation() {
		return this.mLoadAnimation;
	}

	@Override
	public IOpenMenu setMenuMargins(int left, int top, int right, int bottom) {
		setMenuMargins(new Rect(left, top, right, bottom));
		return this;
	}

	@Override
	public IOpenMenu setMenuMargins(Rect rect) {
		this.mMarginRect = rect;
		return this;
	}

	@Override
	public Rect getMargins() {
		return this.mMarginRect;
	}

	@Override
	public IOpenMenu setMenuShowAnimation(Animation animation) {
		this.mShowAnimation = animation;
		return this;
	}

	@Override
	public Animation getMenuShowAnimation() {
		return this.mShowAnimation;
	}

}
