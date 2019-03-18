package com.effective.android.anchors;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.util.HashSet;
import java.util.Set;

public class TaskRuntimeInfo {

    private SparseArray<Long> stateTime;
    private boolean isAnchor;
    private Task task;
    private String threadName;
    private static final long DEFAULT_TIME = -1l;

    public TaskRuntimeInfo(@NonNull Task task) {
        this.task = task;
        threadName = "";
        stateTime = new SparseArray<>();
        setStateTime(TaskState.START, DEFAULT_TIME);
        setStateTime(TaskState.RUNNING, DEFAULT_TIME);
        setStateTime(TaskState.FINISHED, DEFAULT_TIME);
    }

    public Task getTask() {
        return task;
    }

    /**
     * 避免task泄漏
     */
    public void clearTask() {
        task = null;
    }

    public boolean isAnchor() {
        return isAnchor;
    }

    public void setAnchor(boolean anchor) {
        isAnchor = anchor;
    }

    public boolean isProject() {
        return task instanceof Project;
    }

    public SparseArray<Long> getStateTime() {
        return stateTime;
    }

    public void setStateTime(@TaskState int state, long time) {
        stateTime.put(state, time);
    }

    public Set<String> getDependencies() {
        if (task != null) {
            return task.getDependTaskName();
        }
        return new HashSet<>();
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getThreadName() {
        return threadName;
    }

    public boolean isTaskInfo(Task task) {
        return task != null && this.task == task;
    }

    public String getTaskId() {
        return task != null ? task.getId() : "";
    }
}
