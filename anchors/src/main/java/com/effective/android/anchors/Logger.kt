package com.effective.android.anchors

import android.util.Log

/**
 * 调试 log 管理
 * created by yummylau on 2019/03/11
 */
object Logger {
    fun d(obj: Any) {
        d(Constants.TAG, obj)
    }

    fun w(obj: Any) {
        w(Constants.TAG, obj)
    }

    fun e(obj: Any) {
        e(Constants.TAG, obj)
    }

    fun e(tag: String, obj: Any) {
        if (AnchorsRuntime.debuggable()) {
            Log.e(tag, obj.toString())
        }
    }

    fun w(tag: String, obj: Any) {
        if (AnchorsRuntime.debuggable()) {
            Log.w(tag, obj.toString())
        }
    }

    fun d(tag: String, obj: Any) {
        if (AnchorsRuntime.debuggable()) {
            Log.d(tag, obj.toString())
        }
    }
}