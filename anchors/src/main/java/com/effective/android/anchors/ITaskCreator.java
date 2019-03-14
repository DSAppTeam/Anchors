package com.effective.android.anchors;

import android.support.annotation.NonNull;

public interface ITaskCreator {

    @NonNull
    Task createTask(String taskName);
}
