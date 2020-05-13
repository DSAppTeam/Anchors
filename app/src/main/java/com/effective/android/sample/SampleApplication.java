package com.effective.android.sample;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.effective.android.sample.data.TaskTest;
import com.effective.android.sample.util.ProcessUtils;
import com.squareup.leakcanary.LeakCanary;


public class SampleApplication extends Application {

    private static final String TAG = "SampleApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        Log.d(TAG, "SampleApplication#onCreate process Id is " + ProcessUtils.getProcessId());
        Log.d(TAG, "SampleApplication#onCreate process Name is " + ProcessUtils.getProcessName());

        Log.d(TAG, "SampleApplication#onCreate - start");
        initDependenciesCompatMultiProcess();
        Log.d(TAG, "SampleApplication#onCreate - end");
    }


    private void initDependenciesCompatMultiProcess() {
        String processName = ProcessUtils.getProcessName();

        //主进程 com.effective.android.sample
        if (TextUtils.equals(getPackageName(), processName)) {

            Log.d(TAG, "SampleApplication#initDependenciesCompatMutilProcess - startFromApplicationOnMainProcess");
            new TaskTest().startFromApplicationOnMainProcessByDsl();
//            new TaskTest().startFromApplicationOnMainProcess();

            //私有进程 com.effective.android.sample:remote
        } else if (processName.startsWith(getPackageName())) {

            Log.d(TAG, "SampleApplication#initDependenciesCompatMutilProcess - startFromApplicationOnPrivateProcess");
            new TaskTest().startFromApplicationOnPrivateProcess();

            //公有进程 .public
        } else {

            Log.d(TAG, "SampleApplication#initDependenciesCompatMutilProcess - startFromApplicationOnPublicProcess");
            new TaskTest().startFromApplicationOnPublicProcess();
        }
    }
}
