package com.open.androidtvwidget;

import java.util.ArrayList;
import java.util.List;

import com.open.androidtvwidget.adapter.OpenBaseAnimAdapter;
import com.open.androidtvwidget.view.MainUpView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
		OpenBaseAnimAdapter baseAnimAdapter = ((OpenBaseAnimAdapter)mainUpView1.getAnimAdapter());
		baseAnimAdapter.setTranDurAnimTime(200);
		initData();

		listView.setAdapter(new DemoAdapter());
		//
		listView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (view != null) {
					mainUpView1.setFocusView(view, 1.0f);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), "position : " + position, Toast.LENGTH_LONG).show();
			}
		});
		// 要与不要都没有什么鸟用.
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
