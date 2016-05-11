package com.open.androidtvwidget.menu;

import android.graphics.drawable.Drawable;

public class OpenMenuItemImpl implements OpenMenuItem {

	static final int NO_ICON = 0;
	static final int DEFAULT_TEXT_SIZE = 24;
	private final int mId;
	// private final int mGroup;
	// private final int mCategoryOrder;
	// private final int mOrdering;
	private int mTextSize = DEFAULT_TEXT_SIZE;
	private CharSequence mTitle;
	private OpenMenu mMenu;
	private OpenSubMenu mSubMenu;
	private Drawable mIconDrawable;
	private int mIconResId;
	// private int mShowAsAction;

	OpenMenuItemImpl(OpenMenu menu, int group, int id, int categoryOrder, int ordering, CharSequence title,
			int showAsAction) {
		mMenu = menu;
		mId = id;
		// mGroup = group;
		// mCategoryOrder = categoryOrder;
		// mOrdering = ordering;
		mTitle = title;
		// mShowAsAction = showAsAction;
	}

	// public int getOrdering() {
	// return mOrdering;
	// }

	@Override
	public Drawable getIcon() {
		if (mIconDrawable != null)
			return mIconDrawable;
		if (mIconResId != NO_ICON) {
			Drawable icon = mMenu.getResources().getDrawable(mIconResId);
			mIconResId = NO_ICON;
			mIconDrawable = icon;
			return icon;
		}
		return null;
	}

	@Override
	public OpenMenuItem setIcon(Drawable icon) {
		mIconResId = NO_ICON;
		mIconDrawable = icon;
		// mMenu.onItemsChanged(false);
		return this;
	}

	@Override
	public OpenMenuItem setIcon(int iconResId) {
		mIconDrawable = null;
		mIconResId = iconResId;
		// mMenu.onItemsChanged(false);
		return this;
	}

	@Override
	public OpenMenuItem setTitle(CharSequence title) {
		this.mTitle = title;
		return this;
	}

	@Override
	public OpenMenuItem setTitle(int title) {
		return setTitle(mMenu.getContext().getString(title));
	}

	@Override
	public CharSequence getTitle() {
		return mTitle;
	}

	@Override
	public OpenSubMenu getSubMenu() {
		return mSubMenu;
	}
	
	@Override
	public void setSubMenu(OpenSubMenu subMenu) {
        mSubMenu = subMenu;
    }

	@Override
	public boolean hasSubMenu() {
		return (mSubMenu != null);
	}

	@Override
	public OpenMenuItem setTextSize(int size) {
		mTextSize = size;
		return this;
	}

	@Override
	public int getTextSize() {
		return mTextSize;
	}
	
}
