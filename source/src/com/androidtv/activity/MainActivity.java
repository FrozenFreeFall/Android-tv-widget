package com.androidtv.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.androidtv.R;
import com.androidtv.view.FocusBorderView;
import com.androidtv.view.FocusRelativeLayout;
import com.androidtv.view.FocusRelativeLayout.FocusRelativeLayoutCallBack;
import com.androidtv.view.ReflectionRelativeLayout;

/**
 * 
 * @author Frozen Free Fall
 *
 */
public class MainActivity extends Activity {
	private static final String TAG_base = "MainActivity";
	FocusBorderView mBorderView;
	Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_test);
		FocusRelativeLayout focusView = (FocusRelativeLayout) findViewById(R.id.focus1);
		focusView.setBorderViewBg(R.drawable.focus_bound);
		// focusView.setBorderViewBg(R.drawable.ic_white_border_none);
		focusView.setBorderScale(1.2f, 1.2f);
		focusView.setBorderViewSize(5, 5);
		focusView.setReflectPadding(5);
		// focusView.setBorderShow(false);
		focusView.setBorderTV(false); // 设置 tv还有手机像素.
		focusView.setBorderShow(true); // 显示外边框.
		focusView
				.setOnFocusRelativeLayoutCallBack(new FocusRelativeLayoutCallBack() {
					@SuppressLint("NewApi")
					@Override
					public void onFocusOutChild(
							ReflectionRelativeLayout reflectionRelativeLayout) {
						for (int i = 0; i < reflectionRelativeLayout
								.getChildCount(); i++) {
							View v = reflectionRelativeLayout.getChildAt(i);
							if (v instanceof RadioButton) {
								android.view.ViewPropertyAnimator vpa = v
										.animate();
								vpa.scaleX(1.0f).scaleY(1.0f).start();
							} else if (v instanceof Button) {
								Button b = (Button) v;
								b.setText("关闭效果");
							}
						}
					}

					@SuppressLint("NewApi")
					@Override
					public void onFocusInChild(
							ReflectionRelativeLayout reflectionRelativeLayout) {
						for (int i = 0; i < reflectionRelativeLayout
								.getChildCount(); i++) {
							View v = reflectionRelativeLayout.getChildAt(i);
							if (v instanceof RadioButton) {
								android.view.ViewPropertyAnimator vpa = v
										.animate();
								vpa.scaleX(1.5f).scaleY(1.5f).start();
							} else if (v instanceof Button) {
								Button b = (Button) v;
								b.setText("开启动画效果");
							}
						}
					}
				});
	}

}
