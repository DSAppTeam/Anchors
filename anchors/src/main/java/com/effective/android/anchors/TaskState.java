package com.effective.android.anchors;

import android.support.annotation.IntDef;

@IntDef({TaskState.IDLE, TaskState.RUNNING, TaskState.FINISHED, TaskState.START})
public @interface TaskState {
    int IDLE = 0;               //静止
    int START = 1;              //启动,可能需要等待调度，
    int RUNNING = 2;            //运行
    int FINISHED = 3;           //运行结束
}
