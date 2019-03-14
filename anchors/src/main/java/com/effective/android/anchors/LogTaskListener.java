package com.effective.android.anchors;

import android.support.annotation.NonNull;
import android.util.SparseArray;


public class LogTaskListener implements TaskListener {


    @Override
    public void onStart(Task task) {
        Logger.d(task.getId() + Constants.START_METHOD);
    }

    @Override
    public void onRunning(Task task) {
        Logger.d(task.getId() + Constants.RUNNING_METHOD);
    }

    @Override
    public void onFinish(Task task) {
        Logger.d(task.getId() + Constants.FINISH_METHOD);
        logTaskRuntimeInfoString(task);
    }

    public static void logTaskRuntimeInfoString(Task task) {
        TaskRuntimeInfo taskRuntimeInfo = AnchorsRuntime.getTaskRuntimeInfo(task.getId());
        if (taskRuntimeInfo == null) {
            return;
        }
        SparseArray<Long> map = taskRuntimeInfo.stateTime;
        Long startTime = map.get(TaskState.START);
        Long runningTime = map.get(TaskState.RUNNING);
        Long finishedTime = map.get(TaskState.FINISHED);
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.WRAPPED);
        builder.append(Constants.TASK_DETAIL_INFO_TAG);
        builder.append(Constants.WRAPPED);
        buildTaskInfoEdge(builder, taskRuntimeInfo);
        addTaskInfoLineString(builder, Constants.DEPENDENCIES, getDependenceInfo(taskRuntimeInfo), false);
        addTaskInfoLineString(builder, Constants.IS_ANCHOR, String.valueOf(taskRuntimeInfo.isAnchor), false);
        addTaskInfoLineString(builder, Constants.THREAD_INFO, taskRuntimeInfo.threadName, false);
        addTaskInfoLineString(builder, Constants.START_TIME, String.valueOf(startTime), true);
        addTaskInfoLineString(builder, Constants.START_UNTIL_RUNNING, String.valueOf(runningTime - startTime), true);
        addTaskInfoLineString(builder, Constants.RUNNING_CONSUME, String.valueOf(finishedTime - runningTime), true);
        addTaskInfoLineString(builder, Constants.FINISH_TIME, String.valueOf(finishedTime), false);
        buildTaskInfoEdge(builder, null);
        builder.append(Constants.WRAPPED);
        Logger.d(Constants.TASK_DETAIL_INFO_TAG, builder.toString());
        if(taskRuntimeInfo.isAnchor){
            Logger.d(Constants.ANCHORS_INFO_TAG, builder.toString());
        }
    }

    private static void addTaskInfoLineString(StringBuilder stringBuilder, String key, String time, boolean addUnit) {
        if (stringBuilder == null) {
            return;
        }
        stringBuilder.append(Constants.WRAPPED);
        stringBuilder.append(String.format(Constants.LINE_STRING_FORMAT, key, time));
        if (addUnit) {
            stringBuilder.append(Constants.MS_UNIT);
        }
    }

    private static void buildTaskInfoEdge(StringBuilder stringBuilder, TaskRuntimeInfo taskRuntimeInfo) {
        if (stringBuilder == null) {
            return;
        }
        stringBuilder.append(Constants.WRAPPED);
        stringBuilder.append(Constants.HALF_LINE_STRING);
        if (taskRuntimeInfo != null) {
            stringBuilder.append(taskRuntimeInfo.isProject ? " project (" : " task (" + taskRuntimeInfo.taskId + " ) ");
        }
        stringBuilder.append(Constants.HALF_LINE_STRING);
    }


    private static String getDependenceInfo(@NonNull TaskRuntimeInfo taskRuntimeInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        if (taskRuntimeInfo.dependencies != null && !taskRuntimeInfo.dependencies.isEmpty()) {
            for (String s : taskRuntimeInfo.dependencies) {
                stringBuilder.append(s + " ");
            }
        }
        return stringBuilder.toString();
    }
}
