package com.open.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import com.open.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试.
 * Created by hailongqiu on 2016/8/24.
 */
public class RecyclerViewPresenter extends OpenPresenter {

    private final List<String> labels;

    public RecyclerViewPresenter(int count) {
        this.labels = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            labels.add(String.valueOf(i));
        }
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_view, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        GridViewHolder gridViewHolder = (GridViewHolder) viewHolder;
        TextView textView = (TextView) gridViewHolder.tv;
        textView.setText("item " + labels.get(position));
    }

}
