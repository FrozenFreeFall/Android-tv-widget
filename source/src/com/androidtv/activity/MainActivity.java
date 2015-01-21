package com.androidtv.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.androidtv.R;
import com.androidtv.view.FocusBorderView;
import com.androidtv.view.FocusRelativeLayout;
import com.androidtv.view.FocusRelativeLayout.FocusRelativeLayoutCallBack;
import com.androidtv.view.ReflectionRelativeLayout;

/**
 * 首先需要将FocusRelativeLayout作为主布局. <br>
 * 内部将 ReflectionRelativeLayout 作为边框，倒影显示的布局。<br>
 * ReflectionRelativeLayout 属性：waterreflection（是否显示倒影，布尔值） <br>
 * 
 * @author Frozen Free Fall
 *
 */
public class MainActivity extends Activity {
	private static final String TAG_base = "MainActivity";
	FocusBorderView mBorderView;
	Handler mHandler = new Handler();
	ImageView imageView1;
	FocusRelativeLayout focusView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_test);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		focusView = (FocusRelativeLayout) findViewById(R.id.focus1);
		focusView.setBorderViewBg(R.drawable.focus_bound);
		// focusView.setBorderViewBg(R.drawable.ic_white_border_none);
		focusView.setBorderScale(1.5f, 1.5f); // 放大比例.
		focusView.setBorderViewSize(1, 1); // 如果移动边框带有阴影，将阴影的距离填写上去.
		focusView.setReflectPadding(5); // 设置倒影和子控件之间的距离.
		// focusView.setBorderShow(false);
		focusView.setBorderTV(false); // 设置 tv还有手机像素.
		focusView.setBorderShow(true); // 显示外边框.
		focusView
				.setOnFocusRelativeLayoutCallBack(new FocusRelativeLayoutCallBack() {
					@SuppressLint("NewApi")
					@Override
					public void onLastFocusOutChild(
							ReflectionRelativeLayout reflectionRelativeLayout) {
						// 可以在子控件失去焦点的时候设置一些子控件内部子控件的动画或者改变属性等等.
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
						if (reflectionRelativeLayout.getvalue().equals("100")) {
							android.view.ViewPropertyAnimator vpa = imageView1
									.animate();
							vpa.scaleX(1.0f).scaleY(1.0f).start();
							imageView1.bringToFront();
						}
					}

					@SuppressLint("NewApi")
					@Override
					public void onFirstFocusInChild(
							ReflectionRelativeLayout reflectionRelativeLayout) {
						//
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
						//
						if (reflectionRelativeLayout.getvalue().equals("100")) {
							android.view.ViewPropertyAnimator vpa1 = imageView1
									.animate();
							vpa1.scaleX(1.2f).scaleY(1.2f).start();
							imageView1.bringToFront();
						}
					}
				});
	}

}
