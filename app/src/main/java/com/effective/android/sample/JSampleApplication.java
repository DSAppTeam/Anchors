package com.effective.android.sample;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.effective.android.sample.data.JDatas;
import com.effective.android.sample.util.ProcessUtils;
import com.squareup.leakcanary.LeakCanary;

/**
 * java demo
 */
public class JSampleApplication extends Application {

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

    private void initDependenciesCompatMultiProcess(){
        String processName = ProcessUtils.getProcessName();
        if(!TextUtils.isEmpty(processName)){
            if(TextUtils.equals(processName,getPackageName())){
                Log.d(TAG, "SampleApplication#initDependenciesCompatMutilProcess - startFromApplicationOnMainProcess");
                new JDatas().startFromApplicationOnMainProcess();
            }else if(processName.startsWith(getPackageName())){
                Log.d(TAG, "SampleApplication#initDependenciesCompatMutilProcess - startFromApplicationOnPrivateProcess");
                new JDatas().startFromApplicationOnPrivateProcess();
            }else{
                Log.d(TAG, "SampleApplication#initDependenciesCompatMutilProcess - startFromApplicationOnPublicProcess");
                new JDatas().startFromApplicationOnPublicProcess();
            }
        }
    }
}