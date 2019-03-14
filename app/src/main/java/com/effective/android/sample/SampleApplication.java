package com.effective.android.sample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;


public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        new TaskTest().start();
    }
}
