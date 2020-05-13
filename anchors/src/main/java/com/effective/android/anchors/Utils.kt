package com.effective.android.anchors

import android.os.Looper

object Utils {

    @JvmStatic
    fun insertAfterTask(insert: Task, targetTask: Task) {
        val taskBehinds = targetTask.behindTasks
        for (behind in taskBehinds) {
            behind.removeDepend(targetTask)
            insert.behind(behind)
        }
        targetTask.behindTasks.clear()
        insert.dependOn(targetTask)
    }

    /**
     * 比较两个 task
     * [Task.getPriority] 值高的，优先级高
     * [Task.getExecuteTime] 添加到队列的时间最早，优先级越高
     *
     * @param task
     * @param o
     * @return
     */
    @JvmStatic
    fun compareTask(task: Task, o: Task): Int {
        if (task.priority < o.priority) {
            return 1
        }
        if (task.priority > o.priority) {
            return -1
        }
        if (task.executeTime < o.executeTime) {
            return -1
        }
        return if (task.executeTime > o.executeTime) {
            1
        } else 0
    }

    @JvmStatic
    fun assertMainThread() {
        if (Thread.currentThread() !== Looper.getMainLooper().thread) {
            throw RuntimeException("AnchorsManager#start should be invoke on MainThread!")
        }
    }
}