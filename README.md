此项目已经不维护，最新的更新地址在这里：https://git.oschina.net/hailongqiu/AndroidTVWidget (请关注这个项目地址，会继续更新此项目)

【前言】 最新的一个想法的延伸，基于适配器的可扩展思路，所以编写另一个android tv控件，更灵活，更易于使用.
https://gitcafe.com/hailongqiu/android-tv-widget
---------------------------

如果你的横向滚动或者纵向滚动出现了问题（到达最后一个的时候，边框有问题），请看这篇文章:http://my.oschina.net/hailongqiu/blog/471525

因为要加强 Android 投影仪的 luncher 倒影国际化的功能，所以开始的时候在BroderView的基础改了些东西.

后来又一些BUG，修复了，感觉毕竟是用的别人的开源代码，如果不开源又不好意思，再说了，这个代码也不属于

在职期间开发，是业余时间编写，所以没有什么问题.欢迎大家多多指教.

【Android-tv-widget】
---------------------------

Android 组件，主要用于 投影仪，TV电视，手机等等Android设备.

需要定制luncher桌面的请不要联系我.

移动边框，焦点控件动画.

高斯模糊(毛玻璃效果)

图片主色

主题  http://www.oschina.net/p/colortheme

目录结构：
---------------------------
> --com/androidtv/activity
>
>> ----activity.java : 测试demo
>
>>--com/androidtv/color (获取图片颜色主色和其它颜色)
>
>>----ColorArt.java ：
>
>>----HashBag.java
>
>--com/androidtv/utils (一些常用函数)
>
>>----AnimUtils.java
>
>>----DensityUtil.java
>
>>----FastBlur.java  ：高斯模糊
>
>>----ImageUtils.java
>
>>--com/androidtv/view
>
>>----BorderView.java ：移动边框
>
>>----CopyOfCopyOfFocusBorderView.java
>
>>----FocusBorderView.java
>
>>----FocusRelativeLayout.java
>
>>----ReflectionRelativeLayout.java
>
>>----VerticalSmoothGridView.java
>
>--net.qiujuer.imageblurring.jni (高斯模糊JNI)

例子图片查看
----------------------------------

![github](https://github.com/FrozenFreeFall/Android-tv-widget/blob/master/demo/QQ%E5%9B%BE%E7%89%8720150123025437.png)

![github](https://github.com/FrozenFreeFall/Android-tv-widget/blob/master/demo/QQ%E5%9B%BE%E7%89%8720150123025444.jpg)

注意
----------------------------------

如果想在GridView上加上BorderView，只需要调用 runTranslateAnimation



#### 最近找了一种新的方法，可以在最上面放大... ....布局只需要将需要放大的房在最上层 #####
package com.xgimi.doubanfm.utils;

import com.xgimi.doubanfm.widgets.ZoomFrameWidget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class ZoomHelper {
	private static final float DEFULAT_SCALE_VALUE = 1.2f;
	private static final float DEFULAT_SCALE_VALUE2 = 1.4f;

	public void zoomImageFromThumb(final View container, final View thumbView,
			final ZoomFrameWidget zoomView) {
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		thumbView.getGlobalVisibleRect(startBounds);
		container.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);

		zoomView.setVisibility(View.VISIBLE);
		thumbView.setDrawingCacheEnabled(true);
		Bitmap b = thumbView.getDrawingCache();
		zoomView.setImageBitmap(b);
		zoomView.setPivotX(0f);
		zoomView.setPivotY(0f);

		AnimatorSet set = new AnimatorSet();
		float deltaWidth = (startBounds.width() * DEFULAT_SCALE_VALUE - startBounds
				.width()) / 2;
		float deltaHeight = (startBounds.height() * DEFULAT_SCALE_VALUE - startBounds
				.height()) / 2;
		set.play(
				ObjectAnimator.ofFloat(zoomView, View.X, startBounds.left,
						startBounds.left - deltaWidth))
				.with(ObjectAnimator.ofFloat(zoomView, View.Y, startBounds.top,
						startBounds.top - deltaWidth))
				.with(ObjectAnimator.ofFloat(zoomView, View.SCALE_X, 1.0f,
						DEFULAT_SCALE_VALUE))
				.with(ObjectAnimator.ofFloat(zoomView, View.SCALE_Y, 1.0f,
						DEFULAT_SCALE_VALUE));
		set.setDuration(200);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		set.start();
	}

	/**
	 * 设置移动边框.
	 */
	public void zoomImageFromThumb(final View container, final View thumbView,
			final ImageView zoomView) {
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		thumbView.getGlobalVisibleRect(startBounds);
		container.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);

		zoomView.setVisibility(View.VISIBLE);
		thumbView.setDrawingCacheEnabled(true);
		Bitmap thumbViewBitmap = thumbView.getDrawingCache();
		zoomView.setDrawingCacheEnabled(true);
		Bitmap b = zoomView.getDrawingCache();
		Bitmap mBitmap = Bitmap.createScaledBitmap(b,
				thumbViewBitmap.getWidth(), thumbViewBitmap.getHeight(), true);
		zoomView.setImageBitmap(mBitmap);
		zoomView.setPivotX(0f);
		zoomView.setPivotY(0f);

		AnimatorSet set = new AnimatorSet();
		float deltaWidth = (startBounds.width() * DEFULAT_SCALE_VALUE2 - startBounds
				.width()) / 2;
		float deltaHeight = (startBounds.height() * DEFULAT_SCALE_VALUE2 - startBounds
				.height()) / 2;
		set.play(
				ObjectAnimator.ofFloat(zoomView, View.X, startBounds.left,
						startBounds.left - deltaWidth))
				.with(ObjectAnimator.ofFloat(zoomView, View.Y, startBounds.top,
						startBounds.top - deltaWidth))
				.with(ObjectAnimator.ofFloat(zoomView, View.SCALE_X, 1.0f,
						DEFULAT_SCALE_VALUE2))
				.with(ObjectAnimator.ofFloat(zoomView, View.SCALE_Y, 1.0f,
						DEFULAT_SCALE_VALUE2));
		set.setDuration(200);
		set.setInterpolator(new DecelerateInterpolator());
		set.start();
	}
}
