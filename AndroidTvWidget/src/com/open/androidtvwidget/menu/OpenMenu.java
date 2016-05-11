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

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.utils.OPENLOG;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 菜单.
 * 
 * @author hailongqiu
 *
 */
public class OpenMenu implements IOpenMenu {

	private static final String TAG = "OpenMenuBuilder";

	private ArrayList<OpenMenuItemImpl> mItems;
	private Context mContext;
	private Resources mResources;

	// 菜单视图.(暂时测试)
	OpenMenuView mMenuView;
	LayoutAnimationController mLayoutAnimationController;
	MenuAdapter mAdapter;
	LayoutInflater mInflater;
	
	//
	private int mTextSize = OpenMenuItemImpl.DEFAULT_TEXT_SIZE;
	
	public OpenMenu(Context context, int width, int height) {
		init(context);
		// 将菜单添加到root布局上.
		attach2Window(context, width, height);
	}
	
	public OpenMenu(Context context, int width) {
		init(context);
		// 将菜单添加到root布局上.
		attach2Window(context, width, ViewGroup.LayoutParams.MATCH_PARENT);
	}
	
	public OpenMenu(Context context) {
		this(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	}
	
	private void attach2Window(Context context, int width, int height) {
		ViewGroup rootView = (ViewGroup) ((Activity)context).findViewById(Window.ID_ANDROID_CONTENT);
		View view = (View) getMenuView();
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
		view.setVisibility(View.GONE);
		rootView.addView(view, layoutParams);
	}
	
	public void showMenu() {
		View view = (View) getMenuView();
		view.setVisibility(View.VISIBLE);
		((OpenMenuView)getMenuView()).getMenuListView().setFocusable(true);
		((OpenMenuView)getMenuView()).getMenuListView().requestFocus();
	}
	
	public boolean isShowMenu() {
		View view = (View) getMenuView();
		return (view.getVisibility() == View.VISIBLE);
	}
	/**
	 * 设置显示菜单动画.
	 */
	public void setShowMenuAnim() {
	}
	
	/**
	 * 设置隐藏菜单动画.
	 */
	public void setHideMenuAnim() {
	}
	
	public void hideMenu() {
		View view = (View) getMenuView();
		view.setVisibility(View.GONE);
	}
	
	private OpenMenu mParent;
	
	public void setParentMenu(OpenMenu openMenuBuilder) {
		mParent = openMenuBuilder;
	}

	public OpenMenu getParentMenu() {
		return this.mParent;
	}
	
	private void init(Context context) {
		this.mContext = context;
		if (this.mContext != null) {
			this.mResources = context.getResources();
			this.mItems = new ArrayList<OpenMenuItemImpl>();
			// 菜单视图 (暂时测试)
			this.mInflater = LayoutInflater.from(mContext);
		}
	}

	Resources getResources() {
		return mResources;
	}

	Context getContext() {
		return mContext;
	}

	private int mDefaultShowAsAction = 0;
	
	@Override
	public OpenMenu setTextSize(int size) {
		this.mTextSize = size;
		return this;
	}
	
	public OpenMenuItem addInternal(int groupId, int itemId, int order, CharSequence title) {
		final int ordering = order;
		final OpenMenuItemImpl item = new OpenMenuItemImpl(this, groupId, itemId, order, ordering, title,
				mDefaultShowAsAction);
		item.setTextSize(mTextSize);
		mItems.add(item);
		return item;
	}

	@Override
	public OpenMenuItem add(int groupId, int itemId, int order, CharSequence title) {
		return addInternal(groupId, itemId, order, title);
	}

	@Override
	public OpenMenuItem add(int groupId, int itemId, int order, int titleRes) {
		return addInternal(groupId, itemId, order, this.mResources.getString(titleRes));
	}

	@Override
	public OpenMenuItem add(CharSequence title) {
		return addInternal(0, 0, 0, title);
	}

	/**
	 * 添加子菜单.
	 */
	@Override
	public OpenSubMenu addSubMenu(int pos, OpenSubMenu openSubMenu) {
		mItems.get(pos).setSubMenu(openSubMenu);
		// 添加父菜单.
		if (openSubMenu != null) {
			openSubMenu.setParentMenu(OpenMenu.this);
		}
		return openSubMenu;
	}

	/**
	 * 菜单展开动画.
	 */
	@Override
	public IOpenMenu setLayoutAnimation(LayoutAnimationController layoutAnimationController) {
		this.mLayoutAnimationController = layoutAnimationController;
		if (mMenuView != null && mLayoutAnimationController != null) {
			ListView listview = mMenuView.getMenuListView();
			if (listview != null)
				listview.setLayoutAnimation(layoutAnimationController);
		}
		return this;
	}
	
	private int mGravity = Gravity.CENTER_VERTICAL;
	private int mLeftPadding = 0;
	
	@Override
	public OpenMenu setGravity(int rule) {
		mGravity = rule;
		ListView listView = ((OpenMenuView)getMenuView()).getMenuListView();
		RelativeLayout.LayoutParams layPar = (RelativeLayout.LayoutParams) listView.getLayoutParams();
		layPar.addRule(rule);
		listView.requestFocus();
		listView.setLayoutParams(layPar);
		return this;
	}
	
	@Override
	public int getGravity() {
		return this.mGravity;
	}
	
	@Override
	public OpenMenu setLeftPadding(int leftPadding) {
		this.mLeftPadding = leftPadding;
		return this;
	}
	
	@Override
	public int getLeftPadding() {
		return this.mLeftPadding;
	}
	
	public void setMenuWidth(int width) {
	}
	
	// 菜单视图(暂时测试放这里)
	
	public void setBackgroundResource(int resID) {
		mMenuView.setBackgroundResource(resID);
	}
	
	private void showSubMenuView() {
		
	}
	
	public OpenMenuView getMenuView() {
		// 多个listview---主菜单--子菜单（无限个)
		if (mMenuView == null) {
			mMenuView = new OpenMenuView(mContext);
			mMenuView.getMenuListView().setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// 是否有子菜单，则显示子菜单.
					if (mItems.get(position).hasSubMenu()) {
						// test.
						FrameLayout.LayoutParams mainLayoutParams = (LayoutParams) mMenuView.getLayoutParams();
						OpenMenu subMenu = (OpenMenu) mItems.get(position).getSubMenu();
						OpenMenuView menuView = (OpenMenuView) subMenu.getMenuView();
						FrameLayout.LayoutParams layPar = (LayoutParams) menuView.getLayoutParams();
						layPar.leftMargin = mainLayoutParams.leftMargin + mMenuView.getMeasuredWidth() + getLeftPadding();
						subMenu.showMenu();
						//
						Toast.makeText(mContext, "子菜单" + menuView, Toast.LENGTH_LONG).show();
					}
				}
			});
			if (mLayoutAnimationController != null) {
				mMenuView.setLayoutAnimation(mLayoutAnimationController);
			}
			if (mAdapter == null) {
				mAdapter = new MenuAdapter();
			}
			mMenuView.getMenuListView().setAdapter(mAdapter);
			// mMenuView.setOnItemClickListener(this);
		}
		return mMenuView;
	}
	
	private class MenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public OpenMenuItemImpl getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(mMenuItemLayoutID, parent, false);
			}
			IOpenMenuView.ItemView itemView = (IOpenMenuView.ItemView) convertView;
			itemView.initialize(getItem(position), 0);
			return convertView;
		}

	}

	@Override
	public String toString() {
		for (OpenMenuItem item : mItems) {
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

	private int mMenuItemLayoutID = R.layout.list_menu_item_layout;

	/**
	 * 修改菜单item布局. <br>
	 * 修改的布局ID必需和 <br>
	 * list_menu_item_layout 中的名字一样.
	 */
	public void setMenuItemLayoutResId(int resId) {
		mMenuItemLayoutID = resId;
		if (this.mAdapter != null) {
			this.mAdapter.notifyDataSetChanged();
		}
	}

}
