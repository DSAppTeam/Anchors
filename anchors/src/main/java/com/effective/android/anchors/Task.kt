package com.effective.android.anchors

import android.os.Build
import android.os.Trace
import android.text.TextUtils
import com.effective.android.anchors.Utils.compareTask
import java.util.*

/**
 * created by yummylau on 2019/03/11
 */
abstract class Task @JvmOverloads constructor(//mId,唯一存在
        val id: String, //是否是异步存在
        val isAsyncTask: Boolean = false) : Runnable, Comparable<Task> {

    @TaskState
    var state: Int
        protected set
    var priority //优先级，数值越低，优先级越低
            : Int
    var executeTime: Long = 0
        protected set
    val behindTasks = mutableListOf<Task>() //被依赖者
    val dependTasks = mutableSetOf<Task>() //依赖者
    private val taskListeners: MutableList<TaskListener> = ArrayList() //监听器
    private var logTaskListeners: TaskListener? = LogTaskListener()

    fun addTaskListener(taskListener: TaskListener?) {
        if (taskListener != null && !taskListeners.contains(taskListener)) {
            taskListeners.add(taskListener)
        }
    }

    @Synchronized
    open fun start() {
        if (state != TaskState.IDLE) {
            throw RuntimeException("can no run task $id again!")
        }
        toStart()
        executeTime = System.currentTimeMillis()
        AnchorsRuntime.executeTask(this)
    }

    override fun run() {
        if (AnchorsRuntime.debuggable() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.beginSection(id)
        }
        toRunning()
        run(id)
        toFinish()
        notifyBehindTasks()
        release()
        if (AnchorsRuntime.debuggable() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection()
        }
    }

    protected abstract fun run(name: String)

    fun toStart() {
        state = TaskState.START
        AnchorsRuntime.setStateInfo(this)
        if (AnchorsRuntime.debuggable()) {
            logTaskListeners!!.onStart(this)
        }
        for (listener in taskListeners) {
            listener.onStart(this)
        }
    }

    fun toRunning() {
        state = TaskState.RUNNING
        AnchorsRuntime.setStateInfo(this)
        AnchorsRuntime.setThreadName(this, Thread.currentThread().name)
        if (AnchorsRuntime.debuggable()) {
            logTaskListeners!!.onRunning(this)
        }
        for (listener in taskListeners) {
            listener.onRunning(this)
        }
    }

    fun toFinish() {
        state = TaskState.FINISHED
        AnchorsRuntime.setStateInfo(this)
        if (AnchorsRuntime.debuggable()) {
            logTaskListeners!!.onFinish(this)
        }
        for (listener in taskListeners) {
            listener.onFinish(this)
        }
    }

    val dependTaskName: Set<String>
        get() {
            val result: MutableSet<String> = HashSet()
            for (task in dependTasks) {
                result.add(task.id)
            }
            return result
        }


    fun removeDepend(originTask: Task?) {
        if (dependTasks.contains(originTask)) {
            dependTasks.remove(originTask)
        }
    }

    fun updateBehind(updateTask: Task, originTask: Task?) {
        if (behindTasks.contains(originTask)) {
            behindTasks.remove(originTask)
        }
        behindTasks.add(updateTask)
    }

    /**
     * 后置触发, 和 [Task.dependOn] 方向相反，都可以设置依赖关系
     *
     * @param task
     */
    open fun behind(task: Task) {
        var task = task
        if (task !== this) {
            if (task is Project) {
                task = task.startTask
            }
            behindTasks.add(task)
            task.dependOn(this)
        }
    }

    open fun removeBehind(task: Task) {
        var task = task
        if (task !== this) {
            if (task is Project) {
                task = task.startTask
            }
            behindTasks.remove(task)
            task.removeDependence(this)
        }
    }

    /**
     * 前置条件, 和 [Task.behind] 方向相反，都可以设置依赖关系
     *
     * @param task
     */
    open fun dependOn(task: Task) {
        var task = task
        if (task !== this) {
            if (task is Project) {
                task = task.endTask
            }
            dependTasks.add(task)
            //防止外部所有直接调用dependOn无法构建完整图
            if (!task.behindTasks.contains(this)) {
                task.behindTasks.add(this)
            }
        }
    }

    open fun removeDependence(task: Task) {
        var task = task
        if (task !== this) {
            if (task is Project) {
                task = task.endTask
            }
            dependTasks.remove(task)
            if (task.behindTasks.contains(this)) {
                task.behindTasks.remove(this)
            }
        }
    }

    override fun compareTo(o: Task): Int {
        return compareTask(this, o)
    }

    /**
     * 通知后置者自己已经完成了
     */
    fun notifyBehindTasks() {
        if (this is LockableTask) {
            if (!this.successToUnlock()) {
                return
            }
        }
        if (behindTasks.isNotEmpty()) {
            if (behindTasks.size > 1) {
                Collections.sort(behindTasks, AnchorsRuntime.taskComparator)
            }
            //遍历记下来的任务，通知它们说存在的前置已经完成
            for (task in behindTasks) {
                task.dependTaskFinish(this)
            }
        }
    }

    /**
     * 依赖的任务已经完成
     * 比如 B -> A (B 依赖 A), A 完成之后调用该方法通知 B "A依赖已经完成了"
     * 当且仅当 B 的所有依赖都已经完成了, B 开始执行
     *
     * @param dependTask
     */
    @Synchronized
    fun dependTaskFinish(dependTask: Task?) {
        if (dependTasks.isEmpty()) {
            return
        }
        dependTasks.remove(dependTask)
        //所有前置任务都已经完成了
        if (dependTasks.isEmpty()) {
            start()
        }
    }

    open fun release() {
        state = TaskState.RELEASE
        AnchorsRuntime.setStateInfo(this)
        AnchorsRuntime.removeAnchorTask(id)
        AnchorsRuntime.getTaskRuntimeInfo(id)?.clearTask()
        dependTasks.clear()
        behindTasks.clear()
        if (AnchorsRuntime.debuggable()) {
            logTaskListeners?.onRelease(this)
            logTaskListeners = null
        }
        for (listener in taskListeners) {
            listener.onRelease(this)
        }
        taskListeners.clear()
    }

    companion object {
        const val DEFAULT_PRIORITY = 0
    }

    init {
        priority = DEFAULT_PRIORITY
        require(!TextUtils.isEmpty(id)) { "task's mId can't be empty" }
        state = TaskState.IDLE
    }
}