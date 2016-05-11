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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.utils.Utils;
import com.open.androidtvwidget.view.MainUpView;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * GridView Demo测试.
 */
public class DemoGridViewActivity extends Activity {

	private List<Map<String, Object>> data;
	private MainUpView mainUpView1;
	private View mOldView;
	private GridView gridView;
	
	SimpleAdapter simpleAdapter;
	String[] from = { "text" };
	int[] to = { R.id.textView };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_grid_view);

		gridView = (GridView) findViewById(R.id.gridView);
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
					view.bringToFront();
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
				String text = "position:" + position;
				// 测试数据更新.
				if (position == 0) {
					getData(3);
					updateGridViewAdapter();
					text += "-->更新数据3个";
				} else if (position == 1){
					getData(100);
					updateGridViewAdapter();
					text += "-->更新数据100个";
				} else if (position == 2) {
					getData(2000);
					updateGridViewAdapter();
					text += "-->更新数据2000个";
				} else {
					// ... ...
				}
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
			}
		});
		//  pengjunkun <junkun@mgtv.com> 修复.
		// 在布局加咱完成后，设置选中第一个 (test)
		gridView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
					int oldRight, int oldBottom) {
				if (gridView.getChildCount() > 0) {
					gridView.setSelection(0);
					View newView = gridView.getChildAt(0);
					newView.bringToFront();
					mainUpView1.setFocusView(newView, 1.2f);
					mOldView = gridView.getChildAt(0);
				}
			}
		});
	}

	public List<Map<String, Object>> getData(int count) {
		data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < count; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", "电影" + i);
			data.add(map);
		}
		return data;
	}
	
	private void updateGridViewAdapter() {
		simpleAdapter = new SimpleAdapter(this, data, R.layout.item_gridview, from, to);
		gridView.setAdapter(simpleAdapter);
		simpleAdapter.notifyDataSetChanged();
	}
	
}
