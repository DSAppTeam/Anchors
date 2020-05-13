package com.effective.android.anchors

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