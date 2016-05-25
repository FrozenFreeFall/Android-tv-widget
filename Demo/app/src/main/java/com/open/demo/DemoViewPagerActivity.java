package com.open.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.bridge.OpenEffectBridge;
import com.open.androidtvwidget.bridge.OpenEffectBridge.NewAnimatorListener;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.OpenTabHost;
import com.open.androidtvwidget.view.OpenTabHost.OnTabSelectListener;
import com.open.androidtvwidget.view.ReflectItemView;
import com.open.androidtvwidget.view.TextViewWithTTF;
import com.open.demo.adapter.OpenTabTitleAdapter;

import android.animation.Animator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

/**
 * ViewPager demo：
 * 需要注意翻页的时候.(前一页，后一页焦点还在的问题)
 * 还有标题焦点问题.
 * 等等.
 * @author hailongqiu
 *
 */
public class DemoViewPagerActivity extends Activity implements OnTabSelectListener {

	private List<View> viewList;// view数组
	private View view1, view2, view3, view4;
	ViewPager viewpager;
	OpenTabHost mOpenTabHost;
	OpenTabTitleAdapter mOpenTabTitleAdapter;
	
	private OpenEffectBridge mSavebridge;
	private View mOldFocus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_viewpager_activity);
		//
		OPENLOG.initTag("hailongqiu", true); // 测试LOG输出.
		// 初始化标题栏.
		initAllTitleBar();
		// 初始化viewpager.
		initAllViewPager();
		// 初始化.
		initViewMove();
		// 初始化gridview
		initGridview();
	}

	private void initAllTitleBar() {
		mOpenTabHost = (OpenTabHost) findViewById(R.id.openTabHost);
		mOpenTabTitleAdapter = new OpenTabTitleAdapter();
		mOpenTabHost.setOnTabSelectListener(this);
		mOpenTabHost.setAdapter(mOpenTabTitleAdapter);
	}

	private void initAllViewPager() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		//
		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.test_page1, null);
		view2 = inflater.inflate(R.layout.test_page2, null); // gridview demo.
		view3 = inflater.inflate(R.layout.test_page3, null);
		view4 = inflater.inflate(R.layout.test_page4, null);
		viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);
		viewpager.setAdapter(new DemoPagerAdapter());
		// 全局焦点监听.
		viewpager.getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
			@Override
			public void onGlobalFocusChanged(View oldFocus, View newFocus) {
				int pos = viewpager.getCurrentItem();
				final MainUpView mainUpView = (MainUpView) viewList.get(pos).findViewById(R.id.mainUpView1);
				final OpenEffectBridge bridge = (OpenEffectBridge) mainUpView.getEffectBridge();
				if (!(newFocus instanceof ReflectItemView)) { // 不是 ReflectitemView 的话.
					OPENLOG.D("onGlobalFocusChanged no ReflectItemView + " + (newFocus instanceof GridView));
					mainUpView.setUnFocusView(mOldFocus);
					bridge.setVisibleWidget(true); // 隐藏.
					mSavebridge = null;
					// 处理gridview的边框.
					if ((newFocus instanceof GridView) && pos == 1) {
						View newView = null;
						bridge.setVisibleWidget(false);
						newView = gridView.getSelectedView();
						//
						if (newView != null) {
							newView.bringToFront();
							mainUpView.setFocusView(newView, mOldFocus, 1.2f);
						}
						mOldFocus = newView;
						return; // 反之最后的 mOldFocus 出问题.
					}
				} else {
					OPENLOG.D("onGlobalFocusChanged yes ReflectItemView");
					newFocus.bringToFront();
					mSavebridge = bridge;
					// 动画结束才设置边框显示，
					// 是位了防止翻页从另一边跑出来的问题.
					bridge.setOnAnimatorListener(new NewAnimatorListener() {
						@Override
						public void onAnimationStart(OpenEffectBridge bridge, View view, Animator animation) {
						}
						
						@Override
						public void onAnimationEnd(OpenEffectBridge bridge1, View view, Animator animation) {
							if (mSavebridge == bridge1)
								bridge.setVisibleWidget(false);
						}
					});
					float scale = 1.2f;
					// test scale.
					if (pos == 1)
						scale = 1.3f;
					else if (pos == 2)
						scale = 1.0f;
					else if (pos == 3)
						scale = 1.1f;
					mainUpView.setFocusView(newFocus, mOldFocus, scale);
				}
				mOldFocus = newFocus;
			}
		});
		viewpager.setOffscreenPageLimit(4);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				OPENLOG.D("onPageSelected position:" + position);
				switchFocusTab(mOpenTabHost, position);
				// 这里加入是为了防止移动过去后，移动的边框还在的问题.
				// 从标题栏翻页就能看到上次的边框.
				if (position > 0) {
					MainUpView mainUpView0 = (MainUpView) viewList.get(position - 1).findViewById(R.id.mainUpView1);
					OpenEffectBridge bridge0 = (OpenEffectBridge) mainUpView0.getEffectBridge();
					bridge0.setVisibleWidget(true);
				}
				//
				if (position < (viewpager.getChildCount() - 1)) {
					MainUpView mainUpView1 = (MainUpView) viewList.get(position + 1).findViewById(R.id.mainUpView1);
					OpenEffectBridge bridge1 = (OpenEffectBridge) mainUpView1.getEffectBridge();
					bridge1.setVisibleWidget(true);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	GridView gridView;
	SimpleAdapter simpleAdapter;
	String[] from = { "text" };
	int[] to = { R.id.textView };
	private List<Map<String, Object>> data;
	
	private void initGridview() {
		// 初始化gridview-移动边框
		gridView = (GridView) view2.findViewById(R.id.gridView);
		final MainUpView mainUpView = (MainUpView) view2.findViewById(R.id.mainUpView1);
		mainUpView.setDrawUpRectPadding(new Rect(10, 10, 10, -55)); 
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 初始化数据.
		getData(100);
		// 初始化gridview.
		updateGridViewAdapter();
		// 监听gridview选中方框.
		gridView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				OPENLOG.D("onItemSelected position:" + position + " " + (mOldFocus.getParent() instanceof GridView));
				int pos = viewpager.getCurrentItem();
				final MainUpView mainUpView = (MainUpView) viewList.get(pos).findViewById(R.id.mainUpView1);
				final OpenEffectBridge bridge = (OpenEffectBridge) mainUpView.getEffectBridge();
				bridge.setVisibleWidget(false);
				mainUpView.setFocusView(view, mOldFocus, 1.2f);
				mOldFocus = view;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				OPENLOG.D("onNothingSelected");
			}
		});
	}
	
	private void updateGridViewAdapter() {
		simpleAdapter = new SimpleAdapter(this, data, R.layout.item_gridview, from, to);
		gridView.setAdapter(simpleAdapter);
		simpleAdapter.notifyDataSetChanged();
	}
	
	private List<Map<String, Object>> getData(int count) {
		data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < count; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", "电影" + i);
			data.add(map);
		}
		return data;
	}
	
	public void initViewMove() {
		for (View view : viewList) {
			MainUpView mainUpView = (MainUpView) view.findViewById(R.id.mainUpView1);
			// 建议使用 noDrawBridge.
			mainUpView.setEffectBridge(new EffectNoDrawBridge()); // 4.3以下版本边框移动.
			mainUpView.setUpRectResource(R.drawable.white_light_10); // 设置移动边框的图片.
			mainUpView.setDrawUpRectPadding(new Rect(25, 25, 23, 23)); // 边框图片设置间距.
			EffectNoDrawBridge bridget = (EffectNoDrawBridge) mainUpView.getEffectBridge();
			bridget.setTranDurAnimTime(200);
		}
	}

	@Override
	public void onTabSelect(OpenTabHost openTabHost, View titleWidget, int postion) {
		switchTab(openTabHost, postion);
		if (viewpager != null) {
			viewpager.setCurrentItem(postion);
		}
	}

	/**
	 * demo
	 * 设置标题栏被选中，<br>
	 * 但是没有焦点的状态.
	 */
	public void switchFocusTab(OpenTabHost openTabHost, int postion) {
		List<View> viewList = openTabHost.getAllTitleView();
		if (viewList != null && viewList.size() > 0) {
			for (int i = 0; i < viewList.size(); i++) {
				View viewC = viewList.get(i);
				if (i == postion) {
					viewC.setSelected(true);
				} else {
					viewC.setSelected(false);
				}
			}
		}
		switchTab(openTabHost, postion);
	}

	/**
	 * demo
	 * 将标题栏的文字颜色改变. <br>
	 * 你可以写自己的东西，我这里只是DEMO.
	 */
	public void switchTab(OpenTabHost openTabHost, int postion) {
		List<View> viewList = openTabHost.getAllTitleView();
		for (int i = 0; i < viewList.size(); i++) {
			TextViewWithTTF view = (TextViewWithTTF) openTabHost.getTitleViewIndexAt(i);
			if (view != null) {
				Resources res = view.getResources();
				if (res != null) {
					if (i == postion) {
						view.setTextColor(res.getColor(android.R.color.white));
						view.setTypeface(null, Typeface.BOLD);
					} else {
						view.setTextColor(res.getColor(R.color.white_50));
						view.setTypeface(null, Typeface.NORMAL);
					}
				}
			}
		}
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
