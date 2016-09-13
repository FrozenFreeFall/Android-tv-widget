package com.open.demo.network.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.open.androidtvwidget.view.ReflectItemView;
import com.open.demo.R;

/**
 * 用于JSON解析后加载的itemView布局.
 * Created by hailongqiu on 2016/9/13.
 */
public class JsonItemView extends ReflectItemView {

    public JsonItemView(Context context) {
        this(context, null);
    }

    public JsonItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JsonItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.json_item_layout_view, this);
    }

    public void setText(int res) {

    }

    public void setText(String text) {

    }

}
