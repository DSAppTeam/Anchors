package com.effective.android.anchors

internal object Constants {
    // log tag
    const val TAG = "Anchors"
    const val TASK_DETAIL_INFO_TAG = "TASK_DETAIL"
    const val ANCHORS_INFO_TAG = "ANCHOR_DETAIL"
    const val DEPENDENCE_TAG = "DEPENDENCE_DETAIL"
    const val LOCK_TAG = "LOCK_DETAIL"
    //ANCHORS_INFO_TAG
    const val NO_ANCHOR = "has no any anchor！"
    const val HAS_ANCHOR = "has some anchors！"
    const val ANCHOR_RELEASE = "All anchors were released！"
    //TASK_DETAIL_INFO_TAG
    const val START_METHOD = " -- onStart -- "
    const val RUNNING_METHOD = " -- onRunning -- "
    const val FINISH_METHOD = " -- onFinish -- "
    const val RELEASE_METHOD = " -- onRelease -- "
    const val LINE_STRING_FORMAT = "| %s : %s "
    const val MS_UNIT = "ms"
    const val HALF_LINE_STRING = "======================="
    const val DEPENDENCIES = "依赖任务"
    const val THREAD_INFO = "线程信息"
    const val START_TIME = "开始时刻"
    const val START_UNTIL_RUNNING = "等待运行耗时"
    const val RUNNING_CONSUME = "运行任务耗时"
    const val FINISH_TIME = "结束时刻"
    const val IS_ANCHOR = "是否是锚点任务"
    const val WRAPPED = "\n"
}