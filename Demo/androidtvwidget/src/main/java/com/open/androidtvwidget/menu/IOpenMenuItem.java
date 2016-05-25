package com.open.androidtvwidget.menu;

import android.graphics.drawable.Drawable;

/**
 * 菜单item接口.
 * 
 * @author hailongqiu
 */
public interface IOpenMenuItem {
	static final int NO_ICON = 0;
	static final int DEFAULT_TEXT_SIZE = 24;

	/**
	 * 设置菜单item-ID.
	 */
	public IOpenMenuItem setId(int id);

	public int getId();

	/**
	 * 设置菜单图标.
	 */
	public IOpenMenuItem setIcon(Drawable icon);

	public Drawable getIcon();

	/**
	 * 设置菜单文本内容
	 */
	public IOpenMenuItem setTitle(CharSequence title);

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

	/**
	 * 保存子菜单.
	 */
	public IOpenMenuItem setSubMenu(OpenSubMenu subMenu);

	public OpenSubMenu getSubMenu();

	/**
	 * 判断子菜单是否存在.
	 */
	public boolean hasSubMenu();
}
