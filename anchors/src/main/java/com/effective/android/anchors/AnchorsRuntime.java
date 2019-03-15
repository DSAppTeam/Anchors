package com.effective.android.anchors;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
public class AnchorsRuntime {

    //调试信息
    private static boolean sDebuggable = false;

    //线程池
    private static final InnerThreadPool sPool = new InnerThreadPool();

    //设置锚点任务，当且仅当所有锚点任务都完成时, application 不在阻塞 UIThread
    private static volatile Set<String> sAnchorTasks = new HashSet<>();

    //如果存在锚点任务，则同步的任务都所有锚点任务都完成前，在 UIThread 上运行
    //ps: 后续解除锚点之后，所有UI线程上的 Task 都通过 handle 发送执行，不保证业务逻辑的同步。
    private static volatile List<Task> sRunBlockApplication = new ArrayList<>();

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    //所有 task 运行时信息
    private static final Map<String, TaskRuntimeInfo> sTaskRuntimeInfo = new HashMap<>();

    //Task 比较逻辑
    private final static Comparator<Task> sTaskComparator = new Comparator<Task>() {
        @Override
        public int compare(Task lhs, Task rhs) {
            return Utils.compareTask(lhs, rhs);
        }
    };

    private static Set<Task> sTraversalVisitor = new HashSet<>();

    public static boolean debuggable() {
        return sDebuggable;
    }

    protected static void openDebug(boolean debug) {
        AnchorsRuntime.sDebuggable = debug;
    }

    protected static Comparator<Task> getTaskComparator() {
        return sTaskComparator;
    }

    protected static void addWaitTask(String id) {
        if (!TextUtils.isEmpty(id)) {
            sAnchorTasks.add(id);
        }
    }

    protected static void addAnchorTasks(String... ids) {
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                sAnchorTasks.add(id);
            }
        }
    }

    protected static void removeAnchorTask(String id) {
        if (!TextUtils.isEmpty(id)) {
            sAnchorTasks.remove(id);
        }
    }

    protected static boolean hasAnchorTasks() {
        return !sAnchorTasks.isEmpty();
    }

    protected static Set<String> getAnchorTasks() {
        return sAnchorTasks;
    }

    protected static void addRunTasks(Task task) {
        if (task != null && !sRunBlockApplication.contains(task)) {
            sRunBlockApplication.add(task);
        }
    }

    protected static void tryRunBlockRunnable() {
        if (!sRunBlockApplication.isEmpty()) {
            if (sRunBlockApplication.size() > 1) {
                Collections.sort(sRunBlockApplication, AnchorsRuntime.getTaskComparator());
            }
            Runnable runnable = sRunBlockApplication.remove(0);
            if (hasAnchorTasks()) {
                runnable.run();
            } else {
                sHandler.post(runnable);
                for (Runnable blockItem : sRunBlockApplication) {
                    sHandler.post(blockItem);
                }
                sRunBlockApplication.clear();
            }
        }
    }

    protected static boolean hasRunTasks() {
        return !sRunBlockApplication.isEmpty();
    }

    protected static boolean hasTaskRuntimeInfo(String taskId) {
        return sTaskRuntimeInfo.get(taskId) != null;
    }

    @NonNull
    public static TaskRuntimeInfo getTaskRuntimeInfo(@NonNull String taskId) {
        return sTaskRuntimeInfo.get(taskId);
    }

    protected static void setThreadName(@NonNull Task task, String threadName) {
        TaskRuntimeInfo taskRuntimeInfo = sTaskRuntimeInfo.get(task.getId());
        if (taskRuntimeInfo != null) {
            taskRuntimeInfo.threadName = threadName;
        }
    }

    protected static void setStateInfo(@NonNull Task task) {
        TaskRuntimeInfo taskRuntimeInfo = sTaskRuntimeInfo.get(task.getId());
        if (taskRuntimeInfo != null) {
            taskRuntimeInfo.state = task.getState();
            taskRuntimeInfo.stateTime.put(task.getState(), System.currentTimeMillis());
        }
    }

    protected static void executeTask(Task task) {
        if (task.isAsyncTask()) {
            sPool.executeTask(task);
        } else {
            if (!AnchorsRuntime.hasAnchorTasks()) {
                sHandler.post(task);
            } else {
                AnchorsRuntime.addRunTasks(task);
            }
        }
    }

    /**
     * 遍历初始化依赖树
     *
     * @param task
     */
    protected static void traversalDependencies(@NonNull Task task) {
        int maxDepth = getDependenciesMaxDepth(task,sTraversalVisitor);

        sTraversalVisitor.clear();
        Task[] pathTasks = new Task[maxDepth];
        traversalDependenciesPath(task, pathTasks, 0);
        Iterator<String> iterator = sAnchorTasks.iterator();
        while (iterator.hasNext()) {
            String taskId = iterator.next();
            if (!hasTaskRuntimeInfo(taskId)) {
                Logger.w("anchor \"" + taskId + "\" no found !");
                iterator.remove();
            }
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
    private static void traversalDependenciesPath(@NonNull Task task, Task[] pathTasks, int pathLen) {
        pathTasks[pathLen++] = task;
        //依赖路径到尽头了
        if (task.getBehindTasks().isEmpty()) {

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < pathLen; i++) {
                Task pathItem = pathTasks[i];
                if (pathItem != null) {
                    if (hasTaskRuntimeInfo(pathItem.getId())) {
                        TaskRuntimeInfo taskRuntimeInfo = getTaskRuntimeInfo(pathItem.getId());
                        //不允许框架层存在两个相同id的task
                        if (taskRuntimeInfo.taskHashCode != pathItem.hashCode()) {
                            throw new RuntimeException("Multiple different tasks are not allowed to contain the same id (" + pathItem.getId() + ")!");
                        }
                    } else {
                        //如果没有初始化则初始化runtimeInfo
                        TaskRuntimeInfo taskRuntimeInfo = new TaskRuntimeInfo();
                        taskRuntimeInfo.dependencies = pathItem.getDependTaskName();
                        taskRuntimeInfo.isProject = pathItem instanceof Project;
                        taskRuntimeInfo.taskId = pathItem.getId();
                        taskRuntimeInfo.taskHashCode = pathItem.hashCode();
                        if (sAnchorTasks.contains(pathItem.getId())) {
                            taskRuntimeInfo.isAnchor = true;
                        }
                        sTaskRuntimeInfo.put(pathItem.getId(), taskRuntimeInfo);
                    }
                    if (sDebuggable) {
                        stringBuilder.append((i == 0 ? "" : " --> ") + pathItem.getId());
                    }
                }
            }
            if (sDebuggable) {
                Logger.d(Constants.DEPENDENCE_TAG, stringBuilder.toString());
            }
        } else {
            for (Task behindTask : task.getBehindTasks()) {
                traversalDependenciesPath(behindTask, pathTasks, pathLen);
            }
        }
    }

    /**
     * 获取依赖树的最大深度
     *
     * @param task
     * @return
     */
    private static int getDependenciesMaxDepth(@NonNull Task task, Set<Task> sTraversalVisitor) {
        //判断依赖路径是否存在异常，不允许存在回环的依赖
        int maxDepth = 0;
        if (!sTraversalVisitor.contains(task)) {
            sTraversalVisitor.add(task);
        } else {
            throw new RuntimeException("Do not allow dependency graphs to have a loopback！Related task'id is " + task.getId() + "!");
        }
        for (Task behindTask : task.getBehindTasks()) {
            Set<Task> newTasks = new HashSet<>();
            newTasks.addAll(sTraversalVisitor);
            int depth = getDependenciesMaxDepth(behindTask, newTasks);
            if (depth >= maxDepth) {
                maxDepth = depth;
            }
        }
        maxDepth++;
        return maxDepth;
    }

    static class InnerThreadPool {

        private ExecutorService asyncThreadExecutor;
        private final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        private final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
        private final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
        private final int KEEP_ALIVE_SECONDS = 30;

        private final ThreadFactory sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread(r, "Anchors Thread #" + mCount.getAndIncrement());
            }
        };

        private final BlockingQueue<Runnable> sPoolWorkQueue =
                new PriorityBlockingQueue<>(128);

        public InnerThreadPool() {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                    CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                    sPoolWorkQueue, sThreadFactory);
            threadPoolExecutor.allowCoreThreadTimeOut(true);
            asyncThreadExecutor = threadPoolExecutor;
        }

        void executeTask(Runnable runnable) {
            asyncThreadExecutor.execute(runnable);
        }
    }
}
