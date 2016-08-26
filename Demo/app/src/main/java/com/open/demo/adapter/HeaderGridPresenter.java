package com.open.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import com.open.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 带标题头的 Grid RecyclerView测试.
 * Created by hailongqiu on 2016/8/24.
 */
public class HeaderGridPresenter extends OpenPresenter {

    private static final int ITEM_VIEW_TYPE_HEADER = 0; // 头
    private static final int ITEM_VIEW_TYPE_ITEM = 1; // item.

    private final List<String> labels;

    public HeaderGridPresenter(int count) {
        this.labels = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            labels.add(String.valueOf(i));
        }
    }

    /**
     * 判断是否为Header.
     */
    public boolean isHeader(int position) {
        return (position % 12) == 0;
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View headview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_view, parent, false);
            // return new HeaderHolder(headview);
            return new GridViewHolder(headview);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_view, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (isHeader(position)) {
            return;
        }
    }

}
