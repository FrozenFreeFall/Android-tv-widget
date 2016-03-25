package com.open.androidtvwidget.utils;

import android.util.Log;

public class OPENLOG {
	private static final boolean IS_DEBUG = true;
	private static final String SEARCH_KEYWORK = " hailongqiu ";

	public static void D(String tag, String msg) {
		if (IS_DEBUG)
			Log.d(tag, SEARCH_KEYWORK + msg);
	}
	
	public static void E(String tag, String msg) {
		if (IS_DEBUG)
			Log.e(tag, SEARCH_KEYWORK + msg);
	}
	
}
