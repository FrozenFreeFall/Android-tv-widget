package com.open.androidtvwidget.menu;

import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;

/**
 * 菜单item接口.
 *
 * @author hailongqiu
 */
public interface IOpenMenuItem {
    static final int NO_ICON = 0; // 默认icon id.
    static final int DEFAULT_TEXT_SIZE = 24; // 默认字体

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
    public IOpenMenuItem setSubMenu(IOpenMenu subMenu);

    public IOpenMenu getSubMenu();

    /**
     * 判断子菜单是否存在.
     */
    public boolean hasSubMenu();

    /**
     * 设置checked标志位.
     */
    public IOpenMenuItem setChecked(boolean checked);

    public boolean isChecked();

    /**
     * 设置 RadioButton或 CheckBox的view.
     */
    public IOpenMenuItem setCheckedView(CompoundButton compoundButton);

    public CompoundButton getCheckedView();
}
