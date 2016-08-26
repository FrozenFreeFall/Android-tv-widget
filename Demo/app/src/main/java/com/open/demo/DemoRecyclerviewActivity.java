package com.open.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.open.androidtvwidget.bridge.RecyclerViewBridge;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.ItemHeaderPresenter;
import com.open.androidtvwidget.leanback.mode.ItemListPresenter;
import com.open.androidtvwidget.leanback.mode.ListRow;
import com.open.androidtvwidget.leanback.mode.ListRowPresenter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import com.open.androidtvwidget.leanback.recycle.GridLayoutManagerTV;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.view.MainUpView;
import com.open.demo.adapter.HeaderGridPresenter;
import com.open.demo.adapter.LeftMenuPresenter;
import com.open.demo.adapter.RecyclerViewPresenter;
import com.open.demo.mode.Movie;
import com.open.demo.mode.TestMoviceListPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * recyclerview Demo.
 * setSelectedItemAtCentered 设置一直在中间. (如果设置 false，那么请使用setSelectedItemOffset来设置相差的边距)
 *
 * @author hailongqiu
 */
public class DemoRecyclerviewActivity extends Activity implements RecyclerViewTV.OnItemListener {

    private Context mContext;
    private RecyclerViewTV left_menu_rv; // 左侧菜单.
    private RecyclerViewTV mRecyclerView;
    private MainUpView mainUpView1;
    private RecyclerViewBridge mRecyclerViewBridge;
    private View oldView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_recyclerview_activity);
        OPENLOG.initTag("hailongqiu", true); // 打开debug信息.
        mContext = DemoRecyclerviewActivity.this;
        left_menu_rv = (RecyclerViewTV) findViewById(R.id.left_menu_rv);
        mRecyclerView = (RecyclerViewTV) findViewById(R.id.recyclerView);
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
        mainUpView1.setEffectBridge(new RecyclerViewBridge());
        // 注意这里，需要使用 RecyclerViewBridge 的移动边框 Bridge.
        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView1.getEffectBridge();
        mRecyclerViewBridge.setUpRectResource(R.drawable.test_rectangle);
        // 初始化左侧菜单.
        initLeftMenu();
        //  初始化带标题头的demo.
//        testHeaderGridLayout();
        testLeanbackDemo();
        //
        mRecyclerView.setOnItemListener(this);
        // item 单击事件处理.
        mRecyclerView.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
            }
        });
    }

    private void initLeftMenu() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        left_menu_rv.setLayoutManager(layoutManager);
        left_menu_rv.setFocusable(false);
        GeneralAdapter generalAdapter = new GeneralAdapter(new LeftMenuPresenter());
        left_menu_rv.setAdapter(generalAdapter);
        left_menu_rv.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                // 传入 itemView也可以, 自己保存的 oldView也可以.
                mRecyclerViewBridge.setUnFocusView(itemView);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.0f);
                oldView = itemView;
            }

            /**
             * 这里是调整开头和结尾的移动边框.
             */
            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.0f);
                oldView = itemView;
            }
        });
        left_menu_rv.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                // 测试.
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.0f);
                oldView = itemView;
                //
                onViewItemClick(itemView, position);
            }
        });
    }

    /**
     * 测试LinerLayout.
     */
    private void testRecyclerViewLinerLayout(int orientation) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setFocusable(false);
        GeneralAdapter generalAdapter = new GeneralAdapter(new RecyclerViewPresenter(100));
        mRecyclerView.setAdapter(generalAdapter);
    }

    /**
     * 测试GridLayout.
     */
    private void testRecyclerViewGridLayout(int orientation) {
        GridLayoutManagerTV gridlayoutManager = new GridLayoutManagerTV(this, 4); // 解决快速长按焦点丢失问题.
        gridlayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(gridlayoutManager);
        mRecyclerView.setFocusable(false);
        GeneralAdapter generalAdapter = new GeneralAdapter(new RecyclerViewPresenter(100));
        mRecyclerView.setAdapter(generalAdapter);
    }

    /**
     * 测试带标题栏的grid.
     */
    private void testHeaderGridLayout() {
        final GridLayoutManagerTV gridlayoutManager = new GridLayoutManagerTV(this, 5); // 解决快速长按焦点丢失问题.
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        // recyclerView.setHasFixedSize(true); // 保持固定的大小
        mRecyclerView.setLayoutManager(gridlayoutManager);
        mRecyclerView.setFocusable(false);
        final HeaderGridPresenter headerGridAdapter = new HeaderGridPresenter(100);
        GeneralAdapter generalAdapter = new GeneralAdapter(headerGridAdapter);
        mRecyclerView.setAdapter(generalAdapter);
        gridlayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return headerGridAdapter.isHeader(position) ? gridlayoutManager.getSpanCount() : 1;
            }
        });
    }

    /**
     * Leanback 标题头.
     */
    private static final String MOVIE_CATEGORY[] = {
            "全部软件",
            "聊天工具",
            "浏览器",
            "游戏娱乐",
            "网络游戏",
            "杀毒安全",
    };

    /**
     * Leanback 横向 数据测试.
     */
    private static final Movie MOVIE_ITEMS[] = {
            new Movie(0, "有道云笔记"),
            new Movie(0, "陌陌"),
            new Movie(0, "爱奇艺"),
            new Movie(0, "英雄联盟"),
            new Movie(0, "腾讯视频"),
            new Movie(0, "QQ音乐"),
            new Movie(0, "无敌讯飞"),
    };

    /**
     * Leanback Demo.
     */
    private void testLeanbackDemo() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        // 添加标题头.
        List<ListRow> listRows = new ArrayList<ListRow>();
        for (int i = 0; i < MOVIE_CATEGORY.length; i++) {
            String txt = MOVIE_CATEGORY[i];
            // 添加一行的数据.
            ListRow listRow = new ListRow(txt);
            for (int j = 0; j < 20; j++) {
                listRow.add(MOVIE_ITEMS[new Random().nextInt(MOVIE_ITEMS.length - 1)]);
            }
            listRows.add(listRow);
        }
        // 测试demo, 一般你想要自己的效果，
        // 继承 Header 和 List 可以继承 OpenPresente来重写.
        //  而横向中的item 继承 DefualtListPresenter 来重写.
        ListRowPresenter listRowPresenter = new ListRowPresenter(listRows,
                new ItemHeaderPresenter(),
                new ItemListPresenter(new TestMoviceListPresenter()));
        GeneralAdapter generalAdapter = new GeneralAdapter(listRowPresenter);
        mRecyclerView.setAdapter(generalAdapter);
    }

    // 左边侧边栏的单击事件.
    private void onViewItemClick(View v, int pos) {
        switch (pos) {
            case 0: // 横向 liner layout.
                testRecyclerViewLinerLayout(LinearLayoutManager.HORIZONTAL);
                break;
            case 1: // 纵向 liner layout.
                testRecyclerViewLinerLayout(LinearLayoutManager.VERTICAL);
                break;
            case 2: // 横向 grid layout.
                testRecyclerViewGridLayout(GridLayoutManager.HORIZONTAL);
                break;
            case 3: // 纵向 grid layout.
                testRecyclerViewGridLayout(GridLayoutManager.VERTICAL);
                break;
            case 4: // 带header的grid.
                testHeaderGridLayout();
                break;
            case 5: // Leanback demo.
                testLeanbackDemo();
                break;
            default:
                break;
        }
    }

    /**
     * 排除 Leanback demo的RecyclerView.
     */
    private boolean isListRowPresenter() {
        GeneralAdapter generalAdapter = (GeneralAdapter) mRecyclerView.getAdapter();
        OpenPresenter openPresenter = generalAdapter.getPresenter();
        return (openPresenter instanceof ListRowPresenter);
    }

    @Override
    public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
        if (!isListRowPresenter()) {
            mRecyclerViewBridge.setUnFocusView(oldView);
        }
    }

    @Override
    public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
        if (!isListRowPresenter()) {
            mRecyclerViewBridge.setFocusView(itemView, 1.2f);
            oldView = itemView;
        }
    }

    @Override
    public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
        if (!isListRowPresenter()) {
            mRecyclerViewBridge.setFocusView(itemView, 1.2f);
            oldView = itemView;
        }
    }

}
