package com.effective.android.anchors

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
            val map = taskRuntimeInfo.stateTime
            val startTime = map.get(TaskState.START)
            val runningTime = map.get(TaskState.RUNNING)
            val finishedTime = map.get(TaskState.FINISHED)
            val sb: StringBuilder = StringBuilder()
            sb.append(Constants.WRAPPED)
            sb.append(Constants.TASK_DETAIL_INFO_TAG)
            sb.append(Constants.WRAPPED)
            buildTaskInfoEdge(sb, taskRuntimeInfo)
            addTaskInfoLineString(sb, Constants.DEPENDENCIES, getDependenceInfo(taskRuntimeInfo), false)
            addTaskInfoLineString(sb, Constants.IS_ANCHOR, taskRuntimeInfo.isAnchor.toString(), false)
            addTaskInfoLineString(sb, Constants.THREAD_INFO, taskRuntimeInfo.threadName, false)
            addTaskInfoLineString(sb, Constants.START_TIME, startTime.toString(), true)
            addTaskInfoLineString(sb, Constants.START_UNTIL_RUNNING, (runningTime - startTime).toString(), true)
            addTaskInfoLineString(sb, Constants.RUNNING_CONSUME, (finishedTime - runningTime).toString(), true)
            addTaskInfoLineString(sb, Constants.FINISH_TIME, finishedTime.toString(), false)
            buildTaskInfoEdge(sb, null)
            sb.append(Constants.WRAPPED)
            Logger.d(Constants.TASK_DETAIL_INFO_TAG, sb.toString())
            if (taskRuntimeInfo.isAnchor) {
                Logger.d(Constants.ANCHORS_INFO_TAG, sb.toString())
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