package com.open.androidtvwidget.view;


import java.util.HashMap;

import com.open.androidtvwidget.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 一个自定义的TextView控件.
 * 加载 /system/font/** 下面的字库.
 * @author hailongqiu
 *
 */
public class TextViewWithTTF extends TextView {

    private static final TypeFaceMgr SFontMgr = new TypeFaceMgr();
    
	public TextViewWithTTF(Context context) {
		super(context);
	}

    public TextViewWithTTF(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextViewWithTTF);
        setTypeface(SFontMgr.getTypeface(array.getString(R.styleable.TextViewWithTTF_ttf_name)));
        array.recycle();
    }

    

    public TextViewWithTTF(Context context, String aTTFName)
    {
        super(context);
        setTypeface(SFontMgr.getTypeface(aTTFName));
    }
    
    public void setFont(String aTTFName) {
    	setTypeface(SFontMgr.getTypeface(aTTFName));
    }
}


class TypeFaceMgr
{
    HashMap<String, Typeface> mTypefaces;
    public TypeFaceMgr()
    {
        mTypefaces = new HashMap<String, Typeface>();
    }
    public Typeface getTypeface(String aTTFName)
    {
        if(mTypefaces.containsKey(aTTFName))
        {
            return mTypefaces.get(aTTFName);
        }
        else
        {
            Typeface font;
            try
            {
                font = Typeface.createFromFile("/system/fonts/"+aTTFName);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
                return null;
            }
           
            mTypefaces.put(aTTFName, font);
            return font;
        }
    }
}