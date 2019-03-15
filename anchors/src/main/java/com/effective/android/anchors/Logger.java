package com.effective.android.anchors;

import android.util.Log;

/**
 * 调试 log 管理
 * created by yummylau on 2019/03/11
 */
public class Logger {


    public static void d(Object obj) {
        d(Constants.TAG, obj);
    }

    public static void w(Object obj) {
        w(Constants.TAG, obj);
    }

    public static void e(Object obj) {
        e(Constants.TAG, obj);
    }

    public static void e(String tag, Object obj) {
        if (AnchorsRuntime.debuggable()) {
            Log.e(tag, obj.toString());
        }
    }

    public static void w(String tag, Object obj) {
        if (AnchorsRuntime.debuggable()) {
            Log.w(tag, obj.toString());
        }
    }

    public static void d(String tag, Object obj) {
        if (AnchorsRuntime.debuggable()) {
            Log.d(tag, obj.toString());
        }
    }

}
