package com.open.androidtvwidget;

import java.util.ArrayList;
import java.util.List;

import com.open.androidtvwidget.adapter.EffectNoDrawBridge;
import com.open.androidtvwidget.adapter.OpenEffectBridge;
import com.open.androidtvwidget.adapter.OpenEffectBridge.NewAnimatorListener;
import com.open.androidtvwidget.adapter.OpenTabTitleAdapter;
import com.open.androidtvwidget.utils.Utils;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.OpenTabHost;
import com.open.androidtvwidget.view.OpenTabHost.OnTabSelectListener;
import com.open.androidtvwidget.view.ReflectItemView;
import com.open.androidtvwidget.view.TextViewWithTTF;

import android.animation.Animator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;

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
		// 初始化标题栏.
		initAllTitleBar();
		// 初始化viewpager.
		initAllViewPager();
		// 初始化.
		initViewMove();
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
		view2 = inflater.inflate(R.layout.test_page2, null);
		view3 = inflater.inflate(R.layout.test_page3, null);
		view4 = inflater.inflate(R.layout.test_page4, null);
		viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);
		viewpager.setAdapter(new DemoPagerAdapter());
		//
		viewpager.getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
			@Override
			public void onGlobalFocusChanged(View oldFocus, View newFocus) {
				int pos = viewpager.getCurrentItem();
				final MainUpView mainUpView = (MainUpView) viewList.get(pos).findViewById(R.id.mainUpView1);
				final OpenEffectBridge bridge = (OpenEffectBridge) mainUpView.getEffectBridge();
				if (!(newFocus instanceof ReflectItemView)) {
					mainUpView.setUnFocusView(mOldFocus);
					bridge.setVisibleWidget(true);
					mSavebridge = null;
				} else {
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
				switchFocusTab(mOpenTabHost, position);
				// 这里加入是为了防止移动过去后，移动的边框还在的问题.
				// 从标题栏翻页就能看到上次的边框.
				if (position > 0) {
					MainUpView mainUpView = (MainUpView) viewList.get(position - 1).findViewById(R.id.mainUpView1);
					OpenEffectBridge bridge = (OpenEffectBridge) mainUpView.getEffectBridge();
					bridge.setVisibleWidget(true);
				}
				if (position < (viewpager.getChildCount() - 1)) {
					MainUpView mainUpView = (MainUpView) viewList.get(position + 1).findViewById(R.id.mainUpView1);
					OpenEffectBridge bridge = (OpenEffectBridge) mainUpView.getEffectBridge();
					bridge.setVisibleWidget(true);
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

	public void initViewMove() {
		for (View view : viewList) {
			MainUpView mainUpView = (MainUpView) view.findViewById(R.id.mainUpView1);
			//
			if (Utils.getSDKVersion() == 17) {
				mainUpView.setEffectBridge(new EffectNoDrawBridge()); // 4.3以下版本边框移动.
				mainUpView.setUpRectResource(R.drawable.white_light_10); // 设置移动边框的图片.
				mainUpView.setDrawUpRectPadding(new Rect(25, 25, 23, 23)); // 边框图片设置间距.
				EffectNoDrawBridge bridget = (EffectNoDrawBridge) mainUpView.getEffectBridge();
				bridget.setTranDurAnimTime(200);
			} else {
				mainUpView.setUpRectResource(R.drawable.test_rectangle); // 设置移动边框的图片.
				mainUpView.setShadowResource(R.drawable.item_shadow); // 设置移动边框的阴影.
				OpenEffectBridge bridget = (OpenEffectBridge) mainUpView.getEffectBridge();
				bridget.setTranDurAnimTime(250);
			}
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
