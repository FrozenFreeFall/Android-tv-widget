package com.open.demo.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open.androidtvwidget.leanback.mode.DefualtListPresenter;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.leanback.widget.ItemContainerView;
import com.open.androidtvwidget.leanback.widget.ListContentView;
import com.open.androidtvwidget.leanback.widget.OpenCardView;
import com.open.demo.R;

/**
 * Leanback 横向item demo.
 * 如果你想改变标题头的样式，那就写自己的吧.
 * Created by hailongqiu on 2016/8/25.
 */
public class TestMoviceListPresenter extends DefualtListPresenter {

    boolean mIsSelect;

    /**
     * 你可以重写这里，传入AutoGridViewLayoutManger.
     */
    @Override
    public RecyclerView.LayoutManager getLayoutManger(Context context) {
        return super.getLayoutManger(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lb_h_item, parent, false);
        return new ViewHolder(itemView);
    }

    public void setSelect(boolean isSelect) {
        this.mIsSelect = isSelect;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Movie movie = ((Movie) getItem(position));
        OpenCardView openCardView = (OpenCardView) viewHolder.view;
        openCardView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(300).start();
                } else {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).start();
                }
            }
        });
        openCardView.setBackgroundResource(R.drawable.mainview_cloudlist);
        Drawable d = viewHolder.view.getResources().getDrawable(R.drawable.ic_sp_block_focus);
        openCardView.setShadowDrawable(d);
        //
        TextView tv = (TextView) openCardView.findViewById(R.id.title_tv);
        tv.setText(movie.getTitle());
        //
        if (this.mIsSelect) {
            openCardView.setAlpha(0.5f);
        } else {
            openCardView.setAlpha(1.0f);
        }
    }

}
