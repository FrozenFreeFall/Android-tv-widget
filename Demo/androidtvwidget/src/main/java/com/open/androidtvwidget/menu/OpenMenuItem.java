package com.open.androidtvwidget.menu;

import android.graphics.drawable.Drawable;

public class OpenMenuItem implements IOpenMenuItem {

	private int mId;
	private int mTextSize = DEFAULT_TEXT_SIZE;
	private Object mData;
	private CharSequence mTitle;
	private OpenMenu mMenu;
	private OpenSubMenu mSubMenu;
	private Drawable mIconDrawable;
	
	OpenMenuItem(OpenMenu menu, int id, CharSequence title) {
		this.mMenu = menu;
		this.mId = id;
		this.mTitle = title;
	}

	@Override
	public Drawable getIcon() {
		return mIconDrawable;
	}

	@Override
	public IOpenMenuItem setIcon(Drawable icon) {
		mIconDrawable = icon;
		return this;
	}

	@Override
	public IOpenMenuItem setTitle(CharSequence title) {
		this.mTitle = title;
		return this;
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
	public IOpenMenuItem setSubMenu(OpenSubMenu subMenu) {
        mSubMenu = subMenu;
        return this;
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

	@Override
	public IOpenMenuItem setId(int id) {
		this.mId = id;
		return this;
	}

	@Override
	public int getId() {
		return this.mId;
	}
	
}
