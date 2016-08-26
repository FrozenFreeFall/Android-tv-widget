package com.open.androidtvwidget.leanback.mode;

import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * 如果你要设置自己的 横向 RecyclerView 中的item样式，继承这个.
 * Created by hailongqiu on 2016/8/24.
 */
public class DefualtListPresenter extends OpenPresenter {

    List<Object> mItems;

    public DefualtListPresenter() {

    }

    public DefualtListPresenter(Object items) {
        mItems = (List<Object>) items;
    }

    public void setItems(Object items) {
        mItems = (List<Object>) items;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Object getItem(int pos) {
        return this.mItems.get(pos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Button btn = new Button(parent.getContext());
        return new ViewHolder(btn);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String str = ((String) mItems.get(position));
        Button btn = ((Button) viewHolder.view);
        btn.setText(str);
    }
}
