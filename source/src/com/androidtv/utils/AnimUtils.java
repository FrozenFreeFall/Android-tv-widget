package com.androidtv.utils;



import com.androidtv.R;

import android.content.Context;
//import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

/**
 * 
 * @author Frozen Free Fall
 *
 */
public class AnimUtils {

    private static final String TAG = "AnimUtils";
    private static Animation mBoxAnimNormal;

    public static Animation buildAnimBoxNormal(Context context) {
        if (mBoxAnimNormal != null) {
            return mBoxAnimNormal;
        }
        mBoxAnimNormal = android.view.animation.AnimationUtils.loadAnimation(
                context, R.anim.box_alpha);
        return mBoxAnimNormal;
    }

    public static AnimationSet buildAnimBoxClick(Context context) {
        final ScaleAnimation scale = new ScaleAnimation(0.5f, 1.3f, 0.5f, 1.3f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        AlphaAnimation alpha = new AlphaAnimation(0.5f, 1.0f);
        AnimationSet mBoxAnimClick = new AnimationSet(true);
        mBoxAnimClick.addAnimation(scale);
        mBoxAnimClick.addAnimation(alpha);
        mBoxAnimClick.setDuration(100);
        return mBoxAnimClick;
    }
}