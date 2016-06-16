package com.open.androidtvwidget.menu;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.utils.GenerateViewId;
import com.open.androidtvwidget.utils.OPENLOG;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView </br>
 * android:divider="#00000000" </br>
 * android:scrollbars="none" </br>
 * <p/>
 * 菜单的显示窗口.
 *
 * @author hailongqiu
 */
@SuppressWarnings("WrongConstant")
public class OpenMenuView implements IOpenMenuView, OnKeyListener, OnItemSelectedListener, OnItemClickListener {

    private static final int DEFUALT_MENU_WIDTH = 200;
    //
    private Context mContext;
    private boolean isRemoveFloatLat = true;
    private boolean isInitWindowMenu = false;
    // 定义浮动窗口布局
    private View mMainMenuView;
    private LinearLayout mFloatLayout;
    private FrameLayout mMainMenuFlay;
    private View mMoveView; // 用于保存移动VIEW.
    private LayoutParams mWmParams;
    // 创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    private LayoutInflater mInflater;
    private OnMenuListener mOnMenuListener;

    public OpenMenuView(Context context) {
        mContext = context;
        if (mContext == null)
            throw new AssertionError("context is null");
        mInflater = LayoutInflater.from(mContext);
    }

    private void initMenuWindow() {
        mWmParams = new LayoutParams();
        // 获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        // 设置window type
        mWmParams.type = LayoutParams.TYPE_PHONE;
        // 设置图片格式，效果为背景透明
        mWmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        // FLAG_NOT_TOUCH_MODAL 不阻塞事件传递到后面的窗口
        mWmParams.flags = LayoutParams.FLAG_ALT_FOCUSABLE_IM; // |
        // WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        // //LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        // mWmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mWmParams.x = 0;
        mWmParams.y = 0;
        // 设置悬浮窗口长宽数据
        mWmParams.width = LayoutParams.MATCH_PARENT;
        mWmParams.height = LayoutParams.MATCH_PARENT;
    }

    private void initMenuChildView() {
        mMainMenuView = mInflater.inflate(R.layout.open_menu_view, null);
        mFloatLayout = (LinearLayout) mMainMenuView.findViewById(R.id.main_menu_lay); // 用于插入菜单view(listview或者gridview)
        mMainMenuFlay = (FrameLayout) mMainMenuView.findViewById(R.id.main_menu_flay); // 用于插入移动边框.
        // 添加整个菜单的view.
        mWindowManager.addView(mMainMenuView, mWmParams);
        // 添加移动边框.
        addMoveView();
    }

    /**
     * 添加移动边框.
     */
    private void addMoveView() {
        if (mMoveView != null) {
            int count = mMainMenuFlay.getChildCount();
            if (count >= 2) {
                mMainMenuFlay.removeViewAt(count - 1);
            }
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.width = 0;
            layoutParams.height = 0;
            mMainMenuFlay.addView(mMoveView, layoutParams);
            mMainMenuFlay.requestLayout();
        }
    }

    /*
     * 设置菜单数据.
     */
    @Override
    public IOpenMenuView setMenuData(IOpenMenu openMenu) {
        openMenu.registerDataSetObserver(mMenuSetObserver); // 注册--注册者(全局，责任链返回).
        return this;
    }

    @SuppressWarnings("ResourceType")
    private AbsListView getMenuView(IOpenMenu openMenu, ArrayList<IOpenMenuItem> items) {
        AbsListView absListView = openMenu.getMenuView();
        if (absListView == null) {
            // absListView 为null，设置默认.
            absListView = new ListView(mContext);
            // 设置属性(默认)
            if (absListView instanceof ListView) {
                ListView lv = (ListView) absListView;
                lv.setDividerHeight(0);
                lv.setCacheColorHint(0);
                lv.setDivider(null);
                lv.setSelector(R.drawable.nocolor);
            }
        }
        final AbsListView tempAbsListView = absListView;
        // 设置ID.
        int id = openMenu.getId();
        absListView.setId(id != 0 ? id : GenerateViewId.getSingleton().generateViewId());
        // 设置菜单view动画.
        LayoutAnimationController animController = openMenu.getMenuLoadAnimation();
        if (animController != null)
            absListView.setLayoutAnimation(animController);
        // 设置 adpater.
        absListView.setAdapter(new MenuAdpater(openMenu, items));
        // 设置事件
        absListView.setOnKeyListener(this);
        absListView.setOnItemSelectedListener(this);
        absListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mOnMenuListener != null) {
                    View view = tempAbsListView.getSelectedView();
                    mOnMenuListener.onMenuItemFocusChange(tempAbsListView, view);
                }
            }
        });
        absListView.setOnItemClickListener(this);
        // 保持菜单view.
        if (openMenu.getMenuView() == null)
            openMenu.setMenuView(absListView);
        // 设置属性.
        absListView.setFocusable(true);
        absListView.setFocusableInTouchMode(true);
        absListView.requestFocus();
        return absListView;
    }

    MenuSetObserver mMenuSetObserver = new MenuSetObserver() {
        @Override
        public void onShow(IOpenMenu openMenu) {
            OPENLOG.D("====onShow====");
            // 显示菜单.
            setMenuDataInternal(openMenu);
        }

        @Override
        public void onHide(IOpenMenu openMenu) {
            OPENLOG.D("====onHide====");
            // 删除菜单.
            hideMenu(openMenu);
        }

        @Override
        public void onChanged(IOpenMenu openMenu) {
            OPENLOG.D("====onChanged====");
            // 更新菜单界面的数据.
            AbsListView absListView = openMenu.getMenuView();
            if (absListView != null) {
                MenuAdpater adpater = (MenuAdpater) absListView.getAdapter();
                if (adpater != null)
                    adpater.notifyDataSetChanged();
            }
        }

    };

    /**
     * 设置隐藏菜单.
     */
    private void hideMenu(IOpenMenu openMenu) {
        AbsListView absListView = openMenu.getMenuView();
        hideMenu(absListView);
    }

    /**
     * 设置隐藏菜单.
     */
    private void hideMenu(final AbsListView absListView) {
        if (absListView == null)
            return;
        if (setAbsListViewHideAnimation(absListView, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMenuHideInternal(absListView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        })) {
            OPENLOG.D("hideMenu Animation run... ...");
            return;
        }
        setMenuHideInternal(absListView);
    }

    private void setMenuHideInternal(final AbsListView absListView) {
        int count = mFloatLayout.getChildCount();
        int saveIndex = -1;
        // 删除前面的菜单.
        for (int i = 0; i < count; i++) {
            AbsListView v = (AbsListView) mFloatLayout.getChildAt(i);
            if (v.equals(absListView)) {
                saveIndex = i;
                continue;
            } else if ((saveIndex != -1) && ((saveIndex + 1) == i)) {
                AbsListView absListView1 = (AbsListView) mFloatLayout.getChildAt(i);
                MenuAdpater menuAdpater = (MenuAdpater) absListView1.getAdapter();
                IOpenMenu openMenu = menuAdpater.getOpenMenu();
                openMenu.hideMenu();
                break;
            }
        }
        // 删除自身
        mFloatLayout.removeView(absListView);
        mFloatLayout.requestLayout();
        View v = mFloatLayout.getChildAt(mFloatLayout.getChildCount() - 1);
        if (v != null) {
            v.setFocusable(true);
            v.requestFocus();
        }
        // 判断是否已经没有了菜单.
        count = mFloatLayout.getChildCount();
        if (count == 0 && !isRemoveFloatLat) {
            // 删除移动边框.
            if (mMoveView != null) {
                mMainMenuFlay.removeView(mMoveView);
            }
            // 删除多余的菜单view.(不知道有没用)
            mFloatLayout.removeAllViews();
            // 删除菜单主view.
            mWindowManager.removeView(mMainMenuView);
            isRemoveFloatLat = true;
        }
    }

    /**
     * 获取菜单的隐藏动画并设置动画.
     */
    private boolean setAbsListViewHideAnimation(AbsListView absListView, final Animation.AnimationListener cb) {
        MenuAdpater adpater = (MenuAdpater) absListView.getAdapter();
        IOpenMenu openMenu = adpater.getOpenMenu();
        Animation hideAnimation = openMenu.getMenuHideAnimation();
        if (hideAnimation != null) {
            absListView.clearAnimation(); // 清除动画.
            hideAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    cb.onAnimationEnd(animation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            absListView.startAnimation(hideAnimation);
            return true;
        }
        return false;
    }

    /**
     * 设置显示菜单.
     */
    private void setMenuDataInternal(IOpenMenu openMenu) {
        /**
         * 如果为第一次显示出来，
         * 需要进行初始化.
         */
        if (isRemoveFloatLat) {
            if (!isInitWindowMenu) {
                initMenuWindow();
                isInitWindowMenu = true;
            }
            // 获取浮动窗口视图所在布局
            initMenuChildView();
            isRemoveFloatLat = false;
        }
        ArrayList<IOpenMenuItem> items = openMenu.getMenuDatas();
        if (items != null) {
            // 获取自定义的absListView.
            AbsListView absListView = getMenuView(openMenu, items);
            // 设置菜单宽度.
            int defulat = LinearLayout.LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams parm = new LinearLayout.LayoutParams(defulat, defulat);
            int width = openMenu.getMenuWidth();
            int height = openMenu.getMenuHeight();
            parm.width = ((width == 0) ? DEFUALT_MENU_WIDTH : width);
            parm.height = ((height == 0) ? LayoutParams.WRAP_CONTENT : height);
            // 设置菜单 Gravity.
            int gravity = openMenu.getGravity();
            parm.gravity = gravity;
            // 设置菜单view--Margin rect;
            Rect rect = openMenu.getMargins();
            if (rect != null) {
                parm.setMargins(rect.left, rect.top, rect.right, rect.bottom);
            }
            // 添加菜单View到FloatLayout.
            mFloatLayout.addView(absListView, parm);
            mFloatLayout.requestLayout();
            // 设置显示动画.
            Animation showAnimation = openMenu.getMenuShowAnimation();
            if (showAnimation != null)
                absListView.startAnimation(showAnimation);
        }
    }

    /**
     * 菜单Menu item adpater.
     *
     * @author hailongqiu
     */
    public class MenuAdpater extends BaseAdapter {

        private ArrayList<IOpenMenuItem> mItems;
        private IOpenMenu mOpenMenu;

        public MenuAdpater(IOpenMenu openMenu, ArrayList<IOpenMenuItem> items) {
            this.mOpenMenu = openMenu;
            this.mItems = items;
        }

        public IOpenMenu getOpenMenu() {
            return this.mOpenMenu;
        }

        public void setDatas(ArrayList<IOpenMenuItem> items) {
            mItems = items;
            notifyDataSetChanged();
        }

        public ArrayList<IOpenMenuItem> getDatas() {
            return mItems;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public IOpenMenuItem getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(mOpenMenu.getLayoutID(), parent, false);
            }
            ItemView itemView = (ItemView) convertView;
            itemView.initialize(getItem(position));
            return convertView;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            OPENLOG.D("====onKey====keyCode:" + keyCode + " event:" + event);
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    // 删除菜单.
                    hideMenu((AbsListView) v);
                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:// 防止菜单往左边跑到其它地方.
                    // 如果为listview，左边.就消失.
                    if ((v instanceof ListView)) {
                        // 删除菜单.(不一定)
                        hideMenu((AbsListView) v);
                        return true;
                    }
                case KeyEvent.KEYCODE_DPAD_RIGHT: // 防止菜单往右边跑到其它地方.
                case KeyEvent.KEYCODE_DPAD_UP: // 防止菜单往上面跑到其它地方.
                case KeyEvent.KEYCODE_DPAD_DOWN: // 防止菜单往下面跑到其它地方.
                    v.onKeyDown(keyCode, event);
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // 菜单item选中事件触发.
        if (mOnMenuListener != null) {
            IOpenMenuItem menuItem = getAdapterOpenMenuItem(parent, position);
            if (mOnMenuListener.onMenuItemSelected(parent, menuItem, view, position, id))
                return;
        }
        // 删除前面的菜单.(bug:修复鼠标单击)
        if (removeItemMenuFront(parent, position)) {
            return;
        }
        // 显示菜单.
        initMenuView(parent, position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * 菜单子菜单，要么就是菜单事件单击.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 菜单item单击事件触发.
        if (mOnMenuListener != null) {
            IOpenMenuItem menuItem = getAdapterOpenMenuItem(parent, position);
            if (mOnMenuListener.onMenuItemClick(parent, menuItem, view, position, id))
                return;
        }
        // 勾选选中设置.
        setMenuItemChecked(parent, position);
        // 删除前面的菜单.(bug:修复鼠标单击)
        if (removeItemMenuFront(parent, position)) {
            return;
        }
        // 显示菜单.
        initMenuView(parent, position);
    }

    /**
     * 从Adapter中获取菜单item的IOpenMenuItem.
     */
    private IOpenMenuItem getAdapterOpenMenuItem(AdapterView<?> parent, int position) {
        IOpenMenuItem menuItem = null;
        OpenMenuView.MenuAdpater menuAdpater = (OpenMenuView.MenuAdpater) parent.getAdapter();
        if (menuAdpater != null) {
            IOpenMenu openMenu = menuAdpater.getOpenMenu();
            if (openMenu != null) {
                List<IOpenMenuItem> items = openMenu.getMenuDatas();
                if (items != null && items.size() > 0) {
                    menuItem = items.get(position);
                }
            }
        }
        return menuItem;
    }

    /**
     * 设置菜单item-checked属性.
     */
    private void setMenuItemChecked(AdapterView<?> parent, int position) {
        IOpenMenu openMenu = ((MenuAdpater) parent.getAdapter()).getOpenMenu();
        CompoundButton compoundButton = openMenu.getCheckedView();
        if (compoundButton != null) {
            // radiobutton 其它设置为假.
            if (compoundButton instanceof RadioButton) { // radiobutton.
                for (IOpenMenuItem menuItem : openMenu.getMenuDatas()) {
                    menuItem.setChecked(false);
                }
            }
            // 勾选为true.
            List<IOpenMenuItem> items = openMenu.getMenuDatas();
            if (items == null)
                return;
            IOpenMenuItem openMenuItem = items.get(position);
            if (openMenuItem != null)
                openMenuItem.setChecked(true);
        }
    }

    /**
     * 删除前面的菜单.(bug:修复鼠标单击)
     */
    private boolean removeItemMenuFront(AdapterView<?> parent, int position) {
        int count = mFloatLayout.getChildCount();
        int currentPos = -1;
        // 查找控件位置.
        for (int i = 0; i < count; i++) {
            View v = mFloatLayout.getChildAt(i);
            if (v.equals(parent)) {
                currentPos = i;
                break;
            }
        }
        //
        if (currentPos == (count - 1)) {
            currentPos = -1;
        } else {
            AbsListView absListView = (AbsListView) mFloatLayout.getChildAt(currentPos + 1);
            hideMenu(absListView);
        }
        return currentPos != -1;
    }

    private void initMenuView(AdapterView<?> parent, int position) {
        ArrayList<IOpenMenuItem> items = getMenuItems(parent);
        IOpenMenuItem menuItem = items.get(position);
        if (menuItem != null && menuItem.hasSubMenu()) {
            IOpenMenu subMenu = menuItem.getSubMenu();
            if (subMenu != null) {
                setMenuDataInternal(subMenu);
            }
        }
    }

    private ArrayList<IOpenMenuItem> getMenuItems(AdapterView<?> parent) {
        MenuAdpater menuAdapter = (MenuAdpater) parent.getAdapter();
        if (menuAdapter == null) {
            OPENLOG.E("menuAdapter is null");
            return null;
        }
        ArrayList<IOpenMenuItem> items = menuAdapter.getDatas();
        if (items == null) {
            OPENLOG.E("items is null");
            return null;
        }
        return items;
    }

    @Override
    public IOpenMenuView setOnMenuListener(OnMenuListener cb) {
        this.mOnMenuListener = cb;
        return this;
    }

    @Override
    public IOpenMenuView setMoveView(View v) {
        mMoveView = v;
        return this;
    }

}
