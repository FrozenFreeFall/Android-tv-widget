package com.open.androidtvwidget.leanback.mode;

import android.view.View;
import android.view.ViewGroup;

import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;

/**
 * Created by hailongqiu on 2016/8/22.
 */
public abstract class OpenPresenter {

    /**
     * 基本数据类型(ViewHolder)
     */
    public static class ViewHolder {

        public final View view;

        public ViewHolder(View view) {
            this.view = view;
        }
    }

    public int getItemCount() {
        return 0;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public abstract ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public void onViewAttachedToWindow(ViewHolder viewHolder) {

    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {

    }

    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

    }

    public void onViewDetachedFromWindow(ViewHolder viewHolder) {

    }

    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    public void setAdapter(GeneralAdapter adapter) {

    }

}
