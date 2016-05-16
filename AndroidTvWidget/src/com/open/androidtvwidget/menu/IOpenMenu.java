package com.open.androidtvwidget.menu;

import android.view.animation.LayoutAnimationController;

public interface IOpenMenu {
	static final int NONE = 0;

	public IOpenMenuItem add(CharSequence title);
	public IOpenMenuItem add(int groupId, int itemId, int order, CharSequence title);
	public IOpenMenuItem add(int groupId, int itemId, int order, int titleRes);
	public OpenSubMenu addSubMenu(int pos, OpenSubMenu openSubMenu);
	// https://github.com/nhaarman/ListViewAnimations 菜单动画.
	public IOpenMenu setLayoutAnimation(LayoutAnimationController layoutAnimationController);
	//
	public OpenMenu setGravity(int gravity);
	public int getGravity();
	public OpenMenu setLeftPadding(int leftPadding);
	public int getLeftPadding();
	public OpenMenu setTextSize(int size); // 全局设置菜单字体.
}
