package com.open.demo;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.menu.IOpenMenu;
import com.open.androidtvwidget.menu.IOpenMenuItem;
import com.open.androidtvwidget.menu.IOpenMenuView;
import com.open.androidtvwidget.menu.OpenMenu;
import com.open.androidtvwidget.menu.OpenSubMenu;
import com.open.androidtvwidget.utils.GenerateViewId;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.view.MainUpView;

import java.util.ArrayList;

/**
 * ListView </br>
 * android:divider="#00000000" </br>
 * android:scrollbars="none" </br>
 * <p/>
 * 菜单的显示窗口.
 *
 * @author hailongqiu
 */
public class OpenMenuView implements IOpenMenuView, OnKeyListener, OnItemSelectedListener, OnItemClickListener {

    private static final int DEFUALT_MENU_WIDTH = 200;
    private static final float DEFAULT_SCALE = 1.0f;
    //
    private Context mContext;
    private boolean isRemoveFloatLat = false;
    private float mScaleX = DEFAULT_SCALE;
    private float mScaleY = DEFAULT_SCALE;
    // 定义浮动窗口布局
    private View mMainMenuView;
    private LinearLayout mFloatLayout;
    private LayoutParams mWmParams;
    // 创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    private LayoutInflater mInflater;
    private OnMenuListener mOnMenuListener;
    // 移动边框.
    private MainUpView mMainUpView;
    private View mOldView;

    public OpenMenuView(Context context) {
        mContext = context;
        if (mContext == null)
            throw new AssertionError("你麻痹，你能将Context传正确么？都Null... ...");
        mInflater = LayoutInflater.from(mContext);
        initMenuWindow();
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
        // 获取浮动窗口视图所在布局
        initMenuChildView();
    }

    private void initMenuChildView() {
        mMainMenuView = mInflater.inflate(R.layout.open_menu_view, null);
        //
        mFloatLayout = (LinearLayout) mMainMenuView.findViewById(R.id.main_menu_lay);
        mMainUpView = (MainUpView) mMainMenuView.findViewById(R.id.main_up_view);
        // 添加mFloatLayout
        mWindowManager.addView(mMainMenuView, mWmParams);
        //
        EffectNoDrawBridge effectNoDrawBridge = new EffectNoDrawBridge();
        effectNoDrawBridge.setTranDurAnimTime(200);
        mMainUpView.setEffectBridge(effectNoDrawBridge); // 4.3以下版本边框移动.
        mMainUpView.setUpRectResource(R.drawable.white_light_10);
    }

    /*
     * 设置菜单数据.
     */
    @Override
    public IOpenMenuView setMenuData(OpenMenu openMenu) {
        if (isRemoveFloatLat) {
            initMenuChildView();
            isRemoveFloatLat = false;
        }
        setMenuDataInternal(openMenu);
        return this;
    }

    @SuppressWarnings("ResourceType")
    private AbsListView getMenuView(OpenMenu openMenu, ArrayList<IOpenMenuItem> items) {
        AbsListView absListView = openMenu.getMenuView();
        if (absListView == null) {
            absListView = new ListView(mContext);
        }
        final AbsListView tempAbsListView = absListView;
        // 设置ID.
        int id = openMenu.getId();
        absListView.setId(id != 0 ? id : GenerateViewId.getSingleton().generateViewId());
        // 设置菜单view动画.
        LayoutAnimationController animController = openMenu.getMenuLoadAnimation();
        if (animController != null)
            absListView.setLayoutAnimation(animController);
        // 设置属性
        if (absListView instanceof ListView) {
            ListView lv = (ListView) absListView;
            lv.setDividerHeight(0);
            lv.setCacheColorHint(0);
            lv.setDivider(null);
            lv.setSelector(R.drawable.nocolor);
        }
        // 设置 adpater.
        absListView.setAdapter(new MenuAdpater(openMenu, items));
        // 设置事件
        absListView.setOnKeyListener(this);
        absListView.setOnItemSelectedListener(this);
        absListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                View view = tempAbsListView.getSelectedView();
                mMainUpView.setFocusView(view, mScaleX, mScaleY);
                mMainUpView.setUnFocusView(mOldView);
                mOldView = view;
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

    @SuppressWarnings("WrongConstant")
    private void setMenuDataInternal(OpenMenu openMenu) {
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
     * 移除悬浮窗口的布局.
     */
    private void removeFloatLyaout() {
        if (mFloatLayout != null) {
            mFloatLayout.removeAllViews();
            mWindowManager.removeView(mMainMenuView);
            isRemoveFloatLat = true;
        }
    }

    /**
     * 菜单Menu item adpater.
     *
     * @author hailongqiu
     */
    class MenuAdpater extends BaseAdapter {

        private ArrayList<IOpenMenuItem> mItems;
        private IOpenMenu mOpenMenu;

        public MenuAdpater(IOpenMenu openMenu, ArrayList<IOpenMenuItem> items) {
            this.mOpenMenu = openMenu;
            this.mItems = items;
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
            itemView.initialize(getItem(position), 0);
            return convertView;
        }

    }

    /**
     * 删除菜单.
     */
    private boolean removeMenu(View v) {
        if (mFloatLayout.getChildCount() > 1) {
            mFloatLayout.removeView(v);
            mFloatLayout.requestLayout();
            mFloatLayout.getChildAt(mFloatLayout.getChildCount() - 1).setFocusable(true);
            mFloatLayout.getChildAt(mFloatLayout.getChildCount() - 1).requestFocus();
        } else {
            removeFloatLyaout();
        }
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    removeMenu(v);
                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:// 防止菜单往左边跑到其它地方.
                    // 如果为listview，左边.就消失.
                    if ((v instanceof ListView)) {
                        removeMenu(v);
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

    /**
     * 获取菜单的最顶层的移动边框.
     */
    public MainUpView getMainUpView() {
        return this.mMainUpView;
    }

    public OpenMenuView setScale(float scaleX, float scaleY) {
        this.mScaleX = scaleX;
        this.mScaleY = scaleY;
        return this;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // 移动边框.
        mMainUpView.setFocusView(view, this.mScaleX, this.mScaleY);
        mMainUpView.setUnFocusView(mOldView);
        mOldView = view;
        // 菜单item选中事件触发.
        if (mOnMenuListener != null) {
            if (mOnMenuListener.onMenuItemSelected(parent, view, position, id))
                return;
        }
        // 移除之前的菜单.(bug:修复鼠标单击)
        if (removeMenuView(parent, position))
            return;
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
            if (mOnMenuListener.onMenuItemClick(parent, view, position, id))
                return;
        }
        // 移除之前的菜单.(bug:修复鼠标单击)
        if (removeMenuView(parent, position))
            return;
        // 显示菜单.
        initMenuView(parent, position);
    }

    /**
     * 在显示菜单之前，先移除前面的菜单，为了不显示多个菜单. (bug:修复鼠标单击)
     */
    private boolean removeMenuView(AdapterView<?> parent, int position) {
        int count = mFloatLayout.getChildCount();
        boolean isRemove = false;
        int currentPos = 0;
        // 查找控件位置.
        for (int i = 0; i < count; i++) {
            View v = mFloatLayout.getChildAt(i);
            if (v.equals(parent)) {
                isRemove = true;
                currentPos = i;
                break;
            }
        }
        for (int i = currentPos + 1; i < count; i++) {
            if (isRemove) {
                mFloatLayout.removeViewAt(currentPos + 1);
            }
        }
        if (currentPos == (count - 1)) {
            isRemove = false;
        }
        return isRemove;
    }

    private void initMenuView(AdapterView<?> parent, int position) {
        ArrayList<IOpenMenuItem> items = getMenuItems(parent);
        IOpenMenuItem menuItem = items.get(position);
        if (menuItem != null && menuItem.hasSubMenu()) {
            OpenSubMenu subMenu = menuItem.getSubMenu();
            if (subMenu != null) {
                setMenuData(subMenu);
            }
        }
    }

    @Override
    public IOpenMenuView setOnMenuListener(OnMenuListener cb) {
        this.mOnMenuListener = cb;
        return this;
    }

}
