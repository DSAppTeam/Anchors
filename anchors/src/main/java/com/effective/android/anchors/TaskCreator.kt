package com.effective.android.anchors;

import android.support.annotation.NonNull;

public interface TaskCreator {

    @NonNull
    Task createTask(String taskName);
}
