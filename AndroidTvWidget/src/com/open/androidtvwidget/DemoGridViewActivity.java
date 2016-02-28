package com.open.androidtvwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.SmoothGridView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleAdapter;

/**
 * GridView Demo测试.
 */
public class DemoGridViewActivity extends Activity {

	private List<Map<String, Object>> data;
	private MainUpView mainUpView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_grid_view);

		SmoothGridView gridView = (SmoothGridView) findViewById(R.id.gridView);
		mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
		mainUpView1.setUpRectResource(R.drawable.white_light_10);
		mainUpView1.setShadowDrawable(null);
		mainUpView1.setDrawUpRectPadding(10);
		
		getData();
		
		String[] from = { "text" };
		int[] to = { R.id.textView };

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);
		gridView.setAdapter(simpleAdapter);
		simpleAdapter.notifyDataSetChanged();
		//
//		gridView.setOnItemSelectedListener(new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				mainUpView1.setFocusView(view, 1.0f);
//			}
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//			}
//		});
		gridView.getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
			@Override
			public void onGlobalFocusChanged(View oldFocus, View newFocus) {
				if (newFocus != null) {
					mainUpView1.setFocusView(newFocus, 1.0f);
				}
				if (oldFocus != null) {
					mainUpView1.setUnFocusView(oldFocus);
				}
			}
		});
	}

	public List<Map<String, Object>> getData() {
		data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 100; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", "item" + i);
			data.add(map);
		}
		return data;
	}
}
