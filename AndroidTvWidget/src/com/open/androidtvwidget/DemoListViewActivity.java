package com.open.androidtvwidget;

import java.util.ArrayList;
import java.util.List;

import com.open.androidtvwidget.view.MainUpView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DemoListViewActivity extends Activity {

	private static final String TAG = "DemoListViewActivity";

	private List<String> data;
	private MainUpView mainUpView1;
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_list_view);
		this.mInflater = LayoutInflater.from(getApplicationContext());
		ListView listView = (ListView) findViewById(R.id.listview);
		mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
		mainUpView1.setUpRectResource(R.drawable.white_light_10);
		mainUpView1.setShadowDrawable(null);
		mainUpView1.setDrawUpRectPadding(10);
		mainUpView1.setTranDurAnimTime(200);
		initData();

		listView.setAdapter(new DemoAdapter());
		//
		listView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (view != null)
					mainUpView1.setFocusView(view, 1.0f);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		listView.getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
			@Override
			public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//				if (newFocus != null) {
//					mainUpView1.setFocusView(newFocus, 1.2f);
//				}
//				if (oldFocus != null) {
//					mainUpView1.setUnFocusView(oldFocus);
//				}
			}
		});
//		listView.setSelection(0);
	}

	public void initData() {
		data = new ArrayList<String>();
		for (int i = 0; i < 105; i++) {
			String text = "item" + i;
			data.add(text);
		}
	}

	public class DemoAdapter extends BaseAdapter {

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
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_listview, null);
				holder.title = (TextView) convertView.findViewById(R.id.textView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(data.get(position));
			return convertView;
		}

		public class ViewHolder {
			public TextView title;
		}
	}

}
