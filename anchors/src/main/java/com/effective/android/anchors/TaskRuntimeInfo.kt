package com.effective.android.anchors

import android.util.SparseArray

object EmptyTask: Task("  "){
    override fun run(name: String) {
    }
}

class TaskRuntimeInfo(var task: Task) {
    val stateTime: SparseArray<Long> = SparseArray()
    var isAnchor: Boolean = false
    var threadName: String = ""

    init {
        setStateTime(TaskState.START, DEFAULT_TIME)
        setStateTime(TaskState.RUNNING, DEFAULT_TIME)
        setStateTime(TaskState.FINISHED, DEFAULT_TIME)
    }

    /**
     * 避免task泄漏
     */
    fun clearTask() {
        task = EmptyTask
    }

    val isProject: Boolean = task is Project

    fun setStateTime(@TaskState state: Int, time: Long) {
        stateTime.put(state, time)
    }

    val dependencies = task.dependTaskName

    fun isTaskInfo(task: Task): Boolean {
        return this.task === task
    }

    val taskId: String = task.id

    companion object {
        private val DEFAULT_TIME: Long = -1L
    }
}