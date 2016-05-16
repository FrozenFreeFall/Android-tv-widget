package com.open.androidtvwidget.menu;

import android.graphics.drawable.Drawable;

/**
 * 菜单接口.
 * @author hailongqiu
 */
public interface IOpenMenuItem {
	static final int NO_ICON = 0;
	static final int DEFAULT_TEXT_SIZE = 24;
	
	//
	public Drawable getIcon();
	/**
	 * 设置菜单图标.
	 */
	public IOpenMenuItem setIcon(Drawable icon);
	public IOpenMenuItem setIcon(int iconResId);
	/**
	 * 设置菜单文本内容
	 */
	public IOpenMenuItem setTitle(CharSequence title);
	public IOpenMenuItem setTitle(int title);
	public CharSequence getTitle();
	/**
	 * 设置字体大小.
	 */
	public IOpenMenuItem setTextSize(int size);
	public int getTextSize();
	/**
	 * 设置数据.
	 */
	public IOpenMenuItem setObjectData(Object data);
	public Object getObjectData();
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
