package com.open.androidtvwidget.menu;

import android.graphics.drawable.Drawable;

public class OpenMenuItem implements IOpenMenuItem {

	private final int mId;
	// private final int mGroup;
	// private final int mCategoryOrder;
	// private final int mOrdering;
	private int mTextSize = DEFAULT_TEXT_SIZE;
	private Object mData;
	private CharSequence mTitle;
	private OpenMenu mMenu;
	private OpenSubMenu mSubMenu;
	private Drawable mIconDrawable;
	private int mIconResId;
	// private int mShowAsAction;

	OpenMenuItem(OpenMenu menu, int group, int id, int categoryOrder, int ordering, CharSequence title,
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
	public IOpenMenuItem setIcon(Drawable icon) {
		mIconResId = NO_ICON;
		mIconDrawable = icon;
		// mMenu.onItemsChanged(false);
		return this;
	}

	@Override
	public IOpenMenuItem setIcon(int iconResId) {
		mIconDrawable = null;
		mIconResId = iconResId;
		// mMenu.onItemsChanged(false);
		return this;
	}

	@Override
	public IOpenMenuItem setTitle(CharSequence title) {
		this.mTitle = title;
		return this;
	}

	@Override
	public IOpenMenuItem setTitle(int title) {
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
	public IOpenMenuItem setTextSize(int size) {
		mTextSize = size;
		return this;
	}

	@Override
	public int getTextSize() {
		return mTextSize;
	}

	@Override
	public IOpenMenuItem setObjectData(Object data) {
		this.mData = data;
		return this;
	}

	@Override
	public Object getObjectData() {
		return this.mData;
	}
	
}
