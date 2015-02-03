package com.androidtv.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtv.R;
import com.androidtv.view.FocusRelativeLayout;
import com.androidtv.view.FocusRelativeLayout.FocusRelativeLayoutCallBack;
import com.androidtv.view.ReflectionRelativeLayout;

/**
 * 首先需要将FocusRelativeLayout作为主布局. <br>
 * 内部将 ReflectionRelativeLayout 作为边框，倒影显示的布局。<br>
 * ReflectionRelativeLayout 属性：waterreflection（是否显示倒影，布尔值） <br>
 * 
 * @author Frozen Free Fall
 *
 */
public class MainActivity extends Activity {
	// FocusRelativeLayout focusView;
	ViewPager pager1;
	ArrayList<View> viewList = new ArrayList<View>();//
	
	LayoutInflater layoutInf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.page1);
		setContentView(R.layout.activity_main_test);
		layoutInf = getLayoutInflater().from(this);
		View view1 = layoutInf.inflate(R.layout.page1, null);
		View view2 = layoutInf.inflate(R.layout.page2, null);
		View view3 = layoutInf.inflate(R.layout.page3, null);
		View view5 = layoutInf.inflate(R.layout.page5, null);
		// 思维扩展.
//        try {
//        	AssetManager am = null;  
//        	     am = getAssets();  
//        	    InputStream is = am.open("page5.xml");  
//        	// 读取原始资源文件的XML布局.此宽展主要用于.
//    		XmlPullParserFactory pullParserFactory=XmlPullParserFactory.newInstance();
//            //获取XmlPullParser的实例
//            XmlPullParser xmlPullParser=pullParserFactory.newPullParser();
//            //设置输入流  xml文件
//			xmlPullParser.setInput(is, "UTF-8");
//			View view6 = layoutInf.inflate(xmlPullParser, null);
//			//
//			if (view6 != null) {
//				viewList.add(view6);
//			}
//		} catch (XmlPullParserException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view5);
		pager1 = (ViewPager) findViewById(R.id.pager1);
		pager1.setAdapter(new MyPagerView());
	}
	
	class MyPagerView extends PagerAdapter {

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position), 0); // 添加页卡

			FocusRelativeLayout focusView = (FocusRelativeLayout) findViewById(R.id.focus1);
			final ImageView imageView2 = (ImageView) findViewById(R.id.imageView2); // 测试.
			focusView.setBorderViewBg(R.drawable.focus_bound);
			focusView.setViewGroup(container); // 控制焦点顺序.
			// focusView.setBorderViewBg(R.drawable.ic_white_border_none);
			focusView.setBorderScale(1.2f, 1.2f); // 放大比例.
			focusView.setBorderViewSize(8, 8); // 如果移动边框带有阴影，将阴影的距离填写上去.
			focusView.setReflectPadding(5); // 设置倒影和子控件之间的距离.
			focusView.setBorderTV(false); // 设置 tv还有手机像素.
			focusView.setBorderShow(true); // 显示外边框.
			focusView
					.setOnFocusRelativeLayoutCallBack(new FocusRelativeLayoutCallBack() {
						@Override
						public void onFirstFocusInChild(
								ReflectionRelativeLayout reflectionRelativeLayout) {
							String value = reflectionRelativeLayout.getvalue();
							if (value.equals("movie_hide")) {
								View child = reflectionRelativeLayout
										.getChildAt(1);
								if (child instanceof TextView) {
									child.setVisibility(View.VISIBLE);
								}
								//
								if (imageView2 != null) {
									imageView2.bringToFront();
									android.view.ViewPropertyAnimator animator = imageView2
											.animate().scaleX(1.8f)
											.scaleY(1.8f);
									animator.start();
								}
							}
							super.onFirstFocusInChild(reflectionRelativeLayout);
						}

						@Override
						public void onFirstFocusOutChild(
								ReflectionRelativeLayout reflectionRelativeLayout) {
							String value = reflectionRelativeLayout.getvalue();
							if (value.equals("movie_hide")) {
								View child = reflectionRelativeLayout
										.getChildAt(1);
								if (child instanceof TextView) {
									child.setVisibility(View.GONE);
								}
								//
								if (imageView2 != null) {
									imageView2.bringToFront();
									android.view.ViewPropertyAnimator animator = imageView2
											.animate().scaleX(1.0f)
											.scaleY(1.0f);
									animator.start();
								}
							}

							super.onFirstFocusOutChild(reflectionRelativeLayout);
						}
					});
			return viewList.get(position);
		}
	}
}
