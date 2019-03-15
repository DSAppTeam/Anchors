package com.effective.android.anchors;


import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

public class AnchorsManager {

    private volatile static AnchorsManager sInstance = null;

    public static synchronized AnchorsManager getInstance() {
        if (sInstance == null) {
            synchronized (AnchorsManager.class) {
                if (sInstance == null) {
                    sInstance = new AnchorsManager();
                }
            }
        }
        return sInstance;
    }

    public AnchorsManager debuggable(boolean debuggable) {
        AnchorsRuntime.openDebug(debuggable);
        return this;
    }

    public AnchorsManager addAnchor(String taskId) {
        AnchorsRuntime.addWaitTask(taskId);
        return this;
    }

    public AnchorsManager addAnchors(String... taskIds) {
        AnchorsRuntime.addAnchorTasks(taskIds);
        return this;
    }

    @MainThread
    public synchronized void start(@NonNull Task task) {
        Utils.assertMainThread();
        if (task == null) {
            throw new RuntimeException("can no run a task that was null !");
        }
        AnchorsRuntime.traversalDependenciesAndInit(task);
        boolean logEnd = logStartWithAnchorsInfo();
        task.start();
        while (AnchorsRuntime.hasAnchorTasks()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (AnchorsRuntime.hasRunTasks()) {
                AnchorsRuntime.tryRunBlockRunnable();
            }
        }
        if (logEnd) {
            logEndWithAnchorsInfo();
        }
    }

    /**
     * 打印锚点信息
     *
     * @return
     */
    private static boolean logStartWithAnchorsInfo() {
        if (!AnchorsRuntime.debuggable()) {
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        boolean hasAnchorTask = AnchorsRuntime.hasAnchorTasks();
        if (hasAnchorTask) {
            stringBuilder.append(Constants.HAS_ANCHOR);
            stringBuilder.append("( ");
            for (String taskId : AnchorsRuntime.getAnchorTasks()) {
                stringBuilder.append("\"" + taskId + "\" ");
            }
            stringBuilder.append(")");
        } else {
            stringBuilder.append(Constants.NO_ANCHOR);
        }
        Logger.d(Constants.ANCHORS_INFO_TAG, stringBuilder.toString());
        return hasAnchorTask;
    }

    /**
     * 打印锚点信息
     */
    private static void logEndWithAnchorsInfo() {
        if (!AnchorsRuntime.debuggable()) {
            return;
        }
        Logger.d(Constants.ANCHORS_INFO_TAG, Constants.ANCHOR_RELEASE);
    }
}
