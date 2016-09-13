/*
Copyright 2016 The Open Source Project

Author: hailongqiu <356752238@qq.com>
Maintainer: hailongqiu <356752238@qq.com>
					  pengjunkun <junkun@mgtv.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.open.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.GridViewTV;
import com.open.androidtvwidget.view.MainUpView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GridView Demo测试.
 */
public class DemoGridViewActivity extends Activity {

    private List<String> data;
    private MainUpView mainUpView1;
    private View mOldView;
    private GridViewTV gridView;
    private GridViewAdapter mAdapter;
    private int mSavePos = -1;
    private int mCount = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_grid_view);

        gridView = (GridViewTV) findViewById(R.id.gridView);
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
        // 建议使用 NoDraw.
        mainUpView1.setEffectBridge(new EffectNoDrawBridge());
        EffectNoDrawBridge bridget = (EffectNoDrawBridge) mainUpView1.getEffectBridge();
        bridget.setTranDurAnimTime(200);
        // 设置移动边框的图片.
        mainUpView1.setUpRectResource(R.drawable.white_light_10);
        // 移动方框缩小的距离.
        mainUpView1.setDrawUpRectPadding(new Rect(10, 10, 10, -55));
        // 加载数据.
        getData(200);
        //
        updateGridViewAdapter();
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //
        gridView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 这里注意要加判断是否为NULL.
                 * 因为在重新加载数据以后会出问题.
                 */
                if (view != null) {
                    mainUpView1.setFocusView(view, mOldView, 1.2f);
                }
                mOldView = view;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFindhandler.removeCallbacksAndMessages(null);
                mSavePos = position; // 保存原来的位置(不要按照我的抄，只是DEMO)
                initGridViewData(new Random().nextInt(3));
                mFindhandler.sendMessageDelayed(mFindhandler.obtainMessage(), 111);
                Toast.makeText(getApplicationContext(), "GridView Item " + position + " pos:" + mSavePos, Toast.LENGTH_LONG).show();
            }
        });
        initGridViewData(new Random().nextInt(3));
        mFirstHandler.sendMessageDelayed(mFirstHandler.obtainMessage(), 188);
    }

    // 延时请求初始位置的item.
    Handler mFirstHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            gridView.setDefualtSelect(2);
        }
    };

    // 更新数据后还原焦点框.
    Handler mFindhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mSavePos != -1) {
                gridView.requestFocusFromTouch();
                gridView.setSelection(mSavePos);
            }
        }
    };

    private void initGridViewData(int position) {
        String text = "position:" + position;
        // 测试数据更新.
        if (position == 0) {
            mCount += 10;
            getData(mCount);
            updateGridViewAdapter();
            text += "-->更新数据3个";
        } else if (position == 1) {
            mCount += 20;
            getData(mCount);
            updateGridViewAdapter();
            text += "-->更新数据100个";
        } else if (position == 2) {
            mCount += 30;
            getData(mCount);
            updateGridViewAdapter();
            text += "-->更新数据2000个";
        } else {
            // ... ...
        }
    }

    public List<String> getData(int count) {
        data = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            String text = "text" + "电影" + i;
            data.add(text);
        }
        return data;
    }

    private void updateGridViewAdapter() {
        mAdapter = new GridViewAdapter(this, data);
        gridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    ///// Adapter 类 start start //////////

    class GridViewAdapter extends BaseAdapter {

        private List<String> mDatas;
        private final LayoutInflater mInflater;

        public GridViewAdapter(Context context, List<String> data) {
            mDatas = data;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_gridview, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }
            viewHolder = (ViewHolder) convertView.getTag();
            bindViewData(position, viewHolder);
            return convertView;
        }

        private void bindViewData(int position, ViewHolder viewHolder) {
            String title = mDatas.get(position);
            viewHolder.titleTv.setText(title);
        }

        class ViewHolder {
            View itemView;
            TextView titleTv;

            public ViewHolder(View view) {
                this.itemView = view;
                this.titleTv = (TextView) view.findViewById(R.id.textView);
            }
        }
    }

    ///// Adapter 类 end end //////////

}