package com.open.androidtvwidget.menu;

import com.open.androidtvwidget.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 菜单ListView.
 * 
 * @author hailongqiu
 *
 */
public class OpenMenuView extends RelativeLayout {

	public OpenMenuView(Context context) {
		super(context);
		init(context, null);
	}

	public OpenMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private ListView mMenuListView;

	private void init(Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.open_listview_view, this, true);
		mMenuListView = (ListView) findViewById(R.id.menu_listview);
	}

	public ListView getMenuListView() {
		return mMenuListView;
	}

	public void hideListMneuView() {
		setVisibility(View.GONE);
	}
	
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		if (action == KeyEvent.ACTION_DOWN) {
			int keyCode = event.getKeyCode();
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				return true;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				hideListMneuView();
				return true;
			case KeyEvent.KEYCODE_DPAD_UP:
				getMenuListView().dispatchKeyEvent(event);
				return true;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				getMenuListView().dispatchKeyEvent(event);
				return true;
			default:
				break;
			}
		}
		return super.dispatchKeyEvent(event);
	}

}
