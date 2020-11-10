package com.effective.android.anchors.task

import android.support.annotation.IntDef

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(TaskState.IDLE, TaskState.RUNNING, TaskState.FINISHED, TaskState.START, TaskState.RELEASE)
annotation class TaskState {
    companion object {
        const val IDLE = 0 //静止
        const val START = 1 //启动,可能需要等待调度，
        const val RUNNING = 2 //运行
        const val FINISHED = 3 //运行结束
        const val RELEASE = 4 //释放
    }
}