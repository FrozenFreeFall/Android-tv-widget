package com.open.androidtvwidget.utils;

import com.open.androidtvwidget.view.ReflectItemView.RadiusRect;

import android.graphics.Path;
import android.graphics.RectF;

public class DrawUtils {

	private static final int ROUND_90_ANGLE = 90;

	public static Path addRoundPath(int width, int height, RadiusRect radiusRect) {
		//
		Path path = new Path();
		path.moveTo(radiusRect.topLeftRadius, 0);
		// 左上
		RectF arcTopLeft = new RectF(0, 0, radiusRect.topLeftRadius * 2, radiusRect.topLeftRadius * 2);
		path.arcTo(arcTopLeft, -ROUND_90_ANGLE, -ROUND_90_ANGLE);
		path.lineTo(radiusRect.topLeftRadius, 0);
		// 右上.
		RectF arcTopRight = new RectF(width - radiusRect.topRightRadius * 2, 0, width, radiusRect.topRightRadius * 2);
		path.arcTo(arcTopRight, -ROUND_90_ANGLE, ROUND_90_ANGLE);
		// 右下.
		RectF arcBottomRight = new RectF(width - radiusRect.bottomRightRadius * 2,
				height - radiusRect.bottomRightRadius * 2, width, height);
		path.arcTo(arcBottomRight, 0, ROUND_90_ANGLE);
		// 左下.
		RectF arc = new RectF(0, height - radiusRect.bottomLeftRadius * 2, radiusRect.bottomLeftRadius * 2, height);
		path.arcTo(arc, ROUND_90_ANGLE, ROUND_90_ANGLE);
		path.lineTo(0, radiusRect.topLeftRadius);
		path.close();
		return path;
	}

	public static Path addRoundPath2(int width, int height, RadiusRect radiusRect) {
		Path mPath = new Path();
		if (radiusRect == null || width <= 0 || height <= 0)
			return mPath;

		// topleft path
		if (radiusRect.topLeftRadius > 0) {
			Path topLeftPath = new Path();
			topLeftPath.moveTo(0, radiusRect.topLeftRadius);
			topLeftPath.lineTo(0, 0);
			topLeftPath.lineTo(radiusRect.topLeftRadius, 0);
			RectF arc = new RectF(0, 0, radiusRect.topLeftRadius * 2, radiusRect.topLeftRadius * 2);
			topLeftPath.arcTo(arc, -ROUND_90_ANGLE, -ROUND_90_ANGLE);
			topLeftPath.close();
			mPath.addPath(topLeftPath);
		}

		// topRight path
		if (radiusRect.topRightRadius > 0) {
			Path topRightPath = new Path();
			topRightPath.moveTo(width, radiusRect.topRightRadius);
			topRightPath.lineTo(width, 0);
			topRightPath.lineTo(width - radiusRect.topRightRadius, 0);
			RectF arc = new RectF(width - radiusRect.topRightRadius * 2, 0, width, radiusRect.topRightRadius * 2);
			topRightPath.arcTo(arc, -ROUND_90_ANGLE, ROUND_90_ANGLE);
			topRightPath.close();
			mPath.addPath(topRightPath);
		}

		// bottomLeft path
		if (radiusRect.bottomLeftRadius > 0) {
			Path bottomLeftPath = new Path();
			bottomLeftPath.moveTo(0, height - radiusRect.bottomLeftRadius);
			bottomLeftPath.lineTo(0, height);
			bottomLeftPath.lineTo(radiusRect.bottomLeftRadius, height);
			RectF arc = new RectF(0, height - radiusRect.bottomLeftRadius * 2, radiusRect.bottomLeftRadius * 2, height);

			bottomLeftPath.arcTo(arc, ROUND_90_ANGLE, ROUND_90_ANGLE);
			bottomLeftPath.close();
			mPath.addPath(bottomLeftPath);
		}

		// bottomRight path
		if (radiusRect.bottomRightRadius > 0) {
			Path bottomRightPath = new Path();
			bottomRightPath.moveTo(width - radiusRect.bottomRightRadius, height);
			bottomRightPath.lineTo(width, height);
			bottomRightPath.lineTo(width, height - radiusRect.bottomRightRadius);
			RectF arc = new RectF(width - radiusRect.bottomRightRadius * 2, height - radiusRect.bottomRightRadius * 2,
					width, height);
			bottomRightPath.arcTo(arc, 0, ROUND_90_ANGLE);
			bottomRightPath.close();
			mPath.addPath(bottomRightPath);
		}

		return mPath;

	}
	
	public static Path addRoundPath3(int width, int height, float radius) {
		Path path = new Path();
		path.addRoundRect(new RectF(0, 0, width, height), radius, radius, Path.Direction.CW);
		return path;
	}
	
}
