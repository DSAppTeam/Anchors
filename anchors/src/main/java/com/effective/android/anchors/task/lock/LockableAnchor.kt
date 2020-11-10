package com.effective.android.anchors.task.lock

import android.os.Handler
import com.effective.android.anchors.Constants
import com.effective.android.anchors.log.Logger.d

class LockableAnchor internal constructor(private val handler: Handler) {
    private var lockListener: LockListener? = null
    var lockId: String? = null
        private set
    private val mLockObject = Object()
    private var releaseListeners = mutableListOf<ReleaseListener>()
    private var unlock = false

    /**
     * 外部监听上锁时机，在监听到上锁之后处理业务逻辑，并完成解锁或者破坏锁行为。
     * @param lockListener
     */
    fun setLockListener(lockListener: LockListener) {
        this.lockListener = lockListener
    }

    internal fun addReleaseListener(releaseListener: ReleaseListener) {
        if (!releaseListeners.contains(releaseListener)) {
            releaseListeners.add(releaseListener)
        }
    }

    internal fun setTargetTaskId(id: String?) {
        lockId = id
    }

    internal fun successToUnlock(): Boolean {
        return unlock
    }

    /**
     * 解锁，任务链后续任务继续执行
     */
    fun unlock() {
        handler.post {
            d(Constants.LOCK_TAG, Thread.currentThread().name + "- unlock( " + lockId + " )")
            d(Constants.LOCK_TAG, "Continue the task chain...")
            unlockInner()
        }
    }

    @Synchronized
    internal fun unlockInner() {
        synchronized(mLockObject) {
            unlock = true
            mLockObject.notify()
            for (releaseListener in releaseListeners) {
                releaseListener.release()
            }
            releaseListeners.clear()
        }
    }

    @Synchronized
    internal fun smashInner() {
        synchronized(mLockObject) {
            unlock = false
            mLockObject.notify()
            for (releaseListener in releaseListeners) {
                releaseListener.release()
            }
            releaseListeners.clear()
        }
    }

    /**
     * 破坏锁，任务链后续任务无法继续执行
     */
    fun smash() {
        handler.post {
            d(Constants.LOCK_TAG, Thread.currentThread().name + "- smash( " + lockId + " )")
            d(Constants.LOCK_TAG, "Terminate task chain !")
            smashInner()
        }
    }

    /**
     * 上锁
     */
    internal fun lock() {
        d(Constants.LOCK_TAG, Thread.currentThread().name + "- lock( " + lockId + " )")
        try {
            synchronized(mLockObject) {
                handler.post(Runnable {
                    lockListener?.lockUp()
                })
                mLockObject.wait()
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    interface LockListener {
        fun lockUp()
    }

    interface ReleaseListener {
        fun release()
    }

}