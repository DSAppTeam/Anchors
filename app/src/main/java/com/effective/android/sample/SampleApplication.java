package com.effective.android.sample;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;


public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        Log.d("SampleApplication","onCreate - start");
        LeakCanary.install(this);
        new TaskTest().startFromApplication();
        Log.d("SampleApplication","onCreate - end");
    }
}
