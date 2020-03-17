package com.effective.android.anchors

import androidx.annotation.MainThread

object AnchorsManager {
    var debuggable = false
    var anchorTaskIds = mutableSetOf<String>()
    fun debuggable(debuggable: Boolean): AnchorsManager {
        this.debuggable = debuggable
        return this
    }

    /**
     * 扩展支持 https://github.com/YummyLau/Anchors/issues/7   暂停机制
     * 调用前须知：
     * 1. 请充分理解 anchor 的作用并明白，为何 application sleep 频繁等待代码块执行的原因
     * 2. 如果调用 requestBlockWhenFinish 则意味着任务链在 task 执行完毕之后会进入等待阶段，如果此时等待的 task 在[初始节点，Anchors]链中则可能导致界面卡主
     * 3. 在调用 requestBlockWhenFinish 设置等待任务的前提下务必保证 anchors 已经解锁 或者 任务链上没有 anchors。
     * @param task block目标task
     * @return
     */
    fun requestBlockWhenFinish(task: Task): LockableAnchor? {
        return requestBlockWhenFinishInner(task)
    }

    fun requestBlockWhenFinishInner(task: Task): LockableAnchor? {
        if (task.id.isNotEmpty()) {
            val lockableAnchor = LockableAnchor(AnchorsRuntime.handler)
            val lockableTask = LockableTask(task, lockableAnchor)
            Utils.insertAfterTask(lockableTask, task)
            return lockableAnchor
        }
        return null
    }

    fun addAnchor(taskId: String): AnchorsManager {
        if (taskId.isNotEmpty()) {
            anchorTaskIds.add(taskId)
        }
        return this
    }

    fun addAnchors(vararg taskIds: String): AnchorsManager {
        for (id in taskIds) {
            anchorTaskIds.add(id)
        }
        return this
    }

    fun syncConfigInfoToRuntime() {
        AnchorsRuntime.clear()
        AnchorsRuntime.openDebug(debuggable)
        AnchorsRuntime.addAnchorTasks(anchorTaskIds)
        debuggable = false
        anchorTaskIds.clear()
    }

    @MainThread
    @Synchronized
    fun start(task: Task) {
        var startTask = task
        Utils.assertMainThread()
        syncConfigInfoToRuntime()
        if (task is Project) {
            startTask = task.startTask
        }
        AnchorsRuntime.traversalDependenciesAndInit(startTask)
        val logEnd = logStartWithAnchorsInfo()
        startTask.start()
        while (AnchorsRuntime.hasAnchorTasks()) {
            try {
                Thread.sleep(10)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            while (AnchorsRuntime.hasRunTasks()) {
                AnchorsRuntime.tryRunBlockRunnable()
            }
        }
        if (logEnd) {
            logEndWithAnchorsInfo()
        }
    }


    /**
     * 打印锚点信息
     *
     * @return
     */
    private fun logStartWithAnchorsInfo(): Boolean {
        if (!AnchorsRuntime.debuggable()) {
            return false
        }
        val stringBuilder = StringBuilder()
        val hasAnchorTask = AnchorsRuntime.hasAnchorTasks()
        if (hasAnchorTask) {
            stringBuilder.append(Constants.HAS_ANCHOR)
            stringBuilder.append("( ")
            for (taskId in AnchorsRuntime.anchorTasks) {
                stringBuilder.append("\"$taskId\" ")
            }
            stringBuilder.append(")")
        } else {
            stringBuilder.append(Constants.NO_ANCHOR)
        }
        Logger.d(Constants.ANCHORS_INFO_TAG, stringBuilder.toString())
        return hasAnchorTask
    }

    /**
     * 打印锚点信息
     */
    private fun logEndWithAnchorsInfo() {
        if (!AnchorsRuntime.debuggable()) {
            return
        }
        Logger.d(Constants.ANCHORS_INFO_TAG, Constants.ANCHOR_RELEASE)
    }

}