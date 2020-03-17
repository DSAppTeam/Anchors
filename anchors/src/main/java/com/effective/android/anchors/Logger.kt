package com.effective.android.anchors;

import android.util.Log;

/**
 * 调试 log 管理
 * created by yummylau on 2019/03/11
 */
public class Logger {


    static void d(Object obj) {
        d(Constants.TAG, obj);
    }

    static void w(Object obj) {
        w(Constants.TAG, obj);
    }

    static void e(Object obj) {
        e(Constants.TAG, obj);
    }

    static void e(String tag, Object obj) {
        if (AnchorsRuntime.debuggable()) {
            Log.e(tag, obj.toString());
        }
    }

    static void w(String tag, Object obj) {
        if (AnchorsRuntime.debuggable()) {
            Log.w(tag, obj.toString());
        }
    }

    static void d(String tag, Object obj) {
        if (AnchorsRuntime.debuggable()) {
            Log.d(tag, obj.toString());
        }
    }

}
