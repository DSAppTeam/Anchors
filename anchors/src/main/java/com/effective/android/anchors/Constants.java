package com.effective.android.anchors;

class Constants {

    // log tag
    static final String TAG = "Anchors";
    static final String TASK_DETAIL_INFO_TAG = "TASK_DETAIL";
    static final String ANCHORS_INFO_TAG = "ANCHOR_DETAIL";
    static final String DEPENDENCE_TAG = "DEPENDENCE_DETAIL";

    //ANCHORS_INFO_TAG
    static final String NO_ANCHOR = "has no any anchor！";
    static final String HAS_ANCHOR = "has some anchors！";
    static final String ANCHOR_RELEASE = "All anchors were released！";


    //TASK_DETAIL_INFO_TAG
    static final String START_METHOD = " -- onStart -- ";
    static final String RUNNING_METHOD = " -- onRunning -- ";
    static final String FINISH_METHOD = " -- onFinish -- ";
    static final String LINE_STRING_FORMAT = "| %s : %s ";
    static final String MS_UNIT = "ms";
    static final String HALF_LINE_STRING = "=======================";
    static final String DEPENDENCIES = "依赖任务";
    static final String THREAD_INFO = "线程信息";
    static final String START_TIME = "开始时刻";
    static final String START_UNTIL_RUNNING = "等待运行耗时";
    static final String RUNNING_CONSUME = "运行任务耗时";
    static final String FINISH_TIME = "结束时刻";
    static final String IS_ANCHOR = "是否是锚点任务";

    static final String WRAPPED = "\n";
}
