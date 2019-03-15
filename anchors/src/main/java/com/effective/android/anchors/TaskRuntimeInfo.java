package com.effective.android.anchors;

import android.util.SparseArray;

import java.util.Set;

public class TaskRuntimeInfo {

    public int taskHashCode;            //内存标识一个唯一对象
    public String taskId;               //业务层标识一个对象
    public String threadName;
    public Set<String> dependencies;
    public SparseArray<Long> stateTime;
    @TaskState
    public int state = TaskState.IDLE;
    public boolean isAnchor;
    public boolean isProject;

    private static final long DEFAULT_TIME = -1l;

    public TaskRuntimeInfo() {
        taskId = "";
        dependencies = null;
        threadName = "";
        stateTime = new SparseArray<>();
        stateTime.put(TaskState.START, DEFAULT_TIME);
        stateTime.put(TaskState.RUNNING, DEFAULT_TIME);
        stateTime.put(TaskState.FINISHED, DEFAULT_TIME);
        isProject = false;
    }
}
