package com.open.demo;

import java.util.ArrayList;
import java.util.List;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.utils.Utils;
import com.open.androidtvwidget.view.ListViewTV;
import com.open.androidtvwidget.view.MainUpView;

import android.app.Activity;
import android.graphics.Rect;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DemoListViewActivity extends Activity {

	private static final String TAG = "DemoListViewActivity";

	private List<String> data;
	private MainUpView mainUpView1;
	private LayoutInflater mInflater;
	private View mOldView;
	private ListViewTV listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_list_view);
		this.mInflater = LayoutInflater.from(getApplicationContext());
		listView = (ListViewTV) findViewById(R.id.listview);
		mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
		// 默认是 OpenEff...，建议使用 NoDraw... ...
		mainUpView1.setEffectBridge(new EffectNoDrawBridge()); 
		EffectNoDrawBridge bridget = (EffectNoDrawBridge) mainUpView1.getEffectBridge();
		bridget.setTranDurAnimTime(200);
		//
		mainUpView1.setUpRectResource(R.drawable.white_light_10); // 设置移动边框的图片.
		mainUpView1.setDrawUpRectPadding(new Rect(25, 25, 23, 23)); // 边框图片设置间距.
		//
		initData();
		//
		listView.setAdapter(new DemoAdapter());
		//
		listView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (view != null) {
					view.bringToFront();
					mainUpView1.setFocusView(view, mOldView, 1.2f);
					mOldView = view;
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
		// 延时请求其它位置的item.
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				listView.setDefualtSelect(2);
			}
		};
		handler.sendMessageDelayed(handler.obtainMessage(), 188);
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
