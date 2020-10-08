package com.effective.android.anchors

import android.support.annotation.MainThread
import com.effective.android.anchors.AnchorsRuntime.addAnchorTasks
import com.effective.android.anchors.AnchorsRuntime.anchorTasks
import com.effective.android.anchors.AnchorsRuntime.clear
import com.effective.android.anchors.AnchorsRuntime.debuggable
import com.effective.android.anchors.AnchorsRuntime.handler
import com.effective.android.anchors.AnchorsRuntime.hasAnchorTasks
import com.effective.android.anchors.AnchorsRuntime.hasRunTasks
import com.effective.android.anchors.AnchorsRuntime.openDebug
import com.effective.android.anchors.AnchorsRuntime.traversalDependenciesAndInit
import com.effective.android.anchors.AnchorsRuntime.tryRunBlockRunnable
import com.effective.android.anchors.Logger.d
import com.effective.android.anchors.Utils.assertMainThread
import com.effective.android.anchors.Utils.insertAfterTask
import java.util.*
import kotlin.collections.LinkedHashSet

object AnchorsManager {

    var debuggable = false
    var anchorTaskIds: MutableSet<String> = HashSet()
    var curBlockAnchor: LockableAnchor? = null

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
    fun requestBlockWhenFinish(task: Task): LockableAnchor {
        val lockableAnchor = LockableAnchor(handler)
        val lockableTask = LockableTask(task, lockableAnchor)
        insertAfterTask(lockableTask, task)
        curBlockAnchor = lockableAnchor
        curBlockAnchor?.setReleaseListener(object : LockableAnchor.ReleaseListener {
            override fun release() {
                curBlockAnchor = null
            }
        })
        return lockableAnchor
    }

    //用于兼容旧版本java
    fun addAnchors(vararg taskIds: String): AnchorsManager {
        if (taskIds.isNotEmpty()) {
            for (id in taskIds) {
                if (id.isNotEmpty()) {
                    anchorTaskIds.add(id)
                }
            }
        }
        return this
    }

    fun addAnchor(vararg taskIds: String): AnchorsManager {
        if (taskIds.isNotEmpty()) {
            for (id in taskIds) {
                if (id.isNotEmpty()) {
                    anchorTaskIds.add(id)
                }
            }
        }
        return this
    }

    fun syncConfigInfoToRuntime() {
        clear()
        openDebug(debuggable)
        addAnchorTasks(anchorTaskIds)
        debuggable = false
        anchorTaskIds.clear()
    }

    @MainThread
    @Synchronized
    fun start(task: Task?) {
        assertMainThread()
        if (task == null) {
            throw RuntimeException("can no run a task that was null !")
        }
        syncConfigInfoToRuntime()
        var startTask = task
        if (startTask is Project) {
            startTask = (task as Project).startTask
        }
        traversalDependenciesAndInit(startTask)
        val logEnd = logStartWithAnchorsInfo()
        startTask.start()
        while (hasAnchorTasks()) {
            try {
                Thread.sleep(10)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            while (hasRunTasks()) {
                tryRunBlockRunnable()
            }
        }
        if (logEnd) {
            logEndWithAnchorsInfo()
        }
    }


    @Synchronized
    @JvmStatic
    fun getInstance(): AnchorsManager {
        return this
    }

    @Synchronized
    @JvmStatic
    fun instance(): AnchorsManager {
        return this
    }


    /**
     * 打印锚点信息
     *
     * @return
     */
    private fun logStartWithAnchorsInfo(): Boolean {
        if (!debuggable()) {
            return false
        }
        val stringAnchorsManagerBuilder = StringBuilder()
        val hasAnchorTask = hasAnchorTasks()
        if (hasAnchorTask) {
            stringAnchorsManagerBuilder.append(Constants.HAS_ANCHOR)
            stringAnchorsManagerBuilder.append("( ")
            for (taskId in anchorTasks) {
                stringAnchorsManagerBuilder.append("\"$taskId\" ")
            }
            stringAnchorsManagerBuilder.append(")")
        } else {
            stringAnchorsManagerBuilder.append(Constants.NO_ANCHOR)
        }
        d(Constants.ANCHORS_INFO_TAG, stringAnchorsManagerBuilder.toString())
        return hasAnchorTask
    }

    /**
     * 打印锚点信息
     */
    private fun logEndWithAnchorsInfo() {
        if (!debuggable()) {
            return
        }
        d(Constants.ANCHORS_INFO_TAG, Constants.ANCHOR_RELEASE)
    }
}


internal object AnchorsManagerBuilder {
    var debuggable = false
    var anchors: MutableList<String> = mutableListOf()
    var factory: Project.TaskFactory? = null
    var block: String? = null
    var blockListener: ((lockableAnchor: LockableAnchor) -> Unit)? = null
    val allTask: LinkedHashSet<Task> = LinkedHashSet<Task>()

    fun setUp() {
        debuggable = false
        anchors.clear()
        factory = null
        block = null
        blockListener = null
        allTask.clear()
    }

    fun makeTask(taskId: String): Task? = factory?.getTask(taskId)
}


fun AnchorsManager.debuggable(init: () -> Boolean): AnchorsManager {
    AnchorsManagerBuilder.debuggable = init.invoke()
    return this
}

fun AnchorsManager.anchors(init: () -> Array<String>): AnchorsManager {
    val anchorList = init.invoke()
    if (anchorList.isNotEmpty()) {
        for (taskId in anchorList) {
            if (taskId.isNotEmpty()) {
                AnchorsManagerBuilder.anchors.add(taskId)
            }
        }
    }
    return this
}

fun AnchorsManager.taskFactory(init: () -> Project.TaskFactory): AnchorsManager {
    AnchorsManagerBuilder.factory = init.invoke()
    return this
}

fun AnchorsManager.block(block: String, listener: (lockableAnchor: LockableAnchor) -> Unit): AnchorsManager {
    AnchorsManagerBuilder.block = block
    AnchorsManagerBuilder.blockListener = listener
    return this
}

fun AnchorsManager.graphics(graphics: () -> Array<String>): AnchorsManager {
    // 内部调用 String.sons 扩展，将任务信息存入 allTask
    graphics.invoke()
    return this
}


fun AnchorsManager.startUp(): AnchorsManager {

    debuggable = AnchorsManagerBuilder.debuggable

    if (AnchorsManagerBuilder.anchors.isNotEmpty()) {
        for (taskId in AnchorsManagerBuilder.anchors) {
            addAnchor(taskId)
        }
    }

    requireNotNull(AnchorsManagerBuilder.factory) { "kotlin dsl-build should set TaskFactory with invoking AnchorsManager#taskFactory()" }

    if (AnchorsManagerBuilder.allTask.isEmpty()) {
        Logger.w("No task is run ！")
        return this
    }

    val setUp = object : Task("inner_start_up_task") {
        override fun run(name: String) {
            Logger.d("task(inner_start_up_task) start !")
        }
    }

    if (!AnchorsManagerBuilder.block.isNullOrEmpty()) {
        val blockTask = AnchorsManagerBuilder.makeTask(AnchorsManagerBuilder.block!!)
        if (blockTask == null) {
            Logger.w("can find task's id = ${AnchorsManagerBuilder.block} in factory")
        } else {
            val lock = requestBlockWhenFinish(blockTask)
            val listener = AnchorsManagerBuilder.blockListener
            lock.setLockListener(object : LockableAnchor.LockListener {
                override fun lockUp() {
                    listener?.invoke(lock)
                }
            })
        }
    }

    if (AnchorsManagerBuilder.allTask.size == 1) {
        start(AnchorsManagerBuilder.allTask.first())
    } else {
        AnchorsManagerBuilder.allTask.forEach {
            setUp.behind(it)
        }
        start(setUp)
    }
    AnchorsManagerBuilder.setUp()
    return this
}

fun String.sons(vararg taskIds: String): String {
    val curTask = AnchorsManagerBuilder.makeTask(this)
    if (curTask == null) {
        Logger.w("can find task's id = $this in factory,skip it's all sons")
        return this
    }
    if (!AnchorsManagerBuilder.allTask.contains(curTask)) {
        AnchorsManagerBuilder.allTask.add(curTask)
    }
    if (taskIds.isNotEmpty()) {
        for (taskId in taskIds) {
            if (taskId.isNotEmpty()) {
                val task = AnchorsManagerBuilder.makeTask(taskId)
                if (task == null) {
                    Logger.w("can find task's id = $taskId in factory,skip this son")
                    continue
                }
                if (!AnchorsManagerBuilder.allTask.contains(task)) {
                    AnchorsManagerBuilder.allTask.add(task)
                }
                task.dependOn(curTask)
            }
        }
    }
    return this;
}

fun String.alsoParents(vararg taskIds: String): String {
    val curTask = AnchorsManagerBuilder.makeTask(this)
    if (curTask == null) {
        Logger.w("can find task's id = $this in factory,skip it's all sons")
        return this
    }
    if (!AnchorsManagerBuilder.allTask.contains(curTask)) {
        AnchorsManagerBuilder.allTask.add(curTask)
    }
    if (taskIds.isNotEmpty()) {
        for (taskId in taskIds) {
            if (taskId.isNotEmpty()) {
                val task = AnchorsManagerBuilder.makeTask(taskId)
                if (task == null) {
                    Logger.w("can find task's id = $taskId in factory,skip this son")
                    continue
                }
                if (!AnchorsManagerBuilder.allTask.contains(task)) {
                    AnchorsManagerBuilder.allTask.add(task)
                }
                task.behind(curTask)
            }
        }
    }
    return this
}

