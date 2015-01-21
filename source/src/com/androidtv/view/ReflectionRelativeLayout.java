package com.androidtv.view;

import com.androidtv.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 
 * @author Frozen Free Fall
 *
 */
public class ReflectionRelativeLayout extends RelativeLayout {

	private Context mContext = null;
	boolean isReflection = false;
	String value = "";
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
		if (attrs != null) {
			array = context.obtainStyledAttributes(attrs,
					R.styleable.ReflectionRelativeLayout);
			isReflection = array
					.getBoolean(
							R.styleable.ReflectionRelativeLayout_waterreflection,
							false);
			value = array.getString(R.styleable.ReflectionRelativeLayout_value);
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

}