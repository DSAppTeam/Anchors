package com.effective.android.anchors

import android.os.Handler

class LockableAnchor internal constructor(private val handler: Handler) {
    private var lockListener: LockListener? = null
    var lockId: String = ""
        private set
    private val mLockObject = Object()
    private var unlock = false

    /**
     * 外部监听上锁时机，在监听到上锁之后处理业务逻辑，并完成解锁或者破坏锁行为。
     * @param lockListener
     */
    fun setLockListener(lockListener: LockListener?) {
        this.lockListener = lockListener
    }

    fun setTargetTaskId(id: String) {
        lockId = id
    }

    fun successToUnlock(): Boolean {
        return unlock
    }

    /**
     * 解锁，任务链后续任务继续执行
     */
    fun unlock() {
        handler.post {
            Logger.d(Constants.LOCK_TAG, Thread.currentThread().name + "- unlock( " + lockId + " )")
            Logger.d(Constants.LOCK_TAG, "Continue the task chain...")
            unlockInner()
        }
    }

    @Synchronized
    fun unlockInner() {
        synchronized(mLockObject) {
            unlock = true
            mLockObject.notify()
        }
    }

    @Synchronized
    fun smashInner() {
        synchronized(mLockObject) {
            unlock = false
            mLockObject.notify()
        }
    }

    /**
     * 破坏锁，任务链后续任务无法继续执行
     */
    fun smash() {
        handler.post {
            Logger.d(Constants.LOCK_TAG, Thread.currentThread().name + "- smash( " + lockId + " )")
            Logger.d(Constants.LOCK_TAG, "Terminate task chain !")
            smashInner()
        }
    }

    /**
     * 上锁
     */
    fun lock() {
        Logger.d(Constants.LOCK_TAG, Thread.currentThread().name + "- lock( " + lockId + " )")
        try {
            synchronized(mLockObject) {
                handler.post {
                    lockListener?.lockUp()
                }
                mLockObject.wait()
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    interface LockListener {
        fun lockUp()
    }
}