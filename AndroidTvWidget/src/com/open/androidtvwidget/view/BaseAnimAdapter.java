package com.open.androidtvwidget.view;

import android.graphics.Canvas;
import android.view.View;

public abstract class BaseAnimAdapter implements IAnimAdapter {

	@Override
	public void onDrawMainUpView(Canvas canvas) {
	}

	@Override
	public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
	}

	@Override
	public void onFocusView(View focusView, float scaleX, float scaleY) {
	}

	@Override
	public void setMainUpView(MainUpView view) {
	}

	@Override
	public MainUpView getMainUpView() {
		return null;
	}

}
