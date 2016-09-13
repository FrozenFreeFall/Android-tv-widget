package com.open.androidtvwidget.leanback.mode;

import android.view.View;
import android.view.ViewGroup;

import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.leanback.widget.ListContentView;

/**
 * Leanback item 标题头下面的 横向布局.（要弄自己的样式，继承这个，重写)，这里只是规定了横向的.
 * Created by hailongqiu on 2016/8/24.
 */
public class ItemListPresenter extends OpenPresenter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListContentView listContentView = new ListContentView(parent.getContext());
        return new ItemListViewHolder(listContentView, listContentView.getRecyclerViewTV());
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Object item) {
        final ItemListViewHolder itemListViewHolder = (ItemListViewHolder) viewHolder;
        ListRow listRow = (ListRow) item;
        DefualtListPresenter openPresenter = (DefualtListPresenter) listRow.getOpenPresenter();
        itemListViewHolder.setDefualtListPresenter(openPresenter); // 保存一下，外面需要调用.
        openPresenter.setItems(listRow.getItems());
        itemListViewHolder.mRecyclerViewTV.setLayoutManager(openPresenter.getLayoutManger(viewHolder.view.getContext()));
        GeneralAdapter generalAdapter = new GeneralAdapter(openPresenter);
        itemListViewHolder.mRecyclerViewTV.setAdapter(generalAdapter);
        itemListViewHolder.mRecyclerViewTV.setOnItemListener(openPresenter.getOnItemListener());
        itemListViewHolder.mRecyclerViewTV.setOnItemClickListener(openPresenter.getOnItemClickListener());
    }

    public static class ItemListViewHolder extends OpenPresenter.ViewHolder {
        private RecyclerViewTV mRecyclerViewTV;
        private DefualtListPresenter mDefualtListPresenter;

        public ItemListViewHolder(View view, RecyclerViewTV rv) {
            super(view);
            this.mRecyclerViewTV = rv;
        }

        public void setDefualtListPresenter(DefualtListPresenter presenter) {
            this.mDefualtListPresenter = presenter;
        }

        public DefualtListPresenter getDefualtListPresenter() {
            return this.mDefualtListPresenter;
        }

        public RecyclerViewTV getRecyclerViewTV() {
            return this.mRecyclerViewTV;
        }
    }

}
