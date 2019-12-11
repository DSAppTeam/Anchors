package com.effective.android.anchors;

import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Set;

public class Utils {

    public static void insertAfterTask(@NonNull Task insert, @NonNull Task targetTask){
        if(insert == null  || targetTask == null){
            return;
        }
        List<Task> taskBehinds = targetTask.getBehindTasks();
        for(Task behind: taskBehinds){
            behind.removeDepend(targetTask);
            insert.behind(behind);
        }
        targetTask.getBehindTasks().clear();
        insert.dependOn(targetTask);
    }

    /**
     * 比较两个 task
     * {@link Task#getPriority()} 值高的，优先级高
     * {@link Task#getExecuteTime()} 添加到队列的时间最早，优先级越高
     *
     * @param task
     * @param o
     * @return
     */
    public static int compareTask(@NonNull Task task, @NonNull Task o) {
        if (task.getPriority() < o.getPriority()) {
            return 1;
        }
        if (task.getPriority() > o.getPriority()) {
            return -1;
        }
        if (task.getExecuteTime() < o.getExecuteTime()) {
            return -1;
        }
        if (task.getExecuteTime() > o.getExecuteTime()) {
            return 1;
        }
        return 0;
    }


    public static void assertMainThread() {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            throw new RuntimeException("AnchorsManager#start should be invoke on MainThread!");
        }
    }
}
