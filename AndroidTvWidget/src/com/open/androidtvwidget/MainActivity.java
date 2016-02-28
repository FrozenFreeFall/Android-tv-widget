package com.open.androidtvwidget;

import com.open.androidtvwidget.view.MainLayout;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.ReflectItemView;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main);

		ReflectItemView gridview_lay = (ReflectItemView) findViewById(R.id.gridview_lay);
		test_top_iv = findViewById(R.id.test_top_iv);
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
		// MainUpView 设置.
		mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
		// mainUpView1 = new MainUpView(getApplicationContext(), gridview_lay);
//		 mainUpView1.setUpRectResource(R.drawable.item_highlight);
//		 mainUpView1.setUpRectResource(R.drawable.white_light_10);
		 mainUpView1.setUpRectResource(R.drawable.test_rectangle);
		 mainUpView1.setShadowResource(R.drawable.item_shadow);
//		mainUpView1.setDrawUpRectPadding(12);//getResources().getDimensionPixelSize(R.dimen.px25));
//		 mainUpView1.setDrawShadowPadding(0); // 阴影图片设置距离.
		// mainUpView1.setTranDurAnimTime(500);
		MainLayout main_lay11 = (MainLayout) findViewById(R.id.main_lay);
		main_lay11.getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
			@Override
			public void onGlobalFocusChanged(final View oldFocus, final View newFocus) {
				newFocus.bringToFront(); // 防止放大的view被压在下面.
				mainUpView1.setFocusView(newFocus, oldFocus, 1.2f);
				mainUpView1.setDrawUpRectEnabled(true);
				if (newFocus != null) {
//					testTopDemo(newFocus);
				}
			}
		});
	}

	public void testTopDemo(View newView) {
		// 测试第一个小人放大的效果.
		if (newView.getId() == R.id.gridview_lay) {
			test_top_iv.animate().scaleX(2.0f).scaleY(1.5f).setDuration(300).setListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
					mainUpView1.setDrawUpRectEnabled(true);
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					mainUpView1.setDrawUpRectEnabled(false);
					mainUpView1.setDrawUpRectPadding(getResources().getDimensionPixelSize(R.dimen.px22)); // 让移动边框显示出来.
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}
			}).start();
		} else {
			mainUpView1.setDrawUpRectPadding(getResources().getDimensionPixelSize(R.dimen.px25));
			mainUpView1.setDrawUpRectEnabled(true); //
			test_top_iv.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).setListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
					mainUpView1.setDrawUpRectEnabled(true);
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					mainUpView1.setDrawUpRectEnabled(true);
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}
			}).start();
		}
	}

}
