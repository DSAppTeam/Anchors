package com.effective.android.anchors

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import com.effective.android.anchors.Logger.w
import com.effective.android.anchors.Utils.compareTask
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Anchors 框架 runtime 信息管理
 * 包含：
 * application 锚点管理
 * application UIThreadTask 运行管理
 * 调试配置
 * 线程池配置
 * 运行时 Task 信息收集
 * created by yummylau on 2019/03/12
 */
internal object AnchorsRuntime {
    //调试信息
    private var sDebuggable = false
    //线程池
    private val sPool = InnerThreadPool()
    //设置锚点任务，当且仅当所有锚点任务都完成时, application 不在阻塞 UIThread
    private val sAnchorTaskIds: MutableSet<String> = mutableSetOf()
    //如果存在锚点任务，则同步的任务都所有锚点任务都完成前，在 UIThread 上运行
    //ps: 后续解除锚点之后，所有UI线程上的 Task 都通过 handle 发送执行，不保证业务逻辑的同步。
    private val sRunBlockApplication: MutableList<Task> = mutableListOf()
    val handler = Handler(Looper.getMainLooper())

    //所有 task 运行时信息
    private val sTaskRuntimeInfo: MutableMap<String, TaskRuntimeInfo> = HashMap()
    //Task 比较逻辑
    val taskComparator: Comparator<Task> = Comparator { lhs, rhs -> compareTask(lhs, rhs) }

    @JvmStatic
    fun clear() {
        sDebuggable = false
        sAnchorTaskIds.clear()
        sRunBlockApplication.clear()
        sTaskRuntimeInfo.clear()
    }

    @JvmStatic
    fun debuggable(): Boolean {
        return sDebuggable
    }

    @JvmStatic
    fun openDebug(debug: Boolean) {
        sDebuggable = debug
    }

    @JvmStatic
    fun addAnchorTasks(ids: Set<String>) {
        if (ids.isNotEmpty()) {
            sAnchorTaskIds.addAll(ids)
        }
    }

    fun removeAnchorTask(id: String) {
        if (!TextUtils.isEmpty(id)) {
            sAnchorTaskIds.remove(id)
        }
    }

    @JvmStatic
    fun hasAnchorTasks(): Boolean {
        return sAnchorTaskIds.isNotEmpty()
    }

    @JvmStatic
    val anchorTasks: Set<String>
        get() = sAnchorTaskIds

    private fun addRunTasks(task: Task) {
        if (!sRunBlockApplication.contains(task)) {
            sRunBlockApplication.add(task)
        }
    }

    @JvmStatic
    fun tryRunBlockRunnable() {
        if (sRunBlockApplication.isNotEmpty()) {
            if (sRunBlockApplication.size > 1) {
                Collections.sort(sRunBlockApplication, taskComparator)
            }
            val runnable: Runnable = sRunBlockApplication.removeAt(0)
            if (hasAnchorTasks()) {
                runnable.run()
            } else {
                handler.post(runnable)
                for (blockItem in sRunBlockApplication) {
                    handler.post(blockItem)
                }
                sRunBlockApplication.clear()
            }
        }
    }

    @JvmStatic
    fun hasRunTasks(): Boolean {
        return sRunBlockApplication.isNotEmpty()
    }

    private fun hasTaskRuntimeInfo(taskId: String): Boolean {
        return sTaskRuntimeInfo[taskId] != null
    }

    fun getTaskRuntimeInfo(taskId: String): TaskRuntimeInfo? {
        return sTaskRuntimeInfo[taskId]
    }

    fun setThreadName(task: Task, threadName: String) {
        val taskRuntimeInfo = sTaskRuntimeInfo[task.id]
        if (taskRuntimeInfo != null) {
            taskRuntimeInfo.threadName = threadName
        }
    }

    fun setStateInfo(task: Task) {
        val taskRuntimeInfo = sTaskRuntimeInfo[task.id]
        taskRuntimeInfo?.setStateTime(task.state, System.currentTimeMillis())
    }

    fun executeTask(task: Task) {
        if (task.isAsyncTask) {
            sPool.executeTask(task)
        } else {
            if (!hasAnchorTasks()) {
                handler.post(task)
            } else {
                addRunTasks(task)
            }
        }
    }

    /**
     * 遍历依赖树并完成启动前的初始化
     *
     *
     * 1.获取依赖树最大深度
     * 2.遍历初始化运行时数据并打印log
     * 3.如果锚点不存在，则移除
     * 4.提升锚点链的优先级
     *
     * @param task
     */
    @JvmStatic
    fun traversalDependenciesAndInit(task: Task) {

        val traversalVisitor: LinkedHashSet<Task> = linkedSetOf()
        traversalVisitor.add(task)
        traversalDependenciesAndInit(task, traversalVisitor)

        val iterator = sAnchorTaskIds.iterator()
        while (iterator.hasNext()) {
            val taskId = iterator.next()
            if (!hasTaskRuntimeInfo(taskId)) {
                w(Constants.ANCHORS_INFO_TAG, "anchor \"$taskId\" no found !")
                iterator.remove()
            } else {
                val info = getTaskRuntimeInfo(taskId)
                traversalMaxTaskPriority(info?.task)
            }
        }
    }

    /**
     * 回溯算法遍历依赖树，初始化任务，并记录log
     *
     * 如果单条依赖线上存在重复依赖将抛出异常（会造成依赖回环）
     */
    @JvmStatic
    private fun traversalDependenciesAndInit(task: Task, traversalVisitor: LinkedHashSet<Task>) {

        val taskRuntimeInfo = getTaskRuntimeInfo(task.id)
        if (taskRuntimeInfo == null) {
            // 如果没有初始化则初始化runtimeInfo
            val info = TaskRuntimeInfo(task)

            if (sAnchorTaskIds.contains(task.id)) {
                info.isAnchor = true
            }
            sTaskRuntimeInfo[task.id] = info
        } else {
            if (!taskRuntimeInfo.isTaskInfo(task)) {
                throw RuntimeException("Multiple different tasks are not allowed to contain the same id (${task.id})!")
            }
        }

        for (nextTask in task.behindTasks) {
            if (!traversalVisitor.contains(nextTask)) {
                traversalVisitor.add(nextTask)
            } else {
                throw RuntimeException("Do not allow dependency graphs to have a loopback！Related task'id is ${task.id} !")
            }

            if (sDebuggable && nextTask.behindTasks.isEmpty()) {
                val iterator = traversalVisitor.iterator()
                val builder = StringBuilder()
                while (iterator.hasNext()) {
                    builder.append(iterator.next().id)
                    builder.append(" --> ")
                }
                // traversalVisitor 一定不为空，故可以 length-5
                Log.d(Constants.DEPENDENCE_TAG, builder.substring(0, builder.length - 5))
            }

            traversalDependenciesAndInit(nextTask, traversalVisitor)

            traversalVisitor.remove(nextTask)
        }
    }

    /**
     * 递归向上设置优先级
     *
     * @param task
     */
    private fun traversalMaxTaskPriority(task: Task?) {
        if (task == null) {
            return
        }
        task.priority = Int.MAX_VALUE
        for (dependence in task.dependTasks) {
            traversalMaxTaskPriority(dependence)
        }
    }

    internal class InnerThreadPool {
        private val asyncThreadExecutor: ExecutorService
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        /**
         * [android.os.AsyncTask]
         * 相对比 Anchors, AsyncTask 更强调的是在业务处理中，异步业务不应该使得cpu饱和，但是启动场景时间比较短，可以尽可能使用更多的cpu资源。
         * 但是，anchors支持锚点阻塞ui线程，后续可能还会有延迟的异步初始化任务，所以也不要完全饱和。
         */
        private val CORE_POOL_SIZE = Math.max(4, Math.min(CPU_COUNT - 1, 8))
        private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1
        private val KEEP_ALIVE_SECONDS = 30L
        private val sThreadFactory: ThreadFactory = object : ThreadFactory {
            private val mCount = AtomicInteger(1)
            override fun newThread(r: Runnable): Thread {
                return Thread(r, "Anchors Thread #" + mCount.getAndIncrement())
            }
        }
        private val sPoolWorkQueue: BlockingQueue<Runnable> = PriorityBlockingQueue(128)
        fun executeTask(runnable: Runnable?) {
            asyncThreadExecutor.execute(runnable)
        }

        init {
            val threadPoolExecutor = ThreadPoolExecutor(
                    CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                    sPoolWorkQueue, sThreadFactory)
            threadPoolExecutor.allowCoreThreadTimeOut(true)
            asyncThreadExecutor = threadPoolExecutor
        }
    }
}