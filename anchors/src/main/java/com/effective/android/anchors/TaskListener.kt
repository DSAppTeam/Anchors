package com.effective.android.anchors;

public interface TaskListener {

    void onStart(Task task);

    void onRunning(Task task);

    void onFinish(Task task);

    void onRelease(Task task);
}
