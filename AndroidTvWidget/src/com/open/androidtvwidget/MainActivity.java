package com.open.androidtvwidget;

import com.open.androidtvwidget.adapter.OpenEffectBridge;
import com.open.androidtvwidget.view.MainLayout;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.ReflectItemView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.widget.Toast;

/**
 * DEMO测试.
 */
public class MainActivity extends Activity implements OnClickListener {

	MainUpView mainUpView1;
	View test_top_iv;
	OpenEffectBridge mOpenEffectBridge;
	View mOldFocus; // 4.3以下版本需要自己保存.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main);
		//
		ReflectItemView gridview_lay = (ReflectItemView) findViewById(R.id.gridview_lay);
		test_top_iv = findViewById(R.id.test_top_iv);
		/* MainUpView 设置. */
		mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
		mOpenEffectBridge = (OpenEffectBridge) mainUpView1.getEffectBridge();
		// mainUpView1 = new MainUpView(getApplicationContext(), gridview_lay); // 手动添加(test)
		// mainUpView1.setEffectBridge(new EffectNoDrawBridge()); // 4.3以下版本边框移动.
		// mainUpView1.setUpRectResource(R.drawable.item_highlight); // 设置移动边框的图片.(test)
		// mainUpView1.setUpRectResource(R.drawable.white_light_10); // 设置移动边框的图片. (test)
		mainUpView1.setUpRectResource(R.drawable.test_rectangle); // 设置移动边框的图片.
		mainUpView1.setShadowResource(R.drawable.item_shadow); // 设置移动边框的阴影.
		// mainUpView1.setDrawUpRectPadding(-25); // 边框图片设置间距.
		// mainUpView1.setDrawShadowPadding(0); // 阴影图片设置距离.
		// mOpenEffectBridge.setTranDurAnimTime(500); // 动画时间.

		MainLayout main_lay11 = (MainLayout) findViewById(R.id.main_lay);
		main_lay11.getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
			@Override
			public void onGlobalFocusChanged(final View oldFocus, final View newFocus) {
				newFocus.bringToFront(); // 防止放大的view被压在下面. (建议使用MainLayout)
				float scale = 1.2f;
				mainUpView1.setFocusView(newFocus, mOldFocus, scale);
				mOldFocus = newFocus; // 4.3以下需要自己保存.
				// 测试是否让边框绘制在下面，还是上面. (建议不要使用此函数)
				if (newFocus != null) {
					testTopDemo(newFocus);
				}
			}
		});
		// test demo.
		findViewById(R.id.gridview_lay).setOnClickListener(this);
		findViewById(R.id.listview_lay).setOnClickListener(this);
		findViewById(R.id.keyboard_lay).setOnClickListener(this);
		findViewById(R.id.viewpager_lay).setOnClickListener(this);
	}

	public void testTopDemo(View newView) {
		// 测试第一个小人放大的效果.
		if (newView.getId() == R.id.gridview_lay) {
		} else {
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.gridview_lay:
			Toast.makeText(getApplicationContext(), "Gridview demo test", Toast.LENGTH_LONG).show();
			startActivity(new Intent(getApplicationContext(), DemoGridViewActivity.class));
			break;
		case R.id.listview_lay:
			Toast.makeText(getApplicationContext(), "Listview demo test", Toast.LENGTH_LONG).show();
			startActivity(new Intent(getApplicationContext(), DemoListViewActivity.class));
			break;
		case R.id.keyboard_lay:
			Toast.makeText(getApplicationContext(), "键盘 demo test", Toast.LENGTH_LONG).show();
			startActivity(new Intent(getApplicationContext(), DemoKeyBoardActivity.class));
			break;
		case R.id.viewpager_lay:
			Toast.makeText(getApplicationContext(), "ViewPager demo test", Toast.LENGTH_LONG).show();
			startActivity(new Intent(getApplicationContext(), DemoViewPagerActivity.class));
			break;
		default:
			break;
		}
	}

}
