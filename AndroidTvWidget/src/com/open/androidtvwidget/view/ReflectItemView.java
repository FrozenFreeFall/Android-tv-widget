package com.open.androidtvwidget.view;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.cache.BitmapMemoryCache;
import com.open.androidtvwidget.utils.OPENLOG;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
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
	
	private BitmapMemoryCache mBitmapMemoryCache =BitmapMemoryCache.getInstance();
	
	private Paint mClearPaint = null;
	private Paint mRefPaint = null;
	private int mRefHeight = DEFUALT_REFHEIGHT;

	private boolean mIsDrawShape = false;
	private boolean mIsReflection = false;

	private float mRadius = DEFUALT_RADIUS;
	private RadiusRect mRadiusRect = new RadiusRect(mRadius, mRadius, mRadius, mRadius);
	private static final int ROUND_90_ANGLE = 90;

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
		// 初始化倒影参数.
		initRefPaint();
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

	public Bitmap getReflectBitmap() {
		return BitmapMemoryCache.getInstance().getBitmapFromMemCache(getId() + "");
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public void draw(Canvas canvas) {
		// 设置圆角.
		canvas.save();
		if (mIsDrawShape) {
			canvas.clipPath(getShapePath());
		}
		super.draw(canvas);
		canvas.restore();
		// 绘制倒影.
		drawRefleBitmap(canvas);
	}

	public Path getShapePath() {
		return getRoundPath();
	}

	private Path getRoundPath() {
		int width = getWidth();
		int height = getHeight();
		//
		Path path = new Path();
		path.moveTo(mRadiusRect.topLeftRadius, 0);
		// 左上
		RectF arcTopLeft = new RectF(0, 0, mRadiusRect.topLeftRadius * 2, mRadiusRect.topLeftRadius * 2);
		path.arcTo(arcTopLeft, -ROUND_90_ANGLE, -ROUND_90_ANGLE);
		path.lineTo(mRadiusRect.topLeftRadius, 0);
		// 右上.
		RectF arcTopRight = new RectF(width - mRadiusRect.topRightRadius * 2, 0, width, mRadiusRect.topRightRadius * 2);
		path.arcTo(arcTopRight, -ROUND_90_ANGLE, ROUND_90_ANGLE);
		// 右下.
		RectF arcBottomRight = new RectF(width - mRadiusRect.bottomRightRadius * 2,
				height - mRadiusRect.bottomRightRadius * 2, width, height);
		path.arcTo(arcBottomRight, 0, ROUND_90_ANGLE);
		// 左下.
		RectF arc = new RectF(0, height - mRadiusRect.bottomLeftRadius * 2, mRadiusRect.bottomLeftRadius * 2, height);
		path.arcTo(arc, ROUND_90_ANGLE, ROUND_90_ANGLE);
		path.lineTo(0, mRadiusRect.topLeftRadius);
		path.close();
		return path;
	}
	
	/**
	 * 绘制倒影.
	 */
	private void drawRefleBitmap(Canvas canvas) {
		if (mIsReflection) {
			// 创建一个画布. 
			Bitmap reflectBitmap = mBitmapMemoryCache.getBitmapFromMemCache(getId() + "");
			if (reflectBitmap == null) {
				OPENLOG.D(TAG, "drawRefleBitmap cache create bitmap " + getId());
				reflectBitmap = Bitmap.createBitmap(getWidth(), mRefHeight, Bitmap.Config.ARGB_8888);
				mBitmapMemoryCache.addBitmapToMemoryCache(getId() + "", reflectBitmap);
			}
			Canvas reflectCanvas = new Canvas(reflectBitmap);
			reflectCanvas.drawPaint(mClearPaint); // 清空画布.
			/**
			 * 如果设置了圆角，倒影也需要圆角.
			 */
			if (mIsDrawShape) {
				reflectCanvas.clipPath(getShapePath());
			}
			// 绘制倒影.
			drawReflection(reflectCanvas);
			canvas.save();
			int dy = getHeight();
			int dx = 0;
			canvas.translate(dx, dy);
			canvas.drawBitmap(reflectBitmap, 0, 0, null);
			canvas.restore();
		}
	}

	/**
	 * 绘制倒影.
	 */
	public void drawReflection(Canvas canvas) {
		canvas.save();
		canvas.clipRect(0, 0, getWidth(), mRefHeight);
		canvas.save();
		canvas.scale(1, -1);
		canvas.translate(0, -getHeight());
		super.draw(canvas);
		canvas.restore();
		canvas.drawRect(0, 0, getWidth(), mRefHeight, mRefPaint);
		canvas.restore();
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
