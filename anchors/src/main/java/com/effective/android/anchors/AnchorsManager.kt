package com.effective.android.anchors

import android.support.annotation.MainThread
import com.effective.android.anchors.log.Logger.d
import com.effective.android.anchors.util.Utils.assertMainThread
import com.effective.android.anchors.util.Utils.insertAfterTask
import com.effective.android.anchors.log.Logger
import com.effective.android.anchors.task.lock.LockableTask
import com.effective.android.anchors.task.project.Project
import com.effective.android.anchors.task.Task
import com.effective.android.anchors.task.lock.LockableAnchor
import java.util.*
import java.util.concurrent.ExecutorService
import kotlin.collections.HashMap

/**
 * updated by yummylau on 2020/01/09 调整为非单例，支持扩展到任何场景
 */
class AnchorsManager {

    var debuggable = false
    private var anchorTaskIds: MutableSet<String> = HashSet()
    private var blockAnchors = HashMap<String, LockableAnchor?>()
    private var currentBlockAnchor: LockableAnchor? = null
    private val anchorsRuntime: AnchorsRuntime

    private constructor(executor: ExecutorService? = null) {
        this.anchorsRuntime = AnchorsRuntime(executor)
    }

    companion object {
        @JvmStatic
        @JvmOverloads
        fun getInstance(executor: ExecutorService? = null): AnchorsManager {
            return AnchorsManager(executor)
        }
    }

    fun getLockableAnchors(): Map<String, LockableAnchor?> {
        return blockAnchors
    }

    fun getAnchorsRuntime(): AnchorsRuntime {
        return anchorsRuntime
    }

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
        val lockableAnchor = LockableAnchor(anchorsRuntime.handler)
        val lockableTask = LockableTask(task, lockableAnchor)
        insertAfterTask(lockableTask, task)
        blockAnchors[task.id] = lockableAnchor
        lockableAnchor.addReleaseListener(object : LockableAnchor.ReleaseListener {
            override fun release() {
                blockAnchors[task.id] = null
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

    @MainThread
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
        anchorsRuntime.traversalDependenciesAndInit(startTask)
        val logEnd = logStartWithAnchorsInfo()
        startTask.start()
        anchorsRuntime.tryRunBlockTask()
        if (logEnd) {
            logEndWithAnchorsInfo()
        }
    }

    private fun syncConfigInfoToRuntime() {
        anchorsRuntime.clear()
        anchorsRuntime.debuggable = debuggable;
        anchorsRuntime.addAnchorTasks(anchorTaskIds)
        anchorTaskIds.clear()
    }


    private fun logStartWithAnchorsInfo(): Boolean {
        if (!debuggable) {
            return false
        }
        val stringAnchorsManagerBuilder = StringBuilder()
        val hasAnchorTask = anchorsRuntime.hasAnchorTasks()
        if (hasAnchorTask) {
            stringAnchorsManagerBuilder.append(Constants.HAS_ANCHOR)
            stringAnchorsManagerBuilder.append("( ")
            for (taskId in anchorsRuntime.anchorTaskIds) {
                stringAnchorsManagerBuilder.append("\"$taskId\" ")
            }
            stringAnchorsManagerBuilder.append(")")
        } else {
            stringAnchorsManagerBuilder.append(Constants.NO_ANCHOR)
        }
        if (debuggable) {
            d(Constants.ANCHORS_INFO_TAG, stringAnchorsManagerBuilder.toString())
        }
        return hasAnchorTask
    }

    private fun logEndWithAnchorsInfo() {
        if (!debuggable) {
            return
        }
        d(Constants.ANCHORS_INFO_TAG, Constants.ANCHOR_RELEASE)
    }
}


internal object AnchorsManagerBuilder {
    var debuggable = false
    var anchors: MutableList<String> = mutableListOf()
    var factory: Project.TaskFactory? = null
    var blocks = mutableMapOf<String, ((lockableAnchor: LockableAnchor) -> Unit)>()
    val allTask: MutableSet<Task> = mutableSetOf();
    var sons: Array<String>? = null

    fun setUp() {
        debuggable = false
        anchors.clear()
        factory = null
        blocks.clear()
        sons = null
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
    AnchorsManagerBuilder.blocks[block] = listener
    return this
}

fun AnchorsManager.graphics(graphics: () -> Array<String>): AnchorsManager {
    AnchorsManagerBuilder.sons = graphics.invoke();
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

    requireNotNull(AnchorsManagerBuilder.sons) { "kotlin dsl-build should set graphics with invoking AnchorsManager#graphics()" }

    val sons = AnchorsManagerBuilder.sons

    if (sons.isNullOrEmpty()) {
        Logger.w("No task is run ！")
        return this
    }

    val setUp = object : Task("inner_start_up_task") {
        override fun run(name: String) {
            Logger.d("task(inner_start_up_task) start !")
        }
    }

    val validSon = mutableListOf<Task>()

    if (sons.isNotEmpty()) {
        for (taskId in sons) {
            if (taskId.isNotEmpty()) {
                val son = AnchorsManagerBuilder.makeTask(taskId)
                if (son == null) {
                    Logger.w("can find task's id = $taskId in factory,skip this son")
                    continue
                }
                validSon.add(son);
            }
        }
    }

    if (validSon.isEmpty()) {
        Logger.w("No task is run ！")
        return this
    }

    if (!AnchorsManagerBuilder.blocks.isNullOrEmpty()) {
        AnchorsManagerBuilder.blocks.forEach {
            val blockTask = AnchorsManagerBuilder.makeTask(it.key)
            if (blockTask == null) {
                Logger.w("can find task's id = ${it.key} in factory")
            } else {
                val lock = requestBlockWhenFinish(blockTask)
                lock.setLockListener(object : LockableAnchor.LockListener {
                    override fun lockUp() {
                        it.value.invoke(lock)
                    }
                })
            }
        }
    }

    if (validSon.size == 1) {
        start(validSon[0])
    } else {
        for (task in validSon) {
            setUp.behind(task)
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


