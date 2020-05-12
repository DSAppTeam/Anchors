package com.effective.android.anchors

interface TaskListener {
    fun onStart(task: Task)
    fun onRunning(task: Task)
    fun onFinish(task: Task)
    fun onRelease(task: Task)
}