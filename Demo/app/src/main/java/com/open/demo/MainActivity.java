package com.open.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.widget.Toast;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.bridge.OpenEffectBridge;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.utils.Utils;
import com.open.androidtvwidget.view.FrameMainLayout;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.SmoothHorizontalScrollView;

/**
 * DEMO测试.
 * xml布局中 clipChildren clipToPadding 不要忘记了，不然移动的边框无法显示出来的. (强烈注意)
 */
public class MainActivity extends Activity implements OnClickListener {

    MainUpView mainUpView1;
    View test_top_iv;
    OpenEffectBridge mOpenEffectBridge;
    View mOldFocus; // 4.3以下版本需要自己保存.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OPENLOG.initTag("hailongqiu", true); // 开启log输出.
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.test_main);
        SmoothHorizontalScrollView hscroll_view = (SmoothHorizontalScrollView) findViewById(R.id.hscroll_view);
        hscroll_view.setFadingEdge((int)getDimension(R.dimen.w_100)); // 滚动窗口也需要适配.
        //
        test_top_iv = findViewById(R.id.test_top_iv);
        /* MainUpView 设置. */
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
        // mainUpView1 = new MainUpView(this); // 手动添加(test)
        // mainUpView1.attach2Window(this); // 手动添加(test)
        mOpenEffectBridge = (OpenEffectBridge) mainUpView1.getEffectBridge();
        // 4.2 绘制有问题，所以不使用绘制边框.
        // 也不支持倒影效果，绘制有问题.
        // 请大家不要按照我这样写.
        // 如果你不想放大小人超出边框(demo，张靓颖的小人)，可以不使用OpenEffectBridge.
        // 我只是测试----DEMO.(建议大家使用 NoDrawBridge)
        if (Utils.getSDKVersion() == 17) { // 测试 android 4.2版本.
            switchNoDrawBridgeVersion();
        } else { // 其它版本（android 4.3以上）.
            mainUpView1.setUpRectResource(R.drawable.test_rectangle); // 设置移动边框的图片.
            mainUpView1.setShadowResource(R.drawable.item_shadow); // 设置移动边框的阴影.
        }
        // mainUpView1.setUpRectResource(R.drawable.item_highlight); //
        // 设置移动边框的图片.(test)
        // mainUpView1.setDrawUpRectPadding(new Rect(0, 0, 0, -26)); //
        // 设置移动边框的距离.
        // mainUpView1.setDrawShadowPadding(0); // 阴影图片设置距离.
        // mOpenEffectBridge.setTranDurAnimTime(500); // 动画时间.

        FrameMainLayout main_lay11 = (FrameMainLayout) findViewById(R.id.main_lay);
        main_lay11.getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(final View oldFocus, final View newFocus) {
                if (newFocus != null)
                    newFocus.bringToFront(); // 防止放大的view被压在下面. (建议使用MainLayout)
                float scale = 1.2f;
                mainUpView1.setFocusView(newFocus, mOldFocus, scale);
                mOldFocus = newFocus; // 4.3以下需要自己保存.
                // 测试是否让边框绘制在下面，还是上面. (建议不要使用此函数)
                if (newFocus != null) {
                    testTopDemo(newFocus, scale);
                }
            }
        });
        // test demo.
        gridview_lay = findViewById(R.id.gridview_lay);
        gridview_lay.setOnClickListener(this);
        findViewById(R.id.listview_lay).setOnClickListener(this);
        findViewById(R.id.keyboard_lay).setOnClickListener(this);
        findViewById(R.id.viewpager_lay).setOnClickListener(this);
        findViewById(R.id.effect_rlay).setOnClickListener(this);
        findViewById(R.id.menu_rlayt).setOnClickListener(this);
        findViewById(R.id.recyclerview_rlayt).setOnClickListener(this);
        /**
         * 尽量不要使用鼠标. !!!! 如果使用鼠标，自己要处理好焦点问题.(警告)
         */
//		main_lay11.setOnHoverListener(new OnHoverListener() {
//			@Override
//			public boolean onHover(View v, MotionEvent event) {
//				mainUpView1.setVisibility(View.INVISIBLE);
//				return true;
//			}
//		});
        //
        for (int i = 0; i < main_lay11.getChildCount(); i++) {
            main_lay11.getChildAt(i).setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
//						v.performClick();
                        v.requestFocus();
                    }
                    return false;
                }
            });
        }
    }

    public View gridview_lay;

    /**
     * 这是一个测试DEMO，希望对API了解下再使用. 这种DEMO是为了实现这个效果:
     * https://raw.githubusercontent.com/FrozenFreeFall/ImageSaveHttp/master/
     * chaochupingm%20.jpg
     */
    public void testTopDemo(View newView, float scale) {
        // 测试第一个小人放大的效果.
        if (newView.getId() == R.id.gridview_lay) { // 小人在外面的测试.
            RectF rectf = new RectF(getDimension(R.dimen.w_7), -getDimension(R.dimen.h_63), getDimension(R.dimen.w_7),
                    getDimension(R.dimen.h_30));
            mOpenEffectBridge.setDrawUpRectPadding(rectf); // 设置移动边框间距，不要被挡住了。
            mOpenEffectBridge.setDrawShadowRectPadding(rectf); // 设置阴影边框间距，不要被挡住了。
            mOpenEffectBridge.setDrawUpRectEnabled(false); // 让移动边框绘制在小人的下面.
            test_top_iv.animate().scaleX(scale).scaleY(scale).setDuration(100).start(); // 让小人超出控件.
        } else { // 其它的还原.
            mOpenEffectBridge.setDrawUpRectPadding(0);
            mOpenEffectBridge.setDrawShadowPadding(0);
            mOpenEffectBridge.setDrawUpRectEnabled(true);
            test_top_iv.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start(); // 让小人超出控件.
        }
    }

    public float getDimension(int id) {
        return getResources().getDimension(id);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.gridview_lay:
                showMsg("Gridview demo test");
                startActivity(new Intent(getApplicationContext(), DemoGridViewActivity.class));
                break;
            case R.id.listview_lay:
                showMsg("Listview demo test");
                startActivity(new Intent(getApplicationContext(), DemoListViewActivity.class));
                break;
            case R.id.keyboard_lay:
                showMsg("键盘 demo test");
                startActivity(new Intent(getApplicationContext(), DemoKeyBoardActivity.class));
                break;
            case R.id.viewpager_lay: // viewpager页面切换测试.
                showMsg("ViewPager页面切换测试");
                startActivity(new Intent(getApplicationContext(), DemoViewPagerActivity.class));
                break;
            case R.id.effect_rlay:
                showMsg("Effect动画切换测试");
                switchNoDrawBridgeVersion();
                break;
            case R.id.menu_rlayt: // 菜单测试.
                showMsg("菜单测试");
                startActivity(new Intent(getApplicationContext(), DemoMenuActivity.class));
                break;
            case R.id.recyclerview_rlayt:
                showMsg("recyclerview测试");
                startActivity(new Intent(getApplicationContext(), DemoRecyclerviewActivity.class));
            default:
                break;
        }
    }

    private void showMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void switchNoDrawBridgeVersion() {
        float density = getResources().getDisplayMetrics().density;
        RectF rectf = new RectF(getDimension(R.dimen.w_10) * density, getDimension(R.dimen.h_10) * density,
                getDimension(R.dimen.w_9) * density, getDimension(R.dimen.h_9) * density);
        EffectNoDrawBridge effectNoDrawBridge = new EffectNoDrawBridge();
        effectNoDrawBridge.setTranDurAnimTime(200);
//        effectNoDrawBridge.setDrawUpRectPadding(rectf);
        mainUpView1.setEffectBridge(effectNoDrawBridge); // 4.3以下版本边框移动.
        mainUpView1.setUpRectResource(R.drawable.white_light_10); // 设置移动边框的图片.
        mainUpView1.setDrawUpRectPadding(rectf); // 边框图片设置间距.
    }

}
