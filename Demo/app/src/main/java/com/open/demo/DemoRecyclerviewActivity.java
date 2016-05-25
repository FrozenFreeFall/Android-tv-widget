package com.open.demo;

import com.open.androidtvwidget.bridge.RecyclerViewBridge;
import com.open.androidtvwidget.recycle.GridLayoutManagerTV;
import com.open.androidtvwidget.recycle.LinearLayoutManagerTV;
import com.open.androidtvwidget.recycle.OnChildSelectedListener;
import com.open.androidtvwidget.recycle.RecyclerViewTV;
import com.open.androidtvwidget.view.MainUpView;
import com.open.demo.adapter.HeaderGridAdapter;
import com.open.demo.adapter.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;

/**
 * recyclerview Demo.
 * 
 * @author hailongqiu
 *
 */
public class DemoRecyclerviewActivity extends Activity implements OnClickListener, OnFocusChangeListener {

	Context mContext;
	RecyclerViewTV recyclerView;
	MainUpView mainUpView1;
	RecyclerViewBridge mRecyclerViewBridge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_recyclerview_activity);
		mContext = DemoRecyclerviewActivity.this;
		recyclerView = (RecyclerViewTV) findViewById(R.id.recyclerView);
		mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
		mainUpView1.setEffectBridge(new RecyclerViewBridge());
		//
		mRecyclerViewBridge = (RecyclerViewBridge) mainUpView1.getEffectBridge();
		mRecyclerViewBridge.setUpRectResource(R.drawable.test_rectangle);
		//
		testHeaderGridLayout();
		initAllViewEvents();
	}

	private View oldView;

	private void initAllViewEvents() {
		findViewById(R.id.h_liner_btn).setOnClickListener(this);
		findViewById(R.id.v_liner_btn).setOnClickListener(this);
		findViewById(R.id.h_grid_btn).setOnClickListener(this);
		findViewById(R.id.v_grid_btn).setOnClickListener(this);
		findViewById(R.id.head_grid_btn).setOnClickListener(this);
		//
		findViewById(R.id.h_liner_btn).setOnFocusChangeListener(this);
		findViewById(R.id.v_liner_btn).setOnFocusChangeListener(this);
		findViewById(R.id.h_grid_btn).setOnFocusChangeListener(this);
		findViewById(R.id.v_grid_btn).setOnFocusChangeListener(this);
		findViewById(R.id.head_grid_btn).setOnFocusChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.h_liner_btn: // 横向 liner layout.
			testRecyclerViewLinerLayout(LinearLayoutManager.HORIZONTAL);
			break;
		case R.id.v_liner_btn:
			testRecyclerViewLinerLayout(LinearLayoutManager.VERTICAL);
			break;
		case R.id.h_grid_btn: // 横向 grid layout.
			testRecyclerViewGridLayout(GridLayoutManager.HORIZONTAL);
			break;
		case R.id.v_grid_btn:
			testRecyclerViewGridLayout(GridLayoutManager.VERTICAL);
			break;
		case R.id.head_grid_btn: // 带header的grid.
			testHeaderGridLayout();
			break;
		default:
			break;
		}
	}

	/**
	 * 测试LinerLayout.
	 */
	public void testRecyclerViewLinerLayout(int orientation) {
		LinearLayoutManagerTV layoutManager = new LinearLayoutManagerTV(this);
		// 保持一个位置（上，下头，尾的差距填补).
		if (orientation == LinearLayoutManager.HORIZONTAL) {
			layoutManager.setLeftPadding((int) getResources().getDimension(R.dimen.px250));
			layoutManager.setRightPadding((int) getResources().getDimension(R.dimen.px150));
		} else {
			layoutManager.setBottomPadding((int) getResources().getDimension(R.dimen.px250));
			layoutManager.setTopPadding((int) getResources().getDimension(R.dimen.px150));
		}
		layoutManager.setOnChildSelectedListener(new OnChildSelectedListener() {
			@Override
			public void onChildSelected(RecyclerView parent, View focusview, int position, int dy) {
				focusview.bringToFront();
				mRecyclerViewBridge.setFocusView(focusview, oldView, 1.2f);
				oldView = focusview;
			}
		});
		layoutManager.setOrientation(orientation);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setFocusable(false);
		final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(100);
		recyclerView.setAdapter(recyclerViewAdapter);
	}

	/**
	 * 测试GridLayout.
	 */
	private void testRecyclerViewGridLayout(int orientation) {
		GridLayoutManagerTV gridlayoutManager = new GridLayoutManagerTV(this, 4);
		if (orientation == LinearLayoutManager.HORIZONTAL) {
			gridlayoutManager.setLeftPadding((int) getResources().getDimension(R.dimen.px250));
			gridlayoutManager.setRightPadding((int) getResources().getDimension(R.dimen.px150));
		} else {
			gridlayoutManager.setBottomPadding((int) getResources().getDimension(R.dimen.px250));
			gridlayoutManager.setTopPadding((int) getResources().getDimension(R.dimen.px150));
		}
		gridlayoutManager.setOnChildSelectedListener(new OnChildSelectedListener() {
			@Override
			public void onChildSelected(RecyclerView parent, View focusview, int position, int dy) {
				focusview.bringToFront();
				mRecyclerViewBridge.setFocusView(focusview, oldView, 1.2f);
				oldView = focusview;
			}
		});
		gridlayoutManager.setOrientation(orientation);
		recyclerView.setLayoutManager(gridlayoutManager);
		recyclerView.setFocusable(false);
		final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(100);
		recyclerView.setAdapter(recyclerViewAdapter);
	}

	/**
	 * 测试带标题栏的grid.
	 */
	private void testHeaderGridLayout() {
		final GridLayoutManagerTV gridlayoutManager = new GridLayoutManagerTV(this, 5);
		gridlayoutManager.setOnChildSelectedListener(new OnChildSelectedListener() {
			@Override
			public void onChildSelected(RecyclerView parent, View focusview, int position, int dy) {
				focusview.bringToFront();
				mRecyclerViewBridge.setFocusView(focusview, 1.4f, 1.2f);
				mRecyclerViewBridge.setUnFocusView(oldView);
				oldView = focusview;
			}
		});
		// 保持一个位置（上，下头，尾的差距填补).
		gridlayoutManager.setBottomPadding((int) getResources().getDimension(R.dimen.px250));
		gridlayoutManager.setTopPadding((int) getResources().getDimension(R.dimen.px150));
		//
		gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
		// recyclerView.setHasFixedSize(true); // 保持固定的大小
		recyclerView.setLayoutManager(gridlayoutManager);
		recyclerView.setFocusable(false);
		final HeaderGridAdapter mHeaderGridAdapter = new HeaderGridAdapter(100);
		recyclerView.setAdapter(mHeaderGridAdapter);
		gridlayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				return mHeaderGridAdapter.isHeader(position) ? gridlayoutManager.getSpanCount() : 1;
			}
		});
	}

	@Override
	public void onFocusChange(View focusview, boolean hasFocus) {
		mRecyclerViewBridge.setFocusView(focusview, oldView, 1.0f);
		oldView = focusview;
	}

}
