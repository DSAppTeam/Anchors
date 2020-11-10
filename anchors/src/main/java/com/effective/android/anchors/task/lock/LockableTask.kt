package com.effective.android.anchors.task.lock

import com.effective.android.anchors.task.Task

internal class LockableTask(wait: Task, lockableAnchor: LockableAnchor) : Task(wait.id + "_waiter", true) {
    private val lockableAnchor: LockableAnchor
    override fun run(name: String) {
        lockableAnchor.lock()
    }

    fun successToUnlock(): Boolean {
        return lockableAnchor.successToUnlock()
    }

    init {
        lockableAnchor.setTargetTaskId(wait.id)
        this.lockableAnchor = lockableAnchor
    }
}