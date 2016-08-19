package com.open.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;

import com.open.androidtvwidget.bridge.RecyclerViewBridge;
import com.open.androidtvwidget.recycle.RecyclerViewTV;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.view.MainUpView;
import com.open.demo.adapter.HeaderGridAdapter;
import com.open.demo.adapter.RecyclerViewAdapter;

/**
 * recyclerview Demo.
 * setSelectedItemAtCentered 设置一直在中间. (如果设置 false，那么请使用setSelectedItemOffset来设置相差的边距)
 * @author hailongqiu
 */
public class DemoRecyclerviewActivity extends Activity implements OnClickListener, OnFocusChangeListener {

    Context mContext;
    RecyclerViewTV recyclerView;
    MainUpView mainUpView1;
    RecyclerViewBridge mRecyclerViewBridge;
    private View oldView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_recyclerview_activity);
        OPENLOG.initTag("hailongqiu", true); // 打开debug信息.
        mContext = DemoRecyclerviewActivity.this;
        recyclerView = (RecyclerViewTV) findViewById(R.id.recyclerView);
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
        mainUpView1.setEffectBridge(new RecyclerViewBridge());
        //
        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView1.getEffectBridge();
        mRecyclerViewBridge.setUpRectResource(R.drawable.test_rectangle);
        //
        testHeaderGridLayout();
        initAllViewEvents();
        //
        recyclerView.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setUnFocusView(oldView);
                mRecyclerViewBridge.setFocusView(itemView, 1.4f, 1.2f);
                oldView = itemView;
            }

            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
            }

            // 调整边框偏移的问题.
            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.4f, 1.2f);
                oldView = itemView;
            }
        });
    }

    private void initAllViewEvents() {
        findViewById(R.id.h_liner_btn).setOnClickListener(this);
        findViewById(R.id.v_liner_btn).setOnClickListener(this);
        findViewById(R.id.h_grid_btn).setOnClickListener(this);
        findViewById(R.id.v_grid_btn).setOnClickListener(this);
        findViewById(R.id.head_grid_btn).setOnClickListener(this);
        //
        findViewById(R.id.h_liner_btn).setOnFocusChangeListener(this);
        findViewById(R.id.v_liner_btn).setOnFocusChangeListener(this);
        findViewById(R.id.h_grid_btn).setOnFocusChangeListener(this);
        findViewById(R.id.v_grid_btn).setOnFocusChangeListener(this);
        findViewById(R.id.head_grid_btn).setOnFocusChangeListener(this);
    }

    /**
     * 测试LinerLayout.
     */
    public void testRecyclerViewLinerLayout(int orientation) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(orientation);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(100);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    /**
     * 测试GridLayout.
     */
    private void testRecyclerViewGridLayout(int orientation) {
        GridLayoutManager gridlayoutManager = new GridLayoutManager(this, 4);
        gridlayoutManager.setOrientation(orientation);
        recyclerView.setLayoutManager(gridlayoutManager);
        recyclerView.setFocusable(false);
        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(100);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    /**
     * 测试带标题栏的grid.
     */
    private void testHeaderGridLayout() {
        final GridLayoutManager gridlayoutManager = new GridLayoutManager(this, 5);
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        // recyclerView.setHasFixedSize(true); // 保持固定的大小
        recyclerView.setLayoutManager(gridlayoutManager);
        recyclerView.setFocusable(false);
        final HeaderGridAdapter mHeaderGridAdapter = new HeaderGridAdapter(100);
        recyclerView.setAdapter(mHeaderGridAdapter);
        gridlayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mHeaderGridAdapter.isHeader(position) ? gridlayoutManager.getSpanCount() : 1;
            }
        });
    }

    // 左边侧边栏的单击事件.

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.h_liner_btn: // 横向 liner layout.
                testRecyclerViewLinerLayout(LinearLayoutManager.HORIZONTAL);
                break;
            case R.id.v_liner_btn:
                testRecyclerViewLinerLayout(LinearLayoutManager.VERTICAL);
                break;
            case R.id.h_grid_btn: // 横向 grid layout.
                testRecyclerViewGridLayout(GridLayoutManager.HORIZONTAL);
                break;
            case R.id.v_grid_btn:
                testRecyclerViewGridLayout(GridLayoutManager.VERTICAL);
                break;
            case R.id.head_grid_btn: // 带header的grid.
                testHeaderGridLayout();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View focusview, boolean hasFocus) {
        mRecyclerViewBridge.setFocusView(focusview, oldView, 1.0f);
        oldView = focusview;
    }

}
