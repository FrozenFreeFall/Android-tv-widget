package com.open.demo.mode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open.androidtvwidget.leanback.mode.DefualtListPresenter;
import com.open.demo.R;

/**
 * Leanback 横向item demo.
 * Created by hailongqiu on 2016/8/25.
 */
public class TestMoviceListPresenter extends DefualtListPresenter {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View headview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_view, parent, false);
        return new ViewHolder(headview);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Movie movie = ((Movie) getItem(position));
        TextView textview = (TextView) viewHolder.view.findViewById(R.id.textView);
        textview.setText(movie.getTitle());
    }
}
