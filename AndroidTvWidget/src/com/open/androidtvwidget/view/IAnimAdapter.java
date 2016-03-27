package com.open.androidtvwidget.view;

import android.graphics.Canvas;
import android.view.View;

public interface IAnimAdapter {
	
	public void onInitAdapter(MainUpView view);
	public void onDrawMainUpView(Canvas canvas);
	public void onOldFocusView(View oldFocusView, float scaleX, float scaleY);
	public void onFocusView(View focusView, float scaleX, float scaleY);
	
	void setMainUpView(MainUpView view);
	MainUpView getMainUpView();
}
