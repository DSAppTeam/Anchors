package com.effective.android.anchors

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
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
    private val sAnchorTaskIds = mutableSetOf<String>()

    //如果存在锚点任务，则同步的任务都所有锚点任务都完成前，在 UIThread 上运行
    //ps: 后续解除锚点之后，所有UI线程上的 Task 都通过 handle 发送执行，不保证业务逻辑的同步。
    private val sRunBlockApplication = mutableListOf<Task>()

    val handler = Handler(Looper.getMainLooper())

    //所有 task 运行时信息
    private val sTaskRuntimeInfo = mutableMapOf<String, TaskRuntimeInfo>()

    //Task 比较逻辑
    val taskComparator = Comparator<Task> { lhs, rhs -> Utils.compareTask(lhs, rhs) }
    private val sTraversalVisitor = mutableSetOf<Task>()

    fun clear() {
        sDebuggable = false
        sAnchorTaskIds.clear()
        sRunBlockApplication.clear()
        sTaskRuntimeInfo.clear()
        sTraversalVisitor.clear()
    }

    fun debuggable(): Boolean {
        return sDebuggable
    }

    fun openDebug(debug: Boolean) {
        sDebuggable = debug
    }

    fun addAnchorTasks(ids: Set<String>) {
        sAnchorTaskIds.addAll(ids)
    }

    fun removeAnchorTask(id: String) {
        if (id.isNotEmpty()) {
            sAnchorTaskIds.remove(id)
        }
    }

    fun hasAnchorTasks(): Boolean {
        return sAnchorTaskIds.isNotEmpty()
    }

    val anchorTasks: Set<String>
        get() = sAnchorTaskIds

    private fun addRunTasks(task: Task) {
        if (!sRunBlockApplication.contains(task)) {
            sRunBlockApplication.add(task)
        }
    }

    fun tryRunBlockRunnable() {
        if (sRunBlockApplication.isNotEmpty()) {
            if (sRunBlockApplication.size > 1) {
                Collections.sort(sRunBlockApplication, taskComparator)
            }
            val runnable = sRunBlockApplication.removeAt(0)
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

    fun hasRunTasks(): Boolean {
        return sRunBlockApplication.isNotEmpty()
    }

    fun getTaskRuntimeInfo(taskId: String): TaskRuntimeInfo? {
        return sTaskRuntimeInfo[taskId]
    }

    fun setThreadName(task: Task, threadName: String) {
        val taskRuntimeInfo = sTaskRuntimeInfo[task.id]
        taskRuntimeInfo?.threadName = threadName
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
    fun traversalDependenciesAndInit(task: Task) {
        //获取依赖树最大深度
        val maxDepth = getDependenciesMaxDepth(task, sTraversalVisitor)
        sTraversalVisitor.clear()
        val pathTasks = arrayOfNulls<Task>(maxDepth)
        //遍历初始化运行时数据并打印log
        traversalDependenciesPath(task, pathTasks, 0)

        //如果锚点不存在，则移除。存在则提升锚点链的优先级
        val iterator = sAnchorTaskIds.iterator()
        while (iterator.hasNext()) {
            val taskId = iterator.next()
            val info = getTaskRuntimeInfo(taskId)
            if (info == null) {
                Logger.w(Constants.ANCHORS_INFO_TAG, "anchor \"$taskId\" no found !")
                iterator.remove()
            } else {
                traversalMaxTaskPriority(info.task)
            }
        }
    }

    /**
     * 递归向上设置优先级
     *
     * @param task
     */
    private fun traversalMaxTaskPriority(task: Task) {
        task.priority = Int.MAX_VALUE
        for (dependence in task.dependTasks) {
            traversalMaxTaskPriority(dependence)
        }
    }

    /**
     * 遍历依赖树
     * 1. 初始化 sTaskRuntimeInfo
     * 2. 判断锚点是否存在依赖树中
     *
     * @param task
     * @param pathTasks
     * @param pathLen
     */
    private fun traversalDependenciesPath(task: Task, pathTasks: Array<Task?>, pathLen: Int) {
        var pathLen = pathLen
        pathTasks[pathLen++] = task
        //依赖路径到尽头了
        if (task.behindTasks.isEmpty()) {
            val sb = StringBuilder()
            for (i in 0 until pathLen) {
                val pathItem = pathTasks[i]
                if (pathItem != null) {
                    val info = getTaskRuntimeInfo(pathItem.id)
                    if (info != null) {
                        //不允许框架层存在两个相同id的task
                        if (!info.isTaskInfo(pathItem)) {
                            throw RuntimeException(
                                    "Multiple different tasks are not allowed to contain the same id (" + pathItem.id + ")!")
                        }
                    } else {
                        //如果没有初始化则初始化runtimeInfo
                        val taskRuntimeInfo = TaskRuntimeInfo(pathItem)
                        if (sAnchorTaskIds.contains(pathItem.id)) {
                            taskRuntimeInfo.isAnchor = true
                        }
                        sTaskRuntimeInfo[pathItem.id] = taskRuntimeInfo
                    }
                    if (sDebuggable) {
                        sb.append((if (i == 0) "" else " --> ") + pathItem.id)
                    }
                }
            }
            if (sDebuggable) {
                Logger.d(Constants.DEPENDENCE_TAG, sb.toString())
            }
        } else {
            for (behindTask in task.behindTasks) {
                traversalDependenciesPath(behindTask, pathTasks, pathLen)
            }
        }
    }

    /**
     * 获取依赖树的最大深度
     *
     * @param task
     * @return
     */
    private fun getDependenciesMaxDepth(task: Task, sTraversalVisitor: MutableSet<Task>): Int {
        //判断依赖路径是否存在异常，不允许存在回环的依赖
        var maxDepth = 0
        if (!sTraversalVisitor.contains(task)) {
            sTraversalVisitor.add(task)
        } else {
            throw RuntimeException("Do not allow dependency graphs to have a loopback！Related task'id is " + task.id + "!")
        }
        for (behindTask in task.behindTasks) {
            val newTasks = mutableSetOf<Task>()
            newTasks.addAll(sTraversalVisitor)
            val depth = getDependenciesMaxDepth(behindTask, newTasks)
            if (depth >= maxDepth) {
                maxDepth = depth
            }
        }
        maxDepth++
        return maxDepth
    }

    internal class InnerThreadPool {
        private val CPU_COUNT = Runtime.getRuntime()
                .availableProcessors()
        private val CORE_POOL_SIZE = 4.coerceAtLeast((CPU_COUNT - 1).coerceAtMost(8))
        private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1
        private val KEEP_ALIVE_SECONDS = 30L
        private val sPoolWorkQueue = PriorityBlockingQueue<Runnable>(128)

        /**
         * [android.os.AsyncTask]
         * 相对比 Anchors, AsyncTask 更强调的是在业务处理中，异步业务不应该使得cpu饱和，但是启动场景时间比较短，可以尽可能使用更多的cpu资源。
         * 但是，anchors支持锚点阻塞ui线程，后续可能还会有延迟的异步初始化任务，所以也不要完全饱和。
         */
        private val sThreadFactory = object : ThreadFactory {
            private val mCount = AtomicInteger(1)
            override fun newThread(r: Runnable): Thread {
                return Thread(r, "Anchors Thread #" + mCount.getAndIncrement())
            }
        }
        private val asyncThreadExecutor: ExecutorService

        init {
            val threadPoolExecutor =
                ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS, sPoolWorkQueue,
                        sThreadFactory)
            threadPoolExecutor.allowCoreThreadTimeOut(true)
            asyncThreadExecutor = threadPoolExecutor
        }

        fun executeTask(runnable: Runnable) {
            asyncThreadExecutor.execute(runnable)
        }
    }
}