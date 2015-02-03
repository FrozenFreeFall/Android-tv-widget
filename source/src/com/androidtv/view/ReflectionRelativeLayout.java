package com.androidtv.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.androidtv.R;

/**
 * 
 * @author Frozen Free Fall
 *
 */
public class ReflectionRelativeLayout extends RelativeLayout {

	private Context mContext = null;
	boolean isReflection = false;
	boolean isFirst = false;
	boolean isLast = false;
	String value = "";
	Integer pos = -1;
	TypedArray array;

	public ReflectionRelativeLayout(Context context) {
		super(context);
		init(context, null);
	}

	public ReflectionRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public ReflectionRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
//		setClipChildren(false);
//		setClipToPadding(false);
		if (attrs != null) {
			array = context.obtainStyledAttributes(attrs,
					R.styleable.ReflectionRelativeLayout);
			isReflection = array
					.getBoolean(
							R.styleable.ReflectionRelativeLayout_waterreflection,
							false);
			value = array.getString(R.styleable.ReflectionRelativeLayout_value);
			pos = array
					.getInteger(R.styleable.ReflectionRelativeLayout_pos, -1);
			isFirst = array.getBoolean(
					R.styleable.ReflectionRelativeLayout_isfirst, false);
			isLast = array.getBoolean(
					R.styleable.ReflectionRelativeLayout_islast, false);
			array.recycle();
		}
	}

	public boolean isReflection() {
		return isReflection;
	}

	public void setFlection(boolean isReflection) {
		this.isReflection = isReflection;
	}

	public String getvalue() {
		return value != null ? value : "";
	}

	public Integer getPos() {
		return pos;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public boolean isLast() {
		return isLast;
	}

}