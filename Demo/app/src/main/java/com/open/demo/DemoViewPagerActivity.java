package com.open.demo;

import android.animation.Animator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.bridge.OpenEffectBridge;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.OpenTabHost;
import com.open.androidtvwidget.view.OpenTabHost.OnTabSelectListener;
import com.open.androidtvwidget.view.SmoothHorizontalScrollView;
import com.open.androidtvwidget.view.TextViewWithTTF;
import com.open.demo.adapter.OpenTabTitleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager demo：
 * 注意标题栏和viewpager的焦点控制.(在XML布局中控制了, ids)
 * 移动边框的问题也需要注意.
 * @author hailongqiu
 */
public class DemoViewPagerActivity extends Activity implements OnTabSelectListener {

    private List<View> viewList;// view数组
    private View view1, view2, view3, view4;
    ViewPager viewpager;
    OpenTabHost mOpenTabHost;
    OpenTabTitleAdapter mOpenTabTitleAdapter;
    // 移动边框.
    MainUpView mainUpView1;
    EffectNoDrawBridge mEffectNoDrawBridge;
    View mNewFocus;
    View mOldView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_viewpager_activity);
        //
        OPENLOG.initTag("hailongqiu", true); // 测试LOG输出.
        // 初始化标题栏.
        initAllTitleBar();
        // 初始化viewpager.
        initAllViewPager();
        // 初始化移动边框.
        initMoveBridge();
    }

    private void initMoveBridge() {
        float density = getResources().getDisplayMetrics().density;
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
        mEffectNoDrawBridge = new EffectNoDrawBridge();
        mainUpView1.setEffectBridge(mEffectNoDrawBridge);
        mEffectNoDrawBridge.setUpRectResource(R.drawable.white_light_10); // 设置移动边框图片.
        RectF rectF = new RectF(getDimension(R.dimen.w_10) * density, getDimension(R.dimen.h_10) * density,
                getDimension(R.dimen.w_10) * density, getDimension(R.dimen.h_10) * density);
        mEffectNoDrawBridge.setDrawUpRectPadding(rectF);
    }

    private void initAllTitleBar() {
        mOpenTabHost = (OpenTabHost) findViewById(R.id.openTabHost);
        mOpenTabTitleAdapter = new OpenTabTitleAdapter();
        mOpenTabHost.setOnTabSelectListener(this);
        mOpenTabHost.setAdapter(mOpenTabTitleAdapter);
    }

    private void initAllViewPager() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        //
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.test_page1, null);
        view2 = inflater.inflate(R.layout.test_page2, null); // gridview demo.
        view3 = inflater.inflate(R.layout.test_page3, null);
        view4 = inflater.inflate(R.layout.test_page4, null);
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        // 初始化滚动窗口适配. (请注意哈，在不同的dpi下, 滚动相差的间距不一样哈)
        for (View view : viewList) {
            float density = getResources().getDisplayMetrics().density;
            SmoothHorizontalScrollView shsv = (SmoothHorizontalScrollView) view.findViewById(R.id.test_hscroll);
            shsv.setFadingEdge((int) (getDimension(R.dimen.w_200) * density));
        }
        //
        viewpager.setAdapter(new DemoPagerAdapter());
        // 全局焦点监听. (这里只是demo，为了方便这样写，你可以不这样写)
        viewpager.getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                // 判断 : 避免焦点框跑到标题栏. (只是demo，你自己处理逻辑)
                // 你也可以让标题栏放大，有移动边框.
                if (newFocus != null && !(newFocus instanceof TextViewWithTTF)) {
                    mEffectNoDrawBridge.setVisibleWidget(false);
                    mNewFocus = newFocus;
                    mOldView = oldFocus;
                    mainUpView1.setFocusView(newFocus, oldFocus, 1.2f);
                    // 让被挡住的焦点控件在前面.
                    newFocus.bringToFront();
                    OPENLOG.D("addOnGlobalFocusChangeListener");
                } else { // 标题栏处理.
                    mNewFocus = null;
                    mOldView = null;
                    mainUpView1.setUnFocusView(oldFocus);
                    mEffectNoDrawBridge.setVisibleWidget(true);
                }
            }
        });
        viewpager.setOffscreenPageLimit(4);
        viewpager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switchTab(mOpenTabHost, position);
                OPENLOG.D("onPageSelected position:" + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // viewPager 正在滚动中.
                OPENLOG.D("onPageScrolled position:" + position + " positionOffset:" + positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE: // viewpager 滚动结束.
                        mainUpView1.setFocusView(mNewFocus, mOldView, 1.2f);
                        // 监听动画事件.
                        mEffectNoDrawBridge.setOnAnimatorListener(new OpenEffectBridge.NewAnimatorListener() {
                            @Override
                            public void onAnimationStart(OpenEffectBridge bridge, View view, Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(OpenEffectBridge bridge, View view, Animator animation) {
                                // 动画结束的时候恢复原来的时间. (这里只是DEMO)
                                mEffectNoDrawBridge.setTranDurAnimTime(OpenEffectBridge.DEFAULT_TRAN_DUR_ANIM);
                            }
                        });
                        // 让被挡住的焦点控件在前面.
                        if (mNewFocus != null)
                            mNewFocus.bringToFront();
                        OPENLOG.D("SCROLL_STATE_IDLE");
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        OPENLOG.D("SCROLL_STATE_DRAGGING");
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING: // viewPager开始滚动.
                        mEffectNoDrawBridge.clearAnimator(); // 清除之前的动画.
                        mEffectNoDrawBridge.setTranDurAnimTime(0); // 避免边框从其它地方跑出来.
                        OPENLOG.D("SCROLL_STATE_SETTLING");
                        break;
                }
            }
        });
        // 初始化.
        viewpager.setCurrentItem(0);
        switchTab(mOpenTabHost, 0);
    }

    @Override
    public void onTabSelect(OpenTabHost openTabHost, View titleWidget, int position) {
        if (viewpager != null) {
            viewpager.setCurrentItem(position);
        }
    }

    /**
     * demo (翻页的时候改变状态)
     * 将标题栏的文字颜色改变. <br>
     * 你可以写自己的东西，我这里只是DEMO.
     */
    public void switchTab(OpenTabHost openTabHost, int postion) {
        List<View> viewList = openTabHost.getAllTitleView();
        for (int i = 0; i < viewList.size(); i++) {
            TextViewWithTTF view = (TextViewWithTTF) openTabHost.getTitleViewIndexAt(i);
            if (view != null) {
                Resources res = view.getResources();
                if (res != null) {
                    if (i == postion) {
                        view.setTextColor(res.getColor(android.R.color.white));
                        view.setTypeface(null, Typeface.BOLD);
                        view.setSelected(true); // 为了显示 失去焦点，选中为 true的图片.
                    } else {
                        view.setTextColor(res.getColor(R.color.white_50));
                        view.setTypeface(null, Typeface.NORMAL);
                        view.setSelected(false);
                    }
                }
            }
        }
    }

    private float getDimension(int id) {
        return getResources().getDimension(id);
    }

    /**
     * viewpager 的 adpater.
     */
    class DemoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

    }

}
