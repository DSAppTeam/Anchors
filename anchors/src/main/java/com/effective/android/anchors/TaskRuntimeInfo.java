package com.effective.android.anchors;

import android.util.SparseArray;

import java.util.Set;

public class TaskRuntimeInfo {

    public String taskId;
    public String threadName;
    public Set<String> dependencies;
    public SparseArray<Long> stateTime;
    @TaskState
    public int state = TaskState.IDLE;
    public boolean isAnchor;
    public boolean isProject;

    public TaskRuntimeInfo() {
        taskId = "";
        dependencies = null;
        threadName = "";
        stateTime = new SparseArray<>();
        stateTime.put(TaskState.START, -1L);
        stateTime.put(TaskState.RUNNING, -1L);
        stateTime.put(TaskState.FINISHED, -1L);
        isProject = false;
    }
}
