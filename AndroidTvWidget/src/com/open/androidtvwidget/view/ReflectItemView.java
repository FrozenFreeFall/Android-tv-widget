package com.open.androidtvwidget.view;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.utils.DrawUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 倒影，圆角控件.
 * 
 * @author hailongqiu 356752238@qq.com
 *
 */
public class ReflectItemView extends FrameLayout {

	private static final String TAG = "ReflectItemView";

	private static final int DEFUALT_REFHEIGHT = 80;
	private static final int DEFUALT_RADIUS = 12;

	private Paint mClearPaint = null;
	private Paint mShapePaint = null;
	private Paint mRefPaint = null;
	private int mRefHeight = DEFUALT_REFHEIGHT;

	private boolean mIsDrawShape = false;
	private boolean mIsReflection = false;

	private float mRadius = DEFUALT_RADIUS;
	private RadiusRect mRadiusRect = new RadiusRect(mRadius, mRadius, mRadius, mRadius);

	public ReflectItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public ReflectItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public ReflectItemView(Context context) {
		super(context);
		init(context, null);
	}

	private void init(Context context, AttributeSet attrs) {
		setClipChildren(false);
		setClipToPadding(false);
		setWillNotDraw(false);
		// 初始化属性.
		if (attrs != null) {
			TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.reflectItemView);// 获取配置属性
			mIsReflection = tArray.getBoolean(R.styleable.reflectItemView_isReflect, false);
			mRefHeight = (int) tArray.getDimension(R.styleable.reflectItemView_reflect_height, DEFUALT_REFHEIGHT);
			mIsDrawShape = tArray.getBoolean(R.styleable.reflectItemView_isShape, false);
			mRadius = tArray.getDimension(R.styleable.reflectItemView_radius, DEFUALT_RADIUS);
			setRadius(mRadius);
		}
		// 初始化圆角矩形.
		initShapePaint();
		// 初始化倒影参数.
		initRefPaint();
	}

	private void initShapePaint() {
		mShapePaint = new Paint();
		mShapePaint.setAntiAlias(true);
		// 取两层绘制交集。显示下层。
		mShapePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
	}

	private void initRefPaint() {
		if (mRefPaint == null) {
			mRefPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			// 倒影渐变.
			mRefPaint.setShader(
					new LinearGradient(0, 0, 0, mRefHeight, new int[] { 0x77000000, 0x66AAAAAA, 0x0500000, 0x00000000 },
							new float[] { 0.0f, 0.1f, 0.9f, 1.0f }, Shader.TileMode.CLAMP));
			mRefPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
		}
		if (mClearPaint == null) {
			mClearPaint = new Paint();
			mClearPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		}
	}

	/**
	 * 设置是否倒影.
	 */
	public void setReflection(boolean ref) {
		mIsReflection = ref;
		invalidate();
	}

	public boolean isReflection() {
		return this.mIsReflection;
	}

	/**
	 * 设置绘制奇形怪状的形状.
	 */
	public void setDrawShape(boolean isDrawShape) {
		mIsDrawShape = isDrawShape;
		invalidate();
	}

	public boolean isDrawShape() {
		return this.mIsDrawShape;
	}

	/**
	 * 倒影高度.
	 */
	public void setRefHeight(int height) {
		this.mRefHeight = height;
	}

	public int getRefHeight() {
		return this.mRefHeight;
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	private boolean isDrawShapeRadiusRect(RadiusRect radiusRect) {
		if (radiusRect.bottomLeftRadius <= 0 && radiusRect.bottomRightRadius <= 0 && radiusRect.topLeftRadius <= 0
				&& radiusRect.topRightRadius <= 0)
			return false;
		return true;
	}

	@Override
	public void draw(Canvas canvas) {
		if (mIsDrawShape && isDrawShapeRadiusRect(mRadiusRect)) {
			drawShapePathCanvas(canvas);
		} else {
			super.draw(canvas);
		}
		// 绘制倒影.
		drawRefleCanvas(canvas);
	}
	
	/**
	 * 绘制圆角控件.
	 * 修复使用clipPath有锯齿问题.
	 */
	private void drawShapePathCanvas(Canvas shapeCanvas) {
		int width = getWidth();
		int height = getHeight();
		int count = shapeCanvas.save();
		int count2 = shapeCanvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
		//
		Path path = DrawUtils.addRoundPath3(width, height, mRadius);
		super.draw(shapeCanvas);
		shapeCanvas.drawPath(path, mShapePaint);
		//
		shapeCanvas.restoreToCount(count2);
		shapeCanvas.restoreToCount(count);
	}
	
	/**
	 * 绘制倒影.
	 * 修复原先使用bitmap卡顿的问题.
	 */
	private void drawRefleCanvas(Canvas refleCanvas) {
		if (mIsReflection) {
			refleCanvas.save();
			int dy = getHeight();
			int dx = 0;
			refleCanvas.translate(dx, dy);
			drawReflection(refleCanvas);
			refleCanvas.restore();
		}
	}
	
	public Path getShapePath() {
		return DrawUtils.addRoundPath(getWidth(), getHeight(), mRadiusRect);
	}
	
	/**
	 * 绘制倒影.
	 */
	public void drawReflection(Canvas reflectionCanvas) {
		int width = getWidth();
		int height = getHeight();
		int count = reflectionCanvas.save();
		int count2 = reflectionCanvas.saveLayer(0, 0, width, mRefHeight, null, Canvas.ALL_SAVE_FLAG);
		//
		reflectionCanvas.save();
		reflectionCanvas.clipRect(0, 0, getWidth(), mRefHeight);
		reflectionCanvas.save();
		reflectionCanvas.scale(1, -1);
		reflectionCanvas.translate(0, -getHeight());
		super.draw(reflectionCanvas);
		if (mIsDrawShape) {
			Path path = DrawUtils.addRoundPath3(width, height, mRadius);
			reflectionCanvas.drawPath(path, mShapePaint);
		}
		reflectionCanvas.restore();
		reflectionCanvas.drawRect(0, 0, getWidth(), mRefHeight, mRefPaint);
		reflectionCanvas.restore();
		//
		reflectionCanvas.restoreToCount(count2);
		reflectionCanvas.restoreToCount(count);
	}

	/*
	 * 设置/获取-圆角的角度.
	 */

	public void setRadius(float radius) {
		setRadius(radius, radius, radius, radius);
	}

	public void setRadius(float tlRadius, float trRaius, float blRadius, float brRaius) {
		setRadiusRect(new RadiusRect(tlRadius, trRaius, blRadius, brRaius));
	}

	public void setRadiusRect(RadiusRect rect) {
		mRadiusRect = rect;
		invalidate();
	}

	public RadiusRect getRadiusRect() {
		return this.mRadiusRect;
	}

	public class RadiusRect {
		public float topLeftRadius;
		public float topRightRadius;
		public float bottomLeftRadius;
		public float bottomRightRadius;

		public RadiusRect() {
		}

		public RadiusRect(float tlRadius, float trRaius, float blRadius, float brRaius) {
			topLeftRadius = tlRadius;
			topRightRadius = trRaius;
			bottomLeftRadius = blRadius;
			bottomRightRadius = brRaius;
		}

		public void set(float tlRadius, float trRaius, float blRadius, float brRaius) {
			topLeftRadius = tlRadius;
			topRightRadius = trRaius;
			bottomLeftRadius = blRadius;
			bottomRightRadius = brRaius;
		}

		public RadiusRect get() {
			return new RadiusRect(topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
		}
	}

}
