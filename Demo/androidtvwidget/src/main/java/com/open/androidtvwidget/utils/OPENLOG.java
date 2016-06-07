package com.open.androidtvwidget.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * 使用：
 * OPENLOG.initTag("hailongqiu", true); // 测试LOG输出.
 * 如果不进行初始化，那么将使用命令来开启LOG输出.
 * 开启LOG输出:
 * <p/>
 * setprop log.tag.$getTag() DEBUG
 * <p/>
 *
 * @author hailongqiu
 */
public class OPENLOG {

    private static String sTag = "";
    private static boolean sDebug = false;

    /**
     * 初始化tag信息.
     */
    public static void initTag(String tag) {
        initTag(tag, false);
    }

    /**
     * 初始化设置调试位.
     */
    public static void initTag(boolean debug) {
        initTag(sTag, debug);
    }

    public static void initTag(String tag, boolean debug) {
        sTag = tag;
        sDebug = debug;
    }

    public static void D(String str, Object... args) {
        if (isDebug()) {
            Log.d(getTag(), buildLogString(str, args));
        }
    }

    public static void V(String str, Object... args) {
        if (isDebug()) {
            Log.v(getTag(), buildLogString(str, args));
        }
    }

    public static void E(String str, Object... args) {
        if (isDebug()) {
            Log.d(getTag(), buildLogString(str, args));
        }
    }

    /**
     * 如果sTAG是空则自动从StackTrace中取TAG
     */
    private static String getTag() {
        if (!TextUtils.isEmpty(sTag)) {
            return sTag;
        }
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        return caller.getFileName();
    }

    private static String buildLogString(String str, Object... args) {
        if (args.length > 0) {
            str = String.format(str, args);
        }
        //
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        StringBuilder stringBuilder = new StringBuilder();
        //
//        if (TextUtils.isEmpty(sTag)) {
//            stringBuilder.append(caller.getMethodName())
//                    .append("():")
//                    .append(caller.getLineNumber())
//                    .append(":")
//                    .append(str);
//        } else {
        stringBuilder
                .append("(")
                .append(caller.getFileName())
                .append(":")
                .append(caller.getLineNumber())
                .append(").")
                .append(caller.getMethodName())
                .append("():")
                .append(str);
//        }
        return stringBuilder.toString();
    }

    private static boolean isDebug() {
        return sDebug || Log.isLoggable(getTag(), Log.DEBUG);
    }

}
