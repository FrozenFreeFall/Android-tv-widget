package com.open.androidtvwidget;

import com.open.androidtvwidget.view.MainLayout;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.ReflectItemView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * DEMO测试.
 */
public class MainActivity extends Activity implements OnFocusChangeListener {

	MainUpView mainUpView1;
	View test_top_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Gridview demo 测试.
		ReflectItemView relayout11 = (ReflectItemView) findViewById(R.id.relayout11);
		findViewById(R.id.gridview_flayt1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Gridview demo test", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), DemoGridViewActivity.class));
			}
		});
		mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
		// mainUpView1 = new MainUpView(getApplicationContext(), relayout11);
		mainUpView1.setUpRectResource(R.drawable.white_light_10);
		mainUpView1.setShadowDrawable(null);
		mainUpView1.setDrawUpRectPadding(10);
//		mainUpView1.setTranDurAnimTime(500);

		MainLayout main_lay11 = (MainLayout) findViewById(R.id.main_lay11);
		main_lay11.getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
			@Override
			public void onGlobalFocusChanged(View oldFocus, View newFocus) {
				if (newFocus != null) {
					mainUpView1.setFocusView(newFocus, 1.2f);
				}
				if (oldFocus != null) {
					mainUpView1.setUnFocusView(oldFocus);
				}
				// 测试第一个小人放大的效果.
				if (newFocus.getId() == R.id.relayout11) {
					mainUpView1.setDrawUpRectEnabled(false);
					test_top_iv.animate().scaleX(1.2f).scaleY(1.4f).setDuration(500).start();
				} else {
					mainUpView1.setDrawUpRectEnabled(true);
					if (oldFocus != null) {
						test_top_iv.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
					}
				}
			}
		});
		// final ReflectItemView relayout1 = (ReflectItemView)
		// findViewById(R.id.relayout11);
		// final ReflectItemView relayout2 = (ReflectItemView)
		// findViewById(R.id.relayout2);
		// final ReflectItemView relayout3 = (ReflectItemView)
		// findViewById(R.id.relayout3);
		// final ReflectItemView relayout11122 = (ReflectItemView)
		// findViewById(R.id.relayout11122);
		// final ReflectItemView relayout1112233 = (ReflectItemView)
		// findViewById(R.id.relayout1112233);

		test_top_iv = findViewById(R.id.test_top_iv);

		// relayout1.setOnFocusChangeListener(this);
		// relayout2.setOnFocusChangeListener(this);
		// relayout3.setOnFocusChangeListener(this);
		// relayout11122.setOnFocusChangeListener(this);
		// relayout1112233.setOnFocusChangeListener(this);
	}

	private void testtest(View v, boolean hasFocus) {
		if (hasFocus) {
			mainUpView1.setFocusView(v, 1.2f);
		} else {
			mainUpView1.setUnFocusView(v);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		testtest(v, hasFocus);
		// 测试.
		if (v.getId() == R.id.relayout11) {
			if (hasFocus) {
				test_top_iv.animate().scaleX(1.2f).scaleY(1.2f).setDuration(500).start();
			} else {
				test_top_iv.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
			}
		}
	}

}
