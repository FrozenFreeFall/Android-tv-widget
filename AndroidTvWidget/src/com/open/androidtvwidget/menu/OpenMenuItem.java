package com.open.androidtvwidget.menu;

import android.graphics.drawable.Drawable;

/**
 * 菜单接口.
 * @author hailongqiu
 */
public interface OpenMenuItem {
	public Drawable getIcon();
	/**
	 * 设置菜单图标.
	 */
	public OpenMenuItem setIcon(Drawable icon);
	public OpenMenuItem setIcon(int iconResId);
	public OpenMenuItem setTitle(CharSequence title);
	public OpenMenuItem setTitle(int title);
	public OpenMenuItem setTextSize(int size);
	public int getTextSize();
	/**
	 * 获取菜单文字内容.
	 */
	public CharSequence getTitle();
	public OpenSubMenu getSubMenu();
	/**
	 * 保存子菜单.
	 */
	public void setSubMenu(OpenSubMenu subMenu);
	/**
	 * 判断子菜单是否存在.
	 */
	public boolean hasSubMenu(); 
}
