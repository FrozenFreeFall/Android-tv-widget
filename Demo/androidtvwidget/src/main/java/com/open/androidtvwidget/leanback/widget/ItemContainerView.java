package com.open.androidtvwidget.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.open.androidtvwidget.R;

/**
 * Leanback 的一个item-->添加上面的标题头，添加下面的横向.
 * Created by hailongqiu on 2016/8/22.
 */
public class ItemContainerView extends LinearLayout {

    private ViewGroup mHeadDock;

    public ItemContainerView(Context context) {
        this(context, null, 0);
    }

    public ItemContainerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.lb_item_container, this);
        mHeadDock = (ViewGroup) findViewById(R.id.lb_item_container_head_dock);
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        //
        setClipChildren(false);
        setClipToPadding(false);
    }

    public void addHeaderView(View headerView) {
        if (mHeadDock.indexOfChild(headerView) < 0) {
            mHeadDock.addView(headerView, 0);
        }
    }

    public void removeHeaderView(View headerView) {
        if (mHeadDock.indexOfChild(headerView) >= 0) {
            mHeadDock.removeView(headerView);
        }
    }

    public void addRowView(View view) {
        addView(view);
    }

    public void showHeader(boolean show) {
        mHeadDock.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
