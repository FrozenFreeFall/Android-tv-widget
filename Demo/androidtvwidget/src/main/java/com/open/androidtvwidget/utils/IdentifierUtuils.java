package com.open.androidtvwidget.utils;

import android.content.Context;

/**
 * 根据资源名字获取ID.
 * Created by hailongqiu on 2016/9/4.
 */
public class IdentifierUtuils {

    public static float getIdentifierWidthDimen(Context context, String name) {
        try {
            if (!name.startsWith("w_"))
                name = "w_" + name;
            int id = getIdentifierDimen(context, name);
            return context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static float getIdentifierHeightDimen(Context context, String name) {
        try {
            if (!name.startsWith("h_"))
                name = "h_" + name;
            int id = getIdentifierDimen(context, name);
            return context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getIdentifierDimen(Context context, String name) {
        int resid = context.getResources().getIdentifier(name, "dimen", context.getPackageName());
        return resid;
    }

    public static int getIdentifierDrawable(Context context, String name) {
        int resid = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return resid;
    }

    public static int getIdentifierString(Context context, String name) {
        int resid = context.getResources().getIdentifier(name, "string", context.getPackageName());
        return resid;
    }

}
