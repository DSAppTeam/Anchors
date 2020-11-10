package com.effective.android.anchors

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

class AnchorThreadPool {

    private var asyncThreadExecutor: ExecutorService
    private val cpuCount = Runtime.getRuntime().availableProcessors()

    /**
     * [android.os.AsyncTask]
     * 相对比 Anchors, AsyncTask 更强调的是在业务处理中，异步业务不应该使得cpu饱和，但是启动场景时间比较短，可以尽可能使用更多的cpu资源。
     * 但是，anchors支持锚点阻塞ui线程，后续可能还会有延迟的异步初始化任务，所以也不要完全饱和。
     */
    private val corePoolSize = Math.max(4, Math.min(cpuCount - 1, 8))
    private val maximumPoolSize = cpuCount * 2 + 1
    private val keepLivesSecond = 30L
    private val sThreadFactory: ThreadFactory = object : ThreadFactory {
        private val mCount = AtomicInteger(1)
        override fun newThread(r: Runnable): Thread {
            return Thread(r, "Anchors Thread #" + mCount.getAndIncrement())
        }
    }
    private val sPoolWorkQueue: BlockingQueue<Runnable> = PriorityBlockingQueue(128)

    constructor(executor: ExecutorService? = null) {
        asyncThreadExecutor = if (executor == null) {
            val threadPoolExecutor = ThreadPoolExecutor(
                    corePoolSize, maximumPoolSize, keepLivesSecond, TimeUnit.SECONDS,
                    sPoolWorkQueue, sThreadFactory)
            threadPoolExecutor.allowCoreThreadTimeOut(true)
            threadPoolExecutor
        } else {
            executor
        }
    }

    internal fun getExecutorService(): ExecutorService {
        return asyncThreadExecutor
    }
}