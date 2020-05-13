package com.effective.android.sample

import android.app.Application
import android.text.TextUtils
import android.util.Log
import com.effective.android.sample.data.TaskTest
import com.effective.android.sample.util.ProcessUtils
import com.squareup.leakcanary.LeakCanary

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
        Log.d(TAG, "SampleApplication#onCreate process Id is " + ProcessUtils.processId)
        Log.d(TAG, "SampleApplication#onCreate process Name is " + ProcessUtils.processName)
        Log.d(TAG, "SampleApplication#onCreate - start")
        initDependenciesCompatMultiProcess()
        Log.d(TAG, "SampleApplication#onCreate - end")
    }

    private fun initDependenciesCompatMultiProcess() {
        val processName = ProcessUtils.processName?: return

        //主进程 com.effective.android.sample
        when {
            processName  == packageName -> {
                Log.d(TAG, "SampleApplication#initDependenciesCompatMutilProcess - startFromApplicationOnMainProcess")
                TaskTest().startFromApplicationOnMainProcessByDsl()

                //私有进程 com.effective.android.sample:remote
            }
            processName.startsWith(packageName) -> {
                Log.d(TAG, "SampleApplication#initDependenciesCompatMutilProcess - startFromApplicationOnPrivateProcess")
                TaskTest().startFromApplicationOnPrivateProcess()

                //公有进程 .public
            }
            else -> {
                Log.d(TAG, "SampleApplication#initDependenciesCompatMutilProcess - startFromApplicationOnPublicProcess")
                TaskTest().startFromApplicationOnPublicProcess()
            }
        }
    }

    companion object {
        private val TAG: String = "SampleApplication"
    }
}