package com.open.demo;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by hailongqiu on 2016/6/3.
 */
public class MenuAnimationUtils {

    /**
     * 从左到右显示菜单.
     */
    public static Animation showAnimation() {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(1000);
        return animation;
    }

    /**
     * 从右到左隐藏菜单.
     */
    public static Animation hideAnimation() {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        return animation;
    }

    /**
     * 加载动画.
     */
    @SuppressWarnings("ResourceType")
    public static LayoutAnimationController loadAnimation(Context context) {
		/*
		 * 创建动画的集合
		 */
        AnimationSet set = new AnimationSet(false);
        Animation animation;
		/*
		 * 创建旋转动画
		 */
        animation = new RotateAnimation(180, 10);
        animation.setDuration(1000);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 1);
        controller.setInterpolator(context, android.R.anim.accelerate_interpolator);
        controller.setAnimation(set);
        return controller;
    }

    /**
     * 加载动画2.
     */
    public static LayoutAnimationController loadAnimation2() {
        int duration = 300;
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }

}
