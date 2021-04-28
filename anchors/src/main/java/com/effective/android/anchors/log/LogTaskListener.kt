package com.effective.android.anchors.log

import com.effective.android.anchors.Constants
import com.effective.android.anchors.log.Logger.d
import com.effective.android.anchors.task.Task
import com.effective.android.anchors.task.listener.TaskListener
import com.effective.android.anchors.task.TaskRuntimeInfo
import com.effective.android.anchors.task.TaskState

class LogTaskListener : TaskListener {

    override fun onStart(task: Task) {
        d(task.id + Constants.START_METHOD)
    }

    override fun onRunning(task: Task) {
        d(task.id + Constants.RUNNING_METHOD)
    }

    override fun onFinish(task: Task) {
        d(task.id + Constants.FINISH_METHOD)
        logTaskRuntimeInfoString(task)
    }

    override fun onRelease(task: Task) {
        d(task.id + Constants.RELEASE_METHOD)
    }

    companion object {
        private fun logTaskRuntimeInfoString(task: Task) {
            val taskRuntimeInfo = task.anchorsRuntime.getTaskRuntimeInfo(task.id) ?: return
            val map = taskRuntimeInfo.stateTime
            val startTime = map[TaskState.START]
            val runningTime = map[TaskState.RUNNING]
            val finishedTime = map[TaskState.FINISHED]
            val builder = StringBuilder()
            builder.append(Constants.WRAPPED)
            builder.append(Constants.TASK_DETAIL_INFO_TAG)
            builder.append(Constants.WRAPPED)
            buildTaskInfoEdge(builder, taskRuntimeInfo)
            addTaskInfoLineString(builder, Constants.DEPENDENCIES, getDependenceInfo(taskRuntimeInfo), false)
            addTaskInfoLineString(builder, Constants.IS_ANCHOR, java.lang.String.valueOf(taskRuntimeInfo.isAnchor), false)
            addTaskInfoLineString(builder, Constants.THREAD_INFO, taskRuntimeInfo.threadName, false)
            addTaskInfoLineString(builder, Constants.START_TIME, startTime.toString(), false)
            addTaskInfoLineString(builder, Constants.START_UNTIL_RUNNING, (runningTime - startTime).toString(), true)
            addTaskInfoLineString(builder, Constants.RUNNING_CONSUME, (finishedTime - runningTime).toString(), true)
            addTaskInfoLineString(builder, Constants.FINISH_TIME, finishedTime.toString(), false)
            buildTaskInfoEdge(builder, null)
            builder.append(Constants.WRAPPED)
            d(Constants.TASK_DETAIL_INFO_TAG, builder.toString())
            if (taskRuntimeInfo.isAnchor) {
                d(Constants.ANCHORS_INFO_TAG, builder.toString())
            }
        }

        private fun addTaskInfoLineString(stringBuilder: StringBuilder?, key: String, time: String, addUnit: Boolean) {
            if (stringBuilder == null) {
                return
            }
            stringBuilder.append(Constants.WRAPPED)
            stringBuilder.append(String.format(Constants.LINE_STRING_FORMAT, key, time))
            if (addUnit) {
                stringBuilder.append(Constants.MS_UNIT)
            }
        }

        private fun buildTaskInfoEdge(stringBuilder: StringBuilder?, taskRuntimeInfo: TaskRuntimeInfo?) {
            if (stringBuilder == null) {
                return
            }
            stringBuilder.append(Constants.WRAPPED)
            stringBuilder.append(Constants.HALF_LINE_STRING)
            if (taskRuntimeInfo != null) {
                stringBuilder.append(if (taskRuntimeInfo.isProject) " project (" else " task (" + taskRuntimeInfo.taskId + " ) ")
            }
            stringBuilder.append(Constants.HALF_LINE_STRING)
        }

        private fun getDependenceInfo(taskRuntimeInfo: TaskRuntimeInfo): String {
            val stringBuilder = StringBuilder()
            for (s in taskRuntimeInfo.dependencies) {
                stringBuilder.append("$s ")
            }
            return stringBuilder.toString()
        }
    }
}