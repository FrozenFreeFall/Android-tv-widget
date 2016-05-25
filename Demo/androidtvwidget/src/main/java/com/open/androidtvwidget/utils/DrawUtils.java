package com.open.androidtvwidget.utils;

import android.graphics.Path;
import android.graphics.RectF;

public class DrawUtils {

	public static Path addRoundPath3(int width, int height, float radius) {
		Path path = new Path();
		path.addRoundRect(new RectF(0, 0, width, height), radius, radius, Path.Direction.CW);
		return path;
	}
	
}
