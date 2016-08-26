package com.open.androidtvwidget.leanback.mode;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.recycle.LinearLayoutManagerTV;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.leanback.widget.ListContentView;

/**
 * Leanback item 标题头下面的 横向布局.（要弄自己的样式，继承这个，重写)，这里只是规定了横向的.
 * Created by hailongqiu on 2016/8/24.
 */
public class ItemListPresenter extends OpenPresenter {

    DefualtListPresenter mListPresenter;

    public ItemListPresenter() {
        mListPresenter = new DefualtListPresenter();
    }

    public ItemListPresenter(DefualtListPresenter listPresenter) {
        this.mListPresenter = listPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListContentView listContentView = new ListContentView(parent.getContext());
        return new ItemListViewHolder(listContentView, listContentView.getRecyclerViewTV());
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        final ItemListViewHolder itemListViewHolder = (ItemListViewHolder) viewHolder;
        mListPresenter.setItems(item);
        GeneralAdapter generalAdapter = new GeneralAdapter(mListPresenter);
//        new AutoMeaureGridLayoutManager(viewHolder.view.getContext(), 4, GridLayoutManager.VERTICAL, false)
        LinearLayoutManagerTV lm = new LinearLayoutManagerTV(viewHolder.view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        lm.setAutoMeasureEnabled(true); // 自动适应布局.
        itemListViewHolder.mRecyclerViewTV.setLayoutManager(lm);
        itemListViewHolder.mRecyclerViewTV.setAdapter(generalAdapter);
        itemListViewHolder.mRecyclerViewTV.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                itemView.animate().scaleX(1.0f).scaleY(1.0f).start();
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                itemView.animate().scaleX(1.3f).scaleY(1.3f).start();
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
            }
        });
    }

    static class ItemListViewHolder extends OpenPresenter.ViewHolder {
        private RecyclerViewTV mRecyclerViewTV;

        public ItemListViewHolder(View view, RecyclerViewTV rv) {
            super(view);
            this.mRecyclerViewTV = rv;
        }
    }

}
