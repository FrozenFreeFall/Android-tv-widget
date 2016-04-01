package com.open.androidtvwidget.adapter;

import com.open.androidtvwidget.view.MainUpView;

import android.graphics.Canvas;
import android.view.View;

public interface IAnimBridge {
	
	public void onInitBridge(MainUpView view);
	public boolean onDrawMainUpView(Canvas canvas);
	public void onOldFocusView(View oldFocusView, float scaleX, float scaleY);
	public void onFocusView(View focusView, float scaleX, float scaleY);
	
	void setMainUpView(MainUpView view);
	MainUpView getMainUpView();
}
