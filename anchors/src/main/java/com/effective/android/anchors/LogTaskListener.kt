package com.effective.android.anchors

import android.util.SparseArray

class LogTaskListener : TaskListener {
    override fun onStart(task: Task) {
        Logger.d(task.id + Constants.START_METHOD)
    }

    override fun onRunning(task: Task) {
        Logger.d(task.id + Constants.RUNNING_METHOD)
    }

    override fun onFinish(task: Task) {
        Logger.d(task.id + Constants.FINISH_METHOD)
        logTaskRuntimeInfoString(task)
    }

    override fun onRelease(task: Task) {
        Logger.d(task.id + Constants.RELEASE_METHOD)
    }

    companion object {
        private fun logTaskRuntimeInfoString(task: Task) {
            val taskRuntimeInfo = AnchorsRuntime.getTaskRuntimeInfo(task.id) ?: return
            val map: SparseArray<Long> = taskRuntimeInfo.stateTime
            val startTime: Long = map.get(TaskState.START)
            val runningTime: Long = map.get(TaskState.RUNNING)
            val finishedTime: Long = map.get(TaskState.FINISHED)
            val builder: StringBuilder = StringBuilder()
            builder.append(Constants.WRAPPED)
            builder.append(Constants.TASK_DETAIL_INFO_TAG)
            builder.append(Constants.WRAPPED)
            buildTaskInfoEdge(builder, taskRuntimeInfo)
            addTaskInfoLineString(builder, Constants.DEPENDENCIES, getDependenceInfo(taskRuntimeInfo), false)
            addTaskInfoLineString(builder, Constants.IS_ANCHOR, taskRuntimeInfo.isAnchor.toString(), false)
            addTaskInfoLineString(builder, Constants.THREAD_INFO, taskRuntimeInfo.threadName, false)
            addTaskInfoLineString(builder, Constants.START_TIME, startTime.toString(), true)
            addTaskInfoLineString(builder, Constants.START_UNTIL_RUNNING, (runningTime - startTime).toString(), true)
            addTaskInfoLineString(builder, Constants.RUNNING_CONSUME, (finishedTime - runningTime).toString(), true)
            addTaskInfoLineString(builder, Constants.FINISH_TIME, finishedTime.toString(), false)
            buildTaskInfoEdge(builder, null)
            builder.append(Constants.WRAPPED)
            Logger.d(Constants.TASK_DETAIL_INFO_TAG, builder.toString())
            if (taskRuntimeInfo.isAnchor) {
                Logger.d(Constants.ANCHORS_INFO_TAG, builder.toString())
            }
        }

        private fun addTaskInfoLineString(sb: StringBuilder, key: String, time: String, addUnit: Boolean) {
            sb.append(Constants.WRAPPED)
            sb.append(String.format(Constants.LINE_STRING_FORMAT, key, time))
            if (addUnit) {
                sb.append(Constants.MS_UNIT)
            }
        }

        private fun buildTaskInfoEdge(sb: StringBuilder, taskRuntimeInfo: TaskRuntimeInfo?) {
            sb.append(Constants.WRAPPED)
            sb.append(Constants.HALF_LINE_STRING)
            if (taskRuntimeInfo != null) {
                sb.append(if (taskRuntimeInfo.isProject) " project (" else " task (" + taskRuntimeInfo.taskId + " ) ")
            }
            sb.append(Constants.HALF_LINE_STRING)
        }

        private fun getDependenceInfo(taskRuntimeInfo: TaskRuntimeInfo): String {
            val sb = StringBuilder()
            for (s in taskRuntimeInfo.dependencies) {
                sb.append(s + " ")
            }
            return sb.toString()
        }
    }
}