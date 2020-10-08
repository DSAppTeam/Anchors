package com.effective.android.anchors

import android.util.SparseArray

object EmptyTask: Task("inner_default_empty_task"){
    override fun run(name: String) {
    }
}

class TaskRuntimeInfo(var task: Task) {

    val stateTime: SparseArray<Long>
    var isAnchor = false
    val dependencies: Set<String>
    var threadName: String = ""

    /**
     * 避免task泄漏
     */
    fun clearTask() {
        task = EmptyTask
    }

    val isProject: Boolean
        get() = task is Project

    fun setStateTime(@TaskState state: Int, time: Long) {
        stateTime.put(state, time)
    }

    fun isTaskInfo(task: Task?): Boolean {
        return task != null && this.task === task
    }

    val taskId: String
        get() = task.id

    companion object {
        private const val DEFAULT_TIME = -1L
    }

    init {
        threadName = ""
        stateTime = SparseArray()
        setStateTime(TaskState.START, DEFAULT_TIME)
        setStateTime(TaskState.RUNNING, DEFAULT_TIME)
        setStateTime(TaskState.FINISHED, DEFAULT_TIME)
        dependencies = task.getDependTaskName()
    }
}