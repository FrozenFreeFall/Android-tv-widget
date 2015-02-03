package com.androidtv.view;


import com.androidtv.R;
import com.androidtv.utils.AnimUtils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * 
 * @author Frozen Free Fall
 *
 */
public class BorderView extends ImageView implements AnimationListener {

    private static final String TAG = "BorderView";
    private static int BORDER_SIZE = 30;
    private static int TRAN_DUR_ANIM = 250;

    private SoundPool sp;
    private Context mContext;

    private AnimationDrawable mBoxBgAnim;
    private int mLeft, mTop, mRight, mBottom;

    public BorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
      
        mContext = context;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mLeft != left || mTop != top || mRight != right
                || mBottom != bottom) {
            this.layout(this.mLeft, this.mTop, this.mRight, this.mBottom);
        }
    }

    /**
     * 璁剧疆杈圭晫妗嗙殑澶栨澶у皬
     * 
     * @param size
     */
    public void setBorderSize(int size) {
        BORDER_SIZE = size;
    }

    /**
     * 璁剧疆浣嶇Щ鍔ㄧ敾鏃堕棿
     * 
     * @param dur
     */
    public void setTranslateAnimtionDuration(int dur) {
        TRAN_DUR_ANIM = dur;
    }
    
    private class ViewLocation {
        private int x;
        private int y;
        public ViewLocation(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public void setLocation(View view) {
        ViewLocation location = findLocationWithView(view);
        // Log.v(TAG, "setLocation X:"+location.x+" Y:"+location.y);
        mLeft = location.x - (int) BORDER_SIZE;
        mTop = location.y - (int) BORDER_SIZE;
        mRight = location.x + (int) BORDER_SIZE + view.getWidth();
        mBottom = location.y + (int) BORDER_SIZE + view.getHeight();
        this.layout(mLeft, mTop, mRight, mBottom);
        this.clearAnimation();
        BorderView.this.setVisibility(View.VISIBLE);
    }

    /**
     * 鍒濆鍖栫劍鐐规鍔ㄧ敾
     */
    public void runBorderAnimation() {
        this.setBackgroundResource(R.anim.box_normal);
        restartBoxAnim();
    }

    /**
     * 鑾峰彇View鐨勪綅缃?
     * 
     * @param view
     *            鑾峰彇鐨勬帶浠?
     * @return 浣嶇疆
     */
    public ViewLocation findLocationWithView(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new ViewLocation(location[0], location[1]);
    }

    /**
     * 閲嶅惎闂儊鍔ㄧ敾
     * 
     * @param context
     */
    public void restartBoxAnim() {
        BorderView.this.setVisibility(View.VISIBLE);
        this.clearAnimation();
        if (mBoxBgAnim == null) {
            mBoxBgAnim = (AnimationDrawable) this.getBackground();
        }
        if (mBoxBgAnim.isRunning()) {
            mBoxBgAnim.stop();
        }
        mBoxBgAnim.start();
        this.startAnimation(AnimUtils.buildAnimBoxNormal(mContext));
    }

    @Override
    public void onAnimationEnd(Animation arg0) {
        notifyRestartBoxAnim(0);
    }

    @Override
    public void onAnimationRepeat(Animation arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * 璁板綍涓婁竴娆＄殑鐒︾偣缁勪欢锛岀敤浜庡垽鏂槸鍚︽湭绉诲姩鎺т欢鐨勭劍鐐癸紝鐩稿悓鍒欎笉閲嶆柊鍔犺浇鍔ㄧ敾
     */
    private View mLastFocusView;

    /**
     * 鍚姩鐒︾偣妗嗕綅绉诲姩鐢?
     */
    public void runTranslateAnimation(View toView) {
        runBorderAnimation();
        if (toView == null || mLastFocusView == toView) {
            return;
        }
        // 缂╂斁姣斾緥
        float scaleWValue = (float) this.getWidth()
                / ((float) toView.getWidth() + 2 * BORDER_SIZE);
        float scaleHValue = (float) this.getHeight()
                / ((float) toView.getHeight() + 2 * BORDER_SIZE);
        ScaleAnimation scale = new ScaleAnimation(scaleWValue, 1.0f,
                scaleHValue, 1.0f);
        // 璁板綍浣嶇疆淇℃伅锛屼互涓哄惎鍔ㄥ姩鐢诲墠box宸茬粡璁剧疆鍒扮洰鏍囦綅缃簡銆?
        ViewLocation fromLocation = findLocationWithView(this);
        ViewLocation toLocation = findLocationWithView(toView);
        TranslateAnimation tran = new TranslateAnimation(0,
                100,
                0,
                0);
        /*TranslateAnimation tran = new TranslateAnimation(-toLocation.x
                + (float) BORDER_SIZE + fromLocation.x, 0, -toLocation.y
                + (float) BORDER_SIZE + fromLocation.y, 0);*/
        // Log.v("TAG","fromX:"+(-toLocation.x+(float)BORDER_SIZE+fromLocation.x)+" fromY:"+(-toLocation.y+(float)BORDER_SIZE+fromLocation.y));
        // Log.v("TAG","fromX:"+fromLocation.x+ " toX:"
        // +toLocation.x+" fromY:"+fromLocation.y+" toY:"+toLocation.x);
        // TranslateAnimation tran = new TranslateAnimation(0,
        // toLocation.x-(float)BORDER_SIZE-fromLocation.x,
        // 0, toLocation.y-(float)BORDER_SIZE-fromLocation.y);
        AnimationSet boxAnimaSet = new AnimationSet(true);
        boxAnimaSet.setAnimationListener(this);
        boxAnimaSet.addAnimation(scale);
        boxAnimaSet.addAnimation(tran);
        boxAnimaSet.setDuration(TRAN_DUR_ANIM);
        BorderView.this.setVisibility(View.INVISIBLE);
//        setLocation(toView);// 鍏堜綅绉诲埌鐩爣浣嶇疆鍐嶅惎鍔ㄥ姩鐢?
        Log.v(TAG, "setLocation runTranslateAnimation");
        BorderView.this.startAnimation(boxAnimaSet);
        mLastFocusView = toView;
    }

    public void playClickOgg() {
        if (sp == null) {
            sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            sp.load(mContext, R.raw.himi_ogg, 0);
        }
        sp.play(1, 1, 1, 0, 0, 1);
    }

    private static AnimationSet mBoxAnimClick;

    private void runClickAnimtion() {
        playClickOgg();
        if (mBoxAnimClick == null) {
            mBoxAnimClick = AnimUtils.buildAnimBoxClick(mContext);
        }
        BorderView.this.startAnimation(mBoxAnimClick);
        notifyRestartBoxAnim(500);
    }

    public static final int MSG_BOX_BG_ANIM = 10;
    public static final int MSG_BOX_CLICK_ANIM = 11;

    /**
     * 閲嶅惎鑳屾櫙鍔ㄧ敾
     * 
     * @param delay
     *            寤惰繜鏃堕棿姣
     */
    void notifyRestartBoxAnim(int delay) {
        mBoxHandler.sendEmptyMessageDelayed(MSG_BOX_BG_ANIM, delay);
    }

    /**
     * 鐐瑰嚮鍔ㄧ敾
     */
    public void notifyClickBoxAnim() {
        mBoxHandler.sendEmptyMessageDelayed(MSG_BOX_CLICK_ANIM, 10);
    }

    Handler mBoxHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case MSG_BOX_BG_ANIM:
                restartBoxAnim();
                break;
            case MSG_BOX_CLICK_ANIM:
                runClickAnimtion();
                break;
            }
        };
    };

}
