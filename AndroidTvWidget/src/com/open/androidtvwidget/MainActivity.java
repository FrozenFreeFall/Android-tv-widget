package com.open.androidtvwidget;

import com.open.androidtvwidget.adapter.OpenEffectBridge;
import com.open.androidtvwidget.view.MainLayout;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.ReflectItemView;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
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
public class MainActivity extends Activity {

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
		// MainUpView 设置.
		mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
		mOpenEffectBridge = (OpenEffectBridge) mainUpView1.getEffectBridge();
		// mainUpView1 = new MainUpView(getApplicationContext(), gridview_lay);
		// mainUpView1.setUpRectResource(R.drawable.item_highlight);
		// mainUpView1.setUpRectResource(R.drawable.white_light_10);
		mainUpView1.setUpRectResource(R.drawable.test_rectangle);
		mainUpView1.setShadowResource(R.drawable.item_shadow);
		// mainUpView1.setDrawUpRectPadding(12);//getResources().getDimensionPixelSize(R.dimen.px25));
		// mainUpView1.setDrawShadowPadding(0); // 阴影图片设置距离.
		// mainUpView1.setTranDurAnimTime(500);
		MainLayout main_lay11 = (MainLayout) findViewById(R.id.main_lay);
		main_lay11.getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
			@Override
			public void onGlobalFocusChanged(final View oldFocus, final View newFocus) {
				newFocus.bringToFront(); // 防止放大的view被压在下面. (建议使用MainLayout)
				float scale = 1.2f;
				mainUpView1.setFocusView(newFocus, mOldFocus, scale);
				mOldFocus = newFocus; // 4.3以下需要自己保存.
				// 测试是否让边框绘制在下面，还是上面. (建议不要使用此函数)
				// if (newFocus != null) {
				// testTopDemo(newFocus);
				// }
			}
		});
		// Gridview demo 测试.
		findViewById(R.id.gridview_lay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Gridview demo test", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), DemoGridViewActivity.class));
			}
		});
		// ListView demo 测试.
		findViewById(R.id.listview_lay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Listview demo test", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), DemoListViewActivity.class));
			}
		});
		// 键盘测试.
		findViewById(R.id.class_lay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "键盘 demo test", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), DemoKeyBoardActivity.class));
			}
		});
		findViewById(R.id.top_lay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "ViewPager demo test", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), DemoViewPagerActivity.class));
			}
		});
	}

	public void testTopDemo(View newView) {
		// 测试第一个小人放大的效果.
		if (newView.getId() == R.id.gridview_lay) {
			test_top_iv.animate().scaleX(2.0f).scaleY(1.5f).setDuration(300).setListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
					mOpenEffectBridge.setDrawUpRectEnabled(true);
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					mOpenEffectBridge.setDrawUpRectEnabled(false);
					mainUpView1.setDrawUpRectPadding(getResources().getDimensionPixelSize(R.dimen.px22)); // 让移动边框显示出来.
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}
			}).start();
		} else {
			mainUpView1.setDrawUpRectPadding(getResources().getDimensionPixelSize(R.dimen.px25));
			mOpenEffectBridge.setDrawUpRectEnabled(true); //
			test_top_iv.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).setListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
					mOpenEffectBridge.setDrawUpRectEnabled(true);
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					mOpenEffectBridge.setDrawUpRectEnabled(true);
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}
			}).start();
		}
	}

}
