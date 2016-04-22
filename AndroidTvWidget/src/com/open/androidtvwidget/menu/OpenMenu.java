package com.open.androidtvwidget.menu;

import android.view.animation.LayoutAnimationController;

public interface OpenMenu {
	static final int NONE = 0;

	public OpenMenuItem add(CharSequence title);
	public OpenMenuItem add(int groupId, int itemId, int order, CharSequence title);
	public OpenMenuItem add(int groupId, int itemId, int order, int titleRes);
	public OpenSubMenu addSubMenu(int pos, OpenSubMenu openSubMenu);
	// https://github.com/nhaarman/ListViewAnimations 菜单动画.
	public OpenMenu setLayoutAnimation(LayoutAnimationController layoutAnimationController);
	//
	public OpenMenuBuilder setGravity(int gravity);
	public int getGravity();
	public OpenMenuBuilder setLeftPadding(int leftPadding);
	public int getLeftPadding();
	public OpenMenuBuilder setTextSize(int size);
}
