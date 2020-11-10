package com.effective.android.anchors.log

import android.util.Log
import com.effective.android.anchors.Constants

/**
 * 调试 log 管理
 * created by yummylau on 2019/03/11
 */
object Logger {

    @JvmStatic
    fun d(obj: Any) {
        d(Constants.TAG, obj)
    }

    @JvmStatic
    fun w(obj: Any) {
        w(Constants.TAG, obj)
    }

    @JvmStatic
    fun e(obj: Any) {
        e(Constants.TAG, obj)
    }

    @JvmStatic
    fun e(tag: String?, obj: Any) {
        Log.e(tag, obj.toString())
    }

    @JvmStatic
    fun w(tag: String?, obj: Any) {
        Log.w(tag, obj.toString())
    }

    @JvmStatic
    fun d(tag: String?, obj: Any) {
        Log.d(tag, obj.toString())
    }
}