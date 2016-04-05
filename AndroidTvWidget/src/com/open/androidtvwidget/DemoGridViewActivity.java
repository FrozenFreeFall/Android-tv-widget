package com.open.androidtvwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.open.androidtvwidget.adapter.EffectNoDrawBridge;
import com.open.androidtvwidget.utils.Utils;
import com.open.androidtvwidget.view.MainUpView;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
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

		GridView gridView = (GridView) findViewById(R.id.gridView);
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
		// 不知道为何设置 0 不显示.(暂时无解)
		gridView.setSelection(1);
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
