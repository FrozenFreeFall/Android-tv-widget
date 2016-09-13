package com.open.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.open.androidtvwidget.bridge.RecyclerViewBridge;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.DefualtListPresenter;
import com.open.androidtvwidget.leanback.mode.ItemHeaderPresenter;
import com.open.androidtvwidget.leanback.mode.ItemListPresenter;
import com.open.androidtvwidget.leanback.mode.ListRow;
import com.open.androidtvwidget.leanback.mode.ListRowPresenter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import com.open.androidtvwidget.leanback.recycle.GridLayoutManagerTV;
import com.open.androidtvwidget.leanback.recycle.LinearLayoutManagerTV;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.view.MainUpView;
import com.open.demo.adapter.LeftMenuPresenter;
import com.open.demo.adapter.RecyclerViewPresenter;
import com.open.demo.mode.LeanbackTestData;
import com.open.demo.mode.Movie;
import com.open.demo.mode.TestMoviceListPresenter;

import java.util.ArrayList;
import java.util.List;

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
    private View load_more_pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_recyclerview_activity);
        OPENLOG.initTag("hailongqiu", true); // 打开debug信息.
        mContext = DemoRecyclerviewActivity.this;
        load_more_pb = findViewById(R.id.load_more_pb);
        left_menu_rv = (RecyclerViewTV) findViewById(R.id.left_menu_rv);
        mRecyclerView = (RecyclerViewTV) findViewById(R.id.recyclerView);
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
        mainUpView1.setEffectBridge(new RecyclerViewBridge());
        // 注意这里，需要使用 RecyclerViewBridge 的移动边框 Bridge.
        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView1.getEffectBridge();
        mRecyclerViewBridge.setUpRectResource(R.drawable.video_cover_cursor);
        float density = getResources().getDisplayMetrics().density;
        RectF receF = new RectF(getDimension(R.dimen.w_45) * density, getDimension(R.dimen.h_40) * density,
                getDimension(R.dimen.w_45) * density, getDimension(R.dimen.h_40) * density);
        mRecyclerViewBridge.setDrawUpRectPadding(receF);
        // 初始化左侧菜单.
        initLeftMenu();
        //  初始化带标题头的demo.
//        testHeaderGridLayout();
//        testLeanbackDemo();
        testRecyclerViewLinerLayout(RecyclerView.HORIZONTAL);
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
        LinearLayoutManagerTV layoutManager = new LinearLayoutManagerTV(this);
        layoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setFocusable(false);
        mRecyclerViewPresenter = new RecyclerViewPresenter(20);
        mGeneralAdapter = new GeneralAdapter(mRecyclerViewPresenter);
        mRecyclerView.setAdapter(mGeneralAdapter);
        mRecyclerView.setSelectedItemOffset(111, 111); // 测试移动间距.
        mRecyclerView.setPagingableListener(new RecyclerViewTV.PagingableListener() {
            @Override
            public void onLoadMoreItems() {
                // 加载更多测试.
//                moreHandler.removeCallbacksAndMessages(null);
                Message msg = moreHandler.obtainMessage();
                msg.arg1 = 10;
                moreHandler.sendMessageDelayed(msg, 3000);
                load_more_pb.setVisibility(View.VISIBLE);
            }
        });
        mFocusHandler.sendEmptyMessageDelayed(10, 1000);
    }

    private int mSavePos = 2;

    Handler mFocusHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!isListRowPresenter())
                mRecyclerView.setDefaultSelect(mSavePos);
        }
    };

    RecyclerViewPresenter mRecyclerViewPresenter;
    GeneralAdapter mGeneralAdapter;

    Handler moreHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mRecyclerViewPresenter.addDatas(msg.arg1);
            mSavePos = mRecyclerView.getSelectPostion();
            mRecyclerView.setOnLoadMoreComplete(); // 加载更多完毕.
            mFocusHandler.sendEmptyMessageDelayed(10, 10); // 延时请求焦点.
            OPENLOG.D("加载更多....");
            load_more_pb.setVisibility(View.GONE);
        }
    };

    /**
     * 测试GridLayout.
     */
    private void testRecyclerViewGridLayout(int orientation) {
        GridLayoutManagerTV gridlayoutManager = new GridLayoutManagerTV(this, 4); // 解决快速长按焦点丢失问题.
        gridlayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(gridlayoutManager);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setSelectedItemAtCentered(true); // 设置item在中间移动.
        mRecyclerViewPresenter = new RecyclerViewPresenter(25);
        mGeneralAdapter = new GeneralAdapter(mRecyclerViewPresenter);
        mRecyclerView.setAdapter(mGeneralAdapter);
        mRecyclerView.setPagingableListener(new RecyclerViewTV.PagingableListener() {
            @Override
            public void onLoadMoreItems() {
                // 加载更多测试.
//                moreHandler.removeCallbacksAndMessages(null);
                Message msg = moreHandler.obtainMessage();
                msg.arg1 = 21;
                moreHandler.sendMessageDelayed(msg, 3000);
            }
        });
    }

    ///////////////////////////////////////////////////////////////
    ////   Leanback 测试 Demo start ---->
    ///////////////////////////////////////////////////////////////

    private final RecyclerViewTV.OnChildViewHolderSelectedListener mRowSelectedListener =
            new RecyclerViewTV.OnChildViewHolderSelectedListener() {
                @Override
                public void onChildViewHolderSelected(RecyclerView parent,
                                                      RecyclerView.ViewHolder viewHolder, int position) {
                    onRowSelected(parent, viewHolder, position, -1);
                }
            };

    private GeneralAdapter.ViewHolder mSelectedViewHolder;
    List<ListRow> mListRows = new ArrayList<ListRow>();
    ListRowPresenter mListRowPresenter;

    /**
     * 一行选中.
     */
    private void onRowSelected(RecyclerView parent, RecyclerView.ViewHolder viewHolder,
                               int position, int subposition) {
        if (mSelectedViewHolder != viewHolder) {
//            OPENLOG.D("pos:" + position + " vh:" + viewHolder);
            // 先清除 MselectedViewHolder 的一行选中颜色.
            if (mSelectedViewHolder != null) {
                setRowViewSelected(mSelectedViewHolder, false);
            }
            // 设置当前选中的 一行的选中颜色.
            mSelectedViewHolder = (GeneralAdapter.ViewHolder) viewHolder;
            if (mSelectedViewHolder != null) {
                setRowViewSelected(mSelectedViewHolder, true);
            }
        }
    }

    /**
     * 改变一行的颜色.这里只是DEMO，你可以改变一行的图片哈，或者背景颜色哈.
     * 具体可以看Leanback的android demo.
     */
    private void setRowViewSelected(GeneralAdapter.ViewHolder viewHolder, boolean selected) {
        if (isListRowPresenter()) {
            try {
                ListRowPresenter.ListRowViewHolder listRowPresenter = (ListRowPresenter.ListRowViewHolder) viewHolder.getViewHolder();
                ItemListPresenter.ItemListViewHolder itemListViewHolder = (ItemListPresenter.ItemListViewHolder) listRowPresenter.getListViewHolder();
                DefualtListPresenter defualtListPresenter = itemListViewHolder.getDefualtListPresenter();
                if (defualtListPresenter instanceof TestMoviceListPresenter) {
                    TestMoviceListPresenter testMoviceListPresenter = (TestMoviceListPresenter) defualtListPresenter;
                    testMoviceListPresenter.setSelect(selected);
                    //
                    RecyclerViewTV recyclerViewTV = itemListViewHolder.getRecyclerViewTV();
                    int count = recyclerViewTV.getChildCount();
                    for (int i = 0; i < count; i++) {
                        View view = recyclerViewTV.getChildAt(i);
                        if (selected) {
                            view.setAlpha(0.5f);
                        } else {
                            view.setAlpha(1.0f);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Leanback Demo.
     */
    private void testLeanbackDemo() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setSelectedItemAtCentered(true); // 设置item在中间移动.
        // 添加测试数据。
        for (int i = 0; i < LeanbackTestData.MOVIE_CATEGORY.length; i++) {
            String txt = LeanbackTestData.MOVIE_CATEGORY[i];
            // 添加一行的数据.
            ListRow listRow = new ListRow(txt); // 标题头.
            List<Movie> movies = LeanbackTestData.MOVIE_ITEMS;
            if (i % 2 == 1)
                movies = LeanbackTestData.MOVIE_ITEMS2;
            listRow.addAll(movies); // 添加列的数据.
            listRow.setOpenPresenter(new TestMoviceListPresenter()); // 设置列的item样式.
            // 添加一行的数据（标题头，列的数据)
            mListRows.add(listRow);
        }
        // 添加最后一行数据.
        ListRow lastListRow = new ListRow("设置");
        lastListRow.add("网络wifi");
        lastListRow.add("声音");
        lastListRow.add("声音");
        lastListRow.setOpenPresenter(new DefualtListPresenter());
        mListRows.add(lastListRow);
        // 测试demo, 一般你想要自己的效果，
        // 继承 Header 和 List 可以继承 OpenPresente来重写.
        //  而横向中的item 继承 DefualtListPresenter 来重写.
        mListRowPresenter = new ListRowPresenter(mListRows,
                new ItemHeaderPresenter(),
                new ItemListPresenter());
        GeneralAdapter generalAdapter = new GeneralAdapter(mListRowPresenter);
        mRecyclerView.setAdapter(generalAdapter);
        // 行选中的事件.
        mRecyclerView.setOnChildViewHolderSelectedListener(mRowSelectedListener);
        // 更新数据测试.
        handler.sendEmptyMessageDelayed(10, 6666);
    }

    // 更新数据测试(更新某条数据).
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            View view = mRecyclerView.getSelectView();
            // 请求更新数据.
            ListRow listRow = mListRows.get(0);
            listRow.setHeaderItem("改变标题头数据");
            mListRowPresenter.setItems(mListRows, 0);
            // 只有保存原来的焦点view的位置, 然后 延时请求焦点.
        }
    };

    ///////////////////////////////////////////////////////////////
    ////   Leanback 测试 Demo end ---->
    ///////////////////////////////////////////////////////////////

    public float getDimension(int id) {
        return getResources().getDimension(id);
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
            case 4: // Leanback demo. (带标题头的demo).
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
