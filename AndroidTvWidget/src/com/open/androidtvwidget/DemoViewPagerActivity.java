package com.open.androidtvwidget;

import java.util.ArrayList;
import java.util.List;

import com.open.androidtvwidget.view.MainUpView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;

public class DemoViewPagerActivity extends Activity {

	private List<View> viewList;// view数组
	private View view1, view2, view3;
	ViewPager viewpager;
	MainUpView mainUpView1;
	MainUpView mainUpView2;
	MainUpView mainUpView3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_viewpager_activity);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		//
		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.test_main, null);
		view2 = inflater.inflate(R.layout.test_main, null);
		view3 = inflater.inflate(R.layout.test_main, null);

		viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewpager.setAdapter(new DemoPagerAdapter());
		initViewMove();
	}

	public void initViewMove() {
		mainUpView1 = (MainUpView) view1.findViewById(R.id.mainUpView1);
		mainUpView1.setUpRectResource(R.drawable.test_rectangle);
		mainUpView1.setShadowResource(R.drawable.item_shadow);
		ViewGroup mainLay1 = (ViewGroup) view1.findViewById(R.id.main_lay);
		for (int i = 0; i < mainLay1.getChildCount(); i++)
			mainLay1.getChildAt(i).setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					v.bringToFront();
					if (hasFocus)
						mainUpView1.setFocusView(v, 1.2f);
					else
						mainUpView1.setUnFocusView(v);
				}
			});

		mainUpView2 = (MainUpView) view2.findViewById(R.id.mainUpView1);
		mainUpView2.setUpRectResource(R.drawable.test_rectangle);
		mainUpView2.setShadowResource(R.drawable.item_shadow);
		ViewGroup mainLay2 = (ViewGroup) view2.findViewById(R.id.main_lay);
		for (int i = 0; i < mainLay2.getChildCount(); i++)
			mainLay2.getChildAt(i).setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					v.bringToFront();
					if (hasFocus)
						mainUpView2.setFocusView(v, 1.1f);
					else
						mainUpView2.setUnFocusView(v);
				}
			});

		mainUpView3 = (MainUpView) view3.findViewById(R.id.mainUpView1);
		mainUpView3.setUpRectResource(R.drawable.test_rectangle);
		mainUpView3.setShadowResource(R.drawable.item_shadow);
		ViewGroup mainLay3 = (ViewGroup) view3.findViewById(R.id.main_lay);
		for (int i = 0; i < mainLay3.getChildCount(); i++)
			mainLay3.getChildAt(i).setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					v.bringToFront();
					if (hasFocus)
						mainUpView3.setFocusView(v, 1.0f);
					else
						mainUpView3.setUnFocusView(v);
				}
			});

	}

	class DemoPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));
		}

		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position));
			return viewList.get(position);
		};

	}

}
