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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_grid_view);

		final GridView gridView = (GridView) findViewById(R.id.gridView);
		mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
		
		/**
		 * android 4.2有问题.
		 * 需要使用EffectNoDrawBridge.
		 */
		if (Utils.getSDKVersion() == 17) {
			mainUpView1.setEffectBridge(new EffectNoDrawBridge()); // 4.3以下版本边框移动.
			EffectNoDrawBridge bridget = (EffectNoDrawBridge) mainUpView1.getEffectBridge();
			bridget.setTranDurAnimTime(200);
		} 
		
		mainUpView1.setUpRectResource(R.drawable.white_light_10);
		mainUpView1.setDrawUpRectPadding(12);
		
		getData();

		String[] from = { "text" };
		int[] to = { R.id.textView };

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.item_gridview, from, to);
		gridView.setAdapter(simpleAdapter);
		simpleAdapter.notifyDataSetChanged();
		//
		gridView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				view.bringToFront();
				mainUpView1.setFocusView(view, mOldView, 1.2f);
				mOldView = view;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), "position : " + position, Toast.LENGTH_LONG).show();
			}
		});
		//  pengjunkun <junkun@mgtv.com> 修复.
		// 在布局加咱完成后，设置选中第一个
		gridView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
					int oldRight, int oldBottom) {
				if (gridView.getChildCount() > 0) {
					gridView.setSelection(0);
					mainUpView1.setFocusView(gridView.getChildAt(0), 1.2f);
					mOldView = gridView.getChildAt(0);
				}
			}
		});
	}

	public List<Map<String, Object>> getData() {
		data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 200; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", "item" + i);
			data.add(map);
		}
		return data;
	}
}
