package net.qiujuer.imageblurring.jni;

import android.graphics.Bitmap;

/**
 * 
 * @author Frozen Free Fall
 *
 */
public class ImageBlur {
    public static native void blurIntArray(int[] pImg, int w, int h, int r);

    public static native void blurBitMap(Bitmap bitmap, int r);

    static {
        System.loadLibrary("ImageBlur");
    }
}
