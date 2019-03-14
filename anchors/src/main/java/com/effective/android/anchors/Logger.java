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
        if (AnchorsRuntime.debuggable()) {
            Log.w(Constants.TAG, obj.toString());
        }
    }

    public static void d(String tag, Object obj) {
        if (AnchorsRuntime.debuggable()) {
            Log.d(tag, obj.toString());
        }
    }
}
