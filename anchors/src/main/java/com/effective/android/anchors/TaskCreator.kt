package com.effective.android.anchors

interface TaskCreator {
    fun createTask(taskName: String): Task?
}