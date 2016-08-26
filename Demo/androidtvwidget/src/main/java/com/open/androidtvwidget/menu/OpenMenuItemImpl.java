package com.open.androidtvwidget.menu;

public class OpenMenuItemImpl implements OpenMenuItem {

    private int mIconID; // icon 资源 id.
    private int mId; // item id.
    private int mTextSize = DEFAULT_TEXT_SIZE; // item 文本大小.
    private boolean mChecked = false; // item 勾选状态.
    private boolean mIsShowSubMenu = false; // 是否显示子菜单.
    private Object mData;
    private CharSequence mTitle; // item 文本.

    private OpenMenu mMenu; // 保存 item的父菜单.
    private OpenMenu mSubMenu; // 保存 子菜单.


    OpenMenuItemImpl(OpenMenu menu, int id, CharSequence title) {
        this.mMenu = menu;
        this.mId = id;
        this.mTitle = title;
    }

    @Override
    public OpenMenuItem showSubMenu() {
        if (mSubMenu != null) {
            mSubMenu.showMenu();
        }
        this.mIsShowSubMenu = true;
        return this;
    }

    @Override
    public OpenMenuItem hideSubMenu() {
        if (mSubMenu != null) {
            mSubMenu.hideMenu();
        }
        this.mIsShowSubMenu = false;
        return this;
    }

    /**
     * 设置 图标资源id.
     */
    @Override
    public OpenMenuItem setIconRes(int iconID) {
        this.mIconID = iconID;
        notifyChanged();
        return this;
    }

    @Override
    public int getIconRes() {
        return this.mIconID;
    }

    @Override
    public OpenMenuItem setTitle(CharSequence title) {
        this.mTitle = title;
        notifyChanged();
        return this;
    }

    @Override
    public CharSequence getTitle() {
        return mTitle;
    }

    /**
     * 添加子菜单.
     */
    @Override
    public OpenMenuItem addSubMenu(OpenMenu openSubMenu) {
        this.mSubMenu = openSubMenu;
        this.mSubMenu.setParentMenu(mMenu); // 添加父菜单.
        return this;
    }

    @Override
    public OpenMenu getSubMenu() {
        return mSubMenu;
    }

    @Override
    public boolean hasSubMenu() {
        return (mSubMenu != null);
    }

    @Override
    public boolean isShowSubMenu() {
        return this.mIsShowSubMenu;
    }

    @Override
    public void setShowSubMenu(boolean isShow) {
        this.mIsShowSubMenu = isShow;
    }

    @Override
    public OpenMenuItem setChecked(boolean checked) {
        this.mChecked = checked;
        notifyChanged();
        return this;
    }

    @Override
    public boolean isChecked() {
        return this.mChecked;
    }

    @Override
    public OpenMenuItem setTextSize(int size) {
        mTextSize = size;
        notifyChanged();
        return this;
    }

    @Override
    public int getTextSize() {
        return mTextSize;
    }

    @Override
    public OpenMenuItem setObjectData(Object data) {
        this.mData = data;
        notifyChanged();
        return this;
    }

    @Override
    public Object getObjectData() {
        return this.mData;
    }

    @Override
    public OpenMenuItem setId(int id) {
        this.mId = id;
        return this;
    }

    @Override
    public int getId() {
        return this.mId;
    }

    /**
     * 数据更新.
     */

    private void notifyChanged() {
        if (mMenu != null) {
            mMenu.notifyChanged();
        }
    }

    public OpenMenu getMenu() {
        return this.mMenu;
    }

    @Override
    public void setMenu(OpenMenu menu) {
        this.mMenu = menu;
    }

}
